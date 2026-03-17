# Student Grade Processor

A Java-based application designed to process student grades from CSV files, compute final scores using specific weighting, and generate a formatted output file. This project emphasizes clean Object-Oriented Programming (OOP) principles, defensive programming, and comprehensive unit testing.

## Overview

The `StudentGradeProcessor` automates the task of combining student identity information with their course grades. It handles data validation, calculates weighted final grades, deduplicates entries, and outputs the results sorted by Student ID.

### Key Features
* **File Reading & Parsing:** Reads `names.txt` and `courses.txt` files using robust, error-checked file readers.
* **Data Validation:** Implements defensive programming to catch and report missing data, invalid formats, out-of-bounds grades (0-100), and duplicate records.
* **Grade Calculation:** Computes a final grade where three tests account for 20% each (60% total) and a final exam accounts for 40%.
* **Deduplication:** Automatically ignores duplicate grade submissions for the same student in the same course.
* **Formatted Output:** Writes the processed, sorted results to an output CSV file.
* **Comprehensive Testing:** Includes JUnit tests for all models, readers, writers, and processors to ensure reliability.

## Architecture & OOP Principles

This project is structured around standard OOP pillars:
* **Encapsulation:** Data models (`Student`, `GradeRecord`) use private fields, public getters, and validate their own state upon construction.
* **Inheritance:** `NameFileReader` and `CourseFileReader` extend an abstract `FileReader<T>` base class to share file I/O logic. `GradeRecord` extends the abstract `Course` class.
* **Polymorphism:** Methods like `parseLine()` and `toString()` are overridden across different classes to provide specific implementations.
* **Abstraction:** The `FileReader` hides the boilerplate of `BufferedReader` loops. Interfaces like `GradeCalculator` allow for extensible grading strategies.

## Requirements

* **Java:** JDK 8 or higher
* **Testing:** JUnit 5 (for running tests)

## Getting Started

1. **Input Files:** Place your `names.txt` and `courses.txt` files in the appropriate directory (or update the file paths in `Main.java`).
    * **Names format:** `StudentID,Student Name` (e.g., `123456789,John Doe`)
    * **Courses format:** `StudentID,CourseCode,Test1,Test2,Test3,FinalExam` (e.g., `123456789,CS101,80,85,90,95`)
2. **Run:** Execute the `Main.java` class to process the data.
3. **Output:** A `results.txt` file will be generated containing the calculated final grades.

## Project Structure

* `src/main/java/model/`: Data representation classes (`Student`, `Course`, `GradeRecord`, `ProcessedResult`).
* `src/main/java/reader/`: File parsing logic (`FileReader`, `NameFileReader`, `CourseFileReader`).
* `src/main/java/processor/`: Core logic for matching students and calculating grades (`DataProcessor`).
* `src/main/java/writer/`: Output generation (`OutputFileWriter`).
* `src/main/java/exception/`: Custom exceptions (`DataValidationException`).
* `src/test/java/`: Unit tests mirroring the main package structure.
