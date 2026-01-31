package mx.com.iqsec.sdkpan.domain.util

sealed class SDK_ResultValue<out T : Any> {

    /**
     * Represent a successful result with associated data
     */
    data class Success<out T : Any>(val data: T) : SDK_ResultValue<T>()

    /**
     * Represent an error result with associated exception
     */
    data class Error(
        val error: Pair<String, String>,//Titulo, Mensaje
        val codService: Int = 0,
        val messageService: String = ""
    ) : SDK_ResultValue<Nothing>()
}