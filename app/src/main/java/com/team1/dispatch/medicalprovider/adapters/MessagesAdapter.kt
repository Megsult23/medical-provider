package com.team1.dispatch.medicalprovider.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.team1.dispatch.medicalprovider.data.models.MessageModel
import com.team1.dispatch.medicalprovider.databinding.ItemCarRequestBinding
import com.team1.dispatch.medicalprovider.ui.carRequestDetails.CarRequestDetailsActivity
import com.team1.dispatch.medicalprovider.utils.Constants.Companion.CAR_REQUEST_KEY
import com.team1.dispatch.medicalprovider.utils.MainUtils.Companion.getDateNumberOnly
import com.team1.dispatch.medicalprovider.utils.MainUtils.Companion.getTimeOnly

class MessagesAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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
            ItemCarRequestBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
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
        private val binding: ItemCarRequestBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: MessageModel) = with(binding.root) {
            binding.apply {
                tvServiceType.text = model.serviceType ?: ""
                tvReporterName.text = model.patientName ?: ""
                tvReportTime.text = getTimeOnly(model.createdAt)
                tvCaseDate.text = getDateNumberOnly(model.createdAt)
                tvAppointment.text = model.startDate
                tvCaseId.text = (model.id ?: 0).toString()
                btnDetails.setOnClickListener {
                    context.startActivity(
                        Intent(context, CarRequestDetailsActivity::class.java).putExtra(
                            CAR_REQUEST_KEY, model
                        )
                    )
                }
            }
        }
    }
}
