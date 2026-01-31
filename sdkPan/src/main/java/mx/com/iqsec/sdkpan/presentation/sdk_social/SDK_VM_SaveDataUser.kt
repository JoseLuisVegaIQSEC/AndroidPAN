package mx.com.iqsec.sdkpan.presentation.sdk_social

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.repository.save_user_data.SaveData_UseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VM_SaveDataUser() : ViewModel() {
    private val saveDataUserUseCase = SaveData_UseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _saveDataResult =
        MutableLiveData<SDK_ResultValue<DataUserModel>>().apply { value = null }
    val saveDataResult: LiveData<SDK_ResultValue<DataUserModel>> = _saveDataResult

    fun saveUserData(
        userData: UserINEModel,
        config: BaseConfig,
        context: Context
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = saveDataUserUseCase.invoke(
                userData,
                config,
                context
            )
            _saveDataResult.postValue(response)
            isLoading.postValue(false)
        }
    }
}