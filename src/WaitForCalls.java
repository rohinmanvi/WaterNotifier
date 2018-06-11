import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WaitForCalls extends GSMs {

    private ArrayList<String> phoneNumbers = new ArrayList<String>();

    public WaitForCalls(int comports, String phone, String name, int fun) {
        super(comports, phone, name, fun);
    }
    public WaitForCalls(String phone, String name, int fun) {
        super(phone, name, fun);
    }

    public void running() {
        while (true) {
            try {
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
                    System.out.println(threadName + ": waitForCalls");
                    String a = waitForCall();
                    DatabaseClass.newCallLog(a, getPhoneNumber());
                    System.out.println(threadName + ": got a call = " + a);
                    if (DatabaseClass.deviceExists(a))
                        phoneNumbers.add(a);
                    function = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized ArrayList<String> phoneNumbers() {
        return phoneNumbers;
    }

    public synchronized void removeNumber(String phoneNumber) {
        for (int i = 0; i < phoneNumbers.size(); i++)
            if (phoneNumber.equals(phoneNumbers.get(i)))
                phoneNumbers.remove(i);
    }
}
