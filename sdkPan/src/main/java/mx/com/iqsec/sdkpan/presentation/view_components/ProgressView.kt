package mx.com.iqsec.sdkpan.presentation.view_components

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import com.airbnb.lottie.LottieAnimationView
import mx.com.iqsec.sdkpan.R

class ProgressView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attributeSet, defStyleAttr){
    init {
        isClickable = true
        orientation = VERTICAL
        elevation = VIEW_ELEVATION
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        gravity = Gravity.CENTER
        setBackgroundResource(R.drawable.background_gradient)
        val layoutParamsView = LayoutParams(250, 250)
        val progressView = LottieAnimationView(context)
        progressView.apply {
            setAnimation(R.raw.loading_blue_general)
            scaleX = LOTTIE_SCALE
            scaleY = LOTTIE_SCALE
            repeatCount = ValueAnimator.INFINITE
            playAnimation()
        }
        addView(progressView, layoutParamsView)
    }

    private companion object{
        const val  VIEW_ELEVATION = 10f
        const val  LOTTIE_SCALE = 3f
    }
}