package com.achanr.kotlinkrawler.factories

import com.achanr.kotlinkrawler.interfaces.ScenarioFactory
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.Scenario
import kotlin.random.Random

class ScenarioFactoryImpl : ScenarioFactory {
    private var allScenarios: List<Scenario> = emptyList()

    private val veryEasyScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.VeryEasy } }
    private val easyScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Easy } }
    private val mediumScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Medium } }
    private val hardScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Hard } }
    private val veryHardScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.VeryHard } }

    override fun initialize(scenarios: List<Scenario>) {
        allScenarios = scenarios
    }

    override fun createNewScenario(
        random: Random,
        difficulty: Difficulty,
        completedScenarios: List<Scenario>
    ): Scenario {
        return when (difficulty) {
            Difficulty.VeryEasy -> getProbableScenario(
                random,
                completedScenarios,
                veryEasyScenarios,
                veryEasyScenarios,
                veryEasyScenarios
            )
            Difficulty.Easy -> getProbableScenario(
                random,
                completedScenarios,
                easyScenarios,
                veryEasyScenarios,
                veryEasyScenarios
            )
            Difficulty.Medium -> getProbableScenario(
                random,
                completedScenarios,
                mediumScenarios,
                easyScenarios,
                veryEasyScenarios
            )
            Difficulty.Hard -> getProbableScenario(
                random,
                completedScenarios,
                hardScenarios,
                mediumScenarios,
                easyScenarios
            )
            Difficulty.VeryHard -> getProbableScenario(
                random,
                completedScenarios,
                veryHardScenarios,
                hardScenarios,
                mediumScenarios
            )
        }
    }

    private fun getProbableScenario(
        random: Random,
        completedScenarios: List<Scenario>,
        targetList: List<Scenario>,
        secondList: List<Scenario> = targetList,
        thirdList: List<Scenario> = targetList,
        count: Int = 0
    ): Scenario {
        val chosenScenarioList: List<Scenario> = when (random.nextInt(0, 10)) {
            in 3..9 /* 70% */ -> targetList
            1, 2 /* 20% */ -> secondList
            0 /* 10% */ -> thirdList
            else -> targetList
        }

        if (chosenScenarioList.size == 1) {
            return chosenScenarioList[0];
        }

        val chosenScenario = chosenScenarioList[random.nextInt(chosenScenarioList.size)]
        if (chosenScenario in completedScenarios && count < 2) {
            return getProbableScenario(
                random,
                completedScenarios,
                targetList,
                secondList,
                thirdList,
                count + 1
            )
        }
        return chosenScenario;
    }
}