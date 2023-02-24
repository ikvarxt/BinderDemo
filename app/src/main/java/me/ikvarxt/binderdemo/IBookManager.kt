package me.ikvarxt.binderdemo

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel


/**
 * @author ikvarxt
 */
interface IBookManager : IInterface {

    fun addBook(book: Book)

    fun getBookList(): List<Book>?

    abstract class Stub : Binder(), IBookManager {

        companion object {
            const val DESC = "me.ikvarxt.binderdemo.IBookManager"

            const val TRANS_addBook = FIRST_CALL_TRANSACTION + 1
            const val TRANS_getBookList = FIRST_CALL_TRANSACTION + 2

            fun asInterface(remote: IBinder): IBookManager {
                val iin = remote.queryLocalInterface(DESC)
                if (iin != null && iin is IBookManager) {
                    return iin
                }
                return Proxy(remote)
            }
        }

        init {
            attachInterface(this, DESC)
        }

        override fun asBinder(): IBinder = this

        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            val descriptor = DESC
            when (code) {
                INTERFACE_TRANSACTION -> {
                    reply?.writeString(descriptor)
                    return true
                }

                TRANS_addBook -> {
                    data.enforceInterface(descriptor)
                    val book = Book.createFromParcel(data)
                    this.addBook(book)
                    reply?.writeNoException()
                    return true
                }

                TRANS_getBookList -> {
                    data.enforceInterface(descriptor)
                    val result = this.getBookList()
                    reply?.writeNoException()
                    reply?.writeTypedList(result)
                    return true
                }

                else -> return super.onTransact(code, data, reply, flags)
            }
        }

        class Proxy(private val mRemote: IBinder) : IBookManager {

            override fun addBook(book: Book) {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()

                try {
                    data.writeInterfaceToken(DESC)
                    book.writeToParcel(data, 0)
                    val status = mRemote.transact(TRANS_addBook, data, reply, 0)
                    reply.readException()
                } finally {
                    data.recycle()
                    reply.recycle()
                }
            }

            override fun getBookList(): List<Book>? {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()
                val result: List<Book>?
                try {
                    data.writeInterfaceToken(DESC)
                    val status = mRemote.transact(TRANS_getBookList, data, reply, 0)

                    reply.readException()
                    result = reply.createTypedArrayList(Book.CREATOR)
                } finally {
                    data.recycle()
                    reply.recycle()
                }
                return result
            }

            override fun asBinder(): IBinder = mRemote
        }
    }
}