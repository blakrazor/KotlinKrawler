package com.achanr.kotlinkrawler.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ItemViewDecisionButtonBinding
import com.achanr.kotlinkrawler.models.ScenarioDecision
import com.achanr.kotlinkrawler.threading.AppExecutors
import com.achanr.kotlinkrawler.viewmodels.GameViewModel

class DecisionButtonAdapter(
    appExecutors: AppExecutors,
    private val gameViewModel: GameViewModel
) : DataBoundListAdapter<ScenarioDecision>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<ScenarioDecision>() {
        override fun areItemsTheSame(
            oldItem: ScenarioDecision,
            newItem: ScenarioDecision
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ScenarioDecision,
            newItem: ScenarioDecision
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_decision_button,
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: ScenarioDecision) {
        when (binding) {
            is ItemViewDecisionButtonBinding -> {
                binding.scenarioDecision = item
                binding.viewModel = gameViewModel
            }
        }
    }

    override fun submitList(list: List<ScenarioDecision>?) {
        super.submitList(list?.let { ArrayList(it) })
    }
}