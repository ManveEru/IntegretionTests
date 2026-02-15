package ru.manveru.integrationaltests;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.manveru.integrationaltests.Extentions.LoggingExtension;
import ru.manveru.integrationaltests.Helpers.RestApiHelper;

@ExtendWith(LoggingExtension.class)
public class BaseTest {
    public static RestApiHelper apiHelper = RestApiHelper.getInstance();
    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
