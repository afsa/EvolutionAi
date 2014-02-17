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
		
		Label generateLevel = new Label(generator, SWT.NONE);
		generateLevel.setText("Generate a new level.\nIterations to run the level generator:");
		generateLevel.setLayoutData(generateFormData(progressBar, 10, 20));
		
		numberLevels = new Combo(generator, SWT.READ_ONLY);
		numberLevels.setItems(new String[] {"10", "25", "50", "100", "200", "400", "800", "1600", "3200"});
		numberLevels.setLayoutData(generateFormData(generateLevel, 10, 40));
		numberLevels.select(0);
		
		Label startFromLevelInfo = new Label(generator, SWT.NONE);
		startFromLevelInfo.setText("Previous level to start from:");
		startFromLevelInfo.setLayoutData(generateFormData(numberLevels, 10, 20));
		
		Button startFromLevel = new Button(generator, SWT.NONE);
		startFromLevel.setText("Select level");
		startFromLevel.setLayoutData(generateFormData(startFromLevelInfo, 10, 40));
		startFromLevel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				continueFrom = selectFile("Open", SWT.OPEN, false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		startOver = new Button(generator, SWT.CHECK);
		startOver.setText("Start from new level");
		startOver.setLayoutData(generateFormDataNextTo(startFromLevel, startFromLevel, 0, 20));
		
		Label saveFileInfo = new Label(generator, SWT.NONE);
		saveFileInfo.setText("Save level as:");
		saveFileInfo.setLayoutData(generateFormData(startFromLevel, 10, 20));
		
		Button saveFile = new Button(generator, SWT.NONE);
		saveFile.setText("Save as");
		saveFile.setLayoutData(generateFormData(saveFileInfo, 10, 40));
		saveFile.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				save = selectFile("Save", SWT.SAVE, true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		startGenerate = new Button(generator, SWT.NONE);
		startGenerate.setText("Start generating");
		startGenerate.setLayoutData(generateFormData(saveFile, 10, 40));
		startGenerate.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if(save != null) {
					if(startOver.getSelection()) {
						startNonGUIGame(Integer.parseInt((numberLevels.getText())), null, save, progressBar, new Button[] {startGenerate, start});
					} else {
						startNonGUIGame(Integer.parseInt((numberLevels.getText())), continueFrom, save, progressBar, new Button[] {startGenerate, start});
					}
				} else {
					MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
					messageBox.setText("Error");
					messageBox.setMessage("Please select an location to save the level.");
					messageBox.open();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Create all graphical components used in the start-game form.
	 */
	private void startForm() {
		Composite starter = new Composite(shell, SWT.BORDER);	
		starter.setLayout(new FormLayout());
		
		Label startInfo = new Label(starter, SWT.NONE);
		startInfo.setText("Select number of players and level to play.\nFirst player uses WASD to move and the second player uses arrow keys.");
		startInfo.setLayoutData(generateFormData(5, 10, 20));
		
		Button selectLevel = new Button(starter, SWT.NONE);
		selectLevel.setText("Select level");
		selectLevel.setLayoutData(generateFormData(startInfo, 10, 40));
		selectLevel.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				open = selectFile("Open", SWT.OPEN, false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		randomLevel = new Button(starter, SWT.CHECK);
		randomLevel.setText("Generate random level");
		randomLevel.setLayoutData(generateFormDataNextTo(selectLevel, selectLevel, 0, 20));
		
		Label numberPlayersInfo = new Label(starter, SWT.NONE);
		numberPlayersInfo.setText("Select number of players:");
		numberPlayersInfo.setLayoutData(generateFormData(selectLevel, 10, 20));
		
		numberPlayers = new Combo(starter, SWT.READ_ONLY);
		numberPlayers.setItems(new String[] {"1 Player", "2 Players"});
		numberPlayers.setLayoutData(generateFormData(numberPlayersInfo, 10, 40));
		numberPlayers.select(0);
		
		Label gameModeInfo = new Label(starter, SWT.NONE);
		gameModeInfo.setText("Select gamemode:");
		gameModeInfo.setLayoutData(generateFormData(numberPlayers, 10, 20));
		
		gameModeSelecter = new Combo(starter, SWT.READ_ONLY);
		gameModeSelecter.setItems(new String[] {GameMode.ALL_PLAYERS_DEAD.toString(), GameMode.COOPERATION.toString()});
		gameModeSelecter.setLayoutData(generateFormData(gameModeInfo, 10, 40));
		gameModeSelecter.select(0);
		
		Label FPSinfo = new Label(starter, SWT.NONE);
		FPSinfo.setText("Select target FPS:");
		FPSinfo.setLayoutData(generateFormData(gameModeSelecter, 10, 20));
		
		targetFPS = new Combo(starter, SWT.READ_ONLY);
		targetFPS.setItems(new String[] {"30", "50", "60", "80", "100", "120"});
		targetFPS.setLayoutData(generateFormData(FPSinfo, 10, 40));
		targetFPS.select(2);
		
		start = new Button(starter, SWT.NONE);
		start.setText("Start game");
		start.setLayoutData(generateFormData(targetFPS, 10, 40));
		start.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				if(randomLevel.getSelection()) {
					loadGameGUI(numberPlayers.getSelectionIndex() + 1, GameMode.valueOf(gameModeSelecter.getText().toUpperCase().replace(" ", "_")), Integer.parseInt(targetFPS.getText()));
				} else {
					loadGameGUI(numberPlayers.getSelectionIndex() + 1, GameMode.valueOf(gameModeSelecter.getText().toUpperCase().replace(" ", "_")), Integer.parseInt(targetFPS.getText()), open);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
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
	
	private FormData generateFormData(Control control, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(control, offsetY);
		formData.left = new FormAttachment(0, offsetX);
		return formData;
	}
	
	private FormData generateFormData(int percentY, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(percentY, offsetY);
		formData.left = new FormAttachment(0, offsetX);
		return formData;
	}
	
	private FormData generateFormDataNextTo(Control controlY, Control controlX, int offsetY, int offsetX) {
		FormData formData = new FormData();
		formData.top = new FormAttachment(controlY, offsetY, SWT.CENTER);
		formData.left = new FormAttachment(controlX, offsetX);
		return formData;
	}
}
