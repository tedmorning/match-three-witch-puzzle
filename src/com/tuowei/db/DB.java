package com.tuowei.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.gameFrame.T;
import com.tuowei.bdll.GameMainActivity;
import com.tuowei.control.PS;

/**
 * 游戏数据库
 * 
 * @author Joniy
 * 
 */
public class DB {
	// 数据库参数部分
	private int layer = 0;
	private int layers = 0;// 保存主角二的等级
	private int money = 0;
	private int score = 0;// 保存碎片
	private int gmode = 0;// 保存主角一的等级
	private int isold = 0;
	private int isRegister = 0; //是否已注册
	private int isLevelCancelLocked = 0; //是否已解锁

	/** 保存主绝: [0]物理攻击，[1]魔法攻击，[2]防御，[3]剩余技能点，[4]当前经验,[5]当前等级 */
	private int leadSave[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
	// 0--1--2--3--保存到据4--游戏积分
	/**
	 * 每个道具的个数:魔法棒，暴击，血精灵，刷新
	 * */
	private int numbss[] = { 0, 0, 0, 0 };

	/** 0 未达到---1达到未领取----2领取了{31种称号是否获得和领取了} */
	public int title[] = { 0, 0, 0, 0 };

	public int[] getTitle() {
		return title;
	}

	public void setTitle(int[] title) {
		this.title = title;
	}

	public int[] getNumbss() {
		return numbss;
	}

	public void setNumbss(int[] numbss) {
		this.numbss = numbss;
	}

	public int[][] getLeadSave() {
		return leadSave;
	}

	public void setLeadSave(int[][] leadSave) {
		this.leadSave = leadSave;
	}

	public boolean getIsold() {
		return isold == 1;
	}

	public int getLayer() {
		return layer;
	}

	public void setIsRegister(int isRegister) {
		this.isRegister = isRegister;
	}

	public int getIsRegister() {
		return isRegister;
	}
	
	public void setIsLevelCancelLocked(int isLevelCancelLocked) {
		this.isLevelCancelLocked = isLevelCancelLocked;
	}

	public int getIsLevelCancelLocked() {
		return isLevelCancelLocked;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}

	public int getLayers() {
		return layers;
	}

	public void setLayers(int layers) {
		this.layers = layers;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getGmode() {
		return gmode;
	}

	public void setGmode(int gmode) {
		this.gmode = gmode;
	}

	// ==============================================================
	private MyHelper m_Helper;
	private SQLiteDatabase dbsql;
	private final String DB_NAME = PS.DBKEY + ".db";// 数据库名
	private final int VERSION = 1;// 版本
	private final String TB_NAME = "tab" + PS.DBKEY;// 表名
	private final String ID = "_id";// 索引
	private final String LAYER = "layer";// 关卡
	private final String LAYERS = "layers";// 局数
	private final String MONEY = "money";// 金钱
	private final String SCORE = "score";// 积分
	private final String GMODE = "gmode";// 模式
	private final String ISOLD = "isold";// 数据库是否已经修改过
	private final String ISREG = "isreg";// 是否注册
	private final String ISNOLOCK = "isnolock";// 是否有锁
	private final String OTHER = "other";// 附属数据(需要针对不同游戏进行解析)
	private String other = "";// 附属数据(需要针对不同游戏进行解析)

	// 获取数据库额外数据手动解析=================================================

	/**
	 * 获取数据库额外数据手动解析
	 * 
	 * @param oData
	 */
	private void getSAnalysis(String other) {
		if (!other.trim().equals("")) {// 满足条件可以解析
			String[] temp = T.TS.split(other, ";");
			for (int i = 0; i < leadSave.length; i++) {
				leadSave[i] = T.TS.getInts(temp[i], ",");
			}
			numbss = T.TS.getInts(temp[leadSave.length], ",");
			title = T.TS.getInts(temp[leadSave.length + 1], ",");
		}
	}

	/**
	 * 设置数据库额外数据手动解析
	 * 
	 * @param oData
	 */
	private void setSAnalysis() {
		other = "";
		for (int i = 0; i < leadSave.length; i++) {
			other += T.TS.getStrs(leadSave[i], ",") + ";";
		}
		other += T.TS.getStrs(numbss, ",") + ";";
		other += T.TS.getStrs(title, ",") + ";";
	}

	// ==========================================================================
	/**
	 * 保存数据库
	 */
	public void saveDB() {
		dbsql.update(TB_NAME, getValue(false), ID + " = 1", null);
	}

	private void initDB() {
		Cursor c;
		c = dbsql.query(TB_NAME, null, null, null, null, null, null);
		if (c.isAfterLast()) {
			dbsql.insert(TB_NAME, ID, getValue(true));
		} else {
			final int c_0 = c.getColumnIndexOrThrow(LAYER);
			final int c_1 = c.getColumnIndexOrThrow(LAYERS);
			final int c_2 = c.getColumnIndexOrThrow(MONEY);
			final int c_3 = c.getColumnIndexOrThrow(SCORE);
			final int c_4 = c.getColumnIndexOrThrow(GMODE);
			final int c_5 = c.getColumnIndexOrThrow(ISOLD);
			final int c_6 = c.getColumnIndexOrThrow(ISREG);
			final int c_7 = c.getColumnIndexOrThrow(ISNOLOCK);
			final int c_8 = c.getColumnIndexOrThrow(OTHER);
			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				layer = c.getInt(c_0);
				layers = c.getInt(c_1);
				money = c.getInt(c_2);
				score = c.getInt(c_3);
				gmode = c.getInt(c_4);
				isold = c.getInt(c_5);
				isRegister = c.getInt(c_6);
				isLevelCancelLocked = c.getInt(c_7);
				other = c.getString(c_8);
			}
			getSAnalysis(other);
		}
		c.close();
	}

	public void delDB() {
		dbsql.update(TB_NAME, getValue(true), ID + " = 1", null);
	}

	/**
	 * 获取数据 bool = true 数据库初始化
	 * 
	 * @return
	 */
	private ContentValues getValue(boolean bool) {
		ContentValues values = new ContentValues();
		if (bool) {
			values.put(LAYER, layer = 0);
			values.put(LAYERS, layers = 0);
			values.put(MONEY, money = 0);
			values.put(SCORE, score = 0);
			values.put(GMODE, gmode = 0);
			values.put(ISOLD, isold = 0);
			values.put(ISREG, isRegister = 0);
			values.put(ISNOLOCK, isLevelCancelLocked = 0);
			values.put(OTHER, other = "");
		} else {
			values.put(LAYER, layer);
			values.put(LAYERS, layers);
			values.put(MONEY, money);
			values.put(SCORE, score);
			values.put(GMODE, gmode);
			values.put(ISOLD, isold = 1);
			values.put(ISREG, isRegister);
			values.put(ISNOLOCK, isLevelCancelLocked);
			setSAnalysis();// 额外数据转换成字符串进行保存
			values.put(OTHER, other);
		}
		return values;
	}

	public String toString() {
		return layer + " " + layers + " " + money + " " + score + " " + gmode
				+ " " + isold + " "+isRegister+ " "+isLevelCancelLocked+ " " + other;
	}

	/**
	 * 数据库辅助内部类
	 * 
	 * @author Rain
	 * 
	 */
	class MyHelper extends SQLiteOpenHelper {

		/**
		 * 构造数据库
		 * 
		 * @param context
		 * @param name
		 * @param factory
		 * @param version
		 */
		public MyHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}

		/**
		 * 创建表
		 */
		public void onCreate(SQLiteDatabase args) {
			args.execSQL("CREATE TABLE IF NOT EXISTS " + TB_NAME + " (" + ID//
					+ " INTEGER PRIMARY KEY," //
					+ LAYER + " INTEGR," //
					+ LAYERS + " INTEGR," //
					+ MONEY + " INTEGR," //
					+ SCORE + " INTEGR," //
					+ GMODE + " INTEGR," //
					+ ISOLD + " INTEGR," //
					+ ISREG + " INTEGR," //
					+ ISNOLOCK + " INTEGR," //
					+ OTHER + " VARCHAR " //
					+ ")"//
			);
		}

		/**
		 * 更新表
		 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
			onCreate(db);
		}
	}

	private void initData() {
		m_Helper = new MyHelper(GameMainActivity.bffa, DB_NAME, null, VERSION);
		dbsql = m_Helper.getWritableDatabase();
		initDB();
	}

	// =======================================

	public static DB db = new DB();

	public DB() {
		db = this;
		initData();
	}

}
