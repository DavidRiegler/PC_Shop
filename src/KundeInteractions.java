import java.sql.Date;
import java.util.Scanner;
import java.util.ArrayList;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

public class KundeInteractions {
    private static Scanner scanner;
    private static MongoCollection<Document> collection;
    private static ArrayList<Kunde> customers;

    //Konstruktor
    public KundeInteractions() {
        scanner = new Scanner(System.in);
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoDatabase database = mongoClient.getDatabase("PC_Shop");
        collection = database.getCollection("customers");
        customers = new ArrayList<>();

        loadCustomersFromDatabase();
    }

    // Methode zum Laden von Kunden aus der Datenbank
    private void loadCustomersFromDatabase() {
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                Kunde customer = new Kunde();
                // Kundendaten aus dem Dokument extrahieren und setzen
                // (Geschlecht, Name, Adresse usw.)
                customer.setId(doc.getObjectId("_id"));
                customer.setGeschlecht(doc.getString("geschlecht"));
                customer.setNachname(doc.getString("nachname"));
                customer.setVorname(doc.getString("vorname"));
                customer.setAdresse(doc.getString("adresse"));
                customer.setTelefon(doc.getString("telefon"));
                customer.setEmail(doc.getString("email"));
                customer.setSprache(doc.getString("sprache"));
                customer.setGeburtsdatum(new java.sql.Date(((java.util.Date) doc.getDate("geburtsdatum")).getTime()));
                customers.add(customer);
            }
        } finally {
            cursor.close();
        }
    }

    // Methode zur Interaktion mit Kunden
    public static void startInteraction() {
        while (true) {
            System.out.println("1. Neuen Kunden hinzufügen");
            System.out.println("2. Kunden bearbeiten");
            System.out.println("3. Alle Kunden anzeigen");
            System.out.println("4. Kunden löschen");
            System.out.println("5. Zurück zum Hauptmenü");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> addNewCustomer();
                case 2 -> editCustomer();
                case 3 -> displayCustomers();
                case 4 -> deleteCustomer();
                case 5 -> {
                    System.out.println("Zurück zum Hauptmenü.");
                    return;
                }
                default -> System.out.println("Ungültige Option. Bitte wählen Sie erneut.");
            }
        }
    }

    //Methode zur Erstellung eines neuen Kunden
    public static void addNewCustomer() {
        Kunde customer = new Kunde();

        System.out.println("Geschlecht:");
        customer.setGeschlecht(scanner.nextLine());
        System.out.println("Nachname:");
        customer.setNachname(scanner.nextLine());
        System.out.println("Vorname:");
        customer.setVorname(scanner.nextLine());
        System.out.println("Adresse:");
        customer.setAdresse(scanner.nextLine());
        System.out.println("Telefon:");
        customer.setTelefon(scanner.nextLine());
        System.out.println("Email:");
        customer.setEmail(scanner.nextLine());
        System.out.println("Sprache:");
        customer.setSprache(scanner.nextLine());
        System.out.println("Geburtsdatum (yyyy-mm-dd):");
        customer.setGeburtsdatum(Date.valueOf(scanner.nextLine()));

        Document customerDoc = new Document("geschlecht", customer.getGeschlecht())
                .append("nachname", customer.getNachname())
                .append("vorname", customer.getVorname())
                .append("adresse", customer.getAdresse())
                .append("telefon", customer.getTelefon())
                .append("email", customer.getEmail())
                .append("sprache", customer.getSprache())
                .append("geburtsdatum", customer.getGeburtsdatum());
        collection.insertOne(customerDoc);
        ObjectId id = customerDoc.getObjectId("_id");
        customer.setId(id);

        customers.add(customer);

        System.out.println("Neuer Kunde erfolgreich hinzugefügt:");
        System.out.println(customer);
    }

    //Methode zur Bearbeitung eines Kunden
    public static void editCustomer() {
        displayCustomers();

        System.out.println("Geben Sie die Indexnummer des zu bearbeitenden Kunden ein:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= customers.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Kunde customer = customers.get(index);

        System.out.println("Aktueller Kunde:");
        System.out.println(customer);

        System.out.println("Geschlecht:");
        customer.setGeschlecht(scanner.nextLine());
        System.out.println("Nachname:");
        customer.setNachname(scanner.nextLine());
        System.out.println("Vorname:");
        customer.setVorname(scanner.nextLine());
        System.out.println("Adresse:");
        customer.setAdresse(scanner.nextLine());
        System.out.println("Telefon:");
        customer.setTelefon(scanner.nextLine());
        System.out.println("Email:");
        customer.setEmail(scanner.nextLine());
        System.out.println("Sprache:");
        customer.setSprache(scanner.nextLine());
        System.out.println("Geburtsdatum (yyyy-mm-dd):");
        customer.setGeburtsdatum(Date.valueOf(scanner.nextLine()));

        Document filter = new Document("_id", customer.getId());
        Document update = new Document("$set", new Document("geschlecht", customer.getGeschlecht())
                .append("nachname", customer.getNachname())
                .append("vorname", customer.getVorname())
                .append("adresse", customer.getAdresse())
                .append("telefon", customer.getTelefon())
                .append("email", customer.getEmail())
                .append("sprache", customer.getSprache())
                .append("geburtsdatum", customer.getGeburtsdatum()));
        collection.updateOne(filter, update);

        System.out.println("Kunde erfolgreich bearbeitet:");
        System.out.println(customer);
    }

    //Methode zur Anzeige aller Kunden
    public static void displayCustomers() {
        System.out.println("Alle Kunden:");

        if (customers.isEmpty()) {
            System.out.println("Keine Kunden vorhanden.");
            return;
        }

        int index = 0;
        for (Kunde customer : customers) {
            System.out.println("Indexnummer: " + index++);
            System.out.println(customer);
        }
    }

    //Methode zum Löschen eines Kunden
    public static void deleteCustomer() {
        displayCustomers();

        System.out.println("Geben Sie die Indexnummer des Kunden ein, den Sie löschen möchten:");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 0 || index >= customers.size()) {
            System.out.println("Ungültige Indexnummer.");
            return;
        }

        Kunde customer = customers.remove(index);

        Document query = new Document("_id", customer.getId());
        collection.deleteOne(query);

        System.out.println("Kunde erfolgreich gelöscht:");
        System.out.println(customer);
    }
}
