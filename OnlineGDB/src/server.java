/*
 * -------- BIG GOTCHA
 *      
 * This program will only work for a single client, multiple clients won't work.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class server {
    public static void main(String[] args) throws Exception {
        // Constants
        String fileName = "../build/code.c";
        String compileString = "gcc ../build/code.c -o ../build/codeBin.exe";
        String runString = "../build/codeBin";

        // Create a new socket at port 9000
        ServerSocket rootSocket = new ServerSocket(9000);

        while(true) {
            System.out.println("Server Started -----");
            // Wait for an incoming connection
            Socket connectionSocket = rootSocket.accept();

            // Create new file for the program
            File f = new File(fileName);
            // Get file from incoming connection and save it to the already created file replacing its bits
            Files.copy(connectionSocket.getInputStream(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Close this connection as the client closes connection after sending file.
            connectionSocket.close();

            // Accept new connection from client
            connectionSocket = rootSocket.accept();

            // Get the runtime to execute system commands
            Runtime rt = Runtime.getRuntime();

            // Compile the C program using gcc by running the appropriate command
            Process process = rt.exec(compileString.split(" "));

            String line;
            // Open a new output stream to client
            OutputStream outToClient = connectionSocket.getOutputStream();
            // Check if the compilation generates any error and send them to the client
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                outToClient.write(line.getBytes());
            }
            reader.close();
            line = "";

            // Run the built binary for the C program
            process = rt.exec(runString.split(" "));
            // Send the result after execution to the client
            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outToClient.write(line.getBytes());
            }
            reader.close();

            // Close the socket
            connectionSocket.close();
        }
    }
}
