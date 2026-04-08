import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class AimBot implements NativeKeyListener {

    private static Robot robot;
    private static volatile boolean running = false;

    public static void main(String[] args) throws Exception {
        robot = new Robot();

        // Disable annoying logging
        java.util.logging.Logger logger =
                java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);
        logger.setUseParentHandlers(false);

        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new AimBot());
        } catch (Exception e) {
            System.err.println("Native hook failed: " + e.getMessage());
        }

        // SETTINGS
        Color targetColor = new Color(149, 195, 232);
        int startX = 100, startY = 140, width = 1700, height = 560;

        System.out.println("READY. F1 = Toggle | ESC = Exit");

        while (true) {
            if (running) {
                Rectangle area = new Rectangle(startX, startY, width, height);
                BufferedImage screenShot = robot.createScreenCapture(area);

                outerLoop:
                for (int y = 0; y < height; y += 30) {
                    for (int x = 0; x < width; x += 30) {
                        Color pixel = new Color(screenShot.getRGB(x, y));

                        if (isMatch(pixel, targetColor)) {
                            robot.mouseMove(startX + x, startY + y);
                            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                            Thread.sleep(1);
                            break outerLoop;
                        }
                    }
                }

                Thread.sleep(10); // reduce CPU usage
            } else {
                Thread.sleep(100);
            }
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_Z) {
            synchronized (AimBot.class) {
                running = !running;
            }

            Toolkit.getDefaultToolkit().beep();
            System.out.println("Bot: " + (running ? "ON" : "OFF"));

            try { Thread.sleep(20); } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            System.out.println("Stopping...");
            System.exit(0);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {}

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {}

    private static boolean isMatch(Color c1, Color c2) {
        int t = 25;
        return Math.abs(c1.getRed() - c2.getRed()) < t &&
                Math.abs(c1.getGreen() - c2.getGreen()) < t &&
                Math.abs(c1.getBlue() - c2.getBlue()) < t;
    }
}