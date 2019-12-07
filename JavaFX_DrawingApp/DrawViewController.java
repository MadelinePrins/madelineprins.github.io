import javafx.scene.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.application.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.beans.property.*;
import java.util.*;
import javafx.scene.effect.*;


public class DrawViewController extends Application implements DrawObserver{
    private static final double SCENE_WIDTH = 1000; //set window size
    private static final double SCENE_HEIGHT = 600;
    private static final double BUTTON_SIZE = 49;
    private String labelStyle = "-fx-font: 18px \"Futura\"; -fx-text-fill: rgb(230, 120,0);";
    private String sliderLabelStyle = "-fx-font: 15px \"Futura\"; -fx-text-fill: rgb(230, 120,0);";
    private String colorButtonStyle = "-fx-background-color:";
    private BorderPane root = new BorderPane();
    private Canvas drawCanvas;
    private DrawingProperties drawingProperties = new DrawingProperties();//stores user input for color, brushSize, and opacity
    private ToggleGroup buttons = new ToggleGroup();
    private ToggleGroup colorButtons = new ToggleGroup();
    private ToggleButton rectangleButton = new ToggleButton();
    private ToggleButton circleButton = new ToggleButton();
    private ToggleButton triangleButton = new ToggleButton();
    private ToggleButton pencilButton = new ToggleButton();
    private ToggleButton paintBrushButton = new ToggleButton();
    private ToggleButton eraserButton = new ToggleButton();
    private ModelInterface model;
    public final Slider brushSizeSlider = new Slider(0,1,40);
    private ToggleButton yellowButton = new ToggleButton();
    private ToggleButton orangeButton = new ToggleButton();
    private ToggleButton redButton = new ToggleButton();
    private ToggleButton purpleButton = new ToggleButton();
    private ToggleButton blueButton = new ToggleButton();
    private ToggleButton cyanButton = new ToggleButton();
    private ToggleButton greenButton = new ToggleButton();
    private ToggleButton blackButton = new ToggleButton();
    private ToggleButton bloomButton = new ToggleButton();
    private ToggleButton shadowButton = new ToggleButton();
    private ToggleButton reflectionButton = new ToggleButton();

    /** Creates the scene, canvas, menu and control panes, title, and imports stylesheet*/
    @Override
    public void start(Stage primaryStage) {
    	  model = new DrawModel(); //create instance of DrawModel(), which implements the ModelInterface
        model.registerObserver(this);//registering itself

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);/*create window with specified size*/
        drawCanvas = new Canvas(SCENE_WIDTH, SCENE_HEIGHT);
        drawCanvas.setCursor(Cursor.CROSSHAIR); //creates "+" style mouse pointer on canvas

