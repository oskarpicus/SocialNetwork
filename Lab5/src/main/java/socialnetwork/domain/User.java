package socialnetwork.domain;

import java.util.List;
import java.util.Objects;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private List<User> friends;

    //
    private static Long NUMBEROFUSERS = 1L;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        //
        super.setId(getProperNUMBEROFUSERS());
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

    @Override
    public String toString() {
        return "Utilizator{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=" + friends +
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
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    private Long getProperNUMBEROFUSERS(){
        if(NUMBEROFUSERS.equals(7331115341259248461L)) //this is will be reserved for all tests
            NUMBEROFUSERS++;
        return (NUMBEROFUSERS++);
    }

}