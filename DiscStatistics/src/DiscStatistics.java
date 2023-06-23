import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscStatistics {

    //Total size of files.
    private static long sizeOfFiles(File directoryOfFile) {
        long size = 0;
        File[] files = directoryOfFile.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += sizeOfFiles(file);
                }
            }
        }
        return size;
    }

    //Print the path and the size of each directory.
    private static void getDiscStat(String pathOfFolder) {
        File eachFolder = new File(pathOfFolder);

        if (!eachFolder.isDirectory()) {
            System.out.println("Invalid directory path!");
            return;
        }
        //If the path does not correspond to a valid directory,
        // the code alerts the user with the error message and exits the method.
        long folderSize = sizeOfFiles(eachFolder);
        System.out.printf("Folder stats are found: path='%s', size=%,d%n", eachFolder, folderSize);
    }

    private static void scanDirectory(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String folderPath = file.getAbsolutePath();
                    getDiscStat(folderPath);
                    scanDirectory(folderPath);
                }
            }
        }
    }
    

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("The absolute path of directory: ");
        String targetDirectory = scanner.nextLine();

        ExecutorService executor = Executors.newFixedThreadPool(20); // Adjust the thread pool size as needed

        executor.execute(() -> {
            getDiscStat(targetDirectory);
            scanDirectory(targetDirectory);
        });

        executor.shutdown();
    }
}
