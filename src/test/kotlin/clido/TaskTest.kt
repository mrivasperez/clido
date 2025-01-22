package clido

import java.time.LocalDate
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TaskTest {

  @Test
  fun testFromLineBasic() {
    val line = "Buy groceries +GroceryShopping @Home"
    val task = Task.fromLine(line)

    assertEquals("Buy groceries", task.description)
    assertFalse(task.isCompleted)
    assertNull(task.priority)
    assertNull(task.creationDate)
    assertNull(task.completionDate)
    assertEquals(listOf("GroceryShopping"), task.projects)
    assertEquals(listOf("Home"), task.contexts)
  }

  @Test
  fun testFromLineWithPriorityAndDates() {
    val line = "x 2023-10-28 (A) 2023-10-26 Complete report +ProjectX @Work"
    val task = Task.fromLine(line)

    assertEquals("Complete report", task.description)
    assertTrue(task.isCompleted)
    assertEquals('A', task.priority)
    assertEquals(LocalDate.of(2023, 10, 26), task.creationDate)
    assertEquals(LocalDate.of(2023, 10, 28), task.completionDate)
    assertEquals(listOf("ProjectX"), task.projects)
    assertEquals(listOf("Work"), task.contexts)
  }

  @Test
  fun testFromLineWithDueDate() {
    val line = "Submit proposal due:2023-11-15 +ProjectY @Office"
    val task = Task.fromLine(line)

    assertEquals("Submit proposal", task.description)
    assertFalse(task.isCompleted)
    assertNull(task.priority)
    assertNull(task.creationDate)
    assertNull(task.completionDate)
    assertEquals(listOf("ProjectY"), task.projects)
    assertEquals(listOf("Office"), task.contexts)
    assertEquals(LocalDate.of(2023, 11, 15), task.dueDate)
  }

  @Test
  fun testFromLineInvalidDate() {
    val line = "x 2023-10-AA (A) Invalid date +ProjectX @Work"
    val task = Task.fromLine(line)

    assertEquals("x 2023-10-AA (A) Invalid date +ProjectX @Work", task.description)
    assertFalse(task.isCompleted) // Should be false due to invalid date
    assertNull(task.priority)
    assertNull(task.creationDate)
    assertNull(task.completionDate)
    assertEquals(listOf("ProjectX"), task.projects)
    assertEquals(listOf("Work"), task.contexts)
  }

  @Test
  fun testToStringBasic() {
    val task =
            Task(
                    description = "Buy groceries",
                    projects = listOf("GroceryShopping"),
                    contexts = listOf("Home")
            )
    val line = task.toString()

    assertEquals("Buy groceries +GroceryShopping @Home", line)
  }

  @Test
  fun testToStringWithPriorityAndDates() {
    val task =
            Task(
                    description = "Complete report",
                    isCompleted = true,
                    priority = 'A',
                    creationDate = LocalDate.of(2023, 10, 26),
                    completionDate = LocalDate.of(2023, 10, 28),
                    projects = listOf("ProjectX"),
                    contexts = listOf("Work")
            )
    val line = task.toString()

    assertEquals("x 2023-10-28 (A) 2023-10-26 Complete report +ProjectX @Work", line)
  }

  @Test
  fun testToStringWithDueDate() {
    val task =
            Task(
                    description = "Submit proposal",
                    projects = listOf("ProjectY"),
                    contexts = listOf("Office"),
                    dueDate = LocalDate.of(2023, 11, 15)
            )
    val line = task.toString()

    assertEquals("Submit proposal +ProjectY @Office due:2023-11-15", line)
  }
}
