package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;


import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Faker FAKER = new Faker(new Locale("en"));

    private static final RequestSpecification REQUEST_SPEC = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private DataGenerator() {
    }

    static void sendRequest(RegistrationDto user) {
        io.restassured.RestAssured.given()
                .spec(REQUEST_SPEC)
                .body(user)
                .when()
                .log().all()
                .post("/api/system/users")
                .then()
                .log().all()
                .statusCode(200);
    }

    public static String getRandomLogin() {
        return FAKER.name().username();
    }

    public static String getRandomPassword() {
        return FAKER.internet().password();
    }

    public static String generateStringWithSpecialChars() {
        String specialChars = "!@#$%^&*()_+-=<>?";
        int length = 8;
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }
        return sb.toString();
    }

    public static class Registration {

        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            return new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto user = getUser(status);
            sendRequest(user);
            return user;
        }

    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}
