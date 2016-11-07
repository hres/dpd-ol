
package ca.gc.hc.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
/** @author Hibernate CodeGenerator */
public class Packaging implements Serializable {

  /** identifier field */
  private Long packagingId;
 
  /** identifier field */
  private Long drugCode;

  /** nullable persistent field */
  private String upc;

  /** nullable persistent field */
  private String packageSize;

  /** nullable persistent field */
  private String packageSizeUnit;

  /** nullable persistent field */
  private String packageType;

  /** nullable persistent field */
  private String productInformation;

  /** full constructor */
  public Packaging(
	java.lang.Long packagingId,
	java.lang.Long drugCode,
    java.lang.String upc,
    java.lang.String packageSize,
    java.lang.String packageSizeUnit,
    java.lang.String packageType,
    java.lang.String productInformation)
  {
  	this.drugCode = drugCode;
    this.upc = upc;
    this.packageSize = packageSize;
    this.packageSizeUnit = packageSizeUnit;
    this.packageType = packageType;
    this.productInformation = productInformation;
  }

  /** default constructor */
  public Packaging()
  {
  }

  public java.lang.Long getDrugCode()
  {
    return this.drugCode;
  }

  public void setDrugCode(java.lang.Long drugCode)
  {
    this.drugCode = drugCode;
  }

  public java.lang.String getUpc()
  {
    return this.upc;
  }

  public void setUpc(java.lang.String upc)
  {
    this.upc = upc;
  }

  public java.lang.String getPackageSize()
  {
    return this.packageSize;
  }

  public void setPackageSize(java.lang.String packageSize)
  {
    this.packageSize = packageSize;
  }

  public java.lang.String getPackageSizeUnit()
  {
    return this.packageSizeUnit;
  }

  public void setPackageSizeUnit(java.lang.String packageSizeUnit)
  {
    this.packageSizeUnit = packageSizeUnit;
  }

  public java.lang.String getPackageType()
  {
    return this.packageType;
  }

  public void setPackageType(java.lang.String packageType)
  {
    this.packageType = packageType;
  }

  public java.lang.String getProductInformation()
  {
    return this.productInformation;
  }

  public void setProductInformation(java.lang.String productInformation)
  {
    this.productInformation = productInformation;
  }
  public String toString()
  {
	return new ToStringBuilder(this)
	  .append("drugCode", getDrugCode())
	  .append("upc", getUpc())
	  .append("packageSize", getPackageSize())
	  .append("packageSizeUnit", getPackageSizeUnit())
	  .append("PackageType", getPackageType())
	  .append("packageInformation", getProductInformation())
	  .toString();
  }

 


/**
 * @return
 */
public Long getPackagingId() {
	return packagingId;
}

/**
 * @param long1
 */
public void setPackagingId(Long long1) {
	packagingId = long1;
}

}
