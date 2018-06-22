package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SIMCard {
    private Long Phone;
    private String SIMNumber;
    private String ServiceProvider;
    private String Active;
    private String RegisteredDate;
    private Long RegisteredDateTimeMilliSeconds;
    private String PlaceOfPurchase;
    private String PurchaserFirstName;
    private String PurchaserLastName;
    private String Company;
    private int ValidityDays;
    private String CreateDateTime;
    private String UpdateDateTime;

    public static ArrayList<SIMCard> getAllActive() {
        ArrayList<SIMCard> SIMCardArrayList = new ArrayList<SIMCard>();
        String querySelect;
        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT `Phone`, `SIMNumber`, `ServiceProvider`, `Active`, `RegisteredDate`, `PlaceOfPurchase`, `PurchaserFirstName`, `PurchaserLastName`, `Company`, `ValidityDays`, `CreateDateTime`, `UpdateDateTime` " + " "
                    + " FROM SIMCard " + " "
                    + " WHERE Active = 'Y'" + " "
                    + " ORDER BY RegisteredDate DESC " + " "
                    + " ;";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    SIMCard dataSIMCard = new SIMCard();
                    dataSIMCard.setPhone(rs.getLong("Phone"));
                    dataSIMCard.setSIMNumber(rs.getString("SIMNumber"));
                    dataSIMCard.setServiceProvider(rs.getString("ServiceProvider"));
                    dataSIMCard.setActive(rs.getString("Active"));
                    dataSIMCard.setRegisteredDate(rs.getString("RegisteredDate"));
                    dataSIMCard.setRegisteredDateTimeMilliSeconds(convertTextDateToMilliSeconds(rs.getString("RegisteredDate")));
                    dataSIMCard.setPlaceOfPurchase(rs.getString("PlaceOfPurchase"));
                    dataSIMCard.setPurchaserFirstName(rs.getString("PurchaserFirstName"));
                    dataSIMCard.setPurchaserLastName(rs.getString("PurchaserLastName"));
                    dataSIMCard.setCompany(rs.getString("Company"));
                    dataSIMCard.setValidityDays(rs.getInt("ValidityDays"));
                    dataSIMCard.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataSIMCard.setUpdateDateTime(rs.getString("UpdateDateTime"));

                } while (rs.next());
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


        return SIMCardArrayList;
    }

    public static void updateAllRegisteredDateTimeMilliSeconds() {
        String queryUpdate;
        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();


            queryUpdate = "SELECT `Phone`, `SIMNumber`, `ServiceProvider`, `Active`, `RegisteredDate`, `PlaceOfPurchase`, `PurchaserFirstName`, `PurchaserLastName`, `Company`, `ValidityDays`, `CreateDateTime`, `UpdateDateTime` " + " "
                    + " FROM SIMCard " + " "
                    + " WHERE Active = 'Y'" + " "
                    + " ORDER BY RegisteredDate DESC " + " "
                    + " ;";

            PreparedStatement pst = dbconnection.prepareStatement(queryUpdate);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    SIMCard dataSIMCard = new SIMCard();
                    dataSIMCard.setPhone(rs.getLong("Phone"));
                    dataSIMCard.setSIMNumber(rs.getString("SIMNumber"));
                    dataSIMCard.setServiceProvider(rs.getString("ServiceProvider"));
                    dataSIMCard.setActive(rs.getString("Active"));
                    dataSIMCard.setRegisteredDate(rs.getString("RegisteredDate"));
                    dataSIMCard.setRegisteredDateTimeMilliSeconds(convertTextDateToMilliSeconds(rs.getString("RegisteredDate")));
                    dataSIMCard.setPlaceOfPurchase(rs.getString("PlaceOfPurchase"));
                    dataSIMCard.setPurchaserFirstName(rs.getString("PurchaserFirstName"));
                    dataSIMCard.setPurchaserLastName(rs.getString("PurchaserLastName"));
                    dataSIMCard.setCompany(rs.getString("Company"));
                    dataSIMCard.setValidityDays(rs.getInt("ValidityDays"));
                    dataSIMCard.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataSIMCard.setUpdateDateTime(rs.getString("UpdateDateTime"));

                } while (rs.next());
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }


        return;
    }

    public static Long convertTextDateToMilliSeconds(String inTextDate) {
        long oldMillis = 0;
        String dateFormat = "dd MMMM yyyy hh:mm:ss:SSS zzzz";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        simpleDateFormat.setLenient(false);

        Date oldDate = new Date();

        try {
            if (!(inTextDate.isEmpty())) {
                oldDate = simpleDateFormat.parse(inTextDate);
                oldMillis = oldDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return oldMillis;
    }

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

    public void setRegisteredDateTimeMilliSeconds(Long RegisteredDateTimeMilliSeconds) {
        this.RegisteredDateTimeMilliSeconds = RegisteredDateTimeMilliSeconds;
    }

    public Long getRegisteredDateTimeMilliSeconds() {
        return RegisteredDateTimeMilliSeconds;
    }

}
