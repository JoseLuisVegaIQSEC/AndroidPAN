package mx.com.iqsec.sdkpan.common.constants

object SDK_Constants {
    const val TEST_ERROR = 3005
    const val TEST_CALCEL_USER = 4005
    const val TEST_FINISH = 5005
    const val APP_TIMEOUT = 7005
    const val USER_EXIST = 8005
    const val REQUEST_CODE_TAKE_BACK = 1002

    const val WELCOME_STEP = 1
    const val NOTICE_INE = 2
    const val TEST_LIFE_STEP = 3
    const val FRONT_INE_STEP = 4
    const val BACK_INE_STEP = 5
    const val INE_OK_STEP = 6
    const val SIGN_STEP = 7
    const val INTERESTED_STE = 7
    const val UPDATE_DATA_STEP = 7
    const val CONTACT_DETAILS_STEP = 7
    const val SOCIAL_NETWORK_STEP = 7
    const val NOTICE_TEST_LIFE_STEP = 7
    const val TEST_LIFE_OK_STEP = 7
    const val CREDENTIAL_STEP = 7
    const val FINISH_STEP = 8
    const val APP_TIME_OUT = 9

    const val NAME_PREFERENCES = "SDKAntispoffing"
    const val DATA = "data"
    const val BASE_CONF = "baseconf"
    const val PARAM_CREDENTIAL = "credential_user"
    const val PARAM_PAN_CREDENTIAL = "pan_credential_user"
    const val RESULT_ANTISPOFFING = "result_antispoffing"
    const val DATA_INE_VALIDATION = "data_ine_validation"
    const val RES_FACIAL_VALIDATION = "res_facial_validation"
    const val RES_OCR_VALIDATION = "res_ocr_validation"
    const val RES_SERVICE_INE_VALIDATION = "res_service_ine_validation"
    const val SEND_SERVICE_INE_VALIDATION = "send_service_ine_validation"
    const val TAKE_IMAGE = "type_image"
    const val INE_FRONTAL = "ine_frontal"
    const val SDK_IMG_TEMP = "sdk_img_temp.png"
    const val INE_REVERSO = "ine_reverso"
    const val IMG_SIGNATURE = "img_signature"
    const val GEOLOCATION_PERMISION_REQUEST = "geolocation_permision_request"
    const val CAMERA_PERMISSION_REQUEST = "camera_permission_request"
    const val MEDIA_PERMISSION_REQUEST = "media_permission_request"
    const val USER_LOCATION = "user_location"
    const val GLOBAL_ATTEMPS = "global_attemps"
    const val ATTEMPS_ANTISPOFFING = "attemps_antispoffing"
    const val ATTEMPS_UPDATE_DATA = "attemps_update_data"
    const val ATTEMPS_BACK_INE = "attemps_back_ine"
    const val ATTEMPS_SIGN = "attemps_sign"
    const val ATTEMPS_OCR = "attemps_ocr"
    const val MAX_ATTEMPS = 3

    const val MAX_QUEUE_SIZE = 1
    val TOLERANCIA_FANTASMA = 3
    val TYPE_CAPTURE_FRONT = 0
    val TYPE_CAPTURE_BACK = 1
    const val direcrtory_files = "sdf_sdk_pan_files"

    //constantes de toma automatica
    const val SCAN_DOCUMENT_OK = 10
    const val SCAN_DOCUMENT_CANCEL = 20
    const val SCAN_DOCUMENT_BACK = 30
    const val MIN_SIGNATURE_OCCUPATION = 1.5f
    const val CONFIDENCE_SERVICE_INE: Double = 85.0

    //SERVICES
    const val SAVE_SIGN = "Services/ResguardaFirmaV2"

    //Services/OCR
    const val SERVICE_OCR_INE = "Services/OCRV2"

    //Services/OCRDatosUsuario
    const val SERVICE_DATA_USER_INE = "Services/DatosUsuarioV2"

    //Services/PruebaVida
    const val SERVICE_ANTISPOFFING = "Services/PruebaVida"

    //Services/ComparaFotoCredencial
    const val SERVICE_COMPARE_FACE_INE = "Services/ComparaFotoCredencial"

    //Services/ResguardaRostro
    const val SERVICE_SAVE_FACE = "Services/ResguardaRostro"

    //Services/CierreExpediente
    const val SERVICE_CLOSE_FILE = "Services/CierreExpediente"

    //Services/VerificarAfiliacion
    const val SERVICE_VALIDATE_MEMBERSHIP = "Services/VerificarAfiliacion"

    //Services/CatEstados
    const val SERVICE_CAT_ESTADOS = "Services/CatEstados"

    //Services/CatMunicipios
    const val SERVICE_CAT_MUNICIPIOS = "Services/CatMunicipios"

    //
    const val SERVICE_OCR_ADDRESS = "Services/OCRDomicilio"

    //Services/ValidarINE
    const val SERVICE_VALIDATE_INE = "Services/ValidarINE"
}

enum class DialogAction {
    ClickWhite,
    ClickBlue,
    AutoDismiss
}