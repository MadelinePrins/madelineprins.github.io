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

public interface DrawObserver{
  void updateSlider();
  void updateCanvas();
}
