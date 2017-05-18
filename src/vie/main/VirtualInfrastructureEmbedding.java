package vie.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import vie.metrics.BandwidthConsumption;
import vie.metrics.BlockingProbability;

/**
 * VirtualInfranstructureEmbedding class runs all metrics for the simulation.
 * Results are displayed in a JavaFX GUI.
 * @author Carlos M. Galdamez
 */
public class VirtualInfrastructureEmbedding extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// GUI title
		primaryStage.setTitle("Virtual Infrastructure Embedding Results");
		
		Group root = new Group();
		Scene scene = new Scene(root, 1000, 800);
		
		TabPane tabPane = new TabPane();

		BorderPane borderPane = new BorderPane();

		// FIRST TAB: Blocking Probability
		Tab tab1 = new Tab();
		tab1.setText("Blocking Probability");
		HBox hbox1 = new HBox();

		LineChart<Number,Number> graph1 = createGraph("Blocking Probability", "# Of Requests", "Blocking Probability", 15, "blocking");
		graph1.prefWidthProperty().bind(scene.widthProperty());

		hbox1.getChildren().add(graph1);
		hbox1.setAlignment(Pos.CENTER);
		tab1.setContent(hbox1);
		tabPane.getTabs().add(tab1);

		// SECOND TAB: Bandwidth Consumption
		Tab tab2= new Tab();
		tab2.setText("Bandwidth Consumption");
		HBox hbox2 = new HBox();

		LineChart<Number,Number> graph2 = createGraph("Bandwidth Consumption", "# Of Requests", "Bandwidth Consumption", 7, "bandwidth");
		graph2.prefWidthProperty().bind(scene.widthProperty());

		hbox2.getChildren().add(graph2);
		hbox2.setAlignment(Pos.CENTER);
		tab2.setContent(hbox2);
		tabPane.getTabs().add(tab2);

		// THIRD TAB: Blocking Probability vs. Max # of Virtual Nodes
		Tab tab3= new Tab();
		tab3.setText("Blocking Probability VS # of Virtual Nodes");
		HBox hbox3 = new HBox();

		LineChart<Number,Number> graph3 = createGraph("Blocking Probability", "Average Virtual Nodes", "Bandwidth Consumption", 7, "");
		graph3.prefWidthProperty().bind(scene.widthProperty());

		hbox3.getChildren().add(graph3);
		hbox3.setAlignment(Pos.CENTER);
		tab3.setContent(hbox3);
		tabPane.getTabs().add(tab3);

		// bind to take available space
		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());

		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);

		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * Creates a line graph using the available metric.
	 * @param name - Name of the graph
	 * @param x_Axis - Name of X-axis
	 * @param y_Axis - Name of Y-axis
	 * @param numberOfSamples - Number of samples
	 * @param metric - Name of metric to be used
	 * @return - Returns the created line chart
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public LineChart<Number,Number> createGraph(String name, String x_Axis, String y_Axis, int numberOfSamples, String metric) throws IOException{
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel(x_Axis);

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel(y_Axis);

		final LineChart<Number, Number> lineChart = new LineChart<Number,Number>(xAxis,yAxis);
		lineChart.setTitle(name);

		XYChart.Series<Number, Number> series1 = new XYChart.Series<Number, Number>();
		series1.setName("Algortihm 1");
		XYChart.Series<Number, Number> series2 = new XYChart.Series<Number, Number>();
		series2.setName("Algortihm 2");
		XYChart.Series<Number,Number> series3 = new XYChart.Series<Number, Number>();
		series3.setName("Algortihm 3");

		StringBuilder a1 = new StringBuilder("");
		StringBuilder a2 = new StringBuilder("");
		StringBuilder a3 = new StringBuilder("");


		for(int i = 1; i <= numberOfSamples;i++){
			double[] results = (metric.equals("blocking"))?BlockingProbability.averageBlockingProbability(20, i * 100):
				(metric.equals("bandwidth"))?BandwidthConsumption.averageBandwidthConsumption(20, i * 100):
					BlockingProbability.averageBlockingVSRequest(20, i+1);
				series1.getData().add(new XYChart.Data<Number, Number>((metric.equals("blocking") || metric.equals("bandwidth"))?i * 100:(i+3), results[0]));
				series2.getData().add(new XYChart.Data<Number, Number>((metric.equals("blocking") || metric.equals("bandwidth"))?i * 100:(i+3), results[1]));
				series3.getData().add(new XYChart.Data<Number, Number>((metric.equals("blocking") || metric.equals("bandwidth"))?i * 100:(i+3), results[2]));

				a1.append(results[0] + " ");
				a2.append(results[1] + " ");
				a3.append(results[2] + " ");


		}

		System.out.println("----------------- " + name + "--------------------\n");
		System.out.println(a1.toString());
		System.out.println(a2.toString());
		System.out.println(a3.toString());
		System.out.println("----------------------------------------------------");


		lineChart.getData().addAll(series1,series2,series3);
		return lineChart;

	}

	public static void main(String args[]) throws IOException{
		launch(args);
	}
}
