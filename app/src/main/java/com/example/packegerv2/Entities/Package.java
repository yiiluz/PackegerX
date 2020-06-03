package com.example.packegerv2.Entities;

public class Package {
    private Configuration.PackageType packageType;
    private boolean isFragile;
    private Configuration.PackageWeight packageWeight;
    private String packageID;
    private String phoneAddressee;
    private String phoneSender;
    private Address currentAddress;

    public Package(Configuration.PackageType packageType, boolean isFragile, Configuration.PackageWeight packageWeight,
                   String packageID, String phoneAddressee, String phoneSender, Address currentAddress) {
        this.packageType = packageType;
        this.isFragile = isFragile;
        this.packageWeight = packageWeight;
        this.packageID = packageID;
        this.phoneAddressee = phoneAddressee;
        this.phoneSender = phoneSender;
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
}
