package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Notifier {

    private String IMEINumber;
    private Long SIMCardPhone;
    private int LocationZIPCODE;
    private int LocationSeqNumber;
    private String NotifierName;
    private String CreateDateTime;
    private String UpdateDateTime;
    private String Status;

    /**
     * @param inNotifierPhone -- If "" (empty), Returns false
     * @return Boolean - "True" Valid notifier exists with Status = 'Y'.
     * otherwise "False"
     */
    public static Boolean notifierExists(Long inNotifierPhone) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inNotifierPhone == 0) {
            return Boolean.FALSE;
        } else if (DataOperations.IsValidPhone(inNotifierPhone.toString())) {
            whereAdditionalConditions = " AND n.SIMCardPhone = " + inNotifierPhone + " ";
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT DISTINCT(n.SIMCardPhone)"
                    + " FROM SIMCard as sc, Notifiers as n "
                    + " WHERE sc.Phone = n.SIMCardPhone "
                    + " AND n.Status = 'Y' "
                    + " AND sc.Active = 'Y' OR sc.Active IS NULL";

            if (whereAdditionalConditions == null) {
                return Boolean.FALSE;
            } else {
                queryString = querySelect + whereAdditionalConditions;
            }

            PreparedStatement pst = dbconnection.prepareStatement(queryString);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return Boolean.TRUE;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return Boolean.FALSE;
    } // END of notifierExists

    /**
     * @param inNotifierZipCodeSeqNumber -- If "" (empty), Returns false
     * @return  Notifiers - Long SIMCardPhone number - for Valid notifier exists with Status = 'Y'.
     * otherwise "False"
     */
    public static Long getSIMCardPhone(int inNotifierZipCodeSeqNumber) {

        Location tempL = new Location();
        Long outNotifierPhone = 0L;
        String queryString = "";

        if (inNotifierZipCodeSeqNumber == 0) {
            return outNotifierPhone;
        }

        if (Location.validZIPCODESeqNumber(inNotifierZipCodeSeqNumber)) {
            tempL = Location.getZIPCODESeqNumber(inNotifierZipCodeSeqNumber);
        }

        if (tempL == null || tempL.getZIPCODE() == 0 || tempL.getSeqNumber() == 0) {
            return outNotifierPhone;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            queryString = "SELECT n.SIMCardPhone"
                    + " FROM SIMCard as sc, Notifiers as n "
                    + " WHERE sc.Phone = n.SIMCardPhone "
                    + " AND n.Status = 'Y' "
                    + " AND sc.Active = 'Y' " + " "
                    + " AND n.LocationZIPCODE = " + tempL.getZIPCODE() + " "
                    + " AND n.LocationSeqNumber = " + tempL.getSeqNumber() + " "
                    + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(queryString);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                outNotifierPhone = rs.getLong("SIMCardPhone");
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return outNotifierPhone;
    } // END of getSIMCardPhone

    public static ArrayList<Notifier> getAll() {
        ArrayList<Notifier> NotifierArrayList = new ArrayList<Notifier>();
        String querySelect;
        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();


            querySelect = "SELECT IMEINumber, SIMCardPhone, LocationZIPCODE, LocationSeqNumber, NotifierName, CreateDateTime, UpdateDateTime, Status " + " "
                    + " FROM Notifiers " + " "
                    + " WHERE Status = 'Y'"
                    + " ORDER BY LocationZIPCODE, LocationSeqNumber, SIMCardPhone " + " "
                    + " ;";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    Notifier dataNotifier = new Notifier();
                    dataNotifier.setIMEINumber(rs.getString("IMEINumber"));
                    dataNotifier.setSIMCardPhone(rs.getLong("SIMCardPhone"));
                    dataNotifier.setLocationZIPCODE(rs.getInt("LocationZIPCODE"));
                    dataNotifier.setLocationSeqNumber(rs.getInt("LocationSeqNumber"));
                    dataNotifier.setNotifierName(rs.getString("NotifierName"));
                    dataNotifier.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataNotifier.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    dataNotifier.setStatus(rs.getString("Status"));
                    NotifierArrayList.add(dataNotifier);
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


        return NotifierArrayList;
    }

    public String getIMEINumber() {
        return IMEINumber;
    }

    public void setIMEINumber(String IMEINumber) {
        this.IMEINumber = IMEINumber;
    }

    public Long getSIMCardPhone() {
        return SIMCardPhone;
    }

    public void setSIMCardPhone(Long SIMCardPhone) {
        this.SIMCardPhone = SIMCardPhone;
    }

    public int getLocationZIPCODE() {
        return LocationZIPCODE;
    }

    public void setLocationZIPCODE(int LocationZIPCODE) {
        this.LocationZIPCODE = LocationZIPCODE;
    }

    public int getLocationSeqNumber() {
        return LocationSeqNumber;
    }

    public void setLocationSeqNumber(int LocationSeqNumber) {
        this.LocationSeqNumber = LocationSeqNumber;
    }

    public String getNotifierName() {
        return NotifierName;
    }

    public void setNotifierName(String NotifierName) {
        this.NotifierName = NotifierName;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

}
