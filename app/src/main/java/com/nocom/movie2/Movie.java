package com.nocom.movie2;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Moha on 9/13/2017.
 */

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private static Bitmap bitmap_transfer;
    //    public int poster;
    private int poster1;
    private String overview;
    private String release_date;
    private double voteaverge ;
    private String title;
    private String poster2 ;
    private int id ;
    private String key ;


    public Movie(String nposter2, String noverview, String nrelease_date, double nvote_averge, String ntitle, int nid) {
        poster2=nposter2;
        overview=noverview;
        release_date=nrelease_date;
        voteaverge=nvote_averge;
        title=ntitle;
        id=nid;
        //      key=nkey;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(poster1);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeDouble(voteaverge);
        dest.writeString(title);
        dest.writeString(poster2);
        dest.writeInt(id);
        dest.writeString(key);
    }


    private Movie(Parcel in) {
        poster1 = in.readInt();
        overview = in.readString();
        release_date = in.readString();
        voteaverge = in.readDouble();
        title = in.readString();
        poster2 = in.readString();
        id = in.readInt();
        key=in.readString();
    }

    public String gettitle(){

        return title ;
    }


    public String getPoster2() {
        return poster2;
    }

    public int getId() {
        return id;
    }

    public String getreleasedate() {
        return release_date;

    }

    public String getKey() {
        return key;
    }

    public double getvoteaverge() {
        return voteaverge;
    }


    public String getOverview() {
        return overview;
    }


    @Override
    public int describeContents() {
        return 0;
    }

}