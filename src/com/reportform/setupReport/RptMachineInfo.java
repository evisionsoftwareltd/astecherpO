package com.reportform.setupReport;

import java.text.SimpleDateFormat;
import java.util.Arrays;
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
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class RptMachineInfo extends Window 
{
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	private ComboBox cmbsupplierName ;
	@AutoGenerated
	private Label lblsupplierName;
	@AutoGenerated
	private ComboBox cmbMachineName;
	@AutoGenerated
	private Label lblmachineName;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SessionBean sessionBean;
	private CheckBox chkpdf,chkothers,chkAllSupplier,chkAllmachine;
	int type=0;
	private Label lblStatus;
	private OptionGroup opgStatus;
	private static final List<String> Optiontype=Arrays.asList(new String[]{"Active","Inactive"});
	
	public RptMachineInfo(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		cmbMachineName.focus();
		this.setCaption("Machine Information :: " + sessionBean.getCompany());
		Component ob[]={cmbMachineName,cmbsupplierName,previewButton};
		new FocusMoveByEnter(this,ob);
		addSupplierName();
		allButtonAction();
	}
	
	public void addSupplierName()
	{
		cmbsupplierName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			 
			String sql=	"select distinct  vSupplierId,vSupplierName from tbMachineInfo "
					    +"order by vSupplierName ";
			List list = session.createSQLQuery(sql).list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbsupplierName.addItem(element[0].toString());
				cmbsupplierName.setItemCaption(element[0].toString(), element[1].toString());

			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}
	
	public void machineData(String supplier)
	{
		cmbMachineName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			 
			String sql=	 "select distinct  vMachineCode,vMachineName from tbMachineInfo "
					     +"where vSupplierId like '"+supplier+"' ";
			List list = session.createSQLQuery(sql).list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbMachineName.addItem(element[0].toString());
				cmbMachineName.setItemCaption(element[0].toString(), element[1].toString());

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
				if(cmbsupplierName.getValue()!=null ||  chkAllSupplier.booleanValue())
				{
					if(cmbMachineName.getValue()!=null || chkAllmachine.booleanValue())
					{
						reportView();	
					}
					else
					{
						showNotification("Select Machine Name",Notification.TYPE_WARNING_MESSAGE);	
					}
					
				}else
				{
					showNotification("Select Supplier Name",Notification.TYPE_WARNING_MESSAGE);
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
		
		chkAllSupplier.addListener(new ValueChangeListener()
		{
			
			public void valueChange(ValueChangeEvent event)
			{
				
				if(chkAllSupplier.booleanValue())
				{
					
					cmbsupplierName.setEnabled(false);
					cmbsupplierName.setValue(null);
					machineData("%");
					
				}
				
				else
				{
					cmbsupplierName.setEnabled(true);
					cmbMachineName.removeAllItems();
					
				}
				
			}
		});
		
		
		chkAllmachine.addListener(new ValueChangeListener()
		{
			
			public void valueChange(ValueChangeEvent event)
			{
				
				if(chkAllmachine.booleanValue())
				{
					
					cmbMachineName.setEnabled(false);
					cmbMachineName.setValue(null);
					
					
				}
				
				else
				{
					cmbMachineName.setEnabled(true);	
				}
				
			}
		});
		
		
		
		cmbsupplierName.addListener( new ValueChangeListener() {
			
			
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbsupplierName.getValue()!=null)
				{
					String supplier=cmbsupplierName.getValue().toString();
					machineData(supplier);
				}
				
			}
		});
		
	}
	
