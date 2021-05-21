package com.example.plantkeeper.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.plantkeeper.R
import com.example.plantkeeper.models.PlantUpdate
import com.squareup.picasso.Picasso

class UpdateAdapter(
    context: Context,
    var updates: List<PlantUpdate>

) : RecyclerView.Adapter<UpdateAdapter.ViewHolder>() {
    private var context: Context? = context

    fun UpdateAdapter(
        context: Context?
    ) {
        this.context = context
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.imageViewUpdate)

        var heightTextView = itemView.findViewById<TextView>(R.id.heightSpecific)

        var note = itemView.findViewById<TextView>(R.id.textView)

        var ageTextView = itemView.findViewById<TextView>(R.id.ageText)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.update_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(updates[position].image).into(holder.image);
        holder.heightTextView.text = "${ (updates[position].height) } CM"
        holder.note.text = updates[position].note
        holder.ageTextView.text = updates[position].daysOld + " days old"

    }
    override fun getItemCount(): Int {
    return updates.count()

} }