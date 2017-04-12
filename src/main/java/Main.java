public class Main {
    public static void main(String args[]) throws InterruptedException {

        // PREPARE BEFORE ITERATION BEGINS
        Agent[] agents = new Agent[Settings.AGENTS_NUMBER];
        for (int i = 0; i < agents.length; i++)
            agents[i] = new Agent();


        AgentsChart agentsChart = new AgentsChart(agents);
        agentsChart.pack();
        agentsChart.setVisible(true);

        // START ITERATION
        for (int i = 0; i < Settings.NUMBER_OF_ITERATIONS; i++) {
            Thread.sleep(Settings.PAUSE_BETWEEN_ITERATIONS);

            for (Agent agent : agents)
                agent.relocate();

            agentsChart.refresh();
        }
    }
}
