package mx.com.iqsec.sdkpan.presentation.dialog

import androidx.appcompat.app.AppCompatDialog

object DialogManager {
    private var currentDialog: AppCompatDialog? = null

    fun show(dialog: AppCompatDialog) {
        // Cierra el diálogo anterior si sigue visible
        currentDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }

        currentDialog = dialog
        dialog.setOnDismissListener {
            if (currentDialog == dialog) {
                currentDialog = null
            }
        }
        dialog.show()
    }

    fun dismissCurrent() {
        currentDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        currentDialog = null
    }
}
