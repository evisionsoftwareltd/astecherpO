package com.example.sparePartsReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

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

public class RptItemWiseKardexSP extends Window {

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
	private CheckBox chkallCategoryType=new CheckBox();	
	private PopupDateField pdffromdate, pdftodate;
	private Label lblasOnDate;
	private SimpleDateFormat dbformat=new SimpleDateFormat("yyyy-MM-dd");
	private CheckBox chkpdf,chkother;
	boolean type=false;

	public  RptItemWiseKardexSP(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("ITEM WISE KARDEX::  "+ sessionBean.getCompany());
		cmbcategorytype.focus();
		Component ob[]={cmbCategoryName,cmbproductName,cmbsubcategoryName,previewButton};
		new FocusMoveByEnter(this, ob);
		categoryType();
		cmbcategorytype.setValue("Spare Parts");
		cmbcategorytype.setEnabled(false);
		allButtonAction();	
		categoryData(); 
		//cmbCategoryName.setEnabled(false);
		chkAllCategory.setEnabled(false);
		//cmbsubcategoryName.setEnabled(false);
		chkAllsubcategory.setEnabled(true);
		//cmbproductName.setEnabled(false);
		chkAllName.setEnabled(true);
	}

	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("550px");
		mainLayout.setHeight("280px");
		mainLayout.setMargin(false);

		//top-level component properties
		setWidth("560px");
		setHeight("360px");
		
		lblCategoryType = new Label();
		lblCategoryType.setImmediate(false);
		lblCategoryType.setWidth("-1px");
		lblCategoryType.setHeight("-1px");
		lblCategoryType.setValue("Parent Type :");
		mainLayout.addComponent(lblCategoryType, "top:40.0px;left:45.0px;");

