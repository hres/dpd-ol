/*
 * Created on 11-Aug-06
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ca.gc.hc.model;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.Hibernate;

	
	
/**
 * @author dbeauvai
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Label implements Serializable {
	
	/** identifier field */
	private Long drugCode;
	byte[] label;
//	//private byte[] drugProductLabel;
//	private Blob drugProductLabel = null;
//	
	private Blob labelBlob = null;
	//name of pdf file	
	private String labelFileName = "";
	
	//size of pdf file
	private long labelFileSize;
	
		
		
	/**
	 * @return
	 */
	public Long getDrugCode() {
		return drugCode;
	}

	/**
	 * @param long1
	 */
	public void setDrugCode(Long long1) {
		drugCode = long1;
	}


//	public Blob getDrugProductLabel()
//	{
//		return drugProductLabel ;
//		
//		//(oracle.sql.BLOB) ((org.hibernate.lob.SerializableBlob) drugProductLabel).getWrappedBlob();
//	}
//
//
//	public void setDrugProductLabel( Blob drugProductLabel )
//	{
//		this.drugProductLabel = drugProductLabel;
//	}
//
//
//	public InputStream getDrugProductLabelStream()
//			throws SQLException
//	{
//		if (getDrugProductLabel() == null)
//			return null;
//
//		return getDrugProductLabel().getBinaryStream();
//	}
//
//
//	public void setDrugProductLabelContent( InputStream sourceStream )
//			throws IOException
//	{
//		//drugProductLabel = (oracle.sql.BLOB) ( (org.hibernate.lob.SerializableBlob) sourceStream ).getWrappedBlob();
//		setDrugProductLabel( Hibernate.createBlob( sourceStream ) );
//		
//	}

	/**
	 * @return
	 */
	public String getLabelFileName() {
		return labelFileName;
	}

	/**
	 * @return
	 */
	public long getLabelFileSize() {
		return labelFileSize;
	}

	/**
	 * @param string
	 */
	public void setLabelFileName(String string) {
		labelFileName = string;
	}

	/**
	 * @param long1
	 */
	public void setLabelFileSize(long long1) {
		labelFileSize = long1;
	}



	

	 public byte[] getLabel() {
	  return label;
	 }

	 public void setLabel(byte[] label) {
	  this.label = label;
	 }

	 /** Don't invoke this.  Used by Hibernate only. */
	 public void setLabelBlob(Blob labelBlob) throws Exception {
	  this.label = this.toByteArray(labelBlob);
	 }

	 /** Don't invoke this.  Used by Hibernate only. */
	 public Blob getLabelBlob() {
	  return Hibernate.createBlob(this.label);
	 }

	private byte[] toByteArray(Blob fromBlob)   throws SQLException, IOException{
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 try {
		  return toByteArrayImpl(fromBlob, baos);
		 } catch (SQLException e) {
			return null;
		 } catch (IOException e) {
			return null;
		 } finally {
		  if (baos != null) {
		   try {
			baos.close();
		   } catch (IOException ex) {
		   }
		  }
		 }
		}

	 private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
	  throws SQLException, IOException {
	  byte[] buf = new byte[4000];
	  InputStream is = fromBlob.getBinaryStream();
	  try {
	   for (;;) {
		int dataSize = is.read(buf);

		if (dataSize == -1)
		 break;
		baos.write(buf, 0, dataSize);
	   }
	  } finally {
	   if (is != null) {
		try {
		 is.close();
		} catch (IOException ex) {
		}
	   }
	  }
	  return baos.toByteArray();
	 }
	}


