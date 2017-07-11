package core.utilities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;

/**
 * The Images class contains general image capture functions.
 */
public class Images {
	private static String imageType = "jpg"; // "png"

	/**
	 * Captures entire screen (desktop/browser) image and writes it to the
	 * specified file as a jpg/png file.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @return true if desktop/browser capture passed successfully
	 */
	public static boolean captureScreen(String fileName) {
		return captureScreen(fileName, false);
	}

	/**
	 * Captures entire screen (desktop/browser) image and writes it to the
	 * specified file as a jpg/png file.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param isSystem
	 *            should be true if an appeared exception shouldn't be sent to
	 *            {@link Log#errorHandler(String, Exception)}
	 * @return true if desktop/browser capture passed successfully
	 */
	public static boolean captureScreen(String fileName, boolean isSystem) {
		return Log.getEngine().takeCapture(fileName, isSystem);
	}

	/**
	 * Captures individual test object image and writes it to the specified file
	 * as a jpeg.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param x
	 *            coordinate of screen location to capture
	 * @param y
	 *            coordinate of screen location to capture
	 * @param width
	 *            coordinate of screen location to capture
	 * @param height
	 *            coordinate of screen location to capture
	 * @return true if desktop capture passed successfully
	 */
	public static boolean captureScreen(String fileName, int x, int y, int width, int height) {
		return captureScreen(fileName, x, y, width, height, false);
	}

	/**
	 * Captures individual test object image and writes it to the specified file
	 * as a jpeg.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param x
	 *            coordinate of screen location to capture
	 * @param y
	 *            coordinate of screen location to capture
	 * @param width
	 *            coordinate of screen location to capture
	 * @param height
	 *            coordinate of screen location to capture
	 * @param isSystem
	 *            should be true if an appeared exception shouldn't be sent to
	 *            {@link Log#errorHandler(String, Exception)}
	 * @return true if desktop capture passed successfully
	 */
	protected static boolean captureScreen(String fileName, int x, int y, int width, int height, boolean isSystem) {
		return doScreenCapture(fileName, x, y, width, height, isSystem);
	}

