import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbAccess {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    // Methode zum Herstellen einer Verbindung zur Datenbank
    public static void connect(String dbName) {
        String connectionString = "mongodb://localhost:27017";
        mongoClient = MongoClients.create(connectionString);
        database = mongoClient.getDatabase(dbName);
        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
    }

    // Methode zum Abrufen einer Sammlung (Collection) aus der Datenbank
    public static MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    // Methode zum Schließen der Verbindung
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
