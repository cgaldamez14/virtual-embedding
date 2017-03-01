package vie.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.stage.Stage;
import vie.metrics.BandwidthConsumption;
import vie.metrics.BlockingProbability;


public class VirtualInfrastructureEmbedding extends Application {
	
	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Blocking Probability");
		final NumberAxis xAxis = new NumberAxis(0d, 725d, 50);
		xAxis.setLabel("# of Requests");
		
		final NumberAxis yAxis = new NumberAxis(-1d, 22d, 1);
		yAxis.setLabel("Blocking Probability");
		
		final LineChart<Number, Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
/*		lineChart.setTitle("BandwidthConsumption");
		
        XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
        series1.setName("Algortihm 1");
        
        for(int i = 1; i <= 14;i++){
        	int consumption = BandwidthConsumption.averageBandwidthConsumption(20, i * 50, 1);
            series1.getData().add(new XYChart.Data<Number, Number>(i * 50, consumption));

        }
        
        
        XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
        series2.setName("Algortihm 2");
        
        for(int i = 1; i <= 14;i++){
        	int consumption = BandwidthConsumption.averageBandwidthConsumption(20, i * 50, 2);
            series2.getData().add(new XYChart.Data<Number, Number>(i * 50, consumption));

        }
        
        XYChart.Series<Number,Number> series3 = new Series<Number, Number>();
        series3.setName("Algortihm 3");
        for(int i = 1; i <= 14;i++){
        	int consumption = BandwidthConsumption.averageBandwidthConsumption(20, i * 50, 3);
            series3.getData().add(new Data<Number, Number>(i * 50, consumption));

        }*/
        
		lineChart.setTitle("Blocking Probability");
		
        XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
        series1.setName("Algortihm 1");
        
        for(int i = 1; i <= 7;i++){
        	double blockingProbability = BlockingProbability.averageBlockingProbability(40, i * 100, 1);
            series1.getData().add(new XYChart.Data<Number, Number>(i * 100, blockingProbability));

        }
        
        
        XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
        series2.setName("Algortihm 2");
        
        for(int i = 1; i <= 7;i++){
        	double blockingProbability = BlockingProbability.averageBlockingProbability(40, i * 100, 1);
            series2.getData().add(new XYChart.Data<Number, Number>(i * 100, blockingProbability));

        }
        
        XYChart.Series<Number,Number> series3 = new Series<Number, Number>();
        series3.setName("Algortihm 3");
        for(int i = 1; i <= 7;i++){
        	double blockingProbability = BlockingProbability.averageBlockingProbability(40, i * 100, 1);
            series3.getData().add(new XYChart.Data<Number, Number>(i * 100, blockingProbability));

        }
        

        
        Scene scene  = new Scene(lineChart,800,600);       
        lineChart.getData().addAll(series1,series2,series3);
       
        primaryStage.setScene(scene);
        primaryStage.show();

	}
	
	public static void main(String args[]) throws IOException{
		//Simulator simulator = new Simulator(NetworkTopology.US_MESH);
		//Simulator simulator = new Simulator(NetworkTopology.NSFNET);
		//simulator.start();	
		
		launch(args);
	}
}
