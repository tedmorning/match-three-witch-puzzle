package com.tuowei.obj;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.T;
import com.gameFrame.pic.Pic;
import com.tuowei.canvas.GameCanvas;

/*** 连接或者等级特效 */
public class Eff {
	private int sta;// 0特效 1 -----1特效2
	private int doublHit;
	public boolean isDEad;
	private int ddY = 0;
	private GameCanvas gameCanvas;

	// 特效111111
	public Eff(int dubleHit, GameCanvas gameCanvas) {
		this.doublHit = dubleHit;
		sta = 0;
		this.gameCanvas = gameCanvas;
	}

	// 特效222222
	public Eff(int assPic, int aaaa) {
		sta = 1;
		this.assPic = assPic;
	}

	public void paintEEE(Canvas g, Paint p) {
		switch (sta) {
		case 0:
			paintLianji(g, p);
			break;
		default:
			paintExx222(g, p);
			break;
		}
	}

	void run() {

	}

	float biggg[][] = new float[][] { { 2f, 0 }, { 1.95f, 20 }, { 1.80f, 40 },
			{ 1.65f, 90 }, { 1.45f, 130 }, { 1.2f, 155 }, { 0.95f, 200 },
			{ 1f, 255 }, { 1f, 240 }, { 1f, 230 }, { 1f, 220 }, { 1f, 210 },
			{ 1f, 200 }, { 1f, 190 }, { 1f, 180 }, { 1f, 170 }, { 1f, 160 },
			{ 1f, 150 }, { 1f, 140 }, { 1f, 130 }, { 1f, 120 }, { 1f, 110 },
			{ 1f, 100 }, { 1f, 90 }, { 1f, 80 }, { 1f, 70 }, { 1f, 60 },
			{ 1f, 50 }, { 1f, 40 }, { 1f, 30 }, { 1f, 20 }, { 1f, 10 },
			{ 1f, 0 } };
	int bigIndex;
	int temp008 = 0;
	long bigTime;

	// 连击特效
	private void paintLianji(Canvas g, Paint p) {
		p.setAlpha((int) biggg[bigIndex][1]);
		g.save();
		g.scale(biggg[bigIndex][0], biggg[bigIndex][0], 170, 400 + ddY);
		T.TP.paintImage(g, p, Pic.imageSrcs(450), 170, 400 + ddY, T.ANCHOR_CHV);
		g.restore();

		g.save();
		g.scale(biggg[bigIndex][0], biggg[bigIndex][0], 250, 400 + ddY);
		T.TP.paintNumberX(g, p, Pic.imageSrcs(451), doublHit, 250, 400 + ddY,
				-50, T.ANCHOR_CVL);
		g.restore();
		p.setAlpha(255);

		switch (temp008) {
		case 0:
			bigIndex++;
			if (bigIndex >= 8) {
				bigIndex = 7;
				temp008 = 1;
				bigTime = System.currentTimeMillis();
				gameCanvas.comboLogic();
			}
			break;
		default:
			if (System.currentTimeMillis() - bigTime > 300) {
				bigIndex++;
				if (bigIndex >= biggg.length) {
					bigIndex = biggg.length - 1;
					isDEad = true;
				}
			}
			break;
		}

	}

	private float ass[][] = new float[][] { { 0, 0.4f, 150 },
			{ 10, 0.6f, 140 }, { 20, 0.8f, 130 }, { 30, 1f, 120 },
			{ 40, 1.2f, 110 }, { 60, 1.4f, 100 }, { 80, 1.6f, 90 },
			{ 100, 1.8f, 80 }, { 120, 2f, 70 }, { 150, 1.8f, 60 },
			{ 200, 1.6f, 50 }, { 220, 1.4f, 40 }, { 240, 1.2f, 30 },
			{ 255, 1f, 20 }, { 255, 0.8f, 10 }, { 255, 1f, 0 },

			{ 230 }, { 200 }, { 170 }, { 140 }, { 110 }, { 80 }, { 50 },
			{ 20 }, { 0 } };
	private int assInd = 0;
	int assPic;// 当前特效图片
	long assTime;
	int temp010 = 0;

	// 连击特效222
	private void paintExx222(Canvas g, Paint p) {
		switch (temp010) {
		case 0:
			p.setAlpha((int) ass[assInd][0]);
			g.save();
			g.scale(ass[assInd][1], 1f, 160, 320 + ddY);
			T.TP.paintImage(g, p, Pic.imageSrcs(assPic), 160, 320
					+ (int) ass[assInd][2] + ddY, T.ANCHOR_CHV);
			g.restore();

			p.setAlpha(255);
			assInd++;
			if (assInd >= 16) {
				assInd = 15;
				temp010 = 1;
				assTime = System.currentTimeMillis();
			}
			break;
		default:
			p.setAlpha((int) ass[assInd][0]);
			T.TP.paintImage(g, p, Pic.imageSrcs(assPic), 160, 320
					- ((255 - (int) ass[assInd][0]) / 2) + ddY, T.ANCHOR_CHV);
			p.setAlpha(255);
			if (System.currentTimeMillis() - assTime > 200) {
				assInd++;
				if (assInd >= ass.length) {
					assInd = ass.length - 1;
					isDEad = true;
				}
			}
			break;
		}

	}
}
