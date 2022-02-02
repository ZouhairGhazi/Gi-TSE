package fr.tse.fise2.info9.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * 
 * Classe SpiderChart : JPanel permettant d'afficher le spider chart
 *
 */
public class SpiderChart extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Constructeur : crée le spider chart à partir des données passées en paramètres
     * @param d 
     */
    public SpiderChart(String d[][]) {  
    	
        JPanel jpanel = createDemoPanel(d);
        jpanel.setPreferredSize(new Dimension(410, 260));
        add(jpanel);
    }

    /**
     * Méthode createDataset : crée le dataset du spider chart à partir des données (en paramètres)
     * @param alldata
     * @return
     */
    private static CategoryDataset createDataset(String alldata[][]) {
    	
        String c1 = "Nombre de commits";
        String c2 = "Nombre de merge requests";
        String c3 = "Nombre de collaborateurs";
        String c[]= {c1,c2,c3};
        
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();        
		
		for(int l=0;l<3;l++) {
			dataset.addValue(calc((float)Integer.parseInt(alldata[3][l+3]),Float.parseFloat(alldata[1][l]),Float.parseFloat(alldata[2][l])), "projet", c[l]);			     
		}
		for(int k=0;k<3;k++) {
			dataset.addValue(calc(Float.parseFloat(alldata[0][k]),Float.parseFloat(alldata[1][k]),Float.parseFloat(alldata[2][k])), "moyenne", c[k]);	 
		}
		for(int l=0;l<3;l++) {
			dataset.addValue(calc(Float.parseFloat(alldata[2][l]),Float.parseFloat(alldata[1][l]),Float.parseFloat(alldata[2][l])), "max", c[l]);			     
		}
        return dataset;
    }

    /**
     * Méthode createChart : crée le spider chart en utilisant la classe SpiderWebPlot
     * @param categorydataset
     * @return
     */
    private static JFreeChart createChart(CategoryDataset categorydataset) {
        SpiderWebPlot plot = new SpiderWebPlot(categorydataset);
        plot.setSeriesPaint(0, Color.decode("#e80009"));
        plot.setSeriesPaint(1, Color.decode("#6b5ece"));
        plot.setSeriesPaint(2, Color.decode("#909090"));
        plot.setStartAngle(54D);
        plot.setInteriorGap(0.40000000000000002D);
        plot.setToolTipGenerator(new StandardCategoryToolTipGenerator());
        JFreeChart chart = new JFreeChart("Comparaison des projets", new Font("Verdana", Font.PLAIN, 18), plot, false);  //TextTitle.DEFAULT_FONT
        LegendTitle legendtitle = new LegendTitle(plot);
        legendtitle.setPosition(RectangleEdge.BOTTOM);
        chart.addSubtitle(legendtitle);
        return chart;
    }
    
    /**
     * Méthode calc : retourne un nombre correspondant à val (passé en paramètre) qui correspond à une valeur du spiderchart
     * @param val
     * @param min
     * @param max
     * @return
     */
    public static float calc(float val,float min, float max) {
    	float res=6*val/max;
    	return res;    	
    }

    /**
     * Méthode createDemoPanel : crée le panel affichant le spiderchart
     * @param d
     * @return
     */
    public static JPanel createDemoPanel(String d[][]) {
        JFreeChart jfreechart = createChart(createDataset(d));
        return new ChartPanel(jfreechart);
    }

    /**
     * Méthode affichage : met en place le spiderchart : de sa création à l'affichage ; le retourne
     * @param d
     * @return
     */
    public static SpiderChart affichage(String d[][]) {
    	SpiderChart spider = new SpiderChart(d);
        //spiderwebchartdemo1.pack();
        //RefineryUtilities.centerFrameOnScreen(spiderwebchartdemo1);
        spider.setVisible(true);
        return spider;
    }

}