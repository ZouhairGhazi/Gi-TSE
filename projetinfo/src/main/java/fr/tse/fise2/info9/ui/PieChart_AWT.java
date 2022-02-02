package fr.tse.fise2.info9.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Cette classe est une interface qui hérite du JPanel, est qui renvoie un pie chart des langages utilisés dans un projet.
 */
 
public class PieChart_AWT extends JPanel {
   
   
	
	private static final long serialVersionUID = 1L;

	/** 
	 * Le constructeur de la classe, prend le titre du graph et l'objet MAP pour avoir les langages utilisés et leurs pourcentages.
	 * 
	 * @param title
	 * @param languages
	 */
	public PieChart_AWT(String title, Map<String,Float> languages) {
      JPanel jpanel = createDemoPanel(title, languages);
      jpanel.setPreferredSize(new Dimension(410, 260));
      add(jpanel);
	}
   
	/**
	 * La création du dataset à l'objet map passé en paramteres.
	 * 
	 * @param languages
	 * @return PieDataset
	 */
	private static PieDataset<String> createDataset(Map<String,Float> languages) {
      DefaultPieDataset<String> dataset = new DefaultPieDataset<String>( );
      for (Entry<String, Float> entry : languages.entrySet()) {
          dataset.setValue(entry.getKey(), entry.getValue());
      }
      return dataset;         
   }
   
	/**
	 * La construction du graph, en indiquant un titre et la dataset utilisée.
	 * 
	 * @param title
	 * @param dataset
	 * @return JFreeChart
	 */
   private static JFreeChart createChart(String title, PieDataset<String> dataset) {
      JFreeChart chart = ChartFactory.createPieChart(      
    	 title,   // chart title 
         dataset,          // data    
         true,             // include legend   
         true, 
         false);

      TextTitle titre = chart.getTitle();
      titre.setFont(new Font("Verdana", Font.PLAIN, 18));
      return chart;
   }
   
   public static JPanel createDemoPanel(String title, Map<String,Float> languages) {
      JFreeChart chart = createChart(title, createDataset(languages));  
      return new ChartPanel(chart); 
   }
   
   public static PieChart_AWT affichage(String title, Map<String,Float> languages) {
	   PieChart_AWT piechart = new PieChart_AWT(title, languages);
	   piechart.setVisible(true);
	   return piechart;
   }
}