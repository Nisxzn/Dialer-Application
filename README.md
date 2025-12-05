# SmartDialer

A clean, minimal dialer app for Android built with Kotlin and Jetpack Compose.

## What's Inside

This is a basic dialer application that includes:
- Simple dialpad interface for making calls
- Call history to track your recent calls
- Contact blocklist functionality
- Room database for storing call logs
- Runtime permission handling for phone calls

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room
- **Architecture**: MVVM with ViewModels

## Getting Started

### Prerequisites
- Android Studio (Arctic Fox or newer recommended)
- A physical Android device (emulator might not support actual phone calls)

### Setup

1. Clone this repository
2. Open the project in Android Studio
3. Let Gradle sync and download dependencies
4. Update Kotlin/Compose/Gradle versions if prompted
5. Run the app on your device

## Current Status

This is an MVP (minimum viable product) version. It covers the basics but there's room for improvement:
- Incoming call handling isn't implemented yet (would need CallScreeningService)
- No fancy features like call recording or advanced blocking
- Intentionally kept simple to avoid Play Store permission complications

## What's Next

Some ideas for future development:
- Add Firebase integration
- Implement incoming call screening
- Improve the UI with more animations
- Add contact sync
- Implement call analytics

## Notes

- Make sure to grant phone permissions when prompted
- The app needs to be set as default dialer for full functionality
- Test on a real device for best results

Feel free to fork and build upon this!
