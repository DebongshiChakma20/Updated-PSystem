package src.Model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import src.HistoryManager.History;

public class CarParking {
    private static final int TOTAL_SLOTS=30;
    private ArrayList<String>parkedCars=new ArrayList<>();
    private HashMap<String,String>parkedTime=new HashMap<>();
    private HashMap<String,String>parkedDate=new HashMap<>();
    private History history;

    public CarParking(History history){
        this.history=history;
    }

    public boolean parkCar(String licnese){
        if(parkedCars.contains(licnese) || parkedCars.size()>=TOTAL_SLOTS){
            return false;
        }

        parkedCars.add(licnese);
        String time=LocalTime.now().withNano(0).toString();
        parkedTime.put(licnese, time);
        DateTimeFormatter dd=DateTimeFormatter.ofPattern("dd-MM-yy");
        String date=LocalDate.now().format(dd);
        parkedDate.put(licnese, date);
        
        history.appendToHistory(licnese, time, date, " parked ");

        return true;
    }

    public boolean removeCar(String license){
        if(!parkedCars.contains(license)){
            return false;
        }

        parkedCars.remove(license);
        parkedTime.remove(license);
        parkedDate.remove(license);


        String time=LocalTime.now().withNano(0).toString();
        DateTimeFormatter dd=DateTimeFormatter.ofPattern("dd-MM-yy");
        String date=LocalDate.now().format(dd);

        history.appendToHistory(license, time, date, " left ");
        return true;
    }

    public boolean isParked(String license){
        return parkedCars.contains(license);
    }

    public String getTime(String license){
        return parkedTime.get(license);
    }

    public String getDate(String license){
        return parkedDate.get(license);
    }

    public void search(String license){
        if(!parkedCars.contains(license)){
            JOptionPane.showMessageDialog(null, "NO car found", "Not found", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(null, "Car "+license+" has parked at "+parkedTime.get(license)+"/"+parkedDate.get(license), "Search", JOptionPane.PLAIN_MESSAGE);
    }

    public int availableSlots(){
        return TOTAL_SLOTS-parkedCars.size();
    }

    public void clearAll(){
        parkedCars.clear();
        parkedDate.clear();
        parkedTime.clear();
    }
    
}
