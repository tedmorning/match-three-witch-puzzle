package com.tuowei.obj.ui;

import com.gameFrame.T;
import com.gameFrame.pic.Pic;

import android.graphics.Canvas;
import android.graphics.Paint;

public class LockObj {

	public static final float[] wPrp = { 27/42.0f, 32/42.0f, 40/42.0f, 34/42.0f, 32/42.0f };
	private int index;
	private long mTimeo;
	private int x, y;
	private int runTime;

	public boolean isDead;

	public LockObj(int x, int y, int runTime) {
		this.x = x;
		this.y = y;
		this.runTime = runTime;
	}

	public void paintX(Canvas g, Paint p) {
		if (isDead) {
			return;
		}
		g.save();
		g.scale(wPrp[index], wPrp[index], x, y);
		T.TP.paintImage(g, p, Pic.imageSrcs(30), x, y, T.ANCHOR_CHV);

		g.restore();
		if (System.currentTimeMillis() - mTimeo > 50) {
			index++;
			if (index >= wPrp.length) {
				index = 0;
			}
			mTimeo = System.currentTimeMillis();
		}

		runTime--;
		if (runTime < 0) {
			isDead = true;
		}

	}

}
