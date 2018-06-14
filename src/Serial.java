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

public class Serial {
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
                for (int a = 0; a < people.size(); a++) {
                    String mess = people.get(a).getMessage();
//                String phone = people.get(a).getPhoneNumber();
                    boolean callormess = (mess.indexOf('(') > -1 && mess.indexOf(')') > -1 && mess.indexOf('(') < mess.lastIndexOf(')'));
                    System.out.println("Call or message: " + callormess);
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
                            System.out.println(three.get(i) + ": messages assigned " + partPhone);
                            ArrayList<Message> messages = new ArrayList<>();
                            for (String x : partPhone)
                                messages.add(new Message(x, message));
                            three.get(i).setMessages(messages);
                        } else {
                            System.out.println(three.get(i) + ": call assigned " + partPhone);
                            three.get(i).setPhoneNumbers(partPhone);
                        }
                    }
                }
                System.out.println("devices found: " + devices);
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
                        System.out.println(three.get(i) + ": assigned " + partPhone);
                        three.get(i).setPhoneNumbers(partPhone);
                    }
                }
                for (MissedCalls b : three) {
                    function(b, 1);
                }
                for (MissedCalls b : three) {
                    waitFor(b);
                }
                System.out.println("END OF LOOP");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("CONTINUING");
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
