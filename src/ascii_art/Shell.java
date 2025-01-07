package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Class for Shell - Shell program that handles AsciiArt requests.
 */
public class Shell {
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private final BrightnessImgCharMatcher brightnessImgCharMatcher;
    private final AsciiOutput asciiOutput;
    private boolean console = false;
    private Set<Character>  charSet = new HashSet<>();

    private static final String ENTER_CMD_GUI = ">>>";
    private final Scanner input = new Scanner(System.in);
    private static final char FIRST_ASCII = ' ';
    private static final char LAST_ASCII = '~';
    private static final String SHOW_CHARS = "chars";
    private static final String ADD_CHARS = "add";
    private static final String REMOVE_CHARS = "remove";
    private static final String CHANGE_RESOLUTION = "res";
    private static final String RENDER = "render";
    private static final String CONSOLE = "console";
    private static final String EXIT_SHELL = "exit";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INITIAL_CHARS_RANGE = "0-9";
    private static final String WIDTH_SET_TO = "Width set to: ";
    private static final String RESOLUTION_UP = "up";
    private static final String RESOLUTION_DOWN = "down";
    private static final String RESOLUTION_ERR_MSG = "Did not change due to exceeding boundaries";
    private static final String ERROR_INVALID_ADD_FORMAT = "Did not add due to incorrect format";
    private static final String ERROR_INVALID_REMOVE_FORMAT = "Did not remove due to incorrect format";
    private static final String INVALID_COMMAND_ERROR = "Did not executed due to incorrect command";
    public static final String ALL_CHARS = "all";
    public static final char SPACE_CHAR = ' ';
    public static final char CHAR_HYPHEN = '-';
    public static final String SPACE = " ";
    private static final int RESOLUTION_FACTOR = 2;

    /**
     * Constructor for Shell Class.
     * @param img the Image to make AsciiArt from
     */
    public Shell(Image img) {
        minCharsInRow = Math.max(1, img.getWidth()/img.getHeight());
        maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW,maxCharsInRow), minCharsInRow);
        brightnessImgCharMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        asciiOutput = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        addChars(INITIAL_CHARS_RANGE);
    }

    /**
     *
     * @param charParam the chars parameter given to add/remove commands.
     * @param isAddOperation Boolean to distinguish between add or remove operations to decide the ERROR message to
     * print in case of invalid add/remove commands with wrong parameter syntax.
     * @return an array of 2 chars that represents the range of Ascii chars add/remove from the charSet.
     */
    private static char[] parseCharData(String charParam, boolean isAddOperation) {
        switch (charParam) {
            case (SPACE):
                return new char[]{SPACE_CHAR, SPACE_CHAR};
            case (ALL_CHARS):
                return new char[]{FIRST_ASCII, LAST_ASCII};
        }
        if (charParam.length() == 1 && charParam.charAt(0) != SPACE_CHAR) {
            return new char[]{charParam.charAt(0), charParam.charAt(0)};
        }
        else if (charParam.length() == 3 && charParam.charAt(1) == CHAR_HYPHEN)
        {
            if (charParam.charAt(0) < charParam.charAt(2)){
                return new char[]{charParam.charAt(0), charParam.charAt(2)};
            }
            return new char[]{charParam.charAt(2), charParam.charAt(0)};
        }
        else {
            if (isAddOperation)
            {
                System.out.println(ERROR_INVALID_ADD_FORMAT);
            }
            else {
                System.out.println(ERROR_INVALID_REMOVE_FORMAT);
            }
            return null;
        }
    }

    /**
     * prints all chars to the Shell window.
     */
    private void showChars(){
        charSet.stream().sorted().forEach(character -> System.out.print(character + " "));
        System.out.println();
    }

    /**
     * Adds the chars (range or single Ascii chars) to the charSet
     * @param addParam String represents the parameter given as chars to add to the charSet.
     */
    private void addChars(String addParam) {
        char[] range = parseCharData(addParam, true);
        if(range != null){
            for (int i = range[0]; i <= range[1]; i++) {
                char c = (char) i;
                charSet.add(c);
            }
        }
    }

    /**
     * Removes the chars (range or single Ascii chars) from the charSet
     * @param removeParam String represents the parameter given as chars to remove.
     */
    private void removeChars(String removeParam) {
        char[] range = parseCharData(removeParam, false);
        if(range != null){
            for (int i = range[0]; i <= range[1]; i++) {
                char c = (char) i;
                charSet.remove(c);
            }
        }
    }

    /**
     * Changes the resolution according to the user's input ( res up/down ) if "res up" resolution will be doubled by 2
     * if "res down" the resolution will be divided by factor of 2.
     * @param changeResolutionParam  up/down string that represents the change to be made to resolution.
     */
    private void changeResolutionLogic(String changeResolutionParam) {
        switch (changeResolutionParam) {
            case RESOLUTION_DOWN:
                if (charsInRow / RESOLUTION_FACTOR >= minCharsInRow)
                {
                    charsInRow /= RESOLUTION_FACTOR;
                    System.out.println(WIDTH_SET_TO + charsInRow);
                }
                else {
                    System.out.println(RESOLUTION_ERR_MSG);
                }
                break;
            case RESOLUTION_UP:
                if (charsInRow * RESOLUTION_FACTOR <= maxCharsInRow)
                {
                    charsInRow *= RESOLUTION_FACTOR;
                    System.out.println(WIDTH_SET_TO + charsInRow);
                }
                else {
                    System.out.println(RESOLUTION_ERR_MSG);
                }
                break;
        }
    }

    /**
     * renders the image as AsciiArt to the given location (console/out.html) if console is typed by the user the
     * rendering will be performed also to console and not just the out.html.
     */
    private void render(){
        Character[] arr = new Character[charSet.size()];
        char[][] charMatrix = brightnessImgCharMatcher.chooseChars(charsInRow, charSet.toArray(arr));
        if (console) {
            for (char[] charRow : charMatrix) {
                for (char character : charRow) {
                    System.out.print(character);
                }
                System.out.println();
            }
        }
        else {
            asciiOutput.output(charMatrix);
        }
    }

    /**
     * runs the Shell of AsciiArt creation program.
     * Handles all kind of commands: add, remove, chars, render, console res up, res down.
     * Prints errors.
     */
    public void run() {
        System.out.println(ENTER_CMD_GUI);
        String command = input.next().trim();
        while (!(command.equals(EXIT_SHELL)))
        {
            String parameters = input.nextLine().trim();
            switch (command) {
                case ADD_CHARS:
                    addChars(parameters);
                    break;
                case REMOVE_CHARS:
                    removeChars(parameters);
                    break;
                case SHOW_CHARS:
                    if (!(parameters.length() > 0)) {
                        showChars();
                        break;
                    }
                    System.out.println(INVALID_COMMAND_ERROR);
                    break;
                case RENDER:
                    if (parameters.length() > 0){
                        System.out.println(INVALID_COMMAND_ERROR);
                        break;
                    }
                    else if (charSet.isEmpty()) {
                        break;
                    }
                    render();
                case CONSOLE:
                    if (parameters.length() > 0){
                        System.out.println(INVALID_COMMAND_ERROR);
                        break;
                    }
                    console = true;
                    break;
                case CHANGE_RESOLUTION:
                    changeResolutionLogic(parameters);
                    break;
                default:
                    System.out.println(INVALID_COMMAND_ERROR);
            }
            System.out.print(ENTER_CMD_GUI);
            command = input.next();
        }
    }
}
