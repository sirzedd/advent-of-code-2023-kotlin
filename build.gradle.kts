plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}

dependencies {
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
