package cl.usm.telematica.sigamobile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pipos on 07-10-2016.
 */
public class User implements Parcelable{
    private int _id;
    private String _username;
    private String _pass;
    private String _server;
    private boolean _isSelected = false;

    public User(){}

    public User(Parcel parcel){
        _id = parcel.readInt();
        _username = parcel.readString();
        _pass = parcel.readString();
        _server = parcel.readString();
    }

    public User(int id, String username,String pass, String server){
        _id = id;
        _username = username;
        _pass = pass;
        _server = server;
    }
    public User(String username,String pass, String server){
        _username = username;
        _pass = pass;
        _server = server;
    }
    public int getId(){
        return _id;
    }
    public void setId(int id){
        _id = id;
    }
    public String getUsername(){
        return _username;
    }
    public void setUsername(String username){
        _username = username;
    }
    public String getPass(){
        return _pass;
    }
    public void setPass(String pass){
        _pass = pass;
    }
    public void setServer(String server ){ _server = server; }
    public String getServer(){ return _server; }
    public boolean isSelected() { return _isSelected; }
    public void setSelected(boolean selected) { _isSelected = selected; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getUsername());
        dest.writeString(getPass());
        dest.writeString(getServer());
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
