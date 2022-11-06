package user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandomUser() {
        return new User(
                RandomStringUtils.randomAlphanumeric(6).toLowerCase() + "@test.ru",
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static User getRandomUserWithoutEmail() {
        return new User(
                "",
                RandomStringUtils.randomAlphanumeric(6),
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static User getRandomUserWithoutPassword() {
        return new User(
                RandomStringUtils.randomAlphanumeric(6).toLowerCase() + "@test.ru",
                "",
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public static User getRandomUserWithoutName() {
        return new User(
                RandomStringUtils.randomAlphanumeric(6).toLowerCase() + "@test.ru",
                RandomStringUtils.randomAlphanumeric(6),
                ""
        );
    }

    public static User getRandomEmailForUser(User user) {
        return new User(
                RandomStringUtils.randomAlphanumeric(6).toLowerCase() + "@test.ru",
                user.password,
                user.name
        );
    }

    public static User getRandomPasswordForUser(User user) {
        return new User(
                user.email,
                RandomStringUtils.randomAlphanumeric(6),
                user.name
        );
    }

    public static User getRandomNameForUser(User user) {
        return new User(
                user.email,
                user.password,
                RandomStringUtils.randomAlphabetic(6)
        );
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
