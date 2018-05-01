package com.tuowei.obj.spx;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.spx.SpxUtil;
import com.gameFrame.util.M;
import com.tuowei.canvas.GameCanvas;
import com.tuowei.control.GameControl;
import com.tuowei.db.DB;
import com.tuowei.sound.MUAU;
import com.tuowei.sound.MuAuPlayer;
import com.tuowei.tool.LayerData;

public class Enemy extends SpxUtil {
	public int lifeAll;// �ܹ�����//��ǰ����
	public int lifeTemp_;
	public int lifeTemp;
	public int reiki;// ����--��ǰ����
	public boolean isDead;
	public boolean canHandle2;// ���ܽ����κβ���
	private int tp2;

	// ��Ϸ�е�ǰ�ؿ�����
	private GameCanvas gameCanvas;

	public int level;
	public int levelTemp;
	/** ���� */
	private int defense;

	private int phyAtk;
	private int mogicAtk;

	/** ��1�������3�׶�������4֡ */
	private final float[][] playScale = { { 2, 3, 1, 1.06f, 10 },
			{ 2, 3, 2, 1.08f, 30 }, { 2, 3, 3, 1.15f, 60 }, { 2, 3, 4, 1.2f },
			{ 2, 3, 5, 1.2f }, { 2, 3, 6, 1.2f }, { 2, 3, 7, 1.2f },
			{ 2, 3, 8, 1.2f }, { 2, 3, 9, 1.2f }, { 2, 3, 10, 1.2f },
			{ 2, 3, 11, 1.15f, 50 }, { 2, 3, 12, 1.1f, 20 },

			{ 2, 6, 1, 1.06f, 10 }, { 2, 6, 2, 1.08f, 30 },
			{ 2, 6, 3, 1.15f, 60 }, { 2, 6, 4, 1.2f }, { 2, 6, 5, 1.2f },
			{ 2, 6, 6, 1.2f }, { 2, 6, 7, 1.2f }, { 2, 6, 8, 1.2f },
			{ 2, 6, 9, 1.2f }, { 2, 6, 10, 1.2f }, { 2, 6, 11, 1.15f, 50 },
			{ 2, 6, 12, 1.1f, 20 },

			{ 2, 7, 1, 1.06f, 10 }, { 2, 7, 2, 1.08f, 30 },
			{ 2, 7, 3, 1.15f, 60 }, { 2, 7, 4, 1.2f }, { 2, 7, 5, 1.2f },
			{ 2, 7, 6, 1.2f }, { 2, 7, 7, 1.2f }, { 2, 7, 8, 1.2f },
			{ 2, 7, 9, 1.2f }, { 2, 7, 10, 1.2f }, { 2, 7, 11, 1.15f, 50 },
			{ 2, 7, 12, 1.1f, 20 },

			{ 4, 3, 1, 1.1f, 20 }, { 4, 3, 2, 1.2f, 50 }, { 4, 3, 3, 1.2f },
			{ 4, 3, 4, 1.2f }, { 4, 3, 5, 1.1f }, };

	/** ��Ч����֡ :��0�������3�׶����ģ���7֡ */
	private final int[][] actFrame = { { 2, 3, 5 }, { 2, 5, 3 }, { 2, 6, 5 },
			{ 2, 7, 5 }, { 3, 3, 6 }, { 3, 5, 6 }, { 3, 6, 6 }, { 3, 7, 6 },
			{ 4, 3, 3 }, { 4, 5, 5 }, { 4, 6, 4 }, { 4, 7, 4 }, { 5, 3, 8 },
			{ 5, 5, 8 }, { 5, 6, 7 }, { 5, 7, 8 }, };

	private boolean isAct;

	public Enemy(int actionDatIndex, int absX, int absY, GameCanvas gameCanvas) {
		this.actionDatIndex = actionDatIndex;
		visible = true;
		actionDelay = 100;
		actionDirNum = 1;
		actionStatus = 0;
		actionWait = actionStatus;
		this.absX = absX;
		this.absY = absY;

		this.gameCanvas = gameCanvas;
		initSkill();
		initFromDB();
	}

	//
	private void initFromDB() {
		level = GameControl.gameLayer;
		levelTemp = level + 1;
		lifeTemp_ = lifeAll = lifeTemp = LayerData.dateBasic[actionDatIndex][3]
				* (level + 1);// ���ؿ�����Ѫ��
		defense = LayerData.dateBasic[actionDatIndex][2];// ����
		phyAtk = LayerData.dateBasic[actionDatIndex][0];// ������ϵ��
		mogicAtk = LayerData.dateBasic[actionDatIndex][1];// ħ������ϵ��
	}

	private void initSkill() {
		canHandle2 = true;
		tp2 = 0;
	}

	public void run() {
		if (lifeTemp_ != lifeTemp) {
			runLife();
		}

		if (!canHandle2) {// �Ƿ���ס��
			runBounded();
		}
		if (isDead) {
			MuAuPlayer.muaup.aupStart(MUAU.t7);
			gameCanvas.tp00 = 0;
			gameCanvas.setGameStatus(GameControl.GAME_SUC);
		}
	}

