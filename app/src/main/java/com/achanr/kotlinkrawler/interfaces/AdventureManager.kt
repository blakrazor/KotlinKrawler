package com.achanr.kotlinkrawler.interfaces

import android.content.Context
import androidx.lifecycle.LiveData
import com.achanr.kotlinkrawler.models.*

interface AdventureManager {
    fun startAdventure(
        context: Context,
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ) : LiveData<Adventure>

    fun makeDecision(decision: Int)
}