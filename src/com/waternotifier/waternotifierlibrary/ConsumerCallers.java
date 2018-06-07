package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsumerCallers {


    private String IMEINumber;
    private Long SIMCardPhone;
    private String Name;
    private int ConsumerCount;
    private String CreateDateTime;
    private String UpdateDateTime;
    private String Status;

    /**
     * @param inConsumerCallerPhone -- If "" (empty), Returns false
     * @return Boolean - "True" Valid ConsumerCaller exists with Status = 'Y'.
     * otherwise "False"
     */
    public static Boolean exists(Long inConsumerCallerPhone) {

        String querySelect;

        DataOperations dataOperations = new DataOperations();

        if (inConsumerCallerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(DataOperations.IsValidPhone(inConsumerCallerPhone.toString()))) {
            return Boolean.FALSE;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT DISTINCT(cd.Phone)"
                    + " FROM ConsumerCallers as cc "
                    + " WHERE cc.Phone = " + inConsumerCallerPhone + " "
                    + " AND Status = 'Y' " + "; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

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
    } // END of ConsumerCallerExists // exists

    public static ArrayList<ConsumerCallers> getAll() {
        ArrayList<ConsumerCallers> consumerCallersArrayList = new ArrayList<ConsumerCallers>();
        String querySelect;
        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT cc.IMEINumber, cc.SIMCardPhone, cc.Name, cc.ConsumerCount, cc.CreateDateTime, cc.UpdateDateTime, cc.Status "
                    + " FROM ConsumerCallers as cc, SIMCard as sc "
                    + " WHERE cc.SIMCardPhone = sc.Phone " + " "
                    + " AND sc.Active = 'Y' " + " "
                    + " AND cc.Status = 'Y' " + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    ConsumerCallers dataConsumerCaller = new ConsumerCallers();
                    dataConsumerCaller.setIMEINumber(rs.getString("IMEINumber"));
                    dataConsumerCaller.setSIMCardPhone(rs.getLong("SIMCardPhone"));
                    dataConsumerCaller.setName(rs.getString("Name"));
                    dataConsumerCaller.setConsumerCount(rs.getInt("ConsumerCount"));
                    dataConsumerCaller.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataConsumerCaller.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    dataConsumerCaller.setStatus(rs.getString("Status"));

                    consumerCallersArrayList.add(dataConsumerCaller);
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


        return consumerCallersArrayList;
    }

    public static ArrayList<ConsumerCallers> getAll(int inZIPCODE, int inSeqNumber) {
        ArrayList<ConsumerCallers> consumerCallersArrayList = new ArrayList<ConsumerCallers>();
        String querySelect;
        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT cc.IMEINumber, cc.SIMCardPhone, cc.Name, cc.ConsumerCount, cc.CreateDateTime, cc.UpdateDateTime, cc.Status "
                    + " FROM ConsumerCallers as cc, SIMCard as sc "
                    + " WHERE cc.SIMCardPhone = sc.Phone " + " "
                    + " AND sc.Active = 'Y' " + " "
                    + " AND cc.LocationZIPCODE = " + inZIPCODE + " "
                    + " AND cc.LocationSeqNumber = " + inSeqNumber + " "
                    + " AND cc.Status = 'Y' " + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    ConsumerCallers dataConsumerCaller = new ConsumerCallers();
                    dataConsumerCaller.setIMEINumber(rs.getString("IMEINumber"));
                    dataConsumerCaller.setSIMCardPhone(rs.getLong("SIMCardPhone"));
                    dataConsumerCaller.setName(rs.getString("Name"));
                    dataConsumerCaller.setConsumerCount(rs.getInt("ConsumerCount"));
                    dataConsumerCaller.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataConsumerCaller.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    dataConsumerCaller.setStatus(rs.getString("Status"));

                    consumerCallersArrayList.add(dataConsumerCaller);
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


        return consumerCallersArrayList;
    }

    /**
     * Provides list of ConsumerCaller objects for a given -
     *
     * @param inZIPCODELocationSeqNumber - if NOT valid zipcode and sequence combination, it returns null array list
     * @return - Arraylist of ConsumerCallers list objects.
     */
    public static ArrayList<ConsumerCallers> getAll(int inZIPCODELocationSeqNumber) {
        ArrayList<ConsumerCallers> consumerCallersArrayList = new ArrayList<ConsumerCallers>();
        String querySelect;
        Location tempL;

        if (!(Location.validZIPCODESeqNumber(inZIPCODELocationSeqNumber))) {
            return consumerCallersArrayList;
        }

        tempL = Location.getZIPCODESeqNumber(inZIPCODELocationSeqNumber);

        if (tempL == null) {
            return consumerCallersArrayList;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT cc.IMEINumber, cc.SIMCardPhone, cc.Name, cc.ConsumerCount, cc.CreateDateTime, cc.UpdateDateTime, cc.Status "
                    + " FROM ConsumerCallers as cc, SIMCard as sc "
                    + " WHERE cc.SIMCardPhone = sc.Phone " + " "
                    + " AND sc.Active = 'Y' " + " "
                    + " AND cc.LocationZIPCODE = " + tempL.getZIPCODE() + " "
                    + " AND cc.LocationSeqNumber = " + tempL.getSeqNumber() + " "
                    + " AND cc.Status = 'Y' " + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    ConsumerCallers dataConsumerCaller = new ConsumerCallers();
                    dataConsumerCaller.setIMEINumber(rs.getString("IMEINumber"));
                    dataConsumerCaller.setSIMCardPhone(rs.getLong("SIMCardPhone"));
                    dataConsumerCaller.setName(rs.getString("Name"));
                    dataConsumerCaller.setConsumerCount(rs.getInt("ConsumerCount"));
                    dataConsumerCaller.setCreateDateTime(rs.getString("CreateDateTime"));
                    dataConsumerCaller.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    dataConsumerCaller.setStatus(rs.getString("Status"));

                    consumerCallersArrayList.add(dataConsumerCaller);
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

        return consumerCallersArrayList;
    }


    public static Boolean assignPhone(long inConsumerPhone, int inLocationCode) {

        Long callerPhoneNumber = LocationConsumers.getConsumerCallerPhone(inConsumerPhone, inLocationCode);
        Location tempL;
        tempL = Location.getZIPCODESeqNumber(inLocationCode);

        if (!(callerPhoneNumber == 0L)) {
            ConsumerCallers.updateConsumerCallerCount(callerPhoneNumber, tempL.getZIPCODE(), tempL.getSeqNumber());
            return Boolean.TRUE;
        }

        Boolean assignConsumerCallerPhoneStatus = false,
                updateConsumerCallerCountStatus = false;


        ArrayList<ConsumerCallers> listOfCallers = new ArrayList<ConsumerCallers>();
        int lowestIndex = 0;

//        listOfCallers = ConsumerCallers.getAll();
        listOfCallers = ConsumerCallers.getAll(tempL.getZIPCODE(), tempL.getSeqNumber());

        if (!(listOfCallers.size() > 0)) {
            return Boolean.FALSE;
        }

        int smallestCount = listOfCallers.get(0).getConsumerCount();
        callerPhoneNumber = listOfCallers.get(0).getSIMCardPhone();


        for (int i = 0; i < listOfCallers.size(); i++) {
            if (smallestCount > listOfCallers.get(i).getConsumerCount()) {
                lowestIndex = i;
                smallestCount = listOfCallers.get(lowestIndex).getConsumerCount();
                callerPhoneNumber = listOfCallers.get(lowestIndex).getSIMCardPhone();
            }
        }


        if (tempL != null) {
            assignConsumerCallerPhoneStatus = Consumer.updateCallerPhone(inConsumerPhone, tempL.getZIPCODE(), tempL.getSeqNumber(), callerPhoneNumber);
        } else {
            return Boolean.FALSE;
        }

        if (assignConsumerCallerPhoneStatus) {
            updateConsumerCallerCountStatus = ConsumerCallers.updateConsumerCallerCount(callerPhoneNumber, tempL.getZIPCODE(), tempL.getSeqNumber());
        }

        if (assignConsumerCallerPhoneStatus && updateConsumerCallerCountStatus) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * @param callerPhoneNumber -- If 0 or invalid phone number, Returns false
     * @return Boolean - "True" - Able to update to database, otherwise "False".
     */
    public static Boolean updateConsumerCallerCount(Long callerPhoneNumber, int inZipcode, int inSeqNum) {

        if (callerPhoneNumber == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(callerPhoneNumber.toString()))) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();
//
//        String updateSQL = "UPDATE ConsumerCallers "
//                + " SET ConsumerCount = " + callerCount + ", "
//                + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
//                + " WHERE SIMCardPhone = " + callerPhoneNumber + " "
//                + " AND LocationZIPCODE = " + inZipcode + " "
//                + " AND LocationSeqNumber = " + inSeqNum + " "
//                + "; ";


        String updateSQL = " UPDATE ConsumerCallers "
                + " SET ConsumerCount = (SELECT COUNT(*) "
                + " FROM LocationConsumers as LC1 "
                + " WHERE LC1.LocationZIPCODE = " + inZipcode + " "
                + " AND  LC1.LocationSeqNumber = " + inSeqNum + " "
                + " AND LC1.RegisteredFlag = 'Y' "
                + " AND LC1.ConsumerCallerPhone = " + callerPhoneNumber + " "
                + " GROUP BY LC1.LocationZIPCODE, LC1.LocationSeqNumber, LC1.ConsumerCallerPhone, LC1.RegisteredFlag)   "
                + ", "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE SIMCardPhone = " + callerPhoneNumber + " "
                + " AND LocationZIPCODE = " + inZipcode + " "
                + " AND LocationSeqNumber = " + inSeqNum + " "
                + "; ";

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            PreparedStatement pst = dbconnection.prepareStatement(updateSQL);

            int rs = pst.executeUpdate();

            if (rs > 0) {
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

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getConsumerCount() {
        return ConsumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        ConsumerCount = consumerCount;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        UpdateDateTime = updateDateTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
