import java.util.Random;

public class Agent {
    public enum Kind { STRATEGIC, HONEST }

    private static Random generator = new Random();
    private static int numberOfStrategic = 0;
    private static int iterator = 0;

    private int id;
    private int x;
    private int y;
    private Kind kind;

    static ReputationAggregationEngine rae;

    private float[] commodityAvailability = new float[Settings.AGENTS_NUMBER]; // A ij(t)

    Agent() {
        relocate();
        this.kind = generateKind();
        this.id = iterator++;
        rae = new ReputationAggregationEngine();
    }

    void relocate() {
        this.x = generateLocation();
        this.y = generateLocation();
        generateCommodity();
    }

    private int generateLocation() {
        return generator.nextInt(Settings.AGENTS_XY_LIMIT) + 1;
    }

    private void generateCommodity() {
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++) commodityAvailability[i] = generator.nextFloat();
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

    void interact(Agent seller) {
        rae.reportInteraction(
                this.getId(),
                seller.getId(),
                this.reportPurchase(
                        seller,
                        seller.sellMeCommodity(this.getId(), this.getKind()) ));
    }

    float sellMeCommodity(int buyerId, Kind buyerKind) {
        float p_threshold;

        if (this.getKind() == Kind.STRATEGIC && buyerKind == Kind.STRATEGIC) { // yo wassup my homie!
            p_threshold = 1;
        }
        else if (this.getKind() == Kind.STRATEGIC) { // i'm strategic U sucka!
            p_threshold = Settings.STRATEGIC_Y;
        }
        else { // how about a match of cricket, Johnson?
            if (rae.getTrustMeasure(buyerId) >= Settings.HONEST_X) p_threshold = Settings.HONEST_W;
            else p_threshold = 0;
        }
        return p_threshold*commodityAvailability[buyerId]; // P ij(t)
    }

    float reportPurchase(Agent seller, float boughtCommodityAmount) {
        float r_threshold;

        if (this.getKind() == Kind.STRATEGIC && seller.getKind() == Kind.STRATEGIC) { // got blunt?
            r_threshold = 1;
        }
        else if (this.getKind() == Kind.STRATEGIC) { // you're in the wrooong neighbourhood, my man...
            r_threshold = Settings.STRATEGIC_Z;
        }
        else { // now he must be a swell fella, don't you think?
            if (rae.getTrustMeasure(seller.getId()) >= Settings.HONEST_X) r_threshold = Settings.HONEST_W;
            else r_threshold = 0;
        }
        return r_threshold*boughtCommodityAmount; // R ij(t)
    }

    int getId() {return id;}

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
