package com.sdk.sms;


import com.tuowei.bdll.GameMainActivity;
import com.tuowei.canvas.ShopView;
import com.tuowei.db.DB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.widget.Button;
import android.widget.TextView;

public class SmsInfo {

	public static final int HANDLER_MSG_SEND_SMS = 0;
	
	public static int chargeID = 0;
	public static int[] chargeValue = {500,1000,1500};
	
	public static final int SMS_REG = 0;
	public static final int SMS_UNLOCK = 1;
	public static final int SMS_BUYMONEY_1 = 2; //3元
	public static final int SMS_BUYMONEY_2 = 3; //5元
	public static final int SMS_BUYMONEY_3 = 4; //6元
	
	public static String SmsName[] = {"注册激活","解锁关卡","一包金币","一桶金币","一箱金币"};
	public static String SmsDISC[] = {"精彩剧情即将开启，赶快注册吧!","解锁关卡,您可以直接游览后面关卡，是否解锁？","一包金币包含500金币，是否购买？",
		"便宜出售一桶金币包含1000金币，是否购买？","超值优惠一箱金币，惊爆包含1500金币，是否购买?"};
	
	
	
	public static void sms_init(Activity activity){
		
	}
	
	public static void sms_exit(Activity activity){
			
	}
	
	public static void sendSms(int smsChargeID){
		chargeID = smsChargeID;
		Message msg = new Message();
		msg.what = HANDLER_MSG_SEND_SMS;
		GameMainActivity.mHandler.sendMessage(msg);
	}
	
	public static void onBillingSucc(){
		int gameMoney = 0;
		switch(chargeID){
		case SMS_REG:
			DB.db.setIsRegister(1);
			break;
		case SMS_UNLOCK:
			DB.db.setIsLevelCancelLocked(1);
			break;
		case SMS_BUYMONEY_1:
		case SMS_BUYMONEY_2:
		case SMS_BUYMONEY_3:
			gameMoney = chargeValue[chargeID-SMS_BUYMONEY_1];
			ShopView.gameCurmoney += gameMoney;
			DB.db.setMoney(ShopView.gameCurmoney);
			DB.db.saveDB();
			break;
		}
		
	}
	
	public static void onBillingFail(){
		
	}
	
	public static void checlFeesDialog(){
		
		/*AlertDialog.Builder ab = new AlertDialog.Builder(GameMainActivity.bffa);
		Button but = new Button(GameMainActivity.bffa);
		TextView tview = new TextView(GameMainActivity.bffa);
		
		ab.setTitle(SmsName[chargeID]);
		String context = SmsDISC[chargeID];
		tview.setText(context.toCharArray(), 0, context.length());
		ab.setView(tview);
		ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//checkFees(); //计费版本
				onBillingSucc();//无计费版本	
			}
		});
		ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				onBillingFail();
			}
		});
		ab.create();
		ab.setCancelable(false);
		ab.show();*/
		onBillingSucc();//无计费版本	
		
		
	}
	
	
}
