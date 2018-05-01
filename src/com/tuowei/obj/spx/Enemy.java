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
	public int lifeAll;// 总共生命//当前生命
	public int lifeTemp_;
	public int lifeTemp;
	public int reiki;// 灵气--当前灵力
	public boolean isDead;
	public boolean canHandle2;// 不能进行任何操作
	private int tp2;

	// 游戏中当前关卡竖数
	private GameCanvas gameCanvas;

	public int level;
	public int levelTemp;
	/** 防御 */
	private int defense;

	private int phyAtk;
	private int mogicAtk;

	/** 第1个人物，第3套动作，第4帧 */
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

	/** 有效攻击帧 :第0个人物，第3套动作的，第7帧 */
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
				* (level + 1);// 本关卡敌人血量
		defense = LayerData.dateBasic[actionDatIndex][2];// 防御
		phyAtk = LayerData.dateBasic[actionDatIndex][0];// 物理攻击系数
		mogicAtk = LayerData.dateBasic[actionDatIndex][1];// 魔法攻击系数
	}

	private void initSkill() {
		canHandle2 = true;
		tp2 = 0;
	}

	public void run() {
		if (lifeTemp_ != lifeTemp) {
			runLife();
		}

		if (!canHandle2) {// 是否被困住了
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

	// 敌人死亡的渐渐消失效果
	int isTodispeer;

	// 敌人---释放技能---
	public void runSkill() {
		gameCanvas.lastGoldRemoveNumFoe = gameCanvas.lastRemoveNumFoe = 0;
		if (reiki >= 360) {
			setStatut(7);// 切换敌人技能3
			reiki = 0;
		} else if (reiki >= 240) {
			setStatut(6);// 切换敌人技能2
			reiki = 0;
		} else if (reiki >= 120) {
			setStatut(5);// 切换敌人技能1
			reiki = 0;
		}
	}

	// 被困住了--不能使用技能了
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

	/** 设置敌人状态[0]待机[1]喝药[2]受伤[3]普通攻击[4]普通技能[5]技能1[6]技能2[7]技能3[8]眩晕[9]虚弱 */
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
			return phyAtk * levelTemp * gameCanvas.lastRemoveNumFoe// 等级攻击, 个数影响
					+ gameCanvas.lastGoldRemoveNumFoe * phyAtk;// 金色影响
		case 5:
		case 6:
		case 7:
			return mogicAtk * levelTemp * (actionStatus - 4);
		}

		return 0;
	}

	/** 恢复或者打掉地方---一定血量--0+***1- */
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

	// 增加灵气
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
