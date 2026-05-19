package com.maxab.todolost.viewmodel

import com.maxab.todolost.model.Category
import com.maxab.todolost.model.Task
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.UUID

class TaskViewModelTest {

    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        viewModel = TaskViewModel()
    }

    // --- Initial state ---

    @Test
    fun tasks_initiallyEmpty() {
        assertTrue(viewModel.tasks.isEmpty())
    }

    // --- addTask ---

    @Test
    fun addTask_increasesListSize() {
        viewModel.addTask("Buy milk", Category.HOME)
        assertEquals(1, viewModel.tasks.size)
    }

    @Test
    fun addTask_storesCorrectName() {
        viewModel.addTask("Buy milk", Category.HOME)
        assertEquals("Buy milk", viewModel.tasks[0].name)
    }

    @Test
    fun addTask_storesCorrectCategory_home() {
        viewModel.addTask("Clean room", Category.HOME)
        assertEquals(Category.HOME, viewModel.tasks[0].category)
    }

    @Test
    fun addTask_storesCorrectCategory_work() {
        viewModel.addTask("Write report", Category.WORK)
        assertEquals(Category.WORK, viewModel.tasks[0].category)
    }

    @Test
    fun addTask_newTaskDefaultsToNotDone() {
        viewModel.addTask("Exercise", Category.HOME)
        assertFalse(viewModel.tasks[0].isDone)
    }

    @Test
    fun addTask_multipleTasksAllAdded() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        viewModel.addTask("Task C", Category.WORK)
        assertEquals(3, viewModel.tasks.size)
    }

    @Test
    fun addTask_multipleTasksHaveDistinctIds() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val id0 = viewModel.tasks[0].id
        val id1 = viewModel.tasks[1].id
        assertFalse("Tasks should have distinct UUIDs", id0 == id1)
    }

    @Test
    fun addTask_preservesInsertionOrder() {
        viewModel.addTask("First", Category.WORK)
        viewModel.addTask("Second", Category.HOME)
        assertEquals("First", viewModel.tasks[0].name)
        assertEquals("Second", viewModel.tasks[1].name)
    }

    // --- toggleDone ---

    @Test
    fun toggleDone_marksTaskAsDone() {
        viewModel.addTask("Do laundry", Category.HOME)
        val id = viewModel.tasks[0].id
        viewModel.toggleDone(id)
        assertTrue(viewModel.tasks[0].isDone)
    }

    @Test
    fun toggleDone_togglesDoneTaskBackToNotDone() {
        viewModel.addTask("Do laundry", Category.HOME)
        val id = viewModel.tasks[0].id
        viewModel.toggleDone(id)
        viewModel.toggleDone(id)
        assertFalse(viewModel.tasks[0].isDone)
    }

    @Test
    fun toggleDone_doesNotAffectOtherTasks() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val idA = viewModel.tasks[0].id
        viewModel.toggleDone(idA)
        assertFalse(viewModel.tasks[1].isDone)
    }

    @Test
    fun toggleDone_withUnknownUuid_doesNothing() {
        viewModel.addTask("Task A", Category.WORK)
        val unknownId = UUID.randomUUID()
        viewModel.toggleDone(unknownId)
        assertEquals(1, viewModel.tasks.size)
        assertFalse(viewModel.tasks[0].isDone)
    }

    @Test
    fun toggleDone_onlyUpdatesMatchingTask() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val idB = viewModel.tasks[1].id
        viewModel.toggleDone(idB)
        assertFalse(viewModel.tasks[0].isDone)
        assertTrue(viewModel.tasks[1].isDone)
    }

    // --- deleteTask ---

    @Test
    fun deleteTask_removesTaskFromList() {
        viewModel.addTask("Buy coffee", Category.WORK)
        val id = viewModel.tasks[0].id
        viewModel.deleteTask(id)
        assertTrue(viewModel.tasks.isEmpty())
    }

    @Test
    fun deleteTask_decreasesListSize() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val id = viewModel.tasks[0].id
        viewModel.deleteTask(id)
        assertEquals(1, viewModel.tasks.size)
    }

    @Test
    fun deleteTask_removesOnlyMatchingTask() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val idA = viewModel.tasks[0].id
        viewModel.deleteTask(idA)
        assertEquals("Task B", viewModel.tasks[0].name)
    }

    @Test
    fun deleteTask_withUnknownUuid_doesNothing() {
        viewModel.addTask("Task A", Category.WORK)
        val unknownId = UUID.randomUUID()
        viewModel.deleteTask(unknownId)
        assertEquals(1, viewModel.tasks.size)
    }

    @Test
    fun deleteTask_allTasks_leavesListEmpty() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val id0 = viewModel.tasks[0].id
        val id1 = viewModel.tasks[1].id
        viewModel.deleteTask(id0)
        viewModel.deleteTask(id1)
        assertTrue(viewModel.tasks.isEmpty())
    }

    @Test
    fun deleteTask_doesNotAffectRemainingTasksDoneState() {
        viewModel.addTask("Task A", Category.WORK)
        viewModel.addTask("Task B", Category.HOME)
        val idA = viewModel.tasks[0].id
        val idB = viewModel.tasks[1].id
        viewModel.toggleDone(idB)
        viewModel.deleteTask(idA)
        assertTrue(viewModel.tasks[0].isDone)
    }

    // --- tasks exposure ---

    @Test
    fun tasks_exposedAsListNotMutable() {
        // Verifies that the return type is List<Task>, not the internal mutable snapshot list
        val tasks: List<Task> = viewModel.tasks
        assertFalse(tasks is MutableList)
    }

    // --- regression / boundary cases ---

    @Test
    fun addTask_withBlankName_addsTaskAnyway() {
        // ViewModel itself does not enforce non-blank; that is a UI-layer concern
        viewModel.addTask("   ", Category.WORK)
        assertEquals(1, viewModel.tasks.size)
        assertEquals("   ", viewModel.tasks[0].name)
    }

    @Test
    fun toggleDone_multipleTimesEqualsOriginalState() {
        viewModel.addTask("Ping pong", Category.HOME)
        val id = viewModel.tasks[0].id
        repeat(4) { viewModel.toggleDone(id) }
        assertFalse(viewModel.tasks[0].isDone)
    }
}
