package com.example.ThirdPartyReport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class RptThirdPartyReceiptRegister extends Window {
	private AbsoluteLayout mainLayout;

	private Label lblline;
	private Label lblformDate;
	private Label lbltormDate;
	private Label lblCategoryType;
	private ComboBox  cmbCategoryType;
	
	private CheckBox chkAllCategoryType;
	private ComboBox cmbPartyName= new ComboBox();
	private Label lblPartyName= new Label();
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");
	private CheckBox chkAllPartyName=new CheckBox("All");

	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dateFparameter= new SimpleDateFormat("dd-MM-yyyy");
	private PopupDateField fromDate;
	private PopupDateField toDate;

	ArrayList<Component> allComp = new ArrayList<Component>();
	
	private SessionBean sessionBean;
	boolean type=true;
	public RptThirdPartyReceiptRegister(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		
		setContent(buildMainLayout());
		this.setResizable(false);
		this.setCaption("RECEIPT REGISTER ::  "+ sessionBean.getCompany());
		cmbCategoryType.focus();
		Component ob[]={cmbPartyName, fromDate, toDate,  previewButton};
		new FocusMoveByEnter(this, ob);
		allButtonAction();
		categoryTypeData(); 
		focusEnter();
		}
	
	private void focusEnter()
	{
		allComp.add(cmbCategoryType);
		allComp.add(cmbPartyName);
		allComp.add(fromDate);
		allComp.add(toDate);
		
		allComp.add(previewButton);
		allComp.add(exitButton);
		
		new FocusMoveByEnter(this,allComp);
	}


	private void allButtonAction()
	{	
		
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				
					if(cmbCategoryType.getValue()!=null || chkAllCategoryType.booleanValue())
					{
						
							
							if(cmbPartyName.getValue()!=null || chkAllPartyName.booleanValue())
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
				if(chkother.booleanValue()==true)									
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
		
		cmbCategoryType.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbCategoryType.getValue()!=null)
				{
					String category=cmbCategoryType.getValue().toString();
					PartyNameData();
				}
			}
		});
		
		
		
		chkAllCategoryType.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkAllCategoryType.booleanValue()==true)
				 {
					 
					cmbCategoryType.setEnabled(false);
					cmbCategoryType.setValue(null);
					PartyNameData();
				 }
				 else
				 {
					 cmbCategoryType.setEnabled(true);
					cmbPartyName.removeAllItems();
						
				 }
				
			}
		});
		
		
		chkAllPartyName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				 if(chkAllPartyName.booleanValue()==true)
				 {
					 
					cmbPartyName.setEnabled(false);
					cmbPartyName.setValue(null);
					
				 }
				 else
				 {
					 cmbPartyName.setEnabled(true);
						
				 }
				
			}
		});
		
		
	}

	protected void categoryTypeData() 
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="";
		
		String query = "  select Distinct vCategoryTyepe,vCategoryTyepe from tbThirdPartyItemReceiptInfo ";

		System.out.println(query);

		cmbCategoryType.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbCategoryType.addItem(element[0]);
			cmbCategoryType.setItemCaption(element[0], element[1].toString());
		}
		
	}


	protected void categoryData() 
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="";
		
		String query = "  select  Group_Id ,vCategoryName  from tbRawItemCategory where vCategoryType  like '"+categorytype+"' ";

		System.out.println(query);

		cmbCategoryType.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbCategoryType.addItem(element[0]);
			cmbCategoryType.setItemCaption(element[0], element[1].toString());
		}
		
	}

	private void reportShow()
	{
		String query=null;
		String query1=null;
		String activeFlag = null;
		String party="";
		String categorytype="";

		if(chkAllCategoryType.booleanValue())
		{
			categorytype ="%"; 

		}

		else
		{
			categorytype = cmbCategoryType.getValue().toString();
		}

		if(cmbPartyName.getValue()!=null)
		{
			party=cmbPartyName.getValue().toString();	
		}
		else
		{
		  party="%";	
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
			hm.put("Phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+" "+sessionBean.getUserIp());
			hm.put("fromDate", dateFparameter.format(fromDate.getValue()));
			hm.put("toDate",dateFparameter.format(toDate.getValue()));


			query= "select vCategoryTyepe,a.dReceiptDate,a.vReceiptNo,a.vPartyId,a.vPartyName, "
					+ "a.vChallanNo,a.dChallanDate,b.vUnit,b.vProductId,b.vProductName,b.mQty from tbThirdPartyItemReceiptInfo a inner join tbThirdPartyItemReceiptDetails b"
					+ " on a.iTransectionId=b.iTransectionId "
					+ "where a.vCategoryTyepe like '"+categorytype+"'"
					+ " and a.vPartyId like '"+party+"' "
					+ "and CONVERT(date,a.dReceiptDate,105)"
					+ " between '"+dateFormat.format(fromDate.getValue())+"' "
					+ "and '"+dateFormat.format(toDate.getValue())+"' ";

		
			
			System.out.println(query);
			hm.put("sql", query);
			System.out.println("query ok");
			List lst=session.createSQLQuery(query).list();
			System.out.println("convert check");
			if(!lst.isEmpty())
			{
				System.out.println("convert ok");
				Window win = new ReportViewer(hm,"report/production/Thirdparty/rptReceiptRegister.jasper",
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

	
	public void PartyNameData()
	{
		cmbPartyName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			String  category="";
			
			if(cmbCategoryType.getValue()!=null)
			{
				category=cmbCategoryType.getValue().toString();	
			}
			else
			{
				category="%";	
			}
			String query =	"select Distinct vPartyId,vPartyName from tbThirdPartyItemReceiptInfo "
					+ "where vCategoryTyepe like '"+category+"' ";
			
			List list = session.createSQLQuery(query).list();
			System.out.println("Section List:"+list.size());

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbPartyName.addItem(element[0].toString());
				cmbPartyName.setItemCaption(element[0].toString(), element[1].toString());
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
		mainLayout.setHeight("230px");
		mainLayout.setMargin(false);

		// top-level component properties
		//setWidth("480px");
		//setHeight("270px");
		

		lblCategoryType = new Label();
		lblCategoryType.setImmediate(false);
		lblCategoryType.setWidth("-1px");
		lblCategoryType.setHeight("-1px");
		lblCategoryType.setValue("Category Type:");
		mainLayout.addComponent(lblCategoryType, "top:20.0px;left:50.0px;");
		

		// cmbSection
		cmbCategoryType = new ComboBox();
		cmbCategoryType.setImmediate(true);
		cmbCategoryType.setWidth("260px");
		cmbCategoryType.setHeight("24px");
		cmbCategoryType.setNullSelectionAllowed(false);
		cmbCategoryType.setNewItemsAllowed(false);
		cmbCategoryType.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbCategoryType, "top:18.0px;left:140.0px;");
		
		
		chkAllCategoryType = new CheckBox("");
		chkAllCategoryType.setCaption("All");
		chkAllCategoryType.setWidth("-1px");
		chkAllCategoryType.setHeight("24px");
		chkAllCategoryType.setImmediate(true);
		mainLayout.addComponent(chkAllCategoryType, "top:20.0px;left:405.0px;");
		
		
		
		
		// lblSection
		lblPartyName = new Label();
		lblPartyName.setImmediate(false);
		lblPartyName.setWidth("-1px");
		lblPartyName.setHeight("-1px");
		lblPartyName.setValue("Party Name:");
		mainLayout.addComponent(lblPartyName, "top:48.0px;left:50.0px;");
		

		// cmbSection
		cmbPartyName = new ComboBox();
		cmbPartyName.setImmediate(true);
		cmbPartyName.setWidth("260px");
		cmbPartyName.setHeight("24px");
		cmbPartyName.setNullSelectionAllowed(false);
		cmbPartyName.setNewItemsAllowed(false);
		cmbPartyName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbPartyName, "top:46.0px;left:140.0px;");
		
		chkAllPartyName = new CheckBox("");
		chkAllPartyName.setCaption("All");
		chkAllPartyName.setWidth("-1px");
		chkAllPartyName.setHeight("24px");
		chkAllPartyName.setImmediate(true);
		mainLayout.addComponent(chkAllPartyName, "top:48.0px;left:405.0px;");
		
		
						
		lblformDate = new Label();
		lblformDate.setImmediate(false);
		lblformDate.setWidth("-1px");
		lblformDate.setHeight("-1px");
		lblformDate.setValue("From Date:");
		mainLayout.addComponent(lblformDate, "top:76.0px;left:50.0px;");
		
		fromDate= new PopupDateField();
		fromDate.setWidth("109px");
		fromDate.setDateFormat("dd-MM-yyyy");
		fromDate.setValue(new java.util.Date());
		fromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(fromDate, "top:74.0px;left:140.0px;");

		lbltormDate = new Label();
		lbltormDate.setImmediate(true);
		lbltormDate.setWidth("-1px");
		lbltormDate.setHeight("-1px");
		lbltormDate.setValue(" To Date:");
		mainLayout.addComponent(lbltormDate, "top:104.0px;left:50.0px;");


		toDate = new PopupDateField();
		toDate.setWidth("109px");
		toDate.setDateFormat("dd-MM-yyyy");
		toDate.setValue(new java.util.Date());
		toDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(toDate, "top:102.0px;left:140.0px;");

	
		chkpdf.setValue(true);
		chkpdf.setImmediate(true);
		chkother.setImmediate(true);
		mainLayout.addComponent(chkpdf, "top:144.0px;left:170.0px;");
		mainLayout.addComponent(chkother, "top:144.0px;left:215.0px;");
	

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("____________________________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:150.0px;left:0.0px;");

		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		//previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:190.0px; left:130.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:190.0px; left:250.0px");

		return mainLayout;
	}

}
