package mx.com.iqsec.sdkpan.presentation.dialog

import android.content.Context
import android.content.ContextWrapper
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.extensions.obtenerMitad
import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startAnimationTercio
//import mx.com.iqsec.sdkpan.common.extensions.obtenerTercio
import mx.com.iqsec.sdkpan.common.extensions.startLoopAnimation
import mx.com.iqsec.sdkpan.databinding.DSdkLoaderBinding

class SDK_DLoader(
    context: Context,
    var message: String? = null
) :
    AppCompatDialog(context),
    DialogInterface.OnDismissListener {

    private lateinit var binding: DSdkLoaderBinding
    private var currentAnimation: ViewPropertyAnimator? = null
    private var activity: AppCompatActivity? = null
    private var isLoopAnimationRunning = false

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setOnDismissListener(this)
        activity = getTheActivityFromContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DSdkLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        if (message.isNullOrEmpty())
            message = context.getString(R.string.raw_espereUnMomentoPorFavor)

        binding.txtDialogMessage.text = message

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.root.rootView.alpha = 0f // start alpha 0f
        currentAnimation = binding.root.rootView.animate() // Correct use of animate()
        currentAnimation?.alpha(1f)?.setDuration(300)?.start()

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

    override fun dismiss() {
        if (activity?.isFinishing == true || activity?.isDestroyed == true) {
            super.dismiss()
            return
        }
        currentAnimation = binding.root.rootView.animate()
        currentAnimation?.alpha(0f)?.setDuration(1000)?.withEndAction {
            if (activity?.isFinishing == true || activity?.isDestroyed == true) {
                return@withEndAction
            }
            super.dismiss() // call dismiss
        }?.start()
    }

    override fun onDismiss(dialog: DialogInterface) {
        currentAnimation?.cancel()
    }

    private fun getTheActivityFromContext(context: Context): AppCompatActivity? {
        var currentContext = context
        while (currentContext is ContextWrapper) {
            if (currentContext is AppCompatActivity) {
                return currentContext
            }
            currentContext = currentContext.baseContext
        }
        return null
    }
}
