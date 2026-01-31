package mx.com.iqsec.sdkpan.presentation.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import mx.com.iqsec.sdkpan.model.SDK_MSpinner

class SDK_Pan_SpinnerAdapter(
    context: Context,
    resource: Int,
    private val servicesList: List<SDK_MSpinner>
) :
    ArrayAdapter<SDK_MSpinner>(context, resource) {

    override fun getCount(): Int {
        return servicesList.size
    }

    override fun getItem(position: Int): SDK_MSpinner? {
        return servicesList.get(position)
    }

    fun getSelectedItem(position: Int): SDK_MSpinner {
        return servicesList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position)
        view.findViewById<TextView>(android.R.id.text1)?.text = item?.name?.uppercase()
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val item = getItem(position)
        view.findViewById<TextView>(android.R.id.text1)?.text = item?.name?.uppercase()
        return view
    }

}