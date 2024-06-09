# Kotlin-DataResult

[![](https://jitpack.io/v/alterok/Kotlin-DataResult.svg)](https://jitpack.io/#alterok/Kotlin-DataResult)

Kotlin-DataResult is a library designed to provide a robust wrapper class `DataResult` that simplifies handling of loading, success, and failure states in Kotlin. It also includes support for various error types, such as network errors, permission errors and more.

## Usage

Here is an example of how you can use the DataResult class in your project:

```kotlin
fun fetchData(): DataResult<String> {
    // Simulate loading state
    val loadingResult: DataResult<String> = DataResult.Loading()
    // Simulate success state
    val successResult: DataResult<String> = DataResult.Success("Data fetched successfully")
    // Simulate failure state
    val failureResult: DataResult<String> = DataResult.Failure(NetworkResultError.NotFound)

    // Your logic to handle these states
    return successResult
}
```
Example of handling the DataResult in your UI classes or in any other class.

```kotlin
fun handleResult(result: DataResult<String>) {
    when (result) {
        is DataResult.Loading -> {
            // Handle loading state
        }
        is DataResult.Success -> {
            // Handle success state
        }
        is DataResult.Failure -> {
            // Handle failure state
            when (result.error) {
                is NetworkResultError.BadRequest -> {
                    // Handle bad-request error
                }
                is NetworkResultError.NotFound -> {
                    // Handle not-found error
                }
                // Handle other errors
            }
        }
    }
}
```

## Installation

[KOTLIN DSL]

1. Add JitPack repository to your project's `settings.gradle.kts` file:

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = URI.create("https://jitpack.io") }
    }
}
```

2. Define dependency in `libs.versions.toml` file:
   
```kotlin
[versions]
dataResult = 1.0.1

[libraries]
dataResult = {group = "com.github.alterok", name = "Kotlin-DataResult", version.ref = "dataResult"}
```

3. Add dependency in app module `build.gradle.kts` file:
   
```kotlin
dependencies{
    implementation(libs.dataResult)
}
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.


