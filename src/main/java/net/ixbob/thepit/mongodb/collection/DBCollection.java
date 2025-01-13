package net.ixbob.thepit.mongodb.collection;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public abstract class DBCollection {

    public final MongoCollection<Document> legacyDBCollection;

    protected static final String FIELD_ID = "_id";

    protected DBCollection(MongoCollection<Document> legacyDBCollection) {
        this.legacyDBCollection = legacyDBCollection;
    }

    protected void insertOne(Document document) {
        this.legacyDBCollection.insertOne(document);
    }

    protected Document findFirstEqual(String field, Object value, Bson projectionFields) {
        return this.legacyDBCollection
                .find(eq(field, value))
                .projection(projectionFields)
                .first();
    }

    protected void replaceOne(Document document) {
        legacyDBCollection.replaceOne(new Document(FIELD_ID, document.get(FIELD_ID)), document);
    }
}
