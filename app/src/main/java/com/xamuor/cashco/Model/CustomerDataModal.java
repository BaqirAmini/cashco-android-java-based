package com.xamuor.cashco.Model;

public class CustomerDataModal {
    private Double custBalance;
    private String  custId, custStatus, businessName, custFname, custLname, custPhone, custEmail, cust_state, cust_addr;


    public CustomerDataModal(String custId, String custStatus, String businessName, String custFname, String custLname, String custPhone, double custBalance) {
        this.custId = custId;
        this.custStatus = custStatus;
        this.businessName = businessName;
        this.custFname = custFname;
        this.custLname = custLname;
        this.custPhone = custPhone;
        this.custBalance = custBalance;
        this.custEmail = custEmail;
        this.cust_state = cust_state;
        this.cust_addr = cust_addr;

    }

    public String getCustId() {
        return custId;
    }

    public String getCustStatus() {
        return custStatus;
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

    public Double getCustBalance() {
        return custBalance;
    }

   /* public String getCustEmail() {
        return custEmail;
    }

    public String getCust_state() {
        return cust_state;
    }

    public String getCust_addr() {
        return cust_addr;
    }*/
}
