import java.util.stream.Collectors;
import java.util.*;
import java.io.*;
import java.net.*;

public class SuperStore9 {
    public static void main(String[] args) {
        try {
            Thread server = new Thread(new Server());
            server.start();

            Socket client = new Socket("localhost", 8888);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));

            String result = inFromServer.readLine();

            DataOutputStream outToServer = new DataOutputStream(client.getOutputStream());
            outToServer.writeBytes("Success");

            System.out.println("Average price of all products: " + result);
            client.close();
        }
        catch (IOException exception) { System.out.println("IO error"); }
    }
}

class Server implements Runnable {
    private double average_price;

    public void run() {
        try {
            ServerSocket server = new ServerSocket(8888);

            Socket connection_socket = server.accept();
            DataOutputStream outToClient = new DataOutputStream(connection_socket.getOutputStream());

            ParseFile();
            outToClient.writeBytes(average_price + "\n");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection_socket.getInputStream()));
            System.out.println(inFromClient.readLine());

            connection_socket.close();
            server.close();
        }
        catch (IOException exception) { System.out.println("IO error"); }
    }

    private void ParseFile() {
        final String path = "http://homes.ieu.edu.tr/culudagli/files/SE375/datasets/01-January.txt";

        try {
            System.out.println("Client parsing " + path);

            URL url = new URL(path);
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String row;
            List<List<String>> records = new ArrayList<>();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                records.add(Arrays.asList(data));
            }
            csvReader.close();

            //get price string values
            List<String> values = new ArrayList<>();
            for (int i = 1; i < records.size(); i++)
                values.add(records.get(i).get(1));

            //parse values to int
            List<Integer> prices = values.stream().map(Integer::parseInt).collect(Collectors.toList());

            //calculate average price of all products
            double total_price = 0.0;
            for (int price : prices)
                total_price += price;

            average_price = total_price / prices.size();
        }
        catch (FileNotFoundException exception) {
            System.out.println("File cannot found");
        }
        catch (IOException exception) {
            System.out.println("IO error");
        }
    }
}
