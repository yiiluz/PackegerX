package com.packager.user.Entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.packager.user.model.Converters;

import java.util.ArrayList;
import java.util.Date;

@Entity(tableName = "packages_table")
public class Package {
    @ColumnInfo(name = "type")
    @TypeConverters({Converters.class})
    private Configuration.PackageType packageType;
    @ColumnInfo(name = "fragile")
    private boolean isFragile;
    @ColumnInfo(name = "weight")
    @TypeConverters({Converters.class})
    private Configuration.PackageWeight packageWeight;
    @PrimaryKey
    @NonNull
    private String packageID;
    @ColumnInfo(name = "addressee_phone")
    private String phoneAddressee;
    @ColumnInfo(name = "picker")
    private String picker = null;
    @ColumnInfo(name = "sender_phone")
    private String phoneSender;
    @ColumnInfo(name = "status")
    @TypeConverters({Converters.class})
    private Configuration.Status status = Configuration.Status.RECEIVED;
    @ColumnInfo(name = "address")
    @TypeConverters({Converters.class})
    private Address currentAddress;

    @ColumnInfo(name = "friends")
    @TypeConverters({Converters.class})
    private ArrayList<String> friendsPhones = new ArrayList<>();
    @ColumnInfo(name = "date")
    @TypeConverters({Converters.class})
    private Date date = new Date();

    public Package() {
    }

    public Package(Configuration.PackageType packageType, boolean isFragile,
                   Configuration.PackageWeight packageWeight,
                   String packageID, String phoneAddressee, String phoneSender, Address currentAddress) {
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.packageWeight = packageWeight;
        this.packageID = packageID;
        this.phoneAddressee = phoneAddressee;
        this.phoneSender = phoneSender;
        this.currentAddress = currentAddress;
        this.status = Configuration.Status.REGISTERED;
    }

    public Package(Configuration.PackageType packageType, boolean isFragile,
                   Configuration.PackageWeight packageWeight, String packageID,
                   String phoneAddressee, String picker, String phoneSender, Configuration.Status status,
                   Address currentAddress) {
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.packageWeight = packageWeight;
        this.packageID = packageID;
        this.phoneAddressee = phoneAddressee;
        this.picker = picker;
        this.phoneSender = phoneSender;
        this.status = status;
        this.currentAddress = currentAddress;
    }

    public Configuration.PackageType getPackageType() {
        return packageType;
    }

    public void setPackageType(Configuration.PackageType packageType) {
        this.packageType = packageType;
    }

    public boolean isFragile() {
        return isFragile;
    }

    public void setFragile(boolean fragile) {
        isFragile = fragile;
    }

    public Configuration.PackageWeight getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Configuration.PackageWeight packageWeight) {
        this.packageWeight = packageWeight;
    }

    public String getPackageID() {
        return packageID;
    }

    public void setPackageID(String packageID) {
        this.packageID = packageID;
    }

    public String getPhoneAddressee() {
        return phoneAddressee;
    }

    public void setPhoneAddressee(String phoneAddressee) {
        this.phoneAddressee = phoneAddressee;
    }

    public String getPhoneSender() {
        return phoneSender;
    }

    public void setPhoneSender(String phoneSender) {
        this.phoneSender = phoneSender;
    }

    public Address getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(Address currentAddress) {
        this.currentAddress = currentAddress;
    }

    public Configuration.Status getStatus() {
        return status;
    }

    public void setStatus(Configuration.Status status) {
        this.status = status;
    }

    public String getPicker() {
        return picker;
    }

    public void setPicker(String picker) {
        this.picker = picker;
    }

    public ArrayList<String> getFriendsPhones() {
        return this.friendsPhones;
    }

    public void setFriendsPhones(ArrayList<String> friendsPhones) {
        this.friendsPhones = friendsPhones;
    }

    public void addFriend(String friendsPhone) {
        friendsPhones.add(friendsPhone);
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
