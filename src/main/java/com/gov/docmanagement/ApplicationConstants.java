package com.gov.docmanagement;

public class ApplicationConstants {

    //roles
    public static final String HOD= "hod";
    public static final String Head_Of_Department= "Head Of Department";
    public static final String Account_Officer= "Account Officer";
    public static final String AO= "ao";
    public static final String CLERK_ELECTRIC= "clerk_electric";
    public static final String CLERK_ADMINISTRATOR= "clerk_administrator";
    public static final String CLERK_FINANCE= "clerk_finance";
    public static final String CLERK_WATER= "clerk_water";
    public static final String CLERK_SOLID_WASTE= "clerk_solidwaste";
    public static final String CLERK_AUDIT= "clerk_audit";
    public static final String CLERK_CIVIL= "clerk_civil";
    //All Descriptions
    public static final String CLERK_ELECTRIC_DESC= "Clerk Of Electric Department";
    public static final String CLERK_ADMINISTRATOR_DESC= "Clerk Of Administrator Department";
    public static final String CLERK_FINANCE_DESC= "Clerk Of Finance Department";
    public static final String CLERK_WATER_DESC= "Clerk Of Water Department";
    public static final String CLERK_SOLID_WASTE_DESC= "Clerk Of Solid Waste Department";
    public static final String CLERK_AUDIT_DESC= "Clerk Of Audit Department";
    public static final String CLERK_CIVIL_DESC= "Clerk Of Civil Department";

    //portal roles
    public static final String SUPERADMIN= "SUPERADMIN";
    public static final String ADMIN= "ADMIN";
    public static final String SUPERADMIN_DESC= "HOD is SUPERADMIN of application";
    public static final String ADMIN_DESC= "AO is ADMIN of Application";
    public static final String OFFICE_USER= "office_user";
    public static final String OFFICE_USER_DESC= "All Clerks are Office User";

    public static final String CITIZEN= "citizen";
    public static final String CITIZEN_USER= "Citizen (Out of Office users)";
    //Depts
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String FINANCE = "FINANCE";
    public static final String WATER = "WATER";
    public static final String ELECTRIC = "ELECTRIC";
    public static final String SOLID_WASTE = "SOLID WASTE";
    public static final String AUDIT = "AUDIT";
    public static final String CIVIL = "CIVIL";

    //tags
    public static final String GREEN = "GREEN";
    public static final String YELLOW = "YELLOW";
    public static final String WHITE = "WHITE";
    public static final String RED = "RED";


    //category
    public static final String CATEGORY_A = "A";
    public static final String CATEGORY_B = "B";
    public static final String CATEGORY_C = "C";
    public static final String CATEGORY_D = "D";

    //document stages
    public static final String Uploaded = "Uploaded";
    public static final String WaitingForClerkApproval = "WaitingForClerkApproval";
    public static final String ApprovedByClerk = "ApprovedByClerk";
    public static final String WaitingForSrClerkApproval = "WaitingForSrClerkApproval";
    public static final String ApprovedBySrClerk = "ApprovedBySrClerk";
    public static final String WaitingForAccountOfficer = "WaitingForAccountOfficer";
    public static final String ApprovedByAccountOfficer = "ApprovedByAccountOfficer";
    public static final String WaitingForHOD = "WaitingForHOD";
    public static final String ApprovedByHOD = "ApprovedByHOD";

}
