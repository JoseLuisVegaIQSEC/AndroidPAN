package mx.com.iqsec.sdkpan.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mx.com.iqsec.sdkpan.data.di.manager.SDK_RetrofitManager
import mx.com.iqsec.sdkpan.data.retrofit.SDK_ApiClientHelper
import mx.com.iqsec.sdkpan.domain.base.ResponseBase
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB
import mx.com.iqsec.sdkpan.retrofit.SDK_ClientRetrofitHelper
class SDK_ApiService() {

    suspend fun executePost(service: String, body: HashMap<String, Any>): SDK_MResponseFNDB<Any> {
        val clientRetrofit = SDK_ClientRetrofitHelper.getRetrofit().create(SDK_ApiClientHelper::class.java)
        val retrofitManager = SDK_RetrofitManager<SDK_ApiClientHelper>()
        retrofitManager.initialize(clientRetrofit)

        return withContext(Dispatchers.IO) {
            retrofitManager.callServicePost(service, body)
        }
    }

    suspend fun executeGet(service: String): SDK_MResponseFNDB<Any> {
        val clientRetrofit = SDK_ClientRetrofitHelper.getRetrofit().create(SDK_ApiClientHelper::class.java)
        val retrofitManager = SDK_RetrofitManager<SDK_ApiClientHelper>()
        retrofitManager.initialize(clientRetrofit)

        return withContext(Dispatchers.IO) {
            retrofitManager.callServiceGet(service)
        }
    }
}