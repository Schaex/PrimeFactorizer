import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // Constructs the UI (see UI class).
            new UI();

            // Adds a shutdown hook that also shuts down the ExecutorService once the application exits or is terminated.
            Runtime.getRuntime().addShutdownHook(new Thread(
                    UI.executorService::shutdownNow));
        });
    }
}