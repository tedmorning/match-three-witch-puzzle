package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;

public class MapButn {
	private ImagesButton butn;
	private int nx, ny, num;
	private int imageIndex;

	private boolean isShowNum = true;
	public boolean isEnable;
	private int positionY,positionX;

	public void setShowNum(boolean isShowNum) {
		this.isShowNum = isShowNum;
	}

	public int getNum() {
		return num;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public int keyAction(TouchEvent te) {
		return butn.keyAction(te);
	}

	public int getY() {
		return butn.getY();
	}

	public void setNum(int num) {
		this.num = num;
		if (num < 10) {
			nx = positionX + 28;
		} else {
			nx = positionX + 15;
		}
	}

	private int layerIndex;

	public void setLayerIndex(int layerIndex) {
		this.layerIndex = layerIndex;
	}

	public int getLayerIndex() {
		return layerIndex;
	}

	public MapButn(int imgIndex, int x, int y, boolean isEnable) {
		this.imageIndex = imgIndex;
		this.isEnable = isEnable;
		positionY = y;
		positionX = x;
		if (isEnable) {
			butn = new ImagesButton(imgIndex, 80, 89);
		} else {
			butn = new ImagesButton(imgIndex, 80, 89, 101);
		}
		butn.setPosition(x, y, 34, 63);
		if (imgIndex == 98) {
			ny = y + 29;
		} else {
			isShowNum = false;
		}
	}

	public void setPosition(int y) {
		butn.setPositionY(y, 63);
		if (imageIndex == 98) {
			ny = y + 29;
		}
	}

	public int getPosition() {
		return positionY;
	}
	
	public int getNy() {
		return ny;
	}

	public void paint(Canvas g, Paint p) {
		if (isShowNum) {
			butn.paintData(g, p);
			butn.paint(g, p);
			A.a.paintNum(g, p, Pic.imageSrcs(102), num, nx, ny);
			butn.paintData1(g, p);
		} else {
			butn.paintX(g, p);
		}

	}

}
