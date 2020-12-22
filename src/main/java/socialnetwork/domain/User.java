package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private final List<User> friends;
    private static Long NUMBEROFUSERS = 1L;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        super.setId((NUMBEROFUSERS++));
        this.friends = new ArrayList<>();
    }

    public User(String firstName, String lastName, String userName, String password, List<User> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.friends = friends;
    }

    public User(String firstName, String lastName, String userName, String password) {
        this(firstName,lastName);
        this.userName = userName;
        this.password = password;
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

    public List<User> getFriends() {
        return friends;
    }

    public void addFriend(User friend){
        friends.add(friend);
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "ID='"+getId()+'\''+
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName());
    }

    public static void setNUMBEROFUSERS(Long numberofusers){
        NUMBEROFUSERS = numberofusers;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}