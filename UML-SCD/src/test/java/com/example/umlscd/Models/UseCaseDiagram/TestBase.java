package com.example.umlscd.Models.UseCaseDiagram;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;

/**
 * The {@code TestBase} class is an abstract base class used for initializing the JavaFX platform
 * before running any JUnit 5 tests. This class ensures that the JavaFX runtime is properly started
 * and ready before any tests that require JavaFX components are executed.
 * <p>
 * This class should be extended by test classes that need to interact with JavaFX components, ensuring
 * that the JavaFX runtime is initialized in a thread-safe manner before the tests begin.
 * </p>
 */
public abstract class TestBase {

    /**
     * Initializes the JavaFX runtime by starting it on a separate thread. The method uses a {@link CountDownLatch}
     * to wait for the JavaFX platform to be fully initialized before proceeding with the tests.
     * <p>
     * This method is executed once before all tests in the test class, ensuring that the JavaFX runtime is ready
     * for tests that interact with JavaFX components.
     * </p>
     *
     * @throws InterruptedException if the current thread is interrupted while waiting for the latch to count down
     */
    @BeforeAll
    static void initJFX() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(() -> {
            // No need to do anything here
            latch.countDown();
        });
        latch.await();
    }
}
