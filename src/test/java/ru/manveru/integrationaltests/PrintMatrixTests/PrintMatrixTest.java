package ru.manveru.integrationaltests.PrintMatrixTests;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PrintMatrixTest extends PrintMatrixTestBase{
    
    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("testDataProvider")
    public void testParametrizedParams(
                Map<String, Integer> requestParams,
                String expectedString,
                String testDescription) {
        //Data
        //prepareed in testDataProvider
        
        //Actions
        List<String> response = sendRequest(requestParams);

        //Assertions
        assertEquals(expectedString, response.get(0), testDescription);
    }
  
    @Test
    public void testPerLineParams() {
        //Data
        Map<String, Integer> requestParams = Map.of("per_line", 20);
        
        //Actions
        List<String> response = sendRequest(requestParams);
            
        //Assertions
        assertEquals("1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20", response.get(0), "First stirng is incorrect");
        assertEquals("21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40", response.get(1), "Second stirng is incorrect");
        assertEquals("41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60", response.get(2), "Third stirng is incorrect");
        assertEquals("61 62 63 64 65 66 67 68 69 70 71 72 73 74 75 76 77 78 79 80", response.get(3), "Fouth stirng is incorrect");
        assertEquals("81 82 83 84 85 86 87 88 89 90 91 92 93 94 95 96 97 98 99 100", response.get(4), "Fifs stirng is incorrect");
    }
    
    @Test
    public void testNoParams() {
        //Data
        Map<String, Integer> requestParams = Map.of();
        
        //Actions
        List<String> response = sendRequest(requestParams);
            
        //Assertions
        assertEquals("1 2 3 4 5 6 7 8 9 10", response.get(0), "First stirng is incorrect");
        assertEquals("11 12 13 14 15 16 17 18 19 20", response.get(1), "Second stirng is incorrect");
        assertEquals("21 22 23 24 25 26 27 28 29 30", response.get(2), "Third stirng is incorrect");
        assertEquals("31 32 33 34 35 36 37 38 39 40", response.get(3), "Fouth stirng is incorrect");
        assertEquals("41 42 43 44 45 46 47 48 49 50", response.get(4), "Fifs stirng is incorrect");
        assertEquals("51 52 53 54 55 56 57 58 59 60", response.get(5), "Sixth stirng is incorrect");
        assertEquals("61 62 63 64 65 66 67 68 69 70", response.get(6), "Seventh stirng is incorrect");
        assertEquals("71 72 73 74 75 76 77 78 79 80", response.get(7), "Eights stirng is incorrect");
        assertEquals("81 82 83 84 85 86 87 88 89 90", response.get(8), "Nineth stirng is incorrect");
        assertEquals("91 92 93 94 95 96 97 98 99 100", response.get(9), "Tenth stirng is incorrect");
    }
    
    @Test
    public void testTwoLinesZeroBrace() {
        //Data
        Map<String, Integer> requestParams = Map.of(
                "start", 2,
                "end", 6,
                "per_line", 3);
        
        //Actions
        List<String> response = sendRequest(requestParams);

        //Assertions
        assertAll(
        () -> assertEquals("5 6 0", response.get(1), "Second is incorrect"),
        () -> assertEquals(2, response.size(), "Line count is incorrect")
        );
    }
    
    static Stream<Arguments> testDataProvider() {
        return Stream.of(
            arguments(
                Map.of("start", 96, "per_line", 5),
                "96 97 98 99 100",
                "Print digits from start=96 to end=Default with 5 digits per_line"),
            arguments(
                Map.of("start", 2, "end", 11),
                "2 3 4 5 6 7 8 9 10 11",
                "Print digits from start=2 to end=11 with Default digits per_line"),
            arguments(
                Map.of("end", 5, "per_line", 5),
                "0 2 3 4 5",
                "Print digits from start=Default to end=5 with 5 digits per_line"),
            arguments(
                Map.of("start", 91),
                "91 92 93 94 95 96 97 98 99 100",
                "Print digits from start=91 to end=Default with Default digits per_line"),
            arguments(
                Map.of("end", 10),
                "0 2 3 4 5 6 7 8 9 10",
                "Print digits from start=Default to end=10 with Default digits per_line"),
            arguments(
                Map.of("start", 2, "end", 6, "per_line", 5),
                "2 3 4 5 6",
                "Print digits from start=2 to end=6 with 5 digits per_line"));
    }
}
