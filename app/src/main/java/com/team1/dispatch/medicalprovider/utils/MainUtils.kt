package com.team1.dispatch.medicalprovider.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import androidx.activity.result.ActivityResult
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.IS_RESTART
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Currency
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random

class MainUtils {
    companion object {
        private val TAG = "MainUtils"
        fun isNetworkError(msg: String): Boolean {
            when {
                msg.contains(Constants.UNABLE_TO_RESOLVE_HOST) -> return true
                else -> return false
            }
        }

        fun initializeSelectedLanguage(context: Context, userLanguage: String): Configuration {
            // Create a new Locale object
            val locale = Locale(userLanguage, if (userLanguage == "en") "US" else "EG")
            Locale.setDefault(locale)
            // Create a new configuration object
            val config = Configuration(context.resources.configuration)
            // Set the locale of the new configuration
//            config.locale = locale
            config.setLocale(locale)
            config.setLayoutDirection(locale)
            // Update the configuration of the App context
            context.resources.updateConfiguration(
                config,
                context.resources.displayMetrics
            )
            return config
        }

        fun encodeImage(bm: Bitmap?): String? {
            val baos = ByteArrayOutputStream()
            bm?.compress(Bitmap.CompressFormat.JPEG, 40, baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b, Base64.DEFAULT)
        }

        inline fun <reified T : Activity> restartApp(
            activity: Activity
        ) {
            val intent = Intent(activity, T::class.java).putExtra(IS_RESTART, true)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }

        fun generateRandomStringWithLength(length: Int): String {
            val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { charset[Random.nextInt(charset.length)] }
                .joinToString("")
        }


        fun String.removeCharAtIndex(index: Int): String {
            if (index < 0 || index >= this.length) {
                // Index out of bounds, return the original string
                return this
            }

            val stringBuilder = StringBuilder(this)
            stringBuilder.deleteCharAt(index)

            return stringBuilder.toString()
        }

        fun exportImagesFromIntentResult(
            context: Context,
            result: ActivityResult,
            flag: ImageFlag
        ): List<File>? {
            val imageFiles = arrayListOf<File>()

            if (result.resultCode == Activity.RESULT_OK) {
                var bitmapImage: Bitmap? = null

                when (flag) {
                    ImageFlag.CAMERA -> {
                        bitmapImage =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) result?.data?.extras?.getParcelable(
                                "data",
                                Bitmap::class.java
                            ) else result?.data?.extras?.getParcelable("data")
                        val file = File(
                            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "image${System.currentTimeMillis()}.jpg"
                        )
                        val output = FileOutputStream(file)
                        bitmapImage?.compress(Bitmap.CompressFormat.JPEG, 100, output)
                        output.close()
                        imageFiles.add(file)
                    }

                    ImageFlag.GALLERY -> {
                        if (result.data?.clipData != null) {
                            val clipData = result.data?.clipData
                            val count: Int = clipData?.itemCount ?: 0

                            for (i in 0 until count) {
                                val imageUri = clipData?.getItemAt(i)?.uri
                                val bitmapGalleryImage = MediaStore.Images.Media.getBitmap(
                                    context.contentResolver,
                                    imageUri
                                )
                                val file = File(
                                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                    "myImage${System.currentTimeMillis()}.jpg"
                                )
                                val output = FileOutputStream(file)
                                bitmapGalleryImage?.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    output
                                )
                                output.close()
                                imageFiles.add(file)
                            }
                        } else {
                            val imageUri = result.data?.data
                            val bitmapGalleryImage =
                                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
                            val file = File(
                                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                                "myImage${System.currentTimeMillis()}.jpg"
                            )
                            val output = FileOutputStream(file)
                            bitmapGalleryImage?.compress(Bitmap.CompressFormat.JPEG, 100, output)
                            output.close()
                            imageFiles.add(file)
                        }
                    }
                }
            } else {
                return null
            }

