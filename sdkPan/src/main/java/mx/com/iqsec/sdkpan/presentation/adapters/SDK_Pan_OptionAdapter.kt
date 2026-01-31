package mx.com.iqsec.sdkpan.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import mx.com.iqsec.sdkpan.R

class SDK_Pan_OptionAdapter(
    private var items: List<String> = emptyList(),
    private val onItemClick: (String, Int) -> Unit = { _, _ -> }
) : RecyclerView.Adapter<SDK_Pan_OptionAdapter.ViewHolder>() {

    private var selectedPos: Int = RecyclerView.NO_POSITION

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.sdkPanItemOptionTxtTitle)

        init {
            view.setOnClickListener {
                val pos = bindingAdapterPosition
                if (pos == RecyclerView.NO_POSITION) return@setOnClickListener

                animateSelection(itemView)
                val previous = selectedPos
                selectedPos = pos
                if (previous != RecyclerView.NO_POSITION) notifyItemChanged(previous)
                notifyItemChanged(selectedPos)
                onItemClick(items[pos], pos)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.sdk_pan_item_option, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = items[position]
        holder.itemView.isSelected = position == selectedPos
        holder.itemView.alpha = if (position == selectedPos) 0.95f else 1f
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<String>) {
        items = newItems
        selectedPos = RecyclerView.NO_POSITION
        notifyDataSetChanged()
    }

    private fun animateSelection(view: View) {
        view.animate()
            .scaleX(0.97f)
            .scaleY(0.97f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
}