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

class ScoreControllerRA {

    private String adminToken;

    private Map<String, Object> postProduct;

    @BeforeEach
    void setUp() {
        baseURI = "http://localhost:8080";

        String adminUsername = "maria@gmail.com";
        String adminPassword = "123456";

        adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

        postProduct = new HashMap<>();
    }

    @Test
    void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() {
        postProduct.put("movieId", 100);
        postProduct.put("score", 4.0);
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() {
        postProduct.put("score", 4.0);
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() {
        postProduct.put("movieId", 1);
        postProduct.put("score", -1.0);
        JSONObject newProduct = new JSONObject(postProduct);

        given()
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + adminToken)
                .body(newProduct)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/scores")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }
}
