package jsvgfx;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class SVGException extends RuntimeException {

    public SVGException() {
    }

    public SVGException(String message) {
        super(message);
    }

    public SVGException(String message, Throwable cause) {
        super(message, cause);
    }
}
