package com.achanr.kotlinkrawler.managers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.achanr.kotlinkrawler.interfaces.AdventureManager
import com.achanr.kotlinkrawler.interfaces.ScenarioFactory
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.*
import kotlin.random.Random

class AdventureManagerImpl(
    private val scenarioFactory: ScenarioFactory,
    private val scenariosProvider: ScenariosProvider
) : AdventureManager {
    private val currentAdventure: MutableLiveData<Adventure> by lazy { MutableLiveData<Adventure>() }
    private var completedScenarios: MutableList<Scenario> = mutableListOf()
    private var currentScenario: Scenario? = null
    private var currentPlayer: Player? = null
    private val currentEventLog: MutableList<AdventureEvent> = mutableListOf()
    private var seededRandom: Random? = null
    private var currentDifficulty: Difficulty? = null

    override fun startAdventure(
        context: Context,
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ): LiveData<Adventure> {
        this.seededRandom = Random(seed)
        this.currentDifficulty =  difficulty
        val scenarios: List<Scenario> =
            scenariosProvider.getScenariosForType(context, scenarioType);
        scenarioFactory.initialize(scenarios)
        currentScenario =
            scenarioFactory.createNewScenario(seededRandom!!, difficulty, completedScenarios)
        currentPlayer = Player(0)
        currentEventLog.add(AdventureEvent(currentScenario!!.description))
        postAdventureUpdate()
        return currentAdventure
    }

    override fun makeDecision(decision: Int) {
        val scenarioDecision = currentScenario!!.decisions[decision]

        val decisionResult = when (seededRandom!!.nextInt(100)) {
            in 0..((scenarioDecision.successChance * 100).toInt()) -> scenarioDecision.successResult
            else -> scenarioDecision.failureResult
        }
        currentEventLog.add(AdventureEvent(decisionResult.text))
        currentPlayer = Player(currentPlayer!!.goldCount + decisionResult.changeInGold)
        currentScenario =
            scenarioFactory.createNewScenario(seededRandom!!, currentDifficulty!!, completedScenarios)
        postAdventureUpdate()
    }

    private fun postAdventureUpdate() {
        currentAdventure.postValue(Adventure(currentPlayer!!, currentScenario!!, currentEventLog))
    }
}