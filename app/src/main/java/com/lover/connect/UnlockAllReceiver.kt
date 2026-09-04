package com.lover.connect

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class UnlockAllReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // 所有「一键解除全部」入口都必须先过紧急解锁密码，不能在这里直接清锁。
        val launch = Intent(context, EmergencyUnlockActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launch)
    }
}
