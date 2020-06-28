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
    private val currentDecisions: MutableLiveData<List<ScenarioDecision>> by lazy { MutableLiveData<List<ScenarioDecision>>() }
    private val completedScenarios: MutableList<Scenario> = mutableListOf()
    private val currentEventLog: MutableList<AdventureEvent> = mutableListOf()
    private val continueDecisionResult: ScenarioDecision =
        ScenarioDecision("Continue", 1.0, null, null)

    private var currentScenario: Scenario? = null
    private var currentPlayer: Player? = null
    private var currentDifficulty: Difficulty? = null
    private var seededRandom: Random? = null

    override val decisions: LiveData<List<ScenarioDecision>> = currentDecisions

    override fun startAdventure(
        context: Context,
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ): LiveData<Adventure> {
        this.seededRandom = Random(seed)
        this.currentDifficulty = difficulty
        currentPlayer = Player(0)

        val scenarios: List<Scenario> =
            scenariosProvider.getScenariosForType(context, scenarioType);
        scenarioFactory.initialize(scenarios)

        continueAdventure(seededRandom, currentPlayer, currentDifficulty)
        return currentAdventure
    }

    override fun makeDecision(scenarioDecision: ScenarioDecision) {
        makeDecisionImpl(
            scenarioDecision,
            seededRandom,
            currentPlayer,
            currentDifficulty,
            currentScenario,
            currentDecisions.value
        )
    }

    private fun continueAdventure(
        random: Random?,
        player: Player?,
        difficulty: Difficulty?
    ) {
        if (random != null && difficulty != null) {
            val newScenario =
                scenarioFactory.createNewScenario(
                    random,
                    difficulty,
                    completedScenarios
                )
            currentEventLog.add(0, AdventureEvent(newScenario.description))
            postAdventureUpdate(player, newScenario, newScenario.decisions.toList())
        }
    }

    private fun makeDecisionImpl(
        scenarioDecision: ScenarioDecision,
        random: Random?,
        player: Player?,
        difficulty: Difficulty?,
        scenario: Scenario?,
        decisions: List<ScenarioDecision>?
    ) {
        if (random != null && scenario != null && player != null && decisions != null) {
            if (scenarioDecision == continueDecisionResult) {
                continueAdventure(random, player, difficulty)
                return
            }

            val decisionResult = when (random.nextInt(100)) {
                in 0..((scenarioDecision.successChance * 100).toInt()) -> scenarioDecision.successResult
                else -> scenarioDecision.failureResult
            }

            var updatedPlayer = player
            decisionResult?.let {
                currentEventLog.add(0, AdventureEvent(it.text))
                updatedPlayer = Player(player.goldCount + it.changeInGold)
            }
            completedScenarios.add(scenario)
            postAdventureUpdate(updatedPlayer, scenario, listOf(continueDecisionResult))
        }
    }

    private fun postAdventureUpdate(
        player: Player?,
        scenario: Scenario?,
        decisions: List<ScenarioDecision>
    ) {
        if (player != null && scenario != null) {
            currentPlayer = player
            currentScenario = scenario

            currentDecisions.postValue(decisions)
            currentAdventure.postValue(
                Adventure(
                    player,
                    scenario,
                    decisions,
                    currentEventLog
                )
            )
        }
    }
}