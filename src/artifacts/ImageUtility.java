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
 * JOEMAR N. DE LA CRUZ
 * GRETHEL EINSTEIN BERNARDINO
 *
 * OTHER LIBRARIES THAT ARE USED BELONGS TO THEIR RESPECTFUL OWNERS AND AUTHORS.
 * NO COPYRIGHT ARE INTENTIONAL OR INTENDED.
 * THIS PROJECT IS NOT PROFITABLE HENCE FOR EDUCATIONAL PURPOSES ONLY.
 * THIS PROJECT IS ONLY FOR COMPLIANCE TO OUR REQUIREMENTS.
 * THIS PROJECT DOES NOT INCLUDE DISTRIBUTION FOR OTHER PURPOSES.
 *
 */
package artifacts;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Joemar
 */
public class ImageUtility {
    
    private static String DEFAULT_IMAGE = "src/res/img/image_utility/default_image.png";
    private static String DEFAULT_IMAGE_BLUE = "src/res/img/image_utility/default_image_blue.png";
    
    /**
     * 
     * @param imageView
     * @param view 0 = green background of default image; 1 = blue 
     */
    public static void addDefaultImageToFx(ImageView imageView, int view) {
        try {
            Image image = new Image(new File((view==0? DEFAULT_IMAGE :( view==1? DEFAULT_IMAGE_BLUE : DEFAULT_IMAGE))).toURI().toURL().toString());
            imageView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param tempProfilePath temp directory where the downloaded to image will be stored
     * @param ftpFolder folder name where the image location is saved
     * @param imageFilename name of the image from the database
     * @param imageView where the image will be shown
     * @param defaultView
     */
    public static void addImageToFX(String tempProfilePath, String ftpFolder, String imageFilename, ImageView imageView, int defaultView) {
            String tempProfileImagePath = tempProfilePath + "/" + imageFilename;
            File tempProfileDir = new File(tempProfilePath);
            File tempProfileImage = new File(tempProfileImagePath);
            try {
                FileUtils.forceMkdir(tempProfileDir);
                boolean isDownloaded = FTPManager.download(ftpFolder, imageFilename, tempProfileImage.getAbsolutePath());
                System.out.println(isDownloaded);
                if(!isDownloaded) { 
                    addDefaultImageToFx(imageView, defaultView);
                    return;
                }
                try {
                    Image img = new Image((tempProfileImage.toURI().toURL().toExternalForm()));
                    imageView.setImage(img);
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
    }
}
