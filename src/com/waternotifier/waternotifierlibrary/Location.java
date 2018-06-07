package com.waternotifier.waternotifierlibrary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Location {
    private int ZIPCODE;
    private int SeqNumber;
    private int ZIPCODESeqNumber;
    private String LocationName;
    private int ConsumerCount;
    private String Address1;
    private String Address2;
    private String City;
    private String State;
    private String Country;
    private String Landmark;
    private String Note1;
    private String Note2;
    private String Note3;
    private int Population;
    private String ValvemanName;
    private Long Phone;
    private String WaterSourceSWorGW;
    private int DistancetoWaterSource;
    private int WATERsuppliedtoNoofhomesatatime;
    private int noofhomesPerVALVE;
    private int WaterPUMPCapacityHP;
    private int GroundWaterDepth;
    private int WaterTANKheight;
    private int WaterPUMPOutputperhour;
    private int WaterTANKInletPipeDiameter;
    private int WaterTANKCapacity;
    private int WaterTANKOutletPipeDiameter;
    private int PublicWATERStoragesmallTANKs;
    private int PublicWATERStoragesmallTANKCapacity;
    private String SmallTankWaterSourceSWorGW;
    private int PUMPsusedforSmallTanks;
    private int FrequencyofPumping;
    private int FrequencyUnit;
    private String CreateDateTime;
    private String UpdateDateTime;

    /**
     * @param inLocationZIPCODE - For USA - it must be greater than 9999
     *                          For INDIA - It must be greater than 99999
     *                          NOTE : Change the program to behave according to country of implementation
     * @return True - if zipcode conditions are met, otherwise False.
     */
    public static Boolean validZIPCODE(int inLocationZIPCODE) {

//        INDIA
//        if (inLocationZIPCODE <= 99999) {
//            System.out.println("ZIPCODE " + inLocationZIPCODE + " must be of 6-digit or more in length!");
//            return Boolean.FALSE;
//        }

//        USA
        if (inLocationZIPCODE <= 9999) {
            System.out.println("ZIPCODE " + inLocationZIPCODE + " must be of 5-digit or more in length!");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    } // END of validZIPCODE

    public static Boolean validSeqNumber(int inLocationSeqNumber) {

        if (inLocationSeqNumber <= 0) {
            System.out.println("Sequence/Valve number " + inLocationSeqNumber + " must be greather than number 0!");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    } // END of validSeqNumber

    public static Boolean validZIPCODESeqNumber(int inZIPCODESeqNumber) {

////        INDIA
//        if (inZIPCODESeqNumber <= 9999999) {
//            System.out.println("ZIPCODE+SeqNumber number " + inZIPCODESeqNumber + " must be of 7-digit or more in length!");
//            return Boolean.FALSE;
//        }

//         USA
        if (inZIPCODESeqNumber <= 99999) {
            System.out.println("ZIPCODE and SeqNumber number " + inZIPCODESeqNumber + " must be of 6-digit or more in length!");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    } // END of validZIPCODESeqNumber


    /**
     * @param inLocationZIPCODE   -- If <= 99999 (less than 6-digits), Returns false
     * @param inLocationSeqNumber -- If 0 (empty), Returns false
     * @return Boolean - "True" Valid inLocationZIPCODE and inLocationSeqNumber exists with Status = 'Y'.
     * otherwise "False"
     */

    public static Boolean locationExists(int inLocationZIPCODE, int inLocationSeqNumber) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inLocationZIPCODE == 0 || inLocationSeqNumber == 0) {
            return Boolean.FALSE;
        }

        if (!Location.validZIPCODE(inLocationZIPCODE) && !Location.validSeqNumber(inLocationSeqNumber)) {
            return Boolean.FALSE;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT l.ZIPCODE, l.SeqNumber"
                    + " FROM Location as l "
                    + " WHERE l.ZIPCODE = " + inLocationZIPCODE + " "
                    + " AND l.SeqNumber = " + inLocationSeqNumber + " "
                    + " ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return Boolean.TRUE;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return Boolean.FALSE;
    } // END of locationExists

    /**
     * @param inLocationZIPCODESeqNumber -- If <= 999999 (less than 7-digits), Returns false
     * @return Boolean - "True" Valid inLocationZIPCODE and inLocationSeqNumber exists with Status = 'Y'.
     * otherwise "False"
     */

    public static Boolean locationExists(int inLocationZIPCODESeqNumber) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inLocationZIPCODESeqNumber == 0) {
            return Boolean.FALSE;
        }

        if (!Location.validZIPCODESeqNumber(inLocationZIPCODESeqNumber)) {
            return Boolean.FALSE;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT l.ZIPCODESeqNumber"
                    + " FROM Location as l "
                    + " WHERE l.ZIPCODESeqNumber = " + inLocationZIPCODESeqNumber + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return Boolean.TRUE;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return Boolean.FALSE;
    } // END of locationExists


    /**
     * @param inLocationZIPCODESeqNumber -- If <= 999999 (less than 7-digits), Returns false
     * @return Location - object if Valid inLocationZIPCODE and inLocationSeqNumber exists with Status = 'Y'.
     * otherwise "False"
     */

    public static Location getZIPCODESeqNumber(int inLocationZIPCODESeqNumber) {

        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        Location outLocation = new Location();

        if (inLocationZIPCODESeqNumber == 0) {
            return outLocation;
        }

        if (!Location.validZIPCODESeqNumber(inLocationZIPCODESeqNumber)) {
            return outLocation;
        }

        try {
            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT l.ZIPCODE, l.SeqNumber"
                    + " FROM Location as l "
                    + " WHERE l.ZIPCODESeqNumber = " + inLocationZIPCODESeqNumber + " ; ";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                outLocation.setZIPCODE(rs.getInt("ZIPCODE"));
                outLocation.setSeqNumber(rs.getInt("SeqNumber"));

                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return outLocation;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return outLocation;
    } // END of getZIPCODESeqNumber


    /**
     * @param inNotifierPhone -- If "" (empty), Returns "" string
     * @return String - Location Name - if Valid notifier exists with Status = 'Y'.
     * otherwise ""
     */
    public static String getLocationName(Long inNotifierPhone) {

        String returnValue = "";
        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inNotifierPhone == 0) {
            return returnValue;
        } else if (DataOperations.IsValidPhone(inNotifierPhone.toString())) {
            whereAdditionalConditions = " AND n.SIMCardPhone = " + inNotifierPhone + " ";
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT DISTINCT(l.LocationName)"
                    + " FROM SIMCard as sc, Notifiers as n, Location as l "
                    + " WHERE sc.Phone = n.SIMCardPhone "
                    + " AND n.Status = 'Y' "
                    + " AND sc.Active = 'Y' OR sc.Active IS NULL "
                    + " AND l.ZIPCODE = n.LocationZIPCODE "
                    + " AND l.SeqNumber = n.LocationSeqNumber "

            ;

            if (whereAdditionalConditions == null) {
                return returnValue;
            } else {
                queryString = querySelect + whereAdditionalConditions;
            }

            PreparedStatement pst = dbconnection.prepareStatement(queryString);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                returnValue = rs.getString("LocationName");
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return returnValue;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return returnValue;
    } // END of getLocationName


    /**
     * @param inLocationZIPCODESeqNumber -- If 0 or "" (empty), Returns "" string
     * @return String - Location Name - if Valid notifier exists with Status = 'Y'.
     * otherwise ""
     */
    public static String getLocationNameByLocationCode(int inLocationZIPCODESeqNumber) {

        String returnValue = "";
        String queryString = "", whereAdditionalConditions = "", querySelect = "";
        DataOperations dataOperations = new DataOperations();

        if (inLocationZIPCODESeqNumber == 0) {
            return returnValue;
        } else if (Location.validZIPCODESeqNumber(inLocationZIPCODESeqNumber)) {
            whereAdditionalConditions = " AND l.ZIPCODESeqNumber = " + inLocationZIPCODESeqNumber + " ";
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT DISTINCT(l.LocationName)"
                    + " FROM SIMCard as sc, Notifiers as n, Location as l "
                    + " WHERE sc.Phone = n.SIMCardPhone "
                    + " AND n.Status = 'Y' "
                    + " AND sc.Active = 'Y' OR sc.Active IS NULL "
                    + " AND l.ZIPCODE = n.LocationZIPCODE "
                    + " AND l.SeqNumber = n.LocationSeqNumber "

            ;

            if (whereAdditionalConditions == null) {
                return returnValue;
            } else {
                queryString = querySelect + whereAdditionalConditions;
            }

            PreparedStatement pst = dbconnection.prepareStatement(queryString);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                returnValue = rs.getString("LocationName");
                // Closing Statement
                pst.close();
                // Closing database connection
                dbconnection.close();
                return returnValue;
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return returnValue;
    } // END of getLocationName



    /**
     *
     * @param inNotifierPhone -- If "" (empty), Returns false

     * @return Boolean - "True" Valid notifier exists with Status = 'Y'.
     *                   otherwise "False"
     */
    public static Location getNotifierLocationZIPCODESeqNumber(Long inNotifierPhone) {

        Location outLocation = new Location();

        DataOperations dataOperations = new DataOperations();
        String querySelect = "";

        if (inNotifierPhone == 0) {
            System.out.println("Notifier phone number cannot be zero value : " + inNotifierPhone + '\n');
            return outLocation;
        }

        if (!(dataOperations.IsValidPhone(inNotifierPhone.toString()))) {
            System.out.println("Notifier phone is not valid/exists : " + inNotifierPhone + '\n');
            return outLocation;
        }

        if (!(Notifier.notifierExists(inNotifierPhone))) {
            System.out.println("There is no valid Notifier exists for phone : " + inNotifierPhone + '\n');
            return outLocation;
        }

        try {

            Connection dbconnection;
            dbconnection = SqliteConnection.dbConnector();

            querySelect = "SELECT l.ZIPCODE, l.SeqNumber "
                    + " FROM Location as l, Notifiers as n "
                    + " WHERE n.SIMCardPhone = " + inNotifierPhone + " "
                    + " AND n.LocationZIPCODE = l.ZIPCODE "
                    + " AND n.LocationSeqNumber = l.SeqNumber "
                    + " AND n.Status = 'Y' "
                    + " ;";

            PreparedStatement pst = dbconnection.prepareStatement(querySelect);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                outLocation.setZIPCODE(Integer.valueOf(rs.getString("ZIPCODE")));
                outLocation.setSeqNumber(Integer.valueOf(rs.getString("SeqNumber")));
            }
            // Closing Statement
            pst.close();
            // Closing database connection
            dbconnection.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }

        return outLocation;
    } // END of getNotifierLocationZIPCODESeqNumber


    public void setnoofhomesPerVALVE(int noofhomesPerVALVE) {
        this.noofhomesPerVALVE = noofhomesPerVALVE;
    }

    public int getZIPCODE() {
        return ZIPCODE;
    }

    public void setZIPCODE(int ZIPCODE) {
        this.ZIPCODE = ZIPCODE;
    }

    public int getSeqNumber() {
        return SeqNumber;
    }

    public void setSeqNumber(int SeqNumber) {
        this.SeqNumber = SeqNumber;
    }

    public int getZIPCODESeqNumber() {
        return ZIPCODESeqNumber;
    }

    public void setZIPCODESeqNumber(int ZIPCODESeqNumber) {
        this.ZIPCODESeqNumber = ZIPCODESeqNumber;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String LocationName) {
        this.LocationName = LocationName;
    }


    public int getConsumerCount() {
        return ConsumerCount;
    }

    public void setConsumerCount(int consumerCount) {
        ConsumerCount = consumerCount;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getLandmark() {
        return Landmark;
    }

    public void setLandmark(String Landmark) {
        this.Landmark = Landmark;
    }

    public String getNote1() {
        return Note1;
    }

    public void setNote1(String Note1) {
        this.Note1 = Note1;
    }

    public String getNote2() {
        return Note2;
    }

    public void setNote2(String Note2) {
        this.Note2 = Note2;
    }

    public String getNote3() {
        return Note3;
    }

    public void setNote3(String Note3) {
        this.Note3 = Note3;
    }

    public int getPopulation() {
        return Population;
    }

    public void setPopulation(int Population) {
        this.Population = Population;
    }

    public String getValvemanName() {
        return ValvemanName;
    }

    public void setValvemanName(String ValvemanName) {
        this.ValvemanName = ValvemanName;
    }

    public Long getPhone() {
        return Phone;
    }

    public void setPhone(Long Phone) {
        this.Phone = Phone;
    }

    public String getWaterSourceSWorGW() {
        return WaterSourceSWorGW;
    }

    public void setWaterSourceSWorGW(String WaterSourceSWorGW) {
        this.WaterSourceSWorGW = WaterSourceSWorGW;
    }

    public int getDistancetoWaterSource() {
        return DistancetoWaterSource;
    }

    public void setDistancetoWaterSource(int DistancetoWaterSource) {
        this.DistancetoWaterSource = DistancetoWaterSource;
    }

    public int getWATERsuppliedtoNoofhomesatatime() {
        return WATERsuppliedtoNoofhomesatatime;
    }

    public void setWATERsuppliedtoNoofhomesatatime(int WATERsuppliedtoNoofhomesatatime) {
        this.WATERsuppliedtoNoofhomesatatime = WATERsuppliedtoNoofhomesatatime;
    }

    public int getnoofhomesPerVALVE() {
        return noofhomesPerVALVE;
    }

    public int getWaterPUMPCapacityHP() {
        return WaterPUMPCapacityHP;
    }

    public void setWaterPUMPCapacityHP(int WaterPUMPCapacityHP) {
        this.WaterPUMPCapacityHP = WaterPUMPCapacityHP;
    }

    public int getGroundWaterDepth() {
        return GroundWaterDepth;
    }

    public void setGroundWaterDepth(int GroundWaterDepth) {
        this.GroundWaterDepth = GroundWaterDepth;
    }

    public int getWaterTANKheight() {
        return WaterTANKheight;
    }

    public void setWaterTANKheight(int WaterTANKheight) {
        this.WaterTANKheight = WaterTANKheight;
    }

    public int getWaterPUMPOutputperhour() {
        return WaterPUMPOutputperhour;
    }

    public void setWaterPUMPOutputperhour(int WaterPUMPOutputperhour) {
        this.WaterPUMPOutputperhour = WaterPUMPOutputperhour;
    }

    public int getWaterTANKInletPipeDiameter() {
        return WaterTANKInletPipeDiameter;
    }

    public void setWaterTANKInletPipeDiameter(int WaterTANKInletPipeDiameter) {
        this.WaterTANKInletPipeDiameter = WaterTANKInletPipeDiameter;
    }

    public int getWaterTANKCapacity() {
        return WaterTANKCapacity;
    }

    public void setWaterTANKCapacity(int WaterTANKCapacity) {
        this.WaterTANKCapacity = WaterTANKCapacity;
    }

    public int getWaterTANKOutletPipeDiameter() {
        return WaterTANKOutletPipeDiameter;
    }

    public void setWaterTANKOutletPipeDiameter(int WaterTANKOutletPipeDiameter) {
        this.WaterTANKOutletPipeDiameter = WaterTANKOutletPipeDiameter;
    }

    public int getPublicWATERStoragesmallTANKs() {
        return PublicWATERStoragesmallTANKs;
    }

    public void setPublicWATERStoragesmallTANKs(int PublicWATERStoragesmallTANKs) {
        this.PublicWATERStoragesmallTANKs = PublicWATERStoragesmallTANKs;
    }

    public int getPublicWATERStoragesmallTANKCapacity() {
        return PublicWATERStoragesmallTANKCapacity;
    }

    public void setPublicWATERStoragesmallTANKCapacity(int PublicWATERStoragesmallTANKCapacity) {
        this.PublicWATERStoragesmallTANKCapacity = PublicWATERStoragesmallTANKCapacity;
    }

    public String getSmallTankWaterSourceSWorGW() {
        return SmallTankWaterSourceSWorGW;
    }

    public void setSmallTankWaterSourceSWorGW(String SmallTankWaterSourceSWorGW) {
        this.SmallTankWaterSourceSWorGW = SmallTankWaterSourceSWorGW;
    }

    public int getPUMPsusedforSmallTanks() {
        return PUMPsusedforSmallTanks;
    }

    public void setPUMPsusedforSmallTanks(int PUMPsusedforSmallTanks) {
        this.PUMPsusedforSmallTanks = PUMPsusedforSmallTanks;
    }

    public int getFrequencyofPumping() {
        return FrequencyofPumping;
    }

    public void setFrequencyofPumping(int FrequencyofPumping) {
        this.FrequencyofPumping = FrequencyofPumping;
    }

    public int getFrequencyUnit() {
        return FrequencyUnit;
    }

    public void setFrequencyUnit(int FrequencyUnit) {
        this.FrequencyUnit = FrequencyUnit;
    }

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String CreateDateTime) {
        this.CreateDateTime = CreateDateTime;
    }

    public String getUpdateDateTime() {
        return UpdateDateTime;
    }

    public void setUpdateDateTime(String UpdateDateTime) {
        this.UpdateDateTime = UpdateDateTime;
    }


}
