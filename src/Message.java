public class Message {
    private String phoneNumber = "";
    private String message = "";

    public Message(String phone, String mess){
        phoneNumber = phone;
        message = mess;
    }

    public String getMessage() {
        return message;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
