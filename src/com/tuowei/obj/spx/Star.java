package com.tuowei.obj.spx;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.T;
import com.gameFrame.pic.Pic;
import com.tuowei.canvas.GameCanvas;
import com.tuowei.tool.LayerData;

public class Star {
	/**
	 * ��������������Ƿɵ�ָ��λ��
	 * */
	int which;
	/*** ����������������[0]�� --[1]����--[2]����--[3]���--[4]ҩˮ **/
	int path[][];
	int index;
	int pic;
	int sssx[] = new int[4];
	public boolean isDead = false;
	int startA = 0;// ���ٶ�
	int doubledouble = 1;// �ӱ�
	float angle;// xuanz
	float addAng;
	long timeShu = 0;
	private GameCanvas gameCanvas;

	public Star(int which, int path[][], int doubledouble, GameCanvas gameCanvas) {
		this.which = which;
		this.path = path;
		this.gameCanvas = gameCanvas;
		this.pic = LayerData.flyPosition[which];
		this.doubledouble = doubledouble;
		if (which == 0) {// �鱾��ͣ��ת����
			addAng = 400;
		} else if (which == 2) {
			this.angle = LayerData.getAngle(path[0][0], path[0][1],
					path[path.length - 1][0], path[path.length - 1][1]);
		}
	}

	// �����ɶ�
	public Star(int which, int ss[], int doub, GameCanvas gameCanvas) {
		this.doubledouble = doub;
		this.which = which;
		this.path = T.TM.getLine(ss[0], ss[1], ss[2], ss[3]);
		this.sssx = ss;
		this.pic = LayerData.flyPosition[which];
		this.angle = LayerData.getAngle(ss[0], ss[1], ss[2], ss[3]);
		this.gameCanvas = gameCanvas;
	}

	public void paintStar(Canvas g, Paint p) {
		if (which == 3) {
		} else {
			g.save();
			g.rotate(angle, path[index][0], path[index][1]);
			if (which == 1) {
				g.scale(fData, fData, path[index][0], path[index][1]);
			}
			T.TP.paintImage(g, p, Pic.imageSrcs(pic), path[index][0],
					path[index][1], T.ANCHOR_CHV);
			g.restore();
		}
		runStar();
	}

	private float fData;

	public void runStar() {
		index += startA;
		angle += addAng;
		startA += 3;
		if (index >= path.length) {
			index = path.length - 1;
			isDead = true;
			switch (which) {
			case 0:// ��������
				if (gameCanvas.lead != null)
					gameCanvas.lead.addReiki(gameCanvas.getDouble(0) * 10
							* doubledouble);
				break;
			case 1:// ����{���ӵ��˱���==Ч��}
				if (gameCanvas.lead != null)
					gameCanvas.makeLocked(path[path.length - 1][0],
							path[path.length - 1][1], 150 * doubledouble);
				break;
			case 2:// ����
				if (gameCanvas.lead != null)
					gameCanvas.lead.setStats(3);// ��ͨ����
				break;
			case 3:// ���ӽ��
				gameCanvas.addCoin(gameCanvas.getDouble(0) * 1 * doubledouble);
				break;
			case 4:// ҩˮ
				if (gameCanvas.lead != null) {
					gameCanvas.lead.setStats(1);// �л���ҩ״̬
					gameCanvas.lead.addOrReduceLife(0, 10);
				}
				break;
			}
		}

		fData = 1 - index / (1.7f * path.length);
		if (which == 2) {
			// ����
			if (index >= path.length - 80) {
				this.pic = 462;
			} else if (index > 80) {
				this.pic = 461;
			}
		}
	}
}
