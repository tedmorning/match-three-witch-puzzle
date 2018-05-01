package com.tuowei.canvas;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gameFrame.T;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.SystemConfig;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;
import com.gameFrame.util.S;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.GameControl;
import com.tuowei.control.GameControl.MenuStatus;
import com.tuowei.db.DB;
import com.tuowei.obj.MenuSetButn;
import com.tuowei.obj.ShopButn;
import com.tuowei.obj.spx.DeadObj;
import com.tuowei.tool.GameData;
import com.tuowei.tool.ShareCtrl;

/**
 * 游戏主菜单界面
 * 
 * @author Joniy
 */
public class GameMenuView extends IScene {
	public void keyAction(TouchEvent te) {
		switch (GameControl.menu_status) {
		case TOP_STARY:
			switch (te.getEventTye()) {
			case MotionEvent.ACTION_UP:
				switch (storyStatus) {
				case 0:
					storyStatus = 1;
					break;
				case 1:
					setMenuStatus(MenuStatus.MENU_NONE);
					break;
				}
				break;
			default:
				break;
			}
			break;
		case MENU_ABOUT:
			switch (butns[0].keyAction(te)) {
			case 3:
				setMenuStatus(MenuStatus.MENU_NONE);
				return;
			}
			break;
		case MENU_HELP:
			if (pageIndex > 0) {
				switch (butns[3].keyAction(te)) {
				case 3:
					pageHelp(-1);
					return;
				}
			}

			if (pageIndex < 2) {
				switch (butns[6].keyAction(te)) {
				case 3:
					pageHelp(1);
					return;
				}
			}
			switch (butns[0].keyAction(te)) {
			case 3:
				setMenuStatus(MenuStatus.MENU_NONE);
				return;
			}
			break;
		case MENU_MORE:
		case MENU_NONE:
		case MENU_SET:
			boolean isB = true;
			switch (butns[7].keyAction(te)) {
			case 3:
				GameMainActivity.bffa.finish();
				isB = false;
				break;
			}
			switch (butns[1].keyAction(te)) {
			case 3:// 设置
				initSet();
				isB = false;
				break;
			}

			if (shopButn.keyAction(te)) {
				isB = false;
			}

			switch (butns[2].keyAction(te)) {
			case 3:
				setMenuStatus(MenuStatus.MENU_TOP);
				initCJButn();
				isB = false;
				break;
			}

			if (isShowSet) {
				if (musicButn.keyAction(te)) {
					isB = false;
				}
				switch (butns[4].keyAction(te)) {
				case 3:
					setMenuStatus(MenuStatus.MENU_ABOUT);
					isB = false;
					break;
				}
				switch (butns[5].keyAction(te)) {
				case 3:
					setMenuStatus(MenuStatus.MENU_HELP);
					isB = false;
					break;
				}
			}

			if (isB) {
				switch (te.getEventTye()) {
				case MotionEvent.ACTION_UP:
					if (te.point.y > 100 && te.point.y < 700) {
						GameMainActivity.bffa.changeView(2);
					}
					break;
				}
			}
			break;
		case MENU_TOP:
			for (int i = 0; i < 4; i++) {
				switch (pStatus[i]) {
				case 0:
				case 2:
					break;
				case 1:
					switch (butns1[i].keyAction(te)) {
					case 3:
						switch (aStatus[i]) {// 领取动画
						case -1:
							aStatus[i] = 0;
							aTimeo[i] = System.currentTimeMillis();
							break;
						}
						break;
					}
					break;
				}
			}
			switch (butns[0].keyAction(te)) {
			case 3:
				setMenuStatus(MenuStatus.MENU_NONE);
				return;
			}

			break;
		default:
			break;
		}

	}

	/** 0back,1设置,2排行榜,4about,5help,3help left,6help right，7退出 */
	private ImagesButton[] butns = new ImagesButton[8];

	/** 成就领取 */
	private ImagesButton[] butns1 = new ImagesButton[6];

