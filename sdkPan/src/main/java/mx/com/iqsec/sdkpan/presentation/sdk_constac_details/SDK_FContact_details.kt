package mx.com.iqsec.sdkpan.presentation.sdk_constac_details

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import com.google.android.material.textfield.TextInputLayout
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkFcontactDetailsBinding
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.SDK_MSpinner
import mx.com.iqsec.sdkpan.presentation.adapters.SDK_Pan_SpinnerAdapter
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FContact_details : SDK_PAN_BFragment() {
    private var _binding: SdkFcontactDetailsBinding? = null
    private val binding get() = _binding!!
    private var itemSelected: SDK_MSpinner? = null
    private val items: List<SDK_MSpinner> = listOf<SDK_MSpinner>(
        SDK_MSpinner(0, "SELECCIONA UNA OPCIÓN"),
        SDK_MSpinner(1, "SOLO LEER Y ESCRIBIR"),
        SDK_MSpinner(2, "PRIMARIA"),
        SDK_MSpinner(3, "SECUNDARIA"),
        SDK_MSpinner(4, "PREPARATORIA O BACHILLERATO"),
        SDK_MSpinner(5, "PROFESIONAL TÉCNICA/COMERCIAL"),
        SDK_MSpinner(6, "LICENCIATURA"),
        SDK_MSpinner(6, "POSGRADO")
    )
    private var userINE: UserINEModel? = null
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
    ): View? {
        _binding = SdkFcontactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
        initListeners()
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.sdkPanContactDetailsBtnContinue.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.CONTACT_DETAILS_STEP) }
                return@setOnClickListener
            }

            if (validateInputs()) {
                saveDataContactDetails()
            }
        }

        binding.sdkPanContactDetailsRGDialect.setOnCheckedChangeListener { rg, id ->
            //si elige si, mostrar campo para el dialecto
            val elemt = binding.sdkPanContactDetailsRBDialectYes.id
            if (rg.checkedRadioButtonId == elemt) {
                binding.sdkPanContactDetailsLLDialectInput.visibility = View.VISIBLE
            } else {
                binding.sdkPanContactDetailsLLDialectInput.visibility = View.GONE
                binding.sdkPanContactDetailsTIDialectInput.editText?.setText("")
            }

            if (id != -1) {
                binding.sdkPanContactDetailsTIDialect.error = null
            }

            enableBtnContinue()
        }

        initSelectorEducationLevel()
        textWatcerInputs()
        enableBtnContinue()
    }

    private fun enableBtnContinue() {
        binding.sdkPanContactDetailsBtnContinue.isEnabled = if (validateInputs()) true else false
    }

    private fun getData() {
        userINE = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)

        //coloca datos si ya existen
        userINE?.let { user ->
            binding.sdkPanContactDetailsTIEmal.editText?.setText(user.email)
            binding.sdkPanContactDetailsTIPhoneNumber.editText?.setText(user.phone)

            if (!user.speakADialect.isEmpty()) {
                if (user.speakADialect == "NO") {
                    binding.sdkPanContactDetailsRGDialect.check(binding.sdkPanContactDetailsRBDialectNo.id)
                    binding.sdkPanContactDetailsLLDialectInput.visibility = View.GONE
                } else {
                    binding.sdkPanContactDetailsRGDialect.check(binding.sdkPanContactDetailsRBDialectYes.id)
                    binding.sdkPanContactDetailsLLDialectInput.visibility = View.VISIBLE
                    binding.sdkPanContactDetailsTIDialectInput.editText?.setText(user.speakADialect)
                }
            }
        }
    }

    private fun saveDataContactDetails() {
        if (userINE == null) return

        val dialect = binding.sdkPanContactDetailsTIDialectInput.editText?.text.toString()
        userINE = userINE?.apply {
            email = binding.sdkPanContactDetailsTIEmal.editText?.text.toString()
            phone = binding.sdkPanContactDetailsTIPhoneNumber.editText?.text.toString()
            schooling = itemSelected?.name ?: ""

            speakADialect = if (dialect.isNotEmpty()) dialect else "NO"
        }

        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)
        onNavigate(R.id.action_nav_FContact_details_to_nav_FInterests)
    }

    private fun textWatcerInputs() {
        binding.apply {
            sdkPanContactDetailsTIEmal.createCommonTextWatcher()
            sdkPanContactDetailsTIPhoneNumber.createCommonTextWatcher()
            sdkPanContactDetailsTIEducationLevel.createCommonTextWatcher()
            sdkPanContactDetailsTIDialectInput.createCommonTextWatcher()
        }
    }

    private fun TextInputLayout.createCommonTextWatcher() {
        this.editText?.addTextChangedListener(this.AddTextWatcher())
    }

    private fun TextInputLayout.AddTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            if (!s.isNullOrEmpty()) {
                this@AddTextWatcher.error = null
                enableBtnContinue()
            } else {
                validateInputs()
            }
        }
    }


    private fun validateInputs(): Boolean {
        var isValid = 0

        binding.apply {
            //validacion de correo
            isValid += sdkPanContactDetailsTIEmal.Validate(
                Pair(
                    "El correo electrónico es requerido.",
                    "Ingresa un correo electrónico válido."
                ),
                isEmail = true
            )
            isValid += sdkPanContactDetailsTIPhoneNumber.Validate(
                Pair(
                    "El número celular es requerido.",
                    "Ingresa un número celular de 10 dígitos."
                ),
                minLength = 10
            )
            isValid += sdkPanContactDetailsTIEducationLevel.Validate(
                Pair("Selecciona una escolaridad.", "Selecciona una escolaridad."),
                valSpinner = itemSelected?.id ?: 0
            )

            if (sdkPanContactDetailsLLDialectInput.visibility == View.VISIBLE) {
                isValid += sdkPanContactDetailsTIDialectInput.Validate(
                    Pair("Ingresa un dialecto.", "Ingresa un dialecto.")
                )
            }

            isValid += validDialect()
        }

        return isValid == 0
    }

    private fun validDialect(): Int {
        val rg = binding.sdkPanContactDetailsRGDialect
        val selectedRB = rg.checkedRadioButtonId != -1

        binding.sdkPanContactDetailsTIDialect.error = if (!selectedRB) {
            "Selecciona una opción"
        } else {
            null
        }

        return if (!selectedRB) 1 else 0
    }

    private val EMAIL_REGEX = java.util.regex.Pattern.compile(
        "^[^@]+@[^@]+\\.[a-zA-Z]{2,}$"
    )

    fun isValidEmail(email: String?): Boolean {
        if (email.isNullOrBlank()) return false
        val e = email.trim()
        return EMAIL_REGEX.matcher(e).matches()
    }

    private fun TextInputLayout.Validate(
        textError: Pair<String, String> = Pair(
            "Este campo es requerido.",
            "Este campo es requerido."
        ),
        minLength: Int = 0,
        valSpinner: Int = -1,
        isEmail: Boolean = false
    ): Int {
        //Validacion de campo requerido
        val inputText = this.editText?.text.toString()
        var hasError: Int = 0

        if (inputText.isEmpty()) {
            this.error = textError.first
            hasError = 1
        } else if (minLength > 0 && (inputText.length < minLength) || inputText.all { it == inputText.first() }) {
            this.error = textError.first

            if (inputText.length < minLength) {
                this.error = textError.second
            } else if (inputText.length == minLength && inputText.all { it == inputText.first() }) {
                this.error = "Ingresa un número celular válido"
            }

            hasError = 1
        } else if (valSpinner != -1 && valSpinner == 0) {
            this.error = textError.first
            this.editText?.error = textError.first
            hasError = 1
        } else if (isEmail && !isValidEmail(inputText)) {
            this.error = textError.second
            hasError = 1
        } else {
            this.error = null
            hasError = 0
        }

        return hasError
    }

    fun validateCelular(value: String): Boolean {
        val digits = value.filter { it.isDigit() }
        if (digits.isEmpty()) return false
        if (digits.length != 10) return false
        val first = digits.first()
        if (digits.all { it == first }) return false
        return true
    }


    private fun initSelectorEducationLevel() {
        val adapterServices = SDK_Pan_SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            items
        )

        binding.sdkPanContactDetailsSPEducationLevel.setAdapter(adapterServices)
        binding.sdkPanContactDetailsSPEducationLevel.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item =
                    binding.sdkPanContactDetailsSPEducationLevel.adapter.getItem(position) as SDK_MSpinner
                itemSelected = item
                if (item.id != 0) {
                    binding.sdkPanContactDetailsTIEducationLevel.error = null
                }
                enableBtnContinue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        //setea escolaridad
        val schoolingItem = items.find { it.name == userINE?.schooling }
        if (schoolingItem != null) {
            val position = items.indexOf(schoolingItem)
            binding.sdkPanContactDetailsSPEducationLevel.setSelection(position)
            itemSelected = schoolingItem
        }
    }
}