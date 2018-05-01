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
 * ��Ϸlogo������ѯ�ʽ���
 * 
 * @author Joniy
 */
public class GameLogoCanvas extends IScene {
	public final int LOGO_PIC = 0;// ��ʾlogo

	public final int LOGO_SOUND = 1;// ����ѡ��

	public int logoStatus;

	private int logoNumc = 0;// ��ǰ��ʾ��logo

	private int logoNumo = 1;// ����ʾ����LOGO

	private int logoDelay = 3000;// ÿ��LOGO��ʾ��ʱ��
	private long logoTimeo;

	// ��Ҫ����ͼƬ�Ĳ���
	private int[] imageNumsPNG = new int[] {};

	public GameLogoCanvas() {

	}

	public void keyAction(TreeMap<Integer, Point> mPoint) { }

	public void run() {

	}

	public void runThread() {

	}

	/**
	 * ��ʼ������
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
			case 0:// ��һ��LOGO
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
	 * ������Դ
	 */
	public void loadingData() {
		loadingImage();
		initData();
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * �ͷ���Դ
	 */
	public void disingData() {
		Pic.disImage(imageAsPNG);
	}

	/**
	 * ����ͼƬ
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
