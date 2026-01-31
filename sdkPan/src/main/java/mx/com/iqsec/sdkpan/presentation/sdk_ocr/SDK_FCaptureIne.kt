package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.commit
import mx.com.iqsec.auto_detection_ine.modules.captueocr.models.sdk_takePicture
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants
import mx.com.iqsec.auto_detection_ine.presentation.sdk_capture_ine
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.DialogAction
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogCancel
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkFcaptureineBinding
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FCaptureIne : SDK_PAN_BFragment(), sdk_capture_ine.sdk_capture_ineListener {
    private var _binding: SdkFcaptureineBinding? = null
    private val binding get() = _binding!!

    private lateinit var fCaptureINE: sdk_capture_ine
    private lateinit var takePicture: sdk_takePicture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SdkFcaptureineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.btnCancel.setOnClickListener {
            requireActivity().dialogCancel(
                requireContext(),
                "",
                getString(R.string.ocr_ine_txt_cancel),
                getString(R.string.dialog_txt_si),
                getString(R.string.dialog_txt_no),
                0
            ) {
                when (it) {
                    DialogAction.ClickBlue -> cancelCapturebyUser()
                    DialogAction.ClickWhite -> {}
                    DialogAction.AutoDismiss -> {}
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

        fCaptureINE = sdk_capture_ine()
        fCaptureINE.listener = this
        fCaptureINE.arguments = arguments
        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.sdk_a_fade_in,
                R.anim.sdk_a_fade_out,
                R.anim.sdk_a_fade_in,
                R.anim.sdk_a_fade_out
            )
            replace(R.id.fDdkValidate, fCaptureINE)
        }

        getData()

        binding.sdkPanTypeCaptureTitle.text =
            if (takePicture.type_capture == CDConstants.TYPE_CAPTURE_FRONT) {
                getString(R.string.sdk_take_image_front_ine_title_take)
            } else {
                getString(R.string.sdk_take_image_back_ine_title_take)
            }
    }

    private fun getData() {
        takePicture = arguments?.getSerializable(CDConstants.TAKE_IMAGE) as sdk_takePicture
    }


    private fun cancelCapturebyUser() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.TEST_CALCEL_USER,
            descripcion = getString(R.string.txt_cancel_by_user),
            no_transaccion = requireActivity().makeFolio(SDK_Constants.BACK_INE_STEP)
        )
        requireActivity().returnResponse(data, SDK_Constants.TEST_CALCEL_USER)
    }

    override fun cancelbyUser() {
        if (takePicture.type_capture == CDConstants.TYPE_CAPTURE_FRONT) {
            onNavigate(R.id.action_nav_SDK_FCaptureIne_to_nav_SDK_capture_front_ine)
        } else {
            onNavigate(R.id.action_nav_SDK_FCaptureIne_to_nav_SDK_capture_back_ine)
        }
    }

    override fun endCaptureINE(type_capture: Int) {
        if (takePicture.type_capture == CDConstants.TYPE_CAPTURE_FRONT) {
            onNavigate(R.id.action_nav_SDK_FCaptureIne_to_nav_SDK_capture_front_ine)
        } else {
            onNavigate(R.id.action_nav_SDK_FCaptureIne_to_nav_SDK_capture_back_ine)
        }
    }
}