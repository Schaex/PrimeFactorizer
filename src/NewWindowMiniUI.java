import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NewWindowMiniUI extends ApplicationConstants {
    /**
     * Utility class to quickly open a new window
     */
    JFrame frame = new JFrame();
    JPanel listPanel = new JPanel(new GridBagLayout());
    JScrollPane listScrollPane = new JScrollPane(listPanel);

    /**
     * Constructor
     */
    public NewWindowMiniUI(String input, String timestamp) {
        UI.allFrames.add(frame);
        frame.setTitle(timestamp);
        frame.setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3d), (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.9d));
        frame.setLocationRelativeTo(null);
        frame.setLocation(2 * (UI.mainFrame.getX()), frame.getY());
        frame.add(listScrollPane);
        listPanel.add(makeTextArea("Your input: " + input + linesep + linesep + "Calculating... please wait!", 20, null), textAreaConstraints);
        listPanel.add(Box.createGlue(), glueBoxConstraints);

        frame.setVisible(true);

        executorService.submit(() -> {
            List<TextArea> newTextAreas = createOutput(input);

            listPanel.removeAll();

            for (TextArea textArea : newTextAreas) {
                listPanel.add(textArea, textAreaConstraints);
            }

            listPanel.add(Box.createGlue(), glueBoxConstraints);

            frame.validate();
        });
    }
}
