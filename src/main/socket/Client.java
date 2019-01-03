package main.socket;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class Client {
    private static ProgressBar pb;
    private static ProgressIndicator pi;
    public static void sendFile (File file, String ipAddress, ProgressBar pb, ProgressIndicator pi) {
        System.out.println("hiii");
        String fileName = file.getName();
        String filePath = file.getAbsolutePath();


        long start = System.currentTimeMillis();


        //Check if the file exists
        if (!file.exists()) {
            System.out.println("File does not exist");
            return;
        }
        try {
            //Connect to the server
            // Socket s = new Socket("192.168.0.12", 8888);
            Socket s = new Socket(ipAddress, 8888);

            byte[] fileNameInBytes = fileName.getBytes();

            // file.close();
            byte b[] = new byte[1024];

            int bytesRead = 0;

            //get the file path
            FileInputStream f = new FileInputStream(filePath);
            OutputStream os = s.getOutputStream();


            ObjectOutputStream oout = new ObjectOutputStream(os);
            //send the filename as a String object.
            oout.writeObject(fileName);
            // oout.close();
            double iterations = file.length()/1024;
            double count = 0;

            NumberFormat formatter = new DecimalFormat("#0.00");

            //read the entire file in 1024 chunks to send it.
            while (-1 != (bytesRead = f.read(b, 0, b.length))) {
                ++count;
                os.write(b, 0, bytesRead);
                pb.setProgress(count/iterations);
                pi.setProgress(count/iterations);
//                System.out.println("Percentage: " + formatter.format((count/iterations)*100) + "%");
            }
            s.close();
            // f.flush();
        } catch (Exception e) {
            System.out.println(e + " hiiiii");
//            new Thread(() -> alert("Error", "Unable to send file. Check IP Address or if devices are on the same network")).start();
            return;
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        double seconds = timeElapsed/1000;
        // System.out.println("Miliseconds elapsed = " + timeElapsed);
        System.out.println("Time elapsed = " + "Minutes: " + (Math.floor(seconds/60)) + " Seconds: " + seconds%60);

    }

    public static Socket getSocket(String ipAddress) throws IOException {
        Socket s = new Socket(ipAddress, 8888);
        System.out.println("connected");
        return s;
    }
    public static File getFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        return f;
    }

    //Sends one file.
    public static void sendFile(Socket s, String inputPath, String outputPath) throws IOException {
        System.out.println("Sending " + inputPath);
        File file = getFile(inputPath);
        if (file == null) {
            System.out.println("File is null");
            return;
        }
        byte b[] = new byte[1024];
        int bytesRead = 0;

        FileInputStream f = new FileInputStream(file.getAbsolutePath());
        OutputStream os = s.getOutputStream();

        ObjectOutputStream fileNameStream = new ObjectOutputStream(os);
        fileNameStream.writeObject(outputPath);

        double iterations = file.length()/1024;
        double count = 0;

        NumberFormat formatter = new DecimalFormat("#0.00");
        // System.out.println("Before");
        while (-1 != (bytesRead = f.read(b,0,b.length))) {
            ++count;
            // System.out.println("writing");
            os.write(b,0,bytesRead);
            pb.setProgress(count/iterations);
            pi.setProgress(count/iterations);
            // System.out.println("Percentage: " + formatter.format((count/iterations)*100) + "%");
        }
        // System.out.println("After");
        f.close();
        // fileNameStream.close();
        os.flush();
        // System.out.println("Complete");
        os.close();
    }


    //Send all the files in the list
    public static void send(ArrayList<Pair<String,String>> listOfFiles, String ipAddress) throws IOException {

        for (int i = 0; i < listOfFiles.size(); ++i) {
            sendFile(getSocket(ipAddress), listOfFiles.get(i).getKey(), listOfFiles.get(i).getValue());
        }
    }

    public static void printFileNames(File dir) {
        System.out.println(dir.getAbsolutePath());
        if (dir.isDirectory()) {
            for (File child: dir.listFiles()) {
                printFileNames(child);
            }
        }
    }

    public static void testPath(String path) {
        File dir = new File(path);
        printFileNames(dir);
    }

    //Sends folder structure to be recreated.
    public static void sendStructure(ArrayList<String> path, ArrayList<Pair<String,String>> listOfFiles, Socket s) throws IOException {
        OutputStream os = s.getOutputStream();

        DataOutputStream dis = new DataOutputStream(os);

        dis.writeInt(listOfFiles.size());
        dis.flush();
        // dis.close();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(path);
        // oos.writeObject(listOfFiles);
        oos.flush();
        // os.close();
        s.close();
    }

    //Scans the file structure recursively and returns list of paths in a list.
    public static void getFolderStructure(ArrayList<String> folderStructure, ArrayList<Pair<String,String>> listOfFiles,String toRemove, String filePath) throws IOException {
        File file = new File(filePath);

        //Checks if it is a directory
        if (file.isDirectory()) {
            //Adds the Directory path to list.
            folderStructure.add(file.getAbsolutePath().replace(toRemove, ""));
            //Loops the remaining folders.
            for (File child: file.listFiles()) {
                getFolderStructure(folderStructure,listOfFiles,toRemove, child.getAbsolutePath());
            }
        } else {
            //Else add the File path to list
            if (file.getName().equals(".DS_Store")) return; //File to ignore
            Pair<String,String> path = new Pair<String,String>(
                    file.getAbsolutePath(),file.getAbsolutePath().replace(toRemove, "")
            );
            listOfFiles.add(path);
        }
    }
    public void run(File file, String ipAddress, ProgressBar pb, ProgressIndicator pi) throws IOException {
        Client.pb = pb;
        Client.pi = pi;
        //Empty lists
        ArrayList<String> folderStructure = new ArrayList<String>();
        ArrayList<Pair<String,String>> listOfFiles = new ArrayList<Pair<String,String>>();

        //Get and fill the lists with the folder structure.
        getFolderStructure(folderStructure,listOfFiles, file.getAbsolutePath().replace(file.getName(),""), file.getAbsolutePath());

        //Send the file structure to the server to reconstruct.
        sendStructure(folderStructure, listOfFiles,(getSocket(ipAddress)));

        send(listOfFiles, ipAddress);
    }
}
