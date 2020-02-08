package com.hack.naturaldeductionscanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.tutorial_card_item_layout.view.*

data class TutorialCard(var title : String,
                        var description : String,
                        var completed : String)

class TutorialCardItemAdapter(private val listener: (TutorialCard) -> Unit) :
    ListAdapter<TutorialCard, TutorialCardItemAdapter.ViewHolder>(
        FormatDiffer
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.tutorial_card_item_layout,
                false
            )
        )


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)
        holder.itemView.setOnClickListener {
            listener(item)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindItem(item: TutorialCard) {
            view.tutorialTitle.text = item.title
            view.description.text = item.description
            view.completed.text = item.completed

        }
    }



    private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

    object FormatDiffer : DiffUtil.ItemCallback<TutorialCard>() {
        override fun areItemsTheSame(oldItem: TutorialCard, newItem: TutorialCard): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TutorialCard, newItem: TutorialCard): Boolean {
            return oldItem == newItem
        }
    }
}