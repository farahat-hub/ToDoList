package com.maxab.todolost.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.maxab.todolost.model.Category
import com.maxab.todolost.model.Task
import java.util.UUID

class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    fun addTask(name: String, category: Category) {
        _tasks.add(Task(name = name, category = category))
    }

    fun toggleDone(id: UUID) {
        val index = _tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            _tasks[index] = _tasks[index].copy(isDone = !_tasks[index].isDone)
        }
    }

    fun deleteTask(id: UUID) {
        _tasks.removeAll { it.id == id }
    }
}
