package com.reportform.vehicleReport;

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
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

public class RptDateWiseVehicleMainten extends Window
{
	private AbsoluteLayout mainLayout;
	SessionBean sessionBean;
	
	CommonButton cButton=new CommonButton("", "", "", "", "", "", "", "Preview", "", "Exit");
	
	private static final String[] strType = new String[]{"Regular","Maintenance","Servicing"};
	
	private Label lblFromDate;
	private Label lblToDate;
	private Label lblVehicleRegNo;
	private Label lblMainten;
	
	private ComboBox cmbVehicleRegistration;
	private ComboBox cmbMainTenType;
	
	private CheckBox chkVehicleReg;
	private CheckBox chkVehicleMaintenType;
	
	private PopupDateField dFromDate;
	private PopupDateField dToDate;
	
	private String unitId="";
	private String subUnitId ="";
	private String vehicleRegNo="";
	private String vehicleMainten="";

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat datef = new SimpleDateFormat("dd-MM-yyyy");
	
	private ArrayList<Component> allComp = new ArrayList<Component>();
	
	public RptDateWiseVehicleMainten(SessionBean sessionBean)
	{
		this.sessionBean = sessionBean;
		this.setCaption("SATE WISE VEHICLE MAINTENANCE :: "+sessionBean.getCompany());
		this.setResizable(false);
		
		buildMainlayout();
		setContent(mainLayout);
		
		loadVehicleRegistration();
		
		setBtnAction();
		focusMove();
	}
	
