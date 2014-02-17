package se.afsa.evolutionai.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import se.afsa.evolutionai.entities.ComputerPlayer;
import se.afsa.evolutionai.event.GameEvent;
import se.afsa.evolutionai.event.GameEventType;
import se.afsa.evolutionai.event.GameListener;
import se.afsa.evolutionai.resource.Config;
import se.afsa.evolutionai.resource.FileHandler;

/**
 * Runs the game automatically by extracting and reentering entities into the game.
 * Is used for generating levels.
 */
public class Breeder implements GameListener {
	
	private int 
			turns,
			turnsLeft;
	private ProgressBar progressBar;
	private File file;
	private Config config = new Config();
	private Button[] button;

	/**
	 * Set the amount of turns the breeder should run.
	 * @param turns - the amount of turns.
	 * @param ui - the UI in which the game is run.
	 */
	public Breeder(int turns, ProgressBar progressBar, File file, Button[] button) {
		this.turns = turns;
		this.turnsLeft = turns;
		this.progressBar = progressBar;
		this.progressBar.setMinimum(0);
		this.progressBar.setMaximum(this.turns);
		this.progressBar.setSelection(0);
		this.file = file;
		this.button = button;
		buttonAccess(false, button);
	}
	
	/**
	 * Generate new children data from the survivors from the last run.
	 * @param parents - the entities that survived.
	 * @return A list of behavior data for the new entities.
	 */
	private List<BehaviorData> getChildrenData(List<ComputerPlayer> parents) {
		List<BehaviorData> children = new ArrayList<>();
		int parentLength = parents.size();
		int childPerParent = config.getInt("numberOfEntities", "10")/parentLength;
		for (int i = 0; i < parentLength; i++) {
			for (int j = 0; j < childPerParent; j++) {
				children.add(parents.get(i).getBehaviorData().createChildren(parents.get((i+1)%parentLength).getBehaviorData()));
			}
		}
		return children;
	}
	
	private void buttonAccess(boolean access, Button[] button) {
		for (int i = 0; i < button.length; i++) {
			button[i].setEnabled(access);
		}
	}
	
	private void swtThreadAccess(Runnable runnable) {
		Display.getDefault().syncExec(runnable);
	}

	// Detect when to reenter and extract the entities.
	@Override
	public void handleEvent(GameEvent event) {
		// TODO Auto-generated method stub
		if(event.getEventType() == GameEventType.STOP) {
			List<ComputerPlayer> computerPlayers = event.getEngine().getStage().getEntities(ComputerPlayer.class);
			List<BehaviorData> childrenBehaviorData = getChildrenData(computerPlayers);
			
			if(turnsLeft > 0) {
				turnsLeft--;
				swtThreadAccess(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressBar.setSelection(turns-turnsLeft);
					}
				});
				event.getEngine().getStage().addEnities(childrenBehaviorData, 0);
				event.getEngine().reload();
			} else {
				event.getEngine().getGameEventHandler().removeGameListener(this);
				new FileHandler().save(file, childrenBehaviorData.toArray());
				swtThreadAccess(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						buttonAccess(true, button);
						MessageBox messageBox = new MessageBox(new Shell(), SWT.ICON_INFORMATION);
						messageBox.setText("Level generator");
						messageBox.setMessage("Level generation finished!");
						messageBox.open();
						progressBar.setSelection(0);
					}
				});
			}
		}
	}

}
