package socialnetwork.ui;
import socialnetwork.domain.User;
import socialnetwork.service.MasterServiceWithLogging;
import socialnetwork.service.ServiceException;

import java.util.Arrays;

public class ConsoleUIWithLogging extends ConsoleUI {

    private User loggedUser;

    public ConsoleUIWithLogging(MasterServiceWithLogging masterService) {
        super(masterService);
    }

    @Override
    public void run(){
        this.logging();
        super.run();
    }

    @Override
    protected void displayMenu(){
        System.out.println("--------------------------------------------------------------");
        System.out.println("Available functionalities");
        System.out.println("help : To display the menu");
        System.out.println("------");
        System.out.println("addUser firstName lastName : To add a user");
        System.out.println("removeUser userID : To remove a user");
        System.out.println("displayUsers : To display all the saved users");
        System.out.println("------");
        System.out.println("addFriend ID : To add a friendship between users");
        System.out.println("removeFriend ID2 : To remove a friendship between users");
        System.out.println("------");
        System.out.println("getCommunities : To get the number of communities");
        System.out.println("getMostSociable : To get the most sociable community");
        System.out.println("------");
        System.out.println("filterFriendships : To display your friendships");
        System.out.println("filterFriendships Month : To display your friendships made in a particular month");
        System.out.println("------");
        System.out.println("sendFriendRequest toId : To send a friend request");
        System.out.println("acceptFriendRequest ID : To accept a friend request");
        System.out.println("rejectFriendRequest ID : To reject a friend request");
        System.out.println("displayFriendRequests : To display all of the friend requests");
        System.out.println("------");
        System.out.println("sendMessage IDTo1 ... IDToN : To send a message");
        System.out.println("replyMessage IDMessage  : To reply to a message");
        System.out.println("getConversation IdUser2 : To get the conversation between 2 users");
        System.out.println("------");
        System.out.println("exit : To terminate the session");
        System.out.println("--------------------------------------------------------------");
    }

    private void logging(){
        System.out.print("Enter your credentials\nID: ");
        try{
            Long id = Long.parseLong(scanner.nextLine());
            loggedUser = masterService.logging(id);
            System.out.println("Hello "+loggedUser.getFirstName()+" "+loggedUser.getLastName());
        }catch (NumberFormatException e){
            System.out.println("Invalid argument");
            this.logging();
        }catch (ServiceException e){
            System.out.println(e.getMessage());
            this.logging();
        }
    }

    @Override
    protected void addFriend(String[] arguments){
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[]{arguments[0],loggedUser.getId().toString(),arguments[1]};
        super.addFriend(newArguments);
    }

    @Override
    protected void removeFriend(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[]{arguments[0],loggedUser.getId().toString(),arguments[1]};
        super.removeFriend(newArguments);
    }

    @Override
    protected void sendFriendRequest(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[]{arguments[0],loggedUser.getId().toString(),arguments[1]};
        super.sendFriendRequest(newArguments);
    }

    @Override
    protected void sendMessage(String[] arguments) {
        if(arguments.length < 2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[arguments.length+1];//{arguments[0],loggedUser.getId().toString(),arguments[1]};
        newArguments[0]=arguments[0];
        newArguments[1]=loggedUser.getId().toString();
        System.arraycopy(arguments, 1, newArguments, 2, arguments.length - 1);
        super.sendMessage(newArguments);
    }

    @Override
    protected void replyMessage(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[]{arguments[0],arguments[1],loggedUser.getId().toString()};
        super.replyMessage(newArguments);
    }

    @Override
    protected void getConversation(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[]{arguments[0],loggedUser.getId().toString(),arguments[1]};
        super.getConversation(newArguments);
    }

    @Override
    protected void filterFriendships(String[] arguments) {
        if(arguments.length <1){
            System.out.println("Invalid syntax");
            return;
        }
        String[] newArguments = new String[ arguments.length+1 ];
        newArguments[0]=arguments[0];
        newArguments[1]=loggedUser.getId().toString();
        if(arguments.length==2)
            newArguments[2]=arguments[1];
        super.filterFriendships(newArguments);
    }

}
