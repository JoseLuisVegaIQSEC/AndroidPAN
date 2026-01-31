package mx.com.iqsec.sdkpan.data.repository

import mx.com.iqsec.sdkpan.domain.model.CloseFileModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface CloseFileRepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param base64Documento Is base64 image document
     * @param referencia Is reference string
     * @param tipoDto Is type document integer
     */
    suspend fun CloseFile(
        userData: UserINEModel,
        config: BaseConfig,
    ): SDK_ResultValue<CloseFileModel>
}