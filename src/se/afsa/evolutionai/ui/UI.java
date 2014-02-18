package se.afsa.evolutionai.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import se.afsa.evolutionai.engine.BehaviorData;
import se.afsa.evolutionai.engine.Breeder;
import se.afsa.evolutionai.engine.Engine;
import se.afsa.evolutionai.engine.GameMode;
import se.afsa.evolutionai.event.GameEventHandler;
import se.afsa.evolutionai.resource.FileHandler;
import se.afsa.evolutionai.stage.GUIStage;
import se.afsa.evolutionai.stage.Stage;


public class UI {
	
	private GameEventHandler gameEventHandler = new GameEventHandler();
	private Breeder breeder;
	
	private Display display = new Display();
	private Shell shell = new Shell(display, SWT.CLOSE | SWT.MIN);
	private final String path = "se/afsa/evolutionai/resource/data";
	private FileHandler fileHandler = new FileHandler();
	private Combo 
			numberPlayers,
			targetFPS,
			gameModeSelecter,
			numberLevels;
	private Button
			randomLevel,
			startOver,
			startGenerate,
			start;
	private File
			continueFrom,
			open,
			save;
	
	private ProgressBar progressBar;
	
	/**
	 * Open UI window.
	 */
	public UI() {
		shell.setImage(new Image(display, getClass().getClassLoader().getResourceAsStream(path + "/icon.png")));
		shell.setText("Evolution AI");
		
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		generatorForm();
		startForm();
		
		shell.setBackground(new Color(display, new RGB(255, 255, 255)));
		shell.pack();
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
		
		display.dispose();
		System.exit(0);
	}
	
	/**
	 * Load a GUI game. This method then start the GUI game.
	 * @param playerCount - the number of players. 
	 * @param gameMode - the mode that the game will use.
	 * @param FPS - the target FPS.
	 */
	private void loadGameGUI(int playerCount, GameMode gameMode, int FPS) {
		startGUIGame(playerCount, gameMode, FPS, null);
	}
	
	/**
	 * Load a GUI game. This method then start the GUI game.
	 * @param playerCount - the number of players. 
	 * @param gameMode - the mode that the game will use.
	 * @param FPS - the target FPS.
	 * @param level - the file with the level file.
	 */
	private void loadGameGUI(int playerCount, GameMode gameMode, int FPS, File level) {
		startGUIGame(playerCount, gameMode, FPS, load(level));
	}
	
	/**
	 * Load the level file and extract the data into a list.
	 * @param level - the level file
	 * @return A list of behavior data.
	 */
	private List<BehaviorData> load(File level) {
		if(level != null && level.exists()) {
			
			Object loadedData = fileHandler.load(level);
			
			if (loadedData instanceof Object[]) {
				
				List<BehaviorData> temp = new ArrayList<>();
				
				for (int i = 0; i < ((Object[])loadedData).length; i++) {
					
					Object tempEntity = ((Object[])loadedData)[i];
					
					if(tempEntity instanceof BehaviorData) {
						temp.add((BehaviorData) tempEntity);
					}
				}
				
				return temp;
			}
		}
		return null;
	}

	/**
	 * Start the GUI game
	 * @param playerCount - the number of players. 
	 * @param gameMode - the mode that the game will use.
	 * @param FPS - the target FPS.
	 * @param behaviorData - a list of all behavior data (if no data exists use null).
	 */
	private void startGUIGame(int playerCount, GameMode gameMode, int FPS, List<BehaviorData> behaviorData) {
		// TODO Auto-generated method stub
		
		GUIStage guiStage = new GUIStage();
		guiStage.init();
		
		guiStage.addEnities(behaviorData, playerCount);
		
		new Engine(guiStage, gameMode, FPS);
	}
	
