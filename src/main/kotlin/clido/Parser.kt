package clido

import java.io.File
import java.io.IOException

class Parser {
  companion object {
    /**
     * Reads a todo.txt file and returns a list of Task objects.
     *
     * @param filepath The path to the todo.txt file.
     * @return A list of Task objects parsed from the file.
     * @throws IOException If there is an error reading the file.
     */
    fun readFile(filepath: String): List<Task> {
      val tasks = mutableListOf<Task>()
      val file = File(filepath)

      if (!file.exists()) {
        return tasks // Return an empty list if the file doesn't exist
      }

      try {
        file.bufferedReader().useLines { lines ->
          lines.forEach { line -> tasks.add(Task.fromLine(line)) }
        }
      } catch (e: IOException) {
        throw IOException("Error reading file: $filepath", e)
      }

      return tasks
    }

    /**
     * Writes a list of Task objects to a todo.txt file.
     *
     * @param filepath The path to the todo.txt file.
     * @param tasks The list of Task objects to write.
     * @throws IOException If there is an error writing to the file.
     */
    fun writeFile(filepath: String, tasks: List<Task>) {
      val file = File(filepath)

      try {
        file.bufferedWriter().use { writer ->
          tasks.forEach { task ->
            writer.write(task.toString())
            writer.newLine()
          }
        }
      } catch (e: IOException) {
        throw IOException("Error writing to file: $filepath", e)
      }
    }
  }
}
