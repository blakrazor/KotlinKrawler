package com.achanr.kotlinkrawler.providers

import android.content.Context
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.ScenarioType
import com.achanr.kotlinkrawler.models.ScenariosHolder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ScenariosProviderImpl(private val context: Context) : ScenariosProvider {

    override fun getScenarioHolderForType(scenarioType: ScenarioType): ScenariosHolder {
        val json: String = when (scenarioType) {
            ScenarioType.FirstAge -> getJsonFromAsset(ScenarioType.FirstAge.filename)
        }

        val jsonReader = Json(JsonConfiguration.Stable)
        return jsonReader.parse(ScenariosHolder.serializer(), json)
    }

    private fun getJsonFromAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}