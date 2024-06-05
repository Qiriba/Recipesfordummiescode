plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.sonarqube") version "5.0.0.4638"
}

android {
    namespace = "com.example.rezeptefuerdummies"
    compileSdk = 34

    buildFeatures {
        dataBinding = true
    }
    defaultConfig {
        applicationId = "com.example.rezeptefuerdummies"
        minSdk = 21
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

sonar {
  properties {
    property("sonar.projectKey", "RezepteForDummies")
    property("sonar.projectName", "RezepteForDummies")
  }
}
dependencies {
    implementation("com.squareup.picasso:picasso:2.8")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:+")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.robolectric:robolectric:4.7")
    testImplementation ("org.mockito:mockito-core:3.12.4")
    testImplementation ("androidx.test:core:1.4.0")
}
