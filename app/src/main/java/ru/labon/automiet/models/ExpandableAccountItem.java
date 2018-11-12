package ru.labon.automiet.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 05.12.2017.
 */

public class ExpandableAccountItem implements Parcelable {

    private String title;
    private String value;

    public ExpandableAccountItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public static final Creator<ExpandableAccountItem> CREATOR = new Creator<ExpandableAccountItem>() {
        @Override
        public ExpandableAccountItem createFromParcel(Parcel in) {
            return new ExpandableAccountItem(in);
        }

        @Override
        public ExpandableAccountItem[] newArray(int size) {
            return new ExpandableAccountItem[size];
        }
    };

    protected ExpandableAccountItem(Parcel in) {
        title = in.readString();
        value = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(value);
    }
}
