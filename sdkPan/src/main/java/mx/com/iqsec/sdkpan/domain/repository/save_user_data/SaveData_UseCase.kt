package mx.com.iqsec.sdkpan.domain.repository.save_user_data

import android.content.Context
import mx.com.iqsec.sdkpan.data.repository.Impl.SaveDataUserRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SaveData_UseCase() {
    suspend operator fun invoke(
        userData: UserINEModel,
        config: BaseConfig,
        context: Context
    ): SDK_ResultValue<DataUserModel> {
        return SaveDataUserRepositoryImpl().saveData(
            userData,
            config,
            context
        )
    }
}