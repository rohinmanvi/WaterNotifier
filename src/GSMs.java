import com.waternotifier.waternotifierlibrary.LogToFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public abstract class GSMs extends GSM implements Runnable {
    private Thread t;
    public String threadName = "";
    public int function = 0;
    private String phoneNumber = "";
    private String COMport = "";
    private static ArrayList<String> COMports = new ArrayList<>();

    public GSMs(int comports, String phone, String name, int fun) {
        super();
        phoneNumber = phone;
        threadName = name;
        function = fun;
        threadName += " (" + phone + ")";
        LogToFile.log("info",threadName + ": creating");
        COMStart(comports);
        threadName += " (" + COMport + ")";
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public GSMs(String phone, String name, int fun) {
        super();
        phoneNumber = phone;
        threadName = name;
        function = fun;
        threadName += " (" + phone + ")";
        LogToFile.log("info",threadName + ": creating");
        boolean v = false;
        boolean reset = false;
        int count = 0;
        while (!v) {
            if (reset)
                reset = false;
            if (count >= 3) {
                count = 0;
                reset = true;
            }
            for (int i = 0; i < COMports.size(); i++) {
                String port = COMports.get(i);
                LogToFile.log("info",threadName + ": " + port);
                setPortDescription(port);
                try {
                    if (openConnection()) {
                        if (checkConnection()) {
                            if (t == null && ownPhoneNumber()) {
                                t = new Thread(this, threadName);
                                t.start();
                                v = true;
                                COMport = port;
                                threadName += " (" + COMport + ")";
                                try {
                                    if (COMports.size() > 0) {
                                        COMports.remove(i);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogToFile.log(e,"info", "GSMs || ");
                                    LogToFile.log("info","CONTINUING");
                                }
                                break;
                            } else {
                                LogToFile.log("info",threadName + ": " + port + " wrong number");
                            }
                            if (reset) {
                                completeReset();
                            }
                        }
                    }
                } catch (Exception e) {
                    closeConnection();
                    e.printStackTrace();
                    LogToFile.log(e,"info", "GSMs || ");
                    LogToFile.log("info","CONTINUING");
                    v = false;
                }
                closeConnection();
            }
            count++;
            COMports = removeDuplicates(COMports);
        }
    }

    public void COMStart(int comports) {
        boolean v = false;
        boolean reset = false;
        int count = 0;
        while (!v || (COMports.size() < DatabaseClass.getConsumerCallers().size())) {
            if (reset)
                reset = false;
            if (count >= 3 && COMports.size() > 0) {
                count = 0;
                reset = true;
            }
            if (count >= 100)
                count = 0;
            for (int i = 0; i <= comports; i++) {
                String port = "COM" + i;
                LogToFile.log("info",threadName + ": " + port);
                setPortDescription(port);
                try {
                    if (openConnection()) {
                        if (checkConnection()) {
                            COMports.add(0, port);
                            if (ownPhoneNumber()) {
                                try {
                                    if (COMports.size() > 0) {
                                        COMports.remove(0);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    LogToFile.log("info","CONTINUING");
                                }
                                v = true;
                                COMport = port;
                            } else {
                                LogToFile.log("info",threadName + ": " + port + " wrong number");
                            }
                            if (reset) {
                                completeReset();
                            }
                        }
                    }
                } catch (Exception e) {
                    closeConnection();
                    e.printStackTrace();
                    LogToFile.log("info","CONTINUING");
                    v = false;
                }
                closeConnection();
                LogToFile.log("info",v + " " + COMports.size());
            }
            COMports = removeDuplicates(COMports);
        }
        setPortDescription(COMport);
        openConnection();
    }

    public void run() {
        checkConnection();
        startCommands();
        LogToFile.log("info",threadName + ": running");
        int count = 0;
        while (!checkService()) {
            if (count > 5) {
                reset();
                checkConnection();
                startCommands();
                count = 0;
            }
            count++;
            LogToFile.log("info",threadName + ": trying to get service");
            delay(5000);
        }
        LogToFile.log("info",threadName + ": service");

        running();
    }

    public boolean ownPhoneNumber() {
        String a = jsend("AT+CNUM");
        return a.indexOf(phoneNumber) > -1;
    }

    public abstract void running();

    public synchronized void setFunction(int fun) {
        function = fun;
    }

    public synchronized void notif() {
        notify();
    }

    public static void delay(int s) {
        try {
            TimeUnit.MILLISECONDS.sleep(s);
        } catch (InterruptedException e) {
            LogToFile.log(e,"info", "GSMs || ");
            // TODO Auto-generated catch block
        }
    }

    public String toString() {
        return threadName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public static ArrayList<String> getCOMports() {
        return COMports;
    }

    public boolean completeReset() {
        LogToFile.log("info",threadName + ": resetting");
        if (send("AT+CFUN=1,1")) {
            delay(3000);
            checkConnection();
            startCommands();
            int count = 0;
            while (!checkService()) {
                if (count > 5) {
                    reset();
                    checkConnection();
                    startCommands();
                    count = 0;
                }
                count++;
                LogToFile.log("info",threadName + ": trying to get service");
                delay(5000);
            }
            LogToFile.log("info",threadName + ": service");
            return true;
        }
        return false;
    }

    public boolean reset() {
        LogToFile.log("info",threadName + ": resetting");
        if (send("AT+CFUN=1,1")) {
            delay(3000);
            while (!checkConnection())
                delay(1000);
            int counts = 0;
            while (!checkService()) {
                if (counts > 5) {
                    reset();
                    checkConnection();
                    startCommands();
                    counts = 0;
                }
                counts++;
                LogToFile.log("info",threadName + ": trying to get service");
                delay(5000);
            }
            return true;
        }
        return false;
    }

    public ArrayList<String> removeDuplicates(ArrayList<String> a) {
        Set<String> hs = new HashSet<>();
        hs.addAll(a);
        a.clear();
        a.addAll(hs);
        return a;
    }

    public ArrayList<Message> removeDuplicatesMessage(ArrayList<Message> a) {
        Set<Message> hs = new HashSet<>();
        hs.addAll(a);
        a.clear();
        a.addAll(hs);
        return a;
    }
}