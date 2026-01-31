package mx.com.iqsec.sdkpan.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mx.com.iqsec.sdkpan.databinding.SdkPanItemInterestBinding
import mx.com.iqsec.sdkpan.model.Minterest

class SDK_Pan_interestAdapter(
    private val arrayList: ArrayList<Minterest>,
    var onClickitem: (code: Minterest) -> Unit
) :
    RecyclerView.Adapter<SDK_Pan_interestAdapter.ViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SdkPanItemInterestBinding.inflate(inflater)
        return ViewHolder(binding, onClickitem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = arrayList[position]
        holder.bindView(m)
    }

    override fun getItemCount() = arrayList.size

    class ViewHolder(
        private val binding: SdkPanItemInterestBinding,
        var onClickitem: (code: Minterest) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(item: Minterest) {
            if (item.isSelected)
                binding.sdkPanInterestCheckBox.isChecked = true

            binding.sdkPanInterestCheckBox.text = item.name
            binding.sdkPanInterestCheckBox.setOnClickListener {
                val itemState = item
                itemState.isSelected = this.binding.sdkPanInterestCheckBox.isChecked
                onClickitem(itemState)
            }
        }
    }
}