	/**
	 * Captures entire desktop image and writes it to the specified file as a
	 * jpg/png file.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param isSystem
	 *            should be true if an appeared exception shouldn't be sent to
	 *            {@link Log#errorHandler(String, Exception)}
	 * @return true if desktop/browser capture passed successfully
	 */
	public static boolean doDesktopCapture(String fileName, boolean isSystem) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		return doScreenCapture(fileName, 0, 0, screenSize.width, screenSize.height, isSystem);
	}

	/**
	 * Helper function to capture screen image.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param x
	 *            coordinate of screen location to capture
	 * @param y
	 *            coordinate of screen location to capture
	 * @param width
	 *            coordinate of screen location to capture
	 * @param height
	 *            coordinate of screen location to capture
	 * @return true if desktop capture passed successfully
	 */
	protected static boolean doScreenCapture(String fileName, int x, int y, int width, int height) {
		return doScreenCapture(fileName, x, y, width, height, false);
	}

	/**
	 * Helper function to capture screen image.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param x
	 *            coordinate of screen location to capture
	 * @param y
	 *            coordinate of screen location to capture
	 * @param width
	 *            coordinate of screen location to capture
	 * @param height
	 *            coordinate of screen location to capture
	 * @param isSystem
	 *            should be true if an appeared exception shouldn't be sent to
	 *            {@link Log#errorHandler(String, Exception)}
	 * @return true if desktop capture passed successfully
	 */
	protected static boolean doScreenCapture(String fileName, int x, int y, int width, int height, boolean isSystem) {
		// screen capture
		try {
			Rectangle area = new Rectangle(x, y, width, height);
			Robot robot = new Robot();

			ImageIO.write(robot.createScreenCapture(area), imageType, new File(fileName));
		} catch (Exception e) {
			if (isSystem) {
				Log.logScriptInfo("Error in Image#doScreenCapture: error capturing image: " + e,
						Log.LOGTYPE_ERROR_OUTPUT);
			} else {
				Log.errorHandler("Error in Image#doScreenCapture: error capturing image: " + e);
			}

			return false;
		}

		return true;
	}

	/**
	 * Helper function to capture browser window. If alert dialog appears, then
	 * method will capture desktop image.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @return true if browser capture passed successfully
	 */
	protected static boolean doBrowserCapture(String fileName) {
		return doBrowserCapture(fileName, false);
	}

	/**
	 * Helper function to capture browser window. If alert dialog appears, then
	 * method will capture desktop image.
	 * <p>
	 *
	 * @param fileName
	 *            path and filename of file to write image out to
	 * @param isSystem
	 *            should be true if an appeared exception shouldn't be sent to
	 *            {@link Log#errorHandler(String, Exception)}
	 * @return true if browser capture passed successfully
	 */
	public static boolean doBrowserCapture(String fileName, boolean isSystem) {
		try {
			if (SeleniumCore.driver != null) {
				File scrFile = ((TakesScreenshot) SeleniumCore.driver).getScreenshotAs(OutputType.FILE);
				FileIO.copyFile(scrFile, new File(fileName));
			} else {
				return false;
			}
		} catch (Exception e) {
			if (e instanceof UnhandledAlertException) {
				return doDesktopCapture(fileName, isSystem);
			}

			if (isSystem) {
				Log.logScriptInfo("Error in Images#doBrowserCapture: error capturing image: " + e,
						Log.LOGTYPE_ERROR_OUTPUT);
			} else {
				Log.errorHandler("Error in Images#doBrowserCapture: error capturing image: " + e);
			}

			return false;
		}

		return true;
	}

	/**
	 * Compares two images a pixel at a time.
	 *
	 * @param expectedImage
	 *            path and filename of file to compare to
	 * @param actualImage
	 *            path and filename of the captured image
	 * @return true if the images are identical, false if not
	 */
	public static boolean compareImages(String expectedImage, String actualImage) {
		BufferedImage expected = null, actual = null;
		try {
			// read in expected image
			expected = ImageIO.read(new File(expectedImage));

			// read in actual image
			actual = ImageIO.read(new File(actualImage));
		} catch (Exception e) {
			Log.errorHandler("Error in compareImages: error reading images: " + e);
			return false;
		}

		if (expected == null || actual == null || expected.getHeight() != actual.getHeight()
				|| expected.getWidth() != actual.getWidth()) {
			return false;
		}

		for (int y = 0; y < expected.getHeight(); ++y) {
			for (int x = 0; x < expected.getWidth(); ++x) {
				if (expected.getRGB(x, y) != actual.getRGB(x, y)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Creates a difference image by subtracting the actual image from the
	 * expected, with the result being all black if no differences are found,
	 * input images must be the same size, intended for human debugging.
	 *
	 * @param expectedImagePath
	 *            path and filename of file to compare to
	 * @param actualImagePath
	 *            path and filename of the captured image
	 * @param diffImagePath
	 *            path and filename to the image difference
	 */
	public static void doImageDiff(String expectedImagePath, String actualImagePath, String diffImagePath) {
		BufferedImage expected = null, actual = null;
		try {
			// read in expected image
			expected = ImageIO.read(new File(expectedImagePath));

			// read in actual image
			actual = ImageIO.read(new File(actualImagePath));
		} catch (Exception e) {
			Log.errorHandler("Error in Bitmapfuncs#doImageDiff: error reading images: " + e);
			return;
		}

		if (expected == null || actual == null) {
			Log.errorHandler("Error in Bitmapfuncs#doImageDiff: Expected image or actual image is null");
			return;
		}
		if (expected.getHeight() != actual.getHeight() || expected.getWidth() != actual.getWidth()) {
			Log.errorHandler("Error in Bitmapfuncs#doImageDiff: Images are not the same size");
			return;
		}

		try {
			BufferedImage difference = new BufferedImage(expected.getWidth(), expected.getHeight(), expected.getType());

			for (int y = 0; y < expected.getHeight(); ++y) {
				for (int x = 0; x < expected.getWidth(); ++x) {
					int rgb = expected.getRGB(x, y) - actual.getRGB(x, y);
					difference.setRGB(x, y, rgb);
				}
			}

			File diffImage = new File(diffImagePath);
			ImageIO.write(difference, imageType, diffImage);
		} catch (Exception e) {
			Log.errorHandler("Error in doImageDiff() error writing image: " + e);
		}
	}
}
