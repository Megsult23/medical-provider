package com.team1.dispatch.medicalprovider.adapters

/*
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.team1.dispatch.medicalprovider.utils.Constants
import com.team1.dispatch.medicalprovider.utils.MainUtils
import java.util.Locale

class CarRequestsAdapter(
    private val requestManager: RequestManager,
    private val callback: ArticleItemCallback
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "CarRequestsAdapter"
    private val DIFF_CALLBACK =
        object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(
                oldItem: ArticleModel,
                newItem: ArticleModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ArticleModel,
                newItem: ArticleModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    private val differ = AsyncListDiffer(
        CustomRecyclerChangeCallback(this),
        AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
    )

    inner class CustomRecyclerChangeCallback(private val adapter: CarRequestsAdapter) :
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
        return when (viewType) {
            LOADING_TYPE -> {
                LoadingItemViewHolder(
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            ERROR_TYPE -> {
                RetryViewHolder(
                    ItemRetryVideoBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    callback
                )
            }

            else -> {
                CustomItemViewHolder(
                    ItemHomeArticleBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    requestManager,
                    callback
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CustomItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }

            is RetryViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            differ.currentList[position].isLoading -> LOADING_TYPE
            differ.currentList[position].isError -> ERROR_TYPE
            else -> ITEM_TYPE
        }
    }

    fun submitList(list: List<ArticleModel>) {
        differ.submitList(list)
    }


    class CustomItemViewHolder(
        private val binding: ItemHomeArticleBinding,
        private val requestManager: RequestManager,
        private val callback: ArticleItemCallback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleModel) = with(binding.root) {
            binding.apply {
                requestManager.load(item.image).into(ivArticleImage)
                tvArticleTitle.text = item.title
                tvPostTime.text =
                    MainUtils.getTimeAgo(context, item.createdAt ?: "", Locale.getDefault())
                root.setOnClickListener {
                    callback.onArticleItemClicked(absoluteAdapterPosition)
                }
            }
        }
    }

    class LoadingItemViewHolder(binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    class RetryViewHolder(
        private val binding: ItemRetryVideoBinding,
        private val callback: ArticleItemCallback
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticleModel) = with(binding.root) {
            binding.apply {
                tvError.text = item.title

                root.setOnClickListener {
                    callback.onArticleItemClicked(absoluteAdapterPosition, flag = Constants.RETRY)
                }
            }
        }
    }

    interface ArticleItemCallback {
        fun onArticleItemClicked(position: Int, flag: String = "")
    }

    companion object {
        const val ITEM_TYPE = 0
        const val LOADING_TYPE = 1
        const val ERROR_TYPE = 2
    }
}*/
