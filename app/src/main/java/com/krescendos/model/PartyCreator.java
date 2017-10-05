package com.krescendos.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PartyCreator implements Parcelable.Creator<Party> {
    @Override
    public Party createFromParcel(Parcel parcel) {
        return new Party(parcel);
    }

    @Override
    public Party[] newArray(int size) {
        return new Party[size];
    }
}
