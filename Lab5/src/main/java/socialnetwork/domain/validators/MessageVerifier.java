package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.User;
import socialnetwork.service.MessageService;
import socialnetwork.service.ServiceException;
import socialnetwork.service.UserService;

import java.util.Optional;

//validates the entity, in relation to the other entities
public class MessageVerifier {

    private final UserService userService;
    private final MessageService messageService;

    public MessageVerifier(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    public void verifyForReply(Long messageId,Long userToId){

        //we check if the message to reply to exists
        Optional<Message> result = this.messageService.findOne(messageId);
        if(result.isEmpty())
            throw new ServiceException("Message does not exist");

        //we check if the user is in the to-list of the message
        if(!result.get().getTo().contains(userToId))
            throw new ServiceException("This message was not sent to "+userToId);

       // if it was already replied to
        result.get().getReply().forEach( //we iterate over the message ids
                replyMessageId -> {
                    if(replyMessageId!=null){
                        Optional<Message> replyMessage = this.messageService.findOne(replyMessageId);
                        if(replyMessage.isPresent()){
                            if(replyMessage.get().getFrom().equals(userToId))
                                throw new ServiceException("Message already replied");
                        }
                    }
                }
        );
    }

    /**
     * Method for verifying if a user exists
     * @param id : Long, id of the user to look for
     * @return user : User
     * @throws ServiceException, if the user does not exist
     */
    public User userExists(Long id){
        Optional<User> result = userService.findOne(id);
        if(result.isEmpty())
            throw new ServiceException("User does not exist");
        return result.get();
    }
}
