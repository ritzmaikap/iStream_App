# iStream App

## Overview

The iStream App is an Android application developed in Java using Android Studio. It is a YouTube playlist manager that allows users to create an account, log in, play YouTube videos inside the app, save video links, and manage a personal playlist.

This project demonstrates user authentication, Room database integration, user session handling, video playback, and multi-screen Android navigation.

---

## Main Objective

The goal of this project is to build a functional Android media application where each user can maintain their own private video playlist while learning how local databases and Android activities work together.

---

## Core Features

## 1. Authentication System

### Login Screen

Users can log in using:

- Username
- Password

Login credentials are validated using Room Database.

### Sign Up Screen

Users create an account using:

- Full Name
- Username
- Password
- Confirm Password

Validation checks include:

- Empty field checking
- Password match checking
- Duplicate username prevention

---

## 2. Home Screen

After login, users are taken to the dashboard where they can:

- Enter a YouTube video URL
- Play the selected video
- Add the URL to playlist
- Open saved playlist
- Logout

---

## 3. Video Playback

The entered YouTube link is converted into a video ID and loaded into an embedded player inside the application.

This allows users to watch videos without leaving the app.

---

## 4. Playlist Management

Users can save videos into their personal playlist.

Features include:

- Save video URL
- View all saved videos
- Click saved video to replay
- Separate playlists for each user

---

## Database System (Room)

The project uses Room Database for local storage.

---

## Tables Used

### AppUser Table

Stores user account data:

- userId
- fullName
- username
- password

### SavedVideo Table

Stores saved playlist data:

- videoId
- ownerUserId
- videoUrl

The `ownerUserId` links each saved video to a specific user.

---

## Project Structure

```text
app/
 └── src/main/java/com/example/istream_app/
      ├── data/
      │   ├── LocalVaultDatabase.java
      │   ├── AppUser.java
      │   ├── AppUserDao.java
      │   ├── SavedVideo.java
      │   └── SavedVideoDao.java
      │
      ├── utils/
      │   └── AuthKeeper.java
      │
      ├── MainActivity.java
      ├── CreateAccountActivity.java
      ├── DashboardActivity.java
      ├── VideoPlayerActivity.java
      └── LibraryActivity.java