	/**
	 * Start a game without the GUI. Used for simulations.
	 * @param turns - the number of iterations the simulation should run.
	 * @param startFile - the file in which the behavior data is stored, can be null.
	 * @param saveFile - the file in which the data should be stored.
	 * @param progress - a progress bar that displays the progress of the simulation.
	 * @param button - the buttons that should be disabled.
	 */
	private void startNonGUIGame(int turns, File startFile, File saveFile, ProgressBar progress, Button[] button) {
		Stage stage = new Stage();
		stage.addEnities(load(startFile), 0);
		
		breeder = new Breeder(turns, progress, saveFile, button);
		gameEventHandler.addGameListener(breeder);
		
		new Engine(stage, GameMode.LAST_FIVE, 0);
	}
	
	/**
	 * Create all graphical components used in the generator form.
	 */
	private void generatorForm() {
		Composite generator = new Composite(shell, SWT.BORDER);
		generator.setLayout(new FormLayout());
		
		progressBar = new ProgressBar(generator, SWT.NONE);
		progressBar.setLayoutData(generateFormData(5, 10, 40));
		
		Label generateLevel = getLabel(generator, "Generate a new level.\nIterations to run the level generator:",
				progressBar);
		
		numberLevels = getCombo(generator, new String[] {"10", "25", "50", "100", "200", "400", "800"},
				0, generateLevel);
		
		Label startFromLevelInfo = getLabel(generator, "Previous level to start from:", numberLevels);
		
		Button startFromLevel = getButton(generator, "Select level", startFromLevelInfo, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				continueFrom = selectFile("Open", SWT.OPEN, false);
			}
		});
		
		startOver = new Button(generator, SWT.CHECK);
		startOver.setText("Start from new level");
		startOver.setLayoutData(generateFormDataNextTo(startFromLevel, startFromLevel, 0, 20));
		
		Label saveFileInfo = getLabel(generator, "Save level as:", startFromLevel);
		
		Button saveFile = getButton(generator, "Save as", saveFileInfo, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				save = selectFile("Save", SWT.SAVE, true);
			}
		});
		
		startGenerate = getButton(generator, "Start generating", saveFile, new StartGenerating());
	}
	
	/**
	 * Create all graphical components used in the start-game form.
	 */
	private void startForm() {
		Composite starter = new Composite(shell, SWT.BORDER);	
		starter.setLayout(new FormLayout());
		
		Label startInfo = new Label(starter, SWT.NONE);
		startInfo.setText("Select number of players and level to play.\n"
				+ "First player uses WASD to move and the second player uses arrow keys.");
		startInfo.setLayoutData(generateFormData(5, 10, 20));
		
		Button selectLevel = getButton(starter, "Select level", startInfo, new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				open = selectFile("Open", SWT.OPEN, false);
			}
		});
		
		randomLevel = new Button(starter, SWT.CHECK);
		randomLevel.setText("Generate random level");
		randomLevel.setLayoutData(generateFormDataNextTo(selectLevel, selectLevel, 0, 20));
		
		Label numberPlayersInfo = getLabel(starter, "Select number of players:", randomLevel);
		
		numberPlayers = getCombo(starter, new String[] {"1 Player", "2 Players"}, 0, numberPlayersInfo);
		
		Label gameModeInfo = getLabel(starter, "Select gamemode:", numberPlayers);
		
		gameModeSelecter = getCombo(starter, 
				new String[] {GameMode.ALL_PLAYERS_DEAD.toString(), GameMode.COOPERATION.toString()},
				0, gameModeInfo);
		
		Label FPSinfo = getLabel(starter, "Select target FPS:", gameModeSelecter);
		
		targetFPS = getCombo(starter, new String[] {"30", "50", "60", "80", "100", "120"}, 2, FPSinfo);
		
		start = getButton(starter, "Start game", targetFPS, new StartGame());
	}
	
	/**
	 * Create a button.
	 * @param parent - parent composite.
	 * @param text - the text.
	 * @param previous - the previous object.
	 * @param runnable - fired when pressed.
	 * @return The button.
	 */
	private Button getButton(Composite parent, String text, Control previous, final Runnable runnable) {
		Button temp = new Button(parent, SWT.NONE);
		temp.setText(text);
		temp.setLayoutData(generateFormData(previous, 10, 40));
		temp.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				runnable.run();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		return temp;
	}
	
	/**
	 * Create a label.
	 * @param parent - parent composite.
	 * @param text - the text.
	 * @param previous - the previous object.
	 * @return The label.
	 */
	private Label getLabel(Composite parent, String text, Control previous) {
		Label temp = new Label(parent, SWT.NONE);
		temp.setText(text);
		temp.setLayoutData(generateFormData(previous, 10, 20));
		return temp;
	}
	
	/**
	 * Create a combo.
	 * @param parent - the parent composite.
	 * @param data - the data in the combo.
	 * @param selected - the selected data.
	 * @param previous - previous object.
	 * @return The combo.
	 */
	private Combo getCombo(Composite parent, String[] data, int selected, Control previous) {
		Combo temp = new Combo(parent, SWT.READ_ONLY);
		temp.setItems(data);
		temp.setLayoutData(generateFormData(previous, 10, 40));
		temp.select((data.length > selected) ? selected : 0);
		return temp;
	}
	
	/**
	 * Open a file selector dialog.
	 * @param text - the text that should be displayed as title.
	 * @param type - the type of operation (SWT.OPEN or SWT.SAVE).
	 * @param overwriteWarning - should the user be alerted if file is being overwrote. 
	 * @return The file selected.
	 */
	private File selectFile(String text, int type, boolean overwriteWarning) {
		FileDialog fileDialog = new FileDialog(shell, type);
		fileDialog.setFilterExtensions(new String[] {"*.level"});
		fileDialog.setOverwrite(overwriteWarning);
		String file = fileDialog.open();
		if (file != null) {
			return new File(file);
		}
		return null;
	}
	
	/**
	 * Create form data.
	 * @param control - the previous object.
	 * @param offsetY - y.
	 * @param offsetX - x.
	 * @return The form data.
	 */
	private FormData generateFormData(Control control, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(control, offsetY);
		formData.left = new FormAttachment(0, offsetX);
		return formData;
	}
	
	
	/**
	 * Create form data.
	 * @param percentY - the percent to top.
	 * @param offsetY - y.
	 * @param offsetX - x.
	 * @return The form data.
	 */
	private FormData generateFormData(int percentY, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(percentY, offsetY);
		formData.left = new FormAttachment(0, offsetX);
		return formData;
	}
	
	/**
	 * Create form data.
	 * @param controlY - previous y object.
	 * @param controlX - previous x object.
	 * @param offsetY - y.
	 * @param offsetX - x.
	 * @return The form data.
	 */
	private FormData generateFormDataNextTo(Control controlY, Control controlX, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(controlY, offsetY, SWT.CENTER);
		formData.left = new FormAttachment(controlX, offsetX);
		return formData;
	}
	
	/**
	 * @author Mattias Jönsson
	 * Start generating objects.
	 *
	 */
	class StartGenerating implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			if(save != null) {
				
				Button[] buttons = new Button[] {startGenerate, start};
				int turns = Integer.parseInt((numberLevels.getText()));
				
				if(startOver.getSelection()) {
					startNonGUIGame(turns, null, save, progressBar, buttons);
				} else {
					startNonGUIGame(turns, continueFrom, save, progressBar, buttons);
				}
			} else {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
				messageBox.setText("Error");
				messageBox.setMessage("Please select an location to save the level.");
				messageBox.open();
			}
		}
	}
	
	/**
	 * @author Mattias Jönsson
	 * Start game.
	 *
	 */
	class StartGame implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			
			int
				playerCount = numberPlayers.getSelectionIndex() + 1,
				FPS = Integer.parseInt(targetFPS.getText());
			
			GameMode gameMode = GameMode.valueOf(gameModeSelecter.getText().toUpperCase().replace(" ", "_"));
				
			
			if(randomLevel.getSelection()) {
				loadGameGUI(playerCount, gameMode, FPS);
			} else {
				loadGameGUI(playerCount, gameMode, FPS, open);
			}
		}
	}
}
