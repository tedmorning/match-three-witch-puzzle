package com.tuowei.canvas;

import java.util.ArrayList;
import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gameFrame.T;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;
import com.gameFrame.util.M;
import com.gameFrame.util.S;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.GameControl;
import com.tuowei.db.DB;
import com.tuowei.tool.ShareCtrl;

//需要添加：剩余技能点，技能点充值
public class GameSelectView extends IScene {

	/** 0人物图片插入 */
	private int status;

	private int[] xOff = { 700, 600, 510, 420, 340, 260, 190, 130, 80, 40, 20,
			10, 8, 5, 1 };
	private int xOffIndex;
	private long xOffTimeo;
	private ImagesButton toButn;

	@Override
	public void paint(Canvas g, Paint p) {
		super.paint(g, p);
		g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);

		switch (status) {
		case 0:
			g.drawBitmap(Pic.imageSrcs(109), -xOff[xOffIndex], 93, p);
			g.drawBitmap(Pic.imageSrcs(110), xOff[xOffIndex], 230, p);

			if (System.currentTimeMillis() - xOffTimeo > 50) {
				xOffIndex++;
				if (xOffIndex >= xOff.length) {
					xOffIndex = 0;
					status = 1;
				}
				xOffTimeo = System.currentTimeMillis();
			}
			break;
		default:
			if (GameControl.selectIndex == 0) {
				g.save();
				g.rotate(180, 240, 241);
				g.drawBitmap(Pic.imageSrcs(114), 0, 115, p);
				g.restore();
			}
			g.drawBitmap(Pic.imageSrcs(109), 0, 93, p);
			if (GameControl.selectIndex == 1) {
				g.drawBitmap(Pic.imageSrcs(114), 0, 234, p);
			}
			g.drawBitmap(Pic.imageSrcs(110), 0, 230, p);

			switch (GameControl.selectIndex) {// 等级
			case 0:
				T.TP.paintNumberX(g, p, Pic.imageSrcs(601), roleData[0][5],
						334, 148, 0, T.ANCHOR_CHV);
				break;
			default:
				A.a.paintNum(g, p, Pic.imageSrcs(626), roleData[1][5], 186, 438);
				break;
			}

			break;
		}

		g.drawBitmap(Pic.imageSrcs(111), 5, 540, p);
		g.drawBitmap(Pic.imageSrcs(108), 216, 5, p);

		g.drawBitmap(Pic.imageSrcs(623), 7, 754, p);

		int yTemp = 0;
		for (int i = 0; i < 3; i++) {
			yTemp = 537 + 38 * i;
			A.a.paintFrame(g, p, Pic.imageSrcs(600), 205, yTemp, 0, 4);
			switch (roleData[GameControl.selectIndex][i]) {
			case 0:// 没有
				break;
			default:
				g.save();
				g.clipRect(205, yTemp,
						205 + 176 * roleData[GameControl.selectIndex][i] / 10,
						yTemp + 32);
				A.a.paintFrame(g, p, Pic.imageSrcs(600), 205, yTemp, i + 1, 4);
				g.restore();
				break;
			}
			A.a.paintNum(g, p, Pic.imageSrcs(601),
					roleData[GameControl.selectIndex][i], 390, yTemp);

			if (roleData[GameControl.selectIndex][3] > 0) {
				butn[i].paintX(g, p);
			}
		}

		g.drawBitmap(Pic.imageSrcs(609), 7, 710, p);
		A.a.paintNum(g, p, Pic.imageSrcs(601),
				roleData[GameControl.selectIndex][3], 280, 710);

		T.TP.paintNumberX(g, p, Pic.imageSrcs(601),
				roleData[GameControl.selectIndex][4], 207, 775, 0, T.ANCHOR_CHV);// 玩家经验
		A.a.paintNum(g, p, Pic.imageSrcs(601),
				totalExp[GameControl.selectIndex], 275, 760);

