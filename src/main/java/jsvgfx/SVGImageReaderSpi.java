package jsvgfx;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.Locale;

@NullMarked
public class SVGImageReaderSpi extends ImageReaderSpi {

    protected static final String[] FORMAT_NAMES = {"svg", "SVG"};
    protected static final String[] EXTENSIONS = {"svg"};
    protected static final String[] MIME_TYPES = {"image/svg", "image/x-svg", "image/svg+xml", "image/svg-xml"};

    public SVGImageReaderSpi() {
        super(
            "me.decorations",
            "1.0",
            FORMAT_NAMES,
            EXTENSIONS,
            MIME_TYPES,
            "me.decorations.SVGImageReader",
            new Class<?>[]{ImageInputStream.class},
            null,
            false,
            null,
            null,
            null,
            null,
            true,
            null,
            null,
            null,
            null
        );
    }

    @Override
    public String getDescription(Locale locale) {
        return "Scalable Vector Graphics (SVG) format image reader";
    }

    @Override
    public boolean canDecodeInput(Object input) throws IOException {
        return input instanceof ImageInputStream iis && canDecode(iis);
    }

    @Override
    public ImageReader createReaderInstance(@Nullable Object extension) {
        return new SVGImageReader(this);
    }

    //=========================================================================

    @SuppressWarnings("StatementWithEmptyBody")
    protected boolean canDecode(ImageInputStream iis) throws IOException {
        try {
            iis.mark();

            int b;
            while (Character.isWhitespace((char) (b = iis.read()))) {
                // skip leading whitespaces
            }

            return b == '<';
        } catch (EOFException ignore) {
            return false;
        } finally {
            iis.reset();
        }
    }
}
