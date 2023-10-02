@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gradle.secrets)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.navigation.safeArgs)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "me.varoa.nongki"
    compileSdk = 34

    defaultConfig {
        applicationId = "me.varoa.nongki"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        debug {
            applicationIdSuffix = ".debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        // Treat all Kotlin warnings as errors (disabled by default)
        // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
        val warningsAsErrors: String? by project
        allWarningsAsErrors = warningsAsErrors.toBoolean()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xstring-concat=inline",
            "-opt-in=kotlin.RequiresOptIn",
            // Enable experimental coroutines APIs, including Flow
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
        )
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugarJdkLibs)
    implementation(platform(libs.firebase.bom))
    /*
    androidx
     */
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.bundles.androidx.lifecycle)

    /*
    ui stuffs
     */
    implementation(libs.material)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.preference)

    /*
    backend stuffs
     */
    implementation(libs.bundles.koin)
    implementation(libs.kotlinx.coroutines.android)
    // local
    implementation(libs.bundles.androidx.localPersistence)
    implementation(libs.okio)
    implementation(libs.androidx.datastore)
    ksp(libs.room.compiler)
    // remote
    implementation(libs.bundles.networking)
    // work
    implementation(libs.androidx.work)

    /*
    firebase
     */
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation(libs.play.services.auth)
    // gms
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    /*
    other libs
     */
    implementation(libs.androidx.paging)
    implementation(libs.coil)
    implementation(libs.logcat)
    implementation(libs.hashids)

    /*
    unit & integration tests
     */
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
}

secrets {
    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "secrets.defaults.properties"
}
