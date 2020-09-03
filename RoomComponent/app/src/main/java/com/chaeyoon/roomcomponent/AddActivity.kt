package com.chaeyoon.roomcomponent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chaeyoon.roomcomponent.db.CatDB
import com.chaeyoon.roomcomponent.entity.Cat
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {
    private var catDb : CatDB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        catDb = CatDB.getInstance(this)

        /* 새로운 cat 객체를 생성, id 이외의 값을 지정 후 DB에 추가 */
        val addRunnable = Runnable {
            val newCat = Cat(
                0,
                addName.text.toString(),
                addLifeSpan.text.toString().toInt(),
                addOrigin.text.toString(),

            )
        }

        addBtn.setOnClickListener {
            val addThread = Thread(addRunnable)
            addThread.start()

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}