package com.achanr.kotlinkrawler.views

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ItemViewEventLogBinding
import com.achanr.kotlinkrawler.models.AdventureEvent
import com.achanr.kotlinkrawler.threading.AppExecutors

class EventLogAdapter(
    appExecutors: AppExecutors
) : DataBoundListAdapter<AdventureEvent>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<AdventureEvent>() {
        override fun areItemsTheSame(oldItem: AdventureEvent, newItem: AdventureEvent): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdventureEvent, newItem: AdventureEvent): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_event_log, parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: AdventureEvent) {
        when (binding) {
            is ItemViewEventLogBinding -> {
                binding.adventureEvent = item
            }
        }
    }
}
