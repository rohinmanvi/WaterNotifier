import com.waternotifier.waternotifierlibrary.LogToFile;

import java.util.ArrayList;

public class WaitForMessages extends GSMs {

    private String phoneNumber = "";
    private String message = "";
    private String testingPhone = "18189135171";

    public WaitForMessages(int comports, String phone, String name, int fun) {
        super(comports, phone, name, fun);
    }

    public WaitForMessages(String phone, String name, int fun) {
        super(phone, name, fun);
    }

    public void running() {
        int counting = 0;
        try {
            int count = 0;
            while (true) {
                counting++;
                if(counting == 10000){
                    counting = 0;
                    reset();
                }
                    if (function == -1) {
                        closeConnection();
                        LogToFile.log("info",threadName + " exiting.");
                        notif();
                        break;
                    }
                    if (function == 0) {
                        delay(100);
                        notif();
                    }
                    if (function == 1) {
                        count++;
                        try {
//                            int counts = 0;
//                            while (!checkService()) {
//                                if (counts > 5) {
//                                    reset();
//                                    checkConnection();
//                                    startCommands();
//                                    counts = 0;
//                                }
//                                counts++;
//                                LogToFile.log("info",threadName + ": trying to get service");
//                                delay(5000);
//                            }
                            checkService();
                        } catch(Exception e){
                            e.printStackTrace();
                            LogToFile.log("info","CONTINUING");
                        }
                        LogToFile.log("info",threadName + ": testing " + count + " = " + sendMessage(testingPhone, "Notifier is still alive: " + count));
                        for (int j = 0; j < 1000; j++) {
                            for (int i = 1; i <= 100; i++) {
                                String mess = "";
                                try {
                                    mess = checkMessage(i);
                                    if (mess.isEmpty() || mess == null) {
                                        break;
                                    }
                                    LogToFile.log("info",threadName + ": Message Received: \n" + mess);
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
                                            LogToFile.log("info",phoneNumber + " " + locationNum + " " + name);
                                            checkConnection();
                                            LogToFile.log("info", "" + sendMessage(phoneNumber, phoneNumber + " has been subscribed to the "
                                                    + DatabaseClass.getNotifierLocation(locationNum) + " notifier"));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogToFile.log("info","CONTINUING");
                                    break;
                                }
                                delay(2000);
                            }
                            deleteMessages();
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogToFile.log("info","CONTINUING: major");
            if (checkConnection()) {
                LogToFile.log("info","RECONNECTING");
                run();
            } else {
                closeConnection();
                COMStart(20);
                run();
            }
        }
    }

    public synchronized String phoneNumber() {
        return phoneNumber;
    }

    public synchronized String message() {
        return message;
    }
}
