package com.team1.dispatch.medicalprovider.ui.contactParamedic

import android.app.KeyguardManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.databinding.ActivityCallParamedicBinding
import com.team1.dispatch.medicalprovider.viewModels.ContactParamedicViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration

@AndroidEntryPoint
class CallParamedicActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "VideoSupportActivity"

    companion object {

        private val LOG_TAG = CallParamedicActivity::class.java.simpleName

        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        private const val PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1
    }

//    private var supportData: SupportDataModel? = null

    private var mRtcEngine: RtcEngine? = null

    private var videoCall = false
    var carRequestId: String? = ""


    private var isAudioMuted = false
    private var isVideoMuted = false
    private var isCurrentUserRinger = false
    private var shouldPlayAgain = true

    private lateinit var binding: ActivityCallParamedicBinding
    private var localSurfaceView: SurfaceView? = null
    private var remoteSurfaceView: SurfaceView? = null

    private val viewModel: ContactParamedicViewModel by viewModels()

    private val handler = Handler(Looper.getMainLooper())

    // Declare a global variable for the Runnable
    private var runnable: Runnable? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallParamedicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*        LoadingDialog.init(this)
                if (intent.hasExtra(INCOMING_CALL_DATA)) {
                    isCurrentUserRinger = false
                    supportData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            INCOMING_CALL_DATA, SupportDataModel::class.java
                        ) ?: SupportDataModel()
                    } else {
                        intent.getParcelableExtra(INCOMING_CALL_DATA) ?: SupportDataModel()
                    }
                    videoCall = (supportData?.actionType ?: VOICE_CALL) == VIDEO_CALL
                } else {
                    isCurrentUserRinger = true
                    videoCall = (intent.getStringExtra("isVideo") ?: VOICE_CALL) == VIDEO_CALL
                    carRequestId = intent.getStringExtra("car_request_id")
                }
                checkCallPermissions()
                fetchSupportState()
         */
    }

    /*
        private fun fetchSupportState() {
            lifecycleScope.launchWhenCreated {
                viewModel.supportState.collect {
                    when (it) {
                        is UiState.Loading -> LoadingDialog.showDialog()
                        is UiState.Error -> {
                            LoadingDialog.dismissDialog()
                            Toast.makeText(
                                this@CallParamedicActivity, "${it.message}", Toast.LENGTH_SHORT
                            ).show()
                        }

                        is UiState.Success -> {
                            supportData = it.data
                            isCurrentUserRinger = true
                            startCall()
                        }

                        is UiState.TokenExpired -> {
                            LoadingDialog.dismissDialog()
                            UserUtil.clearUserInfo()
                            finish()
                        }

                        else -> Unit
                    }
                }
            }
        }
    */

    /*
        private val mRtcEventHandler = object : IRtcEngineEventHandler() {
            */
    /*
             * Occurs when a remote user (Communication)/ host (Live Broadcast) joins the channel.
             * This callback is triggered in either of the following scenarios:
             *
             * A remote user/host joins the channel by calling the joinChannel method.
             * A remote user switches the user role to the host by calling the setClientRole method after joining the channel.
             * A remote user/host rejoins the channel after a network interruption.
             * The host injects an online media stream into the channel by calling the addInjectStreamUrl method.
             *
             * @param uid User ID of the remote user sending the video streams.
             * @param elapsed Time elapsed (ms) from the local user calling the joinChannel method until this callback is triggered.
             *//*


        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            super.onJoinChannelSuccess(channel, uid, elapsed)
            Log.d(TAG, "onJoinChannelSuccess: ")
            runOnUiThread {
                if (isCurrentUserRinger) {
                    hideRemoteVideoView()
                    startRingingTimer()
                }
            }
        }

        override fun onUserJoined(uid: Int, elapsed: Int) {
            Log.d(TAG, "onUserJoined: ")
            runOnUiThread {
                if (isCurrentUserRinger) {
                    binding.tvName.text =
                        supportData?.receiverName ?: getString(R.string.dispatcher)
                    stopRinging()
                }
                if (videoCall) {
                    setupRemoteVideo(uid)
                }
            }

        }

        */
    /**
     * Occurs when a remote user (Communication)/host (Live Broadcast) leaves the channel.
     *
     * There are two reasons for users to become offline:
     *
     *     Leave the channel: When the user/host leaves the channel, the user/host sends a
     *     goodbye message. When this message is received, the SDK determines that the
     *     user/host leaves the channel.
     *
     *     Drop offline: When no data packet of the user or host is received for a certain
     *     period of time (20 seconds for the communication profile, and more for the live
     *     broadcast profile), the SDK assumes that the user/host drops offline. A poor
     *     network connection may lead to false detections, so we recommend using the
     *     Agora RTM SDK for reliable offline detection.
     *
     * @param uid ID of the user or host who leaves the channel or goes offline.
     * @param reason Reason why the user goes offline:
     *
     *     USER_OFFLINE_QUIT(0): The user left the current channel.
     *     USER_OFFLINE_DROPPED(1): The SDK timed out and the user dropped offline because no data packet was received within a certain period of time. If a user quits the call and the message is not passed to the SDK (due to an unreliable channel), the SDK assumes the user dropped offline.
     *     USER_OFFLINE_BECOME_AUDIENCE(2): (Live broadcast only.) The client role switched from the host to the audience.
     *//*
*/
    /*

            override fun onUserOffline(uid: Int, reason: Int) {
                runOnUiThread { onRemoteUserLeft() }
            }

            *//*

        */
    /**
     * Occurs when a remote user stops/resumes sending the video stream.
     *
     * @param uid ID of the remote user.
     * @param muted Whether the remote user's video stream playback pauses/resumes:
     * true: Pause.
     * false: Resume.
     *//*


        override fun onUserMuteVideo(uid: Int, muted: Boolean) {
            runOnUiThread {
                Log.d(TAG, "onUserMuteVideo: ")
                onRemoteUserVideoMuted(muted)
            }
        }

        override fun onUserMuteAudio(uid: Int, muted: Boolean) {
            runOnUiThread {
                Log.d(TAG, "onUserMuteAudio: ")
                onRemoteUserVoiceMuted(uid, muted)
            }
        }
    }
*/

    private fun stopRinging() {
        try {
            shouldPlayAgain = false
            handler?.removeCallbacksAndMessages(null)
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.stop()
                mediaPlayer?.prepare()
            }
            Log.d(TAG, "stopRinging: ")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /*
        private fun startCall() {
            binding.ivEnableAudio.setOnClickListener(this)
            binding.ivEnableCamera.setOnClickListener(this)
            binding.ivSwitchCamera.setOnClickListener(this)
            binding.ivCloseCall.setOnClickListener(this)

            lockScreen()

            if (videoCall) {
                binding.ivSwitchCamera.visibility = View.VISIBLE
                binding.ivEnableCamera.visibility = View.VISIBLE
                binding.localVideoViewContainer.visibility = View.VISIBLE
            } else {
                binding.ivSwitchCamera.visibility = View.GONE
                binding.ivEnableCamera.visibility = View.GONE
                binding.localVideoViewContainer.visibility = View.GONE
            }
            initAgoraEngineAndJoinChannel()
        }
    */

    /*
        private fun hideRemoteVideoView() {
            binding.remoteVideoViewContainer.visibility = View.GONE
            binding.ivRemoteDisabledCamera.visibility = View.VISIBLE
            binding.llRemoteUserDetails.visibility = View.VISIBLE
            binding.tvName.text = supportData?.receiverName ?: getString(R.string.dispatcher)
            Glide.with(this@CallParamedicActivity).load(R.drawable.pic_logo)
                .placeholder(R.drawable.ic_add_user).error(R.drawable.ic_add_user).into(binding.ivPic)
        }
    */

    private fun lockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            keyguardManager.requestDismissKeyguard(this, null)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }
    }

    private fun startRingingTimer() {
        // Initialize the Runnable
        binding.tvName.text = getString(R.string.ringing)
        mediaPlayer = MediaPlayer.create(this, R.raw.ring)
        mediaPlayer?.start()
        // Replace with your actual sound file
        mediaPlayer?.setOnCompletionListener {
            if (shouldPlayAgain) {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (mediaPlayer?.isPlaying == false) {
                        mediaPlayer?.start()
                    }
                }, 500) // Adjust the delay as needed (in milliseconds)
            }
        }
        runnable = Runnable {
            // Perform the action here
            // This code will be executed after 1 minute

            // Dismiss the timer
            Toast.makeText(
                this@CallParamedicActivity,
                getString(R.string.call_not_answered),
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }

        // Schedule the Runnable to run after 1 minute (60000 milliseconds)
        handler.postDelayed(runnable!!, 60000)
    }

    /*
        private fun checkCallPermissions() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_MEDIA_AUDIO
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.CAMERA
                    )
                )
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    )
                )
            } else {
                if (!isCurrentUserRinger) {
                    LoadingDialog.showDialog()
                    startCall()
                } else getCallDetails()
            }
        }
    */

    /*    private val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions.entries.all {
                    it.value == true
                }
                if (granted) {
                    if (!isCurrentUserRinger) {
                        LoadingDialog.showDialog()
                        startCall()
                    } else getCallDetails()
                }
            }*/

    /*
        private fun getCallDetails() {
            LoadingDialog.showDialog()
            viewModel.connectToSupport(
                carRequestId ?: "", if (videoCall) VIDEO_CALL else VOICE_CALL
            )
        }
    */


    /*    private fun initAgoraEngineAndJoinChannel() {
            initializeAgoraEngine()
            joinChannel()
        }*/

    private fun showLongToast(msg: String) {
        this.runOnUiThread { Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show() }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.ivEnableAudio.id -> {
                onLocalAudioMuteClicked()
            }

            binding.ivEnableCamera.id -> {
                onLocalVideoMuteClicked()
            }

            binding.ivSwitchCamera.id -> {
                onSwitchCameraClicked()
            }

            binding.ivCloseCall.id -> {
                finish()
            }
        }
    }

    private fun onLocalVideoMuteClicked() {
        // Stops/Resumes sending the local video stream.
        // show video muted icon on local screen
        if (isVideoMuted) {
            binding.ivEnableCamera.setImageResource(R.drawable.ic_enabled_camera)
            isVideoMuted = false
        } else {
            binding.ivEnableCamera.setImageResource(R.drawable.ic_disabled_camera)
            isVideoMuted = true
        }

        mRtcEngine?.muteLocalVideoStream(isVideoMuted)

    }

    private fun onLocalAudioMuteClicked() {
        // Stops/Resumes sending the local audio stream.
        // show audio muted icon on local screen
//        binding.ivEnableAudio.setImageResource(ContextCompat.getDrawable(this,R.drawable.ic_disabled_audio))
        if (isAudioMuted) {
            binding.ivEnableAudio.setImageResource(R.drawable.ic_enabled_audio)
            isAudioMuted = false
        } else {
            binding.ivEnableAudio.setImageResource(R.drawable.ic_disabled_audio)
            isAudioMuted = true
        }

        mRtcEngine?.muteLocalAudioStream(isAudioMuted)
    }

    private fun onSwitchCameraClicked() {
        // Switches between front and rear cameras.
        mRtcEngine?.switchCamera()
    }

    fun onEncCallClicked(view: View) {
        finish()
    }

    /*
        private fun initializeAgoraEngine() {
            try {
                try {
                    val config = RtcEngineConfig()
                    config.mContext = baseContext
                    config.mAppId = getString(R.string.agora_app_key)
                    config.mEventHandler = mRtcEventHandler
                    mRtcEngine = RtcEngine.create(config)
                    // By default, the video module is disabled, call enableVideo to enable it.
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                Log.e(LOG_TAG, Log.getStackTraceString(e))
                throw RuntimeException(
                    "NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(
                        e
                    )
                )
            }
        }
    */

    private fun setupVideoProfile() {
        // In simple use cases, we only need to enable video capturing
        // and rendering once at the initialization step.
        // Note: audio recording and playing is enabled by default.
//        mRtcEngine?.enableVideo()
        // https://docs.agora.io/en/Video/API%20Reference/java/classio_1_1agora_1_1rtc_1_1_rtc_engine.html#af5f4de754e2c1f493096641c5c5c1d8f
        mRtcEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {
        binding.localVideoViewContainer.visibility = View.VISIBLE
        val container = binding.localVideoViewContainer
        // Create a SurfaceView object and add it as a child to the FrameLayout.
        localSurfaceView = SurfaceView(baseContext)
        // Call setupLocalVideo with a VideoCanvas having uid set to 0.
        // Call setupLocalVideo with a VideoCanvas having uid set to 0.
        localSurfaceView?.setZOrderMediaOverlay(true)
        mRtcEngine?.setupLocalVideo(
            VideoCanvas(
                localSurfaceView, VideoCanvas.RENDER_MODE_FIT, 0
            )
        )
        container.addView(localSurfaceView)

        localSurfaceView?.visibility = View.VISIBLE
    }

    /*
        private fun joinChannel() {
            val options = ChannelMediaOptions()
            // For a Video call, set the channel profile as COMMUNICATION.
            options.channelProfile = io.agora.rtc2.Constants.CHANNEL_PROFILE_COMMUNICATION
            // Set the client role as BROADCASTER or AUDIENCE according to the scenario.
    //        options.clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_AUDIENCE
            options.clientRoleType = io.agora.rtc2.Constants.CLIENT_ROLE_BROADCASTER
            // Display LocalSurfaceView.
            if (videoCall) {
                mRtcEngine?.enableVideo()
                setupVideoProfile()
                setupLocalVideo()
            }
            // Start local preview.
            mRtcEngine?.startPreview()
            mRtcEngine?.joinChannel(
                supportData?.token?.trim(), supportData?.callId?.trim(), 0, options
            )
            LoadingDialog.dismissDialog()
        }
    */

    private fun setupRemoteVideo(uid: Int) {
        binding.remoteVideoViewContainer.visibility = View.VISIBLE
        val container = binding.remoteVideoViewContainer
        remoteSurfaceView = SurfaceView(baseContext)
        container.addView(remoteSurfaceView)
        mRtcEngine?.setupRemoteVideo(
            VideoCanvas(
                remoteSurfaceView, VideoCanvas.RENDER_MODE_FIT, uid
            )
        )
        remoteSurfaceView?.visibility = View.VISIBLE
    }

    private fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    private fun onRemoteUserLeft() {
        finish()
    }

    /*
        private fun onRemoteUserVideoMuted(muted: Boolean) {
            // show video muted icon on remote screen
            Log.d(TAG, "onRemoteUserVideoMuted: ")
            if (muted) {
                //SHow the icon indicate that the remote user has disabled the camera
                hideRemoteVideoView()
            } else {
                //Hide the icon that indicate that the remote user has disabled the camera
                binding.ivRemoteDisabledCamera.visibility = View.GONE
                //show the remote container view
                binding.remoteVideoViewContainer.visibility = View.VISIBLE
                //Hide remote user details
                binding.llRemoteUserDetails.visibility = View.GONE
            }
        }
    */

    private fun onRemoteUserVoiceMuted(uid: Int, muted: Boolean) {
        // show audio muted icon on remote screen
        if (muted) {
            binding.ivRemoteDisabledAudio.visibility = View.VISIBLE
        } else {
            binding.ivRemoteDisabledAudio.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
        try {
            if (isCurrentUserRinger) stopRinging()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
