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
                    try {
                        int counts = 0;
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
                        checkService();
                    } catch(Exception e){
                        e.printStackTrace();
                        System.out.println("CONTINUING");
                    }
                    delay(100);
                    notif();
                }
                if (function == 1) {
                    ArrayList<String> mess = new ArrayList<>();
                    for(Message a : messages)
                        mess.add(a.getPhoneNumber());
                    System.out.println(threadName + ": sendingMessages: " + mess);
                    for (int i = 0; i < messages.size(); i++) {
                        Message a = messages.get(i);
                        DatabaseClass.newMessageLog(getPhoneNumber(), a.getPhoneNumber(), a.getMessage());
                        try {
                            checkConnection();
                            checkService();
                        } catch(Exception e){
                            e.printStackTrace();
                            System.out.println("CONTINUING");
                        }
                        System.out.println(threadName + ": message " + a.getPhoneNumber() + ": " + sendMessage(a.getPhoneNumber(), a.getMessage()));
                        messages.remove(i);
                        if(messages.size() >= i)
                            i--;
                    }
                    System.out.println(threadName + ": missedCalls: " + phoneNumbers);
                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        DatabaseClass.newCallLog(getPhoneNumber(), phoneNumbers.get(i));
                        System.out.println(threadName + ": call " + phoneNumbers.get(i) + ":  " + missedCall(phoneNumbers.get(i), 10000));
                        phoneNumbers.remove(i);
                        if(messages.size() >= i)
                            i--;
                    }
                    function = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CONTINUING: major");
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

    public synchronized void addMessage(Message a){
        messages.add(a);
    }

    public synchronized void addPhoneNumbers(ArrayList<String> a){
        for(String b : a)
            phoneNumbers.add(b);
    }
}
