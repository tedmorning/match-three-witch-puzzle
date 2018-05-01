package com.tuowei.tool;

/**
 * 关卡数据
 * 
 * @author Rain
 */
public class LayerData {

	/** 总关卡 */
	public static final int layero = 12;//
	public static final int layerAll = 32;//
	public static final int layersAll = 100;//
	/** 初始化分数 */
	public static final int scoreo = 300;//

	public static int reiki;

	/** 主角金币位置 --灵气位置--主角血条位置--敌人血条位置 */
	public static final int LeadCoinPlace[][] = new int[][] { { 210, 180 },
			{ 68, 261 }, { 424, 273 }, { 207, 209 } };//

	/** 关卡背景图片 */
	public static final int BG[] = new int[] { 117, 118, 119, 120, 121, 122 };
	/** 金币随时间增加的数量 */
	public static int goldN[] = new int[] { 2, 2, 3, 3, 3, 3, 4, 4, 5, 6, 7, 7,
			7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 10, 11, 12 };
	/** 飞动目标位置---和 图片 */
	public static int flyPosition[] = new int[] { 458, 459, 460, 446, 446 };

	/** 物理，魔法，防御，生命 */
	public static int[][] dateBasic = { { 10, 6, 2, 400 }, { 5, 45, 1, 300 },
			{ 8, 10, 0, 200 }, { 5, 30, 0, 200 }, { 8, 5, 0, 300 },
			{ 7, 15, 2, 350 } };

	public static float getAngle(float x2, float y2, float absX, float absY) {
		float rote = 0;
		if (x2 == absX && y2 < absY) {
			rote = 0;
		} else if (x2 == absX && y2 > absY) {
			rote = 180;
		} else if (x2 > absX && y2 == absY) {
			rote = 90;
		} else if (x2 < absX && y2 == absY) {
			rote = 270;

		} else if (x2 - absX > 0 && y2 - absY < 0) {
			rote = (float) (Math.atan(((float) (x2 - absX)) / (absY - y2))
					/ Math.PI * 180);
		} else if (x2 - absX > 0 && y2 - absY > 0) {
			rote = 90 + (float) (Math.atan(((float) (y2 - absY)) / (x2 - absX))
					/ Math.PI * 180);
		} else if (x2 - absX < 0 && y2 - absY > 0) {
			rote = 180 + (float) (Math
					.atan(((float) (absX - x2)) / (y2 - absY)) / Math.PI * 180);
		} else if (x2 - absX < 0 && y2 - absY < 0) {
			rote = (float) (Math.atan(((float) (x2 - absX)) / (absY - y2))
					/ Math.PI * 180);
		}
		return rote + 180;
	}
}
