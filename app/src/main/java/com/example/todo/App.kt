package com.example.todo

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class App : MultiDexApplication() {
  override fun onCreate() {
    super.onCreate()
    initDI()
  }

  private fun initDI() {
    startKoin {
      androidLogger()
      androidContext(this@App)
      modules(com.example.todo.di.modules)
    }
  }
}