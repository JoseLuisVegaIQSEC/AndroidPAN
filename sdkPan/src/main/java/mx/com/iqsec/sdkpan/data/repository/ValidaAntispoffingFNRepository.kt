package mx.com.iqsec.sdkpan.data.repository

import mx.com.iqsec.sdkpan.domain.model.AntispoffingDataModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface ValidaAntispoffingFNRepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param apikey Apikey para entrar a la API
     * @param imgs_base64 Is base64 image
     */
    suspend fun ValidaAntispoffiingFN(
        imgs_base64: ArrayList<String>,
        config: BaseConfig
    ): SDK_ResultValue<AntispoffingDataModel>
}