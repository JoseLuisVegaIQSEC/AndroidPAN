package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerMitad
//import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startLoopAnimation
import mx.com.iqsec.sdkpan.databinding.SdkDaLocationPermisionsBinding

/**
 * Diálogo para mostrar permisos de geolocalización.
 * @param titleBtn Texto del botón de acción.
 * @param actionButton Acción a ejecutar al presionar el botón.
 */
class SDK_Pan_DPermisions(
    context: Context,
    private val titleBtn: String? = null,
    private val title: String? = null,
    private val message: String? = null,
    private val actionButton: () -> Unit = {},
) : AppCompatDialog(context, R.style.sdk_pan_dialog_fullscreen) {

    private lateinit var binding: SdkDaLocationPermisionsBinding
    private var isLoopAnimationRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SdkDaLocationPermisionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            sdkPanDialogPermisionsMessage.text = message
            sdkPanDialogPermisionsTitle.text = title
            DaCancelbtnOk.text = titleBtn ?: context.getString(R.string.dialog_txt_close)
            DaCancelbtnOk.setOnClickListener {
                actionButton()
                dismiss()
            }
        }
        setCancelable(false)

        val bitmapOriginal =
            BitmapFactory.decodeResource(context.resources, R.drawable.sdk_pan_clouds)
        binding.sdkPanBackground1.setImageBitmap(bitmapOriginal.obtenerMitad(0))
        binding.sdkPanBackground2.setImageBitmap(bitmapOriginal.obtenerMitad(1))
//        binding.sdkPanBackground3.setImageBitmap(bitmapOriginal.obtenerTercio(2))

        if (isLoopAnimationRunning) return
        else {
            isLoopAnimationRunning = true
            startLoopAnimation(
                context,
                binding.sdkPanBackground1,
                binding.sdkPanBackground2,
//                binding.sdkPanBackground3
            )
        }

        binding.sdkPanBackgroundCards.visibility = View.VISIBLE
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
