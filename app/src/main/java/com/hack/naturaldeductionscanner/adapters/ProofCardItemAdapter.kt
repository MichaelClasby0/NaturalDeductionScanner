package com.hack.naturaldeductionscanner.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hack.naturaldeductionscanner.R
import kotlinx.android.synthetic.main.proof_card_item_layout.view.*
import java.io.File


data class ProofCard(
    var title: String,
    var date: String,
    var verified: String,
    var image: String,
    var fullPath: String,
    var parsedLogic: String
)

class ProofCardItemAdapter(private val listener: (String) -> Unit) :
    ListAdapter<ProofCard, ProofCardItemAdapter.ViewHolder>(
        FormatDiffer
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            parent.inflate(
                R.layout.proof_card_item_layout,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindItem(item)



        //Trash button click listener
        holder.itemView.btnTrash.setOnClickListener {

//            MaterialDialog(holder.itemView.context).show {
//                title(text = "Delete Proof ?")
//                message(text = "Would you like to delete the selected proof?")
//                positiveButton(text = "Delete") {
                    File(item.fullPath).delete()
//                    //Log.d("PATH",item.fullPath)
//                }
//                negativeButton(text = "Cancel") { dialog ->
//                    dialog.cancel()
//                }
//            }

        }




        holder.itemView.imgCard.setOnClickListener {
            listener(item.fullPath)
        }

        holder.itemView.setOnClickListener {
            listener(item.fullPath)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindItem(item: ProofCard) {
            view.proofTitle.text = item.title
            view.date.text = item.date
            view.verified.text = item.verified
            //Log.d("S", item.image)
            view.imgCard.load(File(item.image))

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