import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

class TrustChart extends ApplicationFrame {

    TrustChart(String applicationTitle, String chartTitle, float[][] trustHistory) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Iterations", "Trust",
                createDataset(trustHistory),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 800));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset(float[][] trustHistory) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int iteration = 0; iteration < Settings.NUMBER_OF_ITERATIONS; iteration++) {
            float sumStrategic = 0;
            float sumHonest = 0;

            for (int agentID = 0; agentID < Settings.AGENTS_NUMBER; agentID++) {
                Agent.Kind kind = Main.agents[agentID].getKind();
                float trust = trustHistory[iteration][agentID];

                if (kind == Agent.Kind.STRATEGIC) {
                    sumStrategic += trust;
                } else {
                    sumHonest += trust;
                }
            }

            float averageStrategicForIteration = sumStrategic / Agent.numberOfStrategic;
            float averageHonestForIteration = sumHonest / (Settings.AGENTS_NUMBER - Agent.numberOfStrategic);

            dataset.addValue(averageStrategicForIteration, "Strategic", Integer.toString(iteration + 1));
            dataset.addValue(averageHonestForIteration, "Honest", Integer.toString(iteration + 1));
        }

        return dataset;
    }
}