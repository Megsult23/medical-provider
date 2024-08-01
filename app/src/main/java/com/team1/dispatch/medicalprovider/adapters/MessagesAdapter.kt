package com.team1.dispatch.medicalprovider.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.team1.dispatch.medicalprovider.data.models.MessageModel
import com.team1.dispatch.medicalprovider.databinding.ItemMessageBinding
import com.team1.dispatch.medicalprovider.utils.MainUtils.Companion.getDateTime
import com.team1.dispatch.medicalprovider.utils.SessionManager

class MessagesAdapter(
    private val sessionManager: SessionManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "MessagesAdapter"
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MessageModel>() {
        override fun areItemsTheSame(
            oldItem: MessageModel, newItem: MessageModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MessageModel, newItem: MessageModel
        ): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(
        CustomRecyclerChangeCallback(this), AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    inner class CustomRecyclerChangeCallback(private val adapter: MessagesAdapter) :
        ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CustomItemViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            sessionManager
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }


    fun submitList(list: List<MessageModel>) {
        differ.submitList(list)
    }


    class CustomItemViewHolder(
        private val binding: ItemMessageBinding,
        private val sessionManager: SessionManager
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MessageModel) = with(binding.root) {
            binding.apply {
                if (model.senderUnitName == sessionManager.getUserName()) {
                    inMsgTxtSender.root.visibility = View.VISIBLE
                    inMsgTxtReceiver.root.visibility = View.GONE

                    inMsgTxtSender.tvMessage.text = model.text
                    inMsgTxtSender.tvCreatedAt.text = getDateTime(model.createdAt ?: "")
                } else {
                    inMsgTxtReceiver.root.visibility = View.VISIBLE
                    inMsgTxtSender.root.visibility = View.GONE
                    inMsgTxtReceiver.tvMessage.text = model.text
                    inMsgTxtReceiver.tvCreatedAt.text = getDateTime(model.createdAt ?: "")
                }
            }
        }
    }
}
