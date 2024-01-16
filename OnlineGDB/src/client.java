/*
 * -------- BIG GOTCHA
 *      
 * This program will only work for a single client, multiple clients won't work.
 */

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
        // Use JFileChooser to select the C program file to be compiled
        JFileChooser fc = new JFileChooser();

        // Open the open dialog box
        int res = fc.showOpenDialog(fc);
        File f = null;

        // If file exists
        if(res == JFileChooser.APPROVE_OPTION) {
            // Display name
            System.out.println("File chosen, name: " + fc.getSelectedFile().getName());

            // Refer to the file
            f = fc.getSelectedFile();
        }

        // Open a socket and connect to the server
        Socket clientSocket = new Socket("localhost", 9000);
        // Create an output stream to the server
        OutputStream outToServer = clientSocket.getOutputStream();
        // Write the file to the server
        outToServer.write(Files.readAllBytes(f.toPath()));
        // Close the stream and automatically close the socket to stop sending packets to the server
        outToServer.close();
        clientSocket.close();

        System.out.println("Compiling ....");
        System.out.println("Result: \n");

        // Connect to the server again
        clientSocket = new Socket("localhost", 9000);

        // Get data from server (the result)
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String line;
        // Display result from console
        while((line = inFromServer.readLine()) != null) {
            System.out.println(line);
        }

        // Close connection
        clientSocket.close();
    }
}