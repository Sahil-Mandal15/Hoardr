# Hoardr

&gt; **Note:** This project is under construction and is being built brick by brick.

A read-it-later app for people who save links obsessively and read them never.

## The Problem

Every tech person has this disease. You see a cool GitHub repo, a blog post, a Twitter thread — you hit save, and it goes into a folder where links go to die. Browser bookmarks, Twitter saves, "read later" apps... all of them are just graveyards with better UI.

Hoardr is my attempt to fix that. The idea is simple: one inbox for every link you save, from any app, enriched automatically so you actually know what you saved — and eventually, some gentle shaming mechanics to nudge you into reading instead of hoarding.

## What's Planned

- Auto-fetching titles and descriptions for saved links
- Offline readable article view
- Search across everything you've saved
- Stats that make you feel bad about your hoard-to-read ratio
- A weekly "purge" flow — read it or delete it

## Tech Stack

- Kotlin
- Room (local database)
- WorkManager (background enrichment)
- Jetpack Compose (UI, eventually)

## Why I'm Building This

Mostly for myself — I have 400 saved tweets and a few dozen of articles and I hate it. Also because it's a good excuse to learn Android properly: share targets, background work, offline-first architecture, content parsing. Real problems, not tutorial problems.

## Status

Early. Very early. The app currently saves links and that's about it. I'm building this in public, documenting the process as I go.