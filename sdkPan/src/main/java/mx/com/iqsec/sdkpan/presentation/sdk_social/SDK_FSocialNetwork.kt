package mx.com.iqsec.sdkpan.presentation.sdk_social

import android.content.Context
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorOCR
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkFsocialNetworkBinding
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FSocialNetwork : SDK_PAN_BFragment() {
    private var _binding: SdkFsocialNetworkBinding? = null
    private val binding get() = _binding!!
    private var onStateToolbar: onStateToolbarListener? = null
    private var userInfo: UserINEModel? = null
    private val saveDataVM: SDK_VM_SaveDataUser by viewModels()
    private var findOb = BaseConfig()

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
        _binding = SdkFsocialNetworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        saveDataVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showProgress(isLoading)
        }

        saveDataVM.saveDataResult.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is SDK_ResultValue.Success -> handleResponse(it.data)

                    is SDK_ResultValue.Error -> {
                        messageOnError(
                            SDK_Constants.SIGN_STEP,
                            it.error,
                            it.error.second,
                            { actionFinish(it, SDK_Constants.SIGN_STEP) }
                        )
                    }
                }
            }
        }
    }

    private fun handleResponse(result: DataUserModel) {
        savePreferenceObject(SDK_Constants.PARAM_PAN_CREDENTIAL, result)
        onNavigate(R.id.action_nav_FSocialNetwork_to_nav_FCredentialUser)
    }

    private fun getData() {
        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }

        userInfo = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.sdkPanSocialNetworkBtnSkip.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.SOCIAL_NETWORK_STEP) }
                return@setOnClickListener
            }

            saveSoicialNetworkData()
        }

        binding.sdkPanSocialNetworkBtnContinue.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.SOCIAL_NETWORK_STEP) }
                return@setOnClickListener
            }

            if (validateInputs())
                saveSoicialNetworkData()
        }

        validateInputs()
    }

    private fun validateInputs(): Boolean {
        var isValid = 0

        binding.apply {
            isValid += sdkPanSocialNetworkTIFacebook.Validate("Ingresa un usuario válido", 3)
            isValid += sdkPanSocialNetworkTIInstagram.Validate("Ingresa un usuario válido", 3)
            isValid += sdkPanSocialNetworkTITikTok.Validate("Ingresa un usuario válido", 3)
            isValid += sdkPanSocialNetworkTITwitter.Validate("Ingresa un usuario válido", 3)
        }

        return isValid == 0
    }

    private fun TextInputLayout.Validate(
        textError: String = "Este campo es requerido",
        minLength: Int = 0
    ): Int {
        //Validacion de campo requerido
        val inputText = this.editText?.text.toString()
        var hasError: Int = 0

        if (inputText.isNotEmpty() && minLength > 0 && inputText.length < minLength) {
            this.error = textError
            binding.sdkPanSocialNetworkBtnContinue.isEnabled = false
            hasError = 1
        } else {
            this.error = null
            hasError = 0
        }

        //text change listener para quitar el error al escribir
        this.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                if (!s.isNullOrEmpty()) {
                    this@Validate.error = null

                    if (s.length > minLength) {
                        binding.sdkPanSocialNetworkBtnContinue.isEnabled = true
                    }
                } else {
                    binding.sdkPanSocialNetworkBtnContinue.isEnabled = true
                }
            }
        })

        return hasError
    }

    private fun saveSoicialNetworkData() {
        userInfo = userInfo?.copy(
            socialFacebook = binding.sdkPanSocialNetworkTIFacebook.editText?.text.toString(),
            socialInstagram = binding.sdkPanSocialNetworkTIInstagram.editText?.text.toString(),
            socialTiktok = binding.sdkPanSocialNetworkTITikTok.editText?.text.toString(),
            socialTwitter = binding.sdkPanSocialNetworkTITwitter.editText?.text.toString()
        )

        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userInfo)
        saveUserInfo()
    }

    private fun saveUserInfo() {
        saveDataVM.saveUserData(
            userInfo!!,
            findOb,
            requireContext()
        )
    }
}