	private void setBtnAction()
	{
		cButton.btnPreview.addListener(new ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				formValidation();
			}
		});
		
		cButton.btnExit.addListener(new ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});
		
		chkVehicleReg.addListener(new ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				allVehicleReg();
			}
		});
		
		chkVehicleMaintenType.addListener(new ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				allVehicleMainten();
			}
		});
	}
	
	private void allVehicleReg()
	{
		if(chkVehicleReg.getValue().equals(true))
		{
			cmbVehicleRegistration.setEnabled(false);
			cmbVehicleRegistration.setValue(null);
		}
		else
		{
			cmbVehicleRegistration.setEnabled(true);
		}
	}
	
	private void allVehicleMainten()
	{
		if(chkVehicleMaintenType.getValue().equals(true))
		{
			cmbMainTenType.setEnabled(false);
			cmbMainTenType.setValue(null);
		}
		else
		{
			cmbMainTenType.setEnabled(true);
		}
	}
		
	private void focusMove()
	{
		allComp.add(cmbVehicleRegistration);
		allComp.add(dFromDate);
		allComp.add(dToDate);
		allComp.add(cmbMainTenType);
		allComp.add(cButton.btnPreview);
		
		new FocusMoveByEnter(this,allComp);
	}
	
	private void formValidation()
	{
		if(cmbVehicleRegistration.getValue()!=null || chkVehicleReg.booleanValue()==true)
		{
			if(cmbMainTenType.getValue()!=null || chkVehicleMaintenType.booleanValue()==true)
			{
				readyReport();
			}
			else
			{
				getParent().showNotification("Warning", "Select Vehicle Maintentype");
			}
		}
		else
		{
			getParent().showNotification("Warning", "Select Vehicle Registration No");
		}
	}
	
	private void loadVehicleRegistration()
	{
		cmbVehicleRegistration.removeAllItems();
		try
		{
			Transaction tx;
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		
			tx = session.beginTransaction();
			
			String query="select vehicleId,regNumber from tbVehicleInfo order by vehicleId";
			
			System.out.println("Vehicle Query: "+query);

			List list = session.createSQLQuery(query).list();

			for(Iterator iter = list.iterator(); iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();

				cmbVehicleRegistration.addItem(element[0]+"#");
				cmbVehicleRegistration.setItemCaption(element[0]+"#", element[1].toString());
			}
		}
		catch(Exception ex)
		{
			System.out.print("Hi"+ex);
		}
	}
	
	private void readyReport()
	{
		if(chkVehicleReg.booleanValue()==true)
		{
			vehicleRegNo = "%";
		}
		else
		{
			vehicleRegNo=cmbVehicleRegistration.getValue().toString().replaceAll("#", "");
		}
		
		if(chkVehicleMaintenType.booleanValue()==true)
		{
			vehicleMainten = "%";
		}
		else
		{
			vehicleMainten=cmbMainTenType.getValue().toString().replaceAll("#", "");
		}
		generateReport();
	}
	
	private void generateReport()
	{
		String query=null;
		
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phoneFaxEmail", sessionBean.getCompanyContact());
			hm.put("userName", sessionBean.getUserName());
			hm.put("userIp", sessionBean.getUserIp());
//			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("fromDate", datef.format(dFromDate.getValue()));
			hm.put("toDate", datef.format(dToDate.getValue()));
			
			query = " select a.entryDate,c.regNumber,b.expenseHead,b.quantity,b.unit,b.rate,b.amount,b.billNo," +
				    " b.supplier,a.maintenType,b.endMeterRead,b.runningMilage,b.costPerMile,b.lastRepDate," +
				    " b.totalAmount,b.lastSupplier,b.remarks from tbVehicleMainten as a inner join tbVehicleMaintenTable" +
				    " as b on a.maintenId=b.maintenId left join tbVehicleInfo as c on a.vehicleRegistrationNo=c.vehicleId" +
				    " where a.vehicleRegistrationNo like '"+vehicleRegNo+"' and a.maintenType like '"+vehicleMainten+"' and" +
				    " a.entryDate between '"+dateFormat.format(dFromDate.getValue())+" 00:00:00' and" +
				    " '"+dateFormat.format(dToDate.getValue())+" 23:59:59' ORDER by a.maintenId ";
			
			System.out.println("Report query : "+query);
			
			if(queryValueCheck(query))
			{
				hm.put("sql", query);
			
				Window win = new ReportViewer(hm,"report/account/vehicleModule/rptDateWiseVehicleMainten.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",false);
			
				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);
			}
			else
			{
				showNotification("Warning","There are no Data",Notification.TYPE_WARNING_MESSAGE);
			}
		}
		
		catch(Exception exp)
		{
			this.getParent().showNotification("Error",exp.toString(),Notification.TYPE_ERROR_MESSAGE);
			System.out.println(exp);
		}
	}
	
	private boolean queryValueCheck(String sql)
	{
		Transaction tx = null;

		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			Iterator iter = session.createSQLQuery(sql).list().iterator();

			if (iter.hasNext()) 
			{
				return true;
			}
		} 
		catch (Exception ex) 
		{
			System.out.print(ex);
		}
		return false;
	}
	
	private AbsoluteLayout buildMainlayout()
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("420px");
		setHeight("270px");

		lblVehicleRegNo = new Label("Vehicle Reg. No :");
		lblVehicleRegNo.setImmediate(true);
		lblVehicleRegNo.setWidth("100px");
		lblVehicleRegNo.setHeight("-1px");
		mainLayout.addComponent(lblVehicleRegNo, "top:30px; left:25px;");
		
		cmbVehicleRegistration = new ComboBox();
		cmbVehicleRegistration.setImmediate(true);
		cmbVehicleRegistration.setWidth("200px");
		cmbVehicleRegistration.setHeight("-1px");
		mainLayout.addComponent(cmbVehicleRegistration,"top:27px; left:135px;");
		
		chkVehicleReg = new CheckBox("All");
		chkVehicleReg.setImmediate(true);
		mainLayout.addComponent(chkVehicleReg, "top:28px; left:340px;");
		
		lblFromDate = new Label("Date :");
		lblFromDate.setImmediate(false);
		lblFromDate.setWidth("100px");
		lblFromDate.setHeight("-1px");
		mainLayout.addComponent(lblFromDate, "top:70px; left:25px;");
		
		dFromDate = new PopupDateField();
		dFromDate.setImmediate(true);
		dFromDate.setHeight("-1px");
		dFromDate.setWidth("110px");
		dFromDate.setDateFormat("dd-MM-yyyy");
		dFromDate.setValue(new java.util.Date());
		dFromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dFromDate, "top:67px; left:135px;");
		
		lblToDate = new Label("To");
		lblToDate.setImmediate(false);
		lblToDate.setWidth("20px");
		lblToDate.setHeight("-1px");
		mainLayout.addComponent(lblToDate, "top:70px; left:250px;");
		
		dToDate = new PopupDateField();
		dToDate.setImmediate(true);
		dToDate.setHeight("-1px");
		dToDate.setWidth("110px");
		dToDate.setDateFormat("dd-MM-yyyy");
		dToDate.setValue(new java.util.Date());
		dToDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dToDate, "top:67px; left:267px;");
				
		lblMainten = new Label("Maintenance Type :");
		lblMainten.setImmediate(true);
		lblMainten.setWidth("120px");
		lblMainten.setHeight("-1px");
		mainLayout.addComponent(lblMainten, "top:110px; left:25px;");
		
		cmbMainTenType = new ComboBox();
		cmbMainTenType.setImmediate(true);
		cmbMainTenType.setWidth("150px");
		cmbMainTenType.setHeight("-1px");
		mainLayout.addComponent(cmbMainTenType,"top:107px; left:135px;");
		for (int i = 0; i < strType.length; i++)
		{
			cmbMainTenType.addItem(strType[i]);
			cmbMainTenType.setItemCaption(strType[i], strType[i].toString());
        }
		
		chkVehicleMaintenType = new CheckBox("All");
		chkVehicleMaintenType.setImmediate(true);
		mainLayout.addComponent(chkVehicleMaintenType, "top:108px; left:290px;");
		
		mainLayout.addComponent(cButton, "top:185px; left:125px; ");
		
		return mainLayout;
	}
}
