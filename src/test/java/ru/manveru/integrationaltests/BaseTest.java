package ru.manveru.integrationaltests;

import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.manveru.integrationaltests.Helpers.RestApiHelper;

public class BaseTest {
    public static RestApiHelper apiHelper;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @BeforeAll
    public static void setup() {
        apiHelper = RestApiHelper.getInstance();
    }
}
