# 🗃️ Inventory Management System (Java Swing)

A desktop-based Inventory Management System built with **Java Swing** that allows users to manage items through a graphical user interface with full CRUD functionality, advanced search filters, and undo/redo support. All inventory data is persisted locally using Java's object serialization mechanism.

---

## ✨ Features

✅ Insert new inventory items with details like ID, name, category, price, and quantity  
✅ Update or delete existing items  
✅ Search items by:
- ID
- Category
- Price range
- Quantity (low stock filter)

✅ View all inventory items in a sortable, scrollable table  
✅ **Undo/Redo** support for insert, update, and delete operations  
✅ Persistent storage using `data.dat` file (via `ObjectOutputStream`)  
✅ Interactive GUI using Java Swing components

---

## 🧰 Technologies Used

- **Java SE 8+**
- **Java Swing** (GUI)
- **JTable** for data display
- **TreeMap** and **Stack** for structured storage and undo/redo functionality
- **Object Serialization** for data persistence

---

## 🖥️ GUI Overview

The GUI includes:
- Action buttons for Insert, Update, Delete, Undo, Redo
- Search buttons for ID, Category, Price Range, and Quantity
- A large inventory table showing all data with live updates
- Pop-up dialogs for form inputs and feedback

---

Running the app 
Go to out/artifacts/InventoryManagementSystem_jar
Double click on InventoryManagementSystem.jar file

NOTE: You must have java 23 or higher version installed to run the app 

