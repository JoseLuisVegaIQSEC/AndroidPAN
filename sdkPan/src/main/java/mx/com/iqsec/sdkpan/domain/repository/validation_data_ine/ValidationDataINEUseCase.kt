package mx.com.iqsec.sdkpan.domain.repository.validation_data_ine

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.ValidaDataINERepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class ValidationDataINEUseCase() {
    suspend operator fun invoke(
        cic: String,
        nombre: String,
        paterno: String,
        materno: String,
        claveElector: String,
        numeroEmision: String,
        curp: String,
        anioRegistro: String,
        anioEmision: String,
        latitud: Double,
        longitud: Double,
        ocr: String,
        codigopostal: String,
        ciudad: String,
        estado: Int,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<UserINEDataValidationModel> {
        return ValidaDataINERepositoryImpl().validationDataINE(
            cic,
            nombre,
            paterno,
            materno,
            claveElector,
            numeroEmision,
            curp,
            anioRegistro,
            anioEmision,
            latitud,
            longitud,
            ocr,
            codigopostal,
            ciudad,
            estado,
            config,
            context
        )
    }
}