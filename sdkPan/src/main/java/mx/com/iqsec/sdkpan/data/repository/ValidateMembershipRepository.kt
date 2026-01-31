package mx.com.iqsec.sdkpan.data.repository

import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface ValidateMembershipRepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param base64Documento Is base64 image document
     * @param referencia Is reference string
     * @param tipoDto Is type document integer
     */
    suspend fun ValidateMembership(
        referencia: String,
        curp: String,
        config: BaseConfig,
    ): SDK_ResultValue<ValidateMembershipModel>
}