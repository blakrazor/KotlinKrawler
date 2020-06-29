package com.achanr.kotlinkrawler.managers

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.interfaces.AdventureManager
import com.achanr.kotlinkrawler.interfaces.ScenarioFactory
import com.achanr.kotlinkrawler.interfaces.ScenariosProvider
import com.achanr.kotlinkrawler.models.*
import kotlin.random.Random

class AdventureManagerImpl(
    private val context: Context,
    private val scenarioFactory: ScenarioFactory,
    private val scenariosProvider: ScenariosProvider
) : AdventureManager {
    private val currentAdventure: MutableLiveData<Adventure> by lazy { MutableLiveData<Adventure>() }
    private val currentDecisions: MutableLiveData<List<ScenarioDecision>> by lazy { MutableLiveData<List<ScenarioDecision>>() }
    private val completedScenarios: MutableList<Scenario> = mutableListOf()
    private val currentEventLog: MutableList<AdventureEvent> = mutableListOf()
    private val currentOpponentHealth: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    private val continueDecisionResult: ScenarioDecision =
        ScenarioDecision(context.getString(R.string.btn_continue), 1.0, null, null)
    private val fightDecisionResult: ScenarioDecision =
        ScenarioDecision(context.getString(R.string.btn_fight))
    private val attackDecisionResult: ScenarioDecision =
        ScenarioDecision(context.getString(R.string.btn_attack))
    private val defendDecisionResult: ScenarioDecision =
        ScenarioDecision(context.getString(R.string.btn_defend))
    private val gameOverDecisionResult: ScenarioDecision =
        ScenarioDecision(context.getString(R.string.btn_start_over))

    private var currentScenario: Scenario? = null
    private var currentPlayer: Player? = null
    private var currentDifficulty: Difficulty? = null
    private var seededRandom: Random? = null
    private var currentSeed: Int? = null
    private var currentScenarioType: ScenarioType? = null
    private var currentSessionLength: Int? = null
    private var currentBattle: Battle? = null

    override val decisions: LiveData<List<ScenarioDecision>> = currentDecisions
    override val opponentHealth: LiveData<Int> = currentOpponentHealth

    override fun startNewAdventure(
        seed: Int,
        scenarioType: ScenarioType,
        difficulty: Difficulty,
        sessionLength: Int
    ): LiveData<Adventure> {
        this.currentSeed = seed
        this.currentScenarioType = scenarioType
        this.seededRandom = Random(seed)
        this.currentDifficulty = difficulty
        this.currentSessionLength = sessionLength
        this.currentEventLog.clear()
        this.completedScenarios.clear()
        currentPlayer = Player.newPlayer(difficulty)

        val scenarioHolder: ScenariosHolder =
            scenariosProvider.getScenarioHolderForType(scenarioType);
        scenarioFactory.initialize(scenarioHolder)

        continueAdventure(seededRandom, currentSeed, currentPlayer, currentDifficulty)
        return currentAdventure
    }

    override fun makeDecision(scenarioDecision: ScenarioDecision) {
        makeDecisionImpl(
            scenarioDecision,
            seededRandom,
            currentSeed,
            currentPlayer,
            currentDifficulty,
            currentScenario,
            currentDecisions.value
        )
    }

    private fun continueAdventure(
        random: Random?,
        seed: Int?,
        player: Player?,
        difficulty: Difficulty?
    ) {
        if (random != null && difficulty != null) {
            val newScenario =
                scenarioFactory.createNewScenario(
                    random,
                    difficulty,
                    completedScenarios
                )
            currentEventLog.add(0, AdventureEvent(newScenario.description))
            currentBattle = null
            postAdventureUpdate(
                seed,
                player,
                newScenario,
                currentBattle,
                newScenario.decisions.toList()
            )
        }
    }

    private fun makeDecisionImpl(
        scenarioDecision: ScenarioDecision,
        random: Random?,
        seed: Int?,
        player: Player?,
        difficulty: Difficulty?,
        scenario: Scenario?,
        decisions: List<ScenarioDecision>?
    ) {
        if (random != null && scenario != null && seed != null && player != null && decisions != null && difficulty != null) {
            when (scenarioDecision) {
                continueDecisionResult -> {
                    continueAdventure(random, seed, player, difficulty)
                }
                fightDecisionResult -> {
                    val updatedDecisions = listOf(attackDecisionResult, defendDecisionResult)
                    currentBattle?.let {
                        postAdventureUpdate(seed, player, scenario, it, updatedDecisions)
                    }
                }
                attackDecisionResult -> {
                    handleBattleDecision(
                        scenarioDecision,
                        random,
                        player,
                        scenario,
                        currentOpponentHealth.value,
                        currentBattle
                    )
                }
                defendDecisionResult -> {
                    handleBattleDecision(
                        scenarioDecision,
                        random,
                        player,
                        scenario,
                        currentOpponentHealth.value,
                        currentBattle
                    )
                }
                gameOverDecisionResult -> {
                    currentScenarioType?.let { scenarioType ->
                        currentSessionLength?.let { sessionLength ->
                            startNewAdventure(seed, scenarioType, difficulty, sessionLength)
                        }
                    }
                }
                else -> {
                    handleNormalDecision(scenarioDecision, random, seed, player, scenario)
                }
            }
        }
    }

    private fun handleBattleDecision(
        scenarioDecision: ScenarioDecision,
        random: Random,
        player: Player,
        scenario: Scenario,
        opponentHealth: Int?,
        battle: Battle?
    ) {

        if (battle != null && opponentHealth != null) {
            var updatedDecisions = listOf(
                attackDecisionResult,
                defendDecisionResult
            )

            // Calculate damage
            var playerDamage = Pair(0, false)
            var opponentDamage = Pair(0, false)
            val isOpponentAttacking = isOpponentAttacking(random, battle.attackChance)
            val isPlayerAttacking = (scenarioDecision == attackDecisionResult)
            if (isPlayerAttacking && isOpponentAttacking) {
                playerDamage = calculateDamageAndWasStrong(random, player.attack, 1)
                logBattleEvent(random, battle, true, playerDamage.second)
                opponentDamage = calculateDamageAndWasStrong(random, battle.attack, 1)
                logBattleEvent(random, battle, false, opponentDamage.second)
            } else if (isPlayerAttacking && !isOpponentAttacking) {
                playerDamage = calculateDamageAndWasStrong(random, player.attack, battle.defense)
                logBattleEvent(random, battle, true, playerDamage.second)
            } else if (!isPlayerAttacking && isOpponentAttacking) {
                opponentDamage = calculateDamageAndWasStrong(random, battle.attack, player.defense)
                logBattleEvent(random, battle, false, opponentDamage.second)
            } else {
                currentEventLog.add(0, AdventureEvent(context.getString(R.string.txt_all_defend)))
            }

            // Calculate opponent health
            var newOpponentHealth = opponentHealth - playerDamage.first
            if (newOpponentHealth <= 0) {
                if (battle.reward != 0) {
                    logChangeInGold(battle.successMessage, battle.reward)
                } else {
                    currentEventLog.add(0, AdventureEvent(battle.successMessage))
                }
                updatedDecisions = listOf(continueDecisionResult)
                newOpponentHealth = 0;
                player.goldCount += battle.reward
            }
            currentOpponentHealth.postValue(newOpponentHealth)

            // Calculate player health
            val newPlayerHealth = player.health - opponentDamage.first
            if (newPlayerHealth <= 0) {
                currentEventLog.add(0, AdventureEvent(battle.failureMessage))
                player.health = 0
                setGameOver(currentAdventure.value)
                return
            }
            player.health = newPlayerHealth

            postAdventureUpdate(currentSeed, player, scenario, currentBattle, updatedDecisions)
        }
    }

    private fun isOpponentAttacking(random: Random, attackChance: Double): Boolean {
        return random.nextInt(0, 10) <= (attackChance * 10).toInt()
    }

    private fun calculateDamageAndWasStrong(
        random: Random,
        attack: Int,
        defense: Int
    ): Pair<Int, Boolean> {
        val isStrongAttack = random.nextInt(0, 10) <= attack
        val isStrongDefence = random.nextInt(0, 10) <= defense

        if (isStrongAttack && isStrongDefence) {
            return Pair(random.nextInt(1, 10), false)
        } else if (isStrongAttack && !isStrongDefence) {
            return Pair(random.nextInt(5, 10), true)
        } else if (!isStrongAttack && isStrongDefence) {
            return Pair(random.nextInt(1, 5), false)
        } else if (!isStrongAttack && !isStrongDefence) {
            return Pair(5, false)
        }
        return Pair(0, false)
    }

    private fun logBattleEvent(
        random: Random,
        battle: Battle,
        wasPlayerAttack: Boolean,
        wasStrong: Boolean
    ) {
        val messageList: List<String> = if (wasPlayerAttack) {
            if (wasStrong) battle.strongAttackMessages else battle.weakAttackMessages
        } else {
            if (wasStrong) battle.weakDefenseMessages else battle.strongDefenseMessages
        }
        currentEventLog.add(
            0,
            AdventureEvent(
                messageList[random.nextInt(messageList.size)],
                if (wasPlayerAttack) AdventureEvent.Type.Good else AdventureEvent.Type.Bad
            )
        )
    }

    private fun handleNormalDecision(
        scenarioDecision: ScenarioDecision,
        random: Random,
        seed: Int,
        player: Player,
        scenario: Scenario
    ) {
        val decisionResult = when (random.nextInt(100)) {
            in 0..((scenarioDecision.successChance * 100).toInt()) -> scenarioDecision.successResult
            else -> scenarioDecision.failureResult
        }

        var updatedDecisions = listOf(continueDecisionResult)
        decisionResult?.let {
            if (it.changeInGold != 0) {
                logChangeInGold(it.text, it.changeInGold)
            } else {
                currentEventLog.add(0, AdventureEvent(it.text))
            }
            player.goldCount += it.changeInGold

            if (decisionResult.battleId != null) {
                val battle = scenarioFactory.getBattleForId(decisionResult.battleId)
                currentOpponentHealth.postValue(battle.health)
                currentBattle = battle
                updatedDecisions = listOf(fightDecisionResult)
            }
        }
        completedScenarios.add(scenario)
        currentSessionLength?.let {
            if (completedScenarios.size >= it) {
                setGameOver(currentAdventure.value)
                return
            }
        }

        postAdventureUpdate(seed, player, scenario, currentBattle, updatedDecisions)
    }

    private fun logChangeInGold(mainText: String, changeInGold: Int) {
        currentEventLog.add(
            0, AdventureEvent(
                String.format("%s (%d)", mainText, changeInGold), when {
                    changeInGold > 0 -> AdventureEvent.Type.Gold
                    changeInGold < 0 -> AdventureEvent.Type.Bad
                    else -> AdventureEvent.Type.Neutral
                }
            )
        )
    }

    private fun setGameOver(adventure: Adventure?) {
        if (adventure != null) {
            val gameOverScenario = Scenario(
                context.getString(R.string.txt_game_over_title),
                adventure.scenario.difficulty,
                context.getString(
                    R.string.txt_game_over_description,
                    completedScenarios.size,
                    adventure.player.goldCount,
                    adventure.player.health,
                    adventure.player.maxHealth
                ),
                listOf(gameOverDecisionResult)
            )
            currentBattle = null
            postAdventureUpdate(
                adventure.seed,
                adventure.player,
                gameOverScenario,
                currentBattle,
                gameOverScenario.decisions
            )
        }
    }

    private fun postAdventureUpdate(
        seed: Int?,
        player: Player?,
        scenario: Scenario?,
        battle: Battle?,
        decisions: List<ScenarioDecision>
    ) {
        if (seed != null && player != null && scenario != null) {
            currentScenario = scenario
            currentBattle = battle

            currentDecisions.postValue(decisions)
            currentAdventure.postValue(
                Adventure(
                    seed,
                    player,
                    scenario,
                    battle,
                    decisions,
                    currentEventLog
                )
            )
        }
    }
}