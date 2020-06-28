package com.achanr.kotlinkrawler.interfaces

import android.content.Context
import com.achanr.kotlinkrawler.models.Scenario
import com.achanr.kotlinkrawler.models.ScenarioType

interface ScenariosProvider {
    fun getScenariosForType(context: Context, scenarioType: ScenarioType): List<Scenario>
}