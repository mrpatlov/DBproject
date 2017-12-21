import java.sql.*;

public class DB {
    private static final String jdbcURL = "jdbc:mariadb://classdb2.csc.ncsu.edu:3306/pdsherk"; // Using SERVICE_NAME

    // Update your user and password info here!
    private static final String user = "pdsherk";
    private static final String password = "password";
    private Connection connection;
    public DB() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, user, password);
        } catch (Exception e) {
            System.out.println("cannot create connection");
        }
    }

    //Drop tables before beginning to clear any changes.
    public void dropTables() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate("DROP TABLE stay");
            statement.executeUpdate("DROP TABLE run");
            statement.executeUpdate("DROP TABLE request");
            statement.executeUpdate("DROP TABLE pays");
            statement.executeUpdate("DROP TABLE enter");
            statement.executeUpdate("DROP TABLE consults");
            statement.executeUpdate("DROP TABLE checkincheckout");
            statement.executeUpdate("DROP TABLE assigned");
            statement.executeUpdate("DROP TABLE appointed");
            statement.executeUpdate("DROP TABLE Ward");
            statement.executeUpdate("DROP TABLE Test");
            statement.executeUpdate("DROP TABLE Treatment");
            statement.executeUpdate("DROP TABLE Nurse");
            statement.executeUpdate("DROP TABLE Doctor");
            statement.executeUpdate("DROP TABLE Operator");
            statement.executeUpdate("DROP TABLE InsuranceCompany");
            statement.executeUpdate("DROP TABLE Billing");
            statement.executeUpdate("DROP TABLE MedicalRecord");
            statement.executeUpdate("DROP TABLE Patient");
            statement.executeUpdate("DROP TABLE Staff");
        } catch (SQLException e ) {
            System.out.println("Cannot drop tables");
        }
    }

    //Create all of the tables
    public void createTables() {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE Patient (patientID INT PRIMARY KEY, name VARCHAR(128) NOT NULL, SSN INT, age INT NOT NULL, gender VARCHAR(128) NOT NULL, DOB DATE NOT NULL, phoneNumber VARCHAR(128) NOT NULL, status VARCHAR(128) NOT NULL, address VARCHAR(128) NOT NULL, preferredWard VARCHAR(128))");
            statement.executeUpdate("CREATE TABLE Billing (billingID INT, patientID INT, visitDate DATE NOT NULL, fees INT NOT NULL, payMethod VARCHAR(128) NOT NULL, amount INT NOT NULL, cardNum VARCHAR(4) NOT NULL, PRIMARY KEY(billingID, patientID), CONSTRAINT bil_patient_c FOREIGN KEY (patientID)  REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE MedicalRecord (medicalID INT, patientID INT, start DATE NOT NULL, end DATE NOT NULL, prescription VARCHAR(128) NOT NULL, diagnosis VARCHAR(128) NOT NULL, PRIMARY KEY(medicalID, patientID), CONSTRAINT med_rec_patient FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE Staff (staffID INT PRIMARY KEY, phoneNumber VARCHAR(128) NOT NULL, address VARCHAR(128) NOT NULL, age INT NOT NULL, gender VARCHAR(128) NOT NULL, jobTitle VARCHAR(128) NOT NULL, name VARCHAR(128) NOT NULL, department VARCHAR(128) NOT NULL, profTitle VARCHAR(128) NOT NULL)");
            statement.executeUpdate("CREATE TABLE Ward (wardNum INT, staffID INT, charge INT NOT NULL, currentFill INT NOT NULL, capacity INT NOT NULL, PRIMARY KEY(wardNum, staffID), CONSTRAINT ward_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE NO ACTION)");
            statement.executeUpdate("CREATE TABLE Nurse (staffID INT PRIMARY KEY, CONSTRAINT nur_staff FOREIGN KEY (staffID) REFERENCES Staff (staffID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE Operator (staffID INT PRIMARY KEY, CONSTRAINT op_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE Doctor (staffID INT PRIMARY KEY, CONSTRAINT doc_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE InsuranceCompany (ID INT PRIMARY KEY, name VARCHAR(128) NOT NULL, address VARCHAR(128) NOT NULL, phoneNumber VARCHAR(128) NOT NULL)");
            statement.executeUpdate("CREATE TABLE Treatment (type VARCHAR(128), patientID INT, cost INT NOT NULL, description VARCHAR(128) NOT NULL, PRIMARY KEY (type, patientID), CONSTRAINT treat_t FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE Test (testID INT, patientID INT, type VARCHAR(128) NOT NULL, results VARCHAR(128) NOT NULL, PRIMARY KEY (testID, patientID), CONSTRAINT test_patient_c FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE consults (billingID INT, patientID INT, medicalID INT, PRIMARY KEY(billingID, patientID, medicalID), CONSTRAINT con_bill FOREIGN KEY (billingID) REFERENCES Billing(billingID) ON DELETE CASCADE, CONSTRAINT con_pat FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE, CONSTRAINT con_med FOREIGN KEY (medicalID) REFERENCES MedicalRecord(medicalID) ON DELETE CASCADE )");
            statement.executeUpdate("CREATE TABLE appointed (staffID INT, patientID INT, PRIMARY KEY (staffID, patientID), CONSTRAINT app_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE, CONSTRAINT app_pat FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE )");
            statement.executeUpdate("CREATE TABLE stay (patientID INT, wardNum INT, PRIMARY KEY (patientID, wardNum), CONSTRAINT st_pat FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE, CONSTRAINT st_ward FOREIGN KEY (wardNum) REFERENCES Ward(wardNum) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE checkincheckout (patientID INT, staffID INT, start DATE NOT NULL, end DATE, bed INT NOT NULL, PRIMARY KEY(patientID, staffID), CONSTRAINT chk_pat FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE, CONSTRAINT chk_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE NO ACTION)");
            statement.executeUpdate("CREATE TABLE pays (ID INT, billingID INT, PRIMARY KEY (ID, billingID), CONSTRAINT pay_ins FOREIGN KEY (ID) REFERENCES InsuranceCompany(ID) ON DELETE CASCADE, CONSTRAINT pay_billing FOREIGN KEY (billingID) REFERENCES Billing(billingID) ON DELETE CASCADE )");
            statement.executeUpdate("CREATE TABLE assigned (type VARCHAR(128), staffID INT, patientID INT, PRIMARY KEY (type, staffID, patientID), CONSTRAINT as_type FOREIGN KEY (type) REFERENCES Treatment(type) ON DELETE CASCADE, CONSTRAINT as_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE, CONSTRAINT as_patient FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE enter (staffID INT, medicalID INT, patientID INT, PRIMARY KEY (staffID, medicalID, patientID), CONSTRAINT ent_staff FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE, CONSTRAINT ent_med FOREIGN KEY (medicalID) REFERENCES MedicalRecord(medicalID) ON DELETE CASCADE, CONSTRAINT ent_pat FOREIGN KEY (patientID) REFERENCES Patient(patientID) ON DELETE CASCADE)");
            statement.executeUpdate("CREATE TABLE run (testID INT, staffID INT, PRIMARY KEY (testID, staffID), CONSTRAINT run_t FOREIGN KEY (testID) REFERENCES Test(testID) ON DELETE CASCADE, CONSTRAINT run_s FOREIGN KEY (staffID) REFERENCES Staff(staffID) ON DELETE CASCADE )");
            statement.executeUpdate("CREATE TABLE request (reqstaffID INT, specstaffID INT, PRIMARY KEY (reqstaffID, specstaffID), CONSTRAINT req_r FOREIGN KEY (reqstaffID) REFERENCES Staff(staffID) ON DELETE CASCADE, CONSTRAINT req_s FOREIGN KEY (specstaffID) REFERENCES Staff(staffID) ON DELETE CASCADE )");
        } catch (SQLException e) {
            System.out.println("create tables problem");
        }
    }

    //Insert the Demo Data
    public void insertTestData() {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            //Populate the Staff table
            statement.executeUpdate("INSERT INTO Staff(staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (1001, '919', '21 ABC St, NC 27', 36, 'F', 'Biller', 'Simpson', 'Billing', 'Accounts Supervisor')");
            statement.executeUpdate("INSERT INTO Staff(staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (1002, '123', '22 ABC St, NC 27', 45, 'M', 'Nurse', 'David', 'Casuality', 'Senior Nurse')");
            statement.executeUpdate("INSERT INTO Staff(staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (1005, '456', '23 ABC St, NC 27', 35, 'F', 'Nurse', 'Ruth', 'Casuality', 'Assistant Nurse')");
            statement.executeUpdate("INSERT INTO Staff(staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (1003, '631', '42 ABC St, NC 27', 40, 'F', 'Doctor', 'Lucy', 'Intensive Care', 'Senior Surgeon')");
            statement.executeUpdate("INSERT INTO Staff(staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (1004, '327', '51 ABC St, NC 27', 41, 'M', 'Doctor', 'Joseph', 'Pulmonary', 'Pulmonologist')");
            //Populate the Ward table
            statement.executeUpdate("INSERT INTO Ward (wardNum, staffID, charge, currentFill, capacity) VALUES (5001, 1002, 57, 0, 1)");
            //Populate the Patient table
            statement.executeUpdate("INSERT INTO Patient (patientID, name, SSN, age, gender, DOB, phoneNumber, status, address, preferredWard) VALUES (3001, 'John', null, 31, 'M', '1986-02-22', '513', 'Treatment complete', '81 ABC St, NC 27', '5001')");
            //Populate the checkincheckout table
            statement.executeUpdate("INSERT INTO checkincheckout (patientID, staffID, start, end, bed) VALUES (3001, 1002, '2017-10-05', null, 1)");
            //Populate the stay table
            statement.executeUpdate("INSERT INTO stay (patientID, wardNum) VALUES (3001, 5001)");
            //Populate the appointed table
            statement.executeUpdate("INSERT INTO appointed (staffID, patientID) VALUES (1003, 3001)");
            //Populate the MedicalRecord table
            statement.executeUpdate("INSERT INTO MedicalRecord(medicalID, patientID, start, end, prescription, diagnosis) VALUES (2001, 3001, '2017-10-05', '2017-10-31', 'antibiotics', 'Testing for TB')");
            statement.executeUpdate("INSERT INTO MedicalRecord(medicalID, patientID, start, end, prescription, diagnosis) VALUES (2002, 3001, '2017-11-01', '2017-11-16', 'continue antibiotics', 'Testing for TB')");
            //Populate the enter table
            statement.executeUpdate("INSERT INTO enter (staffID, medicalID, patientID) VALUES (1003, 2001, 3001)");
            statement.executeUpdate("INSERT INTO enter (staffID, medicalID, patientID) VALUES (1003, 2002, 3001)");
            //Populate the Treatment table
            statement.executeUpdate("INSERT INTO Treatment (type, patientID, cost, description) VALUES ('TB treatment', 3001, 199, 'Treatment for DB')");
            statement.executeUpdate("INSERT INTO Treatment (type, patientID, cost, description) VALUES ('Not required', 3001, 0, 'N/A')");
            //Populate the Test table
            statement.executeUpdate("INSERT INTO Test (testID, patientID, type, results) VALUES (1001, 3001, 'TB blood test', 'positive')");
            statement.executeUpdate("INSERT INTO Test (testID, patientID, type, results) VALUES (1002, 3001, 'X-ray chest (TB) Advanced', 'negative')");
            //Populate the run table
            statement.executeUpdate("INSERT INTO run (testID, staffID) VALUES (1001, 1004)");
            statement.executeUpdate("INSERT INTO run (testID, staffID) VALUES (1002, 1004)");
            //Populate the assigned table
            statement.executeUpdate("INSERT INTO assigned(type, staffID, patientID) VALUES ('TB treatment', 1004, 3001)");
            statement.executeUpdate("INSERT INTO assigned(type, staffID, patientID) VALUES ('Not required', 1004, 3001)");
            //Populate the request table
            statement.executeUpdate("INSERT INTO request (reqstaffID, specstaffID) VALUES (1003, 1004)");
            //Populate the Billing table
            statement.executeUpdate("INSERT INTO Billing (billingID, patientID, visitDate, fees, payMethod, amount, cardNum) VALUES ('1001', '3001', '2017-10-31', 324, 'Visa', 324, 1234)");
            statement.executeUpdate("INSERT INTO Billing (billingID, patientID, visitDate, fees, payMethod, amount, cardNum) VALUES ('1002', '3001', '2017-11-16', 125, 'Visa', 125, 1234)");
            //Populate the consults table
            statement.executeUpdate("INSERT INTO consults (billingID, patientID, medicalID) VALUES (1001, 3001, 2001)");
            statement.executeUpdate("INSERT INTO consults (billingID, patientID, medicalID) VALUES (1002, 3001, 2002)");
        } catch (SQLException e) {
            System.out.println("Issue creating test data");
            System.out.println(e);
        }
    }

    //Add a patient
    public void insertPatient(String id, String name, String ssn, String age, String gender, String dob, String phoneNum, String status, String address, String pWard) {
        try {
        	if (!pWard.equals("NULL"))
        		pWard = "'" + pWard + "'";
            Statement statement = null;
            statement = connection.createStatement();
            String stmt = String.format("INSERT INTO Patient (patientID, name, SSN, age, gender, DOB, phoneNumber, status, address, preferredWard) VALUES (%s, '%s', %s, %s, '%s', '%s', '%s', '%s', '%s', %s)", id, name, ssn, age, gender, dob, phoneNum, status, address, pWard);
            statement.executeUpdate(stmt);
            System.out.println("Successfully added patient.");
        } catch (SQLException e) {
            System.out.println("Error inserting patient");
            System.out.println(e);
        }
    }

	//Update a check in record
	public void updateCheckIn(String pid,String sid,String sdate,String edate,String bnum) {
		String[] old = new String[3];
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM checkincheckout WHERE patientID=%s AND staffID=%s",pid, sid);
			ResultSet rs = statement.executeQuery(stmt);
			while (rs.next()) {
				old[0] = rs.getString("start");
				old[1] = rs.getString("end");
				old[2] = rs.getString("bed");
			}
			if(sdate.equals(""))
				sdate = old[0];
			if(edate.equals(""))
				edate = old[1];
			if(bnum.equals(""))
				bnum = old[2];
			if (edate != null)
				edate = "'" + edate + "'";
			stmt = String.format("UPDATE checkincheckout SET start='%s', end=%s, bed=%s WHERE patientID=%s AND staffID=%s", sdate,edate,bnum,pid,sid);
			statement.executeUpdate(stmt);
			System.out.println("Successfully updated a checkin");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

    //Update the amount on a bill
    public void updateBillingAmount(String billID, String amount, String patID) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = String.format("Update Billing SET amount=%s WHERE billingID=%s AND patientID=%s", amount, billID, patID);
            statement.executeUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error updating billing amount");
            System.out.println(e);
        }
    }

    //Update a patient's billing acount (insert a new Billing record)
    public void updateBillingAccount(String billID, String visitDate, String patientID, String fees, String payMethod, String amount, String cardNum) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = String.format("INSERT INTO Billing (billingID, patientID, visitDate, fees, payMethod, amount, cardNum) VALUES(%s, %s, %s, %s, %s, %s, %s)", billID, patientID, visitDate, fees, payMethod, amount, cardNum);
            statement.executeUpdate(stmt);
        } catch (SQLException e) {
            System.out.println("Error updating billing account");
            System.out.println(e);
        }
    }

    //Generate a bill for a patient (grab all Billing records with that patient's ID)
    public void generateBill(String patientID) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = String.format("SELECT * FROM Billing WHERE patientID=%s", patientID);
            ResultSet rs = statement.executeQuery(stmt);
			System.out.println("Generating Bill for Patient " + patientID + "\n");
        	while (rs.next()) {
        		System.out.println("Billing ID: " + rs.getString("billingID"));
        		System.out.println("Visit Date: " + rs.getString("visitDate"));
        		System.out.println("Fees: " + rs.getString("fees"));
        		System.out.println("Payment Method: " + rs.getString("payMethod"));
        		System.out.println("Amount: " + rs.getString("amount"));
        		System.out.println("Card Number: " + rs.getString("cardNum"));
        		System.out.println();
        	}
        } catch (SQLException e) {
            System.out.println("Error generating bill");
            System.out.println(e);
        }
    }

    //Delete a Billing record
    public void deleteBill(String billingID, String patientID) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = String.format("DELETE FROM Billing WHERE billingID=%s AND patientID=%s", billingID, patientID);
            statement.executeUpdate(stmt);
			System.out.println("Bill Successfully Deleted.\n");
        } catch (SQLException e) {
            System.out.println("Error deleting bill");
            System.out.println(e);
        }
    }

    //Delete all Billing records associated with a patient
    public void deleteBillingAccount(String patientID) {
        try {
            Statement statement = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = String.format("DELETE FROM Billing WHERE patientID=%s", patientID);
            statement.executeUpdate(stmt);
            System.out.println("Successfully deleted billing account");
        } catch (SQLException e) {
            System.out.println("Error deleting billing account");
            System.out.println(e);
        }
    }

    //Check if there are any available wards in the hospital
    public void checkHospitalAvailability() {
        try {
            Statement statement = null;
            ResultSet rs = null;
            statement = connection.createStatement();
            // TODO: how to reference other fields
            String stmt = "SELECT currentFill, capacity, wardNum FROM Ward";
            rs = statement.executeQuery(stmt);
            while (rs.next()) {
				System.out.println("Ward Number: " + rs.getString("wardNum"));
                System.out.println("Current Fill: " + rs.getString("currentFill"));
                System.out.println("Capacity: " + rs.getString("capacity"));
				System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Error checking hospital availability");
            System.out.println(e);
        }
    }

    //Delete a patient
    public void deletePatient(String id) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("DELETE FROM Patient WHERE patientID = %s", id);
        statement.executeUpdate(stmt);
        System.out.println("Successfully deleted Patient.");
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Add a staff member
    public void addStaff(String id, String phoneNum, String address, String age, String gender, String jobTitle, String name, String dept, String profTitle) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("INSERT INTO Staff (staffID, phoneNumber, address, age, gender, jobTitle, name, department, profTitle) VALUES (%s, '%s', '%s', %s, '%s', '%s', '%s', '%s', '%s')", id, phoneNum, address, age, gender, jobTitle, name, dept, profTitle);
        statement.executeUpdate(stmt);
        System.out.println("Successfully added Staff.");
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Delete a staff member
    public void deleteStaff(String id) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("DELETE FROM Staff WHERE staffID = %s", id);
        statement.executeUpdate(stmt);
        System.out.println("Successfully deleted Staff.");
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Add a ward
    public void addWard(String wardNum, String staffID, String charge, String currentFill, String capacity) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("INSERT INTO Ward (wardNum, staffID, charge, currentFill, capacity) VALUES (%s, %s, %s, %s, %s)", wardNum, staffID, charge, currentFill, capacity);
        statement.executeUpdate(stmt);
        System.out.println("Successfully added Ward.");
      } catch (SQLException e) {
        System.out.println(e);
      }
    }

    //Check to see if a ward exists.
    //This is for the functionality to update a ward
    public boolean wardExists(String wardNum, String staffID) {
    	try {
    	Statement statement = null;
    	statement = connection.createStatement();
    	String stmt = String.format("SELECT * FROM Ward WHERE wardNum=%s AND staffID=%s", wardNum, staffID);
    	ResultSet i = statement.executeQuery(stmt);
    	if (!i.next())
    		return false;
    	else
    		return true;
    	} catch (SQLException e) {
    		System.out.println(e.toString());
    		return false;
    	}
	   }

     //Update a ward
     //User will only enter information that they want updated, this method
     //ensures that old values will still be there and allows for variable inputs
     public void updateWard(String wardNum, String staffID, String charge, String currentFill, String capacity) {
       String[] old = new String[3];
     	try {
         	Statement statement = null;
         	statement = connection.createStatement();
         	String stmt = String.format("SELECT * FROM Ward WHERE wardNum=%s AND staffID=%s", wardNum, staffID);
         	ResultSet rs = statement.executeQuery(stmt);
         	while (rs.next()) {
         		old[0] = rs.getString("charge");
         		old[1] = rs.getString("currentFill");
         		old[2] = rs.getString("capacity");
         	}
         	if (charge.equals(""))
         		charge = old[0];
         	if (currentFill.equals(""))
         		currentFill = old[1];
         	if (capacity.equals(""))
         		capacity = old[2];
         	stmt = String.format("UPDATE Ward SET charge=%s, currentFill=%s, capacity=%s WHERE wardNum=%s AND staffID=%s", charge, currentFill, capacity, wardNum, staffID);
         	statement.executeUpdate(stmt);
         	System.out.println("Successfully updated a ward.");
         } catch (SQLException e) {
         	System.out.println(e.toString());
         }
     }

    //Delete a ward
    public void deleteWard(String wardNum, String staffID) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("DELETE FROM Ward WHERE wardNum = %s AND staffID = %s", wardNum, staffID);
        statement.executeUpdate(stmt);
        System.out.println("Successfully deleted Ward");
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Check to see if a bed is available in a specific ward.
    public void checkBed(String wardNum) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("SELECT currentFill, capacity FROM Ward WHERE wardNum = %s", wardNum);
        ResultSet rs = statement.executeQuery(stmt);
        while(rs.next()) {
          int currentFill = rs.getInt("currentFill");
          int capacity = rs.getInt("capacity");
          System.out.println("Current Fill: " + currentFill + " Capacity: " + capacity);
        }
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Assign a patient to a ward.
    public void assignToWard(String patientID, String staffID, String start, String bed) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("INSERT INTO checkincheckout(patientID, staffID, start, end, bed) VALUES (%s, %s, '%s', NULL, %s)", patientID, staffID, start, bed);
        statement.executeUpdate(stmt);
        System.out.println("Successfully assigned to ward.");
      } catch (SQLException e) {
          System.out.println(e);
      }
    }

    //Ensure that a patient exists.
    //This is for the functionality to update a patient
    public boolean patientIDExists(String id) {
    	try {
    	Statement statement = null;
    	statement = connection.createStatement();
    	String stmt = String.format("SELECT * FROM Patient WHERE patientID=%s", id);
    	ResultSet i = statement.executeQuery(stmt);
    	if (!i.next())
    		return false;
    	else
    		return true;
    	} catch (SQLException e) {
    		System.out.println(e.toString());
    		return false;
    	}
	}

    //Update a patient
    //User will only enter information that they want updated, this method
    //ensures that old values will still be there and allows for variable inputs
    public void updatePatient(String id, String name, String ssn, String age, String gender, String dob, String phoneNum, String status, String address, String pWard) {
    	String[] old = new String[9];
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM Patient WHERE patientID=%s", id);
        	ResultSet rs = statement.executeQuery(stmt);
        	while (rs.next()) {
        		old[0] = rs.getString("name");
        		old[1] = rs.getString("SSN");
        		old[2] = rs.getString("age");
        		old[3] = rs.getString("gender");
        		old[4] = rs.getString("DOB");
        		old[5] = rs.getString("phoneNumber");
        		old[6] = rs.getString("status");
        		old[7] = rs.getString("address");
        		old[8] = rs.getString("preferredWard");
        	}
        	if (name.equals(""))
        		name = old[0];
        	if (ssn.equals(""))
        		ssn = old[1];
        	if (age.equals(""))
        		age = old[2];
        	if (gender.equals(""))
        		gender = old[3];
        	if (dob.equals(""))
        		dob = old[4];
        	if (phoneNum.equals(""))
        		phoneNum = old[5];
        	if (status.equals(""))
        		status = old[6];
        	if (address.equals(""))
        		address = old[7];
        	if (pWard.equals(""))
        		pWard = old[8];
        	stmt = String.format("UPDATE Patient SET name='%s', SSN=%s, age=%s, gender='%s', DOB='%s', phoneNumber='%s', status='%s', address='%s', preferredWard='%s' WHERE patientID=%s", name, ssn, age, gender, dob, phoneNum, status, address, pWard, id);
        	statement.executeUpdate(stmt);
        	System.out.println("Successfully updated a patient.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Check to see if a staff member exists.
    //This is for the functionality to update a staff member
    public boolean staffIDExists(String id) {
    	try {
    	Statement statement = null;
    	statement = connection.createStatement();
    	String stmt = String.format("SELECT * FROM Staff WHERE staffID=%s", id);
    	ResultSet i = statement.executeQuery(stmt);
    	if (!i.next())
    		return false;
    	else
    		return true;
    	} catch (SQLException e) {
    		System.out.println(e.toString());
    		return false;
    	}
	}

    //Update a staff member
    //User will only enter information that they want updated, this method
    //ensures that old values will still be there and allows for variable inputs
    public void updateStaff(String id, String phoneNum, String address, String age, String gender, String jobTitle, String name, String dept, String profTitle) {
    	String[] old = new String[8];
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM Staff WHERE staffID=%s", id);
        	ResultSet rs = statement.executeQuery(stmt);
        	while (rs.next()) {
        		old[0] = rs.getString("phoneNumber");
        		old[1] = rs.getString("address");
        		old[2] = rs.getString("age");
        		old[3] = rs.getString("gender");
        		old[4] = rs.getString("jobTitle");
        		old[5] = rs.getString("name");
        		old[6] = rs.getString("department");
        		old[7] = rs.getString("profTitle");
        	}
        	if (phoneNum.equals(""))
        		phoneNum = old[0];
        	if (address.equals(""))
        		address = old[1];
        	if (age.equals(""))
        		age = old[2];
        	if (gender.equals(""))
        		gender = old[3];
        	if (jobTitle.equals(""))
        		jobTitle = old[4];
        	if (name.equals(""))
        		name = old[5];
        	if (dept.equals(""))
        		dept = old[6];
        	if (profTitle.equals(""))
        		profTitle = old[7];
        	stmt = String.format("UPDATE Staff SET phoneNumber='%s', address='%s', age=%s, gender='%s', jobTitle='%s', name='%s', department='%s', profTitle='%s' WHERE staffID=%s", phoneNum, address, age, gender, jobTitle, name, dept, profTitle, id);
        	statement.executeUpdate(stmt);
        	System.out.println("Successfully updated a staff member.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Reserve a bed in a ward
    public void reserveBed(String patientID, String bed, String staffID, String wardID) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("UPDATE checkincheckout SET bed=%s WHERE patientID=%s AND staffID=%s", bed, patientID, staffID);
        statement.executeUpdate(stmt);
        stmt = String.format("SELECT currentFill FROM Ward WHERE wardNum=%s", wardID);
        ResultSet rs = statement.executeQuery(stmt);
        rs.next();
        int num = rs.getInt("currentFill");
        num++;
        stmt = String.format("UPDATE Ward SET currentFill= %s", num);
        statement.executeUpdate(stmt);
        System.out.println("Successfully reserved bed.");
      } catch (SQLException e) {
        System.out.println(e);
      }
    }

    //Release a bed in a ward
    //Sets the end date for a checkincheckout record for a patient
    public void releaseBed(String patientID, String end, String staffID, String wardID) {
      try {
        Statement statement = null;
        statement = connection.createStatement();
        String stmt = String.format("UPDATE checkincheckout SET end='%s' WHERE patientID=%s AND staffID=%s", end, patientID, staffID);
        statement.executeUpdate(stmt);
        stmt = String.format("SELECT currentFill FROM Ward WHERE wardNum=%s", wardID);
        ResultSet rs = statement.executeQuery(stmt);
        rs.next();
        int num = rs.getInt("currentFill");
        num--;
        stmt = String.format("UPDATE Ward SET currentFill= %s", num);
        statement.executeUpdate(stmt);
        System.out.println("Successfully released bed.");
      } catch (SQLException e) {
        System.out.println(e);
      }
    }

    //Get a report of the medical records for a specific patient
    public void basicReportNoDate(String id) {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM MedicalRecord WHERE patientID=%s", id);
        	ResultSet rs = statement.executeQuery(stmt);
        	System.out.println("Report for Patient " + id + "\n");
        	while (rs.next()) {
        		System.out.println("Record:" + rs.getString("medicalID"));
        		System.out.println("Start: " + rs.getString("start"));
        		System.out.println("End: " + rs.getString("end"));
        		System.out.println("Prescription: " + rs.getString("prescription"));
        		System.out.println("Diagnosis: " + rs.getString("diagnosis"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Get a report of medical records for a patient for a date range
    public void basicReportDate(String id, String startDate, String endDate) {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM MedicalRecord WHERE patientID=%s AND start >= '%s' AND end <= '%s'", id, startDate, endDate);
        	ResultSet rs = statement.executeQuery(stmt);
        	System.out.println("Report for Patient " + id + " during time window " + startDate + " to " + endDate + "\n");
        	while (rs.next()) {
        		System.out.println("Record:" + rs.getString("medicalID"));
        		System.out.println("Start: " + rs.getString("start"));
        		System.out.println("End: " + rs.getString("end"));
        		System.out.println("Prescription: " + rs.getString("prescription"));
        		System.out.println("Diagnosis: " + rs.getString("diagnosis"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Get the report for all wards and their current status
    public void wardReport() {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	ResultSet rs = statement.executeQuery("SELECT wardNum, currentFill, capacity FROM Ward");
        	System.out.println("Report for all Wards/Beds\n\nWard Number\tCurrent Fill\tCapacity");
        	while (rs.next()) {
        		System.out.print(rs.getString("wardNum"));
        		System.out.print("\t\t" + rs.getString("currentFill"));
        		System.out.print("\t\t\t" + rs.getString("capacity"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Report for patients per month
    public void patientsPerMonth() {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	ResultSet rs = statement.executeQuery("SELECT count(1) AS patients, MONTH(start) FROM checkincheckout GROUP BY MONTH (start)");
        	System.out.println("Report for Patients Per Month\nMonth\tCapacity");
        	while (rs.next()) {
        		System.out.print(rs.getString("MONTH(start)"));
        		System.out.print("\t" + rs.getString("patients"));
        		System.out.println();
        	}
        	System.out.println("\nReport Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Ward percentage
    public void wardPercent() {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	ResultSet rs = statement.executeQuery("SELECT wardNum, (100.0 * (currentFill / capacity)) AS percentage  FROM Ward");
        	System.out.println("Report Percentage Usage Per Ward\nWard Number\tPercentage");
        	while (rs.next()) {
        		System.out.print(rs.getString("wardNum"));
        		System.out.print("\t\t" + rs.getString("percentage"));
        		System.out.println();
        	}
        	System.out.println("\nReport Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Report all the staff that a Doctor is assigned to
    public void reportDocPatient(String doc) {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM Patient WHERE patientID IN (SELECT patientID FROM appointed WHERE staffID=%s)", doc);
        	ResultSet rs = statement.executeQuery(stmt);
        	System.out.println("Report for All Patients that Staff " + doc + " is Responsible For\n");
        	while (rs.next()) {
        		System.out.println("Patient ID: " + rs.getString("patientID"));
        		System.out.println("Name: " + rs.getString("name"));
        		System.out.println("SSN: " + rs.getString("SSN"));
        		System.out.println("Age: " + rs.getString("age"));
        		System.out.println("Gender: " + rs.getString("gender"));
        		System.out.println("DOB: " + rs.getString("DOB"));
        		System.out.println("Phone Number: " + rs.getString("phoneNumber"));
        		System.out.println("Status: " + rs.getString("status"));
        		System.out.println("Address: " + rs.getString("address"));
        		System.out.println("Preferred Ward: " + rs.getString("preferredWard"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Report all staff grouped by their role
    public void reportStaff() {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	ResultSet rs = statement.executeQuery("SELECT * FROM Staff ORDER BY jobTitle");
        	System.out.println("Report Staff Grouped By Role\n");
        	while (rs.next()) {
        		System.out.println("Staff ID: " + rs.getString("staffID"));
        		System.out.println("Phone Number: " + rs.getString("phoneNumber"));
        		System.out.println("Address: " + rs.getString("address"));
        		System.out.println("Age: " + rs.getString("age"));
        		System.out.println("Gender: " + rs.getString("gender"));
        		System.out.println("Job Title: " + rs.getString("jobTitle"));
        		System.out.println("Name: " + rs.getString("name"));
        		System.out.println("Department: " + rs.getString("department"));
        		System.out.println("Professional Title: " + rs.getString("profTitle"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    public void reportBilling(String patientID) {
    	try {
        	Statement statement = null;
        	statement = connection.createStatement();
        	String stmt = String.format("SELECT * FROM Billing WHERE patientID=%s", patientID);
        	ResultSet rs = statement.executeQuery(stmt);
        	System.out.println("Report Billing for Patient " + patientID + "\n");
        	while (rs.next()) {
        		System.out.println("Billing ID: " + rs.getString("billingID"));
        		System.out.println("Visit Date: " + rs.getString("visitDate"));
        		System.out.println("Fees: " + rs.getString("fees"));
        		System.out.println("Payment Method: " + rs.getString("payMethod"));
        		System.out.println("Amount: " + rs.getString("amount"));
        		System.out.println("Card Number: " + rs.getString("cardNum"));
        		System.out.println();
        	}
        	System.out.println("Report Done.");
        } catch (SQLException e) {
        	System.out.println(e.toString());
        }
    }

    //Update a Test record
	public void updateTest(String tid,String pid,String t,String result) {
		String[] old = new String[2];
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM Test WHERE patientID=%s AND testID=%s",pid, tid);
			ResultSet rs = statement.executeQuery(stmt);
			while (rs.next()) {
				old[0] = rs.getString("type");
				old[1] = rs.getString("results");
			}
			if(t.equals(""))
				t=old[0];
			if(result.equals(""))
				result=old[1];

			stmt = String.format("UPDATE Test SET type='%s', results='%s' WHERE patientID=%s AND testID=%s",t,result, pid, tid);
			statement.executeUpdate(stmt);
			System.out.println("Successfully updated a test");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //Update a Treatment record
	public void updateTreatment(String pid,String t, String cost, String des) {
		String[] old = new String[3];
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM Treatment WHERE patientID=%s AND type='%s'",pid,t);
			ResultSet rs = statement.executeQuery(stmt);
			while (rs.next()) {
				old[0] = rs.getString("type");
				old[1] = rs.getString("cost");
				old[2] = rs.getString("description");
			}
			if(cost.equals(""))
				cost=old[1];
			if(des.equals(""))
				des=old[2];
			stmt = String.format("UPDATE Treatment SET cost=%s, description='%s' WHERE patientID=%s AND type='%s'", cost,des, pid, t);
			statement.executeUpdate(stmt);
			System.out.println("Successfully updates a treatment");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //Update a medical record
	public void updateMedicalRecord(String mid,String pid,String sdate,String edate,String pres,String diag) {
		String[] old = new String[4];
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM MedicalRecord WHERE patientID=%s AND medicalID=%s", pid,mid);
			ResultSet rs = statement.executeQuery(stmt);
			while (rs.next()) {
				old[0] = rs.getString("start");
				old[1] = rs.getString("end");
				old[2] = rs.getString("prescription");
				old[3] = rs.getString("diagnosis");
			}
			if(sdate.equals(""))
				sdate=old[0];
			if(edate.equals(""))
				edate=old[1];
			if(pres.equals(""))
				pres=old[2];
			if(diag.equals(""))
				diag=old[3];
			stmt = String.format("UPDATE MedicalRecord SET start='%s', end='%s', prescription='%s', diagnosis='%s' WHERE patientID=%s AND medicalID=%s",sdate,edate,pres,diag, pid, mid);
			statement.executeUpdate(stmt);
			System.out.println("Successfully updated a medical record\n");
			statement.executeUpdate(stmt);
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //See if a test exists
  //Used for updating a test
	public boolean testIDExists(String id, String pid) {
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM Test WHERE testID=%s AND patientID=%s", id, pid);
			ResultSet i = statement.executeQuery(stmt);
			if (!i.next())
				return false;
			else
				return true;
			} catch (SQLException e) {
				System.out.println(e.toString());
				return false;
			}
	}

  //See if a medical record exists
  //Used for updating a medical record
	public boolean medicalRecordExists(String id) {
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("SELECT * FROM MedicalRecord WHERE medicalID=%s", id);
			ResultSet i = statement.executeQuery(stmt);
			if (!i.next())
				return false;
			else
				return true;
			} catch (SQLException e) {
				System.out.println(e.toString());
				return false;
			}
	}

  //Create a new checkin record
	public void newCheckIn(String pid,String sid,String sdate,String edate,String bnum) {
		try {
			if (!edate.equals("NULL"))
				edate = "'" + edate + "'";
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("INSERT INTO checkincheckout(patientID, staffID, start, end, bed) VALUES (%s, %s, '%s', %s, %s)", pid, sid, sdate, edate, bnum);
			statement.executeUpdate(stmt);
			System.out.println("Check in successfull.\n");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //Create a new Test
	public void newTest(String tid,String pid,String t,String result) {
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("INSERT INTO Test (testID, patientID, type, results) VALUES (%s, %s, '%s','%s')", tid, pid, t, result);
			statement.executeUpdate(stmt);
			System.out.println("Successfully added new test for Patient " + pid + "\n");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}
	
	//Assignes a specialized staff to a test
	public void assignStaffToTest(String testID, String staffID) {
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("INSERT INTO run (testID, staffID) VALUES (%s, %s)", testID, staffID);
			statement.executeUpdate(stmt);
			System.out.println("Successfully assigned staff to test\n");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //Create a new Treatment
	public void newTreatment(String pid,String t, String cost, String des) {
		try {
			Statement statement = null;
			statement = connection.createStatement();
			String stmt = String.format("INSERT INTO Treatment (type, patientID, cost, description) VALUES ('%s', %s, %s, '%s')", t, pid, cost, des );
			statement.executeUpdate(stmt);
			System.out.println("New treatment created successfully");
		} catch (SQLException e) {
			System.out.println(e);
		}
	}

  //Create a new medical record
	public void newMedicalRecord(String mid,String pid,String sdate,String edate,String pres,String diag) {
		try {
            // Create statement object
			Statement statement = null;
            // Set auto-commit to false
            connection.setAutoCommit(false);
			statement = connection.createStatement();
            // build our query
			String stmt = String.format("INSERT INTO MedicalRecord(medicalID, patientID, start, end, prescription, diagnosis) VALUES (%s, %s, '%s', '%s', '%s', '%s')",  mid, pid, sdate, edate, pres, diag );
            //execute our update, if it is successful we can commit right after.
            // if an exception is thrown, will be caught in try-catch and then rolled-back
			statement.executeUpdate(stmt);
            connection.commit();
            System.out.println("Transaction Successful");
		} catch (SQLException e) {
            try {
                //Roll back our update if there was an exception
                System.out.println("rolling back");
                connection.rollback();
            } catch (SQLException rollbackErr) {
                System.out.println("Error rolling back");
                System.out.println(rollbackErr);
            }
			System.out.println(e);
        } finally {
            try {
                // Once everything is complete, set autocommit back to true
                connection.setAutoCommit(true);
            } catch (SQLException commitErr) {
                System.out.println(commitErr);
            }
        }
	}

    //Add a billing account
    public void insertBillingAccount(String billID, String visitDate, String patientID, String fees, String payMethod, String amount, String cardNum) {
        try {
            // Create statement object
            Statement statement = null;
            // Set auto-commit to false
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            // build our query
            String stmt = String.format("INSERT INTO Billing (billingID, patientID, visitDate, fees, payMethod, amount, cardNum) VALUES(%s, %s, '%s', %s, '%s', %s, '%s')", billID, patientID, visitDate, fees, payMethod, amount, cardNum);
            //execute our update, if it is successful we can commit right after.
            // if an exception is thrown, will be caught in try-catch and then rolled-back
            statement.executeUpdate(stmt);
            connection.commit();
            System.out.println("Transaction Successful!");
        } catch (SQLException e) {
            try {
                //Roll back our update if there was an exception
                System.out.println("rolling back");
                connection.rollback();
            } catch (SQLException rollbackErr) {
                System.out.println("Error rolling back");
                System.out.println(rollbackErr);
            }
            System.out.println("Error creating billing account");
            System.out.println(e);
        } finally {
            // Once everything is complete, set autocommit back to true
            try {
                connection.setAutoCommit(true);
            } catch (SQLException commitErr) {
                System.out.println(commitErr);
            }
        }
    }
}
