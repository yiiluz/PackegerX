package com.packager.user.Entities;

public class Configuration {

    public enum PackageType {ENVELOPE, SMALL_SIZE, LARGE_SIZE, BIG_SIZE };
    public enum PackageWeight {LT_500_GR, LT_1_KG, LT_5_KG, LT_20_KG};
    public enum SignInMethod {PHONE, EMAIL, GOOGLE};
    public enum Status {REGISTERED, ON_THE_WAY, RECEIVED};
    public static final int MAX_DISTANCE = 200000; //KM
}
