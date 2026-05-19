package com.maxab.todolost.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.maxab.todolost.model.Category
import com.maxab.todolost.model.Task
import com.maxab.todolost.ui.theme.ToDoLostTheme
import com.maxab.todolost.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TaskViewModel) {
    var taskName by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.WORK) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("ToDoLost") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Input section
            OutlinedTextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Task name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = selectedCategory == Category.WORK,
                    onClick = { selectedCategory = Category.WORK },
                    label = { Text("Work") }
                )
                FilterChip(
                    selected = selectedCategory == Category.HOME,
                    onClick = { selectedCategory = Category.HOME },
                    label = { Text("Home") }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.addTask(taskName.trim(), selectedCategory)
                    taskName = ""
                },
                enabled = taskName.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add")
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            // Task list
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                items(viewModel.tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggleDone = { viewModel.toggleDone(task.id) },
                        onDelete = { viewModel.deleteTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItem(
    task: Task,
    onToggleDone: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.isDone, onCheckedChange = { onToggleDone() })

        Text(
            text = task.id.toString(),
            modifier = Modifier.weight(1f),
            textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
        )

        AssistChip(
            onClick = {},
            label = { Text(task.category.name) }
        )

        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoScreenPreview() {
    ToDoLostTheme {
        val viewmodel= TaskViewModel()
        TodoScreen(viewModel = viewmodel)
    }
}
