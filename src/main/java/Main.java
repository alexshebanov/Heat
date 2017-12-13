import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Решение уравнения теплопроводности");
        window.setSize(600, 600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JFreeChart chart = new HeatEquationSolver().result();

        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);

        window.setContentPane(new ChartPanel(chart));

        window.setVisible(true);


    }
}
