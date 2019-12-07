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

 public class DrawModel implements ModelInterface{
  ArrayList observerList = new ArrayList(); //list of observers
  private int brushSize = 0;
  private double opacity = 1.0;
  private double xCoordinateStart;
  private double yCoordinateStart;
  private Boolean isRectanglePressed;
  private Boolean isCirclePressed;
  private Boolean isTrianglePressed;
  private Boolean isPencilPressed;
  private Boolean isPaintBrushPressed;
  private Boolean isEraserPressed;
  private Boolean isBloomButtonPressed;
  private Boolean isShadowButtonPressed;
  private Boolean isReflectionButtonPressed;
  private Color cursorColor;
  //new instances of javaFX effects	 
  private BoxBlur blur = new BoxBlur();
  private Bloom bloom = new Bloom();
  private DropShadow dropShadow = new DropShadow();
  private Reflection reflection = new Reflection();

  /**Initializes all button booleans to false */	 
  public void initializeBooleans(){
    isRectanglePressed = false;
    isCirclePressed = false;
    isTrianglePressed = false;
    isPencilPressed = false;
    isPaintBrushPressed = false;
    isEraserPressed = false;
    isBloomButtonPressed = false;
    isShadowButtonPressed = false;
    isReflectionButtonPressed = false;
  }
	 
  /**Allows the pencil button to be default pressed when the application is just opened */
  public void setDefaultBooleans(Canvas canvas){
    isRectanglePressed = false;
    isPencilPressed = true;
    isCirclePressed = false;
    isPaintBrushPressed = false;
    isTrianglePressed = false;
    isEraserPressed = false;
    isBloomButtonPressed = false;
    isShadowButtonPressed = false;
    isReflectionButtonPressed = false;
  }

   /**quits application*/
   public void close(){
     System.exit(0);
   }

  /** clears everything user has added to canvas */
  public void clearAll(Canvas canvas){
    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setEffect(null);
    graphicsContext.clearRect(0,0,1000,600); //learnt about clearRect() from StackOverFlow
 	}

	/**creates popup window with tool tips*/
   public void helpPopUp(){
		FlowPane popupPane = new FlowPane();
		Stage popupStage = new Stage();
		Scene popUp = new Scene(popupPane,700,250);
		popupStage.setScene(popUp);
		// from javafxtutorials tell stage it is meant to popUp
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Help");
		String popUpString = "Welcome to Draw!" + "\n" + "The tool selector buttons can be used to switch between pencil, paintbrush, and eraser." + "\n" + "The shape buttons can be used to create rectangles, circles, and triangles." + "\n" + "When drawing Triangles, drag from upper left to bottom right to point right, bottom left to upper right to point up," + "\n" + " bottom right to upper left to point left, and top right to bottom left to point down." + "\n" + "To use the shadow, bloom, or reflect effects, use the effect buttons." + "\n" + "To set the size of the lines drawn with the tools and shapes, use the Brush Size slider." + "\n" + "To select a color, click a color in the control bar, or use the More Colors drop-down for more options." + "\n" + "To set or change the background color, use the Canvas Color drop-down and choose a color." + "\n" + "To exit, go to File > Exit, or use control + q." + "\n" + "To clear the drawing, go to File > Clear All, or use control + c." + "\n" + "For help, go to File > Help or use control + H." + "\n" + "Enjoy your doodling!";
		Text popupMessage = new Text(popUpString);
		popupPane.getChildren().add(popupMessage);
		popupStage.showAndWait();
  }
	 
  //Getter methods
	public int getBrushSize(){
		return brushSize;
	}
	public double getOpacity(){
		return opacity;
	}
  public double getXCoordinateStart(){
      return xCoordinateStart;
  }

  public double getYCoordinateStart(){
      return yCoordinateStart;
  }
	 
  //setter methods
	public void setBrushSize(int value){
		this.brushSize = value;
	}
	public void setOpacity(double value){
		this.opacity = value;
	}
  public void setXCoordinateStart(double x){
      this.xCoordinateStart = x;
  }
  public void setYCoordinateStart(double y){
      this.yCoordinateStart = y;
  }

  /*uses HashMap of booleans from ViewController to set instance variable values.
  These must be cast from Object to boolean*/
  public void setBooleans(HashMap hmap){
    initializeBooleans();
    isRectanglePressed = (boolean)hmap.get("rectangleButton");
    isCirclePressed = (boolean)hmap.get("circleButton");
    isTrianglePressed = (boolean)hmap.get("triangleButton");
    isPencilPressed = (boolean)hmap.get("pencilButton");
    isPaintBrushPressed = (boolean)hmap.get("paintBrushButton");
    isEraserPressed = (boolean)hmap.get("eraserButton");
    isBloomButtonPressed = (boolean)hmap.get("bloomButton");
    isShadowButtonPressed = (boolean)hmap.get("shadowButton");
    isReflectionButtonPressed = (boolean)hmap.get("reflectionButton");
  }//end set booleans
	 
	/**creates a rectangle/square shape based on the direction in which the user draws */
	public void drawRectangle(Canvas canvas){
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
			 //make sure that the rectangle button is the only one pressed
       if((isRectanglePressed == true) && (isPencilPressed == false) && (isCirclePressed == false)
        && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.beginPath();
          setXCoordinateStart(event.getX());
          setYCoordinateStart(event.getY());
          graphicsContext.setStroke(cursorColor);
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur); //set blur to 0 so that rectangle is sharp
          event.consume();
        }//end if
      }//end handle
    });//end event handler

    //draws a rectangle when mouse is dragged/held down
    canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isRectanglePressed == true) && (isPencilPressed == false) && (isCirclePressed == false)
        && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());
          graphicsContext.setFill(Color.BLACK);
          final double localXCoordinateStart = getXCoordinateStart();
          final double localYCoordinateStart = getYCoordinateStart();
          final double xCoordinateFinish = event.getX();
          final double yCoordinateFinish = event.getY();
          //dragging from bottom left to top right
          if(xCoordinateStart < xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
              graphicsContext.rect(localXCoordinateStart, yCoordinateFinish, xCoordinateFinish-localXCoordinateStart, localYCoordinateStart-yCoordinateFinish);
          }//dragging from top left to bottom right
          else if(localXCoordinateStart < xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              graphicsContext.rect(localXCoordinateStart, localYCoordinateStart, xCoordinateFinish-localXCoordinateStart, yCoordinateFinish-localYCoordinateStart);
          }//dragging from top right to bottom left
          else if(localXCoordinateStart > xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              graphicsContext.rect(xCoordinateFinish, yCoordinateFinish, localXCoordinateStart-xCoordinateFinish, localYCoordinateStart-yCoordinateFinish);
          }//dragging from bottom right to top left
          else if(localXCoordinateStart > xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
              graphicsContext.rect(xCoordinateFinish, localYCoordinateStart, localXCoordinateStart-xCoordinateFinish, yCoordinateFinish-localYCoordinateStart);
          }
          graphicsContext.stroke();
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur); //set blur effect to 0 to keep rectangle sharp
          event.consume();
        }//end if
      }//end handle
    });//end event handler
	}//end of DrawRectangle()

  /**changes mouse output to desired color */
  public void acceptandpassColor(Color color){
    cursorColor = color;
  }
	/**changes the fill color of the background canvas to desired color */
  public void setBackground(Color color, Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.setFill(color);
    graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
  }

  /**creates a rectangle/square shape based on the direction in which the user draws */
  public void drawCircle(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    graphicsContext.setEffect(null);
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure circle tool is the button that is pressed before execution
        if((isCirclePressed == true) && (isPencilPressed == false) && (isRectanglePressed == false)
         && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
         && (isBloomButtonPressed == false)){
          graphicsContext.beginPath();
          setXCoordinateStart(event.getX());
          setYCoordinateStart(event.getY());
          graphicsContext.setStroke(cursorColor);
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur);
          event.consume();
        }//end if
      }//end handle
    });//end event handler

    //draws a circle when mouse is dragged/held down
    canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//check is circle is the button pressed before execution
       if((isCirclePressed == true) && (isPencilPressed == false) && (isRectanglePressed == false)
         && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
         && (isBloomButtonPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());
          graphicsContext.setFill(Color.BLACK);
          final double localXCoordinateStart = getXCoordinateStart();
          final double localYCoordinateStart = getYCoordinateStart();
          final double xCoordinateFinish = event.getX();
          final double yCoordinateFinish = event.getY();
          //dragging from bottom left to top right
          if(xCoordinateStart < xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
              graphicsContext.strokeOval(localXCoordinateStart, yCoordinateFinish, xCoordinateFinish-localXCoordinateStart, localYCoordinateStart-yCoordinateFinish);
          }//dragging from top left to bottom right
          else if(xCoordinateStart < xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              graphicsContext.strokeOval(localXCoordinateStart, localYCoordinateStart, xCoordinateFinish-localXCoordinateStart, yCoordinateFinish-localYCoordinateStart);
          }//dragging from top right to bottom left
          else if(xCoordinateStart > xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              graphicsContext.strokeOval(xCoordinateFinish, localYCoordinateStart, localXCoordinateStart-xCoordinateFinish, yCoordinateFinish-localYCoordinateStart);
          }//dragging from bottom right to top left
          else if(xCoordinateStart > xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
              graphicsContext.strokeOval(xCoordinateFinish, yCoordinateFinish, localXCoordinateStart-xCoordinateFinish, localYCoordinateStart-yCoordinateFinish);
          }
          graphicsContext.stroke();
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur); //set blur to 0 to keep shape sharp
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end of DrawCircle()

	/** removes previously drawn data wherever user drags on the canvas */
  public void eraserTool(Canvas canvas){
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED,new EventHandler<MouseEvent>() {
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    @Override
       public void handle(MouseEvent e) {
				 //make sure that eraser tool is the button pressed
        if ((isEraserPressed == true) && (isPencilPressed == false) && (isCirclePressed == false)
          && (isRectanglePressed == false) && (isTrianglePressed == false) && (isPaintBrushPressed == false)
          && (isBloomButtonPressed == false)){
          double eraserSize = getBrushSize();
          graphicsContext.setEffect(null);
          graphicsContext.clearRect(e.getX() - 10, e.getY() - 10, 20, 20);// java fx tutorials oracle
         }//end if
       }//end handle
     });//end event handler
  }//end eraserTool()

  /**creates stroke wherever uses clicks and drags mouse on canvas */
	public void drawPencil(Canvas canvas){
		final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure pencil button is the one that is pressed before execution
        if((isPencilPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false)
        && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.beginPath();
          graphicsContext.moveTo(event.getX(), event.getY());
          graphicsContext.setStroke(cursorColor);
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur); //set blur to zero to ensure sharpness
          event.consume();
        }//if
      }//end handle
    }); //end event handler
		
    //draw stroke when mouse is dragged/held down
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure pencil is the button before execution
        if((isPencilPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false)
        && (isTrianglePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());;
          graphicsContext.lineTo(event.getX(), event.getY());
          graphicsContext.stroke();
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur);
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end drawpencil
	 
	/**creates a smoother, slightly blended stroke wherever uses clicks and drags mouse on canvas */
  public void drawPaintBrush(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    graphicsContext.setEffect(null);
    BoxBlur blur = new BoxBlur();
    blur.setWidth(1);
    blur.setHeight(1);
    blur.setIterations(100);
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isPaintBrushPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.beginPath();
          graphicsContext.moveTo(event.getX(), event.getY());
          graphicsContext.setStroke(cursorColor);
          graphicsContext.setEffect(blur);
          event.consume();
        }//end if
      }//end handle
    });//end event handler
    //draw stroke when mouse is dragged/held down
		
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isPaintBrushPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());;
          graphicsContext.lineTo(event.getX(), event.getY());
          graphicsContext.stroke();
          //BoxBlur blur = new BoxBlur();
          blur.setWidth(1);
          blur.setHeight(1);
          blur.setIterations(100);
          graphicsContext.setEffect(blur);
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end drawPaintBrush
	 
	/** creates a triangle shape based on the direction in which the user draws */
  public void drawTriangle(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    graphicsContext.setEffect(null);
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isTrianglePressed == true) && (isPencilPressed == false) && (isRectanglePressed == false) &&
        (isCirclePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.beginPath();
          setXCoordinateStart(event.getX());
          setYCoordinateStart(event.getY());
          graphicsContext.setStroke(cursorColor);
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur);
          event.consume();
        }//end if
      }//end handle
    });//end event handler

    canvas.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isTrianglePressed == true) && (isPencilPressed == false) && (isRectanglePressed == false) &&
        (isCirclePressed == false) && (isEraserPressed == false) && (isPaintBrushPressed == false)
        && (isBloomButtonPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());
          graphicsContext.setFill(Color.BLACK);
          final double localXCoordinateStart = getXCoordinateStart();
          final double localYCoordinateStart = getYCoordinateStart();
          final double xCoordinateFinish = event.getX();
          final double yCoordinateFinish = event.getY();
          if(xCoordinateStart < xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
            final double[] xPoints = new double[]{localXCoordinateStart, xCoordinateFinish, (localXCoordinateStart+xCoordinateFinish)/2};
            final double[] yPoints = new double[]{localYCoordinateStart, localYCoordinateStart, yCoordinateFinish};
            graphicsContext.strokePolygon(xPoints, yPoints,3);
          } else if(localXCoordinateStart < xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              final double[] xPoints = new double[]{localXCoordinateStart, localXCoordinateStart, xCoordinateFinish};
              final double[] yPoints = new double[]{localYCoordinateStart, yCoordinateFinish, (localYCoordinateStart+yCoordinateFinish)/2};
              graphicsContext.strokePolygon(xPoints, yPoints,3);
          } else if(localXCoordinateStart > xCoordinateFinish && localYCoordinateStart < yCoordinateFinish){
              final double[] xPoints = new double[]{localXCoordinateStart, xCoordinateFinish, (localXCoordinateStart+xCoordinateFinish)/2};
              final double[] yPoints = new double[]{localYCoordinateStart, localYCoordinateStart, yCoordinateFinish};
              graphicsContext.strokePolygon(xPoints, yPoints,3);
          } else if(localXCoordinateStart > xCoordinateFinish && localYCoordinateStart > yCoordinateFinish){
              final double[] xPoints = new double[]{localXCoordinateStart, localXCoordinateStart, xCoordinateFinish};
              final double[] yPoints = new double[]{localYCoordinateStart, yCoordinateFinish, (localYCoordinateStart+yCoordinateFinish)/2};
              graphicsContext.strokePolygon(xPoints, yPoints,3);
          }//end else if
          blur.setIterations(0);
          blur.setWidth(0);
          blur.setHeight(0);
          graphicsContext.setEffect(blur); //set blur to 0 to keep shape sharp
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end drawTriangle

 
 /**creates a smoother blooming(spreading effect) and blurred to stroke wherever uses clicks and drags mouse on canvas */
 public void drawBloom(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    graphicsContext.setEffect(null);
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure bloom is pressed
        if((isBloomButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false)){
          graphicsContext.beginPath();
          graphicsContext.moveTo(event.getX(), event.getY());
          graphicsContext.setStroke(cursorColor);
          graphicsContext.setEffect(new Glow(.8));
          event.consume();
        }//end if
      }//end handle
    });//end event handler
    //draw stroke when mouse is dragged/held down
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isBloomButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());;
          graphicsContext.lineTo(event.getX(), event.getY());
          graphicsContext.stroke();
          graphicsContext.setEffect(new Glow(.8));
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end drawBloom

	/**creates  appearance of a 3D stroke by adding a shadow wherever uses clicks and drags mouse on canvas */
  public void drawShadow(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure shadow is pressed
        if((isShadowButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false) ){
          graphicsContext.beginPath();
          graphicsContext.moveTo(event.getX(), event.getY());
          graphicsContext.setStroke(cursorColor);
          dropShadow.setRadius(10.0);
          dropShadow.setOffsetX(10.0);
          dropShadow.setOffsetY(10.0);
          dropShadow.setColor(Color.color(0.4, 0.5, 0.5)); //must have color value for shadow
          graphicsContext.setEffect(dropShadow); //sets shadow effect
          event.consume();
        }//end if
      }//end handle
    });//end event handler
		
    //draw stroke when mouse is dragged/held down
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
				//make sure shadow is pressed
        if((isShadowButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());;
          graphicsContext.lineTo(event.getX(), event.getY());
          graphicsContext.stroke();
          dropShadow.setRadius(5.0);
          dropShadow.setOffsetX(3.0);
          dropShadow.setOffsetY(3.0);
          dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
          graphicsContext.setEffect(dropShadow); //sets shadow effect
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end shadow

	/**creates inverted duplicate stroke underneath stroke by wherever uses clicks and drags mouse on canvas */
  public void drawReflectionEffect(Canvas canvas){
    final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
    graphicsContext.beginPath();
    graphicsContext.setEffect(null);
    //When mouse is first pressed, begin drawing path
    canvas.addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isReflectionButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false) ){
          graphicsContext.beginPath();
          graphicsContext.moveTo(event.getX(), event.getY());
          graphicsContext.setStroke(cursorColor);
          reflection.setFraction(0.9);
          graphicsContext.setEffect(reflection);
          event.consume();
        }//end if
      }//end handle
    });//end event handler
    //draw stroke when mouse is dragged/held down
		
    canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>(){
      @Override
      public void handle(MouseEvent event) {
        if((isReflectionButtonPressed == true) && (isRectanglePressed == false) && (isCirclePressed == false) &&
        (isTrianglePressed == false) && (isEraserPressed == false) && (isPencilPressed == false)
        && (isPaintBrushPressed == false)){
          graphicsContext.setLineWidth(getBrushSize());;
          graphicsContext.lineTo(event.getX(), event.getY());
          graphicsContext.stroke();
          //reflection.setFraction(0.9);
          //graphicsContext.setEffect(reflection);
          event.consume();
        }//end if
      }//end handle
    });//end event handler
  }//end reflection

	/**observer registration method */
  public void registerObserver(DrawObserver o){
    observerList.add(o);
  }//end register
	 
	/**observer nofitication method */
  public void notifyDrawObservers(){
    for(int i = 0; i < observerList.size(); i++){
      DrawObserver observer = (DrawObserver)observerList.get(i);
        observer.updateSlider();
        observer.updateCanvas();
    }
  }//end notifyDrawObservers

	/**observer removal method */
  public void removeObserver(DrawObserver observer){
    int i = observerList.indexOf(observer);
    if(i>=0){
      observerList.remove(i);
    }
  }//end removeObserver

	 
	 
}//end DrawModel class
