package com.example.productionReport;

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

public class RptIssueToLabelingPrinting extends Window 
{
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	private ComboBox cmbIssueNo ;
	@AutoGenerated
	private Label lblIssueNo;
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

	public RptIssueToLabelingPrinting(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		//cmbProductionType.focus();
		this.setCaption("ISSUE REPORT   :: " + sessionBean.getCompany());
		Component ob[]={cmbProductionType,cmbIssueNo,previewButton};
		new FocusMoveByEnter(this,ob);
		//cmbProductionTypeDataLoad();
		allButtonAction();
		IssueNoLoadData();
	}

	public void cmbProductionTypeDataLoad()
	{
		cmbProductionType.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery("select productTypeId,productTypeName from tbProductionType where productTypeId in('PT-1','PT-2','PT-4') order by productTypeName").list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbProductionType.addItem(element[0].toString());
				cmbProductionType.setItemCaption(element[0].toString(), element[1].toString());

			}
			cmbProductionType.addItem("Printing");
			cmbProductionType.addItem("Labeling");
			cmbProductionType.addItem("Lacqure");
			cmbProductionType.addItem("Cap Folding");
			cmbProductionType.addItem("Stretch Blow Molding");
			
			
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
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
				if( cmbIssueNo.getValue()!=null){


					reportView();

				}else{
					showNotification("Select Req No",Notification.TYPE_WARNING_MESSAGE);
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

				IssueNoLoadData();
			}
		});
		dToDate.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {

				IssueNoLoadData();
			}
		});
		cmbProductionType.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				IssueNoLoadData();
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
	private void IssueNoLoadData(){
		String productionType="%";
		if(cmbProductionType.getValue()!=null){
			productionType=cmbProductionType.getValue().toString();
		}
		cmbIssueNo.removeAllItems();
		String sql="";
		if(productionType.equalsIgnoreCase("Lacqure")){
			sql="select issueNo from tbIssueToLacqureInfo  where issueTo='"+productionType+"' and convert(date,issueDate,105)" +
					" between '"+new SimpleDateFormat("yyyy-MM-dd").format(dFromDate.getValue())+"' " +
					" and '"+new SimpleDateFormat("yyyy-MM-dd").format(dToDate.getValue())+"' ";
		}
		else if(productionType.equalsIgnoreCase("Stretch Blow Molding")){
			sql="select issueNo from tbIssueToSBMInfo where issueTo = 'SBM' and CONVERT(date,issueDate,105)"+
					" between '"+new SimpleDateFormat("yyyy-MM-dd").format(dFromDate.getValue())+"' " +
					" and '"+new SimpleDateFormat("yyyy-MM-dd").format(dToDate.getValue())+"' ";
		}
		else{
			sql="select issueNo from tbIssueToLabelPrintingInfo  where issueTo='"+productionType+"' and convert(date,issueDate,105)" +
					" between '"+new SimpleDateFormat("yyyy-MM-dd").format(dFromDate.getValue())+"' " +
					" and '"+new SimpleDateFormat("yyyy-MM-dd").format(dToDate.getValue())+"' ";
		}

		

		Iterator iter=dbService(sql);
		while(iter.hasNext()){
			cmbIssueNo.addItem(iter.next());
		}
	}
	

	private void reportView()
	{		
		String storeReq;
		storeReq=cmbIssueNo.getValue().toString().trim();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date())+" "+"23:59:59";


		if(storeReq=="All")
		{
			storeReq="%";
		}

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
			hm.put("SUBREPORT_DIR", "./report/production/");

			if(cmbProductionType.getValue().toString().equalsIgnoreCase("Lacqure")){
				query = "select a.issueTo,a.batchNo,a.ReqNo,a.reqDate,a.issueNo,a.issueDate,b.productName," +
						"b.unit,b.ReqQty, b.issueQty,b.remarks from tbIssueToLacqureInfo a inner join " +
						"tbIssueToLacqureDetails b on a.issueNo=b.issueNo where a.issueNo='"+cmbIssueNo.getValue()+"'";
			}
			else if(cmbProductionType.getValue().toString().equalsIgnoreCase("Stretch Blow Molding")){
				query = "select a.issueTo,a.batchNo,a.ReqNo,a.reqDate,a.issueNo,a.issueDate,b.productName, "
						+ "b.unit,b.ReqQty,b.issueQty,b.remarks "
						+ "from tbIssueToSBMInfo a inner join tbIssueToSBMDetails b on a.issueNo=b.issueNo "
						+ "where a.issueNo='"+cmbIssueNo.getValue()+"'";
			}
			else{
				query = "select a.issueTo,a.batchNo,a.ReqNo,a.reqDate,a.issueNo,a.issueDate,b.productName," +
						"b.unit,b.ReqQty, b.issueQty,b.remarks from tbIssueToLabelPrintingInfo a inner join " +
						"tbIssueToLabelPrintingDetails b on a.issueNo=b.issueNo where a.issueNo='"+cmbIssueNo.getValue()+"'";
			}
			



			hm.put("sql", query);



			List list=session.createSQLQuery(query).list();
			if(!list.isEmpty()){
				Window win = new ReportViewerNew(hm,"report/production/RptIssueReport.jasper",
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
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("410px");
		mainLayout.setHeight("240px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("470px");
		setHeight("280px");


		lblProductionType = new Label();
		lblProductionType.setImmediate(false);
		lblProductionType.setWidth("-1px");
		lblProductionType.setHeight("-1px");
		lblProductionType.setValue("Production Type :");
		mainLayout.addComponent(lblProductionType, "top:10.0px;left:48.0px;");

		// cmbProductionType
		cmbProductionType = new ComboBox();
		cmbProductionType.setImmediate(true);
		cmbProductionType.setWidth("260px");
		cmbProductionType.setHeight("24px");
		cmbProductionType.setNullSelectionAllowed(false);
		cmbProductionType.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		cmbProductionType.addItem("Dry Offset Printing");
		cmbProductionType.addItem("Screen Printing");
		cmbProductionType.addItem("Heat Trasfer Label");
		cmbProductionType.addItem("Manual Printing");
		cmbProductionType.addItem("Labeling");
		cmbProductionType.addItem("Lacqure");
		cmbProductionType.addItem("Cap Folding");
		cmbProductionType.addItem("Stretch Blow Molding");
		cmbProductionType.addItem("Shrink");
		mainLayout.addComponent(cmbProductionType, "top:8.0px;left:150.0px;");

		// lblDate
		lblFromDate = new Label("From Date: ");
		lblFromDate.setImmediate(true);
		lblFromDate.setWidth("100.0%");
		lblFromDate.setHeight("18px");
		mainLayout.addComponent(lblFromDate, "top:40.0px;left:48.0px;");

		//Declare Date
		dFromDate = new PopupDateField();
		dFromDate.setImmediate(true);
		dFromDate.setWidth("110px");
		dFromDate.setDateFormat("dd-MM-yyyy");
		dFromDate.setValue(new java.util.Date());
		dFromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dFromDate, "top:38.0px;left:150.0px;");

		// lblDate
		lblToDate = new Label("To Date: ");
		lblToDate.setImmediate(true);
		lblToDate.setWidth("100.0%");
		lblToDate.setHeight("18px");
		mainLayout.addComponent(lblToDate, "top:70.0px;left:48.0px;");

		//Declare Date
		dToDate = new PopupDateField();
		dToDate.setImmediate(true);
		dToDate.setWidth("110px");
		dToDate.setDateFormat("dd-MM-yyyy");
		dToDate.setValue(new java.util.Date());
		dToDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dToDate, "top:68.0px;left:150.0px;");

		/*// lblProductionType
		lblProductionType = new Label();
		lblProductionType.setImmediate(false);
		lblProductionType.setWidth("-1px");
		lblProductionType.setHeight("-1px");
		lblProductionType.setValue("Production Type :");
		mainLayout.addComponent(lblProductionType, "top:100.0px;left:48.0px;");

		// cmbProductionType
		cmbProductionType = new ComboBox();
		cmbProductionType.setImmediate(true);
		cmbProductionType.setWidth("260px");
		cmbProductionType.setHeight("24px");
		cmbProductionType.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbProductionType, "top:98.0px;left:150.0px;");*/

		// lblCategory
		lblIssueNo = new Label();
		lblIssueNo.setImmediate(false);
		lblIssueNo.setWidth("100.0%");
		lblIssueNo.setHeight("-1px");
		lblIssueNo.setValue("Issue No:");
		mainLayout.addComponent(lblIssueNo,"top:100.0px;left:48.0px;");

		// cmbCategory
		cmbIssueNo = new ComboBox();
		cmbIssueNo.setImmediate(true);
		cmbIssueNo.setWidth("260px");
		cmbIssueNo.setHeight("-1px");
		cmbIssueNo.setNullSelectionAllowed(false);
		cmbIssueNo.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbIssueNo, "top:98.0px;left:150.0px;");

		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:160.0px; left:145.0px");

		chkothers=new CheckBox("Others");
		chkothers.setImmediate(true);
		mainLayout.addComponent(chkothers, "top:160.0px; left:200.0px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("__________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:175.0px;left:20.0px;");

		//mainLayout.addComponent(button, "top:155.0px;left:200.0px;");
		previewButton.setWidth("80px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:205.opx; left:165.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:205.opx; left:250.0px");

		return mainLayout;
	}
}