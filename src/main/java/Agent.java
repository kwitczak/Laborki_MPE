import java.util.Random;

class Agent {
    public enum Kind { STRATEGIC, HONEST }

    private static Random generator = new Random();
    public static int numberOfStrategic = 0;
    private static int iterator = 0;

    private int id;
    private int x;
    private int y;
    private Kind kind;

    private ReputationAggregationEngine rae = ReputationAggregationEngine.getInstance();

    private float[] commodityAvailability = new float[Settings.AGENTS_NUMBER]; // A ij(t)

    Agent() {
        relocate();
        this.kind = generateKind();
        this.id = iterator++;
    }

    void relocate() {
        this.x = generateLocation();
        this.y = generateLocation();

        // Each agent has different set of commodities for other agents
        generateCommodity();
    }

    private int generateLocation() {
        return generator.nextInt(Settings.AGENTS_XY_LIMIT) + 1;
    }

    private void generateCommodity() {
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++)
            commodityAvailability[i] = generator.nextFloat();
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

    private float sellMeCommodity(int buyerId, Kind buyerKind) {
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
    System.out.println("Sprzedaję ja " + this.getId() + " rodzaju " + this.getKind() + " ilość " + calculateCommodity(p_threshold, commodityAvailability[buyerId]) + " kupującemu " + buyerId + " rodzaju " + buyerKind);
    return calculateCommodity(p_threshold, commodityAvailability[buyerId]); // P ij(t)
}

    private float reportPurchase(Agent seller, float boughtCommodityAmount) {
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
        return calculateCommodity(r_threshold, boughtCommodityAmount); // R ij(t)
    }

    private float calculateCommodity (float tresh, float commodity) {
        if (this.getKind() == Kind.STRATEGIC && Settings.S_POLICY == 'i') {
            return tresh*commodity;
        }
        else if (this.getKind() == Kind.STRATEGIC && Settings.S_POLICY == 'm') {
            return Math.min(tresh, commodity);
        }
        else if (this.getKind() == Kind.HONEST && Settings.H_POLICY == 'i') {
            return tresh*commodity;
        }
        else if (this.getKind() == Kind.HONEST && Settings.H_POLICY == 'm') {
            return Math.min(tresh, commodity);
        }
        System.out.println("Error when calculating commodity!");
        return 0;
    }

    int getId() {return id;}

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    Kind getKind() {
        return kind;
    }
}
