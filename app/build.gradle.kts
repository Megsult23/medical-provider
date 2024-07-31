plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.team1.dispatch.medicalprovider"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.team1.dispatch.medicalprovider"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Hilt
    implementation(libs.hiltDependency)
    kapt(libs.hiltKapt)

    //Lifecycle
    implementation(libs.viewModelKotlinDependency)
    implementation(libs.viewModelSavedStateDependency)
    implementation(libs.liveDataDependency)

    //Lifecycle Extensions
    implementation(libs.lifecycleExtensionDependency)

    //Logging Interceptor
    implementation(libs.loggingInterceptorDependency)

    //Retrofit and Gson
    implementation(libs.retrofitDependency)
    implementation(libs.gsonDependency)


    //Glide
    implementation(libs.glideDependency)
    kapt(libs.glideKapt)

    //Splash Screen
    implementation(libs.splashScreenDependency)

    //RoundedImageView
    implementation(libs.roundImageViewDependency)

    //CircleImageView
    implementation(libs.circleImageViewDependency)

    //SwipeRefreshLayout
    implementation(libs.swipeRefreshLayoutDependency)

    //SDP
    implementation(libs.sdpDependency)
    implementation(libs.sspDependency)

    //Quick Permissions Kotlin
    implementation(libs.quickPermissionsKotlinDependency)

    //Socket IO
    implementation(libs.socketIo) {
        exclude(group = "org.json", module = "json")
    }

    //MaterialDialog
    implementation(libs.materialDialogCoreDependency)
    implementation(libs.materialDialogInputDependency)
    implementation(libs.materialDialogDateTimeDependency)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

}