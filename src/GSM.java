import java.util.concurrent.TimeUnit;

import arduino.Arduino;
import com.waternotifier.waternotifierlibrary.LocationConsumers;
import com.waternotifier.waternotifierlibrary.LogToFile;

public class GSM extends Arduino {
    private String port = "";
    private boolean service;
    private int baud = 0;
    private String imei = "";
    private String ccid = "";
    private String number = "";

    public GSM() {
        super();
        service = false;
    }

    public GSM(String p) {
        super();
        port = p;
        service = false;
        setPortDescription(port);
        try {
            openConnection();
        } catch (Exception e) {
            LogToFile.log(e,"info", "GSM || GSM ");
            closeConnection();
        }
    }

    public GSM(String p, int b) {
        super();
        port = p;
        service = false;
        baud = b;
        setPortDescription(port);
        openConnection();
        setBaudRate(baud);
    }

    public boolean checkConnection() {
        return send("AT", 5);
    }

    public boolean send(String a) {
        for (int i = 0; i < 100; i++) {
            connect();
            serialWrite(a + '\r' + '\n');
            String b = serialRead();
            print(b);
            if (b.indexOf("OK") > -1) {
                return true;
            } else if (b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1) {
                return false;
            }
        }
        return false;
    }

    public boolean send(String a, double s) {
        for (int i = 0; i < s; i++) {
            connect();
            serialWrite(a + '\r' + '\n');
            String b = serialRead();
            print(b);
            if (b.indexOf("OK") > -1) {
                return true;
            } else if (b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1) {
                return false;
            }
        }
        return false;
    }

    public boolean send(String a, String f, int s) {
        String b = "";
        for (int i = 0; i < s; i++) {
            connect();
            serialWrite(a + '\r' + '\n');
            b = serialRead();
            print(b);
            if (b.indexOf(f) > -1) {
                return true;
            }
        }
        return false;
    }

    public boolean oldsend(String a, int s) {
        connect();
        serialWrite(a + '\r' + '\n');
        for (int i = 0; i < s; i++) {
            connect();
            delay(100);
            String b = serialRead();
            if (b.indexOf("OK") > -1) {
                return true;
            }
        }
        return false;
    }

    public String jsend(String a, double s) {
        String b = "";
        for (int i = 0; i < s; i++) {
            connect();
            serialWrite(a + '\r' + '\n');
            b = serialRead();
            print(b);
            if (b.indexOf("OK") > -1 || b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1
                    || b.indexOf(">") > -1) {
                return b;
            }
        }
        return b;
    }

    public String jsend(String a) {
        String b = "";
        for (int i = 0; i < 100; i++) {
            connect();
            serialWrite(a + '\r' + '\n');
            b = serialRead();
            print(b);
            if (b.indexOf("OK") > -1 || b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1
                    || b.indexOf(">") > -1) {
                return b;
            }
        }
        return b;
    }

