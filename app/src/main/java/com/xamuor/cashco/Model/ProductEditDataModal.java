package com.xamuor.cashco.Model;

public class ProductEditDataModal {
    private String pImage, pName, pDesc, pInventory, barcode,  pCost,  pRetailPrice, pCreated, pUpdated;
    private int pid;

    public ProductEditDataModal(int id, String pImage, String pName, String pDesc,  String pInventory, String barcode, String pCost, String pRetailPrice, String pCreated, String pUpdated) {
        this.pid = id;
        this.pImage = pImage;
        this.pName = pName;
        this.pDesc = pDesc;
        this.pInventory = pInventory;
        this.barcode = barcode;
        this.pCost = pCost;
        this.pRetailPrice = pRetailPrice;
        this.pCreated = pCreated;
        this.pUpdated = pUpdated;
    }

    public int getPid() {
        return pid;
    }

    public String getpImage() {
        return pImage;
    }

    public String getpInventory() {
        return pInventory;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getpName() {
        return pName;
    }

    public String getpDesc() {
        return pDesc;
    }


    public String getpCost() {
        return pCost;
    }

    public String getpRetailPrice() {
        return pRetailPrice;
    }

    public String getpCreated() {
        return pCreated;
    }

    public String getpUpdated() {
        return pUpdated;
    }
}
