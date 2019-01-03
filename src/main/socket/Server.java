package main.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static void acceptFile() {
      System.out.println(IpAddress.getIpAddress());
      try {
        byte[] b = new byte[1024];
        int bytesRead = 0;
        ServerSocket ss = new ServerSocket(8888);
        Socket s = ss.accept();
        InputStream is = s.getInputStream();
        // DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
        ObjectInputStream oin = new ObjectInputStream(is);
        FileOutputStream fr=new FileOutputStream(System.getProperty("user.dir") + "/" + (String)oin.readObject());

        while (-1 != (bytesRead = is.read(b,0,b.length))) {
            fr.write(b,0,bytesRead);
        }

        ss.close();
    } catch (Exception e) {
        System.out.println(e);
    }
}

    public static void acceptFile(ServerSocket ss) throws IOException, ClassNotFoundException {
        byte[] b = new byte[1024];
        int bytesRead = 0;
        Socket s = ss.accept();
        InputStream is = s.getInputStream();
        // DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
        ObjectInputStream oin = new ObjectInputStream(is);
        FileOutputStream fr = new FileOutputStream(System.getProperty("user.dir") + "/" + ((String)oin.readObject()).replaceAll("\\\\", "/"));
        while (-1 != (bytesRead = is.read(b,0,b.length))) {
            fr.write(b,0,bytesRead);
        }
        fr.close();
        // dis.close();
    }

    public void run() throws IOException, ClassNotFoundException {
        ServerSocket ss = new ServerSocket(8888);
        Socket s = ss.accept();
        InputStream is = s.getInputStream();
        DataInputStream dis = new DataInputStream(is);

        //Number of files expecting to be sent over.
        int nFiles = dis.readInt();
        System.out.println("Received int val = " + nFiles);
        ObjectInputStream ois = new ObjectInputStream(is);
        //retrieves fold structure.
        ArrayList<String> paths = (ArrayList<String>)ois.readObject();
        //Creates folder structure.
        for (String path: paths) {
            File dir = new File(System.getProperty("user.dir") + "/" + path.replaceAll("\\\\", "/"));

            if (dir.mkdir() == false) {
                System.out.println("Already created");
                // return;
            }
        }

        s.close();
        for (int i = 0; i < nFiles; ++i) {
            acceptFile(ss);
        }

        ss.close();
    }


}
