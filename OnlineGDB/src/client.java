import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.Socket;

import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class client extends JFrame{
    public static void main(String[] args) throws Exception {
        JFileChooser fc = new JFileChooser();

        int res = fc.showOpenDialog(fc);
        File f = null;

        if(res == JFileChooser.APPROVE_OPTION) {
            System.out.println("File chosen, name: " + fc.getSelectedFile().getName());

            f = fc.getSelectedFile();
        }

        Socket clientSocket = new Socket("localhost", 9000);
        OutputStream outToServer = clientSocket.getOutputStream();
        outToServer.write(Files.readAllBytes(f.toPath()));
        outToServer.close();
        clientSocket.close();

        System.out.println("Compiling ....");
        System.out.println("Result: \n");

        clientSocket = new Socket("localhost", 9000);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        while((line = inFromServer.readLine()) != null) {
            System.out.println(line);
        }

        clientSocket.close();
    }
}