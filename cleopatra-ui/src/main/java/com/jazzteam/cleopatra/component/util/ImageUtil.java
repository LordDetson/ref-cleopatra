package com.jazzteam.cleopatra.component.util;

import com.jazzteam.cleopatra.component.util.constant.ErrorMessages;
import lombok.NonNull;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Objects;

public final class ImageUtil {

    private ImageUtil() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.INIT_STATIC_CLASS_ERROR.getMessage());
    }

    public static ImageIcon getIcon(@NonNull String fileNameImg) {
        return new ImageIcon(Objects
                .requireNonNull(SystemUiSettingsUtil.class
                        .getClassLoader()
                        .getResource(fileNameImg)));
    }

    public static BufferedImage createFlippedImage(BufferedImage image) {

        AffineTransform transform = new AffineTransform();
        transform.concatenate(AffineTransform.getScaleInstance(1, -1));
        transform.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));

        return createTransformed(image, transform);
    }

    private static BufferedImage createTransformed(BufferedImage image, AffineTransform at) {

        BufferedImage newImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.transform(at);
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        return newImage;
    }

    @SneakyThrows
    public static BufferedImage getBufferedImage(@NonNull String fileNameImg) {
        return ImageIO.read(Objects.requireNonNull(Thread.currentThread()
                .getContextClassLoader()
                .getResource(fileNameImg)));
    }

}
