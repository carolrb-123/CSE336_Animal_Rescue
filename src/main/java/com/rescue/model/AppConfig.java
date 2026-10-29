package com.rescue.model;

public class AppConfig {
    private static AppConfig instance;
    private String orgName = "Animal Rescue";
    private String contactEmail = "info@rescue.local";

    private AppConfig(){

    }
    public static AppConfig getInstance(){
        if (instance==null)
        {
            instance= new AppConfig();
        }
        return instance;
    }
    public String getOrgName()
    {
        return orgName;
    }
    public String getContactEmail()
    {
        return contactEmail;
    }
    public void setOrgName(String orgName)
    {
        this.orgName=orgName;
    }
    public void setContactEmail(String contactEmail){
        this.contactEmail=contactEmail;
    }
}
