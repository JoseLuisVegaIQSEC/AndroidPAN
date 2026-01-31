package mx.com.iqsec.sdkpan.data.retrofit

import mx.com.iqsec.sdkpan.domain.base.ResponseBase
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap
import retrofit2.http.Url

interface SDK_ApiClientHelper {

    @GET
    suspend fun get(
        @Url uri: String,
        @QueryMap queryMap: HashMap<String, Any>
    ): Response<ResponseBase<Any>>

    @POST
    suspend fun post(@Url uri: String, @Body body: RequestBody): Response<Any>

    @POST
    suspend fun postSFN(@Url uri: String, @Body body: RequestBody): Response<Any>
}