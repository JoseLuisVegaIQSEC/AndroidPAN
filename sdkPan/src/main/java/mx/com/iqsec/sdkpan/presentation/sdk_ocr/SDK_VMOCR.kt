package mx.com.iqsec.sdkpan.presentation.sdk_ocr

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel
import mx.com.iqsec.sdkpan.domain.repository.ocr_ine.OCR_INEUseCase
import mx.com.iqsec.sdkpan.domain.repository.verify_frames.VerifyFramesUseCase
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_VMOCR() : ViewModel() {
    private val ocrINEUseCase = OCR_INEUseCase()
    private val comparacionFacial = VerifyFramesUseCase()

    val isLoading = MutableLiveData<Boolean>()

    private val _oCRINE = MutableLiveData<SDK_ResultValue<INEModel>>().apply { value = null }
    val oCRINE: LiveData<SDK_ResultValue<INEModel>> = _oCRINE

    private val _compare_face = MutableLiveData<SDK_ResultValue<VerifyFramesModel>>().apply { value = null }
    val compare_face: LiveData<SDK_ResultValue<VerifyFramesModel>> = _compare_face

    fun ocrValidate(
        frontalBase64: String,
        reversoBase64: String,
        config: BaseConfig,
        context: Context
    ) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val response = ocrINEUseCase(
                frontalBase64,
                reversoBase64,
                config,
                context
            )
            _oCRINE.postValue(response)
        }
    }

    fun serviceFacialVerification(
        imageBaseB64: String,
        imagesFramesB64: String,
        config: BaseConfig,
        context: Context
    ) {
        viewModelScope.launch {
            val response = comparacionFacial(
                imageBaseB64,
                imagesFramesB64,
                config,
                context
            )
            _compare_face.postValue(response)
        }
    }
}