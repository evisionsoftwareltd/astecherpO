package com.example.sparePartsReport;

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
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class RawRequisitionForHeadOffice extends Window 
{
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	private ComboBox cmbDepartmentReqNo ;
	@AutoGenerated
	private Label lblDepartmentReqNo;
	@AutoGenerated
	private ComboBox cmbDepartment;
	@AutoGenerated
	private Label lblDepartment;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SessionBean sessionBean;

	public RawRequisitionForHeadOffice(SessionBean sessionBean,String str) 
	{
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		cmbDepartment.focus();
		this.setCaption("REQUISITION FORM  FOR HEAD OFFICE :: " + sessionBean.getCompany());
		Component ob[]={cmbDepartment,cmbDepartmentReqNo,previewButton};
		new FocusMoveByEnter(this,ob);
		addSectionName();
		allButtonAction();
	}

	public void addSectionName()
	{
		cmbDepartment.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery("select * from tbDepartmentInfo").list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbDepartment.addItem(element[1].toString());
				cmbDepartment.setItemCaption(element[1].toString(), element[2].toString());

			}
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
				// TODO Auto-generated method stub
				if(cmbDepartment.getValue()!=null && cmbDepartmentReqNo.getValue()!=null){
					reportView();
				}else{
					showNotification("Select All Fields",Notification.TYPE_WARNING_MESSAGE);
				}


			}
		});

		cmbDepartment.addListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				addDepartmentReqNo();

			}
		});
	}

	public void addDepartmentReqNo()
	{
		if(cmbDepartment.getValue().toString()!=null){
			cmbDepartmentReqNo.removeAllItems();

			Transaction tx = null;
			try{
				Session session = SessionFactoryUtil.getInstance().getCurrentSession();
				tx = session.beginTransaction();
				List list = session.createSQLQuery("select * from tbRawRequisitionInfo where sectionId='"+cmbDepartment.getValue()+"' ").list();

				for(Iterator iter=list.iterator();iter.hasNext();)
				{
					Object[] element = (Object[]) iter.next();
					cmbDepartmentReqNo.addItem(element[3].toString());
					cmbDepartmentReqNo.setItemCaption(element[3].toString(), element[3].toString());
				}
			}catch(Exception exp){
				this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			}
		}
	}

	private void reportView()
	{		
		String sectionId,masterReq,storeReq;
		sectionId=cmbDepartment.getValue().toString().trim();
		storeReq=cmbDepartmentReqNo.getValue().toString().trim();
		System.out.print(sectionId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date())+" "+"23:59:59";

		if(sectionId=="All")
		{
			sectionId="%";
		}

		if(storeReq=="All")
		{
			storeReq="%";
		}

		System.out.println(sectionId);
		String query=null;

		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			//hm.put("phone", "Phone : "+sessionBean.getCompanyPhone()+" Fax : "+sessionBean.getCompanyFax()+" E-Mail : "+sessionBean.getCompanyEmail());
			//System.out.println(sessionBean.getCompanyPhone());
			hm.put("UserName", sessionBean.getUserName()+"  "+sessionBean.getUserIp());

			query="select * from funcRawRequisition ('"+date+"','"+sectionId+"','"+storeReq+"') ";
			System.out.println(query);
			hm.put("sql", query);

			Window win = new ReportViewerNew(hm,"report/raw/rptRequisitionforHeadOffice.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",0);
			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);
			//Window win = new ReportViewer(hm,"report/rptSupplierDetails.jasper",this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false);
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error",Notification.TYPE_ERROR_MESSAGE);
			System.out.println(exp);
		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("410px");
		mainLayout.setHeight("200px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("470px");
		setHeight("240px");

		// lblDepartment
		lblDepartment = new Label();
		lblDepartment.setImmediate(false);
		lblDepartment.setWidth("-1px");
		lblDepartment.setHeight("-1px");
		lblDepartment.setValue("Department :");
		mainLayout.addComponent(lblDepartment, "top:40.0px;left:48.0px;");

		// cmbDepartment
		cmbDepartment = new ComboBox();
		cmbDepartment.setImmediate(false);
		cmbDepartment.setWidth("260px");
		cmbDepartment.setHeight("24px");
		cmbDepartment.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbDepartment, "top:38.0px;left:120.0px;");

		// lblDepartmentReqNo
		lblDepartmentReqNo = new Label();
		lblDepartmentReqNo.setImmediate(false);
		lblDepartmentReqNo.setWidth("100.0%");
		lblDepartmentReqNo.setHeight("-1px");
		lblDepartmentReqNo.setValue("Dept Req No:");
		mainLayout.addComponent(lblDepartmentReqNo,"top:66.0px;left:44.0px;");

		// cmbDepartmentReqNo
		cmbDepartmentReqNo = new ComboBox();
		cmbDepartmentReqNo.setImmediate(false);
		cmbDepartmentReqNo.setWidth("260px");
		cmbDepartmentReqNo.setHeight("-1px");
		cmbDepartmentReqNo.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbDepartmentReqNo, "top:64.0px;left:120.0px;");


		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("__________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:100.0px;left:20.0px;");

		//mainLayout.addComponent(button, "top:155.0px;left:200.0px;");
		previewButton.setWidth("80px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:135.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:135.opx; left:250.0px");

		return mainLayout;
	}
}