package com.example.playground

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PlaygroundApp : Application() {
    override fun onCreate() {
        super.onCreate()

    }
}