package com.achanr.kotlinkrawler.interfaces

import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioType

interface AdventureManager {
    fun startAdventure(
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    )
}