package com.ghanshyam.locationtracklib;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Ghanshyam.
 */
public class Data implements Parcelable {
    String channelId;
    String channelName;
    int notificationId;
    int smallIcon;
    boolean isAlertOnce;
    boolean isOnGoing;
    int priority;
    String category;
    int visibility;
    int number;

    private long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    private int DISPLACEMENT = 0;

    public Data() {

    }

    private Data(Parcel in) {
        channelId = in.readString();
        channelName = in.readString();
        notificationId = in.readInt();

        smallIcon = in.readInt();
//        style = in.read();
        isAlertOnce = in.readBoolean();
        isOnGoing = in.readBoolean();
        priority = in.readInt();
        category = in.readString();
        visibility = in.readInt();
        number = in.readInt();

        UPDATE_INTERVAL_IN_MILLISECONDS = in.readLong();
        FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = in.readLong();
        DISPLACEMENT = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(channelId);
        out.writeString(channelName);
        out.writeInt(notificationId);
        out.writeInt(smallIcon);
        out.writeBoolean(isAlertOnce);
        out.writeBoolean(isOnGoing);
        out.writeInt(priority);
        out.writeString(category);
        out.writeInt(visibility);
        out.writeInt(number);

        out.writeLong(UPDATE_INTERVAL_IN_MILLISECONDS);
        out.writeLong(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        out.writeLong(DISPLACEMENT);
    }

    public static final Parcelable.Creator<Data> CREATOR
            = new Parcelable.Creator<Data>() {
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public boolean isAlertOnce() {
        return isAlertOnce;
    }

    public void setAlertOnce(boolean alertOnce) {
        isAlertOnce = alertOnce;
    }

    public boolean isOnGoing() {
        return isOnGoing;
    }

    public void setOnGoing(boolean onGoing) {
        isOnGoing = onGoing;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getUPDATE_INTERVAL_IN_MILLISECONDS() {
        return UPDATE_INTERVAL_IN_MILLISECONDS;
    }

    public void setUPDATE_INTERVAL_IN_MILLISECONDS(long UPDATE_INTERVAL_IN_MILLISECONDS) {
        this.UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS;
    }

    public long getFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS() {
        return FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
    }

    public void setFASTEST_UPDATE_INTERVAL_IN_MILLISECONDS(long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS) {
        this.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
    }

    public int getDISPLACEMENT() {
        return DISPLACEMENT;
    }

    public void setDISPLACEMENT(int DISPLACEMENT) {
        this.DISPLACEMENT = DISPLACEMENT;
    }
}
