package ru.manveru.integrationaltests.Extentions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingExtension implements TestWatcher, BeforeAllCallback, AfterAllCallback, BeforeEachCallback, 
        AfterEachCallback {
    private static final Logger logger = LoggerFactory.getLogger(LoggingExtension.class);
    private List<TestResult> testResults = new ArrayList<>();
    
    private enum TestStatus {
        SUCCESSFUL, ABORTED, FAILED, DISABLED
    }
    
    private static class TestResult {
        String testName;
        TestStatus status;
        Throwable cause;
        
        TestResult(String testName, TestStatus status, Throwable cause) {
            this.testName = testName;
            this.status = status;
            this.cause = cause;
        }
    }
    
    @Override
    public void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        logger.info("Test '{}' successfully end", testName);
        testResults.add(new TestResult(testName, TestStatus.SUCCESSFUL, null));
    }
    
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        String errorMessage = cause.getMessage();
        
        logger.error("Test '{}' failed with exception: ", testName, errorMessage, cause);
        testResults.add(new TestResult(testName, TestStatus.FAILED, cause));
    }
    
    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        String testName = context.getDisplayName();
        String disableReason = reason.orElse("Причина не указана");
        
        logger.info("Tets '{}' disabled. Reason: {}", testName, disableReason);
        testResults.add(new TestResult(testName, TestStatus.DISABLED, null));
    }
    
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        String className = context.getDisplayName();
        logger.info("Test scoupe start: {}", className);
        logger.info("Test's tags: {}", context.getTags());
    }
    
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        String className = context.getDisplayName();
        logger.info("Test scoupe end: {}", className);
        
        // Генерация отчета по результатам
        generateTestReport();
    }
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        logger.info("Test begin: {}", context.getDisplayName());
        // Можно замерить время начала
        context.getStore(ExtensionContext.Namespace.GLOBAL)
               .put(context.getUniqueId(), System.currentTimeMillis());
    }
    
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        Long startTime = context.getStore(ExtensionContext.Namespace.GLOBAL)
                                .remove(context.getUniqueId(), Long.class);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Test '{}' done by {} мс", 
                context.getDisplayName(), duration);
            
            // Логирование медленных тестов
            if (duration > 5000) { // 5 секунд
                logger.warn("Test '{}' running much: {} ms", 
                    context.getDisplayName(), duration);
            }
        }
    }
    
    private void generateTestReport() {
        if (testResults.isEmpty()) {
            logger.info("There is no test result for report");
            return;
        }
        
        // Статистика по тестам
        Map<TestStatus, Long> statistics = testResults.stream()
            .collect(Collectors.groupingBy(
                tr -> tr.status, 
                Collectors.counting()
            ));
        
        logger.info("TEST REPORT:");
        logger.info("Tests at all: {}", testResults.size());
        logger.info("Susccessful: {}", statistics.getOrDefault(TestStatus.SUCCESSFUL, 0L));
        logger.info("Failed: {}", statistics.getOrDefault(TestStatus.FAILED, 0L));
        logger.info("Disabled: {}", statistics.getOrDefault(TestStatus.DISABLED, 0L));
        
        // Детали по проваленным тестам
        List<TestResult> failedTests = testResults.stream()
            .filter(tr -> tr.status == TestStatus.FAILED)
            .collect(Collectors.toList());
        
        if (!failedTests.isEmpty()) {
            logger.info("Failed tests:");
            failedTests.forEach(tr -> 
                logger.info("  - {}: {}", tr.testName, 
                    tr.cause != null ? tr.cause.getMessage() : "без сообщения")
            );
        }
        
        // Очистка списка результатов для следующего класса
        testResults.clear();
    }
}
