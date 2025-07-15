A simple project demonstrating loading SVG images with [jsvg](https://github.com/weisJ/jsvg)
and JavaFX [pluggable image loading](https://bugs.openjdk.org/browse/JDK-8306707) via javax.imageio.

To run the demo:

```sh
git clone https://github.com/mkpaz/jsvgfx
cd jsvgfx

# if your system JVM version < 24
JAVA_HOME=/path/to/jdk/24

./mvnw javafx:run
```
