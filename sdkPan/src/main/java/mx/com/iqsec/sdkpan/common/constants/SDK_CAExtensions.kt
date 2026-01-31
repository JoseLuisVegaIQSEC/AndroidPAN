package mx.com.iqsec.sdkpan.common.constants

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.model.InitializerTestLife
import mx.com.iqsec.sdkpan.presentation.SDK_PAN
import mx.com.iqsec.sdkpan.presentation.dialog.DialogManager
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_DACancel
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_DAError
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_DAPermissions
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_DATimeOut
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_Pan_DOk
import mx.com.iqsec.sdkpan.presentation.sdk_base.TypeDialog
import kotlin.random.Random

object SDK_CAExtensions {

    const val BROADCAST_ERRORS = "mx.com.iqsec.sdkpan.BCMESAGGEERROR"

    fun Activity.dialogError(
        title: String? = null,
        message: String,
        titleBtn: String? = null,
        autoDismiss: Boolean = false,
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_DAError(this, title, message, titleBtn, closeDg, autoDismiss)
        DialogManager.show(dialog)
    }

    fun Activity.dialogErrorService(
        title: String? = null,
        message: String,
        titleBtn: String? = null,
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_Pan_DOk(this, TypeDialog.ERROR_SERVICE, title, message, titleBtn, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.dialogErrorTestLife(
        title: String? = null,
        message: String,
        titleBtn: String? = null,
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_Pan_DOk(this, TypeDialog.ERROR_TESTLIFE, title, message, titleBtn, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.dialogErrorOCR(
        title: String? = null,
        message: String,
        titleBtn: String? = null,
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_Pan_DOk(this, TypeDialog.ERROR_OCR, title, message, titleBtn, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.dialogUserExist(
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_Pan_DOk(
            this, TypeDialog.USER_EXISTS,
            resources.getString(R.string.sdk_pan_f_update_data_user_exist_title),
            resources.getString(R.string.sdk_pan_f_update_data_user_exist_message),
            resources.getString(R.string.sdk_pan_f_update_data_user_exist_my_profile),
            closeDg
        )
        DialogManager.show(dialog)
    }

    fun Activity.dialogNetworkError(
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_Pan_DOk(
            this, TypeDialog.NETWORK_ERROR,
            getString(R.string.fragment_dialog_network_title),
            getString(R.string.fragment_dialog_network_message),
            getString(R.string.fragment_dialog_network_button), closeDg
        )
        DialogManager.show(dialog)
    }

    fun Activity.dialogNoticeTimeOut(
        closeDg: () -> Unit
    ) {
        if (this.isFinishing) return
        val dialog = SDK_DATimeOut(this, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.DialogLocationEnabled(
        titleBtn: String? = null,
        closeDg: () -> Unit,
        GoToPrivacyView: () -> Unit,
    ) {
        val dialog = SDK_DAPermissions(
            this,
            titleBtn,
            closeDg,
            GoToPrivacyView
        )
        DialogManager.show(dialog)
    }

    fun Activity.DialogPermisionGeolocation(
        titleBtn: String? = null,
        title: String? = null,
        message: String? = null,
        closeDg: () -> Unit
    ) {
        val dialog = SDK_Pan_DOk(this, TypeDialog.PERMISSIONS, title, message, titleBtn, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.dialogOk(
        title: String,
        message: String,
        titleBtn: String? = null,
        closeDg: () -> Unit,
    ) {
        val dialog = SDK_Pan_DOk(this, TypeDialog.OK, title, message, titleBtn, closeDg)
        DialogManager.show(dialog)
    }

    fun Activity.dialogCancel(
        context: Context,
        title: String = "",
        message: String,
        messageWhiteButton: String,
        messageBlueButton: String,
        timmeDismiss: Int,
        actionButton: (DialogAction) -> Unit
    ) {
        val dialog = SDK_DACancel(
            context,
            title,
            message,
            messageWhiteButton,
            messageBlueButton,
            actionButton,
            timmeDismiss
        )
        DialogManager.show(dialog)
    }

    fun Fragment.sdk_feedbackStatus(data: SDK_PAN_MResponse) {
        val bundle = Bundle()
        bundle.putSerializable("res", data)

        setFragmentResult("res", bundle)
    }

    fun Activity.extractData(): InitializerTestLife {
        val arguments = this.intent.extras
        if (arguments != null && arguments.size() != 0) {
            return arguments.getSerializable("ConfigTestLife") as InitializerTestLife
        }
        return InitializerTestLife()
    }

    fun Activity.returnResponse(data: SDK_PAN_MResponse, codResposne: Int) {
        val intent = Intent(this, SDK_PAN::class.java)
        val bundle = Bundle()
        bundle.putSerializable("res", data)
        intent.putExtras(bundle)
        this.setResult(codResposne, intent)
        this.finish()
    }

    fun Activity.makeFolio(number: Int): String {
        val timestamp = System.currentTimeMillis()
        val randomString = buildString {
            for (i in 1..6) {
                val randomIndex =
                    Random.nextInt(3) // 0: letra minúscula, 1: letra mayúscula, 2: número
                when (randomIndex) {
                    0 -> append(('a'..'z').random())
                    1 -> append(('A'..'Z').random())
                    2 -> append(('0'..'9').random())
                }
            }
        }
        val result = "${timestamp}_${randomString}_${number}"
        return result
    }

    fun Activity.sendMessage(response: SDK_PAN_MResponse) {
        val intent = Intent(BROADCAST_ERRORS)
        intent.putExtra("res", response)
        this.sendBroadcast(intent)
    }

    fun checkLocationStatus(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
}