package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel containing the app data and methods to process the data
 */
/*
*In the previous tasks, our app listens to the data changes in the code.
* Similarly, apps can listen to the data changes from the layout.
* With Data Binding, when an observable LiveData value changes,
* the UI elements in the layout it's bound to are also notified,
* and the UI can be updated from within the layout.
* (You can bind views to code but not vice versa.)
* (Using view binding you can't reference the app data in the views (layout files). This can be accomplished using Data binding.)
* Data binding is binding data (from code) to views + view binding (binding views to code)
*
* advantage of using data binding is,
* it lets you remove many UI framework calls in your activities,making them simpler and easier to maintain.
* This can also improve your app's performance and help prevent memory leaks and null pointer exceptions.
* */

class GameViewModel : ViewModel(){
    private val _score = MutableLiveData(0)
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _currentScrambledWord = MutableLiveData<String>()
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    // List of words used in the game
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String

    init {
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    /*
    * Updates currentWord and currentScrambledWord with the next word.
    */
    private fun getNextWord() {
        currentWord = allWordsList.random()
        val tempWord = currentWord.toCharArray()
        tempWord.shuffle()

        while (String(tempWord).equals(currentWord, false)) {
            tempWord.shuffle()
        }
        if (wordsList.contains(currentWord)) {
            getNextWord()
        }
        //To access the data within a LiveData object, use the value property.
        else {
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    /*
    * Re-initializes the game data to restart the game.
    */
    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }


    /*
    * Increases the game score if the player's word is correct.
    */
    private fun increaseScore() {
        _score.value =(_score.value)?.plus(SCORE_INCREASE)
    }

    /*
    * Returns true if the player word is correct.
    * Increases the score accordingly.
    */
    fun isUserWordCorrect(playerWord: String): Boolean {
        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    /*
    * Returns true if the current word count is less than MAX_NO_OF_WORDS
    */
    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }
}