package jsvgfx;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.parser.LoaderContext;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.github.weisj.jsvg.view.ViewBox;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jspecify.annotations.NullMarked;

import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

@NullMarked
public class SVGImage {

    // should be either TYPE_INT_ARGB or TYPE_INT_ARGB_PRE,
    // otherwise SwingFXUtils.toFXImage() will convert it to the TYPE_INT_ARGB_PRE
    public static final int BUFFERED_IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB_PRE;

    protected final SVGDocument svgDocument;

    protected SVGImage(SVGDocument svgDocument) {
        this.svgDocument = svgDocument;
    }

    public int getWidth() {
        return (int) svgDocument.size().getWidth();
    }

    public int getHeight() {
        return (int) svgDocument.size().getHeight();
    }

    public BufferedImage toBufferedImage(int width, int height) {
        var image = new BufferedImage(width, height, BUFFERED_IMAGE_TYPE);

        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        svgDocument.render(null, graphics, new ViewBox(width, height));
        graphics.dispose();

        return image;
    }

    public Image toImage(int width, int height) {
        return SwingFXUtils.toFXImage(toBufferedImage(width, height), null);
    }

    //=========================================================================

    public static SVGImage of(ImageInputStream input) throws IOException {
        var loader = new SVGLoader();

        var bos = new ByteArrayOutputStream();
        var buf = new byte[8192];

        int bytesRead;
        while ((bytesRead = input.read(buf)) != -1) {
            bos.write(buf, 0, bytesRead);
        }

        var svgDocument = loader.load(
            new ByteArrayInputStream(bos.toByteArray()), null, LoaderContext.createDefault()
        );

        Objects.requireNonNull(svgDocument, "Unable to read SVG from ImageInputStream");

        return new SVGImage(svgDocument);
    }

    public static SVGImage of(InputStream input) throws IOException {
        var loader = new SVGLoader();

        var svgDocument = loader.load(
            input, null, LoaderContext.createDefault()
        );

        Objects.requireNonNull(svgDocument, "Unable to read SVG from InputStream");

        return new SVGImage(svgDocument);
    }

    public static SVGImage of(URL url) {
        var svgDocument = new SVGLoader().load(url);

        Objects.requireNonNull(svgDocument, "Unable to read SVG from URL");

        return new SVGImage(svgDocument);
    }
}
