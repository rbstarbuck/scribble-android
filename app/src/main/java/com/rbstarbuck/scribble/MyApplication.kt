package com.rbstarbuck.scribble

import android.app.Application
import com.rbstarbuck.scribble.koin.Module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApplication)
            modules(Module.stateModule)
            modules(Module.typesModule)
            modules(Module.viewModelsModule)
        }
    }
}