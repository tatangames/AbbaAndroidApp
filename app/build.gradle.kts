plugins {
    id("com.android.application")

}

android {
    namespace = "com.tatanstudios.abbaappandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tatanstudios.micaminarcondios"
        minSdk = 25
        targetSdk = 33
        versionCode = 11
        versionName = "1.1.1"

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
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")

    implementation("com.squareup.retrofit2:adapter-rxjava2:2.5.0") //rxjava
    implementation("com.squareup.retrofit2:converter-gson:2.5.0") //rxjava
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0") //rxjava
    implementation("io.reactivex.rxjava2:rxjava:2.2.2") //rxjava

    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.github.GrenderG:Toasty:1.5.2")

    implementation("io.github.tutorialsandroid:kalertdialog:20.4.8")
    implementation("com.github.TutorialsAndroid:progressx:v6.0.19")

    // notificaciones one signal
    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    // confetii
    implementation("nl.dionsegijn:konfetti:1.2.2")

    // manejo de permisos
    implementation("pub.devrel:easypermissions:3.0.0")


    implementation("org.jsoup:jsoup:1.14.3")



    // manejador de imagenes
    implementation("com.github.bumptech.glide:glide:4.13.2")
    implementation("androidx.activity:activity:1.8.0") // glide imagenes
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.2") // glide soporte


    // para zoom imagen
    implementation("com.github.chrisbanes:PhotoView:2.3.0")

    // efecto shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")



    implementation("com.google.android.exoplayer:exoplayer:2.18.7")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}