import java.util.Random;

public class Agent {
    public enum Kind { STRATEGIC, HONEST }

    private static Random generator = new Random();
    private static int numberOfStrategic = 0;

    private int x;
    private int y;
    private Kind kind;

    Agent() {
        relocate();
        this.kind = generateKind();
    }

    void relocate() {
        this.x = generateLocation();
        this.y = generateLocation();
    }

    private int generateLocation() {
        return generator.nextInt(Settings.AGENTS_XY_LIMIT) + 1;
    }

    private Kind generateKind() {
        boolean canBeStrategic = numberOfStrategic < Settings.AGENTS_NUMBER * Settings.STRATEGIC_PERCENTAGE;
        if (canBeStrategic) {
            numberOfStrategic ++;
            return Kind.STRATEGIC;
        } else {
            return Kind.HONEST;
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    public Kind getKind() {
        return kind;
    }
}
