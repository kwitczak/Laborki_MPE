import java.util.Arrays;

class ReputationAggregationEngine {

    private float[][] reportedValues = new float[Settings.AGENTS_NUMBER][Settings.AGENTS_NUMBER];
    private float[] reputationAvg = new float[Settings.AGENTS_NUMBER];
    private float[] trustMeasure = new float[Settings.AGENTS_NUMBER];
    private float highTrust = 1;
    private float lowTrust = 1;

    private static ReputationAggregationEngine instance = null;

    static ReputationAggregationEngine getInstance() {
        if (instance == null) {
            instance = new ReputationAggregationEngine();
        }
        return instance;
    }

    private ReputationAggregationEngine() {
        for (int agentID = 0; agentID < Settings.AGENTS_NUMBER; agentID++) {
            trustMeasure[agentID] = Settings.INITIAL_TRUST;
        }

        System.out.println(Arrays.toString(trustMeasure));
    }

    // recalculating trust measures based on reputation data
    void recalculateTrustMeasures(int currentIteration) {
        float[] newReputation = new float[Settings.AGENTS_NUMBER];
        int howManyInteracted;

        // reputation recalculation: sellerID - i, buyerID - j
        for (int sellerID = 0; sellerID < Settings.AGENTS_NUMBER; sellerID++) {
            howManyInteracted = 0;

            for (int buyerID = 0; buyerID < Settings.AGENTS_NUMBER; buyerID++) {
                if (sellerID == buyerID)
                    continue;

                if (reportedValues[sellerID][buyerID] != 0) {
                    howManyInteracted++;
                    newReputation[sellerID] += (trustMeasure[buyerID] * reportedValues[sellerID][buyerID]);
                }
            }

            //System.out.println(howManyInteracted);
            newReputation[sellerID] = (howManyInteracted > 0) ? newReputation[sellerID] / howManyInteracted : Settings.INITIAL_TRUST;
            System.out.println("(" + Main.agents[sellerID].getKind() + ") Seller " + sellerID + " interacted with " + howManyInteracted +
                    " agents, ending with reputation: " + newReputation[sellerID]);
        }

        this.reputationAvg = newReputation; //R i,avg(t)
        //System.out.println("reputationAvg: " + Arrays.toString(reputationAvg));

        // clusterization method ?avg. of all?
        float clusterizationThreshold = 0;
        for (int agentID = 0; agentID < Settings.AGENTS_NUMBER; agentID++) {
            clusterizationThreshold += reputationAvg[agentID];
        }
        clusterizationThreshold /= Settings.AGENTS_NUMBER;

        // recalculating trust levels
        float highAverage = 0;
        float lowAverage = 0;
        int highCount = 0, lowCount = 0;
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++) {
            if (reputationAvg[i] >= clusterizationThreshold) {
                highAverage += reputationAvg[i];
                highCount++;
            } else {
                lowAverage += reputationAvg[i];
                lowCount++;
            }
        }
        highAverage /= highCount;
        lowAverage /= lowCount;
        lowTrust = lowAverage / highAverage;
        // highTrust = highAverage / highAverage; // = 1

//        System.out.println("clusterizationThreshold: " + clusterizationThreshold );
//        System.out.println("highTrust: " + highTrust );
//        System.out.println("lowTrust: " + lowTrust );
        // checking if agent in high or low trust group
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++) {
//            System.out.println("reputation avg: " + reputationAvg[i] );
            if (reputationAvg[i] >= clusterizationThreshold) {
                trustMeasure[i] = highTrust;
            } else {
                trustMeasure[i] = lowTrust;
            }

//            System.out.println("received trust: " +  trustMeasure[i]);
        }
    }

    void reportInteraction(int buyerId, int sellerId, float reportedValue) {
        reportedValues[sellerId][buyerId] = reportedValue;
    }

    float getTrustMeasure(int id) {
        return trustMeasure[id];
    }

    // chart purposes
    float[] getWholeTrust() {
        return trustMeasure;
    }

    float[] getWholeReputation() {
        return reputationAvg;
    }

    float getHighTrust() {
        return highTrust;
    }

    float getLowTrust() {
        return lowTrust;
    }

}