	private void initCJButn() {
		for (int i = 0; i < 4; i++) {
			if (butns1[i] == null) {
				butns1[i] = new ImagesButton(62, 96, 99);
				butns1[i].setPosition(pPoint[i][3], pPoint[i][4], 0, 0);
			}
			switch (pStatus[i]) {
			case 0:
				break;
			case 1:
				switch (aStatus[i]) {// 领取动画
				case -1:
					butns1[i].setImgIndex(61);
					break;
				default:
					butns1[i].setImgIndex(60);
					break;
				}
				break;
			case 2:
				butns1[i].setImgIndex(60);
				break;
			}
		}
	}

	private ShopButn shopButn;
	private MenuSetButn musicButn;

	private boolean isShowSet, showing;
	private int setXoff, xoffSped;
	private int[] setOffValue = { -10, -15, -18, -15, -10, 0, 8, 12, 15, 12, 8,
			0, -8, -12, -15, -12, -8, 0, 5, 8, 10, 8, 5, 0, -3, -5, -3, 0, 2,
			3, 2 };
	private int setOffIndex;
	private int offStatus;
	private int butnIndex;
	private boolean isButn;
	private long butnTimeo;

	private void initSet() {
		if (showing) {
			return;
		}
		showing = true;
		isShowSet = !isShowSet;

		setOffIndex = 0;
		if (isShowSet) {// 将要显示
			setXoff = -113;
		} else {// 将要隐藏
			setXoff = 38;
		}
		xoffSped = 10;
		offStatus = 0;
		butns[4].setStatus(2);
		butnIndex = 0;
		isButn = true;
	}

	private void paintSet(Canvas g, Paint p) {
		if (showing) {
			g.save();
			g.clipRect(38, 724, 300, 795);
			if (isShowSet) {
				switch (offStatus) {
				case 0:
					g.drawBitmap(Pic.imageSrcs(632), setXoff, 724, p);
					setXoff += xoffSped;
					if (setXoff >= 38) {
						setXoff = 38;
						offStatus = 1;
					} else {
						if (xoffSped < 30) {
							xoffSped += 5;
						}
					}
					break;
				case 1:
					g.drawBitmap(Pic.imageSrcs(632), setXoff
							+ setOffValue[setOffIndex], 724, p);

					setOffIndex++;
					if (setOffIndex >= setOffValue.length) {
						setOffIndex = 0;
						showing = false;
					}
					break;
				}
			} else {
				g.drawBitmap(Pic.imageSrcs(632), setXoff, 724, p);
				setXoff -= xoffSped;
				if (setXoff <= -113) {
					setXoff = -113;
					showing = false;
				}
			}
			g.restore();
		} else {
			if (isShowSet) {
				g.drawBitmap(Pic.imageSrcs(632), 38, 724, p);
			}
		}

		butns[1].paintX(g, p);

		if (isShowSet) {
			if (isButn) {
				switch (butnIndex) {
				case 1:
					butns[4].paintX(g, p);
					break;
				case 2:
					butns[4].paintX(g, p);
					butns[5].paintX(g, p);
					break;
				}

				if (System.currentTimeMillis() - butnTimeo > 100) {
					switch (butnIndex) {
					case 1:
						butns[5].setStatus(2);
						break;
					case 2:
						musicButn.setStatus(2);
						break;
					}
					butnIndex++;
					if (butnIndex >= 3) {
						isButn = false;
					}
					butnTimeo = System.currentTimeMillis();
				}

			} else {
				butns[4].paintX(g, p);
				butns[5].paintX(g, p);
				musicButn.paintX(g, p);
			}

		}
	}

