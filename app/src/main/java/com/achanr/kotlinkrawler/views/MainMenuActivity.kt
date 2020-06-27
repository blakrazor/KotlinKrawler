package com.achanr.kotlinkrawler.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ActivityMainMenuBinding
import com.achanr.kotlinkrawler.interfaces.MainMenuNavigator
import com.achanr.kotlinkrawler.viewmodels.MainMenuViewModel
import com.achanr.kotlinkrawler.viewmodels.NavigatorViewModelFactory

class MainMenuActivity : AppCompatActivity(), MainMenuNavigator {

    private val viewModel: MainMenuViewModel by viewModels { NavigatorViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainMenuBinding = DataBindingUtil.setContentView(this, R.layout.activity_main_menu)
        binding.viewModel = viewModel
    }

    override fun navigateToNewGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}