# Hoardr

> **Note:** This project is under construction and is being built brick by brick.

A read-it-later app for people who save links obsessively and read them never.

## The Problem

Every tech person has this disease. You see a cool GitHub repo, a blog post, a Twitter thread — you hit save, and it goes into a folder where links go to die. Browser bookmarks, Twitter saves, "read later" apps... all of them are just graveyards with better UI.

Hoardr is my attempt to fix that. The idea is simple: one inbox for every link you save, from any app, enriched automatically so you actually know what you saved — and eventually, some gentle shaming mechanics to nudge you into reading instead of hoarding.

## What We've Built So Far

- **Inbox & Universal Sharing**: Share links or text from any app straight into Hoardr via Share Targets.
- **Auto-Enrichment**: Scrapes web pages automatically (titles, descriptions, OpenGraph images) using Jsoup with ZenRows fallback.
- **Explore Mode ("Swipe to Read")**: Tinder-style card stack for unread articles — swipe right to keep, swipe left to delete, or mark as read.
- **Priority Hoards**: Star important items as priority and view them in a dedicated Priority screen separated by article, note, or idea.
- **Categories (Articles, Notes, Ideas)**: Filter and organize your hoards with custom badges and filter chips.
- **Profile & Statistics Dashboard**: Reading progress percentage, total hoards, unread backlog, priority counts, and category breakdowns.
- **Storage & Data Management**: Clear image cache, clear read items, and JSON backup export/import for full data ownership.

## What's Planned

- Offline readable article view
- Search across everything you've saved
- A weekly "purge" flow — read it or delete it

## Tech Stack

- Kotlin
- Jetpack Compose (Material 3)
- Room (local database)
- Hilt (Dependency Injection)
- Coil (Image loading)
- Jsoup & OkHttp (Web scraping & metadata enrichment)

## Why I'm Building This

Mostly for myself — I have 400 saved tweets and a few dozen of articles and I hate it. Also because it's a good excuse to learn Android properly: share targets, background work, offline-first architecture, content parsing. Real problems, not tutorial problems.

## Status

Active and growing. The app has evolved from a basic link saver into a fully functional read-it-later and knowledge base app with Explore swiping, priority tracking, statistics, themes, and JSON backups. Still building in public!
