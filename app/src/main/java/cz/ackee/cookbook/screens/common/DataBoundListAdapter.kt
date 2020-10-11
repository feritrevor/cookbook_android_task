package cz.ackee.cookbook.screens.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import cz.ackee.cookbook.util.AppExecutors

abstract class DataBoundListAdapter<T, V : ViewDataBinding>(
        //var items: MutableList<T>,
        appExecutors: AppExecutors,
        diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, DataBoundHolder<V>>(
        AsyncDifferConfig.Builder<T>(diffCallback)
                .setBackgroundThreadExecutor(appExecutors.diskIO())
                .build()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundHolder<V> =
            DataBoundHolder(createBinding(parent))

    //override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: DataBoundHolder<V>, position: Int) {
        //bind(holder.binding, items[position])
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    protected abstract fun bind(binding: V, item: T)
}