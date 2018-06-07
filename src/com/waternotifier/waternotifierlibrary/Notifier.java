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
