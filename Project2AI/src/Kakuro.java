import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class Kakuro extends Application {
	
    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage primaryStage) throws IOException {
    	
    	KakuroBoard board = new KakuroBoard();//Grab the board
    	
    	
    	//Set Variables
    	int rowNum = board.getRowNum();
    	int colNum = board.getColNum();
    	int sideLen = 30;
    	
    	//Set scene title
        primaryStage.setTitle("Kakuro "+rowNum+"x"+colNum);
        
        //Grid of board
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(5, 5, 5, 5));
        
        //Create grid
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {

            	//Set up Square
                Rectangle rec = new Rectangle(sideLen,sideLen);
                rec.setStrokeType(StrokeType.INSIDE);
                rec.getStyleClass().add("square");
                StackPane stack = new StackPane();
                
                
                //Grab cell
                Cell current = board.getBoard()[row][col];

                //If wall
                if(current.getIsWall()) {
                	Text textU = new Text((current.getuTvalue()==0?"": current.getuTvalue())+" ");
                	double scale = 0.9;//scale size
                	textU.setScaleX(scale);
                	textU.setScaleY(scale);
                	Text textB = new Text(" "+(current.getlTvalue()==0?"": current.getlTvalue()));
                	textB.setScaleX(scale);
                	textB.setScaleY(scale);
                	
                	
                	//Add the triangles
                	Polygon triangleU = new Polygon();
                    triangleU.getPoints().addAll((double)sideLen, 0.0,  0.0, 0.0,(double)sideLen, (double)sideLen);
                    triangleU.setStrokeType(StrokeType.INSIDE);
                    triangleU.getStyleClass().add("triangle");
                    
                    Polygon triangleL = new Polygon();
                    triangleL.getPoints().addAll(0.0, (double)sideLen, 0.0, 0.0,(double)sideLen, (double)sideLen);
                    triangleL.setStrokeType(StrokeType.INSIDE);
                    triangleL.getStyleClass().add("triangle");
                    
                    //add to stack and position stuff
                	stack.getChildren().addAll(rec, triangleU,triangleL, textU, textB);
                	StackPane.setAlignment(textU, Pos.TOP_RIGHT);
                	StackPane.setAlignment(textB, Pos.BOTTOM_LEFT);       	
                }
                
                else {
                	
                	Text value = new Text((current.getValue()==0?"": current.getValue())+"");
                	stack.getChildren().addAll(rec, value);   
                	
                }

            	//add to grid
                GridPane.setRowIndex(stack, row);
                GridPane.setColumnIndex(stack, col);
                grid.add(stack, col, row);//stupid col row order

            }
        }
        
		//Add the BorderPane to the Scene
		Scene appScene = new Scene(grid,sideLen*colNum+10,sideLen*colNum+10);
		//Apply style sheet
		appScene.getStylesheets().addAll(this.getClass().getResource("board.css").toExternalForm());
		//Add the Scene to the Stage
		primaryStage.setScene(appScene);
		primaryStage.show();//launch app

    }


}