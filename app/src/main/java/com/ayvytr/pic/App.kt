package com.ayvytr.pic

import android.app.Application
import com.biubiu.eventbus.EventBusInitializer

/**
 * @author Administrator
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        EventBusInitializer.init(this)
    }
}