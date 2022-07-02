import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.*;

public class Lab11 {
    public static void main(String[] args) {
        try {
            Thread client = new Thread(new Client());
            client.start();

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom random = new SecureRandom();
            keyGenerator.init(256, random);
            SecretKey secretKey = keyGenerator.generateKey();

            writeToFile("SecretKey.txt", secretKey.getEncoded());

            ServerSocket server = new ServerSocket(8888);

            Socket connection_socket = server.accept();
            DataInputStream inFromClient = new DataInputStream(connection_socket.getInputStream());

            byte[] client_message = inFromClient.readAllBytes();

            connection_socket.close();
            server.close();

            byte[] hash = new byte[20];
            byte[] encrypted_message = new byte[client_message.length - 20];
            System.arraycopy(client_message, 0, encrypted_message, 0, encrypted_message.length);
            System.arraycopy(client_message, encrypted_message.length, hash, 0, hash.length);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decrypted_text = cipher.doFinal(encrypted_message);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

            if (java.util.Arrays.equals(messageDigest.digest(decrypted_text), hash))
                System.out.println("Message is authentic.");

            System.out.println("Message: " + new String(decrypted_text));
        }
        catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static void writeToFile(String path, byte[] key) {
        try {
            File file = new File(path);
            if (file.createNewFile())
                System.out.println("Created new file.");

            FileOutputStream fileOutputStream = new FileOutputStream(path);
            fileOutputStream.write(key);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (IOException exception) { System.out.println(exception.getMessage()); }
    }
}

class Client implements Runnable {
    public void run() {
        try {
            Socket client = new Socket("localhost", 8888);
            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());

            byte[] keyBytes = Files.readAllBytes(new File("SecretKey.txt").toPath());
            SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");

            String message = "secret message";
            byte[] plain_text = message.getBytes(StandardCharsets.UTF_8);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted_text = cipher.doFinal(plain_text);

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hash = messageDigest.digest(plain_text);

            byte[] messageWithHash = new byte[encrypted_text.length + hash.length];
            System.arraycopy(encrypted_text, 0,  messageWithHash, 0, encrypted_text.length);
            System.arraycopy(hash, 0, messageWithHash, encrypted_text.length, hash.length);

            outToServer.write(messageWithHash);
            outToServer.flush();

            client.close();
        }
        catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
                | IllegalBlockSizeException | BadPaddingException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
