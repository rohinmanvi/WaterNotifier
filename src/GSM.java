import java.util.concurrent.TimeUnit;

import arduino.Arduino;

public class GSM extends Arduino {
	private String port;
	private boolean service;
	private int baud;
	private String imei = "";
	private String ccid = "";
	private String number = "";

	public GSM() {
		super();
		service = false;
	}
	
	public GSM(String p) {
		super();
		port = p;
		service = false;
		setPortDescription(port);
		try {
			openConnection();
		} catch (Exception e) {
			closeConnection();
		}
	}

	public GSM(String p, int b) {
		super();
		port = p;
		service = false;
		baud = b;
		setPortDescription(port);
		openConnection();
		setBaudRate(baud);
	}

	public boolean checkConnection() {
		for (int i = 0; i < 10; i++) {
			if (send("AT", 0.1))
				return true;
			print("trying to connect");
		}
		return false;
	}

	public boolean send(String a) {
		serialWrite(a + '\r' + '\n');
		for (int i = 0; i < 10; i++) {
			delay(100);
			String b = serialRead();
			print(b);
			if (b.indexOf("OK") > -1) {
				return true;
			} else if (b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1) {
				return false;
			}
		}
		return false;
	}

	public boolean send(String a, double s) {
		serialWrite(a + '\r' + '\n');
		for (int i = 0; i < s; i++) {
			delay(100);
			String b = serialRead();
			print(b);
			if (b.indexOf("OK") > -1) {
				return true;
			} else if (b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1) {
				return false;
			}
		}
		return false;
	}

	public String jsend(String a, double s) {
		String b = "";
		serialWrite(a + '\r' + '\n');
		for (int i = 0; i < s; i++) {
			delay(100);
			b = serialRead();
			print(b);
			if (b.indexOf("OK") > -1 || b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1
					|| b.indexOf(">") > -1) {
				return b;
			}
		}
		return b;
	}

	public String jsend(String a) {
		String b = "";
		serialWrite(a + '\r' + '\n');
		for (int i = 0; i < 10; i++) {
			delay(100);
			b = serialRead();
			print(b);
			if (b.indexOf("OK") > -1 || b.indexOf("ERROR") > -1 || b.indexOf("CMS") > -1 || b.indexOf("CME") > -1
					|| b.indexOf(">") > -1) {
				return b;
			}
		}
		return b;
	}

	public boolean send(String a, String f, int s) {
		String b = "";
		serialWrite(a + '\r' + '\n');
		for (int i = 0; i < s; i++) {
			delay(100);
			b = serialRead();
			print(b);
			if (b.indexOf(f) > -1) {
				return true;
			}
		}
		return false;
	}

	public void startCommands() {
		send("AT+CFUN=1");
		send("AT+CPMS=\"SM\",\"SM\",\"SM\"");
		send("AT+CMGF=1");
	}

	public boolean checkService() {
		if (jsend("AT+CIND?").substring(jsend("AT+CIND?").indexOf("+CIND:")).substring(11, 12).equals("1")) {
			service = true;
			return true;
		} else {
			service = false;
			return false;
		}
	}

	public boolean service() {
		return service;
	}

	public boolean sendMessage(String phone, String message) {
		if (jsend("AT+CMGS=" + "\"+" + phone + '\"').indexOf('>') > -1) {
            return send(message + (char) 26, 3000);
		} else
			return false;
	}

	public void deleteMessages() {
		send("AT+CMGD=1,4", 2000);
	}

	public String checkMessage(int x) {
		if (send("AT+CMGR=" + x + '\r' + '\n')) {
			serialWrite("AT+CMGR=" + x + '\r' + '\n');
			delay(1000);
			String a = serialRead();
			if (a.indexOf('\"') != a.lastIndexOf('\"') && a.indexOf('+') > -1 && a.indexOf('O') > -1) {
				number = a;
				number = number.substring(0, number.lastIndexOf('\"') - 1);
				number = number.substring(0, number.lastIndexOf('\"') - 1);
				number = number.substring(number.lastIndexOf('+') + 1, number.lastIndexOf('\"'));
				return a.substring(a.lastIndexOf('\"') + 1, a.lastIndexOf('O') - 1);
			}
		}
		number = null;
		return null;
	}

	public String phoneNum() {
		return number;
	}

	public String waitForCall() {
		String b;
		String a = "";
		while (true) {
			b = serialRead();
			if (b.indexOf("RING") > -1) {
				for (int i = 0; i < 5; i++) {
					a = jsend("AT+CLCC");
					if (a.indexOf("+CLCC:") > -1)
						break;
				}
				for (int k = 0; k < 3; k++) {
					if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
						break;
					}
				}
				if (a.indexOf('"') > -1) {
					a = a.substring(a.indexOf("\"") + 1, a.lastIndexOf("\""));
					return a.substring(a.indexOf("\"") + 1);
				} else
					return "";
			}
			delay(1);
		}
	}

	public String waitForCall(double s) {
		String b;
		String a = "";
		for (int i = 0; i < (s * 1000); i++) {
			b = serialRead();
			if (b.indexOf("RING") > -1) {
				for (int k = 0; k < 3; k++) {
					a = jsend("AT+CLCC");
					if (a.indexOf("OK") > -1)
						break;
				}
				if (!(a.indexOf("CMS") > -1)) {
					a = a.substring(a.indexOf("\"") + 1, a.lastIndexOf("\""));
					for (int k = 0; k < 3; k++) {
						if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
							break;
						}
					}
					return a.substring(a.indexOf("\"") + 1);
				} else
					return "";
			}
			delay(1);
		}
		return "";
	}

	public boolean call(String n) {
		String b;
		for (int i = 0; i < 3; i++) {
			if (send("ATD+" + n)) {
				while (true) {
					b = serialRead();
					print(b);
					if (b.indexOf("SOUNDER\",0") > -1 || b.indexOf("ERROR") > -1) {
						for (int k = 0; k < 5; k++) {
							if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
								return true;
							}
						}
					}
					delay(1);
				}
			}
		}
		return false;
	}

	public boolean missedCall(String n, int s) {
		String b;
		for (int i = 0; i < 3; i++) {
			if (send("ATD+" + n, 5)) {
				while (true) {
					b = serialRead();
					print(b);
					if (b.indexOf("SO") > -1 || b.indexOf("DER") > -1 || b.indexOf("ERROR") > -1) {
						delay(s);
						for (int k = 0; k < 5; k++) {
							if (send("AT+CHUP", "CALL\",0", 5) || send("AT+CHUP", "CME", 5)) {
								return true;
							}
						}
						return false;
					}
					delay(1);
				}
			}
		}
		return false;
	}

	public static void delay(int s) {
		try {
			TimeUnit.MILLISECONDS.sleep(s);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
	}

	private void print(String a) {
		// System.out.println(a);
	}

}
