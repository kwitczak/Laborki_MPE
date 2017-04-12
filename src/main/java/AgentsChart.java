import java.awt.*;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.ApplicationFrame;

class AgentsChart extends ApplicationFrame {

    private Agent[] agents;
    private int iteration = 0;

    AgentsChart(Agent[] agents) {
        super("Wykres Agentów");
        this.agents = agents;
        refresh();
    }

    void refresh() {
        iteration++;
        revalidate();
        JPanel chart = new ChartPanel(createChart(createDataset(agents)));
        chart.setPreferredSize(new Dimension(Settings.AGENTS_CHART_SIZE_X, Settings.AGENTS_CHART_SIZE_Y));
        add(chart);
    }

    private JFreeChart createChart(XYZDataset xyzdataset) {
        JFreeChart jfreechart = ChartFactory.createBubbleChart(
                "Iteracja " + iteration,
                "X",
                "Y",
                xyzdataset,
                PlotOrientation.HORIZONTAL,
                true, true, false);

        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        XYItemRenderer xyitemrenderer = xyplot.getRenderer();
        xyitemrenderer.setSeriesPaint(0, Settings.AGENTS_COLOR_STRATEGIC);
        xyitemrenderer.setSeriesPaint(1, Settings.AGENTS_COLOR_HONEST);

        return jfreechart;
    }

    private XYZDataset createDataset(Agent[] agents) {
        DefaultXYZDataset agentsPlaced = new DefaultXYZDataset();

        double strategic[][] = {new double[agents.length], new double[agents.length], new double[agents.length]};
        double honest[][] = {new double[agents.length], new double[agents.length], new double[agents.length]};
        double series[][][] = {strategic, honest};

        for (int i = 0; i < agents.length; i++) {
            Agent agent = agents[i];
            int kindOrdinal = agent.getKind().ordinal();

            series[kindOrdinal][0][i] = agent.getX();
            series[kindOrdinal][1][i] = agent.getY();
            series[kindOrdinal][2][i] = Settings.AGENTS_DOT_SIZE;
        }

        agentsPlaced.addSeries("Agenci S", strategic);
        agentsPlaced.addSeries("Agenci H", honest);
        return agentsPlaced;
    }
}
