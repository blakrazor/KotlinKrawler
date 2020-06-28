package com.achanr.kotlinkrawler.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.interfaces.AdventureManager
import com.achanr.kotlinkrawler.models.Adventure
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioDecision
import com.achanr.kotlinkrawler.models.ScenarioType

class GameViewModel : ViewModel() {
    var adventure: LiveData<Adventure>? = null
    var goldCount: LiveData<String>? = null
    var decisions: LiveData<List<ScenarioDecision>>? = null

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

        decisions = adventureManager.decisions

        adventure?.let {
            goldCount = Transformations.map(it) { a ->
                context.getString(
                    R.string.txt_gold_count,
                    a.player.goldCount
                )
            }
        }
    }

    fun onDecisionSelected(scenarioDecision: ScenarioDecision) {
        adventureManager?.makeDecision(scenarioDecision)
    }
}