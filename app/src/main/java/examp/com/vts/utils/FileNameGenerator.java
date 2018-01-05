package examp.com.vts.utils;

/**
 * Created by a55 on 2017/12/26.
 */

public interface FileNameGenerator {

    /** Generates unique file name for image defined by URI */
    String generate(String imageUri);
}
