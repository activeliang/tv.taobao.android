package com.ali.auth.third.core.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SNSSignInAccount implements Parcelable {
    public static final Parcelable.Creator<SNSSignInAccount> CREATOR = new Parcelable.Creator<SNSSignInAccount>() {
        public SNSSignInAccount createFromParcel(Parcel source) {
            return new SNSSignInAccount(source);
        }

        public SNSSignInAccount[] newArray(int size) {
            return new SNSSignInAccount[size];
        }
    };
    public String app_id;
    public String company;
    public String countryAbbr;
    public String countryFullName;
    public String email;
    public String firstName;
    public String lastName;
    public boolean neetBindExpressOpenId;
    public int site;
    public String snsType;
    public String token;
    public String trustOpenId;
    public String userId;

    public String toString() {
        return "SNSSignInAccount{snsType='" + this.snsType + '\'' + ", userId='" + this.userId + '\'' + ", token='" + this.token + '\'' + ", email='" + this.email + '\'' + ", firstName='" + this.firstName + '\'' + ", lastName='" + this.lastName + '\'' + ", company='" + this.company + '\'' + ", countryFullName='" + this.countryFullName + '\'' + ", countryAbbr='" + this.countryAbbr + '\'' + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.snsType);
        dest.writeString(this.userId);
        dest.writeString(this.token);
        dest.writeString(this.email);
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.company);
        dest.writeString(this.countryFullName);
        dest.writeString(this.countryAbbr);
    }

    public SNSSignInAccount() {
    }

    protected SNSSignInAccount(Parcel in) {
        this.snsType = in.readString();
        this.userId = in.readString();
        this.token = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.company = in.readString();
        this.countryFullName = in.readString();
        this.countryAbbr = in.readString();
    }
}
