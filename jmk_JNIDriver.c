#include <unistd.h>
#include <fcntl.h>
#include <stdio.h>
#include <assert.h>
#include <jni.h>
#include <string.h>
#include "font.h"
#include "textlcd.h"


static int segfd;
static int ledfd;
static int lcdfd;
static int dotfd;

void intTostr(int val, char* dec){
	int i = 0;
	while(i <= 5 || val != 0){
		dec[5-i] = val % 10 + 48;
		val /= 10;
		i++;
	}
}

JNIEXPORT jint JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_segopen
  (JNIEnv * env, jobject obj){
		segfd = open("/dev/fpga_segment", O_WRONLY);
		assert(segfd != -1);
		return segfd;
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_segprint
  (JNIEnv * env, jobject obj, jint num){
	char dec[7] = "000000";
	intTostr(num,dec);
	int i;
	/*for(i = 0 ; i < 100 ; i++){
		write(segfd, dec, strlen(dec));
	}*/
	write(segfd, dec, strlen(dec));
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_segclose
  (JNIEnv * env, jobject obj){
	close(segfd);
}

JNIEXPORT jint JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_ledopen
  (JNIEnv * env, jobject obj){
	ledfd = open("/dev/fpga_led", O_WRONLY);
	assert(ledfd != -1);
	return ledfd;
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_ledon
  (JNIEnv * env, jobject obj, jint data){
	int val;
	unsigned char ch;
	switch(data){
	case 0:
		val = 0;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;
	case 1:
		val = 128;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;
	case 2:
		val = 192;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;
	case 3:
		val = 224;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;
	case 4:
		val = 240;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;
	case 5:
		val = 248;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;

	case 10:
		val = 85;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;

	case 11:
		val = 170;
		ch = (unsigned char)val;
		write(ledfd, &ch, 1);
		break;

		}
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_ledclose
  (JNIEnv * env, jobject obj){
	close(ledfd);
}

JNIEXPORT jint JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_dotopen
  (JNIEnv * env, jobject obj){
	dotfd = open("/dev/fpga_dotmatrix", O_RDWR | O_SYNC);
	return dotfd;
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_dotprint
  (JNIEnv * env, jobject thiz, jstring str){
	int dev, i, j, offset = 20, ch, len;
	char result[600], tmp[2];
	const char *pStr;
	pStr = (*env)->GetStringUTFChars(env, str, 0);
	len = (*env)->GetStringLength(env, str);
	for (j = 0; j < 20; j++)
			result[j] = '0';

			for (i = 0; i < len; i++) {
				ch = pStr[i];

				ch -= 0x20;

				for (j = 0; j < 5; j++) {
					sprintf(tmp, "%x%x", font[ch][j] / 16, font[ch][j] % 16);

					result[offset++] = tmp[0];
					result[offset++] = tmp[1];
				}
				result[offset++] = '0';
				result[offset++] = '0';
			}

			for (j = 0; j < 20; j++)
				result[offset++] = '0';

			for (i = 0; i < (offset - 18) / 2; i++) {
				for (j = 0; j < 20; j++) {
					write(dotfd, &result[2 * i], 20);
				}
			}
	(*env)->ReleaseStringUTFChars(env, str, pStr);
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_dotclose
  (JNIEnv * env, jobject obj){
	close(dotfd);
}

JNIEXPORT jint JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdopen
  (JNIEnv * env, jobject obj){
	lcdfd = open("/dev/fpga_textlcd", O_WRONLY);
	assert(lcdfd != -1);
	ioctl(lcdfd, TEXTLCD_ON);
	return lcdfd;
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdinitialize
  (JNIEnv * env, jobject obj){
	lcdfd = open("/dev/fpga_textlcd", O_WRONLY);
	assert(lcdfd != -1);
	ioctl(lcdfd, TEXTLCD_INIT);
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdclear
  (JNIEnv * env, jobject obj){
	//if (fd )
		ioctl(lcdfd, TEXTLCD_CLEAR);
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdprint1Line
  (JNIEnv * env, jobject obj, jstring msg){
	const char *str;

	if (lcdfd )
	{
		str = (*env)->GetStringUTFChars(env, msg, 0);
		ioctl(lcdfd, TEXTLCD_LINE1);
		write(lcdfd, str, strlen(str));
		(*env)->ReleaseStringUTFChars(env, msg, str);
	}

}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdprint2Line
  (JNIEnv * env, jobject obj, jstring msg){
	const char *str;

	if (lcdfd )
	{
		str = (*env)->GetStringUTFChars(env, msg, 0);
		ioctl(lcdfd, TEXTLCD_LINE2);
		write(lcdfd, str, strlen(str));
		(*env)->ReleaseStringUTFChars(env, msg, str);
	}
}

JNIEXPORT void JNICALL Java_edu_skku_jmk_1project_1jnidriver_jmk_1JNIDriver_lcdoff
  (JNIEnv * env, jobject obj){
	if (lcdfd )
		{
			ioctl(lcdfd, TEXTLCD_OFF);
			close(lcdfd);
		}

		lcdfd = 0;
}



