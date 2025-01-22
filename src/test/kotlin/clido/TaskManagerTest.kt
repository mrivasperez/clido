package clido

import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class TaskManagerTest {

  @TempDir lateinit var tempDir: Path
  private lateinit var todoFile: Path
  private lateinit var taskManager: TaskManager

  @BeforeEach
  fun setUp() {
    todoFile = tempDir.resolve("todo.txt")
    taskManager = TaskManager(todoFile.toString())
  }

  @Test
  fun testAddTask() {
    taskManager.addTask("Test task", priority = 'A', projects = listOf("project1"))

    val tasks = taskManager.listTasks()
    assertEquals(1, tasks.size)
    assertEquals("Test task", tasks[0].description)
    assertEquals('A', tasks[0].priority)
    assertEquals(LocalDate.now(), tasks[0].creationDate)
    assertEquals(listOf("project1"), tasks[0].projects)
  }

  @Test
  fun testDeleteTask() {
    taskManager.addTask("Task 1")
    taskManager.addTask("Task 2")

    taskManager.deleteTask(1)

    val tasks = taskManager.listTasks()
    assertEquals(1, tasks.size)
    assertEquals("Task 2", tasks[0].description)
  }

  @Test
  fun testDeleteTaskInvalidIndex() {
    assertThrows(IndexOutOfBoundsException::class.java) { taskManager.deleteTask(1) }
  }

  @Test
  fun testCompleteTask() {
    taskManager.addTask("Task 1")

    taskManager.completeTask(1)

    val tasks = taskManager.listTasks()
    assertTrue(tasks[0].isCompleted)
    assertEquals(LocalDate.now(), tasks[0].completionDate)
  }

  @Test
  fun testCompleteTaskInvalidIndex() {
    assertThrows(IndexOutOfBoundsException::class.java) { taskManager.completeTask(1) }
  }

  @Test
  fun testListTasks() {
    taskManager.addTask("Task B", priority = 'B', creationDate = LocalDate.of(2023, 10, 27))
    taskManager.addTask("Task A", priority = 'A', creationDate = LocalDate.of(2023, 10, 26))
    taskManager.addTask("Task C", creationDate = LocalDate.of(2023, 10, 28))

    val tasks = taskManager.listTasks()
    assertEquals("Task A", tasks[0].description)
    assertEquals("Task B", tasks[1].description)
    assertEquals("Task C", tasks[2].description)
  }

  @Test
  fun testListTasksEmpty() {
    val tasks = taskManager.listTasks()
    assertTrue(tasks.isEmpty())
  }

  @Test
  fun testFilterTasksByProject() {
    taskManager.addTask("Task 1", projects = listOf("project1"))
    taskManager.addTask("Task 2", projects = listOf("project2"))
    taskManager.addTask("Task 3", projects = listOf("project1", "project2"))

    val filteredTasks = taskManager.filterTasks { "project1" in it.projects }
    assertEquals(2, filteredTasks.size)
    assertEquals("Task 1", filteredTasks[0].description)
    assertEquals("Task 3", filteredTasks[1].description)
  }

  @Test
  fun testFilterTasksByContext() {
    taskManager.addTask("Task 1", contexts = listOf("context1"))
    taskManager.addTask("Task 2", contexts = listOf("context2"))
    taskManager.addTask("Task 3", contexts = listOf("context1", "context2"))

    val filteredTasks = taskManager.filterTasks { "context2" in it.contexts }
    assertEquals(2, filteredTasks.size)
    assertEquals("Task 2", filteredTasks[0].description)
    assertEquals("Task 3", filteredTasks[1].description)
  }

  @Test
  fun testFilterTasksByCompletionStatus() {
    taskManager.addTask("Task 1")
    taskManager.addTask("Task 2")
    taskManager.completeTask(1)

    val filteredTasks = taskManager.filterTasks { it.isCompleted }
    assertEquals(1, filteredTasks.size)
    assertEquals("Task 1", filteredTasks[0].description)
  }

  @Test
  fun testPersistence() {
    // Add tasks using the first instance of TaskManager
    taskManager.addTask("Task 1")
    taskManager.addTask("Task 2", priority = 'B')

    // Create a new instance of TaskManager, which should load from the same file
    val taskManager2 = TaskManager(todoFile.toString())

    // Verify that the tasks are loaded correctly
    val tasks = taskManager2.listTasks()
    assertEquals(2, tasks.size)
    assertEquals("Task 1", tasks[0].description)
    assertEquals("Task 2", tasks[1].description)

    // Modify tasks using the second instance
    taskManager2.completeTask(1)
    taskManager2.deleteTask(2)

    // Create another instance to verify that changes are saved
    val taskManager3 = TaskManager(todoFile.toString())
    val tasks2 = taskManager3.listTasks()
    assertEquals(1, tasks2.size)
    assertTrue(tasks2[0].isCompleted)
  }

  @Test
  fun testFileCreation() {
    // Assert that the file doesn't exist initially
    assertFalse(Files.exists(todoFile))

    // Add a task, which should create the file
    taskManager.addTask("Test Task")

    // Assert that the file now exists
    assertTrue(Files.exists(todoFile))
  }
}
