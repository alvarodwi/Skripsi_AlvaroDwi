import java.io.FileInputStream
import java.util.Properties

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

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperty = Properties()
keystoreProperty.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "me.varoa.nongki"
    compileSdk = 34

    signingConfigs {
        getByName("debug") {
            keyAlias = keystoreProperty["debugKeyAlias"] as String
            keyPassword = keystoreProperty["debugKeyPassword"] as String
            storeFile = file(rootDir.canonicalPath + "/" + keystoreProperty["debugKeyStore"])
            storePassword = keystoreProperty["debugStorePassword"] as String
        }

        create("release") {
            keyAlias = keystoreProperty["releaseKeyAlias"] as String
            keyPassword = keystoreProperty["releaseKeyPassword"] as String
            storeFile = file(rootDir.canonicalPath + "/" + keystoreProperty["releaseKeyStore"])
            storePassword = keystoreProperty["releaseStorePassword"] as String
        }
    }

    defaultConfig {
        applicationId = "me.varoa.nongki"
        minSdk = 23
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

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
            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            applicationIdSuffix = ".debug"
            signingConfig = signingConfigs.getByName("debug")
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
    implementation(libs.maps.utils.ktx)
    implementation(libs.maps.ktx)

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
