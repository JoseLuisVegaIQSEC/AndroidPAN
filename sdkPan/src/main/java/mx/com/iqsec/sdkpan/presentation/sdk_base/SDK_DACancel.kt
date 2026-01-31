package mx.com.iqsec.sdkpan.presentation.sdk_base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.DialogAction
import mx.com.iqsec.sdkpan.databinding.DSdkOptionBinding

class SDK_DACancel(
    context: Context,
    var title: String = "",
    var message: String,
    var messageWhiteButton: String,
    var messageBlueButton: String,
    var actionButton: (DialogAction) -> Unit,
    val timmeDismiss: Int = 0
) : AppCompatDialog(context, R.style.sdk_pan_dialog_fullscreen_back) {

    // Binding interno para evitar problemas de ciclo de vida de la vista
    private var _binding: DSdkOptionBinding? = null
    private val binding: DSdkOptionBinding
        get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private var countdown = 0

    // Runnable almacenado para poder cancelar correctamente el temporizador
    private var countdownRunnable: Runnable? = null

    // Bandera para evitar que se dispare la acción más de una vez
    private var isActionDispatched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DSdkOptionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        if (timmeDismiss != 0) startCountdown()
    }

    private fun initListeners() {
        binding.apply {
            if (title.isEmpty())
                title = context.getString(mx.com.iqsec.sdkpan.R.string.sdk_pan_d_option_want_go_out)

            if (messageWhiteButton.isNotEmpty())
                DaCancelbtnWhite.visibility = View.VISIBLE

            sdkDTitle.text = title
            DaCancelMessage.text = message
            DaCancelbtnWhite.text = messageWhiteButton
            DaCancelbtnBlue.text = messageBlueButton

            DaCancelbtnWhite.setOnClickListener {
                cancelCountdown()
                dispatchResult(DialogAction.ClickWhite)
                this@SDK_DACancel.dismiss()
            }
            DaCancelbtnBlue.setOnClickListener {
                cancelCountdown()
                dispatchResult(DialogAction.ClickBlue)
                this@SDK_DACancel.dismiss()
            }
        }

        setCancelable(false)
    }

    private fun startCountdown() {
        // Reiniciamos por si acaso
        cancelCountdown()
        countdown = timmeDismiss

        countdownRunnable = object : Runnable {
            override fun run() {
                // Si ya se envió la acción o el diálogo no está visible, no continuamos
                if (isActionDispatched || !isShowing) {
                    cancelCountdown()
                    return
                }

                countdown--
                if (countdown >= 0) {
                    handler.postDelayed(this, 1000L)
                } else {
                    // Tiempo agotado: cancelamos el temporizador y despachamos la acción
                    cancelCountdown()
                    dispatchResult(DialogAction.AutoDismiss)
                    dismiss()
                }
            }
        }

        handler.postDelayed(countdownRunnable!!, 1000L)
    }

    private fun cancelCountdown() {
        countdownRunnable?.let { handler.removeCallbacks(it) }
        countdownRunnable = null
    }

    private fun dispatchResult(value: DialogAction) {
        if (isActionDispatched) return
        isActionDispatched = true
        actionButton(value)
    }

    override fun onStop() {
        // Asegura que el temporizador no siga ejecutándose si el diálogo se cierra
        cancelCountdown()
        super.onStop()
    }

    override fun dismiss() {
        // Cancela el temporizador siempre que se haga dismiss programático
        cancelCountdown()
        super.dismiss()
    }

    override fun onDetachedFromWindow() {
        // Liberamos el binding para evitar fugas de memoria
        _binding = null
        super.onDetachedFromWindow()
    }
}