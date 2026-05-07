# Dijon Événements — Application Android

![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-1.6-4285F4?style=flat-square&logo=jetpack-compose)
![Hilt](https://img.shields.io/badge/Hilt-2.51-DD4B39?style=flat-square&logo=google)
![Retrofit](https://img.shields.io/badge/Retrofit-2.11-48B983?style=flat-square)
![Google Maps](https://img.shields.io/badge/Google_Maps-SDK-4285F4?style=flat-square&logo=google-maps)
![Firebase](https://img.shields.io/badge/Firebase-FCM-FFCA28?style=flat-square&logo=firebase)
![Min SDK](https://img.shields.io/badge/Min_SDK-26_(Android_8.0)-brightgreen?style=flat-square)

Native Android application for discovering cultural events in Dijon and the Burgundy region. Built with Kotlin and Jetpack Compose, following MVVM architecture with Hilt dependency injection.

---

## Demo

📱 [Download APK for Android](https://github.com/AdrianMalmierca/frontend-evenements-dijon-api/releases/latest)

**Advice:** The backend runs on Render so it takes around 60 seconds to start and after 15 minutes of inactivity it sleeps again, so it is normal if the first login or register takes time.

> Enable **Unknown sources** in Settings → Security before installing.

## Architecture

<img src="assets/architecture.svg" alt="Map detail" width="600"/>

> See also: [evenements-dijon-api](https://github.com/AdrianMalmierca/evenements-dijon-api) — the backend in Kotlin.

---

## Screenshots

### Login screen
The first thing you'll see when you open the app is the login page, where you have to put your email and password to access your account. Here you can go to sign up in case you don't have an account.

<img src="assets/login.png" alt="Login" width="300"/>

### Signup screen
If you're not logged in, you'll have to create an account by entering your name, email and password.

<img src="assets/register.png" alt="Register" width="300"/>

### Main screen
This is the main page where you can see all the events. Use the category chips to filter by genre, or the search bar to find a specific event.

<img src="assets/Main.png" alt="Main" width="300"/>

### Detail screen
When you click on a card, you can see all the information about an event. You can also share it or add it to your favourites — adding a favourite triggers a push notification.

<img src="assets/Detail.png" alt="Detail" width="300"/>

### Favourites screen
You can see all your events added to favourites in the Favourites page.

<img src="assets/Favs.png" alt="Favourites" width="300"/>

### Search screen
You can search for an event by name.

<img src="assets/Search.png" alt="Search" width="300"/>

### Map screen
You can see visually where the events are located. Not all events appear on the map as some of them don't have GPS coordinates in the OpenAgenda data.

<img src="assets/Map.png" alt="Map" width="300"/>

### Map screen detail
If you click on one of the markers on the map, you'll see the information for that event.

<img src="assets/Mapdetail.png" alt="Map detail" width="300"/>

---

## Problem Statement

Residents and visitors in Dijon lack a unified mobile experience to discover local events — concerts, exhibitions, gastronomic fairs, and cultural gatherings are scattered across multiple websites and platforms.

Dijon Événements solves this by providing a clean native Android app that:
- Aggregates real events from the Dijon Métropole OpenAgenda feed via a dedicated backend
- Shows events on an interactive map centred on Dijon with GPS markers
- Lets authenticated users save favourite events that persist across sessions
- Provides keyword search and category filters to quickly find relevant events
- Sends push notifications when a favourite event is added

---

## Features

### Events
- Browse real cultural events from Dijon and Burgundy
- Search events by keyword in real time
- Filter events by category via horizontal chip row (Concert, Rock, Pop, Electro, Folk, Jazz…)
- Pull to refresh to reload the latest events
- Tap any event card to see full details: description, location, date, address, categories
- Share any event via the Android share sheet

### Map
- Interactive Google Maps view centred on Dijon
- Markers for all events with GPS coordinates
- Tap a marker to preview event details and toggle favourite directly from the map

### Favourites
- Authenticated users can save and remove favourite events
- Favourites persisted in the backend and restored on each login
- Dedicated Favourites tab with the same card UI as the main list
- Push notification received when a favourite is added

### Authentication
- Register and login with email and password
- JWT token stored securely in DataStore Preferences
- Automatic session restoration on app launch
- Logout clears the local token and redirects to login
- FCM token sent to backend after each login for push notification delivery

### Push Notifications
- Firebase Cloud Messaging integration
- FCM token registered with the backend after login
- Notification received on device when a favourite event is added
- Notification permission requested at first launch (Android 13+)

---

## Tech Stack

| Layer | Technology | Reason |
|-------|-----------|--------|
| Language | Kotlin 2.0 | Modern, null-safe, idiomatic Android development |
| UI | Jetpack Compose | Declarative UI, current Android standard |
| Architecture | MVVM + Repository pattern | Clean separation of concerns, testable |
| DI | Hilt | Google-recommended DI for Android, reduces boilerplate |
| Navigation | Navigation Compose | Type-safe navigation between screens |
| Networking | Retrofit 2 + OkHttp | Industry-standard HTTP client for Android |
| Serialisation | Moshi + Kotlin Codegen | Faster than Gson, null-safe with Kotlin |
| Auth persistence | DataStore Preferences | Replacement for SharedPreferences, coroutine-native |
| Maps | Maps Compose + Play Services Maps | Native Google Maps in Compose |
| Images | Coil | Coroutine-native image loading for Compose |
| Push Notifications | Firebase Cloud Messaging | Real-time push notifications via FCM |
| Theme | Material 3 | Modern Material You design system |
| Min SDK | 26 (Android 8.0) | Covers ~95% of active Android devices |

---

## Project Structure

```
dijon-events-android/
├── app/src/main/java/com/adrianmalmierca/dijonevents/
│   ├── DijonEventsApp.kt                       # Hilt Application class
│   ├── DijonFirebaseMessagingService.kt         # FCM token registration + notification display
│   ├── MainActivity.kt                         # Entry point + Navigation host + notification permission
│   ├── data/
│   │   ├── api/
│   │   │   └── DijonEventsApi.kt               # Retrofit interface (includes FCM token endpoint)
│   │   ├── model/
│   │   │   └── Models.kt                       # DTOs (EventDto, AuthResponse, FcmTokenRequest…)
│   │   └── repository/
│   │       ├── AuthRepository.kt               # Login/register + FCM token update
│   │       ├── EventRepository.kt              # Events and favourites logic
│   │       └── TokenManager.kt                 # JWT persistence via DataStore
│   ├── di/
│   │   └── AppModule.kt                        # Hilt module (Retrofit, Moshi, OkHttp)
│   ├── ui/
│   │   ├── auth/
│   │   │   ├── AuthViewModel.kt                # Login/register state + FCM token send after login
│   │   │   ├── LoginScreen.kt                  # Login form
│   │   │   └── RegisterScreen.kt               # Registration form
│   │   ├── events/
│   │   │   ├── EventsViewModel.kt              # Events + favourites state + category filter
│   │   │   ├── EventsListScreen.kt             # Main event list with search + category chips + pull to refresh
│   │   │   ├── EventDetailScreen.kt            # Full event detail + share button + animated content
│   │   │   └── MapScreen.kt                    # Google Maps with event markers
│   │   ├── favorites/
│   │   │   └── FavoritesScreen.kt              # User's saved events
│   │   └── theme/
│   │       └── Theme.kt                        # Burgundy/cream Material 3 theme
│   └── util/
│       └── Result.kt                           # Sealed class for async states
├── app/src/main/res/
│   ├── values/
│   │   ├── strings.xml
│   │   └── themes.xml
│   └── xml/
│       └── network_security_config.xml         # Allow HTTP to local backend
├── app/google-services.json                    # Firebase config (not in Git)
├── app/build.gradle.kts                        # App-level Gradle config
├── gradle/libs.versions.toml                   # Version catalog
├── local.properties.example                    # API keys template
└── build.gradle.kts                            # Root Gradle config
```

---

## Running Locally

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK with API 26+
- A running instance of [evenements-dijon-api](https://github.com/AdrianMalmierca/evenements-dijon-api)
- A Google Maps API key ([obtain here](https://console.cloud.google.com))
- A Firebase project with `google-services.json` (for push notifications)

```bash
# Clone the repository
git clone https://github.com/AdrianMalmierca/frontend-evenements-dijon-api
cd frontend-evenements-dijon-api

# Set up local properties
cp local.properties.example local.properties
# Fill in sdk.dir and MAPS_API_KEY
```

Place your `google-services.json` from Firebase Console inside `app/`. Open the project in Android Studio and let Gradle sync.

### Environment Variables

In `local.properties`:
```properties
sdk.dir=/Users/your-user/Library/Android/sdk
MAPS_API_KEY=your_google_maps_api_key
```

### Backend URL

The app points to `http://10.0.2.2:8080` by default — this is the Android emulator's address for `localhost` on the host machine. If running on a physical device, update `BASE_URL` in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"http://YOUR_LOCAL_IP:8080/\"")
```

### Run

Select an emulator or connected device (API 26+) and press **Run** in Android Studio.

---

## Navigation Flow

```
App Launch
    │
    ├─ Token exists ──► EventsListScreen (bottom nav)
    │                        ├── MapScreen
    │                        ├── FavoritesScreen
    │                        └── EventDetailScreen (on card tap)
    │
    └─ No token ──► LoginScreen
                        └── RegisterScreen
```

---

## Architecture Decisions

### MVVM + Repository Pattern
Each screen has a dedicated `ViewModel` that exposes a single `UiState` as a `StateFlow`. The UI collects this flow and recomposes reactively. Repositories abstract the data sources — the ViewModel never speaks directly to Retrofit or DataStore.

### Hilt for Dependency Injection
Hilt is Google's recommended DI solution for Android. It eliminates manual constructor wiring and makes the dependency graph explicit and testable. The entire Retrofit stack is provided via a single `@Module` in `AppModule.kt`.

### DataStore over SharedPreferences
`DataStore Preferences` is the modern replacement for `SharedPreferences` — it's coroutine-native, type-safe, and handles concurrent access correctly. The JWT token is persisted here and observed as a `Flow<String?>`, which drives the authentication state throughout the app.

### Moshi over Gson
Moshi with Kotlin Codegen generates adapters at compile time rather than using reflection at runtime. This is faster, avoids issues with Kotlin's non-nullable types, and catches serialisation errors at build time instead of at runtime.

### Single Activity + Navigation Compose
The entire app runs in a single `MainActivity`. Navigation between screens is handled by `NavHost` with type-safe route definitions. The bottom navigation bar and top bar are rendered in the `Scaffold` at the root level and conditionally shown based on the current route.

### Client-side Category Filtering
Rather than relying on the backend to filter events by category (which proved unreliable due to inconsistent keyword casing in the OpenAgenda data), categories are filtered client-side in the `ViewModel`. This is faster for the user, removes a network round-trip, and is more robust against schema variations in the upstream API.

### FCM Token Sent on Login
The FCM token is obtained from Firebase and sent to the backend immediately after a successful login. This ensures the backend always has a fresh, valid token for the authenticated user — even if the token rotates between sessions.

### Material 3 with Burgundy Theme
The colour palette was chosen to reflect the Burgundy/Dijon identity — a deep burgundy primary (`#7B1C2E`) with a gold accent (`#D4AF37`) on a warm cream background. This gives the app a distinctive regional character that reinforces the portfolio positioning.

---

## API Integration

The app communicates exclusively with the `dijon-events-api` backend. All OpenAgenda data is proxied through the backend — the Android app never calls OpenAgenda directly.

```
Android App ──► dijon-events-api ──► OpenAgenda
                     │
                     ├──► PostgreSQL (users, favourites, FCM tokens)
                     │
                     └──► Firebase FCM ──► Push Notification
```

Authentication headers are added per-request in the repository layer using the JWT token from DataStore:

```kotlin
api.getFavorites("Bearer $token")
```

---

## Future Improvements

### Short Term
- ✅ **Pull to refresh** — reload events list with swipe gesture
- ✅ **Empty state illustrations** — custom illustrations for empty search results and favourites
- ✅ **Error handling UI** — user-friendly error messages instead of raw error strings

### Medium Term
- ✅ **Filter by category** — chip filters for Concert, Rock, Pop, Electro, Folk, Jazz…
- ✅ **Event sharing** — share event details via Android share sheet
- ✅ **Push notifications** — FCM token registered after login, notification on favourite added

### Long Term
- ✅ **iOS version** — SwiftUI companion app targeting the same backend ([Ledgerly](https://github.com/AdrianMalmierca/LedgerlyIOS) demonstrates iOS native skills)
- ✅ **Animations** — fade and slide transitions on list and detail screens

---

## What I Learned Building This

### Hilt and the Android DI Lifecycle
Hilt's scoping system (`@Singleton`, `@ActivityRetainedScoped`) determines how long a dependency lives. Getting this wrong causes memory leaks or unexpected state resets. The key insight: `ViewModel`s should be scoped to `@ActivityRetainedScoped`, while repositories and network clients should be `@Singleton`.

### JWT State Management with DataStore
Persisting authentication state across app restarts requires observing the DataStore as a `Flow` and connecting it to the navigation graph. The `isLoggedIn` state drives a `LaunchedEffect` that navigates to the appropriate start destination — but this must be handled carefully to avoid navigation loops on recomposition.

### Compose Navigation with Bottom Bar
Keeping the bottom navigation bar in sync with the current route requires using `NavDestination.hierarchy` to match routes correctly, especially when using nested navigation graphs. Using `saveState = true` and `restoreState = true` on `navigate()` preserves the scroll position of each tab.

### Google Maps in Compose
The `maps-compose` library wraps the native `MapView` in a Composable, but lifecycle management is non-trivial. The map must be initialised with the correct `LifecycleOwner` and the `CameraPositionState` must be remembered at the composable level to survive recomposition.

### Firebase Cloud Messaging in Hilt
Injecting Hilt dependencies into a `FirebaseMessagingService` requires annotating the service with `@AndroidEntryPoint`. The `onNewToken` callback only fires on token rotation — for initial token delivery, `FirebaseMessaging.getInstance().token` must be called explicitly after login.

### Retrofit and Coroutines
Retrofit 2.6+ supports `suspend` functions natively — no need for `Call<T>` wrappers. Wrapping each API call in a `try/catch` inside the repository and returning a sealed `Result<T>` class gives the ViewModel a clean way to handle success, error, and loading states without exposing exceptions to the UI layer.

### Android 13 Notification Permission
From Android 13 (API 33) onwards, apps must explicitly request `POST_NOTIFICATIONS` permission at runtime. Without it, push notifications are silently blocked — the FCM token is registered, the backend sends the message, but nothing appears on the device.

---

## License

MIT — free to use, modify, and deploy.

---

## Author

**Adrián Martín Malmierca**  
Computer Engineer & Mobile Applications Master's Student  
[GitHub](https://github.com/AdrianMalmierca) · [LinkedIn](https://www.linkedin.com/in/adri%C3%A1n-mart%C3%ADn-malmierca-4aa6b0293/)

*Built as a portfolio project targeting the French tech market — ESNs and consulting firms in Burgundy/Dijon.*