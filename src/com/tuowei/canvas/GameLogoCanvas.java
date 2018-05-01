package com.tuowei.canvas;

import java.util.ArrayList;
import java.util.TreeMap;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.gameFrame.T;
import com.gameFrame.controller.IScene;
import com.gameFrame.pic.Pic;

/**
 * 游戏logo和声音询问界面
 * 
 * @author Joniy
 */
public class GameLogoCanvas extends IScene {
	public final int LOGO_PIC = 0;// 显示logo

	public final int LOGO_SOUND = 1;// 声音选择

	public int logoStatus;

	private int logoNumc = 0;// 当前显示的logo

	private int logoNumo = 1;// 共显示几款LOGO

	private int logoDelay = 3000;// 每款LOGO显示的时间
	private long logoTimeo;

	// 需要载入图片的参数
	private int[] imageNumsPNG = new int[] {};

	public GameLogoCanvas() {

	}

	public void keyAction(TreeMap<Integer, Point> mPoint) { }

	public void run() {

	}

	public void runThread() {

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		setLogoStatus(LOGO_PIC);
	}

	private void setLogoStatus(int logoStatus) {
		this.logoStatus = logoStatus;
		switch (logoStatus) {
		case LOGO_PIC:
			logoNumc = 0;
			logoTimeo = 0;
			break;
		case LOGO_SOUND:
			break;
		}
	}

	public void paint(Canvas g, Paint p) {
		switch (logoStatus) {
		case LOGO_PIC:
			switch (logoNumc) {
			case 0:// 第一款LOGO
				T.TP.paintImage(g, p, Pic.imageSrcs(9), 0, 0, T.ANCHOR_LU);
				break;
			}
			if (logoTimeo == 0) {
				logoTimeo = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - logoTimeo >= logoDelay) {
				logoTimeo = System.currentTimeMillis();
				logoNumc++;
				if (logoNumc >= logoNumo) {
					logoNumc = 0;
					setLogoStatus(LOGO_SOUND);
				}
			}
			break;
		case LOGO_SOUND:
			break;
		}
		paintDebug(g, p);
	}

	private void paintDebug(Canvas g, Paint p) {
	}

	/**
	 * 载入资源
	 */
	public void loadingData() {
		loadingImage();
		initData();
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * 释放资源
	 */
	public void disingData() {
		Pic.disImage(imageAsPNG);
	}

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		imageAsPNG.clear();
		for (int intTemp : imageNumsPNG) {
			imageAsPNG.add(intTemp);
		}
//		Pic.loadImage(1);
		Pic.loadImage(imageAsPNG);
	}

}
