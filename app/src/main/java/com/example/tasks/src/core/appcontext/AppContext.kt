package com.example.tasks.src.core.appcontext

import android.content.Context

object AppContext {
    private lateinit var context: Context

    fun init(context: Context){
        this.context = context.applicationContext
    }

    fun get(): Context {
        check(::context.isInitialized) {  "app context no inicializada" }
        return context
    }

}