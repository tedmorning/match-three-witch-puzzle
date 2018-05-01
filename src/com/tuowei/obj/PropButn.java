package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;

public class PropButn {
	private ImagesButton butn;
	private int nx, ny;
	private int num;

	public PropButn(int x, int y, int upImage, int num) {
		butn = new ImagesButton(127, 76, 38, upImage);
		this.num = num;
		switch (upImage) {
		case 125:
			butn.setPosition(x, y, 15, 2);
			break;
		case 126:
			butn.setPosition(x, y, 10, 0);
			break;
		default:
			butn.setPosition(x, y, 20, 2);
			break;
		}
		nx = x + 55;
		ny = y + 15;
	}

	public void paint(Canvas g, Paint p) {
		butn.paintData(g, p);
		butn.paint(g, p);
		A.a.paintNum(g, p, Pic.imageSrcs(131), num, nx, ny);
		butn.paintData1(g, p);
	}

	public int keyAction(TouchEvent te) {
		return butn.keyAction(te);
	}

	public void setNum(int num) {
		this.num = num;
	}
}
