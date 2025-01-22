package clido

import java.io.File
import java.nio.file.Path
import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ParserTest {

  @TempDir lateinit var tempDir: Path // JUnit 5 will create a temporary directory for each test

  @Test
  fun testReadWriteFile() {
    // Create some sample Task objects
    val task1 =
            Task(
                    "Task 1", // Description only
                    projects = listOf("project1"),
                    contexts = listOf("context1")
            )
    val task2 =
            Task(
                    "Task 2", // Description only
                    isCompleted = true,
                    priority = 'A',
                    creationDate = LocalDate.of(2023, 10, 26),
                    completionDate = LocalDate.of(2023, 10, 28),
                    projects = listOf("project2"),
                    contexts = listOf("context2")
            )

    val tasks = listOf(task1, task2)

    // Write the tasks to a temporary file
    val tempFile = tempDir.resolve("todo.txt").toFile()
    Parser.writeFile(tempFile.absolutePath, tasks)

    // Read the tasks back from the file
    val readTasks = Parser.readFile(tempFile.absolutePath)

    // Assert that the read tasks are equal to the original tasks
    assertEquals(tasks.size, readTasks.size)
    for (i in tasks.indices) {
      // Compare each field individually
      assertEquals(tasks[i].description, readTasks[i].description)
      assertEquals(tasks[i].isCompleted, readTasks[i].isCompleted)
      assertEquals(tasks[i].priority, readTasks[i].priority)
      assertEquals(tasks[i].creationDate, readTasks[i].creationDate)
      assertEquals(tasks[i].completionDate, readTasks[i].completionDate)
      assertEquals(tasks[i].projects, readTasks[i].projects)
      assertEquals(tasks[i].contexts, readTasks[i].contexts)
      assertEquals(tasks[i].dueDate, readTasks[i].dueDate)
    }
  }

  @Test
  fun testReadFileNonExistent() {
    // Test reading from a non-existent file
    val nonExistentFilePath = tempDir.resolve("nonexistent.txt").toString()
    val readTasks = Parser.readFile(nonExistentFilePath)

    // Assert that the returned list is empty
    assertTrue(readTasks.isEmpty())
  }

  @Test
  fun testWriteFileCreatesFile() {
    // Test that writing to a non-existent file creates the file
    val newFilePath = tempDir.resolve("newfile.txt").toString()
    val task = Task("New Task")

    // Write a task to the new file
    Parser.writeFile(newFilePath, listOf(task))

    // Assert that the file now exists
    assertTrue(File(newFilePath).exists())
  }
}
