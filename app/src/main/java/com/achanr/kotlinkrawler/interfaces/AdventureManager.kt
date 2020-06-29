package com.achanr.kotlinkrawler.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.achanr.kotlinkrawler.models.*

interface AdventureManager {
    val decisions: LiveData<List<ScenarioDecision>>
    val opponentHealth: LiveData<Int>

    fun startNewAdventure(
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ): LiveData<Adventure>

    fun makeDecision(scenarioDecision: ScenarioDecision)
}