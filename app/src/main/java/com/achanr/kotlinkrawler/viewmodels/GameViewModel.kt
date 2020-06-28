package com.achanr.kotlinkrawler.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.interfaces.AdventureManager
import com.achanr.kotlinkrawler.models.*

class GameViewModel : ViewModel() {
    var adventure: LiveData<Adventure>? = null
    var goldCount: LiveData<String>? = null
    var decisionOne: LiveData<ScenarioDecision>? = null
    var decisionTwo: LiveData<ScenarioDecision>? = null
    var decisionThree: LiveData<ScenarioDecision>? = null

    private var adventureManager: AdventureManager? = null

    fun setupAdventure(context: Context, adventureManager: AdventureManager) {
        this.adventureManager = adventureManager

        adventure = adventureManager.startAdventure(
            context,
            12345,
            ScenarioType.FirstAge,
            Difficulty.VeryEasy,
            10
        )

        decisionOne = Transformations.map(adventure!!) { a -> getScenarioDecisionForButton(a, 1) }
        decisionTwo = Transformations.map(adventure!!) { a -> getScenarioDecisionForButton(a, 2) }
        decisionThree = Transformations.map(adventure!!) { a -> getScenarioDecisionForButton(a, 3) }
        goldCount = Transformations.map(adventure!!) { a ->
            context.getString(
                R.string.txt_gold_count,
                a.player.goldCount
            )
        }
    }

    fun onDecisionSelected(buttonDecision: Int) {
        adventureManager?.makeDecision(buttonDecision - 1)
    }

    private fun getScenarioDecisionForButton(
        adventure: Adventure,
        decision: Int
    ): ScenarioDecision? {
        if (adventure.scenario.decisions.size >= decision) {
            return adventure.scenario.decisions[decision - 1]
        }
        return null
    }
}