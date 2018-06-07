import java.util.concurrent.TimeUnit;

public abstract class GSMs extends GSM implements Runnable {
	private Thread t;
	public String threadName;
	public int function;
	private String phoneNumber;

	public GSMs(String com, String phone, String name, int fun) {
		super(com);
		phoneNumber = phone;
		threadName = name;
		function = fun;
		System.out.println(threadName + ": creating");
		checkConnection();
		if (t == null && ownPhoneNumber()) {
			t = new Thread(this, threadName);
			t.start();
		}
		else
			System.out.println(threadName + ": wrong SIM card/COM port" + "=\n" + jsend("AT+CNUM"));
	}

	public void run() {
		checkConnection();
		startCommands();
		System.out.println(threadName + ": running");
		while(!checkService()) {
			System.out.println(threadName + ": trying to get service");
			delay(5000);
		}
		System.out.println(threadName + ": service");
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
			// TODO Auto-generated catch block
		}
	}
	
	public String toString() {
		return threadName;
	}
}