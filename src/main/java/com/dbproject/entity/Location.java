package com.dbproject.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.text.Style;
import java.math.BigDecimal;

@Entity
@Table(name = "location")
@Getter
@Setter
public class Location {

    @Id
    @Column(name = "attractionId")
    private String attractionId;
    @Column(name = "AttractionName")
    private String AttractionName;
    @Column(name = "Description")
    private String Description;
    @Column(name = "PositionLat")
    private BigDecimal PositionLat;
    @Column(name = "PositionLon")
    private BigDecimal PositionLon;
    @Column(name = "Geometry")
    private String Geometry;
    private Integer AttractionClasses0;
    private Integer AttractionClasses1;
    private String PostalAddressCity;
    private Integer PostalAddressCityCode;
    private String PostalAddressTown;
    private Integer PostalAddressTownCode;
    private Integer PostalAddressZipCode;
    private String PostalAddressStreetAddress;
    private String TelephonesTel;
    private String OrganizationsName;
    private String OrganizationsClass;
    private String OrganizationsTaxCode;
    private String OrganizationsAgencyCode;
    private String OrganizationsUrl;
    private String OrganizationsTelephones;
    private String OrganizationsMobilePhones;
    private String OrganizationsFaxes;
    private String OrganizationsEmail;
    private String ServiceTimeInfo;
    private String TrafficInfo;
    private String ParkingInfo;
    private boolean ServiceStatus;
    private boolean IsPublicAccess;
    private boolean IsAccessibleForFree;
    private String FeeInfo;
    private String WebsiteUrl;
    private String VisitDuration;
    private Integer AssetsClass;
    private String PartOfAttraction;
    private String Remarks;
    private String UpdateTime;
    private Integer TelephonesExt;
    private Integer AttractionClasses2;
    private String TelephonesTel2;
    private String ImagesName;
    private String ImagesDescription;
    private String ImagesUrl;
    private String ImagesWidth;
    private String ImagesHeight;
    private String ImagesName2;
    private String ImagesDescription2;
    private String ImagesUrl2;
    private String ImagesWidth2;
    private String ImagesHeight2;
    private String ImagesName3;
    private String ImagesDescription3;
    private String ImagesUrl3;
    private String ImagesWidth3;
    private String ImagesHeight3;
    private String MapUrls;


    @Override
    public String toString() {
        return "Location{" +
                "attractionId='" + attractionId + '\'' + '\n'+
                ", AttractionName='" + AttractionName + '\'' +'\n'+
                ", Description='" + Description + '\'' +'\n'+
                ", PositionLat=" + PositionLat +'\n'+
                ", PositionLon=" + PositionLon +'\n'+
                ", Geometry='" + Geometry + '\'' +'\n'+
                ", AttractionClasses0=" + AttractionClasses0 +'\n'+
                ", AttractionClasses1=" + AttractionClasses1 +'\n'+
                ", PostalAddressCity='" + PostalAddressCity + '\'' +'\n'+
                ", PostalAddressCityCode=" + PostalAddressCityCode +'\n'+
                ", PostalAddressTown='" + PostalAddressTown + '\'' +'\n'+
                ", PostalAddressTownCode=" + PostalAddressTownCode +'\n'+
                ", PostalAddressZipCode=" + PostalAddressZipCode +'\n'+
                ", PostalAddressStreetAddress='" + PostalAddressStreetAddress + '\'' +'\n'+
                ", TelephonesTel='" + TelephonesTel + '\'' +'\n'+
                ", OrganizationsName='" + OrganizationsName + '\'' +'\n'+
                ", OrganizationsClass='" + OrganizationsClass + '\'' +'\n'+
                ", OrganizationsTaxCode='" + OrganizationsTaxCode + '\'' +'\n'+
                ", OrganizationsAgencyCode='" + OrganizationsAgencyCode + '\'' +'\n'+
                ", OrganizationsURL='" + OrganizationsUrl + '\'' +'\n'+
                ", OrganizationsTelephones='" + OrganizationsTelephones + '\'' +'\n'+
                ", OrganizationsMobilePhones='" + OrganizationsMobilePhones + '\'' +'\n'+
                ", OrganizationsFaxes='" + OrganizationsFaxes + '\'' +'\n'+
                ", OrganizationsEmail='" + OrganizationsEmail + '\'' +'\n'+
                ", ServiceTimeInfo='" + ServiceTimeInfo + '\'' +'\n'+
                ", TrafficInfo='" + TrafficInfo + '\'' +'\n'+
                ", ParkingInfo='" + ParkingInfo + '\'' +'\n'+
                ", ServiceStatus=" + ServiceStatus +'\n'+
                ", IsPublicAccess=" + IsPublicAccess +'\n'+
                ", IsAccessibleForFree=" + IsAccessibleForFree +'\n'+
                ", FeeInfo='" + FeeInfo + '\'' +'\n'+
                ", WebsiteURL='" + WebsiteUrl + '\'' +'\n'+
                ", VisitDuration='" + VisitDuration + '\'' +'\n'+
                ", AssetsClass=" + AssetsClass +'\n'+
                ", PartOfAttraction='" + PartOfAttraction + '\'' +'\n'+
                ", Remarks='" + Remarks + '\'' +'\n'+
                ", UpdateTime='" + UpdateTime + '\'' +'\n'+
                ", TelephonesExt=" + TelephonesExt +'\n'+
                ", AttractionClasses2=" + AttractionClasses2 +'\n'+
                ", TelephonesTel2='" + TelephonesTel2 + '\'' +'\n'+
                ", ImagesName='" + ImagesName + '\'' +'\n'+
                ", ImagesDescription='" + ImagesDescription + '\'' +'\n'+
                ", ImagesURL='" + ImagesUrl + '\'' +'\n'+
                ", ImagesWidth='" + ImagesWidth + '\'' +'\n'+
                ", ImagesHeight='" + ImagesHeight + '\'' +'\n'+
                ", ImagesName2='" + ImagesName2 + '\'' +'\n'+
                ", ImagesDescription2='" + ImagesDescription2 + '\'' +'\n'+
                ", ImagesURL2='" + ImagesUrl2 + '\'' +'\n'+
                ", ImagesWidth2='" + ImagesWidth2 + '\'' +'\n'+
                ", ImagesHeight2='" + ImagesHeight2 + '\'' +'\n'+
                ", ImagesName3='" + ImagesName3 + '\'' +'\n'+
                ", ImagesDescription3='" + ImagesDescription3 + '\'' +'\n'+
                ", ImagesURL3='" + ImagesUrl3 + '\'' +'\n'+
                ", ImagesWidth3='" + ImagesWidth3 + '\'' +'\n'+
                ", ImagesHeight3='" + ImagesHeight3 + '\'' +'\n'+
                ", MapURLs='" + MapUrls + '\'' +'\n'+
                '}';
    }
}




















