import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WaitForCalls extends GSMs{
	
	private ArrayList<String> phoneNumbers = new ArrayList<String>();
	
	public WaitForCalls(String phone, String name, int fun) {
		super(phone, name, fun);
	}
	
	public void running() {
		while(true) {
			if(function == -1) {
				closeConnection();
				System.out.println(threadName + " exiting.");
				notif();
				break;
			}
			if(function == 0) {
				notif();
			}
			if(function == 1) {
				System.out.println(threadName + ": waitForCalls");
				String a = waitForCall();
				System.out.println(threadName + ": got a call = " + a);
				if(DatabaseClass.deviceExists(a))
					phoneNumbers.add(a);
				function = 0;
			}
		}
	}
	
	public synchronized ArrayList<String> phoneNumbers() {
		return phoneNumbers;
	}
	
	public synchronized void removeNumber(String phoneNumber) {
		for(int i = 0; i < phoneNumbers.size(); i++)
			if(phoneNumber.equals(phoneNumbers.get(i)))
				phoneNumbers.remove(i);
	}
}
