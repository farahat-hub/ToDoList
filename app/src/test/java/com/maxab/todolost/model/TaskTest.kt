package com.maxab.todolost.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class TaskTest {

    @Test
    fun task_defaultIdIsNotNull() {
        val task = Task(name = "Buy groceries", category = Category.HOME)
        assertNotNull(task.id)
    }

    @Test
    fun task_defaultIsDoneIsFalse() {
        val task = Task(name = "Buy groceries", category = Category.HOME)
        assertFalse(task.isDone)
    }

    @Test
    fun task_storesNameCorrectly() {
        val task = Task(name = "Write report", category = Category.WORK)
        assertEquals("Write report", task.name)
    }

    @Test
    fun task_storesCategoryCorrectly_work() {
        val task = Task(name = "Write report", category = Category.WORK)
        assertEquals(Category.WORK, task.category)
    }

    @Test
    fun task_storesCategoryCorrectly_home() {
        val task = Task(name = "Clean kitchen", category = Category.HOME)
        assertEquals(Category.HOME, task.category)
    }

    @Test
    fun task_canBeCreatedWithExplicitId() {
        val fixedId = UUID.fromString("12345678-1234-1234-1234-123456789012")
        val task = Task(id = fixedId, name = "Test task", category = Category.WORK)
        assertEquals(fixedId, task.id)
    }

    @Test
    fun task_canBeCreatedWithIsDoneTrue() {
        val task = Task(name = "Done task", category = Category.HOME, isDone = true)
        assertTrue(task.isDone)
    }

    @Test
    fun twoDefaultTasks_haveDistinctIds() {
        val task1 = Task(name = "Task 1", category = Category.WORK)
        val task2 = Task(name = "Task 2", category = Category.HOME)
        assertNotEquals(task1.id, task2.id)
    }

    @Test
    fun task_copyWithIsDoneToggled() {
        val task = Task(name = "Buy milk", category = Category.HOME, isDone = false)
        val toggled = task.copy(isDone = true)
        assertTrue(toggled.isDone)
        assertEquals(task.id, toggled.id)
        assertEquals(task.name, toggled.name)
        assertEquals(task.category, toggled.category)
    }

    @Test
    fun task_copyPreservesAllFields() {
        val id = UUID.randomUUID()
        val task = Task(id = id, name = "Original", category = Category.WORK, isDone = false)
        val copy = task.copy()
        assertEquals(task, copy)
    }

    @Test
    fun task_equalityBasedOnAllFields() {
        val id = UUID.randomUUID()
        val task1 = Task(id = id, name = "Same", category = Category.WORK, isDone = false)
        val task2 = Task(id = id, name = "Same", category = Category.WORK, isDone = false)
        assertEquals(task1, task2)
    }

    @Test
    fun task_inequalityWhenIsDoneDiffers() {
        val id = UUID.randomUUID()
        val task1 = Task(id = id, name = "Task", category = Category.HOME, isDone = false)
        val task2 = Task(id = id, name = "Task", category = Category.HOME, isDone = true)
        assertNotEquals(task1, task2)
    }

    @Test
    fun category_hasWorkValue() {
        assertEquals("WORK", Category.WORK.name)
    }

    @Test
    fun category_hasHomeValue() {
        assertEquals("HOME", Category.HOME.name)
    }

    @Test
    fun category_exactlyTwoValues() {
        assertEquals(2, Category.values().size)
    }

    @Test
    fun task_emptyNameIsAllowed() {
        val task = Task(name = "", category = Category.WORK)
        assertEquals("", task.name)
    }
}