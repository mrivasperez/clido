package clido

import java.time.LocalDate

class TaskManager(private val filepath: String) {
  private var tasks: MutableList<Task> = mutableListOf()

  init {
    loadTasks()
  }

  /** Loads tasks from the todo.txt file using the Parser. */
  private fun loadTasks() {
    tasks = Parser.readFile(filepath).toMutableList()
  }

  /** Saves tasks to the todo.txt file using the Parser. */
  private fun saveTasks() {
    Parser.writeFile(filepath, tasks)
  }

  /**
   * Adds a new task to the list and saves the updated list to the file.
   *
   * @param description The description of the task.
   * @param priority The priority of the task (optional).
   * @param creationDate The creation date of the task (optional, defaults to today).
   * @param projects A list of projects associated with the task (optional).
   * @param contexts A list of contexts associated with the task (optional).
   * @param dueDate The due date of the task (optional).
   */
  fun addTask(
          description: String,
          priority: Char? = null,
          creationDate: LocalDate? = LocalDate.now(),
          projects: List<String> = emptyList(),
          contexts: List<String> = emptyList(),
          dueDate: LocalDate? = null
  ) {
    val task = Task(description, false, priority, creationDate, null, projects, contexts, dueDate)
    tasks.add(task)
    saveTasks()
  }

  /**
   * Deletes a task from the list based on its index and saves the updated list to the file.
   *
   * @param index The index of the task to delete (1-based).
   * @throws IndexOutOfBoundsException If the index is out of range.
   */
  fun deleteTask(index: Int) {
    if (index !in 1..tasks.size) {
      throw IndexOutOfBoundsException("Invalid task index: $index")
    }
    tasks.removeAt(index - 1)
    saveTasks()
  }

  /**
   * Marks a task as complete based on its index, updates its completion date, and saves the updated
   * list to the file.
   *
   * @param index The index of the task to mark as complete (1-based).
   * @throws IndexOutOfBoundsException If the index is out of range.
   */
  fun completeTask(index: Int) {
    if (index !in 1..tasks.size) {
      throw IndexOutOfBoundsException("Invalid task index: $index")
    }
    val task = tasks[index - 1]
    tasks[index - 1] = task.copy(isCompleted = true, completionDate = LocalDate.now())
    saveTasks()
  }

  /**
   * Lists all tasks, sorted by creation date (oldest to newest) and priority.
   *
   * @return A list of all tasks.
   */
  fun listTasks(): List<Task> {
    return tasks.sortedWith(
            compareBy<Task> { it.creationDate }.thenBy { it.priority }.thenBy { it.description }
    )
  }

  /**
   * Filters tasks based on a provided filter function.
   *
   * @param filter The filter function to apply.
   * @return A list of tasks that match the filter.
   */
  fun filterTasks(filter: (Task) -> Boolean): List<Task> {
    return tasks.filter(filter)
  }
}