	public void runSli() {
		if (isTodispeer > 0) {
			return;
		}
		if (reiki >= 120) {
			if (M.getRandom(0, 3) == 1) {
				runSkill();
				gameCanvas.initUpAndDoen();
			}
		}
	}

	// ���������Ľ�����ʧЧ��
	int isTodispeer;

	// ����---�ͷż���---
	public void runSkill() {
		gameCanvas.lastGoldRemoveNumFoe = gameCanvas.lastRemoveNumFoe = 0;
		if (reiki >= 360) {
			setStatut(7);// �л����˼���3
			reiki = 0;
		} else if (reiki >= 240) {
			setStatut(6);// �л����˼���2
			reiki = 0;
		} else if (reiki >= 120) {
			setStatut(5);// �л����˼���1
			reiki = 0;
		}
	}

	// ����ס��--����ʹ�ü�����
	public void initBounded(int times) {
		this.tp2 = times;
		canHandle2 = false;
	}

	private void runBounded() {
		tp2--;
		if (tp2 <= 0) {
			tp2 = 0;
			canHandle2 = true;
		}
	}

	public void paint22(Canvas g, Paint p) {
		if (isTodispeer > 0) {
			p.setAlpha(isTodispeer);
			super.paintX(g, p);
			p.setAlpha(255);
			isTodispeer -= 5;
			if (isTodispeer <= 0) {
				isDead = true;
			}
		} else {
			boolean isScale = false;
			float scale = 1;
			int yTemp = absY;
			for (int i = 0; i < playScale.length; i++) {
				if (actionDatIndex == playScale[i][0]
						&& actionStatus == playScale[i][1]
						&& actionFrameIndex == playScale[i][2]) {
					isScale = true;
					switch (playScale[i].length) {
					case 5:
						absY += playScale[i][4];
						break;
					default:
						absY += 100;
						break;
					}
					scale = playScale[i][3];
					break;
				}
			}
			if (isScale) {
				g.save();
				g.scale(scale, scale, absX, absY);
			}

			super.paintX(g, p);
			if (isScale) {
				g.restore();
				absY = yTemp;
			}

			if (isAct) {
				gameCanvas.lead.setStats(2);
				isAct = false;
			}

		}
	}

	/** ���õ���״̬[0]����[1]��ҩ[2]����[3]��ͨ����[4]��ͨ����[5]����1[6]����2[7]����3[8]ѣ��[9]���� */
	public void setStatut(int i) {
		setActionStatus(i, 1);
		for (int j = 0; j < actFrame.length; j++) {
			if (actionDatIndex == actFrame[j][0] && i == actFrame[j][1]) {
				isAct = true;
				break;
			}
		}
		switch (i) {
		case 2:
			addOrReduceLife(1,
					gameCanvas.getDouble(0) * gameCanvas.lead.getAttack());
			break;
		}
	}

	public int getAttack() {
		switch (actionStatus) {
		case 3:
			return phyAtk * levelTemp * gameCanvas.lastRemoveNumFoe// �ȼ�����, ����Ӱ��
					+ gameCanvas.lastGoldRemoveNumFoe * phyAtk;// ��ɫӰ��
		case 5:
		case 6:
		case 7:
			return mogicAtk * levelTemp * (actionStatus - 4);
		}

		return 0;
	}

	/** �ָ����ߴ���ط�---һ��Ѫ��--0+***1- */
	public void addOrReduceLife(int i, int k) {
		if (lifeTemp_ <= 0) {
			return;
		}
		switch (i) {
		case 0:
			if (lifeTemp_ > 0) {
				lifeTemp_ += k;
				if (lifeTemp_ >= lifeAll) {
					lifeTemp_ = lifeAll;
				}
			}
			break;
		case 1:
			k -= defense;
			if (k < 5) {
				k = 5;
			} else if (k > lifeAll / 5) {
				k = lifeAll / 5;
			}

			lifeTemp_ -= k;
			if (lifeTemp_ <= 0) {
				lifeTemp_ = 0;
				isTodispeer = 255;
				int score = DB.db.getLeadSave()[gameCanvas.lead.actionDatIndex][4];
				score += levelTemp * 10;
				switch (actionDatIndex) {
				case 5:
					score += levelTemp * 90;
					break;
				}
				int upScore = 10 + (gameCanvas.lead.levelTemp - 1) * 100;
				if (score >= upScore) {
					score -= upScore;
					gameCanvas.lead.setStats(8);
					DB.db.getLeadSave()[gameCanvas.lead.actionDatIndex][3]++;
					DB.db.getLeadSave()[gameCanvas.lead.actionDatIndex][5]++;
				}
				DB.db.getLeadSave()[gameCanvas.lead.actionDatIndex][4] = score;
				DB.db.saveDB();
			}
			break;
		}
	}

	// ��������
	public void addReiki(int value) {
		reiki += value;
	}

	int runSpeed = 50;

	private void runLife() {
		if (lifeTemp < lifeTemp_) {
			if (lifeTemp < lifeTemp_ - 30) {
				lifeTemp += runSpeed;
			} else {
				lifeTemp++;
			}

		}
		if (lifeTemp > lifeTemp_) {
			if (lifeTemp > lifeTemp_ + 30) {
				lifeTemp -= runSpeed;
			} else {
				lifeTemp--;
			}

		}
	}
}
