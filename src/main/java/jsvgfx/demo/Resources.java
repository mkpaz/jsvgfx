package jsvgfx.demo;

import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@NullMarked
public final class Resources {

    public static String getPath(String path) {
        return Objects.requireNonNull(Resources.class.getResource(path)).toString();
    }

    public static URL getURL(String path) {
        return Objects.requireNonNull(Resources.class.getResource(path));
    }

    public static InputStream getStream(String path) {
        return Objects.requireNonNull(Resources.class.getResourceAsStream(path));
    }

    public static String read(String path) {
        try (InputStream is = getStream(path)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readAsBase64(String path) {
        try (InputStream is = getStream(path)) {
            return Base64.getEncoder().encodeToString(is.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
