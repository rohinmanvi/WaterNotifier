import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.waternotifier.waternotifierlibrary.*;
import com.waternotifier.waternotifierlibrary.Location;
import com.waternotifier.waternotifierlibrary.DatabaseHelper;


public class DatabaseClass {
    
    public static ArrayList<String> getConsumers(String phonenumb, String callerphone) {
        if (phonenumb == null || callerphone == null)
            return new ArrayList<>();
        phonenumb = phonenumb.replaceAll("\\s+", "");
        callerphone = callerphone.replaceAll("\\s+", "");
        ArrayList<Long> locationConsumersArrayList = new ArrayList<>();
        ArrayList<String> LCA = new ArrayList<>();
        if (!checkNumber(phonenumb) || !checkNumber(callerphone))
            return new ArrayList<>();
        if (phonenumb.length() == 11)
            phonenumb = phonenumb.substring(1);
        if (phonenumb.length() == 12)
            phonenumb = phonenumb.substring(2);
        if (callerphone.length() == 11)
            callerphone = callerphone.substring(1);
        if (callerphone.length() == 12)
            callerphone = callerphone.substring(2);
        try {
            Long inNotifierPhone = Long.parseLong(phonenumb, 10);
            Location tempLoc = new Location();
            tempLoc = Location.getNotifierLocationZIPCODESeqNumber(inNotifierPhone);
            ArrayList<ConsumerCallers> listOfConsumerCallers = new ArrayList<ConsumerCallers>();
            if (tempLoc == null) {
            }
            listOfConsumerCallers = ConsumerCallers.getAll(tempLoc.getZIPCODE(), tempLoc.getSeqNumber());
            if (listOfConsumerCallers.isEmpty() || listOfConsumerCallers == null || listOfConsumerCallers.size() == 0) {
            } else {
                for (int i = 0; i < listOfConsumerCallers.size(); i++) {
                    if (listOfConsumerCallers.get(i).getSIMCardPhone().toString().equals(callerphone)) {
                        locationConsumersArrayList = LocationConsumers.getAllConsumerPhone(
                                listOfConsumerCallers.get(i).getSIMCardPhone(), tempLoc.getZIPCODE(),
                                tempLoc.getSeqNumber());
                        for (Long a : locationConsumersArrayList)
                            LCA.add(a.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return new ArrayList<>();
        }
        return LCA;
    }
    
    public static ArrayList<String> getConsumerCallers() {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<Long> b = ConsumerCallers.getAllUniquePhoneNumbers();
        for (Long c : b)
            a.add(c.toString());
        return a;
    }
    
    public static ArrayList<String> getNotifierNumbers() {
        ArrayList<String> a = new ArrayList<>();
        ArrayList<Notifier> b = Notifier.getAll();
        for (Notifier c : b)
            a.add(c.getSIMCardPhone().toString());
        return a;
    }
    
    public static String getCallerOfConsumer(String a) {
        if (a.length() == 11)
            a = a.substring(1);
        if (a.length() == 12)
            a = a.substring(2);
        ArrayList<String> callers = getConsumerCallers();
        ArrayList<String> devices = getNotifierNumbers();
        for (int i = 0; i < callers.size(); i++) {
            for (int k = 0; k < devices.size(); k++) {
                ArrayList<String> consumers = getConsumers(devices.get(k), callers.get(i));
                for (int j = 0; j < consumers.size(); j++) {
                    if (consumers.get(j).equals(a))
                        return callers.get(i);
                }
            }
        }
        return "";
    }
    
    public static String getNotifierLocation(String locationNum) {
        if (locationNum == null)
            return "";
        locationNum = locationNum.replaceAll("\\s+", "");
        String a = "";
        if (!checkNumber(locationNum))
            return "";
        try {
            a = Location.getLocationNameByLocationCode(Integer.parseInt(locationNum));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return "";
        }
        return a;
    }
    
    public static void madeCall(String phonenumb) {
        if (phonenumb != null) {
            phonenumb = phonenumb.replaceAll("\\s+", "");
            if (checkNumber(phonenumb)) {
                if (phonenumb.length() == 11)
                    phonenumb = phonenumb.substring(1);
                if (phonenumb.length() == 12)
                    phonenumb = phonenumb.substring(2);
                try {
                    System.out.println("Will not call this number for another hour: " + Long.valueOf(phonenumb));
                    LocationConsumers.updateLastWaterNotificationCall(Long.valueOf(phonenumb));
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("CONTINUING");
                }
            }
        }
    }
    
    public static boolean deviceExists(String phonenumb) {
        if (phonenumb == null)
            return false;
        phonenumb = phonenumb.replaceAll("\\s+", "");
        boolean a;
        if (!checkNumber(phonenumb))
            return false;
        if (phonenumb.length() == 11)
            phonenumb = phonenumb.substring(1);
        if (phonenumb.length() == 12)
            phonenumb = phonenumb.substring(2);
        try {
            a = Notifier.notifierExists(Long.parseLong(phonenumb, 10));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return false;
        }
        return a;
    }
    
    public static boolean newConsumer(String phonenumb, String locationNum, String name) {
        if (phonenumb == null || locationNum == null || name == null)
            return false;
        phonenumb = phonenumb.replaceAll("\\s+", "");
        locationNum = locationNum.replaceAll("\\s+", "");
        String repname = name.replaceAll("\\s+", "");
        name = name.replace('\n', ' ');
        boolean x;
        if (!checkNumber(phonenumb) || !checkNumber(locationNum))
            return false;
        if (!checkLetters(repname))
            return false;
        if (phonenumb.length() == 11)
            phonenumb = phonenumb.substring(1);
        if (phonenumb.length() == 12)
            phonenumb = phonenumb.substring(2);
        try {
            long b = Long.parseLong(phonenumb, 10);
            int a = Integer.parseInt(locationNum);
            x = DatabaseHelper.newConsumer(b, a, name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return false;
        }
        return x;
    }
    
    public static boolean newCallLog(String sender, String receiver) {
        if (sender == null || receiver == null)
            return false;
        sender = sender.replaceAll("\\s+", "");
        receiver = receiver.replaceAll("\\s+", "");
        if (!checkNumber(sender) || !checkNumber(receiver))
            return false;
        boolean x;
        if (sender.length() == 11)
            sender = sender.substring(1);
        if (sender.length() == 12)
            sender = sender.substring(2);
        if (receiver.length() == 11)
            receiver = receiver.substring(1);
        if (receiver.length() == 12)
            receiver = receiver.substring(2);
        try {
            x = VoiceCallDetails.insertToDatabase(Long.parseLong(sender, 10), Long.parseLong(receiver, 10));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return false;
        }
        return x;
    }
    
    public static boolean newMessageLog(String sender, String receiver, String message) {
        if (sender == null || receiver == null || message == null)
            return false;
        sender = sender.replaceAll("\\s+", "");
        receiver = receiver.replaceAll("\\s+", "");
        if (!checkNumber(sender) || !checkNumber(receiver) || !checkASCII(message))
            return false;
        boolean x;
        if (sender.length() == 11)
            sender = sender.substring(1);
        if (sender.length() == 12)
            sender = sender.substring(2);
        if (receiver.length() == 11)
            receiver = receiver.substring(1);
        if (receiver.length() == 12)
            receiver = receiver.substring(2);
        try {
            x = SMSDetails.insertToDatabase(Long.parseLong(sender, 10), Long.parseLong(receiver, 10), message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            return false;
        }
        return x;
    }
    
    private static boolean checkNumber(String a) {
        if (a == null)
            return false;
        a = a.replaceAll("\\s+", "");
        return a.matches("\\d+");
    }
    
    private static boolean checkLetters(String a) {
        if (a == null)
            return false;
        a = a.replaceAll("\\s+", "");
        Pattern p = Pattern.compile("^[ A-Za-z]+$");
        Matcher m = p.matcher(a);
        return m.matches();
    }
    
    private static boolean checkASCII(String a) {
        if (a == null)
            return false;
        return a.matches("\\A\\p{ASCII}*\\z");
    }
}