            return imageFiles
        }

        fun openDial(context: Context, phone: String) {
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.setData(Uri.parse("tel:$phone"))
            context.startActivity(callIntent)
        }

        fun isVersionGreater(lastVersion: String, currentVersion: String): Boolean {
            try {
                val parts1 = lastVersion.split(".").map { it.toInt() }
                val parts2 = currentVersion.split(".").map { it.toInt() }

                val minLength = minOf(parts1.size, parts2.size)

                for (i in 0 until minLength) {
                    if (parts1[i] > parts2[i]) {
                        return true
                    } else if (parts1[i] < parts2[i]) {
                        return false
                    }
                }

                return parts1.size > parts2.size
            } catch (e: Exception) {
                return false
            }
        }

        val Double?.formattedCurrency: String
            get() {
                val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.ENGLISH)
                format.maximumFractionDigits = 0
                format.currency = Currency.getInstance("EGP")
                return format.format(this.orDefault())
            }

        fun getKeyFromCacheImage(url: String): String {
            val startIndex = url.indexOf("com/") + "com/".length
            val endIndex = url.indexOf("?")
            return url.substring(startIndex, endIndex)
        }

        suspend fun loadHTMLImage(source: String): Drawable? {
            return try {
                val inputStream = URL(source).openStream()
                val drawable = Drawable.createFromStream(inputStream, "src")
                drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                inputStream.close()
                drawable
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun formatDateStringToTimestamp(inputDate: String): String {
            // Define the input and output date formats
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())

            // Parse the input date string
            val date = inputFormat.parse(inputDate)

            // Format the date to the desired output string
            return outputFormat.format(date)
        }

        fun getFileType(fileExtension: String): String {
            return when (fileExtension) {
                "jpeg" -> "image/jpeg"
                "jpg" -> "image/jpg"
                "png" -> "image/png"
                "mp3" -> "audio/mpeg"
                else -> "application/pdf"
            }
        }

        fun getFileFromUri(context: Context, uri: Uri): File? {
            val fileName = getFileName(context, uri)
            Log.d(TAG, "getFileFromUri:fileName : $fileName")
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputFile =
                File(context.cacheDir, fileName.orDefault("${System.currentTimeMillis()}"))
            val outputStream = FileOutputStream(outputFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            return outputFile
        }

        fun getFileName(context: Context, uri: Uri): String? {
            var fileName: String? = null
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                cursor?.use {
                    if (it.moveToFirst()) {
                        fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
            if (fileName == null) {
                fileName = uri.path
                val cut = fileName?.lastIndexOf('/')
                if (cut != null && cut != -1) {
                    fileName = fileName?.substring(cut + 1)
                }
            }
            return fileName
        }

        fun milliSecondToString(time: Int): String {
            var duration = ""
            val minutes = time / 1000 / 60
            val seconds = time / 1000 % 60
            if (seconds < 10) {
                duration = "$minutes:0$seconds"
            } else {
                duration = "$minutes:$seconds"
            }
            return duration
        }

        fun reformatDateString(dateString: String): String {
            // Define the input and output date formats
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE dd MMMM", Locale.getDefault())

            // Parse the input date string to a Date object
            val date = inputFormat.parse(dateString)

            // Format the Date object to the desired output string
            return outputFormat.format(date)
        }

        fun reformatDateToTime(inputDateString: String): String {
            // Define the input and output date formats
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure the input is parsed as UTC

            val outputFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

            return try {
                // Parse the input date string to a Date object
                val date: Date = inputFormat.parse(inputDateString)

                // Format the Date object to the desired time format
                outputFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                "" // Return an empty string in case of an error
            }
        }

        fun downloadFile(url: String, callback: (File) -> Unit) {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val file = File.createTempFile("download", ".tmp")
                        file.writeBytes(response.body?.bytes() ?: byteArrayOf())
                        callback(file)
                    } else {
                        println("Failed to download file: ${response.message}")
                    }
                }

            })
        }

        fun getDateNumberOnly(serverDate: String?): String? {
            var serverDate = serverDate
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            try {
                val dateObj = sdf.parse(serverDate)
                serverDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return serverDate
        }

        fun getDateTime(serverDate: String?): String? {
            var serverDate = serverDate
//    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC +4")
            try {
                val dateObj = sdf.parse(serverDate)
                serverDate = SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.ENGLISH).format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return serverDate
        }


        fun getTimeOnly(serverDate: String?): String? {
            var serverDate = serverDate
//    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            sdf.timeZone = TimeZone.getTimeZone("UTC +3")
            try {
                val dateObj = sdf.parse(serverDate)
                serverDate = SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(dateObj)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return serverDate
        }

    }
}