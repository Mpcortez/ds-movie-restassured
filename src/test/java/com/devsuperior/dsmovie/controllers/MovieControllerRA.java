package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class MovieControllerRA {

    private String clientToken, adminToken, invalidToken;

    private Long existingMovieId, nonExistingMovieId;

    private Map<String, Object> queryParams;

    private Map<String, Object> postProduct;

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:8080";

        existingMovieId = 1L;
        nonExistingMovieId = 100L;

        String clientUsername = "alex@gmail.com";
        String clientPassword = "123456";

        String adminUsername = "maria@gmail.com";
        String adminPassword = "123456";

        clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
        invalidToken = adminToken + "invalid";

        queryParams = new HashMap<>();

        postProduct = new HashMap<>();
        postProduct.put("title", "New Movie");
        postProduct.put("score", 4.8);
        postProduct.put("count", 1);
        postProduct.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
    }

    @Test
    void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
        given()
                .get("/movies")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.title", hasItems("The Witcher", "Venom: Tempo de Carnificina"));
    }

    @Test
    void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
        queryParams.put("size", 12);
        queryParams.put("page", 0);
        queryParams.put("title", "The Witcher");

        given()
                .queryParams(queryParams)
                .get("/movies")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("content.title", hasItems("The Witcher"))
                .body("size", is(12))
                .body("numberOfElements", is(1));
    }

    @Test
    void findByIdShouldReturnMovieWhenIdExists() {
        given()
                .get("/movies/{id}", existingMovieId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(1))
                .body("title", equalTo("The Witcher"))
                .body("score", is(4.5F))
                .body("count", is(2))
                .body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
        given()
                .get("/movies/{id}", nonExistingMovieId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() {
        postProduct.put("title", "");
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void insertShouldReturnForbiddenWhenClientLogged() {
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + clientToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void insertShouldReturnUnauthorizedWhenInvalidToken() {
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + invalidToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/movies")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
