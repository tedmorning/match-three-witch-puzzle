package com.tuowei.canvas;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Region.Op;
import android.view.MotionEvent;

import com.gameFrame.T;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.SystemConfig;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.M;
import com.gameFrame.util.S;
import com.sdk.sms.SmsInfo;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.GameControl;
import com.tuowei.db.DB;
import com.tuowei.obj.MapButn;
import com.tuowei.obj.ShopButn;
import com.tuowei.obj.SoundButn;
import com.tuowei.tool.GameData;
import com.tuowei.tool.ShareCtrl;

public class GameMapView extends IScene {

	private MapButn[] mapButns = new MapButn[GameData.primaryPosition.length];

	private ImagesButton keyButn;

	private ShopButn shopButn;
	private SoundButn soundButn;
	private int mapLeght;

	private int[] couldImage = { 160, 161, 162 };

	private int color;

	private void init() {
		keyButn = new ImagesButton(107, 104, 101);
		keyButn.setPosition(153, 1113, 0, 0);

		shopButn = new ShopButn(370, 0);
		soundButn = new SoundButn(19, 0);
		mapLeght = Pic.imageSrcs(56).getHeight();

		int layerIndex = 0;

		for (int i = 0; i < GameData.primaryPosition.length; i++) {
			if(DB.db.getIsLevelCancelLocked() == 0){
				if (DB.db.getLayer() >= i) {
					mapButns[i] = new MapButn(GameData.primaryPosition[i][2] + 98,
							GameData.primaryPosition[i][0],
							GameData.primaryPosition[i][1], true);
				} else {
					mapButns[i] = new MapButn(GameData.primaryPosition[i][2] + 98,
							GameData.primaryPosition[i][0],
							GameData.primaryPosition[i][1], false);
				}
			}
			else{
				mapButns[i] = new MapButn(GameData.primaryPosition[i][2] + 98,
						GameData.primaryPosition[i][0],
						GameData.primaryPosition[i][1], true);
			}

			switch (GameData.primaryPosition[i][2]) {
			case 0:
				layerIndex++;
				mapButns[i].setNum(layerIndex);
				break;
			}
			mapButns[i].setLayerIndex(i + 1);
		}

		coulds = new int[10][6];
		for (int i = 0; i < coulds.length; i++) {
			coulds[i][5] = M.getRandom(couldImage.length);
			Bitmap bitmap = Pic.imageSrcs(couldImage[coulds[i][5]]);
			int wTemp = bitmap.getWidth();
			switch (M.getRandom(2)) {
			case 0:
				coulds[i][0] = M.getRandom(480, 1000);
				coulds[i][3] = M.getRandom(-5, -2);
				break;
			default:
				coulds[i][0] = M.getRandom(-250 - wTemp, -50 - wTemp);
				coulds[i][3] = M.getRandom(2, 5);
				break;
			}

			coulds[i][1] = M.getRandom(800);
			coulds[i][2] = M.getRandom(200, 255);
		}
		color = Color.rgb(58, 228, 219);

	}

	private int mapY;
	/** 0x,1y,2a,3xs,4ys,5i */
	private int[][] coulds = {};

	@Override
	public void paint(Canvas g, Paint p) {
		super.paint(g, p);
		switch (mapStatus) {
		case 0:
			paintMap(g, p);
			g.drawBitmap(bitmap, 0, 0, p);
			paintHand(g, p, imagePosition[guideIndex][0],
					imagePosition[guideIndex][1]);
			break;
		case 2:
			paintMap(g, p);
			paintStory(g, p);
			break;
		default:
			paintMap(g, p);
			break;
		}
		ShareCtrl.sc.paintTransitionUI(g, p);
	}

