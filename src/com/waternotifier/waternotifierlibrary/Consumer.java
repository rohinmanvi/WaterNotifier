package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class Consumer {
    private Long Phone;
    private String FirstName;
    private String LastName;
    private String eMailAddress;
    private String AdharCardNumber;
    private String SMS;
    private String MissedCall;
    private String AppMessage;
    private String eMail;
    private String Website;
    private String ReadyToPayFees;
    private String SIGNATURE;
    private String WaterOutletTAP;
    private int TotalFamilySize;
    private int NoOfHouseholdAdults;
    private String WorkHoursFrom;
    private String WorkHoursTo;
    private String WaterStorageUdergroundTank;
    private String WaterUGStorageCapacity;
    private String WaterStorageLandlevelTank;
    private String WaterLLStorageCapacity;
    private String WaterStorageOverheadTank;
    private String WaterOHStorageCapacity;
    private String RecordSMS;
    private String Title;
    private String Subtitle;
    private String DeleteFlag;
    private String Note1;
    private String Note2;
    private String Note3;
    private String CreateDateTime;
    private String UpdateDateTime;

    /**
     * @param inConsumerPhone -- If "" (empty), Returns false
     * @return Boolean - "True" Valid Consumer exists with Status = 'Y'.
     * otherwise "False"
     */
    public static Boolean consumerExists(Long inConsumerPhone) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(DataOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT DISTINCT(c.Phone)"
                    + " FROM Consumers as c "
                    + " WHERE c.Phone = " + inConsumerPhone + "; ";

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
    } // END of ConsumerExists

    /**
     * @param inConsumerPhone -- If 0 or "" (empty), Returns false
     * @return Boolean - "True" Valid Consumer exists and able to update the record at database level.
     * otherwise "False"
     */
    public static Boolean insertToDatabase(Long inConsumerPhone, String inFullName) {

        ConsumerOperations conOp = new ConsumerOperations();

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return false;
        }


        boolean insertToDatabaseStatus = false,
                existingConsumer = false;

        String insertSQL = "";
        Date datetime = new Date();

        Consumer tempConsumer = new Consumer();

        existingConsumer = (Consumer.consumerExists(inConsumerPhone));
        tempConsumer.setPhone(inConsumerPhone);
//            tempConsumer.setPhone(Long.parseUnsignedLong(inConsumerPhone));
        if (!inFullName.isEmpty() || inFullName != "") {
            String[] data = inFullName.split(" ");
            if (data.length > 1) {
                tempConsumer.setFirstName(inFullName.substring(0, inFullName.indexOf(" ")));
                tempConsumer.setLastName(inFullName.substring(inFullName.indexOf(" ") + 1));
            } else {
                tempConsumer.setFirstName(inFullName);
                tempConsumer.setLastName("EmptyLast");
            }
        } else {
            tempConsumer.setFirstName("EmptyFirst");
            tempConsumer.setLastName("EmptyLast");
        }

        if (!(existingConsumer)) {
            insertSQL = "INSERT INTO Consumers ( "
                    + " Phone, "
                    + " FirstName, "
                    + " LastName, "
                    + " DeleteFlag, "
                    + " CreateDateTime, "
                    + " UpdateDateTime "
                    + " ) "
                    + " VALUES "
                    + " ("
                    + tempConsumer.getPhone() + ", "
                    + " \"" + tempConsumer.getFirstName() + "\", "
                    + " \"" + tempConsumer.getLastName() + "\", "
                    + "'N',"
                    + " \"" + datetime.toString() + "\", "
                    + " \"" + datetime.toString() + "\" "
                    + " ); ";
        } else {
            insertSQL = "UPDATE Consumers "
                    + " SET DeleteFlag = 'N', "
                    + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                    + " WHERE Phone = " + inConsumerPhone + "; ";
        }

        try {
            if (!(insertToDatabaseStatus) && !insertSQL.isEmpty()) {
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
                    insertToDatabaseStatus = true;
                    return insertToDatabaseStatus;
                }
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return insertToDatabaseStatus;
    } // END of insertToDatabase

    /**
     * @param inConsumer -- If "" (empty), Returns false
     * @return Boolean - "True" Valid Consumer exists and able to update the record at database level.
     * otherwise "False"
     */
    public static Boolean updateToDatabase(Consumer inConsumer) {

        String queryString = "", whereConditions = "", queryUpdate = "";
        DataOperations dataOperations = new DataOperations();

        if ((inConsumer.getPhone() == 0) || !(DataOperations.IsValidPhone(inConsumer.getPhone().toString()))) {
            return Boolean.FALSE;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            queryUpdate = " UPDATE Consumers SET c.FistName = '" + inConsumer.getFirstName() + "', "
                    + " c.LastName '" + inConsumer.getLastName() + "', "
                    + " c.Note1 '" + inConsumer.getNote1() + "' "
                    + " WHERE c.Phone = '" + inConsumer.getPhone() + "'; ";
//                    + " '" + inConsumer + "', "
//                    + " WHERE c.DeleteFlag = 'Y' OR c.DeleteFlag IS NULL" ;

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
    } // END of updateToDatabase


    /**
     * @param inConsumerPhone -- If 0 or invalid phone number, Returns false
     * @return Boolean - "True" - Able to delete at database, otherwise "False".
     * INCOMPLETE INCOMPLETE INCOMPLETE INCOMPLETE
     */
    public static Boolean deleteToDatabase(Long inConsumerPhone) {

        boolean nomoreLocationConsumers = false;

        if (inConsumerPhone == 0) {
            return Boolean.FALSE;
        }

        if (!(ConsumerOperations.IsValidPhone(inConsumerPhone.toString()))) {
            return Boolean.FALSE;
        }

        String queryLocationConsumer = "SELECT lc.ConsumersPhone "
                + " FROM LocationConsumers as lc "
                + " WHERE lc.ConsumersPhone = " + inConsumerPhone + " "
                + " AND RegisteredFlag = 'Y' ;";

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();
            PreparedStatement pst = dbconnection.prepareStatement(queryLocationConsumer);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                nomoreLocationConsumers = false;
            } else {
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                nomoreLocationConsumers = true;
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        Date datetime = new Date();

        String deleteSQL = "UPDATE Consumers "
                + " SET DeleteFlag = 'Y', "
                + " UpdateDateTime = " + " \"" + datetime.toString() + "\" "
                + " WHERE Phone = " + inConsumerPhone + "; ";

        if (nomoreLocationConsumers) {
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
        }
        return Boolean.FALSE;
    }

    public static Consumer getAllConsumerData(Long inConsumerPhone) {
        Consumer outConsumer = new Consumer();
        return outConsumer;
    }

    /**
     * @param inNotifierPhone -- If "" (empty), Returns ALL Records for a given Consumer Phone Number
     * @param inConsumerPhone -- If "" (empty), Returns ALL Records for a given Notifier Phone Number
     *                        If both inNotifierPhone and inConsumerPhone null/empty (""), Returns EMPTY ArrayList.
     * @param notifierActive  - "" - Notifier Status not considered for query
     *                        - "Y" - (n.Status = 'N' OR n.Status IS NULL)
     *                        - "N" - (n.Status = 'Y')
     * @param consumerActive  - "" - Consumer DeleteFlag not considered for query
     *                        - "Y" - (c.DeleteFlag = 'N' OR c.DeleteFlag IS NULL)
     *                        - "N" - (c.DeleteFlag = 'Y')
     * @return ArrayList<String> - List of String Objects
     */
    public static ArrayList<String> getAllDatabaseNotifierConsumers(Long inNotifierPhone, Long inConsumerPhone, String notifierActive, String consumerActive) {

        ArrayList<String> outNotifierConsumerPhoneList = new ArrayList<String>();


        String queryString = "", whereAdditionalConditions = "", querySelect = "", queryOrderby = "", notfierFlag = "", consumerFlag = "";
        DataOperations dataOperations = new DataOperations();

        if ((inNotifierPhone == 0) && (inConsumerPhone == 0)) {
            return outNotifierConsumerPhoneList;
        } else if ((inNotifierPhone != 0) && (inConsumerPhone != 0)) {
            if (DataOperations.IsValidPhone(inNotifierPhone.toString()) && DataOperations.IsValidPhone(inConsumerPhone.toString())) {
                whereAdditionalConditions = " AND n.SIMCardPhone = " + inNotifierPhone + " AND  c.Phone = " + inConsumerPhone + " ";
            } else {
                return outNotifierConsumerPhoneList;
            }
        } else if (inNotifierPhone != 0) {
            if (DataOperations.IsValidPhone(inNotifierPhone.toString())) {
                whereAdditionalConditions = whereAdditionalConditions + " AND n.SIMCardPhone = " + inNotifierPhone + " ";
                queryOrderby = " lc.ConsumersPhone asc ";
            } else {
                return outNotifierConsumerPhoneList;
            }
        } else if (inConsumerPhone != 0) {
            if (DataOperations.IsValidPhone(inConsumerPhone.toString())) {
                whereAdditionalConditions = whereAdditionalConditions + " AND c.Phone = " + inConsumerPhone + " ";
                queryOrderby = " lc.SIMCardPhone asc ";
            } else {
                return outNotifierConsumerPhoneList;
            }
        } else {
            return outNotifierConsumerPhoneList;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            if (consumerActive == "Y" || consumerActive == "") {
                consumerFlag = " AND (c.DeleteFlag = 'N' OR c.DeleteFlag IS NULL) ";
            } else if (consumerActive == "N") {
                consumerFlag = " AND (c.DeleteFlag = 'Y') ";
            }

            if (notifierActive == "Y") {
                notfierFlag = " AND (n.Status = 'Y') ";
            } else if (notifierActive == "N" || notifierActive == "") {
                notfierFlag = " AND (n.Status = 'N' OR n.Status IS NULL) ";
            }

            querySelect = "SELECT DISTINCT(lc.ConsumersPhone)"
                    + " FROM Consumers as c, LocationConsumers as lc, Notifiers as n "
                    + " WHERE c.Phone = lc.ConsumersPhone " + consumerFlag
                    + " AND lc.RegisteredFlag = 'Y' "
                    + " AND lc.LocationZIPCODE = n.LocationZIPCODE " + notfierFlag
                    + " AND lc.LocationSeqNumber = n.LocationSeqNumber ";

            if (whereAdditionalConditions == null) {
                return outNotifierConsumerPhoneList;
            } else {
                queryString = querySelect + whereAdditionalConditions + " ORDER BY " + queryOrderby;
            }

            PreparedStatement pst = dbconnection.prepareStatement(queryString);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    outNotifierConsumerPhoneList.add(rs.getString("ConsumersPhone"));
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

        return outNotifierConsumerPhoneList;
    } // END of getAllDatabaseNotifierConsumers

    /**
     * @param inConsumerPhone   -- If 0 or invalid phone number, Returns false
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
     * @return ArrayList<Consumers> - List of Consumers Objects
     */
    public static ArrayList<Consumer> getAllActiveDatabaseConsumers() {

        ArrayList<Consumer> outConsumersList = new ArrayList<Consumer>();


        String querySelect = "";
        DataOperations dataOperations = new DataOperations();

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = " SELECT Phone, FirstName, LastName, eMailAddress, AdharCardNumber, SMS, MissedCall, AppMessage, eMail, Website, ReadyToPayFees, SIGNATURE, WaterOutletTAP, TotalFamilySize, NoOfHouseholdAdults, WorkHoursFrom, WorkHoursTo, WaterStorageUdergroundTank, WaterUGStorageCapacity, WaterStorageLandlevelTank, WaterLLStorageCapacity, WaterStorageOverheadTank, WaterOHStorageCapacity, RecordSMS, Title, Subtitle, DeleteFlag, Note1, Note2, Note3, CreateDateTime, UpdateDateTime "
                    + " FROM Consumers "
                    + " WHERE DeleteFlag IS NULL "
                    + " OR DeleteFlag = 'N' ;";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                do {
                    Consumer consumer = new Consumer();

                    consumer.setPhone(rs.getLong("Phone"));
                    consumer.setFirstName(rs.getString("FirstName"));
                    consumer.setLastName(rs.getString("LastName"));
                    consumer.seteMailAddress(rs.getString("eMailAddress"));
                    consumer.setAdharCardNumber(rs.getString("AdharCardNumber"));
                    consumer.setSMS(rs.getString("SMS"));
                    consumer.setMissedCall(rs.getString("MissedCall"));
                    consumer.setAppMessage(rs.getString("AppMessage"));
                    consumer.seteMail(rs.getString("eMail"));
                    consumer.setWebsite(rs.getString("Website"));
                    consumer.setReadyToPayFees(rs.getString("ReadyToPayFees"));
                    consumer.setSIGNATURE(rs.getString("SIGNATURE"));
                    consumer.setWaterOutletTAP(rs.getString("WaterOutletTAP"));
                    consumer.setTotalFamilySize(rs.getInt("TotalFamilySize"));
                    consumer.setNoOfHouseholdAdults(rs.getInt("NoOfHouseholdAdults"));
                    consumer.setWorkHoursFrom(rs.getString("WorkHoursFrom"));
                    consumer.setWorkHoursTo(rs.getString("WorkHoursTo"));
                    consumer.setWaterStorageUdergroundTank(rs.getString("WaterStorageUdergroundTank"));
                    consumer.setWaterUGStorageCapacity(rs.getString("WaterUGStorageCapacity"));
                    consumer.setWaterStorageLandlevelTank(rs.getString("WaterStorageLandlevelTank"));
                    consumer.setWaterLLStorageCapacity(rs.getString("WaterLLStorageCapacity"));
                    consumer.setWaterStorageOverheadTank(rs.getString("WaterStorageOverheadTank"));
                    consumer.setWaterOHStorageCapacity(rs.getString("WaterOHStorageCapacity"));
                    consumer.setRecordSMS(rs.getString("RecordSMS"));
                    consumer.setTitle(rs.getString("Title"));
                    consumer.setSubtitle(rs.getString("Subtitle"));
                    consumer.setDeleteFlag(rs.getString("DeleteFlag"));
                    consumer.setNote1(rs.getString("Note1"));
                    consumer.setNote2(rs.getString("Note2"));
                    consumer.setNote3(rs.getString("Note3"));
                    consumer.setCreateDateTime(rs.getString("CreateDateTime"));
                    consumer.setUpdateDateTime(rs.getString("UpdateDateTime"));

                    outConsumersList.add(consumer);
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

        return outConsumersList;
    } // END of getAllDatabaseNotifierConsumers


    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long Phone) {
        this.Phone = Phone;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public String getAdharCardNumber() {
        return AdharCardNumber;
    }

    public void setAdharCardNumber(String AdharCardNumber) {
        this.AdharCardNumber = AdharCardNumber;
    }

    public String getSMS() {
        return SMS;
    }

    public void setSMS(String SMS) {
        this.SMS = SMS;
    }

    public String getMissedCall() {
        return MissedCall;
    }

    public void setMissedCall(String MissedCall) {
        this.MissedCall = MissedCall;
    }

    public String getAppMessage() {
        return AppMessage;
    }

    public void setAppMessage(String AppMessage) {
        this.AppMessage = AppMessage;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String Website) {
        this.Website = Website;
    }

    public String getReadyToPayFees() {
        return ReadyToPayFees;
    }

    public void setReadyToPayFees(String ReadyToPayFees) {
        this.ReadyToPayFees = ReadyToPayFees;
    }

    public String getSIGNATURE() {
        return SIGNATURE;
    }

    public void setSIGNATURE(String SIGNATURE) {
        this.SIGNATURE = SIGNATURE;
    }

    public String getWaterOutletTAP() {
        return WaterOutletTAP;
    }

    public void setWaterOutletTAP(String WaterOutletTAP) {
        this.WaterOutletTAP = WaterOutletTAP;
    }

    public int getTotalFamilySize() {
        return TotalFamilySize;
    }

    public void setTotalFamilySize(int TotalFamilySize) {
        this.TotalFamilySize = TotalFamilySize;
    }

    public int getNoOfHouseholdAdults() {
        return NoOfHouseholdAdults;
    }

    public void setNoOfHouseholdAdults(int NoOfHouseholdAdults) {
        this.NoOfHouseholdAdults = NoOfHouseholdAdults;
    }

    public String getWorkHoursFrom() {
        return WorkHoursFrom;
    }

    public void setWorkHoursFrom(String WorkHoursFrom) {
        this.WorkHoursFrom = WorkHoursFrom;
    }

    public String getWorkHoursTo() {
        return WorkHoursTo;
    }

    public void setWorkHoursTo(String WorkHoursTo) {
        this.WorkHoursTo = WorkHoursTo;
    }

    public String getWaterStorageUdergroundTank() {
        return WaterStorageUdergroundTank;
    }

    public void setWaterStorageUdergroundTank(String WaterStorageUdergroundTank) {
        this.WaterStorageUdergroundTank = WaterStorageUdergroundTank;
    }

    public String getWaterUGStorageCapacity() {
        return WaterUGStorageCapacity;
    }

    public void setWaterUGStorageCapacity(String WaterUGStorageCapacity) {
        this.WaterUGStorageCapacity = WaterUGStorageCapacity;
    }

    public String getWaterStorageLandlevelTank() {
        return WaterStorageLandlevelTank;
    }

    public void setWaterStorageLandlevelTank(String WaterStorageLandlevelTank) {
        this.WaterStorageLandlevelTank = WaterStorageLandlevelTank;
    }

    public String getWaterLLStorageCapacity() {
        return WaterLLStorageCapacity;
    }

    public void setWaterLLStorageCapacity(String WaterLLStorageCapacity) {
        this.WaterLLStorageCapacity = WaterLLStorageCapacity;
    }

    public String getWaterStorageOverheadTank() {
        return WaterStorageOverheadTank;
    }

    public void setWaterStorageOverheadTank(String WaterStorageOverheadTank) {
        this.WaterStorageOverheadTank = WaterStorageOverheadTank;
    }

    public String getWaterOHStorageCapacity() {
        return WaterOHStorageCapacity;
    }

    public void setWaterOHStorageCapacity(String WaterOHStorageCapacity) {
        this.WaterOHStorageCapacity = WaterOHStorageCapacity;
    }

    public String getRecordSMS() {
        return RecordSMS;
    }

    public void setRecordSMS(String RecordSMS) {
        this.RecordSMS = RecordSMS;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String Subtitle) {
        this.Subtitle = Subtitle;
    }

    public String getDeleteFlag() {
        return DeleteFlag;
    }

    public void setDeleteFlag(String DeleteFlag) {
        this.DeleteFlag = DeleteFlag;
    }

    public String getNote1() {
        return Note1;
    }

    public void setNote1(String Note1) {
        this.Note1 = Note1;
    }

    public String getNote2() {
        return Note2;
    }

    public void setNote2(String Note2) {
        this.Note2 = Note2;
    }

    public String getNote3() {
        return Note3;
    }

    public void setNote3(String Note3) {
        this.Note3 = Note3;
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
