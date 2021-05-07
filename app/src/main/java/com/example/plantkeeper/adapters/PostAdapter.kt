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
import java.net.URL

class PostAdapter(
    var plants: ArrayList<Plant>,
    context: Context
    ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

        // Context object used to inflate list_item layout
        //private var listItems: List<Post> = posts
        private var context: Context? = context

        // Generated constructor from members
        fun PostAdapter(
            //listItems: List<Post>,
            context: Context?
        ) {
            //this.listItems = listItems
            this.context = context
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var textViewHeader: TextView = itemView.findViewById(R.id.updateText)
            var image: ImageView = itemView.findViewById(R.id.plantImage)


        }


        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_post, parent, false)
            return ViewHolder(v)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //holder.textViewHeader.text = position.toString()
            holder.textViewHeader.text = plants[0].name

            val url = URL(plants[0].image)

            val bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream())
            holder.image.setImageBitmap(bmp)
            holder.image.scaleType = ImageView.ScaleType.CENTER_CROP;
            /*
            holder.textViewHeader.text = posts[position].text
            holder.image.setImageBitmap(posts[position].image)
            holder.image.scaleType = ImageView.ScaleType.CENTER_CROP;
             */



        }
        override fun getItemCount(): Int {
            //var count = posts.size
            var count = 1
            if (plants.size < 1) {
                count = plants.size
            } else {
                count = 22
            }
            return count
        }

    }



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