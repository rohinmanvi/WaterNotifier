import java.util.ArrayList;

public class MissedCalls extends GSMs{
	
	private ArrayList<Message> messages = new ArrayList<>();
	private ArrayList<String> phoneNumbers = new ArrayList<>();

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
					for (Message a : messages) {
						DatabaseClass.newMessageLog(getPhoneNumber(), a.getPhoneNumber(), a.getMessage());
						System.out.println(threadName + ": " + sendMessage(a.getPhoneNumber(), a.getMessage()));
					}
					function = 0;
				}
			}
			catch(Exception e){
				e.printStackTrace(); System.out.println("CONTINUING");
			}
		}
	}

	public void setMessages(ArrayList<Message> a) {
		messages = a;
	}

	public void setPhoneNumbers(ArrayList<String> a){
		phoneNumbers = a;
	}
}
