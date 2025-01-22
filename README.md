```markdown

```

# Clido: A Command-Line Todo List Manager

Clido is a simple yet powerful command-line application for managing your todo list. It's built in Kotlin and uses the [Clikt](https://github.com/ajalt/clikt) library for parsing command-line arguments. Clido helps you keep track of your tasks, organize them by projects and contexts, set priorities, and mark them as complete, all from the comfort of your terminal.

## Features

- **Add tasks:** Easily add new tasks with descriptions, priorities, projects, contexts, and due dates.
- **List tasks:** View your tasks in a clean, organized list.
- **Filter tasks:** Filter tasks by project, context, or completion status.
- **Mark tasks as complete:** Update tasks as you complete them.
- **Delete tasks:** Remove tasks that are no longer needed.
- **Prioritize tasks:** Assign priorities to your tasks to focus on what's most important.
- **Plain text storage:** Your todo list is stored in a simple `todo.txt` file, making it easy to manage and back up.

## Prerequisites

- **Java Development Kit (JDK):** Clido requires JDK 11 or later.
- **Gradle:** Gradle is used as the build tool for this project.

## Building the Project

1. **Clone the repository:**

   ```bash
   git clone https://github.com/mrivasperez/clido.git
   cd clido
   ```

2. **Build the project using Gradle:**

   ```bash
   gradle clean build
   ```

   This will create an executable JAR file in the `build/libs` directory.

## Usage

Clido is run from the command line using the Gradle wrapper. The general syntax is:

```bash
gradle run --args="<command> [options] [arguments]"
```

### Commands

Here are the available commands:

#### `add`

Adds a new task to your todo list.

**Usage:**

```bash
gradle run --args="add <description> [-p priority] [-pr project] [-c context] [-d dueDate]"
```

**Arguments:**

- `<description>`: The description of the task (required). Enclose in quotes if it contains spaces.

**Options:**

- `-p <priority>`: Sets the priority of the task (A-Z, where A is the highest priority).
- `-pr <project>`: Assigns the task to a project.
- `-c <context>`: Assigns the task to a context.
- `-d <dueDate>`: Sets the due date for the task in YYYY-MM-DD format.

**Example:**

```bash
gradle run --args="add \"Write report\" -p A -pr ProjectX -c Work -d 2024-03-15"
```

#### `list`

Lists your tasks, with options to filter the output.

**Usage:**

```bash
gradle run --args="list [-p project] [-c context] [-d]"
```

**Options:**

- `-p <project>`: Filters tasks by the specified project.
- `-c <context>`: Filters tasks by the specified context.
- `-d`: Shows only completed tasks.

**Examples:**

```bash
gradle run --args="list"  # List all tasks
gradle run --args="list -p ProjectX"  # List tasks in ProjectX
gradle run --args="list -c Work -d"  # List completed tasks with context @Work
```

#### `complete`

Marks a task as complete.

**Usage:**

```bash
gradle run --args="complete <taskIndex>"
```

**Arguments:**

- `<taskIndex>`: The index of the task to mark as complete (as shown in the `list` command output).

**Example:**

```bash
gradle run --args="complete 3"  # Marks the third task as complete
```

#### `delete`

Deletes a task from your todo list.

**Usage:**

```bash
gradle run --args="delete <taskIndex>"
```

**Arguments:**

- `<taskIndex>`: The index of the task to delete.

**Example:**

```bash
gradle run --args="delete 1" # Deletes the first task
```

## `todo.txt` File

Clido stores your tasks in a plain text file named `todo.txt` in the root directory of the project. The file format follows the [Todo.txt format specification](http://todotxt.org/). You can manually edit this file if you wish, but it's generally recommended to manage tasks using the Clido commands.

## Contributing

Contributions to Clido are welcome! If you find a bug or have a feature request, please open an issue on the GitHub repository. If you'd like to contribute code, please fork the repository and submit a pull request.

## License

This project is licensed under the [MIT License](LICENSE) - see the `LICENSE` file for details.
