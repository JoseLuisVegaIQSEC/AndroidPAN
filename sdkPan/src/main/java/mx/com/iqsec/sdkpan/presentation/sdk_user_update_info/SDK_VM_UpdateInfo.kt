package mx.com.iqsec.sdkpan.presentation.sdk_user_update_info

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.repository.service_municipalities.municipalities_UseCase
import mx.com.iqsec.sdkpan.domain.repository.service_states.states_UseCase
import mx.com.iqsec.sdkpan.domain.repository.validate_membership.Validate_MembershipUseCase
import mx.com.iqsec.sdkpan.domain.repository.validation_data_ine.ValidationDataINEUseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VM_UpdateInfo() : ViewModel() {
    private val statesUseCase = states_UseCase()
    private val municipalitiesUseCase = municipalities_UseCase()
    private val validateMemberUseCase = Validate_MembershipUseCase()
    private val validationDataINE = ValidationDataINEUseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _stateList =
        MutableLiveData<SDK_ResultValue<List<StatesModel>>>().apply { value = null }
    val stateList: LiveData<SDK_ResultValue<List<StatesModel>>> = _stateList

    private val _municipalitiesData =
        MutableLiveData<SDK_ResultValue<List<MunicipalitiesModel>>>().apply { value = null }
    val municipalitiesData: LiveData<SDK_ResultValue<List<MunicipalitiesModel>>> =
        _municipalitiesData

    private val _membershipData =
        MutableLiveData<SDK_ResultValue<ValidateMembershipModel>>().apply { value = null }
    val membershipData: LiveData<SDK_ResultValue<ValidateMembershipModel>> =
        _membershipData

    private val _ValidationINEData =
        MutableLiveData<SDK_ResultValue<UserINEDataValidationModel>>().apply { value = null }
    val ValidationINEData: LiveData<SDK_ResultValue<UserINEDataValidationModel>> =
        _ValidationINEData

    fun getStates(
        config: BaseConfig,
        context: Context
    ) {
        viewModelScope.launch {
            val response = statesUseCase.invoke(
                config,
                context
            )
            _stateList.postValue(response)
        }
    }

    fun getMunicipalities(
        stateId: Int,
        config: BaseConfig,
        context: Context
    ) {
        viewModelScope.launch {
            val response = municipalitiesUseCase.invoke(
                stateId,
                config,
                context
            )
            _municipalitiesData.postValue(response)
        }
    }

    fun serviceMembership(
        referencia: String,
        curp: String,
        config: BaseConfig,
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = validateMemberUseCase.invoke(
                referencia,
                curp,
                config,
            )
            _membershipData.postValue(response)
        }
    }

    fun serviceValidationDataINE(
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
    ) {
        viewModelScope.launch {
            val response = validationDataINE.invoke(
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
            _ValidationINEData.postValue(response)
        }
    }
}