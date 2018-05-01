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
	public static final int SMS_BUYMONEY_1 = 2; //3Ԫ
	public static final int SMS_BUYMONEY_2 = 3; //5Ԫ
	public static final int SMS_BUYMONEY_3 = 4; //6Ԫ
	
	public static String SmsName[] = {"ע�ἤ��","�����ؿ�","һ�����","һͰ���","һ����"};
	public static String SmsDISC[] = {"���ʾ��鼴���������Ͽ�ע���!","�����ؿ�,������ֱ����������ؿ����Ƿ������","һ����Ұ���500��ң��Ƿ���",
		"���˳���һͰ��Ұ���1000��ң��Ƿ���","��ֵ�Ż�һ���ң���������1500��ң��Ƿ���?"};
	
	
	
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
		ab.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//checkFees(); //�ƷѰ汾
				onBillingSucc();//�޼ƷѰ汾	
			}
		});
		ab.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				onBillingFail();
			}
		});
		ab.create();
		ab.setCancelable(false);
		ab.show();*/
		onBillingSucc();//�޼ƷѰ汾	
		
		
	}
	
	
}
