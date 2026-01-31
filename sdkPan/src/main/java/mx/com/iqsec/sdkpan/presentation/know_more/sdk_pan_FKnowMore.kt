package mx.com.iqsec.sdkpan.presentation.know_more

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkPanFknowMoreBinding
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class sdk_pan_FKnowMore : SDK_PAN_BFragment() {
    private var _binding: SdkPanFknowMoreBinding? = null
    private val binding get() = _binding!!
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SdkPanFknowMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.sdkPanFKnowMoreBtnUnipan.setOnClickListener {
            openSiteInternalBrowser("https://unipan.mx")
        }

        binding.sdkPanFKnowMoreBtnCandidate.setOnClickListener {
            openSiteInternalBrowser("https://applicants.z41.web.core.windows.net")
        }

        binding.sdkPanFKnowMoreBtnFinish.setOnClickListener {
            actionFinish()
        }
    }

    private fun actionFinish() {

        val data = SDK_PAN_MResponse(
            descripcion = getString(R.string.validation_ok),
            estado = SDK_Constants.TEST_FINISH,
            no_transaccion = requireActivity().makeFolio(SDK_Constants.FINISH_STEP)
        )
        cleanPreferences()
        deleteImageTemp(requireContext(), imageFrontTemp)
        deleteImageTemp(requireContext(), imageBackTemp)
        requireActivity().returnResponse(data, SDK_Constants.TEST_FINISH)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openSiteInternalBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)

    }
}