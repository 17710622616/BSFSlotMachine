package com.bs.john_li.bsfslotmachine.BSSMUtils;

import android.util.Log;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密的方法
 */
public class DigestUtils {

	private static final char[] BCD_LOOKUP = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	private static final String HEX_STR = "0123456789ABCDEF";

	public static final String SECRET = "TY27CG61SM95JV18";

	/**
	 * byte to hex
	 * 
	 * @param bcd
	 *            字节数组
	 * @return
	 */
	public static String byte2Hex(byte[] bcd) {
		StringBuilder sb = new StringBuilder(bcd.length * 2);
		for (int i = 0; i < bcd.length; i++) {
			sb.append(BCD_LOOKUP[(bcd[i] >>> 4) & 0x0f]);
			sb.append(BCD_LOOKUP[bcd[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * hex to byte
	 * 
	 * @param hexStr
	 *            hex串
	 * @return
	 */
	public static byte[] hex2Byte(String hexStr) {
		char[] hexs = hexStr.toUpperCase().toCharArray();
		byte[] bytes = new byte[hexStr.length() / 2];
		int iTmp = 0x00;
		for (int i = 0; i < bytes.length; i++) {
			iTmp = HEX_STR.indexOf(hexs[2 * i]) << 4;
			iTmp |= HEX_STR.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (iTmp & 0xFF);
		}
		return bytes;
	}

	/**
	 * AES加密算法
	 * 
	 * @param content
	 *            加密文本
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encryptAES(String content, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");
			// 初始化
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES解密算法
	 * 
	 * @param content
	 *            解密文本
	 * @param password
	 *            解密密码
	 * @return
	 */
	public static String decryptAES(byte[] content, byte[] password) {
		try {
			SecretKeySpec key = new SecretKeySpec(password, "AES");
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 初始化
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 解密
			byte[] result = cipher.doFinal(content);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES加密算法
	 *
	 * @param content
	 *            加密文本
	 * @param password
	 *            加密密码
	 * @return
	 */
	public static byte[] encryptDES(String content, byte[] password) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
			// 获取数据并加密
			byte[] byteContent = content.getBytes("utf-8");
			return cipher.doFinal(byteContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * DES解密算法
	 *
	 * @param content
	 *            解密文本
	 * @param password
	 *            解密密码
	 * @return
	 */
	public static String decryptDES(byte[] content, byte[] password) {
		try {
			// DES算法要求有一个可信任的随机数源
			SecureRandom random = new SecureRandom();
			// 创建一个DESKeySpec对象
			DESKeySpec desKey = new DESKeySpec(password);
			// 创建一个密匙工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// 将DESKeySpec对象转换成SecretKey对象
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成解密操作
			Cipher cipher = Cipher.getInstance("DES");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, securekey, random);
			// 真正开始解密操作
			byte[] result = cipher.doFinal(content);
			return new String(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * MD5加密算法
	 * 
	 * @param content
	 *            加密文本
	 * @return
	 */
	public static String encryptMD5(String content) {
		String re_md5 = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(content.getBytes());
			byte bArr[] = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte b : bArr) {
				i = b;
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	/**
	 * 加密方法
	 * @param pw
	 * @return
	 */
	public static String encryptPw(String pw) {
		String secret = DigestUtils.encryptMD5("VY22HM93SM95AB85");
		String paypwdStr = DigestUtils.byte2Hex(DigestUtils.encryptAES(pw, DigestUtils.hex2Byte(secret)));
		System.out.println("支付密码" + paypwdStr);// 加密串
		return paypwdStr;
	}

	/**
	 * 对字符串md5加密
	 *
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String getMD5Str(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16).toUpperCase();
		} catch (Exception e) {
			//Toast.makeText(this, "MD5加密出现错误", Toast.LENGTH_LONG).show();
			Log.d("digest error", "加密错误");
		}
		return str;
	}

	/**
	 * md5加密
	 * @param buffer
	 * @return
	 */
	public final static String getMessageDigest(byte[] buffer) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(buffer);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
}
