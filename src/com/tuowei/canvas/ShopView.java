package com.tuowei.canvas;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gameFrame.controller.GameDirector;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.SystemConfig;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;
import com.gameFrame.util.M;
import com.sdk.sms.SmsInfo;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.GameControl;
import com.tuowei.control.GameControl.MenuTop;
import com.tuowei.db.DB;
import com.tuowei.tool.ShareCtrl;

public class ShopView extends IScene {
	/**
	 * 载入资源
	 */
	public void loadingData() {
		loadingImage();
		initData();
	}

	private void initData() {
		initButns();
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();
	/** 0back,1,2,3金,4确定,5取消 */
	private ImagesButton[] butns = new ImagesButton[6];

	private void initButns() {
		butns[0] = new ImagesButton(52, 67, 67);// back
		butns[0].setPosition(14, 14, 0, 0);

		butns[1] = new ImagesButton(81, 102, 56, 84, 3);// 1
		butns[1].setPosition(317, 430, 58, 14);

		butns[2] = new ImagesButton(81, 102, 56, 84, 5);// 2
		butns[2].setPosition(317, 535, 58, 14);

		butns[3] = new ImagesButton(81, 102, 56, 84, 6);// 3
		butns[3].setPosition(317, 626, 58, 14);
		//butns[3].setPosition(317, 626, 47, 14);

		butns[4] = new ImagesButton(87, 91, 50);// sure
		butns[4].setPosition(98, 447, 0, 0);

		butns[5] = new ImagesButton(86, 91, 50);// cancel
		butns[5].setPosition(267, 447, 0, 0);
		gameCurmoney = DB.db.getMoney();
	}

	private int selectIndex = -1;

	private int[][] backPosition = { { 53, 163, 180, 217 },
			{ 255, 163, 380, 213 }, { 53, 280, 180, 333 },
			{ 255, 280, 380, 333 }, { 59, 416 }, { 59, 514 }, { 59, 611 } };

	public static int gameCurmoney;
	private int[] prices = { 100, 200, 300, 400 };

	private int[] topTip = { 82, 159 };
	private int topTipIndex;

	@Override
	public void paint(Canvas g, Paint p) {
		switch (GameControl.menuTop) {
		case TOP_BAD:
			g.drawBitmap(ShareCtrl.sc.gameBuffer, 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(97), 107, 260, p);
			switch (topTipIndex) {
			case 0:
				g.drawBitmap(Pic.imageSrcs(topTip[topTipIndex]), 155, 346, p);
				break;
			default:
				g.drawBitmap(Pic.imageSrcs(topTip[topTipIndex]), 144, 362, p);
				break;
			}
			butns[4].paintX(g, p);
			break;
		case TOP_NONE:
			g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(68), 12, 27, p);
			g.drawBitmap(Pic.imageSrcs(70), 104, 0, p);
			g.drawBitmap(Pic.imageSrcs(69), 35, 373, p);
			for (int i = 0; i < 4; i++) {
				if (i == selectIndex) {
					A.a.paintFrame(g, p, Pic.imageSrcs(71), backPosition[i][0],
							backPosition[i][1], 1, 2);
				} else {
					A.a.paintFrame(g, p, Pic.imageSrcs(71), backPosition[i][0],
							backPosition[i][1], 0, 2);
				}

				A.a.paintNum(g, p, Pic.imageSrcs(80), prices[i],
						backPosition[i][2], backPosition[i][3]);
			}

			for (int i = 4; i < backPosition.length; i++) {
				if (i == selectIndex) {
					A.a.paintFrame(g, p, Pic.imageSrcs(72), backPosition[i][0],
							backPosition[i][1], 1, 2);
				} else {
					A.a.paintFrame(g, p, Pic.imageSrcs(72), backPosition[i][0],
							backPosition[i][1], 0, 2);
				}
			}

			g.drawBitmap(Pic.imageSrcs(76), 78, 167, p);
			g.drawBitmap(Pic.imageSrcs(77), 272, 169, p);
			g.drawBitmap(Pic.imageSrcs(75), 55, 290, p);
			g.drawBitmap(Pic.imageSrcs(78), 273, 291, p);

			A.a.paintFrame(g, p, Pic.imageSrcs(79), 157, 165, 1, 4);
			A.a.paintFrame(g, p, Pic.imageSrcs(79), 360, 165, 2, 4);
			A.a.paintFrame(g, p, Pic.imageSrcs(79), 157, 285, 0, 4);
			A.a.paintFrame(g, p, Pic.imageSrcs(79), 360, 284, 3, 4);

			g.drawBitmap(Pic.imageSrcs(93), 59, 415, p);
			g.drawBitmap(Pic.imageSrcs(94), 74, 509, p);
			g.drawBitmap(Pic.imageSrcs(95), 70, 615, p);

			A.a.paintFrame(g, p, Pic.imageSrcs(96), 171, 432, 0, 3);
			A.a.paintFrame(g, p, Pic.imageSrcs(96), 171, 530, 1, 3);
			A.a.paintFrame(g, p, Pic.imageSrcs(96), 171, 627, 2, 3);

			for (int i = 0; i < 4; i++) {
				butns[i].paintX(g, p);
			}

			g.drawBitmap(Pic.imageSrcs(133), 99, 579, p);
			g.drawBitmap(Pic.imageSrcs(73), 149, 754, p);
			A.a.paintNum(g, p, Pic.imageSrcs(84), gameCurmoney, 203, 767);
			break;
		case TOP_SURE:
			g.drawBitmap(ShareCtrl.sc.gameBuffer, 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(85), 49, 282, p);
			if (selectIndex < 4) {
				Bitmap bitmap = Pic.imageSrcs(tipIndex[selectIndex]);
				g.drawBitmap(bitmap, 240 - bitmap.getWidth() / 2,
						702 - bitmap.getHeight() / 2, p);
				g.drawBitmap(Pic.imageSrcs(88), 88, 320, p);
			} else {
				g.drawBitmap(Pic.imageSrcs(88), 88, 320, p);
			}

			butns[4].paintX(g, p);
			butns[5].paintX(g, p);
			break;
		default:
			break;
		}

		ShareCtrl.sc.paintTransitionUI(g, p);
	}

