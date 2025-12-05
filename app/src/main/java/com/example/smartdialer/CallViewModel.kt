package com.example.smartdialer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartdialer.data.CallLogDao
import com.example.smartdialer.data.CallLogEntity
import kotlinx.coroutines.launch

class CallViewModel(
    private val callLogDao: CallLogDao
) : ViewModel() {

    fun saveCall(number: String, type: String) {
        viewModelScope.launch {
            val entry = CallLogEntity(
                number = number,
                timestamp = System.currentTimeMillis(),
                type = type
            )
            callLogDao.insert(entry)
        }
    }
}
