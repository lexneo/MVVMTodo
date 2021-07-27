package com.codinginflow.mvvmtodo.data

import android.content.Context
import android.util.Log
import androidx.datastore.createDataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


private const val TAG = "PreferencesManager"
enum class SortOrder{
    BY_NAME, BY_DATE
}

data class FilterPreferences(val sortOrder: SortOrder,val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch {exception ->
            if (exception is IOException){
                Log.e(TAG, "Error reading preferences ", exception )
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map {
         val sortOrder = SortOrder.valueOf(
             it[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
         )

            val hideCompleted = it[PreferencesKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder,hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit {
            it[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        dataStore.edit {
            it[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys{
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED= preferencesKey<Boolean  >("hide_completed")
    }
}