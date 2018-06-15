import java.util.ArrayList;

public class MissedCalls extends GSMs {

    private ArrayList<Message> messages = new ArrayList<>();
    private ArrayList<String> phoneNumbers = new ArrayList<>();

    public MissedCalls(int comports, String phone, String name, int fun) {
        super(comports, phone, name, fun);
    }

    public MissedCalls(String phone, String name, int fun) {
        super(phone, name, fun);
    }

    public void running() {
        try {
            while (true) {
                if (function == -1) {
                    closeConnection();
                    System.out.println(threadName + " exiting.");
                    notif();
                    break;
                }
                if (function == 0) {
                    if(checkConnection())
                        checkService();
                    delay(100);
                    notif();
                }
                if (function == 1) {
                    System.out.println(threadName + ": missedCalls ");
                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        DatabaseClass.newCallLog(getPhoneNumber(), phoneNumbers.get(i));
                        System.out.println(threadName + ": call " + phoneNumbers.get(i) + ":  " + missedCall(phoneNumbers.get(i), 10000));
                    }
                    System.out.println(threadName + ": sendingMessages");
                    for (int i = 0; i < messages.size(); i++) {
                        Message a = messages.get(i);
                        DatabaseClass.newMessageLog(getPhoneNumber(), a.getPhoneNumber(), a.getMessage());
                        checkConnection();
                        checkService();
                        System.out.println(threadName + ": message " + a.getPhoneNumber() + ": " + sendMessage(a.getPhoneNumber(), a.getMessage()));
                    }
                    messages = new ArrayList<>();
                    function = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING");
            if (checkConnection()) {
                run();
            } else {
                System.out.println("RECONNECTING");
                closeConnection();
                COMStart(20);
                run();
            }
        }
    }

    public synchronized void setMessages(ArrayList<Message> a) {
        messages = a;
    }

    public synchronized void setPhoneNumbers(ArrayList<String> a) {
        phoneNumbers = a;
    }

    public synchronized void addMessages(ArrayList<Message> a){
        for(Message b : a)
            messages.add(b);
    }
}
