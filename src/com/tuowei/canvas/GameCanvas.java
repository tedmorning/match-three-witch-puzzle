package com.tuowei.canvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.MotionEvent;

import com.gameFrame.T;
import com.gameFrame.controller.GameDirector;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.JPoint;
import com.gameFrame.controller.TouchEvent;
import com.gameFrame.controls.ImagesButton;
import com.gameFrame.pic.Pic;
import com.gameFrame.spx.SpxCtrl;
import com.gameFrame.ui.UICtrl;
import com.gameFrame.util.A;
import com.gameFrame.util.M;
import com.sdk.sms.SmsInfo;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.GameControl;
import com.tuowei.db.DB;
import com.tuowei.obj.Eff;
import com.tuowei.obj.MogicButn;
import com.tuowei.obj.NameObj;
import com.tuowei.obj.PropButn;
import com.tuowei.obj.SoundButn;
import com.tuowei.obj.spx.DeadObj;
import com.tuowei.obj.spx.Enemy;
import com.tuowei.obj.spx.ExpClick;
import com.tuowei.obj.spx.Player;
import com.tuowei.obj.spx.Star;
import com.tuowei.obj.ui.LockObj;
import com.tuowei.sound.MUAU;
import com.tuowei.sound.MuAuPlayer;
import com.tuowei.tool.GameData;
import com.tuowei.tool.LayerData;
import com.tuowei.tool.ShareCtrl;
import com.tuowei.tool.StrData;

/**
 * 游戏主界面 111----普通模式下关卡layer------挑战模式下关卡-layer------------金币模式下关卡 --layer-
 * -----被东技能----------- --灵气----------血-------- 刀（攻击力）-------------——手铐时间
 * =====主动技能 ----------技能11------------技能22---- --------------------技能33333333
 * 总--------金币----碎片----分数----
 * --------------------------------------------------- -当前所选择的 主角--主角 每一关卡的总血量
 */
public class GameCanvas extends IScene {

	// 按住下一格子是否触发事件
	private JPoint tempoint;
	private int ii, jj, ik, jk;
	private int direct;// 1--2--3--4上下左右
	private int temp1, temp111;
	public int tp00;// 0成功、、1失败

	// 瓶震图片
	private int shake[][] = new int[][] { { 0, 0 }, { 2, 3 }, { -2, 3 },
			{ 0, 13 }, { -6, 9 }, { 20, 3 }, { 18, -3 }, { -2, -3 }, { 5, 10 },
			{ 12, -3 }, { -2, 10 }, { 8, 3 }, { -8, -9 }, { -2, -13 },
			{ 2, 8 }, { 0, 0 }, { 2, 3 }, { 2, 3 }, { 2, 3 }, { 2, 3 },
			{ 2, 3 }, { 2, 3 }, { 12, 3 }, { 3, 9 }, { -9, 1 }, { 1, -7 },
			{ -8, -10 }, { -1, 1 } };
	boolean isShake = false;
	private int shakeIndex = 0;

	public void initShake() {
		isShake = true;
	}

	// 初始--图框不抖动---受到攻击时抖动
	private boolean crush = false;
	private float crushNum[][] = new float[][] { { 1.02f, 0.99f },
			{ 1.06f, 0.97f }, { 1.12f, 0.94f }, { 1.15f, 0.92f },
			{ 1.07f, 0.98f }, { 0.98f, 1.02f }, { 0.96f, 1.03f },
			{ 1.04f, 0.98f }, { 1.08f, 0.96f }, { 1.12f, 0.94f },
			{ 1.15f, 0.92f }, { 1.06f, 0.97f }, { 0.98f, 1.02f },
			{ 0.96f, 1.03f } };
	private int crushIndex = 0;

	private boolean canClick() {

		switch (GameControl.game_mode) {
		case GameControl.GAME_MODE_0:
			// 存在1元素静止，不下落主角，敌人未死亡，主角为待机状态，主角屏幕未受到攻击时
			if (isCanMove && !canSlow && lead.isStatic() && !crush
					&& !foe.isDead && !lead.isDead) {
				return true;
			}
			break;
		default:
			// 存在1元素静止，不下落主角，敌人未死亡，主角为待机状态，主角屏幕未受到攻击时
			if (isCanMove && !canSlow) {
				return true;
			}
			break;
		}

		return false;
	}

	private int keyActionIndex = -1;

	public void keyAction(TouchEvent te) {
		switch (GameControl.gameMenu2Status) {
		case GameControl.GAME_MENU2_1:// 是否回主菜单
			break;
		default:
			switch (GameControl.gameStatus) {
			case GameControl.GAME_STORY:// 背景介绍
			case GameControl.GAME_LAYER:// 关卡介绍
			case GameControl.GAME_JS:// 脚本
				break;
			case GameControl.GAME_TECH:// 新手教程
				gemKeyAction(te);
				break;
			case GameControl.GAME_INTO:// 游戏
				switch (GameControl.game_mode) {
				case 0:
					switch (mogicButn.keyAction(te)) {
					case 3:// 魔法攻击
						lead.keyAn();
						// setGameStatus(GameControl.GAME_SUCALL);
						break;
					}
					for (int i = 0; i < butns.length; i++) {
						switch (butns[i].keyAction(te)) {
						case 3:
							int index = i + 1;
							if (DB.db.getNumbss()[index] > 0) {
								lastGoldRemoveNum = lastRemoveNum = 0;
								switch (i) {
								case 0:// 暴击
									lead.setStats(3);// 普通攻击
									break;
								case 1:// 血精灵
									lead.setStats(1);
									lead.addOrReduceLife(0, 2000);
									break;
								case 2:// 刷新
									initGameXY();
									break;
								}
								DB.db.getNumbss()[index]--;
								butns[i].setNum(DB.db.getNumbss()[index]);
								DB.db.saveDB();
								break;
							}
							break;
						}
					}
					break;
				}

				switch (pauseButn.keyAction(te)) {
				case 3:
					pauseGame();
					break;
				}

				gemKeyAction(te);
				break;
			case GameControl.GAME_SHOP:// 商店
				break;
			case GameControl.GAME_SUC:// 过关
				switch (nextButn.keyAction(te)) {
				case 3:
					if(DB.db.getIsRegister() == 0 && GameControl.gameLayer == GameControl.REG_Layer){
						SmsInfo.sendSms(SmsInfo.SMS_REG);
						return;
					}
					nextGame();
					break;
				}
				switch (GameControl.game_mode) {
				case GameControl.GAME_MODE_0:
					switch (restartButn.keyAction(te)) {
					case 3:
						GameControl.gameLayer--;
						if (GameControl.gameLayer < 0) {
							GameControl.gameLayer = 0;
						}
						reGame();
						break;
					}
					break;
				}
				switch (pauseButns[2].keyAction(te)) {
				case 3:
					GameMainActivity.bffa.changeView(0);
					break;
				}
				break;
			case GameControl.GAME_ERR:// 失败
				switch (restartButn.keyAction(te)) {
				case 3:
					reGame();
					break;
				}
				switch (pauseButns[2].keyAction(te)) {
				case 3:
					GameMainActivity.bffa.changeView(0);
					break;
				}
				break;
			case GameControl.GAME_SUCALL:// 通关
				switch (restartButn.keyAction(te)) {
				case 3:
					reGame();
					break;
				}
				switch (pauseButns[2].keyAction(te)) {
				case 3:
					GameMainActivity.bffa.changeView(0);
					break;
				}
				break;
			case GameControl.GAME_PAUSE:// 暂停
				switch (GameControl.gamePauseStatus) {
				case GameControl.GAME_PAUSE_1:// 继续游戏

					break;
				case GameControl.GAME_PAUSE_2:// 游戏设置

					break;
				case GameControl.GAME_PAUSE_3:// 游戏帮助

					break;
				case GameControl.GAME_PAUSE_4:// 退出游戏

					break;
				default:
					switch (pauseButns[0].keyAction(te)) {
					case 3:
						setGameStatus(GameControl.GAME_INTO);
						break;
					}
					switch (pauseButns[1].keyAction(te)) {
					case 3:
						reGame();
						break;
					}
					switch (pauseButns[2].keyAction(te)) {
					case 3:
						GameMainActivity.bffa.changeView(0);
						break;
					}
					soundButn.keyAction(te);
					break;
				}
			case GameControl.GAME_STATUS1:
				switch (skip.keyAction(te)) {
				case 3:
					storyindex++;
					if (storyindex >= story[layindex].length) {
						storyindex = 0;
						setGameStatus(GameControl.GAME_JS);
					} else {
						initStory(1);
					}
					break;
				}
				break;
			case GameControl.GAME_STATUS2:
				switch (te.getEventTye()) {
				case MotionEvent.ACTION_UP:
					teachcount++;
					if (teachcount >= teachinfo.length) {
						teachcount = teachinfo.length - 1;
						setGameStatus(GameControl.GAME_INTO);
					} else {
						initTeach(1);
					}
					break;
				}
				break;
			case GameControl.GAME_STATUS3:
				break;
			case GameControl.GAME_STATUS4:
				break;
			}
			break;
		}
	}

	private void nextGame() {
		GameMainActivity.bffa.changeView(4);
	}

	private void gemKeyAction(TouchEvent te) {
		switch (keyActionIndex) {
		case -1:
			if (te.getEventTye() == MotionEvent.ACTION_DOWN) {
				gemKeyDown(te);
			}
			break;
		default:
			if (keyActionIndex == te.touchIndex) {
				switch (te.getEventTye()) {
				case MotionEvent.ACTION_MOVE: {
					if (tempoint != null) {
						// 点下去没有抬起来
						int xOff = (int) (te.point.x - tempoint.x);
						int yOff = (int) (te.point.y - tempoint.y);
						int xTemp = Math.abs(xOff);
						int yTemp = Math.abs(yOff);
						if ((xTemp > 20 || yTemp > 20)) {
							tempoint = null;
							keyActionIndex = -1;
							sxzy(xTemp, yTemp, xOff, yOff);
						}
					}
				}
					break;
				case MotionEvent.ACTION_UP:
					if (tempoint == null) {
						keyActionIndex = -1;
					}
					break;

				case MotionEvent.ACTION_DOWN:
					if (tempoint != null) {
						int yIndex = (int) ((te.point.x - startX + width / 2) / width);
						int xIndex = (int) ((te.point.y - startY + height / 2) / height);

						if (xIndex > -1 && xIndex < row && yIndex > -1
								&& yIndex < col) {
							if (goods[xIndex][yIndex][8] == 0) {
								if (goods[xIndex][yIndex][2] > 5) {// 炸弹
									tempoint = null;
									keyActionIndex = -1;
									runExplode(xIndex, yIndex);
								} else {
									if (ii == xIndex) {// 同一行
										int off = jj - yIndex;
										switch (off) {
										case -1:
										case 1:
											int xOff = (int) (te.point.x - tempoint.x);
											int yOff = (int) (te.point.y - tempoint.y);
											int xTemp = Math.abs(xOff);
											int yTemp = Math.abs(yOff);
											if ((xTemp > 10 || yTemp > 10)) {
												sxzy(xTemp, yTemp, xOff, yOff);
												tempoint = null;
											}
											break;
										default:// 重新算起点
											jj = yIndex;
											tempoint = new JPoint(te.point.x,
													te.point.y);
											break;
										}
									} else if (jj == yIndex) {// 同一列
										int off = ii - xIndex;
										switch (off) {
										case -1:
										case 1:
											int xOff = (int) (te.point.x - tempoint.x);
											int yOff = (int) (te.point.y - tempoint.y);
											int xTemp = Math.abs(xOff);
											int yTemp = Math.abs(yOff);
											if ((xTemp > 10 || yTemp > 10)) {
												sxzy(xTemp, yTemp, xOff, yOff);
												tempoint = null;
											}
											break;
										default:// 重新算起点
											ii = xIndex;
											tempoint = new JPoint(te.point.x,
													te.point.y);
											break;
										}
									} else {
										ii = xIndex;
										jj = yIndex;
										tempoint = new JPoint(te.point.x,
												te.point.y);
									}
								}
							}
						}
					}
					break;
				}
			}
			break;
		}
	}

	private void gemKeyDown(TouchEvent te) {
		if (canClick()) {// 连连看
			tempoint = null;
			int yIndex = (int) ((te.point.x - startX + width / 2) / width);
			int xIndex = (int) ((te.point.y - startY + height / 2) / height);

			if (xIndex > -1 && xIndex < row && yIndex > -1 && yIndex < col) {
				if (goods[xIndex][yIndex][8] == 0) {
					if (goods[xIndex][yIndex][2] > 5) {
						runExplode(xIndex, yIndex);
					} else {
						keyActionIndex = te.touchIndex;
						ii = xIndex;
						jj = yIndex;
						tempoint = new JPoint(te.point.x, te.point.y);
					}
				}
			}
		} else {
			tempoint = null;
		}
	}

	// 金币模式下---判断是否炸弹
	private void runExplode(int i, int j) {
		isBoom = true;
		isCanMove = false;
		if (goods[i][j][2] == 6) {// 消除行
			for (int z = 0; z < col; z++) {
				if (z == j) {
					goods[i][z][7] = 2;
				} else {
					goods[i][z][7] = 3;
				}
				goods[i][z][8] = 0;
				goods[i][z][9] = 0;
			}
			removed();
			return;
		} else if (goods[i][j][2] == 7) {// 消除列
			for (int z = 0; z < row; z++) {
				if (z == i) {
					goods[z][j][7] = 2;
				} else {
					goods[z][j][7] = 3;
				}
				goods[z][j][8] = 0;
				goods[z][j][9] = 0;
			}
			removed();//
			return;
		} else if (goods[i][j][2] == 8) {// 消除周围
			for (int v4 = i - 1; v4 <= i + 1; v4++) {
				for (int v8 = j - 1; v8 <= j + 1; v8++) {
					if (v4 >= 0 && v4 < row && v8 >= 0 && v8 < col) {
						if (v4 == i && v8 == j) {
							goods[v4][j][7] = 2;
						} else {
							goods[v4][v8][7] = 3;
						}
					}
				}
			}

			removed();//
			return;
		}
	}

	private void sxzy(int xTemp, int yTemp, int xOff, int yOff) {
		if (xTemp > yTemp) {// 左右
			if (xOff > 0) {// 向右
				int jTemp = jj + 1;
				if (jj < col - 1 && goods[ii][jTemp][8] == 0) {// 右
					ik = ii;
					jk = jTemp;
					temp1 = goods[ii][jTemp][2];
					goods[ii][jTemp][2] = goods[ii][jj][2];
					goods[ii][jj][2] = temp1;
					// 交换金色
					temp111 = goods[ii][jTemp][9];
					goods[ii][jTemp][9] = goods[ii][jj][9];
					goods[ii][jj][9] = temp111;

					// 移动数据
					goods[ii][jj][6] = width;
					goods[ii][jTemp][6] = -width;
					isRandom = true;
					direct = 4;
				}
			} else {
				int jTemp = jj - 1;
				if (jj >= 1 && goods[ii][jTemp][8] == 0) {// 左
					ik = ii;
					jk = jTemp;
					temp1 = goods[ii][jTemp][2];
					goods[ii][jTemp][2] = goods[ii][jj][2];
					goods[ii][jj][2] = temp1;
					// 交换金色
					temp111 = goods[ii][jTemp][9];
					goods[ii][jTemp][9] = goods[ii][jj][9];
					goods[ii][jj][9] = temp111;
					// 移动数据
					goods[ii][jj][5] = -width;
					goods[ii][jTemp][5] = width;
					isRandom = true;
					direct = 3;
				}
			}
		} else {// 上下走
			if (yOff > 0) {// 想下
				int iTemp = ii + 1;

				if (ii < row - 1 && goods[iTemp][jj][8] == 0) {// 下
					ik = iTemp;
					jk = jj;
					temp1 = goods[iTemp][jj][2];
					goods[iTemp][jj][2] = goods[ii][jj][2];
					goods[ii][jj][2] = temp1;
					// 交换金黄色
					temp111 = goods[iTemp][jj][9];
					goods[iTemp][jj][9] = goods[ii][jj][9];
					goods[ii][jj][9] = temp111;

					// 移动数据
					goods[ii][jj][4] = height;
					goods[iTemp][jj][4] = -height;
					isRandom = true;
					direct = 2;
				}
			} else {
				int iTemp = ii - 1;

				if (ii >= 1 && goods[iTemp][jj][8] == 0) {// 上
					ik = iTemp;
					jk = jj;
					temp1 = goods[iTemp][jj][2];
					goods[iTemp][jj][2] = goods[ii][jj][2];
					goods[ii][jj][2] = temp1;
					// 交换金黄色
					temp111 = goods[iTemp][jj][9];
					goods[iTemp][jj][9] = goods[ii][jj][9];
					goods[ii][jj][9] = temp111;

					// 移动数据
					goods[ii][jj][3] = -height;
					goods[iTemp][jj][3] = height;
					isRandom = true;
					direct = 1;
				}
			}
		}
	}

