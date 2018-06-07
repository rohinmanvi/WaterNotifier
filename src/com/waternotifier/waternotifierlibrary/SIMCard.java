package com.waternotifier.waternotifierlibrary;

public class SIMCard {
    private Long Phone;
    private String SIMNumber;
    private String ServiceProvider;
    private String Active;
    private String RegisteredDate;
    private String PlaceOfPurchase;
    private String PurchaserFirstName;
    private String PurchaserLastName;
    private String Company;
    private int ValidityDays;
    private String CreateDateTime;
    private String UpdateDateTime;

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long Phone) {
        this.Phone = Phone;
    }

    public String getSIMNumber() {
        return SIMNumber;
    }

    public void setSIMNumber(String SIMNumber) {
        this.SIMNumber = SIMNumber;
    }

    public String getServiceProvider() {
        return ServiceProvider;
    }

    public void setServiceProvider(String ServiceProvider) {
        this.ServiceProvider = ServiceProvider;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }

    public String getRegisteredDate() {
        return RegisteredDate;
    }

    public void setRegisteredDate(String RegisteredDate) {
        this.RegisteredDate = RegisteredDate;
    }

    public String getPlaceOfPurchase() {
        return PlaceOfPurchase;
    }

    public void setPlaceOfPurchase(String PlaceOfPurchase) {
        this.PlaceOfPurchase = PlaceOfPurchase;
    }

    public String getPurchaserFirstName() {
        return PurchaserFirstName;
    }

    public void setPurchaserFirstName(String PurchaserFirstName) {
        this.PurchaserFirstName = PurchaserFirstName;
    }

    public String getPurchaserLastName() {
        return PurchaserLastName;
    }

    public void setPurchaserLastName(String PurchaserLastName) {
        this.PurchaserLastName = PurchaserLastName;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public int getValidityDays() {
        return ValidityDays;
    }

    public void setValidityDays(int ValidityDays) {
        this.ValidityDays = ValidityDays;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String CreateDateTime) {
        this.CreateDateTime = CreateDateTime;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String UpdateDateTime) {
        this.UpdateDateTime = UpdateDateTime;
    }

}
