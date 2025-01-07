package image;

import java.awt.*;


/**
 * SubImage class represents a SubImage of the original given Image. Implements Image interface so we could use iterator
 * to iterate over SubImage objects which is identified as Image.
 */
class SubImage implements Image {
    private final int subimageSize;
    private Color[][] subimageColorArr;

    /**
     * Constructor for SubImage
     * @param subimageSize SubImage dimensions row or col or the same size.
     * @param image Image Object
     * @param subimageRowIdx Index of Row in the original Image.
     * @param subimageColIdx Index of Col in the original Image.
     */
    SubImage(int subimageSize, Image image, int subimageRowIdx, int subimageColIdx) {
        this.subimageSize = subimageSize;
        this.subimageColorArr =  new Color[subimageSize][subimageSize];
        for (int i = 0; i < subimageSize; i++) {
            for (int j = 0; j < subimageSize; j++) {
                subimageColorArr[i][j] = image.getPixel(i + subimageRowIdx,j + subimageColIdx);
            }
        }
    }

    /**
     * Getter for Color Pixel
     * @param x x coordinate for row
     * @param y y coordinate for col
     * @return A color Type represents Pixel
     */
    @Override
    public Color getPixel(int x, int y) {
        return subimageColorArr[x][y];
    }

    /**
     * Getter for width
     * @return SubImage Width
     */
    @Override
    public int getWidth() {
        return this.subimageSize;
    }

    /**
     * Getter for height
     * @return SubImage Height
     */
    @Override
    public int getHeight() {
        return this.subimageSize;
    }
}
