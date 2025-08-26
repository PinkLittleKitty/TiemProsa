# TiemProsa

**A Literary Clock for Android**

TiemProsa is an Android app that tells time through literature. It displays quotes from books (only in spanish rn) that reference the current hour, creating a unique way to experience both time and prose.

## Features

- **Literary Time Display**: Shows quotes that mention the current hour.
- **Interactive Details**: Tap quotes to reveal author and book information
- **Home Screen Widget**: Add a literary clock widget to your Android home screen
- **Curated Collection**: Features quotes from Gabriel García Márquez, Julio Cortázar, Isabel Allende, Juan Rulfo, and many other celebrated authors

## How It Works

The app contains a curated collection of quotes from Latin American literature, each containing a specific time reference (1:00 AM through 12:00 AM). When you open the app or view the widget, it displays a quote that matches the current hour.

For example, at 3:00 AM, you might see:
> "A las tres de la madrugada despertó con la certeza de que algo había cambiado para siempre."
> — Isabel Allende, *La casa de los espíritus*

## Installation & Usage

### Main App

1. Install the APK or build from source
2. Open TiemProsa to see the current literary time
3. Tap the quote to toggle between showing just the quote or including author/book details
4. Use "New quote" button to get a different quote for the current hour

### Widget

1. Long-press on your Android home screen
2. Select "Widgets" from the menu
3. Find "TiemProsa" in the widget list
4. Drag it to your home screen
5. Tap the widget to toggle author/book details

## Technical Details

- **Built with**: Kotlin, Jetpack Compose, Android Widgets API
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 15 (API 36)
- **Architecture**: Repository pattern with CSV data source
- **Widget Features**: Smart caching, persistent state, automatic hourly updates

## Data Source

The quotes are stored in a CSV file (`app/src/main/assets/quotes.csv`) containing:

- Hour (1-24)
- Quote text in Spanish
- Author name
- Book title

## Building from Source

1. Clone this repository
2. Open in Android Studio
3. Build and run

The project uses standard Android Gradle build system and should compile without additional setup.

## License

This project is open source. The literary quotes are from published works and used for educational/cultural purposes.
