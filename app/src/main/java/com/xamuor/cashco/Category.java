package com.xamuor.cashco;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "categories", indices = @Index(value = {"categoryID"}, unique = true))
public class Category {
    private int categoryID;
    private int companyId;
    private String categoryName;
    private String CategoryDesc;


    @PrimaryKey(autoGenerate = true)
    public int ctgId;


    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }


    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDesc() {
        return CategoryDesc;
    }

    public void setCategoryDesc(String categoryDesc) {
        CategoryDesc = categoryDesc;
    }
}
