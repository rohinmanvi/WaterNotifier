import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import arduino.*;
import com.waternotifier.waternotifierlibrary.ConsumerCallers;
import com.waternotifier.waternotifierlibrary.Location;
import com.waternotifier.waternotifierlibrary.LocationConsumers;

public class Serial {
	private static String CC = "1";
	
	public static void main(String[] args) {
		WaitForCalls two = new WaitForCalls("7472558416", "Incoming Calls GSM", 1);
		ArrayList<MissedCalls> three = new ArrayList<>();
		three.add(new MissedCalls("4086370230", "MissedCalls 1", 0));
		three.add(new MissedCalls("8186439252", "MissedCalls 2", 0));

		while (true) {
			waitFor(two);
			ArrayList<String> devices = two.phoneNumbers();
			function(two, 1);
			System.out.println("devices found: " + devices);
			for (int a = 0; a < devices.size(); a++) {
				String x = "";
				if(devices.get(a).length() == 11)
					x = devices.get(a).substring(1);
				if(devices.get(a).length() == 12)
					x = devices.get(a).substring(2);
				two.removeNumber(devices.get(a));
				for (int i = 0; i < three.size(); i++) {
					ArrayList<String> partPhone = DatabaseClass.getConsumers(x, three.get(i).getPhoneNumber());
					System.out.println(three.get(i) + ": assigned " + partPhone);
					three.get(i).phoneNumbers(partPhone);
				}
				for (MissedCalls b : three) {
					function(b, 1);
				}
				for (MissedCalls b : three) {
					waitFor(b);
				}
			}
		}
	}

	private static void function(GSMs b, int a) {
		synchronized (b) {
			b.setFunction(a);
		}
	}

	private static void waitFor(GSMs b) {
		synchronized (b) {
			try {
				b.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void delay(int s) {
		try {
			TimeUnit.MILLISECONDS.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}
}
