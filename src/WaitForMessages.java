public class WaitForMessages extends GSMs {

    private String phoneNumber = "";
    private String message = "";
    private String testingPhone = "18189135171";

    public WaitForMessages(String phone, String name, int fun) {
        super(phone, name, fun);
    }

    public void running() {
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
                                String a = "";
                                if (phoneNumber.length() == 11)
                                    a = phoneNumber.substring(1);
                                if (phoneNumber.length() == 12)
                                    a = phoneNumber.substring(2);
                                if (DatabaseClass.newConsumer(a, locationNum, name)) {
                                    System.out.println(phoneNumber + " " + locationNum + " " + name);
                                    checkConnection();
                                    System.out.println(sendMessage(phoneNumber, phoneNumber + " has been subscribed to the "
                                            + DatabaseClass.notifierLocation(locationNum) + " notifier"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        delay(2000);
                    }
                    deleteMessages();
                }
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
