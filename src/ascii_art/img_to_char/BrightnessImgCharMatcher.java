package ascii_art.img_to_char;

import java.awt.*;
import image.Image;
import java.util.HashMap;

/**
 * BrightnessImgCharMatcher class - matches between chars and brightness and converts the Original image to Ascii Art
 * according to given parameters.
 */
public class BrightnessImgCharMatcher {
    private static final int MAX_RGB_VAL = 255;
    private static final int RESOLUTION = 16;
    private static final double RED_TO_GREY_VAL = 0.2126;
    private static final double GREEN_TO_GREY_VAL = 0.7152;
    private static final double BLUE_TO_GRAY_VAL = 0.0722;
    private final Image image;
    private final String font;
    private final HashMap<Image, Float> subImageBrightnessDict = new HashMap<>();
    /**
     * Constructor for BrightnessImgCharMatcher.
     * @param image Image object
     * @param font Font String
     */
    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     *
     * @param numCharsInRow number of chars in each row
     * @param charSet the charSet - set of given Ascii Characters
     * @return A 2D Array of chars that represents the AsciiArt Image.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet){
        float [] charToBrightness = translateCharToBrightness(charSet);
        float[] charBrightnessLinearStretched = linearStretch(charToBrightness);
        return getAsciiArtCharArray(numCharsInRow, charSet, charBrightnessLinearStretched);
    }

    /**
     * translates the Ascii Characters to float values which represents brightness value.
     * @param charSet the charSet - set of given Ascii Characters
     * @return an array of floats represents the ascii characters to brightness float values.
     */
    private float[] translateCharToBrightness(Character[] charSet){
         float[] brightnessArr = new float[charSet.length];
         if (charSet.length == 0){
             return brightnessArr;
         }
         for (int i = 0; i < charSet.length; i++) {
             int whiteCounter = getWhitePixelCount(charSet[i]);
             brightnessArr[i] = (float)whiteCounter / (RESOLUTION*RESOLUTION);
         }
         return brightnessArr;
    }

    /**
     * Gets the total number of white Color pixels in the given character when it is rendered.
     * @param character An Ascii Character
     * @return Returns the white pixel counter for specific Character
     */
    private int getWhitePixelCount(Character character) {
        int whiteCounter = 0;
        boolean[][] boolMatrix = CharRenderer.getImg(character, RESOLUTION, font);
        for (boolean[] boolRow : boolMatrix) {
            for (int k = 0; k < boolMatrix[0].length; k++) {
                if (boolRow[k]) {
                    whiteCounter++;
                }
            }
        }
        return whiteCounter;
    }

    /**
     *
     * @param charBrightnessArray character brightness array
     * @return a linear stretched array of floats represnts
     */
    private static float[] linearStretch(float[] charBrightnessArray) {
        float maxBrightness = 0;
        float minBrightness = 1;
        for (int i = 0; i < charBrightnessArray.length; i++) {
            if (charBrightnessArray[i] < minBrightness) {
                minBrightness = charBrightnessArray[i];
            }
            if (charBrightnessArray[i] > maxBrightness) {
                maxBrightness = charBrightnessArray[i];
            }
        }
        float[] linearStretchedArray = new float[charBrightnessArray.length];
        if (charBrightnessArray.length == 0)
        {
            return linearStretchedArray;
        }
        for (int i = 0; i < linearStretchedArray.length; i++) {
            linearStretchedArray[i] = (charBrightnessArray[i] - minBrightness) / (maxBrightness - minBrightness);
        }
        return linearStretchedArray;
    }

    /**
     *
     * @param subImage a SubImage object
     * @return Average Brightness of a subimage
     */
    private float imageAverageBrightness(Image subImage){
        if (subImageBrightnessDict.containsKey(subImage)){
            return subImageBrightnessDict.get(subImage);
        }
        float grayPixelSum = 0;
        int pixelCounter = 0;
        for (Color pixel : subImage.pixels()) {
             grayPixelSum += (pixel.getRed() * RED_TO_GREY_VAL + pixel.getGreen() * GREEN_TO_GREY_VAL +
                     pixel.getBlue() * BLUE_TO_GRAY_VAL) / MAX_RGB_VAL;
             pixelCounter++;
        }
        subImageBrightnessDict.put(subImage, grayPixelSum / pixelCounter);
        return grayPixelSum / pixelCounter;
    }

    /**
     *
     * @param numCharsInRow number of chars in each row
     * @param charSet Set of Ascii Characters
     * @param charBrightnessLinearStretched An array of linear stretched float values represents brightness level
     * of each Ascii Char belongs to the charSet.
     * @return char 2D Array that represents the AsciiArt Image
     */
    private char[][] getAsciiArtCharArray(int numCharsInRow, Character[] charSet, float[] charBrightnessLinearStretched)
    {
        int subImageSize = image.getWidth() / numCharsInRow;
        int charRows = image.getHeight()/subImageSize;
        int charCols = image.getWidth()/subImageSize;
        char[][] asciiConvertedImage = new char[charRows][charCols];
        if (charSet.length == 0){
            return asciiConvertedImage;
        }
        int row = 0 , col = 0;
        for(Image subImage : image.SubimageIterator(subImageSize)) {
            float subImgAvgBrightness = imageAverageBrightness(subImage);
            int bestCharIndex = 0;
            float bestDistance = 1;
            for (int currIdx = 0; currIdx < charBrightnessLinearStretched.length; currIdx++) {
                float distance = charBrightnessLinearStretched[currIdx] - subImgAvgBrightness;
                if (Math.abs(distance) < bestDistance){
                    bestDistance = Math.abs(distance);
                    bestCharIndex = currIdx;
                }
            }
            asciiConvertedImage[row][col] = charSet[bestCharIndex];
            col++;
            if (col == numCharsInRow){
                col = 0;
                row++;
            }
        }
        return asciiConvertedImage;
    }
}
