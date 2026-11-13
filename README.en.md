# Biblioteca

[Versão em português](README.md)

A desktop library management system built with **Java 25**, **Swing**, **JDBC**, and **MySQL**. The application separates responsibilities using an MVC-inspired structure and uses Maven for dependency management, builds, and tests.

> Academic project under active development. Review the documentation before configuring the database or running the application.

## Features

- Create and manage authors and categories.
- Create and manage books and physical copies.
- Register and manage library members.
- Record loans and returns.
- View active and overdue loans.
- Generate reports filtered by date range and status.
- Validate input data, including Brazilian CPF and ISBN.

## Technology stack

| Technology | Purpose |
|---|---|
| Java 25 | Language and platform |
| Swing + FlatLaf | Desktop UI |
| JDBC | Relational database access |
| MySQL 8.0.16+ | Database |
| Maven | Dependencies and build |
| JUnit 5 | Automated tests |

## Architecture

Source code is located in `src/main/java/br/com/biblioteca`:

- `model`: domain entities and objects.
- `view`: Swing screens and components.
- `controller`: validation and use-case coordination.
- `dao`: JDBC persistence and queries.
- `connection`: database connection configuration.
- `util`: helper functions and validation.

The general flow is **View → Controller → DAO → MySQL**. See [docs/architecture.md](docs/architecture.md).

## Requirements

- JDK 25 or later.
- Maven 3.9 or later.
- MySQL Server 8.0.16 or later.
- Git (optional, for cloning).

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Lennox-code-cell/biblioteca.git
cd biblioteca
```

### 2. Create the database

The initial schema is defined in `sql/biblioteca.sql`. It creates the `biblioteca` database and its tables, but **does not insert demo records**.

For a fresh installation:

```bash
mysql -u YOUR_USER -p < sql/biblioteca.sql
```

If the database already exists and contains data, do not run this script without reviewing it and taking a backup. See [docs/database.md](docs/database.md).

### 3. Configure credentials

The application reads these environment variables:

| Variable | Required | Description |
|---|---:|---|
| `DB_USER` | Yes | MySQL username |
| `DB_PASSWORD` | Yes | MySQL password |
| `DB_URL` | No | JDBC URL; defaults to `jdbc:mysql://127.0.0.1:3306/biblioteca` |

PowerShell example (the password is prompted instead of being written in the command):

```powershell
$env:DB_URL = "jdbc:mysql://127.0.0.1:3306/biblioteca"
$env:DB_USER = "biblioteca_app"
$secure = Read-Host "MySQL password" -AsSecureString
$env:DB_PASSWORD = [System.Net.NetworkCredential]::new("", $secure).Password
```

Never commit passwords, `.env` files, real database dumps, or personal data. Use a database account with limited permissions on the application schema.

## Run

From the project root:

```bash
mvn clean test
mvn clean package
mvn exec:java -Dexec.mainClass=br.com.biblioteca.Main
```

On Windows, you can also use `build.cmd` and `run.bat`.

## Project structure

```text
biblioteca/
├── docs/
├── sql/
├── src/
│   ├── main/java/br/com/biblioteca/
│   └── test/java/br/com/biblioteca/
├── .github/workflows/
├── pom.xml
├── README.md
└── README.en.md
```

## Tests and continuous integration

Run `mvn clean test` locally. GitHub Actions also runs the test suite on pushes and pull requests targeting the `main` branch.

## Documentation

- [Architecture](docs/architecture.md)
- [Database](docs/database.md)
- [Portuguese README](README.md)

## Security

If a real credential has ever been committed, rotate it immediately. Removing a password from the current source code does not erase previous copies from Git history. History rewriting requires planning, backups, and coordination with all clones; it has not been performed as part of this repository organization.

## Status

This project is under development. Features, documentation, and tests may change as new versions are implemented.
