# Clothing Store Stock Management System

This project is a stock management system for a clothing store.

## Unix-like Environment Setup

To set up the project in a Unix-like environment, run the following commands in a terminal:

```bash
# install dependencies
./gradlew --refresh-dependencies

# create and populate the working database
mysql -u root < sql/create_clothingstore_database.sql
mysql -u root < sql/populate_clothingstore_tables.sql

# create and populate the test database
mysql -u root < sql/create_test_database.sql
mysql -u root < sql/populate_test_tables.sql
```

## Windows Environment Setup

To set up the project in a Windows environment, run the following commands in Command Prompt or PowerShell:

```powershell
# install dependencies
gradlew.bat --refresh-dependencies

# create and populate the working database
mysql -u root < sql\create_clothingstore_database.sql
mysql -u root < sql\populate_clothingstore_tables.sql

# create and populate the test database
mysql -u root < sql\create_test_database.sql
mysql -u root < sql\populate_test_tables.sql
```

## Building and Running

To build and run the project:

```bash
# Unix
./gradlew build run

# Windows
gradlew.bat build run
```

To build and run the tests:

```bash
# Unix
./gradlew build test

# Windows
gradlew.bat build test
```
