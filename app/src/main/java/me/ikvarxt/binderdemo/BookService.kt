package me.ikvarxt.binderdemo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class BookService : Service() {

    private val list = mutableListOf<Book>()

    private val binder = object : IBookManager.Stub() {

        override fun addBook(book: Book) {
            list.add(book)
            Log.d(TAG, "addBook: $book")
            Log.d(TAG, "books: \n${list.joinToString("\n")}")
        }

        override fun getBookList(): List<Book> {
            return list.toList()
        }
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onCreate() {
        super.onCreate()

    }

    companion object {
        const val TAG = "BookService"
    }
}