import java.util.Random;

class Agent {
    public enum Kind {
        STRATEGIC, HONEST;

        @Override
        public String toString() {
            switch(this) {
                case STRATEGIC: return Settings.ANSI_RED + "STRATEGIC" + Settings.ANSI_RESET;
                case HONEST: return Settings.ANSI_CYAN + "HONEST" + Settings.ANSI_RESET;
                default: throw new IllegalArgumentException();
            }
        }
    }

    private static Random generator = new Random();
    static int numberOfStrategic = 0;
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

    double distanceFrom(Agent seller) {
        return Math.sqrt(Math.pow((double) (getX() - seller.getX()), 2) +
                Math.pow((double) (getY() - seller.getY()), 2));
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
                getId(),
                seller.getId(),
                reportPurchase(
                        seller,
                        seller.sellMeCommodity(getId(), getKind())));
    }

    private float sellMeCommodity(int buyerId, Kind buyerKind) {
        float p_threshold;
        Kind sellerKind = getKind();

        if (sellerKind == Kind.STRATEGIC && buyerKind == Kind.STRATEGIC) { // yo wassup my homie!
            p_threshold = 1;
        } else if (sellerKind == Kind.STRATEGIC) { // i'm strategic U sucka!
            p_threshold = Settings.STRATEGIC_Y;
        } else { // how about a match of cricket, Johnson?
            p_threshold = (rae.getTrustMeasure(buyerId) >= Settings.HONEST_X) ? Settings.HONEST_W : 0;
        }

        float soldCommodity = calculateCommodity(p_threshold, commodityAvailability[buyerId]);
        System.out.println("Sprzedaję ja " + getId() + " rodzaju " + sellerKind + " ilość " + soldCommodity +
                " kupującemu " + buyerId + " rodzaju " + buyerKind);

        return soldCommodity; // P ij(t)
    }

    private float reportPurchase(Agent seller, float boughtCommodityAmount) {
        float r_threshold;
        Kind buyerKind = getKind();
        Kind sellerKind = seller.getKind();

        if (buyerKind == Kind.STRATEGIC && seller.getKind() == Kind.STRATEGIC) { // got blunt?
            r_threshold = 1;
        } else if (buyerKind == Kind.STRATEGIC) { // you're in the wrooong neighbourhood, my man...
            r_threshold = Settings.STRATEGIC_Z;
        } else { // now he must be a swell fella, don't you think?
            r_threshold = (rae.getTrustMeasure(seller.getId()) >= Settings.HONEST_X) ? Settings.HONEST_W : 0;
        }

        float reportedCommodity = calculateCommodity(r_threshold, boughtCommodityAmount);
        System.out.println("Raportuję ja " + getId() + " rodzaju " + buyerKind + " ilość " + reportedCommodity +
                " sprzedającemu " + seller.getId() + " rodzaju " + sellerKind);
        return reportedCommodity; // R ij(t)
    }

    private float calculateCommodity(float p_threshold, float commodity) {
        Kind sellerKind = getKind();

        if (sellerKind == Kind.STRATEGIC && Settings.S_POLICY == 'i') {
            return p_threshold * commodity;
        } else if (sellerKind == Kind.STRATEGIC && Settings.S_POLICY == 'm') {
            return Math.min(p_threshold, commodity);
        } else if (sellerKind == Kind.HONEST && Settings.H_POLICY == 'i') {
            return p_threshold * commodity;
        } else if (sellerKind == Kind.HONEST && Settings.H_POLICY == 'm') {
            return Math.min(p_threshold, commodity);
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
