Act as a Principal Java Engineer designing a Low Level Design (LLD)
solution for a File Storage System (Mini Dropbox).

The solution must be interview-grade, production-inspired, and
demonstrate strong object-oriented design, SOLID principles, and design
patterns.

======================== OBJECTIVE ======================== Design and
implement a File Storage System in Java that supports: - File upload -
File download - File versioning - Sharing permissions - Folder
hierarchy - Consistency guarantees

======================== MANDATORY DESIGN CONSTRAINTS
======================== 1. Apply OOP principles explicitly: -
Encapsulation - Abstraction - Inheritance (only where appropriate) -
Polymorphism

2.  Follow ALL SOLID principles:
    -   SRP: Each class must have one clear responsibility
    -   OCP: New storage strategies or permission types must be added
        without modifying existing code
    -   LSP: Subtypes must be substitutable
    -   ISP: Prefer small, focused interfaces
    -   DIP: Depend on abstractions, not implementations
3.  Use Design Patterns where they naturally fit:
    -   Strategy Pattern → Storage backend (Local, Cloud, Memory)
    -   Command Pattern → File operations (Upload, Delete, Share)
    -   Composite Pattern → Folder hierarchy
    -   Observer Pattern → File change notifications
    -   Facade Pattern → Simplified file storage API
    -   Builder Pattern → File metadata construction
4.  Favor composition over inheritance
5.  Avoid God objects
6.  Avoid static utility-heavy design

======================== FUNCTIONAL REQUIREMENTS
======================== 1. Upload file 2. Download file 3. Create
folder 4. Delete file/folder 5. Maintain file versions 6. Share file
with users 7. Set permissions (read/write) 8. List folder contents

======================== DOMAIN MODEL (EXPECTED)
========================

-   User

    -   userId
    -   name

-   FileSystemNode (abstract)

    -   name
    -   owner
    -   createdTime

-   File extends FileSystemNode

    -   versions
    -   size

-   Folder extends FileSystemNode

    -   children (files/folders)

-   FileVersion

    -   versionId
    -   content
    -   timestamp

-   Permission

    -   user
    -   accessType (READ, WRITE)

-   StorageStrategy (interface)

    -   store()
    -   retrieve()
    -   delete()

-   LocalStorageStrategy

-   CloudStorageStrategy

-   InMemoryStorageStrategy

-   FileCommand (interface)

    -   execute()

-   UploadCommand

-   DeleteCommand

-   ShareCommand

-   FileStorageService

    -   upload()
    -   download()
    -   share()
    -   list()

======================== BUSINESS RULES ======================== - File
names must be unique within a folder - Uploading same file creates new
version - Only owner or permitted user can access file - Deleting folder
deletes all children - Permissions inherited from parent folder

======================== CODING STANDARDS (STRICT)
======================== 1. Follow Java naming conventions 2. Classes
must be cohesive and small 3. No public fields 4. Use final where
applicable 5. Use immutable objects where possible 6. Proper access
modifiers 7. No Lombok, no frameworks, no external libraries 8. Avoid
magic numbers 9. Write clean, readable, interview-friendly code

======================== ERROR HANDLING ======================== - Throw
meaningful custom exceptions - Validate inputs at boundaries - Do NOT
silently ignore errors - Handle permission violations

======================== EXTENSIBILITY EXPECTATIONS
======================== - Adding new storage backend must not modify
existing classes - New permission types should be pluggable - Versioning
logic should evolve independently - Support distributed storage later

======================== DELIVERABLES ======================== 1.
Complete Java implementation with package structure 2. Clear separation
of concerns 3. Minimal but meaningful comments explaining design
decisions 4. A Main class demonstrating: - Creating folders - Uploading
files - Versioning - Sharing files - Listing contents - Downloading file

======================== INTERVIEW EXPECTATION
======================== - Code should be explainable in 20 minutes -
Trade-offs must be obvious - Design must scale from simple to complex

======================== IMPORTANT RULES ======================== - DO
NOT over-engineer - DO NOT add unnecessary patterns - DO NOT include
unit tests unless asked - Output ONLY Java source code

Note: ZIP all the files to a folder.