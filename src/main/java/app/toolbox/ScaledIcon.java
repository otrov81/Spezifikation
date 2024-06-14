package app.toolbox;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScaledIcon {

    public static Image getScaledImage(Image srcImg, int targetHeight) {
        double aspectRatio = (double) srcImg.getWidth(null) / srcImg.getHeight(null);
        int targetWidth = (int) (targetHeight * aspectRatio);

        BufferedImage resizedImg = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, targetWidth, targetHeight, null);
        g2.dispose();

        return resizedImg;
    }
}
