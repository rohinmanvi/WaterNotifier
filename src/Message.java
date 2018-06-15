public class Message {
    private String phoneNumber = "";
    private String message = "";

    public Message(String phone, String mess){
        phoneNumber = phone;
        message = mess;
    }

    public synchronized String getMessage() {
        return message;
    }

    public synchronized String getPhoneNumber() {
        return phoneNumber;
    }
}
