package ph.edu.dlsu.greencanyonairbnb.model;

public class User {
    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String role;

    public User (String firstName, String lastName, String email, String passwordHash, String role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getEmail() { return email;}
    public String getPasswordHash() { return passwordHash;}
    public String getRole() { return role;}

}
