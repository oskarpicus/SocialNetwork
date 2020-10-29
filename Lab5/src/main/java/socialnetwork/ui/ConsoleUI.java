package socialnetwork.ui;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.FriendshipService;
import socialnetwork.service.MasterService;
import socialnetwork.service.UserService;

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
                case "displayUsers" -> displayAllUsers();
                case "removeUser" -> removeUser(arguments);
                case "addFriend" -> addFriend(arguments);
                case "removeFriend" -> removeFriend(arguments);
                case "getCommunities" -> getCommunities();
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
        System.out.println("// getCommunities : To get the number of communities");
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

    @Override
    public void displayAllUsers() {
        List<User> list = this.masterService.getAllUsers();
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
            Long id1 = Long.parseLong(arguments[1]);
            Long id2 = Long.parseLong(arguments[2]);
            Tuple<Long,Long> ids = new Tuple<>(id1,id2);
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
            Tuple<Long,Long> ids = new Tuple<>(id1,id2);
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
        System.out.println(this.masterService.getNumberOfCommunities());
    }
}
