package com.ayvytr.pic

import android.app.Application
import com.biubiu.eventbus.EventBusInitializer
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel

/**
 * @author Administrator
 */
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        MMKV.setLogLevel(if(BuildConfig.DEBUG) MMKVLogLevel.LevelInfo else MMKVLogLevel.LevelNone )

        EventBusInitializer.init(this)
    }
}