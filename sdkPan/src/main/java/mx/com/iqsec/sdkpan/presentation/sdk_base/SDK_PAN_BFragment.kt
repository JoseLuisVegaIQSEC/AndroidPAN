package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorService
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogNetworkError
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.extractData
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.sendMessage
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants.GLOBAL_ATTEMPS
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import java.lang.Thread.sleep

open class SDK_PAN_BFragment : Fragment() {
    val baseActivity by lazy { requireActivity() as SDK_PAN_BActivity }
    val scopeDF = CoroutineScope(Dispatchers.Default)
    var isDialogOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity.initializeBase(requireContext())
    }

    // Funciones delegadas a SDK_BActivity
    fun saveConf(key: String, array: BaseConfig) {
        baseActivity.saveConf(key, array)
    }

    fun getConf(key: String): BaseConfig? {
        return baseActivity.getConf(key)
    }

    fun onNavigate(idComponent: Int, bundle: Bundle? = null) {
        view?.let { Navigation.findNavController(it).navigate(idComponent, bundle) }
    }

    fun removePreference(key: String) {
        baseActivity.removePreference(key)
    }

    fun savePreferenceString(key: String, value: String) {
        baseActivity.savePreferenceString(key, value)
    }

    fun getPreferenceString(key: String, defaultValue: String): String? {
        return baseActivity.getPreferenceString(key, defaultValue)
    }

    inline fun <reified T> savePreferenceObject(key: String, obj: T? = null) {
        baseActivity.savePreferenceObject(key, obj)
    }

    inline fun <reified T> getPreferenceObject(key: String): T? {
        return baseActivity.getPreferenceObject(key)
    }

    fun cleanPreferences() {
        baseActivity.cleanPreferences()
    }

    fun Bitmap.convertBitmapToBase64StringPNG(quality: Int = 50): String? {
        return baseActivity.run {
            this@convertBitmapToBase64StringPNG.convertBitmapToBase64StringPNG(
                quality
            )
        }
    }

    fun Bitmap.signToPNGString(): String? {
        return baseActivity.run { this@signToPNGString.signToPNGString() }
    }

    fun showProgress(show: Boolean = true, message: String? = null) {
        Log.e("SDK_PAN_BFragment", "showProgress: $show")
        val activity = activity ?: return
        if (activity.isFinishing) return
        baseActivity.showProgress(show, message)
        isDialogOpen = baseActivity.isDialogOpen
    }

    fun getImgUser(): String {
        return baseActivity.getImgUser()
    }

    fun getImgFront(): String {
        return baseActivity.getImgFront()
    }

    fun getImgBack(): String {
        return baseActivity.getImgBack()
    }

    fun getUserScore(): Double {
        return baseActivity.getUserScore()
    }

    fun getPOcrINE(): String {
        return baseActivity.getPOcrINE()
    }

    fun getPSendServiceINE(): String {
        return baseActivity.getPSendServiceINE()
    }

    fun getPResponseServiceINE(): String {
        return baseActivity.getPResponseServiceINE()
    }

    fun saveConfig() {
        val modelTestLife = requireActivity().extractData()

        saveConf(
            SDK_Constants.BASE_CONF, BaseConfig(
                numFrames = modelTestLife.numFrames,
                enableBrighnessValidation = modelTestLife.enableBrighnessValidation,
                enableTwoFacesValidation = modelTestLife.enableTwoFacesValidation,
                enableExtraBrightness = modelTestLife.enableExtraBrightness,
                servicesUrl = modelTestLife.url_services,
                confidence = modelTestLife.confidence,
                timeoutStart = modelTestLife.timeoutStart,
                timeoutDialog = modelTestLife.timeoutDialog
            )
        )
    }

    fun messageOnError(
        stepConstants: Int,
        error: Pair<String, String>,
        messageService: String,
        actionFinish: (String) -> Unit,
        retryAction: (() -> Unit)? = null
    ) {
        val _error = error.second

        requireActivity().sendMessage(
            SDK_PAN_MResponse(
                descripcion = messageService,
                estado = SDK_Constants.TEST_ERROR,
                no_transaccion = requireActivity().makeFolio(stepConstants)
            )
        )

        val (dTitle, bTitle, reachedAttempts) = retryAttemp()
        val titleError = if (reachedAttempts) dTitle else error.first

        requireActivity().dialogErrorService(
            titleError,
            _error,
            bTitle
        ) { if (reachedAttempts) actionFinish(titleError) else retryAction?.invoke() }
    }

    fun handleConectionAttemps(reachedAttemps: (String) -> Unit) {
        val (dTitle, bTitle, reachedAttempts) = retryAttemp()
        if (reachedAttempts.not()) {
            requireActivity().dialogNetworkError() {}
        } else {
            requireActivity().dialogErrorService(
                dTitle,
                getString(R.string.fragment_dialog_network_message),
                bTitle
            ) { reachedAttemps(getString(R.string.fragment_dialog_network_message)) }
        }
    }

    fun retryAttemp(): Triple<String, String, Boolean> {
        val maxAttempts = getAttempts()
        if (maxAttempts < SDK_Constants.MAX_ATTEMPS) {
            addAttempt()
            return Triple(
                getString(R.string.lbl_service_not_available),
                getString(R.string.lbl_retry),
                false
            )
        } else {
            return Triple(
                getString(R.string.lbl_txt_max_attempts_reached),
                getString(R.string.lbl_close),
                true
            )
        }
    }

    fun getAttempts(): Int {
        val attemptsStr = getPreferenceString(GLOBAL_ATTEMPS, "0")
        return attemptsStr?.toIntOrNull() ?: 0
    }

    fun addAttempt() {
        val currentAttempts = getAttempts()
        val newAttempts = currentAttempts + 1
        savePreferenceString(GLOBAL_ATTEMPS, newAttempts.toString())
    }

    fun resetAttempts() {
        savePreferenceString(GLOBAL_ATTEMPS, "0")
    }

    fun actionFinish(message: String, stepConstants: Int, state: Int = SDK_Constants.TEST_ERROR) {
        sleep(400)

        val data = SDK_PAN_MResponse(
            descripcion = message,
            estado = state,
            no_transaccion = requireActivity().makeFolio(stepConstants)
        )
        cleanPreferences()
        deleteImageTemp(requireContext(), imageFrontTemp)
        deleteImageTemp(requireContext(), imageBackTemp)
        requireActivity().returnResponse(data, state)
    }
}
