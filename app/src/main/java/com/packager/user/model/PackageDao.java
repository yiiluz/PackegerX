package com.packager.user.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.packager.user.Entities.Configuration;
import com.packager.user.Entities.Package;

import java.util.List;

@Dao
public interface PackageDao {
    @Insert
    void insert(Package p);

    @Update
    void update(Package p);

    @Delete
    void delete(Package p);

    @Query("SELECT * FROM packages_table")
    LiveData<List<Package>> getAll();

    @Query("SELECT * FROM packages_table WHERE sender_phone LIKE :senderPhone LIMIT 1")
    Package findBySenderPhone(String senderPhone);

    @Query("SELECT * FROM packages_table WHERE sender_phone LIKE :addresseePhone LIMIT 1")
    Package findByAddresseePhone(String addresseePhone);
//
//    @Query("SELECT * FROM packages_table WHERE sender_phone LIKE :addresseePhone AND status LIKE :s LIMIT 1")
//    Package findByAddresseeAndStatus(String addresseePhone, Configuration.Status s);

//    @TypeConverters({Converters.class})
//    @Query("SELECT * FROM packages_table WHERE addressee_phone LIKE :addresseePhone AND status LIKE :s LIMIT 1")
//    Package findBySenderAndStatus(String addresseePhone, Configuration.Status s);

    @Query("DELETE FROM packages_table")
    void deleteAllPackages();

    @Insert
    void insertAll(Package... packages);
}
