package com.tuowei.canvas;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.controller.GameDirector;
import com.gameFrame.controller.IScene;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.A;
import com.gameFrame.util.M;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.tool.ShareCtrl;

/**
 * 游戏过渡
 * 
 * @author Joniy
 */
public class LoadingCanvas extends IScene {

	public static final int NONE = -1;

	public static int showIndex;

	private int progressc = 0;// 当前进度
	private int progressTemp = 0;// 进度临时参数
	private int progresso = 100;// 总进度

	private int waitDelay = 1500;
	private long waitTime;
	private long waitTimeLast;

	// 需要载入图片的参数
	private int[] imageNumsPNG = new int[] { 476, 633, 478 };

	public LoadingCanvas() {
		loadingImage();
		initData();
		showIndex = NONE;
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

	}

	private int[] c = { 275, 287, 303, 323 };
	private int clipIndex;
	private long clipTime;
	private int fIndex;

	public void paint(Canvas g, Paint p) {
		g.drawBitmap(Pic.imageSrcs(633), 0, 0, p);

		g.save();
		g.clipRect(165, 478, c[clipIndex], 508);
		try {
			g.drawBitmap(Pic.imageSrcs(478), 165, 478, p);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			g.restore();
		}

		A.a.paintFrame(g, p, Pic.imageSrcs(476), 189, 324, fIndex, 6);

		if (System.currentTimeMillis() - clipTime > 200) {
			clipIndex++;
			if (clipIndex >= c.length) {
				clipIndex = 0;
			}
			fIndex++;
			if (fIndex >= 6) {
				fIndex = 0;
			}
			clipTime = System.currentTimeMillis();
		}

		ShareCtrl.sc.paintTransitionUI(g, p);
		logic();
	}

	public void show() {
		ShareCtrl.sc.playTransitionUI();
		GameDirector.shareDirector().isTouchPatcher = false;
		initShowData();
		super.show();
	}

	/**
	 * 初始化百分比参数
	 */
	private void initShowData() {
		progressc = 0;
		progressTemp = 0;
	}

	/**
	 * 逻辑处理
	 */
	private void logic() {
		if (!GameMainActivity.bffa.isLoading && progressTemp == progresso) {
			GameMainActivity.bffa.showView();
			GameDirector.shareDirector().isTouchPatcher = true;
			ShareCtrl.sc.playTransitionUI();
		}
		if (!GameMainActivity.bffa.isLoading) {
			progressc = progresso;
		} else {
			if (progressc <= progresso - 10) {
				waitTimeLast = System.currentTimeMillis();
				if (waitTimeLast - waitTime >= waitDelay) {
					waitTime = waitTimeLast;
					progressc += (M.getRandom(2) + 9);
				}
			}
		}
		//
		if (progressTemp < progressc) {
			if (progressc - progressTemp >= 5 && progressc == progresso) {
				progressTemp += 5;
			} else {
				progressTemp++;
			}
		}
	}

	public void run() {

	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(Integer.valueOf(intTemp));
		}
		// Pic.loadImage(0);
		Pic.loadImage(imageAsPNG);
	}

}