		// cmbSection
		cmbcategorytype = new ComboBox();
		cmbcategorytype.setImmediate(true);
		cmbcategorytype.setWidth("295px");
		cmbcategorytype.setHeight("24px");
		cmbcategorytype.setNullSelectionAllowed(false);
		cmbcategorytype.setNewItemsAllowed(false);
		cmbcategorytype.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbcategorytype, "top:38.0px;left:185.0px;");
				
		chkallCategoryType = new CheckBox("");
		chkallCategoryType.setCaption("All");
		chkallCategoryType.setWidth("-1px");
		chkallCategoryType.setHeight("24px");
		chkallCategoryType.setImmediate(true);
		//mainLayout.addComponent(chkallCategoryType, "top:38.0px;left:445.0px;");
		chkallCategoryType.setVisible(false);
		
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
		cmbCategoryName.setWidth("295px");
		cmbCategoryName.setHeight("24px");
		cmbCategoryName.setNullSelectionAllowed(false);
		cmbCategoryName.setNewItemsAllowed(false);
		cmbCategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbCategoryName, "top:64.0px;left:185.0px;");
			
		chkAllCategory = new CheckBox("");
		chkAllCategory.setCaption("All");
		chkAllCategory.setWidth("-1px");
		chkAllCategory.setHeight("24px");
	//	chkAllCategory.setImmediate(true);
		//mainLayout.addComponent(chkAllCategory, "top:64.0px;left:445.0px;");
		
		lblsubcategoryName = new Label();
		lblsubcategoryName.setImmediate(false);
		lblsubcategoryName.setWidth("-1px");
		lblsubcategoryName.setHeight("-1px");
		lblsubcategoryName.setValue("Sub-Category Name :");
		mainLayout.addComponent(lblsubcategoryName, "top:92.0px;left:45.0px;");

		cmbsubcategoryName = new ComboBox();
		cmbsubcategoryName.setWidth("295px");
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
	//	mainLayout.addComponent(chkAllsubcategory, "top:90.0px;left:480.0px;");
		
		lblProductName = new Label();
		lblProductName.setImmediate(false);
		lblProductName.setWidth("-1px");
		lblProductName.setHeight("-1px");
		lblProductName.setValue("Product  Name :");
		mainLayout.addComponent(lblProductName, "top:118.0px;left:45.0px;");

		cmbproductName = new ComboBox();
		cmbproductName.setImmediate(true);
		cmbproductName.setWidth("295px");
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
		//mainLayout.addComponent(chkAllName, "top:116.0px; left:480.0px;");
		
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
		mainLayout.addComponent(lblline, "top:230.0px;left:20.0px;");

		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:250.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:250.opx; left:250.0px");

		return mainLayout;
	}

	private void categoryData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="Spare Parts";
		
		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();
		}
		
	/*	if(chkallCategoryType.booleanValue())
		{
			categorytype="%";
		}
*/
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

	private void categoryType()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		
		String query= "select distinct 0, c.vCategoryType  from tbRawIssueInfo a "
				      +"inner join "
				      +"tbRawIssueDetails b on a.IssueNo=b.IssueNo "
				      +"inner join "
				      +"tbRawItemInfo c on c.vRawItemCode=b.ProductID where c.vCategoryType  like '%Spare Parts%' and a. vflag='New' ";

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
	
	private void productData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";

		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();

		String  query=  "select vRawItemCode, vRawItemName,vGroupId from tbRawItemInfo  where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"' and vflag='New' " ;

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

	private void subcategorydataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		
		String categoryType="Spare Parts";
		String Category="";
		
	/*	if(chkallCategoryType.booleanValue())
		{
			categoryType="%";	
		}*/
		
		if(cmbcategorytype.getValue()!=null)
		{
			categoryType=cmbcategorytype.getValue().toString();
		}
		
		if(chkAllCategory.booleanValue())
		{
			Category="%";
		}
		if(cmbCategoryName.getValue()!=null)
		{
			Category=cmbCategoryName.getValue().toString();
		}
		
		String query = "select SubGroup_Id, vSubCategoryName from tbRawItemSubCategory where Group_Id like '"+Category+"'and vCategoryType like '"+categoryType+"' and vflag='New'";

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
		String strcategory="", strsubcategory="";
		String categorytype="Spare Parts";
		if(chkAllCategory.booleanValue()==true)
		{
			strcategory="%";
		}
		else
		{
			strcategory=cmbCategoryName.getValue().toString();
		}
		if(cmbsubcategoryName.getValue()==null)
		{
			strsubcategory="%";}
		else
		{
			strsubcategory=cmbsubcategoryName.getValue().toString();}
		
		if(cmbcategorytype.getValue().toString()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}
				
		String query= " select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"'   and  a.vCategoryType like '"+categorytype+"' and a. vflag='New'";
		System.out.println(query);

		List<?> list = session.createSQLQuery(query).list();
		cmbproductName.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbproductName.addItem(element[0]);
			cmbproductName.setItemCaption(element[0], element[1].toString());
		}
	}

	private void SubsubCategoryDataAdd()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";
		String categorytype="Spare Parts";
		
		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();
		
	/*	if(chkallCategoryType.booleanValue())
		{
			categorytype="%";	
		}*/
		if(cmbcategorytype.getValue().toString()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}		
		
		String query= " select iSubCategoryid,vSubSubCategoryName from tbRawItemsubSubCategory  where groupid like '"+strcategory+"' and SubGroupid like '"+strsubcategory+"' and vCategoryType like '"+categorytype+"'";
		System.out.println(query);
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

					cmbproductName.setEnabled(true);
					chkAllName.setEnabled(true);
					
					subcategorydataload();
					productDataload(); 
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
					//productDataload(); 			
				}			
				else{
					/*cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);*/
					
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
		/*cmbcategorytype.addListener(new ValueChangeListener()
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

	private void reportShow()
	{
		System.out.println("into Reportshow");
		String query=null;
		Transaction tx= null;
		String categoryID="";
		String subcategoryID="";
		String productID="";
		
		if(cmbCategoryName.getValue()!=null)
		{	
			categoryID = cmbCategoryName.getValue().toString();
		}
		if(cmbsubcategoryName.getValue()!=null){
		
			subcategoryID=cmbsubcategoryName.getValue().toString();
		}
		if(cmbproductName.getValue()!=null)
		{
			productID=cmbproductName.getValue().toString();
		}
	
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("parentType", cmbcategorytype.getValue().toString());
			hm.put("category",cmbCategoryName.getItemCaption(cmbCategoryName.getValue()));
			hm.put("subcategory", cmbsubcategoryName.getItemCaption(cmbsubcategoryName.getValue()));
			hm.put("item", cmbproductName.getItemCaption(cmbproductName.getValue()));
			hm.put("fromdate", new SimpleDateFormat("dd-MM-yy").format(pdffromdate.getValue()));
			hm.put("todate", new SimpleDateFormat("dd-MM-yy").format(pdftodate.getValue()));
			if(cmbproductName.getValue()!=null)
			{
				
				String sql="select  * from funRawMaterialsStockNewkardex ('"+dbformat.format(pdffromdate.getValue())+"','"+dbformat.format(pdftodate.getValue())+"','"+cmbproductName.getValue()+"') ";
			
			System.out.println(sql);
			hm.put("sql", sql);
			List lst= session.createSQLQuery(sql).list();
			if(!lst.isEmpty())
			{
				System.out.println("Trying To Do");
				Window win = new ReportViewer(hm,"report/raw/RptItemWiseKardex.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",true);				
				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);	
			}		
			else
			{
			   showNotification("Data does not exists on the given criteria.",Notification.TYPE_WARNING_MESSAGE);	
			}
			}
			else
			{			
				getParent().showNotification("Please select Item name!!!", Notification.TYPE_WARNING_MESSAGE);
				
			}
		}
		catch(Exception exp){
			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);
		}
		
	}
}