	private int[] tipIndex = { 91, 90, 92, 89 };

	private int[][] butnCollon = { { 48, 159, 189, 98 },
			{ 244, 157, 197, 100 }, { 46, 270, 195, 111 },
			{ 249, 269, 195, 124 } };

	@Override
	public void keyAction(TouchEvent te) {
		switch (GameControl.menuTop) {
		case TOP_BAD:
			switch (butns[4].keyAction(te)) {
			case 3:
				setMenuStatus(MenuTop.TOP_NONE);
				break;
			}
			break;
		case TOP_NONE:
			switch (butns[0].keyAction(te)) {
			case 3:
				GameMainActivity.bffa.changeView(GameControl.lastShowView);
				break;
			}

			for (int i = 1; i < 4; i++) {
				switch (butns[i].keyAction(te)) {
				case 3:
					//selectIndex = i + 3;
					//setMenuStatus(MenuTop.TOP_SURE);
					SmsInfo.sendSms(i+1);
					break;
				}
			}

			if (te.getEventTye() == MotionEvent.ACTION_UP) {
				for (int i = 0; i < 4; i++) {
					if (M.m.isInRect(te.point.x, te.point.y, butnCollon[i][0],
							butnCollon[i][1], butnCollon[i][2],
							butnCollon[i][3])) {
						selectIndex = i;
						setMenuStatus(MenuTop.TOP_SURE);
						return;
					}
				}
			}
			break;
		case TOP_SURE:
			switch (butns[4].keyAction(te)) {
			case 3:
				if (selectIndex < 4) {
					if (gameCurmoney < prices[selectIndex]) {
						topTipIndex = 0;
						setMenuStatus(MenuTop.TOP_BAD);
					} else {
						gameCurmoney -= prices[selectIndex];
						DB.db.setMoney(gameCurmoney);
						int[] prop = DB.db.getNumbss();
						prop[selectIndex]++;
						DB.db.setNumbss(prop);
						DB.db.saveDB();
						topTipIndex = 1;
						setMenuStatus(MenuTop.TOP_BAD);
					}
				} else {// 嵌入收费点
					/*gameMenoy += butns[selectIndex - 3].getNum() * 50;
					DB.db.setMoney(gameMenoy);
					DB.db.saveDB();
					topTipIndex = 1;
					setMenuStatus(MenuTop.TOP_BAD);*/
				}
				break;
			}
			switch (butns[5].keyAction(te)) {
			case 3:
				setMenuStatus(MenuTop.TOP_NONE);
				break;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 释放资源
	 */
	public void disingData() {
		Pic.disImage(imageAsPNG);
	}

	private void setMenuStatus(MenuTop menuStatus) {
		GameControl.menuTop = menuStatus;

		switch (menuStatus) {
		case TOP_BAD:
			butns[4].setPosition(325, 418, 0, 0);
			break;
		case TOP_NONE:
			break;
		case TOP_SURE:
			butns[4].setPosition(98, 447, 0, 0);
			ShareCtrl.sc.buffCanvas.drawBitmap(GameDirector.systemBitmap, 0, 0,
					SystemConfig.systemP);
			ShareCtrl.sc.paintB(ShareCtrl.sc.buffCanvas, SystemConfig.systemP,
					200);

			break;
		default:
			break;
		}

		ShareCtrl.sc.playTransitionUI();
	}

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		int[] imageNumsPNG = new int[] { 52, 54, 68, 69, 70, 71, 72, 73, 75,
				76, 77, 78, 79, 80, 81, 82, 84, 85, 86, 87, 88, 89, 90, 91, 92,
				93, 94, 95, 96, 97, 133, 159 };

		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(Integer.valueOf(intTemp));
		}
		// Pic.loadImage(2);
		Pic.loadImage(imageAsPNG);
	}

	@Override
	public void onBackPressed() {
		switch (GameControl.menuTop) {
		case TOP_BAD:
			setMenuStatus(MenuTop.TOP_NONE);
			break;
		case TOP_NONE:
			GameMainActivity.bffa.changeView(GameControl.lastShowView);
			break;
		case TOP_SURE:
			setMenuStatus(MenuTop.TOP_NONE);
			break;
		default:
			break;
		}
	}

}
