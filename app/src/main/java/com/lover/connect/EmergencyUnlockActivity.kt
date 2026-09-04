package com.lover.connect

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/**
 * 紧急解锁：所有「解除全部应用锁」的入口都先到这里验密码，
 * 验证通过才调用 AppLockManager.clearAll，避免用户自己一键解锁。
 */
class EmergencyUnlockActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(64, 64, 64, 64)
            setBackgroundColor(Color.rgb(25, 22, 35))
        }

        val title = TextView(this).apply {
            text = "紧急解锁"
            textSize = 26f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val desc = TextView(this).apply {
            text = if (AppLockManager.hasEmergencyPin(this@EmergencyUnlockActivity)) {
                "输入你设置的紧急解锁密码，将解除全部应用锁。"
            } else {
                "尚未设置紧急解锁密码。请先在 LoverConnect 主界面设置密码后再使用。"
            }
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 32)
        }

        val input = EditText(this).apply {
            hint = "紧急解锁密码"
            textSize = 18f
            setTextColor(Color.WHITE)
            setHintTextColor(Color.GRAY)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isSingleLine = true
        }

        val confirm = Button(this).apply {
            text = "解锁全部"
            setOnClickListener {
                if (AppLockManager.verifyEmergencyPin(this@EmergencyUnlockActivity, input.text.toString())) {
                    AppLockManager.clearAll(this@EmergencyUnlockActivity)
                    LCAccessibilityService.instance?.dismissLockOverlay()
                    Toast.makeText(this@EmergencyUnlockActivity, "已解除全部应用锁", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this@EmergencyUnlockActivity, "密码错误", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val cancel = Button(this).apply {
            text = "返回"
            setOnClickListener { finish() }
        }

        root.addView(title)
        root.addView(desc)
        root.addView(input, LinearLayout.LayoutParams(-1, -2))
        root.addView(confirm, LinearLayout.LayoutParams(-1, -2).apply { topMargin = 28 })
        root.addView(cancel, LinearLayout.LayoutParams(-1, -2).apply { topMargin = 12 })
        setContentView(root)
    }
}
