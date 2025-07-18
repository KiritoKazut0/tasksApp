package com.example.tasks.src.core.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys  {
    val TOKEN = stringPreferencesKey("token")
    val USER_ID = stringPreferencesKey("user_id")
}