package mx.com.iqsec.sdkpan.presentation.sdk_welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.databinding.FSdkFPrivacyviewBinding
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FPrivacyView : SDK_PAN_BFragment() {
    private var _binding: FSdkFPrivacyviewBinding? = null
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FSdkFPrivacyviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    fun initListeners() {

        val webView = binding.vwTerms
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false

        webView.webViewClient = WebViewClient()
        val pdfUrl = getString(R.string.sdk_terms_privacy_txt_title_t2)
        loadPdfInWebView(pdfUrl)
    }

    private fun loadPdfInWebView(url: String) {
        binding.vwTerms.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        System.gc()
    }
}