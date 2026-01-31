package mx.com.iqsec.sdkpan.domain.repository.service_municipalities

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.MunicipalitiesRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class municipalities_UseCase() {
    suspend operator fun invoke(
        id_estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<List<MunicipalitiesModel>> {
        return MunicipalitiesRepositoryImpl().GetMunicipalities(
            id_estado,
            config,
            context
        )
    }
}