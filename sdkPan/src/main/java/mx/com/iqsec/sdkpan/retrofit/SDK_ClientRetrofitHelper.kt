package mx.com.iqsec.sdkpan.retrofit

import com.google.gson.GsonBuilder
import com.google.gson.internal.bind.TypeAdapters
import mx.com.iqsec.sdkpan.BuildConfig
import mx.com.iqsec.sdkpan.domain.base.SDK_DataAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SDK_ClientRetrofitHelper {

    fun getRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(TypeAdapters.newFactory(String::class.java, SDK_DataAdapter()))
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.DEFAULT_HOST)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getUnsafeOkHttpClient())
            .build()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()

            if (chain.request().headers["ApiKey"].isNullOrEmpty()) {
                newRequest.addHeader("ApiKey", BuildConfig.API_KEY)
            }

            chain.proceed(newRequest.build())
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Añadir el interceptor de logging aquí
            .addInterceptor(headerInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS) // Tiempo de espera de conexión
            .readTimeout(60, TimeUnit.SECONDS) // Tiempo de espera de lectura
            .writeTimeout(60, TimeUnit.SECONDS) // Tiempo de espera de escritura
            .build()
    }
}