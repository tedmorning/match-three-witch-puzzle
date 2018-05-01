package com.tuowei.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.tuowei.bdll.R;
import com.tuowei.bdll.GameMainActivity;

/**
 * “Ù¿÷“Ù–ß≤•∑≈¿‡
 * 
 * @author Joniy
 * 
 */
public class MuAuPlayer {

	MediaPlayer mup;// ±≥æ∞“Ù¿÷
	SoundPool aup;// ±≥æ∞“Ù–ß
	SparseIntArray aupm;// “Ù–ßIDª∫¥Ê

	private int muauStatus = MUAU.DIS;

	/**
	 * “Ù¿÷≤•∑≈
	 */
	public void mupStart() {
		if (muauStatus == MUAU.LOAD) {
			if (mup != null && !mup.isPlaying()) {
				mup.start();
			}
		}
	}

	/**
	 * “Ù¿÷Õ£÷π
	 */
	public void mupStop() {
		if (muauStatus == MUAU.LOAD) {
			if (mup != null && mup.isPlaying()) {
				mup.stop();
			}
		}
	}

	/**
	 * “Ù–ß≤•∑≈
	 * 
	 * @param auid
	 */
	public void aupStart(int auid) {
		if (muauStatus == MUAU.LOAD) {
			if (aup != null) {
				AudioManager mgr = (AudioManager) GameMainActivity.bffa
						.getSystemService(Context.AUDIO_SERVICE);
				float streamVolumeCurrent = mgr
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				float streamVolumeMax = mgr
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volume = streamVolumeCurrent / streamVolumeMax;
				aup.play(aupm.get(auid), volume, volume, 1, 0, 1f);// ≤•∑≈…˘“Ù
			}
		}
	}

	/**
	 * “Ù–ßÕ£÷π
	 * 
	 * @param auid
	 */
	public void aupStop(int auid) {
		if (muauStatus == MUAU.LOAD) {
			if (aup != null) {
				aup.pause(aupm.get(auid));
			}
		}
	}

	/**
	 * “Ù–ßÕ£÷π(À˘”–)
	 */
	public void aupStopAll() {
		if (muauStatus == MUAU.LOAD) {

			for (int i = 0, j = aupm.size(); i < j; i++) {
				aup.stop(aupm.valueAt(i));
			}
		}
	}

	/**
	 * ≥ı ºªØ“Ù∆µ
	 */
	public void loadMAData() {
		if (muauStatus == MUAU.DIS) {
			mup = MediaPlayer.create(GameMainActivity.bffa, R.raw.mu0);
			mup.setLooping(true);// —≠ª∑≤•∑≈
			aup = new SoundPool(6, AudioManager.STREAM_MUSIC, 10);
			aupm = new SparseIntArray();
			aupm.put(MUAU.t5, aup.load(GameMainActivity.bffa, R.raw.t5, 1));
			aupm.put(MUAU.t7, aup.load(GameMainActivity.bffa, R.raw.t7, 1));
			aupm.put(MUAU.t8, aup.load(GameMainActivity.bffa, R.raw.t8, 1));
			aupm.put(MUAU.t9, aup.load(GameMainActivity.bffa, R.raw.t9, 1));
			aupm.put(MUAU.t10, aup.load(GameMainActivity.bffa, R.raw.t10, 1));
			aupm.put(MUAU.t11, aup.load(GameMainActivity.bffa, R.raw.t11, 1));

			muauStatus = MUAU.LOAD;
		}
	}

	/**
	 *  Õ∑≈“Ù∆µ
	 */
	public void disMAData() {
		if (muauStatus == MUAU.LOAD) {
			mupStop();
			aupStopAll();
			mup = null;
			aup = null;
			aupm.clear();
			aupm = null;
			muauStatus = MUAU.DIS;
		}
	}

	// ===================================================

	public static MuAuPlayer muaup = new MuAuPlayer();

	public MuAuPlayer() {
		muaup = this;
	}

}
