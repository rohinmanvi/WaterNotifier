/**
 * 
 */
package com.waternotifier.waternotifierlibrary;

/**
 * @author rajeshmanvi This is equivalent to ConsumerOperations on Water
 *         Notifier Android Application
 */
public class DataOperations {

	/**
	 * 
	 */

	// public boolean IsValidPhone(String phone, Context context) {
	public static boolean IsValidPhone(String phone) {
		// Enter 10-Digit Phone Number - Make sure while using for SMS - Add COUNTRY
		// code in the beginning of 10-Digit Phone
		// Otherwise SMS will not be sent.
		return phone.matches("[1-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
	}

	public static boolean IsValidIMEI(String IMEI) {
		// Minmum 15-Digit
		return IMEI.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
	}

	public static boolean IsValidICCID(String ICCID) {
		// Minmum 19-Digit, it can be of 20-Digit based on service provider.
		if (ICCID.length() == 19) {
			return ICCID.matches(
					"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
		} else if (ICCID.length() == 20) {
			return ICCID.matches(
					"[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
		}
		return false;
	}

	
}
