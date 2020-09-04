package com.chaeyoon.mvvmpattern.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.chaeyoon.mvvmpattern.db.ContactDB
import com.chaeyoon.mvvmpattern.entity.Contact

class ContactRepository(application: Application) {
    private val contactDB = ContactDB.getInstance(application)
    private val contactDao = contactDB!!.getContactDao()
    private val contacts = contactDao.getAll()

    fun getAll():LiveData<List<Contact>>{
        return contacts
    }

    fun insert(contact: Contact){
        try {
            val thread = Thread(Runnable {
                contactDao.insert(contact) })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun delete(contact: Contact){
        try {
            val thread = Thread(Runnable {
                contactDao.delete(contact)
            })
            thread.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}