package mx.com.iqsec.sdkpan.presentation.sdk_welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.PermissionManager
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.extractData
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.FSdkWelcomeBinding
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.InitializerTestLife
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FWelcome : SDK_PAN_BFragment() {
    private var _binding: FSdkWelcomeBinding? = null
    private val binding get() = _binding!!
    var modelTestLife = InitializerTestLife()
    private lateinit var permissionManager: PermissionManager
    private var onStateToolbar: onStateToolbarListener? = null
    private lateinit var selectedRole: String
    private var userINE: UserINEModel? = null

    override fun onDetach() {
        super.onDetach()
        onStateToolbar = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onStateToolbar = context as? onStateToolbarListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FSdkWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getData()
        initListeners()
        styleText()
    }

    private fun getData() {
        //Limpieza de preferencias
        deleteImageTemp(requireContext(), imageFrontTemp)
        deleteImageTemp(requireContext(), imageBackTemp)
        cleanPreferences()

        saveConfig()
    }

    private fun styleText() {
        val text = resources.getString(R.string.sdk_pan_welcome_terns)
        var spannableString = android.text.SpannableString(text)
        val words = listOf("Autorizo la recolección y")

        words.forEach { word ->
            spannableString = makeSpanColor(text, word, spannableString)
        }
        binding.sdkPanWelcomecbTerms.text = spannableString
    }

    private fun makeSpanColor(
        baseText: String,
        textSpan: String,
        spannableString: android.text.SpannableString
    ): android.text.SpannableString {
        val colorStyle = android.text.style.ForegroundColorSpan(
            ContextCompat.getColor(requireContext(), R.color.sdk_pan_text_h3_blue)
        )
        val appearanceSpan =
            android.text.style.TextAppearanceSpan(requireContext(), R.style.sdk_pan_text_h7_blue)

        val startIndex = baseText.indexOf(textSpan)
        val endIndex = startIndex + textSpan.length
        spannableString.setSpan(
            appearanceSpan,
            startIndex,
            endIndex,
            android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    override fun onResume() {
        super.onResume()
        onStateToolbar?.activityUpdateToolbar()
    }

    private fun initListeners() {
        permissionManager = PermissionManager(this)
        modelTestLife = requireActivity().extractData()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.sdPanWelcomeBtnStart.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it,SDK_Constants.WELCOME_STEP) }
                return@setOnClickListener
            }
            checkPermisins()
        }

        binding.sdkPanWelcomecbTerms.setOnCheckedChangeListener { comp, isChecked ->
            val rg = binding.sdkPanWelcomeRGChooseRole
            val selectedRB = rg.checkedRadioButtonId != -1

            binding.sdPanWelcomeBtnStart.isEnabled = if (selectedRB) isChecked else false
        }

        binding.sdkPanWelcomeTxtPrivacy.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it,SDK_Constants.WELCOME_STEP) }
                return@setOnClickListener
            }
            onNavigate(R.id.action_Nav_SDK_Welcome_User_Fragment_to_nav_SDK_FPrivacyView)
        }

        binding.sdkPanWelcomeRGChooseRole.setOnCheckedChangeListener { group, checkedId ->
            val selectedRB = checkedId != -1
            val isChecked = binding.sdkPanWelcomecbTerms.isChecked
            if (binding.sdkPanWelcomeRBtnCandidate.isChecked) {
                selectedRole = "Militante"
            }

            if (binding.sdkPanWelcomeRBtnForeignCandidate.isChecked) {
                selectedRole = "Militante extranjero"
            }

            binding.sdPanWelcomeBtnStart.isEnabled = if (selectedRB) isChecked else false
        }
    }

    private fun checkPermisins() {
        goToSign()
    }

    private fun goToSign() {
        //guarado de perfil
        userINE = UserINEModel(
            perfil = selectedRole
        )
        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)
        resetAttempts()
        onNavigate(R.id.action_Nav_SDK_Welcome_User_Fragment_to_nav_SDK_sign_user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        System.gc()
    }
}