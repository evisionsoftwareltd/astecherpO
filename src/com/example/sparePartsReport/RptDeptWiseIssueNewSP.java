package com.example.sparePartsReport;

import java.text.SimpleDateFormat;
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

public class RptDeptWiseIssueNewSP extends Window {
	private AbsoluteLayout mainLayout;

	private ComboBox  cmbSectionName;
	//private ComboBox cmbdemandNo;

	//private Label lbldemandNo;
	private Label lblSectionName;
	private Label lblline;
	private Label lblformDate;
	private Label lbltormDate;
	private Label lblCategoryName;
	private ComboBox  cmbCategoryName;
	private CheckBox chkAllCategory;
	private Label lblCategoryType=new Label();
	private ComboBox cmbcategorytype=new ComboBox();
	
	private ComboBox cmbitem= new ComboBox();
	private Label lblitem= new Label();
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");
	private CheckBox chkAllItem=new CheckBox("All");

	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private PopupDateField fromDate;
	private PopupDateField toDate;

	private SessionBean sessionBean;
	boolean type=false;
	public RptDeptWiseIssueNewSP(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		
		setContent(buildMainLayout());
		this.setResizable(false);
		this.setCaption("SECTION WISE ISSUE REGISTER ::  "+ sessionBean.getCompany());
		cmbcategorytype.focus();
		Component ob[]={fromDate, toDate, cmbSectionName,  previewButton};
		new FocusMoveByEnter(this, ob);
		allButtonAction();
		categoryType();
		cmbcategorytype.setValue("Spare Parts");
		cmbcategorytype.setEnabled(false);
		addCategoryName();
	}

	private void categoryType()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		
		 
		String query= "select distinct 0, b.vCategoryType from tbRawIssueDetails a "
				        + "inner join "
				         +"tbRawItemInfo b on a.ProductID=b.vRawItemCode where b.vCategoryType like '%Spare Parts%' and b.vflag='New'";

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
		
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
					if(cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue())
					{
						if(cmbSectionName.getValue()!=null)
						{
							if(cmbitem.getValue()!=null || chkAllItem.booleanValue())
							{
								reportShow();	
							}
							else
							{
								getParent().showNotification("Plese Select Item Name", Notification.TYPE_WARNING_MESSAGE);	
							}
								
						}
						else
						{
							getParent().showNotification("Plese Select Section Name", Notification.TYPE_WARNING_MESSAGE);
						}		
					}
					else
					{
						getParent().showNotification("Please Select Category Name", Notification.TYPE_WARNING_MESSAGE);	
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
		
		chkpdf.addListener(new ValueChangeListener()
		{
			
			public void valueChange(ValueChangeEvent event)
			{
				
				if(chkpdf.booleanValue()==true)
					chkother.setValue(false);
				else
					chkother.setValue(true);
				
			}
		});

		chkother.addListener(new ValueChangeListener()
		{
			
			public void valueChange(ValueChangeEvent event)
			{
				if(chkother.booleanValue()==true)
					chkpdf.setValue(false);
				else
					chkpdf.setValue(true);
				
			}
		});
		
		cmbCategoryName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbCategoryName.getValue()!=null)
				{
					String category=cmbCategoryName.getValue().toString();
					addSectionName(category);
				}
			}
		});
		
