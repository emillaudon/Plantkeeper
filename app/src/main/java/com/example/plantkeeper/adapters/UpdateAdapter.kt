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

    // Context object used to inflate list_item layout
    //private var listItems: List<Post> = posts
    private var context: Context? = context

    // Generated constructor from members
    fun UpdateAdapter(
        //listItems: List<Post>,
        context: Context?
    ) {
        //this.listItems = listItems
        this.context = context
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var textViewHeader: TextView = itemView.findViewById(R.id.updateText)
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
        holder.ageTextView.text = updates[position].time.toString() + " days old"

    }
    override fun getItemCount(): Int {
    return updates.count()

} }



/*
val post : Post? = listItems?.get(position)
            if (post != null) {
                holder.textViewText.text = post.getText().toString()
                if (post.getUserName() != null) {
                    holder.textViewHeader.text = EncryptionHandler.decrypt(post.getUserName()!!)
                }
                else {
                    holder.textViewHeader.text = post.getUserName()
                }
                holder.degreesViewText.text = post.getTemperature().toString() + "°C"



                println("abc" + IconHandler.get("sun").toString())

                var icon: Bitmap? = null

                if (post.getTemperature()!! >= 20 && IconHandler.isCached()) {
                    icon = IconHandler.get("sun")!!
                }
                else if(IconHandler.isCached()) {
                    icon = IconHandler.get("snow")!!
                }
                if (icon != null)
                    holder.postIcon.setImageBitmap(icon)


                if (position % 2 == 0) {
                    holder.relativeLayout.setBackgroundResource(R.color.colorAccent)
                } else if (position % 3 == 0) {
                    holder.relativeLayout.setBackgroundResource(R.color.postColorTwo)
                } else {
                    holder.relativeLayout.setBackgroundResource(R.color.postColorOne)
                }
            }
 */