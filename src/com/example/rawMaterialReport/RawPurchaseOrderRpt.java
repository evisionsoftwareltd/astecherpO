package com.example.rawMaterialReport;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class RawPurchaseOrderRpt extends Window {

	private AbsoluteLayout mainLayout;
	private ComboBox cmbDate;
	private Label lblDate;
	private ComboBox cmbSupplierName;
	private Label lblSupplierName;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SessionBean sessionBean;

	private HorizontalLayout chklayout=new HorizontalLayout();
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");
	int type=0;

	public RawPurchaseOrderRpt(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("PURCHASE ORDER :: "+ sessionBean.getCompany());
		cmbSupplierNameDataAdd();
		allButtonAction();
		cmbSupplierName.focus();
		Component ob[]={cmbSupplierName,cmbDate,previewButton};
		new FocusMoveByEnter(this, ob);
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
				cmpEnbleDisable(true);
				cmdDateAdding();
			}
		});

		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if(cmbSupplierName.getValue()!=null){
					if(cmbDate.getValue()!=null){
						reportShow();
					}
					else{
						getParent().showNotification("Select Purchase Order Date", Notification.TYPE_HUMANIZED_MESSAGE);
					}
				}
				else{
					getParent().showNotification("Select Supplier Name", Notification.TYPE_HUMANIZED_MESSAGE);
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

	private void cmdDateAdding()
	{
		cmbDate.removeAllItems();
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();

		String query = "SELECT supplierId,convert(varchar,poDate,105) orderDate FROM tbRawPurchaseOrderInfo WHERE supplierId='"+cmbSupplierName.getValue()+"'";
		System.out.println(query);

		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbDate.addItem(element[1].toString());
			cmbDate.setItemCaption(element[1], element[1].toString());
		}
	}

	private void cmpEnbleDisable(boolean tf)
	{
		System.out.println(tf);
		previewButton.setEnabled(tf);
	}

	private void reportShow()
	{
		String query=null;
		String activeFlag = null;

		if(chkpdf.booleanValue()==true)
			type=1;
		else
			type=0;

		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			//hm.put("phone", "Phone: "+sessionBean.getCompanyPhone()+"   Fax:  "+sessionBean.getCompanyFax()+",   E-mail:  "+sessionBean.getCompanyEmail());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("logo",sessionBean.getCompanyLogo());
			hm.put("user", sessionBean.getUserName());

			query="Select * from vwPurchaseOrder  where poDate=convert(date,'"+cmbDate.getItemCaption(cmbDate.getValue())+"',105) and supplierId='"+cmbSupplierName.getValue()+"'";
			System.out.println(query);
			hm.put("sql", query);

			Window win = new ReportViewerNew(hm,"report/raw/rptRawPurchaseOrder.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
			win.setCaption("Report : Purchase Order");
			this.getParent().getWindow().addWindow(win);
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("520px");
		mainLayout.setHeight("160px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("525px");
		setHeight("220px");

		// lblSupplierName
		lblSupplierName = new Label();
		lblSupplierName.setImmediate(false);
		lblSupplierName.setWidth("-1px");
		lblSupplierName.setHeight("-1px");
		lblSupplierName.setValue("Supplier Name :");
		mainLayout.addComponent(lblSupplierName, "top:20.0px;left:61.0px;");

		// cmbSupplierName
		cmbSupplierName = new ComboBox();
		cmbSupplierName.setImmediate(false);
		cmbSupplierName.setWidth("267px");
		cmbSupplierName.setHeight("24px");
		cmbSupplierName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbSupplierName, "top:20.0px;left:153.0px;");

		// lblDate
		lblDate = new Label();
		lblDate.setImmediate(false);
		lblDate.setWidth("100%");
		lblDate.setHeight("-1px");
		lblDate.setValue("Date :");
		mainLayout.addComponent(lblDate, "top:45.0px; left:110.0px");

		// comboBox_1
		cmbDate = new ComboBox();
		cmbDate.setImmediate(true);
		cmbDate.setNullSelectionAllowed(true);
		cmbDate.setNewItemsAllowed(true);
		cmbDate.setWidth("127px");
		cmbDate.setHeight("24px");
		cmbDate.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbDate, "top:46.0px;left:153.0px;");

		chkpdf.setValue(true);
		chkpdf.setImmediate(true);
		chkother.setImmediate(true);
		chklayout.addComponent(chkpdf);
		chklayout.addComponent(chkother);
		mainLayout.addComponent(chklayout, "top:75.0px; left:153.0px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("____________________________________________________________________");
		mainLayout.addComponent(lblline, "top:100.0px;left:20.0px;");



		previewButton.setWidth("90px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:130.opx; left:165.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:130.opx; left:255.0px");

		return mainLayout;
	}

}
