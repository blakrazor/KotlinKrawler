package com.achanr.kotlinkrawler.interfaces

import com.achanr.kotlinkrawler.models.Scenario
import com.achanr.kotlinkrawler.models.ScenarioType

interface ScenariosProvider {
    fun getScenariosForType(scenarioType: ScenarioType): List<Scenario>
}