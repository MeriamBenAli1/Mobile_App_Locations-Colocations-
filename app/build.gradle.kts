plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mobileappproject_tekdev_5sae3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mobileappproject_tekdev_5sae3"
        minSdk = 28
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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.0")
        implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.android.car.ui:car-ui-lib:2.5.0")
        implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.car.ui.lib)
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")



    // Room dependencies
    implementation("androidx.room:room-runtime:2.5.2")
    annotationProcessor("androidx.room:room-compiler:2.5.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
