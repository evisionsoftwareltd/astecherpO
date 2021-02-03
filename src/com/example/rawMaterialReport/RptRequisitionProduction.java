package com.example.rawMaterialReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

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
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class RptRequisitionProduction extends Window 
{
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	private ComboBox cmbReqNo ;
	@AutoGenerated
	private Label lblReqNo;
	@AutoGenerated
	private ComboBox cmbProductionType;
	@AutoGenerated
	private Label lblProductionType;
	private Label lblline,lblFromDate,lblToDate;
	private PopupDateField dFromDate,dToDate;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SessionBean sessionBean;
	private CheckBox chkpdf,chkothers;
	int type=0;

	public RptRequisitionProduction(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		
		setContent(buildMainLayout());
		this.setResizable(false);
		//cmbProductionType.focus();
		this.setCaption("REQUISITION WISE ISSUE  :: " + sessionBean.getCompany());
		Component ob[]={cmbProductionType,cmbReqNo,previewButton};
		new FocusMoveByEnter(this,ob);
		allButtonAction();
		 ReqLoadData();
	}

	private void allButtonAction()
	{
		exitButton.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});

		previewButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				if( cmbReqNo.getValue()!=null){
					reportView();
				}else{
					showNotification("Select All Fields",Notification.TYPE_WARNING_MESSAGE);
				}


			}
		});
		
		chkpdf.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkpdf.booleanValue())
				{

					chkothers.setValue(false);
					type=1;

				}

				else
				{

					chkothers.setValue(true);
					type=0;

				}

			}
		});

		chkothers.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkothers.booleanValue())
				{

					chkpdf.setValue(false);
					type=0;

				}

				else
				{

					chkpdf.setValue(true);
					type=1;

				}

			}
		});
		dFromDate.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {

				 ReqLoadData();
			}
		});
		dToDate.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {

				 ReqLoadData();
			}
		});
	}
	private Iterator dbService(String sql){
		Transaction tx=null;
		Session session=null;
		try{
			session=SessionFactoryUtil.getInstance().openSession();
			tx=session.beginTransaction();
			return session.createSQLQuery(sql).list().iterator();
		}
		catch(Exception exp){
			showNotification(exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
		finally{
			if(tx!=null||session!=null){
				session.close();
			}
		}
		return null;
	}
	private void ReqLoadData(){
		cmbReqNo.removeAllItems();
		String sql= "select distinct ReqNo from tbMouldSectionReceiptInfo where CONVERT(date,IssueDate,105) between " +
				"'"+new SimpleDateFormat("yyyy-MM-dd").format(dFromDate.getValue())+"' " +
						"and '"+new SimpleDateFormat("yyyy-MM-dd").format(dToDate.getValue())+"'";
		Iterator iter=dbService(sql);
		while(iter.hasNext()){
			cmbReqNo.addItem(iter.next());
		}
	}


	private void reportView()
	{		
		String  storeReq;
		//storeReq=cmbReqNo.getValue().toString().trim();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String date = df.format(new Date())+" "+"23:59:59";

		String query=null;
		if(chkpdf.booleanValue()==true)
			type=1;
		else
			type=0;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("Phone", sessionBean.getCompanyContact());
			hm.put("UserName", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("user", sessionBean.getUserName());
			hm.put("type","" );
			
			query = "select * from funReqWiseIssue('"+cmbReqNo.getValue()+"')";
			
			hm.put("sql", query);
		List list=session.createSQLQuery(query).list();
			if(!list.isEmpty()){
				Window win = new ReportViewerNew(hm,"report/production/RptRequisitionWiseIssue.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",1);
				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);

			}
			else{
				this.getParent().showNotification("There are no Data!!",Notification.TYPE_WARNING_MESSAGE);
			}
		}
		catch(Exception exp){
			showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() 
	{

		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("470px");
		mainLayout.setHeight("230px");
		mainLayout.setMargin(false);

		lblFromDate = new Label("From Date: ");
		lblFromDate.setImmediate(true);
		lblFromDate.setWidth("100.0%");
		lblFromDate.setHeight("18px");
		mainLayout.addComponent(lblFromDate, "top:40.0px;left:48.0px;");

		dFromDate = new PopupDateField();
		dFromDate.setImmediate(true);
		dFromDate.setWidth("110px");
		dFromDate.setDateFormat("dd-MM-yyyy");
		dFromDate.setValue(new java.util.Date());
		dFromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dFromDate, "top:38.0px;left:150.0px;");

		lblToDate = new Label("To Date: ");
		lblToDate.setImmediate(true);
		lblToDate.setWidth("100.0%");
		lblToDate.setHeight("18px");
		mainLayout.addComponent(lblToDate, "top:70.0px;left:48.0px;");

		dToDate = new PopupDateField();
		dToDate.setImmediate(true);
		dToDate.setWidth("110px");
		dToDate.setDateFormat("dd-MM-yyyy");
		dToDate.setValue(new java.util.Date());
		dToDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dToDate, "top:68.0px;left:150.0px;");


		lblReqNo = new Label("Req No: ");
		lblReqNo.setImmediate(false);
		lblReqNo.setWidth("100.0%");
		lblReqNo.setHeight("-1px");
		lblReqNo.setValue("Req No:");
		mainLayout.addComponent(lblReqNo,"top:100.0px;left:48.0px;");

		cmbReqNo = new ComboBox();
		cmbReqNo.setImmediate(true);
		cmbReqNo.setWidth("260px");
		cmbReqNo.setHeight("-1px");
		cmbReqNo.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbReqNo, "top:98.0px;left:150.0px;");

		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:140.0px; left:175.0px");

		chkothers=new CheckBox("Others");
		chkothers.setImmediate(true);
		mainLayout.addComponent(chkothers, "top:140.0px; left:220.0px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("______________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:155.0px;left:0.0px;");

		previewButton.setWidth("80px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:180.0px; left:155.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:180.0px; left:235.0px");

		return mainLayout;
	}
}
