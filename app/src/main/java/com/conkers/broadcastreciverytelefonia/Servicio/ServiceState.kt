
package com.conkers.broadcastreciverytelefonia.Servicio
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.TelephonyManager
import com.conkers.broadcastreciverytelefonia.componentes.MyBroadcastReceiver

class ServiceState : Service() {
    private val br = MyBroadcastReceiver()
    private val intentFilter = IntentFilter().apply {
        addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(br, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(br)
    }
}
