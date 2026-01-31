package mx.com.iqsec.sdkpan.model

import java.io.Serializable

data class BaseConfig(
    val idx: Int = 0,
    var numFrames: Int = 3,
    var enableBrighnessValidation: Boolean = true,
    var enableTwoFacesValidation: Boolean = true,
    var enableExtraBrightness: Boolean = false,
    var servicesUrl: String = "",
    var entity: String = "",
    var user: String = "",
    var password: String = "",
    var reference: String = "",
    var confidence: Double = 0.0,
    var timeoutStart: Int = 120,
    var timeoutDialog: Int = 23
) : Serializable {

}