package com.achanr.kotlinkrawler.interfaces

import com.achanr.kotlinkrawler.models.ScenarioType
import com.achanr.kotlinkrawler.models.ScenariosHolder

interface ScenariosProvider {
    fun getScenarioHolderForType(scenarioType: ScenarioType): ScenariosHolder
}