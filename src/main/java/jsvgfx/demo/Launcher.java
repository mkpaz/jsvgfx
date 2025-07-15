package jsvgfx.demo;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jsvgfx.SVGException;
import jsvgfx.SVGImage;
import jsvgfx.SVGTransformer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Launcher extends Application {

    private static final String SVG_PATH = "/java.svg";
    private static final int SVG_W = 120;
    private static final int SVG_H = 120;

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        var root = new FlowPane(20, 20);
        root.setAlignment(Pos.CENTER);

        root.getChildren().setAll(
            new VBox(5, new Label("image-io"), loadWithImageIO()),
            new VBox(5, new Label("style path"), loadWithStylePath()),
            new VBox(5, new Label("style data-uri"), loadWithStyleDataUri()),
            new VBox(5, new Label("stylesheet path"), loadWithStylesheetPath(root)),
            new VBox(5, new Label("stylesheet data-uri"), loadWithStylesheetDataUri(root)),
            new VBox(5, new Label("transform size"), loadTransformSize()),
            new VBox(5, new Label("transform color"), loadTransformColor())
        );
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        stage.setScene(new Scene(root, 800, 600));
        stage.show();
    }

    //=========================================================================

    private static Node loadWithImageIO() {
        try {
            var bufferedImage = ImageIO.read(Objects.requireNonNull(Resources.getURL(SVG_PATH)));
            return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (IOException e) {
            throw new SVGException(e.getMessage(), e);
        }
    }

    private static Node loadWithStylePath() {
        var pane = new Pane();
        pane.setId("svg-pane");
        pane.setStyle(String.format(
            "-fx-background-image: url(\"%s\");-fx-background-repeat: no-repeat;",
            SVG_PATH
        ));
        pane.setMinSize(SVG_W, SVG_H);
        pane.setMaxSize(SVG_W, SVG_H);
        return pane;
    }

    private static Node loadWithStyleDataUri() {
        var pane = new Pane();
        pane.setId("svg-pane");
        pane.setStyle(String.format(
            "-fx-background-image: url(\"data:image/svg;base64,%s\");-fx-background-repeat: no-repeat",
            Resources.readAsBase64(SVG_PATH)
        ));
        pane.setMinSize(SVG_W, SVG_H);
        pane.setMaxSize(SVG_W, SVG_H);
        return pane;
    }

    private static Node loadWithStylesheetPath(Parent parent) {
        var pane = new Pane();
        pane.getStyleClass().add("pane1");
        pane.setMinSize(SVG_W, SVG_H);
        pane.setMaxSize(SVG_W, SVG_H);

        var css = """
            data:text/css,
            .pane1 {
              -fx-background-image: url("%s");
              -fx-background-repeat: no-repeat;
            }""".formatted(SVG_PATH);
        parent.getStylesheets().add(css);

        return pane;
    }

    private static Node loadWithStylesheetDataUri(Parent parent) {
        var pane = new Pane();
        pane.getStyleClass().add("pane2");
        pane.setMinSize(SVG_W, SVG_H);
        pane.setMaxSize(SVG_W, SVG_H);

        var css = """
            .pane2 {
              -fx-background-image: url("data:image/svg;base64,%s");
              -fx-background-repeat: no-repeat;
            }""".formatted(Resources.readAsBase64(SVG_PATH));
        parent.getStylesheets().add(toDataURI(css));

        return pane;
    }

    private static Node loadTransformSize() {
        var transformer = new SVGTransformer(Resources.getPath(SVG_PATH))
            .setWidth(SVG_W * 2)
            .setHeight(SVG_H * 2);

        try (var input = transformer.toInputStream()) {
            var bufferedImage = SVGImage.of(input).toBufferedImage(SVG_W * 2, SVG_H * 2);
            return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (IOException e) {
            throw new SVGException(e.getMessage(), e);
        }
    }

    private static Node loadTransformColor() {
        var transformer = new SVGTransformer(Resources.getPath(SVG_PATH))
            .setNodeValue("//g/path/@fill", "#A020F0");

        try (var input = transformer.toInputStream()) {
            var bufferedImage = SVGImage.of(input).toBufferedImage(SVG_W, SVG_H);
            return new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (IOException e) {
            throw new SVGException(e.getMessage(), e);
        }
    }

    private static String toDataURI(String css) {
        if (css == null) {
            throw new NullPointerException("CSS string cannot be null!");
        }
        return "data:base64," + new String(Base64.getEncoder().encode(css.getBytes(UTF_8)), UTF_8);
    }
}
