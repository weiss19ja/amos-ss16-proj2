package de.developgroup.mrf.server.handler;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientInformationHandler {

    // Contains Client's IP and additional information
    private static final List<ClientInformation> clientInformationList = Collections
            .synchronizedList(new ArrayList<ClientInformation>());

    public List<ClientInformation> getClientInformationList() {
        return clientInformationList;
    }


    public void addConnection(String ipAddress, int clientId) {
        boolean foundExistingObject = false;
        for (ClientInformation item : clientInformationList) {
            if (item.getIpAddress().equals(ipAddress)) {
                // Found the right client object
                foundExistingObject = true;
                item.addClientId(clientId);
                break;
            }
        }
        if (!foundExistingObject) {
            // add a new one
            ClientInformation item = new ClientInformation();
            item.setIpAddress(ipAddress);
            item.addClientId(clientId);
            clientInformationList.add(item);
        }
    }

    public void removeConnection(int clientId){
        for (ClientInformation item : clientInformationList) {
            if (item.containsClientId(clientId)) {
                // Found the right client object
                item.removeClientId(clientId);
                // Remove if no more connections to this ip exist
                if(item.hasNoClientId()){
                    clientInformationList.remove(item);
                }
                break;
            }
        }
    }

}
