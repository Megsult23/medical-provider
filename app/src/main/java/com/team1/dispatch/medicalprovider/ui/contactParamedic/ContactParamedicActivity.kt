package com.team1.dispatch.medicalprovider.ui.contactParamedic

//uncomment this when turning on agora

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.dispatch.medicalprovider.R
import com.team1.dispatch.medicalprovider.adapters.MessagesAdapter
import com.team1.dispatch.medicalprovider.base.BaseActivity
import com.team1.dispatch.medicalprovider.data.models.MessageModel
import com.team1.dispatch.medicalprovider.databinding.ActivityContactParamedicBinding
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.VIDEO_CALL
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.VOICE_CALL
import com.team1.dispatch.medicalprovider.utils.setUpToolBar
import com.team1.dispatch.medicalprovider.viewModels.ContactParamedicViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ContactParamedicActivity : BaseActivity() {
    companion object {
        private val TAG = "ContactParamedicActivity"
    }

    private val viewModel: ContactParamedicViewModel by viewModels()

    private lateinit var binding: ActivityContactParamedicBinding

    //    private var supportData: SupportDataModel? = null
    private var carRequestId: String? = ""

    private var isJoinedRoom = false

    private var selection = VOICE_CALL

    @Inject
    lateinit var messageAdapter: MessagesAdapter

    private val messagesList = ArrayList<MessageModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactParamedicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
//        fetchMessages()
//        joinChatRoom()
    }


    private fun initViews() {
        setUpToolBar(
            toolbarBinding = binding.mainToolbar,
            isHome = false,
            title = getString(R.string.paramedic)
        )

        carRequestId = intent.getStringExtra("car_request_id")
        binding.apply {
            rvMessages.adapter = messageAdapter
            (rvMessages.layoutManager as LinearLayoutManager).reverseLayout = true
            ivSendAction.setOnClickListener {
//                if (etMessage.text.isNotEmpty()) sendMessage()
            }
            etMessage.doOnTextChanged { text, start, before, count ->
                ivSendAction.isEnabled = !text.isNullOrBlank()
            }
        }
    }

    /*    private fun fetchMessages() {
            viewModel.fetchMessages() {
                runOnUiThread {
                    it.error?.getContentIfNotHandled()?.let { Log.d(TAG, "fetchMessages: $it") }

                    it.data?.data?.getContentIfNotHandled()?.let {
                        messagesList.clear()
                        messagesList.addAll(it)
                        messagesList.reverse()
                        messageAdapter.submitList(messagesList)
                        messageAdapter.notifyDataSetChanged()
                        binding.rvMessages.scrollToPosition(0)
                    }
                }
            }
        }

    private fun joinChatRoom() {
        viewModel.joinChatRoom(UserUtil.getUserUnitID().toString(), carRequestId ?: "") {
            runOnUiThread {
                it.error?.getContentIfNotHandled()?.let { Log.d(TAG, "joinChatRoom: $it") }

                it.data?.data?.getContentIfNotHandled()?.let {
                    isJoinedRoom = true
                    subscribeOnMessageReceived()
                }
            }
        }
        subscribeOnMessageReceived()
    }

    private fun subscribeOnMessageReceived() {
        viewModel.onMessageReceived {
            runOnUiThread {
                it.error?.getContentIfNotHandled()?.let { stateError ->
                    Log.d(
                        TAG, "listenMessageReceive: ${stateError}"
                    )
                }

                it.data?.data?.getContentIfNotHandled()?.let { message ->
                    if (message.senderUnitName == UserUtil.getUserUnitName()) binding.etMessage.text.clear()

                    messagesList.add(0, message)
                    messageAdapter.submitList(messagesList)
                    messageAdapter.notifyDataSetChanged()
                    binding.rvMessages.scrollToPosition(0)
                }
            }
        }
    }

    private fun sendMessage() {
        viewModel.sendMessage(
            UserUtil.getUserUnitID().toString(), carRequestId ?: "",
            textMessage = binding.etMessage.text.toString().trim().ifEmpty { null },
        )
        {
            runOnUiThread {
                it.error?.getContentIfNotHandled()
                    ?.let { stateError -> Log.d(TAG, "sendMessage: $stateError") }

                it.data?.data?.getContentIfNotHandled()?.let {}
            }
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.call_paramedic_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            R.id.action_voice -> {
                hideSoftKeyboard()
                selection = VOICE_CALL
                openCallActivityUponSelection()
                true
            }

            R.id.action_video -> {
                hideSoftKeyboard()
                selection = VIDEO_CALL
                openCallActivityUponSelection()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openCallActivityUponSelection() {
        /*        startActivity(
                    Intent(this@ContactParamedicActivity, LiveCallActivity::class.java).putExtra(
                        "isVideo",
                        selection
                    ).putExtra("car_request_id", carRequestId)
                )*/
        startActivity(
            Intent(this@ContactParamedicActivity, CallParamedicActivity::class.java).putExtra(
                "isVideo", selection
            ).putExtra("car_request_id", carRequestId)
        )
    }

    override fun onBackPressed() {
        finish()
    }

    override fun showProgressBar(show: Boolean) {
        TODO("Not yet implemented")
    }

    override fun showErrorUI(
        show: Boolean,
        image: Int?,
        title: String?,
        desc: String?,
        isAuthenticated: Boolean?,
        showButton: Boolean?
    ) {
        TODO("Not yet implemented")
    }
}