	private void paintStory(Canvas g, Paint p) {
		switch (pStatus) {
		case 0:
			break;
		case 1:
			g.drawBitmap(Pic.imageSrcs(104), xOff + xoffValue[xoffIndex], 470,
					p);
			if (System.currentTimeMillis() - pTimeo > 50) {
				xoffIndex++;
				if (xoffIndex >= xoffValue.length) {
					xoffIndex = 0;
					pStatus = 2;
				}
				pTimeo = System.currentTimeMillis();
			}
			break;
		default:
			g.drawBitmap(Pic.imageSrcs(104), xOff, 470, p);
			break;
		}
		g.drawBitmap(Pic.imageSrcs(105), 0, 707, p);

		switch (storyStatus) {
		case 0:
			if (System.currentTimeMillis() - storyTimeo > 50) {
				sb.append(story.charAt(tIndex));
				tIndex++;
				if (tIndex >= story.length()) {
					tIndex = 0;
					storyStatus = 1;
				}
				storyTimeo = System.currentTimeMillis();
			}
			p.setColor(color);
			p.setTextAlign(Align.LEFT);
			String text = sb.toString();
			S.s.paintString(g, p, text, 10, 720, 460);
			break;
		default:
			S.s.paintString(g, p, story, 10, 720, 460);
			break;
		}
	}

	private void paintMap(Canvas g, Paint p) {
		g.save();
		g.clipRect(0, 0, 480, 800);
		Bitmap bitMap = Pic.imageSrcs(56);
		g.drawBitmap(Pic.imageSrcs(56), 0, mapY, p);
		paintCloud(g, p);
		for (int i = 0; i < GameData.primaryPosition.length; i++) {
			if (mapButns[i].getNy() < 860 && mapButns[i].getNy() > -60) {
				mapButns[i].paint(g, p);
			}
		}
		g.drawBitmap(Pic.imageSrcs(105), 0, 0, p);
		shopButn.paint(g, p);
		soundButn.paint(g, p);

		if (mapY < -bitMap.getHeight() + 800 + 74) {
			keyButn.paintX(g, p);
		}
		g.restore();
	}

	/** xoff,yoff,alpha */
	private int[] xData = { 9, 8, 6, 3, 1 };
	private int[] yData = { 9, 8, 6, 3, 1 };

	private int[] aData = { 135, 165, 195, 225, 255 };
	/** scale */
	private float[] handlData = { 0.6f, 0.7f, 0.8f, 0.9f, 1f };

	private int index;

	private long dataTime;

	/** 0手移动到，1显示 */
	private int status;

	private void paintHand(Canvas g, Paint p, int px, int py) {
		switch (status) {
		case 0:
			g.drawBitmap(Pic.imageSrcs(625), px + xData[index], py
					+ yData[index], p);
			if (System.currentTimeMillis() - dataTime > 50) {
				index++;
				if (index >= xData.length) {
					index = 0;
					status = 1;
				}
				dataTime = System.currentTimeMillis();
			}
			break;
		case 1:
			p.setAlpha(aData[index]);
			g.save();
			g.scale(handlData[index], handlData[index], px, py);
			T.TP.paintImage(g, p, Pic.imageSrcs(631), px, py, T.ANCHOR_CHV);
			g.restore();
			p.setAlpha(255);
			g.drawBitmap(Pic.imageSrcs(625), px, py, p);

			if (System.currentTimeMillis() - dataTime > 50) {
				index++;
				if (index >= handlData.length) {
					index = 0;
					status = 0;
				}
				dataTime = System.currentTimeMillis();
			}

			break;
		}

		switch (pStatus) {
		case 0:
			g.drawBitmap(Pic.imageSrcs(104), xOff, 470, p);
			xOff += xOffSped;
			if (xOffSped < 30) {
				xOffSped += 10;
			}
			if (xOff >= -124) {
				xOff = -124;
				pStatus = 1;
				xoffIndex = 0;
			}
			break;
		case 1:
			g.drawBitmap(Pic.imageSrcs(104), xOff + xoffValue[xoffIndex], 470,
					p);
			if (System.currentTimeMillis() - pTimeo > 50) {
				xoffIndex++;
				if (xoffIndex >= xoffValue.length) {
					xoffIndex = 0;
					pStatus = 2;
				}
				pTimeo = System.currentTimeMillis();
			}
			break;
		default:
			g.drawBitmap(Pic.imageSrcs(104), xOff, 470, p);
			break;
		}
		g.drawBitmap(Pic.imageSrcs(105), 0, 707, p);
		if (cIndex > -1) {
			g.save();
			g.scale(cValue[cIndex], cValue[cIndex], 300, 750);
			T.TP.paintImage(g, p, Pic.imageSrcs(imageIndex[guideIndex]), 300,
					750, T.ANCHOR_CHV);
			g.restore();
			cIndex++;
			if (cIndex >= cValue.length) {
				cIndex = -1;
			}
		} else {
			T.TP.paintImage(g, p, Pic.imageSrcs(imageIndex[guideIndex]), 300,
					750, T.ANCHOR_CHV);
		}
	}

