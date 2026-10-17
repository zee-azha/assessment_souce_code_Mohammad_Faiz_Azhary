# GitHub User Finder & Detail App 📱

A modern Android application for searching GitHub users, viewing detailed profiles, repositories, followers, and following lists. Built with **Kotlin**, **Jetpack Compose**, **Hilt**, **Retrofit**, and **Room Database**.

---

## 🚀 Features

- 🔍 **User Search**: Search for GitHub users in real-time.
- 👤 **User Details**: View user profiles including avatar, bio, company, location, repository count, followers, and following.
- 📁 **Repositories, Followers & Following**: Tabbed navigation to explore a user's repositories and social graph.
- 💾 **Offline Caching**: Room database integration caches search results and profile data for offline access.
- 🐛 **Network Inspection**: Built-in [Chucker](https://github.com/ChuckerTeam/chucker) inspector for monitoring network requests in real-time.
- 🎨 **Modern Compose UI**: Responsive UI built with Material 3 design guidelines and smooth navigation.

---

## 🛠️ Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3) + Compose Navigation
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture Principles
- **Dependency Injection**: [Dagger Hilt](https://dagger.dev/hilt/)
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) + [OkHttp 3](https://square.github.io/okhttp/) + Gson Converter
- **Local Database**: [Room Persistence Library](https://developer.android.com/training/data-storage/room) + KSP
- **Async & Reactive Programming**: Kotlin Coroutines & StateFlow
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/)
- **HTTP Inspection**: [Chucker Inspector](https://github.com/ChuckerTeam/chucker)
- **Unit Testing**: JUnit4 & Coroutines Test

---

## 📋 Prerequisites

Before building and running the project, ensure you have:

- **Android Studio**: Ladybug / Jellyfish or newer (2024.1+)
- **JDK**: JDK 17 or JDK 21
- **Android SDK**:
  - `compileSdk`: 35
  - `minSdk`: 24 (Android 7.0)
  - `targetSdk`: 35
- **Device/Emulator**: Android device or emulator running API 24 or higher.

---

## 🔑 GitHub API Key / Personal Access Token Configuration

GitHub REST API requests are rate-limited. Using an API Key (Personal Access Token) increases your rate limit from 60 requests/hour to 5,000 requests/hour.

Anda memiliki **2 pilihan** untuk konfigurasi API Key:

### Option 1: Menggunakan Token Bawaan di Gradle (Default)
Project ini sudah dilengkapi dengan API Key default di dalam `app/build.gradle`:
```groovy
```

Anda **tidak perlu mengubah apa pun** jika ingin langsung mencoba menjalankan aplikasi.

### Option 2: Menggunakan API Key / Token Milik Sendiri

Jika Anda ingin memakai GitHub Personal Access Token milik Anda sendiri:

1. Buat token di GitHub via [GitHub Settings > Developer Settings > Personal Access Tokens](https://github.com/settings/tokens).
2. Buka file `app/build.gradle`.
3. Ganti nilai `API_KEY` dengan token milik Anda:
   ```groovy
   buildConfigField "String", "API_KEY", "\"YOUR_PERSONAL_ACCESS_TOKEN_HERE\""
   ```
4. Jalankan **Sync Project with Gradle Files** di Android Studio (`File > Sync Project with Gradle Files`).

---

## 🏗️ How to Build and Run

### Method 1: Using Android Studio (GUI)

1. **Clone or Download** repository ini:
   ```bash
   git clone https://github.com/your-username/assessment_source_code_faiz.git
   cd assessment_source_code_faiz
   ```
2. Buka Android Studio, pilih **Open** lalu pilih folder proyek `assessment_source_code_faiz`.
3. Biarkan Android Studio menyelesaikan proses **Gradle Sync** dan mengunduh semua dependency.
4. Hubungkan Android Physical Device (aktifkan *USB Debugging*) atau jalankan Android Emulator.
5. Pada toolbar atas, pastikan modul `app` terpilih, lalu klik ikon **Run** ▶️ (atau tekan `Shift + F10` / `Ctrl + R`).

---

### Method 2: Using Command Line (Terminal / Gradle Wrapper)

Anda juga bisa melakukan build dan instalasi via command line menggunakan Gradle Wrapper bawaan:

#### 1. Build Debug APK
- **Windows (Command Prompt / PowerShell)**:
  ```cmd
  gradlew.bat assembleDebug
  ```
- **macOS / Linux**:
  ```bash
  ./gradlew assembleDebug
  ```
*Output APK akan berada di directory: `app/build/outputs/apk/debug/app-debug.apk`*

#### 2. Install and Run langsung ke Perangkat Terhubung
Pastikan emulator atau perangkat sudah terhubung (`adb devices`).

- **Windows**:
  ```cmd
  gradlew.bat installDebug
  ```
- **macOS / Linux**:
  ```bash
  ./gradlew installDebug
  ```

#### 3. Run Unit Tests
Untuk menjalankan unit test dalam proyek:

- **Windows**:
  ```cmd
  gradlew.bat test
  ```
- **macOS / Linux**:
  ```bash
  ./gradlew test
  ```

---

## 📁 Project Structure

```text
com.example.assessment_souce_code_faiz/
├── api/                  # Retrofit API service interface
├── data/                 # Data models, DTOs, Room DAO, and Repository implementations
│   ├── local/            # Room Database, DAO, and Entities
│   └── repository/       # UserRepositoryImpl with offline fallback logic
├── di/                   # Hilt Dependency Injection modules (NetworkModule, DatabaseModule, RepositoryModule)
├── domain/               # Domain layer models and repository interfaces
│   ├── model/            # Domain entities (User, Repository)
│   └── repository/       # UserRepository interface
├── ui/                   # Jetpack Compose UI Screens and Components
│   ├── screen/           # SplashScreen, MainScreen, DetailScreen
│   └── theme/            # Theme, Color, Type definitions
├── viewmodel/            # ViewModels (MainViewModel, DetailsViewModels, FollowsViewModel)
├── Constants.kt          # Global constants (BASE_URL, API_KEY)
├── MainActivity.kt       # Activity entry point with Navigation Compose
└── MyApplication.kt      # Application class annotated with @HiltAndroidApp
```

---

## 📄 License

This project is created for technical assessment purposes.
