import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import arduino.*;
import com.waternotifier.waternotifierlibrary.ConsumerCallers;
import com.waternotifier.waternotifierlibrary.Location;
import com.waternotifier.waternotifierlibrary.LocationConsumers;
import com.waternotifier.waternotifierlibrary.LogToFile;

public class Serial {
//    private static String receiveCalls = "8277816186";
    private static String receiveCalls = "7472558416";
    private static String CC = "91";
    private static String CC2 = "1";
    private static ArrayList<String> USnumbers = new ArrayList<>();

    public static void main(String[] args) {
        USnumbers.add("8189135171");
        USnumbers.add("8189678112");
        USnumbers.add("8189570369");

        WaitForCalls two = new WaitForCalls(20, receiveCalls, "Incoming Calls GSM", 0);
        waitFor(two);
        function(two, 1);

        ArrayList<MissedCalls> three = new ArrayList<>();
        ArrayList<String> callerPhone = DatabaseClass.getConsumerCallers();
        for (int i = 0; i < callerPhone.size(); i++) {
            three.add(new MissedCalls(callerPhone.get(i), "MissedCalls " + (i + 1), 0));
            waitFor(three.get(i));
        }

        while (true) {
            try {
                waitFor(two);
                ArrayList<String> devices = two.getNumbersCall();
                function(two, 1);
                ArrayList<Message> people = two.getReceiveMessages();
//                for(MissedCalls b : three) {
//                    b.setMessages(new ArrayList<Message>());
//                    b.setPhoneNumbers(new ArrayList<String>());
//                }
                ArrayList<Message> send = two.getSendMessages();
                for(int i = 0; i < send.size(); i++){
                    boolean v = false;
                    String devicenumber = DatabaseClass.getCallerOfConsumer(send.get(i).getPhoneNumber());
                    for(MissedCalls a : three){
                        if(a.getPhoneNumber().equals(devicenumber)) {
                            a.addMessage(send.get(i));
                            v = true;
                        }
                    }
                    if(!v)
                        three.get(0).addMessage(send.get(i));
                }
                for(int i = 0; i < send.size(); i++){
                    two.removeMessage(send.get(i));
                }
                for (int a = 0; a < people.size(); a++) {
                    String mess = people.get(a).getMessage();
                    boolean callormess = (mess.indexOf('(') > -1 && mess.indexOf(')') > -1 && mess.indexOf('(') < mess.lastIndexOf(')'));
                    LogToFile.log("info","Call or message: " + callormess);
                    String number = "";
                    String message = "";
                    if (callormess) {
                        message = mess.substring(mess.indexOf('(') + 1, mess.lastIndexOf(')'));
                        number = mess.substring(0, mess.indexOf('('));
                    } else
                        number = mess;
                    two.removeMessage(people.get(a));
                    for (int i = 0; i < three.size(); i++) {
                        ArrayList<String> partPhone = DatabaseClass.getConsumers(number, three.get(i).getPhoneNumber());
                        for (int j = 0; j < partPhone.size(); j++) {
                            boolean b = true;
                            for (String c : USnumbers) {
                                if (c.equals(partPhone.get(j))) {
                                    b = false;
                                    break;
                                }
                            }
                            if (b)
                                partPhone.set(j, CC + partPhone.get(j));
                            else
                                partPhone.set(j, CC2 + partPhone.get(j));
                        }
                        if (callormess) {
                            LogToFile.log("info",three.get(i) + ": messages assigned " + partPhone);
                            ArrayList<Message> messages = new ArrayList<>();
                            for (String x : partPhone)
                                messages.add(new Message(x, message));
                            three.get(i).addMessages(messages);
                        } else {
                            LogToFile.log("info",three.get(i) + ": call assigned " + partPhone);
                            three.get(i).addPhoneNumbers(partPhone);
                        }
                    }
                }
                LogToFile.log("info","devices found: " + devices);
                for (int a = 0; a < devices.size(); a++) {
                    String x = "";
                    if (devices.get(a).length() == 11)
                        x = devices.get(a).substring(1);
                    if (devices.get(a).length() == 12)
                        x = devices.get(a).substring(2);
                    two.removeNumberCall(devices.get(a));
                    for (int i = 0; i < three.size(); i++) {
                        ArrayList<String> partPhone = DatabaseClass.getConsumers(x, three.get(i).getPhoneNumber());
                        for (int j = 0; j < partPhone.size(); j++) {
                            boolean b = true;
                            for (String c : USnumbers) {
                                if (c.equals(partPhone.get(j))) {
                                    b = false;
                                    break;
                                }
                            }
                            if (b)
                                partPhone.set(j, CC + partPhone.get(j));
                            else
                                partPhone.set(j, CC2 + partPhone.get(j));
                        }
                        LogToFile.log("info",three.get(i) + ": call assigned " + partPhone);
                        three.get(i).addPhoneNumbers(partPhone);
                    }
                }
                for (MissedCalls b : three) {
                    function(b, 1);
                }
//                for (MissedCalls b : three) {
//                    waitFor(b);
//                }
                LogToFile.log("info","END OF LOOP");
            } catch (Exception e) {
                e.printStackTrace();
                LogToFile.log("info","CONTINUING: major");
            }
        }
    }

    private static void function(GSMs b, int a) {
        synchronized (b) {
            b.setFunction(a);
        }
    }

    private static void waitFor(GSMs b) {
        synchronized (b) {
            try {
                b.wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void delay(int s) {
        try {
            TimeUnit.MILLISECONDS.sleep(s);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
        }
    }
}
