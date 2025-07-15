module jsvgfx {
    requires static org.jspecify;

    requires javafx.controls;
    requires javafx.swing;
    requires java.desktop;
    requires com.github.weisj.jsvg;

    exports jsvgfx;
    exports jsvgfx.demo;

    uses javax.imageio.spi.ImageReaderSpi;
    provides javax.imageio.spi.ImageReaderSpi with jsvgfx.SVGImageReaderSpi;
}