        HBox menuBar = createMenus(drawCanvas);//calls method that creates horizontal menu bar at top of window
        VBox controlPane = new VBox();//flowpane to hold the drawing tools
        FlowPane buttonPane = createButtons();//pencil,paintbrush,eraser,and shape buttons
        FlowPane sliderPane = createSliders();//brushsize and rgb sliders
        FlowPane colorPane = createColorButtons();//8 color button options
        FlowPane additionalColorPane = createColorCombobox();
        FlowPane backgroundColorPane = createBackgroundColorCombobox();
        controlPane.getChildren().addAll(buttonPane, sliderPane, colorPane, additionalColorPane, backgroundColorPane);
        //set preferences for the tool bar
        controlPane.setPrefWidth(185);
        controlPane.setStyle("-fx-background-color: rgb(105,105,105);");
        controlPane.setMinWidth(controlPane.getPrefWidth());
        //arrange menu bar and tool bar on the window
        root.setTop(menuBar);
        root.setCenter(drawCanvas);
        root.setLeft(controlPane);
        primaryStage.setTitle("DRAW!");//replace with logo
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        scene.getStylesheets().add("Style.css"); //css stylesheet holding :hover button preferences
        setDefaultPencil();
        updateCanvas();
        updateColor();
    }//end start

    /* creates an HBox holding the menu bar across the top of the window, and
     * calls model functions for buttons like Clear All, Export, etc. */
    private HBox createMenus(Canvas canvas) {
        HBox menuPane = new HBox();
        menuPane.setAlignment(Pos.TOP_LEFT);
        MenuBar menuBar = new MenuBar(); //has (File)
        menuBar.setMinWidth(SCENE_WIDTH);

        Menu fileMenu = new Menu("File"); //has (clear all, exit)
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem clearAllItem = new MenuItem("Clear All");
        MenuItem helpItem = new MenuItem("Help");

        //action listener for exit button
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override //when clicked, calls close() method from model
            public void handle(ActionEvent event) {
                model.close();
            }//end handle
        });//end event handler

        //Action listener for Clear All button
        clearAllItem.setOnAction(new EventHandler<ActionEvent>() {
        @Override //when clicked, calls clearAll() from model
            public void handle(ActionEvent event) {
                model.clearAll(canvas);
            }//end handle
        });//end event handler

        //Action listener for help button
        helpItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override //when clicked, calls clearAll() from model
            public void handle(ActionEvent event) {
                model.helpPopUp();
            }//end handle
        });//end event handler

        //keyboard shortcuts
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        clearAllItem.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
        helpItem.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

        //add menu buttons to File and Menu bar
        fileMenu.getItems().addAll(clearAllItem, helpItem, exitItem);
        menuBar.getMenus().addAll(fileMenu);

        //add menuBar to menuPane; aligns the buttons horizontally*/
        menuPane.getChildren().add(menuBar);

        return menuPane;
    }//end of addMenus()

    /*creates a series of 8 preset button colors that change the color of drawing
    tools and shapes */
    private FlowPane createColorButtons(){
      FlowPane colorPane = new FlowPane();
      colorPane.setAlignment(Pos.TOP_LEFT);
      colorPane.setPadding(new Insets(10, 5, 0, 5));
      colorPane.setVgap(10);
      colorPane.setHgap(5);

      /*place toggle buttons in an arraylist and create String UserData
      to reference in initialization for loop*/
      ArrayList<ToggleButton> toolToggleButtonList = new ArrayList<ToggleButton>();
      toolToggleButtonList.add(yellowButton);
      yellowButton.setUserData("yellowButton");
      toolToggleButtonList.add(orangeButton);
      orangeButton.setUserData("orangeButton");
      toolToggleButtonList.add(redButton);
      redButton.setUserData("redButton");
      toolToggleButtonList.add(purpleButton);
      redButton.setUserData("purpleButton");
      toolToggleButtonList.add(blueButton);
      blueButton.setUserData("blueButton");
      toolToggleButtonList.add(cyanButton);
      redButton.setUserData("cyanButton");
      toolToggleButtonList.add(greenButton);
      blueButton.setUserData("greenButton");
      toolToggleButtonList.add(blackButton);
      blueButton.setUserData("blackButton");

      ArrayList<String> toolStringList = new ArrayList<String>();
      toolStringList.add("yellow");
      toolStringList.add("orange");
      toolStringList.add("red");
      toolStringList.add("purple");
      toolStringList.add("blue");
      toolStringList.add("cyan");
      toolStringList.add("green");
      toolStringList.add("black");
      /*set default properties for each of the colors in the ToggleButton arrayList*/
      for(int i=0; i<toolToggleButtonList.size(); i++){
        ToggleButton thisToolToggle = toolToggleButtonList.get(i);
        thisToolToggle.setStyle(colorButtonStyle + " " + toolStringList.get(i));
        thisToolToggle.setMinWidth(18);
        thisToolToggle.setMinHeight(18);
        thisToolToggle.setMaxWidth(18);
        thisToolToggle.setMaxHeight(18);
        thisToolToggle.setToggleGroup(colorButtons);
      }//end for

      /*Label on the GUI window above the 8 color buttons*/
      final Label colorCaption = new Label("COLOR TOOLS:           ");
      colorCaption.setStyle(labelStyle);

      colorPane.getChildren().addAll(colorCaption,yellowButton,orangeButton,redButton,purpleButton,blueButton,cyanButton,greenButton,blackButton);
      return colorPane;

    }//end createColorButtons

    /*@return a FlowPane with all of the buttons*/
    private FlowPane createButtons(){
      //creates the FlowPane
      FlowPane buttonPane = new FlowPane();
      buttonPane.setAlignment(Pos.TOP_LEFT);
      buttonPane.setPadding(new Insets(10, 5, 0, 5));
      buttonPane.setVgap(10);
      buttonPane.setHgap(5);

      //place tool toggle buttons in an arraylist and create String UserData to reference in initialization for loop
      ArrayList<ToggleButton> toolToggleButtonList = new ArrayList<ToggleButton>();
      toolToggleButtonList.add(pencilButton);
      pencilButton.setSelected(true);
      pencilButton.setUserData("pencilButton");
      toolToggleButtonList.add(paintBrushButton);
      paintBrushButton.setUserData("paintBrushButton");
      toolToggleButtonList.add(eraserButton);
      eraserButton.setUserData("eraserButton");

      //creating a string list of image names
      ArrayList<String> toolStringList = new ArrayList<String>();
      toolStringList.add("pencil");
      toolStringList.add("paintBrush");
      toolStringList.add("eraser");

      //initializes the properties for the tool ToggleButtons and adds the tools to the ToggleGroup
      for(int i=0; i<toolToggleButtonList.size(); i++){
        ToggleButton thisToolToggle = toolToggleButtonList.get(i);
        Image toggleImg = new Image(getClass().getResourceAsStream("images/" + toolStringList.get(i)+".png"),BUTTON_SIZE,BUTTON_SIZE,false,false);
        thisToolToggle.setGraphic(new ImageView(toggleImg));
        thisToolToggle.setMinWidth(BUTTON_SIZE);
        thisToolToggle.setMinHeight(BUTTON_SIZE);
        thisToolToggle.setMaxWidth(BUTTON_SIZE);
        thisToolToggle.setMaxHeight(BUTTON_SIZE);
      }
      //set buttons to buttons togglegroup
      pencilButton.setToggleGroup(buttons);
      paintBrushButton.setToggleGroup(buttons);
      eraserButton.setToggleGroup(buttons);

      //place shape toggle buttons in an arraylist and create String UserData to reference in initialization for loop
      ArrayList<ToggleButton> shapesToggleButtonList = new ArrayList<ToggleButton>();
      shapesToggleButtonList.add(rectangleButton);
      rectangleButton.setUserData("rectangleButton");
      shapesToggleButtonList.add(circleButton);
      circleButton.setUserData("circleButton");
      shapesToggleButtonList.add(triangleButton);
      triangleButton.setUserData("triangleButton");

      //creating a list of shape image names
      ArrayList<String> shapesStringList = new ArrayList<String>();
      shapesStringList.add("rectangle");
      shapesStringList.add("circle");
      shapesStringList.add("triangle");

      //initializes the properties for the tool ToggleButtons, and adds the tools to the ToggleGroup
      for(int i=0; i<shapesToggleButtonList.size(); i++){
        ToggleButton thisShapeToggle = shapesToggleButtonList.get(i);
        Image toggleImg = new Image(getClass().getResourceAsStream("images/" + shapesStringList.get(i)+".png"),30,30,false,false);
        thisShapeToggle.setGraphic(new ImageView(toggleImg));
        thisShapeToggle.setMinWidth(BUTTON_SIZE);
        thisShapeToggle.setMinHeight(BUTTON_SIZE);
        thisShapeToggle.setMaxWidth(BUTTON_SIZE);
        thisShapeToggle.setMaxHeight(BUTTON_SIZE);
      }
      //append button to button group
      rectangleButton.setToggleGroup(buttons);
      circleButton.setToggleGroup(buttons);
      triangleButton.setToggleGroup(buttons);

      //Labels on the GUI window above the 8 color buttons
      final Label drawingToolCaption = new Label("DRAWING TOOLS:");
      drawingToolCaption.setStyle(labelStyle);
      final Label shapesCaption = new Label("SHAPE TOOLS:           ");
      shapesCaption.setStyle(labelStyle);

      //place effect toggle buttons in an arraylist and create String UserData to reference in initialization for loop
      ArrayList<ToggleButton> effectButtonList = new ArrayList<ToggleButton>();
      effectButtonList.add(bloomButton);
      bloomButton.setUserData("bloomButton");
      effectButtonList.add(shadowButton);
      shadowButton.setUserData("shadowButton");
      effectButtonList.add(reflectionButton);
      reflectionButton.setUserData("reflectionButton");

      //creating string list of image names for effect tools
      ArrayList<String> effectStringList = new ArrayList<String>();
      effectStringList.add("bloom");
      effectStringList.add("shadow");
      effectStringList.add("reflection");

      //acccess png logo images for effect buttons
      for(int i=0; i< effectButtonList.size(); i++){
        ToggleButton thisEffectButton = effectButtonList.get(i);
        Image effectButtonImg = new Image(getClass().getResourceAsStream("images/" + effectStringList.get(i)+".png"),BUTTON_SIZE,BUTTON_SIZE,false,false);
        thisEffectButton.setGraphic(new ImageView(effectButtonImg));
        thisEffectButton.setMinWidth(BUTTON_SIZE);
        thisEffectButton.setMinHeight(BUTTON_SIZE);
        thisEffectButton.setMaxWidth(BUTTON_SIZE);
        thisEffectButton.setMaxHeight(BUTTON_SIZE);
      }

      //setting effect tools to buttons togglegroup
      shadowButton.setToggleGroup(buttons);
      bloomButton.setToggleGroup(buttons);
      reflectionButton.setToggleGroup(buttons);

      //Labels on the GUI window above the 8 color buttons
      final Label effectToolsCaption = new Label("EFFECT TOOLS:           ");
      effectToolsCaption.setStyle(labelStyle);

      //adding all buttons to button pane
      buttonPane.getChildren().addAll(drawingToolCaption,pencilButton,paintBrushButton,eraserButton,
      shapesCaption,circleButton,triangleButton, rectangleButton,effectToolsCaption, shadowButton, bloomButton, reflectionButton);

      return buttonPane;
    }//end createButtons

    /**creates an additionalColor Combobox*/
    private FlowPane createColorCombobox(){
      FlowPane additionalColorPane = new FlowPane();
      additionalColorPane.setAlignment(Pos.TOP_LEFT);
      additionalColorPane.setPadding(new Insets(10, 5, 0, 5));
      additionalColorPane.setVgap(10);
      additionalColorPane.setHgap(5);
      final ComboBox colorBox = new ComboBox();
      colorBox.getItems().addAll(
          "None Selected",
          "White",
          "Corn Flower Blue",
          "Dark Orchid",
          "Chocolate",
          "Gold",
          "Coral",
          "Crimson",
          "Deep Pink",
          "Honey Dew",
          "Lavender",
          "Lime Green",
          "Mint Cream",
          "Misty Rose",
          "Old Lace",
          "Plum",
          "Powder Blue",
          "Silver",
          "Khaki"
      );//end of adding items to ColorBox

      //listener for color selector buttons
      colorBox.setValue("None Selected");
      colorBox.setVisibleRowCount(3);
      colorBox.valueProperty().addListener(new ChangeListener<String>() {
          @Override public void changed(ObservableValue ov, String t, String t1) {
            if (colorBox.getValue() == "White"){
              model.acceptandpassColor(Color.WHITE);
            }
            if (colorBox.getValue() == "Corn Flower Blue"){
              model.acceptandpassColor(Color.CORNFLOWERBLUE);
            }
            if (colorBox.getValue() == "Dark Orchid"){
              model.acceptandpassColor(Color.DARKORCHID);
            }
            if (colorBox.getValue() == "Chocolate"){
              model.acceptandpassColor(Color.CHOCOLATE);
            }
            if (colorBox.getValue() == "Gold"){
              model.acceptandpassColor(Color.GOLD);
            }
            if (colorBox.getValue() == "Coral"){
              model.acceptandpassColor(Color.CORAL);
            }
            if (colorBox.getValue() == "Crimson"){
              model.acceptandpassColor(Color.CRIMSON);
            }
            if (colorBox.getValue() == "Deep Pink"){
              model.acceptandpassColor(Color.DEEPPINK);
            }
            if (colorBox.getValue() == "Honey Dew"){
              model.acceptandpassColor(Color.HONEYDEW);
            }
            if (colorBox.getValue() == "Lavender"){
              model.acceptandpassColor(Color.LAVENDER);
            }
            if (colorBox.getValue() == "Lime Green"){
              model.acceptandpassColor(Color.LIMEGREEN);
            }
            if (colorBox.getValue() == "Mint Cream"){
              model.acceptandpassColor(Color.MINTCREAM);
            }
            if (colorBox.getValue() == "Misty Rose"){
              model.acceptandpassColor(Color.MISTYROSE);
            }
            if (colorBox.getValue() == "Old Lace"){
              model.acceptandpassColor(Color.OLDLACE);
            }
            if (colorBox.getValue() == "Plum"){
              model.acceptandpassColor(Color.PLUM);
            }
            if (colorBox.getValue() == "Powder Blue"){
              model.acceptandpassColor(Color.POWDERBLUE);
            }
            if (colorBox.getValue() == "Silver"){
              model.acceptandpassColor(Color.SILVER);
            }
            if (colorBox.getValue() == "Khaki"){
              model.acceptandpassColor(Color.KHAKI);
            }
          }
      });//end of listener

      //creating caption for color drop-down
      final Label colorCaption = new Label("MORE COLORS:           ");
      colorCaption.setStyle(labelStyle);
      additionalColorPane.getChildren().addAll(colorCaption,colorBox);
      return additionalColorPane;
    }// end of comboBox

    /*creates an additionalColor Combobox for the */
    private FlowPane createBackgroundColorCombobox(){
      FlowPane backgroundColorPane = new FlowPane();
      backgroundColorPane.setAlignment(Pos.TOP_LEFT);
      backgroundColorPane.setPadding(new Insets(10, 5, 0, 5));
      backgroundColorPane.setVgap(10);
      backgroundColorPane.setHgap(5);
      final ComboBox backgroundBox = new ComboBox();
      backgroundBox.getItems().addAll(
          "White",
          "Yellow",
          "Orange",
          "Red",
          "Purple",
          "Blue",
          "Olive",
          "Cyan",
          "Black"
      );//end of adding items to ColorBox

      //action listener for canvas color selector
      backgroundBox.setValue("White");
      backgroundBox.setVisibleRowCount(3);
      backgroundBox.valueProperty().addListener(new ChangeListener<String>() {
          @Override public void changed(ObservableValue ov, String t, String t1) {
            if (backgroundBox.getValue() == "White"){
              model.setBackground(Color.WHITE, drawCanvas);
            }
            if (backgroundBox.getValue() == "Yellow"){
              model.setBackground(Color.YELLOW, drawCanvas);
            }
            if (backgroundBox.getValue() == "Orange"){
              model.setBackground(Color.ORANGE, drawCanvas);
            }
            if (backgroundBox.getValue() == "Red"){
              model.setBackground(Color.RED, drawCanvas);
            }
            if (backgroundBox.getValue() == "Purple"){
              model.setBackground(Color.PURPLE, drawCanvas);
            }
            if (backgroundBox.getValue() == "Blue"){
              model.setBackground(Color.BLUE, drawCanvas);
            }

            if (backgroundBox.getValue() == "Cyan"){
              model.setBackground(Color.CYAN, drawCanvas);
            }
            if (backgroundBox.getValue() == "Black"){
              model.setBackground(Color.BLACK, drawCanvas);
            }
            if (backgroundBox.getValue() == "Olive"){
              model.setBackground(Color.OLIVE, drawCanvas);
            }
          }
      });//end of listener

      //creating caption for background color selector
      final Label backgroundCaption = new Label("CANVAS COLOR:           ");
      backgroundCaption.setStyle(labelStyle);
      backgroundColorPane.getChildren().addAll(backgroundCaption,backgroundBox);
      return backgroundColorPane;
    }// end of backgroundcolor

    /*creates brush size sliders and its initial properties*/
    private FlowPane createSliders(){
      //creates the FlowPane for the slider and its caption
      FlowPane sliderPane = new FlowPane();
      sliderPane.setAlignment(Pos.TOP_LEFT);
      sliderPane.setPadding(new Insets(10, 5, 0, 5));
      sliderPane.setVgap(10);
      sliderPane.setHgap(5);
      sliderPane.setAlignment(Pos.TOP_LEFT);
      sliderPane.setPrefWrapLength(185);
      brushSizeSlider.setMin(0);
      brushSizeSlider.setMax(100);
      brushSizeSlider.setMaxWidth(125);
      brushSizeSlider.setBlockIncrement(100);
      brushSizeSlider.setBlockIncrement(10);
      brushSizeSlider.setValue(1);
      brushSizeSlider.setMin(1);
      brushSizeSlider.setMax(20);

      //Creates brushSize caption and label, initializes the brushSizeValue
      final Label brushSizeCaption = new Label("BRUSH SIZE:");
      brushSizeCaption.setStyle(labelStyle);
      final Label brushSizeLabel = new Label(Double.toString(brushSizeSlider.getValue()));
      brushSizeLabel.setStyle(sliderLabelStyle);
      int brushSizeValue = 1;

      //creates and displays the value for the slider
      brushSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
            String label = Integer.toString(newValue.intValue());
            brushSizeLabel.setText(label);
        }//end changed
      });//end listener

      //calling listener
      updateSlider();

      //adds all the objects to the controlPane to be displayed
      sliderPane.getChildren().addAll(brushSizeCaption,brushSizeSlider,brushSizeLabel);

      return sliderPane;
    }//end createSliders

    /*registers listeners to detect when slider value changes. Calls the necessary
    model methods to update functionality */
    public void updateSlider(){
      brushSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
    		@Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
            		model.setBrushSize(newValue.intValue());
            	}//end changed
    		});//end listener

  	}//end updateSliders

    /* returns which item in the toggle button group is selected at any given time */
    public Toggle findSelectedButton(ToggleGroup buttonGroup){
      Toggle selectedToggle = buttons.getSelectedToggle();
      return selectedToggle;
    }//end findSelectedButton

    /* Registers ActionEvents and handlers to detect when color buttons are pressed.
     Calls the necessary model methods to update functionalities */
    public void updateColor(){
      yellowButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "YELLOWButton");
            model.acceptandpassColor(Color.YELLOW);
           }
      });//end event handler

      orangeButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "orangeButton");
            model.acceptandpassColor(Color.ORANGE);
           }
      });//end event handler

      redButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "redButton");
            model.acceptandpassColor(Color.RED);
           }
      });//end event handler

      purpleButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "purpleButton");
            model.acceptandpassColor(Color.PURPLE);
           }
      });//end event handler

      blueButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "blueButton");
            model.acceptandpassColor(Color.BLUE);
           }
      });//end event handler

      cyanButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "cyanButton");
            model.acceptandpassColor(Color.CYAN);
           }
      });//end event handler

      greenButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "greenButton");
            model.acceptandpassColor(Color.GREEN);
           }
      });//end event handler

      blackButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "blackButton");
            model.acceptandpassColor(Color.BLACK);
           }
      });//end event handler
    }//end update colors

    /* Registers ActionEvents and handlers to detect when tool/shape buttons are pressed.
     Calls the necessary model methods to update functionalities */
     //default method
     public void setDefaultPencil(){
       buttons.selectToggle(pencilButton);
       model.setDefaultBooleans(drawCanvas);
       model.drawPencil(drawCanvas);
     }

    /* updates the canvas with the drawings createds from the users input*/
    public void updateCanvas(){
      //pencil button listener and draw method call
      model.drawPencil(drawCanvas);
      pencilButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "pencilButton");
              updateBooleans();
              model.drawPencil(drawCanvas);
           }
      });//end event handler

      //paint brush button listener and draw method call
      paintBrushButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "paintBrushButton");
              updateBooleans();
              model.drawPaintBrush(drawCanvas);
           }//end handle
      });//end event handler

      //eraser button listener and draw method call
      eraserButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()== "eraserButton");
            updateBooleans();
            model.eraserTool(drawCanvas);
           }
      });//end event handler

      //rectangle button listener and draw method call
      rectangleButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()=="rectangleButton");
              updateBooleans();
              model.drawRectangle(drawCanvas);
              }
          });//end event handler

      //circle button listener and draw method call
      circleButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()=="circleButton");
              updateBooleans();
              model.drawCircle(drawCanvas);
              }
          });//end event handler

      //triangle button listener and draw method call
      triangleButton.setOnAction(new EventHandler<ActionEvent>() {
          @Override
          public void handle(ActionEvent event) {
            if (buttons.getSelectedToggle().getUserData()=="triangleButton");
              updateBooleans();
              model.drawTriangle(drawCanvas);
              }
          });//end event handler

      //bloom button listener and draw method call
      bloomButton.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent event){
          if(buttons.getSelectedToggle().getUserData() == "bloomButton");
          updateBooleans();
          model.drawBloom(drawCanvas);
        }//end handle
      }); //end event handler

      //shadow button listener and draw method call
      shadowButton.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent e){
          if(buttons.getSelectedToggle().getUserData()== "shadowButton");
          updateBooleans();
          model.drawShadow(drawCanvas);
        }//end handle
      });//end event handler

    //reflection button listener and draw method call
     reflectionButton.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent e){
          if(buttons.getSelectedToggle().getUserData()== "reflectionButton");
          updateBooleans();
          model.drawReflectionEffect(drawCanvas);
        }//end handle
      });//end event handler


    }//end updateCanvas


    /*creates a HashMap of Strings and booleans for each of the tool/shape buttons
    in order to pass these to the model*/
    public void updateBooleans(){
      HashMap<String, Boolean> hmap = new HashMap<String, Boolean>();
      /*Adding elements to HashMap*/
      hmap.put("rectangleButton", rectangleButton.isSelected());
      hmap.put("circleButton",circleButton.isSelected()) ;
      hmap.put("triangleButton",triangleButton.isSelected());
      hmap.put("pencilButton",pencilButton.isSelected());
      hmap.put("paintBrushButton",paintBrushButton.isSelected());
      hmap.put("eraserButton",eraserButton.isSelected());
      hmap.put("bloomButton", bloomButton.isSelected());
      hmap.put("shadowButton", shadowButton.isSelected());
      hmap.put("reflectionButton", reflectionButton.isSelected());
      model.setBooleans(hmap);
    }


   	public static void main(String[] args) {
    	launch(args);
    }

}//end of DrawView class
