package edu.skku.jmk_project_jnidriver;


public class jmk_JNIDriver {
	static {
		System.loadLibrary("jmk_JNIDriver");
	}
	public native int segopen();
	public native void segprint(int num);
	public native void segclose();
	public native int ledopen();
	public native void ledon(int data);
	public native void ledclose();
	public native int dotopen();
	public native void dotprint(String str);
	public native void dotclose();
	public native int lcdopen();
	public native void lcdinitialize();
	public native void lcdclear();
	public native void lcdprint1Line(String str);
	public native void lcdprint2Line(String str);
	public native void lcdoff();

}
