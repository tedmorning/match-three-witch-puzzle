package com.tuowei.obj.spx;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.T;
import com.gameFrame.pic.Pic;
import com.gameFrame.util.M;
import com.tuowei.tool.MathData;

//点击消除时候、、、星星效果
public class ExpClick {
	public int Asp = 255;// 当前亮度
	public int Angle;// 飞行角度
	/** x===y===v */
	public float Position[] = new float[3];
	int pic = 446;
	public boolean isDead;
	double pi = Math.PI;
	int appearSpeed;
	float big;// 放大倍数
	int rotate, rotateSpeed;

	public ExpClick(float po[], int Angle, int appearSpeed, float big,
			int rotateSpeed) {
		this.Position = po;
		this.Angle = Angle;
		Asp = 255;
		this.appearSpeed = appearSpeed;
		this.big = big;
		this.rotateSpeed = rotateSpeed;
		pic = M.getRandom(463, 472);
	}

	public void paintE(Canvas g, Paint p) {
		p.setAlpha(Asp);
		g.save();
		g.rotate(rotate, Position[0], Position[1]);
		g.scale(big, big, Position[0], Position[1]);
		T.TP.paintImage(g, p, Pic.imageSrcs(pic), (int) Position[0],
				(int) Position[1], T.ANCHOR_CHV);
		g.restore();
		p.setAlpha(255);
		runE();
	}

	private void runE() {
		Position[0] += Position[2] * MathData.cos[Angle];
		Position[1] += Position[2] * MathData.sin[Angle];
		rotate += rotateSpeed;
		Asp -= appearSpeed;
		if (Asp <= 0) {
			Asp = 0;
			isDead = true;
		}
	}
}
