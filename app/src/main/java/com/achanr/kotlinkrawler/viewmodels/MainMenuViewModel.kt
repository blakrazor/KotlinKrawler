package com.achanr.kotlinkrawler.viewmodels

import androidx.lifecycle.ViewModel
import com.achanr.kotlinkrawler.interfaces.MainMenuNavigator
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioType
import com.achanr.kotlinkrawler.models.SessionLength

class MainMenuViewModel(private val navigator: MainMenuNavigator) : ViewModel() {
    val themes: List<ScenarioType> = ScenarioType.values().toList()
    val difficulties: List<Difficulty> = Difficulty.values().toList()
    val sessionLengths: List<SessionLength> = SessionLength.values().toList()

    fun onClickNewGameButton() {
        navigator.navigateToNewGame();
    }
}