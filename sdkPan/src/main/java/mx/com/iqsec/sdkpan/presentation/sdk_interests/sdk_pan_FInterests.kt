package mx.com.iqsec.sdkpan.presentation.sdk_interests

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import mx.com.iqsec.sdkpan.R
import mx.com.iqsec.sdkpan.common.constants.SDK_CAExtensions.dialogNetworkError
import mx.com.iqsec.sdkpan.common.constants.SDK_Constants
import mx.com.iqsec.sdkpan.databinding.SdkPanFinterestsBinding
import mx.com.iqsec.sdkpan.domain.model.UserINEModel
import mx.com.iqsec.sdkpan.model.Minterest
import mx.com.iqsec.sdkpan.presentation.adapters.SDK_Pan_interestAdapter
import mx.com.iqsec.sdkpan.presentation.onStateToolbarListener
import mx.com.iqsec.sdkpan.presentation.sdk_base.SDK_PAN_BFragment

class sdk_pan_FInterests : SDK_PAN_BFragment() {
    private var _binding: SdkPanFinterestsBinding? = null
    private val binding get() = _binding!!

    private var isFrontTaken = false
    private var onStateToolbar: onStateToolbarListener? = null
    private var interestsSelected = ArrayList<Minterest>()
    private var userInfo: UserINEModel? = null

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
        _binding = SdkPanFinterestsBinding.inflate(inflater, container, false)
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

        interestsSelected = ArrayList()
        loadInterests()

        binding.sdkPanSocialNetworkBtnContinue.setOnClickListener {
            if (onStateToolbar?.hasConnection() == false) {
                requireActivity().dialogNetworkError() {}
                return@setOnClickListener
            }

            userInfo = userInfo?.copy(
                userInterests = interestsSelected
            )
            savePreferenceObject(SDK_Constants.PARAM_CREDENTIAL, userInfo)
            onNavigate(R.id.action_nav_FInterests_to_nav_FSocialNetwork)
        }
    }

    private fun getData() {
        userInfo = getPreferenceObject(SDK_Constants.PARAM_CREDENTIAL)
    }

    private fun loadInterests() {
        var interests = ArrayList<Minterest>()
        interests.add(Minterest(1, "Integrarte a Acción Juvenil", false))
        interests.add(Minterest(2, "Integrarte a Promoción Política de la Mujer", false))
        interests.add(Minterest(3, "Capacitarte", false))
        interests.add(Minterest(4, "Ser capacitador", false))
        interests.add(
            Minterest(
                5,
                "Liderar o apoyar causas sociales (Familia, medio ambiente, vida, grupos vulnerables)",
                false
            )
        )
        interests.add(Minterest(6, "Activista digital, ayudar en redes sociales", false))
        interests.add(Minterest(7, "Representante de casilla", false))
        interests.add(Minterest(8, "Ayudar en defensa jurídica del voto", false))
        interests.add(Minterest(9, "Sumar en territorio puerta por puerta", false))
        interests.add(Minterest(10, "Apoyo en la defensa jurídica", false))

        //verificar si ya tiene intereses seleccionados
        val existingInterests = userInfo?.userInterests ?: ArrayList()
        if (!existingInterests.isEmpty()) {
            val updatedInterests = interests.map { interest ->
                if (existingInterests.any { it.id == interest.id }) {
                    interest.isSelected = true
                }
                interest
            }

            interestsSelected = existingInterests
            interests = ArrayList(updatedInterests)

            if (interestsSelected.size >= 1)
                binding.sdkPanSocialNetworkBtnContinue.isEnabled = true
        }

        binding.sdkPanInterestsList.layoutManager = LinearLayoutManager(requireContext())
        binding.sdkPanInterestsList.adapter = SDK_Pan_interestAdapter(interests) {
            val interest = it
            //revisar si existe en la lista
            if (interest.isSelected == false) {
                interestsSelected.removeIf { it.id == interest.id }
                Log.e(
                    "InterestSelected",
                    "Intereses User ${interest.name} eliminado ${interestsSelected.size}"
                )
            } else {
                interestsSelected.add(interest)
                Log.e(
                    "InterestSelected",
                    "Intereses User ${interest.name} agregado ${interestsSelected.size}"
                )
            }
            Log.e("InterestSelected", "Intereses User ${interestsSelected.size}")
            if (interestsSelected.size >= 1) {
                binding.sdkPanSocialNetworkBtnContinue.isEnabled = true
            } else {
                binding.sdkPanSocialNetworkBtnContinue.isEnabled = false
            }
        }
    }
}