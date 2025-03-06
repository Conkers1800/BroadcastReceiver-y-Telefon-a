
package com.conkers.broadcastreciverytelefonia.Servicio
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.TelephonyManager
import com.conkers.broadcastreciverytelefonia.componentes.MyBroadcastReceiver

class ServiceState : Service() {
    val br: MyBroadcastReceiver = MyBroadcastReceiver()
    val intentFilter: IntentFilter = IntentFilter().apply {
        addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED)
        addAction(Intent.ACTION_BOOT_COMPLETED)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        registerReceiver(br, intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(br)
    }
}
