package mx.com.iqsec.sdkpan.domain.model

import androidx.annotation.Keep
import java.io.Serializable

@Keep
class ValidateMembershipModel(
    var existe: Boolean,
    val folioAfiliado: String?,
    val referencia: String?
) : Serializable