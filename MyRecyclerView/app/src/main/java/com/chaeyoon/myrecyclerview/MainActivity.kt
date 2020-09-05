package com.chaeyoon.myrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var dogAdapter: DogAdapter
    private lateinit var dogList:List<Dog>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dogList = listOf(
            Dog("Chow Chow", "Male", 4, "dog"),
            Dog("Breed Pomeranian", "Female", 1, "dog"),
            Dog("Golden Retriver", "Female", 3, "dog"),
            Dog("Yorkshire Terrier", "Male", 5, "dog"),
            Dog("Pug", "Male", 4, "dog"),
            Dog("Alaskan Malamute", "Male", 7, "dog"),
            Dog("Shih Tzu", "Female", 5, "dog")
        )
        dogAdapter = DogAdapter(this,
            {dog->
                Toast.makeText(this, "개의 품종은 ${dog.breed} 이며, 나이는 ${dog.age}세이다.", Toast.LENGTH_SHORT).show()
            },
            {
                deleteDialog(it)
            })
        dogRecyclerView.adapter = dogAdapter

        val lm = LinearLayoutManager(this)
        dogRecyclerView.layoutManager = lm
        dogRecyclerView.setHasFixedSize(true)

        dogAdapter.setDogs(dogList)

    }
    fun deleteDialog(dog:Dog){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                Toast.makeText(this, "deleted", Toast.LENGTH_LONG).show()
            }
        builder.show()
    }
}