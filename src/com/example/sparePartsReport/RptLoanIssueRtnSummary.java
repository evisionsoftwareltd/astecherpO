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

public class RptLoanIssueRtnSummary extends Window
{

	private AbsoluteLayout mainLayout;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private PopupDateField dAsonDate;
	private Label lblAsOnDate;
	private SessionBean sessionBean;

	public RptLoanIssueRtnSummary(SessionBean sessionBean,String str) 
	{
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("Loan Issue/Return Summary ::  "+ sessionBean.getCompany());
		allButtonAction();
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("460px");
		mainLayout.setHeight("160px");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("480px");
		setHeight("200px");
		

		lblAsOnDate = new Label();
		lblAsOnDate.setImmediate(false);
		lblAsOnDate.setWidth("-1px");
		lblAsOnDate.setHeight("-1px");
		lblAsOnDate.setValue("As On Date :");
		mainLayout.addComponent(lblAsOnDate, "top:40.0px;left:120.0px;");
			
		dAsonDate= new PopupDateField();
		dAsonDate.setWidth("107px");
		dAsonDate.setHeight("24px");
		dAsonDate.setDateFormat("dd-MM-yyyy");
		dAsonDate.setValue(new java.util.Date());
		dAsonDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dAsonDate, "top:38.0px;left:192.0px;");
	
		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("________________________________________________________________");
		mainLayout.addComponent(lblline, "top: 64.0px;left:20.0px;");
		
		//mainLayout.addComponent(button, "top:155.0px;left:200.0px;");
				
		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:100.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit.png"));
		mainLayout.addComponent(exitButton,"top:100.opx; left:250.0px");
		
		return mainLayout;
	}


	private void allButtonAction()
	{
	

		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				
			   reportShow();
		
			}
		});


		exitButton.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});
	
	}
	
	private void reportShow()
	{
		String query=null;
		String activeFlag = null;
		
		String productID ;
		String Date= new SimpleDateFormat("yyyy-MM-dd").format(dAsonDate.getValue())+ " "+"23:59:59";
		
		/*if(cmbproductName.getValue()!=null){
			productID=cmbproductName.getValue().toString();
		}else
		    {
			productID ="%";
		    }*/
		
		try{

			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			String asonDate = new SimpleDateFormat("yyyy-MM-dd").format(dAsonDate.getValue());

			
			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("asOnDate",new SimpleDateFormat("dd-MM-yyyy").format(dAsonDate.getValue()));
			
			query="select * from  LoanIssueRetunSummary('"+Date+"') order by partyId";
			
			System.out.println(query);
			hm.put("sql", query);


			Window win = new ReportViewer(hm,"report/raw/rptLoanIssueReturnSummary.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",true);

			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}
}
