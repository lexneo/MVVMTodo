package com.codinginflow.mvvmtodo.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.SortOrder
import com.codinginflow.mvvmtodo.data.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    private val preferencesManager: PreferencesManager
    ) : ViewModel() {

        val searchQuery = MutableStateFlow("")

        /*val sortOrder = MutableStateFlow(SortOrder.BY_DATE)
        val hideCompleted = MutableStateFlow(false)*/

    val preferencesFlow = preferencesManager.preferencesFlow

        private val taskFlow =
            combine(
                searchQuery,
               preferencesFlow
            ){
                query,filteredPreferences ->
                Pair(query,filteredPreferences)

            }.flatMapLatest {(query,filteredPreferences) ->
                taskDao.getTasks(query,filteredPreferences.sortOrder,filteredPreferences.hideCompleted)
            }

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted : Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

        val tasks = taskFlow.asLiveData()
}

