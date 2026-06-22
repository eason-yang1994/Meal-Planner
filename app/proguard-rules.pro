# Add project specific ProGuard rules here.
# You can control the set of deductions for your project.
#
# For more details, see the official documentation:
# https://developer.android.com/studio/build/shrink-code

# Kotlin
-keep class kotlin.Metadata { *; }
-keep class kotlin.reflect.** { *; }

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class javax.annotation.** { *; }

# Room
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.RoomOpenHelper
-keep @androidx.room.Entity class *

# Compose
-keep class androidx.compose.** { *; }
-keep class kotlinx.coroutines.** { *; }

# Retrofit + OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-keep class com.squareup.retrofit2.** { *; }
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.retrofit2.**

# Kotlinx Serialization
-keep class kotlinx.serialization.** { *; }

# ML Kit
-keep class com.google.mlkit.** { *; }

# Vico Charts
-keep class com.patrykandpatrick.vico.** { *; }

# DataStore
-keep class androidx.datastore.** { *; }

# WorkManager
-keep class androidx.work.** { *; }

# General
-keepclassmembers class * {
    @androidx.annotation.Keep <methods>;
    @androidx.annotation.Keep <fields>;
}

# Suppress warnings
-dontwarn kotlinx.coroutines.flow.**
-dontwarn kotlinx.coroutines.channels.**