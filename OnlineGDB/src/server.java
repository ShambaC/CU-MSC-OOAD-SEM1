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
        String fileName = "../build/code.c";
        String compileString = "gcc ../build/code.c -o ../build/codeBin.exe";
        String runString = "../build/codeBin";

        ServerSocket rootSocket = new ServerSocket(9000);

        while(true) {
            System.out.println("Server Started -----");
            Socket connectionSocket = rootSocket.accept();

            File f = new File(fileName);
            Files.copy(connectionSocket.getInputStream(), f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            connectionSocket.close();

            connectionSocket = rootSocket.accept();

            Runtime rt = Runtime.getRuntime();

            Process process = rt.exec(compileString.split(" "));

            String line;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                outToClient.write(line.getBytes());
            }
            reader.close();
            line = "";

            process = rt.exec(runString.split(" "));
            OutputStream outToClient = connectionSocket.getOutputStream();
            reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            while ((line = reader.readLine()) != null) {
                outToClient.write(line.getBytes());
            }
            reader.close();

            connectionSocket.close();
        }
    }
}
