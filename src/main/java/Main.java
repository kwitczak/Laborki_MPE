import org.jfree.ui.RefineryUtilities;

import java.util.Arrays;

public class Main {
    static Agent[] agents;

    public static void main(String args[]) throws InterruptedException {

        // PREPARE BEFORE ITERATION BEGINS
        agents = new Agent[Settings.AGENTS_NUMBER];
        ReputationAggregationEngine rae = ReputationAggregationEngine.getInstance();
        for (int i = 0; i < agents.length; i++)
            agents[i] = new Agent();

        AgentsChart agentsChart = new AgentsChart(agents);
        agentsChart.pack();
        agentsChart.setVisible(true);

        float[][] trustMeasureHistory = new float[Settings.NUMBER_OF_ITERATIONS][Settings.AGENTS_NUMBER];

        // START ITERATION
        for (int i = 0; i < Settings.NUMBER_OF_ITERATIONS; i++) {
//            Thread.sleep(Settings.PAUSE_BETWEEN_ITERATIONS);

            // change position and commodity availability
            for (Agent agent : agents)
                agent.relocate();

            // look for agents to interact, and do so
            for (Agent buyer : agents) {
                for (Agent seller : agents) {
                    if (buyer.getId() == seller.getId())
                        continue;

                    if (Settings.INTERACTION_RADIUS > buyer.distanceFrom(seller)) {
//                        System.out.println("------- Interakcja pomiędzy " + buyer.getId() + " oraz " + seller.getId() + " ---------");
                        buyer.interact(seller);
                    }
                }
            }

//            agentsChart.refresh();
            System.out.println("\n##########");

            // history of trust
            rae.recalculateTrustMeasures(i);
            trustMeasureHistory[i] = rae.getWholeTrust().clone();
//            System.out.println("Reputations: " + Arrays.toString(rae.getWholeReputation()));
//            System.out.println("Trusts: " + Arrays.toString(rae.getWholeTrust()));
            System.out.println("\n####################### koniec iteracji " + (i + 1) + " ######################\n");
        }

        // after finish
        // print log table
        for (int agentID = 0; agentID < Settings.AGENTS_NUMBER; agentID++) {
            System.out.print(agents[agentID].getKind() + ": ");
            for (int iteration = 0; iteration < Settings.NUMBER_OF_ITERATIONS; iteration++) {
                System.out.print(trustMeasureHistory[iteration][agentID] + " ");
            }
            System.out.println();
        }

        // draw trust shitty chart
        TrustChart chart = new TrustChart(
                "Trust Chart",
                "Trust changes per iteration",
                trustMeasureHistory);

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
