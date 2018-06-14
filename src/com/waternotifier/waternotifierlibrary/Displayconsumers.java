package com.waternotifier.waternotifierlibrary;

import java.awt.EventQueue;

//import javax.swing.table.DefaultTableModel;
//import net.proteanit.sql.DbUtils;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTable;
//import javax.swing.JOptionPane;


import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.sql.*;
import javax.swing.*;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import static javax.swing.JOptionPane.INFORMATION_MESSAGE;


public class Displayconsumers {

    Connection dbconnection;
    private JFrame frmConsumersReport;
    private JTable table;
    private JTextField textFirstName;
    private JTextField textLastName;
    private JTextField textPhone;

    Date datetime = new Date();

    /**
     * Create the application.
     */
    public Displayconsumers() {
        dbconnection = null;
        SqliteConnection.createNewDatabase("test.db");
//        JOptionPane.showMessageDialog(null, "You are here CreateNewDatabase done", "Dialoge Title", INFORMATION_MESSAGE );
        dbconnection = SqliteConnection.dbConnector();
        initialize();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Displayconsumers window = new Displayconsumers();
                    window.frmConsumersReport.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmConsumersReport = new JFrame();
        frmConsumersReport.setTitle("CONSUMERS REPORT");
        frmConsumersReport.setBounds(100, 100, 1201, 757);
        frmConsumersReport.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmConsumersReport.getContentPane().setLayout(null);

        JButton btnDisplayConsumers = new JButton("Display Consumers");
        btnDisplayConsumers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String firstName, lastName, phoneNum, whereAdditionalConditions = null, querySelect, queryOrderby;

                firstName = textFirstName.getText();
                lastName = textLastName.getText();
                phoneNum = textPhone.getText();

                if (!(firstName.isEmpty())) {
                    whereAdditionalConditions = " AND c.FirstName LIKE '%" + firstName + "%'";
                }

                if (!(lastName.isEmpty())) {
                    if (whereAdditionalConditions != null) {
                        whereAdditionalConditions = whereAdditionalConditions + " AND LastName LIKE '%" + lastName
                                + "%'";
                    } else {
                        whereAdditionalConditions = " AND c.LastName LIKE '%" + lastName + "%'";
                    }
                }

                if (!(phoneNum.isEmpty())) {
                    if (whereAdditionalConditions != null) {
                        whereAdditionalConditions = whereAdditionalConditions + " AND c.Phone = " + phoneNum + " ";
                    } else {
                        whereAdditionalConditions = " AND c.Phone = " + phoneNum + " ";
                    }
                }

                try {
                    // String query="select FirstName, LastName, Phone from Consumers order by
                    // CreateDateTime desc limit 4";
                    // String query="select FirstName, LastName, Phone from Consumers order by
                    // CreateDateTime desc";
                    // String query="select * from Consumers order by CreateDateTime desc";

//					querySelect = "select c.FirstName, c.LastName, c.Phone, n.NotifierName, n.LocationCityDistrict, n.IMEINumber, n.SIMPhone "
//							+ "from Consumers as c, NotifiersConsumers as nc, Notifiers as n "
//							+ "where c.Phone = nc.ConsumerROWID " + "AND nc.NotifierROWID = n.IMEINumber ";
//					queryOrderby = " order by n.LocationCityDistrict, c.CreateDateTime desc ";

                    querySelect = "select c.FirstName, c.LastName, c.Phone from Consumers as c";
                    queryOrderby = " order by c.CreateDateTime desc ";

                    String queryString = "";

                    if (whereAdditionalConditions == null) {
                        queryString = querySelect + queryOrderby;
                    } else {
                        queryString = querySelect + whereAdditionalConditions + queryOrderby;
                    }

                    Statement st = dbconnection.createStatement();
                    st.executeQuery(queryString);
//                    PreparedStatement pst = dbconnection.prepareStatement(queryString);

                    ResultSet rs = st.executeQuery(queryString);
//                    table.setModel(DbUtils.resultSetToTableModel(rs));

                    rs.close();
                    st.close();

                } catch (Exception e1) {
                    e1.printStackTrace();
                }


//                if (DatabaseHelper.newConsumer(8189678112L, 912142, "Rajesh Manvi")) {
//                    System.out.println("Successfully added user Rajesh Manvi to database." + '\n');
//                }
//
//
//                if (DatabaseHelper.newConsumer(8189570369L, 912142, "Rajesh Manvi")) {
//                    System.out.println("Successfully added user Rajesh Manvi to database." + '\n');
//                }

//                Consumer consumer = new Consumer();
//                List<String> listOfConsumers = new ArrayList<String>();
//
//                listOfConsumers = Consumer.getAllDatabaseNotifierConsumers(8186439252L, 0L, "Y", "");
//                if (listOfConsumers.isEmpty()) {
//                    System.out.println("There are no Consumers for given Notifier Phone");
//                } else {
//                    System.out.println("There are TOTAL of " + listOfConsumers.size() + " Consumers for given Notifier Phone " + 8186439252L);
//
//                    for (int i = 0; i < listOfConsumers.size(); i++) {
//                        System.out.println(listOfConsumers.get(i) + '\n');
//                    }
//
//                }
//
//                Notifier notifier = new Notifier();
//                if (Notifier.notifierExists(8186439252L)) {
//                    System.out.println("Valid Notifier exists " + 8186439252L + " in database." + '\n');
//                    System.out.println("Notifier Location is : " + Location.getLocationName(8186439252L) + " at database." + '\n');
//                } else {
//                    System.out.println("NO Notifier exists with SIMCardPhone " + 8186439252L + " at database." + '\n');
//                }
//
//                if (Consumer.consumerExists(8189678112L)) {
//                    System.out.println("Valid Consumer exists " + 8189678112L + " in database. %S" + datetime + '\n');
//                    System.out.println("Consumer belongs to following Notifiers/Locations : %S" + datetime + '\n');
//
//                } else {
//                    System.out.println("NO Notifier exists with SIMCardPhone " + 8189678112L + " at database." + '\n');
//                }
//
//                if (SMSDetails.insertToDatabase(8189678112L, 4086370230L, "Hello Mr. ROHIN this is a test message to store SMS details.")) {
//                    System.out.println("Successfully inserted SMS message to database. %S"  + datetime +  '\n');
//                }
//
//
//                if (VoiceCallDetails.insertToDatabase(8189678112L, 4086370230L)) {
//                    System.out.println("Successfully inserted Voice Call details, %S" + datetime + " to database." + '\n');
//                }

//                if (DatabaseHelper.deleteConsumer(8189678112L)) {
//                    System.out.println("Successfully Deleted Consumer 8189678112L for all locations." + '\n');
//                }


//                Long inNotifierPhone = 7472558416L;

// Function "LocationConsumers.deleteToDatabase(LocationCode)" will UPDATE all LocationConsumers
// for ConsumerCallerPhone = 0
// AND RegisteredFlag = 'N'
//                LocationConsumers.deleteToDatabase(5861041, "INDIA");
//                LocationConsumers.deleteToDatabase(5861041);
//                LocationConsumers.deleteToDatabase(912142, "USA");
//                LocationConsumers.deleteToDatabase(912142);


// Function "DatabaseHelper.updateAllConsumersForCallerPhone()" will UPDATE all Active - LocationConsumers
// with ConsumerCaller phone number.
                DatabaseHelper.updateAllConsumersForCallerPhone();


//                Long inNotifierPhone = 7472558828L;
                Long inNotifierPhone = 8189678112L;
//                Long inNotifierPhone = 8189135171L;

                Location tempLoc = new Location();
                tempLoc = Location.getNotifierLocationZIPCODESeqNumber(inNotifierPhone);


                List<ConsumerCallers> listOfConsumerCallers = new ArrayList<ConsumerCallers>();

                if (tempLoc == null) {
                    System.out.println("System could not find Location details for given Notifier Phone : " + inNotifierPhone + " !");
                }

                listOfConsumerCallers = ConsumerCallers.getAll(tempLoc.getZIPCODE(), tempLoc.getSeqNumber());

                if (listOfConsumerCallers.isEmpty() || listOfConsumerCallers == null) {
                    System.out.println("There are no Consumer Callers for given Notifier Phone : " + inNotifierPhone +
                            " at LocationCode : " + tempLoc.getZIPCODE() + tempLoc.getSeqNumber());
                } else {
                    System.out.println("There are TOTAL of " + listOfConsumerCallers.size() + "  Consumer Callers for given Notifier Phone : " + inNotifierPhone +
                            " at LocationCode : " + tempLoc.getZIPCODE() + tempLoc.getSeqNumber() + '\n');
                    for (int i = 0; i < listOfConsumerCallers.size(); i++) {
                        System.out.println("The Consumers for :: " + listOfConsumerCallers.get(i).getName() + '\n');

                        ArrayList<Long> locationConsumersArrayList = new ArrayList<>();

                        locationConsumersArrayList = LocationConsumers.getAllConsumerPhone(listOfConsumerCallers.get(i).getSIMCardPhone(),
                                tempLoc.getZIPCODE(), tempLoc.getSeqNumber());

                        for (int j = 0; j < locationConsumersArrayList.size(); j++) {
                            System.out.println( Integer.valueOf(j+1) + " :: " + locationConsumersArrayList.get(j) + '\n');
                        }
                    }

                }


                //Function "ConsumerCallers.getAllUniquePhoneNumbers()" will return unique all active ConsumerCallers from the database.
                ArrayList<Long> listOfUniqueConsumerCallers = ConsumerCallers.getAllUniquePhoneNumbers();

                if (!(listOfUniqueConsumerCallers.isEmpty() || listOfUniqueConsumerCallers == null)) {
                    System.out.println("There are total of " + listOfUniqueConsumerCallers.size() + " UNIQUE Consumer Callers in the system : \n");
                    for (int j = 0; j < listOfUniqueConsumerCallers.size(); j++) {
                        System.out.println(Integer.valueOf(j + 1) + " :: " + listOfUniqueConsumerCallers.get(j) + '\n');
                    }
                }

//                Following function will return value of Notifier Phone number for given LocationZIPCODESeqNumber
                Long tempNotifierPhone = 0L;
                int tempNotifierZIPCODESeqNumber = 0;

                tempNotifierZIPCODESeqNumber = 912144;
                tempNotifierPhone = Notifier.getSIMCardPhone(tempNotifierZIPCODESeqNumber);
                System.out.println("The Notifier Phone Number for given NotifierZIPCODESeqNumber :: " + tempNotifierZIPCODESeqNumber + " is =  " + tempNotifierPhone + '\n');


                tempNotifierZIPCODESeqNumber = 912141;
                tempNotifierPhone = Notifier.getSIMCardPhone(tempNotifierZIPCODESeqNumber);
                System.out.println("The Notifier Phone Number for given NotifierZIPCODESeqNumber :: " + tempNotifierZIPCODESeqNumber + " is =  " + tempNotifierPhone + '\n');


                tempNotifierZIPCODESeqNumber = 5861041;
                tempNotifierPhone = Notifier.getSIMCardPhone(tempNotifierZIPCODESeqNumber);
                System.out.println("The Notifier Phone Number for given NotifierZIPCODESeqNumber :: " + tempNotifierZIPCODESeqNumber + " is =  " + tempNotifierPhone + '\n');


            }
        });

        btnDisplayConsumers.setBounds(602, 35, 197, 22);
        frmConsumersReport.getContentPane().add(btnDisplayConsumers);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(51, 72, 1089, 605);
        frmConsumersReport.getContentPane().add(scrollPane);

        table = new JTable();
        scrollPane.setViewportView(table);

        JLabel lblNewLabel = new JLabel("First Name");
        lblNewLabel.setBounds(152, 13, 56, 16);
        frmConsumersReport.getContentPane().add(lblNewLabel);

        JLabel lblLastName = new JLabel("Last Name");
        lblLastName.setBounds(293, 13, 56, 16);
        frmConsumersReport.getContentPane().add(lblLastName);

        JLabel lblPhone = new JLabel("Phone");
        lblPhone.setBounds(438, 13, 56, 16);
        frmConsumersReport.getContentPane().add(lblPhone);

        textFirstName = new JTextField();
        textFirstName.setBounds(152, 35, 116, 22);
        frmConsumersReport.getContentPane().add(textFirstName);
        textFirstName.setColumns(10);

        textLastName = new JTextField();
        textLastName.setBounds(293, 35, 116, 22);
        frmConsumersReport.getContentPane().add(textLastName);
        textLastName.setColumns(10);

        textPhone = new JTextField();
        textPhone.setBounds(438, 35, 116, 22);
        frmConsumersReport.getContentPane().add(textPhone);
        textPhone.setColumns(10);

        JLabel lblReportFilters = new JLabel("FILTERS :");
        lblReportFilters.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblReportFilters.setBounds(51, 33, 81, 25);
        frmConsumersReport.getContentPane().add(lblReportFilters);
    }
}