/*	public void addSecReqNo()
	{
		if(cmbMachineName.getValue().toString()!=null){
			cmbsupplierName.removeAllItems();

			Transaction tx = null;
			try{
				Session session = SessionFactoryUtil.getInstance().getCurrentSession();
				tx = session.beginTransaction();
				List list = session.createSQLQuery("select * from tbRawRequisitionInfo where sectionId='"+cmbMachineName.getValue()+"' ").list();

				for(Iterator iter=list.iterator();iter.hasNext();)
				{
					Object[] element = (Object[]) iter.next();
					cmbsupplierName.addItem(element[3].toString());
					cmbsupplierName.setItemCaption(element[3].toString(), element[3].toString());
				}
			}catch(Exception exp){
				this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			}
		}
	}*/
	
	private void reportView()
	{		
		String supplierId,machineId;
		String status="Active";
		/*sectionId=cmbMachineName.getValue().toString().trim();
		storeReq=cmbsupplierName.getValue().toString().trim();
		System.out.print(sectionId);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String date = df.format(new Date())+" "+"23:59:59";*/

	
		
		if(cmbsupplierName.getValue()!=null)
		{
			supplierId=cmbsupplierName.getValue().toString();
		}
		else
		{
			supplierId="%";	
		}
		
		if(cmbMachineName.getValue()!=null)
		{
			machineId=cmbMachineName.getValue().toString();	
		}
		else
		{
			machineId="%";
		}
		
		String query=null;
		if(chkpdf.booleanValue()==true)
			type=1;
		else
			type=0;
		
		if(opgStatus.getValue().toString().equalsIgnoreCase("Inactive")){
			status="Inactive";
		}
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			//hm.put("phone", "Phone : "+sessionBean.get+" Fax : "+sessionBean.getCompanyFax()+" E-Mail : "+sessionBean.getCompanyEmail());
			//System.out.println(sessionBean.getCompanyPhone());
			hm.put("UserName", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("URL",getApplication().getURL().toString().replace("uptd/", ""));
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("parentType", "Machine Information");
			hm.put("Phone", sessionBean.getCompanyContact());
			
			///http://localhost:8080/report/uptd/Requisition/1.jpg
			
			System.out.println("URL: "+getApplication().getURL().toString().replace("uptd/", ""));

			//query="select * from funcRawRequisition ('"+date+"','"+sectionId+"','"+storeReq+"') ";
			
			 

			query=    "select  status,vMachineCode,vMachineName,vSupplierId, "
                      +"vSupplierName,vDeptId,vDeptName,machineSerial,machineModel,machineCapacity,dPurchaseDate,cast(vPurchaseAmount as money)vPurchaseAmount  from tbMachineInfo "
                      +"where vSupplierId like '"+supplierId+"' and vMachineCode like '"+machineId+"' and status ='"+status+"'  order by vSupplierName,vMachineName  ";
			
			System.out.println(query);
			hm.put("sql", query);

			/*Window win = new ReportViewerNew(hm,"report/raw/rptRequisitionForm.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);*/
			
			List list=session.createSQLQuery(query).list();
			if(!list.isEmpty()){
				Window win = new ReportViewerNew(hm,"report/RptMachineInfo.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);
			}
			else{
				this.getParent().showNotification("There are no Data!!",Notification.TYPE_WARNING_MESSAGE);
			}
			//Window win = new ReportViewer(hm,"report/rptSupplierDetails.jasper",this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false);
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error" +exp,Notification.TYPE_ERROR_MESSAGE);
			System.out.println(exp);
		}
	}
	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("440px");
		mainLayout.setHeight("200px");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("500px");
		setHeight("250px");
		
		// lblCategory
		lblsupplierName = new Label();
		lblsupplierName.setImmediate(false);
		lblsupplierName.setWidth("100.0%");
		lblsupplierName.setHeight("-1px");
		lblsupplierName.setValue("Supplier Name:");
		mainLayout.addComponent(lblsupplierName,"top:40.0px;left:48.0px;");
		
		// cmbCategory
		cmbsupplierName = new ComboBox();
		cmbsupplierName.setImmediate(true);
		cmbsupplierName.setWidth("260px");
		cmbsupplierName.setHeight("-1px");
		cmbsupplierName.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbsupplierName, "top:38.0px;left:140.0px;");
		
		chkAllSupplier=new CheckBox("All");
		chkAllSupplier.setImmediate(true);
		mainLayout.addComponent(chkAllSupplier, "top:38.0px; left:400.0px");
		
		// lblDepartment
		lblmachineName = new Label();
		lblmachineName.setImmediate(false);
		lblmachineName.setWidth("-1px");
		lblmachineName.setHeight("-1px");
		lblmachineName.setValue("Machine Name:");
		mainLayout.addComponent(lblmachineName, "top:66.0px;left:48.0px;");
		
		// cmbDepartment
		cmbMachineName = new ComboBox();
		cmbMachineName.setImmediate(true);
		cmbMachineName.setWidth("260px");
		cmbMachineName.setHeight("24px");
		cmbMachineName.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbMachineName, "top:64.0px;left:140.0px;");
		
		chkAllmachine=new CheckBox("All");
		chkAllmachine.setImmediate(true);
		mainLayout.addComponent(chkAllmachine, "top:64.0px; left:400.0px");
		
		lblStatus = new Label();
		lblStatus.setImmediate(false);
		lblStatus.setWidth("-1px");
		lblStatus.setHeight("-1px");
		lblStatus.setValue("Status: ");
		mainLayout.addComponent(lblStatus, "top:100.0px;left:48.0px;");
	
		opgStatus=new OptionGroup("",Optiontype);
		opgStatus.setImmediate(true);
		opgStatus.setValue("Active");
		opgStatus.setStyleName("horizontal");
		mainLayout.addComponent(opgStatus, "top:100.0px;left:190.0px;");
		
		
		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:120.0px; left:182.0px");
		
		chkothers=new CheckBox("Others");
		chkothers.setImmediate(true);
		mainLayout.addComponent(chkothers, "top:120.0px; left:240.0px");
		
		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("__________________________________________________________________________________");
		mainLayout.addComponent(lblline, "top:140.0px;left:20.0px;");
		
		//mainLayout.addComponent(button, "top:155.0px;left:200.0px;");
		previewButton.setWidth("100px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:170.0px; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:170.0px; left:260.0px");
		
		return mainLayout;
	}
}