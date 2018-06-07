import java.util.ArrayList;

import com.waternotifier.waternotifierlibrary.*;

public class DatabaseClass {

    public static ArrayList<String> getConsumers(String phonenumb) {
        return Consumer.getAllDatabaseNotifierConsumers(Long.parseLong(phonenumb, 10), 0L, "Y", "Y");
    }

    public static boolean deviceExists(String phonenumb) {

        return Notifier.notifierExists(Long.parseLong(phonenumb, 10));
    }

    public static boolean newConsumer(String phonenumb, String locationNum, String name) {
    	long b = Long.parseLong(phonenumb, 10);
    	int a = Integer.parseInt(locationNum);
        return DatabaseHelper.newConsumer(b, a, name);
    }

    public static String notifierLocation(String locationNum) {

        return Location.getLocationNameByLocationCode(Integer.parseInt(locationNum));
    }
}
