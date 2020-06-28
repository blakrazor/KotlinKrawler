package com.achanr.kotlinkrawler.factories

import com.achanr.kotlinkrawler.models.Scenario
import com.achanr.kotlinkrawler.interfaces.ScenarioFactory
import com.achanr.kotlinkrawler.models.Difficulty
import kotlin.random.Random

class ScenarioFactoryImpl(private val allScenarios: List<Scenario>) :
    ScenarioFactory {
    private val veryEasyScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.VeryEasy } }
    private val easyScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Easy } }
    private val mediumScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Medium } }
    private val hardScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.Hard } }
    private val veryHardScenarios: List<Scenario> by lazy { allScenarios.filter { it.difficulty == Difficulty.VeryHard } }

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
        val chosenScenarioList: List<Scenario> = when (random.nextInt(1, 10)) {
            in 4..10 /* 70% */ -> targetList
            2, 3 /* 20% */ -> secondList
            1 /* 10% */ -> thirdList
            else -> targetList
        }

        val chosenScenario = chosenScenarioList[random.nextInt(chosenScenarioList.size) - 1]
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