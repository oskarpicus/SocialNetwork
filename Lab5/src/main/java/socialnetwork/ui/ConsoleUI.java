package socialnetwork.ui;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.service.MasterService;

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
}