		if (storyindex < 2 && status != 0) {
			paintStory(g, p);
		}
		toButn.paintX(g, p);

	}

	private String story[] = {
			"布丁王国中有个布丁学院，有很多的萝莉和正太在那里学习魔法和武技 ，他们每年都会举行一次试练比赛 以此来决定谁是年级的NO.1 ",
			"今年的比赛中娜奥美和艾维 斯是夺冠的热门，快来帮助他们完成试练吧。 " };

	private final int w_fix = 480, h_fix = 800;
	private final int storyarea[] = { 300, 450 };
	private int storyindex;

	private void initStory() {
		storyindex = 0;
		initStory(0);
	}

	/** 0放大,1继续放大,2缩小 **/
	private int storystatus;

	private void initStory(int status) {
		storystatus = status;
		switch (storystatus) {
		case 0:
			width = 0;
			height = 0;
			zoomspeed = 0;
			st = "";
			stindex = 0;
			break;
		case 1:
			zoomspeed = 20;
			break;
		case 2:
			zoomspeed = 20;
			break;
		}
	}

	private int width, height;
	private int zoomspeed;
	private final int zoomadd = 3;
	private String st;
	private int stindex;

	private void runStory() {
		switch (storystatus) {
		case 0:
			if (width < storyarea[0]) {
				zoomspeed += zoomadd;
				width += zoomspeed;
				if (width >= storyarea[0]) {
					width = storyarea[0];
				}
				height = storyarea[1] * width / storyarea[0];
			}
			if (stindex < story[storyindex].length() - 1) {
				stindex++;
				st = story[storyindex].substring(0, stindex);
			}
			break;
		case 1:
			width += zoomspeed;
			if (width >= storyarea[0] + 100) {
				initStory(2);
			}
			height = storyarea[1] * width / storyarea[0];
			break;
		case 2:
			width -= zoomspeed;
			if (width <= 0) {
				width = 0;
				initStory(0);
			}
			height = storyarea[1] * width / storyarea[0];
			break;
		}
	}

	private void paintStory(Canvas g, Paint p) {
		runStory();
		ShareCtrl.sc.paintB(g, p, 100);
		ShareCtrl.sc.paintRect(g, p, w_fix / 2, h_fix / 2, width, height);
		if (storystatus == 0 && width >= storyarea[0]) {
			p.setARGB(255, 250, 220, 135);
			paintString(g, p, st, w_fix / 2 - storyarea[0] / 2 + 10, h_fix / 2
					- storyarea[1] / 2 + 30, storyarea[0] - 40);
		}
	}

	@Override
	public void keyAction(TouchEvent e) {
		super.keyAction(e);

		switch (toButn.keyAction(e)) {
		case 3:
			if (storyindex < 2) {
				storyindex++;
				initStory(1);
			} else {
				GameMainActivity.bffa.changeView(4);
			}
			break;
		}

		for (int i = 0; i < 3; i++) {
			if (roleData[GameControl.selectIndex][3] > 0) {
				switch (butn[i].keyAction(e)) {
				case 3:
					DB.db.getLeadSave()[GameControl.selectIndex][3]--;
					DB.db.getLeadSave()[GameControl.selectIndex][i]++;
					DB.db.saveDB();
					roleData = DB.db.getLeadSave();
					break;
				}
			}
		}

		switch (e.getEventTye()) {
		case MotionEvent.ACTION_DOWN:
			if (M.m.isInRect(e.point.x, e.point.y, 0, 104, 480, 153)) {
				setSelectIndex(0);
			} else if (M.m.isInRect(e.point.x, e.point.y, 0, 335, 480, 153)) {
				setSelectIndex(1);
			}
			break;
		}

	}

	/** 保存主绝: [0]物理攻击，[1]魔法攻击，[2]防御，[3]剩余技能点，[4]当前经验， [5]当前等级 */
	private int[][] roleData;
	/** 当前升级需要的经验 */
	private int[] totalExp = new int[2];

	private void setSelectIndex(int selectIndex) {
		GameControl.selectIndex = selectIndex;
	}

	@Override
	public void loadingData() {
		super.loadingData();
		loadingImage();
		toButn = new ImagesButton(57, 90, 84, 115, 1, 2);
		toButn.setImageType(5);
		toButn.setPosition(385, 710, 19, 16);
		roleData = DB.db.getLeadSave();

		totalExp[0] = 10 + roleData[0][5] * 100;
		totalExp[1] = 10 + roleData[1][5] * 100;

		for (int i = 0; i < butn.length; i++) {
			butn[i] = new ImagesButton(608, 35, 35);
			butn[i].setPosition(445, 533 + 38 * i, 0, 0);
		}
		storyindex = 3;
		if (!DB.db.getIsold()) {
			initStory();
		}
	}

	private ImagesButton[] butn = new ImagesButton[3];

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		int[] imageNumsPNG = new int[] { 54, 114, 109, 110, 111, 108, 57, 115,
				600, 601, 608, 609, 623, 626 };

		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(Integer.valueOf(intTemp));
		}
		// Pic.loadImage(4);
		Pic.loadImage(imageAsPNG);
	}

	@Override
	public void onBackPressed() {
		GameMainActivity.bffa.changeView(2);
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
				if (curWidth >= width) {// 是空格那么就可以判断了
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
