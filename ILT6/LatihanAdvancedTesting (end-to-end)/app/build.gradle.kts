plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.dicoding.newsapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.dicoding.newsapp"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        buildConfigField("String", "API_KEY", ""db874e166f4c473e9132d19a45135274"")
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"))
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        resources {
            excludes += listOf("META-INF/AL2.0", "META-INF/LGPL2.1")
        }
    }

    sourceSets {
        androidTest.java.srcDirs += "src/sharedTest/java"
        test.java.srcDirs += "src/sharedTest/java"
    }

    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    //desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    //ui
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.recyclerview)
    implementation(libs.constraintlayout)
    implementation(libs.material)

    implementation(libs.glide)
    implementation(libs.viewpager2)
    implementation(libs.fragment.ktx)

    //testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //mockito
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    //special testing
    testImplementation(libs.core.testing) // InstantTaskExecutorRule
    testImplementation(libs.kotlinx.coroutines.test) //TestCoroutineDispatcher

    //special instrumentation testing
    androidTestImplementation(libs.core.testing) // InstantTaskExecutorRule
    androidTestImplementation(libs.kotlinx.coroutines.test) //TestCoroutineDispatcher
    debugImplementation(libs.fragment.testing) //launchFragmentInContainer
    androidTestImplementation(libs.espresso.contrib) //RecyclerViewActions
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    //mock web server
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.okhttp.tls)
    implementation("androidx.test.espresso:espresso-idling-resource:3.5.1")

    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)

    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    //coroutine support
    implementation(libs.androidx.lifecycle.viewmodel.ktx) //viewModelScope
    implementation(libs.androidx.lifecycle.livedata.ktx) //liveData
    implementation(libs.androidx.room.ktx)
}
