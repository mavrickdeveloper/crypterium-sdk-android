# crypterium-android-library


1.  The SDK is supplied as .aar file “crypteriumsdk.aar”. To connect to the project, you need to add a module from the library and add the dependencies to the build.gradle of your project. **minSdkVersion is 21, target android sdk is 29**



2.  For custom styles you shoud override below:

```
//Buttom of app
<style name="CRPTSDKRoundButton" parent="Widget.AppCompat.Button.Borderless">
   ….
</style>

//Background of app with round corners on main screen
<style name="CRPTSDKRoundBackground" >
….
</style>

//Navigation panel
<style name="CRPTSDKNavBarBackground" >
...
</style>

//Basic backgroud of each screen
<style name="CRPTSDKWindowBackground" >
   ...
</style>
```



3. For interract with lib you can use Crypterium class. Available to you next methods:

```
// Is user logged
Crypterium.getInstance(<YourActivity>).isLoggedIn()


// open concrete screen of sdk
// where YourActivity is current activity and Screens is 
// RECEIVE, 
// SEND_BY_WALLET,
// PROFILE,
// HISTORY,
// EXCHANGE,
// PAYOUT,
// PAYIN
//
// Before open any screen you should ensure that sdk is autorised
Crypterium.getInstance(<YourActivity>).openScreen(<YourActivity>, <Screen>)

// Authorize in sdk with token
Crypterium.getInstance(<AppContext>).setAuthToken(<Auth token string>)
```

4. Sdk callback

Crypterium.appCallback = object : CrypteriumInterface {
                // when token is expired
                override fun onTokenExpired() {
                    //your flow
                }
                // when sdk is finish its work
                override fun onFinish() {
                    //your flow
                }
            }
            

5. All sdk dependencies and their versions
```
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
    implementation "androidx.core:core-ktx:$androidCoreKtxVersion"

    implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"

    //ktx
    implementation "androidx.navigation:navigation-fragment-ktx:$navigationVersion"
    implementation "androidx.navigation:navigation-ui-ktx:$navigationVersion"
    implementation "androidx.preference:preference-ktx:$prefsKtxVersion"

    // Support Library
    implementation "androidx.appcompat:appcompat:$androidxAppCompatVersion"
    implementation "androidx.legacy:legacy-support-v4:$androidxVersion"
    implementation "androidx.cardview:cardview:$androidxVersion"
    implementation "com.google.android.material:material:$materialVersion"
    implementation "androidx.constraintlayout:constraintlayout:$constraintLayoutVersion"

    // Lifecycle
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-runtime:$lifecycleVersion"
    implementation "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion"
    //без нее на 6 и ниже падает
    kapt "androidx.lifecycle:lifecycle-compiler:$lifecycleVersion"

    implementation "com.airbnb.android:lottie:$lottieVersion"

    // FlowLayout
    implementation "com.nex3z:flow-layout:$nex3zFlowLayoutVersion"

    implementation "androidx.emoji:emoji:$emojiVersion"
    implementation "androidx.emoji:emoji-appcompat:$emojiVersion"
    implementation "androidx.emoji:emoji-bundled:$emojiVersion"

    implementation "com.github.Cielsk:clearable-edittext:$clearableEditTextVersion"

    implementation "com.google.dagger:dagger:$daggerVersion"
    kapt "com.google.dagger:dagger-compiler:$daggerVersion"

    implementation "net.opacapp:multiline-collapsingtoolbar:$opacappMultilineCollapsingToolbarVersion"

    implementation "io.reactivex.rxjava2:rxjava:$rxjavaVersion"
    implementation "io.reactivex.rxjava2:rxandroid:$rxandroidVersion"
    implementation "io.reactivex.rxjava2:rxkotlin:$rxkotlinVersion"

    implementation "com.google.android.gms:play-services-maps:$playServicesMapVersion"

    implementation "com.jakewharton.timber:timber:$timberVersion"

    implementation "com.googlecode.libphonenumber:libphonenumber:$phoneNumberVersion"

    implementation "com.squareup.retrofit2:retrofit:$retrofitVersion"
    implementation "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    implementation "com.squareup.retrofit2:converter-scalars:$retrofitVersion"

    implementation "com.squareup.okhttp3:logging-interceptor:$okhttpVersion"
    implementation "com.facebook.stetho:stetho:$stethoVersion"
    implementation "com.facebook.stetho:stetho-okhttp3:$stethoVersion"

    // ReactiveCache
    implementation "com.github.VictorAlbertos:ReactiveCache:$reactiveCacheVersion"
    implementation "com.github.VictorAlbertos.Jolyglot:gson:$jolyglotGsonVersion"

    // Flurry Analytics
    implementation "com.flurry.android:analytics:$flurryAnalyticsVersion"

    implementation "com.amplitude:android-sdk:$amplitudeSdkVersion"

    implementation "io.branch.sdk.android:library:$branchVersion"

    // Room 2003 (best film ever)
    implementation "androidx.room:room-runtime:$roomVersion"
    kapt "androidx.room:room-compiler:$roomVersion"

    implementation "com.github.PhilJay:MPAndroidChart:$mpAndroidChartVersion"

    implementation "com.github.bumptech.glide:glide:$glideVersion"
    kapt "com.github.bumptech.glide:compiler:$glideVersion"

    implementation "com.otaliastudios:cameraview:$cameraviewVersion"

    implementation "com.google.guava:guava:$guavaVersion"

    implementation "com.baoyz.pullrefreshlayout:library:$pullRefreshLayoutVersion"

    implementation "com.github.shts:StoriesProgressView:$storyProgressView"

    implementation "com.google.firebase:firebase-config:$firebaseConfigVersion"
    implementation "com.google.firebase:firebase-perf:$firebasePerfVersion"
    implementation "com.google.firebase:firebase-crashlytics:$crashlyticsVersion"
    implementation "com.google.firebase:firebase-messaging:$firebaseMessagingVersion"
    implementation "com.google.firebase:firebase-core:$firebaseCoreVersion"

    implementation "com.android.installreferrer:installreferrer:$installReferrerVersion"

    implementation "com.github.kenglxn.QRGen:android:$kenglxnQrGenVersion"

    implementation "com.journeyapps:zxing-android-embedded:$zxingVersion"

    implementation "com.facebook.shimmer:shimmer:$shimmerVersion"

    implementation "com.daimajia.swipelayout:library:$daimajiaSwipeLayoutVersion"

    implementation "org.apache.commons:commons-lang3:$apacheLang3"

    implementation "log4j:log4j:$log4j"
```


