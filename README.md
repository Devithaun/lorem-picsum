# LoremPicSum

## Description
LoremPicSum is a small Android application built with Jetpack Compose that fetches photos from the LoremPicSum API and displays them in a list. 
It follows Clean Architecture principles and incorporates various modern Android development tools.

I built this in Compose rather than XML as a challenge to myself, and for a nice learning experience.

## Screenshots and Videos

![LoremPicSum1](https://github.com/user-attachments/assets/c51cb51c-6e6a-409e-95ee-4ac6507ba006)

![LoremPicSum2](https://github.com/user-attachments/assets/9889a046-c038-4d5a-8e2c-71252f87dd8e)

![LoremPicSum3](https://github.com/user-attachments/assets/13d46434-64b9-474d-9ad8-da25c78e0c5c)

![LoremPicSum4](https://github.com/user-attachments/assets/c3a01048-2f57-44a9-bb2e-2c79f5eb502c)

![LoremPicSum5](https://github.com/user-attachments/assets/00f13c52-c8e4-4d3f-8a30-4cef5e762161)

https://github.com/user-attachments/assets/ff94caa7-245d-4395-9b76-619b08172c83

https://github.com/user-attachments/assets/e28a43a8-2f47-42ae-942c-06ad1966e048

## Technologies Used
- **Jetpack Compose**
- **Kotlin**
- **Retrofit**
- **Gson**
- **Room**
- **DataStore**
- **Mockito**
- **Hilt**
- **Coil**

## Setup & Installation

### Prerequisites
- Java 8
- Gradle 8+
- Minimum SDK: 24

### Steps to Run the Project
1. Clone this repository:
    ```bash
    git clone https://github.com/devithaun/LoremPicSum.git
    ```
2. Open the project in Android Studio.
3. Sync the Gradle files and build the project.
4. Connect a device/emulator and run the app.

## Project Structure
The app follows Clean Architecture principles:

- **Domain Layer**: Contains the use cases and interface definitions.
- **Data Layer**: Implements the domain layer interfaces. Manages data sources including Retrofit API calls and the Room database.
- **UI Layer**: I built this using Jetpack Compose rather than XML, more as a challenge to myself. Contains reusable components like `ErrorDialog`, `FilterDropDown`, and `LoadingSpinner`.

## Design Decisions
- Used **DataStore** for author filter persistence instead of Room for simplicity.
- Used **Mockito** instead of **Mockk** – To be honest, I really wish I used Mockk for its nicer coroutine support, but my head was in Mockito. The `mockito-kotlin` library does make a difference though.
- Implemented unit tests but these could be improved. In particular, the Loading State.
- Used `Hilt` as the dependancy injection framework, for it's ease of setup. I could also have used `Dagger` or `Koin`.

## Potential Improvements
- **Pull to Refresh** – Implement a swipe-to-refresh feature.
- **WorkManager** – Periodically sync with the API to refresh cached data.
- **UI Testing** – Add automated UI tests for better test coverage.
- **Gradle Structure** - I would have liked to clean this up a bit and implement a `common-libs.gradle` that could have been shared across the layers (in particular, the data and ui layers) so as not to copy/paste dependencies in multiple places.

## License
MIT License
