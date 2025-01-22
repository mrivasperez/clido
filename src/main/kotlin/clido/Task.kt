package clido

import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Task(
        val description: String,
        val isCompleted: Boolean = false,
        val priority: Char? = null,
        val creationDate: LocalDate? = null,
        val completionDate: LocalDate? = null,
        val projects: List<String> = emptyList(),
        val contexts: List<String> = emptyList(),
        val dueDate: LocalDate? = null
) {

  companion object {
    private val DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE

    fun fromLine(line: String): Task {
      // TODO: Implement the parsing logic here
      // ... (See detailed breakdown in the next message)
      throw NotImplementedError("Parsing not implemented yet")
    }
  }

  override fun toString(): String {
    // TODO: Implement formatting logic to convert Task back to todo.txt line
    // ... (We'll work on this after the parsing logic)
    throw NotImplementedError("Task to String formatting not implemented yet")
  }
}
