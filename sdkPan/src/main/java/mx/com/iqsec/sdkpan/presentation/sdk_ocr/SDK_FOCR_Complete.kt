package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogNetworkError
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkFocrcompleteBinding
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FOCR_Complete : SDK_PAN_BFragment() {
    private var _binding: SdkFocrcompleteBinding? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SdkFocrcompleteBinding.inflate(inflater, container, false)
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

        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.INE_OK_STEP) }
                return@setOnClickListener
            }

            onNavigate(R.id.action_nav_SDK_FOCR_Complete_to_nav_UpdateData)
        }
    }
}