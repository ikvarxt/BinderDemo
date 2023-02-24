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

    /**
     * used to create real logic in remote service
     */
    abstract class Stub : Binder(), IBookManager {

        companion object {
            const val DESC = "me.ikvarxt.binderdemo.IBookManager"

            /**
             * IPC function code, Binder IPC uses code to recognize which function been called
             */
            const val TRANS_addBook = FIRST_CALL_TRANSACTION + 1
            const val TRANS_getBookList = FIRST_CALL_TRANSACTION + 2

            /**
             * client used this function to get a IBookManager interface,
             * across process connection will be exec by Proxy
             */
            fun asInterface(remote: IBinder): IBookManager {
                val iin = remote.queryLocalInterface(DESC)
                if (iin != null && iin is IBookManager) {
                    return iin
                }
                return Proxy(remote)
            }
        }

        init {
            // TODO: 2/24/23 currently workaround of AS warning
            kotlin.run {
                attachInterface(this, DESC)
            }
        }

        override fun asBinder(): IBinder = this

        /**
         * remote service implement a binder inherited by Stub class,
         * so this function is the IPC core of transfer data between
         * process
         */
        override fun onTransact(code: Int, data: Parcel, reply: Parcel?, flags: Int): Boolean {
            val descriptor = DESC
            when (code) {
                INTERFACE_TRANSACTION -> {
                    // TODO: 2/24/23 why reply write descriptor
                    reply?.writeString(descriptor)
                    return true
                }

                TRANS_addBook -> {
                    data.enforceInterface(descriptor)
                    val book = Book.createFromParcel(data)
                    // call remote service real function
                    this.addBook(book)
                    reply?.writeNoException()
                    return true
                }

                TRANS_getBookList -> {
                    data.enforceInterface(descriptor)
                    val result = this.getBookList()
                    reply?.writeNoException()
                    // TODO: 2/24/23 must called after writeNoException, WHY?
                    reply?.writeTypedList(result)
                    return true
                }

                else -> return super.onTransact(code, data, reply, flags)
            }
        }

        /**
         * used for client-end to access remote service's protocol
         */
        class Proxy(private val mRemote: IBinder) : IBookManager {

            override fun addBook(book: Book) {
                val data = Parcel.obtain()
                val reply = Parcel.obtain()

                try {
                    data.writeInterfaceToken(DESC)
                    book.writeToParcel(data, 0)
                    // call remoteBinder#transact, this method called onTransact inner
                    mRemote.transact(TRANS_addBook, data, reply, 0)
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
                    mRemote.transact(TRANS_getBookList, data, reply, 0)

                    reply.readException()
                    // read remote data from reply Parcel instance,
                    //  that's why we need to add a CREATOR object
                    result = reply.createTypedArrayList(Book.CREATOR)
                } finally {
                    data.recycle()
                    reply.recycle()
                }
                return result
            }

            /**
             * this method from IInterface, which could identify
             * which binder is remote binder, separated by proxy binder
             */
            override fun asBinder(): IBinder = mRemote
        }
    }
}