package mx.com.iqsec.sdkpan.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import mx.com.iqsec.sdkpan.databinding.SdkDconfirmationBottomBinding
import mx.com.iqsec.sdkpan.presentation.adapters.SDK_Pan_OptionAdapter

class ConfirmationBottomSheet(
    private val title: String,
    private val confirmText: String,
    private val items: List<String>
) : BottomSheetDialogFragment() {
    private lateinit var binding: SdkDconfirmationBottomBinding
    private var selectedOption: String? = null

    interface Listener {
        fun onItemSelected(item: String)
    }

    var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SdkDconfirmationBottomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.sdkPanCbRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sdkPanCbRecyclerView.adapter = SDK_Pan_OptionAdapter(items) { cad, pos ->
            selectedOption = cad
            binding.cbConfirm.isEnabled = true
        }

        binding.cbTitle.text = title
        binding.cbConfirm.text = confirmText

        binding.cbConfirm.setOnClickListener {
            listener?.onItemSelected(selectedOption ?: "")
            dismiss()
        }
    }
}