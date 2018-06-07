import java.util.ArrayList;

import com.waternotifier.waternotifierlibrary.*;

public class DatabaseClass {

   public static ArrayList<String> getConsumers(String phonenumb, String callerphone) {
		ArrayList<Long> locationConsumersArrayList = new ArrayList<>();
		ArrayList<String> LCA = new ArrayList<>();
		Long inNotifierPhone = Long.parseLong(phonenumb, 10);
		Location tempLoc = new Location();
		tempLoc = Location.getNotifierLocationZIPCODESeqNumber(inNotifierPhone);
		ArrayList<ConsumerCallers> listOfConsumerCallers = new ArrayList<ConsumerCallers>();
		if (tempLoc == null) {
			System.out.println(
					"System could not find Location details for given Notifier Phone : " + inNotifierPhone + " !");
		}
		listOfConsumerCallers = ConsumerCallers.getAll(tempLoc.getZIPCODE(), tempLoc.getSeqNumber());
		if (listOfConsumerCallers.isEmpty() || listOfConsumerCallers == null) {
			System.out.println("There are no Consumer Callers for given Notifier Phone : " + inNotifierPhone
					+ " at LocationCode : " + tempLoc.getZIPCODE() + tempLoc.getSeqNumber());
		} else {
			System.out.println("There are TOTAL of " + listOfConsumerCallers.size()
					+ "  Consumer Callers for given Notifier Phone : " + inNotifierPhone + " at LocationCode : "
					+ tempLoc.getZIPCODE() + tempLoc.getSeqNumber() + '\n');
			for (int i = 0; i < listOfConsumerCallers.size(); i++) {
				if (listOfConsumerCallers.get(i).getSIMCardPhone().toString().equals(callerphone)) {
					System.out.println("The Consumers for :: " + listOfConsumerCallers.get(i).getName() + '\n');
					locationConsumersArrayList = LocationConsumers.getAllConsumerPhone(
							listOfConsumerCallers.get(i).getSIMCardPhone(), tempLoc.getZIPCODE(),
							tempLoc.getSeqNumber());
					for (Long a : locationConsumersArrayList)
						LCA.add(a.toString());
					return LCA;
				}
			}
		}
		return LCA;
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
