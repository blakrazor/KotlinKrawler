package com.achanr.kotlinkrawler.interfaces

import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.Scenario
import kotlin.random.Random

interface ScenarioFactory {
    fun initialize(scenarios: List<Scenario>)

    fun createNewScenario(
        random: Random,
        difficulty: Difficulty,
        completedScenarios: List<Scenario>
    ): Scenario
}