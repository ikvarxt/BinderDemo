package me.ikvarxt.binder_code;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


/**
 * @author ikvarxt
 */
class Book implements Parcelable {

    private final int id;
    private final String name;

    public Book(Parcel parcel) {
        this.id = parcel.readInt();
        this.name = parcel.readString();
    }

    public Book(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[0];
        }
    };
}