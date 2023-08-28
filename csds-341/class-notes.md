# CSDS 341: Database Systems

## Syllabus Notes

- Dianne Foreback (drf68)
- Office Hours 17:00-18:00; [Appointments available](https://bit.ly/3OTgjgC)

## Class Notes

### Module 1 Lecture 1

- A **database** is an organized collection of structured information, or data,
typically stored electronically in a computer system.
- A **database management system (DBMS)** is a set of interrelated data and a
set of programs to access those data.
  - The data and the applications associated with them are referred to as a
    **database system**, often shortened to **database**.
  - Examples: MySQL, Oracle, Microsoft SQL Server, PostgreSQL, MongoDB
- Advantages of DBMS: reduces data redundancy, removes data inconsistency,
controls data access, isolates data, ensures data integrity, atomize data,
enables concurrent access, and resolves security problems.
- 4 categories of data models
  - Relational model: uses a collection of tables to represent both data and the
  relationships among those data.
  - Entity-relationship model: mainly used for database design
  - Object-based data model: object-oriented and object-relational
  - Semi-structured data model: XML, JSON
- Levels of abstraction
  - **Physical level** describes how a record (data entry) is stored.
  - **Logical level** describes data stored in database, and the relationships
  among the data.
  - **View level** is where application programs hide details of data types.
  Views can also hide information for security purposes.