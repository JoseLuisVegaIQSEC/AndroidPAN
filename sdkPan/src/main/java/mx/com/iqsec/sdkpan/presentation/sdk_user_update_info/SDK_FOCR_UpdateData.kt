package mx.com.iqsec.sdkpan.presentation.sdk_user_update_info

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mx.com.iqsec.auto_detection_ine.constants.SDK_CAExtensions.deleteImageTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageBackTemp
import mx.com.iqsec.auto_detection_ine.presentation.detection.CDConstants.imageFrontTemp
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogErrorService
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogUserExist
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkFocrUpdatedataBinding
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.util.SDK_ResultValue
import mx.com.iqsec.sdkpan.model.BaseConfig
import mx.com.iqsec.sdkpan.model.SDK_MSpinner
import mx.com.iqsec.sdkpan.presentation.adapters.SDK_Pan_SpinnerAdapter
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class SDK_FOCR_UpdateData : SDK_PAN_BFragment() {
    private var _binding: SdkFocrUpdatedataBinding? = null
    private val binding get() = _binding!!
    private var onStateToolbar: onStateToolbarListener? = null
    private var userINE: UserINEModel? = null
    private val credentialViewModel: SDK_VM_UpdateInfo by viewModels()
    private var findOb = BaseConfig()
    private var stateSelected: SDK_MSpinner? = null
    private var municipalitySelected: SDK_MSpinner? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SdkFocrUpdatedataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { // Noncompliant - method is empty

                }
            })

        binding.btnEnter.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                handleConectionAttemps() { actionFinish(it, SDK_Constants.UPDATE_DATA_STEP) }
                return@setOnClickListener
            }

            if (validateFields()) {
                updateUserINE()
            }
        }

        binding.sdkPanOCRCOmpleteDataBtnRetakeINE.setOnClickListener {
            goToNoticeINE()
        }

        val etNoRegister = binding.sdkPanOCRCOmpleteDataETNoRegister
        etNoRegister.filters = arrayOf(android.text.InputFilter.LengthFilter(7))
        etNoRegister.addTextChangedListener(createNoRegisterMaskWatcher(etNoRegister))

        playAnimationSpinner(binding.sdkPanOCRCOmpleteDataLoadingState)
        playAnimationSpinner(binding.sdkPanOCRCOmpleteDataLoadingMunipalities)

        initObservers()
        getData()
        initializeTextWatcherInputs()
        enableBtnContinue()
    }

    private fun enableBtnContinue() {
        binding.btnEnter.isEnabled = if (validateFields()) true else false
    }

    // Función helper
    fun createNoRegisterMaskWatcher(editText: TextInputEditText) = object : TextWatcher {
        var isUpdating = false

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) return

            val digitsOnly = s?.toString()?.replace("\\D".toRegex(), "") ?: ""
            val maxDigits = 6 // 4 + 2
            val trimmed = if (digitsOnly.length > maxDigits) digitsOnly.substring(
                0,
                maxDigits
            ) else digitsOnly

            val part1 = if (trimmed.length >= 4) trimmed.substring(0, 4) else trimmed
            val part2 = if (trimmed.length > 4) trimmed.substring(4) else ""
            val formatted = if (part2.isNotEmpty()) "$part1 $part2" else part1

            isUpdating = true
            editText.setText(formatted)
            editText.setSelection(formatted.length.coerceAtMost(editText.text?.length ?: 0))
            isUpdating = false
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    private fun goToNoticeINE() {
        deleteImageTemp(requireContext(), imageBackTemp)
        deleteImageTemp(requireContext(), imageFrontTemp)

        val userINE: UserINEModel? = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)
        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE?.cleanDataMembership())

        onNavigate(R.id.action_nav_UpdateData_to_nav_SDK_FNoticeINE)
    }

    private fun playAnimationSpinner(view: View) {
        val animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.sdk_pan_spinner_loading_translate)
        view.visibility = View.VISIBLE
        view.startAnimation(animation)
    }

    private fun initObservers() {
        credentialViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            showProgress(it)
        }

        credentialViewModel.stateList.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            when (it) {
                is SDK_ResultValue.Success -> handleStates(it.data)

                is SDK_ResultValue.Error -> messageOnError(
                    SDK_Constants.UPDATE_DATA_STEP,
                    it.error,
                    it.error.second,
                    { actionFinish(it, SDK_Constants.UPDATE_DATA_STEP) },
                    {
                        getData()
                    }
                )
            }
        }

        credentialViewModel.municipalitiesData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            when (it) {
                is SDK_ResultValue.Success -> handleMunicipalities(it.data)

                is SDK_ResultValue.Error -> messageOnError(
                    SDK_Constants.UPDATE_DATA_STEP,
                    it.error,
                    it.error.second,
                    { actionFinish(it, SDK_Constants.UPDATE_DATA_STEP) },
                    { getMunicipalities() }
                )
            }
        }

        credentialViewModel.membershipData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            when (it) {
                is SDK_ResultValue.Success -> handlerSericeMembership(it.data)

                is SDK_ResultValue.Error -> {
                    showProgress(false)
                    messageOnError(
                        SDK_Constants.UPDATE_DATA_STEP,
                        it.error,
                        it.error.second,
                        { actionFinish(it, SDK_Constants.UPDATE_DATA_STEP) },
                        {}
                    )
                }
            }
        }

        credentialViewModel.ValidationINEData.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            when (it) {
                is SDK_ResultValue.Success -> {
                    showProgress(false)
                    handlerServiceValidationINE(it.data)
                }

                is SDK_ResultValue.Error -> {
                    showProgress(false)
                    messageOnError(
                        SDK_Constants.UPDATE_DATA_STEP,
                        it.error,
                        it.error.second,
                        { actionFinish(it, SDK_Constants.UPDATE_DATA_STEP) },
                        { }
                    )
                }
            }
        }
    }

    private fun getData() {
        val prefBConf = getConf(SDK_Constants.BASE_CONF)
        if (prefBConf != null) {
            findOb = prefBConf
        }

        userINE = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)

        binding.item = userINE

        credentialViewModel.getStates(findOb, requireContext())
    }

    private fun handleStates(states: List<StatesModel>) {
        //reinicia el contador de intentos
        resetAttempts()

        val dataList = arrayListOf<SDK_MSpinner>()
        dataList.add(SDK_MSpinner(0, "-- Selecciona un estado --"))
        states.forEach {
            dataList.add(SDK_MSpinner(it.id_estado, it.nombre ?: "", it.abreviatura ?: ""))
        }

        val adapterStates = SDK_Pan_SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            dataList
        )

        binding.sdkPanOCRCOmpleteDataETEState.setAdapter(adapterStates)
        binding.sdkPanOCRCOmpleteDataETEState.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item =
                    binding.sdkPanOCRCOmpleteDataETEState.adapter.getItem(position) as SDK_MSpinner
                stateSelected = item
                enableBtnContinue()
                if (item.id != 0) {
                    if (item.id == 87) {
                        handleEmptyMunicipalities()
                    } else {
                        getMunicipalities()
                    }
                } else {
                    playAnimationSpinner(binding.sdkPanOCRCOmpleteDataLoadingMunipalities)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //en caso de que exista estado seleccionado
        val steteItem = dataList.find { it.name == userINE?.state }
        if (steteItem != null) {
            val position = dataList.indexOf(steteItem)
            binding.sdkPanOCRCOmpleteDataETEState.setSelection(position)
            stateSelected = steteItem
        }

        binding.sdkPanOCRCOmpleteDataLoadingState.animation?.cancel()
        binding.sdkPanOCRCOmpleteDataLoadingState.visibility = View.GONE
    }

    private fun getMunicipalities() {
        val item =
            binding.sdkPanOCRCOmpleteDataETEState.selectedItem as SDK_MSpinner
        playAnimationSpinner(binding.sdkPanOCRCOmpleteDataLoadingMunipalities)
        credentialViewModel.getMunicipalities(item.id, findOb, requireContext())
    }

    private fun handleEmptyMunicipalities() {
        val dataList = arrayListOf<SDK_MSpinner>()
        dataList.add(SDK_MSpinner(0, "NINGUNO"))
        municipalitySelected = dataList[0]

        val adapterMunicipalities = SDK_Pan_SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            dataList
        )
        binding.sdkPanOCRCOmpleteDataETEMunicipality.setAdapter(adapterMunicipalities)
        binding.sdkPanOCRCOmpleteDataTIEMunicipality.error = null
        binding.sdkPanOCRCOmpleteDataLoadingMunipalities.animation?.cancel()
        binding.sdkPanOCRCOmpleteDataLoadingMunipalities.visibility = View.GONE
        binding.sdkPanOCRCOmpleteDataETEMunicipality.isEnabled = false
    }

    private fun handleMunicipalities(states: List<MunicipalitiesModel>) {
        //reinicia el contador de intentos
        resetAttempts()

        val dataList = arrayListOf<SDK_MSpinner>()
        dataList.add(SDK_MSpinner(0, "-- Selecciona un municipio --"))
        states.forEach {
            dataList.add(SDK_MSpinner(it.id_municipio, it.nombre ?: ""))
        }

        val adapterMunicipalities = SDK_Pan_SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            dataList
        )
        binding.sdkPanOCRCOmpleteDataETEMunicipality.setAdapter(adapterMunicipalities)
        binding.sdkPanOCRCOmpleteDataETEMunicipality.isEnabled = true

        binding.sdkPanOCRCOmpleteDataETEMunicipality.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val item =
                    binding.sdkPanOCRCOmpleteDataETEMunicipality.adapter.getItem(position) as SDK_MSpinner
                municipalitySelected = item

                enableBtnContinue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //en caso de que exista estado seleccionado
        val municipalityItem = dataList.find { it.name == userINE?.municipio }
        if (municipalityItem != null) {
            val position = dataList.indexOf(municipalityItem)
            binding.sdkPanOCRCOmpleteDataETEMunicipality.setSelection(position)
            municipalitySelected = municipalityItem
        }

        binding.sdkPanOCRCOmpleteDataLoadingMunipalities.animation?.cancel()
        binding.sdkPanOCRCOmpleteDataLoadingMunipalities.visibility = View.GONE
    }

    private fun initializeTextWatcherInputs() {
        binding.apply {
            sdkPanOCRCOmpleteDataTIName.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTIFirstName.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTILastName.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTIElectorKey.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTIECurp.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTINoEmission.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTINoRegister.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTICalle.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTINoExterior.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTIColonia.createCommonTextWatcher()
            sdkPanOCRCOmpleteDataTICodPostal.createCommonTextWatcher()
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
            } else {
                validateFields()
            }
            enableBtnContinue()
        }
    }

    private fun validateFields(): Boolean {
        var isValid = 0

        binding.apply {
            isValid += sdkPanOCRCOmpleteDataTIName.Validate("El nombre es requerido")
            isValid += sdkPanOCRCOmpleteDataTIFirstName.Validate("El apellido paterno es requerido")
            isValid += sdkPanOCRCOmpleteDataTILastName.Validate("El apellido materno es requerido")
            isValid += sdkPanOCRCOmpleteDataTIElectorKey.Validate(
                "La clave de elector es requerida",
                minLength = 18
            )
            isValid += sdkPanOCRCOmpleteDataTIECurp.Validate(
                "La CURP es requerida",
                minLength = 18
            )
            isValid += sdkPanOCRCOmpleteDataTINoEmission.Validate("El año de emisión es requerido")
            isValid += sdkPanOCRCOmpleteDataTINoRegister.Validate(
                "El año de registro es requerido",
                minLength = 7
            )
            isValid += sdkPanOCRCOmpleteDataTICalle.Validate("La calle es requerida")
            isValid += sdkPanOCRCOmpleteDataTINoExterior.Validate("El número exterior es requerido")
            //isValid += sdkPanOCRCOmpleteDataTINoInterior.Validate("El número interior es requerido")
            isValid += sdkPanOCRCOmpleteDataTIColonia.Validate("La colonia es requerida")
            isValid += sdkPanOCRCOmpleteDataTIEState.Validate(
                "El estado es requerido",
                valSpinner = stateSelected?.id ?: 0
            )

            if (stateSelected?.id != 87) {
                isValid += sdkPanOCRCOmpleteDataTIEMunicipality.Validate(
                    "El municipio es requerido",
                    valSpinner = municipalitySelected?.id ?: 0
                )
            }

            isValid += sdkPanOCRCOmpleteDataTICodPostal.Validate(
                "El código postal es requerido",
                minLength = 5
            )
        }

        return isValid == 0
    }

    private fun handlerSericeMembership(data: ValidateMembershipModel) {
        //reinicia el contador de intentos
        resetAttempts()

        //validacion de existencia
        if (data.existe) {
            showProgress(false)

            // se debe mandar alerta de existencia de membresia
            requireActivity().dialogUserExist {
                actionFinish(
                    resources.getString(R.string.sdk_pan_f_update_data_user_exist_title),
                    SDK_Constants.UPDATE_DATA_STEP,
                    SDK_Constants.USER_EXIST
                )
            }
        } else {
            serviceValidaIne()
        }
    }

    private fun serviceValidaIne() {
        credentialViewModel.serviceValidationDataINE(
            cic = userINE?.cic ?: "",
            nombre = userINE?.nombres ?: "",
            paterno = userINE?.primerApellido ?: "",
            materno = userINE?.segundoApellido ?: "",
            claveElector = userINE?.claveElector ?: "",
            numeroEmision = userINE?.noEmision ?: "",
            curp = userINE?.curp ?: "",
            anioRegistro = userINE?.registro ?: "",
            anioEmision = userINE?.emision ?: "", //anio emision
            latitud = 0.0,
            longitud = 0.0,
            ocr = userINE?.ocr ?: "", //ocr
            codigopostal = userINE?.codigoPostal ?: "",
            ciudad = userINE?.ciudad ?: "",
            estado = stateSelected?.id ?: 0,
            config = findOb,
            context = requireContext()
        )
    }

    private fun handlerServiceValidationINE(data: UserINEDataValidationModel) {
        if (!data.isCompleteValidation()) {
            requireActivity().dialogErrorService(
                "La INE no es consistente",
                "La información proporcionada no coincide con los registros oficiales. Por favor, verifica los datos e intenta nuevamente.",
                getString(R.string.lbl_retry)
            ) { goToNoticeINE() }
        } else {
            goToContactDetails()
        }
    }

    private fun TextInputLayout.Validate(
        textError: String = "Este campo es requerido",
        minLength: Int = 0,
        valSpinner: Int = -1
    ): Int {
        //Validacion de campo requerido
        val inputText = this.editText?.text.toString()
        var hasError: Int = 0

        if (inputText.isEmpty()) {
            this.error = textError
            hasError = 1
        } else if (minLength > 0 && inputText.length < minLength) {
            this.error = textError
            hasError = 1
        } else if (valSpinner != -1 && valSpinner == 0) {
            this.error = textError
            this.editText?.error = textError
            hasError = 1
        } else {
            this.error = null
            hasError = 0
        }

        return hasError
    }

    private fun updateUserINE() {
        userINE = userINE?.copy(
            nombres = binding.sdkPanOCRCOmpleteDataTIName.editText?.text.toString(),
            primerApellido = binding.sdkPanOCRCOmpleteDataTIFirstName.editText?.text.toString(),
            segundoApellido = binding.sdkPanOCRCOmpleteDataTILastName.editText?.text.toString(),
            claveElector = binding.sdkPanOCRCOmpleteDataTIElectorKey.editText?.text.toString(),
            curp = binding.sdkPanOCRCOmpleteDataTIECurp.editText?.text.toString(),
            emision = binding.sdkPanOCRCOmpleteDataTINoEmission.editText?.text.toString(),
            registro = binding.sdkPanOCRCOmpleteDataTINoRegister.editText?.text.toString(),
            calle = binding.sdkPanOCRCOmpleteDataTICalle.editText?.text.toString(),
            noExterior = binding.sdkPanOCRCOmpleteDataTINoExterior.editText?.text.toString(),
            noInterior = binding.sdkPanOCRCOmpleteDataTINoInterior.editText?.text.toString(),
            codigoPostal = binding.sdkPanOCRCOmpleteDataTICodPostal.editText?.text.toString(),
            colonia = binding.sdkPanOCRCOmpleteDataTIColonia.editText?.text.toString(),
            codState = stateSelected?.id.toString(),
            state = stateSelected?.name.toString(),
            cortoState = stateSelected?.abrev.toString(),
            codMunicipaly = municipalitySelected?.id.toString(),
            municipio = municipalitySelected?.name.toString(),
        )

        savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userINE)

        credentialViewModel.serviceMembership(
            referencia = userINE?.referencia ?: "",
            curp = userINE?.curp ?: "",
            config = findOb
        )
    }

    private fun goToContactDetails() {
        //Ir a detalle de contacto
        onNavigate(R.id.action_nav_UpdateData_to_nav_FContact_details)
    }
}