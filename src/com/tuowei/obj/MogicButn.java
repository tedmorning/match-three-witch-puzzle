package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.T;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;
import com.gameFrame.util.M;
import com.tuowei.db.DB;

public class MogicButn {
	private ImagesButton butn;
	private int nx, ny, nx1, ny1, nx2, ny2;
	private int havaNum;
	private int degrees;

	private int degreesSpeed;

	private int x, y, x1, y1, y0, numx, numy;
	private int xOff = 9;
	private int yOff = -5;
	/** [0]angle，[1]position,[2]x,[3]y */
	private int[][] mogicData = { { 0, 58 + xOff, 278 + yOff },
			{ -60, 58 + xOff, 267 + yOff }, { -122, 57 + xOff, 258 + yOff },
			{ -178, 48 + xOff, 259 + yOff }, { 123, 44 + xOff, 268 + yOff },
			{ 55, 48 + xOff, 276 + yOff } };

	private int mogicIndex;
	private long mogicTimeo;

	public MogicButn(int x, int y) {
		butn = new ImagesButton(132, 114, 114);
		butn.setPosition(x, y, 20, 2);
		// 闪光的坐标
		nx = x + 17;
		ny = y + 17;
		// 闪光的中心坐标
		nx1 = x + 57;
		ny1 = y + 57;

		// 魔法棒的坐标
		nx2 = x + 29;
		ny2 = y + 20;

		// 整体的坐标
		this.x = x;
		this.y0 = y;

		// 整体的右下角坐标
		this.x1 = x + 114;
		this.y1 = y + 114;
		degreesSpeed = M.getRandom(2, 30);
		numx = x + 85;
		numy = y + 85;
	}

	public void setHavaNum(int havaNum) {
		this.havaNum = havaNum;
		y = y0 + 114 - 114 * havaNum / 360;
	}

	public int getHavaNum() {
		return havaNum;
	}

	public void paint(Canvas g, Paint p) {
		butn.paintData(g, p);
		butn.paint(g, p);

		if (havaNum > 0) {
			g.save();
			g.clipRect(x, y, x1, y1);
			g.drawBitmap(Pic.imageSrcs(128), x, y0, p);
			g.restore();
		}
		if (havaNum > 120) {
			g.save();
			g.rotate(degrees, nx1, ny1);
			g.drawBitmap(Pic.imageSrcs(130), nx, ny, p);
			g.restore();
			degrees += degreesSpeed;
			if (degrees > 360) {
				degrees -= 360;
			}
		}
		if (havaNum > 120) {
			T.TP.paintImageRotate(g, p, Pic.imageSrcs(129), 60, 60,
					mogicData[mogicIndex][0], mogicData[mogicIndex][1],
					mogicData[mogicIndex][2]);
			if (System.currentTimeMillis() - mogicTimeo > 100) {
				mogicIndex++;
				if (mogicIndex >= mogicData.length) {
					mogicIndex = 0;
				}
				mogicTimeo = System.currentTimeMillis();
			}
		}
		g.drawBitmap(Pic.imageSrcs(123), nx2, ny2, p);
		butn.paintData1(g, p);

		int num = DB.db.getNumbss()[0];
		if (num > 0) {
			A.a.paintNum(g, p, Pic.imageSrcs(131), num, numx, numy);
		}
	}

	public int keyAction(TouchEvent te) {
		return butn.keyAction(te);
	}
}
