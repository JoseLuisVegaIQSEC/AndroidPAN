package mx.com.iqsec.sdkpan.domain.repository.validate_membership

import mx.com.iqsec.sdkpan.data.repository.Impl.ValidateMembershipRepositoryImpl
import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class Validate_MembershipUseCase() {
    suspend operator fun invoke(
        referencia: String,
        curp: String,
        config: BaseConfig,
    ): SDK_ResultValue<ValidateMembershipModel> {
        return ValidateMembershipRepositoryImpl().ValidateMembership(
            referencia,
            curp,
            config
        )
    }
}