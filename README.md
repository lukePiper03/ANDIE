# ANDIE (A Non Destructive Image Editior)

## COSC202 Group Syntax
**Authors**
- *Luke Piper*
- *Eszter Scarlett-Herbert*
- *Hannah Srzich*
- *Dante Vannini*
- *Lia Walker*

## What is ANDIE
ANDIE is a non-destructive image editor program. A non-destructive image editor stores the original data of the image and maintains a stack (list) of operations applied to the image. This way, once you apply a filter to an image, the program can undo or redo changes and reset the image back to the original copy, as no data on the image is lost.

!["ANDIE"](/pictures/newAndie.png  "Display of ANDIE program")

## Reviews
#### *“People ignore design that ignores people.”* - Frank Chimero, *Designer*

The ultimate goal of Syntax's ANDIE application was to maximise usability for entry level end users. So, each of the Syntax team got their non-computer science major friends to test out our ANDIE application. This feedback allowed us to implement some of the exception/error handling that we otherwise wouldn’t have spotted.

* _"Wow, this feels like an actual application, I cant believe that your team made this. Even though I don’t know what some of the filters mean its fun to be able to preview their effect."_ - Isabel Addison, *Toxicology and Pharmacology Major*

* _"It offers a seamless and intuitive user interface that makes it a joy to use. The wide range of editing tools and features provided is impressive, especially the record features and Dithering filter."_ - Imogen Everette, *LLB and PPE Major*

* _"The ANDIE program is sleek and modern, making it a pleasure to work with. It reminds me of photoshop, but almost offers much more niche filters. Playing around with the sliders is very fun."_ - Izzy Hogg, *Culinary Arts*

## How to Run
To run ANDIE, on any IDE with the program open click run and the GUI window containing ANDIE will appear.

The task bar contains every action you can perform:

**File -** Opening, saving and exporting images<br>

**Language -** Select the language that the programme should be run in. We have provided 5 options: English, Māori, Italian, Samoan, and French<br>

**Edit -** Undo and redo operations performed on image<br>

**View -** Zoom in/out on image<br>

**Transform -** Contains image rotations (90° right, 90° left, and 180°), image flipping (vertical/horizontal), image resizing (50%-200%) and image cropping<br>

**Colour -** Greyscale and brightness/contrast filter, invert filter, hue filter, and the dither filter<br>

**Filter -** Contains mean, median, sharpen, gaussian filter, sobel filter, and the emboss filter<br>

**Record -** Starts a recording to record all actions performed on image. This recording can be saved and later used to perform a series of actions on different images all at once<br>

**Draw Shapes -** Allows you to draw squares, ovals, and lines the image, these shapes can just filled shapes or outlines, and you can select different colours to draw in

## Features of ANDIE

#### *Done By Luke:*
**Image Resize**
The image resize feature allows you to adjust the size of the image from 50% to 200% of its original size. Upon clicking the "Resize Image" button in the transformations menu, a window popup will appear, prompting you to enter the desired image percentage compared to the current size.

**Image rotations**
With the image rotation feature, you can rotate an image 90 degrees to the left or right, as well as 180 degrees. The buttons for these features can be found in the transformations menu.

**Image Flip**
The image flip functionality enables you to flip an image vertically and horizontally. The corresponding buttons for these actions can be accessed in the transformations menu.

**Mouse Selection Region**
The mouse selection region feature allows users to select a specific portion of the screen by dragging their mouse. Once the selection is made, a rectangle will be displayed to indicate the chosen region. This functionality is useful for implementing other features such as the cropImage function and drawing different shapes. Typically, the selected region is outlined with an animated white border. However, if the displayed photo is too light, the outline colour will be black.

**Crop Image**
The crop image feature enables users to selectively crop the image according to their preferences. To begin cropping, users must first select the desired region using the mouse region dragging tool.

#### *Done By Eszter:*
**Gaussian Blur**
The Gaussian filter allows the user to select the radius of the blur that they want, then  blurs an image by replacing each pixel with that provided by the Gaussian function.  The filter applies the convolution operation on the input image and returns the blurred image.

**Sharpen Filter**
The SharpenFilter filter sharpens an image. It creates a  kernel matrix of values for the filter. The filter applies the convolution operation on the input image using the kernel matrix and returns the sharpened image.

