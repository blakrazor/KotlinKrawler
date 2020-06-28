package com.achanr.kotlinkrawler.providers

import android.content.Context
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.*
import org.json.JSONArray
import org.json.JSONObject

class ScenariosProviderImpl : ScenariosProvider {

    override fun getScenariosForType(context: Context, scenarioType: ScenarioType): List<Scenario> {
        val json: String = when (scenarioType) {
            ScenarioType.FirstAge -> getJsonFromAsset(context, ScenarioType.FirstAge.filename)
        }

        return parseScenariosFromJson(json)
    }

    private fun getJsonFromAsset(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    private fun parseScenariosFromJson(jsonString: String): List<Scenario> {
        val scenarios: MutableList<Scenario> = mutableListOf()

        val scenarioListHolder = JSONObject(jsonString);
        val scenarioArray = scenarioListHolder.getJSONArray("scenarios")
        for (i in 0 until scenarioArray.length()) {
            val scenarioObject = scenarioArray.getJSONObject(i)
            scenarios.add(parseScenarioObject(scenarioObject))
        }

        return scenarios
    }

    private fun parseScenarioObject(scenarioObject: JSONObject): Scenario {
        return Scenario(
            scenarioObject.getString("title"),
            Difficulty.fromInt(scenarioObject.getInt("difficulty")),
            scenarioObject.getString("description"),
            false,
            parseScenarioDecisionsArray(scenarioObject.getJSONArray("decisions"))
        )
    }

    private fun parseScenarioDecisionsArray(scenarioDecisionsArray: JSONArray): List<ScenarioDecision> {
        val scenarioDecisions: MutableList<ScenarioDecision> = mutableListOf()

        for (i in 0 until scenarioDecisionsArray.length()) {
            val scenarioDecisionObject = scenarioDecisionsArray.getJSONObject(i)
            val text = scenarioDecisionObject.getString("text")
            val successChance = scenarioDecisionObject.getDouble("successChance")
            val successResult =
                parseScenarioDecisionResultObject(scenarioDecisionObject.getJSONObject("successResult"))
            val failureResult = if (successChance < 1.0) parseScenarioDecisionResultObject(
                scenarioDecisionObject.getJSONObject("failureResult")
            ) else null
            scenarioDecisions.add(
                ScenarioDecision(
                    text,
                    successChance,
                    successResult,
                    failureResult
                )
            )
        }

        return scenarioDecisions
    }

    private fun parseScenarioDecisionResultObject(scenarioDecisionObject: JSONObject): ScenarioDecisionResult {
        return ScenarioDecisionResult(
            scenarioDecisionObject.getString("text"),
            scenarioDecisionObject.getInt("changeInGold")
        )
    }
}