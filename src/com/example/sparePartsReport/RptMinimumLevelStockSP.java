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

public class RptMinimumLevelStockSP extends Window {

	private AbsoluteLayout mainLayout;
	private ComboBox  cmbCategoryName;
	private Label lblCategoryName;
	private Label lblProductName;
	private ComboBox  cmbproductName;
	private Label lblsubcategoryName;
	private ComboBox cmbsubcategoryName;
	private CheckBox chkAllsubcategory;
	private ComboBox cmbsubsubCategory;
	private Label lblsubsubCategory;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	SessionBean sessionBean;
	private CheckBox chkAllName;
	private CheckBox chkAllCategory;
	private CheckBox chkAllsubsubCategory;
	private Label lblCategoryType=new Label();
	private ComboBox cmbcategorytype=new ComboBox();
	private CheckBox chkallCategoryType=new CheckBox();
	
	private PopupDateField asOnDate;
	private Label lblasOnDate;
	
	private CheckBox chkpdf,chkother;
	boolean type=false;

	public  RptMinimumLevelStockSP(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("MINIMUM LEVEL STOCK STATEMENT ::  "+ sessionBean.getCompany());
		cmbcategorytype.focus();
		Component ob[]={cmbCategoryName,cmbproductName,cmbsubcategoryName,previewButton};
		new FocusMoveByEnter(this, ob);
		//categoryData();
		categoryType();
		allButtonAction();
	}

	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("520px");
		mainLayout.setHeight("270px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("560px");
		setHeight("350px");
		
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
		cmbcategorytype.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbcategorytype, "top:38.0px;left:185.0px;");
		
		
		chkallCategoryType = new CheckBox("");
		chkallCategoryType.setCaption("All");
		chkallCategoryType.setWidth("-1px");
		chkallCategoryType.setHeight("24px");
		chkallCategoryType.setImmediate(true);
		mainLayout.addComponent(chkallCategoryType, "top:38.0px;left:445.0px;");
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
		
		lblsubsubCategory = new Label();
		lblsubsubCategory.setImmediate(false);
		lblsubsubCategory.setWidth("-1px");
		lblsubsubCategory.setHeight("-1px");
		lblsubsubCategory.setValue("Sub Sub-Category Name :");
		mainLayout.addComponent(lblsubsubCategory, "top:118.0px;left:45.0px;");

