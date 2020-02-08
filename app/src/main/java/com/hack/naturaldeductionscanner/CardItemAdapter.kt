package com.hack.naturaldeductionscanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item_layout.view.*

data class ProofCard(var Title : String,
                     var Date : String,
                     var Verified : Boolean,
                     var Image : String)
)

class CardItemAdapter(private val listener: (Int) -> Unit) :
    ListAdapter<ProofCard, CardItemAdapter.ViewHolder>(
        FormatDiffer
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.card_item_layout,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        holder.itemView.setOnClickListener {
            listener(position)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindItem(item: ProofCard) {
            view.title.text = item.Title
            view.date.text = item.Date

        }
    }

    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    object FormatDiffer : DiffUtil.ItemCallback<ProofCard>() {
        override fun areItemsTheSame(oldItem: ProofCard, newItem: ProofCard): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProofCard, newItem: ProofCard): Boolean {
            return oldItem == newItem
        }
    }
}