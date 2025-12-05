package com.example.smartdialer.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartdialer.repo.T9Repository

class T9ViewModelFactory(private val repo: T9Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(T9ViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return T9ViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
