# SnookerSpot Android App

This is a full Android Studio project that packages the SnookerSpot club finder into an installable Android application using WebView. It searches live OpenStreetMap data, needs internet access, and requests GPS permission when you tap Nearby.

## Build an APK with Android Studio
1. Open this folder in Android Studio (latest stable).
2. Allow Gradle to sync and download dependencies.
3. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
4. Find the APK at `app/build/outputs/apk/debug/app-debug.apk`.

## Build an APK with GitHub Actions
Upload the project files to a GitHub repository, open **Actions > Build Android APK > Run workflow**, then download the **SnookerSpot-debug-APK** artifact after the workflow completes. Extract that artifact to obtain `app-debug.apk`.

## Notes
- Debug APK is suitable for personal testing, not a production Play Store release.
- Search results depend on OpenStreetMap coverage and public map APIs; ratings are not provided.
- No APK is bundled with this source project; it must be compiled with Android SDK.
