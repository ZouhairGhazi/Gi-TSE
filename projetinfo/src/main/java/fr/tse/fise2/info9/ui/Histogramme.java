package fr.tse.fise2.info9.ui;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.gitlab4j.api.models.MergeRequest;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
 
/**
 * 
 * Classe Histogramme : JPanel contenant l'histogramme permettant de voir les merge requests et les merged (merge requests qui ont fonctionné) par mois
 *
 */
public class Histogramme extends JPanel{

	private static final long serialVersionUID = 1L;
	public static int nbexperiences = 2000;
	public int nbMerges;
    public int N = 4;
    public static int nbbarres = 5;
     
    /**
     * Constructeur : crée un histogramme à partir d'une liste de MergeRequests
     * @param merges
     */
    public Histogramme( List<MergeRequest> merges) { 
          JFreeChart barChart = ChartFactory.createBarChart(
             "Merge requests",   
             null,
             "Frequence",
             createDataset(merges),
             PlotOrientation.VERTICAL,
             true, true, false);
              

          ChartPanel chartPanel = new ChartPanel( barChart );  
          chartPanel.setPreferredSize(new java.awt.Dimension( 650 , 230 ) );
          
          TextTitle title = barChart.getTitle();
          title.setFont(new Font("Verdana", Font.PLAIN, 18));

          add( chartPanel );
          
       }
     
    /**
     * Méthode createDataset : crée le dataset pour l'histogramme en fonction de la liste de merge requests passées en paramètres
     * @param merges
     * @return
     */
    private CategoryDataset createDataset( List<MergeRequest> merges) {
    	
    	List<Integer> months =  new ArrayList<>();
    	List<Integer> monthsMerged =  new ArrayList<>();
    	int nbRequests=0;
    	int [] compte= {0,0,0,0,0,0,0,0,0,0,0,0};
    	int [] compteM= {0,0,0,0,0,0,0,0,0,0,0,0};
    	nbMerges=0;
    	
    	// utilisation de la classe Calendar pour récupérer le mois de chaque merge requests et les classer
    	Calendar date = Calendar.getInstance();
    	for (Iterator<MergeRequest> iterator = merges.iterator(); iterator.hasNext();) {
			MergeRequest mergeRequest = (MergeRequest) iterator.next();	
    		date.setTime(mergeRequest.getCreatedAt());
			months.add(date.get(Calendar.MONTH));
			compte[months.get(nbMerges)]++;
			if (mergeRequest.getMergedAt()!=(null)) {
				date.setTime(mergeRequest.getMergedAt());
				monthsMerged.add(date.get(Calendar.MONTH));
				compteM[monthsMerged.get(nbRequests)]++;
				nbRequests++;
			}
			nbMerges++;
		}
    	int maxi=0;
    	
    	for(int i=0;i<12;i++) {
    		if(compte[i]>maxi)
    			maxi=compte[i];
    		if(compte[i]>0) {
    			nbbarres++;
    		}
    	}
    	String legendes[] = new String[nbbarres]; //légende abscisses
    	int valeurs[] = new int[nbbarres]; //valeurs
    	int valeursM[] = new int[nbbarres];
    	int truei=0;
    	for(int i=8;i<12;i++) {
    		if(compte[i]>0) {
    			legendes[truei] = getMonth(i);
    			valeurs[truei] = compte[i];
    			valeursM[truei] = compteM[i];
    			truei++;
    		}
    	}
    	for(int i=0;i<8;i++) {
    		if(compte[i]>0) {
    			legendes[truei] = getMonth(i);
    			valeurs[truei] = compte[i];
    			valeursM[truei] = compteM[i];
    			truei++;
    		}
    	}
    	
        final String total = "merge requests";       
        final String merged = "merged";
        
        
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
       
         
        for (int i=0;i<truei;i++) {
            dataset.addValue( valeurs[i] , total , legendes[i] ); 
            dataset.addValue( valeursM[i] , merged , legendes[i] );
        }
        return dataset;
    }
     
   /**
    * Méthode getMonth : renvoie le mois de l'année en français (méthode existante mais en anglais seulement)
    * @param m
    * @return
    */
   public String getMonth(int m) {
	   switch (m) {
	case 0:
		return "Janvier";
	case 1:
		return "Février";
	case 2:
		return "Mars";
	case 3:
		return "Avril";
	case 4:
		return "Mai";
	case 5:
		return "Juin";
	case 6:
		return "Juillet";
	case 7:
		return "Août";
	case 8:
		return "Septembre";
	case 9:
		return "Octobre";
	case 10:
		return "Novembre";
	case 11:
		return "Décembre";
	default:
		return "Décembre";
	}
   }
}