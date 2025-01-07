# ASCII Art Converter Project

## Overview
This project is an ASCII Art Converter that transforms image files into visually striking ASCII art representations. Users can specify the input image path, select ASCII characters for conversion, and define the desired resolution. The output can be displayed in the console or saved as an HTML file. Additionally, the project includes a user interface to simplify interaction.

## Features
- Converts images to ASCII art using customizable character sets.
- Adjustable resolution to control the detail level of the output.
- Supports console display and HTML file output.
- User-friendly interface for selecting conversion parameters.

## How It Works
### Image Representation
Images are processed as two-dimensional arrays of pixels, where each pixel has color values for Red (R), Green (G), and Blue (B) channels. Each color value ranges from 0 to 255. The brightness of each pixel is used to determine the corresponding ASCII character.

### ASCII Characters
ASCII characters are mapped to pixel brightness levels. Characters with higher brightness values appear lighter, while those with lower values appear darker. The character set used in the conversion process impacts the visual effect of the resulting ASCII art.

## Conversion Process
1. **Preprocessing:**
   - Calculate brightness values for the selected ASCII characters.
   - Normalize brightness values to a scale of 0 to 1.

2. **Image Padding:**
   - Pad the image with white pixels to make its dimensions a power of two.

3. **Subdivision:**
   - Divide the padded image into smaller sub-images based on the desired resolution.

4. **Character Mapping:**
   - Calculate the brightness of each sub-image.
   - Replace each sub-image with the ASCII character that best matches its brightness.

## Project Structure
The project is organized into the following packages:

1. **`image` package:**
   - Handles image processing tasks like padding and division.

2. **`ascii_art` package:**
   - Contains the core conversion algorithm.

3. **`image_char_matching` package:**
   - Matches sub-images to the closest ASCII characters based on brightness.

4. **`ascii_output` package:**
   - Manages output display and file saving options.

## Provided Code
- **`KeyboardInput` Class:** Facilitates user input from the console.
- **`CharConverter` Class:** Converts ASCII characters to 16x16 boolean arrays representing their black-and-white images.
- **`Image` Class:** Represents an image object in the application.
- **`AsciiOutput` Interface:** Defines methods for outputting ASCII art.

## User Interface
The user interface allows users to:
1. Select an image file for conversion.
2. Choose a set of ASCII characters.
3. Set the desired resolution.
4. View the ASCII art in the console or save it as an HTML file.

## Default Settings
- **Image:** `cat.jpeg` (included in resources).
- **Character Set:** Digits 0-9.
- **Resolution:** 128 characters per row.


