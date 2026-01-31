package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.content.ContextWrapper
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
import mx.com.iqsec.sdkpan.databinding.SdkDaerrorBinding

class SDK_DAError(
    context: Context,
    var title: String? = null,
    var message: String,
    var titleBtn: String? = null,
    var actionButton: () -> Unit = {},
    val autoDismiss: Boolean = false
) : AppCompatDialog(context) {

    private lateinit var binding: SdkDaerrorBinding
    private val handler = Handler(Looper.getMainLooper())
    private var countdown = 3

    private var isLoopAnimationRunning = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SdkDaerrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        initListeners()
        if (autoDismiss) startCountdown()
    }

    private fun initListeners() {
        binding.apply {
            if (title.isNullOrEmpty())
                title = context.getString(R.string.sdk_pan_d_error_warning)

            sdkDTitle.text = title
            DaCancelMessage.text = message

            if(!titleBtn.isNullOrEmpty())
                DaCancelbtnOk.text = titleBtn
            else
                DaCancelbtnOk.text = context.getString(R.string.dialog_txt_close)

            DaCancelbtnOk.setOnClickListener {
                actionButton()
                this@SDK_DAError.dismiss()
            }
        }

        val size = (getScreenHeight(context) / 100) * 60
        binding.lldaCancel.layoutParams.height = size
        setCancelable(false)
    }

    override fun dismiss() {
        super.dismiss()
    }

    override fun show() {
        super.show()
    }

    private fun startCountdown() {
        //updateButtonText()
        handler.postDelayed(object : Runnable {
            override fun run() {
                countdown--
                if (countdown >= 0) {
                    //updateButtonText()
                    handler.postDelayed(this, 1000L)
                } else {
                    actionButton()
                    this@SDK_DAError.dismiss()
                }
            }
        }, 1000L)
    }

    private fun updateButtonText() {
        val baseText = titleBtn ?: context.getString(R.string.dialog_txt_close)
        binding.DaCancelbtnOk.text = "$baseText ($countdown)"
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
                v.setPadding(
                    insets.systemWindowInsetLeft,
                    insets.systemWindowInsetTop,
                    insets.systemWindowInsetRight,
                    insets.systemWindowInsetBottom
                )
                insets
            }
        }
    }
}