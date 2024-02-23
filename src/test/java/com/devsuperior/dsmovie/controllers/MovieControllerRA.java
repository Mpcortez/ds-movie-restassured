package com.devsuperior.dsmovie.controllers;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

class MovieControllerRA {

    @Test
    void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
    }

    @Test
    void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
    }

    @Test
    void findByIdShouldReturnMovieWhenIdExists() {
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
    }

    @Test
    void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
    }

    @Test
    void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
    }

    @Test
    void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
    }
}