    public void startCommands() {
        closeConnection();
        for (int i = 0; i < 3; i++) {
            boolean a = false;
            while (!(a)) {
                openConnection();
                if (send("ATE0")) {
                    delay(100);
                    if (send("AT+CFUN=1")) {
                        delay(100);
                        if (send("AT+CPMS=\"SM\",\"SM\",\"SM\"")) {
                            delay(100);
                            if (send("AT+CMGF=1")) {
                                delay(100);
                                a = true;
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean checkService() {
        String a = jsend("AT+CIND?");
        int b = a.indexOf("+CIND:");
        if (b > -1 && a.length() > b) {
            a = a.substring(b);
            if (a.length() > 11)
                if (a.substring(11, 12).equals("1")) {
                    service = true;
                    return true;
                }
        }
        service = false;
        return false;
    }

    public boolean service() {
        return service;
    }

    public boolean sendMessage(String phone, String message) {
        jsend("" + (char) 26);
        if (jsend("AT+CMGS=" + "\"+" + phone + '\"').indexOf('>') > -1) {
            return oldsend(message + (char) 26, 300);
        } else {
            return oldsend(message + (char) 26, 10);
        }
    }

    public void deleteMessages() {
        send("AT+CMGD=1,4", 5);
    }

    public String checkMessage(int x) {
        if (send("AT+CMGR=" + x + '\r' + '\n') && send("AT+CMGR=" + x + '\r' + '\n') && send("AT+CMGR=" + x + '\r' + '\n')) {
            for (int i = 0; i < 5; i++) {
                connect();
                serialWrite("AT+CMGR=" + x + '\r' + '\n');
                delay(1000);
                String a = serialRead();
                LogToFile.log("info",a);
                if (a.indexOf('\"') != a.lastIndexOf('\"') && a.indexOf('+') > -1 && a.indexOf('O') > -1) {
                    number = a;
                    number = number.substring(0, number.lastIndexOf('\"') - 1);
                    number = number.substring(0, number.lastIndexOf('\"') - 1);
                    if (number.indexOf('+') < number.indexOf('\"') && a.lastIndexOf('\"') + 1 < a.lastIndexOf('O') - 1) {
                        number = number.substring(number.lastIndexOf('+') + 1, number.lastIndexOf('\"'));
                        return a.substring(a.lastIndexOf('\"') + 1, a.lastIndexOf('O') - 1);
                    }
                }
            }
        }
        number = "";
        return "";
    }

    public String checkMessageCommand(int x) {
        if (send("AT+CMGR=" + x + '\r' + '\n') && send("AT+CMGR=" + x + '\r' + '\n') && send("AT+CMGR=" + x + '\r' + '\n')) {
            for (int i = 0; i < 5; i++) {
                connect();
                serialWrite("AT+CMGR=" + x + '\r' + '\n');
                delay(1000);
                String a = serialRead();
                LogToFile.log("info",a);
                if (a.indexOf("WN(") > -1 && a.lastIndexOf(')') > -1 && a.indexOf("WN(") < a.lastIndexOf(')')) {
                    String b = a.substring(a.indexOf("WN(") + 3, a.lastIndexOf(')'));
                    if (b.indexOf('(') > -1) {
                        if (b.indexOf(')') > -1 && b.indexOf("WN(") < 0)
                            return a.substring(a.indexOf("WN(") + 3, a.lastIndexOf(')'));
                    } else if (a.substring(a.indexOf("WN(") + 3, a.lastIndexOf(')')).length() <= 12 && b.indexOf("WN(") < 0)
                        return a.substring(a.indexOf("WN(") + 3, a.lastIndexOf(')'));
                }
            }
        }
        return "";
    }

    public String phoneNum() {
        return number;
    }

    public String waitForCall() {
        String b = "";
        String a = "";
        while (true) {
            connect();
            b = serialRead();
            if (b.indexOf("RING") > -1) {
                for (int i = 0; i < 5; i++) {
                    a = jsend("AT+CLCC");
                    if (a.indexOf("+CLCC:") > -1)
                        break;
                }
                for (int k = 0; k < 3; k++) {
                    if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
                        break;
                    }
                }
                if (a.indexOf('"') > -1) {
                    a = a.substring(a.indexOf("\"") + 1, a.lastIndexOf("\""));
                    return a.substring(a.indexOf("\"") + 1);
                } else
                    return "";
            }

        }
    }

    public String waitForCall(int s) {
        String b = "";
        String a = "";
        connect();
        delay(s);
        b = serialRead();
        if (b.indexOf("RING") > -1) {
            for (int k = 0; k < 3; k++) {
                a = jsend("AT+CLCC");
                if (a.indexOf("OK") > -1)
                    break;
            }
            if (!(a.indexOf("CMS") > -1)) {
                a = a.substring(a.indexOf("\"") + 1, a.lastIndexOf("\""));
                for (int k = 0; k < 3; k++) {
                    if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
                        break;
                    }
                }
                return a.substring(a.indexOf("\"") + 1);
            } else
                return "";
        }
        return "";
    }

    public boolean call(String n) {
        String b = "";
        send("AT+CHUP", 1);
        for (int i = 0; i < 3; i++) {
            if (send("ATD+" + n)) {
                while (true) {
                    connect();
                    b = serialRead();
                    print(b);
                    if (b.indexOf("SOUNDER\",0") > -1 || b.indexOf("ERROR") > -1) {
                        for (int k = 0; k < 5; k++) {
                            if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
                                return true;
                            }
                        }
                    }

                }
            }
            else{
                send("AT+CHUP", 1);
            }
        }
        return false;
    }

    public boolean missedCall(String n, int s) {
        String b = "";
        send("AT+CHUP", 5);
        for (int i = 0; i < 10; i++) {
            if (send("ATD+" + n)) {
                delay(s);
                DatabaseClass.madeCall(n);
                for (int k = 0; k < 5; k++) {
                    if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
                        return true;
                    }
                }
                return true;
            }
            else{
                send("AT+CHUP", 5);
            }
        }
        return false;
    }

    public boolean advancedMissedCall(String n, int s) {
        String b = "";
        send("AT+CHUP", 1);
        for (int i = 0; i < 10; i++) {
            if (send("ATD+" + n)) {
                while (true) {
                    connect();
                    b = serialRead();
                    print(b);
                    if (b.indexOf("SO") > -1 || b.indexOf("DER") > -1 || b.indexOf("ERROR") > -1) {
                        delay(s);
                        DatabaseClass.madeCall(n);
                        for (int k = 0; k < 5; k++) {
                            if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
                                return true;
                            }
                        }
                        return true;
                    }

                }
            }
            else{
                send("AT+CHUP", 1);
            }
        }
        return false;
    }

    public static void delay(int s) {
        try {
            TimeUnit.MILLISECONDS.sleep(s);
        } catch (InterruptedException e) {
            LogToFile.log(e,"info", "GSM || delay ");
            // TODO Auto-generated catch block
        }
    }

    private void print(String a) {
        System.out.println(a);
    }

    public void connect(){
        if(!openConnection()){
            LogToFile.log("info", "openConnection did not work");
            delay(500);
            while(!openConnection()){
                delay(500);
                closeConnection();
                delay(500);
            }
            LogToFile.log("info", "openConnection worked");
        }
//        startCommands();
    }

}
