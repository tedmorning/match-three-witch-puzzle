package com.tuowei.obj;

import com.gameFrame.pic.Pic;

import android.graphics.Canvas;
import android.graphics.Paint;

public class NameObj {

	private int alpha;
	private int y;
	private int imageIndex;

	/** 0上升，减速，1显示，2加速，3消失 */
	public int status;
	private int ySpeed;
	private long timeo;

	public NameObj(int imageIndex) {
		this.imageIndex = imageIndex;
		alpha = 100;
		y = 500;
		ySpeed = 10;
	}

	public void paint(Canvas g, Paint p) {
		p.setAlpha(alpha);

		g.drawBitmap(Pic.imageSrcs(imageIndex), 160, y, p);
		p.setAlpha(255);

		if (System.currentTimeMillis() - timeo > 20) {
			switch (status) {
			case 0:
				if (alpha < 255) {
					alpha += 5;
				}
				y -= ySpeed;
				ySpeed--;
				if (ySpeed < 0) {
					ySpeed = 0;
					status = 1;
				}
				break;
			case 1:
				alpha++;
				if (alpha >= 255) {
					alpha = 255;
					status = 2;
				}
				break;
			case 2:
				alpha -= 10;
				if (alpha < 10) {
					alpha = 10;
					status = 3;
				}
				y -= ySpeed;
				if (ySpeed > 0) {
					ySpeed += 2;
				}
				break;
			}
			timeo = System.currentTimeMillis();
		}
	}
}
