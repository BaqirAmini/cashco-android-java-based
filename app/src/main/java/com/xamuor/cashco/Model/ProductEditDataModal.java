package com.xamuor.cashco.Model;

public class ProductEditDataModal {
    private String pImage, pName, pInventory, pRetailPrice, pCreated, pUpdated;
    private int pid;

    public ProductEditDataModal(int id, String pImage, String pName, String pInventory, String pRetailPrice, String pCreated, String pUpdated) {
        this.pid = id;
        this.pImage = pImage;
        this.pName = pName;
        this.pInventory = pName;
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

    public String getpName() {
        return pName;
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
