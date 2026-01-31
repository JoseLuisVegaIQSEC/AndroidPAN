package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.getImageTemp
import mx.com.iqsec.auto_detection_ine.modules.captueocr.models.sdk_takePicture
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorOCR
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorTestLife
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.extractData
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.sendMessage
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.FSdkOcrBackBinding
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.model.MTestLife
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.model.InitializerTestLife
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment
import java.util.Calendar

class SDK_FOCRBack : SDK_PAN_BFragment() {
    private var _binding: FSdkOcrBackBinding? = null
    private val binding get() = _binding!!

    private var isBackTaken = false

    private val credentialViewModel: SDK_VMOCR by viewModels()
    private var findOb = BaseConfig()
    var userINE: UserINEModel? = null
    private var imgUser: MTestLife? = null
    private var verifyBiometricData: VerifyFramesModel? = null
    var modelTestLife = InitializerTestLife()
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FSdkOcrBackBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initObservers()
        initListeners()
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.BACK_INE_STEP) }
                return@setOnClickListener
            }

            if (!isBackTaken) {
                takeImageBackIne()
            } else {
                val imgFront = getImageTemp(requireContext(), imageFrontTemp)
                val imgBack = getImageTemp(requireContext(), imageBackTemp)
                credentialViewModel.ocrValidate(
                    imgFront?.convertBitmapToBase64StringPNG() ?: "",
                    imgBack?.convertBitmapToBase64StringPNG() ?: "",
                    findOb, requireContext()
                )
            }
        }

        binding.clZoneTakeImage.setOnClickListener {
            takeImageBackIne()
        }
    }

    private fun cancelbyUser() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.TEST_CALCEL_USER,
            descripcion = getString(R.string.txt_cancel_by_user),
            no_transaccion = requireActivity().makeFolio(SDK_Constants.BACK_INE_STEP)
        )
        requireActivity().returnResponse(data, SDK_Constants.TEST_CALCEL_USER)
    }

    private fun takeImageBackIne() {
        val data = Bundle()
        val mtakeImg = sdk_takePicture(
            SDK_Constants.INE_REVERSO,
            getString(R.string.sdk_take_image_back_ine_title),
            SDK_Constants.TYPE_CAPTURE_BACK
        )
        data.putSerializable(SDK_Constants.TAKE_IMAGE, mtakeImg)
        onNavigate(R.id.action_nav_SDK_capture_back_ine_to_nav_SDK_FCaptureIne, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.e(
            "onActivityResult",
            "requestCode: $requestCode, resultCode: $resultCode"
        )
        if (requestCode == SDK_Constants.REQUEST_CODE_TAKE_BACK) {
            when (resultCode) {
                SDK_Constants.SCAN_DOCUMENT_OK -> getData()
                SDK_Constants.SCAN_DOCUMENT_CANCEL -> cancelbyUser()
            }
        }
    }

    private fun takeImagesAgain(title: String, message: String, buttonText: String) {
        var cTitle = title
        var cMessage = message
        var cButtonText = buttonText

        deleteImageTemp(requireContext(), imageBackTemp)
        deleteImageTemp(requireContext(), imageFrontTemp)
        val attemps = getAttempts()

        if (attemps >= SDK_Constants.MAX_ATTEMPS) {
            cTitle = getString(R.string.lbl_something_wrong)
            cMessage = getString(R.string.lbl_txt_max_attempts_reached)
            cButtonText = getString(R.string.lbl_close)
        }

        requireActivity().dialogErrorOCR(
            cTitle,
            cMessage,
            cButtonText
        ) {
            if (attemps >= SDK_Constants.MAX_ATTEMPS) {
                actionFinish(
                    getString(R.string.lbl_txt_max_attempts_reached),
                    SDK_Constants.BACK_INE_STEP
                )
            } else {
                addAttempt()
                onNavigate(R.id.action_nav_SDK_capture_back_ine_to_nav_SDK_capture_front_ine)
            }
        }
    }

    private fun initObservers() {
        credentialViewModel.isLoading.observe(viewLifecycleOwner) {
            val activity = activity ?: return@observe
            if (activity.isFinishing || activity.isDestroyed) return@observe
            showProgress(it, getString(R.string.capture_ine_loader))
        }

        credentialViewModel.oCRINE.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is SDK_ResultValue.Success -> {
                        handleResponseINE(it.data)
                    }

                    is SDK_ResultValue.Error -> {
                        showProgress(false)
                        var takeImagesAgain = false
                        var error = it.error.second
                        var titleError = getString(R.string.lbl_something_wrong)
                        if (error.contains("LOCALIZAR") || error.contains("LOZALIZAR")) {
                            titleError =
                                getString(R.string.sdk_pan_capture_back_txt_vote_id_not_legible)
                            error = getString(R.string.fragment_credential_bad_images)
                            takeImagesAgain = true
                        }

                        // validacion para reemplazar mensaje de error NO EXISTE IDENTIFICACION A VALIDAR
                        if (error.contains("NO EXISTE IDENTIFICACION A VALIDAR") || error.contains(
                                "ERROR INTERNO EN APLICACION, INTENTAR NUEVAMENTE"
                            )
                        ) {
                            titleError =
                                getString(R.string.sdk_pan_capture_back_txt_vote_id_not_legible)
                            error = getString(R.string.fragment_credential_bad_images)
                            takeImagesAgain = true
                        }

                        // validacion para reemplazar mensaje de error LA FOTOGRAFIA ES BORROSA FAVOR DE TOMAR UNA NUEVA, INTENTAR NUEVAMENTE
                        if (error.contains("LA FOTOGRAFIA ES BORROSA") ||
                            error.contains("LA IMAGEN NO CUMPLE LA CALIDAD REQUERIDA")
                        ) {
                            titleError =
                                getString(R.string.sdk_pan_capture_back_txt_vote_id_not_legible)
                            error = getString(R.string.fragment_credential_bad_images)
                            takeImagesAgain = true
                        }

                        if (takeImagesAgain) {
                            requireActivity().sendMessage(
                                SDK_PAN_MResponse(
                                    estado = SDK_Constants.TEST_ERROR,
                                    descripcion = error
                                )
                            )

                            takeImagesAgain(titleError, error, getString(R.string.lbl_retry))
                        } else {
                            messageOnError(
                                SDK_Constants.BACK_INE_STEP,
                                it.error,
                                it.error.second,
                                { actionFinish(it, SDK_Constants.BACK_INE_STEP) }
                            )
                        }
                    }
                }
            }
        }

        credentialViewModel.compare_face.observe(viewLifecycleOwner) {
            if (it != null) {
                Log.e("TAG", "initObservers: comparacion facial")
                when (it) {
                    is SDK_ResultValue.Success -> {
                        if (it.data.score >= findOb.confidence) {

                            verifyBiometricData = it.data
                            savePreferenceObject(
                                SDK_Constants.RES_FACIAL_VALIDATION,
                                verifyBiometricData
                            )

                            //Reiniciar contador de intentos
                            resetAttempts()
                            showProgress(false)
                            // Continuar flujo
                            goToOCRCaptureOk()
                        } else {
                            showProgress(false)
                            requireActivity().sendMessage(
                                SDK_PAN_MResponse(
                                    estado = SDK_Constants.TEST_ERROR,
                                    descripcion = getString(R.string.data_ine_bad_data_verification)
                                )
                            )
                            val (dTitle, bTitle, reachedAttempts) = retryAttemp()
                            val error = if (reachedAttempts) dTitle else "Error en la validación"
                            requireActivity().dialogErrorTestLife(
                                error,
                                getString(R.string.data_ine_bad_data_verification),
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
                        messageOnError(
                            SDK_Constants.TEST_LIFE_STEP,
                            it.error,
                            it.error.second,
                            { actionFinish(it, SDK_Constants.TEST_LIFE_STEP) }
                        )
                    }
                }

            }
        }
    }

    private fun goToNoticeTestLife() {
        onNavigate(R.id.action_nav_SDK_capture_back_ine_to_nav_SDK_Face_Capture_Notice)
    }

    private fun goToOCRCaptureOk() {
        onNavigate(R.id.action_nav_SDK_capture_back_ine_to_nav_SDK_FOCR_Complete)
    }

    private fun handleResponseINE(data: INEModel) {
        data.let {
            // LA INE DE TIPO C FUE VIGENTE HASTA 2023 SEGUN EL INE

            if (it.subTipo == "C") {
                showProgress(false)
                takeImagesAgain(
                    getString(R.string.sdk_pan_capture_back_txt_vote_id_is_not_valid),
                    getString(R.string.sdk_pan_capture_back_txt_vote_id_not_valid_for_register),
                    getString(R.string.lbl_retry)
                )

            } else if (!data.isOCRValid()) {
                takeImagesAgain(
                    getString(R.string.sdk_pan_capture_back_txt_vote_id_not_legible),
                    getString(R.string.fragment_credential_bad_images),
                    getString(R.string.lbl_retry)
                )
            } else if (!data.verifySameCredential()) {
                takeImagesAgain(
                    getString(R.string.sdk_pan_capture_back_txt_vote_id_not_consistent),
                    getString(R.string.sdk_pan_capture_back_txt_use_the_same_vote_id),
                    getString(R.string.lbl_retry)
                )
            } else {
                userINE = it.buildUserInfoModel()
                savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)
                FacialVerification()
            }
        }
    }

    fun getData() {
        modelTestLife = requireActivity().extractData()
        imgUser = getPreferenceObject<MTestLife>(SDK_Constants.RESULT_ANTISPOFFING)
        val imgBack = getImageTemp(requireContext(), imageBackTemp)

        userINE = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)

        if (imgBack == null) {
            binding.clZoneTakeImage.visibility = View.GONE
            isBackTaken = false

            binding.takeBackINEtxtTitle.text =
                getString(R.string.sdk_take_image_back_ine_title_take)
            binding.takeBackINEtxtSubTitle.text =
                getString(R.string.sdk_take_image_back_ine_subtitle_take)

            binding.btnEnter.text = getString(R.string.sdk_take_ine_txt_start)
        } else {
            binding.clZoneTakeImage.visibility = View.VISIBLE
            isBackTaken = true
            binding.noticeImg.setImageBitmap(imgBack)

            binding.takeBackINEtxtTitle.text =
                getString(R.string.sdk_take_image_back_ine_title_retake)
            binding.takeBackINEtxtSubTitle.text =
                getString(R.string.sdk_take_image_back_ine_subtitle_retake)
            binding.btnEnter.text = getString(R.string.sdk_take_ine_txt_continue)
            System.gc()
        }
        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }
    }

    private fun INEModel.buildUserInfoModel(): UserINEModel {
        val imgFront = getImageTemp(requireContext(), imageFrontTemp)
        val imgBack = getImageTemp(requireContext(), imageBackTemp)
        userINE = userINE?.copy(
            tipo = this.tipo,
            subTipo = this.subTipo,
            claveElector = this.claveElector,
            registro = this.registro,
            curp = this.curp,
            seccion = this.seccion,
            vigencia = this.vigencia,
            emision = this.emision,
            noEmision = this.registro,
            sexo = this.sexo,
            primerApellido = this.primerApellido,
            segundoApellido = this.segundoApellido,
            nombres = this.nombres ?: "",
            calle = this.calle,
            noInterior = "",
            noExterior = "",
            colonia = this.colonia,
            ciudad = this.ciudad,
            fechaNacimiento = this.fechaNacimiento,
            folio = this.folio,
            mrz = this.mrz,
            cic = this.cic,
            ocr = this.ocr,
            identificadorCiudadano = this.identificadorCiudadano,
            dataCredentialOk = true,
            curpVerified = false,
            base64Front = imgFront?.convertBitmapToBase64StringPNG(),
            base64Reverse = imgBack?.convertBitmapToBase64StringPNG(),
            referencia = this.referencia ?: "",
            credentiaVigency = obtenerAnioActual().toString()
        )

        return userINE!!
    }

    private fun FacialVerification() {
        //get face image
        val imageGet =
            getPreferenceObject<MTestLife>(SDK_Constants.RESULT_ANTISPOFFING)?.image ?: ""
        //realizar comparacion facial
        credentialViewModel.serviceFacialVerification(
            imageGet,
            userINE?.base64Front ?: "",
            findOb,
            requireContext()
        )
    }

    fun obtenerAnioActual(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}