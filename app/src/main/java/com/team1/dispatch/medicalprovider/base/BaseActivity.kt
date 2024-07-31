package com.team1.dispatch.medicalprovider.base

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.RequestManager
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.network.DataState
import com.team1.dispatch.medicalprovider.ui.auth.AuthActivity
import com.team1.dispatch.medicalprovider.utils.DataStateChangeListener
import com.team1.dispatch.medicalprovider.utils.Event
import com.team1.dispatch.medicalprovider.utils.ImageFlag
import com.team1.dispatch.medicalprovider.utils.MainUtils
import com.team1.dispatch.medicalprovider.utils.Response
import com.team1.dispatch.medicalprovider.utils.ResponseType
import com.team1.dispatch.medicalprovider.utils.SessionManager
import com.team1.dispatch.medicalprovider.utils.StateError
import com.team1.dispatch.medicalprovider.utils.displayErrorDialog
import com.team1.dispatch.medicalprovider.utils.displaySuccessDialog
import com.team1.dispatch.medicalprovider.utils.displayToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity(), DataStateChangeListener,
    SessionManager.LogoutCallback {
    private val TAG = "BaseActivity"

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var requestManager: RequestManager

    var imageCroppedInterface: ImageCroppedInterface? = null
    var file: File? = null
    var imageSelectionType: ImageFlag = ImageFlag.GALLERY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sessionManager.setLogoutCallback(this@BaseActivity)
        initUserSetting()

        Log.d(TAG, "onCreate: Backend Token ${sessionManager.getToken()}")
    }

    private fun initUserSetting() {
        AppCompatDelegate.setDefaultNightMode(
            sessionManager.getNightMode()
        )
        if (sessionManager.getLanguage().isEmpty()) {
            val currentLocale = resources.configuration.locale
            val language: String = currentLocale.language
            sessionManager.setLanguage(language)
        }
        MainUtils.initializeSelectedLanguage(
            this, sessionManager.getLanguage()
        )
    }

    private fun handleStateErrorEvent(errorResponse: Response) {
        if ("Unauthenticated." == errorResponse.message?.trim()) {
            displayToast(R.string.your_session_has_expired)
            sessionManager.setLogoutCallback(this@BaseActivity)
            sessionManager.logout()
        } else {
            when (errorResponse.responseType) {
                is ResponseType.Dialog -> {
                    errorResponse.message?.let { message -> displayErrorDialog(message) }
                    if (errorResponse.localizedMessage != null && errorResponse.localizedMessage != 0) displayErrorDialog(
                        errorResponse.localizedMessage
                    )
                }

                is ResponseType.Toast -> {
                    errorResponse.message?.let { message -> displayToast(message) }
                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateErrorEvent: ${errorResponse.message}")
                }
            }

        }
    }

    private fun handleStateErrorEvent(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when (it.response.responseType) {
                is ResponseType.Dialog -> {
                    it.response.message?.let { message -> displayErrorDialog(message) }
                    it.response.localizedMessage?.let { message -> displayErrorDialog(message) }
                }

                is ResponseType.Toast -> {
                    it.response.message?.let { message -> displayToast(message) }
                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateErrorEvent: ${it.response.message}")
                }


            }

        }
    }

    private fun handleStateResponse(event: Event<Response>) {
        event.getContentIfNotHandled()?.let {
            when (it.responseType) {
                is ResponseType.Dialog -> {
                    it.message?.let { message -> displaySuccessDialog(message) }
                    it.localizedMessage?.let { message -> displaySuccessDialog(message) }
                }

                is ResponseType.Toast -> {
                    it.message?.let { message -> displayToast(message) }
                }

                is ResponseType.None -> {
                    Log.d(TAG, "handleStateErrorEvent: ${it.message}")
                }
            }

        }
    }

    /*
        private val requestImagePickUpPermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all {
                    it.value
                }
                if (granted) {
                    if (imageSelectionType == ImageFlag.CAMERA) chooseUserPhotoFromCamera()
                    else chooseUserPhotoFromGallery()
                }
            }

        fun chooseUserPhotoFromGallery() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                try {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestImagePickUpPermissions.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                    } else {
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        cropImageActivityLauncher.launch(pickPhoto)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                try {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestImagePickUpPermissions.launch(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                    } else {
                        val pickPhoto =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        cropImageActivityLauncher.launch(pickPhoto)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }

        private val cropImageActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                run {
                    if (it.resultCode == RESULT_OK && it.data != null) {
                        val data: Intent = it.data!!
                        cropImage(data.data!!)
                    }
                }
            }

        private val croppedImageActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                run {
                    if (it.resultCode == RESULT_OK && it.data != null) {
                        val resultUri: Uri? = UCrop.getOutput(it.data!!);
                        file = resultUri?.path?.let { it1 -> File(it1) };
                        val bm = BitmapFactory.decodeFile(resultUri!!.path)
                        val baos = ByteArrayOutputStream()
                        bm.compress(Bitmap.CompressFormat.JPEG, 30, baos)
    //                    requestManager.load(bm)
                        imageCroppedInterface?.onImagePicked(file, bm)
                    }
                }
            }

        fun chooseUserPhotoFromCamera() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            {
                try {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_MEDIA_IMAGES
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestImagePickUpPermissions.launch(
                            arrayOf(
                                Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
                            )
                        )
                    } else {
                        val imageDirectory: File? = externalCacheDir
                        if (imageDirectory != null) startCameraIntent(imageDirectory.absolutePath)
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            else
            {
                try {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestImagePickUpPermissions.launch(
                            arrayOf(
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                    } else {
                        val imageDirectory: File? = externalCacheDir
                        if (imageDirectory != null) startCameraIntent(imageDirectory.getAbsolutePath())
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
        }

        private fun startCameraIntent(imageDirectory: String) {
            file = createImageFile(imageDirectory)
            cameraActivityLauncher.launch(getCameraIntent())
            Log.d(TAG, "startCameraIntent: ")
        }

        private val cameraActivityLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                run {
                    if (it.resultCode == RESULT_OK) {
                        cropImage(Uri.fromFile(file))
                        revokeUriPermission(
                            FileProvider.getUriForFile(
                                this, BuildConfig.APPLICATION_ID + ".provider", file!!
                            ), Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        )
                        revokeUriPermission(
                            FileProvider.getUriForFile(
                                this, BuildConfig.APPLICATION_ID + ".provider", file!!
                            ), Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
            }

       private fun getCameraIntent(): Intent {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            // Put the uri of the image file as intent extra
            val imageUri: Uri = FileProvider.getUriForFile(
                this, BuildConfig.APPLICATION_ID + ".provider", file!!
            )
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            // Get a list of all the camera apps
            val resolvedIntentActivities: List<ResolveInfo> = getPackageManager().queryIntentActivities(
                cameraIntent, PackageManager.MATCH_DEFAULT_ONLY
            )

            // Grant uri read/write permissions to the camera apps
            for (resolvedIntentInfo in resolvedIntentActivities) {
                val packageName = resolvedIntentInfo.activityInfo.packageName
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            return cameraIntent
        }

        private fun createImageFile(directory: String): File? {
            var imageFile: File? = null
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
                val storageDir = File(directory)
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        return null
                    }
                }
                val imageFileName: String =
                    getString(R.string.app_name) + System.currentTimeMillis() + "_"
                try {
                    imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return imageFile
        }

        private fun cropImage(photoUri: Uri) {
            val destinationFileName: String =
                getString(R.string.app_name) + System.currentTimeMillis() + ".jpg"
            val destinationUri: Uri = Uri.fromFile(File(cacheDir, destinationFileName))
            val options = UCrop.Options()
            options.setCompressionQuality(50)
            options.setCircleDimmedLayer(true)
            options.setShowCropFrame(false)
            options.setShowCropGrid(false)
    //        options.setToolbarTitle(getString(R.string.crop_image_text))
    //        options.setStatusBarColor(ContextCompat.getColor(this, R.color.heliotrope))
    //        options.setToolbarColor(ContextCompat.getColor(this, R.color.heliotrope))
            croppedImageActivityLauncher.launch(
                UCrop.of(photoUri, destinationUri).withOptions(options).withAspectRatio(1f, 1f)
                    .getIntent(this)
            )
            //            .start(this)


        }*/

    override fun onDataStateChange(dataState: DataState<*>?) {
        Log.d(TAG, "onDataStateChange: ")
        dataState?.let {
            GlobalScope.launch(Dispatchers.Main) {
                showProgressBar(it.loading.isLoading)
                it.error?.let { errorEvent ->
                    Log.d(TAG, "onDataStateChange: Error")
                    handleStateErrorEvent(errorEvent)
                }
                it.data?.let {
                    it.response?.let { responseEvent ->
                        Log.d(TAG, "onDataStateChange: Success")
                        handleStateResponse(responseEvent)
                    }
                }
            }
        }
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    override fun showSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(currentFocus, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onErrorStateChange(stateError: StateError) {
        GlobalScope.launch(Dispatchers.Main) {
            handleStateErrorEvent(stateError.response)
        }
    }

    override fun onLogoutComplete() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    abstract fun showProgressBar(show: Boolean)

    abstract fun showErrorUI(
        show: Boolean,
        image: Int? = 0,
        title: String? = "",
        desc: String? = null,
        isAuthenticated: Boolean? = true,
        showButton: Boolean? = false
    )

    interface ImageCroppedInterface {
        fun onImagePicked(uri: File?, bm: Bitmap)
    }
}