package com.example.sparePartsReport;

import java.awt.Checkbox;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.ReportViewer;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class ReceiveSummarySP extends Window {

	private AbsoluteLayout mainLayout;
	private ComboBox  cmbCategoryName;
	private Label lblCategoryName;
	private Label lblProductName;
	private ComboBox  cmbproductName;
	private Label lblsubcategoryName;
	private ComboBox cmbsubcategoryName;
	private CheckBox chkAllsubcategory;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	SessionBean sessionBean;
	private CheckBox chkAllName;
	private CheckBox chkAllCategory;
	private Label lblCategoryType=new Label();
	private ComboBox cmbcategorytype=new ComboBox();
	//private CheckBox chkallCategoryType=new CheckBox();
	
	private PopupDateField pdffromdate, pdftodate;
	private Label lblasOnDate;
	
	private CheckBox chkpdf,chkother;
	boolean type=false;

	public  ReceiveSummarySP(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("RECEIVE SUMMARY::  "+ sessionBean.getCompany());
		cmbCategoryName.focus();
		Component ob[]={cmbCategoryName,cmbproductName,cmbsubcategoryName,previewButton};
		new FocusMoveByEnter(this, ob);
		categoryType();
		cmbcategorytype.setValue("Spare Parts");
		cmbcategorytype.setEnabled(false);
		allButtonAction();
		categoryData();
		//cmbCategoryName.setEnabled(false);
		//chkAllCategory.setEnabled(false);
		
		cmbsubcategoryName.setEnabled(false);
		chkAllsubcategory.setEnabled(false);

		cmbproductName.setEnabled(false);
		chkAllName.setEnabled(false);
	}

	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("520px");
		mainLayout.setHeight("290px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("560px");
		//setHeight("30px");
		
		lblCategoryType = new Label();
		lblCategoryType.setImmediate(false);
		lblCategoryType.setWidth("-1px");
		lblCategoryType.setHeight("-1px");
		lblCategoryType.setValue("Parent Type :");
		mainLayout.addComponent(lblCategoryType, "top:40.0px;left:45.0px;");

		// cmbSection
		cmbcategorytype = new ComboBox();
		cmbcategorytype.setImmediate(true);
		cmbcategorytype.setWidth("260px");
		cmbcategorytype.setHeight("24px");
		cmbcategorytype.setNullSelectionAllowed(false);
		cmbcategorytype.setNewItemsAllowed(false);
		mainLayout.addComponent( cmbcategorytype, "top:38.0px;left:185.0px;");
		cmbcategorytype.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
				
/*		chkallCategoryType = new CheckBox("");
		chkallCategoryType.setCaption("All");
		chkallCategoryType.setWidth("-1px");
		chkallCategoryType.setHeight("24px");
		chkallCategoryType.setImmediate(true);
		//mainLayout.addComponent(chkallCategoryType, "top:38.0px;left:445.0px;");
		chkallCategoryType.setVisible(false);
		*/
		// lblSection
		lblCategoryName = new Label();
		lblCategoryName.setImmediate(false);
		lblCategoryName.setWidth("-1px");
		lblCategoryName.setHeight("-1px");
		lblCategoryName.setValue("Category  Name :");
		mainLayout.addComponent(lblCategoryName, "top:66.0px;left:45.0px;");

		// cmbSection
		cmbCategoryName = new ComboBox();
		cmbCategoryName.setImmediate(true);
		cmbCategoryName.setWidth("260px");
		cmbCategoryName.setHeight("24px");
		cmbCategoryName.setNullSelectionAllowed(false);
		cmbCategoryName.setNewItemsAllowed(false);
		cmbCategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbCategoryName, "top:64.0px;left:185.0px;");
				
		chkAllCategory = new CheckBox("");
		chkAllCategory.setCaption("All");
		chkAllCategory.setWidth("-1px");
		chkAllCategory.setHeight("24px");
		chkAllCategory.setImmediate(true);
		mainLayout.addComponent(chkAllCategory, "top:64.0px;left:445.0px;");
		
		lblsubcategoryName = new Label();
		lblsubcategoryName.setImmediate(false);
		lblsubcategoryName.setWidth("-1px");
		lblsubcategoryName.setHeight("-1px");
		lblsubcategoryName.setValue("Sub-Category Name :");
		mainLayout.addComponent(lblsubcategoryName, "top:92.0px;left:45.0px;");

		cmbsubcategoryName = new ComboBox();
		cmbsubcategoryName.setWidth("260px");
		cmbsubcategoryName.setHeight("24px");
		cmbsubcategoryName.setImmediate(true);
		cmbsubcategoryName.setNewItemsAllowed(false);
		cmbsubcategoryName.setNullSelectionAllowed(false);
		cmbsubcategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbsubcategoryName, "top:90.0px;left:185.0px;");
				
		chkAllsubcategory=new CheckBox("");
		chkAllsubcategory.setCaption("All");
		chkAllsubcategory.setImmediate(true);
		chkAllsubcategory.setWidth("-1px");
		chkAllsubcategory.setHeight("24px");
		mainLayout.addComponent(chkAllsubcategory, "top:90.0px;left:445.0px;");
		
		lblProductName = new Label();
		lblProductName.setImmediate(false);
		lblProductName.setWidth("-1px");
		lblProductName.setHeight("-1px");
		lblProductName.setValue("Product  Name :");
		mainLayout.addComponent(lblProductName, "top:118.0px;left:45.0px;");

		cmbproductName = new ComboBox();
		cmbproductName.setImmediate(true);
		cmbproductName.setWidth("260px");
		cmbproductName.setHeight("24px");
		cmbproductName.setNullSelectionAllowed(false);
		cmbproductName.setNewItemsAllowed(false);
		cmbproductName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbproductName, "top:116.0px;left:185.0px;");

		chkAllName = new CheckBox("");
		chkAllName.setCaption("All");
		chkAllName.setWidth("-1px");
		chkAllName.setHeight("24px");
		chkAllName.setImmediate(true);
		mainLayout.addComponent(chkAllName, "top:116.0px; left:445.0px;");
		
		Label lblfromdate=new Label("From Date");
		lblfromdate.setImmediate(false);
		mainLayout.addComponent(lblfromdate, "top:144px; left:45px");

		pdffromdate=new PopupDateField();
		pdffromdate.setImmediate(true);
		pdffromdate.setWidth("110px");
		pdffromdate.setHeight("24px");
		pdffromdate.setResolution(pdffromdate.RESOLUTION_DAY);
		pdffromdate.setValue(new Date());
		pdffromdate.setDateFormat("dd-MM-yyyy");
		mainLayout.addComponent(pdffromdate, "top:142px; left:185px");

		Label lbltodate=new Label("To Date");
		lbltodate.setImmediate(false);
		mainLayout.addComponent(lbltodate, "top:170px; left:45px");

		pdftodate=new PopupDateField();
		pdftodate.setImmediate(true);
		pdftodate.setWidth("110px");
		pdftodate.setHeight("24px");
		pdftodate.setResolution(pdffromdate.RESOLUTION_DAY);
		pdftodate.setValue(new Date());
		pdftodate.setDateFormat("dd-MM-yyyy");
		mainLayout.addComponent(pdftodate, "top:168px; left:185px");
		
		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:194px; left:185px");

		chkother=new CheckBox("Other");
		chkother.setImmediate(true);
		
		mainLayout.addComponent(chkother, "top:194px; left:245px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("____________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:210.0px;left:20.0px;");


		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:240.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:240.opx; left:260.0px");

		return mainLayout;
	}

	
	private void categoryType()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		
		String query= "select distinct 0, c.vCategoryType  from tbRawIssueInfo a "
				      +"inner join "
				      +"tbRawIssueDetails b on a.IssueNo=b.IssueNo "
				      +"inner join "
				      +"tbRawItemInfo c on c.vRawItemCode=b.ProductID where vCategoryType  like '%Spare Parts%' and a.vflag='New'";
		System.out.println(query);

		cmbCategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbcategorytype.addItem(element[1]);
			cmbcategorytype.setItemCaption(element[1], element[1].toString());
		}
	}

	private void allButtonAction()
	{
		chkpdf.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(chkpdf.booleanValue())
				{
					chkother.setValue(false);
					type=true;
				}
				else
				{
					chkother.setValue(true);
					type=false;
				}
		}

		});
		chkother.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(chkother.booleanValue())
				{
					chkpdf.setValue(false);
					type=false;
				}
				else
				{
					chkpdf.setValue(true);
					type=true;
				}
			}
		});

		cmbCategoryName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbCategoryName.getValue()!=null)
				{
					cmbsubcategoryName.setEnabled(true);
					chkAllsubcategory.setEnabled(true);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);

					subcategorydataload();
				}
				else
				{
					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					
					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					
					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					cmbsubcategoryName.removeAllItems();
					categoryData();
				}
			}
		});
		
		cmbsubcategoryName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				
				if(cmbsubcategoryName.getValue()!=null)
				{		
					cmbproductName.setEnabled(true);
					chkAllName.setEnabled(true);
					productDataload(); 			
				}			
				else{
					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					
					cmbproductName.removeAllItems();
					subcategorydataload();
				}
			}
		});
		
		
		chkAllsubcategory.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{

				boolean bv = chkAllsubcategory.booleanValue();
				if(bv==true)
				{
					cmbsubcategoryName.setValue(null);
					cmbsubcategoryName.setEnabled(false);

					cmbproductName.setEnabled(true);
					chkAllName.setEnabled(true);
					productDataload();
					}	
					else{
						cmbsubcategoryName.setEnabled(true);

						cmbsubcategoryName.setValue(null);
						chkAllsubcategory.setValue(false);

						cmbproductName.setEnabled(false);
						chkAllName.setEnabled(false);

						cmbproductName.setValue(null);
						chkAllName.setValue(false);
						cmbproductName.removeAllItems();
						subcategorydataload();
					}	
			}
		});
	/*	cmbcategorytype.addListener(new ValueChangeListener()
		{			
			public void valueChange(ValueChangeEvent event)
			{				
				if(cmbcategorytype.getValue()!=null)
				{
					cmbCategoryName.setEnabled(true);
					chkAllCategory.setEnabled(true);

					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					
					cmbsubcategoryName.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					
					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					
					chkAllCategory.setValue(false);
					categoryData();
					
				}			
				else
				{
					if(!chkallCategoryType.booleanValue())
					{
					cmbCategoryName.setEnabled(false);
					chkAllCategory.setEnabled(false);		
					
					cmbCategoryName.setValue(null);
					chkAllCategory.setValue(false);

					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					
					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					}
				}
			}
		});		
		chkallCategoryType.addListener(new ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkallCategoryType.booleanValue())
				 {
					 cmbcategorytype.setValue(null);
					 cmbcategorytype.setEnabled(false);
					 categoryData(); 
				 }
			}
		});*/
	
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if(cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()==true)
				{

					if(cmbproductName.getValue()!=null || chkAllName.booleanValue()==true)
					{			
						reportShow();
					}

					else
					{
						showNotification("Select Product Name", Notification.TYPE_WARNING_MESSAGE);
					}
				}
				else
				{
					getParent().showNotification("Select Category Name", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});


		exitButton.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});

		chkAllName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				boolean bv = chkAllName.booleanValue();
				if(bv==true)
				{
					cmbproductName.setValue(null);
					cmbproductName.setEnabled(false);
				}
				else{
					cmbproductName.setEnabled(true);
					if((cmbsubcategoryName.getValue()!=null || chkAllsubcategory.booleanValue()) && (cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()) && (cmbcategorytype.getValue()!=null ))
					productDataload();
				}
			}
		});


		chkAllCategory.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				boolean bv = chkAllCategory.booleanValue();
				if(bv==true)
				{
					cmbCategoryName.setValue(null);
					cmbCategoryName.setEnabled(false); 
					
					cmbsubcategoryName.setEnabled(true);
					chkAllsubcategory.setEnabled(true);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					subcategorydataload();
				}
				else{
					cmbCategoryName.setEnabled(true);
					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					
					cmbsubcategoryName.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					cmbsubcategoryName.removeAllItems();
					categoryData();
				}
			}
		});
	}
	
	private void categoryData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String 	categorytype="Spare Parts";
		
		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();
		}

		String query = "  select  Group_Id ,vCategoryName  from tbRawItemCategory where vCategoryType  like '"+categorytype+"' and vflag='New' ";

		System.out.println(query);

		cmbCategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbCategoryName.addItem(element[0]);
			cmbCategoryName.setItemCaption(element[0], element[1].toString());
		}
	}

	private void subcategorydataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		
		String categoryType="Spare Parts";
		String Category="";	
		
		if(cmbcategorytype.getValue()!=null)
		{
			categoryType=cmbcategorytype.getValue().toString();
		}
		
		if(chkAllCategory.booleanValue()==true)
		{
			Category="%";
		}
		else
		{
			Category=cmbCategoryName.getValue().toString();
		}
		
		String query = "select SubGroup_Id, vSubCategoryName from tbRawItemSubCategory where   vCategoryType like '"+categoryType+"' and Group_Id like '"+Category+"' and vflag='New'";

		System.out.println(query);

		cmbsubcategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();
		if(!list.isEmpty())
		{
			for(Iterator iter = list.iterator(); iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbsubcategoryName.addItem(element[0]);
				cmbsubcategoryName.setItemCaption(element[0], element[1].toString());
			}
		}
	}
	
	private void productDataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="" ;
		String strsubcategory="";
		String categorytype="Spare Parts";

		if(cmbcategorytype.getValue()!= null)
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}
		
		if(chkAllCategory.booleanValue()==true)
		{
			strcategory="%";
		}
		else
		{
			strcategory=cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue()==true)
		{
			strsubcategory="%";
		}
		else
		{
			strsubcategory=cmbsubcategoryName.getValue().toString();
		}
		
		String query= " select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where  a.vCategoryType like '"+categorytype+"' and a.vflag='New' and vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"'  ";
		System.out.println(query);

		List list = session.createSQLQuery(query).list();
		cmbproductName.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbproductName.addItem(element[0]);
			cmbproductName.setItemCaption(element[0], element[1].toString());
		}
	}
	private void reportShow()
	{
		System.out.println("into Reportshow");
		String query=null;
		String activeFlag = null;
		String categoryID="";
		String productID="";
		String subcategoryID="";
		String categirytype="Spare Parts";

		if(chkAllCategory.booleanValue()==true)
		{
			categoryID ="%"; 
		}
		else
		{
			categoryID = cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue()==true){
			subcategoryID="%";}
		else{
			subcategoryID=cmbsubcategoryName.getValue().toString();}

		if(chkAllName.booleanValue()==true)
		{
			productID ="%";
		}
		else
		{
			productID=cmbproductName.getValue().toString();
		}
		
		if(cmbcategorytype.getValue()!=null)
		{
			categirytype=cmbcategorytype.getValue().toString();	
		}		
		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();
			
			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			hm.put("UserName", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("parentType", cmbcategorytype.getValue().toString());
			hm.put("fromDate", new SimpleDateFormat("dd-MM-yyyy").format(pdffromdate.getValue()));
			hm.put("toDate", new SimpleDateFormat("dd-MM-yyyy").format(pdftodate.getValue()));
			hm.put("itemType", cmbcategorytype.getItemCaption(cmbcategorytype.getValue().toString()));

			query=" select * from [dbo].[funcatwiseStockDatebetween] ('"+new SimpleDateFormat("yyyy-MM-dd").format(pdffromdate.getValue())+"','"+new SimpleDateFormat("yyyy-MM-dd").format(pdftodate.getValue())+"', '"+productID+"','"+categoryID+"','"+subcategoryID+"','"+categirytype+"' )  where productId like '"+productID+"' "
					+ "and  purchaseQty >0 order by  groupid,subgroupid "; 

			System.out.println(query);
			hm.put("sql", query);
			System.out.println("123");
			List lst= session.createSQLQuery(query).list();
			if(!lst.isEmpty())
			{
				Window win = new ReportViewer(hm,"report/raw/RptMonthlyReceiveSummary.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",true);
				
				System.out.println("789");

				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);	
			}
			else
			{
			   showNotification("There are no Data",Notification.TYPE_WARNING_MESSAGE);	
			}			
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}
}
