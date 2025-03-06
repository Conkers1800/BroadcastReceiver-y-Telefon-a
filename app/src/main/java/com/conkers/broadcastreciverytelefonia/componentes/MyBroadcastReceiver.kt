import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log

class MyBroadcastReceiver : BroadcastReceiver() {

    private var lastIncomingNumber: String? = null // Guarda el número de la llamada entrante

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // Guardar el número entrante si está sonando
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                lastIncomingNumber = incomingNumber
            }

            // Detectar si la llamada se colgó
            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                // Verificar si el número coincide con el configurado
                val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
                val savedNumber = sharedPreferences.getString("savedNumber", null)
                val savedMessage = sharedPreferences.getString("savedMessage", null)

                if (lastIncomingNumber == savedNumber) {
                    sendAutoReply(savedNumber, savedMessage)
                }

                // Limpiar el estado del último número
                lastIncomingNumber = null
            }
        }
    }

    private fun sendAutoReply(number: String?, message: String?) {
        if (!number.isNullOrEmpty() && !message.isNullOrEmpty()) {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(number, null, message, null, null)
            Log.d("MyBroadcastReceiver", "Mensaje enviado a $number: $message")
        } else {
            Log.e("MyBroadcastReceiver", "Número o mensaje no configurado correctamente.")
        }
    }
}
