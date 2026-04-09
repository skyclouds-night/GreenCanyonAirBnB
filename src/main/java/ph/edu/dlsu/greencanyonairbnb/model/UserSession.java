package ph.edu.dlsu.greencanyonairbnb.model;

public class UserSession {
    private static int userId;
    private static String username;
    private static String userRole;

    public static void setUser(int id, String name, String role) {
        userId = id;
        username = name;
        userRole = role;
    }

    public static String getRole() {return userRole;}
    public static int getUserId() {return userId;}
    public static String getUsername() {return username;}
    public static void endSession(){
        userId = 0;
        username = null;
        userRole = null;
    }
}
