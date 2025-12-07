package ru.manveru.integrationaltests.PrintMatrixTests;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PrintMatrixTest extends PrintMatrixTestBase{
    
    @Test
    public void testAllParams() {
        List<String> response = sendRequest(Map.of("start", 2, "end", 10, "per_line", 5));

        assertAll(
        () -> assertEquals("2 3 4 5 6", response.get(0), "First stirng is 2 3 4 5 6"),
        () -> assertEquals("7 8 9 10 0", response.get(1), "Second stirng is 7 8 9 10 0")
         );
    }

    @Test
    public void testStartPerLineParams() {
        List<String> response = sendRequest(Map.of("start", 91, "per_line", 5));

        assertAll(
        () -> assertEquals("91 92 93 94 95", response.get(0), "First stirng is 91 92 93 94 95"),
        () -> assertEquals("96 97 98 99 100", response.get(1), "Second stirng is 96 97 98 99 100")
         );
    }
    
    @Test
    public void testStartEndParams() {
        List<String> response = sendRequest(Map.of("start", 2, "end", 20));
            
        assertAll(
        () -> assertEquals("2 3 4 5 6 7 8 9 10 11", response.get(0), "First stirng is 2 3 4 5 6 7 8 9 10 11"),
        () -> assertEquals("12 13 14 15 16 17 18 19 20 0", response.get(1), "Second stirng is 12 13 14 15 16 17 18 19 20 0")
         );
    }
    
    @Test
    public void testEndPerLineParams() {
        List<String> response = sendRequest(Map.of("end", 7, "per_line", 5));
            
        assertAll(
        () -> assertEquals("1 2 3 4 5", response.get(0), "First stirng is 1 2 3 4 5"),
        () -> assertEquals("6 7 0 0 0", response.get(1), "Second stirng is 6 7 0 0 0")
         );
    }
    
    @Test
    public void testNoParams() {
        List<String> response = sendRequest(Map.of());
            
        assertAll(
        () -> assertEquals("1 2 3 4 5 6 7 8 9 10", response.get(0), "First stirng is 1 2 3 4 5"),
        () -> assertEquals("6 7 0 0 0", response.get(1), "Second stirng is 6 7 0 0 0")
         );
    }
}
