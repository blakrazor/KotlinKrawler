package com.achanr.kotlinkrawler.interfaces

import com.achanr.kotlinkrawler.models.Battle
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.Scenario
import com.achanr.kotlinkrawler.models.ScenariosHolder
import kotlin.random.Random

interface ScenarioFactory {
    fun initialize(scenariosHolder: ScenariosHolder)

    fun createNewScenario(
        random: Random,
        difficulty: Difficulty,
        completedScenarios: List<Scenario>
    ): Scenario

    fun getBattleForId(id: Int): Battle
}