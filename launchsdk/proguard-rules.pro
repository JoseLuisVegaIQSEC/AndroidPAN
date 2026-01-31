# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

#Huawei atributes
-dontwarn android.telephony.HwTelephonyManager
-dontwarn com.huawei.android.os.BuildEx$VERSION
-dontwarn com.huawei.hianalytics.process.HiAnalyticsConfig$Builder
-dontwarn com.huawei.hianalytics.process.HiAnalyticsConfig
-dontwarn com.huawei.hianalytics.process.HiAnalyticsInstance$Builder
-dontwarn com.huawei.hianalytics.process.HiAnalyticsInstance
-dontwarn com.huawei.hianalytics.process.HiAnalyticsManager
-dontwarn com.huawei.hianalytics.util.HiAnalyticTools
-dontwarn com.huawei.libcore.io.ExternalStorageFile
-dontwarn com.huawei.libcore.io.ExternalStorageFileInputStream
-dontwarn com.huawei.libcore.io.ExternalStorageFileOutputStream
-dontwarn com.huawei.libcore.io.ExternalStorageRandomAccessFile
-dontwarn org.bouncycastle.crypto.BlockCipher
-dontwarn org.bouncycastle.crypto.engines.AESEngine
-dontwarn org.bouncycastle.crypto.prng.SP800SecureRandom
-dontwarn org.bouncycastle.crypto.prng.SP800SecureRandomBuilder

-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

# Reglas para evitar conflictos con JUnit y clases de prueba
-dontwarn junit.**
-dontwarn android.test.**
-keep class junit.** { *; }
-keep class android.test.** { *; }

 -dontwarn com.google.api.client.http.GenericUrl
 -dontwarn com.google.api.client.http.HttpHeaders
 -dontwarn com.google.api.client.http.HttpRequest
 -dontwarn com.google.api.client.http.HttpRequestFactory
 -dontwarn com.google.api.client.http.HttpResponse
 -dontwarn com.google.api.client.http.HttpTransport
 -dontwarn com.google.api.client.http.javanet.NetHttpTransport$Builder
 -dontwarn com.google.api.client.http.javanet.NetHttpTransport
 -dontwarn org.joda.time.Instant

-keep class mx.com.iqsec.antispoffing.modules.facedetector.detector.CameraSource {*;}
-keep class mx.com.iqsec.antispoffing.modules.facedetector.detector.BitmapUtils {*;}
-keep class mx.com.iqsec.auto_detection_ine.modules.captueocr.capture.CameraSourceOCR{*;}
-keep class mx.com.iqsec.auto_detection_ine.modules.facedetector.detector.BitmapUtils {*;}
 -keep class * {
   @com.google.gson.annotations.SerializedName <fields>;
 }

 # Reglas para ONNX Runtime
 -keep class ai.onnxruntime.** { *; }
 -keepclassmembers class ai.onnxruntime.** { *; }
 -keepattributes *Annotation*, InnerClasses, Signature, Exception
 -keepattributes SourceFile, LineNumberTable

 # Evitar que R8 elimine clases nativas
 -keepclasseswithmembernames class * {
     native <methods>;
 }
  -keepclassmembers class mx.com.iqsec.sdkpan.model.InitializerTestLife {
      public <init>(...);
  }