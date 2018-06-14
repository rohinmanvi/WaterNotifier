/**
 *
 */
package com.waternotifier.waternotifierlibrary;

import java.util.ArrayList;

/**
 * @author rajeshmanvi
 */


public class DatabaseHelper {

    /**
     *
     */

    /**
     * * All CRUD(Create, Read, Update, Delete) Operations  -- Consumer
     */

    /**
     * @param inConsumerPhone :: Consumer phone number to be inserted as NEW
     * @param inLocationCode  :: ZIPCODE and Serial (Valve) number the user is trying to register at.
     * @param inFullName      :: FirstName LastName
     * @return ::   true - if insert is successful, or user already exists and associated to the given location.
     */
    public static boolean newConsumer(Long inConsumerPhone, int inLocationCode, String inFullName) {

        boolean newConsumerStatus = false,
                insertConsumerStatus = false,
                insertConsumerLocationStatus = false,
                assignConsumerCallerPhone = false;

        insertConsumerStatus = Consumer.insertToDatabase(inConsumerPhone, inFullName);

        insertConsumerLocationStatus = LocationConsumers.insertToDatabase(inConsumerPhone, inLocationCode, inFullName);

        assignConsumerCallerPhone = ConsumerCallers.assignPhone(inConsumerPhone, inLocationCode);

        if (insertConsumerStatus && insertConsumerLocationStatus && assignConsumerCallerPhone) {
            newConsumerStatus = true;
        }

        return newConsumerStatus;
    }


    public static void  updateAllConsumersForCallerPhone() {

        ArrayList<LocationConsumers> listLocationConsumers = new ArrayList<>();

        listLocationConsumers = LocationConsumers.getAllActiveDatabaseLocationConsumersWithNoConsumerCaller();

        String locationCode = "";
        if (!(listLocationConsumers.isEmpty() || listLocationConsumers.size() == 0)) {
            for (int i = 0; i < listLocationConsumers.size(); i++) {
                locationCode = "" + listLocationConsumers.get(i).getLocationZIPCODE() + listLocationConsumers.get(i).getLocationSeqNumber();
                if (ConsumerCallers.assignPhone(listLocationConsumers.get(i).getConsumersPhone(), Integer.valueOf(locationCode))) {
                    System.out.println("Successfully updated user " + listLocationConsumers.get(i).getConsumersPhone() + " with LocationCode " + locationCode + " for ConsumerCallerPhone! " + '\n');
                }
            }
        }

        ConsumerCallers.updateConsumerCallerCount();
        Location.updateLocationConsumerCount();
    }

    public static boolean deleteConsumer(Long inConsumerPhone, int inLocationCode) {

        boolean newConsumerStatus = false,
                deleteConsumerStatus = false,
                deleteConsumerLocationStatus = false;

        deleteConsumerLocationStatus = LocationConsumers.deleteToDatabase(inConsumerPhone, inLocationCode);

        deleteConsumerStatus = Consumer.deleteToDatabase(inConsumerPhone);

        if (deleteConsumerStatus && deleteConsumerLocationStatus) {
            newConsumerStatus = true;
        }

        return newConsumerStatus;
    }


    public static boolean deleteConsumer(Long inConsumerPhone) {

        boolean newConsumerStatus = false,
                deleteConsumerStatus = false,
                deleteConsumerLocationStatus = false;

        deleteConsumerLocationStatus = LocationConsumers.deleteToDatabase(inConsumerPhone);

        deleteConsumerStatus = Consumer.deleteToDatabase(inConsumerPhone);

        if (deleteConsumerStatus && deleteConsumerLocationStatus) {
            newConsumerStatus = true;
        }

        return newConsumerStatus;
    }


//    public static boolean newConsumer(Long inConsumerPhone, Long inNotifierPhone, String inFullName) {
//
//        boolean newConsumerStatus = false,
//                insertConsumerStatus = false,
//                insertConsumerLocationStatus = false;
//
//        insertConsumerStatus = Consumer.insertToDatabase(inConsumerPhone, inFullName);
//
////        insertConsumerLocationStatus = LocationConsumers.insertToDatabase(inConsumerPhone, inNotifierPhone, inFullName);
//        insertConsumerLocationStatus = LocationConsumers.insertToDatabase(inConsumerPhone, inNotifierPhone, inFullName);
//
//        if (insertConsumerStatus && insertConsumerLocationStatus) {
//            newConsumerStatus = true;
//        }
//
//        return newConsumerStatus;
//    }

}
