package com.chaeyoon.roomcomponent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaeyoon.roomcomponent.db.CatDB
import com.chaeyoon.roomcomponent.entity.Cat
import com.chaeyoon.roomcomponent.ui.CatRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var catDb : CatDB? = null
    private var catList = listOf<Cat>()
    private lateinit var mAdapter:CatRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        catDb = CatDB.getInstance(this)

        mAdapter = CatRecyclerViewAdapter(this, catList)

        val r = Runnable {
            try {
                catList = catDb?.catDao()?.getAll()!!
                mAdapter = CatRecyclerViewAdapter(this, catList)
                mAdapter.notifyDataSetChanged()

                mRecyclerView.adapter = mAdapter
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.setHasFixedSize(true)
            } catch (e: Exception) {
                Log.d("tag", "Error - $e")
            }
        }

        val thread = Thread(r)
        thread.start()

        mAddBtn.setOnClickListener {
            val i = Intent(this, AddActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}