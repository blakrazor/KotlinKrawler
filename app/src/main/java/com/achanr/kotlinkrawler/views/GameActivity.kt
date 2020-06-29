package com.achanr.kotlinkrawler.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ActivityGameBinding
import com.achanr.kotlinkrawler.factories.ScenarioFactoryImpl
import com.achanr.kotlinkrawler.managers.AdventureManagerImpl
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioType
import com.achanr.kotlinkrawler.models.SessionLength
import com.achanr.kotlinkrawler.providers.ScenariosProviderImpl
import com.achanr.kotlinkrawler.threading.AppExecutors
import com.achanr.kotlinkrawler.viewmodels.GameViewModel
import com.achanr.kotlinkrawler.views.adapters.DecisionButtonAdapter
import com.achanr.kotlinkrawler.views.adapters.EventLogAdapter

class GameActivity : AppCompatActivity() {
    companion object {
        const val seedKey: String = "seed_key"
        const val themeKey: String = "theme_key"
        const val difficultyKey: String = "difficulty_key"
        const val sessionLengthKey: String = "session_length_key"
    }

    private val viewModel: GameViewModel by viewModels()
    private lateinit var eventLogAdapter: EventLogAdapter
    private lateinit var decisionButtonAdapter: DecisionButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityGameBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupAdapters(binding)

        viewModel.setupAdventure(
            this,
            AdventureManagerImpl(this, ScenarioFactoryImpl(), ScenariosProviderImpl(this)),
            intent.getIntExtra(seedKey, 0),
            intent.getIntExtra(themeKey, ScenarioType.FirstAge.value),
            intent.getIntExtra(difficultyKey, Difficulty.VeryEasy.value),
            intent.getIntExtra(sessionLengthKey, SessionLength.Short.value)
        )

        viewModel.adventure?.observe(this, Observer { eventLogAdapter.submitList(it.eventLog) })
        viewModel.decisions?.observe(this, Observer {
            decisionButtonAdapter.submitList(it)
            binding.rvEventLog.smoothScrollToPosition(0)
        })
    }

    private fun setupAdapters(binding: ActivityGameBinding) {
        val eventLogLayoutManager = LinearLayoutManager(this)
        eventLogAdapter = EventLogAdapter(AppExecutors())
        binding.rvEventLog.adapter = eventLogAdapter
        binding.rvEventLog.layoutManager = eventLogLayoutManager
        binding.rvEventLog.setHasFixedSize(true)
        binding.rvEventLog.addItemDecoration(
            DividerItemDecoration(
                this,
                eventLogLayoutManager.orientation
            )
        )

        val decisionButtonLayoutManager = LinearLayoutManager(this)
        decisionButtonAdapter = DecisionButtonAdapter(AppExecutors(), viewModel)
        binding.rvButtons.adapter = decisionButtonAdapter
        binding.rvButtons.layoutManager = decisionButtonLayoutManager
        binding.rvButtons.setHasFixedSize(true)
    }
}