package com.achanr.kotlinkrawler.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.achanr.kotlinkrawler.interfaces.MainMenuNavigator
import com.achanr.kotlinkrawler.interfaces.Navigator
import java.lang.IllegalArgumentException

class NavigatorViewModelFactory(private val navigator: Navigator) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainMenuViewModel::class.java)
            && navigator is MainMenuNavigator) {
            return MainMenuViewModel(navigator) as T
        }
        throw IllegalArgumentException("Cannot find view model with navigator")
    }
}