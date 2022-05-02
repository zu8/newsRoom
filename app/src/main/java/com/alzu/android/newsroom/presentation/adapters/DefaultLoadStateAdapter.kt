package com.alzu.android.newsroom.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alzu.android.newsroom.R

typealias TryAgainAction = () -> Unit

class DefaultLoadStateAdapter(
    private val tryAgainAction: TryAgainAction
) : LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        return Holder(parent, tryAgainAction)
    }

    class Holder(
        parent: ViewGroup,
        tryAgainAction: TryAgainAction
    ) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_default_load_state, parent, false)
    ) {
        val TAG = "DefaultLoadStateAdapter.Holder"
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar_load_state)
        private val errorMsg: TextView = itemView.findViewById(R.id.tv_load_state)
        private val tryAgainAction: Button = itemView.findViewById<Button?>(R.id.try_again_button)
            .also { it.setOnClickListener { tryAgainAction.invoke() } }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                Log.i(TAG, "LoadState.Error!!")
                errorMsg.text = loadState.error.localizedMessage
            }
            progressBar.visibility = toVisibility(loadState is LoadState.Loading)
            tryAgainAction.visibility = toVisibility(loadState !is LoadState.Loading)
            errorMsg.visibility = toVisibility(loadState !is LoadState.Loading)
        }

        private fun toVisibility(constraint: Boolean): Int = if (constraint) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}