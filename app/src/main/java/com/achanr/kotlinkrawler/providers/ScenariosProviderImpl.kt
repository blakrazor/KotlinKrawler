package com.achanr.kotlinkrawler.providers

import android.content.Context
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.json.JSONArray
import org.json.JSONObject

class ScenariosProviderImpl(private val context: Context) : ScenariosProvider {

    override fun getScenariosForType(scenarioType: ScenarioType): List<Scenario> {
        val json: String = when (scenarioType) {
            ScenarioType.FirstAge -> getJsonFromAsset(ScenarioType.FirstAge.filename)
        }

        return parseScenariosFromJson(json)
    }

    private fun getJsonFromAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun parseScenariosFromJson(jsonString: String): List<Scenario> {
        val json = Json(JsonConfiguration.Stable)
        val scenariosHolder = json.parse(ScenariosHolder.serializer(), jsonString)
        return scenariosHolder.scenarios
    }
}