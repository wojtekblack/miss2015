package pl.edu.agh.miss.chart;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public abstract class Chart {
	protected String title, xTitle, yTitle;
	protected int [] size = new int [] {640, 480};
	
	public Chart setTitle(String title){
		this.title = title;
		return this;
	}
	
	public Chart setXAxisTitle(String title){
		this.xTitle = title;
		return this;
	}
	
	public Chart setYAxisTitle(String title){
		this.yTitle = title;
		return this;
	}
	
	public Chart setWidth(int width){
		size[0] = width;
		return this;
	}
	
	public Chart setHeight(int height){
		size[1] = height;
		return this;
	}
	
	public void save(String fileName){
		File file = new File(fileName);
		try {
			file.createNewFile();
			save(file);
			System.out.println("Chart saved at " + file.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
		String fileName = "results/chart_" + timeStamp + ".jpg";
		save(fileName);
	}
	
	public abstract Chart addSeries(String name, List<Point> values);
	
	protected abstract void save(File file) throws IOException;
}
