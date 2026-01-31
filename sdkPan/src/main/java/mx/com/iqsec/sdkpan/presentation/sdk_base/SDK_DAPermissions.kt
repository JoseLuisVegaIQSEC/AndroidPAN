package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
import mx.com.iqsec.sdkpan.databinding.SdkDaGeolocationBinding

class SDK_DAPermissions(
    context: Context,
    var titleBtn: String? = null,
    var actionButton: () -> Unit = {},
    var goToPermisions: () -> Unit = {},
) : AppCompatDialog(context) {

    private lateinit var binding: SdkDaGeolocationBinding
    private var isLoopAnimationRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SdkDaGeolocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            daLocationPermissionC3.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            daLocationPermissionC3.setOnClickListener {
                goToPermisions()
                this@SDK_DAPermissions.dismiss()
            }


            if(!titleBtn.isNullOrEmpty())
                DaCancelbtnOk.text = titleBtn
            else
                DaCancelbtnOk.text = context.getString(R.string.dialog_txt_close)

            DaCancelbtnOk.setOnClickListener {
                actionButton()
                this@SDK_DAPermissions.dismiss()
            }
        }

        setCancelable(false)

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
