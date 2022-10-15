package impl.server;

import impl.utils.FileHelper;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class FileStorage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private static ConcurrentHashMap<Integer, String> filesMap = new ConcurrentHashMap<>();
    private static final String filesDir = System.getProperty("user.dir") +
            File.separator + "server" + File.separator + "data" + File.separator;
    private static final String saveDir = System.getProperty("user.dir") +
            File.separator + "server" + File.separator + "save" + File.separator;

    public static String getFileNameByID(int id) {
        return filesMap.get(id);
    }

    public static void addToFilesMap(int id, String filename) {
        filesMap.put(id, filename);
    }

    public static void remove(int id) {
        filesMap.remove(id);
    }

    public static void remove(String filename) {
        int id = FileManager.getFileID(filename);
        filesMap.remove(id);
    }

    public static void save() {
        String saveName = getSaveDir() + "map.ser";
        Path savePath = Path.of(saveName);
        if (!Files.exists(savePath)) {
            try {
                Files.createFile(savePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(saveName);
             ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)
        ) {
            oos.writeObject(filesMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The state of the server is saved");
    }

    public static void load() {
        String filename = getSaveDir() + "map.ser";
        if (Files.exists(Paths.get(filename))) {
            try (FileInputStream fileInputStream = new FileInputStream(filename);
                 ObjectInputStream ois = new ObjectInputStream(fileInputStream)
            ) {
                filesMap = (ConcurrentHashMap<Integer, String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            filesMap = new ConcurrentHashMap<>();
        }
    }

    public static String getStoragePath(){
        FileHelper.createDirIfNotExist(filesDir);
        return filesDir;
    }

    private static String getSaveDir() {
        FileHelper.createDirIfNotExist(saveDir);
        return saveDir;
    }
}
