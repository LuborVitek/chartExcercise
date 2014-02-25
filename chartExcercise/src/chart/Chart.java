package chart;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.ColumnArrangement;
import org.jfree.chart.block.GridArrangement;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map.Entry;




public class Chart {

	LinkedHashMap<String,Double> data = null;
	JFreeChart chart = null;
	
	public Chart() {
		super();
		data = new LinkedHashMap<String,Double>();
	}

	public static void main(String[] args) {
		String inputFileName = "data/piechart-data.xls";
		String outputFileName = "C:\\chart.jpg";
		Chart c = new Chart();
		c.run(inputFileName,outputFileName);
	 }

	public void run(String inputFileName, String outputFileName){
		loadData(inputFileName);
		generate();
		if (chart != null) {
			saveChartAsJPEG(outputFileName);
		} else {
			System.out.println("Chart wasn't generated");
		}
	}
	
	private void generate(){
		if (data == null) {
			System.out.println("No data for Chart generation");
			return;
		}
		DefaultPieDataset pieDataset = populatePieChartDataSet(data);

		chart = ChartFactory.createPieChart(null, pieDataset);
		Font font = chart.getLegend().getItemFont();
		System.out.println(chart.getLegend().getPadding());
		chart.removeLegend();
		
		//Setting new shapes for legend items
		LegendItemCollection legendItemsOld = chart.getPlot().getLegendItems();
		final LegendItemCollection legendItemsNew = new LegendItemCollection();
		
		
		for(int i = 0; i< legendItemsOld.getItemCount(); i++){
		    LegendItem it = legendItemsOld.get(i);
		    it.setShape(new Rectangle(10,10));
		    legendItemsNew.add(it);
		    
		}
		
		LegendItemSource source = new LegendItemSource() {
			LegendItemCollection lic = new LegendItemCollection();
			{
				lic.addAll(legendItemsNew);
			}
		public LegendItemCollection getLegendItems() {  
		    return lic;
		}
		};
		
		//Adding adjusted legend
		LegendTitle title =  new LegendTitle(source,new ColumnArrangement(),new ColumnArrangement());
		title.setHorizontalAlignment(HorizontalAlignment.LEFT);
		title.setPosition(RectangleEdge.BOTTOM);
		//chart.addLegend(title);
		
		BlockContainer legendContainer = new BlockContainer(new GridArrangement(1,2));
		
		//Legend first Column
		BlockContainer firstColumnContainer = new BlockContainer(new ColumnArrangement());
		firstColumnContainer.add(new TextTitle("ANTEIL AM FONDSVERMÖGENS"));
		firstColumnContainer.add(title);
		CompositeTitle firstColumnComposite = new CompositeTitle(firstColumnContainer);
		firstColumnComposite.setPosition(RectangleEdge.BOTTOM);
		legendContainer.add(firstColumnContainer);
		
		//Legend second Column
		BlockContainer secondColumnContainer = new BlockContainer(new ColumnArrangement());
		secondColumnContainer.add(new TextTitle("(%)"));
		for (int i = 0; i<pieDataset.getItemCount();i++){
			TextTitle subtitle = new TextTitle(pieDataset.getValue(i).toString());
			subtitle.setFont(font);
			secondColumnContainer.add(subtitle);
		}
		CompositeTitle secondColumnComposite = new CompositeTitle(secondColumnContainer);
		secondColumnComposite.setPosition(RectangleEdge.BOTTOM);
		legendContainer.add(secondColumnContainer);

		//Legend second Column
		CompositeTitle groupTitle = new CompositeTitle(legendContainer);
		groupTitle.setPosition(RectangleEdge.BOTTOM);
		chart.addSubtitle(groupTitle);
		
		//PiePlot color and label adjustments
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setSimpleLabels(true);
		Paint labelColorTransparent = new Color(255,255,255,0);
		plot.setLabelBackgroundPaint(labelColorTransparent);
		plot.setLabelOutlinePaint(labelColorTransparent);
		plot.setLabelShadowPaint(labelColorTransparent);

		PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{2}", new DecimalFormat("0"), new DecimalFormat("0.0%"));
		
		plot.setLabelGenerator(generator);
		plot.setBackgroundPaint(Color.WHITE);
		plot.setOutlineVisible(false);
	}
	
	private void saveChartAsJPEG(String outputFileName){
		try {
			 ChartUtilities.saveChartAsJPEG(new File(outputFileName), chart, 500, 500);
			 System.out.println("Chart generated to " + outputFileName);
			 } catch (Exception e) {
				 System.out.println("Problem occurred while saving chart.");
			 }
	}
	
	private void loadData(String inputFileName){
		try {
			data = ReadExcel.readFile(inputFileName);
		} catch (IOException e) {
			e.printStackTrace();
			data = null;
		}
	}
	
	private DefaultPieDataset populatePieChartDataSet(LinkedHashMap<String,Double> data){
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		for (Entry<String, Double> e : data.entrySet()){
			pieDataset.setValue(e.getKey(), e.getValue());
		}
		return pieDataset;
	}
}

	
	
	
	
	
	
	
	
	
	
	
