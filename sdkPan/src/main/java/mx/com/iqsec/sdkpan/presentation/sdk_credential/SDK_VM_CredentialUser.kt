package mx.com.iqsec.sdkpan.presentation.sdk_credential

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.CloseFileModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.repository.close_file.Close_FileUseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VM_CredentialUser() : ViewModel() {
    private val saveCloseFileUseCase = Close_FileUseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _dataCloseFile =
        MutableLiveData<SDK_ResultValue<CloseFileModel>>().apply { value = null }
    val dataCloseFile: LiveData<SDK_ResultValue<CloseFileModel>> = _dataCloseFile

    fun serviceCloseFile(
        userData: UserINEModel,
        config: BaseConfig,
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = saveCloseFileUseCase(
                userData,
                config
            )
            _dataCloseFile.postValue(response)
            isLoading.postValue(false)
        }
    }
}