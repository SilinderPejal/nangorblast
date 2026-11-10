# **Dokumentasi**
File dokumentasi pribadi kelompok yang berisi tutorial, struktur projek, dan penjelasan masing-masing file java. Dokumentasi ini akan diupdate secara berkala

# Daftar Isi
- [Tutorial](#tutorial)
  - [1. Gradle](#1-gradle)
- [Struktur Projek](#struktur-projek)
  - [1. Direktori Keseluruhan](#1-direktori-keseluruhan)
  - [2. Penjelasan](#2-penjelasan)
    - [Exception](#exception)
    - [Interface](#interface)
    - [Game](#game)
    - [Entities](#entities)
    - [Environment](#environment)
    - [System](#system)
    - [Display](#display)
    - [Event](#event)
    - [State](#state)

# **Tutorial**
## **1. Gradle**
| Syntax | Fungsi |
|---|---|
| `.\gradlew.bat build` | Compile dan build projek |
| `.\gradlew.bat run` | Menjalankan hasil compile |

# **Struktur Projek**
Struktur buat direktori file dipisah berdasarkan kegunaan
## **1. Direktori keseluruhan**
```
src/
└── main/
    ├── java/
    │   └── nangor_blast/
    │       ├── exception/
    │       ├── game/
    │       │   ├── entities/
    │       │   ├── environment/
    │       │   └── system/
    │       │       ├── display/
    │       │       ├── event/
    │       │       └── state/
    │       └── interface/
    │
    └── assets/
        ├── audio/
        ├── image/
        └── font/ 
```
## **2. Penjelasan**
### **Exception**
[Exception](exception/) folder buat file java yang berfungsi sebagai exception

### **Interface**
[Interface](interfaces/) folder buat file java yang berfungsi sebagai interface

### **Game**
[Game](game/) folder buat meletakkan logic dari game

### **Entites**
[Entites](game/entities/) folder buat objek yang terlibat dan memiliki perilaku khusus di dalam game (seperti player, bomb, enemy, dan lain-lain)

### **Environment**
[Environment](game/environment/) folder buat lingkungan di dalam game yang memengaruhi pergerakan maupun interaksi entity

### **System**
[System](game/system/) folder buat komponen yang berfungsi sebagai pengelola mekanisme dan proses permainan

### **Display**
[Display](game/system/display) folder buat implementasi tampilan permainan
- CLI: Implementasi tampilan berbasis Command Line Interface menggunakan Lanterna
- GUI: Implementasi tampilan berbasis Graphical User Interface

### **Event**
[Event](game/system/event) folder buat sistem event yang berfungsi untuk memberi tahu komponen lain bahwa sesuatu telah terjadi di dalam game

### **State**
[State](game/system/state/) folder buat implementasi kondisi atau fase permainan


