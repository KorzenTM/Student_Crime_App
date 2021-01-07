package pl.edu.uwr.pum.studentcrimeapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

public class Crime implements Parcelable
{

    private int id;
    private String title;
    private Date date;
    private boolean solved;

    public Crime(){
        date = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {

    }
}
