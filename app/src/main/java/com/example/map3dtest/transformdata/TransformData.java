package com.example.map3dtest.transformdata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 朱宏博 on 2018/7/7.
 */

public class TransformData implements Parcelable{
    private String ID = "";
    private String numberPlate = "";
    private String prjName = "";//该参数在provinceOrName为true时代表province，在provinceOrName为false时代表project name
    private boolean provinceOrName = false;//true代表传递的参数为province，false代表传递的参数为project name

    public TransformData(String prjName) {

        this.prjName = prjName;
    }

    public TransformData(String ID, String numberPlate, String prjName) {
        this.ID = ID;
        this.numberPlate = numberPlate;
        this.prjName = prjName;
    }

    public TransformData(String ID,boolean provinceOrName) {
        this.ID = ID;
        this.provinceOrName = provinceOrName;
    }

    public TransformData(String ID, String prjName, boolean provinceOrName) {
        this.ID = ID;
        this.prjName = prjName;
        this.provinceOrName = provinceOrName;
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

    public boolean isProvinceOrName() {
        return provinceOrName;
    }

    public void setProvinceOrName(boolean provinceOrName) {
        this.provinceOrName = provinceOrName;
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
        out.writeByte((byte)(provinceOrName ? 1 : 0));
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
        provinceOrName = in.readByte() != 0;
    }


}
