package com.tourneynizer.tourneynizer.controller;

/**
 * Created by ryanwiener on 3/9/18.
 */

public class TournamentControllerTest {

    @Test
    public void getTournamentTypesTest() {
        String[] result = TournamentController.getTournamentTypeValues();
        assertEquals(result, new String[] {"Volleyball Pooled", "Volleyball Bracket"});
    }
}
