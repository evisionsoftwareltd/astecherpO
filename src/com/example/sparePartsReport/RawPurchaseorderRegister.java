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
import com.common.share.ReportViewerNew;
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

public class RawPurchaseorderRegister extends Window {

	private AbsoluteLayout mainLayout;
	private ComboBox  cmbSupplierName;
	private Label lblSupplierName;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private PopupDateField fromDate;
	private PopupDateField toDate;
	private Label lblformDate;
	private Label lbltormDate;
	private SessionBean sessionBean;
	
	int type=0;
	private Label lbl_blank=new Label();
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");

	public RawPurchaseorderRegister(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("SUPPLIER WISE PURCHASE ORDER REGISTER ::  "+ sessionBean.getCompany());
		cmbSupplierName.focus();
		Component ob[]={cmbSupplierName,fromDate,toDate,previewButton};
		new FocusMoveByEnter(this, ob);
		cmbSupplierNameDataAdd();
		allButtonAction();
	}

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("460px");
		mainLayout.setHeight("220px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("480px");
		setHeight("270px");

		// lblSection
		lblSupplierName = new Label();
		lblSupplierName.setImmediate(false);
		lblSupplierName.setWidth("-1px");
		lblSupplierName.setHeight("-1px");
		lblSupplierName.setValue("Supplier Name :");
		mainLayout.addComponent(lblSupplierName, "top:40.0px;left:51.0px;");

		// cmbSection
		cmbSupplierName = new ComboBox();
		cmbSupplierName.setImmediate(false);
		cmbSupplierName.setWidth("260px");
		cmbSupplierName.setHeight("24px");
		cmbSupplierName.setNullSelectionAllowed(false);
		cmbSupplierName.setNewItemsAllowed(false);
		cmbSupplierName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent( cmbSupplierName, "top:38.0px;left:142.0px;");




		lblformDate = new Label();
		lblformDate.setImmediate(false);
		lblformDate.setWidth("-1px");
		lblformDate.setHeight("-1px");
		lblformDate.setValue("From Date:");
		mainLayout.addComponent(lblformDate, "top:66.0px;left:78.0px;");

		fromDate= new PopupDateField();
		fromDate.setWidth("110px");
		fromDate.setDateFormat("dd-MM-yyyy");
		fromDate.setValue(new java.util.Date());
		fromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(fromDate, "top:64.0px;left:142.0px;");


		lbltormDate = new Label();
		lbltormDate.setImmediate(true);
		lbltormDate.setWidth("-1px");
		lbltormDate.setHeight("-1px");
		lbltormDate.setValue(" To Date :");
		mainLayout.addComponent(lbltormDate, "top:92.0px;left:90.0px;");

		toDate = new PopupDateField();
		toDate.setWidth("110px");
		toDate.setDateFormat("dd-MM-yyyy");
		toDate.setValue(new java.util.Date());
		toDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(toDate, "top:90.0px;left:142.0px;");
		
		chkpdf.setImmediate(true);
		chkpdf.setWidth("-1px");
		chkpdf.setHeight("-1px");
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:126.0px;left:145.0px;");
		
		chkother.setImmediate(true);
		chkother.setWidth("-1px");
		chkother.setHeight("-1px");
		mainLayout.addComponent(chkother, "top:126.0px;left:190.0px;");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("________________________________________________________________");
		mainLayout.addComponent(lblline, "top:150.0px;left:20.0px;");

		previewButton.setWidth("90px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:175.opx; left:160.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:175.opx; left:250.0px");

		return mainLayout;
	}

	private void cmbSupplierNameDataAdd()
	{
		cmbSupplierName.removeAllItems();
		Transaction tx = null;
		try {
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery("select a.supplierId,b.supplierName from tbRawPurchaseOrderInfo a inner join tbSupplierInfo b on a.supplierId=b.supplierId ").list();
			for (Iterator iter = list.iterator();iter.hasNext();) 
			{
				Object[] element = (Object[]) iter.next();
				cmbSupplierName.addItem(element[0].toString());
				cmbSupplierName.setItemCaption(element[0].toString(), element[1].toString());

			}
		}
		catch (Exception ex) 
		{
			System.out.print(ex);
		} 
	}

	private void allButtonAction()
	{
		cmbSupplierName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				//cmpEnbleDisable(true);
			}
		});

		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if(cmbSupplierName.getValue()!=null){
					reportShow();
				}
				else{
					getParent().showNotification("Select Supplier Name", Notification.TYPE_WARNING_MESSAGE);
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
	}
	private boolean queryCheck(String query,Session session){

		List list=session.createSQLQuery(query).list();
		if(list.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}

	private void reportShow()
	{
		String query=null;
		String activeFlag = null;
		
		if(chkpdf.booleanValue()==true)
			type=1;
		else
			type=0;

		String supplyID = cmbSupplierName.getItemCaption(cmbSupplierName.getValue());

		if(supplyID.equals("All")){
			supplyID="%";
		}else{
			supplyID = cmbSupplierName.getValue().toString();
		}

		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			String FromDate = new SimpleDateFormat("yyyy-MM-dd").format(fromDate.getValue());
			String ToDate =  new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue());

			System.out.println("From Date : "+FromDate+", To Date : "+ToDate);

			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("user", sessionBean.getUserName());

			hm.put("fromdate", new SimpleDateFormat("dd-MM-yyyy").format(fromDate.getValue()));
			hm.put("todate",new SimpleDateFormat("dd-MM-yyyy").format(toDate.getValue()));
			query="select * from [funcRawPurchaseOrderRegister]('"+FromDate+"','"+ToDate+"','"+supplyID+"') order by poDate";

			System.out.println(query);
			hm.put("sql", query);

			if(!queryCheck(query,session))
			{
				Window win = new ReportViewerNew(hm,"report/raw/rptpuschaseorderRegister.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
				win.setCaption("Report : R/M Purchase Order Register");
				this.getParent().getWindow().addWindow(win);
			}
			else{
				this.getParent().showNotification("There are no Data!!",Notification.TYPE_WARNING_MESSAGE);
			}
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}
}
