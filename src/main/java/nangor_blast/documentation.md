# **Dokumentasi**
File dokumentasi pribadi kelompok yang berisi tutorial, struktur projek, dan penjelasan masing-masing file java (kalau sempat). Dokumentasi ini akan diupdate secara berkala.

**Aturan umum:**
1. Naming convention pakai snake_case
2. Kelompokkan kode berdasarkan kegunaan ke dalam folder yang sama
3. Kalau sentuh kode orang lain harap lapor ke grup
4. Selalu Kabarin di grup tiap ngepush sesuatu 

Uhh karena aku malas ngeformat mdnya, untuk sementara arsitektur kodenya aku jelasin di sini:
1. game menerima input user dari input_handler (ini di cli_input_handler)
2. game logic menentukan input bakal ngapain (uh buat detailnya game logic itu buat mengelola keseluruhan mekanisme/perilaku dari gamenya dan diatur di folder game (entities, environment, system))
3. game state nanti menyimpan kondisi game secara real time biar bisa jadi sumber informasi bagi bagian lain untuk mengetahui kondisi game 
4. event buat ngerepresentasiin sama ngasih tahu kejadian yang ada di game ke komponen lain
5. poin 2, 3, 4 saling interaksi buat ngasih display berdasarkan kondisi game ke user (yah intinya buat ngerender ada di folder display semua) 
Buat contoh biar ada gambaran:
anggap skenarionya player naro bomb dengan nekan spasi
1. input handler nerima inputnya terus diterusin ke game logic
2. game logic yang nentuin si bom ini bakal ngapain
3. game state ngabarin kalau game lagi dimainin
4. game event ngasih tahu kalau bom udah berhasil ditempatkan jadinya komponen lain bisa merespons event tersebut, kayak mainin audio atau ngasih efek visual
5. yes
maaf gak jelas, pertama kali bikin game di java

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


# Interface
Jadi buat interfacenya ada:
1. entity (yg kegolong entity objek yg punya perilaku khusus in game, berarti dari f01-f06 itu entity)
2. moveable (enemy dan player)
3. damageable (player,  enemy, destructible block, reinforced block)
4. playable (player 1, player 2)
5. collectible (powerup)
6. explodable (bomb)

Terus ntar ada abstract class game_entity yang bakal jadi parent buat masing-masing entity in game 

Terus abstract class buat entity yg punya lebih dari 1 jenis kayak player, enemy, block, bomb, power-up

Contoh penerapan:
1. game_entity implement entity
2. enemy extend game_entity implement moveable, damageable
3. normal_enemy extend enemy
4. Boss_enemy extend enemy

