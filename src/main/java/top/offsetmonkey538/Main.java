package top.offsetmonkey538;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final Path INPUT_DIR = Paths.get(WORKING_DIR, "packs");
    public static final Pattern INPUT_NAME_PATTERN = Pattern.compile("\\d+-");

    public static void main(String[] args) throws IOException {
        // Check if input directory exists
        if (!INPUT_DIR.toFile().exists()) createDir(INPUT_DIR.toFile());
        if (!INPUT_DIR.toFile().isDirectory()) throw new RuntimeException("Input directory '" + INPUT_DIR + "' is a file!");

        // Gather resource packs in correct order (1-mypack.zip before 20-otherpack.zip)
        final File[] sourcePacksArray = INPUT_DIR.toFile().listFiles();
        if (sourcePacksArray == null) throw new RuntimeException("Input directory '" + INPUT_DIR + "' is empty!");

        final List<File> sourcePacks = Stream.of(sourcePacksArray)
                .filter(file -> !file.isDirectory())
                .filter(file -> file.getName().endsWith(".zip"))
                .sorted(Comparator.comparingInt(Main::extractPriorityFromFile).reversed())
                .toList();

        // Create tmp directory and extract source packs
        final Path tmpDir = Files.createTempDirectory("minecraft-resourcepack-merger");
        final File inputExtractDir = createDir(tmpDir.resolve("input").toFile());
        final File outputPack = createNewFile(new File(WORKING_DIR, "pack.zip"));

        try (final ZipFile outputZip = new ZipFile(outputPack)) {
            sourcePacks.forEach(pack -> {
                try (final ZipFile inputZip = new ZipFile(pack)) {
                    final File extractedPack = createDir(new File(inputExtractDir, pack.getName()));

                    inputZip.extractAll(extractedPack.getAbsolutePath());

                    final File[] extractedFilesArray = extractedPack.listFiles();
                    if (extractedFilesArray == null) throw new RuntimeException("Couldn't extract content of '" + pack + "'!");
                    final List<File> extractedFiles = List.of(extractedFilesArray);

                    for(File file : extractedFiles) {
                        if (file.isDirectory()) outputZip.addFolder(file, new ZipParameters());
                        else outputZip.addFile(file, new ZipParameters());
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        // Get the SHA-1 of the output pack and display it in the terminal.
        System.out.println("SHA1: " + DigestUtils.sha1Hex(Files.newInputStream(outputPack.toPath())));
    }

    public static int extractPriorityFromFile(File file) {
        final String filename = file.getName();

        final Matcher matcher = INPUT_NAME_PATTERN.matcher(filename);

        if (!matcher.find()) throw new RuntimeException("File '" + file + "' doesn't start with priority!");

        return Integer.parseInt(matcher.group().replace('-', ' ').strip());
    }

    public static File createDir(File file) {
        if (!file.exists() && !file.mkdirs()) throw new RuntimeException("Couldn't create directory '" + file + "'!");
        return file;
    }

    public static File createNewFile(File file) throws IOException {
        if (file.exists() && !file.delete()) throw new RuntimeException("Couldn't delete file '" + file + "'!");

        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();

        return file;
    }
}