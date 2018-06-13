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
        try {
            int count = 0;
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
                        count++;
                        startCommands();
                        checkService();
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
                                                    + DatabaseClass.notifierLocation(locationNum) + " notifier"));
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

    public synchronized String phoneNumber() {
        return phoneNumber;
    }

    public synchronized String message() {
        return message;
    }
}
