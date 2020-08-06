package com.ali.auth.third.core.config;

public class Version {
    private int major;
    private int micro;
    private int minor;

    public Version(int major2, int minor2, int micro2) {
        this.major = major2;
        this.minor = minor2;
        this.micro = micro2;
    }

    public String toString() {
        return this.major + "." + this.minor + "." + this.micro;
    }
}
