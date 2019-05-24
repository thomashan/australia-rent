package thomashan.github.io.australia.rent.geb


import geb.report.ExceptionToPngConverter
import geb.report.PngUtils
import geb.report.ReportState
import geb.report.ScreenshotReporter
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import ru.yandex.qatools.ashot.AShot
import ru.yandex.qatools.ashot.Screenshot
import ru.yandex.qatools.ashot.shooting.ShootingStrategies

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

class FullPageScreenshotReporter extends ScreenshotReporter {
    @Override
    void writeReport(ReportState reportState) {
        // note - this is not covered by tests unless using a driver that can take screenshots
        def screenshotDriver = determineScreenshotDriver(reportState.browser)
        if (screenshotDriver) {
            def decoded
            try {
                decoded = getFullScreenshotBytes(reportState.browser.driver)

                // WebDriver has a bug where sometimes the screenshot has been encoded twice
                if (!PngUtils.isPng(decoded)) {
                    decoded = Base64.mimeDecoder.decode(decoded)
                }
            } catch (WebDriverException e) {
                decoded = new ExceptionToPngConverter(e).convert('An exception has been thrown while getting the screenshot:')
            }

            def file = saveScreenshotPngBytes(reportState.outputDir, reportState.label, decoded)
            notifyListeners(reportState, [file])
        }
    }

    private byte[] getFullScreenshotBytes(WebDriver webDriver) {
        Screenshot screenshot = new AShot()
                .shootingStrategy(ShootingStrategies.viewportPasting(100))
                .takeScreenshot(webDriver)
        BufferedImage bufferedImage = screenshot.image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream)
        byteArrayOutputStream.flush()
        byteArrayOutputStream.close()

        return byteArrayOutputStream.toByteArray()
    }
}
