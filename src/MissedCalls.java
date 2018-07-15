import com.waternotifier.waternotifierlibrary.LocationConsumers;
import com.waternotifier.waternotifierlibrary.LogToFile;

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
        int counting = 0;
        try {
            while (true) {
                counting++;
                if(counting == 1000){
                    counting = 0;
                    completeReset();
                }
                if (function == -1) {
                    closeConnection();
                    LogToFile.log("info",threadName + " exiting.");
                    notif();
                    break;
                }
                if (function == 0) {
                    try {
//                        int counts = 0;
//                        while (!checkService()) {
//                            if (counts > 10) {
//                                completeReset();
//                                counts = 0;
//                            }
//                            counts++;
//                            LogToFile.log("info", threadName + ": trying to get service");
//                            delay(10000);
//                        }
                        clearTransmit();
                        if (!checkService()) {
                            startCommands();
                            if (!ownPhoneNumber())
                                startCommands();
                        }
                    } catch(Exception e){
                        e.printStackTrace();
                        LogToFile.log("info","CONTINUING");
                    }
                    delay(100);
                    notif();
                }
                if (function == 1) {
                    removeDuplicates(phoneNumbers);
                    removeDuplicatesMessage(messages);
                    ArrayList<String> mess = new ArrayList<>();
                    for(Message a : messages)
                        mess.add(a.getPhoneNumber());
                    LogToFile.log("info",threadName + ": sendingMessages: " + mess);
                    for (int i = 0; i < messages.size(); i++) {
                        Message a = messages.get(i);
                        DatabaseClass.newMessageLog(getPhoneNumber(), a.getPhoneNumber(), a.getMessage());
                        try {
                            checkConnection();
                            checkService();
                        } catch(Exception e){
                            e.printStackTrace();
                            LogToFile.log("info","CONTINUING");
                        }
                        LogToFile.log("info",threadName + ": message " + a.getPhoneNumber());
                        RsendMessage(a.getPhoneNumber(), a.getMessage());
                        messages.remove(i);
                        if(messages.size() >= i)
                            i--;
                    }
                    LogToFile.log("info",threadName + ": missedCalls: " + phoneNumbers);
                    for (int i = 0; i < phoneNumbers.size(); i++) {
                        DatabaseClass.newCallLog(getPhoneNumber(), phoneNumbers.get(i));
                        LogToFile.log("info",threadName + ": call " + phoneNumbers.get(i));
                        RmissedCall(phoneNumbers.get(i), 15000);
                        phoneNumbers.remove(i);
                        if(messages.size() >= i)
                            i--;
                    }
                    function = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogToFile.log("info","CONTINUING: major");
            if (checkConnection()) {
                run();
            } else {
                LogToFile.log("info","RECONNECTING");
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
