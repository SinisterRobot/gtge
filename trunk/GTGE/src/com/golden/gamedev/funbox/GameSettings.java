/*
 * Copyright (c) 2008 Golden T Studios.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.golden.gamedev.funbox;

// JFC
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;

// GTGE
import com.golden.gamedev.util.ImageUtil;
import com.golden.gamedev.engine.graphics.WindowExitListener;


/**
 * <code>GameSettings</code> is a dialog UI to show the user the basic game
 * options for the player to choose of. <p>
 *
 * The options includes : <br>
 * Option to play between fullscreen or windowed mode, option to play using
 * bufferstrategy or not, and option turn on or turn off the sound. <p>
 *
 * To give the user more options or remove some options, subclass this class and
 * override the {@link #initSettings() initSettings()} method. <p>
 *
 * Example of how-to-use <code>GameSettings</code> class :
 * <pre>
 * GameSettings settings = new GameSettings() {
 *    public void start() {
 *       // here goes the usual game initialization
 *       GameLoader game = new GameLoader();
 *
 *       game.setup(new RoboticsWarGame() {
 *          protected void initEngine() {
 *             super.initEngine();
 *             // set active sound base on user setting
 *             bsSound.setActive(sound.isSelected());
 *             bsMusic.setActive(sound.isSelected());
 *          }
 *       }, new Dimension(640,480), fullscreen.isSelected(), bufferstrategy.isSelected());
 *
 *       game.start();
 *    }
 *
 *    // example removing bufferstrategy option from the list
 *    // then adding new option to use OpenGL or not
 *    JCheckBox opengl;
 *
 *    protected JPanel initSettings() {
 *       JPanel pane = super.initSettings();
 *
 *       // remove bufferstrategy option
 *       pane.remove(bufferstrategy);
 *
 *       // add opengl option
 *       opengl = new JCheckBox("OpenGL", true);
 *       pane.add(opengl, 0);
 *    }
 * };
 * </pre>
 *
 * @see #initSettings()
 */