	private boolean isRandom = false;
	private boolean backRandom = false;
	private int speeg = 12;

	private void runRandom(int dir) {
		switch (dir) {
		case 1:// 上
			goods[ii][jj][3] += speeg;
			goods[ik][jk][3] -= speeg;
			if (goods[ii][jj][3] >= 0 && goods[ik][jk][3] <= 0) {
				goods[ii][jj][3] = 0;
				goods[ik][jk][3] = 0;
				isRandom = false;
				isClickChange = true;
				if (remove()) {
					removed();
				} else {
					backRandom = true;
					temp1 = goods[ii][jj][2];
					goods[ii][jj][2] = goods[ik][jk][2];
					goods[ik][jk][2] = temp1;

					temp111 = goods[ii][jj][9];
					goods[ii][jj][9] = goods[ik][jk][9];
					goods[ik][jk][9] = temp111;

					// 移动数据
					goods[ii][jj][3] = -height;
					goods[ik][jk][3] = +height;
				}
			}
			break;
		case 2:// 下
			goods[ii][jj][4] -= speeg;
			goods[ik][jk][4] += speeg;
			if (goods[ii][jj][4] <= 0 && goods[ik][jk][4] >= 0) {
				goods[ii][jj][4] = 0;
				goods[ik][jk][4] = 0;
				isRandom = false;
				isClickChange = true;
				if (remove()) {
					removed();
				} else {
					backRandom = true;
					temp1 = goods[ii][jj][2];
					goods[ii][jj][2] = goods[ik][jk][2];
					goods[ik][jk][2] = temp1;

					temp111 = goods[ii][jj][9];
					goods[ii][jj][9] = goods[ik][jk][9];
					goods[ik][jk][9] = temp111;
					// 移动数据
					goods[ii][jj][4] = height;
					goods[ik][jk][4] = -height;
				}
			}
			break;
		case 3:// 左
			goods[ii][jj][5] += speeg;
			goods[ik][jk][5] -= speeg;
			if (goods[ii][jj][5] >= 0 && goods[ik][jk][5] <= 0) {
				goods[ii][jj][5] = 0;
				goods[ik][jk][5] = 0;
				isRandom = false;
				isClickChange = true;
				if (remove()) {
					removed();
				} else {
					backRandom = true;
					temp1 = goods[ii][jj][2];
					goods[ii][jj][2] = goods[ik][jk][2];
					goods[ik][jk][2] = temp1;

					temp111 = goods[ii][jj][9];
					goods[ii][jj][9] = goods[ik][jk][9];
					goods[ik][jk][9] = temp111;
					// 移动数据
					goods[ii][jj][5] = -width;
					goods[ik][jk][5] = width;
				}
			}
			break;
		case 4:// 右

			goods[ii][jj][6] -= speeg;
			goods[ik][jk][6] += speeg;
			if (goods[ii][jj][6] <= 0 && goods[ik][jk][6] >= 0) {
				goods[ii][jj][6] = 0;
				goods[ik][jk][6] = 0;
				isRandom = false;
				isClickChange = true;
				if (remove()) {
					removed();
				} else {
					backRandom = true;
					temp1 = goods[ii][jj][2];
					goods[ii][jj][2] = goods[ik][jk][2];
					goods[ik][jk][2] = temp1;

					temp111 = goods[ii][jj][9];
					goods[ii][jj][9] = goods[ik][jk][9];
					goods[ik][jk][9] = temp111;

					// 移动数据
					goods[ii][jj][6] = width;
					goods[ik][jk][6] = -width;
				}
			}
			break;
		}

	}

	// 返回
	private void backRandom(int dir) {
		switch (dir) {
		case 1:// 上
			goods[ii][jj][3] += speeg;
			goods[ik][jk][3] -= speeg;
			if (goods[ii][jj][3] >= 0 && goods[ik][jk][3] <= 0) {
				goods[ii][jj][3] = 0;
				goods[ik][jk][3] = 0;
				backRandom = false;
			}
			break;
		case 2:// 下
			goods[ii][jj][4] -= speeg;
			goods[ik][jk][4] += speeg;
			if (goods[ii][jj][4] <= 0 && goods[ik][jk][4] >= 0) {
				goods[ii][jj][4] = 0;
				goods[ik][jk][4] = 0;
				backRandom = false;
			}
			break;
		case 3:// 左
			goods[ii][jj][5] += speeg;
			goods[ik][jk][5] -= speeg;
			if (goods[ii][jj][5] >= 0 && goods[ik][jk][5] <= 0) {
				goods[ii][jj][5] = 0;
				goods[ik][jk][5] = 0;
				backRandom = false;
			}
			break;
		case 4:// 右
			goods[ii][jj][6] -= speeg;
			goods[ik][jk][6] += speeg;
			if (goods[ii][jj][6] <= 0 && goods[ik][jk][6] >= 0) {
				goods[ii][jj][6] = 0;
				goods[ik][jk][6] = 0;
				backRandom = false;
			}
			break;
		}

	}

	private boolean isCanMove = false;

	private boolean canSlow = false;// 有消除才有下滑
	private int speedAdd;// 各种模式下的加速度

	private void runSlow() {
		for (int i = 0; i < goods.length; i++) {
			for (int j = 0; j < goods[i].length; j++) {
				if (goods[i][j][4] != 0) {
					goods[i][j][4] += speedAdd;
					if (goods[i][j][4] >= 0) {
						goods[i][j][4] = 0;
					}
				}
			}
		}
		speedAdd += 6;
	}

	private int showTime;
	private boolean isHaveShow;
	private int[][] showIndex = new int[4][2];
	private int[][] teachxy = new int[2][2];

	private long showTimeo;

	public void run() {
		switch (GameControl.gameStatus) {
		case GameControl.GAME_TECH://
		case GameControl.GAME_JS:// 脚本
			runLead();
			switch (GameControl.game_mode) {
			case 0:
				run_foe();
				foe.run();
				lead.run();
				break;
			}
			break;
		case GameControl.GAME_INTO:// 游戏
			
			if (canClick()) {
				if (!isteach && !canchange) {
					canchange = true;
					setGameStatus(GameControl.GAME_STATUS2);
				}
			}
			runLead();
			if (System.currentTimeMillis() - showTimeo > 100) {
				showTime++;
				showTimeo = System.currentTimeMillis();
			}
			switch (GameControl.game_mode) {
			case 0:
				run_foe();
				foe.run();
				lead.run();
				break;
			}
			runGameTime();
			break;
		case GameControl.GAME_STATUS1:
			break;
		case GameControl.GAME_STATUS2:
			break;
		case GameControl.GAME_STATUS3:
			break;
		case GameControl.GAME_STATUS4:
			break;
		}
	}

	private void runGameTime() {
		if (System.currentTimeMillis() - gameTimeo > 100) {
			gameTime++;
			switch (GameControl.game_mode) {
			case 1:
				gameLeaveTime -= timeRunSpeed;
				if (gameLeaveTime <= 0) {
					gameLeaveTime = 0;
					setGameStatus(GameControl.GAME_SUC);
				}
				gameProp = gameLeaveTime / gameTotalTime;

				gamePropTemp = 36 + 409 * gameProp;
				break;
			}
			gameTimeo = System.currentTimeMillis();
		}
	}

	// 实现颤抖
	private void runShake() {
		shakeIndex++;
		if (shakeIndex >= shake.length) {
			shakeIndex = 0;
			isShake = false;
		}
	}

	// 增加时间
	private void addTime(float tis) {
		gameLeaveTime += tis;
		if (gameLeaveTime > gameTotalTime) {
			gameLeaveTime = gameTotalTime;
		}
		gameProp = gameLeaveTime / gameTotalTime;
	}

	private void runLead() {
		if (isRandom && !crush) {// 手动移动--过程
			runRandom(direct);
		}
		if (backRandom && !crush) {// 移动--返回过程
			backRandom(direct);
		}

		if (isCanMove) {
			if (canSlow) {// 消除后下降过程
				if (remove()) {
					if (lead != null) {
						leaveProp++;
						if (leaveProp >= needProp) {
							leaveProp -= needProp;
							lead.setStats(8);
							DB.db.getLeadSave()[lead.actionDatIndex][3]++;
							DB.db.getLeadSave()[lead.actionDatIndex][5]++;
							lead.initFromDB();
							needProp = 10 + (lead.levelTemp - 1) * 100;
							DB.db.saveDB();
						}
						DB.db.getLeadSave()[lead.actionDatIndex][4] = leaveProp;
					}

					removed();
				} else {
					canSlow = false;
				}
			}
		} else {
			if (tiList.size() == 0 && !crush) {// 消除完毕之后---才下落
				runSlow();
			}
		}

		if (isCanMove) {// 没有可以消除的了，如果没有重新产生
			switch (GameControl.gameStatus) {
			case GameControl.GAME_INTO:
				if (System.currentTimeMillis() - lastCheckTime > 1000) {
					if (checkRefresh()) {
						initGameXY();
					}
					lastCheckTime = System.currentTimeMillis();
				}
				break;
			case GameControl.GAME_JS:
				if (System.currentTimeMillis() - lastCheckTime > 1000) {
					if (checkRefresh()) {
						initGameXY();
					} else {
						switch (GameControl.gameLayer) {
						case 0:
							setGameStatus(GameControl.GAME_TECH);
							break;
						}
					}
					lastCheckTime = System.currentTimeMillis();
				}
				break;
			case GameControl.GAME_TECH:
				break;
			}
		} else {
			lastCheckTime = System.currentTimeMillis();
		}

		if (gameMoney > gameMoney_) {
			int mTemp = (gameMoney - gameMoney_) / 20;
			if (mTemp <= 0) {
				mTemp = 1;
			}
			gameMoney_ += mTemp;
		} else if (gameMoney < gameMoney_) {
			int mTemp = (gameMoney_ - gameMoney) / 20;
			if (mTemp <= 0) {
				mTemp = 1;
			}
			gameMoney_ -= mTemp;
		}
	}

	// 增加金币
	public void addCoin(int value) {
		gameMoney += value;
	}

	private long lastCheckTime;
	private NameObj nameObj;

	// 检测称号是否达到---标准尊
	// 统计连击数量
	private void runNamed() {
		for (int i = 0; i < 4; i++) {
			int[] titile = DB.db.getTitle();
			if (titile[i] == 0) {
				if (doubleHit >= StrData.doubeHit[i]) {
					titile[i] = 1;
					DB.db.setTitle(titile);
					DB.db.saveDB();
					timeNamed = System.currentTimeMillis();
					nameObj = new NameObj(35 + i);
				}
				return;
			}
		}
	}

	private void run_foe() {
		if (isRandom_foe) {// 手动移动--过程
			runRandom_foe(dir_foe);
		}
		if (backRandom_foe) {// 移动--返回过程
			backRandom_foe(dir_foe);
		}
		if (canSlow_foe) {// 消除后下降过程
			if (getStatut_foe()) {
				if (remove_foe()) {
					removed_foe();
				} else {
					canSlow_foe = false;
				}
			} else {
				runSlow_foe();
			}
		}
		if (getStatut_foe()) {// 没有可以消除的了，如果没有重新产生
			if (checkRefresh_foe()) {
				initGameXY_foe();
			}
		}
		if (System.currentTimeMillis() - time > intervalTime && foe.canHandle2
				&& !canSlow_foe && getStatut_foe()) {
			handle();
			intervalTime = getTimeInterval();
		}
		runFury();// 主角怒气增加
		TimeDisappear();
		if (isShake) {
			runShake();
		}
	}

	private void handle() {
		// 敌人的----连连看
		foe.runSli();
		time = System.currentTimeMillis();
		int XY[] = new int[4];
		XY = getXY_foe();
		II = XY[0];
		JJ = XY[1];
		sxzy_foe(XY[2], XY[3]);
	}

	// 根据系统时间=======随机UI 判断时间
	private int getTimeInterval() {
		if (foe != null) {
			int temp = 10 - foe.level - M.getRandom(4);
			if (temp < 2) {
				temp = 2;
			}
			return temp * 1000;
		}
		return 5000;
	}

	// 双方被锁住---时间的消失
	private void TimeDisappear() {
		for (int i = 0; i < goods.length; i++) {
			for (int j = 0; j < goods[i].length; j++) {
				if (goods[i][j][8] != 0) {
					goods[i][j][8]--;
				}
			}
		}
		for (int i = 0; i < goodsFoe.length; i++) {
			for (int j = 0; j < goodsFoe[i].length; j++) {
				if (goodsFoe[i][j][8] != 0) {
					goodsFoe[i][j][8]--;
				}
			}
		}
	}

	// 让敌人指定位置被锁住///方块xy以及被所住的 时间
	public void makeLocked(int x, int y, int times) {
		tip22List.add(new LockObj(x, y, times));
	}

	private int lockTimeO = 150;
	public int lastRemoveNum, lastRemoveNumFoe;
	public int lastGoldRemoveNum, lastGoldRemoveNumFoe;

