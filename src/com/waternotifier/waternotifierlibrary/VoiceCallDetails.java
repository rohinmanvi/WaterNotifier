package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class VoiceCallDetails {
    private int SenderPhone;
    private int ReceiverPhone;
    private String DateTime;
    private Long DateTimeMilliSeconds;

    /**
     * @param SenderPhone, ReceiverPhone -- If 0 or "" (empty), Returns false
     * @return Boolean - "True" Valid phone numbers and able insert the record at database level.
     * otherwise "False"
     */
    public static Boolean insertToDatabase(Long SenderPhone, Long ReceiverPhone) {

        if (!(ConsumerOperations.IsValidPhone(SenderPhone.toString()))) {
            return false;
        }

        if (!(ConsumerOperations.IsValidPhone(ReceiverPhone.toString()))) {
            return false;
        }

        boolean insertToDatabaseStatus = false;

        String insertSQL = "";

        Date datetime = new Date();
        Long milliseconds = datetime.getTime();

        insertSQL = "INSERT INTO VoiceCallDetails ( "
                + " SenderPhone, "
                + " ReceiverPhone, "
                + " DateTime, "
                + " DateTimeMilliSeconds "
                + " ) "
                + " VALUES "
                + " ("
                + SenderPhone + ", "
                + " " + ReceiverPhone + ", "
                + " \"" + datetime.toString() + "\", "
                + " " + milliseconds + " "
                + " ); ";

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


    public int getSenderPhone() {
        return SenderPhone;
    }

    public void setSenderPhone(int SenderPhone) {
        this.SenderPhone = SenderPhone;
    }

    public int getReceiverPhone() {
        return ReceiverPhone;
    }

    public void setReceiverPhone(int ReceiverPhone) {
        this.ReceiverPhone = ReceiverPhone;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
    }

    public Long getDateTimeMilliSeconds() {
        return DateTimeMilliSeconds;
    }

    public void setDateTimeMilliSeconds(Long DateTimeMilliSeconds) {
        this.DateTimeMilliSeconds = DateTimeMilliSeconds;
    }


}
