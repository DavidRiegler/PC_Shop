import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ComputerInteractions computerManager = new ComputerInteractions();
        KundeInteractions customerManager = new KundeInteractions();
        BestellungInteractions orderManager = new BestellungInteractions();

        DbAccess.connect("PC_Shop");

        while (true) {
            System.out.println("Hauptmenü:");
            System.out.println("1. Computer verwalten");
            System.out.println("2. Kunden verwalten");
            System.out.println("3. Bestellungen verwalten");
            System.out.println("4. Beenden");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> ComputerInteractions.startInteraction();
                case 2 -> KundeInteractions.startInteraction();
                case 3 -> BestellungInteractions.startInteraction();
                case 4 -> {
                    System.out.println("Auf Wiedersehen!");
                    DbAccess.close();
                    System.exit(0);
                }
                default -> System.out.println("Ungültige Eingabe. Bitte wählen Sie erneut.");
            }
        }
    }
}
