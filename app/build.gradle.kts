plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.pettracker.demosignature"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.pettracker.demosignature"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.activity:activity-ktx:1.8.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.github.barteksc:pdfium-android:1.9.0")
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation("com.github.AkshayHarsoda:AksPermission-Android:1.1.0")
    implementation("org.jetbrains.anko:anko:0.10.8")
    implementation("com.github.yalantis:ucrop:2.2.8")

    val camerax_version = "1.3.0"
// CameraX core library using camera2 implementation
    implementation ("androidx.camera:camera-camera2:$camerax_version")
// CameraX Lifecycle Library
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")
// CameraX View class
    implementation ("androidx.camera:camera-view:$camerax_version")

    implementation("com.itextpdf:itextg:5.5.10")
    implementation(project(path = ":sticker"))
    implementation(project(path = ":photoeditor"))

    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt("com.github.bumptech.glide:compiler:4.15.1")


}
