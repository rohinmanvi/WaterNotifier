package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class LocationConsumers {

    private int LocationZIPCODE;
    private int LocationSeqNumber;
    private Long ConsumersPhone;
    private String CreateDateTime;
    private String UpdateDateTime;
    private String RegisteredFlag;
    private String RegisteredDateTime;
    private String DeleteDateTime;
    private Long ConsumerCallerPhone;

    /**
     * @param inConsumerPhone     -- If 0 (empty), Returns false
     * @param inLocationZIPCODE   -- If 0 (empty), Returns false
     * @param inLocationSeqNumber -- If 0 (empty), Returns false
     * @return Boolean - "True" - inConsumerPhone exists for inLocationZIPCOde, inLocationSeqNumber.
     * otherwise "False"
     */
    public static Boolean exists(Long inConsumerPhone, int inLocationZIPCODE, int inLocationSeqNumber) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        inConsumerPhone = Math.abs(inConsumerPhone);
        inLocationZIPCODE = Math.abs(inLocationZIPCODE);
        inLocationSeqNumber = Math.abs(inLocationSeqNumber);

        if (inConsumerPhone == 0 || inLocationZIPCODE == 0 || inLocationSeqNumber == 0) {
            return Boolean.FALSE;
        }

        if (!Location.validZIPCODE(inLocationZIPCODE)) {
            return Boolean.FALSE;
        }

        if (!Location.validSeqNumber(inLocationSeqNumber)) {
            return Boolean.FALSE;
        }

        if (!DataOperations.IsValidPhone(inConsumerPhone.toString())) {
            return Boolean.FALSE;
        }

        if (!Location.locationExists(inLocationZIPCODE, inLocationSeqNumber)) {
            return Boolean.FALSE;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT lc.ConsumersPhone, lc.LocationZIPCODE, lc.LocationSeqNumber "
                    + " FROM LocationConsumers as lc "
                    + " WHERE lc.ConsumersPhone = " + inConsumerPhone + " "
                    + " AND lc.LocationZIPCODE = " + inLocationZIPCODE + " "
                    + " AND lc.LocationSeqNumber = " + inLocationSeqNumber + " ; ";

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
    } // END of exists

    /**
     * @param inConsumerPhone            -- If 0 (empty), Returns false
     * @param inLocationZIPCODESeqNumber -- If 0 (empty), Returns false
     * @return Boolean - "True" - inConsumerPhone exists for inLocationZIPCOde, inLocationSeqNumber.
     * otherwise "False"
     */
    public static Boolean exists(Long inConsumerPhone, int inLocationZIPCODESeqNumber) {

        String querySelect = "";
        DataOperations dataOperations = new DataOperations();

        inConsumerPhone = Math.abs(inConsumerPhone);
        inLocationZIPCODESeqNumber = Math.abs(inLocationZIPCODESeqNumber);

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        if (inConsumerPhone == 0 || inLocationZIPCODESeqNumber == 0) {
            return Boolean.FALSE;
        }

        if (!Location.validZIPCODESeqNumber(inLocationZIPCODESeqNumber)) {
            return Boolean.FALSE;
        }

        if (!DataOperations.IsValidPhone(inConsumerPhone.toString())) {
            return Boolean.FALSE;
        }

        if (!Location.locationExists(inLocationZIPCODESeqNumber)) {
            return Boolean.FALSE;
        }

        Location tempL = new Location();

        tempL = Location.getZIPCODESeqNumber(inLocationZIPCODESeqNumber);

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT lc.ConsumersPhone, lc.LocationZIPCODE, lc.LocationSeqNumber "
                    + " FROM LocationConsumers as lc "
                    + " WHERE lc.ConsumersPhone = " + inConsumerPhone + " "
                    + " AND lc.LocationZIPCODE = " + tempL.getZIPCODE() + " "
                    + " AND lc.LocationSeqNumber = " + tempL.getSeqNumber() + "; ";

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
    } // END of exists

    /**
     * @param inLocationConsumers -- If Object is null, Returns false
     * @return Boolean - "True" - Able to insert into database, otherwise "False".
     */
    public static Boolean insertToDatabase(LocationConsumers inLocationConsumers) {

        if (inLocationConsumers == null) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();
        String insertSQL = "INSERT INTO LocationConsumers ( "
                + " LocationZIPCODE, "
                + " LocationSeqNumber, "
                + " ConsumersPhone, "
                + " CreateDateTime, "
                + " UpdateDateTime, "
                + " RegisteredFlag "
                + " ) "
                + " VALUES "
                + " ("
                + inLocationConsumers.getLocationZIPCODE() + ", "
                + inLocationConsumers.getLocationSeqNumber() + ", "
                + inLocationConsumers.getConsumersPhone() + ", "
                + " \"" + datetime.toString() + "\", "
                + " \"" + datetime.toString() + "\", "
                + "'Y'"
                + " ); ";

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            PreparedStatement pst = dbconnection.prepareStatement(insertSQL);

            int rs = pst.executeUpdate();
//                ResultSet rs = pst.executeQuery();

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

    /**
     * @param inConsumerPhone -- If 0 or "" , Returns false
     * @return Boolean - "True" - Able to insert into database, otherwise "False".
     */
    public static Boolean insertToDatabase(Long inConsumerPhone, int inLocationCode, String inFullName) {

        boolean existingConsumer = false,
                existingLocation = false,
                existingLocationConsumer = false,
                insertConsumerStatus = false,
                insertConsumerLocationStatus = false;
        int inLocationZIPCODE = 0, inLocationSeqNumber = 0;
        String insertSQL = "";
        LocationConsumers tempLC = new LocationConsumers();
        Location tempL = new Location();

        if (!(Location.validZIPCODESeqNumber(inLocationCode))) {
            return false;
        }

        ConsumerOperations conOp = new ConsumerOperations();

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return false;
        }

        existingLocation = (Location.locationExists(inLocationCode));
        existingConsumer = (Consumer.consumerExists(inConsumerPhone));

        if (!(existingConsumer)) {
            return false;
        }

        if (existingConsumer && existingLocation) {

            existingLocationConsumer = (LocationConsumers.exists(inConsumerPhone, inLocationCode));

            tempL = Location.getZIPCODESeqNumber(inLocationCode);

            tempLC.setConsumersPhone(inConsumerPhone);
            tempLC.setLocationZIPCODE(tempL.getZIPCODE());
            tempLC.setLocationSeqNumber(tempL.getSeqNumber());

            if (existingLocationConsumer) {
                LocationConsumers.updateToDatabase(tempLC);
            } else {
                insertConsumerLocationStatus = LocationConsumers.insertToDatabase(tempLC);
            }
        }

        if (existingConsumer && existingLocationConsumer) {
            return true;
        }

        return insertConsumerLocationStatus;
    }


    /**
     * @param inLocationConsumers -- If Object is null, Returns false
     * @return Boolean - "True" - Able to update to database, otherwise "False".
     */
    public static Boolean updateToDatabase(LocationConsumers inLocationConsumers) {

        if (inLocationConsumers == null) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();

        String updateSQL = "UPDATE LocationConsumers "
                + " SET RegisteredFlag = 'Y', "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE LocationZIPCODE = " + inLocationConsumers.getLocationZIPCODE() + " AND "
                + " LocationSeqNumber = " + inLocationConsumers.getLocationSeqNumber() + " AND "
                + " ConsumersPhone = " + inLocationConsumers.getConsumersPhone() + "; ";

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

    /**
     *
     * @param inConsumerPhone - -- If 0 or invalid phone number, Returns false
     * @param inZipcode -- If 0 or invalid ZIPCODE number, Returns false
     * @param inLocSeqNum -- If 0 or invalid SeqNumber , Returns false
     * @param callerPhoneNumber -- If 0 or invalid phone number, Returns false
     * @return Boolean - "True" - Able to update to database, otherwise "False".
     */
    public static Boolean updateCallerPhone(Long inConsumerPhone, int inZipcode, int inLocSeqNum, Long callerPhoneNumber) {
        boolean existingConsumer = false,
                existingLocation = false,
                existingLocationConsumer = false;

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        if (callerPhoneNumber == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(callerPhoneNumber.toString()))) {
            return Boolean.FALSE;
        }

        if (!(Location.validZIPCODE(inZipcode))) {
            return false;
        }

        if (!(Location.validSeqNumber(inLocSeqNum))) {
            return false;
        }

        existingLocation = Location.locationExists(inZipcode, inLocSeqNum);
        existingConsumer = (Consumer.consumerExists(inConsumerPhone));

        if (!(existingConsumer)) {
            return false;
        }

        if (existingConsumer && existingLocation) {

            String inLocationCode = "" + inZipcode + inLocSeqNum;
            existingLocationConsumer = (LocationConsumers.exists(inConsumerPhone, Integer.valueOf(inLocationCode)));

            if (existingLocationConsumer) {
                Date datetime = new Date();

                String updateSQL = "UPDATE LocationConsumers "
                        + " SET ConsumerCallerPhone = " + callerPhoneNumber + ", "
                        + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                        + " WHERE ConsumersPhone = " + inConsumerPhone + " "
                        + " AND LocationZIPCODE = " + inZipcode + " "
                        + " AND LocationSeqNumber = " + inLocSeqNum + " "
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
            }
        }
        return Boolean.FALSE;

    } // END of updateCallerPhone


    /**
     * @param inConsumerPhone -- If 0 or invalid phone number, Returns false
     * @return Boolean - "True" - Able to update to database, otherwise "False".
     */
    public static Boolean updateToDatabase(Long inConsumerPhone) {

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();

        String updateSQL = "UPDATE LocationConsumers "
                + " SET RegisteredFlag = 'Y', "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE ConsumersPhone = " + inConsumerPhone + "; ";

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


    /**
     *
     * @param inConsumerPhone - -- If 0 or invalid phone number, Returns false
     * @param inZipcode -- If 0 or invalid ZIPCODE number, Returns false
     * @param inLocSeqNum -- If 0 or invalid SeqNumber , Returns false
     * @return Boolean - "True" - Able to update to database, otherwise "False".
     */
    public static Boolean updateLastWaterNotificationCall(Long inConsumerPhone, int inZipcode, int inLocSeqNum) {
        boolean existingConsumer = false,
                existingLocation = false,
                existingLocationConsumer = false;

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        if (!(Location.validZIPCODE(inZipcode))) {
            return false;
        }

        if (!(Location.validSeqNumber(inLocSeqNum))) {
            return false;
        }

        existingLocation = Location.locationExists(inZipcode, inLocSeqNum);
        existingConsumer = (Consumer.consumerExists(inConsumerPhone));

        if (!(existingConsumer)) {
            return false;
        }

        if (existingConsumer && existingLocation) {

            String inLocationCode = "" + inZipcode + inLocSeqNum;
            existingLocationConsumer = (LocationConsumers.exists(inConsumerPhone, Integer.valueOf(inLocationCode)));

            if (existingLocationConsumer) {
                Date datetime = new Date();
                Long milliseconds = datetime.getTime();

                String updateSQL = "UPDATE LocationConsumers "
                        + " SET LastWaterNotificationCall = " + milliseconds + ", "
                        + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                        + " WHERE ConsumersPhone = " + inConsumerPhone + " "
                        + " AND LocationZIPCODE = " + inZipcode + " "
                        + " AND LocationSeqNumber = " + inLocSeqNum + " "
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
            }
        }
        return Boolean.FALSE;

    } // END of updateLastWaterNotificationCall




    /**
     * @param inConsumerPhone -- If 0 or invalid phone number, Returns false
     * @return Boolean - "True" - Able to delete at database, otherwise "False".
     */
    public static Boolean deleteToDatabase(Long inConsumerPhone) {

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();

        String deleteSQL = "UPDATE LocationConsumers "
                + " SET RegisteredFlag = 'N', "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\", "
                + " DeleteDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE ConsumersPhone = " + inConsumerPhone + "; ";

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            PreparedStatement pst = dbconnection.prepareStatement(deleteSQL);

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

    /**
     * @param inLocationConsumers -- If Object is null, Returns false
     * @return Boolean - "True" - Able to delete into database, otherwise "False".
     */
    public static Boolean deleteToDatabase(LocationConsumers inLocationConsumers) {

        if (inLocationConsumers == null) {
            return Boolean.FALSE;
        }

        Date datetime = new Date();

        String deleteSQL = "UPDATE LocationConsumers "
                + " SET RegisteredFlag = 'N', "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\", "
                + " DeleteDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE LocationZIPCODE = " + inLocationConsumers.getLocationZIPCODE() + " AND "
                + " LocationSeqNumber = " + inLocationConsumers.getLocationSeqNumber() + " AND "
                + " ConsumersPhone = " + inLocationConsumers.getConsumersPhone() + "; ";

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            PreparedStatement pst = dbconnection.prepareStatement(deleteSQL);

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

    /**
     * @param inConsumerPhone -- If 0 or "" , Returns false
     * @return Boolean - "True" - Able to delete at database, otherwise "False".
     */
    public static Boolean deleteToDatabase(Long inConsumerPhone, int inLocationCode) {

        boolean existingConsumer = false,
                existingLocation = false,
                existingLocationConsumer = false,
                deleteConsumerStatus = false,
                deleteConsumerLocationStatus = false;
        String deleteSQL = "";
        LocationConsumers tempLC = new LocationConsumers();
        Location tempL = new Location();

        if (!(Location.validZIPCODESeqNumber(inLocationCode))) {
            return false;
        }

        ConsumerOperations conOp = new ConsumerOperations();

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return false;
        }

        existingLocation = (Location.locationExists(inLocationCode));
        existingConsumer = (Consumer.consumerExists(inConsumerPhone));

        if (!(existingConsumer)) {
            return false;
        }

        if (existingConsumer && existingLocation) {
            existingLocationConsumer = (LocationConsumers.exists(inConsumerPhone, inLocationCode));
        }

        if (!(existingConsumer) && !(existingLocationConsumer)) {
            return true;
        }

        if (existingLocationConsumer) {
            tempL = Location.getZIPCODESeqNumber(inLocationCode);

            tempLC.setConsumersPhone(inConsumerPhone);
            tempLC.setLocationZIPCODE(tempL.getZIPCODE());
            tempLC.setLocationSeqNumber(tempL.getSeqNumber());

            deleteConsumerLocationStatus = LocationConsumers.deleteToDatabase(tempLC);
        }

        return deleteConsumerLocationStatus;
    }

    /**
     * @param inConsumerPhone            -- If 0 (empty), Returns 0L
     * @param inLocationZIPCODESeqNumber -- If 0 (empty), Returns 0L
     * @return Long - returnConsumerCallerPhone - inConsumerPhone exists for inLocationZIPCOde, inLocationSeqNumber.
     * otherwise 0L
     */
    public static Long getConsumerCallerPhone(Long inConsumerPhone, int inLocationZIPCODESeqNumber) {

        Long returnConsumerCallerPhone = 0L;

        String querySelect = "";
        DataOperations dataOperations = new DataOperations();

        inConsumerPhone = Math.abs(inConsumerPhone);
        inLocationZIPCODESeqNumber = Math.abs(inLocationZIPCODESeqNumber);

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return returnConsumerCallerPhone;
        }

        if (inConsumerPhone == 0 || inLocationZIPCODESeqNumber == 0) {
            return returnConsumerCallerPhone;
        }

        if (!Location.validZIPCODESeqNumber(inLocationZIPCODESeqNumber)) {
            return returnConsumerCallerPhone;
        }

        if (!DataOperations.IsValidPhone(inConsumerPhone.toString())) {
            return returnConsumerCallerPhone;
        }

        if (!Location.locationExists(inLocationZIPCODESeqNumber)) {
            return returnConsumerCallerPhone;
        }

        Location tempL = new Location();

        tempL = Location.getZIPCODESeqNumber(inLocationZIPCODESeqNumber);

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            //querySelect = "SELECT lc.LocationZIPCODE, lc.LocationSeqNumber, lc.ConsumersPhone, lc.ConsumerCallerPhone, lc.CreateDateTime, lc.UpdateDateTime, lc.RegisteredFlag, lc.RegisteredDateTime, lc.DeleteDateTime "
            querySelect = "SELECT lc.ConsumerCallerPhone "
                    + " FROM LocationConsumers as lc "
                    + " WHERE lc.ConsumersPhone = " + inConsumerPhone + " "
                    + " AND lc.LocationZIPCODE = " + tempL.getZIPCODE() + " "
                    + " AND lc.LocationSeqNumber = " + tempL.getSeqNumber() + "; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                returnConsumerCallerPhone = rs.getLong("ConsumerCallerPhone");
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return returnConsumerCallerPhone;
    } // END of getConsumerCallerPhone

    /**
     * @param inConsumerCallerPhone -- If 0 (empty), Returns 0L
     * @param inZIPCODE             -- If 0 (empty), Returns 0L
     * @param inSeqNumber           -- If 0 (empty), Returns 0L
     * @return Aray List of - ConsumerPhone.
     * otherwise empty array list
     */
    public static ArrayList<Long> getAllConsumerPhone(Long inConsumerCallerPhone, int inZIPCODE, int inSeqNumber) {

        ArrayList<Long> returnListOfConsumerPhone = new ArrayList<>();

        String querySelect = "";

        inConsumerCallerPhone = Math.abs(inConsumerCallerPhone);
        inZIPCODE = Math.abs(inZIPCODE);
        inSeqNumber = Math.abs(inSeqNumber);

        if (inConsumerCallerPhone == 0 || inZIPCODE == 0 || inSeqNumber == 0) {
            return returnListOfConsumerPhone;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerCallerPhone.toString()))) {
            return returnListOfConsumerPhone;
        }

        if (!Location.validZIPCODE(inZIPCODE)) {
            return returnListOfConsumerPhone;
        }

        if (!Location.validSeqNumber(inSeqNumber)) {
            return returnListOfConsumerPhone;
        }

        if (!Location.locationExists(inZIPCODE, inSeqNumber)) {
            return returnListOfConsumerPhone;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            Date datetime = new Date();
            Long milliseconds = datetime.getTime();

            //querySelect = "SELECT lc.LocationZIPCODE, lc.LocationSeqNumber, lc.ConsumersPhone, lc.ConsumerCallerPhone, lc.CreateDateTime, lc.UpdateDateTime, lc.RegisteredFlag, lc.RegisteredDateTime, lc.DeleteDateTime "
            querySelect = "SELECT lc.ConsumersPhone "
                    + " FROM LocationConsumers as lc "
                    + " WHERE lc.ConsumerCallerPhone = " + inConsumerCallerPhone + " "
                    + " AND lc.LocationZIPCODE = " + inZIPCODE + " "
                    + " AND lc.LocationSeqNumber = " + inSeqNumber + " "
                    + " AND (( " + milliseconds + " - lc.LastWaterNotificationCall) > 3600000)" + " ; ";
//querySelect = "SELECT lc.ConsumersPhone "
//                    + " FROM LocationConsumers as lc "
//                    + " WHERE lc.ConsumerCallerPhone = " + inConsumerCallerPhone + " "
//                    + " AND lc.LocationZIPCODE = " + inZIPCODE + " "
//                    + " AND lc.LocationSeqNumber = " + inSeqNumber + "; ";


            
            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    returnListOfConsumerPhone.add(rs.getLong("ConsumersPhone"));
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

        return returnListOfConsumerPhone;
    } // END of getConsumerCallerPhone

    /**
     * @return ArrayList<LocationConsumers> - List of LocationConsumers Objects
     */
    public static ArrayList<LocationConsumers> getAllActiveDatabaseLocationConsumersWithNoConsumerCaller() {

        ArrayList<LocationConsumers> outLocationConsumersList = new ArrayList<LocationConsumers>();


        String querySelect = "";
        DataOperations dataOperations = new DataOperations();

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = " SELECT LocationZIPCODE, LocationSeqNumber, ConsumersPhone, ConsumerCallerPhone, CreateDateTime, UpdateDateTime, RegisteredFlag, RegisteredDateTime, DeleteDateTime "
                    + " FROM LocationConsumers "
                    + " WHERE RegisteredFlag = 'Y' " + " "
                    + " AND ConsumerCallerPhone = 0 " + " "
                    + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    LocationConsumers locationConsumer = new LocationConsumers();

                    locationConsumer.setLocationZIPCODE(rs.getInt("LocationZIPCODE"));
                    locationConsumer.setLocationSeqNumber(rs.getInt("LocationSeqNumber"));
                    locationConsumer.setConsumersPhone(rs.getLong("ConsumersPhone"));
                    locationConsumer.setConsumerCallerPhone(rs.getLong("ConsumerCallerPhone"));
                    locationConsumer.setCreateDateTime(rs.getString("CreateDateTime"));
                    locationConsumer.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    locationConsumer.setRegisteredFlag(rs.getString("RegisteredFlag"));
                    locationConsumer.setRegisteredDateTime(rs.getString("RegisteredDateTime"));
                    locationConsumer.setDeleteDateTime(rs.getString("DeleteDateTime"));

                    outLocationConsumersList.add(locationConsumer);
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

        return outLocationConsumersList;
    } // END of getAllActiveDatabaseLocationConsumers

    /**
     * @return ArrayList<LocationConsumers> - List of LocationConsumers Objects
     */
    public static ArrayList<LocationConsumers> getAllActiveDatabaseLocationConsumers() {

        ArrayList<LocationConsumers> outLocationConsumersList = new ArrayList<LocationConsumers>();


        String querySelect = "";
        DataOperations dataOperations = new DataOperations();

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = " SELECT LocationZIPCODE, LocationSeqNumber, ConsumersPhone, ConsumerCallerPhone, CreateDateTime, UpdateDateTime, RegisteredFlag, RegisteredDateTime, DeleteDateTime "
                    + " FROM LocationConsumers "
                    + " WHERE RegisteredFlag = 'Y' " + " "
                    + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    LocationConsumers locationConsumer = new LocationConsumers();

                    locationConsumer.setLocationZIPCODE(rs.getInt("LocationZIPCODE"));
                    locationConsumer.setLocationSeqNumber(rs.getInt("LocationSeqNumber"));
                    locationConsumer.setConsumersPhone(rs.getLong("ConsumersPhone"));
                    locationConsumer.setConsumerCallerPhone(rs.getLong("ConsumerCallerPhone"));
                    locationConsumer.setCreateDateTime(rs.getString("CreateDateTime"));
                    locationConsumer.setUpdateDateTime(rs.getString("UpdateDateTime"));
                    locationConsumer.setRegisteredFlag(rs.getString("RegisteredFlag"));
                    locationConsumer.setRegisteredDateTime(rs.getString("RegisteredDateTime"));
                    locationConsumer.setDeleteDateTime(rs.getString("DeleteDateTime"));

                    outLocationConsumersList.add(locationConsumer);
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

        return outLocationConsumersList;
    } // END of getAllActiveDatabaseLocationConsumers

    public Long getConsumerCallerPhone() {
        return ConsumerCallerPhone;
    }


//    /**
//     * @param inConsumerPhone -- If 0 or "" , Returns false
//     * @return Boolean - "True" - Able to insert into database, otherwise "False".
//     */
//    public static Boolean insertToDatabase(Long inConsumerPhone, Long inNotifierPhone, String inFullName) {
//
//        boolean existingConsumer = false,
//                existingNotifier = false,
//                existingLocationConsumer = false,
//                insertConsumerStatus = false,
//                insertConsumerLocationStatus = false;
//        int inLocationZIPCODE = 0, inLocationSeqNumber = 0;
//        String insertSQL = "";
//        LocationConsumers tempLC = new LocationConsumers();
//        Location tempL = new Location();
//
//        ConsumerOperations conOp = new ConsumerOperations();
//
//        if (!(conOp.IsValidPhone(inConsumerPhone.toString()))) {
//            return false;
//        }
//
//        if (!(conOp.IsValidPhone(inNotifierPhone.toString()))) {
//            return false;
//        }
//
//        existingNotifier = (Notifier.notifierExists(inNotifierPhone));
//
//        existingConsumer = (Consumer.consumerExists(inConsumerPhone));
//
//        if (existingConsumer && existingNotifier) {
//
//            existingLocationConsumer = (LocationConsumers.exists(inConsumerPhone, inLocationCode));
//        } else {
//            return false;
//        }
//
//        if (existingConsumer && existingLocationConsumer) {
//            return true;
//        }
//
//        if (!(existingLocationConsumer)) {
//            tempL = Location.getZIPCODESeqNumber(inLocationCode);
//
//            tempLC.setConsumersPhone(inConsumerPhone);
//            tempLC.setLocationZIPCODE(tempL.getZIPCODE());
//            tempLC.setLocationSeqNumber(tempL.getSeqNumber());
//
//            insertConsumerLocationStatus = LocationConsumers.insertToDatabase(tempLC);
//        }
//
//        return insertConsumerLocationStatus;
//    }

    public void setConsumerCallerPhone(Long consumerCallerPhone) {
        ConsumerCallerPhone = consumerCallerPhone;
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

    public Long getConsumersPhone() {
        return ConsumersPhone;
    }

    public void setConsumersPhone(Long ConsumersPhone) {
        this.ConsumersPhone = ConsumersPhone;
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

    public String getRegisteredFlag() {
        return RegisteredFlag;
    }

    public void setRegisteredFlag(String RegisteredFlag) {
        this.RegisteredFlag = RegisteredFlag;
    }

    public String getRegisteredDateTime() {
        return RegisteredDateTime;
    }

    public void setRegisteredDateTime(String RegisteredDateTime) {
        this.RegisteredDateTime = RegisteredDateTime;
    }

    public String getDeleteDateTime() {
        return DeleteDateTime;
    }

    public void setDeleteDateTime(String DeleteDateTime) {
        this.DeleteDateTime = DeleteDateTime;
    }


}
