package src.HistoryManager;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class History {
    private File historyFile;
    
    public void initializeHistoryFile(){
       URL url=getClass().getResource("/resources/History.txt");

       try{
            if(url != null){
                historyFile=new File(url.toURI());
            }else{
                historyFile=new File("resources/History.txt");
            }

            if(!historyFile.exists()){
                historyFile.createNewFile();
            }
       }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error causing: "+e.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
       }
    }

    public void appendToHistory(String license, String time,String date,String action){
        try(BufferedWriter writer=new BufferedWriter(new FileWriter(historyFile,true))){
            writer.write("Car "+license+" has "+action+" parked at "+time+" on "+date);
            writer.newLine();
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Failed to write history : "+e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void displayHistory(){
        try(BufferedReader reader=new BufferedReader(new FileReader(historyFile))){
            StringBuilder sb=new StringBuilder();
            String line;

            while((line=reader.readLine())!=null){
                sb.append(line).append("\n");
            }
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

           
            JDialog dialog = new JDialog();
            dialog.setTitle("Parking History");
            dialog.setModal(true);
            dialog.getContentPane().add(scrollPane);
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Failed to load cause: "+e.getMessage(), "Error", JOptionPane.ERROR);
        }
    }

    public void downloadHistory(){
        JFileChooser fileChooser=new JFileChooser();
           fileChooser.setSelectedFile(new File("History_File.txt"));

           int option=fileChooser.showSaveDialog(null);
           if(option==JFileChooser.APPROVE_OPTION){
              File dest=fileChooser.getSelectedFile();
            try(BufferedWriter writer=new BufferedWriter(new FileWriter(dest));
                BufferedReader reader=new BufferedReader(new FileReader(historyFile))){
                    String line;

                    while((line=reader.readLine())!=null){
                        writer.write(line);
                        writer.newLine();
                    }
                    JOptionPane.showMessageDialog(null, "History Downloaded to "+dest.getAbsolutePath());
                }catch(IOException ex){
                     JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                }
           }
        }
    }

