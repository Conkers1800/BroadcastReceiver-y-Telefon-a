import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AutoReplyScreen(context: Context) {
    var phoneNumber by remember { mutableStateOf("") }
    var autoReplyMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número telefónico") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = autoReplyMessage,
            onValueChange = { autoReplyMessage = it },
            label = { Text("Mensaje de respuesta automática") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                // Guardar los datos en SharedPreferences
                val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("savedNumber", phoneNumber)
                    putString("savedMessage", autoReplyMessage)
                    apply()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Configuración")
        }
    }
}
