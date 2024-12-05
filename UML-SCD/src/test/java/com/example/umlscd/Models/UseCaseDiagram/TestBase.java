package com.example.umlscd.Models.UseCaseDiagram;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;

import java.util.concurrent.CountDownLatch;

public abstract class TestBase {

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
