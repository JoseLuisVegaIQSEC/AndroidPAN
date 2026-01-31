package mx.com.iqsec.sdkpan.data.di.manager

import com.google.gson.Gson
import mx.com.iqsec.sdkpan.data.retrofit.SDK_ApiClientHelper
import mx.com.iqsec.sdkpan.domain.base.SDK_FNResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import mx.com.iqsec.sdkpan.domain.base.SDK_MResponseFNDB

class SDK_RetrofitManager<T> {

    private var api: T? = null
    fun initialize(apiInstance: T) {
        api = apiInstance
    }

    suspend fun callServiceGet(service: String): SDK_MResponseFNDB<Any> {
        try {
            val response = (api as SDK_ApiClientHelper).get(service, HashMap())
            val gson = Gson()
            val jsonString = gson.toJson(response.body())
            val sdkResponse = gson.fromJson(jsonString, SDK_FNResponse::class.java)

            if (response.isSuccessful) {
                if (sdkResponse.codigo != 0) {
                    return SDK_MResponseFNDB(
                        message = getErrorMessage(null),
                        codigo = sdkResponse.codigo,
                        data = "",
                        codService = sdkResponse.codigo,
                        messageService = sdkResponse.message
                    )
                }

                return SDK_MResponseFNDB(
                    message = Pair("", sdkResponse.message),
                    codigo = sdkResponse.codigo,
                    data = sdkResponse.data ?: ""
                )
            } else {
                return SDK_MResponseFNDB(
                    message = getErrorMessage(null),
                    codigo = response.code(),
                    data = "",
                    codService = response.code(),
                    messageService = response.message()
                )
            }
        } catch (throwable: Throwable) {
            val errorMessage = getErrorMessage(throwable)

            return SDK_MResponseFNDB(
                message = errorMessage,
                codigo = throwable.hashCode(),
                data = "",
                codService = throwable.hashCode(),
                messageService = throwable.message ?: ""
            )
        }
    }

    suspend fun callServicePost(
        service: String,
        bodyRequest: HashMap<String, Any>
    ): SDK_MResponseFNDB<Any> {

        val mediaType = "application/json; charset=utf-8".toMediaType()

        val gson = Gson()
        val jsonBody = gson.toJson(bodyRequest).toRequestBody(mediaType)

        try {
            val response = (api as SDK_ApiClientHelper).postSFN(service, jsonBody)
            val gson = Gson()
            val jsonString = gson.toJson(response.body())
            val sdkResponse = gson.fromJson(jsonString, SDK_FNResponse::class.java)

            if (response.isSuccessful) {
                if (sdkResponse.codigo != 0) {
                    return SDK_MResponseFNDB(
                        message = getErrorMessage(null),
                        codigo = sdkResponse.codigo,
                        data = "",
                        codService = sdkResponse.codigo,
                        messageService = sdkResponse.message
                    )
                }

                return SDK_MResponseFNDB(
                    message = Pair("", sdkResponse.message),
                    codigo = sdkResponse.codigo,
                    data = sdkResponse.data!!
                )
            } else {
                return SDK_MResponseFNDB(
                    message = getErrorMessage(null),
                    codigo = response.code(),
                    data = "",
                    codService = response.code(),
                    messageService = response.message()
                )
            }
        } catch (throwable: Throwable) {
            val errorMessage = getErrorMessage(throwable)

            return SDK_MResponseFNDB(
                message = errorMessage,
                codigo = throwable.hashCode(),
                data = "",
                codService = throwable.hashCode(),
                messageService = throwable.message ?: ""
            )
        }
    }

    private fun getErrorMessage(throwable: Throwable?): Pair<String, String> {
        val defaultMsg =
            "Por el momento, el servicio no está disponible. Inténtalo de nuevo más tarde."

        if (throwable == null) return Pair("Servicio no disponible", defaultMsg)

        return when (throwable) {
            //El error sucede cuando no se puede resolver el host
            is java.net.UnknownHostException -> Pair(
                "SIN CONEXIÓN A INTERNET",
                "Revisa tu red y vuelve a intentarlo para seguir con el proceso."
            )
            //El error sucede cuando no hay conexión
            is java.net.SocketException -> Pair(
                "SIN CONEXIÓN A INTERNET",
                "Revisa tu red y vuelve a intentarlo para seguir con el proceso."
            )
            //El error sucede cuando la conexión tarda demasiado tiempo
            is java.net.SocketTimeoutException -> Pair(
                "ALGO SALIO MAL",
                "Tiempo de espera agotado. Intenta de nuevo."
            )
            //Errores de E/S generales de red por ejemplo, al leer o escribir datos
            is java.io.InterruptedIOException, is java.io.IOException -> Pair(
                "SIN CONEXIÓN A INTERNET",
                "Error de comunicación. Revisa tu conexión."
            )
            //Errores relacionados con SSL por ejemplo, problemas de certificado
            is javax.net.ssl.SSLHandshakeException, is javax.net.ssl.SSLException -> Pair(
                "ALGO SALIO MAL",
                "Error de seguridad (SSL). Verifica la conexión segura."
            )

            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    // El error sucede cuando el servidor no puede procesar la solicitud debido a un error del cliente
                    400 -> Pair("ALGO SALIO MAL", "Solicitud inválida.")
                    //El error sucede cuando no se tiene permisos para acceder al recurso
                    401 -> Pair("ALGO SALIO MAL", "No autorizado. Revisa credenciales.")
                    //El error sucede cuando el servidor entiende la solicitud pero se niega a cumplirla
                    403 -> Pair("ALGO SALIO MAL", "Acceso prohibido.")
                    //El error sucede cuando no se encuentra el recurso solicitado
                    404 -> Pair("ALGO SALIO MAL", "Recurso no encontrado.")
                    //El error sucede cuando el servidor tarda demasiado en responder
                    408 -> Pair("ALGO SALIO MAL", "Tiempo de espera del servidor agotado.")
                    //El error sucede cuando se han enviado demasiadas solicitudes en un corto período de tiempo
                    429 -> Pair("ALGO SALIO MAL", "Demasiadas solicitudes. Intenta más tarde.")
                    //El error sucede cuando hay un problema en el servidor
                    500, 502, 503, 504 -> Pair("ALGO SALIO MAL", "Error en el servidor. Intenta nuevamente más tarde.")
                    else -> Pair("Servicio no disponible", defaultMsg)
                }
            }
            //Errores al convertir JSON en objetos o viceversa cuando el estado no es el esperado
            is com.google.gson.JsonParseException, is java.lang.IllegalStateException -> Pair(
                "ALGO SALIO MAL",
                "Error al procesar la respuesta del servidor."
            )

            else -> {
                Pair("Servicio no disponible", defaultMsg)
            }
        }
    }
}
