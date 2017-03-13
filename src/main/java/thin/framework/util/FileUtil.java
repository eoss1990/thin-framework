package thin.framework.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangyu on 2017/2/13.
 */
public final class FileUtil {

    public static String getRealFileName(String fileName){
        return FilenameUtils.getName(fileName);
    }

    public static File createFile(String filePath){
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()){
            try {
                FileUtils.forceMkdir(parentDir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return file;
    }
}
