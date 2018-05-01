package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.tuowei.control.PS;
import com.tuowei.sound.MuAuPlayer;

public class SoundButn {
	private ImagesButton butn;
	private boolean isEnable;
	private int x1, y1;

	public SoundButn(int cx, int cy) {
		butn = new ImagesButton(57, 90, 84, 51, 2, 4);// set
		butn.setImageType(5);
		butn.setPosition(cx, cy, 22, 16);
		x1 = cx + 17;
		y1 = cy + 13;
		isEnable = PS.IS_SoundMU;
	}

	public void keyAction(TouchEvent te) {
		switch (butn.keyAction(te)) {
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
			isEnable = PS.IS_SoundMU;
			break;
		}
	}

	public void paint(Canvas g, Paint p) {
		butn.paintData(g, p);
		butn.paint(g, p);
		if (!isEnable) {
			g.drawBitmap(Pic.imageSrcs(103), x1, y1, p);
		}
		butn.paintData1(g, p);
	}

	public void setStatus() {
		butn.setStatus(2);
	}
}
