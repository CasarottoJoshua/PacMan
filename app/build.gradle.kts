plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.pacman"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pacman"
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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
    implementation(libs.firebase.database)
    implementation(libs.activity)
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.github.VishnuSivadasVS:Advanced-HttpURLConnection:1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.activity:activity-ktx:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.0")
    implementation("com.github.bumptech.glide:glide:4.15.0")
    implementation("com.github.VishnuSivadasVS:Advanced-HttpURLConnection:1.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")

    implementation ("com.android.volley:volley:1.2.1")


    implementation(libs.firebase.database)
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.android.application")
    implementation("com.google.android.gms:play-services-auth:20.4.0")
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.google.firebase:firebase-auth")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
}

tasks.whenTaskAdded {
    if (name == "mergeDebugResources") {
        dependsOn("processDebugGoogleServices")
    }
}