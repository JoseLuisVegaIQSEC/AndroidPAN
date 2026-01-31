package mx.com.iqsec.sdkpan.domain.repository.close_file

import mx.com.iqsec.sdkpan.data.repository.Impl.CloseFileRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.CloseFileModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class Close_FileUseCase() {
    suspend operator fun invoke(
        userData: UserINEModel,
        config: BaseConfig,
    ): SDK_ResultValue<CloseFileModel> {
        return CloseFileRepositoryImpl().CloseFile(
            userData,
            config
        )
    }
}