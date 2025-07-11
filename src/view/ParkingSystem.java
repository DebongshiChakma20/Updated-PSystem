package src.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.*;

import src.HistoryManager.History;
import src.Model.CarParking;
import src.Model.User;

public class ParkingSystem extends JFrame{

    private JTextField licenseF,searchF;
    private JTable carTable;
    private DefaultTableModel tableModel;
    private boolean signInStatus=false;
    JLabel slotLabel;

    History history;
    CarParking parking;
    User users;

    ImageIcon logo=new ImageIcon(getClass().getResource("/resources/parking.png"));
    
    public ParkingSystem(){
        history=new History();
        history.initializeHistoryFile();

        parking=new CarParking(history);
        users=new User();
        
        setTitle("Parking_System");
        setSize(600,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel(new BorderLayout());

        JLabel timeLabel=new JLabel();

        timeLabel.setBackground(new Color(25, 118, 210));
        timeLabel.setPreferredSize(new Dimension(120,50));
        timeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        timeLabel.setForeground(Color.white);
        timeLabel.setFont(new Font("Sagoe UI",Font.BOLD,20));
        timeLabel.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
        timeLabel.setOpaque(true);
        

        Timer time =new Timer(1000, e->{
            timeLabel.setText(LocalTime.now().withNano(0).toString());
        });
        time.start();

        titlePanel.add(timeLabel,BorderLayout.WEST);

        JLabel title=new JLabel("Parking System");
        title.setBackground(new Color(25, 118, 210));
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,25));
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(180,50));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalTextPosition(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(0,40,9,60));

        titlePanel.add(title,BorderLayout.CENTER);

        
        JLabel dateLabel=new JLabel();
        DateTimeFormatter dd=DateTimeFormatter.ofPattern("dd-MM-yy");
        String date=LocalDate.now().format(dd);

        dateLabel.setText(date);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dateLabel.setBackground(new Color(25, 118, 210));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Segoe UI",Font.BOLD,20));
        dateLabel.setOpaque(true);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(0,0,0,20));

        titlePanel.add(dateLabel,BorderLayout.EAST);
        
        add(titlePanel,BorderLayout.NORTH);

        JPanel leftPanel=new JPanel(new BorderLayout());

        leftPanel.setBorder(BorderFactory.createTitledBorder(""));
        
        leftPanel.setPreferredSize(new Dimension(220,0));

        JPanel leftTop=new JPanel(new FlowLayout());
        leftTop.setPreferredSize(new Dimension(220,100));
        leftTop.setBackground(new Color(227,242,253));
        
        licenseF=new JTextField();
        licenseF.setPreferredSize(new Dimension(150,30));
        leftTop.add(licenseF);

        JButton parkB=new JButton("Park");
        JButton remove=new JButton("Remove");
        
        leftTop.add(parkB);
        leftTop.add(remove);
        
        leftPanel.add(leftTop,BorderLayout.NORTH);

        JPanel leftBottom = new JPanel();
        leftBottom.setLayout(new BoxLayout(leftBottom, BoxLayout.Y_AXIS));
        leftBottom.setPreferredSize(new Dimension(220,410));
        leftBottom.setBackground(new Color(224,247,250));
        

        JButton signIn=new JButton("SignIn");
        JButton signUp=new JButton("SignUp");
        JButton logOut=new JButton("Log Out");
        JButton viewHistory=new JButton("View History");
        JButton downHis=new JButton("Download History");

        
        signIn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signUp.setAlignmentX(Component.CENTER_ALIGNMENT);
        logOut.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewHistory.setAlignmentX(Component.CENTER_ALIGNMENT);
        downHis.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftBottom.add(Box.createVerticalStrut(25));
        leftBottom.add(signIn);
        leftBottom.add(Box.createVerticalStrut(25));
        leftBottom.add(signUp);
        leftBottom.add(Box.createVerticalStrut(25));
        leftBottom.add(logOut);
        leftBottom.add(Box.createVerticalStrut(25));
        leftBottom.add(viewHistory);
        leftBottom.add(Box.createVerticalStrut(25));
        leftBottom.add(downHis);
        
        leftPanel.add(leftBottom,BorderLayout.CENTER);

        JPanel searchPanel=new JPanel(new FlowLayout());
        searchPanel.setPreferredSize(new Dimension(220,35));
        searchF=new JTextField();
        searchF.setPreferredSize(new Dimension(100,25));
        searchPanel.setBackground(new Color(224,247,250));
        searchPanel.add(searchF);

        JButton searchB=new JButton("Search");
        searchPanel.add(searchB);

        leftPanel.add(searchPanel,BorderLayout.SOUTH);

        add(leftPanel,BorderLayout.WEST);


        JPanel rightPanel=new JPanel(new BorderLayout());
        

        String[] columns={"Cars","Time","Date"};
        tableModel=new DefaultTableModel(columns,0);
        carTable = new JTable(tableModel);

        JScrollPane scrollPane=new JScrollPane(carTable);
        scrollPane.getViewport().setBackground(new Color(229,235,231));
        
        rightPanel.add(scrollPane,BorderLayout.CENTER);
        
        slotLabel =new JLabel("Available Slots: "+parking.availableSlots());
        slotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        slotLabel.setBorder(BorderFactory.createEmptyBorder(10,0,10,10));
        slotLabel.setBackground(new Color(232,234,246));     
        slotLabel.setOpaque(true);   

        rightPanel.add(slotLabel,BorderLayout.SOUTH);

        add(rightPanel,BorderLayout.CENTER);

        parkB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!signInStatus){
                    JOptionPane.showMessageDialog(null, "SignIn First","Login",JOptionPane.ERROR_MESSAGE);
                    licenseF.setText("");
                    return;
                }
               String license=licenseF.getText().trim();
               if(license.isEmpty() ){
                    JOptionPane.showMessageDialog(null, "Enter a number First","Error",JOptionPane.ERROR_MESSAGE);
               }else if(parking.isParked(license)){
                    JOptionPane.showMessageDialog(null, "Car is already parked","Error",JOptionPane.ERROR_MESSAGE);
                    return;
               }else if(parking.availableSlots()==0){
                    JOptionPane.showMessageDialog(null, "NO slots available","Error",JOptionPane.ERROR_MESSAGE);
               }else{
                    parking.parkCar(license);
                    tableModel.addRow(new String[]{license,parking.getTime(license),parking.getDate(license)});
                    JOptionPane.showMessageDialog(null, "Car parked");
                    updateSlotLabel();
               }
               licenseF.setText("");
            }
            
        });

        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!signInStatus){
                    JOptionPane.showMessageDialog(null, "SignIn First","Login",JOptionPane.ERROR_MESSAGE);
                    licenseF.setText("");
                    return;
                }
                String license=licenseF.getText().trim();
                if(license.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Enter a number First","Error",JOptionPane.ERROR_MESSAGE);
               }else if(!parking.isParked(license)){
                    JOptionPane.showMessageDialog(null, "No car with such number","Not Found",JOptionPane.ERROR_MESSAGE);
                    return;
               }else{
                    parking.removeCar(license);
                    removeFromTable(license);
                    updateSlotLabel();
                    JOptionPane.showMessageDialog(null, "Car removed");
               }
               licenseF.setText("");
            }
            
        });

        signIn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                users.loadUsers();
               String user=JOptionPane.showInputDialog("Enter user name");
               if(user ==null || user.isEmpty()) return;

               JPasswordField passwordField=new JPasswordField();
               int confirm=JOptionPane.showConfirmDialog(null, passwordField, "Enter Password", JOptionPane.OK_CANCEL_OPTION);

               if(confirm !=JOptionPane.OK_OPTION) return;

               String pass=new String(passwordField.getPassword());
               if(users.signIn(user, pass)){
                    JOptionPane.showMessageDialog(null, "SignIn Succesfull","SignIn",JOptionPane.PLAIN_MESSAGE);
                    signInStatus=true;
               }else{
                    JOptionPane.showMessageDialog(null, "Invalid user or password","Error",JOptionPane.ERROR_MESSAGE);
               }
            }
            
        });

        signUp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String user=JOptionPane.showInputDialog("Enter a user name");
                if(user.isEmpty() || user==null) return;
                String pass=JOptionPane.showInputDialog("Enter a password");
                if(pass.isBlank() || pass==null) return;
                
                if(users.signUp(user, pass)){
                    JOptionPane.showMessageDialog(null, "New user added");
                    signInStatus=true;
                }else{
                     JOptionPane.showMessageDialog(null, "User already exist","Error",JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });



        viewHistory.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!signInStatus){
                    JOptionPane.showMessageDialog(null, "SignIn First","Login",JOptionPane.ERROR_MESSAGE);
                    licenseF.setText("");
                    return;
                }
                history.displayHistory();
            }
            
        });

        downHis.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(!signInStatus){
                    JOptionPane.showMessageDialog(null, "SignIn First","Login",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                history.downloadHistory();
            }
            
        });

        searchB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String license=searchF.getText().trim();
                parking.search(license);
            }
            
        });
        
        logOut.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm=JOptionPane.showConfirmDialog(null, "Are you sure?", "LogOut", JOptionPane.YES_NO_OPTION);
                if(confirm==JOptionPane.YES_OPTION){
                    parking.clearAll();
                    tableModel.setRowCount(0);
                    updateSlotLabel();
                    signInStatus=false;
                }

            }
            
        });
        
        setIconImage(logo.getImage());
        setVisible(true);
    }


    public void updateSlotLabel(){
        slotLabel.setText("Available Slots: "+parking.availableSlots());
    }

    public void removeFromTable(String license){
        for(int i=0;i<tableModel.getRowCount();i++){
            if(tableModel.getValueAt(i, 0).equals(license)){
                tableModel.removeRow(i);
                break;
            }
        }
    }
}
