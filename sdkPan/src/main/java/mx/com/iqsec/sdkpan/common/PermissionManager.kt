package mx.com.iqsec.sdkpan.common

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import mx.com.iqsec.sdkpan.R

class PermissionManager(private val fragment: Fragment) {
    private var settingsOpened = false

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val LOCATION_PERMISSION_REQUEST_CODE = 101
        const val MEDIA_PERMISSION_REQUEST_CODE = 102

        // Función para verificar si todos los permisos están concedidos
        fun areAllPermissionsGranted(context: Context, permissions: Array<String>): Boolean {
            return permissions.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }
        }
    }

    // Solicitar permiso de cámara
    fun requestCameraPermission(onResult: (Boolean) -> Unit) {
        val permission = Manifest.permission.CAMERA

        if (ContextCompat.checkSelfPermission(fragment.requireContext(), permission) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            onResult(true)
        } else {
            fragment.requestPermissions(arrayOf(permission), CAMERA_PERMISSION_REQUEST_CODE)

            // Configuramos un callback para recibir el resultado
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                onResult(isGranted)
            }
        }
    }

    fun permissionsMedia(): Array<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permissions.toTypedArray()
    }

    fun isMediaPermissionGranted(): Boolean {
        return areAllPermissionsGranted(fragment.requireContext(), permissionsMedia())
    }

    fun requestMediaPermission(onResult: (Boolean) -> Unit) {
        if (areAllPermissionsGranted(fragment.requireContext(), permissionsMedia())) {
            onResult(true)
        } else {
            fragment.requestPermissions(permissionsMedia(), MEDIA_PERMISSION_REQUEST_CODE)
        }
    }

    // Solicitar permisos de ubicación
    fun requestLocationPermission(onResult: (Boolean) -> Unit) {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (areAllPermissionsGranted(fragment.requireContext(), permissions)) {
            onResult(true)
        } else {
            fragment.requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE)

            // Configuramos un callback para recibir el resultado
            fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                onResult(isGranted)
            }
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        val context = fragment.requireContext()
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }


    fun isCAMERAPermissionGranted(): Boolean {
        val context = fragment.requireContext()
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    // Método para abrir ajustes de la aplicación
    fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", fragment.requireContext().packageName, null)
        }
        fragment.startActivity(intent)
    }

    // Mostrar diálogo para ir a configuración
    fun showSettingsDialog(onResult: (Boolean) -> Unit) {
        val context = fragment.requireContext()
        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle("Permiso requerido")
            .setMessage(fragment.getString(R.string.sdk_permisions_camera))
            .setPositiveButton("Ir a configuración") { _, _ ->
                settingsOpened = true  // Marcamos que el usuario fue a configuración
                openAppSettings()
                onResult(true)
            }
            .setNegativeButton("Cancelar") { _, _ -> onResult(false) }
            .setCancelable(false)
            .show()
    }

    // Método para verificar si el usuario fue a configuración
    fun wasSettingsOpened(): Boolean {
        return settingsOpened
    }

    // Método para reiniciar el estado
    fun resetSettingsOpened() {
        settingsOpened = false
    }
}