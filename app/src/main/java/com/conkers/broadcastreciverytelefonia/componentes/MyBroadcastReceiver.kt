
package com.conkers.broadcastreciverytelefonia.componentes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log


private const val TAG = ".MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                // Leer el número y mensaje guardados en SharedPreferences
                val sharedPref = context.getSharedPreferences("AutoRespuesta", Context.MODE_PRIVATE)
                val userDefinedNumber = sharedPref.getString("phoneNumber", "")
                val autoReplyMessage = sharedPref.getString("message", "")

                // Comparar el número de llamada entrante con el configurado
                if (incomingNumber == userDefinedNumber) {
                    sendAutoReplySMS(incomingNumber, autoReplyMessage ?:"", context)
                }
            }
        }
    }

    private fun sendAutoReplySMS(phoneNumber: String?, message: String, context: Context) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d(TAG, "Mensaje enviado a: $phoneNumber")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando el SMS", e)
        }
    }
}
