package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class VerifyFramesModel (
    var estado: Int = 0,
    var descripcion: String?,
    var score: Double = -1.0
):Serializable