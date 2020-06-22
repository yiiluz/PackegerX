package com.packager.user.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.packager.user.Entities.Package;

import java.util.List;

public class PackageViewModel extends AndroidViewModel {
    private PackageRepository packageRepository;
    private LiveData<List<Package>> allPackages;

    public PackageViewModel(@NonNull Application application) {
        super(application);
        packageRepository = new PackageRepository(application);
        allPackages = packageRepository.getAllPackages();
    }

    public void insert(Package p) {
        packageRepository.insert(p);
    }

    public void update(Package p) {
        packageRepository.update(p);
    }

    public void delete(Package p) {
        packageRepository.delete(p);
    }

    public void deleteAllNotes() {
        packageRepository.deleteAllPackages();
    }

    public LiveData<List<Package>> getAllPackages() {
        return allPackages;
    }

}
