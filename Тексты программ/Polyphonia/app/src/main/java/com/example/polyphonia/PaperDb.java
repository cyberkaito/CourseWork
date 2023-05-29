package com.example.polyphonia;

import io.paperdb.Paper;

public class PaperDb {
    public Root.Clients getClient(){
        return Paper.book("session").read("client_authorized");
    }
    public void setClient(Root.Clients client){
        Paper.book("session").write("client_authorized", client);
    }
    public void removeUser(){
        Paper.book("session").delete("client_authorized");
    }
}
