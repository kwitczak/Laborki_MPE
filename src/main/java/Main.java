public class Main {
    public static void main(String args[]) throws InterruptedException {

        // PREPARE BEFORE ITERATION BEGINS
        Agent[] agents = new Agent[Settings.AGENTS_NUMBER];
        ReputationAggregationEngine rae = ReputationAggregationEngine.getInstance();
        for (int i = 0; i < agents.length; i++)
            agents[i] = new Agent();

        AgentsChart agentsChart = new AgentsChart(agents);
        agentsChart.pack();
        agentsChart.setVisible(true);

        float[][] trustMeasureHistory = new float[Settings.NUMBER_OF_ITERATIONS][Settings.AGENTS_NUMBER];

        // START ITERATION
        for (int i = 0; i < Settings.NUMBER_OF_ITERATIONS; i++) {
            Thread.sleep(Settings.PAUSE_BETWEEN_ITERATIONS);

            // change position and commodity availability
            for (Agent agent : agents)
                agent.relocate();

            // look for agents to interact, and do so
            for (Agent buyer : agents) {
                for (Agent seller : agents) {
                    if ( buyer.getId() != seller.getId() &&
                            Settings.INTERACTION_RADIUS > Math.sqrt( Math.pow((double)(buyer.getX()-seller.getX()), 2) +
                                    Math.pow((double)(buyer.getY()-seller.getY()), 2) ) ) {
                        System.out.println("Interakcja pomiędzy " + buyer.getId() + " oraz " + seller.getId());
                        buyer.interact(seller);

                    }
                }
            }

            agentsChart.refresh();

            // history of trust
            rae.recalculateTrustMeasures(i);
            trustMeasureHistory[i] = rae.getWholeTrust();
        }

        // after finish
        for (int i = 0; i < Settings.AGENTS_NUMBER; i++) {
            for (int j = 0; j < Settings.NUMBER_OF_ITERATIONS; j++) {
                System.out.print(trustMeasureHistory[j][i] + " ");
            }
            System.out.println();
        }
    }
}
