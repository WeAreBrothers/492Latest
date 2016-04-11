package com.example.robien.beachbuddy;

/**
 * Created by Robien on 3/18/2016.
 */
public class Message {

    private String sender, recipient, msg, msgID;

    public Message(String sender, String recipient, String msg, String msgID) {
        this.setSender(sender);
        this.setRecipient(recipient);
        this.setMsg(msg);
        this.setMsgID(msgID);

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }


}
