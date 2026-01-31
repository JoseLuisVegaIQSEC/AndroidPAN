package mx.com.iqsec.sdkpan.model

import java.io.Serializable

data class InitializerTestLife(
    var numFrames: Int = 3,
    var enableBrighnessValidation: Boolean = true,
    var enableTwoFacesValidation: Boolean = true,
    var enableExtraBrightness: Boolean = false,
    var url_services: String = "",
    var timeInactivitySeconds: Int = 300,
    var confidence: Double = 0.0,
    var timeoutStart: Int = 120,
    var timeoutDialog: Int = 23
) : Serializable