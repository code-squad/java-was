package auth;

import java.util.Map;

import db.DataBase;
import model.User;

public class Join {
    
    private DataBase db;
    
    public Join (DataBase db) {
        this.db = db;
    }
    
    public void createNewMember(Map<String, String> info) {
        this.db.addUser(User.createNewUser(info));
    }

}
