package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.data.di.manager.SDK_SPManager
import mx.com.iqsec.sdkpan.domain.model.MTestLife
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.presentation.dialog.SDK_DLoader
import java.io.ByteArrayOutputStream

open class SDK_PAN_BActivity : AppCompatActivity() {
    lateinit var sharedPreferencesManager: SDK_SPManager
    private var dialogLoading: SDK_DLoader? = null
    val scopeDF = CoroutineScope(Dispatchers.Default)
    var isDialogOpen = false

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun initializeBase(context: Context) {
        sharedPreferencesManager = SDK_SPManager(context)
    }

    fun saveConf(key: String, array: BaseConfig) {
        return sharedPreferencesManager.saveConf(key, array)
    }

    fun getConf(key: String): BaseConfig? {
        return sharedPreferencesManager.getConf(key)
    }

    fun onNavigate(view: View, idComponent: Int, bundle: Bundle? = null) {
        Navigation.findNavController(view).navigate(idComponent, bundle)
    }

    fun removePreference(key: String) {
        sharedPreferencesManager.removePreference(key)
    }

    fun savePreferenceString(key: String, value: String) {
        sharedPreferencesManager.saveString(key, value)
    }

    fun getPreferenceString(key: String, defaultValue: String): String? {
        return sharedPreferencesManager.getString(key, defaultValue)
    }

    inline fun <reified T> savePreferenceObject(key: String, obj: T? = null) {
        sharedPreferencesManager.saveEncryptedObject(key, obj)
    }

    inline fun <reified T> getPreferenceObject(key: String): T? {
        return sharedPreferencesManager.getEncryptedObject(key)
    }

    fun cleanPreferences() {
        sharedPreferencesManager.clearAllSharedPreferences()
    }

    fun Bitmap.convertBitmapToBase64StringPNG(quality: Int = 50): String? {
        return try {
            val baos = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            val byteArrayImage = baos.toByteArray()
            Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        } catch (exception: OutOfMemoryError) {
            null
        }
    }

    fun Bitmap.signToPNGString(): String? {
        return try {
            val baos = ByteArrayOutputStream()
            this.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val byteArrayImage = baos.toByteArray()
            Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
        } catch (exception: Exception) {
            null
        }
    }

    fun showProgress(show: Boolean = true, message: String? = null) {
        if (show == true) {
            dialogLoading = SDK_DLoader(this, message)

            if (dialogLoading?.isShowing == false) {
                isDialogOpen = true
                if (this?.isFinishing == true || this?.isDestroyed == true) return
                dialogLoading?.show()
            }
        } else {
            dialogLoading?.dismiss()
            isDialogOpen = false
        }
    }

    fun getImgUser(): String {
        val imgUser = getPreferenceObject<MTestLife>(SDK_Constants.RESULT_ANTISPOFFING)
        return imgUser?.image ?: ""
    }

    fun getImgFront(): String {
        val ineData = getPreferenceObject<UserINEModel>(SDK_Constants.PARAM_CREDENTIAL)
        return ineData?.base64Front ?: ""
    }

    fun getImgBack(): String {
        val ineData = getPreferenceObject<UserINEModel>(SDK_Constants.PARAM_CREDENTIAL)
        return ineData?.base64Reverse ?: ""
    }

    fun getUserScore(): Double {
        val ineData = getPreferenceObject<VerifyFramesModel>(SDK_Constants.RES_FACIAL_VALIDATION)
        return ineData?.score ?: 0.0
    }

    fun getPOcrINE(): String {
        return getPreferenceString(SDK_Constants.RES_OCR_VALIDATION, "") ?: ""
    }

    fun getPSendServiceINE(): String {
        return getPreferenceString(SDK_Constants.SEND_SERVICE_INE_VALIDATION, "") ?: ""
    }

    fun getPResponseServiceINE(): String {
        return getPreferenceString(SDK_Constants.RES_SERVICE_INE_VALIDATION, "") ?: ""
    }
}
