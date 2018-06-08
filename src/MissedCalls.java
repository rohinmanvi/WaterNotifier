import java.util.ArrayList;

public class MissedCalls extends GSMs{
	
	private ArrayList<String> phoneNumbers;
	private String phoneNumber;
	private String message;
	
	public MissedCalls(String phone, String name, int fun) {
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
				delay(100);
				notif();
			}
			if(function == 1) {
				System.out.println(threadName + ": missedCalls " + phoneNumbers);
				for(String a : phoneNumbers)
					System.out.println(threadName + ": " + missedCall(a, 10000));
				function = 0;
			}
			if(function == 2) {
				System.out.println(threadName + ": sendingMessages");
				for(String a : phoneNumbers)
					System.out.println(threadName + ": " + sendMessage(a, "Water has been supplied"));
				function = 0;
			}
			if(function == 3) {
				System.out.println(threadName + ": sendingReply");
				System.out.println(threadName + ": " + sendMessage(phoneNumber, message));
				function = 0;
			}
			if(function == 4) {
				System.out.println(threadName + ": sendingMessages");
				for(String a : phoneNumbers)
					System.out.println(threadName + ": " + sendMessage(a, message));
				function = 0;
			}
		}
	}
	
	public void message(String a) {
		message = a;
	}
	
	public void phoneNumber(String a) {
		phoneNumber = a;
	}
	
	public void phoneNumbers(ArrayList<String> a) {
		phoneNumbers = a;
	}
}
