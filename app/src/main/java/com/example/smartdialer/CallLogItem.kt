package com.example.smartdialer

data class CallLogItem(
    val name: String?,
    val number: String,
    val type: String,   // Incoming / Outgoing / Missed / Rejected
    val time: String
)
