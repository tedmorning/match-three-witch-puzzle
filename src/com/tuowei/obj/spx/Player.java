package com.tuowei.obj.spx;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gameFrame.spx.SpxUtil;
import com.tuowei.canvas.GameCanvas;
import com.tuowei.db.DB;
import com.tuowei.sound.MUAU;
import com.tuowei.sound.MuAuPlayer;
import com.tuowei.tool.LayerData;

public class Player extends SpxUtil {
	public int lifeAll;// �ܹ�����//��ǰ����
	public int lifeTemp_;
	public int lifeTemp;
	public int reiki_;
	public int reiki;// ����--��ǰ����{0--360}
	public boolean isDead;

	private int level;
	/** ���� */
	private int defense;

	private int phyAtk;
	private int mogicAtk;

	public int levelTemp;

	private GameCanvas gameCanvas;

	/** ��1�������3�׶�������4֡ */
	private final int[][] playScale = { { 1, 3, 2, -20 }, { 1, 3, 3, -50 },
			{ 1, 3, 4 }, { 1, 3, 5 }, { 1, 3, 6 }, { 1, 3, 7 }, { 1, 3, 8 },
			{ 1, 3, 9 }, { 1, 3, 10 }, { 1, 3, 11 }, { 1, 3, 12 },
			{ 1, 7, 2, -20 }, { 1, 7, 3, -50 }, { 1, 7, 4 }, { 1, 7, 5 },
			{ 1, 7, 6 }, { 1, 7, 7 }, { 1, 7, 8 }, { 1, 7, 9 }, { 1, 7, 10 },
			{ 1, 7, 11 }, { 1, 7, 12 } };

	/** ��Ч����֡ :��0�������3�׶����ģ���7֡ */
	private final int[][] actFrame = { { 0, 3, 7 }, { 0, 5, 7 }, { 0, 6, 7 },
			{ 0, 7, 7 }, { 1, 3, 8 }, { 1, 5, 7 }, { 1, 6, 7 }, { 1, 7, 7 }, };

	private boolean isAct;

	public Player(int actionDatIndex, int absX, int absY, GameCanvas gameCanvas) {
		this.actionDatIndex = actionDatIndex;
		visible = true;
		actionDelay = 100;
		actionDirNum = 1;
		actionStatus = 0;
		actionWait = actionStatus;
		this.absX = absX;
		this.absY = absY;

		this.gameCanvas = gameCanvas;
		initFromDB();
	}

	public void initFromDB() {
		level = DB.db.getLeadSave()[actionDatIndex][5];
		levelTemp = level + 1;
		lifeTemp_ = lifeAll = lifeTemp = LayerData.dateBasic[actionDatIndex][3]
				* levelTemp;// ���ؿ�����Ѫ��
		defense = LayerData.dateBasic[actionDatIndex][2]
				+ DB.db.getLeadSave()[actionDatIndex][2];// ����
		phyAtk = LayerData.dateBasic[actionDatIndex][0]
				+ DB.db.getLeadSave()[actionDatIndex][0];// ������ϵ��
		mogicAtk = LayerData.dateBasic[actionDatIndex][1]
				+ DB.db.getLeadSave()[actionDatIndex][1];// ħ������ϵ��

		reiki_ = reiki = LayerData.reiki;
	}

	public void run() {
		if (reiki_ != reiki || lifeTemp != lifeTemp_) {
			runCoin_Reiki();
		}
	}

	public void keyAn() {
		if (isStatic()) {
			HandleSkill1();
		}
	}

