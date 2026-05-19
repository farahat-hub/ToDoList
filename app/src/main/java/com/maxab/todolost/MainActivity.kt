package com.maxab.todolost

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.maxab.todolost.ui.TodoScreen
import com.maxab.todolost.ui.theme.ToDoLostTheme
import com.maxab.todolost.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToDoLostTheme {
                val taskViewModel: TaskViewModel = viewModel()
                TodoScreen(viewModel = taskViewModel)
            }
        }
    }
}
