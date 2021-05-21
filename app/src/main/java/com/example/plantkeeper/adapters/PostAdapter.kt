package com.example.plantkeeper.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.plantkeeper.R
import com.example.plantkeeper.models.Plant
import com.example.plantkeeper.models.PlantUpdate
import com.squareup.picasso.Picasso
import java.net.URL

class PostAdapter(
    var updates: ArrayList<PlantUpdate>,
    context: Context
    ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
        private var context: Context? = context

        fun PostAdapter(
            context: Context?
        ) {
            this.context = context
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textViewHeader: TextView = itemView.findViewById(R.id.updateText)
            var image: ImageView = itemView.findViewById(R.id.plantImage)
            var userNameTextView: TextView = itemView.findViewById(R.id.friendName)


        }


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_post, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.textViewHeader.text = updates[position].note

            Picasso.get().load(updates[position].image).into(holder.image);
            holder.image.scaleType = ImageView.ScaleType.CENTER_CROP;
            holder.userNameTextView.text = updates[position].creatorName + ": "



        }
        override fun getItemCount(): Int {
            return updates.count()
        }

    }

