package cl.usm.telematica.sigamobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pipos on 05-12-2016.
 */

public class Event implements Parcelable {
    private int year;
    private String month;
    private String date;
    private String type;
    private String description;

    public Event(){}
    public Event(int year,String month,String date,String type,String description){
        this.year = year;
        this.month = month;
        this.date = date;
        this.type = type;
        this.description = description;
    }
    public Event(Parcel in){

        year = in.readInt();
        month = in.readString();
        date = in.readString();
        type = in.readString();
        description = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(year);
        parcel.writeString(month);
        parcel.writeString(date);
        parcel.writeString(type);
        parcel.writeString(description);

    }
}
