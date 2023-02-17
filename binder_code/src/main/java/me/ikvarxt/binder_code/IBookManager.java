package me.ikvarxt.binder_code;

import java.util.List;

public interface IBookManager extends android.os.IInterface {
    /**
     * Default implementation for IBookManager.
     */
     class Default implements IBookManager {
        @Override
        public void addBook(Book book) throws android.os.RemoteException {
        }

        @Override
        public java.util.List<Book> getBookList() throws android.os.RemoteException {
            return null;
        }

        @Override
        public android.os.IBinder asBinder() {
            return null;
        }
    }

    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements IBookManager {
        private static final java.lang.String DESCRIPTOR = "me.ikvarxt.binder_code.IBookManager";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IBookManager interface,
         * generating a proxy if needed.
         */
        public static IBookManager asInterface(android.os.IBinder remoteBinder) {
            if ((remoteBinder == null)) {
                return null;
            }
            // 在当前进程找 binder 对象？
            android.os.IInterface iin = remoteBinder.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IBookManager) {
                return ((IBookManager) iin);
            }
            // 没找到返回代理对象
            return new IBookManager.Stub.Proxy(remoteBinder);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            java.lang.String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(descriptor);
                    Book _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_getBookList: {
                    data.enforceInterface(descriptor);
                    java.util.List<Book> _result = this.getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        private static class Proxy implements IBookManager {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void addBook(Book book) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book != null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        getDefaultImpl().addBook(book);
                        return;
                    }
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public java.util.List<Book> getBookList() throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                java.util.List<Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    boolean _status = mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                    if (!_status && getDefaultImpl() != null) {
                        return getDefaultImpl().getBookList();
                    }
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            public static IBookManager sDefaultImpl;
        }

        static final int TRANSACTION_addBook = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_getBookList = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);

        public static boolean setDefaultImpl(IBookManager impl) {
            // Only one user of this interface can use this function
            // at a time. This is a heuristic to detect if two different
            // users in the same process use this function.
            if (Stub.Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Stub.Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBookManager getDefaultImpl() {
            return Stub.Proxy.sDefaultImpl;
        }
    }

    public void addBook(Book book) throws android.os.RemoteException;

    public List<Book> getBookList() throws android.os.RemoteException;
}