**Key Board Shortcuts**
The keyboard shortcuts implemented for each menu actions class provide convenient ways to perform various operations in the application. By utilizing these keyboard shortcuts, users can efficiently apply various transformations to their images, draw shapes, adjust colors, and perform other operations provided by different menu actions classes with faster interaction.

**Toolbar**
The Toolbar class provides a vertical toolbar containing various buttons with icons and associated actions. Thus providing a convenient and visually appealing way for users to access various features, by simply clicking the corresponding buttons on the toolbar.

#### *Done By Hannah:*
**Median Filter**
A Mean filter blurs an image by replacing each centeral pixel by the median of the pixels in a surrounding local neighbourhood of a given radius. A median filter is more effective than convolution when the goal is to simultaneously reduce "salt and pepper" noise and preserve edges.

**Image Export**
The image export feature allows a user to saves an edited image as a png, jpeg and gif. After choosing export image a FileChoose window will appear to allow to name and store location of saved image.

**Extended Filters**
The ImageEdgeExtension class provides methods for dealing with edge cases when applying convolution filters to images. It includes methods to extend an image by adding border pixels, as well as methods to crop an image by removing border pixels. The extension and cropping can be performed by specifying the number of pixels to add or remove on each side of the image. Both operations are applied respectively before and after various image convolutions and blurs that (because of their nature) cannot directly deal with edge pixels.

**Negative Results**
The NormalizeImage class provides methods to deal with negative pixel values in an image. It includes operations such as rescaling the pixel values to a specified range, adjusting the mid-value of negative pixels, and shifting pixel values. If specified it can be applied after a convolution to "normalize" the pixel values.

**Emboss Filter**
The Emboss filter enhances the differences between neighboring pixel values by simulating the effect of embossing through a convolution. The resulting effect creates a 3D-like appearance, as if the image is embossed on a surface. The filter achieves this effect by calculating the gradient of the image and mapping it to a gray-scale range. As such, each of the eight kernels for the image convolution will enhance edges in the direction of the selected convolution masks.

**Sobel Filter**
The Sobel filter applies a convolution operation using two separate kernels, one for horizontal edges and another for vertical edges. As such, it outputs an image highlighting the areas with high gradients, typically representing the edges. The filter calculates the gradient of the image by convolving the image with these kernels. It uses the gradient information to detect edges by finding areas with high gradients.

#### *Done By Dante:*
**Brightness and Constrat Adjustment**
This function allows you to adjust the brightness and contrast of the selected image. Two windows will appear, where you can modify the brightness and contrast individually. You can choose values between -100% and +100%, with negative numbers decreasing the brightness or contrast.

**Macro**
This allows a user to record the operations performed on an image, and save them to a file so that they may be re applied to a different image at any time. The "Record" button starts a recording, meaning that any image operations performed until the recording is stopped will be saved. While recording, a red dot appears next to the recording menu, to signal to the user that the macro is recording. When a user wishes to save the recording, the "Save" button stops the recording, and saves it to a .ops file of the users choosing. There is also an option to cancel the recording, which stops the recording without saving any actions performed while recording. 

#### *Done By Lia:*
**Multilingual Support**
The program has implemented Multilingual Support, enabling the user to choose their preferred language. The language selection option is available on the taskbar, allowing the user to choose from English, Te Reo, French, Samoan, and Italian. Once a language is selected, all headings on the taskbar, error messages, and user input boxes will be displayed in the chosen language.

**Draw**
The draw section in ANDIE's menu contains a collection of drawing actions for creating different shapes and modifying colours. Using the region selection tool the user can draw rectangles, ovals, and lines, all with the option of being either filled or outlined.

## Something *Special*

**Invert Filter**
The invert filter converts each pixel's colour value to its complementary colour, resulting in a negative or "inverted" version of the original image, and may enhance the visibility of certain elements in the image. (Hannah)

**Hue Filter**
The hue filter modifies the hue values of the pixels while keeping the saturation and lightness or value intact. This allows you to shift the entire colour spectrum of an image, via our extensive colour chooser panel. (Hannah)

