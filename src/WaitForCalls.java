import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WaitForCalls extends GSMs {

    private ArrayList<String> phoneNumbersCall = new ArrayList<>();
    private ArrayList<Message> sendMessages = new ArrayList<>();
    private ArrayList<Message> receiveMessages = new ArrayList<>();
    private String phoneNumber = "";
    private String message = "";
    private String testingPhone = "18189135171";

    public WaitForCalls(int comports, String phone, String name, int fun) {
        super(comports, phone, name, fun);
    }

    public WaitForCalls(String phone, String name, int fun) {
        super(phone, name, fun);
    }

    public void running() {
        System.out.println(threadName + ": waitForCalls");
        try {
            while (true) {
                if (function == -1) {
                    closeConnection();
                    System.out.println(threadName + " exiting.");
                    notif();
                    break;
                }
                if (function == 0) {
                    delay(100);
                    notif();
                }
                if (function == 1) {
                    try {
                        startCommands();
                        checkService();
                    }catch(Exception e){
                        e.printStackTrace();
                        System.out.println("CONTINUING");
                    }
                    boolean v = false;
                    for (int i = 1; i <= 100; i++) {
                        String mess = "";
                        mess = checkMessage(i);
                        if (mess.isEmpty() || mess == null) {
                            break;
                        }
                        System.out.println(threadName + ": Message Received: \n" + mess);
                        if (mess.length() > 3 && (super.phoneNum().length() == 11 || super.phoneNum().length() == 12)
                                && mess.indexOf(',') > -1 && mess.indexOf('!') > -1) {
                            message = mess;
                            String locationNum = message.substring(message.indexOf('!') + 1, message.indexOf(','));
                            String name = message.substring(message.indexOf(',') + 1);
                            name = name.replaceAll("\\s+", "");
                            locationNum = locationNum.replaceAll("\\s+", "");
                            phoneNumber = super.phoneNum();
                            phoneNumber = phoneNumber.replaceAll("\\s+", "");
                            DatabaseClass.newMessageLog(phoneNumber, getPhoneNumber(), message);
                            if (DatabaseClass.newConsumer(phoneNumber, locationNum, name)) {
                                System.out.println(phoneNumber + " " + locationNum + " " + name);
                                sendMessages.add(new Message(phoneNumber, phoneNumber + " has been subscribed to the "
                                        + DatabaseClass.notifierLocation(locationNum) + " notifier"));
                            }
                        }
                        if (mess.length() > 4 && (super.phoneNum().length() == 11 || super.phoneNum().length() == 12)
                                && mess.indexOf('(') > -1 && mess.indexOf(')') > -1 && mess.indexOf('(') < mess.lastIndexOf(')') && mess.indexOf("WN") > -1) {
                            message = mess;
                            String mes = message.substring(message.indexOf('(') + 1, message.lastIndexOf(')'));
                            mes = mes.replace('\n', ' ');
                            phoneNumber = super.phoneNum();
                            phoneNumber = phoneNumber.replaceAll("\\s+", "");
                            DatabaseClass.newMessageLog(phoneNumber, getPhoneNumber(), message);
                            System.out.println("RECEIVED MESSAGE: " + mes);
                            receiveMessages.add(new Message(phoneNumber, mes));
                            v = true;
                        }
                        delay(2000);
                    }
                    if(v)
                        function = 0;
                    deleteMessages();
                    String a = waitForCall(0);
                    if (!a.isEmpty()) {
                        DatabaseClass.newCallLog(a, getPhoneNumber());
                        System.out.println(threadName + ": got a call = " + a);
                        if (DatabaseClass.deviceExists(a)) {
                            phoneNumbersCall.add(a);
                            function = 0;
                        }
                    }
                }
                if (function == 2) {
                    System.out.println(threadName + ": waitForCalls");
                    String a = waitForCall();
                    DatabaseClass.newCallLog(a, getPhoneNumber());
                    System.out.println(threadName + ": got a call = " + a);
                    if (DatabaseClass.deviceExists(a))
                        phoneNumbersCall.add(a);
                    function = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            if (checkConnection()) {
                System.out.println("RECONNECTING");
                run();
            } else {
                closeConnection();
                COMStart(20);
                run();
            }
        }
    }


    public synchronized ArrayList<String> getNumbersCall() {
        return phoneNumbersCall;
    }

    public synchronized ArrayList<Message> getReceiveMessages() {
        return receiveMessages;
    }

    public synchronized ArrayList<Message> getSendMessages() {
        return sendMessages;
    }

    public synchronized void removeNumberCall(String phoneNumber) {
        for (int i = 0; i < phoneNumbersCall.size(); i++)
            if (phoneNumber.equals(phoneNumbersCall.get(i)))
                phoneNumbersCall.remove(i);
    }

    public synchronized void removeMessage(Message mess) {
        for (int i = 0; i < sendMessages.size(); i++)
            if (mess.equals(sendMessages.get(i)))
                sendMessages.remove(i);
        for (int i = 0; i < receiveMessages.size(); i++)
            if (mess.equals(receiveMessages.get(i)))
                receiveMessages.remove(i);
    }

}
