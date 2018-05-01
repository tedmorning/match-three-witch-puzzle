package com.tuowei.obj;

public class ChangeObj {
	public int ii, jj, iTemp, jTemp, ik, jk;
	public int direct;// 1--2--3--4上下左右
//	public int temp1 = -1, temp111;

	/** 0开始点下，1确认了移动中，2带确认中，3取消中 */
	public int status;
	
	public boolean isRandom;
	public boolean backRandom;
}
