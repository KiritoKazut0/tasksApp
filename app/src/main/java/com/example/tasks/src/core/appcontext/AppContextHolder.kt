package com.example.tasks.src.core.appcontext

import android.content.Context

object AppContextHolder{
    private lateinit var context: Context

    fun init(context: Context){
        this.context = context.applicationContext
    }

    fun get(): Context {
        check(::context.isInitialized) {  "AppContextHolder no ha sido inicializada" }
        return context
    }

}