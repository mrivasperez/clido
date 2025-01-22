package clido

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.split
import com.github.ajalt.clikt.parameters.types.int
import java.time.LocalDate

class Add : CliktCommand() {
  fun help(): String = "Add a new task"

  val description by argument()
  val priority by option("-p", help = "Task priority (A-Z)")
  val creationDate by
          option("-c", help = "Creation date (YYYY-MM-DD)").default(LocalDate.now().toString())
  val projects by option("-P", help = "Project names (comma-separated)").split(",")
  val contexts by option("-C", help = "Context names (comma-separated)").split(",")
  val dueDate by option("-d", help = "Due date (YYYY-MM-DD)")

  override fun run() {
    val parsedCreationDate = LocalDate.parse(creationDate)
    val parsedDueDate = dueDate?.let { LocalDate.parse(it) }

    taskManager.addTask(
            description,
            priority?.firstOrNull(),
            parsedCreationDate,
            projects.orEmpty(),
            contexts.orEmpty(),
            parsedDueDate
    )
    println("Task added: $description")
  }
}

class List : CliktCommand() {
  fun help(): String = "List all tasks"

  val project by option("-p", help = "Filter by project")
  val context by option("-c", help = "Filter by context")
  val completed by option("-d", help = "Show only completed tasks").flag()

  override fun run() {
    var tasks = taskManager.listTasks()

    project?.let { p -> tasks = tasks.filter { task: Task -> p in task.projects } }
    context?.let { c -> tasks = tasks.filter { task: Task -> c in task.contexts } }
    if (completed) {
      tasks = tasks.filter { task: Task -> task.isCompleted }
    }

    if (tasks.isEmpty()) {
      println("No tasks found.")
    } else {
      tasks.forEachIndexed { index: Int, task: Task -> println("${index + 1}. ${task}") }
    }
  }
}

class Done : CliktCommand() {
  fun help(): String = "Mark a task as complete"

  val index by argument().int()

  override fun run() {
    try {
      taskManager.completeTask(index)
      println("Task $index marked as complete.")
    } catch (e: IndexOutOfBoundsException) {
      println("Error: Invalid task index.")
    }
  }
}

class Delete : CliktCommand() {
  fun help(): String = "Delete a task"

  val index by argument().int()

  override fun run() {
    try {
      taskManager.deleteTask(index)
      println("Task $index deleted.")
    } catch (e: IndexOutOfBoundsException) {
      println("Error: Invalid task index.")
    }
  }
}

class CliDo : CliktCommand() {
  fun help(): String = "A simple command-line todo.txt manager"
  fun epilog(): String = "See also: https://github.com/todotxt/todo.txt"
  override fun run() = Unit
}

val filepath = "todo.txt"
val taskManager = TaskManager(filepath)

fun main(args: Array<String>) = CliDo().subcommands(Add(), List(), Done(), Delete()).main(args)
