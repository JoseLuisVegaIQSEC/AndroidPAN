package mx.com.iqsec.sdkpan.presentation

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.navigation.findNavController
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.DialogAction
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogCancel
import mx.com.iqsec.sdkpan.model.SDK_PAN_MResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogNoticeTimeOut
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.extractData
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.makeFolio
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.returnResponse
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
import mx.com.iqsec.sdkpan.databinding.ASdkAntispoffingBinding
import mx.com.iqsec.sdkpan.model.InitializerTestLife
import mx.com.iqsec.sdkpan.modules.checkconnection.NetworkMonitor
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BActivity

interface onStateToolbarListener {
    fun activityUpdateToolbar()
    fun hasConnection(): Boolean
}

class SDK_PAN : SDK_PAN_BActivity(), onStateToolbarListener {
    private lateinit var binding: ASdkAntispoffingBinding
    private val handler = Handler(Looper.getMainLooper())
    private var timeoutRunnable: Runnable? = null
    var modelTestLife = InitializerTestLife()
    var onTimeoutListener: OnTimeoutListener? = null
    private lateinit var navController: androidx.navigation.NavController
    private var isLoopAnimationRunning = false
    private lateinit var networkMonitor: NetworkMonitor

    var timeoutInicio: Int = 0
    var timeoutDialogo: Int = 0

    interface OnTimeoutListener {
        fun onTimeout()
    }

