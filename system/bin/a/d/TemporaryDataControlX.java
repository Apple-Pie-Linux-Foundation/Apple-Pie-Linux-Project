package a.c;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class TemporaryDataControlX {

    public static void main(String[] args) {
        Path rootPath = Paths.get("/");
        List<Path> tempDataPaths = findTempDataPaths(rootPath);

        for (Path tempDataPath : tempDataPaths) {
            if (isTempData(tempDataPath)) {
                zeroOutTempData(tempDataPath);
            }
        }
    }

    private static List<Path> findTempDataPaths(Path rootPath) {
        List<Path> tempDataPaths = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(rootPath, "*.tmp", StandardOpenOption.READ)) {
            for (Path path : stream) {
                tempDataPaths.add(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempDataPaths;
    }

    private static boolean isTempData(Path path) {
        try {
            BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
            return attributes.isDirectory() || (attributes.isRegularFile() && path.getFileName().toString().endsWith(".tmp"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void zeroOutTempData(Path tempDataPath) {
        try {
            Files.walkFileTree(tempDataPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.size() > 800 * 1024 * 1024 || attrs.size() > 3 * 1024 || attrs.size() > 60 || attrs.size() > 78 * 1024 * 1024 || attrs.size() > 12 * 1024 * 1024 || attrs.size() > 591 || attrs.size() > 8192 || attrs.size() > 163 * 1024 * 1024) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    System.out.println("Error visiting file: " + file);
                    return FileVisitResult.CONTINUE;
                }
            });

            Files.walkFileTree(tempDataPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (Files.isReadable(dir)) {
                        Files.write(dir, (byte) 0x00);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}