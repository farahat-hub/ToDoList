package com.maxab.todolost.model

import java.util.UUID

enum class Category { WORK, HOME }

data class Task(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val category: Category,
    val isDone: Boolean = false
)
