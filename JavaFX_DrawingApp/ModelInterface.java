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

/*ModelInterface.java
 *@authors Tresa, Owen, Madeline
 */
 /*SUBJECT INTERFACE*/

  public interface ModelInterface{

  public void initializeBooleans();
  
  public void close();
      
  public void helpPopUp();

  public void clearAll(Canvas canvas);

  public int getBrushSize();

  public void setBrushSize(int value);

  public void setOpacity(double value);

  public void setDefaultBooleans(Canvas canvas);

  //public void updateSliders(Slider thisSlider);

  public void acceptandpassColor(Color color);

  public void setBackground(Color color, Canvas canvas);

  public void drawRectangle(Canvas canvas);

  public void drawCircle(Canvas canvas);

  public void drawPencil(Canvas canvas);

  public void drawPaintBrush(Canvas canvas);

  public void drawTriangle(Canvas canvas);

  public void eraserTool(Canvas canvas);

  public void drawBloom(Canvas canvas);

  public void drawShadow(Canvas canvas);

  public void drawReflectionEffect(Canvas canvas);

  public void setBooleans(HashMap hmap);

  void registerObserver(DrawObserver o);

  void removeObserver(DrawObserver o);
 }
