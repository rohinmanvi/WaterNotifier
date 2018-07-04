package com.waternotifier.waternotifierlibrary;

import javax.swing.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.*;

public class LogToFile {
    
    protected static final Logger logger = Logger.getLogger("MYLOG");
    
    public static void main(String[] args) {
        
//        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
        
        logger.setUseParentHandlers(false);
        try {
            int i = 10, j = 0, x;
            // This block configure the logger with handler and formatter
            fh = new FileHandler("C:/WNDB/log/WNLogFile.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            
            // the following statement is used to log any messages
//            logger.info("WN first log");
            x = i/j;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
//            System.err.println("Got an exception! ");
//            System.err.println(e.getMessage());
            logger.info(e.getMessage());
//            logger.info(e.printStackTrace());
            log(e, "severe", "This is severe message!");
            log(e, "warning", "This is warning message!");
            log(e, "info", "This is info message!");
            log(e, "config", "This is config message!");
            log(e, "fine", "This is fine message!");
            log(e, "finer", "This is finer message!");
            log(e, "finest", "This is finest message!");
        }
        
//        logger.info("Hi How r u? - WN");
        
    }
    
    /**
     * log Method
     * enable to log all exceptions to a file and display user message on demand
     * @param ex
     * @param level
     * @param msg
     */
    public static void log(Exception ex, String level, String msg){
        
        FileHandler fh = null;
        try {
    
            // This block configure the logger with handler and formatter
            fh = new FileHandler("C:/WNDB/log/WNLogFile.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            
//            fh.setFormatter(new Formatter() {
//                @Override
//                public String format(LogRecord record) {
//                    SimpleDateFormat logTime = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//                    Calendar cal = new GregorianCalendar();
//                    cal.setTimeInMillis(record.getMillis());
//                    return record.getLevel()
//                            + logTime.format(cal.getTime())
//                            + " || "
//                            + record.getSourceClassName().substring(
//                            record.getSourceClassName().lastIndexOf(".")+1,
//                            record.getSourceClassName().length())
//                            + "."
//                            + record.getSourceMethodName()
//                            + "() : "
//                            + record.getMessage() + "\n";
//                }
//            });
    
            switch (level) {
                case "severe":
                    logger.log(Level.SEVERE, msg, ex);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case "warning":
                    logger.log(Level.WARNING, msg, ex);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Warning", JOptionPane.WARNING_MESSAGE);
                    break;
                case "info":
                    logger.log(Level.INFO, msg, ex);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "config":
                    logger.log(Level.CONFIG, msg, ex);
                    break;
                case "fine":
                    logger.log(Level.FINE, msg, ex);
                    break;
                case "finer":
                    logger.log(Level.FINER, msg, ex);
                    break;
                case "finest":
                    logger.log(Level.FINEST, msg, ex);
                    break;
                default:
                    logger.log(Level.CONFIG, msg, ex);
                    break;
            }
        } catch (IOException | SecurityException ex1) {
            logger.log(Level.SEVERE, null, ex1);
        } finally{
            if(fh!=null)fh.close();
        }
    }

    public static void log(String level, String msg){

        FileHandler fh = null;
        try {

            // This block configure the logger with handler and formatter
            fh = new FileHandler("C:/WNDB/log/WNLogFile.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            switch (level) {
                case "severe":
                    logger.log(Level.SEVERE, msg);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case "warning":
                    logger.log(Level.WARNING, msg);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Warning", JOptionPane.WARNING_MESSAGE);
                    break;
                case "info":
                    logger.log(Level.INFO, msg);
//                    if(!msg.equals(""))
//                        JOptionPane.showMessageDialog(null,msg,
//                                "Info", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case "config":
                    logger.log(Level.CONFIG, msg);
                    break;
                case "fine":
                    logger.log(Level.FINE, msg);
                    break;
                case "finer":
                    logger.log(Level.FINER, msg);
                    break;
                case "finest":
                    logger.log(Level.FINEST, msg);
                    break;
                default:
                    logger.log(Level.CONFIG, msg);
                    break;
            }
        } catch (IOException | SecurityException ex1) {
            logger.log(Level.SEVERE, null, ex1);
        } finally{
            if(fh!=null)fh.close();
        }
    }
    
}
