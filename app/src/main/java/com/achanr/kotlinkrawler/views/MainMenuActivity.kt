package com.achanr.kotlinkrawler.views

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.achanr.kotlinkrawler.R
import com.achanr.kotlinkrawler.databinding.ActivityMainMenuBinding
import com.achanr.kotlinkrawler.interfaces.MainMenuNavigator
import com.achanr.kotlinkrawler.models.Difficulty
import com.achanr.kotlinkrawler.models.ScenarioType
import com.achanr.kotlinkrawler.models.SessionLength
import com.achanr.kotlinkrawler.viewmodels.MainMenuViewModel
import com.achanr.kotlinkrawler.viewmodels.NavigatorViewModelFactory
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlin.random.Random

class MainMenuActivity : AppCompatActivity(), MainMenuNavigator {

    private val viewModel: MainMenuViewModel by viewModels { NavigatorViewModelFactory(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainMenuBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main_menu)
        binding.viewModel = viewModel

        editTextSeed.setText(Random.nextInt(0, Int.MAX_VALUE).toString())
        editTextSeed.filters = arrayOf(InputFilterMinMaxInt())
    }

    override fun navigateToNewGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(GameActivity.seedKey, getSeed())
        intent.putExtra(GameActivity.themeKey, getSelectedTheme())
        intent.putExtra(GameActivity.difficultyKey, getSelectedDifficulty())
        intent.putExtra(GameActivity.sessionLengthKey, getSelectedSessionLength())
        startActivity(intent)
    }

    private fun getSeed(): Int {
        return editTextSeed.text.toString().toInt()
    }

    private fun getSelectedTheme(): Int {
        val item = spinnerTheme.selectedItem
        if (item is ScenarioType) {
            return item.value
        }
        return ScenarioType.FirstAge.value
    }

    private fun getSelectedDifficulty(): Int {
        val item = spinnerDifficulty.selectedItem
        if (item is Difficulty) {
            return item.value
        }
        return Difficulty.VeryEasy.value
    }

    private fun getSelectedSessionLength(): Int {
        val item = spinnerSessionLength.selectedItem
        if (item is Difficulty) {
            return item.value
        }
        return SessionLength.Short.value
    }

    class InputFilterMinMaxInt : InputFilter {
        private val min: Int = 0
        private val max: Int = Int.MAX_VALUE

        override fun filter(
            source: CharSequence,
            start: Int,
            end: Int,
            dest: Spanned,
            dstart: Int,
            dend: Int
        ): CharSequence? {
            try {
                val input = (dest.subSequence(0, dstart).toString() + source + dest.subSequence(
                    dend,
                    dest.length
                )).toInt()
                if (input in min..max) {
                    return null
                }
            } catch (nfe: NumberFormatException) {
            }
            return ""
        }
    }
}