package com.tuowei.control;

public class GameControl {
	// 菜单界面当前的停留界面
	public enum MenuStatus {
		MENU_NONE, // 未操作，一般在封面上
		MENU_MORE, // 更多游戏界面，一般也是在封面上
		MENU_SET, // 设置界面，一般也是在封面上
		MENU_HELP, // 帮助界面
		MENU_ABOUT, // 关于界面
		MENU_TOP, // 二次菜单界面
		TOP_MAP, // 地图界面
		TOP_STARY// 故事界面
	}

	public static MenuStatus menu_status = MenuStatus.MENU_NONE;// 当前界面停在某种状态
	public static MenuStatus menu_showStatus;// 当前选择，准备进入的状态

	// 菜单界面中当前上层界面停留的状态
	public enum MenuTop {
		TOP_NONE, // 正常
		TOP_SURE, // 确认
		TOP_BAD;// 提示
	}

	public static MenuTop menuTop = MenuTop.TOP_NONE;

	public static int lastShowView;
	public static int showView;

	/** 选择的人物 */
	public static int selectIndex;

	// 游戏中===============================

	/** 普通模式 */
	public static final int GAME_MODE_0 = 0;
	/** 金币模式 */
	public static final int GAME_MODE_1 = 1;
	/** 挑战模式 */
	public static final int GAME_MODE_2 = 2;

	public static int game_mode = GAME_MODE_0;

	public static int gameLayer;
	public static final int MAX_LAYER = 50;
	public static final int REG_Layer = 3;

	/** 背景介绍 */
	public static final int GAME_STORY = 0;//
	/** 新手教程 */
	public static final int GAME_TECH = 1;//
	/** 关卡介绍 */
	public static final int GAME_LAYER = 2;//
	/** 宝石下落开场 */
	public static final int GAME_JS = 3;//
	/** 游戏 */
	public static final int GAME_INTO = 4;//
	/** 商店 */
	public static final int GAME_SHOP = 5;//
	/** 过关 */
	public static final int GAME_SUC = 6;//
	/** 失败 */
	public static final int GAME_ERR = 7;//
	/** 通关 */
	public static final int GAME_SUCALL = 8;//
	/** 暂停 */
	public static final int GAME_PAUSE = 9;//
	/** 对话 */
	public static final int GAME_STATUS1 = 12;//
	/** 教程 */
	public static final int GAME_STATUS2 = 13;//
	/** 暂停 */
	public static final int GAME_STATUS3 = 14;//
	/** 暂停 */
	public static final int GAME_STATUS4 = 15;//

	public static int gameStatus;

	/** 二级菜单未操作状态 */
	public static final int GAME_MENU2_0 = -1;
	/** 二级菜单是否回主菜单 */
	public static final int GAME_MENU2_1 = 0;

	public static int gameMenu2Status;

	/** 未选中状态 */
	public static final int GAME_PAUSE_0 = -1;
	/** 继续游戏 */
	public static final int GAME_PAUSE_1 = 0;
	/** 游戏设置 */
	public static final int GAME_PAUSE_2 = 1;
	/** 游戏帮助 */
	public static final int GAME_PAUSE_3 = 2;
	/** 回主菜单 */
	public static final int GAME_PAUSE_4 = 3;

	public static int gamePauseShowStatus;

	public static int gamePauseStatus;

}
