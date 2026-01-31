package mx.com.iqsec.test_sdk

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mx.com.iqsec.launchsdk.databinding.ActivityTestSdkPanBinding
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.model.InitializerTestLife
import mx.com.iqsec.sdkpan.presentation.SDK_PAN
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TestSDKPANActivity : AppCompatActivity() {
    private val TAG = TestSDKPANActivity::class.java.simpleName
    private lateinit var binding: ActivityTestSdkPanBinding
    private lateinit var launcher: ActivityResultLauncher<Intent>

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private val myReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if ("mx.com.iqsec.sdkpan.BCMESAGGEERROR" == action) {
                try {
                    executor.execute {
                        val rTest: SDK_PAN_MResponse =
                            intent.getSerializableExtra("res") as SDK_PAN_MResponse
                        rTest?.let {
                            Log.e(TAG, "BroadcastReceiver ${rTest.descripcion}")
                            val scope = CoroutineScope(Dispatchers.Default)

                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    // Aquí va el consumo del servicio
                                    serviceTest()
                                }
                            }
                        } ?: run {
                            Log.e(TAG, "No se recibió el objeto")
                        }
                    }
                } catch (error: Exception) {
                    Log.e(TAG, "Error parse")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        ContextCompat.registerReceiver(
            this,
            myReceiver,
            IntentFilter("mx.com.iqsec.sdkpan.BCMESAGGEERROR"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(myReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestSdkPanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnTestLife.setOnClickListener {
            testFlavor()
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                try {
                    val rTest: SDK_PAN_MResponse =
                        it.data?.getSerializableExtra("res") as SDK_PAN_MResponse
                    val objectSize = getObjectSizeInBytes(rTest)

                    Log.e(TAG, "Tamaño del objeto en bytes: $objectSize")
                    Log.e(TAG, "Error message: ${rTest.descripcion}")

                    if (it.resultCode == 3005) {
                        Log.e(TAG, "TEST ERROR ${rTest.descripcion}")
                        //Redirigir a la pantalla principal
                    }
                    if (it.resultCode == 4005) {
                        Log.e(TAG, "test calcel by user")
                        //Redirigir a la pantalla principal
                    }
                    if (it.resultCode == 5005) {
                        Log.e(TAG, "Enrolamiento exitoso")
                        //Redirigir a la pantalla principal
                    }
                    if (it.resultCode == 7005) {
                        Log.e(TAG, "APP_TIMEOUT")
                        //Redirigir a la pantalla principal
                    }

                } catch (error: Exception) {
                    Log.e(TAG, "Error parse")
                }
            }
        }
    }

    private fun testFlavor() {
        val intent = Intent(this, SDK_PAN::class.java)
        val data = Bundle()

        data.putSerializable(
            "ConfigTestLife", InitializerTestLife(
                numFrames = 8,
                timeInactivitySeconds = 300,//300
                url_services = "https://servicespan.azurewebsites.net/",//Dev PAN
                confidence = 25.0,
                timeoutStart = 600,//600
                timeoutDialog = 0
            )
        )

        intent.putExtras(data)
        launcher.launch(intent)
    }

    suspend fun serviceTest() {
        delay(1000) // Wait for 1 second
        Log.e(TAG, "Mande servicio")
    }

    fun createUUID(): String {
        return UUID.randomUUID().toString()
    }

    private fun getObjectSizeInBytes(obj: Any): Int {
        return try {
            val byteStream = ByteArrayOutputStream()
            val objectStream = ObjectOutputStream(byteStream)
            objectStream.writeObject(obj)
            objectStream.flush()
            byteStream.size()
        } catch (e: Exception) {
            Log.e(TAG, "Error al calcular el tamaño del objeto: ${e.message}")
            -1
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                testFlavor()
            }
        }
    }

    companion object Companion {
        private val REQUIRED_RUNTIME_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}