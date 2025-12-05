package com.example.smartdialer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdialer.data.ContactEntity
import com.example.smartdialer.repo.T9Repository
//import com.example.smartdialer.t9.T9Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class T9ViewModel(private val repo: T9Repository) : ViewModel() {

    private val _results = MutableStateFlow<List<ContactEntity>>(emptyList())
    val results: StateFlow<List<ContactEntity>> = _results

    fun search(inputDigits: String, rawText: String = "", numberPart: String = "") {
        viewModelScope.launch {
            val res = repo.searchByT9OrText(inputDigits, rawText, numberPart)
            _results.value = res
        }
    }

    fun incrementCall(number: String) {
        viewModelScope.launch {
            repo.incrementCallCount(number)
        }
    }
}
