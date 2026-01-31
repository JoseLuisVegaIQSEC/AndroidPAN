package mx.com.iqsec.sdkpan.data.repository

import android.content.Context
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

interface SaveDataUserRepository {
    /**
     * Fetches Challenge Create from a remote data source
     * @param frontalBase64 Is base64 image front
     * @param reversoBase64 Is base64 image back
     */
    suspend fun saveData(
        userData: UserINEModel,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<DataUserModel>
}