	private void leadEffect() {
		lastRemoveNum = 0;
		lastGoldRemoveNum = 0;
		// 主角效果
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (goods[i][j][7] == 1 || goods[i][j][7] == 2
						|| goods[i][j][7] == 3) {
					goods[i][j][8] = 0;// 消除后---无锁
					switch (goods[i][j][7]) {
					case 1:
						tiList.add(new DeadObj(goods[i][j][2], goods[i][j][0],
								goods[i][j][1]));
						break;
					case 2:// 爆炸
						tiList.add(new DeadObj(-1, goods[i][j][0],
								goods[i][j][1]));
						break;
					case 3:// 星星爆炸
						tiList.add(new DeadObj(-2, goods[i][j][0],
								goods[i][j][1]));
						break;
					}

					goods[i][j][7] = 1;
					// 消除的对象
					switch (goods[i][j][2]) {
					case 0:// 书本---增加灵气的
						int path01[][] = T.TM.getLine(goods[i][j][0],
								goods[i][j][1], goods[i][j][0] - 20,
								goods[i][j][1] + 20);
						int path1[][] = T.TM.getLine(goods[i][j][0] - 20,
								goods[i][j][1] + 20,
								LayerData.LeadCoinPlace[1][0],
								LayerData.LeadCoinPlace[1][1]);// 落线

						int path0101[][] = new int[path01.length + path1.length][2];
						for (int qi = 0; qi < path01.length; qi++) {
							path0101[qi][0] = path01[qi][0];
							path0101[qi][1] = path01[qi][1];
						}
						for (int v = path01.length; v < path0101.length; v++) {
							path0101[v][0] = path1[v - path01.length][0];
							path0101[v][1] = path1[v - path01.length][1];
						}

						starList.add(new Star(0, path0101, getDouByCol(i, j),
								this)); // 特效位置
						break;
					case 1:// 手铐
							// 随机n个敌人--并且锁住
						MuAuPlayer.muaup.aupStart(MUAU.t11);
						lead.setStats(4);// 普通技能
						int vv[] = new int[2];
						vv = locked(goods[i][j][9]);

						int xTemp = (int) (startX_foe
								+ (goodsFoe[vv[0]][vv[1]][0] - startX_foe)
								* 0.3f + 9);
						int yTemp = (int) (startY_foe
								+ (goodsFoe[vv[0]][vv[1]][1] - startY_foe)
								* 0.3f + 9);
						// 飞向敌人
						int ss[] = new int[] { goods[i][j][0], goods[i][j][1],
								xTemp, yTemp };

						starList.add(new Star(1, ss, getDouByCol(i, j), this));
						break;
					case 2:// 刀子
						MuAuPlayer.muaup.aupStart(MUAU.t5);
						int path7[][] = T.TM.getLine(goods[i][j][0],
								goods[i][j][1], LayerData.LeadCoinPlace[3][0],
								LayerData.LeadCoinPlace[3][1]);//
						starList.add(new Star(2, path7, getDouByCol(i, j), this));
						lastRemoveNum++;
						if (goods[i][j][9] != 0) {
							lastGoldRemoveNum++;
						}

						break;
					case 3:// 金币
						MuAuPlayer.muaup.aupStart(MUAU.t10);
						int path[][] = T.TM.getLine(goods[i][j][0],
								goods[i][j][1], LayerData.LeadCoinPlace[0][0],// 线路
								LayerData.LeadCoinPlace[0][1]);

						starList.add(new Star(3, path, getDouByCol(i, j), this));
						break;
					case 4:// 药水 增加药水消除效果图
						int path5[][] = T.TM.getLine(goods[i][j][0],
								goods[i][j][1], LayerData.LeadCoinPlace[2][0],
								LayerData.LeadCoinPlace[2][1]);//
						starList.add(new Star(4, path5, getDouByCol(i, j), this));
						break;
					}
					goods[i][j][9] = 0;// 消除后--无金色标志
				}
			}
		}
	}

	// 中毒
	public void poisoning() {
	}

	// 道具变成金黄色---属性加倍
	public int getDouByCol(int i, int j) {
		if (goods[i][j][9] == 1) {
			return 2;
		} else {
			return 1;
		}
	}

	// 实现锁住一个方块{锁住的个数}-------默认情况下是一个
	public int[] locked(int k) {
		boolean whil = true;
		int vv[] = new int[2];
		while (whil) {
			vv[0] = M.getRandom(0, row);
			vv[1] = M.getRandom(0, col);
			if (goodsFoe[vv[0]][vv[1]][8] > 0 || goodsFoe[vv[0]][vv[1]][2] > 5) {
			} else {
				switch (k) {
				case 1:
					goodsFoe[vv[0]][vv[1]][8] = 2 * lockTimeO;
					break;
				default:
					goodsFoe[vv[0]][vv[1]][8] = lockTimeO;
					break;
				}

				whil = false;
			}
		}
		return vv;
	}

	// 主角--敌人的暴怒-值 ----各自是否处于暴怒状态
	private int furyLead;// 暴怒值
	private boolean isFuryLead;// 是否处于暴怒状态
	private boolean isFuryFoe;
	private long leadTime1;// 暴怒记录时间
	private boolean isBreakLead;// 暴怒是否中断了
	private int cl222;
	private long timeLength = 5000;

	/** 主角暴怒---增加情况 */
	private void runFury() {
		if (!isBreakLead
				&& System.currentTimeMillis() - leadTime1 <= timeLength) {
			if (System.currentTimeMillis() % 50 <= 5) {
				furyLead++;
			}
			if (furyLead >= 474) {
				furyLead = 474;
			}
			cl222 = 0;
		} else {
			isBreakLead = true;// 是中断了
			if (cl222 == 2
					&& System.currentTimeMillis() - leadTime1 <= timeLength) {
				isBreakLead = false;
			}
			if (System.currentTimeMillis() % 50 <= 5) {
				furyLead--;
			}
			if (furyLead <= 0) {
				furyLead = 0;
			}
		}
		if (furyLead >= 464) {
			isFuryLead = true;
		} else {
			isFuryLead = false;
		}
	}

	/** 金币模式下--消除东西--增加时间 */
	private void removeToAddTime() {
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (goods[i][j][7] == 1 || goods[i][j][7] == 2
						|| goods[i][j][7] == 3) {
					int aa = (int) gamePropTemp;
					switch (goods[i][j][7]) {
					case 1:
						tiList.add(new DeadObj(goods[i][j][2], goods[i][j][0],
								goods[i][j][1]));
						break;
					case 2:// 爆炸
						tiList.add(new DeadObj(-1, goods[i][j][0],
								goods[i][j][1]));
						break;
					case 3:// 星星爆炸
						tiList.add(new DeadObj(-2, goods[i][j][0],
								goods[i][j][1]));
						break;
					}

					goods[i][j][7] = 1;
					int[][] paht = null;
					switch (goods[i][j][2]) {
					case 0:
					case 1:
					case 2:
					case 4:
					case 5:
						paht = T.TM.getLine(goods[i][j][0], goods[i][j][1], aa,
								136);
						break;
					case 3:
						paht = T.TM.getLine(goods[i][j][0], goods[i][j][1],
								205, 30);
						break;
					}

					switch (goods[i][j][2]) {
					case 0:// 书本
						addTime(getDouByCol(i, j) * 0.8f);
						starList.add(new Star(2, paht, 0, this));
						break;
					case 1:// 手铐
						MuAuPlayer.muaup.aupStart(MUAU.t10);
						addTime(-getDouByCol(i, j) * 0.4f);
						starList.add(new Star(2, paht, 0, this));
						break;
					case 2:// 刀子
						MuAuPlayer.muaup.aupStart(MUAU.t5);
						addTime(-getDouByCol(i, j) * 0.4f);
						starList.add(new Star(2, paht, 0, this));
						break;
					case 3:// 金币
						MuAuPlayer.muaup.aupStart(MUAU.t11);
						timeRunSpeed += 0.001;
						gameMoney += (getDouByCol(i, j) * LayerData.goldN[(int) ((System
								.currentTimeMillis() - timeLog) / 200000)]);
						starList.add(new Star(3, paht, 0, this));
						break;

					case 4:// 药水
						addTime(getDouByCol(i, j) * 0.8f);
						starList.add(new Star(2, paht, 0, this));
						break;
					case 5:// 炸弹
						break;
					}
					goods[i][j][9] = 0;// /消除后无金色标志
				}
			}
		}
	}

	// 达到称号要求显示行管信息
	private long timeNamed = 0;

	private void removed() {
		showTime = 0;
		if (isteach) {
			isteach = false;
		}
		isHaveShow = false;

		if (System.currentTimeMillis() - doubleTime < 3000) {
			doubleHit++;
			eeeList.add(new Eff(doubleHit, this));
			runNamed();
		} else {
			doubleHit = 0;
		}
		if (doubleHit > doubleHiTotal) {
			// 改变最高连击
			doubleHiTotal = doubleHit;
		}
		doubleTime = System.currentTimeMillis();
		//
		switch (GameControl.game_mode) {
		case 0:
			leadEffect();
			cl222++;
			leadTime1 = System.currentTimeMillis();
			break;
		case 1:
			removeToAddTime();
			break;
		}

		// =========================================填补=========================================

		// 下移
		for (int i = 0; i < col; i++) {
			for (int j = row - 2; j >= 0; j--) {
				// 最下 第二排开始------下方是否有空位置
				if (goods[j][i][7] != 1) {
					int num = 0;
					for (int jt = j + 1; jt < row; jt++) {
						if (goods[jt][i][7] == 1) {
							num++;
						}
					}
					if (num > 0) {
						canSlow = true;
						speedAdd = 0;
						goods[j + num][i][2] = goods[j][i][2];// 上面的图片移动下面去
						goods[j + num][i][9] = goods[j][i][9];
						goods[j][i][9] = 0;
						goods[j + num][i][4] = -height * num;
					}
				}
			}
		}
		// 上面产生新的
		for (int i = 0; i < col; i++) {
			int num = 0;
			for (int j = 0; j < row; j++) {
				if (goods[j][i][7] == 1) {
					canSlow = true;
					speedAdd = 0;
					num++;
					// 金币模式下---随机产生炸弹
					int iIndex = num - 1;

					switch (GameControl.game_mode) {
					case 1:
						productGem(i, iIndex);
						break;
					default:
						switch (foe.actionDatIndex) {
						case 5:
							switch (M.getRandom(10)) {
							case 0:
								switch (lead.actionDatIndex) {
								case 0:
									goods[iIndex][i][2] = 2;
									break;
								default:
									goods[iIndex][i][2] = 0;
									break;
								}
								break;
							default:
								productGem(i, iIndex);
								break;
							}
							break;
						default:
							productGem(i, iIndex);
							break;
						}
						break;
					}
					goods[iIndex][i][4] = -height * num;
				}
			}
			int tp_num = num;
			// 从新定义下滑效果
			for (int j = 0; j < row; j++) {
				if (goods[j][i][7] == 1) {
					goods[tp_num - num][i][4] -= height * (num - 1);
					num--;
				}
			}
		}

		// 指定消除的点---清除
		for (int t = 0; t < row; t++) {
			for (int v = 0; v < col; v++) {
				goods[t][v][7] = 0;
			}
		}
	}

	private void productGem(int i, int iIndex) {
		switch (M.getRandom(10)) {
		case 2:
			if (isBoom) {
				goods[iIndex][i][2] = 6 + M.getRandom(3);// 消除周围8个
				isBoom = false;
			} else {
				goods[iIndex][i][2] = M.getRandom(6);
			}
			break;
		case 3:
			goods[iIndex][i][2] = M.getRandom(2, 4);
			break;
		default:
			if (isBoom && M.getRandom(10) == 2) {
				goods[iIndex][i][2] = 6 + M.getRandom(3);// 消除周围8个
				isBoom = false;
			} else {
				goods[iIndex][i][2] = M.getRandom(6);
			}
			break;
		}
	}

	/** true表示可以出炸弹 */
	private boolean isBoom;

	public void paint(Canvas g, Paint p) {
		switch (GameControl.gameStatus) {
		case GameControl.GAME_JS:// 脚本
			paintGameIng(g, p);
			ShareCtrl.sc.paintB(g, p, 200);
			if (isCanMove) {
				setGameStatus(GameControl.GAME_INTO);
			}
			break;
		case GameControl.GAME_TECH:// 教程
			paintGameIng(g, p);
			ShareCtrl.sc.paintB(g, p, 200);
			if (isCanMove) {
				setGameStatus(GameControl.GAME_INTO);
			}
			break;
		case GameControl.GAME_INTO:// 游戏
			paintGameIng(g, p);
			paintTitle(g, p);
			break;
		case GameControl.GAME_SUC:// 过关
			paintGameSuc(g, p);
			break;
		case GameControl.GAME_SUCALL:// 过关
			paintGameSucAll(g, p);
			break;
		case GameControl.GAME_ERR:// 失败
			paintFauil(g, p);
			break;
		case GameControl.GAME_PAUSE:// 暂停
			paintPause(g, p);
			break;
		case GameControl.GAME_STATUS1:
			if (layindex < showindex.length) {
				paintStory(g, p);
			} else {
				setGameStatus(GameControl.GAME_JS);
			}
			break;
		case GameControl.GAME_STATUS2:
			paintTeach(g, p);
			break;
		case GameControl.GAME_STATUS3:
			break;
		case GameControl.GAME_STATUS4:
			break;
		}
		ShareCtrl.sc.paintTransitionUI(g, p);
	}

	private String story[][] = {
			{ "哈，快开始吧，我都等不及了 ", "感受到魔法的力量了吗？ ", "鸡肉味的嘎嘣脆。 " },
			{ "升级的感觉真棒！ ", "我现在感觉自己充满了力量。 ", "你想来试一试吗？ " },
			{ "感觉好强大的样子哦！ ", "不过我坚信自己可以获胜！ ", "无论如何都要对自己有信心嘛。 " },
			{ "你看起来都等不及的样子。 ", "准备好了，我来喽。 " },
			{ "我一定会是冠军的！ ", "这是我所坚持的信念！ ", "你好，我这个人不太会说话，如果有什么冒犯的地方，来打我啊！ " },
			{ "烤鸡翅膀，我最爱吃。 ", "你说什么唱错了？ ", "哎，不要打脸。 " },
			{ "猴赛雷啊！？！ ", "两个炸弹送你上西天。 " },
			{ "终于胜利在望了。 ", "好激动啊。 ", "努力就会有回报的。 " },
			{ "像我这种人见人爱，花见花开…… ", "你吐了，是什么意思？ ", "你要说清楚哦，不然我打你哦。 " },
			{ "噢耶，我激动得都说不出来话了。 ", "卡忙北鼻切克闹。 ", "让我们来一发吧。 " } };

	private final int w_fix = 480, h_fix = 800;
	private final int storyarea[] = { 400, 200 };
	private int storyindex;
	private final int storypoint[] = { 400, 700 };
	private int showindex[] = { 0, 3, 6, 9, 12, 15, 18, 21, 24,
			GameControl.MAX_LAYER - 1 };
	private int layindex;
	private int index;
	private int movespeed = 10;

	private void initStory() {
		if (GameControl.gameLayer == 0) {
			canchange = false;
			isteach = true;
			showTime = 60;
		}
		storyindex = 0;
		line = T.TM.getLine(400, 250, 240, 400);
		initStory(0);
	}

	/** 0放大,1继续放大,2缩小 **/
	private int storystatus;
	private int zoomspeed;
	private final int zoomadd = 3;
	private int storyw, storyh;
	private String st;
	private int stindex;

	private void initStory(int status) {
		storystatus = status;
		switch (storystatus) {
		case 0:
			storyw = 0;
			storyh = 0;
			zoomspeed = 0;
			index = 0;
			stindex = 0;
			break;
		case 1:
			zoomspeed = 20;
			break;
		case 2:
			zoomspeed = 20;
			break;
		}
	}

	private int storyx, storyy;
	private int line[][];

	private void runStory() {
		switch (storystatus) {
		case 0:
			index += movespeed;
			if (index >= line.length) {
				index = line.length - 1;
			}
			storyx = line[index][0];
			storyy = line[index][1];
			if (storyw < storyarea[0]) {
				zoomspeed += zoomadd;
				storyw += zoomspeed;
				if (storyw >= storyarea[0]) {
					storyw = storyarea[0];
					storyx = w_fix / 2;
					storyy = h_fix / 2;
				}
				storyh = storyarea[1] * storyw / storyarea[0];
			}
			if (stindex < story[layindex][storyindex].length() - 1) {
				stindex++;
				st = story[layindex][storyindex].substring(0, stindex);
			}
			break;
		case 1:
			storyw += zoomspeed;
			if (storyw >= storyarea[0] + 100) {
				initStory(2);
			}
			storyh = storyarea[1] * storyw / storyarea[0];
			break;
		case 2:
			storyw -= zoomspeed;
			if (storyw <= 0) {
				storyw = 0;
				initStory(0);
			}
			storyh = storyarea[1] * storyw / storyarea[0];
			break;
		}
	}

	private void paintStory(Canvas g, Paint p) {
		paintGameIng(g, p);
		ShareCtrl.sc.paintB(g, p, 100);
		runStory();
		ShareCtrl.sc.paintOther(g, p, storyx, storyy, storyw, storyh, 0);
		// ShareCtrl.sc.paintRect(g, p, w_fix / 2, h_fix / 2, storyarea[0],
		// storyarea[1]);
		if (storystatus == 0 && storyw >= storyarea[0]) {
			p.setARGB(255, 160, 230, 255);
			paintString(g, p, st, w_fix / 2 - storyarea[0] / 2 + 48, h_fix / 2
					- storyarea[1] / 2 + 30, storyarea[0] - 100);
		}
		skip.paintX(g, p);
	}

	private int teachindex;
	private int pointxy[] = new int[2];
	private int teachline[][] = null;
	private int teachin;
	private final int teachspeed = 2;

	private void initThum() {
		teachline = T.TM.getLine(teachxy[0][0], teachxy[0][1], teachxy[1][0],
				teachxy[1][1]);
		pointxy[0] = teachline[teachin][0];
		pointxy[1] = teachline[teachin][1];
	}

	private final int teacharea[][] = { { 250, 300 }, { 220, 300 } };
	private int teachw, teachh;
	private int teachx, teachy;
	private int leadx = 50, leady = 50, leadtype;
	/** 0方块,1带箭头的 */
	private int teachstatus;
	private int teachcount;

	private String teachinfo[] = { "干的漂亮!你还可以尝试完成3个以上宝石的消除,效果惊人哦.计时开始喽. ",
			"我是魔法小布丁，我如果和我的另外两个或更多的小伙伴在一起便可回复一定技能值 ",
			"我是冰雪小布丁，我如果和我的另外两个或更多的小伙伴在一起便可将对方的数个布丁冻住 ",
			"我是战斗小布丁，我如果和我的另外两个或更多的小伙伴在一起便可对敌人造成一定伤害 ",
			"我是金钱小布丁，我如果和我的另外两个或更多的小伙伴在一起便可获得一定金币 ",
			"我是生命小布丁，我如果和我的另外两个或更多的小伙伴在一起便可回复自身一定生命 ",
			"这是讨厌的砖块，它会阻挡住你消除别的布丁，它如果和另外两个或更多的砖块在一起你就可以清除掉这个障碍啦 ",
			"啊！火药弹，这可是能帮助你走向成功的神器，点击它可以消除一行布丁哦 ",
			"啊！毒气弹，这可是能帮助你走向成功的神器，点击它可以消除一列布丁哦 ",
			"啊！无敌炸弹，这可是能帮助你走向成功的神器，点击它可以消除临近的布丁哦 " };

	private void initTeach() {
	}

	private void initTeach(int status) {
		stindex = 0;
		teachstatus = status;
		switch (status) {
		case 0:
			teachcount = 0;
			break;
		default:
			initList(teachcount - 1);
			teachw = 0;
			teachh = 0;
			storystatus = 0;
			zoomspeed = 0;
			if (leadx < w_fix / 2) {
				teachx = leadx + teacharea[1][0] / 2;
				teachindex = 8;
			} else {
				teachx = leadx - teacharea[1][0] / 2;
				teachindex = 9;
			}
			if (leady < h_fix / 2) {
				teachy = leady + teacharea[1][1] / 2;
				if (teachindex == 8) {
					teachindex = 3;
				} else {
					teachindex = 0;
				}
			} else {
				teachy = leady - teacharea[1][1] / 2;
				if (teachindex == 8) {
					teachindex = 2;
				} else {
					teachindex = 1;
				}
			}
			break;
		}
		switch (teachcount) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		case 8:
			break;
		}
	}

	private void runTeach() {
		switch (storystatus) {
		case 0:
			if (teachw < teacharea[teachstatus][0]) {
				zoomspeed += zoomadd;
				teachw += zoomspeed;
				if (teachw >= teacharea[teachstatus][0]) {
					teachw = teacharea[teachstatus][0];
				}
				teachh = teacharea[teachstatus][1] * teachw
						/ teacharea[teachstatus][0];
			}
			if (stindex < teachinfo[teachcount].length() - 1) {
				stindex++;
				st = teachinfo[teachcount].substring(0, stindex);
			}
			break;
		case 1:
			teachw += zoomspeed;
			if (teachw >= teacharea[teachstatus][0] + 100) {
				initStory(2);
			}
			teachh = teacharea[teachstatus][1] * teachw
					/ teacharea[teachstatus][0];
			break;
		case 2:
			teachw -= zoomspeed;
			if (teachw <= 0) {
				teachw = 0;
				initStory(0);
			}
			teachh = teacharea[teachstatus][1] * teachw
					/ teacharea[teachstatus][0];
			break;
		}
	}

	private ArrayList<int[]> list = new ArrayList<int[]>();
	private int count = 2;
	private long teachtime;
	private int teachdelay = 500;

	private void initList(int t) {
		list.clear();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (goods[i][j][2] == t) {
					list.add(goods[i][j]);
				}
			}
		}
		if (list.size() <= 0) {
			setGameStatus(GameControl.GAME_INTO);
			return;
		}
		int index = M.getRandom(list.size());
		int info[] = list.get(index);
		leadx = info[0];
		leady = info[1];
		leadtype = info[2];
	}

	private void paintTeach(Canvas g, Paint p) {
		paintGameIng(g, p);
		ShareCtrl.sc.paintB(g, p, 100);
		runTeach();
		switch (teachstatus) {
		case 0:
			ShareCtrl.sc.paintRect(g, p, w_fix / 2, h_fix / 2, teachw, teachh);
			// ShareCtrl.sc.paintRect(g, p, w_fix / 2, h_fix / 2, storyarea[0],
			// storyarea[1]);
			if (storystatus == 0 && teachw >= teacharea[0][0]) {
				p.setARGB(255, 160, 230, 255);
				paintString(g, p, st, w_fix / 2 - teacharea[0][0] / 2 + 45,
						h_fix / 2 - teacharea[0][1] / 2 + 30,
						teacharea[0][0] - 100);
			}
			break;
		default:
			ShareCtrl.sc.paintOther(g, p, teachx, teachy, teachw, teachh,
					teachindex);
			if (storystatus == 0 && teachw >= teacharea[1][0]) {
				p.setARGB(255, 160, 230, 255);
				paintString(g, p, st, teachx - teacharea[1][0] / 2 + 10, teachy
						- teacharea[1][1] / 2 + 30, teacharea[1][0] - 40);
			}
			if (count < 0) {
				T.TP.paintImage(g, p,
						Pic.imageSrcs(GameData.ordGem[teachcount - 1]), leadx,
						leady, T.ANCHOR_CHV);
			}
			if (System.currentTimeMillis() - teachtime >= teachdelay) {
				teachtime = System.currentTimeMillis();
				count = -count;
			}
			break;
		}
	}

	// ****************************************************************************************************
	private boolean isteach = false;
	private boolean canchange = true;

	private void paintTitle(Canvas g, Paint p) {
		if (nameObj != null) {
			nameObj.paint(g, p);
			switch (nameObj.status) {
			case 3:
				nameObj = null;
				break;
			}
		}
	}

	private void paintPause(Canvas g, Paint p) {
		g.drawBitmap(Pic.imageSrcs(54), 0, 0, p);
		pauseButns[0].paintX(g, p);
		pauseButns[1].paintX(g, p);
		pauseButns[2].paintX(g, p);
		soundButn.paint(g, p);
	}

	private void paintGameIng(Canvas g, Paint p) {
		paintBG(g, p);
		switch (GameControl.game_mode) {
		case 0:
			paintEnemyGem(g, p);
			if (lead.actionStatus == 3 || lead.actionStatus == 5
					|| lead.actionStatus == 6 || lead.actionStatus == 7) {
				foe.paint22(g, p);
				lead.paintX(g, p);
			} else {
				lead.paintX(g, p);
				foe.paint22(g, p);
			}

			A.a.paintFrame(g, p, Pic.imageSrcs(48), 187, 220, 0, 2);
			g.save();
			g.clipRect(187, 220, 187 + 42 * foe.lifeTemp / foe.lifeAll, 232);
			A.a.paintFrame(g, p, Pic.imageSrcs(48), 187, 220, 1, 2);
			g.restore();
			A.a.paintFrame(g, p, Pic.imageSrcs(624), 0, 792, 0, 2);
			g.save();
			g.clipRect(0, 792, 480 * leaveProp / needProp, 800);
			A.a.paintFrame(g, p, Pic.imageSrcs(624), 0, 792, 1, 2);
			g.restore();
			break;
		}
		paintEff(g, p);
		paintPrompt(g, p);
	}

	private int leaveProp = 0;
	private int needProp = 0;
	int moveY = -200;

	// 达到称号要求后弹出提示
	private void paintPrompt(Canvas g, Paint p) {
		if (System.currentTimeMillis() - timeNamed < 2000) {
		} else {
			moveY = -200;
		}
	}

	/** 消除效果 */
	private void paintEff(Canvas g, Paint p) {
		for (Iterator<DeadObj> iterator = tiList.iterator(); iterator.hasNext();) {
			DeadObj type = iterator.next();
			switch (type.status) {
			case 2:
				iterator.remove();
				break;
			default:
				type.paint(g, p);
				break;
			}
		}

		for (Iterator<Star> iterator = starList.iterator(); iterator.hasNext();) {
			Star type = iterator.next();
			if (type.isDead) {
				iterator.remove();
			} else {
				type.paintStar(g, p);
			}
		}

		for (Iterator<ExpClick> iterator = clickList.iterator(); iterator
				.hasNext();) {
			ExpClick type = iterator.next();
			if (type.isDead) {
				iterator.remove();
			} else {
				type.paintE(g, p);
			}
		}

		for (Iterator<Eff> iterator = eeeList.iterator(); iterator.hasNext();) {
			Eff type = iterator.next();
			if (type.isDEad) {
				iterator.remove();
			} else {
				type.paintEEE(g, p);
			}
		}
	}

	/*** 存放连击特效的 */
	List<Eff> eeeList = new ArrayList<Eff>();

	/** 存放敌人被锁住特效 */
	public List<LockObj> tip22List = new ArrayList<LockObj>();

	// 判断是否继续特效
	public void comboLogic() {
		boolean gugu = false;
		int assPic = -1;
		for (int i = 0; i < GameData.Exx22.length; i++) {
			if (doubleHit >= GameData.Exx22[i][0]) {
				assPic = GameData.Exx22[i][1];
				gugu = true;
			}
		}
		if (gugu) {
			eeeList.add(new Eff(assPic, 0));
		}
	}

	private void paintGameSucAll(Canvas g, Paint p) {// 胜利
		paintResult(g, p);

		g.drawBitmap(Pic.imageSrcs(24), 21, 84, p);
		pauseButns[2].paintX(g, p);
		restartButn.paintX(g, p);
	}

	private final float[] scale = { 0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f,
			0.4f, 0.5f, 0.6f, 0.7f, 0.9f, 1.1f, 1, 0.95f, 1, 1.05f, 1 };

	private int scaleIndex;

	private int sucStatus;

	/** [0]status0表示匀速运动,1表示晃动,2表示正常，[1]运动的X，或者表示index */
	private int[][] sucXoff = new int[4][2];

	private final int[] xoffValue = { -10, -15, -18, -15, -10, 0, 8, 12, 15,
			12, 8, 0, -8, -12, -15, -12, -8, 0, 5, 8, 10, 8, 5, 0, -3, -5, -3,
			0, 2, 3, 2 };

	private void initSucData() {
		for (int i = 0; i < sucXoff.length; i++) {
			sucXoff[i][0] = 0;
			sucXoff[i][1] = 355 + 100 * i;
		}
		xSped = 5;
		for (int i = 0; i < numStatus.length; i++) {
			numStatus[i][0] = 0;
			numStatus[i][1] = -i * 2;
		}
		isShine = false;
	}

	private int xSped;

	private final float[] numScale = { 0.5f, 0.8f, 1.2f, 1.25f, 1.28f, 1.25f,
			1.2f, 1f, 0.9f, 0.85f, 0.82f, 0.85f, 0.9f, 1f, 1.1f, 1.15f, 1.18f,
			1.15f, 1.1f, 1, 0.95f, 0.92f, 0.9f, 0.92f, 0.95f, 1, 1.05f, 1.08f,
			1.05f };

	/** [0]status:0未出现，1变化中，2正常[1]index */
	private int[][] numStatus = new int[5][2];
	private long numTimeo;
	private boolean isShine;

	/**** 过关统计 ***/
	private void paintGameSuc(Canvas g, Paint p) {// 胜利
		g.drawBitmap(bitmap, 0, 0, p);
		g.drawBitmap(Pic.imageSrcs(23), 50, 175, p);

		g.save();
		g.clipRect(64, 268, 414, 487);

		boolean isMove = true;
		for (int i = 0; i < 4; i++) {
			switch (sucXoff[i][0]) {
			case 0:
				A.a.paintFrame(g, p, Pic.imageSrcs(25), 81 + sucXoff[i][1],
						277 + 54 * i, i, 4);

				sucXoff[i][1] -= xSped;
				if (xSped < 40) {
					xSped += 5;
				}
				if (sucXoff[i][1] <= 0) {
					sucXoff[i][0] = 1;
					sucXoff[i][1] = 0;
				}
				isMove = false;
				break;
			case 1:
				A.a.paintFrame(g, p, Pic.imageSrcs(25),
						81 + xoffValue[sucXoff[i][1]], 277 + 54 * i, i, 4);

				sucXoff[i][1]++;
				if (sucXoff[i][1] >= xoffValue.length) {
					sucXoff[i][0] = 2;
					sucXoff[i][1] = 0;
				}
				break;
			default:
				A.a.paintFrame(g, p, Pic.imageSrcs(25), 81, 277 + 54 * i, i, 4);
				break;
			}
		}
		g.restore();

		if (isMove && !isShine) {
			isShine = true;
		}
		if (isShine) {
			switch (numStatus[0][0]) {
			case 1:
				g.save();
				String strTemp = doubleHiTotal + "";
				g.scale(numScale[numStatus[0][1]], numScale[numStatus[0][1]],
						300 + 14 + 28 * (strTemp.length() - 1), 299);
				A.a.paintNum(g, p, Pic.imageSrcs(26), doubleHiTotal, 300, 275);
				g.restore();
				break;
			case 2:
				A.a.paintNum(g, p, Pic.imageSrcs(26), doubleHiTotal, 300, 275);
				break;
			}

			switch (numStatus[1][0]) {
			case 1:
				g.save();
				String strTemp = rewardNum + "";
				g.scale(numScale[numStatus[1][1]], numScale[numStatus[1][1]],
						300 + 14 + 28 * (strTemp.length() - 1), 353);
				A.a.paintNum(g, p, Pic.imageSrcs(27), rewardNum, 300, 335);
				g.restore();
				break;

			case 2:
				A.a.paintNum(g, p, Pic.imageSrcs(27), rewardNum, 300, 335);
				break;
			}
			switch (numStatus[2][0]) {
			case 1:
				g.save();
				String strTemp = gameTime_m + "";
				g.scale(numScale[numStatus[2][1]], numScale[numStatus[2][1]],
						313 - 14 - 28 * (strTemp.length() - 1), 409);
				T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_m, 313,
						395, 0, T.ANCHOR_RU);
				g.restore();
				break;
			case 2:
				T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_m, 313,
						395, 0, T.ANCHOR_RU);

				break;
			}
			switch (numStatus[3][0]) {
			case 1:
				g.save();
				String strTemp = gameTime_s + "";
				g.scale(numScale[numStatus[3][1]], numScale[numStatus[3][1]],
						346 + 14 + 28 * (strTemp.length() - 1), 409);
				T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_s, 346,
						395, 0, T.ANCHOR_LU);
				g.restore();
				break;
			case 2:
				T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_s, 346,
						395, 0, T.ANCHOR_LU);
				break;
			}

			switch (numStatus[4][0]) {
			case 1:
				g.save();
				String strTemp = oldGold + "";
				g.scale(numScale[numStatus[4][1]], numScale[numStatus[4][1]],
						300 + 14 + 28 * (strTemp.length() - 1), 432);
				A.a.paintNum(g, p, Pic.imageSrcs(27), oldGold, 300, 448);
				g.restore();
				break;
			case 2:
				A.a.paintNum(g, p, Pic.imageSrcs(27), oldGold, 300, 448);
				break;
			}

			if (System.currentTimeMillis() - numTimeo > 50) {
				for (int i = 0; i < numStatus.length; i++) {
					switch (numStatus[i][0]) {
					case 0:
						numStatus[i][1]++;
						if (numStatus[i][1] >= 0) {
							numStatus[i][1] = 0;
							numStatus[i][0] = 1;
						}
						break;
					case 1:
						numStatus[i][1]++;
						if (numStatus[i][1] >= numScale.length) {
							numStatus[i][1] = 0;
							numStatus[i][0] = 2;
						}
						break;
					}
				}
				numTimeo = System.currentTimeMillis();
			}
		}

		switch (sucStatus) {
		case 0:
			g.save();
			g.scale(scale[scaleIndex], scale[scaleIndex], 250, 165);
			g.drawBitmap(Pic.imageSrcs(24), 21, 84, p);
			g.restore();

			if (System.currentTimeMillis() - timell > 50) {
				scaleIndex++;
				if (scaleIndex >= scale.length) {
					scaleIndex = 0;
					sucStatus = 1;
				}
				timell = System.currentTimeMillis();
			}
			break;
		default:
			g.drawBitmap(Pic.imageSrcs(24), 21, 84, p);
			break;
		}

		switch (GameControl.game_mode) {
		case GameControl.GAME_MODE_0:
			pauseButns[2].paintX(g, p);
			restartButn.paintX(g, p);
			nextButn.paintX(g, p);
			break;
		default:
			nextButn.paintX(g, p);
			pauseButns[2].paintX(g, p);
			break;
		}
	}

	private int yOff;
	private int yOffIndex;
	private int[] yOffValue = { -100, -95, -90, -85, -80, -70, -60, -45, -30,
			-10, 0, 10, 15, 18, 15, 10, 0, -5, -8, -5, 0, 5, 8, 5, 0, -3, -5,
			-3, 0, 2, 3, 2, 1, 0 };
	private int yOffStatus;

	private void paintFauil(Canvas g, Paint p) {// 胜利
		paintResult(g, p);

		switch (yOffStatus) {
		case 0:
			g.drawBitmap(Pic.imageSrcs(53), 66, 110 + yOff, p);
			if (System.currentTimeMillis() - timell > 50) {
				yOffIndex++;
				if (yOffIndex >= yOffValue.length) {
					yOffIndex = 0;
					yOffStatus = 1;
				}
				yOff = yOffValue[yOffIndex];
				timell = System.currentTimeMillis();
			}
			break;
		default:
			g.drawBitmap(Pic.imageSrcs(53), 66, 110, p);
			break;
		}
		switch (GameControl.game_mode) {
		case 0:
			restartButn.paintX(g, p);
			break;
		default:
			nextButn.paintX(g, p);
			break;
		}
		pauseButns[2].paintX(g, p);
	}

	private void paintResult(Canvas g, Paint p) {
		g.drawBitmap(bitmap, 0, 0, p);
		g.drawBitmap(Pic.imageSrcs(23), 50, 175, p);

		for (int i = 0; i < 4; i++) {
			A.a.paintFrame(g, p, Pic.imageSrcs(25), 81, 277 + 54 * i, i, 4);
		}

		A.a.paintNum(g, p, Pic.imageSrcs(26), doubleHiTotal, 300, 275);
		A.a.paintNum(g, p, Pic.imageSrcs(27), rewardNum, 300, 335);
		T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_m, 313, 395, 0,
				T.ANCHOR_RU);
		T.TP.paintNumberX(g, p, Pic.imageSrcs(27), gameTime_s, 346, 395, 0,
				T.ANCHOR_LU);
		A.a.paintNum(g, p, Pic.imageSrcs(27), oldGold, 300, 448);
	}

	// 连连看图框抖动
	public void initUpAndDoen() {
		initBitmap();
		crushIndex = 0;
		crush = true;
	}

	// 敌人使用手链--图框左右晃动
	private int around[] = new int[] { -2, -5, -10, -3, 6, 1, -7, 2, 7, 0 };
	private int aroundIndex = 0;
	boolean isAround;

	public void initAround() {
		initBitmap();
		aroundIndex = 0;
		isAround = true;
	}

	private int bgImageIndex;

	// 背景---相关
	private void paintBG(Canvas g, Paint p) {
		switch (GameControl.game_mode) {
		case 0:// 普通模式
			g.drawBitmap(Pic.imageSrcs(LayerData.BG[bgImageIndex]),
					15 + shake[shakeIndex][0], 30 + shake[shakeIndex][1], p);
			break;
		}

		g.drawBitmap(Pic.imageSrcs(116), 0, 0, p);// 背景

		switch (GameControl.game_mode) {
		case 0:
			for (PropButn butn : butns) {
				butn.paint(g, p);
			}
			mogicButn.setHavaNum(lead.reiki);
			mogicButn.paint(g, p);

			g.drawBitmap(Pic.imageSrcs(140), 14, 324, p);
			g.drawBitmap(Pic.imageSrcs(136), 33, 329, p);
			g.save();
			g.clipRect(36, 331, 36 + 409 * lead.lifeTemp / lead.lifeAll, 342);
			g.drawBitmap(Pic.imageSrcs(137), 36, 331, p);
			g.restore();
			if (lead.lifeTemp <= 0) {
				MuAuPlayer.muaup.aupStart(MUAU.t8);
				tp00 = 1;
				setGameStatus(GameControl.GAME_ERR);
			}

			break;
		case 1:// 时间模式==金币模式
			g.drawBitmap(Pic.imageSrcs(134), 16 + shake[shakeIndex][0],
					28 + shake[shakeIndex][1], p);
			break;
		}

		if (crush) {// 实现受伤抖动效果
			g.save();
			g.clipRect(0, 330, 480, 800);
			g.scale(crushNum[crushIndex][0], crushNum[crushIndex][1], 240, 400);
			g.drawBitmap(bitmap, 0, 0, p);
			g.restore();
			crushIndex++;
			if (crushIndex >= crushNum.length) {
				crushIndex = 0;
				crush = false;
			}
		} else if (isAround) {// 敌人手链实现左右晃动
			g.save();
			g.clipRect(0, 330, 480, 800);
			g.drawBitmap(bitmap, around[aroundIndex], 0, p);
			g.restore();
			aroundIndex++;
			if (aroundIndex >= around.length) {
				aroundIndex = 0;
				isAround = false;
			}
		} else {
			switch (GameControl.game_mode) {
			case 0:
				g.drawBitmap(Pic.imageSrcs(141), 9, 339, p);
				paintFury_Lead(g, p);
				break;
			case 1:
				g.drawBitmap(Pic.imageSrcs(143), 9, 219, p);
				g.drawBitmap(Pic.imageSrcs(135), 9, 193, p);//
				g.drawBitmap(Pic.imageSrcs(136), 33, 218, p);// 219
				g.save();
				g.clipRect(36, 221, gamePropTemp, 232);
				g.drawBitmap(Pic.imageSrcs(138), 36, 221, p);
				g.restore();
				break;
			}
			paintPlayerGem(g, p);
			paintThum(g, p);
		}
		pauseButn.paintX(g, p);

		g.drawBitmap(Pic.imageSrcs(83), 187, 14, p);
		A.a.paintNum(g, p, Pic.imageSrcs(84), gameMoney_, 223, 28);
	}

	private void eff(Canvas g, Paint p) { // 敌人方片被锁住特效
		for (Iterator<LockObj> iterator = tip22List.iterator(); iterator
				.hasNext();) {
			LockObj type = iterator.next();
			if (type.isDead) {
				iterator.remove();
			} else {
				type.paintX(g, p);
			}
		}
	}

	private boolean isShow;
	private long isShowTimeo;

	private void paintPlayerGem(Canvas g, Paint p) {
		g.save();
		if (GameControl.game_mode == 1) {
			g.clipRect(20, 230, 460, 795);
		} else {
			g.clipRect(0, 340, 480, 800);
		}

		isCanMove = true;
		int xTemp = 0;
		int yTemp = 0;
		int imageTemp = 0;

		if (System.currentTimeMillis() - isShowTimeo > 200) {
			isShow = !isShow;
			isShowTimeo = System.currentTimeMillis();
		}

		// 主角---连连看
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (goods[i][j][3] != 0 || goods[i][j][4] != 0
						|| goods[i][j][5] != 0 || goods[i][j][6] != 0) {
					isCanMove = false;
				}
				if (isHaveShow) {
					boolean isBreak = false;
					for (int j2 = 0; j2 < showIndex.length; j2++) {
						if (showIndex[j2][0] == i && showIndex[j2][1] == j) {
							if (isShow) {
								isBreak = true;
							}
						}
					}
					if (isBreak) {
						continue;
					}
				}

				switch (goods[i][j][9]) {
				case 1:// 发光
					imageTemp = GameData.bbbcoled[goods[i][j][2]];
					break;
				default:
					switch (GameControl.game_mode) {
					case GameControl.GAME_MODE_0:
						switch (goods[i][j][8]) {
						case 0:// 普通
							imageTemp = GameData.ordGem[goods[i][j][2]];
							break;
						default:// 灰色
							imageTemp = GameData.bbblocked[goods[i][j][2]];
							break;
						}
						break;
					default:// 普通
						imageTemp = GameData.ordGem[goods[i][j][2]];
						break;
					}
					break;
				}

				xTemp = goods[i][j][0] + goods[i][j][5] + goods[i][j][6];
				yTemp = goods[i][j][1] + goods[i][j][3] + goods[i][j][4];

				T.TP.paintImage(g, p, Pic.imageSrcs(imageTemp), xTemp, yTemp,
						T.ANCHOR_CHV);
				if (tempoint != null) {
					if (ii == i && jj == j) {
						selectScale += scaleSpeed;
						if (selectScale > 1.2f) {
							scaleSpeed = -0.05f;
						} else if (selectScale < 0.8f) {
							scaleSpeed = 0.05f;
						}
						g.save();
						g.scale(selectScale, selectScale, xTemp, yTemp);
						T.TP.paintImage(g, p, Pic.imageSrcs(634), xTemp, yTemp,
								T.ANCHOR_CHV);
						g.restore();
					}
				}
			}
		}
		g.restore();
	}

	private void paintThum(Canvas g, Paint p) {
		if (isteach) {
			if (isHaveShow && teachline != null) {
				teachin += teachspeed;
				if (teachin >= teachline.length) {
					teachin = 0;
				}
				pointxy[0] = teachline[teachin][0];
				pointxy[1] = teachline[teachin][1];
				T.TP.paintImage(g, p, Pic.imageSrcs(625), pointxy[0],
						pointxy[1], T.ANCHOR_LU);
			}
		}
	}

	private float selectScale = 1;
	private float scaleSpeed = 0.05f;

	// 敌人=----连连看
	private void paintEnemyGem(Canvas g, Paint p) {
		g.drawBitmap(Pic.imageSrcs(142), 12, 17, p);
		g.save();
		g.clipRect(12, 17, 142, 147);
		g.scale(0.3f, 0.3f, startX_foe, startY_foe);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				g.drawBitmap(Pic.imageSrcs(GameData.ordGem[goodsFoe[i][j][2]]),
						goodsFoe[i][j][0] + goodsFoe[i][j][5]
								+ goodsFoe[i][j][6], goodsFoe[i][j][1]
								+ goodsFoe[i][j][3] + goodsFoe[i][j][4], p);
			}
		}
		g.restore();
		eff(g, p);
	}

	private int[] furyImage = { 39, 40 };
	private int furyIndex;
	private long furyTimeo;

	/** 主角暴怒 */
	private void paintFury_Lead(Canvas g, Paint p) {
		if (furyLead > 5) {
			g.save();
			int yTemp = 800 - furyLead;// 800-474 =326
			g.clipRect(0, yTemp, 480, 800);
			g.drawBitmap(Pic.imageSrcs(furyImage[furyIndex]), 0, yTemp, p);
			if (System.currentTimeMillis() - furyTimeo > 100) {
				furyIndex = 1 - furyIndex;
				furyTimeo = System.currentTimeMillis();
			}
			g.restore();
		}
	}

	private int rewardNum;

	/**
	 * 载入资源
	 */
	public void loadingData() {
		initDataB();
		initStatus();
		initDataA();
		loadingImage();
	}

	private int row;
	private int col;
	/** 玩家 */
	public Player lead;
	public Enemy foe;

	/** 0x,1y,2类型,3向上移动距离,4向下移动距离,5向左移动距离,6向右移动距离,为7则消除，8锁住时间,9--4个以上的变色 */
	public int goods[][][] = new int[7][7][10];
	// 敌人方
	/** 0x,1y,2类型,3向上移动距离,4向下移动距离,5向左移动距离,6向右移动距离,为7则消除，8锁住时间 */
	public int goodsFoe[][][] = new int[6][6][9];
	private float timeRunSpeed;// 金币模式下时间走动速度
	public List<DeadObj> tiList = new ArrayList<DeadObj>();// 存放消除效果
	public List<Star> starList = new ArrayList<Star>();// 存放消除后的星星
	public List<ExpClick> clickList = new ArrayList<ExpClick>();// 存放点击星星

	private int doubleHit;// 连击次数
	private int doubleHiTotal;
	private long doubleTime;
	long timeLog;// 统计时间的
	long timell;
	boolean isClickChange;// 是否由点击产生的变色

	private int gameTime, gameTime_m, gameTime_s;
	private float gameLeaveTime, gameTotalTime, gameProp, gamePropTemp;
	private long gameTimeo;

	private void initDataA() {
		eeeList.clear();
		clear();
		tiList.clear();
		starList.clear();
		clickList.clear();

		gameTime = 0;
		speedAdd = 0;
		isClickChange = false;
		isCanMove = false;
		isHaveShow = false;
		timeLog = System.currentTimeMillis();
		intervalTime = getTimeInterval();
		whichFoe = GameControl.gameLayer % 4 + 2;
		switch (GameData.primaryPosition[GameControl.gameLayer][2]) {
		case 1:
			GameControl.game_mode = 1;
			break;
		default:
			GameControl.game_mode = 0;
			break;
		case 2:
			GameControl.game_mode = 0;
			whichFoe = 5;
			break;
		}
		switch (GameControl.game_mode) {
		case 0:

			initCommom();
			lead = new Player(GameControl.selectIndex, 420, 314, this);
			foe = new Enemy(whichFoe, 208, 208, this);
			leaveProp = DB.db.getLeadSave()[lead.actionDatIndex][4];
			needProp = 10 + (lead.levelTemp - 1) * 100;
			break;
		case 1:
			timeRunSpeed = 0.1f;
			startX = 60;
			startY = 266;
			width = 60;
			height = 60;
			gameLeaveTime = gameTotalTime = 60;
			gameProp = 1;
			row = 9;
			col = 7;
			goods = new int[row][col][10];
			initGameXY();
			break;
		}
		gameTimeo = System.currentTimeMillis();

		// whichFoe = 4;
	}

	int whichFoe;

	/** 加载普通--挑战模式 下---共同 */
	private void initCommom() {
		startX = 60;
		startY = 385;
		width = 60;
		height = 60;
		row = col = 7;
		goods = new int[row][col][10];
		goodsFoe = new int[row][col][9];
		initGameXY();
		initGameXY_foe();
		initFury();
		time = System.currentTimeMillis() + 3000;// 敌人每隔一段时间--进行一次消除--但=游戏开始前三秒不能动作
	}

	private void initFury() {
		furyLead = 0;
		isFuryLead = false;
		isFuryFoe = false;
		leadTime1 = -99990;
		isBreakLead = true;// 第一次点击无暴怒增加
		cl222 = 0;
	}

	private void initBitmap() {
		T.TP.paintImage(canvas, paint, GameDirector.systemBitmap, 0, 0,
				T.ANCHOR_LU);
	}

	private Canvas canvas;
	private Paint paint;
	private Bitmap bitmap;

	/***
	 * 主----信息信息
	 */
	private int startX = 50;
	private int startY = 375;
	/** 宝石的宽度 */
	private int width = 75, height = 75;

	/** 主角刷新 */
	private void initGameXY() {
		boolean nb = true;
		isBoom = true;
		speedAdd = 0;
		canSlow = true;
		crush = false;// 初始--图框不抖动
		isClickChange = false;
		isCanMove = false;
		while (nb) {// 开始产生的图片状态----上下落下
			for (int i = 0; i < goods.length; i++) {
				for (int j = 0; j < goods[i].length; j++) {
					goods[i][j][0] = startX + j * (width);
					goods[i][j][1] = startY + i * (height);
					goods[i][j][2] = M.getRandom(5);
					goods[i][j][4] = -height * row - (goods.length - 1 - i)
							* 30;
					goods[i][j][7] = 0;// 不消除
					goods[i][j][8] = 0;// 未被锁住
					goods[i][j][9] = 0;// 原始颜色
				}
			}
			if (initremove()) {
			} else {
				nb = false;
			}
		}
	}

	/***
	 * 敌--信息
	 */

	/***
	 * 主----信息信息
	 */
	private int startX_foe = 20;
	private int startY_foe = 27;
	/** 宝石的宽度 */
	private int width_foe = 30, height_foe = 24;

	private void initGameXY_foe() {
		boolean nn = true;
		canSlow_foe = true;
		while (nn) {
			for (int i = 0; i < goodsFoe.length; i++) {
				for (int j = 0; j < goodsFoe[i].length; j++) {
					goodsFoe[i][j][0] = startX_foe + j * (width_foe);
					goodsFoe[i][j][1] = startY_foe + i * (height_foe);
					goodsFoe[i][j][2] = M.getRandom(0, 5);
					goodsFoe[i][j][4] = -height_foe * (i + 1);
				}
			}
			if (initremove_foe()) {
			} else {
				nn = false;
			}
		}
	}

	private void initDataB() {
		timeNamed = 0;// 显示称号维持时间
		crush = false;// 初始--图框不抖动
		shakeIndex = 0;
		doubleHiTotal = doubleHit = 0;// 连击次数
		doubleTime = System.currentTimeMillis();// 5秒之内算是连击
		speeg = 12;
		lockTimeO = 150;
		timeLength = 5000;
		startX_foe = 16;
		startY_foe = 19;
		width_foe = 60;
		height_foe = 60;
		oldGold = gameMoney = gameMoney_ = DB.db.getMoney();
	}

	private void reGame() {
		initDataB();
		initStatus();
		initDataA();
	}

	/**
	 * 初始化状态
	 */
	private void initStatus() {
		setGameMenu2Status(GameControl.GAME_MENU2_0);
		setGamePauseStatus(GameControl.GAME_PAUSE_0);
		// setGameStatus(GS.GAME_SUC);
		// setGameStatus(GameControl.GAME_JS);
		for (int i = 0; i < showindex.length; i++) {
			if (showindex[i] == GameControl.gameLayer) {
				layindex = i;
				setGameStatus(GameControl.GAME_STATUS1);
				return;
			}
		}
		setGameStatus(GameControl.GAME_JS);
	}

	/**
	 * 设置游戏模式
	 * 
	 * @param game_mode
	 */
	public void setGame_mode(int game_mode) {
		switch (game_mode) {
		case GameControl.GAME_MODE_0:

			break;
		case GameControl.GAME_MODE_1:

			break;
		}
		setGameMenu2Status(GameControl.GAME_MENU2_0);
		setGamePauseStatus(GameControl.GAME_PAUSE_0);
		setGameStatus(GameControl.GAME_INTO);
		GameControl.game_mode = game_mode;
	}

	public void setGameStatus(int gameStatus) {
		switch (gameStatus) {
		case GameControl.GAME_STORY:// 背景介绍
			break;
		case GameControl.GAME_TECH:// 新手教程
			break;
		case GameControl.GAME_LAYER:// 关卡介绍
			break;
		case GameControl.GAME_JS:// 脚本
			break;
		case GameControl.GAME_INTO:// 游戏
			tempoint = null;
			keyActionIndex = -1;
			gameTimeo = System.currentTimeMillis();
			break;
		case GameControl.GAME_SHOP:// 商店
			break;
		case GameControl.GAME_SUC:// 过关
			initSUC();
			switch (GameControl.game_mode) {
			case GameControl.GAME_MODE_0:// 第一种玩法
				DB.db.setScore(DB.db.getScore() + foe.level * 10);
				break;
			}
			DB.db.setMoney(gameMoney + rewardNum);
			if (tp00 == 0) {
				GameControl.gameLayer++;
				if (GameControl.gameLayer >= GameControl.MAX_LAYER) {
					GameControl.gameLayer = GameControl.MAX_LAYER;
				}
				if (GameControl.gameLayer > DB.db.getLayer()) {
					DB.db.setLayer(GameControl.gameLayer);
				}
			}
			/*if(GameControl.gameLayer == 2){
				SmsInfo.sendSms(SmsInfo.SMS_REG);
			}*/
			//DB.db.setIsLevelCancelLocked(1);
			DB.db.saveDB();
			break;
		case GameControl.GAME_ERR:// 失败
			initFauil();
			ShareCtrl.sc.playTransitionUI();
			break;
		case GameControl.GAME_SUCALL:// 通关
			// 将对象 的金币付给主角
			initSUCAll();
			break;
		case GameControl.GAME_PAUSE:// 暂停
			initBitmap();
			ShareCtrl.sc.paintB(canvas, paint, 200);
			pauseButns[2].setPosition(132, 532, 19, 19);
			break;
		case GameControl.GAME_STATUS1:
			initStory();
			break;
		case GameControl.GAME_STATUS2:
			initTeach(0);
			break;
		case GameControl.GAME_STATUS3:
			break;
		case GameControl.GAME_STATUS4:
			break;
		}
		GameControl.gameStatus = gameStatus;

	}

	public int gameMoney, gameMoney_;

	// 过关---处理
	private void initSUC() {
		// 将对象 的金币付给主角
		oldGold = gameMoney - oldGold;
		if (oldGold < 0) {
			oldGold = 0;
		}
		rewardNum = (int) (10 * (1200 - gameTime) / 600f) + doubleHiTotal;
		if (rewardNum < 0) {
			rewardNum = 0;
		}
		// 统计时间
		timell = System.currentTimeMillis() - timeLog;
		if (nextButn == null) {
			nextButn = new ImagesButton(57, 90, 84, 115, 1, 2);
			nextButn.setImageType(5);
		}
		switch (GameControl.game_mode) {
		case 0:
			if (restartButn == null) {
				restartButn = new ImagesButton(57, 90, 84, 115, 0, 2);
				restartButn.setImageType(5);
			}

			restartButn.setPosition(90, 522, 19, 16);
			pauseButns[2].setPosition(205, 511, 19, 19);
			nextButn.setPosition(319, 522, 19, 16);
			break;
		default:
			nextButn.setPosition(319, 532, 19, 16);
			pauseButns[2].setPosition(90, 532, 19, 19);
			break;
		}

		initBitmap();
		ShareCtrl.sc.paintB(canvas, paint, 200);
		gameTime_m = gameTime / 60;
		gameTime_s = gameTime - gameTime_m * 60;
		sucStatus = scaleIndex = 0;
		initSucData();
	}

	private void initSUCAll() {
		// 将对象 的金币付给主角
		oldGold = gameMoney - oldGold;
		if (oldGold < 0) {
			oldGold = 0;
		}
		rewardNum = (int) (10 * (1200 - gameTime) / 600f) + doubleHiTotal;
		if (rewardNum < 0) {
			rewardNum = 0;
		}
		DB.db.setMoney(gameMoney + rewardNum);
		DB.db.saveDB();
		// 统计时间
		timell = System.currentTimeMillis() - timeLog;

		if (restartButn == null) {
			restartButn = new ImagesButton(57, 90, 84, 115, 0, 2);
			restartButn.setImageType(5);
		}

		restartButn.setPosition(90, 522, 19, 16);
		pauseButns[2].setPosition(319, 511, 19, 19);

		initBitmap();
		ShareCtrl.sc.paintB(canvas, paint, 200);
		gameTime_m = gameTime / 60;
		gameTime_s = gameTime - gameTime_m * 60;
	}

	private void initFauil() {
		switch (GameControl.game_mode) {
		case 0:
			if (restartButn == null) {
				restartButn = new ImagesButton(57, 90, 84, 115, 0, 2);
				restartButn.setImageType(5);
			}
			restartButn.setPosition(319, 522, 19, 16);
			pauseButns[2].setPosition(90, 522, 19, 19);
			break;
		default:
			if (nextButn == null) {
				nextButn = new ImagesButton(57, 90, 84, 115, 1, 2);
				nextButn.setImageType(5);
			}
			nextButn.setPosition(319, 522, 19, 16);
			pauseButns[2].setPosition(90, 522, 19, 19);
			break;
		}
		gameTime_m = gameTime / 60;
		gameTime_s = gameTime - gameTime_m * 60;
		yOffIndex = yOffStatus = 0;
		yOff = yOffValue[0];
		initBitmap();
		ShareCtrl.sc.paintB(canvas, paint, 200);
	}

	/**
	 * 游戏暂停状态统一处理
	 * 
	 * @param gamePase0
	 */
	private void setGamePauseStatus(int gamePauseStatus) {
		switch (gamePauseStatus) {
		case GameControl.GAME_PAUSE_1:// 继续游戏

			break;
		case GameControl.GAME_PAUSE_2:// 游戏设置

			break;
		case GameControl.GAME_PAUSE_3:// 游戏帮助

			break;
		case GameControl.GAME_PAUSE_4:// 退出游戏

			break;
		}
		GameControl.gamePauseStatus = gamePauseStatus;
	}

	/**
	 * 二级菜单统一处理
	 * 
	 * @param gameMenu2Status
	 */
	private void setGameMenu2Status(int gameMenu2Status) {
		switch (gameMenu2Status) {
		case GameControl.GAME_MENU2_1:// 是否回 主菜单

			break;
		default:
			break;
		}
		GameControl.gameMenu2Status = gameMenu2Status;
	}

	private ArrayList<Integer> imageAsPNG = new ArrayList<Integer>();

	/**
	 * 释放资源
	 */
	public void disingData() {
		bitmap = null;
		canvas = null;
		paint = null;

		UICtrl.uic.disingData();
		SpxCtrl.sc.disingData();
		Pic.disImage(imageAsPNG);
	}

	private void clear() {
		if (lead != null) {
			lead.clear();
			tip22List.clear();
		}
	}

	private int oldGold;

	/**
	 * 载入图片
	 */
	private void loadingImage() {
		bitmap = Bitmap.createBitmap(480, 800, Config.RGB_565);
		canvas = new Canvas(bitmap);
		paint = new Paint();
		imageAsPNG.clear();
		// 动态图片载入===
		switch (GameControl.game_mode) {
		case 0:// 对战模式---血条图片
		{
			bgImageIndex = M.getRandom(LayerData.BG.length);
			SpxCtrl.sc.loadingData(new int[] { GameControl.selectIndex,
					whichFoe }, 6);

			int[] imageNumsPNG = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 48, 51,
					53, 54, 57, 83, 84, 103, 115, 116, 117, 118, 119, 120, 121,
					122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 136,
					137, 139, 140, 141, 142, 150, 151, 152, 153, 154, 155, 156,
					157, 158, 441, 442, 443, 444, 445, 446, 450, 451, 452, 453,
					454, 455, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467,
					468, 469, 470, 471, 575, 576, 577, 578, 579, 624, 625, 634 };

			for (int intTemp : imageNumsPNG) {
				imageAsPNG.add(intTemp);
			}
			// Pic.loadImage(5, new int[] { 117, 118, 119, 120, 121, 122 });
			// Pic.loadLazyImage(LayerData.BG[bgImageIndex]);
			imageAsPNG.add(LayerData.BG[bgImageIndex]);
			Pic.loadImage(imageAsPNG);
		}
			break;
		case 1: {
			int[] imageNumsPNG = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
					12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26,
					27, 28, 29, 30, 31, 33, 34, 35, 36, 37, 38, 39, 40, 51, 53,
					54, 57, 83, 84, 103, 115, 116, 134, 135, 136, 138, 139,
					140, 141, 143, 441, 150, 151, 152, 153, 154, 155, 156, 157,
					158, 442, 443, 444, 445, 446, 450, 451, 452, 453, 454, 455,
					458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469,
					470, 471, 575, 576, 577, 578, 579, 634 };
			for (int intTemp : imageNumsPNG) {
				imageAsPNG.add(intTemp);
			}
			// Pic.loadImage(6);
			Pic.loadImage(imageAsPNG);
		}
			break;
		case 2: {
		}
			break;
		}
		initButn();
	}

	/** 暴击，血精灵，刷新 */
	private PropButn[] butns;

	/** 0继续游戏；1重新开始，2主界面 */
	private ImagesButton[] pauseButns = new ImagesButton[3];

	private MogicButn mogicButn;

	private ImagesButton pauseButn;
	private ImagesButton nextButn;
	private ImagesButton restartButn;

	private SoundButn soundButn;
	private ImagesButton skip;

	private void initButn() {
		switch (GameControl.game_mode) {
		case GameControl.GAME_MODE_0:// 普通模式
			int[] numbss = DB.db.getNumbss();
			butns = new PropButn[3];
			butns[0] = new PropButn(142, 289, 124, numbss[1]);
			butns[1] = new PropButn(230, 289, 126, numbss[2]);
			butns[2] = new PropButn(317, 289, 125, numbss[3]);
			mogicButn = new MogicButn(6, 206);
			break;
		}

		pauseButn = new ImagesButton(139, 67, 67);
		pauseButn.setPosition(408, 2, 0, 0);

		pauseButns[0] = new ImagesButton(20, 338, 126, 21, 0, 3);// 继续游戏
		pauseButns[0].setImageType(5);
		pauseButns[0].setPosition(79, 155, 52, 34);

		pauseButns[1] = new ImagesButton(20, 338, 126, 21, 1, 3);// 重新开始
		pauseButns[1].setImageType(5);
		pauseButns[1].setPosition(79, 337, 52, 34);

		pauseButns[2] = new ImagesButton(57, 90, 84, 22);
		pauseButns[2].setPosition(132, 532, 19, 19);
		soundButn = new SoundButn(284, 532);
		skip = new ImagesButton(57, 90, 84, 115, 1, 2);
		skip.setImageType(5);
		skip.setPosition(385, 710, 19, 16);
	}

	@Override
	public void onPause() {
		super.onPause();
		pauseGame();
	}

	/**
	 * 游戏暂停
	 */
	public void pauseGame() {
		switch (GameControl.gameStatus) {
		case GameControl.GAME_INTO:
			setGameStatus(GameControl.GAME_PAUSE);
			// tp00 = 0;
			// setGameStatus(GameControl.GAME_SUC);
		}
	}

	//

	private boolean remove() {
		boolean yes = false;
		boolean four = false;
		int end = 0;// 终点位置[0]代表移到终点的是iijj[1]代表移到终点的是ikjk{初始点击的是iijj}

		for (int i = 0; i < goods.length; i++) {// 行--消除
			for (int j = 0; j < goods[i].length - 2; j++) {
				if (goods[i][j][2] == goods[i][j + 1][2]
						&& goods[i][j][2] == goods[i][j + 2][2]) {
					goods[i][j][7] = 1;
					goods[i][j + 1][7] = 1;
					goods[i][j + 2][7] = 1;
					yes = true;
					if (isClickChange && j < goods[i].length - 3// 已成四个了
							&& goods[i][j][2] == goods[i][j + 3][2]) {
						if (j < goods[i].length - 4
								&& goods[i][j][2] == goods[i][j + 4][2]) {
							// 最多成5个 ----不做讨论
						} else {
							if (i == ik
									&& (jk == j || jk == j + 1 || jk == j + 2 || jk == j + 3)) {
								if (goods[ik][jk][9] == 1) {
									goods[ik][jk][9] = 0;
								} else {
									goods[ik][jk][9] = 1;
									end = 1;
									four = true;
								}
							} else if (i == ii
									&& (jj == j || jj == j + 1 || jj == j + 2 || jj == j + 3)) {
								if (goods[ii][jj][9] == 1) {
									goods[ii][jj][9] = 0;
								} else {
									goods[ii][jj][9] = 1;
									end = 0;
									four = true;
								}
							}
						}
					}
				}
			}
		}
		for (int i = 0; i < col; i++) {// 列--消除
			for (int j = 0; j < row - 2; j++) {
				if (goods[j][i][2] == goods[j + 1][i][2]
						&& goods[j][i][2] == goods[j + 2][i][2]) {
					goods[j][i][7] = 1;
					goods[j + 1][i][7] = 1;
					goods[j + 2][i][7] = 1;
					yes = true;
					if (isClickChange && j < row - 3// 已成四个了
							&& goods[j][i][2] == goods[j + 3][i][2]) {
						if (j < row - 4 && goods[j][i][2] == goods[j + 4][i][2]) {
						} else {
							if (i == jk
									&& (ik == j || ik == j + 1 || ik == j + 2 || ik == j + 3)) {
								if (goods[ik][jk][9] == 1) {
									// 已经变色的直接消除
									goods[ik][jk][9] = 0;
								} else {
									goods[ik][jk][9] = 1;
									end = 1;
									four = true;
								}
							} else if (i == jj
									&& (ii == j || ii == j + 1 || ii == j + 2 || ii == j + 3)) {
								if (goods[ii][jj][9] == 1) {
									goods[ii][jj][9] = 0;
								} else {
									goods[ii][jj][9] = 1;
									end = 0;
									four = true;
								}
							}
						}
					}
				}
			}
		}
		// 金黄色 的--不消除
		if (isClickChange) {
			if (four) {
				switch (end) {
				case 0:
					if (goods[ii][jj][9] == 1) {
						goods[ii][jj][7] = 0;
					}
					break;
				case 1:
					if (goods[ik][jk][9] == 1) {
						goods[ik][jk][7] = 0;
					}
					break;

				}
			} else {
				// 有了3个或者4个同时消除---的点击效果
				for (int m = 0; m < row; m++) {
					for (int n = 0; n < col; n++) {
						if (goods[m][n][7] == 1) {
							if (m == ii && n == jj) {
								addClickExp(m, n);
							} else if (m == ik && n == jk) {
								addClickExp(m, n);
							}
						}
					}
				}
			}
		}

		isClickChange = false;
		return yes;
	}

	// 增加点击效果
	private void addClickExp(int h, int l) {
		for (int i = 0; i < 9; i++) {
			clickList.add(new ExpClick(new float[] { goods[h][l][0],
					goods[h][l][1], M.getRandom(4, 15) }, M.getRandom(0, 360),
					M.getRandom(5, 20), M.getRandom(1, 6) / 10f, M.getRandom(0,
							10) * 5));
		}
	}

	private boolean initremove() {
		boolean yes = false;
		for (int i = 0; i < goods.length; i++) {// 行--消除
			for (int j = 0; j < goods[i].length - 2; j++) {
				if (goods[i][j][2] == goods[i][j + 1][2]
						&& goods[i][j][2] == goods[i][j + 2][2]) {
					goods[i][j][7] = 1;
					goods[i][j + 1][7] = 1;
					goods[i][j + 2][7] = 1;
					yes = true;
				}
			}
		}
		for (int i = 0; i < col; i++) {// 列--消除
			for (int j = 0; j < row - 2; j++) {
				if (goods[j][i][2] == goods[j + 1][i][2]
						&& goods[j][i][2] == goods[j + 2][i][2]) {
					goods[j][i][7] = 1;
					goods[j + 1][i][7] = 1;
					goods[j + 2][i][7] = 1;
					yes = true;
				}
			}
		}
		// // 清除
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				goods[i][j][7] = 0;
			}
		}
		return yes;
	}

	private int iTemp, jTemp, iTemp1, jTemp1;

	/** 判断有没有可以消除的了，如果没有重新产生 */
	private boolean checkRefresh() {
		boolean isRefresh = false;
		for (int i = 0; i < goods.length; i++) {
			int i_j = -1;
			for (int j = 0; j < goods[i].length; j++) {
				if (i - 1 >= 0) {
					iTemp = i - 1;
					jTemp = j;

					iTemp1 = i;
					jTemp1 = j;

					i_j = goods[i - 1][j][2];
					goods[i - 1][j][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
					if (canDelete(goods)) {
						isRefresh = true;
						if (isteach) {
							teachxy[0][0] = goods[i - 1][j][0];
							teachxy[0][1] = goods[i - 1][j][1];
							teachxy[1][0] = goods[i][j][0];
							teachxy[1][1] = goods[i][j][1];
							initThum();
						}
					}
					i_j = goods[i - 1][j][2];
					goods[i - 1][j][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
				}

				if (j - 1 >= 0) {
					iTemp = i;
					jTemp = j - 1;

					iTemp1 = i;
					jTemp1 = j;

					i_j = goods[i][j - 1][2];
					goods[i][j - 1][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
					if (canDelete(goods)) {
						isRefresh = true;
						if (isteach) {
							teachxy[0][0] = goods[i][j - 1][0];
							teachxy[0][1] = goods[i][j - 1][1];
							teachxy[1][0] = goods[i][j][0];
							teachxy[1][1] = goods[i][j][1];
							initThum();
						}
					}
					i_j = goods[i][j - 1][2];
					goods[i][j - 1][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
				}

				if (i + 1 < goods.length) {
					iTemp = i + 1;
					jTemp = j;

					iTemp1 = i;
					jTemp1 = j;

					i_j = goods[i + 1][j][2];
					goods[i + 1][j][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
					if (canDelete(goods)) {
						isRefresh = true;
						if (isteach) {
							teachxy[0][0] = goods[i + 1][j][0];
							teachxy[0][1] = goods[i + 1][j][1];
							teachxy[1][0] = goods[i][j][0];
							teachxy[1][1] = goods[i][j][1];
							initThum();
						}
					}
					i_j = goods[i + 1][j][2];
					goods[i + 1][j][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
				}

				if (j + 1 < goods[i].length) {
					iTemp = i;
					jTemp = j + 1;

					iTemp1 = i;
					jTemp1 = j;

					i_j = goods[i][j + 1][2];
					goods[i][j + 1][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
					if (canDelete(goods)) {
						isRefresh = true;
						if (isteach) {
							teachxy[0][0] = goods[i][j + 1][0];
							teachxy[0][1] = goods[i][j + 1][1];
							teachxy[1][0] = goods[i][j][0];
							teachxy[1][1] = goods[i][j][1];
							initThum();
						}
					}
					i_j = goods[i][j + 1][2];
					goods[i][j + 1][2] = goods[i][j][2];
					goods[i][j][2] = i_j;
				}

				if (isRefresh) {
					return false;
				}
			}
		}
		return true;
	}

	/** 移动之后是否满足删除条件 */
	private boolean canDelete(int a[][][]) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (j > 0 && j < a[i].length - 1
						&& a[i][j][2] == a[i][j - 1][2]
						&& a[i][j][2] == a[i][j + 1][2]) {
					if (showTime > 50) {
						showIndex[0][0] = i;
						showIndex[0][1] = j;
						showIndex[1][0] = i;
						showIndex[1][1] = j - 1;
						showIndex[2][0] = i;
						showIndex[2][1] = j + 1;
						isHaveShow = true;

						for (int j2 = 0; j2 < showIndex.length; j2++) {
							if (showIndex[j2][0] == iTemp
									&& showIndex[j2][1] == jTemp) {
								showIndex[j2][0] = iTemp1;
								showIndex[j2][1] = jTemp1;

								showIndex[3][0] = iTemp;
								showIndex[3][1] = jTemp;
								break;
							} else if (showIndex[j2][0] == iTemp1
									&& showIndex[j2][1] == jTemp1) {
								showIndex[j2][0] = iTemp;
								showIndex[j2][1] = jTemp;
								showIndex[3][0] = iTemp1;
								showIndex[3][1] = jTemp1;
								break;
							}
						}
					}
					return true;
				}
				if (i > 0 && i < a.length - 1 && a[i][j][2] == a[i - 1][j][2]
						&& a[i][j][2] == a[i + 1][j][2]) {
					if (showTime > 50) {
						showIndex[0][0] = i;
						showIndex[0][1] = j;
						showIndex[1][0] = i - 1;
						showIndex[1][1] = j;
						showIndex[2][0] = i + 1;
						showIndex[2][1] = j;
						isHaveShow = true;
						for (int j2 = 0; j2 < showIndex.length; j2++) {
							if (showIndex[j2][0] == iTemp
									&& showIndex[j2][1] == jTemp) {
								showIndex[j2][0] = iTemp1;
								showIndex[j2][1] = jTemp1;

								showIndex[3][0] = iTemp;
								showIndex[3][1] = jTemp;

							} else if (showIndex[j2][0] == iTemp1
									&& showIndex[j2][1] == jTemp1) {
								showIndex[j2][0] = iTemp;
								showIndex[j2][1] = jTemp;

								showIndex[3][0] = iTemp1;
								showIndex[3][1] = jTemp1;
							}
						}

					}
					return true;
				}
			}
		}
		return false;
	}

	private int II, JJ, IK, JK, temp2;
	private long time = 0;
	private int intervalTime;

	// *********************复制数据*******************************
	private void sxzy_foe(int ee, int eee) {// px,py----手指下一步指向的行列
		int px = goodsFoe[ee][eee][0];
		int py = goodsFoe[ee][eee][1];

		if (II >= 1
				&& goodsFoe[II - 1][JJ][8] == 0
				&& T.TM.intersectRectWithRect(px, py, 1, 1,
						goodsFoe[II - 1][JJ][0] - width_foe / 2,
						goodsFoe[II - 1][JJ][1] - height_foe / 2, width_foe,
						height_foe)) {// 上

			IK = II - 1;
			JK = JJ;
			temp2 = goodsFoe[II - 1][JJ][2];
			goodsFoe[II - 1][JJ][2] = goodsFoe[II][JJ][2];
			goodsFoe[II][JJ][2] = temp2;
			// 移动数据
			goodsFoe[II][JJ][3] = -height_foe;
			goodsFoe[II - 1][JJ][3] = height_foe;
			isRandom_foe = true;
			dir_foe = 1;
		} else if (II < row - 1
				&& goodsFoe[II + 1][JJ][8] == 0
				&& T.TM.intersectRectWithRect(px, py, 1, 1,
						goodsFoe[II + 1][JJ][0] - width_foe / 2,
						goodsFoe[II + 1][JJ][1] - height_foe / 2, width_foe,
						height_foe)) {// 下
			IK = II + 1;
			JK = JJ;
			temp2 = goodsFoe[II + 1][JJ][2];
			goodsFoe[II + 1][JJ][2] = goodsFoe[II][JJ][2];
			goodsFoe[II][JJ][2] = temp2;
			// 移动数据
			goodsFoe[II][JJ][4] = height_foe;
			goodsFoe[II + 1][JJ][4] = -height_foe;
			isRandom_foe = true;
			dir_foe = 2;
		} else if (JJ >= 1
				&& goodsFoe[II][JJ - 1][8] == 0
				&& T.TM.intersectRectWithRect(px, py, 1, 1,
						goodsFoe[II][JJ - 1][0] - width_foe / 2,
						goodsFoe[II][JJ - 1][1] - height_foe / 2, width_foe,
						height_foe)) {// 左
			IK = II;
			JK = JJ - 1;
			temp2 = goodsFoe[II][JJ - 1][2];
			goodsFoe[II][JJ - 1][2] = goodsFoe[II][JJ][2];
			goodsFoe[II][JJ][2] = temp2;
			// 移动数据
			goodsFoe[II][JJ][5] = -width_foe;
			goodsFoe[II][JJ - 1][5] = width_foe;
			isRandom_foe = true;
			dir_foe = 3;

		} else if (JJ < col - 1
				&& goodsFoe[II][JJ + 1][8] == 0
				&& T.TM.intersectRectWithRect(px, py, 1, 1,
						goodsFoe[II][JJ + 1][0] - width_foe / 2,
						goodsFoe[II][JJ + 1][1] - height_foe / 2, width_foe,
						height_foe)) {// 右

			IK = II;
			JK = JJ + 1;
			temp2 = goodsFoe[II][JJ + 1][2];
			goodsFoe[II][JJ + 1][2] = goodsFoe[II][JJ][2];
			goodsFoe[II][JJ][2] = temp2;

			// 移动数据
			goodsFoe[II][JJ][6] = width_foe;
			goodsFoe[II][JJ + 1][6] = -width_foe;
			isRandom_foe = true;
			dir_foe = 4;
		}

	}

	private boolean isRandom_foe = false;
	private boolean backRandom_foe = false;

	private int dir_foe = 0;

	private void runRandom_foe(int dir_foe) {
		switch (dir_foe) {
		case 1:// 上
			goodsFoe[II][JJ][3] += speeg_foe;
			goodsFoe[IK][JK][3] -= speeg_foe;
			if (goodsFoe[II][JJ][3] >= 0 && goodsFoe[IK][JK][3] <= 0) {
				goodsFoe[II][JJ][3] = 0;
				goodsFoe[IK][JK][3] = 0;
				isRandom_foe = false;
				if (remove_foe()) {
					removed_foe();
				} else {
					backRandom_foe = true;
					temp2 = goodsFoe[II][JJ][2];
					goodsFoe[II][JJ][2] = goodsFoe[IK][JK][2];
					goodsFoe[IK][JK][2] = temp2;
					// 移动数据
					goodsFoe[II][JJ][3] = -height_foe;
					goodsFoe[IK][JK][3] = +height_foe;
				}
			}
			break;
		case 2:// 下
			goodsFoe[II][JJ][4] -= speeg_foe;
			goodsFoe[IK][JK][4] += speeg_foe;
			if (goodsFoe[II][JJ][4] <= 0 && goodsFoe[IK][JK][4] >= 0) {
				goodsFoe[II][JJ][4] = 0;
				goodsFoe[IK][JK][4] = 0;
				isRandom_foe = false;
				if (remove_foe()) {
					removed_foe();
				} else {
					backRandom_foe = true;
					temp2 = goodsFoe[II][JJ][2];
					goodsFoe[II][JJ][2] = goodsFoe[IK][JK][2];
					goodsFoe[IK][JK][2] = temp2;

					// 移动数据
					goodsFoe[II][JJ][4] = height_foe;
					goodsFoe[IK][JK][4] = -height_foe;
				}
			}
			break;
		case 3:// 左
			goodsFoe[II][JJ][5] += speeg_foe;
			goodsFoe[IK][JK][5] -= speeg_foe;
			if (goodsFoe[II][JJ][5] >= 0 && goodsFoe[IK][JK][5] <= 0) {
				goodsFoe[II][JJ][5] = 0;
				goodsFoe[IK][JK][5] = 0;
				isRandom_foe = false;
				if (remove_foe()) {
					removed_foe();
				} else {
					backRandom_foe = true;
					temp2 = goodsFoe[II][JJ][2];
					goodsFoe[II][JJ][2] = goodsFoe[IK][JK][2];
					goodsFoe[IK][JK][2] = temp2;

					// 移动数据
					goodsFoe[II][JJ][5] = -width_foe;
					goodsFoe[IK][JK][5] = width_foe;
				}
			}
			break;
		case 4:// 右

			goodsFoe[II][JJ][6] -= speeg_foe;
			goodsFoe[IK][JK][6] += speeg_foe;
			if (goodsFoe[II][JJ][6] <= 0 && goodsFoe[IK][JK][6] >= 0) {
				goodsFoe[II][JJ][6] = 0;
				goodsFoe[IK][JK][6] = 0;
				isRandom_foe = false;
				if (remove_foe()) {
					removed_foe();
				} else {
					backRandom_foe = true;
					temp2 = goodsFoe[II][JJ][2];
					goodsFoe[II][JJ][2] = goodsFoe[IK][JK][2];
					goodsFoe[IK][JK][2] = temp2;

					// 移动数据
					goodsFoe[II][JJ][6] = width_foe;
					goodsFoe[IK][JK][6] = -width_foe;
				}
			}
			break;
		}

	}

	// 返回
	private void backRandom_foe(int dir_foe) {
		switch (dir_foe) {
		case 1:// 上
			goodsFoe[II][JJ][3] += speeg_foe;
			goodsFoe[IK][JK][3] -= speeg_foe;
			if (goodsFoe[II][JJ][3] >= 0 && goodsFoe[IK][JK][3] <= 0) {
				goodsFoe[II][JJ][3] = 0;
				goodsFoe[IK][JK][3] = 0;
				backRandom_foe = false;
			}
			break;
		case 2:// 下
			goodsFoe[II][JJ][4] -= speeg_foe;
			goodsFoe[IK][JK][4] += speeg_foe;
			if (goodsFoe[II][JJ][4] <= 0 && goodsFoe[IK][JK][4] >= 0) {
				goodsFoe[II][JJ][4] = 0;
				goodsFoe[IK][JK][4] = 0;
				backRandom_foe = false;
			}
			break;
		case 3:// 左
			goodsFoe[II][JJ][5] += speeg_foe;
			goodsFoe[IK][JK][5] -= speeg_foe;
			if (goodsFoe[II][JJ][5] >= 0 && goodsFoe[IK][JK][5] <= 0) {
				goodsFoe[II][JJ][5] = 0;
				goodsFoe[IK][JK][5] = 0;
				backRandom_foe = false;
			}
			break;
		case 4:// 右
			goodsFoe[II][JJ][6] -= speeg_foe;
			goodsFoe[IK][JK][6] += speeg_foe;
			if (goodsFoe[II][JJ][6] <= 0 && goodsFoe[IK][JK][6] >= 0) {
				goodsFoe[II][JJ][6] = 0;
				goodsFoe[IK][JK][6] = 0;
				backRandom_foe = false;
			}
			break;
		}

	}

	// 是否禁止状态
	private boolean getStatut_foe() {
		for (int i = 0; i < goodsFoe.length; i++) {
			for (int j = 0; j < goodsFoe[i].length; j++) {
				if (goodsFoe[i][j][3] != 0 || goodsFoe[i][j][4] != 0
						|| goodsFoe[i][j][5] != 0 || goodsFoe[i][j][6] != 0) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean canSlow_foe = false;// 有消除才有下滑
	private int speeg_foe = 6;

	private void runSlow_foe() {
		for (int i = 0; i < goodsFoe.length; i++) {
			for (int j = 0; j < goodsFoe[i].length; j++) {
				if (goodsFoe[i][j][4] != 0) {
					goodsFoe[i][j][4] += speeg_foe;
					if (goodsFoe[i][j][4] >= 0) {
						goodsFoe[i][j][4] = 0;
					}
				}
			}
		}
	}

	private boolean remove_foe() {
		boolean yes = false;
		for (int i = 0; i < goodsFoe.length; i++) {// 行--消除
			for (int j = 0; j < goodsFoe[i].length - 2; j++) {
				if (goodsFoe[i][j][2] == goodsFoe[i][j + 1][2]
						&& goodsFoe[i][j][2] == goodsFoe[i][j + 2][2]) {
					goodsFoe[i][j][7] = 1;
					goodsFoe[i][j + 1][7] = 1;
					goodsFoe[i][j + 2][7] = 1;
					yes = true;
				}
			}
		}
		for (int i = 0; i < col; i++) {// 列--消除
			for (int j = 0; j < row - 2; j++) {
				if (goodsFoe[j][i][2] == goodsFoe[j + 1][i][2]
						&& goodsFoe[j][i][2] == goodsFoe[j + 2][i][2]) {
					goodsFoe[j][i][7] = 1;
					goodsFoe[j + 1][i][7] = 1;
					goodsFoe[j + 2][i][7] = 1;
					yes = true;
				}
			}
		}
		return yes;
	}

	private boolean initremove_foe() {
		boolean yes = false;
		for (int i = 0; i < goodsFoe.length; i++) {// 行--消除
			for (int j = 0; j < goodsFoe[i].length - 2; j++) {
				if (goodsFoe[i][j][2] == goodsFoe[i][j + 1][2]
						&& goodsFoe[i][j][2] == goodsFoe[i][j + 2][2]) {
					goodsFoe[i][j][7] = 1;
					goodsFoe[i][j + 1][7] = 1;
					goodsFoe[i][j + 2][7] = 1;
					yes = true;
				}
			}
		}
		for (int i = 0; i < col; i++) {// 列--消除
			for (int j = 0; j < row - 2; j++) {
				if (goodsFoe[j][i][2] == goodsFoe[j + 1][i][2]
						&& goodsFoe[j][i][2] == goodsFoe[j + 2][i][2]) {
					goodsFoe[j][i][7] = 1;
					goodsFoe[j + 1][i][7] = 1;
					goodsFoe[j + 2][i][7] = 1;
					yes = true;
				}
			}
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				goodsFoe[i][j][7] = 0;
			}
		}
		return yes;
	}

	private void leadEffect_foe() {
		// diren效果
		lastRemoveNumFoe = 0;
		lastGoldRemoveNumFoe = 0;

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (goodsFoe[i][j][7] == 1) {
					goodsFoe[i][j][8] = 0;
					lastRemoveNumFoe++;
					if (goods[i][j][9] != 0) {
						lastGoldRemoveNumFoe++;
					}

					// 消除的对象
					switch (goodsFoe[i][j][2]) {
					case 0:// 书本---增加灵气的
						foe.addReiki(10);
						break;
					case 1:// 手铐
						foe.setStatut(4);// 敌人切换至普通技能
						lockAlny(1);
						break;
					case 2:// 刀子
						foe.setStatut(3);// 敌人切换至普通攻击
						break;
					case 3:// 金币
						break;
					case 4:// 药水
						foe.setStatut(1);// 敌人切换至喝药状态
						foe.addOrReduceLife(0, 10);
						break;
					}
				}
			}
		}
	}

	// //实现锁住主主角的几个位置
	public void lockAlny(int k) {
		int num = 0;
		while (num < k) {
			int i = M.getRandom(0, row);
			int j = M.getRandom(0, col);
			if (goods[i][j][9] == 0) {
				goods[i][j][8] = lockTimeO;
				num++;
			}
		}
	}

	private void removed_foe() {
		switch (GameControl.game_mode) {
		case 0:
			leadEffect_foe();
			break;
		}
		// 下移
		for (int i = 0; i < col; i++) {
			for (int j = row - 2; j >= 0; j--) {
				// 最下 第二排开始------下方是否有空位置
				if (goodsFoe[j][i][7] != 1) {
					int num = 0;
					for (int jt = j + 1; jt < row; jt++) {
						if (goodsFoe[jt][i][7] == 1) {
							num++;
						}
					}
					if (num > 0) {
						canSlow_foe = true;
						goodsFoe[j + num][i][2] = goodsFoe[j][i][2];
						goodsFoe[j + num][i][4] = -height_foe * num;
					}
				}
			}
		}
		// 上面产生新的
		for (int i = 0; i < col; i++) {
			int num = 0;
			for (int j = 0; j < row; j++) {
				if (goodsFoe[j][i][7] == 1) {
					canSlow_foe = true;
					num++;
					goodsFoe[num - 1][i][2] = M.getRandom(5);
					goodsFoe[num - 1][i][4] = -height_foe * num;
					// goods[j][i][7] = 0;// 消除这点
					// goods[num - 1][i][7] = 0;
				}
			}
		}

		// 指定消除的点---清除
		for (int t = 0; t < row; t++) {
			for (int v = 0; v < col; v++) {
				goodsFoe[t][v][7] = 0;
			}
		}
	}

	/** 判断有没有可以消除的了，如果没有重新产生 */
	public boolean checkRefresh_foe() {
		boolean refresh = true;
		for (int i = 0; i < goodsFoe.length; i++) {
			int i_j = -1;
			for (int j = 0; j < goodsFoe[i].length; j++) {
				if (i - 1 >= 0) {
					i_j = goodsFoe[i - 1][j][2];
					goodsFoe[i - 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (canDelete_foe(goodsFoe)) {
						refresh = false;
					}
					i_j = goodsFoe[i - 1][j][2];
					goodsFoe[i - 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
				}

				if (j - 1 >= 0) {
					i_j = goodsFoe[i][j - 1][2];
					goodsFoe[i][j - 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (canDelete_foe(goodsFoe)) {
						refresh = false;
					}
					i_j = goodsFoe[i][j - 1][2];
					goodsFoe[i][j - 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
				}

				if (i + 1 < goodsFoe.length) {
					i_j = goodsFoe[i + 1][j][2];
					goodsFoe[i + 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (canDelete_foe(goodsFoe)) {
						refresh = false;
					}
					i_j = goodsFoe[i + 1][j][2];
					goodsFoe[i + 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
				}

				if (j + 1 < goodsFoe[i].length) {
					i_j = goodsFoe[i][j + 1][2];
					goodsFoe[i][j + 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (canDelete_foe(goodsFoe)) {
						refresh = false;
					}
					i_j = goodsFoe[i][j + 1][2];
					goodsFoe[i][j + 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
				}
			}
		}
		return refresh;
	}

	/** 移动之后是否满足删除条件 */
	private boolean canDelete_foe(int a[][][]) {
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[i].length; j++) {
				if (j > 0 && j < a[i].length - 1
						&& a[i][j][2] == a[i][j - 1][2]
						&& a[i][j][2] == a[i][j + 1][2]) {
					return true;
				}
				if (i > 0 && i < a.length - 1 && a[i][j][2] == a[i - 1][j][2]
						&& a[i][j][2] == a[i + 1][j][2]) {
					return true;
				}
			}
		}
		return false;
	}

	/** 敌人获取可以消除的行列 */
	public int[] getXY_foe() {
		boolean refresh = false;
		int abc_XY[] = new int[4];
		jie: for (int i = 0; i < row; i++) {
			int i_j = -1;
			for (int j = 0; j < col; j++) {
				if (i - 1 >= 0) {
					i_j = goodsFoe[i - 1][j][2];
					goodsFoe[i - 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (goodsFoe[i - 1][j][8] == 0 && goodsFoe[i][j][8] == 0
							&& canDelete_foe(goodsFoe)) {
						abc_XY[0] = i - 1;
						abc_XY[1] = j;
						abc_XY[2] = i;
						abc_XY[3] = j;
						refresh = true;
					}
					i_j = goodsFoe[i - 1][j][2];
					goodsFoe[i - 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (refresh) {
						break jie;
					}
				}

				if (j - 1 >= 0) {
					i_j = goodsFoe[i][j - 1][2];
					goodsFoe[i][j - 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (goodsFoe[i][j - 1][8] == 0 && goodsFoe[i][j][8] == 0
							&& canDelete_foe(goodsFoe)) {
						abc_XY[0] = i;
						abc_XY[1] = j - 1;
						abc_XY[2] = i;
						abc_XY[3] = j;
						refresh = true;
					}
					i_j = goodsFoe[i][j - 1][2];
					goodsFoe[i][j - 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (refresh) {
						break jie;
					}
				}

				if (i + 1 < goodsFoe.length) {
					i_j = goodsFoe[i + 1][j][2];
					goodsFoe[i + 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (goodsFoe[i + 1][j][8] == 0 && goodsFoe[i][j][8] == 0
							&& canDelete_foe(goodsFoe)) {
						abc_XY[0] = i + 1;
						abc_XY[1] = j;
						abc_XY[2] = i;
						abc_XY[3] = j;
						refresh = true;
					}
					i_j = goodsFoe[i + 1][j][2];
					goodsFoe[i + 1][j][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (refresh) {
						break jie;
					}
				}

				if (j + 1 < goodsFoe[i].length) {
					i_j = goodsFoe[i][j + 1][2];
					goodsFoe[i][j + 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (goodsFoe[i][j + 1][8] == 0 && goodsFoe[i][j][8] == 0
							&& canDelete_foe(goodsFoe)) {
						abc_XY[0] = i;
						abc_XY[1] = j + 1;
						abc_XY[2] = i;
						abc_XY[3] = j;
						refresh = true;
					}
					i_j = goodsFoe[i][j + 1][2];
					goodsFoe[i][j + 1][2] = goodsFoe[i][j][2];
					goodsFoe[i][j][2] = i_j;
					if (refresh) {
						break jie;
					}
				}
			}
		}
		return abc_XY;
	}

	// 主角或者敌人--暴怒下--翻倍--0主，1敌
	public int getDouble(int i) {
		int multible = 1;
		switch (i) {
		case 0:
			if (isFuryLead) {
				multible = 2;
			}
			break;
		case 1:
			if (isFuryFoe) {
				multible = 2;
			}
			break;
		}
		return multible;
	}

	@Override
	public void onBackPressed() {
		switch (GameControl.gameMenu2Status) {
		case GameControl.GAME_MENU2_1:// 是否回主菜单
			break;
		default:
			switch (GameControl.gameStatus) {
			case GameControl.GAME_INTO:// 游戏
				pauseGame();
				break;
			case GameControl.GAME_SUC:// 过关
			case GameControl.GAME_ERR:// 失败
			case GameControl.GAME_SUCALL:// 通关
				GameMainActivity.bffa.changeView(0);
				break;
			case GameControl.GAME_PAUSE:// 暂停
				setGameStatus(GameControl.GAME_INTO);
				break;
			}
			break;
		}
	}

	public void paintString(Canvas g, Paint p, String str, int x, int y,
			int width) {
		String[] strs = str2lines(str, width, p);

		FontMetrics fm = p.getFontMetrics();

		float fFontHeight = (float) Math.ceil(fm.descent - fm.ascent);
		y += fFontHeight / 2;
		for (int i = 0; i < strs.length; i++) {
			g.drawText(strs[i], x, y + i * fFontHeight, p);
		}
	}

	private static String[] str2lines(String str, int width, Paint p) {
		ArrayList<String> result = new ArrayList<String>();
		if (str != null && str.length() > 0) {
			int len = str.length();
			float[] widths = new float[len];

			p.getTextWidths(str, widths);

			int curWidth = 0;
			int curLineWidth = 0;
			StringBuilder sbLine = new StringBuilder();
			StringBuilder sbWord = new StringBuilder();
			for (int j = 0; j < len; j++) {
				int curCharWidth = (int) Math.ceil(widths[j]);
				char tChar = str.charAt(j);
				if (curWidth >= width) {// 是空格那么就可以判断了
					if (curLineWidth + curWidth <= width) {
						curLineWidth += curWidth;
					} else {
						curLineWidth = curWidth;
						result.add(sbLine.toString());
						sbLine.delete(0, sbLine.length());
					}

					sbLine.append(sbWord);
					sbWord.delete(0, sbWord.length());

					curWidth = curCharWidth;
					sbWord.append(tChar);
				} else {
					curWidth += curCharWidth;
					sbWord.append(tChar);
				}
			}

			if (sbWord.length() > 0) {
				if (curLineWidth + curWidth <= width) {
					curLineWidth += curWidth;
					sbLine.append(sbWord);
					result.add(sbLine.toString());
				} else {
					if (sbLine.length() > 0) {
						result.add(sbLine.toString());
						sbLine.delete(0, sbLine.length());
					}
					sbLine.append(sbWord);
					result.add(sbLine.toString());
				}
			} else {
				if (sbLine.length() > 0) {
					result.add(sbLine.toString());
				}
			}
		}

		return result.toArray(new String[result.size()]);
	}
}
