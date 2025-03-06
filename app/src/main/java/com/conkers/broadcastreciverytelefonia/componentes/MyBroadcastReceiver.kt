
package com.conkers.broadcastreciverytelefonia.componentes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

private const val TAG = "MyBroadcastReceiver"

class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "El sistema se ha iniciado", Toast.LENGTH_LONG).show()
            }

            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
                    Log.d(TAG, "Llamada entrante detectada del número: $incomingNumber")

                    // Obtener número y mensaje configurados por el usuario
                    val sharedPref = context.getSharedPreferences("AutoReplySettings", Context.MODE_PRIVATE)
                    val userDefinedNumber = sharedPref.getString("phoneNumber", null)
                    val autoReplyMessage = sharedPref.getString("message", null)

                    Log.d(TAG, "Número configurado: $userDefinedNumber, Mensaje configurado: $autoReplyMessage")

                    if (incomingNumber == userDefinedNumber && autoReplyMessage != null) {
                        sendAutoReplySMS(context, incomingNumber, autoReplyMessage)
                    }
                }
            }

            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                // Este bloque lo dejamos para manejar mensajes SMS si es necesario
                val bndSMS = intent.extras
                val pdus = bndSMS!!["pdus"] as Array<Any>?
                val smms: Array<SmsMessage?> = arrayOfNulls(pdus!!.size)
                var strMensaje = ""
                for (i in smms.indices) {
                    smms[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    strMensaje = "Mensaje: " + smms[i]!!.originatingAddress + "\n" +
                            smms[i]!!.messageBody.toString()
                }
                Log.d(TAG, strMensaje)
                Toast.makeText(context, strMensaje, Toast.LENGTH_LONG).show()
            }

            else -> {
                Log.d(TAG, "Acción no manejada: ${intent.action}")
            }
        }
    }

    private fun sendAutoReplySMS(context: Context, phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Log.d(TAG, "Mensaje enviado a: $phoneNumber")
        } catch (e: Exception) {
            Log.e(TAG, "Error enviando SMS", e)
        }
    }
}
