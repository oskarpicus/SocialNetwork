package socialnetwork.domain;

import java.util.List;

public class ReplyMessage extends Message{

    private final Message messageToReply; //message to reply to

    public ReplyMessage(User from, List<User> to, String message,Message messageToReply) {
        super(from, to, message);
        this.messageToReply=messageToReply;
    }

    public Message getMessageToReply() {
        return messageToReply;
    }
}
