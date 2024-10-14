import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CarRace {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Race");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CarPanel carPanel = new CarPanel();
        frame.getContentPane().add(carPanel);
        frame.pack();
        frame.setSize(800, 400);
        frame.setVisible(true);

        // Creăm mașinile
        Car[] cars = new Car[] {
                new Car("Red car", carPanel, Color.RED),
                new Car("Blue car", carPanel, Color.BLUE),
                new Car("Green car", carPanel, Color.GREEN),
                new Car("Yellow car", carPanel, Color.YELLOW)
        };

        // Începem cursele
        for (Car car : cars) {
            car.start();
        }
    }
}

class Car extends Thread {
    private String name;
    private int distance = 0;
    private CarPanel carPanel;
    private Color carColor;
    private boolean isWinner = false;

    public Car(String name, CarPanel carPanel, Color carColor) {
        this.name = name;
        this.carPanel = carPanel;
        this.carColor = carColor;
    }

    public void run() {
        while (distance < 700) { // ajustăm pentru o cursă mai lungă
            int speed = (int) (Math.random() * 20) + 1; // viteze variabile
            distance += speed;

            carPanel.updateCarPosition(name, distance, carColor);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!carPanel.hasWinner()) {
            carPanel.setWinner(name);
            this.isWinner = true;
        }

        carPanel.carFinished(name);
    }

    public boolean isWinner() {
        return isWinner;
    }
}

class CarPanel extends JPanel {
    private int[] carPositions;
    private String[] carNames;
    private Color[] carColors;
    private String winnerCar = null;

    public CarPanel() {
        carPositions = new int[4];
        carNames = new String[] { "Red car", "Blue car", "Green car", "Yellow car" };
        carColors = new Color[4]; // Culori dinamice
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawTrack(g); // Desenează pista de curse

        for (int i = 0; i < 4; i++) {
            int yPos = 50 + i * 70; // Poziționare mai distanțată pe verticală
            int xPos = carPositions[i];
            int carWidth = 50;
            int carHeight = 30;

            g.setColor(carColors[i]);
            g.fillRect(xPos, yPos, carWidth, carHeight);
            g.setColor(Color.BLACK);
            g.drawRect(xPos, yPos, carWidth, carHeight);
            g.drawString(carNames[i], xPos, yPos - 10);

            // Contur pentru câștigător
            if (carNames[i].equals(winnerCar)) {
                g.setColor(new Color(255, 215, 0)); // Aurie
                g.drawRect(xPos, yPos, carWidth, carHeight);
                g.drawString("Winner!", xPos + 60, yPos + 15);
            }
        }
    }

    // Desenează pista de curse
    private void drawTrack(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 30, getWidth(), 300);

        g.setColor(Color.WHITE);
        for (int i = 0; i < 8; i++) {
            g.drawLine(i * 100, 30, i * 100, 330); // linii de delimitare
        }

        g.setColor(Color.RED);
        g.fillRect(700, 30, 10, 300); // linia de sosire
        g.setColor(Color.WHITE);
        g.drawString("Finish", 710, 25);
    }

    public void updateCarPosition(String carName, int distance, Color carColor) {
        int carIndex = getCarIndex(carName);
        if (carIndex != -1) {
            carPositions[carIndex] = distance;
            carColors[carIndex] = carColor;
            repaint();
        }
    }

    public void carFinished(String carName) {
        System.out.println(carName + " finished the race.");
    }

    public void setWinner(String carName) {
        this.winnerCar = carName;
        repaint();
    }

    public boolean hasWinner() {
        return winnerCar != null;
    }

    private int getCarIndex(String carName) {
        for (int i = 0; i < 4; i++) {
            if (carNames[i].equals(carName)) {
                return i;
            }
        }
        return -1;
    }
}
