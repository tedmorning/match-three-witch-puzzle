package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.tuowei.bdll.GameMainActivity;

public class ShopButn {
	private static final float[] scale = { 0.7f, 0.9f, 1.1f, 1, 0.95f, 1,
			1.05f, 1 };

	private int scaleIndex;
	private long scaleTimeo;

	private int x, y, x1, y1;
	private int rx, ry, rx1, ry1;
	private int px, py, degrees, px1, py1;

	private ImagesButton butn;

	public ShopButn(int cx, int cy) {
		x = cx - 27;
		y = cy - 28;

		rx = x + 144;
		ry = y + 139;
		px = x + 72;
		py = y + 70;

		x1 = cx + 16;
		y1 = cy + 11;

		rx1 = x1 + 58;
		ry1 = y1 + 61;
		px1 = x1 + 29;
		py1 = y1 + 30;

		butn = new ImagesButton(57, 90, 84);
		butn.setPosition(cx, cy, 0, 0);
	}

	public boolean keyAction(TouchEvent te) {
		switch (butn.keyAction(te)) {
		case 3:
			GameMainActivity.bffa.changeView(1);
			return true;
		}

		return false;
	}

	public void paint(Canvas g, Paint p) {
		int butnStatus = butn.getStatus();
		butn.paintX(g, p);

		switch (butnStatus) {
		case 1:
			g.save();
			g.clipRect(x, y, rx, ry);
			g.scale(scale[0], scale[0], px1, py1);
			g.drawBitmap(Pic.imageSrcs(44), x, y, p);
			g.drawBitmap(Pic.imageSrcs(45), x1, y1, p);
			g.restore();
			break;
		default:
			g.save();
			g.clipRect(x, y, rx, ry);
			g.rotate(degrees, px, py);
			g.drawBitmap(Pic.imageSrcs(44), x, y, p);
			g.restore();
			g.save();
			g.clipRect(x1, y1, rx1, ry1);
			g.scale(scale[scaleIndex], scale[scaleIndex], px1, py1);
			g.drawBitmap(Pic.imageSrcs(45), x1, y1, p);
			g.restore();
			if (System.currentTimeMillis() - scaleTimeo > 50) {
				scaleIndex++;
				degrees += 20;
				if (degrees >= 360)
					degrees -= 360;

				if (scaleIndex >= scale.length) {
					scaleIndex = 0;
				}

				scaleTimeo = System.currentTimeMillis();
			}
			break;
		}

	}
}
