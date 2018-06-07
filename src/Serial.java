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
		WaitForMessages one = new WaitForMessages("COM3", "8189678112", "Receive messages GSM", 1);
//		WaitForCalls two = new WaitForCalls("COM5", "8189135171", "Incoming Calls GSM", 1);
		ArrayList<MissedCalls> three = new ArrayList<>();
		//QUESTION for Rohin :: COM port numbers ???????
		three.add(new MissedCalls("COM3", "4086370230", "MissedCalls 1", 0));
		three.add(new MissedCalls("COM3", "8186439252", "MissedCalls 2", 0));


		//RAJESH- 20180604 - Start
		Long inNotifierPhone = 7472558828L;
		WaitForCalls two = new WaitForCalls("COM5", inNotifierPhone.toString(), "Incoming Calls GSM", 1);

		Location tempLoc = new Location();
		tempLoc = Location.getNotifierLocationZIPCODESeqNumber(7472558828L);


		List<ConsumerCallers> listOfConsumerCallers = new ArrayList<ConsumerCallers>();

		if (tempLoc == null) {
			System.out.println("System could not find Location details for given Notifier Phone : " + inNotifierPhone + " !");
		}
		listOfConsumerCallers = ConsumerCallers.getAll(tempLoc.getZIPCODE(), tempLoc.getSeqNumber());

		if (listOfConsumerCallers.isEmpty() || listOfConsumerCallers == null) {
			System.out.println("There are no Consumer Callers for given Notifier Phone : " + inNotifierPhone +
					" at LocationCode : " + tempLoc.getZIPCODE()+tempLoc.getSeqNumber() );
		} else {
			System.out.println("There are TOTAL of " + listOfConsumerCallers.size() + "  Consumer Callers for given Notifier Phone : " + inNotifierPhone +
					" at LocationCode : " + tempLoc.getZIPCODE()+tempLoc.getSeqNumber() + '\n');


			for (int i = 0; i < listOfConsumerCallers.size(); i++) {
				System.out.println("The Consumers for :: " + listOfConsumerCallers.get(i).getName() + '\n');

//				three.add(new MissedCalls("COM3", listOfConsumerCallers.get(i).getSIMCardPhone().toString(), "MissedCalls 1", 0));

				ArrayList<Long> locationConsumersArrayList = new ArrayList<>();

				locationConsumersArrayList = LocationConsumers.getAllConsumerPhone(listOfConsumerCallers.get(i).getSIMCardPhone(),
						tempLoc.getZIPCODE(), tempLoc.getSeqNumber());

				for (int j = 0; j < locationConsumersArrayList.size(); j++) {
					System.out.println( Integer.valueOf(j+1) + " :: " + locationConsumersArrayList.get(j) + '\n');
				}
			}

		}
		//RAJESH- 20180604 - Start


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
				ArrayList<String> phone = DatabaseClass.getConsumers(x);
				two.removeNumber(devices.get(a));
				System.out.println("device's phone numbers: " + phone);
				int index = -1;
				double thesize = (double) phone.size() / three.size();
				int size = (int) (thesize + 0.5);
				for (int i = 0; i < three.size() - 1; i++) {
					ArrayList<String> partPhone = new ArrayList<>();
					for (int j = size * i; j < size * (i + 1); j++) {
						partPhone.add(CC + phone.get(j));
						index = j;
					}
					System.out.println(three.get(i) + ": assigned " + partPhone);
					three.get(i).phoneNumbers(partPhone);
				}
				ArrayList<String> partPhone = new ArrayList<>();
				for (int i = index + 1; i < phone.size(); i++) {
					partPhone.add(CC + phone.get(i));
				}
				System.out.println(three.get(three.size() - 1) + ": assigned " + partPhone);
				three.get(three.size() - 1).phoneNumbers(partPhone);
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
