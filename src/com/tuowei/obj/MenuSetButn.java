package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.tuowei.control.PS;
import com.tuowei.sound.MuAuPlayer;

public class MenuSetButn {
	private int x, y;
	private ImagesButton soundSetButn;

	public MenuSetButn() {
		soundSetButn = new ImagesButton(51, 70, 84, 51, 2, 4);// about
		soundSetButn.setPosition(205, 738, 0, 0);
		x = 205 - 5;
		y = 738 - 5;
	}

	public void paintX(Canvas g, Paint p) {
		soundSetButn.paintX(g, p);
		if (!PS.IS_SoundMU) {
			g.drawBitmap(Pic.imageSrcs(103), x, y, p);
		}
	}

	public boolean keyAction(TouchEvent te) {
		switch (soundSetButn.keyAction(te)) {
		case 3:
			if (PS.IS_SoundMU) {
				PS.IS_SoundMU = false;
				MuAuPlayer.muaup.mupStop();
				MuAuPlayer.muaup.disMAData();
			} else {
				PS.IS_SoundMU = true;
				MuAuPlayer.muaup.loadMAData();
				MuAuPlayer.muaup.mupStart();
			}
			return true;
		}
		return false;
	}

	public void setStatus(int i) {
		soundSetButn.setStatus(i);
	}

}
