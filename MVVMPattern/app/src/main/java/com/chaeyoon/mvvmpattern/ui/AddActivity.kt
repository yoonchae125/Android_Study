package com.chaeyoon.mvvmpattern.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.chaeyoon.mvvmpattern.R
import com.chaeyoon.mvvmpattern.entity.Contact
import com.chaeyoon.mvvmpattern.viewModel.ContactViewModel
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), ViewModelStoreOwner{
    private lateinit var contactViewModel: ContactViewModel
    private var id: Int? = null
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        if(intent!=null&& intent.hasExtra(EXTRA_CONTACT_NAME) && intent.hasExtra(EXTRA_CONTACT_NUMBER)
                && intent.hasExtra(EXTRA_CONTACT_ID)){
            add_edittext_name.setText(intent.getStringExtra(EXTRA_CONTACT_NAME))
            add_edittext_number.setText(intent.getStringExtra(EXTRA_CONTACT_NUMBER))
            id = intent.getIntExtra(EXTRA_CONTACT_ID, -1)
        }
        add_button.setOnClickListener {
            val name = add_edittext_name.text.toString().trim()
            val number = add_edittext_number.text.toString()

            if (name.isEmpty() || number.isEmpty()) {
                Toast.makeText(this, "Please enter name and number.", Toast.LENGTH_SHORT).show()
            } else {
                val initial = name[0].toUpperCase()
                val contact = Contact(id, name, number, initial.toString())
                contactViewModel.insert(contact)
                finish()
            }
        }
    }
    companion object {
        const val EXTRA_CONTACT_NAME = "EXTRA_CONTACT_NAME"
        const val EXTRA_CONTACT_NUMBER = "EXTRA_CONTACT_NUMBER"
        const val EXTRA_CONTACT_ID = "EXTRA_CONTACT_ID"
    }
}