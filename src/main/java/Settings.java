import java.awt.*;

class Settings {
    static int AGENTS_NUMBER = 1000;
    static double STRATEGIC_PERCENTAGE = 0.5;
    static int NUMBER_OF_ITERATIONS = 200;
    static int PAUSE_BETWEEN_ITERATIONS = 50;
    static double INTERACTION_RADIUS = 50;

    static float HONEST_W = 1f; //po co to obniżać poniżej 1? konkurencja z innymi HONEST?
    static float HONEST_X = 0f;
    static float STRATEGIC_Z = 0.5f;
    static float STRATEGIC_Y = 0.5f; //po co to obniżać?

    static int AGENTS_XY_LIMIT = 1000;
    static int AGENTS_DOT_SIZE = 10;
    static Color AGENTS_COLOR_HONEST = Color.black;
    static Color AGENTS_COLOR_STRATEGIC = Color.red;
    static int AGENTS_CHART_SIZE_X = 500;
    static int AGENTS_CHART_SIZE_Y = 500;

    static float INITIAL_TRUST = 1f;
    static char H_POLICY = 'i'; // m = min
    static char S_POLICY = 'i'; // i = iloczyn

    static final String ANSI_RESET = "\u001B[0m";
    static final String ANSI_RED = "\u001B[31m";
    static final String ANSI_CYAN = "\u001B[36m";
}
