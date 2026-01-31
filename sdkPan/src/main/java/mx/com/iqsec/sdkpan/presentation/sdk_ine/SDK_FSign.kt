package mx.com.iqsec.sdkpan.presentation.sdk_ine

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorService
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.sendMessage
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.FSdkSignBinding
import mx.com.iqsec.sdkpan.domain.model.SaveSignModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FSign : SDK_PAN_BFragment() {
    private var _binding: FSdkSignBinding? = null
    private val binding get() = _binding!!

    private val vmSign: SDK_VMServiceINE by viewModels()
    private var findOb = BaseConfig()
    private var userINE: UserINEModel? = null
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
        _binding = FSdkSignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initListeners()
        initObservers()
    }

    private fun getData() {
        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }

        userINE = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)
    }

    private fun initObservers() {
        vmSign.isLoading.observe(viewLifecycleOwner) {
            showProgress(it)
        }

        vmSign.saveDataResult.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is SDK_ResultValue.Success -> handleSaveDataUser(it.data)

                    is SDK_ResultValue.Error -> {
                        messageOnError(
                            SDK_Constants.SIGN_STEP,
                            it.error,
                            it.error.second,
                            { actionFinish(it, SDK_Constants.SIGN_STEP) },
                            {}
                        )
                    }
                }
            }
        }
    }

    fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })


        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.SIGN_STEP) }
                return@setOnClickListener
            }

            val space = binding.llcanvas.getSignatureOccupationPercentage()

            if (space == 0f) {
                requireActivity().sendMessage(
                    SDK_PAN_MResponse(
                        estado = SDK_Constants.TEST_ERROR,
                        descripcion = getString(R.string.sdk_sign_txt_sign_required)
                    )
                )
                requireActivity().dialogErrorService(
                    getString(R.string.lbl_something_wrong),
                    getString(R.string.sdk_sign_txt_sign_required),
                    getString(R.string.lbl_retry)
                ) {

                }
            } else if (space < SDK_Constants.MIN_SIGNATURE_OCCUPATION) {
                requireActivity().sendMessage(
                    SDK_PAN_MResponse(
                        estado = SDK_Constants.TEST_ERROR,
                        descripcion = getString(R.string.sdk_sign_txt_sign_to_small)
                    )
                )
                requireActivity().dialogErrorService(
                    getString(R.string.lbl_something_wrong),
                    getString(R.string.sdk_sign_txt_sign_to_small),
                    getString(R.string.lbl_retry)
                ) {
                    binding.llcanvas.clearCanvas()
                }
            } else {
                saveUserData()
            }
        }

        binding.llcanvas.onOccupationChanged = { space ->
            binding.btnEnter.isEnabled = space >= SDK_Constants.MIN_SIGNATURE_OCCUPATION
        }

        binding.btnClenanSign.setOnClickListener {
            binding.llcanvas.clearCanvas()
        }

        binding.llcanvas.disableScroll = {
            binding.sdkSignNSVContent.requestDisallowInterceptTouchEvent(it)
        }
    }

    private fun saveUserData() {
        if (userINE == null) return

        //Guardar la firma en base64 en preferencias
        val imageB64 = binding.llcanvas.b.signToPNGString()
        savePreferenceString(SDK_Constants.IMG_SIGNATURE, imageB64.toString())

        userINE = userINE?.copy(firma = imageB64)
        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)

        //reintentar firma
        if (imageB64 == null) {
            requireActivity().dialogErrorService(
                getString(R.string.lbl_something_wrong),
                getString(R.string.sdk_sign_txt_sign_to_small),
                getString(R.string.lbl_retry)
            ) {
                binding.llcanvas.clearCanvas()
            }
        } else {
            vmSign.serviceSaveSign(
                findOb,
                imageB64
            )
        }
    }

    private fun handleSaveDataUser(data: SaveSignModel) {
        //Reiniciar contador de intentos
        savePreferenceString(SDK_Constants.ATTEMPS_SIGN, "0")

        //Guardado de folio y referencia
        userINE = userINE?.copy(folio = data.folio ?: 0, referencia = data.referencia ?: "")
        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)

        findOb = findOb.copy(reference = data.referencia ?: "")
        saveConf(SDK_Constants.BASE_CONF, findOb)

        onNavigate(R.id.action_nav_SDK_sign_user_to_nav_SDK_Face_Capture_Notice)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        System.gc()
    }
}