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

-keepclassmembers enum * { *; }
-keep class com.google.code.gson.* { *; }
-keepattributes *Annotation*, Signature, Exception

-keep class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

 # With R8 full mode generic signatures are stripped for classes that are not
 # kept. Suspend functions are wrapped in continuations where the type argument
 # is used.
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 # R8 full mode strips generic signatures from return types if not kept.
 -if interface * { @retrofit2.http.* public *** *(...); }
 -keep,allowoptimization,allowshrinking,allowobfuscation class <3>

 # With R8 full mode generic signatures are stripped for classes that are not kept.
 -keep,allowobfuscation,allowshrinking class retrofit2.Response

 # Retain generic signatures of TypeToken and its subclasses with R8 version 3.0 and higher.
 -keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
 -keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken

 -keep class com.google.gson.reflect.TypeToken
 -keep class * extends com.google.gson.reflect.TypeToken
 -keep public class * implements java.lang.reflect.Type

 # Please add these rules to your existing keep rules in order to suppress warnings.
 -dontwarn com.google.api.client.http.GenericUrl
 -dontwarn com.google.api.client.http.HttpHeaders
 -dontwarn com.google.api.client.http.HttpRequest
 -dontwarn com.google.api.client.http.HttpRequestFactory
 -dontwarn com.google.api.client.http.HttpResponse
 -dontwarn com.google.api.client.http.HttpTransport
 -dontwarn com.google.api.client.http.javanet.NetHttpTransport$Builder
 -dontwarn com.google.api.client.http.javanet.NetHttpTransport
 -dontwarn org.joda.time.Instant

 -dontwarn java.lang.invoke.StringConcatFactory

 -keepclassmembers class mx.com.iqsec.sdkpan.model.InitializerTestLife {
     public <init>(...);
 }

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

 # Mantener constructores específicos
 -keepclassmembers class ai.onnxruntime.TensorInfo {
     <init>(...);
 }