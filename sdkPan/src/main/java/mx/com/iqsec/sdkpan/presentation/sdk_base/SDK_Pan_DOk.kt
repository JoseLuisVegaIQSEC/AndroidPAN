package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.res.ResourcesCompat
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
import mx.com.iqsec.sdkpan.databinding.DSdkErrorBinding

enum class TypeDialog {
    ERROR_OCR,
    ERROR_SERVICE,
    ERROR_TESTLIFE,
    OK,
    USER_EXISTS,
    PERMISSIONS,
    NETWORK_ERROR,
}

class SDK_Pan_DOk(
    context: Context,
    var type: TypeDialog,
    var title: String? = null,
    var message: String? = null,
    var titleBtn: String? = null,
    private val actionButton: () -> Unit = {},
) : AppCompatDialog(context) {
    private lateinit var binding: DSdkErrorBinding
    private var isLoopAnimationRunning = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DSdkErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        applyTypeDialog()
        binding.btnEnter.text = titleBtn
        binding.sdkPanDialogErrorTitle.text = title
        binding.sdkPanDialogErrorMessage.text = message

        binding.btnEnter.setOnClickListener {
            actionButton()
            dismiss()
        }

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

    private fun applyTypeDialog() {
        when (type) {
            TypeDialog.ERROR_OCR -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_error_ocr,
                        null
                    )
                )
            }

            TypeDialog.ERROR_SERVICE -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_exclamation,
                        null
                    )
                )
            }

            TypeDialog.ERROR_TESTLIFE -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_error_testlife,
                        null
                    )
                )
            }

            TypeDialog.OK -> {
                binding.sdkPanDialogPermisionslottie.visibility = View.VISIBLE
            }

            TypeDialog.USER_EXISTS -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_user_exist,
                        null
                    )
                )
            }

            TypeDialog.PERMISSIONS -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_exclamation,
                        null
                    )
                )
            }

            TypeDialog.NETWORK_ERROR -> {
                binding.sdkPanDialogPermisionsImg.visibility = View.VISIBLE
                binding.sdkPanDialogPermisionsImg.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.img_pan_exclamation,
                        null
                    )
                )
            }
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
