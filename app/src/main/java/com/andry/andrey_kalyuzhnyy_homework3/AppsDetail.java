package com.andry.andrey_kalyuzhnyy_homework3;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by andry on 16.04.2016.
 */
public class AppsDetail implements Parcelable{
    private String label;
    private String name;
    private Drawable icon;
    private boolean isOnMainScreen;

    public AppsDetail() {

    }

    public AppsDetail(String label, String name, Drawable icon, boolean isOnMainScreen) {
        this.label = label;
        this.name = name;
        this.icon = icon;
        this.isOnMainScreen = isOnMainScreen;
    }

    public AppsDetail(Parcel in) {
        this.label = in.readString();
        this.name = in.readString();
        Bitmap bitmap = in.readParcelable(getClass().getClassLoader());
        this.icon = new BitmapDrawable(bitmap);
        this.isOnMainScreen = in.readByte() == 1;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isOnMainScreen() {
        return isOnMainScreen;
    }

    public void setOnMainScreen(boolean onMainScreen) {
        isOnMainScreen = onMainScreen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(label);
        out.writeString(name);
        Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
        out.writeParcelable(bitmap, flags);
        out.writeByte((byte) (isOnMainScreen() ? 1 : 0));
    }

    public static Creator<AppsDetail> CREATOR = new Creator<AppsDetail>() {
        @Override
        public AppsDetail createFromParcel(Parcel source) {
            return new AppsDetail(source);
        }

        @Override
        public AppsDetail[] newArray(int size) {
            return new AppsDetail[size];
        }
    };
}
