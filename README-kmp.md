# Proyecto PAN

****Integración de SDK****

## Tabla de Contenidos

- [Tabla de Contenidos](#tabla-de-contenidos)

- [Requisitos](#requisitos)

- [Permisos](#permisos)

- [Agregar configuración para descargar librería](#agregar-configuración-para-descargar-librería)

- [Contempla agregar al gradle](#contempla-agregar-al-gradle)

- [Implementar librería en el proyecto](#implementar-librería-en-el-proyecto)

- [Inicializar Libreria](#inicializar-libreria)

- [RESPUESTA DEL SDK](#respuesta-del-sdk)

- [Versionado](#Versionado)

- [Para recibir los mensajes de error](#para-recibir-los-mensajes-de-error)

- [Inicializar Broadcast](#inicializar-broadcast)

- [Compilación en Release](#compilación-en-release)

## Requisitos
Este SDK requiere una **API mínima 24 o superior** para su correcta implementación y ejecución.  
Esta versión mínima garantiza la compatibilidad con las librerías utilizadas y las APIs del sistema empleadas dentro del SDK, asegurando un rendimiento óptimo y una integración estable.  
No se recimienda el uso de versiones de APi iferiores ya que podría provocar errores de compilación o comportamientos inesperados debido a la falta de soporte en ciertas funcionalidades del sistema.

    Version minima de API: 24


## Permisos


```xml
<uses-feature android:name="android.hardware.camera" android:required="true" />   

<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  
<application  
android:largeHeap="true">  
  
</application>
```


## Agregar configuración para descargar librería


Esta configuración es necesaria para acceder al repositorio remoto de maven


```groovy
    //Repositorioi de librerias requeridas por el SDK
    maven {
        name = ""
        url =
                uri("")
        credentials {
            username = ""
            password =
                    ""
        }
    }
    
    //Repositoio de libreria del PAN
    maven {
        name = ""
        url =
                uri("")
        credentials {
            username = ""
            password =
                    ""
        }
    }
```


Credenciales de acceso al repositorio maven:
- `name:` Se proporcionará por otro medio por buenas practicas.
- `url:` Se proporcionará por otro medio por buenas practicas.
- `username:` Se proporcionará por otro medio por buenas practicas.
- `password:` Se proporcionará por otro medio por buenas practicas.


## Contempla agregar al gradle


```groovy
  

  buildFeatures { 
    viewBinding = true 
    buildConfig = true 
  }
  

```


## Implementar librería en el proyecto


```toml
    [versions]
    sdk_pan = "x.x.x"

    [libraries]
    iqsec-sdkpan-onboarding = { group = "mx.com.iqsec.sdkpan", name = "omboarding", version.ref = "sdk_pan" }
```
Una vez realizada la implementacion, sincronizar y compilar proyecto.


## Inicializar Libreria


Se deberá de usar ******_**_ActivityResultLauncher__****.


```kotlin
private lateinit var launcher: ActivityResultLauncher<Intent>
```


## RESPUESTA DEL SDK
```groovy
    SDK_PAN_MResponse( 
        var estado: Int = 0,
        var descripcion: String = "",
        var no_transaccion: String = ""
    )
```

Al momento de cargar la aplicación se deberá de asignar el valor a ****launcher****
Y al momento de finalizar la prueba de vida se podrá obtener la información por medio de los extras según sea el caso de uso.


```kotlin
  launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
  { 
    if (it.data != null) { 
  

      try { 
        //AQUI VENDRÁ EL OBEJETO CON LOS DATOS DE RESPUESTA DE LA PRUEBA
        val rTest: SDK_PAN_MResponse = 
                     it.data?.getSerializableExtra("res") as SDK_PAN_MResponse 
    
        if (it.resultCode == 7005) {  
            Log.e("TestSDKPANActivity", "Tiempo agotado")  
        }
        if (it.resultCode == 5005) { 
            Log.e("TestSDKPANActivity", "Finalizado con exito") 
        } 
        if (it.resultCode == 4005) { 
          Log.e("TestSDKPANActivity", "Cancelado por usuario") 
        } 
        if (it.resultCode == 3005) { 
          Log.e("TestSDKPANActivity", "Error en flujo") 
        }
      } catch (error: Exception) { 
  

      } 
    } 
  }
```


Para invocar al SDK de prueba de vida, se requiere lo siguiente.


```kotlin
    val intent = Intent(this, SDK_PAN::class.java)
val data = Bundle()

data.putSerializable(
    "ConfigTestLife", InitializerTestLife(
        numFrames = 8,
        url_services = "",
        confidence = 25.0,
        timeoutStart = 600,
        timeoutToast = 10,
        timeoutDialog = 0
    )
)

intent.putExtras(data)
launcher.launch(intent)
```


Donde:
- `numFrames:` Valores enteros aceptados son del 5 al 15, en donde 5 es mayor velocidad con menor precisión y 15 es mayor precisión pero menor velocidad.
- `url_services:` Se proporcionará por otro medio por buenas practicas.
- `confidence:` Valor para dar por verdadera la validación de prueba de vida contra la credencial INE
- `timeoutStart:` Valor total de donde el timmer empezara a decrementar
- `timeoutToast:` Valor en el cual se va a mostrar el Toast notificando el tiempo restante para cerrar el flujo
- `timeoutDialog:` Valor en el cual se estará mostrando el dialog de tiempo agotado para dar margen al cierre normal de la App.


## Versionado

Este SDK contiene el numero de version accesible desde **BuildConfig**

    var SDKVersion = mx.com.iqsec.sdkpan.BuildConfig.SDK_VERSION
Ademas de que en tiempo de ejeucion, buscando en **Logcat**, al buscar **CheckVersionInfo** podras encontrar los registros que indican la version actual y la mas reciente en caso de que exista.

    Iniciando verificación de versión
    Versión actual: x.x.x
    La versión es la más reciente: x.x.x

## Para recibir los mensajes de error

El nombre del broadcast donde se recibiran los mensajes:
```string
mx.com.iqsec.sdkpan.BCMESAGGEERROR
```
Para poder recibir los mensajes de error en la actividad o fragmento se debera de declarar el **broadcast** donde se recibiran los mensajes, ademas del **executor.**


```kotlin
private val executor: ExecutorService = Executors.newSingleThreadExecutor()  
private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {  
    override fun onReceive(context: Context, intent: Intent) {  
        val action = intent.action  
        if ("mx.com.iqsec.sdkpan.BCMESAGGEERROR" == action) {  
            try {  
                executor.execute {  
                    val rTest: SDK_MResponseCetes =  
                        intent.getSerializableExtra("res") as SDK_MResponseCetes  
                    rTest?.let {  
                        Log.e("BroadcastReceiver", "BroadcastReceiver ${rTest.descripcion}")  
                        val scope = CoroutineScope(Dispatchers.Default)  
  
                        scope.launch {  
                            withContext(Dispatchers.IO) {
                                //Procesamiento de mensajes de error
                            }  
                        }                    } ?: run {  
                        Log.e("BroadcastReceiver", "No se recibió el objeto")  
                    }  
                }  
            } catch (error: Exception) {  
                Log.e("BroadcastReceiver", "Error parse")  
            }  
        }  
    }  
}
```

## Inicializar Broadcast

Se debe de registrar el broadcast y desregistrar segun aplique si es en un **Fragmento** o **Avtividad**.
```kotlin
override fun onStart() {  
    super.onStart()  
    LocalBroadcastManager.getInstance(this).registerReceiver(  
        myReceiver,  
        IntentFilter("mx.com.iqsec.sdkpan.BCMESAGGEERROR")  
    )  
}  
  
override fun onDestroy() {  
    super.onDestroy()  
    LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver)  
}
```

## Compilación en Release
Agregar las siguientes reglas al archivo de ****progrard-rules****


```groovy
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

```