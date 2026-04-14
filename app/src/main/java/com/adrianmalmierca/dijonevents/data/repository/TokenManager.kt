package com.adrianmalmierca.dijonevents.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenManager @Inject constructor(/*@Inject to indicate this class can be built automatically
hilt resolves the dependencies, like context. Is to avoid to create object manually*/
    @ApplicationContext private val context: Context
) {
    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    /* Flow because if the token changes(login, logout, refresh): each part of the app that makes collect of the flow
     receives the new value automatically */
    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }

    suspend fun saveAuth(token: String, name: String, email: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_NAME_KEY] = name
            prefs[USER_EMAIL_KEY] = email
        }
    }

    suspend fun clearAuth() {
        context.dataStore.edit { it.clear() }
    }

    fun bearerToken(token: String) = "Bearer $token"
}
