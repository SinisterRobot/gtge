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
package com.golden.gamedev.engine.audio;

// JFC
import java.net.URL;
import javax.sound.sampled.*;

// GTGE
import com.golden.gamedev.engine.BaseAudioRenderer;


/**
 * Play wave sound (*.wav, *.au).
 */
public class WaveRenderer extends BaseAudioRenderer implements LineListener {


 /*************************** WAVE PROPERTIES ********************************/

	private Clip 		clip;


 /*********************** VALIDATING WAVE RENDERER ***************************/

	private static boolean available;
	private static boolean volumeSupported;
	private static Mixer mixer;				// workaround for Java 1.5

	private static final int UNINITIALIZED = 0;
	private static final int INITIALIZING  = 1;
	private static final int INITIALIZED   = 2;

	private static int rendererStatus = UNINITIALIZED;


 /****************************************************************************/
 /***************************** CONSTRUCTOR **********************************/
 /****************************************************************************/

	/**
	 * Creates new wave audio renderer.
	 */
	public WaveRenderer() {
		if (rendererStatus == UNINITIALIZED) {
			rendererStatus = INITIALIZING;

			Thread thread = new Thread() {
			    public final void run() {
					try {
						URL sample = WaveRenderer.class.getResource("Sample.wav");
					    AudioInputStream ain = AudioSystem.getAudioInputStream(sample);
						AudioFormat format = ain.getFormat();

					    DataLine.Info info =
							new DataLine.Info(Clip.class, ain.getFormat(),
											  ((int) ain.getFrameLength() * format.getFrameSize()));
					    Clip clip = (Clip) AudioSystem.getLine(info);

						clip.open(ain);

						volumeSupported = clip.isControlSupported(FloatControl.Type.VOLUME) ||
										  clip.isControlSupported(FloatControl.Type.MASTER_GAIN);

					    clip.drain();
						clip.close();

						// workaround for Java 1.5
				        Mixer.Info[] mixers = AudioSystem.getMixerInfo();
				        for (int i = 0; i < mixers.length; i++) {
				            if ("Java Sound Audio Engine".equals(mixers[i].getName())) {
				                mixer = AudioSystem.getMixer(mixers[i]);
				            }
				        }

						available = true;
					} catch (Throwable e) {
						System.err.println("WARNING: Wave audio playback is not available!");
						available = false;
					}

					rendererStatus = INITIALIZED;
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}

    public boolean isAvailable() {
	    if (rendererStatus != INITIALIZED) {
		    int i = 0;
			while (rendererStatus != INITIALIZED && i++ < 50) {
			    try {
					Thread.sleep(50L);
				} catch (InterruptedException e) { }
			}

			if (rendererStatus != INITIALIZED) {
				rendererStatus = INITIALIZED;
				available = false;
			}
		}

		return available;
	}


 /****************************************************************************/
 /************************ AUDIO PLAYBACK FUNCTION ***************************/
 /****************************************************************************/

	protected void playSound(URL audiofile) {
		try {
			if (clip != null) {
				clip.drain();
				clip.close();
			}

		    AudioInputStream ain = AudioSystem.getAudioInputStream(getAudioFile());
			AudioFormat format = ain.getFormat();

			if ((format.getEncoding() == AudioFormat.Encoding.ULAW) ||
				(format.getEncoding() == AudioFormat.Encoding.ALAW)) {
				// we can't yet open the device for ALAW/ULAW playback,
				// convert ALAW/ULAW to PCM
				AudioFormat temp =
					new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
									format.getSampleRate(),
									format.getSampleSizeInBits() * 2,
									format.getChannels(),
									format.getFrameSize() * 2,
									format.getFrameRate(),
									true);
				ain = AudioSystem.getAudioInputStream(temp, ain);
				format = temp;
			}

		    DataLine.Info info =
				new DataLine.Info(Clip.class, ain.getFormat(),
								  ((int) ain.getFrameLength() * format.getFrameSize()));
			// workaround for Java 1.5
			if (mixer != null) {
				clip = (Clip) mixer.getLine(info);

			} else {
				clip = (Clip) AudioSystem.getLine(info);
			}
//		    clip = (Clip) AudioSystem.getLine(info);
		    clip.addLineListener(this);

			clip.open(ain);
			clip.start();

			// the volume of newly loaded audio is always 1.0f
			if (volume != 1.0f) {
				setSoundVolume(volume);
			}
		} catch (Exception e) {
			status = ERROR;
			System.err.println("ERROR: Can not playing " + getAudioFile() +
							   " caused by: " + e);
		}
	}

	protected void replaySound(URL audiofile) {
		clip.setMicrosecondPosition(0);
	    clip.start();
	    clip.addLineListener(this);
	}

    protected void stopSound() {
	    clip.stop();
		clip.setMicrosecondPosition(0);
		clip.removeLineListener(this);
	}


 /****************************************************************************/
 /************************* WAVE EVENT LISTENER ******************************/
 /****************************************************************************/

    /**
     * Notified when the sound is stopped externally.
     */
	public void update(LineEvent e) {
	    if (e.getType() == LineEvent.Type.STOP) {
		    status = END_OF_SOUND;
		    clip.stop();
		    clip.setMicrosecondPosition(0);
		    clip.removeLineListener(this);
		}
	}


 /****************************************************************************/
 /************************** AUDIO VOLUME SETTINGS ***************************/
 /****************************************************************************/

    protected void setSoundVolume(float volume) {
	    if (clip == null) {
			return;
		}

		Control.Type vol1 = FloatControl.Type.VOLUME,
					 vol2 = FloatControl.Type.MASTER_GAIN;

		if (clip.isControlSupported(vol1)) {
			FloatControl volumeControl = (FloatControl) clip.getControl(vol1);
        	volumeControl.setValue(volume);

      	} else if (clip.isControlSupported(vol2)) {
    		FloatControl gainControl = (FloatControl) clip.getControl(vol2);
        	float dB = (float) (Math.log(((volume == 0.0) ? 0.0001 : volume)) / Math.log(10.0)*20.0);
        	gainControl.setValue(dB);
      	}
	}

	public boolean isVolumeSupported() {
		return volumeSupported;
	}

}