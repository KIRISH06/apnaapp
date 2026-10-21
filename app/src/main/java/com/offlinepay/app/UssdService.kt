package com.offlinepay.app

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class UssdService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Suppresses *99# USSD system dialogs and automates response steps in background
    }

    override fun onInterrupt() {}
}
