package com.achanr.kotlinkrawler.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.viewmodels.GameViewModel

class GameActivity : AppCompatActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
    }
}