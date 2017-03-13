package thin.framework.util;

import java.io.*;

/**
 * Created by yangyu on 2017/2/7.
 */
public final class StreamUtil {

    public static String getString(InputStream is){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream){
        int length;
        byte[] bytes = new byte[8192];
        BufferedInputStream buffer = new BufferedInputStream(inputStream);
        try {
            while ((length=buffer.read(bytes))!=-1){
                outputStream.write(bytes,0,length);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