	private void initButns() {
		butns[0] = new ImagesButton(52, 67, 67);// back
		butns[0].setPosition(14, 14, 0, 0);

		butns[3] = new ImagesButton(613, 67, 67);// left
		butns[3].setPosition(0, 370, 0, 0);
		butns[6] = new ImagesButton(612, 67, 67);// right
		butns[6].setPosition(414, 370, 0, 0);

		butns[1] = new ImagesButton(57, 90, 84, 47);// 设置
		butns[1].setPosition(11, 715, 18, 16);

		butns[7] = new ImagesButton(57, 90, 84, 46);// 退出
		butns[7].setPosition(377, 715, 22, 14);

		butns[2] = new ImagesButton(57, 90, 84, 51, 3, 4);// 排行榜
		butns[2].setImageType(5);
		butns[2].setPosition(11, 11, 22, 16);

		butns[4] = new ImagesButton(51, 70, 84, 0, 0, 4);// about
		butns[4].setPosition(95, 736, 0, 0);

		butns[5] = new ImagesButton(51, 70, 84, 0, 1, 4);// help
		butns[5].setPosition(150, 735, 0, 0);

		musicButn = new MenuSetButn();

		shopButn = new ShopButn(384, 7);
		pStatus = DB.db.getTitle();
	}

	// private final String about =
	// " Once upon a time, there is a pudding called Bingo and he wants to be a number ONE hero."
	// +
	// " He starts his journey from level 1 and he collects the same color and shape puddings to launch a strike to his enemies."
	// + " The more puddings he collects, the stronger punches he gives."
	// + "Give me advice to service@ec-base.net";
	private final String about = "游戏名称：布丁萝莉消消消 版本号：1.0";

	// private final String about1 = "版本号：1.0";

	public void paint(Canvas g, Paint p) {
		switch (GameControl.menu_status) {
		case MENU_ABOUT:
			g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(55), 36, 105, p);
			g.drawBitmap(Pic.imageSrcs(50), 173, 45, p);
			p.setColor(color);
			p.setTextAlign(Align.LEFT);
			paintString(g, p, about, 51, 120, 377);
			// S.s.paintString(g, p, about1, 51, 160, 377);
			butns[0].paintX(g, p);
			break;
		case MENU_HELP:
			g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(55), 36, 105, p);
			g.drawBitmap(Pic.imageSrcs(49), 173, 45, p);
			butns[0].paintX(g, p);

			paintHelp(g, p);

