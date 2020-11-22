package socialnetwork.domain.dtos;

public class UserDTO {

    private final Long id;
    private String firstName;
    private String lastName;
    private boolean friendsWithLoggedUser;

    public UserDTO(Long id,String firstName, String lastName, boolean friendsWithLoggedUser) {
        this.id=id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.friendsWithLoggedUser = friendsWithLoggedUser;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isFriendsWithLoggedUser() {
        return friendsWithLoggedUser;
    }

    public void setFriendsWithLoggedUser(boolean friendsWithLoggedUser) {
        this.friendsWithLoggedUser = friendsWithLoggedUser;
    }

    public Long getId() {
        return id;
    }
}
