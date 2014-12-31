package com.isbank.framework.Ldap;

/**
 * Created by TK57720 on 13.03.2014.
 */

import java.io.Serializable;

public class EmployeeInfo implements Serializable {
    private int corporateNumber = 0;
    private String firstName = "";
    private String lastName = "";
    /*private String branchName = "";//TOLGA: kendi şube adı (ou)
    //private int superiorNumber = 0;
    private int branchCode = 0; //TOLGA: geçici görevli ise geçici görev şube kodu (gmudkod) değilse kendi şube kodu (mudkod)
    private int branchCode2  = 0; //TOLGA: kendi şube kodu (mudkod)
    private int rootBranchCode = 0;
    private String title = "";
    private String emailAddress = "";
    private int titleCode = 0;
    private String domainUserName = "";
    //public StatusCodes StatusCode { get; set; }
    //public HRDurumlar HRDurum { get; set; }
    //public SignatureLevels SignatureLevel { get; set; }
    //public Date BirthDate { get; set; }
    private String pozitionCode = "";
    private String pozitionTitle  = "";
    private String gorKod = "";
    //public String passWord ;
    private String bolumKod = "";
    private String ekipKod = "";
    private String bolumsuzPozKod = "";
    private String servisKod = "";*/

    public int getCorporateNumber() {
        return this.corporateNumber;
    }
    public void setCorporateNumber(int corporateNumber) {
        this.corporateNumber = corporateNumber;
    }

    public String getFirstName() {
        return this.firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return String.format("%s %s", (this.firstName != null ? this.firstName : ""), (this.lastName != null ? this.lastName : "")) ;
    }

    /*
    public int getSuperiorNumber() {
        return this.superiorNumber;
    }
    public void setSuperiorNumber(int superiorNumber) {
        this.superiorNumber = superiorNumber;
    }

    public String getBranchName() {
        return this.branchName;
    }
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getBranchCode() {
        return this.branchCode;
    }
    public void setBranchCode(int branchCode) {
        this.branchCode = branchCode;
    }

    public int getBranchCode2() {
        return this.branchCode2;
    }
    public void setBranchCode2(int branchCode2) {
        this.branchCode2 = branchCode2;
    }

    public int getRootBranchCode() {
        return this.rootBranchCode;
    }
    public void setRootBranchCode(int rootBranchCode) {
        this.rootBranchCode = rootBranchCode;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    */
}
