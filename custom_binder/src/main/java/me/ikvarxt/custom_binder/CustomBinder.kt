package me.ikvarxt.custom_binder

import android.os.Binder
import android.os.IBinder
import android.os.IInterface
import android.os.Parcel
import android.os.Parcelable
import android.os.RemoteException


/**
 * @author ikvarxt
 */
data class Book(
    val id: Int,
    val name: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            // TODO: 2/23/23 为什么创建数组
            return arrayOfNulls(size)
        }
    }
}

interface IBookManager : IInterface {

    fun addBook(book: Book)

    fun getBookList(): List<Book>
}

/**
 * used to real implementation
 */
abstract class Stub : Binder(), IBookManager {

    companion object {

        private const val DESCRIPTOR = "me.ikvarxt.custom_binder.Stub"

        private const val TRANSACTION_addBook = FIRST_CALL_TRANSACTION + 1
        private const val TRANSACTION_getBookList = FIRST_CALL_TRANSACTION + 2

        fun asInterface(remote: IBinder?): IBookManager? {
            if (remote == null) return null
            val localBinder = remote.queryLocalInterface(DESCRIPTOR)
            if (localBinder != null) {
                return localBinder as IBookManager
            }
            return Proxy(remote)
        }
    }

    init {
        attachInterface(this, DESCRIPTOR)
    }

    class Proxy(
        private val mRemote: IBinder
    ) : IBookManager {

        @Throws(RemoteException::class)
        override fun addBook(book: Book) {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()

            try {
                data.writeInterfaceToken(DESCRIPTOR)
                data.writeInt(0)
                book.writeToParcel(data, 0)
                val status = mRemote.transact(TRANSACTION_addBook, data, reply, 0)

                reply.readException()
            } finally {
                data.recycle()
                reply.recycle()
            }
        }

        @Throws(RemoteException::class)
        override fun getBookList(): List<Book> {
            val data = Parcel.obtain()
            val reply = Parcel.obtain()
            val result: List<Book>?
            try {
                data.writeInterfaceToken(DESCRIPTOR)

                val status = mRemote.transact(TRANSACTION_addBook, data, reply, 0)
                reply.readException()
                result = reply.createTypedArrayList(Book.CREATOR)
            } finally {
                data.recycle()
                reply.recycle()
            }
            return result ?: listOf()
        }

        override fun asBinder(): IBinder = mRemote
    }
}

class ServiceBinder : Stub() {

    override fun asBinder(): IBinder {
        TODO("Not yet implemented")
    }

    override fun addBook(book: Book) {
        TODO("Not yet implemented")
    }

    override fun getBookList(): List<Book> {
        TODO("Not yet implemented")
    }
}