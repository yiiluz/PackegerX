package com.packager.user.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.packager.user.Entities.Package;

@Database(entities = {Package.class}, version = 1)
public abstract class PackagesDataBase extends RoomDatabase {
    private static PackagesDataBase instance;
    public abstract PackageDao PackageDao();

    public static synchronized PackagesDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), PackagesDataBase.class,
                    "package_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
