package com.achanr.kotlinkrawler.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ActivityGameBinding
import com.achanr.kotlinkrawler.factories.ScenarioFactoryImpl
import com.achanr.kotlinkrawler.managers.AdventureManagerImpl
import com.achanr.kotlinkrawler.viewmodels.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityGameBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_game)
        binding.viewModel = viewModel
    }
}