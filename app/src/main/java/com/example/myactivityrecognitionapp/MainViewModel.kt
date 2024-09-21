package com.example.myactivityrecognitionapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _transitions = MutableLiveData<List<String>>(emptyList())
    val transitions: LiveData<List<String>> = _transitions

    fun addTransition(transition: String) {
        _transitions.value = _transitions.value?.plus(transition)
    }
}