	/** 0人物出现，1晃动，2正常说明 */
	private int pStatus;
	private int xOff, xOffSped;

	private long pTimeo;
	private final int[] xoffValue = { 5, 8, 10, 11, 10, 8, 5 };
	private int xoffIndex;
	private int[] imageIndex = { 628, 629, 630 };
	private int[][] imagePosition = { { 164, 140 }, { 138, 335 }, { 364, 405 } };

	private void paintCloud(Canvas g, Paint p) {
		boolean isRefresh = false;
		/** 0x,1y,2a,3xs,4ys,5i */
		for (int i = 0; i < coulds.length; i++) {
			p.setAlpha(coulds[i][2]);
			Bitmap bitmap = Pic.imageSrcs(couldImage[coulds[i][5]]);
			int wTemp = bitmap.getWidth();
			g.drawBitmap(bitmap, coulds[i][0], coulds[i][1], p);
			p.setAlpha(255);
			coulds[i][0] += coulds[i][3];
			isRefresh = false;
			if (coulds[i][3] > 0) {
				if (coulds[i][0] > 480 + wTemp) {
					isRefresh = true;
				}
			} else {
				if (coulds[i][0] < -50 - wTemp) {
					isRefresh = true;
				}
			}

			if (isRefresh) {
				coulds[i][5] = M.getRandom(couldImage.length);
				bitmap = Pic.imageSrcs(couldImage[coulds[i][5]]);
				wTemp = bitmap.getWidth();
				switch (M.getRandom(2)) {
				case 0:
					coulds[i][0] = M.getRandom(480, 1000);
					coulds[i][3] = M.getRandom(-5, -2);
					break;
				default:
					coulds[i][0] = M.getRandom(-250 - wTemp, -50 - wTemp);
					coulds[i][3] = M.getRandom(2, 5);
					break;
				}

				coulds[i][1] = M.getRandom(800);
				coulds[i][2] = M.getRandom(200, 255);
			}
		}
	}

	private void updateClip() {
		bitmap.eraseColor(Color.TRANSPARENT);
		Path path = new Path();
		path.addCircle(imagePosition[guideIndex][0],
				imagePosition[guideIndex][1], 50, Path.Direction.CCW);
		canvas.save();
		canvas.clipPath(path, Op.DIFFERENCE);
		ShareCtrl.sc.paintB(canvas, SystemConfig.systemP, 200);
		canvas.restore();
		cIndex = 0;

		switch (pStatus) {
		case 2:
			pStatus = 1;
			xoffIndex = 0;
			break;
		}
	}

	private int cIndex;
	private final float[] cValue = { 0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f,
			0.4f, 0.5f, 0.6f, 0.7f, 0.9f, 1.1f, 1, 0.95f, 1, 1.05f, 1 };

	private int teType = -1;
	private float lastY;

