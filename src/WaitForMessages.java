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
                        System.out.println(threadName + " exiting.");
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
//                                System.out.println(threadName + ": trying to get service");
//                                delay(5000);
//                            }
                            checkService();
                        } catch(Exception e){
                            e.printStackTrace();
                            System.out.println("CONTINUING");
                        }
                        System.out.println(threadName + ": testing " + count + " = " + sendMessage(testingPhone, "Notifier is still alive: " + count));
                        for (int j = 0; j < 1000; j++) {
                            for (int i = 1; i <= 100; i++) {
                                String mess = "";
                                try {
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
                                            checkConnection();
                                            System.out.println(sendMessage(phoneNumber, phoneNumber + " has been subscribed to the "
                                                    + DatabaseClass.getNotifierLocation(locationNum) + " notifier"));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    System.out.println("CONTINUING");
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
            System.out.println("CONTINUING: major");
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

    public synchronized String phoneNumber() {
        return phoneNumber;
    }

    public synchronized String message() {
        return message;
    }
}
