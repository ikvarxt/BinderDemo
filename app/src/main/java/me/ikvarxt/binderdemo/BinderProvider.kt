package me.ikvarxt.binderdemo

import android.os.IBinder
import android.os.IInterface
import android.util.Log


/**
 * @author ikvarxt
 */
interface BinderProvider<T : IInterface> {

    fun asInterface(binder: IBinder): T

    fun getRealBinder(): IBinder
}

object BinderFactory : BinderProvider<IBookManager> {

    private var provider: BinderProvider<IBookManager> = AidlBinderImpl()

    fun setProvider(provider: BinderProvider<IBookManager>) {
        this.provider = provider
    }

    override fun asInterface(binder: IBinder): IBookManager {
        return provider.asInterface(binder)
    }

    override fun getRealBinder(): IBinder {
        return provider.getRealBinder()
    }
}

class AidlBinderImpl : BinderProvider<IBookManager> {

    private val list = mutableListOf<Book>()

    override fun asInterface(binder: IBinder): IBookManager {
        return IBookManager.Stub.asInterface(binder)
    }

    override fun getRealBinder(): IBinder {

        return object : IBookManager.Stub() {

            override fun addBook(book: Book) {
                list.add(book)
                Log.d(BookService.TAG, "addBook: $book")
                Log.d(BookService.TAG, "books: \n${list.joinToString("\n")}")
            }

            override fun getBookList(): List<Book> {
                return list.toList()
            }
        }
    }
}
