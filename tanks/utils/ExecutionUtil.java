package tanks.utils;

import tanks.utils.di.Component;
import tanks.utils.di.Prototype;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionUtil {
    public static void execute(Runnable command) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(command);
        executorService.shutdown();
    }
}
