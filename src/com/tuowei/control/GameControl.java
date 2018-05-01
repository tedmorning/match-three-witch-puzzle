package com.tuowei.control;

public class GameControl {
	// �˵����浱ǰ��ͣ������
	public enum MenuStatus {
		MENU_NONE, // δ������һ���ڷ�����
		MENU_MORE, // ������Ϸ���棬һ��Ҳ���ڷ�����
		MENU_SET, // ���ý��棬һ��Ҳ���ڷ�����
		MENU_HELP, // ��������
		MENU_ABOUT, // ���ڽ���
		MENU_TOP, // ���β˵�����
		TOP_MAP, // ��ͼ����
		TOP_STARY// ���½���
	}

	public static MenuStatus menu_status = MenuStatus.MENU_NONE;// ��ǰ����ͣ��ĳ��״̬
	public static MenuStatus menu_showStatus;// ��ǰѡ��׼�������״̬

	// �˵������е�ǰ�ϲ����ͣ����״̬
	public enum MenuTop {
		TOP_NONE, // ����
		TOP_SURE, // ȷ��
		TOP_BAD;// ��ʾ
	}

	public static MenuTop menuTop = MenuTop.TOP_NONE;

	public static int lastShowView;
	public static int showView;

	/** ѡ������� */
	public static int selectIndex;

	// ��Ϸ��===============================

	/** ��ͨģʽ */
	public static final int GAME_MODE_0 = 0;
	/** ���ģʽ */
	public static final int GAME_MODE_1 = 1;
	/** ��սģʽ */
	public static final int GAME_MODE_2 = 2;

	public static int game_mode = GAME_MODE_0;

	public static int gameLayer;
	public static final int MAX_LAYER = 50;
	public static final int REG_Layer = 3;

	/** �������� */
	public static final int GAME_STORY = 0;//
	/** ���ֽ̳� */
	public static final int GAME_TECH = 1;//
	/** �ؿ����� */
	public static final int GAME_LAYER = 2;//
	/** ��ʯ���俪�� */
	public static final int GAME_JS = 3;//
	/** ��Ϸ */
	public static final int GAME_INTO = 4;//
	/** �̵� */
	public static final int GAME_SHOP = 5;//
	/** ���� */
	public static final int GAME_SUC = 6;//
	/** ʧ�� */
	public static final int GAME_ERR = 7;//
	/** ͨ�� */
	public static final int GAME_SUCALL = 8;//
	/** ��ͣ */
	public static final int GAME_PAUSE = 9;//
	/** �Ի� */
	public static final int GAME_STATUS1 = 12;//
	/** �̳� */
	public static final int GAME_STATUS2 = 13;//
	/** ��ͣ */
	public static final int GAME_STATUS3 = 14;//
	/** ��ͣ */
	public static final int GAME_STATUS4 = 15;//

	public static int gameStatus;

	/** �����˵�δ����״̬ */
	public static final int GAME_MENU2_0 = -1;
	/** �����˵��Ƿ�����˵� */
	public static final int GAME_MENU2_1 = 0;

	public static int gameMenu2Status;

	/** δѡ��״̬ */
	public static final int GAME_PAUSE_0 = -1;
	/** ������Ϸ */
	public static final int GAME_PAUSE_1 = 0;
	/** ��Ϸ���� */
	public static final int GAME_PAUSE_2 = 1;
	/** ��Ϸ���� */
	public static final int GAME_PAUSE_3 = 2;
	/** �����˵� */
	public static final int GAME_PAUSE_4 = 3;

	public static int gamePauseShowStatus;

	public static int gamePauseStatus;

}
