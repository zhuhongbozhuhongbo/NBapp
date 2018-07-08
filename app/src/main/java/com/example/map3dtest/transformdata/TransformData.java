package com.example.map3dtest.transformdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 朱宏博 on 2018/7/7.
 */

public class TransformData implements Parcelable{
    private String ID;
    private String numberPlate;
    private String prjName;

    public TransformData(String ID, String numberPlate, String prjName) {
        this.ID = ID;
        this.numberPlate = numberPlate;
        this.prjName = prjName;
    }

    public String getPrjName() {
        return prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        out.writeString(ID);
        out.writeString(numberPlate);
        out.writeString(prjName);
    }

    public static final Creator<TransformData> CREATOR = new Creator<TransformData>(){
        @Override
        public TransformData[] newArray(int size){
            return new TransformData[size];
        }
        @Override
        public TransformData createFromParcel(Parcel in){
            return new TransformData(in);
        }
    };

    public TransformData(Parcel in){
        ID = in.readString();
        numberPlate = in.readString();
        prjName = in.readString();
    }


}
