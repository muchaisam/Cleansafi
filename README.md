# 🧺 CleanSafi - Modern Android Laundry Service App

<div align="center">

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Material Design](https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=material-design&logoColor=white)

**A production-ready, feature-rich Android application showcasing modern app development practices**

[Features](#-features) • [Architecture](#-architecture) • [Tech Stack](#-tech-stack) • [Screenshots](#-screenshots) • [Installation](#-installation)

</div>

---

## 📱 Overview

CleanSafi is a **fully functional laundry service application** built with **100% Kotlin** and **Jetpack Compose**. This portfolio project demonstrates enterprise-level Android development, featuring Clean Architecture, dependency injection, local persistence, M-PESA payment simulation, and a polished Material 3 UI with dark mode support.

### 🎯 Project Highlights

- ✅ **Production-Ready** - 95% complete with error handling, offline support, and accessibility features
- 🏗️ **Clean Architecture** - Separation of concerns with Domain, Data, and Presentation layers
- 🎨 **Modern UI/UX** - Material 3 design system with dynamic theming and smooth animations
- 💉 **Dependency Injection** - Hilt for scalable and testable code
- 💾 **Local Database** - Room for robust offline-first functionality
- 🔄 **Reactive Programming** - Kotlin Coroutines + Flow for asynchronous operations
- 📱 **M-PESA Integration** - Payment flow simulation with realistic order progression

---

## ✨ Features

### Core Functionality
- 🔐 **Authentication** - User registration and login with validation
- 🧺 **Place Orders** - Select laundry items, service types (Wash/Iron/Both), and schedule pickup
- 🛒 **Smart Cart** - Dynamic pricing, item suggestions, and swipe-to-delete
- 💳 **Payment Flow** - M-PESA simulation with phone number validation (254XXXXXXXXX format)
- 📦 **Order Tracking** - Real-time status updates with 8-stage progression system
- 📜 **Order History** - Complete order timeline with filter and search
- 👤 **User Profile** - Statistics, theme toggle, and settings management

### Production Features
- 🌐 **Network Monitoring** - Real-time connectivity detection with offline indicators
- ⚠️ **Error Handling** - Comprehensive error states with retry mechanisms
- 🎭 **Empty States** - Contextual empty views for better UX
- ♿ **Accessibility** - Content descriptions and proper semantic structure
- 🌓 **Dark Mode** - Persistent theme switching with Material 3 dynamic colors
- ✅ **Form Validation** - Client-side validation for all user inputs
- 📳 **Haptic Feedback** - Tactile responses for user interactions
- ✨ **Animations** - Smooth transitions, shimmer loading, and confetti celebrations
- 🎬 **Onboarding** - 4-page introduction flow for first-time users
- 🔔 **Runtime Permissions** - Proper notification permission handling (Android 13+)

---

## 🏛️ Architecture

CleanSafi follows **Clean Architecture** principles with MVVM pattern for maintainability and testability:

```
┌─────────────────────────────────────────────────────┐
│                 Presentation Layer                   │
│  (UI - Jetpack Compose + ViewModels + StateFlow)    │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                  Domain Layer                        │
│     (Business Logic - Models + Repositories)         │
└─────────────────┬───────────────────────────────────┘
                  │
┌─────────────────▼───────────────────────────────────┐
│                   Data Layer                         │
│  (Room Database + Repository Implementations)        │
└─────────────────────────────────────────────────────┘
```

### Package Structure
```
com.cleansafi/
├── presentation/          # UI Layer (Compose Screens + ViewModels)
│   ├── splash/
│   ├── onboarding/
│   ├── auth/             # Login & Signup
│   ├── home/             # Dashboard
│   ├── order/placeorder/ # Order creation
│   ├── cart/             # Shopping cart
│   ├── checkout/         # Order scheduling
│   ├── payment/          # M-PESA simulation
│   ├── orders/           # Order history
│   ├── profile/          # User profile
│   └── navigation/       # Navigation graph
├── domain/               # Business Logic
│   ├── model/            # Domain models
│   └── repository/       # Repository interfaces
├── data/                 # Data Layer
│   ├── local/            # Room Database
│   │   ├── dao/          # Data Access Objects
│   │   ├── entity/       # Database entities
│   │   └── CleanSafiDatabase.kt
│   └── repository/       # Repository implementations
└── core/                 # Shared Infrastructure
    ├── di/               # Hilt modules
    ├── theme/            # Material 3 theme
    ├── ui/               # Reusable components
    ├── error/            # Error handling
    ├── network/          # Connectivity monitoring
    ├── validation/       # Form validators
    └── util/             # Utilities
```

---

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin 1.9.21
- **UI Framework**: Jetpack Compose (BOM 2023.10.01)
- **Design System**: Material 3
- **Min SDK**: 26 (Android 8.0) | Target SDK: 34 (Android 14)

### Architecture Components
- **ViewModel** - UI state management
- **Navigation Compose** - Type-safe screen navigation
- **Hilt** 2.50 - Dependency injection
- **Room** 2.6.1 - Local database with SQLite
- **Coroutines + Flow** - Asynchronous operations
- **StateFlow** - Reactive state management
- **WorkManager** 2.9.0 - Background tasks

### Development Tools
- **KSP** - Kotlin Symbol Processing for annotation processing
- **Gradle Version Catalogs** - Dependency management
- **Material Icons Extended** - Comprehensive icon set

### Key Dependencies
```kotlin
// Compose & UI
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Architecture
implementation("com.google.dagger:hilt-android:2.50")
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
implementation("androidx.room:room-ktx:2.6.1")

// Lifecycle & Navigation
implementation("androidx.navigation:navigation-compose:2.7.6")
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
```

---

## 📸 Screenshots

<div align="center">

### Authentication & Onboarding
<p float="left">
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen1.jpg" width="200" />
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen2.jpg" width="200" />
</p>

### Home & Services
<p float="left">
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen3.jpg" width="200" />
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen4.jpg" width="200" />
</p>

### Order Flow
<p float="left">
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen5.jpg" width="200" />
  <img src="https://github.com/muchaisam/Cleansafi/blob/main/Images/Screen6.jpg" width="200" />
</p>

*More screenshots and dark mode preview coming soon*

</div>

---

## 🚀 Installation

### Prerequisites
- **Android Studio**: Hedgehog (2023.1.1) or later
- **JDK**: 17 or higher
- **Android SDK**: API Level 34
- **Gradle**: 8.2+ (included via wrapper)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/muchaisam/Cleansafi.git
   cd Cleansafi
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   ```bash
   ./gradlew assembleDebug  # Build debug APK
   ./gradlew installDebug   # Install on connected device
   ```
   Or use the ▶️ Run button in Android Studio

### Build Variants
```bash
./gradlew assembleDebug     # Debug build
./gradlew assembleRelease   # Release build (requires signing)
./gradlew test              # Run unit tests
./gradlew clean build       # Clean build
```

---

## 💡 Usage Guide

### Test Credentials
For demo purposes, you can use any credentials to sign up/login:
- **Email**: Any valid email format (e.g., `demo@cleansafi.com`)
- **Password**: Minimum 6 characters
- **Phone**: 10-digit number (e.g., `0712345678`)

### M-PESA Simulation
When making a payment:
- Use format: `254XXXXXXXXX` (e.g., `254712345678`)
- Payment is simulated - no real money transactions
- Order progresses through realistic stages automatically

### Demo Mode (Hidden Feature)
Long-press the app logo on the Home screen to enable **Demo Mode**:
- Speeds up order progression from minutes to seconds
- Perfect for showcasing the full order lifecycle quickly
- Great for demos and portfolio presentations

---

## 🎯 Key Features Deep Dive

### 1. **Smart Pricing System**
```kotlin
enum class ServiceType {
    WASH_ONLY,      // Base price
    IRON_ONLY,      // -20% from wash price  
    WASH_AND_IRON   // +25% from wash price
}

// Dynamic pricing per item type
LaundryItemType.TOPS       // KSh 40-50
LaundryItemType.TROUSERS   // KSh 50-60
LaundryItemType.BEDSHEETS  // KSh 60-70
LaundryItemType.OTHERS     // KSh 70-80
```

### 2. **Order Status Progression**
```
PENDING → CONFIRMED → PICKED_UP → PROCESSING → 
READY → OUT_FOR_DELIVERY → DELIVERED → COMPLETED
```
Each stage includes:
- Timestamp tracking
- Status history
- Email notifications (simulated)
- Real-time UI updates

### 3. **Offline-First Architecture with Room Database**
All data is persisted locally using Room database, making the app fully functional without an internet connection:

```kotlin
// Room Database with multiple DAOs for complete offline functionality
@Database(
    entities = [UserEntity, OrderEntity, OrderItemEntity, CartItemEntity, PaymentEntity],
    version = 1
)
abstract class CleanSafiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun orderDao(): OrderDao
    abstract fun cartDao(): CartDao
    abstract fun paymentDao(): PaymentDao
}
```

**What works offline:**
- ✅ User registration and login (credentials stored locally)
- ✅ Browse services and add items to cart
- ✅ Place orders and schedule pickups
- ✅ View complete order history with status timeline
- ✅ Track spending statistics (Profile & Orders screens)
- ✅ M-PESA payment simulation
- ✅ Theme preferences persist across sessions

**Why this matters:**
- 🚀 **Instant app startup** - No network calls needed
- 📱 **Works in low-connectivity areas** - Critical for Kenya/Africa markets
- 💾 **Data never lost** - SQLite persistence survives app kills
- 🔄 **Reactive UI updates** - Flow-based observation of database changes

### 4. **Error Handling Strategy**
```kotlin
sealed class AppError {
    data class NetworkError(val message: String)
    data class DatabaseError(val message: String)
    data class ValidationError(val message: String)
    data class UnknownError(val throwable: Throwable)
}
```
Every screen includes:
- ✅ Loading states with shimmer effects
- ⚠️ Error states with retry functionality
- 📭 Empty states with actionable guidance

---

## 🔧 Configuration

### Customizing Pricing
Edit `PricingCalculator.kt`:
```kotlin
object PricingCalculator {
    fun calculateItemPrice(
        itemType: LaundryItemType,
        serviceType: ServiceType
    ): Int {
        // Modify base prices here
        val basePrice = when (itemType) {
            LaundryItemType.TOPS -> 40
            LaundryItemType.TROUSERS -> 50
            // ...
        }
        // Adjust multipliers as needed
    }
}
```

### Theme Customization
Modify `Color.kt` and `Theme.kt` in `core/theme/`:
```kotlin
val md_theme_light_primary = Color(0xFF006C4C)
val md_theme_dark_primary = Color(0xFF6FDBAC)
// Full Material 3 color scheme
```

---

## 📊 Database Schema

### Core Tables
1. **UserEntity** - User accounts
2. **OrderEntity** - Orders with status and scheduling
3. **OrderItemEntity** - Individual items per order
4. **CartItemEntity** - Temporary shopping cart
5. **PaymentEntity** - Payment records with transaction IDs
6. **OrderStatusHistoryEntity** - Complete status timeline

### Relationships
```sql
User (1) ──→ (N) Orders
Order (1) ──→ (N) OrderItems
Order (1) ──→ (1) Payment
Order (1) ──→ (N) StatusHistory
```

---

## 🧪 Testing

### Run Tests
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Instrumented tests
./gradlew testDebugUnitTest      # Debug unit tests only
```

### Test Coverage
- ViewModel logic testing
- Repository pattern testing
- Database migration testing
- Validation logic testing

---

## 📈 Performance Optimizations

- ✅ Lazy column for efficient list rendering
- ✅ Remember and derivedStateOf for composition optimization
- ✅ Database indexes on frequently queried columns
- ✅ Flow-based reactive updates (no unnecessary recompositions)
- ✅ Image loading optimization
- ✅ Minimal overdraw with proper layout hierarchy

---

## 🔐 Security Considerations

### Current Implementation (Demo)
- ⚠️ Passwords stored in plain text (Room database)
- ⚠️ No server-side authentication
- ⚠️ Simulated payment system

### Production Recommendations
- 🔒 Implement password hashing (BCrypt/Argon2)
- 🔒 Add JWT/OAuth authentication
- 🔒 Integrate real M-PESA/payment gateway
- 🔒 Enable ProGuard/R8 obfuscation
- 🔒 Add SSL pinning for network requests
- 🔒 Implement biometric authentication

---

## 🚧 Roadmap

### Phase 1: Core Features ✅ (Complete)
- [x] User authentication
- [x] Order placement and management
- [x] Cart functionality
- [x] Payment simulation
- [x] Order tracking
- [x] Profile management

### Phase 2: Production Readiness ✅ (Complete)
- [x] Error handling system
- [x] Offline support
- [x] Empty states
- [x] Network monitoring
- [x] Form validation
- [x] Accessibility features

### Phase 3: Enhancements 🚀 (Future)
- [ ] Backend API integration
- [ ] Real M-PESA Daraja API
- [ ] Push notifications (FCM)
- [ ] Order rating and reviews
- [ ] Promo codes and discounts
- [ ] Multiple delivery addresses
- [ ] In-app chat support
- [ ] Analytics integration

### Phase 4: Advanced Features 💎 (Future)
- [ ] Multi-language support (i18n)
- [ ] Wear OS companion app
- [ ] Widget for quick order status
- [ ] Share order receipts
- [ ] Subscription plans
- [ ] AI-powered laundry tips

---

## 🤝 Contributing

This is a portfolio project, but feedback and suggestions are welcome!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Developer

**Sam Muchai**
- 🐙 GitHub: [@muchaisam](https://github.com/muchaisam)

---

## 🙏 Acknowledgments

- **Material Design 3** - Design system and components
- **Google Codelabs** - Android development best practices
- **Android Developer Documentation** - Official guides and references
- **Stack Overflow Community** - Problem-solving assistance

---

## 📞 Support

For questions or support:
- 🐛 Issues: [GitHub Issues](https://github.com/muchaisam/Cleansafi/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/muchaisam/Cleansafi/discussions)

---

<div align="center">

### ⭐ If you find this project useful, please consider giving it a star!

**Built with ❤️ using Kotlin and Jetpack Compose**

</div>