/*		cmbcategorytype.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbcategorytype.getValue()!=null)
				{
					addCategoryName();
				}
			}
		});*/
		
		
		chkAllCategory.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkAllCategory.booleanValue()==true)
				 {
					 
					cmbCategoryName.setEnabled(false);
					cmbCategoryName.setValue(null);
					addSectionName("%");
				 }
				 else
				 {
					 cmbCategoryName.setEnabled(true);
					cmbSectionName.removeAllItems();
						
				 }
				
			}
		});
		
		cmbSectionName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbSectionName.getValue()!=null && cmbcategorytype.getValue()!=null && (chkAllCategory.booleanValue() || cmbCategoryName.getValue()!=null ))
				{
					addItem();
				}
			}
		});
		
		chkAllItem.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkAllItem.booleanValue()==true)
				 {
					cmbitem.setEnabled(false);
					cmbitem.setValue(null);
				 }
				 else
				 {
					 cmbitem.setEnabled(true);
				 }
			}
		});
	}
	protected void categoryData() 
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="Spare Parts";
		
		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();
		}
		
		String query = "  select  Group_Id ,vCategoryName  from tbRawItemCategory where vCategoryType  like '"+categorytype+"' and vflag='New'";

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

	private void reportShow()
	{
		String query=null;
		String query1=null;
		String activeFlag = null;
		String categoryID="";
		String sectionid="";
		String categorytype="Spare Parts";
		String item="";

		String sectionId= cmbSectionName.getItemCaption(cmbSectionName.getValue());
		//String demandNo=cmbdemandNo.getItemCaption(cmbdemandNo.getValue());

		if(chkAllCategory.booleanValue())
		{
			categoryID ="%"; 
		}

		else
		{
			categoryID = cmbCategoryName.getValue().toString();
		}

		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}
		if(cmbitem.getValue()!=null)
		{
			item=cmbitem.getValue().toString();	
		}
		else
		{
		  item="%";	
		}
		
	
		if(chkpdf.booleanValue()==true)
			type=true;
		else
			type=false;

		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

	
			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			//hm.put("phone", "Phone: "+sessionBean.getCompanyPhone()+"   Fax:  "+sessionBean.getCompanyFax()+",   E-mail:  "+sessionBean.getCompanyEmail());
			hm.put("phone", sessionBean.getCompanyContact());
			hm.put("parentType", cmbcategorytype.getValue().toString());
			hm.put("userName", sessionBean.getUserName());
			hm.put("userIP",sessionBean.getUserIp());
			hm.put("fromDate", new SimpleDateFormat("dd-MM-yyyy").format(fromDate.getValue()));
			hm.put("toDate",new SimpleDateFormat("dd-MM-yyyy").format(toDate.getValue()));


			query=     "select * from  "
					   +"( "
						     +"select a.IssueRef, a.IssueNo,a.IssuedTo,c.SectionName,a.Date,a.challanNo,b.ProductID,d.vRawItemName,a.ProductionType,e.productTypeName ,a.productionStep,f.StepName ,a.finishedGoods,b.Qty,b.Rate,g.vProductName,d.vGroupName,d.vCategoryType  from tbRawIssueInfo a "
							 +"inner join "
							 +"tbRawIssueDetails b on a.IssueNo=b.IssueNo "
							 +"inner join "
							 +"tbSectionInfo c on c.AutoID=a.IssuedTo "
							 +"inner join "
							+"tbRawItemInfo d on d.vRawItemCode=b.ProductID "
							+"left join "
							+"tbProductionType e on e.productTypeId=a.ProductionType "
							+"left join "
							+"tbProductionStep f on f.StepId=a.productionStep "
							+"left join "
							+"tbFinishedProductInfo g on g.vProductId=a.finishedGoods "
							+"where   CONVERT(Date,a.Date,105)  between  '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate.getValue())+"' and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"'  "
							+"and a.vflag='New' and a.challanNo like '%' and a.IssuedTo like '"+cmbSectionName.getValue()+"' and d.vCategoryType='"+categorytype+"' and d.vGroupId like '"+categoryID+"' and b.ProductID like '"+item+"' and a.IssueType is null  " 
							
							+"union all "
							
							+"select a.IssueRef,a.IssueNo,a.IssuedTo,c.supplierName,a.Date,a.challanNo,b.ProductID,d.vRawItemName,a.ProductionType,e.productTypeName ,a.productionStep,f.StepName ,a.finishedGoods,b.Qty,b.Rate,g.vProductName,d.vGroupName,d.vCategoryType  from tbRawIssueInfo a "
							+"inner join "
							+"tbRawIssueDetails b on a.IssueNo=b.IssueNo "
							+"inner join "
							+"tbSupplierInfo c on c.AutoID=a.IssuedTo "
							+"inner join "
							+"tbRawItemInfo d on d.vRawItemCode=b.ProductID "
							+"left join "
							+"tbProductionType e on e.productTypeId=a.ProductionType "
							+"left join "
							+"tbProductionStep f on f.StepId=a.productionStep "
							+"left join "
							+"tbFinishedProductInfo g on g.vProductId=a.finishedGoods "
							+"where   CONVERT(Date,a.Date,105)  between  '"+new SimpleDateFormat("yyyy-MM-dd").format(fromDate.getValue())+"' and '"+new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"'  "
							+"and a.vflag='New'  and a.challanNo like '%' and a.IssuedTo like '"+cmbSectionName.getValue()+"' and d.vCategoryType='"+categorytype+"' and d.vGroupId like '"+categoryID+"' and b.ProductID like '"+item+"' and a.IssueType is not null   " 
							
						    +")  as temp order by temp.vCategoryType, temp.vGroupName ,temp.ProductID,  temp.Date, cast(temp.IssueNo as int), temp. challanNo  ";

		
			
			System.out.println(query);
			hm.put("sql", query);

			List lst=session.createSQLQuery(query).list();
			
			if(!lst.isEmpty())
			{
				Window win = new ReportViewer(hm,"report/raw/rptDepartmentWiseIssue.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
				win.setCaption("DEPARTMENT WISE ISSUE :: "+sessionBean.getCompany());

				this.getParent().getWindow().addWindow(win);
			}
			
			else
			{
				getParent().showNotification("Data Not Found", Notification.TYPE_WARNING_MESSAGE);
			}
			
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);
		}
	}

	public void addSectionName(String category)
	{
		cmbSectionName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			/*String query = "select distinct  a.IssuedTo,d.SectionName from tbRawIssueInfo a "
					       +"inner join "
					      +"tbRawIssueDetails b " 
					      +" on a.IssueNo=b.IssueNo "
					     +"inner join "
					     +"tbRawItemInfo c on b.ProductID=c.vRawItemCode "
					     +"inner join "
					     +"tbSectionInfo d on d.AutoID=a.IssuedTo "
					    +"where c.vCategoryType like '"+cmbcategorytype.getValue().toString()+"' and c.vGroupId like '"+category+"' ";*/
			
			String query=  
					 "select distinct  a.IssuedTo,(select SectionName from tbSectionInfo where CAST(AutoID as varchar)=a.IssuedTo) SectionName from tbRawIssueInfo a "
					 +"inner join " 
					 +"tbRawIssueDetails b  on a.IssueNo=b.IssueNo " 
					 +"inner join " 
					 +"tbRawItemInfo c on b.ProductID=c.vRawItemCode " 	
					 +"where c.vCategoryType like '"+cmbcategorytype.getValue().toString()+"' and c.vGroupId like '"+category+"'  "
					 + "and IssueType is null and a.vflag='New' "
					 +"union all "
					 +"select distinct  a.IssuedTo,d.supplierName from tbRawIssueInfo a " 
					 +"inner join " 
					 +"tbRawIssueDetails b on a.IssueNo=b.IssueNo "
					 +"inner join " 
					 +"tbRawItemInfo c on b.ProductID=c.vRawItemCode " 
					 +"inner join "
					 +"tbSupplierInfo d on d.AutoID=a.IssuedTo " 
					 +"where c.vCategoryType like '"+cmbcategorytype.getValue().toString()+"' and c.vGroupId like '"+category+"'  "
					 + "and IssueType is not null and a.vflag='New'";
		
			List list = session.createSQLQuery(query).list();
			System.out.println("Section List:"+list.size());

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbSectionName.addItem(element[0].toString());
				cmbSectionName.setItemCaption(element[0].toString(), element[1].toString());
			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	public void addCategoryName()
	{
		cmbCategoryName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			String query =	 "select distinct vGroupId,vGroupName from tbRawIssueDetails a "
					         +"inner join "
					         +"tbRawItemInfo b on a.ProductID=b.vRawItemCode "
					         +"where vCategoryType like '"+cmbcategorytype.getValue().toString()+"' and b.vflag='New'";
			
			List list = session.createSQLQuery(query).list();
			System.out.println("Section List:"+list.size());

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbCategoryName.addItem(element[0].toString());
				cmbCategoryName.setItemCaption(element[0].toString(), element[1].toString());
			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	public void addItem()
	{
		cmbitem.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			String  category="";
			
			String parenttype=cmbcategorytype.getValue().toString();
			String section="";
			String productid="";
			
			if(cmbCategoryName.getValue()!=null)
			{
				category=cmbCategoryName.getValue().toString();	
			}
			else
			{
				category="%";	
			}
			
			section=cmbSectionName.getValue().toString();
			
			if(cmbitem.getValue()!=null)
			{
				productid=cmbitem.getValue().toString();	
			}
			else
				
			{
				productid="%";	
			}
			
			
			String query =	"select distinct  b.ProductID,c.vRawItemName from tbRawIssueInfo a inner join "
					+"tbRawIssueDetails b on a.IssueNo=b.IssueNo inner join tbRawItemInfo c  "
					+"on c.vRawItemCode=b.ProductID where c.vCategoryType like '"+parenttype+"' and c.vGroupId like '"+category+"' and a.IssuedTo "
					+"like '"+section+"' and a.vflag='New' ";
			
			List list = session.createSQLQuery(query).list();
			System.out.println("Section List:"+list.size());

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbitem.addItem(element[0].toString());
				cmbitem.setItemCaption(element[0].toString(), element[1].toString());
			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("460px");
		mainLayout.setHeight("270px");
		mainLayout.setMargin(false);

		// top-level component properties
		//setWidth("480px");
		//setHeight("270px");
		
		lblCategoryType = new Label();
		lblCategoryType.setImmediate(false);
		lblCategoryType.setWidth("-1px");
		lblCategoryType.setHeight("-1px");
		lblCategoryType.setValue("Parent Type :");
		mainLayout.addComponent(lblCategoryType, "top:20.0px;left:50.0px;");

		// cmbSection
		cmbcategorytype = new ComboBox();
		cmbcategorytype.setImmediate(true);
		cmbcategorytype.setWidth("260px");
		cmbcategorytype.setHeight("24px");
		cmbcategorytype.setNullSelectionAllowed(false);
		cmbcategorytype.setNewItemsAllowed(false);
		cmbcategorytype.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbcategorytype, "top:18.0px;left:140.0px;");
		

		// lblSection
		lblCategoryName = new Label();
		lblCategoryName.setImmediate(false);
		lblCategoryName.setWidth("-1px");
		lblCategoryName.setHeight("-1px");
		lblCategoryName.setValue("Category  Name :");
		mainLayout.addComponent(lblCategoryName, "top:48.0px;left:50.0px;");

		// cmbSection
		cmbCategoryName = new ComboBox();
		cmbCategoryName.setImmediate(true);
		cmbCategoryName.setWidth("260px");
		cmbCategoryName.setHeight("24px");
		cmbCategoryName.setNullSelectionAllowed(false);
		cmbCategoryName.setNewItemsAllowed(false);
		cmbCategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbCategoryName, "top:46.0px;left:140.0px;");
		
		
		chkAllCategory = new CheckBox("");
		chkAllCategory.setCaption("All");
		chkAllCategory.setWidth("-1px");
		chkAllCategory.setHeight("24px");
		chkAllCategory.setImmediate(true);
		mainLayout.addComponent(chkAllCategory, "top:46.0px;left:405.0px;");
		
		
		// lblSection
		lblSectionName = new Label();
		lblSectionName.setImmediate(false);
		lblSectionName.setWidth("-1px");
		lblSectionName.setHeight("-1px");
		lblSectionName.setValue("Section Name");
		mainLayout.addComponent(lblSectionName, "top:76.0px;left:50.0px;");
		mainLayout.addComponent(new Label(":"), "top:76.0px; left:130.0px;");

		// cmbSection
		cmbSectionName = new ComboBox();
		cmbSectionName.setImmediate(true);
		cmbSectionName.setWidth("260px");
		cmbSectionName.setHeight("24px");
		cmbSectionName.setNullSelectionAllowed(false);
		cmbSectionName.setNewItemsAllowed(false);
		cmbSectionName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbSectionName, "top:74.0px;left:140.0px;");
		
		// lblSection
		lblitem = new Label();
		lblitem.setImmediate(false);
		lblitem.setWidth("-1px");
		lblitem.setHeight("-1px");
		lblitem.setValue("Item Name");
		mainLayout.addComponent(lblitem, "top:104.0px;left:50.0px;");
		

		// cmbSection
		cmbitem = new ComboBox();
		cmbitem.setImmediate(true);
		cmbitem.setWidth("260px");
		cmbitem.setHeight("24px");
		cmbitem.setNullSelectionAllowed(false);
		cmbitem.setNewItemsAllowed(false);
		cmbitem.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbitem, "top:102.0px;left:140.0px;");
		
		chkAllItem = new CheckBox("");
		chkAllItem.setCaption("All");
		chkAllItem.setWidth("-1px");
		chkAllItem.setHeight("24px");
		chkAllItem.setImmediate(true);
		mainLayout.addComponent(chkAllItem, "top:102.0px;left:405.0px;");
		
		
						
		lblformDate = new Label();
		lblformDate.setImmediate(false);
		lblformDate.setWidth("-1px");
		lblformDate.setHeight("-1px");
		lblformDate.setValue("From Date");
		mainLayout.addComponent(lblformDate, "top:132.0px;left:50.0px;");
		
		fromDate= new PopupDateField();
		fromDate.setWidth("109px");
		fromDate.setDateFormat("dd-MM-yyyy");
		fromDate.setValue(new java.util.Date());
		fromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(fromDate, "top:130.0px;left:140.0px;");

		lbltormDate = new Label();
		lbltormDate.setImmediate(true);
		lbltormDate.setWidth("-1px");
		lbltormDate.setHeight("-1px");
		lbltormDate.setValue(" To Date");
		mainLayout.addComponent(lbltormDate, "top:160.0px;left:50.0px;");


		toDate = new PopupDateField();
		toDate.setWidth("109px");
		toDate.setDateFormat("dd-MM-yyyy");
		toDate.setValue(new java.util.Date());
		toDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(toDate, "top:158.0px;left:140.0px;");

	
		chkpdf.setValue(true);
		chkpdf.setImmediate(true);
		chkother.setImmediate(true);
		mainLayout.addComponent(chkpdf, "top:180.0px;left:170.0px;");
		mainLayout.addComponent(chkother, "top:180.0px;left:215.0px;");
	

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("____________________________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:186.0px;left:0.0px;");

		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		//previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:220.0px; left:150.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:220.0px; left:230.0px");

		return mainLayout;
	}
}
