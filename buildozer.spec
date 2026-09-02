[app]

# (str) Title of your application
title = Quotex Bot

# (str) Package name
package.name = quotexbot

# (str) Package domain (used for java/package names)
package.domain = com.quotexbot

# (str) Source code where the main.py live
source.dir = .

# (list) Source files to include
source.include_exts = py,png,jpg,kv,atlas

# (str) Application versioning (method 1)
version = 1.0

# (list) Application requirements
requirements = python3,kivy,kivy-garden.webview,requests,numpy

# (str) Presplash of the application
# presplash.filename = %(source.dir)s/data/presplash.png

# (str) Icon of the application
# icon.filename = %(source.dir)s/data/icon.png

# (str) Supported orientation (one of landscape, portrait or all)
orientation = landscape

# (bool) Indicate if the application should be fullscreen or not
fullscreen = 0

# (list) Permissions
android.permissions = INTERNET, ACCESS_NETWORK_STATE, WRITE_EXTERNAL_STORAGE

# (int) Target Android API
android.api = 30

# (int) Minimum API your APK will support
android.minapi = 21

# (int) Android SDK version to use
android.sdk = 30

# (str) Android NDK version to use
android.ndk = 23b

# (int) Android NDK API to use
android.ndk_api = 21

# (bool) Enable AndroidX support
android.enable_androidx = True

# (str) Android entry point
android.entrypoint = org.kivy.android.PythonActivity

# (str) Android app theme
android.apptheme = "@android:style/Theme.NoTitleBar"

# (bool) Copy the python source to the device
android.copy_python_source = True

# (bool) Use the new Android Gradle build system
android.use_gradle = True

# (str) Android Gradle plugin version
android.gradle_plugin_version = 3.4.0

# (bool) Use AndroidX instead of Android Support Library
android.use_androidx = True

# (str) Android package name (use for android.intent.category.LAUNCHER)
android.launcher_name = Quotex Bot

# (str) Android activity name
android.activity_name = main

# (bool) Enable the Android NDK build
android.ndk = True

# (bool) Use the old Python for Android toolchain
android.old_p4a = False

# (bool) Enable the Android wake lock to keep the screen on
android.wakelock = True

# (bool) Enable the Android debug mode
android.debug = False

# (bool) Enable the Android logcat
android.logcat = True
