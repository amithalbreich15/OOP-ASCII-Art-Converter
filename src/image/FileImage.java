package image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * A package-private class of the package image.
 * @author Dan Nirel
 */
class FileImage implements Image {
    private static final Color DEFAULT_COLOR = Color.WHITE;
    private final int imageWidth;
    private final int imageHeight;

    private final Color[][] pixelArray;

    /**
     * Constructor for FileImage - Builds an FileImage object and fills the Color array with correct Pixels, if padding
     * is needed to complete the image dimensions to a Power of 2, the Image will be padded with zeros e.g White Color
     * Pixels.
     * @param filename file's name
     * @throws IOException
     */
    public FileImage(String filename) throws IOException {
        java.awt.image.BufferedImage im = ImageIO.read(new File(filename));
        int origWidth = im.getWidth(), origHeight = im.getHeight();
        //im.getRGB(x, y)); getter for access to a specific RGB rates
        if (isPowerOfTwo(origHeight) && isPowerOfTwo(origWidth)) {
            this.imageWidth = origWidth;
            this.imageHeight = origHeight;
            pixelArray = new Color[imageHeight][imageWidth];
            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    pixelArray[i][j] = new Color(im.getRGB(i, j));
                }
            }
        }
        else {
            int newWidth = findClosestPowerOfTwo(origWidth);
            int newHeight = findClosestPowerOfTwo(origHeight);
            pixelArray = new Color[newHeight][newWidth];
            this.imageHeight = newHeight;
            this.imageWidth = newWidth;
            int toPadRows = (newHeight - origHeight) / 2;
            int toPadCols = (newWidth - origWidth) / 2;
            for (int i = 0; i < newHeight; i++) {
                for (int j = 0; j < newWidth; j++) {
                    if (j < toPadCols || j >= (newWidth - toPadCols) || i < toPadRows || i >= (newHeight - toPadRows)) {
                        pixelArray[i][j] = DEFAULT_COLOR;
                    }
                    else {
                        pixelArray[i][j] = new Color(im.getRGB( j - toPadCols, i - toPadRows));
                    }
                }
            }
        }
    }

    /**
     * Getter for FileImage Width
     * @return FileImage Width
     */
    @Override
    public int getWidth() {
        return this.imageWidth;
    }

    /**
     * Getter for FileImage Height
     * @return FileImage Height
     */
    @Override
    public int getHeight() {
        return this.imageHeight;
    }

    /**
     * Getter for Color Pixel
     * @param x x coordinate for row
     * @param y y coordinate for col
     * @return A color Type represents Pixel
     */
    @Override
    public Color getPixel(int x, int y) {
        return pixelArray[x][y];
    }

    /**
     * Finds the Closest greater Power of 2 of a specific number
     * @param dimensionLength The dimension length of the row/col of the image.
     * @return  The Closest greater Power of 2 of a specific number
     */
    private static int findClosestPowerOfTwo(int dimensionLength) {
        int closestPower = 1;
        while (closestPower < dimensionLength)
        {
            closestPower = closestPower << 1;
        }
        return  closestPower;
    }

    /**
     * Checks if a given number is a power of 2.
     * @param number An int number
     * @return True if the number is power of 2, false otherwise.
     */
    public static boolean isPowerOfTwo(int number) {
        if (number <= 0) {
            return false;
        }
        return (number & (number - 1)) == 0;
    }
}
