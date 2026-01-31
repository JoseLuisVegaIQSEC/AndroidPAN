package mx.com.iqsec.sdkpan.data.di.manager

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.model.BaseConfig

class SDK_SPManager(var context: Context) {

    private var sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor
    private val gson: Gson = Gson()

    init {
        sharedPreferences = createEncryptedPreferences()
        editor = sharedPreferences.edit()
    }

    private fun createEncryptedPreferences(): SharedPreferences {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            EncryptedSharedPreferences.create(
                context,
                SDK_Constants.NAME_PREFERENCES,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            recreatePreferences()
        }
    }

    private fun recreatePreferences(): SharedPreferences {
        context.getSharedPreferences(
            SDK_Constants.NAME_PREFERENCES, Context.MODE_PRIVATE
        ).edit()
            .clear()
            .apply()

        return createEncryptedPreferences()
    }

    fun saveConf(key: String, array: BaseConfig) {
        val json = gson.toJson(array)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getConf(key: String): BaseConfig? {
        val json = sharedPreferences.getString(key, null)
        val type = object : TypeToken<BaseConfig>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

    fun saveInt(key: String, value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    inline fun <reified T> saveEncryptedObject(key: String, obj: T? = null) {
        val json = Gson().toJson(obj)
        saveString(key, json)
    }

    inline fun <reified T> getEncryptedObject(key: String, defaultValue: T? = null): T? {
        val encryptedJson = getString(key, "")
        return encryptedJson.let {
            try {
                Gson().fromJson(encryptedJson, T::class.java)
            } catch (e: Exception) {
                defaultValue
            }
        } ?: defaultValue
    }

    fun removePreference(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAllSharedPreferences() {
        sharedPreferences.edit().clear().apply()
    }
}