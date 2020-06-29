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
    var seedValue: LiveData<String>? = null
    var decisions: LiveData<List<ScenarioDecision>>? = null

    private var adventureManager: AdventureManager? = null

    fun setupAdventure(
        context: Context,
        adventureManager: AdventureManager,
        seed: Int,
        scenarioType: Int,
        difficulty: Int,
        sessionLength: Int
    ) {
        this.adventureManager = adventureManager

        adventure = adventureManager.startNewAdventure(
            seed,
            ScenarioType.fromInt(scenarioType),
            Difficulty.fromInt(difficulty),
            sessionLength
        )

        decisions = adventureManager.decisions

        adventure?.let {
            goldCount = Transformations.map(it) { a ->
                context.getString(
                    R.string.txt_gold_count,
                    a.player.goldCount
                )
            }
            seedValue = Transformations.map(it) { a ->
                context.getString(
                    R.string.txt_seed,
                    a.seed
                )
            }
        }
    }

    fun onDecisionSelected(scenarioDecision: ScenarioDecision) {
        adventureManager?.makeDecision(scenarioDecision)
    }
}