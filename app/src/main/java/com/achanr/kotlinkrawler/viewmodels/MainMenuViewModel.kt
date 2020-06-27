package com.achanr.kotlinkrawler.viewmodels

import androidx.lifecycle.ViewModel
import com.achanr.kotlinkrawler.interfaces.MainMenuNavigator

class MainMenuViewModel(private val navigator: MainMenuNavigator) : ViewModel() {
    fun onClickNewGameButton() {
        navigator.navigateToNewGame();
    }
}