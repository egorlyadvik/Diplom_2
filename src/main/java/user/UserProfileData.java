package user;

import lombok.Data;

@Data
public class UserProfileData {

    private String email;
    private String password;
    private String name;

    public UserProfileData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserProfileData from(User user) {
        return new UserProfileData(user.getEmail(), user.getPassword(), user.getName());
    }
}
