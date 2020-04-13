
/**
 * @author Theodoulos Christou
 * Name: Theodoulos 
 * SurnName: Christou
 * Student ID: 966851
 * I assume that this CourseWork is my own work.
 **/


/*
CS-255 Getting started code for the assignment
I do not give you permission to post this code online
Do not post your solution online
Do not copy code
Do not use JavaFX functions or other libraries to do the main parts of the assignment:
	Gamma Correction
	Contrast Stretching
	Histogram calculation and equalisation
	Cross correlation
All of those functions must be written by yourself
You may use libraries to achieve a better GUI
*/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.lang.Math;

import javax.swing.JOptionPane;



public class Photoshop extends Application {

	@Override
	public void start(Stage stage) throws FileNotFoundException {
		stage.setTitle("Photoshop");

		// Read the image
		Image image = new Image(new FileInputStream("raytrace.jpg"));

		// Create the graphical view of the image
		ImageView imageView = new ImageView(image);

		// Create the simple GUI
		Button invert_button = new Button("Invert");
		Button gamma_button = new Button("Gamma Correct");
		Button contrast_button = new Button("Contrast Stretching");
		Button histogram_button = new Button("Histograms");
		Button cc_button = new Button("Cross Correlation");

		// Add all the event handlers (this is a minimal GUI - you may try to do better)
		invert_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Invert");
				// At this point, "image" will be the original image
				// imageView is the graphical representation of an image
				// imageView.getImage() is the currently displayed image

				// Let's invert the currently displayed image by calling the invert function
				// later in the code
				Image inverted_image = ImageInverter(imageView.getImage());
				// Update the GUI so the new image is displayed
				imageView.setImage(inverted_image);
			}
		});
		
		
		//This is the GUI for Gamma Correction
		gamma_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Gamma Correction");

				// create a stage for Gamma Correction
				Stage popout = new Stage();

				// create a VBox named layout
				VBox layout = new VBox();
				//Create a HBox named hBox
				HBox hbox = new HBox();
				
				// create a Label named gammaLabel
				Label gammaLabel = new Label("Enter a gamma value");
				
				// create a Button named exit
				Button exit = new Button("Exit");
				
				//creates a slider that User
				//can change values for Gamma Correction
				//and sets all the details that slider needs.
				Slider slider = new Slider();
				slider.setMin(0);
				slider.setMax(20);
				slider.setValue(5);
				slider.setShowTickLabels(true);
				slider.setShowTickMarks(true);
				slider.setMajorTickUnit(5);
				slider.setMinorTickCount(5);
				slider.setBlockIncrement(10);
				slider.setPrefWidth(10);
			
				
		   //put a listener to slider for recognise the old_val and new_val
		   //when the User change the slider.It takes the image as it is and then 
		   //it calls the method of lookUpTable to take the value and finally it sets the new Image
		   //according to the new Value of Slider.
		   slider.valueProperty().addListener(new ChangeListener<Number>() {
			            public void changed(ObservableValue<? extends Number> observeV,
			                Number oldVal, Number newVal) {
			            	imageView.setImage(image);
			            	lookUpTable(newVal.doubleValue());
			            	Image new_gamma = ImageGamma(imageView.getImage(),newVal.doubleValue());
							// Update the GUI so the new image is displayed
							imageView.setImage(new_gamma);
							
			            }
			        });
				

				// create an action to close the stage
				exit.setOnAction(e -> {
					popout.close();
				});
				
				//set the position of Button exit
				exit.setAlignment(Pos.CENTER);

				// set all the values to the VBox
				layout.getChildren().addAll(gammaLabel,slider,exit);
				
				//set the VBox inside HBox
				hbox.getChildren().add(layout);

				// set the position of hbox to CENTER
				hbox.setAlignment(Pos.CENTER);
				

				// create a new Scene that puts hbox
				Scene scene1 = new Scene(hbox,400,200);
				
				//put the scene into stage
				popout.setScene(scene1);
				
				//show the stage
				popout.showAndWait();
			}
		});
		
		
		//This is the GUI for Contrast Stretching
		contrast_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Contrast Stretching");
				
				// create a stage for Contrast Stretching
				Stage contrastStretchingStage = new Stage();
				
				//Title for stage of Contrast Stretching
				contrastStretchingStage.setTitle("Contrast Stretching for Image");
				
				//create a pane for Contrast Stretching
				Pane canvas = new Pane();
				
				//Create a VBox for Contrast Stretching
				VBox contrastStretch = new VBox(5);
				
				//Title for VBox of Contrast Stretching
				contrastStretch.setTranslateX(50);
				
				//Create an HBox for Contrast Stretching
				HBox contrastHBox = new HBox(10);
				
				//Button for set the values of Contrast Stretching
				Button setButton = new Button();
				
				//Text inside the button
				setButton.setText("Set Values");
				
				//Details for pane of Contrast Stretching
				canvas.setPrefSize(255, 255);
				canvas.setMaxHeight(255);
				canvas.setMaxWidth(255);
				canvas.setTranslateX(100);

				//Create texts to get the values
				//for circles of Contrast Stretching
				//when User move the circles in Pane.
				TextField R1text = new TextField();
				TextField S1text = new TextField();
				TextField R2text = new TextField();
				TextField S2text = new TextField();
				
				//Create Labels that sets Title for TextFields.
				Label label1 = new Label("Value of R1");
				Label label2 = new Label("Value of S1");
				Label label3 = new Label("Value of R2");
				Label label4 = new Label("Value of S2");

				//Coordinates for two Circles.
				int c1XCoordinate = 89;
				int c1YCoordinate = 169;
				int c2XCoordinate = 170;
				int c2YCoordinate = 92;
				
				//Create lines and Circles for Contrast Stretching Pane.
				//l1 is for coordinates of first circle,and starts from 0 to 255.
				//l2 is between of circle1 and circle2,
				//l3 is for coordinates of second circle,and starts from 255 to 0.
				//c1 takes coordinates of r1,s1.
				//c2 takes coordinates of r2,s2.
				Line l1 = new Line(0, 255, c1XCoordinate, c1YCoordinate);
				Line l2 = new Line(c1XCoordinate, c1YCoordinate, c2XCoordinate, c2YCoordinate);
				Line l3 = new Line(c2XCoordinate, c2YCoordinate, 255, 0);
				Circle c1 = new Circle(c1XCoordinate, c1YCoordinate, 7, Color.WHITE);
				Circle c2 = new Circle(c2XCoordinate, c2YCoordinate, 7, Color.WHITE);
				
				
				//puts the circle1 to setOnMouseDragged and allows to User to
				//move the circle into the pane,and can drag only circle1
				//so the first line and second moves but third is constant,
				//therefore checks when the circle1 getting outOfBounds on Pane
				//and return it back into Pane.
				c1.setOnMouseDragged(e -> {
					if ((e.getX() > c2.getCenterX())) {
						/*
						 * if the action e in x axis is greater than the centerX of circle2
						 * then it stopped it, and allows the second circle to move,because
						 * it don't let the e action in x axis to pass the circle2 x axis.
						 */
						return;
					} else if ((e.getX() > 255) || (e.getY() > 255) || (e.getX() < 0) || (e.getY() < 0)) {
						/*
						 * if the action e in x axis is greater than 255, or 
						 * if the action e in y axis is greater than 255, or
						 * if the action e in x axis is less than 0, or
						 * if the action e in y axis is less than 0
						 * then return it back to pane.
						 */
						return;
					} else {
						/*
						 * if it isn't outOfBounds then get
						 * Centres of c1 in x,y axis,
						 * line1 ends in x axis of c1 centre x
						 * line1 ends in y axis of c1 centre y
						 * line2 starts in x axis of c1 centre x
						 * line2 starts in y axis of c1 centre y
						 * text1 for circle1 x axis
						 * text2 for circle1 y axis that is minus by 255.
						 * Finally calls the method of lookUpTableContrast 
						 * to pass all the values of circles inside the texts
						 * and sets the new image according coordinates of circles.
						 */
						c1.setCenterX(e.getX());
						c1.setCenterY(e.getY());
						l1.setEndX(c1.getCenterX());
						l1.setEndY(c1.getCenterY());
						l2.setStartX(c1.getCenterX());
						l2.setStartY(c1.getCenterY());
						R1text.setText(String.valueOf(c1.getCenterX()));
						S1text.setText(String.valueOf(255 - c1.getCenterY()));
						
						lookUpTableContrast(c1.getCenterX(), 255.00-c1.getCenterY(),c2.getCenterX(),255.00-c2.getCenterY());
						
						Image new_contrast = ImageContrast(image);
						// Update the GUI so the new image is displayed
						imageView.setImage(new_contrast);
					}
					
				});
				
				
				//puts the circle2 to setOnMouseDragged and allows to User to
				//move the circle into the pane,and can drag only circle2
				//so the second line and third moves but first is constant,
				//therefore checks when the circle2 getting outOfBounds on Pane
				//and return it back into Pane.
				c2.setOnMouseDragged(e -> {
					if ((e.getX() < c1.getCenterX())) {
						/*
						 * if the action e in x axis is less than the centerX of circle1
						 * then it stopped it, and allows the first circle to move,because
						 * it don't let the e action in x axis to pass the circle1 x axis.
						 */
						return;
					} else if ((e.getX() > 255) || (e.getY() > 255) || (e.getX() < 0) || (e.getY() < 0)) {
						/*
						 * if the action e in x axis is greater than 255, or 
						 * if the action e in y axis is greater than 255, or
						 * if the action e in x axis is less than 0, or
						 * if the action e in y axis is less than 0
						 * then return it back to pane.
						 */
						return;
					} else {
						/*
						 * if it isn't outOfBounds then get
						 * Centres of c2 in x,y axis,
						 * line2 ends in x axis of c1 centre x
						 * line2 ends in y axis of c1 centre y
						 * line3 starts in x axis of c1 centre x
						 * line3 starts in y axis of c1 centre y
						 * text3 for circle2 x axis
						 * text4 for circle2 y axis that is minus by 255.
						 * Finally calls the method of lookUpTableContrast 
						 * to pass all the values of circles inside the texts
						 * and sets the new image according coordinates of circles.
						 */
						c2.setCenterX(e.getX());
						c2.setCenterY(e.getY());
						l2.setEndX(c2.getCenterX());
						l2.setEndY(c2.getCenterY());
						l3.setStartX(c2.getCenterX());
						l3.setStartY(c2.getCenterY());
						R2text.setText(String.valueOf(c2.getCenterX()));
						S2text.setText(String.valueOf(255 - c2.getCenterY()));
						
						lookUpTableContrast(c1.getCenterX(),255.0-c1.getCenterY(),c2.getCenterX(),255.0-c2.getCenterY());
						Image new_contrast = ImageContrast(image);
						// Update the GUI so the new image is displayed
						imageView.setImage(new_contrast);
					}
					
				});
				
				
				/*
				 * puts the setButton to setOnAction and allows to User
				 * to set the values of coordinates for circles into the texts
				 * and set them by press the button set.
				 */
				setButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e3) {
						/*
					 	 * that method takes
						 * Centres of c2 and c1 in x,y axis
						 * and pass them into texts,
						 * line1 ends in x axis of c1 centre x,
						 * line1 ends in y axis of c1 centre y,
						 * line2 starts in x axis of c1 centre x,
						 * line2 starts in y axis of c1 centre y,
						 * line3 ends in x axis of c2 centre x,
						 * line3 ends in y axis of c2 centre y,
						 * Finally calls the method of lookUpTableContrast, 
						 * to pass all the values of circles inside the texts,
						 * and sets the new image according coordinates of circles.
						 */
						c1.setCenterX(Double.valueOf(R1text.getText()));
						c1.setCenterY(255.0-Double.valueOf(S1text.getText()));
						c2.setCenterX(Double.valueOf(R2text.getText()));
						c2.setCenterY(255.0-Double.valueOf(S2text.getText()));
						l1.setEndX(c1.getCenterX());
						l1.setEndY(c1.getCenterY());
						l2.setStartX(c1.getCenterX());
						l2.setStartY(c1.getCenterY());
						l2.setEndX(c2.getCenterX());
						l2.setEndY(c2.getCenterY());
						l3.setStartX(c2.getCenterX());
						l3.setStartY(c2.getCenterY());
						lookUpTableContrast(Double.valueOf(R1text.getText()),Double.valueOf(S1text.getText()),Double.valueOf(R2text.getText()),Double.valueOf(S2text.getText()));
						Image new_contrast = ImageContrast(image);
						// Update the GUI so the new image is displayed
						imageView.setImage(new_contrast);
					}
				});
				
				
				//add inside to pane the lines and circles
				canvas.getChildren().addAll(l1, l2, l3, c1, c2);
				
				//set background of pane into light blue
				canvas.setStyle("-fx-background-color: #ADD8E6");
				
				//add inside VBox all the labels the texts and the setButton
				contrastStretch.getChildren().addAll(label1, R1text, label2, S1text, label3, R2text, label4, S2text,setButton);
				
				//add into HBox the VBox
				contrastHBox.getChildren().addAll(contrastStretch,canvas);
				
				//set spaces for HBox
				contrastHBox.setPadding(new Insets(4, 4, 4, 4));
				
				//add in scene the HBox and distances of Scene
				Scene contrastScene = new Scene(contrastHBox,600,300);
				
				//set the scene into stage 
				contrastStretchingStage.setScene(contrastScene);
				
				//show the stage
				contrastStretchingStage.show();
			}
		});
		
		
		
		//This is the GUI for Histograms
		histogram_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Histogram");
				
				
				Image new_histogram = ImageHistogram(imageView.getImage());
				// Update the GUI so the new image is displayed
				imageView.setImage(image);
				
				
				//creates stage for Histograms
				//and sets a title.
				Stage hS = new Stage();
				hS.setTitle("Chart Of Histogram");
				
				//creates a VBox for Histograms
				VBox histogramVBox = new VBox();
				
				//create hBox for Histograms
				HBox h = new HBox();
				
				//creates x axis for LineChart of Histograms
				//and sets a title
				NumberAxis x = new NumberAxis(0, 255, 10);
				x.setLabel("Pixel");
				
				//creates y axis for LineChart of Histograms
				//and sets a title
				NumberAxis y = new NumberAxis(0,40000,1000);
				y.setLabel("Numbers of Pixels");
				
				//creates a LineChart for Histograms
				//and sets the symbols false.
				LineChart chart = new LineChart(x, y);
				chart.setCreateSymbols(false);
				
				//creates XYCharts for LineChart of Histograms
				//in colour of Red,Green,Blue,Brightness,Equalisation
				XYChart.Series histogramOfRed = new XYChart.Series();
				histogramOfRed.setName("Color Red");
				XYChart.Series histogramOfGreen = new XYChart.Series();
				histogramOfGreen.setName("Color Green");
				XYChart.Series histogramOfBlue = new XYChart.Series();
				histogramOfBlue.setName("Color Blue");
				XYChart.Series histogramOfBrightness = new XYChart.Series();
				histogramOfBrightness.setName("Brightness");
				XYChart.Series histogramOfEqualization = new XYChart.Series();
				histogramOfEqualization.setName("Histogram Equalisation");
				
				
				//creates checkBoxes for colours
				//RGB,Brightness,Equalisation
				//and sets texts inside them.
				CheckBox checkBoxRed = new CheckBox();
				CheckBox checkBoxGreen = new CheckBox();
				CheckBox checkBoxBlue = new CheckBox();
				CheckBox checkBoxBrightness = new CheckBox();
				CheckBox checkBoxEqualization = new CheckBox();
				checkBoxRed.setText("Red ");
				checkBoxGreen.setText("Green ");
				checkBoxBlue.setText("Blue ");
				checkBoxBrightness.setText("Brightness ");
				checkBoxEqualization.setText("Histogram Equalization ");
				
				//this loop gets all the data for Histograms of
				//Red,Green,Blue,Brightness.
				for (int i = 0; i < 255; i++) {
					histogramOfRed.getData().add(new XYChart.Data(i, histogram[i][0]));
					histogramOfGreen.getData().add(new XYChart.Data(i, histogram[i][1]));
					histogramOfBlue.getData().add(new XYChart.Data(i, histogram[i][2]));
					histogramOfBrightness.getData().add(new XYChart.Data(i, histogram[i][3]));
				}
				
				
				  //puts a listener for checkBoxRed and checks if it is a newVal then 
				  //put the other checkBoxes as false and take the image and Data for the Image
				  checkBoxRed.selectedProperty().addListener(new ChangeListener<Boolean>() {
				        public void changed(ObservableValue<? extends Boolean> observVal,
				            Boolean oldVal, Boolean newVal) {
				        	if (newVal) {
				        		checkBoxRed.setSelected(true);
			        			checkBoxBlue.setSelected(false);
			        			checkBoxBrightness.setSelected(false);
			        			checkBoxEqualization.setSelected(false);
			        			checkBoxGreen.setSelected(false);
				            	Image new_histogram = ImageHistogram(imageView.getImage());
								// Update the GUI so the new image is displayed
								imageView.setImage(image);
				            	chart.getData().removeAll(histogramOfGreen, histogramOfBlue, histogramOfBrightness,histogramOfEqualization);
								chart.getData().add(histogramOfRed);
				        	}
				        }
				 });
				        	
				
				  //puts a listener for checkBoxGreen and checks if it is a newVal then 
				  //put the other checkBoxes as false and take the image and Data for the Image
				  checkBoxGreen.selectedProperty().addListener(new ChangeListener<Boolean>() {
				        public void changed(ObservableValue<? extends Boolean> ov,
				            Boolean old_val, Boolean new_val) {
				        	if (new_val) {
				        			checkBoxGreen.setSelected(true);
				        			checkBoxRed.setSelected(false);
				        			checkBoxBlue.setSelected(false);
				        			checkBoxBrightness.setSelected(false);
				        			checkBoxEqualization.setSelected(false);
				            		Image new_histogram = ImageHistogram(imageView.getImage());
									// Update the GUI so the new image is displayed
									imageView.setImage(image);
				            		chart.getData().removeAll(histogramOfRed,histogramOfBlue,histogramOfBrightness,histogramOfEqualization);
									chart.getData().add(histogramOfGreen);
				            	}
				        	}
		        	});
				  
				
				  //puts a listener for checkBoxBlue and checks if it is a newVal then 
				  //put the other checkBoxes as false and take the image and Data for the Image
				  checkBoxBlue.selectedProperty().addListener(new ChangeListener<Boolean>() {
				        public void changed(ObservableValue<? extends Boolean> ov,
				            Boolean old_val, Boolean new_val) {
						        	if (new_val) {
					            		checkBoxBlue.setSelected(true);
					            		checkBoxGreen.setSelected(false);
					        			checkBoxRed.setSelected(false);
					        			checkBoxBrightness.setSelected(false);
					        			checkBoxEqualization.setSelected(false);
					            		Image new_histogram = ImageHistogram(imageView.getImage());
										// Update the GUI so the new image is displayed
										imageView.setImage(image);
										chart.getData().removeAll(histogramOfRed, histogramOfGreen, histogramOfBrightness,histogramOfEqualization);
										chart.getData().add(histogramOfBlue);
					            	}
				          }
				        });
				
				  
				  //puts a listener for checkBoxBrightness and checks if it is a newVal then 
				  //put the other checkBoxes as false and take the image and Data for the Image
				  checkBoxBrightness.selectedProperty().addListener(new ChangeListener<Boolean>() {
				        public void changed(ObservableValue<? extends Boolean> ov,
				            Boolean old_val, Boolean new_val) {
				        	if (new_val) {
			            		checkBoxBrightness.setSelected(true);
			            		checkBoxGreen.setSelected(false);
			        			checkBoxRed.setSelected(false);
			        			checkBoxBlue.setSelected(false);
			        			checkBoxEqualization.setSelected(false);
			            		Image new_histogram = ImageHistogram(imageView.getImage());
								// Update the GUI so the new image is displayed
								imageView.setImage(image);
								chart.getData().removeAll(histogramOfRed,histogramOfGreen,histogramOfBlue,histogramOfEqualization);
								chart.getData().add(histogramOfBrightness);
			            	}
				        }
				  });   
				
				  
				  //puts a listener for checkBoxEqualization and checks if it is a newVal then 
				  //put the other checkBoxes as false and take the image and Data for the Image
				  checkBoxEqualization.selectedProperty().addListener(new ChangeListener<Boolean>() {
				        public void changed(ObservableValue<? extends Boolean> ov,
				            Boolean old_val, Boolean new_val) {
				        	if (new_val) {
			            		checkBoxEqualization.setSelected(true);
			            		checkBoxGreen.setSelected(false);
			        			checkBoxRed.setSelected(false);
			        			checkBoxBlue.setSelected(false);
			        			checkBoxBrightness.setSelected(false);
			            		Image new_histogram_eq = histogramEqualization(imageView.getImage());
								for (int i = 0; i < 255; i++) {
									histogramOfEqualization.getData().add(new XYChart.Data(i, histogramEqualizationArray[i][4]));
								}
								chart.getData().removeAll(histogramOfRed,histogramOfGreen,histogramOfBlue,histogramOfBrightness);
								chart.getData().add(histogramOfEqualization);
								// Update the GUI so the new image is displayed
								imageView.setImage(new_histogram_eq);
			            	}
				        }
				}); 
				  
				  
				  	//adds chart and hBox inside VBox
				  	//adds in each hBoxes the colour 
				  	//that belongs to them.
					histogramVBox.getChildren().addAll(chart,h);
					
					
					//adds in hBox all the hBoxes
					//and sets the position 
					//in centre
					h.getChildren().addAll(checkBoxRed,checkBoxGreen,checkBoxBlue,checkBoxBrightness,checkBoxEqualization);
					h.setAlignment(Pos.CENTER);
					
					//sets the VBox inside scene
					Scene histogramScene = new Scene(histogramVBox);
					
					//sets inside stage the scene
					hS.setScene(histogramScene);
					
					//shows the stage
					hS.show();
				}
			});
		
		
		
		//This is the GUI for Cross Correlation
		cc_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Cross Correlation");
				Image cross_correlation = crossCorrelation(imageView.getImage());
				imageView.setImage(cross_correlation);
			}
		});

		// Using a flow pane
		FlowPane root = new FlowPane();
		// Gaps between buttons
		root.setVgap(10);
		root.setHgap(5);

		// Add all the buttons and the image for the GUI
		root.getChildren().addAll(invert_button, gamma_button, contrast_button, histogram_button, cc_button, imageView);

		// Display to user
		Scene scene = new Scene(root, 1024, 768);
		stage.setScene(scene);
		stage.show();
	}

	// Example function of invert
	public Image ImageInverter(Image image) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		// Create a new image of that width and height
		WritableImage inverted_image = new WritableImage(width, height);
		// Get an interface to write to that image memory
		PixelWriter inverted_image_writer = inverted_image.getPixelWriter();
		// Get an interface to read from the original image passed as the parameter to
		// the function
		PixelReader image_reader = image.getPixelReader();

		// Iterate over all pixels
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = image_reader.getColor(x, y);
				// Do something (in this case invert) - the getColor function returns colours as
				// 0..1 doubles (we could multiply by 255 if we want 0-255 colours)
				color = Color.color((1.0 - color.getRed()), 1.0 - color.getGreen(), 1.0 - color.getBlue());
				// Note: for gamma correction you may not need the divide by 255 since getColor
				// already returns 0-1, nor may you need multiply by 255 since the Color.color
				// function consumes 0-1 doubles.

				// Apply the new colour
				inverted_image_writer.setColor(x, y, color);
			}
		}
		return inverted_image;
	}

	/**
	 * This method takes the Image and with all the
	 * calculations that needs,according of gamma value 
	 * sets a new Image for Gamma Correction with new gamma value.
	 * 
	 * @param image is the reference that takes for Gamma Correction.
	 * @param gamma is the value that takes for Gamma Correction.
	 * @return new_gamma, is the new Gamma Correction Image.
	 */
	public Image ImageGamma(Image image, double gamma) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		// Create a new image of that width and height
		WritableImage new_gamma = new WritableImage(width, height);
		
		// Get an interface to write to that image memory
		PixelWriter new_gamma_writer = new_gamma.getPixelWriter();

		// Get an interface to read from the original image passed as the parameter to
		// the function
		PixelReader new_gamma_reader = image.getPixelReader();
		
		// Iterate over all pixels
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = new_gamma_reader.getColor(x, y);
				//In Case of Gamma Correction the getColor function returns the
				//new colour of new gamma Image therefore it takes the table and apply 
				//each colour by multiply it by 255.
				color = Color.color(table[(int)(color.getRed() * 255)], table[(int) (color.getGreen() * 255)],
						table[(int)(color.getBlue() * 255)]);
				
				// Apply the new colour
				new_gamma_writer.setColor(x, y, color);
			}
		}
		return new_gamma;
	}
	
	
	//Array of LookUpTable
	double[] table = new double[256];
	
	/**
	 * This method takes new_gamma as parameter and apply 
	 * a calculation to find the new gamma correction value
	 * for the new Image.
	 * @param new_gamma reference for gamma input value.
	 */
	public void lookUpTable(double new_gamma) {
		for (int i = 0; i < table.length; i++) {
			table[i] = ((255 * (Math.pow((double) i / 255.0, 1 / new_gamma)) / 255));
		}
	}
	
	
	
	/**
	 * This method takes the Image as it is and then apply 
	 * the calculations that needs,according the table of 
	 * Contrast Stretching and multiply each colour by 255 and 
	 * sets the new Image of Contrast Stretching.
	 * @param image the reference of image as it is.
	 * @return new_contrast, is the new image of Contrast Image.
	 */
	public Image ImageContrast(Image image) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();

		// Create a new image of that width and height
		WritableImage new_contrast = new WritableImage(width, height);
		
		// Get an interface to write to that image memory
		PixelWriter new_contrast_writer = new_contrast.getPixelWriter();
		
		// Get an interface to read from the original image passed as the parameter to
		// the method.
		PixelReader new_contrast_reader = image.getPixelReader();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = new_contrast_reader.getColor(x, y);
				//In Case of Contrast Stretching the getColor function returns the
				//new colour of Contrast Stretching Image therefore it takes the table_contrast and apply 
				//each colour by multiply it by 255.
				color = Color.color(table_contrast[(int)(color.getRed()*255.0)],table_contrast[(int)(color.getGreen()*255.0)], table_contrast[(int)(color.getBlue()*255.0)]);
				// Apply the new colour
				new_contrast_writer.setColor(x, y, color);
			}
		}
		return new_contrast;
	}
	
	
	//Array of lookUpTable Of Contrast Stretching
	double[] table_contrast = new double[256];
	
	/**
	 * This method takes the coordinates of Contrast Stretching Image
	 * which are r1,s1,r2,s2(that circles(c1,c2) takes in GUI of Contrast Stretching),
	 * does the calculations and store each pixel to lookUpTable that is table_contrast.
	 * @param r1 the reference of X coordinate of Contrast Stretching c1.
	 * @param s1 the reference of Y coordinate of Contrast Stretching c1.
	 * @param r2 the reference of X coordinate of Contrast Stretching c2.
	 * @param s2 the reference of Y coordinate of Contrast Stretching c2.
	 */
	public void lookUpTableContrast(double r1, double s1, double r2, double s2) {
		for (int i = 0; i < table_contrast.length; i++) {
			if (i < r1) {
				/*
				 * if the input in table is less than r1 of first circle
				 * then continue to the next if statements.
				 */
				if((((s1/r1)*i)) <= 0.0) {
					/*
					 * if the calculation is less or equal to 0
					 * then set the input value of table to 0.
					 */
					table_contrast[i] = 0.0;
				} else if((((s1/r1)*i)) >= 1.0){
					/*
					 * if the calculation is greater or equal to 1
					 * then set the input value of table to 1.
					 */
					table_contrast[i] = 1.0;
				} else {
					/*
					 * do the calculation as it is and store it
					 * in the table.
					 */
					table_contrast[i] = ((s1 / r1) * i) / 255.0;
					
				} 
			} else if ((r1 <= i) && (i <= r2)) {
				/*
				 * if the r1  is greater or equal  to i and 
				 * if the i is less or equal to r2 then continue to the next if statements.
				 */
					if (((((s2 - s1) /(r2 - r1) * (i - r1))  + s1) / 255.0) <= 0.0) {
						/*
						 * if the calculation is less or equal to 0
						 * then set the input value of table to 0.
						 */
						table_contrast[i] = 0.0;
					} else if (((((s2 - s1) /(r2 - r1) * (i - r1))+s1)/255.0) >= 1.0) {
						/*
						 * if the calculation is greater or equal to 1
						 * then set the input value of table to 1.
						 */
						table_contrast[i] = 1.0;
					} else {
						/*
						 * do the calculation as it is and store it
						 * in the table.
						 */
						table_contrast[i] = ((((s2 - s1) /(r2 - r1) *(i - r1))  + s1))/ 255.0;
					}
			} else if (i >= r2) {
				/*
				 * if the i is greater or equal to r2 then continue to the next if statements.
				 */
					if ((((((255.0 - s2) / (255.0 - r2)) * (i - r2))  + s2) / 255.0) <= 0.0){
						/*
						 * if the calculation is less or equal to 0
						 * then set the input value of table to 0.
						 */
						table_contrast[i] = 0.0;
					} else if ((((((255.0 - s2) / (255.0 - r2)) * (i - r2))  + s2) / 255.0) >= 1.0){
						/*
						 * if the calculation is greater or equal to 1
						 * then set the input value of table to 1.
						 */
						table_contrast[i] = 1.0;
					} else {
						/*
						 * do the calculation as it is and store it
						 * in the table.
						 */
						table_contrast[i] = (((((255.0 - s2) / (255.0 - r2)) * (i - r2))  + s2))/ 255.0;
					}
			}
		}
	}
	
	
	//Array for histogram
	double[][] histogram = new double[256][4];
	/**
	 * This method takes the Image as it is and iterate over pixels
	 * and set each colour and brightness inside histogram array
	 * that takes all the pixels of each colour and then prints it 
	 * and return the image
	 * @param image the reference of image
	 * @return image
	 */
	public Image ImageHistogram(Image image) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		// Create a new image of that width and height
		WritableImage new_histogram = new WritableImage(width, height);
		
		// Get an interface to write to that image memory
		PixelWriter new_histogram_image_writer = new_histogram.getPixelWriter();
		
		// Get an interface to read from the original image passed as the parameter to
		// the method.
		PixelReader new_histogram_reader = image.getPixelReader();
		
		
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = new_histogram_reader.getColor(x, y);
				//In Case of Histogram the getColor function returns the
				//image of Histogram Image therefore it takes each pixel of
				//colour and multiply it by 255 and stores it in histogramArray.
				int red = (int) (color.getRed() * 255);
				int green = (int) (color.getGreen() * 255);
				int blue = (int) (color.getBlue() * 255);
				int brightness = (int) ((red + green + blue)/3);
				histogram[red][0]++;
				histogram[green][1]++;
				histogram[blue][2]++;
				histogram[brightness][3]++;
			}
		}
		
		//a loop that prints all the RGB and brightness pixels
		//of Image
		for (int i = 0; i < 256; i++) {
			System.out.println(i + " Red= " + histogram[i][0] + " Green= " + histogram[i][1] + " Blue= "
					+ histogram[i][2] + " Brightness= " + histogram[i][3]);
		}
		
		return new_histogram;
	}
	
	
	//Array of histogram Equalisation
	double histogramEqualizationArray[][] = new double[256][5];
	
	//Array of distribution
	double tArray[] = new double[256];
	
	//Array of mapping
	double[] mappingArray = new double[256];
	
	/**
	 * This method takes the Image as it is and iterate over pixels
	 * and set each colour and brightness inside histogramEqualization array
	 * that takes all the pixels of each colour,calculates the distribution
	 * function t,pass it in mapping and then takes the new colour 
	 * inside two loops that pass it inside mappingArray index. 
	 * and return the image
	 * @param image reference of image
	 * @return new_histogram_eq
	 */
	public Image histogramEqualization(Image image) {
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		// Create a new image of that width and height
		WritableImage new_histogram_eq = new WritableImage(width, height);
		// Get an interface to write to that image memory
		PixelWriter new_histogram_eq_image_writer = new_histogram_eq.getPixelWriter();
		// Get an interface to read from the original image passed as the parameter to
		// the function
		PixelReader new_histogram_eq_reader = image.getPixelReader();
		
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// For each pixel, get the colour
				Color color = new_histogram_eq_reader.getColor(x, y);
				
				//In Case of HistogramEqualisation the getColor function returns the
				//Image of Histogram Image therefore it takes each pixel of
				//colour and multiply it by 255 and stores it in histogramEqualizationArray.
				int red = (int) (color.getRed() * 255.00);
				int green = (int) (color.getGreen() * 255.00);
				int blue = (int) (color.getBlue() * 255.00);
				int brightness = (int) ((red + green + blue)/3.0);
				histogramEqualizationArray[red][0]++;
				histogramEqualizationArray[green][1]++;
				histogramEqualizationArray[blue][2]++;
				histogramEqualizationArray[brightness][3]++;
			}
		}
			
			//calculate the distribution function of t 
			calculateCumutativeDistributionFunctionT();
			
			//size of Image
			int size = width * height;
			
			//this loop mapping the Image and uses 
			//calculation for tArray and stores it in mappingArray.
			for(int j =0 ; j<mappingArray.length; j++) {
				mappingArray[j] = (int)(255.0*(tArray[j]/(size)));
			}
				
			
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						// For each pixel, get the colour
						Color color_histogram = new_histogram_eq_reader.getColor(x, y);	
						
						//In Case of Histogram Equalisation the getColor function returns the
						//image of Histogram Equalisation Image therefore it takes the mappingArray and apply 
						//each colour by multiply index by 255 and divide it by 255.
						color_histogram = Color.color((mappingArray[(int)(color_histogram.getRed()*255.0)])/255.0,(mappingArray[(int)(color_histogram.getGreen()*255.0)])/255.0,(mappingArray[(int)(color_histogram.getBlue()*255.0)])/255.0);
						
						// Apply the new colour
						new_histogram_eq_image_writer.setColor(x, y, color_histogram);
						
						//create an integer brightness that takes each colour multiply it
						//by 255 and sum it and then divide the summation by 3
						int bright = (int)((color_histogram.getRed()*255.0+color_histogram.getGreen()*255.0+color_histogram.getBlue()*255.0)/3.0);
						
						//stores it in histogramEqualizationArray
						histogramEqualizationArray[bright][4]++;
					}
				}
				return new_histogram_eq;
				
		}
	
	
	/**
	 * This method calculates the distribution t Array
	 * according histogramEqualization in brightness position.
	 */
	public void calculateCumutativeDistributionFunctionT() {
		int i =0;
		while(i<tArray.length) {
			if(i == 0) {
				tArray[0] = (int) histogramEqualizationArray[0][3];
			} else {
				tArray[i] = (int) (tArray[i-1] + histogramEqualizationArray[i][3]);
			}
			i++;
		}
	}
	
	/**
	 * This method takes the Image as it is and iterate over pixels from 0 to 4,
	 * and set each colour according of each pixel,so finds where is the position of 
	 * pixel and do the calculation for the laplacianMatrix 5x5 and stores them in arrays
	 * of each colours(which are the sum of them(RGB),therefore it has a colour that recognise when is red green blue.After that 
	 * calculates the Max,Minimum of the sum of colours then normalised the Image according of calculation
	 * and return new image for Cross Correlation.
	 */
	public Image crossCorrelation(Image image) {
		
		// Find the width and height of the image to be process
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		
		// Create a new image of that width and height
		WritableImage cross_correlation_image = new WritableImage(width, height);
		
		
		// Get an interface to write to that image memory
		PixelWriter cross_correlation_image_writer = cross_correlation_image.getPixelWriter();
		
		
		// Get an interface to read from the original image passed as the parameter to
		// the method.
		PixelReader cross_correlation_image_reader = image.getPixelReader();
		
		//creates arrays sums of RGB
		//values for max ,minimum of each colour
		//value to store the result of pixels RGB and 
		//stores it in arrays,
		//a colour that defines which is the colour
		//of each case
		//and a general max,minimum for them.
		double sumOfRed[] = new double[(width)*(height)];
		double sumOfGreen[] = new double[(width)*(height)];
		double sumOfBlue[] = new double[(width)*(height)];
		double maxRed = sumOfRed[0];
		double minRed = sumOfGreen[0];
		double maxGreen = sumOfGreen[0];
		double minGreen = sumOfGreen[0];
		double maxBlue = sumOfBlue[0];
		double minBlue = sumOfBlue[0];
		double max= 0;
		double min= 0;
		double laplacianRed;
		double laplacianGreen;
		double laplacianBlue;
		int matrix = 0;
		String colour = "red";
		
		
		
		while(colour != "done") {
			int i=0;
		for (int y = 2; y<height-2; y++) {
			for (int x = 2; x<width-2; x++) {
				laplacianRed=0;
				laplacianGreen=0;
				laplacianBlue=0;
				for(int y1 = y-2; y1<=y+2; y1++) {
					for(int x1 = x-2; x1<=x+2; x1++) {
					if (((x1 == x-1) && (y1 == y)) || ((x1 == x) && (y1 == y-1)) || ((x1 == x) && (y1 == y+1)) || ((x1 == x+1) && (y1 == y))) {
						matrix = 3;
					}else if (((x1 == x) && (y1 == y))){
						matrix = 4;
					}else if (((x1 == x-1) && (y1 == y-1)) || ((x1 == x-1) && (y1 == y+1)) || ((x1 == x+1) && (y1 == y-1)) || ((x1 == x+1) && (y1 == y+1))){					
						matrix = 2;
					}else if (((x1 == x-2) && (y1 == y)) || ((x1 == x) && (y1 == y-2)) || ((x1 == x) && (y1 == y+2)) || ((x1 == x+2) && (y1 == y))) {
						matrix = 0;
					}else if(((x1 == x-2) && (y1 == y-2)) || ((x1 == x-2) && (y1 == y+2)) || ((x1 == x+2) && (y1 == y-2)) || ((x1 == x+2) && (y1 == y+2))) {
						matrix = -4;
					}if (((x1 == x-2) && (y1 == y-1)) || ((x1 == x-2) && (y1 == y+1)) || ((x1 == x-1) && (y1 == y-2)) || ((x1 == x-1) && (y1 == y+2)) || ((x1 == x+1) &&  (y1 == y-2)) ||((x1 == x+1) &&(y1 == y+2)) ||((x1 == x+2) && (y1 == y-1)) || ((x1 == x+2) && (y1 == y+1))) {
						matrix = -1;
					}
					
					
						Color cross_corr = cross_correlation_image_reader.getColor(x1, y1);
						switch(colour) {
						case "red" :  laplacianRed = laplacianRed + cross_corr.getRed() * matrix * 255.00;
									  break;
						case "green": laplacianGreen = laplacianGreen + cross_corr.getGreen() * matrix * 255.00;
									  break;
						case "blue": laplacianBlue = laplacianBlue + cross_corr.getBlue() * matrix * 255.00;
							    	 break;
						}	
					}
				}
				
				
				switch(colour) {
				case "red" :  sumOfRed[i] = laplacianRed;
							  break;
				case "green": sumOfGreen[i] = laplacianGreen;
							  break;
				case "blue": sumOfBlue[i] = laplacianBlue;
					    	 break;
				}
				i++;
			}
		}
		
		
		switch(colour) {
		case "red" :  colour = "green";
					  break;
		case "green": colour = "blue";
					  break;
		case "blue": colour = "done";
			    	 break;
		}
	}
		
		
		
		//finds the maximum value for sumOfRed
		for(int o =0; o<sumOfRed.length; o++) {
			if(maxRed < sumOfRed[o]) {
				maxRed = sumOfRed[o];
			}
		}
		
		
		//finds the minimum value for sumOfRed
		for(int o =0; o<sumOfRed.length; o++) {
			if(minRed > sumOfRed[o]) {
				minRed = sumOfRed[o];
			}
		}
		
		
		
		//finds the max value for sumOfGreen
		for(int o =0; o<sumOfGreen.length; o++) {
			if(maxGreen < sumOfGreen[o]) {
				maxGreen = sumOfGreen[o];
			}
		}
		
		
		//finds the min value for sumOfGreen
		for(int o =0; o<sumOfGreen.length; o++) {
			if(minGreen > sumOfGreen[o]) {
				minGreen = sumOfGreen[o];
			}
		}
		
		
		
		//finds the maximum value for sumOfBlue
		for(int o =0; o<sumOfBlue.length; o++) {
			if(maxBlue < sumOfBlue[o]) {
				maxBlue = sumOfBlue[o];
			}
		}
		
		
		//finds the minimum value for sumOfBlue
		for(int o =0; o<sumOfBlue.length; o++) {
			if(minBlue > sumOfBlue[o]) {
				minBlue = sumOfBlue[o];
			}
		}
		
		
		
		//Finds the max and minimum value of 
		//all colours
		if ((maxRed>maxGreen) && (maxRed>maxBlue)) {
			max = maxRed;
		}else if ((maxGreen>maxRed) && (maxGreen>maxBlue)) {
			max = maxGreen;
		}else if ((maxBlue>maxRed) && (maxBlue>maxGreen)) {
			max = maxBlue;
		}
		
		if ((minRed<minGreen) && (minRed<maxBlue)) {
			min = minRed;
		}else if ((minRed>minGreen) && (minGreen<minBlue)) {
			min = minGreen;
		}else if ((minBlue<minRed) && (minBlue<minGreen)) {
			min = minBlue;
		}
		System.out.println(max);
		System.out.println(min);
		
		
		
		
		int p = 0;
		//does two loops to normalise 
		//the new Image according
		//of max,minimum of sums of each Colours.
		for (int y = 2; y < height-2; y++) {
			for (int x = 2; x < width-2; x++) {
				Color new_color = cross_correlation_image_reader.getColor(x, y);
				new_color  = Color.color((sumOfRed[p] - min)/(max-min),(sumOfGreen[p] - min)/(max-min), (sumOfBlue[p] - min)/(max-min));
				cross_correlation_image_writer.setColor(x, y, new_color);				
				p++;
			}
		}
		return cross_correlation_image;
   }
	public static void main(String[] args) {
		launch();
	}
}