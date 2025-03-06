package com.conkers.broadcastreciverytelefonia.componentes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

private const val TAG = "MyBroadcastReceiver"
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "El sistema se inició correctamente", Toast.LENGTH_LONG).show()
                Log.d("MyBroadcastReceiver", "Sistema iniciado")
            }
            TelephonyManager.ACTION_PHONE_STATE_CHANGED -> {
                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
                val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                // Recuperar datos guardados
                val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
                val savedNumber = sharedPreferences.getString("savedNumber", null)
                val savedMessage = sharedPreferences.getString("savedMessage", null)

                if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber == savedNumber) {
                    sendAutoReply(incomingNumber, savedMessage)
                }
            }
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                val bundle = intent.extras
                val pdus = bundle?.get("pdus") as? Array<*>
                pdus?.forEach { pdu ->
                    val sms = Telephony.SmsMessage.createFromPdu(pdu as ByteArray)
                    val message = "Mensaje de: ${sms.originatingAddress}\nContenido: ${sms.messageBody}"
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    Log.d("MyBroadcastReceiver", message)
                }
            }
        }
    }

    private fun sendAutoReply(number: String?, message: String?) {
        if (!number.isNullOrEmpty() && !message.isNullOrEmpty()) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, message, null, null)
            Log.d("MyBroadcastReceiver", "Mensaje enviado a $number: $message")
        }
    }
}
