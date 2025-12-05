package com.example.smartdialer

import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle

class MyCallService : ConnectionService() {

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Connection {

        val conn = object : Connection() {
            override fun onDisconnect() {
                setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
                destroy()
            }
        }

        conn.setDialing()
        conn.setInitialized()

        return conn
    }
}
