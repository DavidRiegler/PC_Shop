import java.util.Scanner;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ComputerInteractions {
    private static Scanner scanner;
    private static MongoCollection<Document> collection;
    private static ArrayList<Computer> computers;

    //Konstruktor
    public ComputerInteractions() {
        scanner = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("PC_Shop");
        collection = database.getCollection("computers");
        computers = new ArrayList<>();

        loadComputersFromDatabase();
    }

    // Methode zum Laden von Computern aus der Datenbank
    private void loadComputersFromDatabase() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Computer computer = new Computer();
                // Computerdaten aus dem Dokument extrahieren und setzen
                // (Hersteller, Modell, Arbeitsspeicher usw.)
                computer.setId(doc.getObjectId("_id"));
                computer.setHersteller(doc.getString("hersteller"));
                computer.setModell(doc.getString("modell"));
                computer.setArbeitsspeicher(doc.getInteger("arbeitsspeicher"));
                computer.setCpu(doc.getString("cpu"));
                computer.setMassenspeicher(doc.getInteger("massenspeicher"));
                computer.setTyp(doc.getString("typ"));
                computer.setEinzelpreis(doc.getDouble("einzelpreis"));
                computer.setSchnittstellen(doc.getString("schnittstellen"));
                computers.add(computer);
            }
        } finally {
            cursor.close();
        }
    }

    // Methode zur Interaktion mit Bestellungen
    public static void startInteraction() {
        while (true) {
            System.out.println("1. Neuen Computer hinzufügen");
            System.out.println("2. Computer bearbeiten");
            System.out.println("3. Alle Computer anzeigen");
            System.out.println("4. Computer löschen");
            System.out.println("5. Zurück zum Hauptmenü");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> addNewComputer();
                case 2 -> editComputer();
                case 3 -> displayComputers();
                case 4 -> deleteComputer();
                case 5 -> {
                    System.out.println("Zurück zum Hauptmenü.");
                    return;
                }
                default -> System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    //Methode zur Erstellung eines neuen Computers
    public static void addNewComputer() {
        Computer computer = new Computer();

        System.out.println("Hersteller:");
        computer.setHersteller(scanner.nextLine());
        System.out.println("Modell:");
        computer.setModell(scanner.nextLine());
        System.out.println("Arbeitsspeicher (GB):");
        computer.setArbeitsspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("CPU:");
        computer.setCpu(scanner.nextLine());
        System.out.println("Massenspeicher (GB):");
        computer.setMassenspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Typ:");
        computer.setTyp(scanner.nextLine());
        System.out.println("Einzelpreis:");
        computer.setEinzelpreis(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Schnittstellen:");
        computer.setSchnittstellen(scanner.nextLine());

        Document computerDoc = new Document("hersteller", computer.getHersteller())
                .append("modell", computer.getModell())
                .append("arbeitsspeicher", computer.getArbeitsspeicher())
                .append("cpu", computer.getCpu())
                .append("massenspeicher", computer.getMassenspeicher())
                .append("typ", computer.getTyp())
                .append("einzelpreis", computer.getEinzelpreis())
                .append("schnittstellen", computer.getSchnittstellen());
        collection.insertOne(computerDoc);
        computer.setId(computerDoc.getObjectId("_id"));
        computers.add(computer);

        System.out.println("Neuer Computer erfolgreich hinzugefügt:");
        System.out.println(computer);
    }

    //Methode zur Bearbeitung eines Computers
    public static void editComputer() {
        displayComputers();

        System.out.println("Geben Sie die Indexnummer des zu bearbeitenden Computers ein:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= computers.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Computer computer = computers.get(index);

        System.out.println("Aktueller Computer:");
        System.out.println(computer);

        System.out.println("Hersteller:");
        computer.setHersteller(scanner.nextLine());
        System.out.println("Modell:");
        computer.setModell(scanner.nextLine());
        System.out.println("Arbeitsspeicher (GB):");
        computer.setArbeitsspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("CPU:");
        computer.setCpu(scanner.nextLine());
        System.out.println("Massenspeicher (GB):");
        computer.setMassenspeicher(scanner.nextInt());
        scanner.nextLine();
        System.out.println("Typ:");
        computer.setTyp(scanner.nextLine());
        System.out.println("Einzelpreis:");
        computer.setEinzelpreis(scanner.nextDouble());
        scanner.nextLine();
        System.out.println("Schnittstellen:");
        computer.setSchnittstellen(scanner.nextLine());

        Document filter = new Document("_id", computer.getId());
        Document update = new Document("$set", new Document("hersteller", computer.getHersteller())
                .append("modell", computer.getModell())
                .append("arbeitsspeicher", computer.getArbeitsspeicher())
                .append("cpu", computer.getCpu())
                .append("massenspeicher", computer.getMassenspeicher())
                .append("typ", computer.getTyp())
                .append("einzelpreis", computer.getEinzelpreis())
                .append("schnittstellen", computer.getSchnittstellen()));
        collection.updateOne(filter, update);

        System.out.println("Computer erfolgreich bearbeitet:");
        System.out.println(computer);
    }

    //Methode zur Anzeige aller Computer
    public static void displayComputers() {
        System.out.println("Alle Computer:");

        if (computers.isEmpty()) {
            System.out.println("Keine Computer vorhanden.");
            return;
        }

        int index = 0;
        for (Computer computer : computers) {
            System.out.println("Indexnummer: " + index++);
            System.out.println(computer);
        }
    }

    //Methode zum Löschen eines Computers
    public static void deleteComputer() {
        displayComputers();

        System.out.println("Geben Sie die Indexnummer des Computers ein, den Sie löschen möchten:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= computers.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Computer computer = computers.remove(index);

        Document query = new Document("_id", computer.getId());
        collection.deleteOne(query);

        System.out.println("Computer erfolgreich gelöscht:");
        System.out.println(computer);
    }
}
