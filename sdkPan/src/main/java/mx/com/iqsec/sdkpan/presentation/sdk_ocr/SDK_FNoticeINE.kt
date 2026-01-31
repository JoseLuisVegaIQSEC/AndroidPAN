package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.PermissionManager
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.DialogPermisionGeolocation
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.checkLocationStatus
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkfnoticeineBinding
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FNoticeINE : SDK_PAN_BFragment() {
    private var _binding: SdkfnoticeineBinding? = null
    private val binding get() = _binding!!
    private var onStateToolbar: onStateToolbarListener? = null
    private lateinit var permissionManager: PermissionManager
    private lateinit var locationPermissionLauncher: ActivityResultLauncher<String>

    override fun onDetach() {
        super.onDetach()
        onStateToolbar = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onStateToolbar = context as? onStateToolbarListener
    }

    override fun onResume() {
        super.onResume()
        onStateToolbar?.activityUpdateToolbar()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SdkfnoticeineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        permissionManager = PermissionManager(this)

        // Registrar el ActivityResult para permisos de ubicación
        locationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    navToCaptureIne()
                } else {
//                    // Si el usuario no concede el permiso, mostrar un mensaje de error
                    requireActivity().DialogPermisionGeolocation(
                        titleBtn = getString(R.string.sdk_permisions_activate),
                        "Permiso de la cámara",
                        getString(R.string.sdk_permisions_camera), {
                            // Ir a la configuración de la app para habilitar el permiso
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = android.net.Uri.fromParts(
                                        "package",
                                        requireActivity().packageName,
                                        null
                                    )
                                }
                            requireActivity().startActivity(intent)
                        }
                    )
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it,SDK_Constants.NOTICE_INE) }
                return@setOnClickListener
            }
            checkPermisionsAction()
        }
    }

    private fun navToCaptureIne() {
        resetAttempts()

        onNavigate(R.id.action_nav_SDK_FNoticeINE_to_nav_SDK_capture_front_ine)
    }

    private fun checkCameraPermission() {
        val permission = Manifest.permission.CAMERA

        // Verificar si los permisos ya están concedidos
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (checkLocationStatus(requireContext())) {
                navToCaptureIne()
            }
        } else {
            locationPermissionLauncher.launch(permission)
        }
    }

    private fun checkPermisionsAction() {
        val preferencr = getPreferenceString(SDK_Constants.CAMERA_PERMISSION_REQUEST, "")
        if (!permissionManager.isCAMERAPermissionGranted()) {
            requireActivity().DialogPermisionGeolocation(
                getString(R.string.sdk_permisions_activate),
                "Permiso de la cámara",
                getString(R.string.sdk_permisions_camera),
                {
                    val shouldShowRationale =
                        shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)

                    if (!preferencr.isNullOrEmpty()) {
                        if (shouldShowRationale) {
                            checkCameraPermission()
                        } else {
                            val intent =
                                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = android.net.Uri.fromParts(
                                        "package",
                                        requireActivity().packageName,
                                        null
                                    )
                                }
                            requireActivity().startActivity(intent)
                        }
                    } else {
                        savePreferenceString(SDK_Constants.CAMERA_PERMISSION_REQUEST, "1")
                        checkCameraPermission()
                    }
                }
            )
        } else {
            navToCaptureIne()
        }
    }
}