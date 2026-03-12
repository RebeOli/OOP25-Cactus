plugins {
    // Apply the java plugin to add support for Java
    java

    // Apply the application plugin to add support for building a CLI application
    // You can run your app via task "run": ./gradlew run
    application

    /*
     * Adds tasks to export a runnable jar.
     * In order to create it, launch the "shadowJar" task.
     * The runnable jar will be found in build/libs/projectname-all.jar
     */
    id("com.gradleup.shadow") version "9.3.1"
    id("org.danilopianini.gradle-java-qa") version "1.166.0"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        // Java version used to compile and run the project
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Detecting os
val osName = System.getProperty("os.name").lowercase()
val osArch = System.getProperty("os.arch").lowercase()

// Correct classifier for JavaFX
val javafxPlatform = when {
    osName.contains("win") -> "win"
    osName.contains("mac") -> if (osArch == "aarch64") "mac-aarch64" else "mac"
    osName.contains("nux") || osName.contains("nix") -> if (osArch == "aarch64") "linux-aarch64" else "linux"
    else -> "linux" // Fallback
}

val javaFXModules = listOf("base", "controls", "fxml", "swing", "graphics")
val javaFxVersion = "23.0.2"

dependencies {
    // Suppressions for SpotBugs
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    // Example library: Guava. Add what you need (and use the latest version where appropriate).
    // implementation("com.google.guava:guava:28.1-jre")

    // YAML parser
    implementation("org.yaml:snakeyaml:2.2")

    
    // Loads jars for desired platform. no "package not found" errors this way.
    for (module in javaFXModules) {
        implementation("org.openjfx:javafx-$module:$javaFxVersion:$javafxPlatform")
    }

    // The BOM (Bill of Materials) synchronizes all the versions of Junit coherently.
    testImplementation(platform("org.junit:junit-bom:6.0.3"))
    // The annotations, assertions and other elements we want to have access when compiling our tests.
    testImplementation("org.junit.jupiter:junit-jupiter")
    // The engine that must be available at runtime to run the tests.
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.guava:guava:33.5.0-jre")
}

val main: String by project

application {
    // Define the main class for the application
    mainClass.set(main)
}