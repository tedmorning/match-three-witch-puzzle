package com.tuowei.tool;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

import com.gameFrame.controller.GameDirector;
import com.tuowei.control.PS;

/**
 * 共用的方法
 * 
 * @author Joniy
 * 
 */
public class ShareCtrl {

	/** 统一填写类名 */
	public final String LOGKEY = "ShareCtrl";

	public static ShareCtrl sc = new ShareCtrl();

	public ShareCtrl() {
		sc = this;
	}

	public Bitmap gameBuffer;
	public Canvas buffCanvas;

	public void initGameBuffer() {
		if (gameBuffer == null) {
			gameBuffer = Bitmap.createBitmap(PS.screenw, PS.screenh,
					Config.RGB_565);
			buffCanvas = new Canvas(gameBuffer);
		}
	}

	/** 用于界面过度的缓存图片 */
	public static Bitmap buffer;
	public boolean transitionBool = false;
	private int alphaSped = -35;
	private int transitionAlpha;

	/**
	 * 过渡界面UI
	 * 
	 * @param g
	 */
	public void paintTransitionUI(Canvas g, Paint p) {
		if (transitionBool) {
			transitionAlpha += alphaSped;
			if (alphaSped < 0) {
				if (transitionAlpha < 0) {
					transitionAlpha = 0;
					transitionBool = false;
				}
			} else {
				if (transitionAlpha > 220) {
					transitionAlpha = 255;
					alphaSped = -alphaSped;
				}
			}

			if (alphaSped > 0) {
				g.drawBitmap(buffer, 0, 0, p);
			}
			paintB(g, p, transitionAlpha);
		}
	}

	/***
	 * 绘画半透明黑色背景
	 * 
	 * @param g
	 * @param p
	 */
	public void paintB(Canvas g, Paint p, int bwNum) {
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL);
		p.setAlpha(bwNum);
		g.drawRect(new Rect(0, 0, PS.screenw, PS.screenh), p);
		p.setAlpha(255);
	}

	public void paintRect(Canvas g, Paint p, int x, int y, int w, int h) {
		// p.setColor(Color.GREEN);
		p.setARGB(150, 0, 0, 0);
		// p.setAlpha(150);
		p.setStyle(Style.FILL);
		RectF rect = new RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
		g.drawRoundRect(rect, 10, 10, p);
		// p.setColor(Color.BLUE);
		p.setARGB(200, 110, 199, 206);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(5);
		RectF rect1 = new RectF(x - w / 2 - 2, y - h / 2 - 2, x + w / 2 + 4, y
				+ h / 2 + 4);
		g.drawRoundRect(rect1, 10, 10, p);
		p.setStrokeWidth(1);
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL);
		p.setAlpha(255);

	}

	public void paintOther(Canvas g, Paint p, int x, int y, int w, int h,
			int type) {
		// p.setColor(Color.GREEN);
		// p.setAlpha(150);
		p.setARGB(150, 0, 0, 0);
		p.setStyle(Style.FILL);
		RectF rect = new RectF(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
		Path path = new Path();
		switch (type) {
		case 0:// 右上
			path.moveTo(x - w / 2, y - h / 2);
			path.lineTo(x + w / 2 - w / 10, y - h / 2);
			path.lineTo(x + w / 2 - w / 30, y - h / 2 - h / 10);
			path.lineTo(x + w / 2 - w / 20, y - h / 2);
			path.lineTo(x + w / 2, y - h / 2);
			path.lineTo(x + w / 2, y + h / 2);
			path.lineTo(x - w / 2, y + h / 2);
			break;
		case 1:// 右下
			path.moveTo(x - w / 2, y - h / 2);
			path.lineTo(x + w / 2, y - h / 2);
			path.lineTo(x + w / 2, y + h / 2);
			path.lineTo(x + w / 2 - w / 20, y + h / 2);
			path.lineTo(x + w / 2 - w / 30, y + h / 2 + h / 10);
			path.lineTo(x + w / 2 - w / 10, y + h / 2);
			path.lineTo(x - w / 2, y + h / 2);
			break;
		case 2:// 左下
			path.moveTo(x - w / 2, y - h / 2);
			path.lineTo(x + w / 2, y - h / 2);
			path.lineTo(x + w / 2, y + h / 2);
			path.lineTo(x - w / 2 + w / 10, y + h / 2);
			path.lineTo(x - w / 2 + w / 30, y + h / 2 + h / 10);
			path.lineTo(x - w / 2 + w / 20, y + h / 2);
			path.lineTo(x - w / 2, y + h / 2);
			break;
		case 3:// 左上
			path.moveTo(x - w / 2, y - h / 2);
			path.lineTo(x - w / 2 + w / 20, y - h / 2);
			path.lineTo(x - w / 2 + w / 30, y - h / 2 - h / 10);
			path.lineTo(x - w / 2 + w / 10, y - h / 2);
			path.lineTo(x + w / 2, y - h / 2);
			path.lineTo(x + w / 2, y + h / 2);
			path.lineTo(x - w / 2, y + h / 2);
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		}
		path.close();
		g.drawPath(path, p);
		// g.drawRoundRect(rect, 10, 10, p);
		// p.setColor(Color.BLUE);
		p.setARGB(150, 110, 199, 206);
		p.setStyle(Style.STROKE);
		p.setStrokeWidth(5);
		RectF rect1 = new RectF(x - w / 2 - 2, y - h / 2 - 2, x + w / 2 + 4, y
				+ h / 2 + 4);
		// g.drawRoundRect(rect1, 10, 10, p);
		g.drawPath(path, p);
		p.setStrokeWidth(1);
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL);
		p.setAlpha(255);
		path = null;

	}

	public void cancelTransition() {
		transitionBool = false;
	}

	Canvas gTransitionUI;

	/**
	 * 播放过渡UI效果
	 */
	public void playTransitionUI() {
		// 初始化
		if (buffer == null) {
			buffer = Bitmap
					.createBitmap(PS.screenw, PS.screenh, Config.RGB_565);
			gTransitionUI = new Canvas(buffer);
		}
		gTransitionUI.drawBitmap(GameDirector.systemBitmap, 0, 0, null);

		//
		transitionBool = true;
		alphaSped = 35;
		transitionAlpha = 20;
	}

	/** 更加两点获得滑动的方向与竖直方向的夹角 */
	public int getAngleByTowPoint(Point sp, Point ep) {
		int angle = 0;
		double tempX = sp.x;
		double tempY = sp.y;

		double tempX1 = ep.x;
		double tempY1 = ep.y;

		if (tempX == tempX1 && tempY == tempY1) {
			return -1;
		}
		double a = Math.abs(Math.abs(tempX1) - Math.abs(tempX));
		double b = Math.abs(Math.abs(tempY1) - Math.abs(tempY));
		double c = Math.sqrt(a * a + b * b);
		angle = Math.abs((int) Math.toDegrees(Math.asin(a / c)));
		if (tempX1 > tempX) {
			if (tempY1 > tempY) {
				angle = 180 - angle;
			}
		} else if (tempX1 < tempX) {
			if (tempY1 >= tempY) {
				angle += 180;
			} else {
				angle = 360 - angle;
			}
		}
		if (tempX1 == tempX) {
			if (tempY1 > tempY) {
				angle += 180;
			}
		}
		return angle;
	}
}