    private val inactivityHandler = Handler(Looper.getMainLooper())
    private var inactivityRunnable: Runnable? = null
    private var INACTIVITY_TIMEOUT_SECONDS = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ASdkAntispoffingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mantener la pantalla encendida durante el proceso
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        getData()
        initListeners()
        initInactivityTimeouts()
    }

    private fun initListeners() {
        val bitmapOriginal = BitmapFactory.decodeResource(resources, R.drawable.sdk_pan_clouds)
        binding.sdkPanBackground1.setImageBitmap(bitmapOriginal.obtenerTercio(0))
        binding.sdkPanBackground2.setImageBitmap(bitmapOriginal.obtenerTercio(1))
        binding.sdkPanBackground3.setImageBitmap(bitmapOriginal.obtenerTercio(2))
    }

    private fun onDestinationChanged() {
        navController = findNavController(R.id.sdkPanNavHostFragment)

        val currentDestinationId = navController.currentDestination?.id

        when (currentDestinationId) {
            R.id.Nav_SDK_Welcome_User_Fragment -> {
                updateUIColorsToolbar(false, false, true)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
                binding.sdkPanToolbarImgClose.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_SDK_FPrivacyView -> {
                updateUIColorsToolbar(true, false, false)
                updateToolbar("Aviso de privacidad", R.drawable.ic_pan_arrow_left)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { navController.popBackStack() }
            }

            R.id.nav_SDK_FNoticeINE -> {
                updateUIColorsToolbar()
                updateToolbar("Captura tu INE", R.drawable.ic_pan_close)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_SDK_capture_front_ine,
            R.id.nav_SDK_capture_back_ine -> {
                updateUIColorsToolbar()
                updateToolbar("Captura tu INE", R.drawable.ic_pan_close)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_SDK_FOCR_Complete, R.id.SDK_FAntispoffing_Ok -> {
                updateUIColorsToolbar()
                updateToolbar()
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { }
            }

            R.id.nav_UpdateData -> {
                updateUIColorsToolbar()
                updateToolbar("Valida tus datos", R.drawable.ic_pan_close)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_FContact_details -> {
                updateUIColorsToolbar()
                updateToolbar("Ingresa tus datos", R.drawable.ic_pan_arrow_left)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener {
                    onNavigateFragment(R.id.action_nav_FContact_details_to_nav_UpdateData)
                }
            }

            R.id.nav_FSocialNetwork -> {
                updateUIColorsToolbar()
                updateToolbar("Redes sociales", R.drawable.ic_pan_arrow_left)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener {
                    onNavigateFragment(R.id.action_nav_FSocialNetwork_to_nav_FInterests)
                }
            }

            R.id.nav_FInterests -> {
                updateUIColorsToolbar()
                updateToolbar("Elige tus intereses", R.drawable.ic_pan_arrow_left)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener {
                    onNavigateFragment(R.id.action_nav_FInterests_to_nav_FContact_details)
                }
            }

            R.id.nav_SDK_Face_Capture_Notice,
            R.id.nav_SDK_Face_Capture -> {
                updateUIColorsToolbar()
                updateToolbar("Tómate una foto", R.drawable.ic_pan_close)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_SDK_sign_user -> {
                iniciarTimeouts(timeoutInicio, timeoutDialogo)
                updateUIColorsToolbar()
                updateToolbar("Digitaliza tu firma", R.drawable.ic_pan_close)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { dialogCancelByUser() }
            }

            R.id.nav_FCredentialUser, R.id.nav_FKnowMore -> {
                updateUIColorsToolbar(false, true, false)
                binding.sdkPanToolbarImgToolbar2.setOnClickListener { }
            }
        }
    }

    private fun onNavigateFragment(idComponent: Int, bundle: Bundle? = null) {
        findNavController(R.id.sdkPanNavHostFragment).navigate(idComponent, bundle)
    }

    private fun updateToolbar(title: String = "", imgToolbar: Int = 0) {
        val visible = title.isNotEmpty() && imgToolbar != 0
        binding.sdkPanToolbarTitle2.visibility = if (visible) View.VISIBLE else View.GONE
        binding.sdkPanToolbarImgToolbar2.visibility = if (visible) View.VISIBLE else View.GONE

        binding.sdkPanToolbarTitle2.text = title
        binding.sdkPanToolbarImgToolbar2.setImageResource(imgToolbar)

    }

    private fun dialogCancelByUser() {
        this.dialogCancel(
            this,
            getString(R.string.sdk_pan_d_option_want_go_out),
            getString(R.string.ocr_ine_txt_cancel),
            getString(R.string.sdk_pan_d_option_go_out),
            getString(R.string.sdk_pan_d_option_continue_registration),
            10
        ) {
            when (it) {
                DialogAction.ClickBlue -> {}
                DialogAction.ClickWhite -> {
                    cancelbyUser()
                }

                DialogAction.AutoDismiss -> {}
            }
        }
    }

    private fun cancelbyUser() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.TEST_CALCEL_USER,
            descripcion = getString(R.string.txt_cancel_by_user),
            no_transaccion = this.makeFolio(SDK_Constants.SIGN_STEP)
        )
        cleanAppPreferences()
        this.returnResponse(data, SDK_Constants.TEST_CALCEL_USER)
    }

    private fun updateUIColorsToolbar(
        showToolbar: Boolean = true,
        showTimer: Boolean = true,
        showCloseButton: Boolean = false
    ) {
        binding.sdkPanToolbarCLNavigation.visibility = showItem(showToolbar)
        binding.sdkIQSECTimerText.visibility = showItem(showTimer)
        binding.sdkPanToolbarImgClose.visibility = showItem(showCloseButton)
        binding.sdkPanBackgroundCards.visibility = View.VISIBLE
        if (isLoopAnimationRunning) return
        else {
            isLoopAnimationRunning = true
            startAnimationTercio(
                this,
                binding.sdkPanBackground1,
                binding.sdkPanBackground2,
                binding.sdkPanBackground3
            )
        }

        binding.sdkPanToolbar.setBackgroundColor(getColor(R.color.sdk_wfc_transparent))
        binding.sdkPanAntispoffingRoot.background = getDrawable(R.drawable.gradient_pan_blue)

        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
    }

    private fun showItem(show: Boolean): Int {
        return when (show) {
            false -> View.GONE
            else -> View.VISIBLE
        }
    }

    private fun getData() {
        modelTestLife = this.extractData()
        // Obtén los valores de los extras
        timeoutInicio = (modelTestLife.timeoutStart)
        timeoutDialogo = modelTestLife.timeoutDialog

        INACTIVITY_TIMEOUT_SECONDS = modelTestLife.timeInactivitySeconds

        this.dialogNoticeTimeOut() {

        }
    }

    private fun iniciarTimeouts(inicio: Int, dialogo: Int) {
        // Evita iniciar otro timer si ya hay uno corriendo
        if (timeoutRunnable != null) return

        var contador = inicio
        timeoutRunnable = object : Runnable {
            override fun run() {
                contador--
                if (contador == dialogo) {
                    TimeOut()
                }
                if (contador >= 0) {
                    updateTimerText(contador)
                    handler.postDelayed(this, 1000L)
                }
            }
        }
        handler.post(timeoutRunnable!!)
    }

    private fun timeToast(toast: Int) {
        Toast.makeText(
            this@SDK_PAN,
            getString(R.string.sdk_ma_time_left, toast),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun TimeOut() {
        onTimeoutListener?.onTimeout()

        //Cancelacion de todos los timeouts
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        inactivityRunnable?.let { inactivityHandler.removeCallbacks(it) }

        this.dialogCancel(
            this,
            "Tiempo agotado",
            getString(R.string.sdk_ma_time_expire),
            "",
            "Volver al inicio",
            0 //60
        ) {
            when (it) {
                DialogAction.ClickBlue -> {
                    sdk_AppTimeOut()
                }

                DialogAction.ClickWhite -> {}
                DialogAction.AutoDismiss -> {}
            }
        }
    }

    private fun updateTimerText(contador: Int) {
        val minutes = contador / 60
        val seconds = contador % 60
        val timeFormatted = String.format("%02d:%02d", minutes, seconds)
        binding.timerText.text = timeFormatted
    }

    private fun sdk_AppTimeOut() {
        cleanPreferences()
        val data = SDK_PAN_MResponse(
            estado = SDK_Constants.APP_TIMEOUT,
            descripcion = getString(R.string.txt_app_time_out),
            no_transaccion = this.makeFolio(SDK_Constants.APP_TIME_OUT)
        )
        cleanAppPreferences()

        this.returnResponse(data, SDK_Constants.APP_TIMEOUT)
    }

    private fun cleanAppPreferences() {
        removePreference(SDK_Constants.PARAM_CREDENTIAL)
        removePreference(SDK_Constants.BASE_CONF)
        removePreference(SDK_Constants.RESULT_ANTISPOFFING)
        deleteImageTemp(this, imageFrontTemp)
        deleteImageTemp(this, imageBackTemp)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        inactivityRunnable?.let { inactivityHandler.removeCallbacks(it) }
    }

    override fun activityUpdateToolbar() {
        onDestinationChanged()
    }

    override fun hasConnection(): Boolean {
        return networkMonitor.isConnected.value ?: false
    }

    override fun onStop() {
        super.onStop()
        networkMonitor.stop()
    }

    override fun onStart() {
        super.onStart()
        networkMonitor = NetworkMonitor(this)
        networkMonitor.isConnected.observe(this) { connected ->

        }
        networkMonitor.start()
    }

    private fun initInactivityTimeouts() {
        // Timeout final
        inactivityRunnable = Runnable {
            handleInactivityTimeout()
        }

        resetInactivityTimer()
    }

    private fun resetInactivityTimer() {
        inactivityRunnable?.let { inactivityHandler.removeCallbacks(it) }

        // Programar timeout final
        inactivityRunnable?.let {
            inactivityHandler.postDelayed(it, INACTIVITY_TIMEOUT_SECONDS * 1000L)
        }
    }

    override fun dispatchTouchEvent(ev: android.view.MotionEvent?): Boolean {
        resetInactivityTimer()
        return super.dispatchTouchEvent(ev)
    }

    private fun handleInactivityTimeout() {
        // Notificar a quien lo necesite
        onTimeoutListener?.onTimeout()

        // Podrías reutilizar tu diálogo de timeout
        this.dialogCancel(
            this,
            "Inactividad en el Registro", // crea un string si no existe
            "Tu proceso de registro será interrumpido y redirigido a la página principal debido a un periodo prolongado sin interacción.",
            "Volver al inicio",
            "Continuar con el registro",
            60 //60
        ) {
            when (it) {
                DialogAction.ClickBlue -> {
                    resetInactivityTimer()
                }

                DialogAction.ClickWhite -> {
                    sdk_AppTimeOut()
                }

                DialogAction.AutoDismiss -> dialogInterruptedRegistration()
            }
        }
    }

    private fun dialogInterruptedRegistration() {
        //Cancelacion de todos los timeouts
        timeoutRunnable?.let { handler.removeCallbacks(it) }
        inactivityRunnable?.let { inactivityHandler.removeCallbacks(it) }

        // Podrías reutilizar tu diálogo de timeout
        this.dialogCancel(
            this,
            "Registro interrumpido", // crea un string si no existe
            "Tu proceso de registro se interrumpió debido a un periodo prolongado sin interacción.",
            "",
            getString(R.string.lbl_close),
            0
        ) {
            when (it) {
                DialogAction.ClickBlue -> {
                    sdk_AppTimeOut()
                }

                DialogAction.ClickWhite -> {}
                DialogAction.AutoDismiss -> {
                    sdk_AppTimeOut()
                }
            }
        }
    }
}