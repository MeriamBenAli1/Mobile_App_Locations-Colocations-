plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mobileappproject_tekdev_5sae3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mobileappproject_tekdev_5sae3"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    // Libraries AndroidX et autres
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Room dependencies
    implementation(libs.room.common)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler) // Utilisé pour générer les classes Room via Java
    // SQLite dependency
    implementation(libs.sqlite)
}
