import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;
import java.util.List;

public class HeatEquationSolver {

    final int dt = 21, N = 15;
    final double lambda = 71.7, L = 0.1, ro = 21470, Cp = 132.6, Time = 60;
    final double T0 = 60, Tst = 60, Tv = 30;

    public JFreeChart result() {
        double[][] T = new double[dt][N];
        double[] x = new double[N + 1];
        double delx = L / (N - 1), dtau = Time / (dt - 1);
        for (int i = 0; i < N; i++)
            x[i] = delx * i;
        Nach(T, Tst, T0);
        setCoefficients(T, delx, dtau, Tst, Cp, ro, lambda);

        List<XYSeries> seriesList = new ArrayList<>();

        for (int i = 0; i < dt; i++) {
            XYSeries series = new XYSeries(Integer.toString(i), false);
            for (int j = 0; j < N; j++) {
                series.add(x[j], T[i][j]);
            }
            seriesList.add(series);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        for (XYSeries series : seriesList) {
            dataset.addSeries(series);
        }

        JFreeChart chart = ChartFactory.createXYLineChart("", "", "", dataset, PlotOrientation.VERTICAL, false, false, false);


        return chart;
    }


    private void Nach(double[][] T, double Tst, double T0) {
        for (int i = 1; i < N - 1; i++) {
            T[0][i] = Tv;
        }
        T[0][0] = T0;
        T[0][N - 1] = Tst;
    }

    private void setCoefficients(double[][] T, double h, double eta, double Tst, double cp, double ro, double lambda) {
        double[] u = new double[N];
        double[] a = new double[N];
        double[] b = new double[N];
        double[] c = new double[N];
        double[] d = new double[N];
        for (int t = 1; t < dt; t++) {
            a[0] = 0;
            b[0] = 1;
            c[0] = 0;
            d[0] = -T0;
            a[N - 1] = 0;
            b[N - 1] = 1;
            c[N - 1] = 0;
            d[N - 1] = -Tst;
            for (int i = 1; i < N - 1; i++) {
                a[i] = lambda / h / h;
                b[i] = -(ro * cp) * 1 / eta - 2 * lambda / h / h;
                c[i] = lambda / h / h;
                d[i] = (ro * cp / eta) * T[t - 1][i];
            }
            u = solveByGaussMethod(a, b, c, d);
            for (int i = 0; i < N; i++)
                T[t][i] = u[i];
        }
    }

    private double[] solveByGaussMethod(double[] a, double[] b, double[] c, double[] d) {
        double[] f = new double[N];
        double[] g = new double[N];
        double[] u = new double[N];
        f[0] = -a[0] / b[0];
        g[0] = -d[0] / b[0];
        for (int n = 1; n < N - 1; n++) {
            f[n] = -a[n] / (b[n] + c[n] * f[n - 1]);
            g[n] = -(d[n] + c[n] * g[n - 1]) / (b[n] + c[n] * f[n - 1]);
        }
        u[N - 1] = -(d[N - 1] + c[N - 1] * g[N - 2]) / (b[N - 1] + c[N - 1] * f[N - 2]);
        for (int n = N - 2; n >= 0; n--)
            u[n] = f[n] * u[n + 1] + g[n];
        return u;
    }

}
