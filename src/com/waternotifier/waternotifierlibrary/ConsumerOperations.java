package com.waternotifier.waternotifierlibrary;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by rajeshmanvi on 10/13/2017.
 */
public class ConsumerOperations {

	public static boolean IsValidPhone(String phone) {
        String copTitle = "Phone Number";
        String onValidatorMessage = "";
        // Enter 10-Digit Phone Number - Make sure while using for SMS - Add COUNTRY code in the beginning of 10-Digit Phone
        // Otherwise SMS will not be sent.
        if (phone.matches("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
            return true;
        }
        System.out.println("Enter valid 10-Digit Phone Number " + phone + " !");
        onValidatorMessage = "Enter valid 10-Digit.";
//        Toast.makeText(context, onValidatorMessage, Toast.LENGTH_LONG).show();
//        ShowPopupMessage(copTitle, onValidatorMessage);
//        Toast.makeText(context, "Enter valid 10-Digit Phone Number.", Toast.LENGTH_LONG).show();
        return false;
    }


    public static boolean IsValidPhoneNoPopupMessage(String phone) {
        // Enter 10-Digit Phone Number - Make sure while using for SMS - Add COUNTRY code in the beginning of 10-Digit Phone
        // Otherwise SMS will not be sent.
        if (phone.matches("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
            return true;
        }
        System.out.println("Enter valid 10-Digit Phone Number " + phone + " !");
        return false;
    }


    public boolean IsValidIMEI(long IMEI) {
        String copTitle = "IMEI Number";
        String onValidatorMessage = "";
        //Minmum 15-Digit

//        Convert long to String for regular expressions
        String inputIMEI = String.valueOf(IMEI);
        if (inputIMEI.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
            return true;
        }
        System.out.println("Enter valid 15-Digit IMEI Number " + IMEI + " !");
        onValidatorMessage = "Enter valid 15-Digit.";
//        Toast.makeText(context, onValidatorMessage, Toast.LENGTH_LONG).show();
//        ShowPopupMessage(copTitle, onValidatorMessage, context);
//        Toast.makeText(context, "Enter valid 15-Digit IMEI Number.", Toast.LENGTH_LONG).show();
        return false;
    }

    public boolean IsValidICCID(String ICCID) {
        String copTitle = "SIM/CCID Card Number";
        String onValidatorMessage = "";
        //Minmum 19-Digit, it can be of 20-Digit based on service provider.
        if (ICCID.length() == 19) {
            if (ICCID.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
                return true;
            }
        } else if (ICCID.length() == 20) {
            if (ICCID.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]")) {
                return true;
            }
        }
        System.out.println("Enter valid 19-Digit or 20-Digit  " + ICCID + " ICCID/SIM Card Number!");
        onValidatorMessage = "Enter valid 19-Digit or 20-Digit.";
//        Toast.makeText(context, onValidatorMessage, Toast.LENGTH_LONG).show();
//        ShowPopupMessage(copTitle, onValidatorMessage, context);
//        Toast.makeText(context, "Enter valid 19-Digit or 20-Digit ICCID/SIM Card Number.", Toast.LENGTH_LONG).show();
        return false;
    }

    public static String ConvertMilliSecondsToFormattedDateUptoMilliSec(String milliSeconds) {
//        String dateFormat = "dd-MM-yyyy hh:mm:ss";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        String dateFormat = "dd MMMM yyyy hh:mm:ss:SSS zzzz";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milliSeconds));
        return simpleDateFormat.format(calendar.getTime());
    }

    //
//    public void sendBulkSMS(Context applicationContext) {
//        String inPhone = "8189135171";
//        String receiverPhone = inPhone;
//        String senderPhone = "";
//        DatabaseHelper myDb;
//
//        for (int i = 0; i < 3; i++) {
//            String tempSMSMessage = "Hello there! Message# " + i;
//            String confMsg = "Message# " + i + " Sent to : " + inPhone + " . ";
//            String defaultSMS = "Welcome to Water Notifier! BULK SMS TEST. ";
//            String copTitle = "SMS Response";
//
//            SmsManager smsmanager = SmsManager.getDefault();
//            smsmanager.sendTextMessage(inPhone, null, defaultSMS + tempSMSMessage, null, null);
//
//            TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
////            senderPhone = telephonyManager.getLine1Number();
//            try {
//                senderPhone = telephonyManager.getLine1Number();
//            } catch (NullPointerException ex) {
//            }
//
//            if (senderPhone.equals("")) {
//                senderPhone = telephonyManager.getSubscriberId();
////                senderPhone = "9686095659";
//            }
//
//            myDb = new DatabaseHelper(getApplicationContext());
//            ConsumerDetails consumerDetails = myDb.getConsumerDetails(inPhone);
//
//            if (myDb.insertSMSMSG(senderPhone, receiverPhone, defaultSMS + tempSMSMessage)) {
//            // Toast.makeText(getApplicationContext(), "Sent SMS Message Recorded", Toast.LENGTH_LONG).show();
//            }
//            Intent intent = new Intent(Intent.ACTION_CALL);
//            intent.setAction(inPhone);
//
//        // Toast.makeText(getApplicationContext(), confMsg, Toast.LENGTH_SHORT).show();
//        }  // END of FOR Loop
//
//    }
//
//    public String convertDateTimeToRedableDateTime(String inputDate, String inputIMEI, Context context) {
//
//        String outputDate = inputDate;
//        SimpleDateFormat countrySpecificDateFormat;
//        DatabaseHelper MyDb = new DatabaseHelper(context);
//        NotifierDeviceDetails notifierDeviceDetails = new NotifierDeviceDetails();
//        notifierDeviceDetails = MyDb.getNotifierDeviceDetailsFiltered("IMEI", inputIMEI);
//
//        String country = notifierDeviceDetails.getLocationCountry();
//
//        switch (country) {
//            case "INDIA":
//            case "India":
//            case "india":
//                countrySpecificDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
//                break;
//            case "USA":
//            case "usa":
//            case "US":
//            case "Usa":
//                countrySpecificDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm zzz");
//                break;
//            default:
//                countrySpecificDateFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm zzz");
//        }
//
//
////        String target = "Thu Sep 28 20:29:30 JST 2000";
////        String target2 = "Sat Dec 16 19:09:50 PST 2017";
////        DateFormat df = DateFormat.getDateInstance();
//        DateFormat df = new SimpleDateFormat("EEE MMM dd kk:mm:ss zzz yyyy");
//
//        try {
//            Date dateConverted = df.parse(inputDate);
//            outputDate = countrySpecificDateFormat.format(dateConverted);
//        } catch (ParseException e) {
//            System.out.println("Unable to parse " + inputDate);
//        }
//        return outputDate;
//    }
//

    /**
     * This method makes a "deep clone" of any object it is given.
     */
    public static Object deepClone(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
