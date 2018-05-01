package com.tuowei.control;

/**
 * 属性状态(开发人员必须熟知每个参数的用意)
 * 
 * @author Joniy
 * 
 */
public class PS {

	// ==系统配置固定参数==========================================================
	/** 调试模式 */
	public static final boolean IS_DEBUG = true;
	/** 游戏每秒的帧数 */
	public static final int gameFPS = 30;
	/** 屏幕固定大小 */
	public static final int screenw = 480;
	/** 屏幕固定大小 */
	public static final int screenh = 800;
	/** 是否允许载入精灵辅助数据 */
	public static final boolean IS_LOADSPXDB = false;
	/** 是否显示视频LOGO */
	public static final boolean IS_PLAYLOGOVIDEO = false;
	/** 是否自适应触碰点 */
	public static final boolean IS_ADATOUCHPOINT = true;
	/** 是否需要开启传感器 */
	public static final boolean IS_OPENSENSOR = true;
	/** 是否开启蓝牙功能 */
	public static final boolean IS_BTOOTH = false;
	// <<<<<<字体>>>>>-------------------------------------------------
	/** 是否允许载入点阵字库 (非特殊情况不考虑使用) */
	public static final boolean IS_LOADFONT = false;
	/** 是否允许在如自定义字库 字体名称必须是 fontbf.ttf */
	public static final boolean IS_LOADDEFFONT = false;
	/** 字体大小配置 */
	public static final int FONTSIZE = 25;
	/** 字体大小配置 */
	public static final int FONT_LINEW = 1;
	/** 字体大小配置 */
	public static final int FONT_OFFSETW = 1;
	/** 字体大小配置 */
	public static final int FONT_OFFSETH = 2;
	// <<<<<<声音>>>>>-------------------------------------------------
	/** 是否开启游戏音乐 */
	public static boolean IS_SoundMU = false;
	// <<<<<<图片>>>>>-------------------------------------------------
	/** 禁止释放的图片 */
	public static int[] imageSrcsNoDis = new int[] { 129, 476, 477, 478, 479,
			480, 481 };
	// 图片加密数据
	public static final String[] picEnData = new String[] {};
	// <<<<<<数据库>>>>>-------------------------------------------
	/** 数据库KEY值参数 */
	public static final String DBKEY = "chuangwei";

	// 动态初始化的数据
	public static void initDebug() {
	}
	// ==移植配置动态参数==========================================================

	// ===========================================================================
}
