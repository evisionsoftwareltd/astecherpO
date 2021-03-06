package com.example.RecycledModuleReport;

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

public class RptRecycledStoreRejectQtyStockReport extends Window {
	private AbsoluteLayout mainLayout;

	private ComboBox cmbStoreName;

	private Label lblStoreName;
	private Label lblline;
	private Label lblAsOnDate;
	private Label lbltormDate;
	
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");
	
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private PopupDateField AsOnDate;


	private SessionBean sessionBean;
	boolean type=false;
	public RptRecycledStoreRejectQtyStockReport(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		setContent(buildMainLayout());
		this.setResizable(false);
		this.setCaption("STORE REJECT QTY STOCK REPORT ::  "+ sessionBean.getCompany());
		cmbStoreName.focus();
		Component ob[]={AsOnDate, cmbStoreName,  previewButton};
		new FocusMoveByEnter(this, ob);
		addStoreName();
		allButtonAction();
		
	}

	private void allButtonAction()
	{	
		
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if(cmbStoreName.getValue()!=null ){
					reportShow();
				}
				else{
					getParent().showNotification("Warning!","Please select item name.", Notification.TYPE_WARNING_MESSAGE);
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


	private void reportShow()
	{
		String query=null;
		String activeFlag = null;
		String storeName= "";

		if (cmbStoreName.getValue()!= null)
		{
			storeName = cmbStoreName.getValue().toString();
		}
		else
		{
			storeName="%";
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
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("AsOnDate", new SimpleDateFormat("dd-MM-yyyy").format(AsOnDate.getValue()));
			hm.put("StoreName", cmbStoreName.getItemCaption(cmbStoreName.getValue()));

			
				query= "select * from funRejectedProductStockStoreWise("
						+ " '"+new SimpleDateFormat("yyyy-MM-dd").format(AsOnDate.getValue())+"', "
						+ " '%', "
						+ " '"+storeName+"' ) ";					
					
			System.out.println(query);
			hm.put("sql", query);
			
			List lst=session.createSQLQuery(query).list();
			
			if(!lst.isEmpty())
			{
				Window win = new ReportViewer(hm,"report/production/RecycledModuleReport/rptStoreRejectQtyStockReport.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
				win.setCaption("Project Report :: "+sessionBean.getCompany());

				this.getParent().getWindow().addWindow(win);
				
			}
			
			else
			{
				
				getParent().showNotification("Date Not Found", Notification.TYPE_WARNING_MESSAGE);
				
			}
			
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}

	public void addStoreName()
	{
		cmbStoreName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			String query = "select Distinct iStoreId,vStoreName from tbRejectedOpening";
			List list = session.createSQLQuery(query).list();
			System.out.println("Section List:"+list.size());

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbStoreName.addItem(element[0].toString());
				cmbStoreName.setItemCaption(element[0].toString(), element[1].toString());
			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}

	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("520px");
		mainLayout.setHeight("220px");
		mainLayout.setMargin(false);

//		setWidth("480px");
//		setHeight("270px");

		lblStoreName = new Label("Store Name:");
		lblStoreName.setImmediate(false);
		lblStoreName.setWidth("-1px");
		lblStoreName.setHeight("-1px");
		mainLayout.addComponent(lblStoreName, "top:40.0px;left:30.0px;");
		mainLayout.addComponent(new Label(":"), "top:40.0px; left:90.0px;");

		cmbStoreName = new ComboBox();
		cmbStoreName.setImmediate(true);
		cmbStoreName.setWidth("300px");
		cmbStoreName.setHeight("24px");
		cmbStoreName.setNullSelectionAllowed(false);
		cmbStoreName.setNewItemsAllowed(false);
		mainLayout.addComponent( cmbStoreName, "top:38.0px;left:100.0px;");
		
		lblAsOnDate = new Label();
		lblAsOnDate.setImmediate(false);
		lblAsOnDate.setWidth("-1px");
		lblAsOnDate.setHeight("-1px");
		lblAsOnDate.setValue("As On Date");
		mainLayout.addComponent(lblAsOnDate, "top:68.0px;left:30.0px;");
		mainLayout.addComponent(new Label(":"), "top:68.0px; left:90.0px;");

		AsOnDate= new PopupDateField();
		AsOnDate.setWidth("109px");
		AsOnDate.setDateFormat("dd-MM-yyyy");
		AsOnDate.setValue(new java.util.Date());
		AsOnDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(AsOnDate, "top:66.0px;left:100.0px;");


		chkpdf.setValue(true);
		chkpdf.setImmediate(true);
		chkother.setImmediate(true);
		mainLayout.addComponent(chkpdf, "top:130.0px;left:180.0px;");
		mainLayout.addComponent(chkother, "top:130.0px;left:225.0px;");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("________________________________________________________________");
		mainLayout.addComponent(lblline, "top:150.0px;left:20.0px;");

		previewButton.setWidth("80px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:175.opx; left:150.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:175.opx; left:250.0px");

		return mainLayout;
	}

}
