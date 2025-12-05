package com.example.smartdialer_mvp

import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.util.Log

class MyConnectionService : ConnectionService() {

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: android.telecom.PhoneAccountHandle?,
        request: ConnectionRequest?
    ): Connection {
        val conn = object : Connection() {
            override fun onAnswer() { /* not for outgoing */ }
            override fun onDisconnect() {
                setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
                destroy()
            }
        }
        conn.setInitializing()
        conn.setActive()
        Log.d("MyConnectionService","Outgoing connection created")
        return conn
    }
}
