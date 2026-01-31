package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class MTestLife(
    var image: String,
    var score: Double,
    var message: String,
    var codService: Int = 0,
    var messageService: String = "",
) : Serializable