package com.example.pozi_android.ui.searchLocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pozi_android.databinding.ItemSearchAddressBinding
import com.example.pozi_android.ui.searchLocation.model.SearchModel

class SearchLocationAdapter(
    private var list: List<SearchModel>,
    private val onClick: (SearchModel) -> Unit,
) : RecyclerView.Adapter<SearchLocationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchAddressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick = onClick,
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])
    override fun getItemCount(): Int = list.size

    fun updateList(updatedList: List<SearchModel>) {
        list = updatedList
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: ItemSearchAddressBinding,
        onClick: (SearchModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(binding.model ?: return@setOnClickListener)
            }
        }

        fun bind(item: SearchModel) {
            binding.model = item
        }
    }

}