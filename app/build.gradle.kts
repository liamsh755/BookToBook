import com.android.build.gradle.internal.res.processResources
import org.gradle.internal.impldep.org.eclipse.jgit.lib.ObjectChecker.encoding

plugins {
    id("com.android.application")
}

android {
    namespace = "co.il.liam.booktobook"
    compileSdk = 34

    defaultConfig {
        applicationId = "co.il.liam.booktobook"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(project(mapOf("path" to ":HELPER")))
    implementation(project(mapOf("path" to ":MODEL")))
    implementation(project(mapOf("path" to ":VIEWMODEL")))
    implementation("androidx.activity:activity:1.8.0")
    implementation("org.chromium.net:cronet-embedded:119.6045.31")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.sun.mail:android-mail:1.6.2")
    implementation("com.sun.mail:android-activation:1.6.2")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.picasso:picasso:2.71828")
}