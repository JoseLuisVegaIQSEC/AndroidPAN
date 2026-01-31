package mx.com.iqsec.sdkpan.presentation.captureframes

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import mx.com.iqsec.antispoffing.presentation.sdk_antispoffing.FaceCaptureFramesFragment
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.common.PermissionManager
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorService
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorTestLife
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.sdk_feedbackStatus
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.sendMessage
import mx.com.iqsec.sdkpan.databinding.FSdkAntispoffingBinding
import mx.com.iqsec.sdkpan.domain.model.MTestLife
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FAntispoffing : SDK_PAN_BFragment() {
    private var _binding: FSdkAntispoffingBinding? = null
    private val binding get() = _binding!!

    private val vmAniSpoffing: SDK_VMantispoffing by viewModels()
    private lateinit var fValidation: FaceCaptureFramesFragment
    private lateinit var frameUser: ArrayList<String>
    private var findOb = BaseConfig()
    private var isLoaderActive = true
    private var userInfo: UserINEModel? = null
    private lateinit var permissionManager: PermissionManager

    private var onStateToolbar: onStateToolbarListener? = null

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

        // Si fValidation no está inicializada, significa que estamos en proceso de solicitud de permisos
        if (!::fValidation.isInitialized) {
            // Verificar si el permiso se concedió mientras estábamos en ajustes
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Permiso concedido, inicializar cámara
                initListeners()
            } else {
                // El usuario regresó de configuración sin conceder el permiso
                // Verificamos que estamos retornando de configuración y no en la carga inicial
                if (permissionManager.wasSettingsOpened()) {
                    // Cancelar proceso por falta de permisos
                    cancelbyUser()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FSdkAntispoffingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        permissionManager = PermissionManager(this)

        extractData()
        initObservers()
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        permissionManager.requestCameraPermission { isGranted ->
            if (isGranted) {
                // Permiso concedido, inicializar cámara
                initListeners()
            } else {
                permissionsDenied()
            }
        }
    }

    private fun permissionsDenied() {
        requireActivity().dialogErrorService(
            getString(R.string.lbl_something_wrong),
            getString(R.string.sdk_permisions_camera),
            getString(R.string.lbl_retry)
        ) {

            // Permiso denegado, verificar si debemos mostrar diálogo para ir a configuración
            // "No volver a preguntar" marcado, mostrar diálogo para ir a configuración
            permissionManager.showSettingsDialog { userWentToSettings ->
                if (!userWentToSettings) {
                    // Usuario no quiso ir a configuración, retornar error
                    val data = SDK_PAN_MResponse(
                        estado = SDK_Constants.TEST_ERROR,
                        descripcion = getString(R.string.sdk_permisions_camera),
                        no_transaccion = requireActivity().makeFolio(SDK_Constants.TEST_LIFE_STEP)
                    )
                    requireActivity().returnResponse(data, SDK_Constants.TEST_ERROR)
                    cancelbyUser()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PermissionManager.CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, inicializar cámara
                initListeners()
            } else {
                permissionsDenied()
            }
        }
    }

    private fun extractData() {
        userInfo = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)

        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }
    }

    fun initListeners() {

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

        fValidation = FaceCaptureFramesFragment()
        val bundle = Bundle()
        val testLifeInit = mx.com.iqsec.antispoffing.model.InitializerTestLife(
            entity = findOb.entity,
            user = findOb.user,
            password = findOb.password,
            url_services = findOb.servicesUrl,
            reference = findOb.reference,
            numFrames = findOb.numFrames,
            enableBrighnessValidation = findOb.enableBrighnessValidation,
            enableTwoFacesValidation = findOb.enableTwoFacesValidation,
            enableExtraBrightness = findOb.enableExtraBrightness
        )
        bundle.putSerializable("ConfigTestLife", testLifeInit)
        fValidation.arguments = bundle
        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.sdk_a_fade_in,
                R.anim.sdk_a_fade_out,
                R.anim.sdk_a_fade_in,
                R.anim.sdk_a_fade_out
            )
            replace(R.id.fDdkValidate, fValidation)
        }

        fValidation.setCallbacks(
            onCaptureEnd = {
                frameUser = it
                callService()
            },
            onCaptureError = { code, message ->
                requireActivity().sendMessage(
                    SDK_PAN_MResponse(
                        estado = SDK_Constants.TEST_ERROR,
                        descripcion = message,
                    )
                )
                requireActivity().runOnUiThread {
                    fValidation.StopCaptureFrames()
                    checkAttempts(
                        getString(R.string.sdk_pan_antispoffing_error_validation),
                        message,
                        getString(R.string.lbl_retry)
                    )
                }
            },
            onResponseReceived = {

            }
        )
    }

    private fun checkAttempts(title: String, message: String, buttonText: String) {
        var cTitle = title
        var cMessage = message
        var cButtonText = buttonText
        val attemps = getAttempts()

        if (attemps >= SDK_Constants.MAX_ATTEMPS) {
            cTitle = getString(R.string.lbl_txt_max_attempts_reached)
            cMessage = message
            cButtonText = getString(R.string.lbl_close)
        }

        requireActivity().dialogErrorTestLife(
            cTitle,
            cMessage,
            cButtonText
        ) {
            if (attemps >= SDK_Constants.MAX_ATTEMPS) {
                actionFinish(
                    getString(R.string.lbl_txt_max_attempts_reached),
                    SDK_Constants.TEST_LIFE_STEP
                )
            } else {
                addAttempt()
                goToNoticeTestLife()
            }
        }
    }

    private fun cancelbyUser() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.TEST_CALCEL_USER,
            descripcion = getString(R.string.txt_cancel_by_user),
            no_transaccion = requireActivity().makeFolio(SDK_Constants.TEST_LIFE_STEP)
        )
        requireActivity().returnResponse(data, SDK_Constants.TEST_CALCEL_USER)
    }

    private fun initObservers() {
        Log.e("TAG", "initObservers: init observers antispoffing")
        vmAniSpoffing.isLoading.observe(viewLifecycleOwner) {
            showProgress(it, getString(R.string.test_life_loader))
            if (it)
                isLoaderActive = it
        }

        vmAniSpoffing.eAntispoffing.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is SDK_ResultValue.Success -> {
                        if (it.data.analisis == "Real") {
                            sdk_feedbackStatus(
                                SDK_PAN_MResponse(
                                    estado = SDK_Constants.TEST_ERROR,
                                    descripcion = getString(R.string.txt_success_validation)
                                )
                            )

                            val indice =
                                if (it.data.indices != null && it.data.indices.isNotEmpty()) it.data.indices[0] else 0
                            val imageGet = frameUser[indice]
                            val data = MTestLife(
                                image = imageGet,
                                score = (it.data.promedio ?: 0.0) * 100,
                                message = getString(R.string.txt_success_validation)
                            )

                            //Guardar la imagen del rostro
                            userInfo = userInfo?.copy(fotousuario = imageGet)
                            //Reiniciar contador de intentos
                            resetAttempts()
                            savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userInfo)
                            savePreferenceObject(SDK_Constants.RESULT_ANTISPOFFING, data)

                            showProgress(false)
                            goToCaptureOk()
                        } else {
                            showProgress(false)
                            requireActivity().sendMessage(
                                SDK_PAN_MResponse(
                                    estado = SDK_Constants.TEST_ERROR,
                                    descripcion = getString(R.string.txt_fail_validation),
                                )
                            )
                            val (dTitle, bTitle, reachedAttempts) = retryAttemp()
                            val error = if (reachedAttempts) dTitle else "Error en la validación"
                            requireActivity().dialogErrorService(
                                error,
                                getString(R.string.txt_fail_validation),
                                bTitle
                            ) {
                                if (reachedAttempts) actionFinish(
                                    error,
                                    SDK_Constants.TEST_LIFE_STEP
                                ) else goToNoticeTestLife()
                            }
                        }
                    }

                    is SDK_ResultValue.Error -> {
                        showProgress(false)
                        messageOnErrorTestLife(
                            it.error,
                            it.error.second
                        )
                    }
                }

            }
        }
    }

    private fun messageOnErrorTestLife(error: Pair<String, String>, messageService: String) {
        var _error = error.second
        showProgress(false)
        requireActivity().sendMessage(
            SDK_PAN_MResponse(
                descripcion = messageService,
                estado = SDK_Constants.TEST_ERROR,
                no_transaccion = requireActivity().makeFolio(SDK_Constants.TEST_LIFE_STEP)
            )
        )
        val (dTitle, bTitle, reachedAttempts) = retryAttemp()
        var titleError =
            if (reachedAttempts) dTitle else error.first

        if (_error.contains("NO SE DETECTO NINGUN ROSTRO EN LA IMAGEN")) {
            titleError = getString(R.string.lbl_something_wrong)
            _error = getString(R.string.txt_fail_validation)
        }

        requireActivity().dialogErrorService(
            titleError,
            _error,
            bTitle
        ) {
            if (reachedAttempts) actionFinish(
                titleError,
                SDK_Constants.TEST_LIFE_STEP
            ) else goToNoticeTestLife()
        }
    }

    private fun goToCaptureOk() {
        onNavigate(R.id.action_nav_SDK_Face_Capture_to_SDK_FAntispoffing_Ok)
    }

    private fun goToNoticeTestLife() {
        onNavigate(R.id.action_nav_SDK_Face_Capture_to_nav_SDK_Face_Capture_Notice)
    }

    private fun callService() {
        val listImages = ArrayList(frameUser.subList(0, frameUser.size - 1))
        vmAniSpoffing.AntispoffingValidate(listImages, findOb)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        System.gc()
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "onPause: $isLoaderActive")
        if (!isLoaderActive) {
            childFragmentManager.beginTransaction().remove(fValidation).commitAllowingStateLoss()

            val error = getString(R.string.errorOnPause)
            messageOnErrorTestLife(Pair("", error), error)
        }
    }
}