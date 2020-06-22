package com.packager.user.controller;


import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.packager.user.Entities.Package;
import com.packager.user.repository.PackageRepository;

import java.util.List;

public class HistoryPackageViewModel extends AndroidViewModel {
    PackageRepository packageRepository;
    LiveData<List<Package>> livePackages;

    public HistoryPackageViewModel(@NonNull Application application){
        super(application);
        packageRepository = new PackageRepository(application);
        livePackages = packageRepository.getAllPackages();


    }

    public LiveData<List<Package>> getAllPackages(){
        return livePackages;
    }
}
