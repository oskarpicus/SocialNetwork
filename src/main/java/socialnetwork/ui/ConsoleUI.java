package socialnetwork.ui;

import socialnetwork.domain.*;
import socialnetwork.domain.dtos.FriendshipDTO;
import socialnetwork.domain.dtos.MessageDTO;
import socialnetwork.service.MasterService;

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UI {

    private final MasterService masterService;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(MasterService masterService){
        this.masterService=masterService;
    }

    public void run(){
        displayMenu();
        while(true){
            String command = scanner.nextLine();
            String[] arguments = command.split(" ");
            switch (arguments[0]){
                case "help" -> displayMenu();
                case "addUser" -> addUser(arguments);
                case "displayUsers" -> displayUsers(this.masterService.getAllUsers());
                case "removeUser" -> removeUser(arguments);
                case "addFriend" -> addFriend(arguments);
                case "removeFriend" -> removeFriend(arguments);
                case "getCommunities" -> getCommunities();
                case "getMostSociable" -> getMostSociable();
                case "filterFriendships" -> filterFriendships(arguments);
                case "sendFriendRequest" -> sendFriendRequest(arguments);
                case "acceptFriendRequest" -> acceptFriendRequest(arguments);
                case "rejectFriendRequest" -> rejectFriendRequest(arguments);
                case "displayFriendRequests" -> displayFriendRequests();
                case "sendMessage" -> sendMessage(arguments);
                case "replyMessage" -> replyMessage(arguments);
                case "getConversation" -> getConversation(arguments);
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Invalid command");
            }
        }
    }


    private void displayMenu(){
        System.out.println("--------------------------------------------------------------");
        System.out.println("Available functionalities");
        System.out.println("help : To display the menu");
        System.out.println("------");
        System.out.println("addUser firstName lastName : To add a user");
        System.out.println("removeUser userID : To remove a user");
        System.out.println("displayUsers : To display all the saved users");
        System.out.println("------");
        System.out.println("addFriend ID1 ID2 : To add a friendship between users");
        System.out.println("removeFriend ID1 ID2 : To remove a friendship between users");
        System.out.println("------");
        System.out.println("getCommunities : To get the number of communities");
        System.out.println("getMostSociable : To get the most sociable community");
        System.out.println("------");
        System.out.println("filterFriendships ID : To display one's friendships");
        System.out.println("filterFriendships ID Month : To display one's friendships made in a particular month");
        System.out.println("------");
        System.out.println("sendFriendRequest fromId toId : To send a friend request");
        System.out.println("acceptFriendRequest ID : To accept a friend request");
        System.out.println("rejectFriendRequest ID : To reject a friend request");
        System.out.println("displayFriendRequests : To display all of the friend requests");
        System.out.println("------");
        System.out.println("sendMessage IDFrom IDTo1 ... IDToN : To send a message");
        System.out.println("replyMessage IDMessage IDTo : To reply to a message");
        System.out.println("getConversation IdUser1 IdUser2 : To get the conversation between 2 users");
        System.out.println("------");
        System.out.println("exit : To terminate the session");
        System.out.println("--------------------------------------------------------------");
    }

    @Override
    public void addUser(String[] arguments) {
        if(arguments.length!=3){
            System.out.println("Invalid syntax for command");
            return;
        }

        try{
            this.masterService.addUser(new User(arguments[1],arguments[2])).ifPresentOrElse(
                    x -> System.out.println("User was not added - it already exists"),
                    () -> System.out.println("User added successfully")
            );
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void displayUsers(List<User> list) {
        System.out.format("%5s%20s%20s\n","ID","First Name","Last Name");
        list.forEach(user -> System.out.format("%5d%20s%20s\n",user.getId(),user.getFirstName(),user.getLastName()));
    }

    @Override
    public void removeUser(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        try {
            Long id = Long.parseLong(arguments[1]);
            this.masterService.removeUser(id).ifPresentOrElse(
                    x -> System.out.println("Removal of user with ID "+id+" succeeded"),
                    () -> System.out.println("No user with ID "+id+" exists")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid ID");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void addFriend(String[] arguments){
        if(arguments.length!=3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long id2 = Long.parseLong(arguments[2]);
            Long id1 = Long.parseLong(arguments[1]);
            Tuple<Long,Long> ids;
            if(id1.compareTo(id2)<0)
                ids = new Tuple<>(id1,id2);
            else
                ids=new Tuple<>(id2,id1);
            Friendship friendship = new Friendship();
            friendship.setId(ids);
            this.masterService.addFriendship(friendship).ifPresentOrElse(
                    x -> System.out.println("Friendship already exists - was not added"),
                    () -> System.out.println("Friendship added successfully")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid ID");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void removeFriend(String[] arguments) {
        if(arguments.length!=3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long id1 = Long.parseLong(arguments[1]);
            Long id2 = Long.parseLong(arguments[2]);
            Tuple<Long,Long> ids;
            if(id1.compareTo(id2)<0)
                ids = new Tuple<>(id1,id2);
            else
                ids=new Tuple<>(id2,id1);
            this.masterService.removeFriendship(ids).ifPresentOrElse(
                    x -> System.out.println("Friendship removed successfully"),
                    () -> System.out.println("No friendship between "+id1+" and "+id2+" exists")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid ID");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void getCommunities(){
        System.out.println("There are in total "+this.masterService.getNumberOfCommunities()+" communities");
    }

    private void getMostSociable(){
        System.out.println("The most sociable community is composed of:");
        displayUsers(this.masterService.getMostSociable());
    }

    private void filterFriendships(String[] arguments){
        switch (arguments.length){
            case 2 -> filterFriendshipsID(arguments);
            case 3 -> filterFriendshipsIDMonth(arguments);
            default ->
                System.out.println("Invalid syntax");
        }
    }

    private void filterFriendshipsID(String[] arguments){
        try{
            Long id = Long.parseLong(arguments[1]);
            List<FriendshipDTO> list = this.masterService.filterFriendshipsID(id);
            printFriendshipsDTO(list);
        }catch (NumberFormatException e){
            System.out.println("Invalid argument");
        }
    }

    private void filterFriendshipsIDMonth(String[] arguments){
        try{
            Long id = Long.parseLong(arguments[1]);
            Month month = Month.of(Integer.parseInt(arguments[2]));
            List<FriendshipDTO> list = this.masterService.filterFriendshipsIDMonth(id,month);
            printFriendshipsDTO(list);
        }catch (IllegalArgumentException e){
            System.out.println("Invalid arguments");
        }
    }

    private void printFriendshipsDTO(List<FriendshipDTO> list){
        if(list.isEmpty()){
            System.out.println("There are no friendships");
            return;
        }
        System.out.format("%20s%20s%20s\n","First Name","Last Name","Date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        list.forEach(x -> System.out.format("%20s%20s%20s\n",
                x.getFirstName(),x.getLastName(),x.getDate().format(formatter)));
    }

    private void sendFriendRequest(String[] arguments){
        if(arguments.length!=3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long fromId = Long.parseLong(arguments[1]);
            Long toId = Long.parseLong(arguments[2]);
            this.masterService.sendFriendRequest(fromId,toId).ifPresentOrElse(
                    (x) -> System.out.println("Friend Request was already sent"),
                    () -> System.out.println("Friend Request sent successfully"));
        }
        catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void acceptFriendRequest(String[] arguments){
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long id = Long.parseLong(arguments[1]);
            this.masterService.acceptFriendRequest(id).ifPresentOrElse(
                    (x) -> System.out.println("The friend request could not be accepted"),
                    () -> System.out.println("Friend Request accepted successfully")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void rejectFriendRequest(String[] arguments){
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long id = Long.parseLong(arguments[1]);
            this.masterService.rejectFriendRequest(id).ifPresentOrElse(
                    (x) -> System.out.println("The friend request could not be rejected"),
                    () -> System.out.println("Friend Request rejected successfully")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void displayFriendRequests(){
        List<FriendRequest> all = this.masterService.getAllFriendRequests();
        System.out.format("%10s%10s%10s%10s\n","ID","ID From","ID To","Status");
        all.forEach(request -> System.out.format("%10d%10d%10d%10s\n",request.getId(),request.getFromUser(),request.getToUser(),request.getStatus()));
    }

    private void getConversation(String[] arguments){
        if(arguments.length!=3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long id1= Long.parseLong(arguments[1]);
            Long id2= Long.parseLong(arguments[2]);
            List<MessageDTO> all = this.masterService.getConversation(id1,id2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.format("%15s%15s%20s %10s\n","First Name","Last Name","Text","Date");
            all.forEach(messageDTO ->
                System.out.format("%15s%15s%20s %10s\n",
                        messageDTO.getFrom().getFirstName(),
                        messageDTO.getFrom().getLastName(),
                        messageDTO.getText(),formatter.format(messageDTO.getDate()))
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void sendMessage(String[] arguments){
        if(arguments.length < 3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long fromId = Long.parseLong(arguments[1]);
            List<Long> toIds = new ArrayList<>();
            for(int i=2;i<arguments.length;i++)
                toIds.add(Long.parseLong(arguments[i]));
            System.out.println("What would you like to say ?");
            String text = scanner.nextLine();
            this.masterService.sendMessage(new Message(fromId,toIds,text)).ifPresentOrElse(
                    (x) -> System.out.println("Message could not be sent"),
                    () -> System.out.println("Message sent successfully")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void replyMessage(String[] arguments){
        if(arguments.length!=3){
            System.out.println("Invalid syntax");
            return;
        }
        try{
            Long messageId = Long.parseLong(arguments[1]);
            Long userToId = Long.parseLong(arguments[2]);
            System.out.println("What would you like to say?");
            String text = scanner.nextLine();
            this.masterService.replyMessage(messageId,userToId,text).ifPresentOrElse(
                    (x) -> System.out.println("The message could not be replied to"),
                    () -> System.out.println("The message was replied successfully")
            );
        }catch (NumberFormatException e){
            System.out.println("Invalid arguments");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
