package com.packager.user.Data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.packager.user.Entities.Package;
import com.packager.user.Data.PackageDao;
import com.packager.user.Data.PackagesDataBase;

import java.util.List;

public class PackageRepository {
    private PackageDao packageDao;
    private LiveData<List<Package>> allPackages;

    public PackageRepository(Application application) {
        PackagesDataBase database = PackagesDataBase.getInstance(application);
        packageDao = database.PackageDao();
        allPackages = packageDao.getAll();
    }

    public void insert(Package p) {
        new InsertPackageAsyncTask(packageDao).execute(p);
    }

    public void update(Package p) {
        new UpdatePackageAsyncTask(packageDao).execute(p);
    }

    public void delete(Package p) {
        new DeletePackageAsyncTask(packageDao).execute(p);
    }

    public void deleteAllPackages() {
        new DeleteAllPackageAsyncTask(packageDao).execute();
    }

    public LiveData<List<Package>> getAllPackages() {
        return allPackages;
    }

    public static class InsertPackageAsyncTask extends AsyncTask<Package, Void, Void> {
        private PackageDao packageDao;

        public InsertPackageAsyncTask(PackageDao packageDao) {
            this.packageDao = packageDao;
        }

        @Override
        protected Void doInBackground(Package... packages) {
            packageDao.insert(packages[0]);
            return null;
        }
    }

    public static class UpdatePackageAsyncTask extends AsyncTask<Package, Void, Void> {
        private PackageDao packageDao;

        public UpdatePackageAsyncTask(PackageDao packageDao) {
            this.packageDao = packageDao;
        }

        @Override
        protected Void doInBackground(Package... packages) {
            packageDao.update(packages[0]);
            return null;
        }
    }

    public static class DeletePackageAsyncTask extends AsyncTask<Package, Void, Void> {
        private PackageDao packageDao;

        public DeletePackageAsyncTask(PackageDao packageDao) {
            this.packageDao = packageDao;
        }

        @Override
        protected Void doInBackground(Package... packages) {
            packageDao.delete(packages[0]);
            return null;
        }
    }


    public static class DeleteAllPackageAsyncTask extends AsyncTask<Package, Void, Void> {
        private PackageDao packageDao;

        public DeleteAllPackageAsyncTask(PackageDao packageDao) {
            this.packageDao = packageDao;
        }

        @Override
        protected Void doInBackground(Package... packages) {
            packageDao.deleteAllPackages();
            return null;
        }
    }
}