	private void HandleSkill1() {
		if (reiki_ >= 360) {
			MuAuPlayer.muaup.aupStart(MUAU.t9);
			gameCanvas.initShake();
			setStats(7);
			reiki_ -= 360;
			reiki = reiki_;
			LayerData.reiki = reiki;
		} else if (reiki_ >= 240) {
			MuAuPlayer.muaup.aupStart(MUAU.t9);
			gameCanvas.initShake();
			setStats(6);
			reiki_ -= 240;
			reiki = reiki_;
			LayerData.reiki = reiki;
		} else if (reiki_ >= 120) {
			MuAuPlayer.muaup.aupStart(MUAU.t9);
			gameCanvas.initShake();
			setStats(5);
			reiki_ -= 120;
			reiki = reiki_;
			LayerData.reiki = reiki;
		} else if (DB.db.getNumbss()[0] > 0) {
			MuAuPlayer.muaup.aupStart(MUAU.t9);
			DB.db.getNumbss()[0]--;
			DB.db.saveDB();
			gameCanvas.initShake();
			setStats(7);
		}
	}

	public void paintX(Canvas g, Paint p) {
		boolean isScale = false;
		int yTemp = absY;
		for (int i = 0; i < playScale.length; i++) {
			if (actionDatIndex == playScale[i][0]
					&& actionStatus == playScale[i][1]
					&& actionFrameIndex == playScale[i][2]) {
				switch (playScale[i].length) {
				case 4:
					absY += playScale[i][3];
					break;
				default:
					absY -= 100;
					isScale = true;
					break;
				}
				break;
			}
		}
		if (isScale) {
			g.save();
			g.scale(0.8f, 0.8f, absX, absY);
		}
		super.paintX(g, p);
		if (isScale) {
			g.restore();
		}
		absY = yTemp;

		if (isAct) {
			gameCanvas.foe.setStatut(2);
			isAct = false;
		}
	}

	int r = 35;

	// ��������
	public void addReiki(int value) {
		reiki_ += value;
		if (reiki_ >= 360) {
			reiki_ = 360;
		}
	}

	/** �л�״̬[0]����[1]��ҩ[2]����[3]��ͨ����[4]��ͨ����[5]����1[6]����2[7]����3[8]�ж� */
	public void setStats(int i) {
		switch (actionStatus) {
		case 8:
			return;
		}
		setActionStatus(i, 1);
		for (int j = 0; j < actFrame.length; j++) {
			if (actionDatIndex == actFrame[j][0] && i == actFrame[j][1]) {
				isAct = true;
				gameCanvas.initUpAndDoen();// ʵ�ֶ���
				break;
			}
		}

		switch (i) {
		case 2:
			addOrReduceLife(1, gameCanvas.foe.getAttack());
			break;
		}
	}

	public int getAttack() {
		switch (actionStatus) {
		case 3:
			return phyAtk * levelTemp * gameCanvas.lastRemoveNum
					+ gameCanvas.lastGoldRemoveNum * phyAtk;// �ȼ�����, ����Ӱ��, ��ɫӰ��
		case 5:
		case 6:
		case 7:
			return mogicAtk * levelTemp * (actionStatus - 4);
		}

		return 0;
	}

	public boolean isStatic() {
		if (actionStatus == 0) {
			return true;
		} else {
			return false;
		}
	}

	/** �ָ����ߴ������---һ��Ѫ��--0+***1- */
	public void addOrReduceLife(int i, int k) {
		switch (i) {
		case 0:
			lifeTemp_ += k;
			if (lifeTemp_ >= lifeAll) {
				lifeTemp_ = lifeAll;
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
				isDead = true;
			}
			break;
		}
	}

	int runSpeed = 20;

	private void runCoin_Reiki() {
		if (reiki < reiki_) {
			reiki++;
			LayerData.reiki = reiki;
		}
		// runLife
		if (lifeTemp < lifeTemp_) {
			if (lifeTemp < lifeTemp_ - 11) {
				lifeTemp += runSpeed;
			} else {
				lifeTemp++;
			}
		}
		if (lifeTemp > lifeTemp_) {
			if (lifeTemp > lifeTemp_ + 11) {
				lifeTemp -= runSpeed;
			} else {
				lifeTemp--;
			}
			if (lifeTemp <= 0) {
				lifeTemp = 0;
			}
		}

	}

	public void clear() {
		// coinPicList.clear();
	}

}
