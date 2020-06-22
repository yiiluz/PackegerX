package com.packager.user.Utils;

import androidx.room.TypeConverter;

import com.packager.user.Entities.Address;
import com.packager.user.Utils.Configuration;

import java.util.ArrayList;
import java.util.Date;

public class Converters {
    @TypeConverter
    public static Configuration.PackageType fromString(String type) {
        return Configuration.PackageType.valueOf(type);
    }

    @TypeConverter
    public static String fromEnum(Configuration.PackageType type) {
        return type.toString();
    }

    @TypeConverter
    public static Configuration.PackageWeight fromStringWeight(String weight) {
        return Configuration.PackageWeight.valueOf(weight);
    }

    @TypeConverter
    public static String fromEnumWeight(Configuration.PackageWeight weight) {
        return weight.toString();
    }

    @TypeConverter
    public static Configuration.Status fromStringStatus(String status) {
        return Configuration.Status.valueOf(status);
    }

    @TypeConverter
    public static String fromEnumStatus(Configuration.Status status) {
        return status.toString();
    }

    @TypeConverter
    public static Address fromStringAddress(String add) {
        String[] address = add.split("@");
        return new Address(Double.parseDouble(address[0]), Double.parseDouble(address[1]), address[2]);
    }

    @TypeConverter
    public static String fromEnumStatus(Address add) {
        return add.getLat() + "@" + add.getLongt() + "@" + add.getAddress();
    }

    @TypeConverter
    public static ArrayList<String> fromStringList(String string) {
        String[] listOfString = string.split("@");
        ArrayList<String> temp = new ArrayList<>();
        for (String s : listOfString) {
            temp.add(s);
        }
        return temp;
    }

    @TypeConverter
    public static String fromEnumArrayList(ArrayList<String> list) {
        String temp = "";
        for (String s : list) {
            temp = temp + s + "@";
        }
        return temp.substring(0, temp.length() - 1);
    }

    @TypeConverter
    public static Date fromStringDate(String string) {
        return new Date(string);
    }

    @TypeConverter
    public static String fromEnumArrayList(Date d) {
        return d.toString();
    }

}