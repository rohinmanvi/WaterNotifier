import com.waternotifier.waternotifierlibrary.LogToFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
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
        LogToFile.log("info", threadName + ": waitForCalls");
        int counting = 0;
        try {
            while (true) {
//                counting++;
//                if (counting == 5000) {
//                    counting = 0;
//                    completeReset();
//                }
                if (function == -1) {
                    closeConnection();
                    LogToFile.log("info", threadName + " exiting.");
                    notif();
                    break;
                }
                if (function == 0) {
                    delay(100);
                    notif();
                }
                if (function == 1) {
                    try {
                        try {
//                            int counts = 0;
//                            while (!checkService()) {
//                                if (counts > 5) {
//                                    completeReset();
//                                    counts = 0;
//                                }
//                                counts++;
//                                LogToFile.log("info", threadName + ": trying to get service");
//                                delay(5000);
//                            }
                            send("AT+CMGF=1");
                            if (!checkService()) {
                                startCommands();
                                if (!ownPhoneNumber())
                                    startCommands();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogToFile.log("info", "CONTINUING");
                        }
                        phoneNumber = "";
                        message = "";
                        boolean v = false;
                        for (int i = 1; i <= 100; i++) {
                            String mess = "";
                            mess = checkMessage(i);
                            if (mess.isEmpty() || mess == null) {
                                if (i > 1)
                                    deleteMessages();
                                break;
                            }
                            LogToFile.log("info", threadName + ": Message Received: \n" + mess);
                            if (mess.length() > 3 && (super.phoneNum().length() == 11 || super.phoneNum().length() == 12)
                                    && mess.indexOf(',') > -1 && mess.indexOf('!') > -1) {
                                message = mess;
                                String locationNum = message.substring(message.indexOf('!') + 1, message.indexOf(','));
                                String name = message.substring(message.indexOf(',') + 1);
                                name = name.replace('\n', ' ');
                                locationNum = locationNum.replaceAll("\\s+", "");
                                phoneNumber = super.phoneNum();
                                phoneNumber = phoneNumber.replaceAll("\\s+", "");
                                DatabaseClass.newMessageLog(phoneNumber, getPhoneNumber(), message);
                                if (DatabaseClass.newConsumer(phoneNumber, locationNum, name)) {
                                    LogToFile.log("info", phoneNumber + " " + locationNum + " " + name);
                                    sendMessages.add(new Message(phoneNumber, phoneNumber + " has been subscribed to the "
                                            + DatabaseClass.getNotifierLocation(locationNum) + " notifier"));
                                    counting = 0;
                                    v = true;
                                }
                            }
                            if (mess.indexOf("WN(") > -1 && mess.indexOf(')') > -1 && mess.indexOf("WN(") < mess.lastIndexOf(')')) {
                                mess = mess.substring(mess.indexOf("WN(") + 3, mess.lastIndexOf(')'));
                                phoneNumber = super.phoneNum();
                                phoneNumber = phoneNumber.replaceAll("\\s+", "");
                            } else {
                                mess = checkMessageCommand(i);
                                if (mess.isEmpty() || mess == null) {
                                    if (i > 1)
                                        deleteMessages();
                                    break;
                                }
                                LogToFile.log("info", threadName + ": Command Message Received: \n" + mess);
                            }
                            if (mess.length() >= 10) {
                                message = mess;
                                if (message.indexOf('(') > -1 && message.indexOf(')') > -1 && message.indexOf('(') < message.indexOf(')')) {
                                    message = message.replace('\n', ' ');
                                    LogToFile.log("info", "RECEIVED COMMAND MESSAGE: " + message);
                                    receiveMessages.add(new Message(phoneNumber, message));
                                } else {
                                    receiveMessages.add(new Message(phoneNumber, message));
                                }
                                if (phoneNumber.length() >= 10) {
                                    sendMessages.add(new Message(phoneNumber, "OK: " + message));
                                    counting = 0;
                                }
                                v = true;
                            }
                            delay(2000);
                        }
                        deleteMessages();
                        if (v)
                            function = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogToFile.log("info", "CONTINUING");
                    }
                    String a = waitForCall(0);
                    if (!a.isEmpty()) {
                        DatabaseClass.newCallLog(a, getPhoneNumber());
                        LogToFile.log("info", threadName + ": got a call = " + a);
                        if (DatabaseClass.deviceExists(a)) {
                            phoneNumbersCall.add(a);
                            function = 0;
                        }
                    }
                    removeDuplicates(phoneNumbersCall);
                    removeDuplicatesMessage(receiveMessages);
                    removeDuplicatesMessage(sendMessages);
                }
                if (function == 2) {
                    LogToFile.log("info", threadName + ": waitForCalls");
                    String a = waitForCall();
                    DatabaseClass.newCallLog(a, getPhoneNumber());
                    LogToFile.log("info", threadName + ": got a call = " + a);
                    if (DatabaseClass.deviceExists(a))
                        phoneNumbersCall.add(a);
                    function = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogToFile.log("info", "CONTINUING: major");
            if (checkConnection()) {
                LogToFile.log("info", "RECONNECTING");
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
