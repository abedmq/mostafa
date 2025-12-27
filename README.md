# Local Video Audio Player (Android, Java)

Android 10+ local media player using Java, XML, and AndroidX Media3 ExoPlayer with foreground playback service, Room, WorkManager, and ffmpeg-kit for video-to-MP3 conversion.

## Tech stack
- Java + XML layouts
- AndroidX Media3 ExoPlayer
- Foreground `PlaybackService`
- Room (Java)
- WorkManager (Java)
- ffmpeg-kit (audio extraction)
- MVVM

## Project structure
- `app/src/main/java/com/example/localvideoaudioplayer/`
  - `data/` – MediaStore repository and Room database/entities/DAOs
  - `player/` – `PlayerManager`
  - `service/` – `PlaybackService`
  - `converter/` – `AudioConverter` (ffmpeg-kit)
  - `download/` – `DownloadWorker` (WorkManager)
  - `ui/` – Activities for folders, videos, player, audio library, tasks, settings
- `app/src/main/res/layout/` – XML layouts for all screens
- `app/src/main/res/xml/preferences.xml` – settings screen

## Build & run
1. Open the project in Android Studio (Giraffe+).
2. Use Android Studio SDK Manager to ensure Android SDK 34 and Build Tools are installed.
3. Connect a device/emulator on Android 10+.
4. Run the `app` configuration. Grant media permissions when prompted.

## Key classes
- `PlaybackService` – Media3 `MediaSessionService` powering background playback and notifications.
- `PlayerManager` – Initializes ExoPlayer/MediaSession, toggles audio-only mode, controls speed/seek.
- `MediaStoreRepository` – Scans device videos via `MediaStore`, groups by folder, exposes LiveData.
- `AudioConverter` – Uses ffmpeg-kit to extract audio to MP3 and stores results in Room.
- `DownloadWorker` – WorkManager worker downloading MP4 via direct URL with progress + Room tracking.
- `AppDatabase` – Room database hosting videos, audio files, and download tasks.
