package com.tuowei.bdll;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.gameFrame.T;
import com.gameFrame.controller.GameDirector;
import com.gameFrame.controller.IScene;
import com.gameFrame.controller.opengl.CanvasView;
import com.gameFrame.pic.Pic;
import com.sdk.sms.SmsInfo;
import com.tuowei.canvas.GameCanvas;
import com.tuowei.canvas.GameMapView;
import com.tuowei.canvas.GameMenuView;
import com.tuowei.canvas.GameSelectView;
import com.tuowei.canvas.LoadingCanvas;
import com.tuowei.canvas.ShopView;
import com.tuowei.control.GameControl;
import com.tuowei.control.PS;
import com.tuowei.db.DB;
import com.tuowei.sound.MuAuPlayer;
import com.tuowei.tool.ShareCtrl;

/**
 * 游戏框架主运行类
 * 
 * @author Joniy
 * 
 */
public class GameMainActivity extends Activity {

	// 是否正在载入
	public boolean isLoading = false;
	/** 框架主类 */
	public static GameMainActivity bffa;

	/**
	 * 构造函数
	 */
	public GameMainActivity() {
		bffa = this;
	}

	/**
	 * 初始化工具数据
	 */
	private void initTData() {
		T.setResources(getResources());
		// 固定屏幕大小
		GameDirector._WIDTH = PS.screenw;
		GameDirector._HIGHT = PS.screenh;
		// 字体配置
		GameDirector.shareDirector().fontSize = PS.FONTSIZE;

		T.FG.linew = PS.FONT_LINEW;
		T.FG.offsetW = PS.FONT_OFFSETW;
		T.FG.offsetH = PS.FONT_OFFSETH;

	}
	
	/**
	 * GAME的资源载入处理
	 */
	public Runnable loadDataThread = new Runnable() {
		 
		public void run() {
			//Looper.prepare();
			
			refData();
			isLoading = false;
			
			//Looper.loop();
			
		}
		
	};
	private static boolean isFirstLoad = true;

	/**
	 * 资源控制综合管理
	 * 
	 * @param LoadingStatus
	 */
	private void refData() {
		// 获取LOADING切换时的状态
		int loadingView = LoadingCanvas.showIndex;
		if (showScene != null)
			showScene.disingData();
		try {
			showScene = (IScene) gameScenes[loadingView].newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isFirstLoad) {
			MuAuPlayer.muaup.loadMAData();
		}
		showScene.loadingData();
	}

	private Class<?> gameScenes[] = { GameMenuView.class, ShopView.class,
			GameMapView.class, GameSelectView.class, GameCanvas.class };

	private IScene showScene;

	/** 0=GameMenuView； */
	public void changeView(int sceneIndex) {
		GameControl.lastShowView = LoadingCanvas.showIndex;
		GameControl.showView = sceneIndex;
		isLoading = true;
		LoadingCanvas.showIndex = sceneIndex;
		loadingView.show();
		new Thread(loadDataThread).start();
	}

	public void showView() {
		showScene.show();

		if (isFirstLoad) {
			PS.IS_SoundMU = true;
			isFirstLoad = false;
			MuAuPlayer.muaup.mupStart();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/**
	 * 创建
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		T.isAlive = true;
		getWindow().setFlags(//
				WindowManager.LayoutParams.FLAG_FULLSCREEN,//
				WindowManager.LayoutParams.FLAG_FULLSCREEN);//
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	private CanvasView showView;

	/**
	 * 显示界面控制
	 */
	private void showViewCtrl() {
		isStarted = true;
		initTData();// 初始化工具数据一定要提前执行
		initData();
		PS.initDebug();
		GameDirector.shareDirector().isAlive = true;
		showView = new CanvasView(this);
		setContentView(showView);
		ShareCtrl.sc.initGameBuffer();
		changeView(0);
	}

	private LoadingCanvas loadingView;

	private void initData() {
		if (loadingView == null)
			loadingView = new LoadingCanvas();
	}

	private boolean isStarted = false;

	/**
	 * 开始
	 */
	protected void onStart() {
		super.onStart();
		if (!isStarted) {
			showViewCtrl();
		}
	}

	/**
	 * 继续
	 */
	protected void onResume() {
		super.onResume();
		if (PS.IS_SoundMU) {
			MuAuPlayer.muaup.loadMAData();
			MuAuPlayer.muaup.mupStart();
		}
	}

	/**
	 * 暂停
	 */
	protected void onPause() {
		super.onPause();
		MuAuPlayer.muaup.mupStop();
		MuAuPlayer.muaup.aupStopAll();
		MuAuPlayer.muaup.disMAData();
	}

	/**
	 * 停止
	 */
	protected void onStop() {
		super.onStop();
	}

	/**
	 * 释放
	 */
	protected void onDestroy() {
		super.onDestroy();
		disApp();
	}

	/**
	 * 释放资源
	 */
	private void disData() {
		// 暂做释放处理
		MuAuPlayer.muaup.disMAData();
		Pic.clearSrcs();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (showScene != null) {
				showScene.onBackPressed();
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 彻底释放程序
	 */
	private void disApp() {
		T.isAlive = false;
		disData();
		// 方法一 ==============================================
		// android.os.Process.killProcess(android.os.Process.myPid());
		// 方法二==============================================
		System.exit(0);
	}
	
	public static Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case SmsInfo.HANDLER_MSG_SEND_SMS:
				SmsInfo.checlFeesDialog();
				break;
			}
		}
	};
}