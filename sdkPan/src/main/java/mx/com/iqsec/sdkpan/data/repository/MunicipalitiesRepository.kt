package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface MunicipalitiesRepository {
    /**
     * Fetches Challenge Create from a remote data source
     */
    suspend fun GetMunicipalities(
        id_estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<MunicipalitiesModel>>
}