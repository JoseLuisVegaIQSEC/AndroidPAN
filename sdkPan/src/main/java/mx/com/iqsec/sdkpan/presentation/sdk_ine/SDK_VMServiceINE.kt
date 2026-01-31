package mx.com.iqsec.sdkpan.presentation.sdk_ine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.SaveSignModel
import mx.com.iqsec.sdkpan.domain.repository.save_sign.SaveSign_UseCase
import mx.com.iqsec.sdkpan.domain.repository.save_user_data.SaveData_UseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VMServiceINE() : ViewModel() {
    private val saveDataUserUseCase = SaveData_UseCase()
    private val saveSignUserUseCase = SaveSign_UseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _saveDataResult =
        MutableLiveData<SDK_ResultValue<SaveSignModel>>().apply { value = null }
    val saveDataResult: LiveData<SDK_ResultValue<SaveSignModel>> = _saveDataResult

    fun serviceSaveSign(
        config: BaseConfig,
        firma: String
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = saveSignUserUseCase.invoke(
                config, firma
            )
            _saveDataResult.postValue(response)
            isLoading.postValue(false)
        }
    }
}