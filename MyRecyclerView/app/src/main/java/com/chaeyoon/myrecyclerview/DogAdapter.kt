package com.chaeyoon.myrecyclerview

import android.content.Context
import android.system.Os.bind
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.RecyclerView

class DogAdapter(private val context: Context, val itemClick:(Dog)->Unit, val itemLongClick:(Dog)->Unit) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {
    private var dogList = listOf<Dog>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_dog, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dogList[position], context)
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    fun setDogs(dogs: List<Dog>) {
        dogList = dogs
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dogPhoto = view.findViewById<ImageView>(R.id.dogPhotoImg)
        val dogBreed = view.findViewById<TextView>(R.id.dogBreedTv)
        val dogAge = view.findViewById<TextView>(R.id.dogAgeTv)
        val dogGender = view.findViewById<TextView>(R.id.dogGenderTv)

        fun bind(dog: Dog, context: Context) {
            if (dog.photo != "") {
                val resourceId = context.resources.getIdentifier(dog.photo, "drawable", context.packageName)
                dogPhoto?.setImageResource(resourceId)
            } else {
                dogPhoto?.setImageResource(R.mipmap.ic_launcher)
            }

            dogBreed.text = dog.breed
            dogAge.text = dog.age.toString()
            dogGender.text = dog.gender

            itemView.setOnClickListener {
                itemClick(dog)
            }
            itemView.setOnLongClickListener {
                itemLongClick(dog)
                true
            }
        }
    }

}