package com.conkers.broadcastreciverytelefonia

import AutoReplyApp
import android.content.Intent
import android.os.Bundle
import android.telephony.ServiceState
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoReplyApp { phoneNumber, message ->
                // Guardar número y mensaje en SharedPreferences
                val sharedPref = getSharedPreferences("AutoRespuesta", MODE_PRIVATE)
                sharedPref.edit {
                    putString("phoneNumber", phoneNumber)
                    putString("message", message)
                }
                // Notificar al usuario que se ha guardado la configuración
                Toast.makeText(this, "Listo para enviar", Toast.LENGTH_SHORT)
                    .show()

                // Iniciar el servicio
                val serviceIntent = Intent(this, ServiceState::class.java)
                startService(serviceIntent)
            }
        }
    }
}