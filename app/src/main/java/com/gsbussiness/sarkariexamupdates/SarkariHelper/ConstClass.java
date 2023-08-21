package com.gsbussiness.sarkariexamupdates.SarkariHelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ConstClass {
    static {
        System.loadLibrary("hello-jni");
    }
    public static String PARA_TOKEN ="token";
    public static String PARA_FIRE_TOKEN ="fireToken";

    public static native String baseurl();
    public static String baseurl=baseurl();

    public static native String categoryurl();
    public static String categoryurl=categoryurl();

    public static native String getnotification();
    public static String getnotification=getnotification();

    public static native String getpostdetails();
    public static String getpostdetails=getpostdetails();

    public static native String appkey();
    public static String appkey=appkey();

    public static native String ivkey();
    public static String ivkey=ivkey();

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void ShowToast(Context cntx, String message) {
        Toast.makeText(cntx, message, Toast.LENGTH_LONG).show();
    }

    public static String decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
          //  Log.v(":::JWT_DECODED", "Header: " + getJson(split[0]));
           // Log.v(":::JWT_DECODED", "Body: " + getJson(split[1]));
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
        }
        return "";
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1),
                    16);
            int low = Integer.parseInt(
                    hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String decryptMsg(byte[] cipherText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {

        try {
            IvParameterSpec iv = new IvParameterSpec(ivkey.getBytes());
            SecretKeySpec skeySpec = new SecretKeySpec(appkey.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(cipherText);

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
