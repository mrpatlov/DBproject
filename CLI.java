import java.util.*;
import java.util.Scanner;

public class CLI {

    private static DB dbInstance = new DB();

    public static void main(String[] args) {
       init();
    }

    // create all of our tables, insert test data, and then begin prompting users for input
    public static void init() {
        dbInstance.dropTables();
        dbInstance.createTables();
        dbInstance.insertTestData();
        System.out.println("Welcome to WakeMed North DB Project.\nType 'help' for list of commands, a command, or 'quit' to quit.\n");
        //Input scanner
        processInput();
    }

    // Here is the main CLI loop, it processes user then passes it to the correct DB method
    public static void processInput() {
        Scanner in = new Scanner(System.in);
		//Now we will start processing input from CLI
		String curr = in.nextLine(); //Gets the next line
		//While the user doesn't quit the application, process input
		while (!curr.equalsIgnoreCase("quit")) {
            // All of the available options to a user
            if (curr.equals("help")) {
                System.out.println("Information Processing:\n"
                		+ "\tadd patient\tEnters a new Patient\n"
                		+ "\tupdate patient\tUpdates a Patient's information\n"
                		+ "\tdelete patient\tDeletes a Patient\n"
                		+ "\tadd staff\tEnters a new Staff member\n"
                		+ "\tupdate staff\tUpdates a Staff member\n"
                		+ "\tdelete staff\tDeletes a Staff member\n"
                		+ "\tadd ward\tAdds a new Ward\n"
                		+ "\tupdate ward\tUpdates a Ward\n"
                		+ "\tdelete ward\tDeletes a Ward\n"
                		+ "\tcheck bed availability\tChecks the availability of a Ward\n"
                		+ "\tassign to ward\tAssignes a Patient to a Ward\n"
                		+ "\treserve bed\tReserves a bed\n"
                		+ "\trelease bed\tReleases a bed\n"
                		+ "Maintain Billing Account:\n"
                		+ "\tadd billing account\tAdds a new Billing Account for a Patient\n"
                		+ "\tgenerate bill\tGenerates a Bill for a certain Patient.\n"
                		+ "\tcheck hospital availability\tChecks the hospital availability.\n"
                		+ "\tdelete billing account\tDeletes a Billing Account\n"
                		+ "\tupdate bill amount\tUpdates the amount of money on a bill.\n"
                		+ "\tdelete bill\tDeletes a bill from Billing.\n"
                		+ "Maintain Medical Records:\n"
                		+ "\tnew check in\tCreates a new check in.\n"
                		+ "\tupdate check in\tUpdates a check in.\n"
                		+ "\tnew treatment\tCreates a new Treatment.\n"
                		+ "\tupdate treatment\tUpdates a Treatment\n"
                		+ "\tnew test\tCreates a new Test\n"
                		+ "\tupdate test\tUpdates a Test\n"
						+ "\tassign test to staff\tAssignes a specialist to a test.\n"
                		+ "\tnew medical record\tCreates a new MedicalRecord\n"
                		+ "\tupdate medical record\tUpdates Medical Record\n"
                		+ "Reporting:\n"
                		+ "\tbasic report\tA basic report on a specific Patient.\n"
                		+ "\tbasic report date\tA basic report for a certain time window.\n"
                		+ "\tward usage report\tA report on the current Ward usage.\n"
                		+ "\tpatients per month report\tA report on the number of Patients per month.\n"
                		+ "\tward percentage report\tA report on the current Ward usage percentage\n"
                		+ "\tpatient for doc report\tA report on the Patient's a Doctor is assigned to.\n"
                		+ "\tstaff report\tA report on all Staff grouped by their role.\n"
                		+ "\tbilling report\tA report on all the billing for a Patient.");
            // Collects all info when the user wants to add a new patient
            } else if (curr.equals("add patient")) {
                String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                String name = "";
                System.out.print("Name: ");
                name = in.nextLine();
                String ssn = "";
                System.out.print("SSN (optional): ");
                ssn = in.nextLine();
                if (ssn.equals(""))
                	ssn = "NULL";
                String age = "";
                System.out.print("Age: ");
                age = in.nextLine();
                String gender = "";
                System.out.print("Gender: ");
                gender = in.nextLine();
                String dob = "";
                System.out.print("Birthdate (YYYY-MM-DD): ");
                dob = in.nextLine();
                String phoneNum = "";
                System.out.print("Phone Number: ");
                phoneNum = in.nextLine();
                String status = "";
                System.out.print("Patient Status: ");
                status = in.nextLine();
                String address = "";
                System.out.print("Address: ");
                address = in.nextLine();
                String pWard = "";
                System.out.print("Preferred Ward (optional): ");
                pWard = in.nextLine();
                if (pWard.equals(""))
                	pWard = "NULL";
                dbInstance.insertPatient(id, name, ssn, age, gender, dob, phoneNum, status, address, pWard);
            // Collects all info if the user wants to add a billing account
            } else if (curr.equals("add billing account")) {
                String billID = "";
                System.out.print("Billing ID: ");
                billID = in.nextLine();
                String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                String fees = "";
                System.out.print("Fees: ");
                fees = in.nextLine();
                String payMethod = "";
                System.out.print("Pay Method: ");
                payMethod = in.nextLine();
                String amount = "";
                System.out.print("Amount: ");
                amount = in.nextLine();
                String cardNum = "";
                System.out.print("Card Number: ");
                cardNum = in.nextLine();
                String visitDate = "";
                System.out.print("Visit Date (YYYY-MM-DD): ");
                visitDate = in.nextLine();
                dbInstance.insertBillingAccount(billID, visitDate, id, fees, payMethod, amount, cardNum);
            //Gets new amount if user wants to update the bill amount
            } else if (curr.equals("update bill amount")) {
                String billID = "";
                System.out.print("Billing ID: ");
                billID = in.nextLine();
				String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                String amount = "";
                System.out.print("Amount: ");
                amount = in.nextLine();
                dbInstance.updateBillingAmount(billID, amount, id);
            //This options prints the current bill for a given patient
            } else if (curr.equals("generate bill")) {
                String patientID = "";
                System.out.print("Patient ID: ");
                patientID = in.nextLine();
                dbInstance.generateBill(patientID);
            // Checks if their are available beds within the hospital
            } else if (curr.equals("check hospital availability")) {
                dbInstance.checkHospitalAvailability();
            //Delete's all billts with a given patientID
            } else if (curr.equals("delete billing account")) {
                String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                dbInstance.deleteBillingAccount(id);
            //Deletes a bill with a specfic billID
            } else if (curr.equals("delete bill")) {
                String billID = "";
                System.out.print("Billing ID: ");
                billID = in.nextLine();
				String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                dbInstance.deleteBill(billID, id);
            //Deletes a patient from the DB, given a patientID
            } else if (curr.equals("delete patient")) {
                String id = "";
                System.out.print("Patient ID: ");
                id = in.nextLine();
                dbInstance.deletePatient(id);
            // Adds a new staff with all of their information
            } else if (curr.equals("add staff")) {
                String id = "";
                System.out.print("Staff ID: ");
                id = in.nextLine();
                String phoneNum = "";
                System.out.print("Phone number: ");
                phoneNum = in.nextLine();
                String address = "";
                System.out.print("Address: ");
                address = in.nextLine();
                String age = "";
                System.out.print("Age: ");
                age = in.nextLine();
                String gender = "";
                System.out.print("Gender: ");
                gender = in.nextLine();
                String jobTitle = "";
                System.out.print("Job title: ");
                jobTitle = in.nextLine();
                String name = "";
                System.out.print("Name: ");
                name = in.nextLine();
                String dept = "";
                System.out.print("Department: ");
                dept = in.nextLine();
                String profTitle = "";
                System.out.print("Professional title: ");
                profTitle = in.nextLine();
                dbInstance.addStaff(id, phoneNum, address, age, gender, jobTitle, name, dept, profTitle);
            // Deletes a staff member from the database
            } else if (curr.equals("delete staff")) {
                String id = "";
                System.out.print("Staff ID: ");
                id = in.nextLine();
                dbInstance.deleteStaff(id);
            //Adds a new ward that patients can be assigned to.  Must have a staffID
            } else if (curr.equals("add ward")) {
                String wardNum = "";
                System.out.print("Ward Number: ");
                wardNum = in.nextLine();
                String staffID = "";
                System.out.print("Staff ID: ");
                staffID = in.nextLine();
                String charge = "";
                System.out.print("Charge: ");
                charge = in.nextLine();
                String currentFill = "";
                System.out.print("Current Fill: ");
                currentFill = in.nextLine();
                String capacity = "";
                System.out.print("Capacity: ");
                capacity = in.nextLine();
                dbInstance.addWard(wardNum, staffID, charge, currentFill, capacity);
            // Updates all of the possible information a patient can have
            } else if (curr.equals("update patient")) {
            	String patientID = "";
            	System.out.print("Patient ID: ");
            	patientID = in.nextLine();
            	boolean exists = dbInstance.patientIDExists(patientID);
            	while (!exists) {
            		System.out.println("patientID doesn't exist. Enter again.");
            		System.out.print("Patient ID: ");
                	patientID = in.nextLine();
                	exists = dbInstance.patientIDExists(patientID);
            	}
                String name = "";
                System.out.print("Name: ");
                name = in.nextLine();
                String ssn = "";
                System.out.print("SSN: ");
                ssn = in.nextLine();
                String age = "";
                System.out.print("Age: ");
                age = in.nextLine();
                String gender = "";
                System.out.print("Gender: ");
                gender = in.nextLine();
                String dob = "";
                System.out.print("Birthdate (YYYY-MM-DD): ");
                dob = in.nextLine();
                String phoneNum = "";
                System.out.print("Phone Number: ");
                phoneNum = in.nextLine();
                String status = "";
                System.out.print("Patient Status: ");
                status = in.nextLine();
                String address = "";
                System.out.print("Address: ");
                address = in.nextLine();
                String pWard = "";
                System.out.print("Preferred Ward: ");
                pWard = in.nextLine();
                dbInstance.updatePatient(patientID, name, ssn, age, gender, dob, phoneNum, status, address, pWard);
            // Gives the user an option to update any of the staff's information
            } else if (curr.equals("update staff")) {
            	String staffID = "";
            	System.out.print("Staff ID: ");
            	staffID = in.nextLine();
            	boolean exists = dbInstance.staffIDExists(staffID);
            	while (!exists) {
            		System.out.println("staffID doesn't exist. Enter again.");
            		System.out.print("Staff ID: ");
                	staffID = in.nextLine();
                	exists = dbInstance.staffIDExists(staffID);
            	}
              String phoneNum = "";
              System.out.print("Phone number: ");
              phoneNum = in.nextLine();
              String address = "";
              System.out.print("Address: ");
              address = in.nextLine();
              String age = "";
              System.out.print("Age: ");
              age = in.nextLine();
              String gender = "";
              System.out.print("Gender: ");
              gender = in.nextLine();
              String jobTitle = "";
              System.out.print("Job title: ");
              jobTitle = in.nextLine();
              String name = "";
              System.out.print("Name: ");
              name = in.nextLine();
              String dept = "";
              System.out.print("Department: ");
              dept = in.nextLine();
              String profTitle = "";
              System.out.print("Professional title: ");
              profTitle = in.nextLine();
                dbInstance.updateStaff(staffID, phoneNum, address, age, gender, jobTitle, name, dept, profTitle);
            // Updates a ward information (i.e. updating current fill)
            } else if (curr.equals("update ward")) {
            	String wardNum = "";
            	System.out.print("Ward Number: ");
            	wardNum = in.nextLine();
              String staffID = "";
              System.out.print("Staff ID: ");
              staffID = in.nextLine();
            	boolean exists = dbInstance.wardExists(wardNum, staffID);
            	while (!exists) {
            		System.out.println("Ward does not exist. Enter again.");
                System.out.print("Ward Number: ");
              	wardNum = in.nextLine();
            		System.out.print("Staff ID: ");
                	staffID = in.nextLine();
                	exists = dbInstance.wardExists(wardNum, staffID);
            	}
                String charge = "";
                System.out.print("Charge: ");
                charge = in.nextLine();
                String currentFill = "";
                System.out.print("Current Fill: ");
                currentFill = in.nextLine();
                String capacity = "";
                System.out.print("Capacity: ");
                capacity = in.nextLine();
                dbInstance.updateWard(wardNum, staffID, charge, currentFill, capacity);
            //Can delete a ward from the hospital
            } else if (curr.equals("delete ward")) {
                String wardNum = "";
                System.out.print("Ward Number: ");
                wardNum = in.nextLine();
                String staffID = "";
                System.out.print("Staff ID: ");
                staffID = in.nextLine();
                dbInstance.deleteWard(wardNum, staffID);
            // Can check if a bed is available in a specific ward
            } else if (curr.equals("check bed availability")) {
                String wardNum = "";
                System.out.print("Ward Number: ");
                wardNum = in.nextLine();
                dbInstance.checkBed(wardNum);
            //Assigns a patient to a ward
            } else if (curr.equals("assign to ward")) {
                String patientID = "";
                System.out.print("Patient ID: ");
                patientID = in.nextLine();
                String staffID = "";
                System.out.print("Staff ID: ");
                staffID = in.nextLine();
                String start = "";
                System.out.print("Start Date (YYYY-MM-DD): ");
                start = in.nextLine();
                String bed = "";
                System.out.print("Bed: ");
                bed = in.nextLine();
                dbInstance.assignToWard(patientID, staffID, start, bed);
            // Reserves a specfic bed for a patient
            } else if (curr.equals("reserve bed")) {
                String patientID = "";
                System.out.print("Patient ID: ");
                patientID = in.nextLine();
                String sid = "";
				System.out.print("Staff ID: ");
				sid = in.nextLine();
                String bed = "";
                System.out.print("Bed: ");
                bed = in.nextLine();
                String wardID = "";
                System.out.print("Ward: ");
                wardID = in.nextLine();
                dbInstance.reserveBed(patientID, bed, sid, wardID);
            // Releases a bed so other patients can have it
            } else if (curr.equals("release bed")) {
                String patientID = "";
                System.out.print("Patient ID: ");
                patientID = in.nextLine();
                String sid = "";
				System.out.print("Staff ID: ");
				sid = in.nextLine();
				String wardID = "";
                System.out.print("Ward: ");
                wardID = in.nextLine();
                String end = "";
                System.out.print("End Date (YYYY-MM-DD): ");
                end = in.nextLine();
                dbInstance.releaseBed(patientID, end, sid, wardID);
            // Processes a new patient being checked in
			} else if (curr.equals("new check in")) {
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				String sid = "";
				System.out.print("Staff ID: ");
				sid = in.nextLine();
				String bnum = "";
				System.out.print("Bed Number: ");
				bnum = in.nextLine();
				String sdate = "";
				System.out.print("Start date (YYYY-MM-DD): ");
				sdate = in.nextLine();
				String edate = "";
				System.out.print("End date (YYYY-MM-DD, optional): ");
				edate = in.nextLine();
				if(edate.equals(""))
					edate = "NULL";
				dbInstance.newCheckIn(pid, sid, sdate, edate, bnum);
            // Updates any information about a previous check in
			} else if (curr.equals("update check in")) {
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				boolean exists = dbInstance.patientIDExists(pid);
				while (!exists) {
					System.out.println("Patient ID doesn't exist. Enter again.");
					System.out.print("Patient ID: ");
					pid = in.nextLine();
					exists = dbInstance.patientIDExists(pid);
				}
				String sid = "";
				System.out.print("Staff ID: ");
				sid = in.nextLine();
				exists = dbInstance.staffIDExists(sid);
				while (!exists) {
					System.out.println("Staff ID doesn't exist. Enter again.");
					System.out.print("Staff ID: ");
					sid = in.nextLine();
					exists = dbInstance.staffIDExists(sid);
				}
				String bnum = "";
				System.out.print("Bed Number: ");
				bnum = in.nextLine();
				String sdate = "";
				System.out.print("Start date (YYYY-MM-DD): ");
				sdate = in.nextLine();
				String edate = "";
				System.out.print("End date (YYYY-MM-DD): ");
				edate = in.nextLine();
				dbInstance.updateCheckIn(pid, sid, sdate, edate, bnum);
            //Adds a new test to the DB
			} else if(curr.equals("new test")) {
				String tid = "";
				System.out.print("Test ID: ");
				tid = in.nextLine();
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				String t = "";
				System.out.print("Type: ");
				t = in.nextLine();
				String result = "";
				System.out.print("Results: ");
				result = in.nextLine();
				dbInstance.newTest(tid, pid, t, result);
            // Updates information about an existing test
			} else if(curr.equals("update test")) {
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				boolean exists = dbInstance.patientIDExists(pid);
				while (!exists) {
					System.out.println("Patient ID doesn't exist. Enter again.");
					System.out.print("Patient ID: ");
					pid = in.nextLine();
					exists = dbInstance.patientIDExists(pid);
				}
				String tid = "";
				System.out.print("Test ID: ");
				tid = in.nextLine();
				exists = dbInstance.testIDExists(tid, pid);
				while (!exists) {
					System.out.println("Test ID for patient doesn't exist. Enter again.");
					System.out.print("Test ID: ");
					tid = in.nextLine();
					exists = dbInstance.testIDExists(tid, pid);
				}
				String t = "";
				System.out.print("Type: ");
				t = in.nextLine();
				String result = "";
				System.out.print("Results: ");
				result = in.nextLine();
				dbInstance.updateTest(tid, pid, t, result);
            // Adds a new treatment for Staff to utilize
			} else if(curr.equals("new treatment")) {
				String t = "";
				System.out.print("Type (primary key): ");
				t = in.nextLine();
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				String cost = "";
				System.out.print("Cost: ");
				cost = in.nextLine();
				String des = "";
				System.out.print("Description: ");
				des = in.nextLine();
				dbInstance.newTreatment(pid, t, cost, des);
            // Updates information about existing treatments
			} else if(curr.equals("update treatment")) {
				String t = "";
				System.out.print("Type (primary key): ");
				t = in.nextLine();
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				boolean exists = dbInstance.patientIDExists(pid);
				while (!exists) {
					System.out.println("Patient ID doesn't exist. Enter again.");
					System.out.print("Patient ID: ");
					pid = in.nextLine();
					exists = dbInstance.patientIDExists(pid);
				}
				String cost = "";
				System.out.print("Cost: ");
				cost = in.nextLine();
				String des = "";
				System.out.print("Description: ");
				des = in.nextLine();
				dbInstance.updateTreatment(pid, t, cost, des);
            // Inserts a new medical record into the DB
			} else if (curr.equals("new medical record")) {
				String mid = "";
				System.out.print("Medical Record ID: ");
				mid = in.nextLine();
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				String sdate = "";
				System.out.print("Start date (YYYY-MM-DD): ");
				sdate = in.nextLine();
				String edate = "";
				System.out.print("End date (YYYY-MM-DD): ");
				edate = in.nextLine();
				if(edate.equals(""))
					edate = "null";
				String pres = "";
				System.out.print("Prescription: ");
				pres = in.nextLine();
				String diag = "";
				System.out.print("Diagnosis Details: ");
				diag = in.nextLine();
				dbInstance.newMedicalRecord(mid, pid, sdate, edate, pres, diag);
            // Updates any information about an existing medical record
			} else if (curr.equals("update medical record")) {
				String pid = "";
				System.out.print("Patient ID: ");
				pid = in.nextLine();
				boolean exists = dbInstance.patientIDExists(pid);
				while (!exists) {
					System.out.println("Patient ID doesn't exist. Enter again.");
					System.out.print("Patient ID: ");
					pid = in.nextLine();
					exists = dbInstance.patientIDExists(pid);
				}
				String mid = "";
				System.out.print("Medical Record ID: ");
				mid = in.nextLine();
				exists = dbInstance.medicalRecordExists(mid);
				while (!exists) {
					System.out.println("Medical Record ID doesn't exist for patient. Enter again.");
					System.out.print("Medical Record ID: ");
					mid = in.nextLine();
					exists = dbInstance.medicalRecordExists(mid);
				}
				String sdate = "";
				System.out.print("Start date (YYYY-MM-DD): ");
				sdate = in.nextLine();
				String edate = "";
				System.out.print("End date (YYYY-MM-DD): ");
				edate = in.nextLine();
				String pres = "";
				System.out.print("Prescription: ");
				pres = in.nextLine();
				String diag = "";
				System.out.print("Diagnosis Details: ");
				diag = in.nextLine();
				dbInstance.updateMedicalRecord(mid, pid, sdate, edate, pres, diag);
            // Generates a basic patient report
			} else if (curr.equals("basic report")) {
            	String patientID = "";
            	System.out.print("Patient ID: ");
            	patientID = in.nextLine();
            	boolean exists = dbInstance.patientIDExists(patientID);
            	while (!exists) {
            		System.out.println("patientID doesn't exist. Enter again.");
            		System.out.print("Patient ID: ");
                	patientID = in.nextLine();
                	exists = dbInstance.patientIDExists(patientID);
            	}
            	dbInstance.basicReportNoDate(patientID);
            // Generates a report for a patient within a given date range
            } else if (curr.equals("basic report date")) {
            	String patientID = "";
            	System.out.print("Patient ID: ");
            	patientID = in.nextLine();
            	boolean exists = dbInstance.patientIDExists(patientID);
            	while (!exists) {
            		System.out.println("patientID doesn't exist. Enter again.");
            		System.out.print("Patient ID: ");
                	patientID = in.nextLine();
                	exists = dbInstance.patientIDExists(patientID);
            	}
            	String startDate = "";
            	System.out.print("Start Date (YYYY-MM-DD): ");
            	startDate = in.nextLine();
            	String endDate = "";
            	System.out.print("End Date (YYYY-MM-DD): ");
            	endDate = in.nextLine();
            	dbInstance.basicReportDate(patientID, startDate, endDate);
            // Tells how many patients are currently in a ward
            } else if (curr.equals("ward usage report")) {
            	dbInstance.wardReport();
            // Retrieves how many patients visited the hospital in the last month
            } else if (curr.equals("patients per month report")) {
            	dbInstance.patientsPerMonth();
            // Calculates the percentage filled in a ward
            } else if (curr.equals("ward percentage report")) {
            	dbInstance.wardPercent();
            // Generates a report for all patients for a given Doctor
            } else if (curr.equals("patient for doc report")) {
            	String docID = "";
            	System.out.print("Staff ID: ");
            	docID = in.nextLine();
            	boolean exists = dbInstance.staffIDExists(docID);
            	while (!exists) {
            		System.out.println("staffID doesn't exist. Enter again.");
            		System.out.print("Staff ID: ");
                	docID = in.nextLine();
                	exists = dbInstance.staffIDExists(docID);
            	}
            	dbInstance.reportDocPatient(docID);
            // Produces a report about all staff in the hospital
            } else if (curr.equals("staff report")) {
            	dbInstance.reportStaff();
            // Creates a bill for the given patient
            } else if (curr.equals("billing report")) {
            	String patientID = "";
            	System.out.print("Patient ID: ");
            	patientID = in.nextLine();
            	boolean exists = dbInstance.patientIDExists(patientID);
            	while (!exists) {
            		System.out.println("patientID doesn't exist. Enter again.");
            		System.out.print("Patient ID: ");
                	patientID = in.nextLine();
                	exists = dbInstance.patientIDExists(patientID);
            	}
            	dbInstance.reportBilling(patientID);
			//you can assign a specialist to run the test
            } else if (curr.equals("assign test to staff")) {
				String testID = "";
				System.out.print("Test ID: ");
				testID = in.nextLine();
				String docID = "";
            	System.out.print("Staff ID: ");
            	docID = in.nextLine();
            	boolean exists = dbInstance.staffIDExists(docID);
            	while (!exists) {
            		System.out.println("staffID doesn't exist. Enter again.");
            		System.out.print("Staff ID: ");
                	docID = in.nextLine();
                	exists = dbInstance.staffIDExists(docID);
            	}
				dbInstance.assignStaffToTest(testID, docID);
			// If the command has not been recognized, re-prompt user
            } else {
                System.out.println("Input not recognized. Type 'help' for help");
            }
			System.out.println("Enter a command below.\n");
			curr = in.nextLine();
		}
        // Closes input buffer
		in.close();
    }
}
