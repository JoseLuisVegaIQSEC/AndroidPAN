package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.getImageTemp
import mx.com.iqsec.auto_detection_ine.modules.captueocr.models.sdk_takePicture
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.FSdkOcrFrrontBinding
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FOCRFront : SDK_PAN_BFragment() {
    private var _binding: FSdkOcrFrrontBinding? = null
    private val binding get() = _binding!!

    private var isFrontTaken = false
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
        _binding = FSdkOcrFrrontBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initListeners()
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it,SDK_Constants.FRONT_INE_STEP) }
                return@setOnClickListener
            }
            if (!isFrontTaken) {
                takeImageFrontIne()
            } else {
                onNavigate(R.id.action_SDK_capture_front_ine_to_SDK_capture_back_ine)
            }
        }

        binding.clZoneTakeImage.setOnClickListener {
            takeImageFrontIne()
        }
    }

    private fun cancelbyUser() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.TEST_CALCEL_USER,
            descripcion = getString(R.string.txt_cancel_by_user),
            no_transaccion = requireActivity().makeFolio(SDK_Constants.FRONT_INE_STEP)
        )

        requireActivity().returnResponse(data, SDK_Constants.TEST_CALCEL_USER)
    }

    private fun takeImageFrontIne() {
        val data = Bundle()
        val mtakeImg = sdk_takePicture(
            SDK_Constants.INE_FRONTAL,
            getString(R.string.sdk_take_image_front_ine_title),
            SDK_Constants.TYPE_CAPTURE_FRONT
        )
        data.putSerializable(SDK_Constants.TAKE_IMAGE, mtakeImg)
        onNavigate(R.id.action_nav_SDK_capture_front_ine_to_nav_SDK_FCaptureIne, data)
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

    private fun getData() {
        val imgFront = getImageTemp(requireContext(), imageFrontTemp)
        if (imgFront == null) {
            isFrontTaken = false
            binding.clZoneTakeImage.visibility = View.GONE

            binding.takeFrontINEtxtTitle.text =
                getString(R.string.sdk_take_image_front_ine_title_take)
            binding.takeFrontINEtxtSubTitle.text =
                getString(R.string.sdk_take_image_front_ine_subtitle_take)

            binding.btnEnter.text = getString(R.string.sdk_take_ine_txt_start)
        } else {
            binding.noticeImg.setImageBitmap(imgFront)
            isFrontTaken = true
            binding.clZoneTakeImage.visibility = View.VISIBLE

            binding.takeFrontINEtxtTitle.text =
                getString(R.string.sdk_take_image_front_ine_title_retake)
            binding.takeFrontINEtxtSubTitle.text =
                getString(R.string.sdk_take_image_front_ine_subtitle_retake)

            binding.btnEnter.text = getString(R.string.sdk_take_ine_txt_continue)
            System.gc()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}