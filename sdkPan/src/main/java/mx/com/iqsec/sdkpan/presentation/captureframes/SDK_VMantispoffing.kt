package mx.com.iqsec.sdkpan.presentation.captureframes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.AntispoffingDataModel
import mx.com.iqsec.sdkpan.domain.repository.valida_antispoffing_fn.ValidaAntispoffingFNUseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VMantispoffing() : ViewModel() {
    private val validaAntispoofFNUseCase = ValidaAntispoffingFNUseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _eAntispoffing = MutableLiveData<SDK_ResultValue<AntispoffingDataModel>>().apply { value = null }
    val eAntispoffing: LiveData<SDK_ResultValue<AntispoffingDataModel>> = _eAntispoffing

    fun AntispoffingValidate(
        imgs_base64: ArrayList<String>,
        config: BaseConfig
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = validaAntispoofFNUseCase(imgs_base64, config)
            _eAntispoffing.postValue(response)
        }
    }
}