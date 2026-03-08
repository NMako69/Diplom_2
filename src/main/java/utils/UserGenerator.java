package utils;

import com.github.javafaker.Faker;
import models.CreateUser;

public class UserGenerator {

    private static final Faker faker = new Faker();

    // Генерация случайного пользователя
    public static CreateUser randomUser(String password) {

        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();

        return new CreateUser(email, password, name);
    }

    // Генерация пользователя без email
    public static CreateUser userWithoutEmail(String password) {

        String name = faker.name().firstName();

        return new CreateUser("", password, name);
    }

    // Генерация пользователя без password
    public static CreateUser userWithoutPassword() {

        String email = faker.internet().emailAddress();
        String name = faker.name().firstName();

        return new CreateUser(email, "", name);
    }

    // Генерация пользователя без name
    public static CreateUser userWithoutName(String password) {

        String email = faker.internet().emailAddress();

        return new CreateUser(email, password, "");
    }
}
