package ru.manveru.integrationaltests.DigitCalcTests;

import ru.manveru.integrationaltests.model.DigitSumResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DigitsCalculatorTest extends DigitCalculatorTestBase{

    @Test
    public void testDigitsSum() {
        DigitSumResponse response = sendRequest("12345");

        assertAll(
        () -> assertEquals(15, response.getTotalSum(), "Total sum should be 15"),
        () -> assertEquals(9, response.getOddSum(), "Odd sum should be 9"),
        () -> assertEquals(5, response.getMaxDigit(), "Max digit should be 5")
         );
    }

    @Test
    public void testDigitsSumOfZero() {
        DigitSumResponse response = sendRequest("0");

         assertAll(
        () -> assertEquals(0, response.getTotalSum(), "Total sum should be 0"),
        () -> assertEquals(0, response.getOddSum(), "Odd sum should be 0"),
        () -> assertEquals(0, response.getMaxDigit(), "Max digit should be 0")
         );
    }
    
    @Test
    public void testOddDigitsSum() {
        DigitSumResponse response = sendRequest("7");

        assertAll(
        () -> assertEquals(7, response.getTotalSum(), "Total sum should be 7"),
        () -> assertEquals(7, response.getOddSum(), "Odd sum should be 7"),
        () -> assertEquals(7, response.getMaxDigit(), "Max digit should be 7")
        );
    }
    
    @Test
    public void testEvenDigitsSum() {
        DigitSumResponse response = sendRequest("8");

        assertAll(
        () -> assertEquals(8, response.getTotalSum(), "Total sum should be 8"),
        () -> assertEquals(0, response.getOddSum(), "Odd sum should be 0"),
        () -> assertEquals(8, response.getMaxDigit(), "Max digit should be 8")
        );
    }
}