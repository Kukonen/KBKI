import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class FileOutput extends JFrame {

    BufferedImage image;

    FileOutput(BufferedImage image) {
        this.image = image;

        Graphics2D g2d = image.createGraphics();
        g2d.dispose();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(image.getWidth(), image.getHeight());
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, null);
    }
}