**Colour Changing Dither Filter**
The Dither filter applies a technique called "error diffusion dithering" to convert a colour image into a black-and-white (or colour selected) image. It produces a pattern of "randomly" scattered dots or pixels with different intensities in "light" and "dark" colours that blend together to create the illusion of the original image. The user can select this level of dithering by changing the intensities. Note that there are many different implementations that each produce a different dithering effect. (Hannah)

**Sound Effects**
A sound effects will play on the application open operation with a start up sound, and application exit operation with aa shut down sound. All error message pop ups also include an error sound effect. Note that no other button click sound effects exist as it would simply be to much for the users sake. (Hannah)

## Testing
* JUnit tests were used to test the various features added by the team, which cover the different filters, blurs, and transformations. These JUnit tests ensure that all image and user inputs are covered, and the features work correctly with the image.

* Testing logs have been created for both file export and multilingual support. These tests ensure that all image and user inputs are thoroughly covered, and that the features perform as intended.

## Exception Handling
**Error Handling:**
* The team has incorporated error handling code to prevent any runtime errors. Error and exception handling were implemented using try/catch and throws statements to ensure a smooth runtime. The JUnit tests can be found in the test folder within the src directory.

* An error message will be displayed if attempting to make changes before opening an image when the operations (ops) stack is already full. (Hannah)

* When the program is pushed to Git, the continuous integration environment conducts various checks. These checks include verifying the ability to deploy pages, successful program building, proper functioning of unit tests, performing code linting, and ensuring both the program and pages can be deployed without any issues. (Eszter)

<br>

**File Type Handling:**
* Error handling has also been implemented for the supported file types that can be loaded onto Andie, which include JPEG, PNG, BMP, WEBP, or GIF. Furthermore, when attempting to "export" or "save as" a file, if the file path already exists, the user must choose to either override it or select another directory or file name. (Hannah)

* The actions of Export, Save, Save As, transformations, colors, filters, edits, and views will only be available if an image is loaded. Otherwise, an error message will be displayed. (Hannah)

<br>

**Action Enhancements:**
* When applying the GaussianBlur, Sharpen, and Median filters, measures have been taken to prevent negative neighbouring pixels from creating a black frame around the image. Namely the extended filter approach as described above. (Eszter and Hannah)

* Change listeners have been added to all sliders and color selectors for filters. (Hannah)

* The region selection rectangle is animated with circular rotations, similar to Apple's Preview application, and will turn black if the root image is too light to see the original white region selection rectangle. (Hannah)

* When the record button for Macros is selected, a red dot appears on the menu bar. This feature ensures that users recording their macro operations do not have to rely solely on their memory. The red dot disappears when the macro actions are either canceled or saved, providing visual feedback and indicating the status of the macro recording process. (Eszter)

* An "Undo All" option has been added so that the user can reset all operations. The operations still exist under the "Redo" action for the user to re-implement in the correct order of operations. (Hannah)

* Previous operations are cleared upon opening the application. (Hannah)

<br>

**User Confirmation:**
* Upon attempting to exit the program, the user will be presented with a confirmation pop-up, allowing them to choose between yes or no. (Hannah)

* There is also an error message that informs the user that there is no previous action to undo or redo, which provides feedback and prevents confusion for the user. (Hannah)

* The Andie application will now retain the language settings from your previous session when it is reopened. (Hannah)

* Image resize occurs now at all appropriate points, including edit actions. (Hannah)

* The application will only allow an immediate exit either via the tool bar or red button if no operations are on the stack. (Hannah)

* Error messages pop up if the user tries to do any file actions (expect for exit if there are no operations on the stack) while the record option is on. (Hannah)

<br>

**User Interface Enhancements:**
* When the program is opened, it is no longer displayed as a small window on the screen. Instead, it is designed to occupy approximately ninety percent of the screen's height and width, providing a more optimized viewing experience. (Eszter)

* Every time a user opens an image, the JFrame gets set to the size of the image and is centred on the user's screen. Additionally, the whole transform and view action class now resizes on image transformation. (Hannah)

* Set maximum and minimum height for the Andie application, so that it cannot exceed the users screen, and opens at the top in a centred position. (Hannah)

* All file chooser, slider, and color selector frames have the same font as the root Andie application, for consistency and aesthetics. (Hannah)