	@Override
	public void keyAction(TouchEvent te) {
		super.keyAction(te);
		switch (mapStatus) {
		case 0:
			switch (te.getEventTye()) {
			case MotionEvent.ACTION_UP:
				if (ShareCtrl.sc.transitionBool) {
					return;
				}
				guideIndex++;
				if (guideIndex < imageIndex.length) {
					updateClip();
				} else {
					pStatus = 1;
					xoffIndex = 0;
					mapStatus = 2;
				}
				ShareCtrl.sc.playTransitionUI();
				break;
			}
			return;
		case 2:
			switch (storyStatus) {
			case 0:
				storyStatus = 1;
				break;
			case 1:
				mapStatus = 1;
				break;
			}
			break;
		}
		shopButn.keyAction(te);
		soundButn.keyAction(te);

		if (teType == -1) {
			switch (te.getEventTye()) {
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:
				teType = te.touchIndex;
				lastY = te.point.y;
				break;
			}
		} else {
			if (teType == te.touchIndex) {
				switch (te.getEventTye()) {
				case MotionEvent.ACTION_MOVE:
					mapY = mapY + (int) (te.point.y - lastY);
					if (mapY > 0) {
						mapY = 0;
					} else {
						int upL = 800 - mapLeght;
						if (mapY < upL)
							mapY = upL;
					}
					lastY = te.point.y;

					for (int i = 0; i < GameData.primaryPosition.length; i++) {
						mapButns[i].setPosition(mapButns[i].getPosition()
								+ mapY);
					}

					keyButn.setPositionY(1113 + mapY, 0);

					break;
				case MotionEvent.ACTION_UP:
					teType = -1;
					break;
				}
			}
		}

		for (int i = 0; i < GameData.primaryPosition.length; i++) {
			if (mapButns[i].isEnable) {
				switch (mapButns[i].keyAction(te)) {
				case 3:
					if(DB.db.getIsRegister() == 0 && GameControl.gameLayer == GameControl.REG_Layer){
						SmsInfo.sendSms(SmsInfo.SMS_REG);
						return;
					}
					GameMainActivity.bffa.changeView(3);
					GameControl.gameLayer = i;
					return;
				}
			} else {
				switch (mapButns[i].keyAction(te)) {
				case 3:
					if(DB.db.getIsRegister() == 1 && i > GameControl.REG_Layer){
						SmsInfo.sendSms(SmsInfo.SMS_UNLOCK);
					}
					return;
				}
		
			}
		}

		switch (keyButn.keyAction(te)) {
		case 3:
			GameMainActivity.bffa.changeView(3);
			GameControl.game_mode = GameControl.GAME_MODE_0;
			GameControl.gameLayer = GameData.primaryPosition.length - 1;
			return;
		}

	}

	@Override
	public void loadingData() {
		super.loadingData();
		loadingImage();
		init();
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	private Bitmap bitmap;
	private Canvas canvas;
	private int guideIndex;

	/** 0向导中，1正常,2游戏情节中 */
	private int mapStatus;

	private final String story = "Bingo逐步开始他的旅程";
	private StringBuilder sb = new StringBuilder();
	/** 0内容出现过程，1内容显示， */
	private int storyStatus;
	private long storyTimeo;
	private int tIndex;

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		int[] imageNumsPNG = new int[] { 44, 45, 51, 56, 57, 98, 99, 100, 101,
				102, 103, 104, 105, 107, 160, 161, 162, 625, 628, 629, 630, 631 };

		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(Integer.valueOf(intTemp));
		}
		// Pic.loadImage(3, new int[] { 104, 625, 628, 629, 630, 631 });
		guideIndex = -1;
		mapStatus = 1;
		if (!DB.db.getIsold()) {
			bitmap = Bitmap.createBitmap(480, 800, Config.ARGB_4444);
			canvas = new Canvas(bitmap);
			guideIndex = 0;
			updateClip();
			Pic.loadLazyImage(104);
			Pic.loadLazyImage(625);
			Pic.loadLazyImage(628);
			Pic.loadLazyImage(629);
			Pic.loadLazyImage(630);
			Pic.loadLazyImage(631);
			mapStatus = 0;
			xOff = -124 - 100;
			imageAsPNG.add(104);
			imageAsPNG.add(625);
			imageAsPNG.add(628);
			imageAsPNG.add(629);
			imageAsPNG.add(630);
			imageAsPNG.add(631);
		}
		Pic.loadImage(imageAsPNG);
	}

	@Override
	public void onBackPressed() {
		GameMainActivity.bffa.changeView(0);
	}

}
