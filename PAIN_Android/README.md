# PAIN — Android reminder app

## Build an installable APK without Android Studio (GitHub Actions)

1. Create a new **public or private** GitHub repository named `PAIN-Android`.
2. On your computer, extract this project ZIP.
3. In the new GitHub repository, choose **Add file → Upload files**.
4. Upload the *contents* of the extracted `PAIN_Android` folder, including the hidden `.github` folder. Do not upload only the ZIP file.
5. Commit the files to the `main` branch.
6. Open the repository's **Actions** tab. Select **Build PAIN APK** and click **Run workflow** (or wait for the automatic build after your commit).
7. When the workflow finishes with a green check, open that run, scroll to **Artifacts**, and download `PAIN-debug-apk`.
8. Extract the downloaded artifact ZIP. It contains `app-debug.apk`.
9. Transfer `app-debug.apk` to your Android phone, tap it, and allow installation from that source if Android asks.

## App behavior
- The first reminder is scheduled after the interval selected by the user.
- Tapping the notification opens a half-screen choice activity.
- **The pain of growth** schedules the next reminder 120 minutes later.
- **The pain of staying where you are** schedules the next reminder after the configured interval.
- Reminders continue until stopped and are rescheduled after device restart.

## Notes
- GitHub Actions builds the APK in the cloud; Android Studio is not required.
- This is a debug APK for personal testing, not a Play Store release.
- Android may defer background alarms during Doze/battery saver. The project uses inexact, battery-friendly alarms.
- The choice screen opens after tapping the notification; Android does not automatically show it over another app.
- Grant notification permission when prompted on Android 13+.
