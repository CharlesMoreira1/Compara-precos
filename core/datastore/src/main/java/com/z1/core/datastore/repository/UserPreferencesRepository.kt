package com.z1.core.datastore.repository

import com.z1.comparaprecos.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userData: Flow<UserData>

    suspend fun putSelectedTheme(themeId: Long)
    suspend fun putUseDynamicColor(useDynamicColor: Long)
    suspend fun putDarkThemeMode(darkThemeMode: Long)
}