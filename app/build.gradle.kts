plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.orderfood"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.orderfood"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //lottie
    implementation ("com.airbnb.android:lottie:6.6.6")
    //circle_indicator
    implementation ("me.relex:circleindicator:2.1.6")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation("com.google.firebase:firebase-database")
    //firebase auth
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.firebaseui:firebase-ui-auth:8.0.2")
    //glide
    implementation (libs.glide)

    //paper
    implementation ("io.github.pilgr:paperdb:2.7.2")
    //test gemimi
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")


    //hdodenhof
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //room database
    val room_version = "2.7.2"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")

}