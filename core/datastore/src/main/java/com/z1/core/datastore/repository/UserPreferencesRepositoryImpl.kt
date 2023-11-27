package com.z1.core.datastore.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.z1.comparaprecos.core.model.UserData
import com.z1.core.datastore.keys.Keys
import com.z1.core.datastore.keys.ThemeKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
): UserPreferencesRepository {
    override val userData: Flow<UserData> = dataStore.data.map { preferences ->
            UserData(
                themeId = preferences[longPreferencesKey(ThemeKeys.SELECTED_THEME)] ?: 0L,
                useDynamicColor = preferences[longPreferencesKey(ThemeKeys.USE_DYNAMIC_COLOR)] ?: 0L,
                darkThemeMode = preferences[longPreferencesKey(ThemeKeys.DARK_THEME_MODE)] ?: 0L
            )
        }

    override val listOfProdutoOrdenation: Flow<Long> =
        dataStore.data.map { preference ->
            preference[longPreferencesKey(Keys.ORDENACAO_LISTA_PRODUTO)] ?: 2L
        }

    override suspend fun putSelectedTheme(themeId: Long) {
        dataStore.edit { preference ->
            preference[longPreferencesKey(ThemeKeys.SELECTED_THEME)] = themeId
        }
    }

    override suspend fun putUseDynamicColor(useDynamicColor: Long) {
        dataStore.edit { preference ->
            preference[longPreferencesKey(ThemeKeys.USE_DYNAMIC_COLOR)] = useDynamicColor
        }
    }

    override suspend fun putDarkThemeMode(darkThemeMode: Long) {
        dataStore.edit { preference ->
            preference[longPreferencesKey(ThemeKeys.DARK_THEME_MODE)] = darkThemeMode
        }
    }

    override suspend fun putListOfProdutoOrdenation(ordenationId: Long) {
        dataStore.edit { preference ->
            preference[longPreferencesKey(Keys.ORDENACAO_LISTA_PRODUTO)] = ordenationId
        }
    }
}