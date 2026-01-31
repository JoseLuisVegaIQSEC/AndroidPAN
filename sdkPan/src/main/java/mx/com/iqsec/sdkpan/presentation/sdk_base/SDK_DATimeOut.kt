package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
import mx.com.iqsec.sdkpan.databinding.SdkDaTimeOutBinding
import kotlin.text.compareTo

class SDK_DATimeOut(
    context: Context,
    var actionButton: () -> Unit = {},
) : AppCompatDialog(context) {

    private lateinit var binding: SdkDaTimeOutBinding
    private var isLoopAnimationRunning = false
    private val handler = Handler(Looper.getMainLooper())
    private var countdownMessage = 3
    private var countdownBtn = 10

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SdkDaTimeOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        startCountdownBtn()
        startCountdownMessage()
    }

    private fun initListeners() {
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        val bitmapOriginal =
            BitmapFactory.decodeResource(context.resources, R.drawable.sdk_pan_clouds)
        binding.sdkPanBackground1.setImageBitmap(bitmapOriginal.obtenerTercio(0))
        binding.sdkPanBackground2.setImageBitmap(bitmapOriginal.obtenerTercio(1))
        binding.sdkPanBackground3.setImageBitmap(bitmapOriginal.obtenerTercio(2))

        if (isLoopAnimationRunning) return
        else {
            isLoopAnimationRunning = true
            startAnimationTercio(
                context,
                binding.sdkPanBackground1,
                binding.sdkPanBackground2,
                binding.sdkPanBackground3
            )
        }

        binding.sdkPanBackgroundCards.visibility = View.VISIBLE

        binding.apply {
            DaCancelMessage.text = context.getString(R.string.sdk_da_time_out_body)

            DaCancelbtnOk.text = context.getString(R.string.dialog_txt_close)

            DaCancelbtnOk.setOnClickListener {
                actionButton()
                this@SDK_DATimeOut.dismiss()
            }
        }

        setCancelable(false)
    }

    override fun dismiss() {
        super.dismiss()
    }

    private fun startCountdownMessage() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                countdownMessage--
                if (countdownMessage >= 0) {
                    handler.postDelayed(this, 1000L)
                } else {
                    this@SDK_DATimeOut.dismiss()
                }
            }
        }, 1000L)
    }

    private fun startCountdownBtn() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                countdownBtn--
                if (countdownBtn >= 0) {
                    handler.postDelayed(this, 1000L)
                } else {
                    actionButton()
                    this@SDK_DATimeOut.dismiss()
                }
            }
        }, 1000L)
    }

    private fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = windowManager.currentWindowMetrics
            windowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    override fun onStart() {
        super.onStart()
        // Ajusta el tamaño del diálogo aquí
        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            // Asegúrate de que el sistema dibuje las barras
            decorView.setOnApplyWindowInsetsListener { v, insets ->
                val paddingInsets = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    insets.getInsets(WindowInsets.Type.systemBars())
                } else {
                    @Suppress("DEPRECATION")
                    Insets.of(
                        insets.systemWindowInsetLeft,
                        insets.systemWindowInsetTop,
                        insets.systemWindowInsetRight,
                        insets.systemWindowInsetBottom
                    )
                }

                v.setPadding(
                    paddingInsets.left,
                    paddingInsets.top,
                    paddingInsets.right,
                    paddingInsets.bottom
                )

                insets
            }
        }
    }
}