```
  rxjavaVersion = '2.2.10'
    rxandroidVersion = '2.1.1'
    rxkotlinVersion = '2.4.0'
    retrofitVersion = '2.6.2'
    daggerVersion = '2.21'
    androidxVersion = '1.0.0'
    androidxAppCompatVersion = '1.0.2'
    opacappMultilineCollapsingToolbarVersion = '27.1.1'
    constraintLayoutVersion = '2.0.0-beta6'
    playServicesMapVersion = '17.0.0'
    firebaseCoreVersion = '17.1.0'
    firebaseDynamicLinksVersion = '19.1.0'
    firebaseConfigVersion = '19.1.4'
    firebasePerfVersion = '19.0.7'
    firebaseMessagingVersion = '20.1.7'
    roomVersion = '2.2.5'
    lifecycleVersion = '2.2.0'
    installReferrerVersion = '1.1.2'
    junitVersion = '4.12'
    testExtJunitVersion = '1.1.1'
    androidTestRunnerVersion = '1.0.2'
    mockitoVersion = '2.23.4'
    espressoVersion = '3.2.0'
    roboelecricVersion = '3.8'
    okhttpVersion = '4.6.0'
    leakcanaryVersion = '1.6.1'
    clearableEditTextVersion = '0.0.5'
    daimajiaSwipeLayoutVersion = '1.2.0'
    mpAndroidChartVersion = 'v3.1.0-alpha'
    kenglxnQrGenVersion = '2.3.0'
    nex3zFlowLayoutVersion = '1.2.2'
    glideVersion = '4.9.0'
    socketIoClientVersion = '1.0.0'
    jakewhartonRxbindingVersion = '2.1.1'
    flurryAnalyticsVersion = '10.0.0@aar'
    stethoVersion = '1.5.1'
    reactiveCacheVersion = '1.1.3-2.x'
    jolyglotGsonVersion = '0.0.4'
    zxingVersion = '3.5.0'
    emojiVersion = '1.0.0'
    timberVersion = '4.7.1'
    guavaVersion = '26.0-android'
    crashlyticsVersion = '17.0.0'
    phoneNumberVersion = '8.10.18'
    lottieVersion = '3.0.7'
    materialVersion = '1.2.0-alpha06'
    navigationVersion = '2.3.0'
    androidCoreKtxVersion = '1.2.0'
    branchVersion = '5.0.1'
    amplitudeSdkVersion = '2.14.1'
    pullRefreshLayoutVersion = '1.2.0'
    storyProgressView = '3.0.0'
    shimmerVersion = '0.5.0'
    facebookSdkVersion = '5.4.0'
    prefsKtxVersion = '1.1.1'
    materialratingbarVersion = '1.3.2'
    cameraviewVersion = '1.6.0'
    apacheLang3 = '3.10'
    log4j = '1.2.17'
```




# crypterium-sdk-android
