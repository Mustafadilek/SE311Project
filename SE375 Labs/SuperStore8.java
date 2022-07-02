import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;
import java.net.*;

public class SuperStore8 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        HashMap<String, List<Integer>>[] sales_maps = new HashMap[12];
        for (int i = 0; i < sales_maps.length; i++) { sales_maps[i] = new HashMap<>(); }

        //list index for months, hash map keys are product names,
        //hash map value list index for value type (total in-store sales = 0, total online sales = 1)
        List<HashMap<String, List<Integer>>> product_sales = Arrays.asList(sales_maps);
        product_sales = Collections.synchronizedList(product_sales);

        try {
            Thread server = new Thread(new Server());
            server.start();

            ExecutorService executor = Executors.newFixedThreadPool(10);
            for (int i = 0; i < 12; i++) {
                Socket client = new Socket("localhost", 8888);
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(client.getInputStream()));

                executor.execute(new RunnableThread8(i, inFromServer.readLine(), product_sales));

                client.close();
            }

            executor.shutdown();
            while (!executor.isTerminated()) {}
            System.out.println("Threads are complete.\n");

            System.out.println("Which product do you want to search?");
            String input = scanner.nextLine();

            int store_sales = 0;
            int online_sales = 0;
            for (int i = 0; i < 12; i++) {
                if (product_sales.get(i).get(input) != null) {
                    store_sales += product_sales.get(i).get(input).get(0);
                    online_sales += product_sales.get(i).get(input).get(1);
                }
            }

            System.out.println("For the product " + input + ",");
            System.out.println("In-store sales: " + store_sales + " TL");
            System.out.println("Online sales: " + online_sales + " TL");
            System.out.println("Total sales: " + (store_sales + online_sales) + " TL");

            System.out.println("\nProduct Sales:\n" + product_sales);
        }
        catch (IOException exception) { System.out.println("IO error"); }
    }
}

class Server implements Runnable {
    private final String[] files = {"01-January", "02-February", "03-March", "04-April", "05-May", "06-June", "07-July", "08-August",
            "09-September", "10-October", "11-November", "12-December"};

    public void run() {
        final String url = "http://homes.ieu.edu.tr/culudagli/files/SE375/datasets/";

        try {
            ServerSocket server = new ServerSocket(8888);

            for (String file : files) {
                Socket connection_socket = server.accept();
                DataOutputStream outToClient = new DataOutputStream(connection_socket.getOutputStream());

                outToClient.writeBytes((url + file + ".txt"));

                connection_socket.close();
            }

            server.close();
        }
        catch (IOException exception) { System.out.println("IO error"); }
    }
}

class RunnableThread8 implements Runnable {
    private final int month_index;
    private final String path;
    private final List<HashMap<String, List<Integer>>> shared_list;

    public RunnableThread8(int month_index, String path, List<HashMap<String, List<Integer>>> list) {
        this.month_index = month_index;
        this.path = path;
        shared_list = list;
    }

    public void run() {
        try {
            System.out.println((month_index + 1) + ". Thread parsing " + path);

            URL url = new URL(path);
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String row;
            List<List<String>> records = new ArrayList<>();
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                records.add(Arrays.asList(data));
            }

            csvReader.close();

            //remove the string values that cannot be parsed to int
            //and get product names to use as key values
            List<String> products = new ArrayList<>();
            String[][] values = new String[records.size() - 1][records.get(0).size() - 1];
            for (int i = 0; i < records.size() - 1; i++) {
                for (int j = 0; j < records.get(i + 1).size() - 1; j++) {
                    values[i][j] = records.get(i + 1).get(j + 1);
                }

                products.add(records.get(i + 1).get(0));
            }

            //parse values to int
            List<List<Integer>> product_sales;
            product_sales = (Arrays.stream(values)
                    .map(line -> Arrays.stream(line).map(Integer::parseInt)
                            .collect(Collectors.toList())).collect(Collectors.toList()));

            //calculate total sale amounts
            List<List<Integer>> total_product_sales = new ArrayList<>();
            for (List<Integer> list : product_sales) {
                int[] total_sales = new int[list.size() - 1];
                total_sales[0] = list.get(0) * list.get(1);
                total_sales[1] = list.get(0) * list.get(2);

                total_product_sales.add(Arrays.stream(total_sales).boxed().collect(Collectors.toList()));
            }

            //add sales to hash maps
            for (int i = 0; i < total_product_sales.size(); i++) {
                shared_list.get(month_index).put(products.get(i), total_product_sales.get(i));
            }
        }
        catch (FileNotFoundException exception) {
            System.out.println("File cannot found");
        }
        catch (IOException exception) {
            System.out.println("IO error");
        }
    }
}
