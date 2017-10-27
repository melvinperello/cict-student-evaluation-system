/**
 * CAPSTONE PROJECT.
 * BSIT 4A-G1.
 * MONOSYNC TECHNOLOGIES.
 * MONOSYNC FRAMEWORK VERSION 1.0.0 TEACUP RICE ROLL.
 * THIS PROJECT IS PROPRIETARY AND CONFIDENTIAL ANY PART THEREOF.
 * COPYING AND DISTRIBUTION WITHOUT PERMISSION ARE NOT ALLOWED.
 *
 * COLLEGE OF INFORMATION AND COMMUNICATIONS TECHNOLOGY.
 * LINKED SYSTEM.
 *
 * PROJECT MANAGER: JHON MELVIN N. PERELLO
 * DEVELOPERS:
 * JOEMAR N. DELA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package org.cict.reports;

import com.jhmvin.fx.async.SimpleTask;
import java.io.File;
import java.net.URL;
import javafx.application.Platform;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import static org.apache.poi.hssf.usermodel.HeaderFooter.file;
import org.cict.PublicConstants;

/**
 *
 * @author Jhon Melvin
 */
public class ProfileImage {

    // IP Address of server
    private final static String server = PublicConstants.getServer();
    // API to get the image
    private final static String serverApi = "/laravel/linked/public/linked/photo/";
    // saving directory
    private final static String saveDirectory = "temp/images/profile";
    // read deadline 10 seconds
    private final static int readTimeOut = 10000;
    // connection deadline 10 seconds.
    private final static int connTimeOut = 10000;

    private static String getAbsoluteUrl() {
        return "http://" + server + serverApi;
    }

    private static String downloadImage(String imageName) {
        try {
            URL url = new URL(getAbsoluteUrl() + imageName);
            File imageFile = new File(saveDirectory + "/" + imageName);
            FileUtils.copyURLToFile(url, imageFile, connTimeOut, readTimeOut);
            // checks whether the file is an image.
            if (ImageIO.read(imageFile) == null) {
                // not a file
                imageFile.delete();
                return null;
            }
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ImageDownloader {

        void get(String absolutePath);

    }

    private static String PATH;

    public static void download(String imageName, ImageDownloader downloader) {
        PATH = null;
        SimpleTask imageDownload = new SimpleTask("img-download");
        imageDownload.setTask(() -> {
            PATH = downloadImage(imageName);
        });
        imageDownload.whenStarted(() -> {
            //
        });
        imageDownload.whenCancelled(() -> {
            //
        });
        imageDownload.whenFailed(() -> {
            //
        });
        imageDownload.whenSuccess(() -> {
            //
        });
        imageDownload.whenFinished(() -> {

            downloader.get(PATH);
        });
        Platform.runLater(() -> {
            imageDownload.start();
        });

    }

}
