package pl.edu.agh.miss.chart;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ScatterChart extends Chart{
	private Map<String, List<Point>> data;
	
	public ScatterChart(){
		data = new HashMap<String, List<Point>>();
	}

	@Override
	public Chart addSeries(String name, List<Point> values) {
		data.put(name, values);
		return this;
	}

	@Override
	protected void save(File file) throws IOException {
		//create series
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for(String key : data.keySet()){
			XYSeries series = new XYSeries(key);
			
			for(Point point : data.get(key)){
				series.add(point.x, point.y);
			}
			
			dataset.addSeries(series);
		}
		
		JFreeChart chart = ChartFactory.createScatterPlot(title, xTitle, yTitle, dataset);
		XYPlot plot = (XYPlot) chart.getPlot();
		LogAxis logAxis = new LogAxis(yTitle);
		plot.setRangeAxis(logAxis);
		
		ChartUtilities.saveChartAsJPEG(file, chart, size[0], size[1]);
	}
}
