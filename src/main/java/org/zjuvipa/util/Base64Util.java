package org.zjuvipa.util;
import java.util.Base64;

import javax.imageio.stream.FileImageInputStream;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Base64Util {
    /**
     * base64转文件并输出到指定目录
     * @param base64Str
     * @param fileName
     * @param filePath
     * @return
     */
    public static byte[] decode(String base64Str,String fileName,String filePath){
        File file = null;
        //创建文件目录
        File  dir=new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        java.io.FileOutputStream fos = null;

        byte[] b = null;
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
            b = Base64.getDecoder().decode(replaceEnter(base64Str));
            //window
            //file=new File(filePath+"\\"+fileName);
            //linux
            file=new File(filePath+"/"+fileName);
            fos = new java.io.FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return b;
    }

    /**
     * 图片转字符串
     * @param image
     * @return
     */
    public static String encode(byte[] image){
        String encode = new String(Base64.getEncoder().encode(image));
        return replaceEnter(encode);
    }

    public static String encode(String uri){
        String encode = new String(Base64.getEncoder().encode(uri.getBytes()));
        return replaceEnter(encode);
    }

    /**
     *
     * @path    图片路径
     * @return
     */

    public static byte[] imageTobyte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while((numBytesRead = input.read(buf)) != -1){
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }
}
