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
 * ��Ϸ���ݿ�
 * 
 * @author Joniy
 * 
 */
public class DB {
	// ���ݿ��������
	private int layer = 0;
	private int layers = 0;// �������Ƕ��ĵȼ�
	private int money = 0;
	private int score = 0;// ������Ƭ
	private int gmode = 0;// ��������һ�ĵȼ�
	private int isold = 0;
	private int isRegister = 0; //�Ƿ���ע��
	private int isLevelCancelLocked = 0; //�Ƿ��ѽ���

	/** ��������: [0]��������[1]ħ��������[2]������[3]ʣ�༼�ܵ㣬[4]��ǰ����,[5]��ǰ�ȼ� */
	private int leadSave[][] = { { 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 } };
	// 0--1--2--3--���浽��4--��Ϸ����
	/**
	 * ÿ�����ߵĸ���:ħ������������Ѫ���飬ˢ��
	 * */
	private int numbss[] = { 0, 0, 0, 0 };

	/** 0 δ�ﵽ---1�ﵽδ��ȡ----2��ȡ��{31�ֳƺ��Ƿ��ú���ȡ��} */
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
	private final String DB_NAME = PS.DBKEY + ".db";// ���ݿ���
	private final int VERSION = 1;// �汾
	private final String TB_NAME = "tab" + PS.DBKEY;// ����
	private final String ID = "_id";// ����
	private final String LAYER = "layer";// �ؿ�
	private final String LAYERS = "layers";// ����
	private final String MONEY = "money";// ��Ǯ
	private final String SCORE = "score";// ����
	private final String GMODE = "gmode";// ģʽ
	private final String ISOLD = "isold";// ���ݿ��Ƿ��Ѿ��޸Ĺ�
	private final String ISREG = "isreg";// �Ƿ�ע��
	private final String ISNOLOCK = "isnolock";// �Ƿ�����
	private final String OTHER = "other";// ��������(��Ҫ��Բ�ͬ��Ϸ���н���)
	private String other = "";// ��������(��Ҫ��Բ�ͬ��Ϸ���н���)

	// ��ȡ���ݿ���������ֶ�����=================================================

	/**
	 * ��ȡ���ݿ���������ֶ�����
	 * 
	 * @param oData
	 */
	private void getSAnalysis(String other) {
		if (!other.trim().equals("")) {// �����������Խ���
			String[] temp = T.TS.split(other, ";");
			for (int i = 0; i < leadSave.length; i++) {
				leadSave[i] = T.TS.getInts(temp[i], ",");
			}
			numbss = T.TS.getInts(temp[leadSave.length], ",");
			title = T.TS.getInts(temp[leadSave.length + 1], ",");
		}
	}

	/**
	 * �������ݿ���������ֶ�����
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
	 * �������ݿ�
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
	 * ��ȡ���� bool = true ���ݿ��ʼ��
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
			setSAnalysis();// ��������ת�����ַ������б���
			values.put(OTHER, other);
		}
		return values;
	}

	public String toString() {
		return layer + " " + layers + " " + money + " " + score + " " + gmode
				+ " " + isold + " "+isRegister+ " "+isLevelCancelLocked+ " " + other;
	}

	/**
	 * ���ݿ⸨���ڲ���
	 * 
	 * @author Rain
	 * 
	 */
	class MyHelper extends SQLiteOpenHelper {

		/**
		 * �������ݿ�
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
		 * ������
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
		 * ���±�
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
