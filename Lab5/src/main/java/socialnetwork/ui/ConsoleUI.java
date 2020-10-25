package socialnetwork.ui;

import socialnetwork.domain.User;
import socialnetwork.service.UserService;

import java.util.List;
import java.util.Scanner;

public class ConsoleUI implements UI {

    private final UserService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleUI(UserService service){
        this.service=service;
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
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Invalid command");
            }
        }
    }

    private void displayMenu(){
        System.out.println("Available functionalities");
        System.out.println("help : To display the menu");
        System.out.println("addUser firstName lastName : To add a user");
        System.out.println("removeUser userID : To remove a user");
        System.out.println("displayUsers : To display all the saved users");
        System.out.println("exit : To terminate the session");
    }

    @Override
    public void addUser(String[] arguments) {
        if(arguments.length!=3){
            System.out.println("Invalid syntax for command");
            return;
        }

        try{
            if(service.addUser(new User(arguments[1],arguments[2])).isEmpty())
                System.out.println("User added successfully");
            else
                System.out.println("User was not added - it already exists");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void displayAllUsers() {
        List<User> list = this.service.getAllUsers();
        System.out.format("%5s%20s%20s\n","ID","First Name","Last Name");
        list.forEach(user -> {
            System.out.format("%5d%20s%20s\n",user.getId(),user.getFirstName(),user.getLastName());
        });
    }

    @Override
    public void removeUser(String[] arguments) {
        if(arguments.length!=2){
            System.out.println("Invalid syntax");
            return;
        }
        try {
            Long id = Long.parseLong(arguments[1]);
            if(this.service.removeUser(id).isEmpty())
                System.out.println("No user with ID "+id+" exists");
            else
                System.out.println("Removal of user succeeded");
        }catch (NumberFormatException e){
            System.out.println("Invalid ID");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
