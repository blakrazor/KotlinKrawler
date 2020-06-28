package com.achanr.kotlinkrawler.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ActivityGameBinding
import com.achanr.kotlinkrawler.factories.ScenarioFactoryImpl
import com.achanr.kotlinkrawler.managers.AdventureManagerImpl
import com.achanr.kotlinkrawler.providers.ScenariosProviderImpl
import com.achanr.kotlinkrawler.threading.AppExecutors
import com.achanr.kotlinkrawler.viewmodels.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()
    private lateinit var adapter: EventLogAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityGameBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        adapter = EventLogAdapter(AppExecutors())
        binding.rvEventLog.adapter = adapter
        binding.rvEventLog.layoutManager = LinearLayoutManager(this)
        binding.rvEventLog.setHasFixedSize(true)
        viewModel.setupAdventure(this, AdventureManagerImpl(ScenarioFactoryImpl(), ScenariosProviderImpl()))
        viewModel.adventure!!.observe(this, Observer { a -> adapter.submitList(a.eventLog) })
    }
}