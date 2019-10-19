package com.xamuor.cashco.Model;

public class CustomerDataModal {
    private String sellerPermit, businessName, custFname, custLname,
            custPhone, custEmail, country, city, cust_state, zipCode, address1, address2, regDate;
    private int custId;


    public CustomerDataModal(int custId, String seller, String businessName, String custFname, String custLname, String custPhone,
                             String custEmail, String country, String city, String custState, String zipCode, String addr1, String addr2, String regDate) {
        this.custId =  custId;
        this.sellerPermit = seller;
        this.businessName = businessName;
        this.custFname = custFname;
        this.custLname = custLname;
        this.custPhone = custPhone;
        this.regDate = regDate;
        this.custEmail = custEmail;
        this.cust_state = custState;
        this.zipCode = zipCode;
        this.country = country;
        this.city = city;
        this.address1 = addr1;
        this.address2 = addr2;

    }

    public int getCustId() {
        return custId;
    }


    public String getBusinessName() {
        return businessName;
    }

    public String getCustFname() {
        return custFname;
    }

    public String getCustLname() {
        return custLname;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getSellerPermit() {
        return sellerPermit;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public String getCust_state() {
        return cust_state;
    }


}
