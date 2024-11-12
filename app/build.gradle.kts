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

    // Ajout de viewBinding
    buildFeatures {
        viewBinding=true
    }
    packaging {
        resources {
            resources.excludes.add("META-INF/NOTICE.md")
            resources.excludes.add("META-INF/LICENSE.md")
            resources.excludes.add("META-INF/INDEX.LIST")
            resources.excludes.add("META-INF/DEPENDENCIES")
            resources.excludes.add("META-INF/io.netty.versions.properties")

            /*pickFirsts += ['META-INF/LICENSE.txt']
            excludes += ['META-INF/NOTICE.md', 'META-INF/LICENSE.md', 'META-INF/INDEX.LIST', 'META-INF/DEPENDENCIES', 'META-INF/io.netty.versions.properties']*/
        }
        //resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.room.testing)
    implementation(libs.android.mail)
    implementation(libs.android.activation)


}
