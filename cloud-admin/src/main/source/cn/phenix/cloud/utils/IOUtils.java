package cn.phenix.cloud.utils;

import jodd.io.StreamUtil;

import java.io.*;
import java.util.zip.CheckedInputStream;

/**
 * @author xiaobin
 * @create 2018-01-17 下午5:34
 **/
public class IOUtils {
    /**
     * 根据文件获取crc64
     * -1180816182260372658
     *
     * @param file
     * @return
     */
    public static long getCRC64(File file) throws IOException {
        if (!file.exists()) {
            return 0;
        }
        CRC64 crc64 = new CRC64();
        byte[] bys = new byte[8192];
        try (FileInputStream fileinputstream = new FileInputStream(file);
             CheckedInputStream checkedinputstream = new CheckedInputStream(fileinputstream, crc64)) {
            int data = checkedinputstream.read(bys);
            while (data != -1) {
                data = checkedinputstream.read(bys);
            }
            return crc64.getValue();
        }
    }

    public static void out(OutputStream out, InputStream in) throws IOException {
        try {
            byte buffer[] = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        } finally {
            StreamUtil.close(in);
            StreamUtil.close(out);
        }
    }

    public static void close(InputStream inputStream){
        org.apache.commons.io.IOUtils.closeQuietly(inputStream);
    }

    //6814133478917465791
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/xiaobin/demo/11.mp4");
        //InputStream inputStream = new FileInputStream(file);
        //InputStream checkInput = new CheckedInputStream(inputStream,new CRC64());
        System.out.println(getCRC64(file));
    }
}
