import java.util.ArrayList;

public class MissedCalls extends GSMs{
	
	private ArrayList<String> phoneNumbers = new ArrayList<>();
	private String phoneNumber = "";
	private String message = "";

	public MissedCalls(int comports, String phone, String name, int fun) {
		super(comports, phone, name, fun);
	}
	public MissedCalls(String phone, String name, int fun) {
		super(phone, name, fun);
	}
	
	public void running() {
		while(true) {
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
					System.out.println(threadName + ": missedCalls " + phoneNumbers);
					for (String a : phoneNumbers) {
						DatabaseClass.newCallLog(getPhoneNumber(), a);
						System.out.println(threadName + ": " + missedCall(a, 10000));
					}
					function = 0;
				}
				if (function == 2) {
					System.out.println(threadName + ": sendingMessages");
					for (String a : phoneNumbers) {
						DatabaseClass.newMessageLog(getPhoneNumber(), a, "Water has been supplied");
						System.out.println(threadName + ": " + sendMessage(a, "Water has been supplied"));
					}
					function = 0;
				}
				if (function == 3) {
					System.out.println(threadName + ": sendingReply");
					DatabaseClass.newMessageLog(getPhoneNumber(), phoneNumber, message);
					System.out.println(threadName + ": " + sendMessage(phoneNumber, message));
					function = 0;
				}
				if (function == 4) {
					System.out.println(threadName + ": sendingMessages");
					for (String a : phoneNumbers) {
						DatabaseClass.newMessageLog(getPhoneNumber(), a, message);
						System.out.println(threadName + ": " + sendMessage(a, message));
					}
					function = 0;
				}
			}
			catch(Exception e){
				e.printStackTrace();
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
