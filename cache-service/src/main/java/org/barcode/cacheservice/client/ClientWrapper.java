package org.barcode.cacheservice.client;

import io.confluent.ksql.api.client.Client;
import io.confluent.ksql.api.client.ClientOptions;

public class ClientWrapper {

    private Client client;

    private ClientWrapper(){}

    private static ClientWrapper instance;

    public static ClientWrapper getInstance(){
        if(instance == null){
            instance = new ClientWrapper();
            instance.createClient();
            return instance;
        }
        return instance;
    }

    public Client getClient() {
        return client;
    }

    private void createClient(){
        this.client = Client.create(ClientOptions
                .create()
                .setHost("localhost")
                .setPort(8088)
                .setUseTls(false));
    }
}
