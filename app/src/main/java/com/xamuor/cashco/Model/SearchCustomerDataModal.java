package com.xamuor.cashco.Model;

public class SearchCustomerDataModal {
    private String photo;
    private String businessName;
    private String phone;
    private String fName;
    private String lName;
    private String sellerPermitNumber;

    public SearchCustomerDataModal(String photo, String businessName, String phone, String fName, String lName, String sellerPermitNumber) {
        this.photo = photo;
       this.businessName = businessName;
       this.phone = phone;
       this.fName = fName;
       this.lName = lName;
       this.sellerPermitNumber = sellerPermitNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getPhone() {
        return phone;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getSellerPermitNumber() {
        return sellerPermitNumber;
    }
}
