# ♟️ Chess Clock - Professional Android Chess Timer

<div align="center">

![Chess Clock](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Material Design 3](https://img.shields.io/badge/Material%20Design%203-6750A4?style=for-the-badge&logo=materialdesign&logoColor=white)
![Min API](https://img.shields.io/badge/Min%20API-24%2B-brightgreen?style=for-the-badge)

*🎯 A stunning, professional-grade chess clock for Android with Material Design 3*

[📱 Download APK](#) • [📖 Features](#-features) • [🚀 Getting Started](#-getting-started) • [📸 Screenshots](#-screenshots)

</div>

---

## ✨ Features

### 🎮 Core Chess Clock Functionality
<div align="center">

| Feature | Description |
|---------|-------------|
| 🎯 **Dual Timers** | Separate countdown timers for White & Black players |
| 🔄 **Smart Switching** | Tap your side to stop your timer and start opponent's |
| ⏱️ **Time Controls** | 5min, 10min, 15min, 30min, 1hr presets + custom time |
| 📊 **Precision** | 100ms accuracy with tenths display (MM:SS.t) |

</div>

### 🎨 Beautiful Design & UX
<div align="center">

| Design Element | Implementation |
|----------------|----------------|
| 🌙 **Nordic Theme** | Elegant dark theme with carefully curated colors |
| 📱 **Full Screen** | Immersive, distraction-free chess experience |
| ✨ **Animations** | Smooth card transitions and visual feedback |
| 🎭 **Material 3** | Modern UI with cards, elevation, and micro-interactions |

</div>

### 🔊 Audio & Haptics
<div align="center">

| Audio Feature | Experience |
|---------------|------------|
| 🔊 **Smart Sounds** | Different tones for warnings, critical time, game end |
| 📳 **Haptic Feedback** | Vibration for turn switches and important events |
| ⚙️ **Customizable** | Toggle sounds, vibrations, and low-time warnings |

</div>

### ⚙️ Advanced Settings
- 🎛️ **Time Management**: Preset selection + custom time picker
- 🔊 **Audio Controls**: Individual toggles for all sound types
- 🎨 **Theme Options**: Dark/Light theme support
- 🔋 **Power Management**: Smart wake lock for uninterrupted play

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 24+ (Android 7.0)
- Kotlin 1.9.10+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/kreggscode/chess-clock.git
   cd chess-clock
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned directory

3. **Build & Run**
   - Wait for Gradle sync to complete
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon)

---

## 📱 How to Use

<div align="center">

### Quick Start Guide

| Step | Action | Description |
|------|--------|-------------|
| 1️⃣ | **Set Time** | Choose preset (5min, 10min, etc.) or custom time |
| 2️⃣ | **Start Game** | Tap "Start Game" or tap either player area |
| 3️⃣ | **Play Chess** | Complete your move → tap your timer to switch turns |
| 4️⃣ | **Game Over** | Winner announced when time expires |

</div>

### Advanced Features
- **⏸️ Pause/Resume**: Use the pause button for breaks
- **🔄 Reset**: Confirmation dialog prevents accidental resets
- **⚙️ Settings**: Access via gear icon in top-left corner
- **🎵 Audio**: Customize all sound and vibration preferences

---

## 🏗️ Technical Architecture

```kotlin
📁 Project Structure
chess-clock/
├── 📱 app/src/main/
│   ├── 📋 AndroidManifest.xml      # Permissions & app config
│   ├── 🏗️ java/com/example/chessclock/
│   │   ├── 🎮 MainActivity.kt       # Core game logic & UI
│   │   ├── ⚙️ SettingsActivity.kt   # Settings management
│   │   └── 🔊 SoundManager.kt       # Audio system
│   └── 🎨 res/                      # Resources
│       ├── 📱 layout/               # XML layouts
│       ├── 🎨 values/               # Colors, strings, themes
│       └── 📸 drawable/             # Icons & graphics
├── 📋 build.gradle                  # App dependencies
├── ⚙️ settings.gradle               # Project settings
└── 🚫 .gitignore                    # Git ignore rules
```

### 🛠️ Technology Stack

<div align="center">

| Component | Technology | Purpose |
|-----------|------------|---------|
| **Language** | Kotlin 1.9.10+ | Modern Android development |
| **Architecture** | MVVM Pattern | Clean code structure |
| **UI Framework** | Material Design 3 | Beautiful, consistent UI |
| **Audio System** | SoundPool + ToneGenerator | Reliable cross-device audio |
| **Persistence** | SharedPreferences | Settings storage |
| **Threading** | CountDownTimer | Precise timing |
| **Build System** | Gradle 8.2+ | Dependency management |

</div>

### 🔐 Permissions

| Permission | Purpose | Android Version |
|------------|---------|-----------------|
| `WAKE_LOCK` | Keep screen on during games | All versions |
| `VIBRATE` | Haptic feedback | Auto-granted 6.0+ |

---

## 📸 Screenshots

<div align="center">

*Coming soon - Add beautiful screenshots of your app here*

</div>

---

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Material Design 3** for the beautiful design system
- **Nordic Color Palette** inspiration
- **Android Developer Community** for best practices
- **Chess Community** for the inspiration

---

<div align="center">

**Made with ❤️ for chess players worldwide**

⭐ Star this repo if you find it useful! ⭐

[🐛 Report Issues](https://github.com/kreggscode/chess-clock/issues) • [💡 Request Features](https://github.com/kreggscode/chess-clock/discussions)

</div>