public abstract class GameSettings extends JDialog implements ActionListener,
															  Runnable {


 /***************************** CHECK BOX UI *********************************/

    /**
     * Check box UI for fullscreen option.
     */
	protected JCheckBox 	fullscreen;

    /**
     * Check box UI for bufferstrategy option.
     */
	protected JCheckBox 	bufferstrategy;

    /**
     * Check box UI for sound option.
     */
	protected JCheckBox 	sound;

	/**
	 * The OK button, clicking this button will launch the game.
	 *
	 * @see #actionPerformed(ActionEvent)
	 */
	protected JButton		btnOK;

	/**
	 * The Cancel button, clicking this button will abort the game.
	 *
	 * @see #actionPerformed(ActionEvent)
	 */
	protected JButton		btnCancel;

	/**
	 * The splash image URL, null if the game has no splash image.
	 */
	protected URL			splashImage;


 /****************************************************************************/
 /**************************** CONSTRUCTOR ***********************************/
 /****************************************************************************/

	/**
	 * Builds up the game settings/options dialog for the user with specified
	 * splash image URL or null . <p>
	 *
	 * For example :
	 * <pre>
	 *    new GameSettings(YourGame.class.getResource("splash.jpg")) {
	 *       public void start() {
	 *          // create the actual game in here
	 *       }
	 *    };
	 * </pre>
	 *
	 * @see com.golden.gamedev.GameLoader
	 */
	public GameSettings(URL splashImage) {
		super((Frame) null, "Settings", true);

		// init dialog
		setResizable(false);
		addWindowListener(WindowExitListener.getInstance());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// init GUI
		this.splashImage = splashImage;
		JPanel contentPane = initGUI();
		setContentPane(contentPane);

	    pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Builds up the game settings/options dialog for the player without splash
	 * image.
	 */
	public GameSettings() {
		this(null);
	}

    /**
     * Skips the option dialog, and start the game with specified mode and
     * bufferstrategy. <p>
     *
     * Used for direct play without specifying the game option everytime running
     * the game while in developing stage.
     */
	public GameSettings(boolean fullScreen, boolean bufferStrategy) {
		initGUI();

	    fullscreen.setSelected(fullScreen);
	    bufferstrategy.setSelected(bufferStrategy);

		dispose();
		start();
	}

    /**
     * Skips the option dialog, and directly init the game with specified mode
     * and using bufferstrategy by default. <p>
     *
     * Used in game development, so no need to always specify the game option
     * everytime running the game while in developing stage.
     */
    public GameSettings(boolean fullscreen) {
	    this(fullscreen, true);
	}


 /****************************************************************************/
 /**************************** GUI INITIALIZATION ****************************/
 /****************************************************************************/

    /**
     * Initializes the content of the settings, by default consists of splash
     * image (if any), {@linkplain #initSettings() settings panel}, and launch
     * panel. <p>
     *
     * Example of modifying some UI components : <br>
     * The first component is splash image (JLabel), settings panel (JPanel),
     * and launch panel (JPanel), using BorderLayout
     * <pre>
     *    protected JPanel initGUI() {
     *       JPanel pane = super.initGUI();
     *
     *       // modify launch panel, add Credits button
     *       JPanel launchPane = (JPanel) pane.getComponent(2);
     *       launchPane.add(new JButton("Credits"), 0);
     *
     *       // remove splash image
     *       pane.remove(0);
     *
     *       // insert splash image
     *       pane.add(new JButton("Splash Image"), BorderLayout.NORTH);
     *    }
     * </pre>
     *
     * @return The <code>GameSettings</code> content pane.
     * @see #initSettings()
     */
	protected JPanel initGUI() {
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());

			// splash image
			JLabel splashLabel = null;
			if (splashImage != null) {
				try {
					splashLabel = new JLabel(new ImageIcon(ImageUtil.getImage(splashImage)));
				} catch (Exception e) {
					splashLabel = null;
				}
			}

			// option panel
			JPanel optionPane = initSettings();

			// submit pane
			JPanel submitPane = new JPanel();
			submitPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			submitPane.setBorder(BorderFactory.createEmptyBorder(0,0,3,0));
				// start button
				btnOK = new JButton("OK");
				btnOK.addActionListener(this);
				getRootPane().setDefaultButton(btnOK);

				// cancel button
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(this);
			submitPane.add(btnOK);
			submitPane.add(btnCancel);

        if (splashLabel != null) {
			contentPane.add(splashLabel, BorderLayout.NORTH);
		}
		if (optionPane != null) {
			contentPane.add(optionPane, BorderLayout.CENTER);
		}
        contentPane.add(submitPane, BorderLayout.SOUTH);

		return contentPane;
	}

    /**
     * Initializes and return the settings GUI, override this method to insert
     * new option or remove some options. <p>
     *
     * For example :
     * <pre>
     *    JCheckBox opengl;
     *
     *    protected JPanel initSettings() {
     *       JPanel pane = super.initSettings();
     *
     *       // add opengl option
     *       opengl = new JCheckBox("OpenGL");
     *
     *       // remove bufferstrategy option
     *       pane.remove(bufferstrategy);
     *
     *       // set windowed mode by default
     *       fullscreen.setSelected(false);
     *    }
     * </pre>
     *
     * @return The settings panel where all the settings are layed out.
     */
    protected JPanel initSettings() {
		JPanel optionPane = new JPanel();
		optionPane.setLayout(new GridLayout(0,2));
		Border border = BorderFactory.createEmptyBorder(6, 4, 3, 4);
		optionPane.setBorder(BorderFactory.createCompoundBorder(
			border, BorderFactory.createTitledBorder("Game Settings")));

    	GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().
								getDefaultScreenDevice();

		// full screen
		boolean supportFSEM = true;
		try {
			supportFSEM = device.isFullScreenSupported();
		} catch (Throwable e) {
			e.printStackTrace();
			supportFSEM = false;
		}
		fullscreen = new JCheckBox("Fullscreen", supportFSEM);
        fullscreen.setEnabled(supportFSEM);
    	fullscreen.setToolTipText((supportFSEM) ?
			"fullscreen/windowed mode" : "fullscreen mode not supported");

		// buffer strategy
		bufferstrategy = new JCheckBox("Bufferstrategy", true);
        bufferstrategy.setToolTipText("turn this off if the game experiencing graphics problem");

        // sound
        sound = new JCheckBox("Sound", true);
        sound.setToolTipText("turn on/off sound and music");

		optionPane.add(fullscreen);
		optionPane.add(bufferstrategy);
		optionPane.add(sound);

		return optionPane;
	}


	/**
	 * Notified when the user press 'OK' or 'Cancel' button. <p>
	 *
	 * Pressing 'OK' will launch the game, while pressing 'Cancel' will
	 * immediatelly close the app.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnOK) {
			// dispose this frame
			dispose();

			new Thread(this).start();

		} else if (e.getSource() == btnCancel) {
			System.exit(0);
		}
	}

	/**
	 * Thread implementation when the user pressing 'OK' button, directly called
	 * {@link #start()} method.
	 */
	public void run() {
		start();
	}


 /****************************************************************************/
 /**************************** START THE GAME ********************************/
 /****************************************************************************/

	/**
	 * Starts the game with all the defined settings, the game is actually
	 * created in this method. <p>
	 *
	 * This method is called when the user is pressing the 'OK' button.
	 */
	public abstract void start();


}