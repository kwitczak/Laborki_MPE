import java.util.Arrays;

class ReputationAggregationEngine {

    private float[][] reputationMeasure = new float [Settings.AGENTS_NUMBER][Settings.AGENTS_NUMBER];
    private float[] reputationAvg = new float [Settings.AGENTS_NUMBER];
    private float[] trustMeasure = new float [Settings.AGENTS_NUMBER];
    private float highTrust = 1;
    private float lowTrust = 1;

    private static ReputationAggregationEngine instance = null;

    public static ReputationAggregationEngine getInstance() {
        if(instance == null) {
            instance = new ReputationAggregationEngine();
        }
        return instance;
    }

    ReputationAggregationEngine () {
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++) {
            trustMeasure[i] = Settings.INITIAL_TRUST;
        }

        System.out.println( Arrays.toString(trustMeasure));
    }

    // recalculating trust measures based on reputation data
    void recalculateTrustMeasures(int currentIteration) {
        float[] newReputation = new float [Settings.AGENTS_NUMBER];
        int howManyInteracted;

        // reputation recalculation
        for (int i=0; i < Settings.AGENTS_NUMBER; i++) {
            howManyInteracted = 0;
            for (int j=0; j < Settings.AGENTS_NUMBER; j++) {
                if (i != j && reputationMeasure[i][j] != 0) {
                    howManyInteracted++;
                    newReputation[i] += (trustMeasure[j]*reputationMeasure[i][j]);
                }
            }
            //System.out.println("howManyInteracted: " + howManyInteracted);
            if (howManyInteracted > 0) newReputation[i] /= howManyInteracted;
            else newReputation[i] = Settings.INITIAL_TRUST;
        }

        this.reputationAvg = newReputation; //R i,avg(t)
        //System.out.println("reputationAvg: " + Arrays.toString(reputationAvg));

        // clusterization method ?avg. of all?
        float clusterizationThreshold = 0;
        for (int i=0; i < Settings.AGENTS_NUMBER; i++) {
            clusterizationThreshold += reputationAvg[i];
        }
        clusterizationThreshold /= Settings.AGENTS_NUMBER;

        // recalculating trust levels
        float highAverage = 0;
        float lowAverage = 0;
        int highCount = 0, lowCount = 0;
        for (int i=0; i < Settings.AGENTS_NUMBER; i++) {
            if (reputationAvg[i] >= clusterizationThreshold) {
                highAverage += reputationAvg[i];
                highCount++;
            }
            else {
                lowAverage += reputationAvg[i];
                lowCount++;
            }
        }
        highAverage /= highCount;
        lowAverage /= lowCount;
        lowTrust = lowAverage / highAverage;
        // highTrust = highAverage / highAverage; // = 1

        // checking if agent in high or low trust group
        for (int i=0; i < Settings.AGENTS_NUMBER; i++) {
            if (reputationAvg[i] >= clusterizationThreshold) trustMeasure[i] = highTrust;
            else trustMeasure[i] = lowTrust;
        }
    }

    void reportInteraction (int buyerId, int sellerId, float reportedValue) {
        reputationMeasure[sellerId][buyerId] = reportedValue;
    }

    float getTrustMeasure(int id) {
        return trustMeasure[id];
    }

    // chart purposes
    float[] getWholeTrust() {return trustMeasure;}
    float[] getWholeReputation() {return reputationAvg;}
    float getHighTrust() {return highTrust;}
    float getLowTrust() {return lowTrust;}

}
