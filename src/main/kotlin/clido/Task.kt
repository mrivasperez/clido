package clido

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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
      val parts = line.split(" ")

      var index = 0

      // Check for completion status
      var isCompleted = false
      if (parts[index] == "x") {
        isCompleted = true
        index++
      }

      // Check for completion date
      var completionDate: LocalDate? = null
      if (isCompleted) {
        try {
          completionDate = LocalDate.parse(parts[index], DATE_FORMATTER)
          index++
        } catch (e: DateTimeParseException) {
          // Handle invalid completion date
          isCompleted = false
          index = 0 // Reset index to reprocess from the beginning
        }
      }

      // Check for priority
      var priority: Char? = null
      if (index < parts.size &&
                      parts[index].length == 3 &&
                      parts[index][0] == '(' &&
                      parts[index][2] == ')'
      ) {
        priority = parts[index][1]
        index++
      }

      // Check for creation date
      var creationDate: LocalDate? = null
      try {
        creationDate = LocalDate.parse(parts[index], DATE_FORMATTER)
        index++
      } catch (e: DateTimeParseException) {
        // Handle invalid creation date
      }

      // Check for due date
      var dueDate: LocalDate? = null
      val dueDateMatch = Regex("""due:\d{4}-\d{2}-\d{2}""").find(line)
      if (dueDateMatch != null) {
        dueDate = LocalDate.parse(dueDateMatch.value.substring(4), DATE_FORMATTER)
      }

      // Extract description, projects, and contexts
      val description = StringBuilder()
      val projects = mutableListOf<String>()
      val contexts = mutableListOf<String>()
      for (i in index until parts.size) {
        val word = parts[i]

        if (word.startsWith("+")) {
          projects.add(word.substring(1))
        } else if (word.startsWith("@")) {
          contexts.add(word.substring(1))
        } else if (word != dueDateMatch?.value) {
          description.append(word).append(" ")
        }
      }

      return Task(
              description.trim().toString(),
              isCompleted,
              priority,
              creationDate,
              completionDate,
              projects,
              contexts,
              dueDate
      )
    }
  }

  override fun toString(): String {
    val builder = StringBuilder()

    // 1. Completion Status
    if (isCompleted) {
      builder.append("x ")
    }

    // 2. Completion Date (only if completed)
    if (isCompleted && completionDate != null) {
      builder.append(completionDate.format(DATE_FORMATTER)).append(" ")
    }

    // 3. Priority
    if (priority != null) {
      builder.append("($priority) ")
    }

    // 4. Creation Date
    if (creationDate != null) {
      builder.append(creationDate.format(DATE_FORMATTER)).append(" ")
    }

    // 5. Description
    builder.append(description)

    // 6. Projects
    for (project in projects) {
      builder.append(" +").append(project)
    }

    // 7. Contexts
    for (context in contexts) {
      builder.append(" @").append(context)
    }

    // 8. Due Date
    if (dueDate != null) {
      builder.append(" due:").append(dueDate.format(DATE_FORMATTER))
    }

    return builder.toString().trim()
  }
}
