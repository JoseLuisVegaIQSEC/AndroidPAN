package mx.com.iqsec.sdkpan.presentation.sdk_credential

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkPanFcredentialuserBinding
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.MTestLife
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment
import kotlin.math.min

//TODO REVISAR FOLIO NO NULO
//TODO REVISAR LONGITUD DE CLAVE DE ELECTOR Y CURP
//TODO REVISAR CAPTURA AUTOMATICA DE INE NUEVA
//TODO REVISAR PRUEBAS UNITARIAS
class sdk_pan_FCredentialUser : SDK_PAN_BFragment() {
    private var _binding: SdkPanFcredentialuserBinding? = null
    private val binding get() = _binding!!
    private var userInfo: UserINEModel? = null
    private var credentialInfo: DataUserModel? = null
    private var findOb = BaseConfig()
    private var onStateToolbar: onStateToolbarListener? = null

    override fun onDetach() {
        super.onDetach()
        onStateToolbar = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onStateToolbar = context as? onStateToolbarListener
    }

    override fun onResume() {
        super.onResume()
        onStateToolbar?.activityUpdateToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SdkPanFcredentialuserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initListeners()
    }

    private fun getData() {
        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }

        userInfo = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)

        credentialInfo = getPreferenceObject(SDK_Constants.PARAM_PAN_CREDENTIAL)

        handleResponseCloseFile(credentialInfo!!)
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.sdkPanCredentialBtnContinue.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.CREDENTIAL_STEP) }
                return@setOnClickListener
            }

            onNavigate(R.id.action_nav_FCredentialUser_to_nav_FKnowMore)
        }
    }

    private fun handleResponseCloseFile(data: DataUserModel) {
        binding.apply {
            sdkPanCredentialTxtFirstName.text = data.nombre

            val lastName = getString(
                R.string.sdk_pan_f_credential_txt_last_name_format,
                data.primerApellido.orEmpty(),
                data.segundoApellido.orEmpty()
            )
            sdkPanCredentialTxtLastName.setText(lastName)
            sdkPanCredentialTxtState.text = data.entidadFederativa
            sdkPanCredentialTxtMunicipality.text = data.municipio

            sdkPanCredentialTxtFolio.text = data.folioAfiliado.toString()
            sdkPanCredentialTxtRegisterDate.text = data.registro

            val objTestLife = getPreferenceObject<MTestLife>(SDK_Constants.RESULT_ANTISPOFFING)
            val imageUser = base64ToBitmap(data.rostro ?: objTestLife?.image)
            // Llamada simplificada: solo bitmap, punto normalizado y factor de escala
            sdkPanCredentialImgProfile.zoomBitmapToNormalizedPoint(
                imageUser,
                0.5f,
                0.6f,
                extraScale = 1.8f
            )
            sdkPanCredentialTxtRole.text = resources.getString(R.string.sdk_pan_welcome_militant)

            val qrBitmap = base64ToBitmap(data.qr ?: "")
            sdkPanCredentialImgQR.setImageBitmap(qrBitmap)
        }
    }

    private fun base64ToBitmap(base64Str: String?): Bitmap? {
        if (base64Str.isNullOrBlank()) return null
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun ImageView.zoomBitmapToNormalizedPoint(
        bitmap: Bitmap?,
        normX: Float,
        normY: Float,
        extraScale: Float = 1.0f
    ) {
        if (bitmap == null) return

        // Aseguramos que el bitmap se asigne siempre; si el cálculo del zoom falla,
        // en el catch volvemos a asignar el bitmap sin transformar
        setImageBitmap(bitmap)

        try {
            if (width == 0 || height == 0) {
                post { zoomBitmapToNormalizedPoint(bitmap, normX, normY, extraScale) }
                return
            }

            scaleType = ImageView.ScaleType.MATRIX
            val targetMatrix = Matrix()

            val imgW = bitmap.width.toFloat()
            val imgH = bitmap.height.toFloat()
            val vW = width.toFloat()
            val vH = height.toFloat()

            val baseScale = min(vW / imgW, vH / imgH)
            val fullScale = baseScale * extraScale

            val fx = normX.coerceIn(0f, 1f) * imgW
            val fy = normY.coerceIn(0f, 1f) * imgH

            val sx = fx * fullScale
            val sy = fy * fullScale

            var tx = vW / 2f - sx
            var ty = vH / 2f - sy

            val minTx = vW - imgW * fullScale
            val minTy = vH - imgH * fullScale
            tx = tx.coerceIn(minTx, 0f)
            ty = ty.coerceIn(minTy, 0f)

            targetMatrix.postScale(fullScale, fullScale)
            targetMatrix.postTranslate(tx, ty)

            // Aplicar zoom inmediatamente sin animación
            imageMatrix = targetMatrix
        } catch (e: Exception) {
            // En caso de cualquier fallo, aseguramos que la imagen se muestre sin transformaciones
            setImageBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}