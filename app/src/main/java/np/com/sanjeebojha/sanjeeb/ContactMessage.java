package np.com.sanjeebojha.sanjeeb;

import java.util.Date;

/**
 * Created by sanjeeb on 09/01/2017.
 */

public class ContactMessage {

        private int id;
        private String phone;
        private String message;
        private Date messageTime;
        public ContactMessage(){

        }
        public ContactMessage(int ID,String Phone, String Message,Date MessageTime){
            this.setId(ID);
            this.setPhone(Phone);
            this.setMessage(Message);
            this.setMessageTime(MessageTime);
        }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Date messageTime) {
        this.messageTime = messageTime;
    }
}
