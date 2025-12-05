SmartDialer MVP - Android Studio project skeleton.

What you get:
- Minimal Android project (Kotlin + Jetpack Compose)
- Room database for call logs and blocklist
- Compose UI with a dialer field, call button, and call history
- Permission handling for CALL_PHONE (runtime request)
- ViewModel scaffolding

How to open:
1. Download the ZIP file from the link provided in the assistant message.
2. Unzip and open the folder in Android Studio (Arctic Fox or later recommended).
3. Let Gradle sync and build. Update Kotlin/Compose/Gradle plugin versions if needed.
4. Run on a real device (emulator may not support ACTION_CALL).

Notes & next steps:
- This is a skeleton MVP. It intentionally avoids Play Store sensitive-permissions boilerplate.
- For incoming-call handling, CallScreeningService or being default dialer is required.
- Add Firebase and other features as needed.
