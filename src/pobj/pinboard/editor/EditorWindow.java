package pobj.pinboard.editor;

import java.io.File;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import pobj.pinboard.document.Board;
import pobj.pinboard.document.Clip;
import pobj.pinboard.document.ClipGroup;
import pobj.pinboard.editor.commands.CommandGroup;
import pobj.pinboard.editor.commands.CommandUngroup;
import pobj.pinboard.editor.tools.Tool;
import pobj.pinboard.editor.tools.ToolEllipse;
import pobj.pinboard.editor.tools.ToolImage;
import pobj.pinboard.editor.tools.ToolRect;
import pobj.pinboard.editor.tools.ToolSelection;

public class EditorWindow implements EditorInterface,ClipboardListener {
	private Board board;
	private Color color = Color.BLACK;
	private Tool tool = new ToolRect(); //par défaut
	private Selection selection = new Selection();
	private Clipboard clipBoard = Clipboard.getInstance();
	private CommandStack commandStack = new CommandStack();
	private MenuItem Copy,Paste,Delete,Undo,Redo;
	
	public EditorWindow(Stage stage){
		board = new Board();
		stage.setTitle("コルクボード"); //choix d’un titre.
		VBox vbox = new VBox(); //création d’un layout.
		
		MenuBar menuBar = new MenuBar();
		ToolBar toolBar = new ToolBar();
		
		Menu file = new Menu("File");
		MenuItem New = new MenuItem("New");
		MenuItem Close = new MenuItem("Close");
		file.getItems().addAll(New, Close);
		
		Menu edit = new Menu("Edit");
		Copy = new MenuItem("Copy");
		Paste = new MenuItem("Paste");
		Delete = new MenuItem("Delete");
		Undo = new MenuItem("Undo");
		Redo = new MenuItem("Redo");
		
		edit.getItems().addAll(Copy,Paste,Delete,Undo,Redo);
		
		Menu tools = new Menu("Tools");
		MenuItem Rectangle = new MenuItem("Rectangle");
		MenuItem Ellipse = new MenuItem("Ellipse");
		tools.getItems().addAll(Rectangle,Ellipse);
		
		Menu group = new Menu("Groupage");
		MenuItem Grouper = new MenuItem("Grouper");
		MenuItem Degrouper = new MenuItem("Dégrouper");
		group.getItems().addAll(Grouper,Degrouper);
		
		menuBar.getMenus().addAll(file, edit, tools, group);
		
		Button box = new Button("Box");
		Button ellipse = new Button("Ellipse");
		Button img = new Button("Img...");
		Button select = new Button("Select");
		toolBar.getItems().addAll(box, ellipse, img, select);
		
		Canvas canvas = new Canvas(500, 500);
		Label label = new Label();
		
		
		
		New.setOnAction((e)-> { new EditorWindow(new Stage()); });
		Close.setOnAction((e)-> { stage.close(); clipBoard.removeListener(this);});
		
		Rectangle.setOnAction((e)-> { this.tool = new ToolRect(); label.setText("Filled Rectangle tool"); });
		Ellipse.setOnAction((e)-> { this.tool = new ToolEllipse(); label.setText("Filled Ellipse tool"); });
		
		Copy.setOnAction((e)->{	clipBoard.copyToClipboard(selection.getContents());	});
		Paste.setOnAction((e)->{
			board.addClip(clipBoard.copyFromClipboard());
			board.draw(canvas.getGraphicsContext2D());
		});
		Delete.setOnAction((e)->{
			board.removeClip(selection.getContents());
			board.draw(canvas.getGraphicsContext2D());
		});
		Undo.setOnAction((e)->{
			commandStack.undo();
			clipBoard.notifyListeners();
			board.draw(canvas.getGraphicsContext2D());
		});
		Redo.setOnAction((e)->{
			commandStack.redo();
			clipBoard.notifyListeners();
			board.draw(canvas.getGraphicsContext2D());
		});
		
		Grouper.setOnAction((e)->{
			CommandGroup groupe = new CommandGroup(this,selection.getContents());
			groupe.execute();
			commandStack.addCommand(groupe);
		});
		Degrouper.setOnAction((e)->{
			for (Clip clip: selection.getContents())
				if (clip instanceof ClipGroup) {
					CommandUngroup ungroup = new CommandUngroup(this,(ClipGroup) clip);
					ungroup.execute();
					commandStack.addCommand(ungroup);
				}
		});
		
		
		box.setOnAction((e)->{ this.tool = new ToolRect(); label.setText("Filled Rectangle tool"); });
		ellipse.setOnAction((e)->{ this.tool = new ToolEllipse(); label.setText("Filled Ellipse tool"); });
		img.setOnAction((e)->{
			FileChooser f = new FileChooser();
			f.setTitle("Choisissez un fichier.");
			f.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			File selectedFile = f.showOpenDialog(stage);
			if (selectedFile != null) tool = new ToolImage(selectedFile);
			label.setText("Image tool");
		});
		select.setOnAction((e)->{ this.tool = new ToolSelection() ; label.setText("Selection tool");});
		
		
		canvas.setOnMousePressed((e)-> { tool.press(this, e); });
		canvas.setOnMouseDragged((e)-> {
			tool.drag(this, e);
			board.draw(canvas.getGraphicsContext2D());
			tool.drawFeedback(this, canvas.getGraphicsContext2D());
		});
		canvas.setOnMouseReleased((e)-> {
			tool.release(this, e);
			board.draw(canvas.getGraphicsContext2D());
			tool.drawFeedback(this, canvas.getGraphicsContext2D());
			clipBoard.notifyListeners();
		});
		
		clipBoard.addListener(this);
		
		clipboardChanged();
		
		vbox.getChildren().addAll(menuBar,toolBar,canvas,label); //création et ajout à cette boîte de contrôles.
		Scene scene = new Scene(vbox); //création d’un objet scène contenant notre boîte principale.
		stage.setScene(scene); //association de la scène à la fenêtre
		stage.show(); //finalement, affichage de la fenêtre.
	}
	
	@Override
	public Board getBoard() {
		return board;
	}
	@Override
	public Selection getSelection() {
		return selection;
	}
	@Override
	public CommandStack getUndoStack() {
		return commandStack;
	}
	
	@Override
	public Color getCurrentColor() {
		return color;
	}
	
	public void clipboardChanged() {
		if(clipBoard.isEmpty()) Paste.setDisable(true);
		else Paste.setDisable(false);
		if(selection.getContents().isEmpty()) {
			Copy.setDisable(true);
			Delete.setDisable(true);
		}
		else {
			Copy.setDisable(false);
			Delete.setDisable(false);
		}
		if(commandStack.isUndoEmpty()) Undo.setDisable(true);
		else Undo.setDisable(false);
		if(commandStack.isRedoEmpty()) Redo.setDisable(true);
		else Redo.setDisable(false);
	}
}
