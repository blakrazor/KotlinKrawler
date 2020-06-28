package com.achanr.kotlinkrawler.managers

import com.achanr.kotlinkrawler.interfaces.AdventureManager
import com.achanr.kotlinkrawler.interfaces.ScenarioFactory
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioType
import kotlin.random.Random

class AdventureManagerImpl(
    private val scenarioFactory: ScenarioFactory,
    private val scenariosProvider: ScenariosProvider
) : AdventureManager {
    private var seededRandom: Random = Random(0)
    private var difficulty: Difficulty = Difficulty.VeryEasy
    private var sessionLength: Int = 0

    override fun startAdventure(
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ) {
        seededRandom = Random(seed);
        this.difficulty = difficulty;
        this.sessionLength = sessionLength;


    }
}