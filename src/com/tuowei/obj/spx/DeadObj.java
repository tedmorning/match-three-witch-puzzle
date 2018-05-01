package com.tuowei.obj.spx;

import com.gameFrame.T;
import com.gameFrame.pic.Pic;
import com.tuowei.tool.GameData;

import android.graphics.Canvas;
import android.graphics.Paint;

/** 精灵死亡特效 */
public class DeadObj {

	private float[][] whOff = { { 1, 1 }, { 1.10f, 0.9f }, { 1.15f, 0.85f } };

	public static final int[] images = { 575, 576, 577, 578, 579 };
	public static final int[] images1 = { 150, 151, 152, 153, 154, 155, 156, 157, 158 };

	private int whIndex;
	private long whTimeo;

	private int x, y;

	private int imageIndex;

	public int status;

	private int type;

	public DeadObj(int imageIndex, int x, int y) {
		this.x = x;
		this.y = y;
		switch (imageIndex) {
		case -1:
			type = 1;
			break;
		case -2:
			type = 2;
			break;
		default:
			this.imageIndex = GameData.deadGem[imageIndex];
			break;
		}
	}

	public void paint(Canvas g, Paint p) {
		switch (type) {
		case 1:// 爆炸
			T.TP.paintImage(g, p, Pic.imageSrcs(images1[whIndex]), x, y,
					T.ANCHOR_CHV);

			if (System.currentTimeMillis() - whTimeo > 50) {
				whIndex++;
				if (whIndex >= images1.length) {
					whIndex = 0;
					status = 2;
				}
				whTimeo = System.currentTimeMillis();
			}
			break;
		case 2:// 直接播放星星
			T.TP.paintImage(g, p, Pic.imageSrcs(images[whIndex]), x, y,
					T.ANCHOR_CHV);

			if (System.currentTimeMillis() - whTimeo > 50) {
				whIndex++;
				if (whIndex >= images.length) {
					whIndex = 0;
					status = 2;
				}
				whTimeo = System.currentTimeMillis();
			}

			break;
		default:
			switch (status) {
			case 0:
				g.save();
				g.scale(whOff[whIndex][0], whOff[whIndex][1], x, y);
				T.TP.paintImage(g, p, Pic.imageSrcs(imageIndex), x, y,
						T.ANCHOR_CHV);
				g.restore();

				if (System.currentTimeMillis() - whTimeo > 50) {
					whIndex++;
					if (whIndex >= whOff.length) {
						whIndex = 0;
						status = 1;
					}
					whTimeo = System.currentTimeMillis();
				}
				break;
			case 1:
				T.TP.paintImage(g, p, Pic.imageSrcs(images[whIndex]), x, y,
						T.ANCHOR_CHV);

				if (System.currentTimeMillis() - whTimeo > 50) {
					whIndex++;
					if (whIndex >= images.length) {
						whIndex = 0;
						status = 2;
					}
					whTimeo = System.currentTimeMillis();
				}
				break;
			}
			break;
		}
	}
}
