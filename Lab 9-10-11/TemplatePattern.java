import java.util.ArrayList;
import java.util.Scanner;

public class TemplatePattern {
    public static void main(String[] args) {
        Sort sort1 = new AscendingOrder();
        Sort sort2 = new DescendingOrder();
        Sort sort3 = new AscendingOrderNegative();

        System.out.println("Ascending sort:");
        sort1.sort();

        System.out.println("\nDescending sort:");
        sort2.sort();

        System.out.println("\nAscending negative sort:");
        sort3.sort();
    }
}

abstract class Sort {
    protected ArrayList<Integer> numbers = new ArrayList<>();

    public void sort() {
        getInput();
        bubbleSort();
        display();
    }

    protected void getInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter a non-negative number to add it to the list.");
            System.out.println("Entering a negative number will finalize the list.");
            int input = scanner.nextInt();

            if (checkInput(input))
                return;

            numbers.add(input);
        }
    }

    protected boolean checkInput(int input) { return (input < 0); }
    protected void display() { System.out.println(numbers); }

    private void bubbleSort() {
        for (int i = 0; i < numbers.size(); i++) {
            for (int j = 0; j < numbers.size() - 1; j++) {
                if (compare(numbers.get(j), numbers.get(j + 1)))
                    swap(j, j + 1);
            }
        }
    }

    private void swap(int index1, int index2) {
        int temp = numbers.get(index1);
        numbers.set(index1, numbers.get(index2));
        numbers.set(index2, temp);
    }

    protected abstract boolean compare(int a, int b);
}

class AscendingOrder extends Sort {
    protected boolean compare(int a, int b) { return (a > b); }
}

class DescendingOrder extends Sort {
    protected boolean compare(int a, int b) { return (a < b); }
}

class AscendingOrderNegative extends Sort {
    protected void getInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter a negative number to add it to the list.");
            System.out.println("Entering 0 will finalize the list.");
            int input = scanner.nextInt();

            if (checkInput(input))
                return;

            if (input < 0)
                numbers.add(input);
            else
                System.out.println("Please enter a negative number.");
        }
    }

    protected boolean checkInput(int input) { return (input == 0); }

    protected boolean compare(int a, int b) { return (a > b); }
}
