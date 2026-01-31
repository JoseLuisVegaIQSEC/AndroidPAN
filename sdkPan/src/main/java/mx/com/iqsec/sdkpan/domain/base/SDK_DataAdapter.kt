package mx.com.iqsec.sdkpan.domain.base

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class SDK_DataAdapter<T> : TypeAdapter<T>() {
    override fun write(out: JsonWriter?, value: T) {
        // No es necesario implementar la escritura
    }

    override fun read(`in`: JsonReader): T {
        when (`in`.peek()) {
            JsonToken.BEGIN_OBJECT -> {
                //"Si data es un objeto, puedes deserializarlo aquí"
                return Gson().fromJson(`in`, Any::class.java) as T
            }
            JsonToken.BEGIN_ARRAY -> {
                //"Si data es una lista, puedes deserializarla aquí"
                return Gson().fromJson(`in`, Any::class.java) as T
            }
            JsonToken.STRING ->{
                //"Si data es un string, puedes deserializarla aqui"
                return Gson().fromJson(`in`, Any::class.java) as T
            }
            else -> {
                // Puedes manejar otros tipos de datos aquí según tus necesidades
                throw IllegalStateException("Tipo de datos no válido en 'data'" + `in`.peek())
            }
        }
    }
}
