package src.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

public class User {
    private HashMap<String, String>users=new HashMap<>();
    private File userFile;

    public User(){
       try{
            URL url=getClass().getResource("/resources/user.txt");
            if(url != null){
                userFile=new File(url.toURI());
            }else{
                userFile=new File("resources/user.txt");
            }

            if(!userFile.exists()){
                userFile.createNewFile();
            }
       }catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error causing: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
       }
    }

    public boolean signIn(String userName,String password){
        String hashed=hashPassword(password);
        return users.containsKey(userName) && users.get(userName).equals(hashed);
    }

    public boolean signUp(String userName,String password){
        if(users.containsKey(userName)){
            return false;
        }
        String hashed=hashPassword(password);
        users.put(userName, hashed);
        saveUser();

        return true;
    }

    public void logOut(){}

    
    public void saveUser(){
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(userFile))){
            for(Map.Entry<String,String>entry : users.entrySet()){
                writer.write(entry.getKey()+":"+entry.getValue());
                writer.newLine();
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Failed to save history "+e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadUsers(){
        try(BufferedReader reader=new BufferedReader(new FileReader(userFile))){
            String line;
            while((line=reader.readLine())!=null){
                String[] p=line.split(":",2);
                if(p.length==2){
                    users.put(p[0], p[1]);
                }
            }
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Failed to load users : "+e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String hashPassword(String password){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes=md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error hashing password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

}
