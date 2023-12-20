import java.awt.*;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new UI();
            Runtime.getRuntime().addShutdownHook(new Thread(
                    UI.executorService::shutdown));
        });
    }
}