		cmbsubsubCategory = new ComboBox();
		cmbsubsubCategory.setWidth("260px");
		cmbsubsubCategory.setHeight("24px");
		cmbsubsubCategory.setImmediate(true);
		cmbsubsubCategory.setNewItemsAllowed(false);
		cmbsubsubCategory.setNullSelectionAllowed(false);
		cmbsubsubCategory.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbsubsubCategory, "top:116.0px;left:185.0px;");

		chkAllsubsubCategory=new CheckBox("");
		chkAllsubsubCategory.setCaption("All");
		chkAllsubsubCategory.setImmediate(true);
		chkAllsubsubCategory.setWidth("-1px");
		chkAllsubsubCategory.setHeight("24px");
		mainLayout.addComponent(chkAllsubsubCategory, "top:116.0px;left:445.0px;");
		
		lblProductName = new Label();
		lblProductName.setImmediate(false);
		lblProductName.setWidth("-1px");
		lblProductName.setHeight("-1px");
		lblProductName.setValue("Product  Name :");
		mainLayout.addComponent(lblProductName, "top:144.0px;left:45.0px;");

		cmbproductName = new ComboBox();
		cmbproductName.setImmediate(true);
		cmbproductName.setWidth("260px");
		cmbproductName.setHeight("24px");
		cmbproductName.setNullSelectionAllowed(false);
		cmbproductName.setNewItemsAllowed(false);
		cmbproductName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbproductName, "top:142.0px;left:185.0px;");

		chkAllName = new CheckBox("");
		chkAllName.setCaption("All");
		chkAllName.setWidth("-1px");
		chkAllName.setHeight("24px");
		chkAllName.setImmediate(true);
		mainLayout.addComponent(chkAllName, "top:142.0px; left:445.0px;");

		lblasOnDate=new Label("As On Date");
		lblasOnDate.setImmediate(false);
		mainLayout.addComponent(lblasOnDate, "top:170.0px;left:45.0px;");

		asOnDate=new PopupDateField();
		asOnDate.setImmediate(true);
		asOnDate.setWidth("110px");
		asOnDate.setHeight("24px");
		asOnDate.setResolution(asOnDate.RESOLUTION_DAY);
		asOnDate.setValue(new Date());
		asOnDate.setDateFormat("dd-MM-yyyy");
		mainLayout.addComponent(asOnDate, "top:168px; left:185px");
		
		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:200px; left:185px");

		chkother=new CheckBox("Other");
		chkother.setImmediate(true);
		mainLayout.addComponent(chkother, "top:200px; left:245px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("____________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:220.0px;left:20.0px;");

		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:240.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:240.opx; left:250.0px");

		return mainLayout;
	}

	private void categoryData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="";
		
		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();
		}
		
		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";
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

	private void categoryType()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();

		String query = "  select  distinct 0,vCategoryType from tbRawItemCategory where vCategoryType  like '%Spare Parts%' and  vflag='New'";

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
		
		String categoryType="";
		String Category="";
		
		if(chkallCategoryType.booleanValue())
		{
			categoryType="%";	
		}
		
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
		/*else
		{
			productDataload();
		}*/
	}

	private void productDataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";
		String strsubsubCategory="";
		String subsubcategory="";
		String categorytype="";

		//String catid="%";

		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();
		
	/*	if(chkAllsubsubCategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();*/
		if(chkAllsubsubCategory.booleanValue())
		{
			subsubcategory="%";
		}
		else
		{
			subsubcategory=cmbsubsubCategory.getValue().toString();
		}
		
		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";
		}
		else
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}
		
		
		//String  query=  "select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"'and  " ;
		
		String query= " select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"' and  vsubsubCategoryId like '"+subsubcategory+"'  and a.vCategoryType like '"+categorytype+"'  and  a.vflag='New'";


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
/*
	private void productDataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";

		//String catid="%";

		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();

		String  query=  "select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"'" ;


		System.out.println(query);

		List list = session.createSQLQuery(query).list();
		cmbproductName.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbproductName.addItem(element[0]);
			cmbproductName.setItemCaption(element[0], element[1].toString());
		}
	}*/
	
	private void SubsubCategoryDataAdd()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";
		String categorytype="";

		//String catid="%";

		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();
		
		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";	
		}
		else
		{
			categorytype=cmbcategorytype.getValue().toString();
		}

		//String  query=  "select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"'" ;
		
		String query= " select iSubCategoryid,vSubSubCategoryName from tbRawItemsubSubCategory  where groupid like '"+strcategory+"' and SubGroupid like '"+strsubcategory+"' and vCategoryType like '"+categorytype+"' ";


		System.out.println(query);

		List list = session.createSQLQuery(query).list();
		cmbsubsubCategory.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbsubsubCategory.addItem(element[0]);
			cmbsubsubCategory.setItemCaption(element[0], element[1].toString());
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
					subcategorydataload();
				}
			}
		});
		
		chkAllCategory.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkAllCategory.booleanValue()==true)
				 { 
					 if(chkallCategoryType.booleanValue()|| cmbcategorytype.getValue()!=null)
					 {
						 subcategorydataload(); 
					 }
				 }
				
			}
		});
		
		cmbsubcategoryName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbsubcategoryName.getValue()!=null)
				{
					SubsubCategoryDataAdd();
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
					if ((cmbcategorytype.getValue()!=null || chkallCategoryType.booleanValue())  && (cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()) )
					{
						cmbsubcategoryName.setValue(null);
						cmbsubcategoryName.setEnabled(false);
						SubsubCategoryDataAdd();	
					}	
				}
			}
		});

		cmbcategorytype.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbcategorytype.getValue()!=null)
				{
					categoryData();
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
		});

		cmbsubsubCategory.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event)
			{
			     if(cmbsubsubCategory.getValue()!=null)
			     {
			    	 productDataload(); 
			     }
				
			}
		});
		
		chkAllsubsubCategory.addListener(new ValueChangeListener() {
			
			public void valueChange(ValueChangeEvent event) 
			{
				  if(chkAllsubsubCategory.booleanValue())
				  {
					  if (  (chkallCategoryType.getValue()!=null || chkallCategoryType.booleanValue()  ) && (chkAllCategory.booleanValue() || cmbCategoryName.getValue()!=null ) && (chkAllsubsubCategory.booleanValue()|| cmbsubsubCategory.getValue()!=null)    )
					  {
						  cmbsubsubCategory.setEnabled(false);
						  productDataload(); 
					  }
					  
				  }
			}
		});

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
					subcategorydataload();
					//productDataload();
				}
				else{
					cmbCategoryName.setEnabled(true);
					cmbsubcategoryName.removeAllItems();
					cmbproductName.removeAllItems();
					categoryData();
				}
			}
		});
	}

	private void reportShow()
	{
		System.out.println("into Reportshow");
		String query=null;
		String activeFlag = null;
		String categoryID="";
		String productID="";
		String subcategoryID="";
		String categirytype="";
		String subsubcategory="";


		if(chkAllCategory.booleanValue())
		{
			categoryID ="%"; 
		}
		else
		{
			categoryID = cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue())
			subcategoryID="%";
		else
			subcategoryID=cmbsubcategoryName.getValue().toString();

		if(chkAllName.booleanValue())
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
		
		if(chkallCategoryType.booleanValue())
		{
			categirytype="%";	
		}
		
		if(cmbsubsubCategory.getValue()!=null)
		{
			subsubcategory=cmbsubsubCategory.getValue().toString();
		}
		
		if(chkAllsubsubCategory.booleanValue())
		{
			subsubcategory="%";	
		}


		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();



			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("Phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("reportName", cmbcategorytype.getValue().toString());
			hm.put("AsonDate", new SimpleDateFormat("dd-MM-yy").format(asOnDate.getValue()));
		
			/* query=" select * from tbRawItemInfo a  "
					+" inner join "
					+" tbRawItemCategory b "
					+" on a.vGroupId=b.Group_Id "
					+" where  a.vGroupId like '"+categoryID+"' and a.vRawItemCode like '"+productID+"' order by iCategoryCode ";*/

			//session.createSQLQuery(query).executeUpdate();
/*
			query="select * from tbRawItemInfo a inner join tbRawItemCategory b on a.vGroupId=b.Group_Id left outer join tbRawItemSubCategory c "
					+"on c.SubGroup_Id=a.vSubGroupId where a.vGroupId like '"+categoryID+"' and a.vSubGroupId like '"+subcategoryID+"' and a.vRawItemCode like '"+productID+"' order by iCategoryCode";*/
			
			
			session.createSQLQuery("exec PrcRawLevelStocknew '"+new SimpleDateFormat("yyyy/MM/dd").format(asOnDate.getValue()) +" "+"23:59:59" + "','"+categoryID+"','"+subcategoryID+"','"+subsubcategory+"','"+categirytype+"','"+productID+"'").executeUpdate();
			tx.commit();
			 
             query="select * from   tbTmpLevelStatement where MinLavel >=StockQty order by SUBSTRING(groupid,2,LEN(groupid)),SUBSTRING(subgroupid,2,LEN(subgroupid)),SUBSTRING(subsubgroupid,2,LEN(subsubgroupid))  ";     
			


			System.out.println(query);
			hm.put("sql", query);
			System.out.println("123");

		/*	Window win = new ReportViewer(hm,"report/rawProductInfoRpt.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",false);*/
			
			Window win = new ReportViewer(hm,"report/raw/rptminimumevel.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",true);
			
			System.out.println("789");

			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}
}