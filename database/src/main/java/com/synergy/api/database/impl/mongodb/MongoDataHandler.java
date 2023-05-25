package com.synergy.api.database.impl.mongodb;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.synergy.api.database.AbstractDataHandler;
import com.synergy.api.database.Credentials;
import java.util.concurrent.Executor;

public abstract class MongoDataHandler<T> extends AbstractDataHandler<T> {

  protected final MongoClient mongoClient;
  protected final MongoDatabase mongoDatabase;

  public MongoDataHandler(Executor executor, Credentials credentials) {
    this(
        executor,
        "mongodb+srv://"
            + credentials.user()
            + ":"
            + credentials.password()
            + "@"
            + credentials.host()
            + ":"
            + credentials.port()
            + "/?retryWrites=true&w=majority",
        credentials.database());
  }

  public MongoDataHandler(Executor executor, String connectionString, String database) {
    super(executor);
    ServerApi serverApi = ServerApi.builder().version(ServerApiVersion.V1).build();
    MongoClientSettings settings =
        MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .serverApi(serverApi)
            .build();
    mongoClient = MongoClients.create(settings);
    mongoDatabase = mongoClient.getDatabase(database);
  }

  @Override
  public void close() {
    mongoClient.close();
  }
}
