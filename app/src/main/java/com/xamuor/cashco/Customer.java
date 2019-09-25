package com.xamuor.cashco;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "customers", indices = @Index(value = {"customerId"}, unique = true))
public class Customer {
    private int customerId;
    private String sellerPermitNumber;
    private String businessName;
    private int companyId;
    private String customerName;
    private String customerLastName;
    private String customerPhone;
    private String customerPhoto;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int pCustId;
    @NonNull
    public int getpCustId() {
        return pCustId;
    }

    public void setpCustId(@NonNull int pCustId) {
        this.pCustId = pCustId;
    }
    public int getCustomerId() {
        return customerId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSellerPermitNumber() {
        return sellerPermitNumber;
    }

    public void setSellerPermitNumber(String sellerPermitNumber) {
        this.sellerPermitNumber = sellerPermitNumber;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(String customerPhoto) {
        this.customerPhoto = customerPhoto;
    }
}