			if (pageIndex < 2) {
				butns[6].paintX(g, p);
			}
			if (pageIndex > 0) {
				butns[3].paintX(g, p);
			}
			break;
		case MENU_MORE:
		case MENU_NONE:
		case MENU_SET:
			g.drawBitmap(Pic.imageSrcs(41), 0, 0, p);
			g.drawBitmap(Pic.imageSrcs(43), 28, 57, p);
			g.drawBitmap(Pic.imageSrcs(42), 72, 584, p);
			shopButn.paint(g, p);
			paintSet(g, p);
			butns[2].paintX(g, p);
			butns[7].paintX(g, p);
			break;
		case MENU_TOP:// 成就
			paintAchievement(g, p);
			break;
		case TOP_STARY:
			paintStory(g, p);
			break;
		case TOP_MAP:
			break;
		}

		ShareCtrl.sc.paintTransitionUI(g, p);
	}

	private final String story = "Once upon a time, a pudding called Bingo wants to be a number ONE hero…";
	private StringBuilder sb = new StringBuilder();
	/** 0内容出现过程，1内容显示， */
	private int storyStatus;
	private long storyTimeo;
	private int tIndex;

	private int color;

	private void paintStory(Canvas g, Paint p) {
		g.drawBitmap(Pic.imageSrcs(41), 0, 0, p);
		g.drawBitmap(Pic.imageSrcs(43), 28, 57, p);
		g.drawBitmap(Pic.imageSrcs(42), 72, 584, p);
		shopButn.paint(g, p);
		paintSet(g, p);
		butns[2].paintX(g, p);
		butns[7].paintX(g, p);
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

	private void paintAchievement(Canvas g, Paint p) {
		g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);
		g.drawBitmap(Pic.imageSrcs(59), 10, 22, p);
		butns[0].paintX(g, p);

		for (int i = 0; i < 4; i++) {
			switch (pStatus[i]) {
			case 0:
				A.a.paintFrame(g, p, Pic.imageSrcs(pPoint[i][0]), pPoint[i][1],
						pPoint[i][2], 1, 3);
				break;
			case 1:
				A.a.paintFrame(g, p, Pic.imageSrcs(pPoint[i][0]), pPoint[i][1],
						pPoint[i][2], 0, 3);
				switch (aStatus[i]) {// 领取动画
				case -1:
					break;
				default:
					int lx = pPoint1[i][0] - 72;
					int ly = pPoint1[i][1] - 70;

					g.save();
					g.clipRect(lx, ly, pPoint1[i][0] + 72, pPoint1[i][1] + 70);
					g.scale(wprop[aStatus[i]], wprop[aStatus[i]],
							pPoint1[i][0], pPoint1[i][1]);

					g.drawBitmap(Pic.imageSrcs(44), lx, ly, p);
					g.restore();
					p.setAlpha(255);

					if (System.currentTimeMillis() - aTimeo[i] > 100) {
						aStatus[i]++;
						if (aStatus[i] >= wprop.length) {
							aStatus[i] = -1;
						} else {
							if (wprop[aStatus[i]] >= 1) {
								pStatus[i] = 2;
								DB.db.setTitle(pStatus);
								DB.db.saveDB();
							}
						}

						aTimeo[i] = System.currentTimeMillis();
					}
					break;
				}
				break;
			case 2:
				A.a.paintFrame(g, p, Pic.imageSrcs(pPoint[i][0]), pPoint[i][1],
						pPoint[i][2], 2, 3);
				switch (aStatus[i]) {// 领取动画
				case -1:
					break;
				default:
					int lx = pPoint1[i][0] - 72;
					int ly = pPoint1[i][1] - 70;
					g.save();
					g.scale(wprop[aStatus[i]], wprop[aStatus[i]],
							pPoint1[i][0], pPoint1[i][1]);

					g.drawBitmap(Pic.imageSrcs(44), lx, ly, p);
					g.restore();
					if (System.currentTimeMillis() - aTimeo[i] > 100) {
						aStatus[i]++;
						if (aStatus[i] >= wprop.length) {
							aStatus[i] = -1;
						}
						aTimeo[i] = System.currentTimeMillis();
					}
					break;
				}
				break;
			}

			switch (pStatus[i]) {
			case 0:
				break;
			case 1:
				switch (aStatus[i]) {// 领取动画
				case -1:
					butns1[i].setImgIndex(61);
					break;
				default:
					butns1[i].setImgIndex(60);
					break;
				}
				break;
			case 2:
				butns1[i].setImgIndex(60);
				break;
			}

			butns1[i].paintX(g, p);
		}
	}

	/** 0未获得，1可领取，2已获得 */
	private int[] pStatus = { 0, 0, 0, 0 };

	/** wprop的Index */
	private int[] aStatus = { -1, -1, -1, -1 };

	/** wprop的Time */
	private long[] aTimeo = { -1, -1, -1, -1 };

	/** 缩放值 */
	private float[] wprop = { 0.5f, 0.52f, 0.56f, 0.61f, 0.67f, 0.74f, 0.82f,
			0.91f, 1f, 1.1f, 1, 0.95f, 0.9f, 0.95f, 1, 1.3f, 1f };

	/** [0] 图标的index，[1][2]图标的位置，[3][4]标记的位置 */
	private int[][] pPoint = { { 63, 50, 124, 350, 141 },
			{ 64, 35, 275, 350, 275 }, { 65, 55, 410, 350, 425 },
			{ 66, 59, 550, 350, 570 }, };
	/** 光耀的位置 */
	private int[][] pPoint1 = { { 57 + 41, 144 + 56 }, { 42 + 49, 312 + 50 },
			{ 91, 485 }, { 91, 630 }, };

	/**
	 * 载入资源
	 */
	public void loadingData() {
		loadingImage();
		initData();
	}

	private void initData() {
		initButns();
		color = Color.rgb(58, 228, 219);
		setMenuStatus(MenuStatus.MENU_NONE);
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * 释放资源
	 */
	public void disingData() {
		Pic.disImage(imageAsPNG);
	}

	private void setMenuStatus(MenuStatus menuStatus) {
		GameControl.menu_status = menuStatus;

		switch (menuStatus) {
		case MENU_ABOUT:
			color = Color.rgb(35, 135, 115);
			break;
		case MENU_HELP:
			pageIndex = 0;
			isMove = false;
			deadObj.clear();
			break;
		case MENU_MORE:
			break;
		case MENU_NONE:
			deadObj.clear();
			break;
		case MENU_SET:
			break;
		case MENU_TOP:
			break;
		case TOP_MAP:
			break;
		default:
			break;

		}

		ShareCtrl.sc.playTransitionUI();
	}

	private int pageIndex;
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private Canvas bitG;
	private Canvas bitG1;
	private boolean isMove;
	/** false 表示向右 */
	private boolean isLeft;

	private float moveScale;
	private float moveScale1;
	private int moveAlpha;

	private int[] helpOff = { 235, 230, 220, 210, 190, 170, 160, 150, 145, 140,
			145, 150, 160, 170, 190, 210, 220, 230, 235, 240 };
	private int[] helpOff1 = { 245, 250, 260, 270, 290, 310, 320, 330, 335,
			340, 335, 330, 320, 310, 290, 270, 260, 250, 245, 240 };

	private int helpXindex;

	private void paintHelp(Canvas g, Paint p) {
		if (isMove) {
			if (isLeft) {// 向左移动
				if (helpXindex < 9) {
					g.save();
					g.scale(moveScale1, moveScale1, helpOff[helpXindex], 400);
					p.setAlpha(255 - moveAlpha);
					T.TP.paintImage(g, p, bitmap1, helpOff[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();

					g.save();
					g.scale(moveScale, moveScale, helpOff1[helpXindex], 400);
					p.setAlpha(moveAlpha);
					T.TP.paintImage(g, p, bitmap, helpOff1[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();
				} else {
					g.save();
					g.scale(moveScale, moveScale, helpOff1[helpXindex], 400);
					p.setAlpha(moveAlpha);
					T.TP.paintImage(g, p, bitmap, helpOff1[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();

					g.save();
					g.scale(moveScale1, moveScale1, helpOff[helpXindex], 400);
					p.setAlpha(255 - moveAlpha);
					T.TP.paintImage(g, p, bitmap1, helpOff[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();
				}
				moveAlpha -= 10;
				if (moveAlpha < 0) {
					moveAlpha = 0;
				}
				moveScale -= 0.015f;
				moveScale1 += 0.015f;
				helpXindex++;
				if (helpXindex >= helpOff.length) {
					helpXindex = 0;
					isMove = false;
				}
			} else {
				if (helpXindex < 9) {
					g.save();
					g.scale(moveScale1, moveScale1, helpOff1[helpXindex], 400);
					p.setAlpha(255 - moveAlpha);
					T.TP.paintImage(g, p, bitmap1, helpOff1[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();

					g.save();
					g.scale(moveScale, moveScale, helpOff[helpXindex], 400);
					p.setAlpha(moveAlpha);
					T.TP.paintImage(g, p, bitmap, helpOff[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();
				} else {
					g.save();
					g.scale(moveScale, moveScale, helpOff[helpXindex], 400);
					p.setAlpha(moveAlpha);
					T.TP.paintImage(g, p, bitmap, helpOff[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();

					g.save();
					g.scale(moveScale1, moveScale1, helpOff1[helpXindex], 400);
					p.setAlpha(255 - moveAlpha);
					T.TP.paintImage(g, p, bitmap1, helpOff1[helpXindex], 400,
							T.ANCHOR_CHV);
					g.restore();
				}

				moveAlpha -= 10;
				if (moveAlpha < 0) {
					moveAlpha = 0;
				}
				moveScale -= 0.015f;
				moveScale1 += 0.015f;
				helpXindex--;
				if (helpXindex < 0) {
					helpXindex = 0;
					isMove = false;
				}
			}
			p.setAlpha(255);
		} else {
			paintCanvas(g, p, pageIndex);
		}
	}

	private void paintCanvas(Canvas g, Paint p, int pageIndex) {
		switch (pageIndex) {
		case 1:
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[0]), 67, 137, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[1]), 67, 268, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[2]), 67, 393, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[3]), 67, 521, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[4]), 67, 643, p);
			g.drawBitmap(Pic.imageSrcs(163), 142, 123, p);
			break;
		case 2:
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[5]), 67, 135, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[6]), 65, 268, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[7]), 65, 430, p);
			g.drawBitmap(Pic.imageSrcs(GameData.ordGem[8]), 65, 580, p);
			g.drawBitmap(Pic.imageSrcs(164), 133, 123, p);
			break;
		case 0:
			g.drawBitmap(Pic.imageSrcs(165), 66, 139, p);

			for (int i = 8; i < 13; i++) {
				T.TP.paintImage(g, p, Pic.imageSrcs(i), 79 + (i - 8) * 70, 503,
						T.ANCHOR_LD);
			}

			switch (help3Status) {
			case 0:
				for (int i = 0; i < helpObj.length; i++) {
					T.TP.paintImage(g, p, Pic.imageSrcs(helpObj[i]),
							193 + i * 68, 200, T.ANCHOR_CHV);
				}
				for (int i = 0; i < helpObj1.length; i++) {
					T.TP.paintImage(g, p, Pic.imageSrcs(helpObj1[i]),
							193 + i * 68, 273, T.ANCHOR_CHV);
				}
				g.drawBitmap(Pic.imageSrcs(625), 250, 199 + yOffTemp, p);

				yOffTemp += 10;
				if (yOffTemp >= 74) {
					yOffTemp = 0;
					setHelp3Status(1);
				}
				break;
			case 1:
				for (int i = 0; i < helpObj.length; i++) {
					switch (i) {
					case 1:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj[i]),
								193 + i * 68, 200 + yOffTemp, T.ANCHOR_CHV);
						break;
					default:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj[i]),
								193 + i * 68, 200, T.ANCHOR_CHV);
						break;
					}
				}
				for (int i = 0; i < helpObj1.length; i++) {
					switch (i) {
					case 1:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj1[i]),
								193 + i * 68, 273 - yOffTemp, T.ANCHOR_CHV);
						break;
					default:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj1[i]),
								193 + i * 68, 273, T.ANCHOR_CHV);
						break;
					}
				}

				yOffTemp += 12;
				if (yOffTemp >= 75) {
					yOffTemp = 0;
					setHelp3Status(2);
					for (int i = 0; i < helpObj1.length; i++) {
						switch (i) {
						case 1:
							break;
						default:
							deadObj.add(new DeadObj(0, 193 + i * 68, 273));
							break;
						}
					}
				}
				break;
			case 2:
				for (int i = 0; i < helpObj.length; i++) {
					switch (i) {
					case 1:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj1[i]),
								193 + i * 68, 200 + yOffTemp, T.ANCHOR_CHV);
						break;
					default:
						T.TP.paintImage(g, p, Pic.imageSrcs(helpObj[i]),
								193 + i * 68, 200, T.ANCHOR_CHV);
						break;
					}
				}

				g.drawBitmap(Pic.imageSrcs(8), 232, 242, p);
				for (Iterator<DeadObj> iterator = deadObj.iterator(); iterator
						.hasNext();) {
					DeadObj type = iterator.next();
					type.paint(g, p);
					switch (type.status) {
					case 2:
						iterator.remove();
						break;
					}
				}

				switch (deadObj.size()) {
				case 0:
					setHelp3Status(0);
					break;
				}
				break;
			}

			break;
		}
	}

	private int yOffTemp;
	/** 0正常，1滑动中，2爆炸中 */
	private int help3Status;
	/***/
	private int[] helpObj = { 4, 1, 3, 4 };
	private int[] helpObj1 = { 1, 2, 1, 1 };

	private ArrayList<DeadObj> deadObj = new ArrayList<DeadObj>(4);

	public void setHelp3Status(int help3Status) {
		this.help3Status = help3Status;
	}

	/** -1left,1right */
	private void pageHelp(int dir) {
		if (isMove) {
			return;
		}
		isMove = true;

		bitmap.eraseColor(Color.TRANSPARENT);
		paintCanvas(bitG, SystemConfig.systemP, pageIndex);
		if (dir < 0) {
			isLeft = true;
			helpXindex = 0;
		} else {
			isLeft = false;
			helpXindex = helpOff.length - 1;
		}
		pageIndex += dir;
		bitmap1.eraseColor(Color.TRANSPARENT);
		paintCanvas(bitG1, SystemConfig.systemP, pageIndex);

		moveAlpha = 205;
		moveScale = 1;
		moveScale1 = 0.7f;
	}

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		int[] imageNumsPNG = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				14, 15, 33, 41, 42, 43, 44, 45, 46, 47, 49, 50, 51, 52, 54, 55,
				57, 59, 60, 61, 62, 63, 64, 65, 66, 103, 105, 150, 151, 152,
				153, 154, 155, 156, 157, 158, 163, 164, 575, 576, 577, 578,
				579, 612, 613, 163, 164, 165, 625, 632 };

		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(Integer.valueOf(intTemp));
		}
		// Pic.loadImage(1);
		Pic.loadImage(imageAsPNG);

		bitmap = Bitmap.createBitmap(480, 800, Config.ARGB_4444);
		bitG = new Canvas(bitmap);

		bitmap1 = Bitmap.createBitmap(480, 800, Config.ARGB_4444);
		bitG1 = new Canvas(bitmap1);
	}

	@Override
	public void onBackPressed() {
		switch (GameControl.menu_status) {
		case MENU_ABOUT:
			setMenuStatus(MenuStatus.MENU_NONE);
			break;
		case MENU_HELP:
			setMenuStatus(MenuStatus.MENU_NONE);
			break;
		case MENU_MORE:
		case MENU_NONE:
		case MENU_SET:
			break;
		case MENU_TOP:
			setMenuStatus(MenuStatus.MENU_NONE);
			break;
		case TOP_MAP:
			break;
		case TOP_STARY:
			break;
		default:
			break;
		}

	}

	public void paintString(Canvas g, Paint p, String str, int x, int y,
			int width) {
		String[] strs = str2lines(str, width, p);

		FontMetrics fm = p.getFontMetrics();

		float fFontHeight = (float) Math.ceil(fm.descent - fm.ascent);
		y += fFontHeight / 2;
		for (int i = 0; i < strs.length; i++) {
			g.drawText(strs[i], x, y + i * fFontHeight, p);
		}
	}

	private static String[] str2lines(String str, int width, Paint p) {
		ArrayList<String> result = new ArrayList<String>();
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];

			p.getTextWidths(str, widths);
			int curWidth = 0;
			int curLineWidth = 0;
			StringBuilder sbLine = new StringBuilder();
			StringBuilder sbWord = new StringBuilder();
			for (int j = 0; j < len; j++) {
				int curCharWidth = (int) Math.ceil(widths[j]);
				char tChar = str.charAt(j);
				if (Character.isWhitespace(tChar)) {// 是空格那么就可以判断了
					if (curLineWidth + curWidth <= width) {
						curLineWidth += curWidth;
					} else {
						curLineWidth = curWidth;
						result.add(sbLine.toString());
						sbLine.delete(0, sbLine.length());
					}

					sbLine.append(sbWord);
					sbWord.delete(0, sbWord.length());

					curWidth = curCharWidth;
					sbWord.append(tChar);
				} else {
					curWidth += curCharWidth;
					sbWord.append(tChar);
				}
			}

			if (sbWord.length() > 0) {
				if (curLineWidth + curWidth <= width) {
					curLineWidth += curWidth;
					sbLine.append(sbWord);
					result.add(sbLine.toString());
				} else {
					if (sbLine.length() > 0) {
						result.add(sbLine.toString());
						sbLine.delete(0, sbLine.length());
					}
					sbLine.append(sbWord);
					result.add(sbLine.toString());
				}
			} else {
				if (sbLine.length() > 0) {
					result.add(sbLine.toString());
				}
			}
		}

		return result.toArray(new String[result.size()]);
	}
}
