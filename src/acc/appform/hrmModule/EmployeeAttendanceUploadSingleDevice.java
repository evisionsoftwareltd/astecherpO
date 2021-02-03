package acc.appform.hrmModule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.BtUpload;
import com.common.share.CommonButton;
import com.common.share.MessageBox;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.data.Property.ValueChangeListener;

@SuppressWarnings("serial")
public class EmployeeAttendanceUploadSingleDevice extends Window 
{
	private SessionBean sessionBean;
	private AbsoluteLayout mainLayout;

	private Table table= new Table();
	private PopupDateField dWorkingDate ;
	private Label lblDate ;

	private ArrayList <Label> lblsl = new ArrayList<Label>();
	private ArrayList <Label> lblEmployeeId = new ArrayList<Label>(); 
	private ArrayList <Label> lblEmployeeCode = new ArrayList<Label>();
	private ArrayList <Label> lblProximityId = new ArrayList<Label>();
	private ArrayList <Label> lblEmployeeName = new ArrayList<Label>();
	private ArrayList <Label> lblDesignationID = new ArrayList<Label>();
	private ArrayList <Label> lblDesignation = new ArrayList<Label>();
	private ArrayList <Label> lblDepartmentID = new ArrayList<Label>();
	private ArrayList <Label> lblDepartment = new ArrayList<Label>();
	private ArrayList <Label> lblSectionID = new ArrayList<Label>();
	private ArrayList <Label> lblSection = new ArrayList<Label>();
	private ArrayList <PopupDateField> lblAttDate = new ArrayList<PopupDateField>();
	private ArrayList <Label> lblInOutTime = new ArrayList<Label>();
	ArrayList<Component> allComp = new ArrayList<Component>();	

	private SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dFormatBd = new SimpleDateFormat("M/d/yyyy");
	private SimpleDateFormat dFormatDDMMYY = new SimpleDateFormat("dd-MM-yyyy");

	CommonButton button = new CommonButton("New", "Save", "", "","Refresh","","","","","Exit");
	String username="";

	private NativeButton btnShowFile;
	public BtUpload fileAttUpload;

	String empCode="";
	String empFingerId="";
	String empProxId="";
	String empDesId="";
	String tableName="";
	String inOut="";
	String type="";

	@SuppressWarnings("unused")
	private String fileName = "";
	Object[] element;

	private HashMap <String,Object> hmEmployeeID = new HashMap <String,Object> ();
	private HashMap <String,Object> hmempID=new HashMap <String,Object> ();
	private HashMap <String,Object> hmEmployeeName = new HashMap <String,Object> ();
	private HashMap <String,Object> hmDesignationID = new HashMap <String,Object> ();
	private HashMap <String,Object> hmDesignation = new HashMap <String,Object> ();
	private HashMap <String,Object> hmDepartmentId = new HashMap <String,Object> ();
	private HashMap <String,Object> hmDepartment= new HashMap <String,Object> ();
	private HashMap <String,Object> hmSectionId = new HashMap <String,Object> ();
	private HashMap <String,Object> hmSection = new HashMap <String,Object> ();

	public EmployeeAttendanceUploadSingleDevice(SessionBean sessionBean) 
	{
		this.sessionBean=sessionBean;
		this.setResizable(false);
		this.setCaption("EMPLOYEE ATTENDANCE UPLOAD (SINGLE DEVICE) :: " + sessionBean.getCompany());

		buildMainLayout();
		setContent(mainLayout);
		tableinitialise();

		componentIni(true);
		btnIni(true);

		SetEventAction();
		authenticationCheck();
		dWorkingDate.focus();
		dataSaveToHashMap();
	}

	private void authenticationCheck()
	{
		if(!sessionBean.isSubmitable())
		{
			button.btnSave.setVisible(false);
		}

		if(!sessionBean.isUpdateable())
		{
			button.btnEdit.setVisible(false);
		}

		if(!sessionBean.isDeleteable())
		{
			button.btnDelete.setVisible(false);
		}
	}

	private void SetEventAction()
	{
		button.btnNew.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				componentIni(false);
				btnIni(false);
				txtClear();
				button.btnSave.setEnabled(false);
			}
		});

		button.btnRefresh.addListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event) 
			{
				componentIni(true);
				btnIni(true);
				txtClear();
				button.btnSave.setEnabled(false);
			}
		});

		button.btnSave.addListener( new Button.ClickListener() 
		{			
			public void buttonClick(ClickEvent event)
			{	
				if(!lblEmployeeId.get(0).getValue().toString().isEmpty())
				{
					saveBtnAction(event);
				}
				else
				{
					showNotification("Warning", "No Data Found!!!", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnExit.addListener( new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});

		btnShowFile.addListener( new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				button.btnSave.setEnabled(false);
				tableClear();
				showData();
				button.btnSave.setEnabled(true);
			}
		});

		dWorkingDate.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(dWorkingDate.getValue()!=null)
				{
					System.out.println("DateFormat = "+ dFormatBd.format(dWorkingDate.getValue()));
				}
			}
		});
	}

	private void dataSaveToHashMap()
	{
		/********************Edit Start By Mezbah****************************/
		hmEmployeeID.clear();
		hmempID.clear();
		hmEmployeeName.clear();
		hmDesignationID.clear();
		hmDesignation.clear();
		hmDepartmentId.clear();
		hmDepartment.clear();
		hmSectionId.clear();
		hmSection.clear();
		/********************Edit End By Mezbah****************************/
		
		Session session = SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			/*List <?> list = session.createSQLQuery("select ei.vEmployeeId,ei.employeeCode,ei.vProximityId,ei.vEmployeeName," +
					" ei.vDesignationID,di.designationName,ei.vDepartmentID,dept.vDepartmentName," +
					" ei.vSectionId,si.SectionName from tbEmployeeInfo ei left join tbDesignationInfo di on" +
					" ei.vDesignationId=di.designationId left join tbSectionInfo si on ei.vSectionId=si.vSectionID inner " +
					" join tbDepartmentInfo dept on dept.vDepartmentID=ei.vDepartmentID where ei.iStatus = 1 order by " +
					" CAST(SUBSTRING(vEmployeeId,5,LEN(vEmployeeId)) as int)").list();*/
			
			/********************Edit Start By Mezbah****************************/
			List <?> list = session.createSQLQuery("select ei.vEmployeeId,ei.employeeCode,ei.vProximityId,ei.vEmployeeName," +
					" ei.vDesignationID,di.designationName,ei.vDepartmentID,dept.vDepartmentName," +
					" ei.vSectionId,si.SectionName from tbEmployeeInfo ei left join tbDesignationInfo di on" +
					" ei.vDesignationId=di.designationId left join tbSectionInfo si on ei.vSectionId=si.vSectionID inner " +
					" join tbDepartmentInfo dept on dept.vDepartmentID=ei.vDepartmentID where ei.iStatus = 1 " +
					" and dept.vDepartmentId='DEPT10' or ei.vSectionId = 'SEC-5' order by " +
					" CAST(SUBSTRING(vEmployeeId,5,LEN(vEmployeeId)) as int)").list();
			/********************Edit End By Mezbah****************************/

			int count=0;
			for(Iterator <?> iter = list.iterator();iter.hasNext();)
			{
				Object element[] = (Object[]) iter.next();

				hmEmployeeID.put(element[2].toString(), element[0].toString());
				hmempID.put(element[2].toString(), element[1].toString());
				hmEmployeeName.put(element[2].toString(), element[3].toString());
				hmDesignationID.put(element[2].toString(), element[4].toString());
				hmDesignation.put(element[2].toString(), element[5].toString());
				hmDepartmentId.put(element[2].toString(), element[6].toString());
				hmDepartment.put(element[2].toString(), element[7].toString());
				hmSectionId.put(element[2].toString(), element[8].toString());
				hmSection.put(element[2].toString(), element[9].toString());

				count++;
				System.out.println(count+" Hash Map: "+element[0].toString()+" "+element[1].toString()+" "+element[2].toString()+" "+element[3].toString()+" "+element[4].toString()+" "+element[5].toString());
			}
		}
		catch (Exception e)
		{
			showNotification("Unable to Initialize", e.toString()+"",Notification.TYPE_ERROR_MESSAGE);
		}
		finally
		{
			session.close();
		}
	}
	public void saveAttendanceFileToDirectory(String path,String attUloadDateTime,String dworkdate,String s,String id,
			String ip,String deviceType,String actionType){
		
		File attendanceFileUpload=new File(path+attUloadDateTime+" DateOfAttendance ("+dworkdate+")- "+id+" "+ip+" "+deviceType+" "+actionType+".txt");
		
		try
		{
			if(!attendanceFileUpload.exists()){
				attendanceFileUpload.createNewFile();
			}
			
			FileWriter fw=new FileWriter(attendanceFileUpload,true);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(s);
			bw.newLine();
			bw.close();
		} 
		catch (Exception exp) {
			System.out.println(exp);
		}
	}
	private void saveDataToDirectory()
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			FileReader fr = new FileReader(getWindow().getApplication().getContext().getBaseDirectory()+""
					.replace("\\","/")+"/VAADIN/themes/"+fileAttUpload.fileName);
			fileName = fileAttUpload.fileName;
			BufferedReader br = new BufferedReader(fr);

			String s;
			StringTokenizer st;
			String strdate="";
			int i = 0;
			//String dworkdate1=dFormat.format(dWorkingDate.getValue()).toString().trim().replace("-", "/");


			

			//---------------File Save To Directory--------------------//
			String workdate=dFormatDDMMYY.format(dWorkingDate.getValue()).toString().trim();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			Date date = new Date();
			String path="D:/Tomcat 7.0/webapps/report/astecherp/employee/application/save/SingleDevice/";
			String attUloadDateTime=dateFormat.format(date);
			String id=sessionBean.getUserName().toString();
			String ip=sessionBean.getUserIp().toString();
			ip=ip.replace(":", ".");
			String deviceType="Single device"; //single and multiple device
			String actionType="Save";

			//---------------File Save To Directory--------------------//

			while((s = br.readLine())!=null)
			{
				//---------------File Save To Directory SaveAction By Didar--------------------//
				saveAttendanceFileToDirectory(path,attUloadDateTime,workdate,s,id,ip,deviceType,actionType);
				//---------------File Save To Directory SaveAction By Didar--------------------//

			}
			if(i==0)
				//showNotification("Warning","No Data Found!!!",Notification.TYPE_WARNING_MESSAGE);
			fr.close();
		}
		catch(java.io.FileNotFoundException n)
		{
			//showNotification("Warning!","Please first upload a valid attendance file.",Notification.TYPE_WARNING_MESSAGE);
		}
		catch(Exception exp)
		{
			//showNotification("Unable to upload",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
		finally{session.close();}
	}
	private void showData()
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			FileReader fr = new FileReader(getWindow().getApplication().getContext().getBaseDirectory()+""
					.replace("\\","/")+"/VAADIN/themes/"+fileAttUpload.fileName);
			fileName = fileAttUpload.fileName;
			BufferedReader br = new BufferedReader(fr);

			String s;
			StringTokenizer st;
			String strdate="";
			int i = 0;
			String dworkdate=dFormat.format(dWorkingDate.getValue()).toString().trim();
			//String dworkdate1=dFormat.format(dWorkingDate.getValue()).toString().trim().replace("-", "/");

			String dworkdate2=session.createSQLQuery("Select convert(date,DATEADD(DD,1,'"+dworkdate+"'))").list().iterator().next().toString();

			

			//---------------File Save To Directory--------------------//
			String workdate=dFormatDDMMYY.format(dWorkingDate.getValue()).toString().trim();
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");
			Date date = new Date();
			String path="D:/Tomcat 7.0/webapps/report/astecherp/employee/application/show/SingleDevice/";
			String attUloadDateTime=dateFormat.format(date);
			String id=sessionBean.getUserName().toString();
			String ip=sessionBean.getUserIp().toString();
			ip=ip.replace(":", ".");
			String deviceType="Single device"; //single and multiple device
			String actionType="Show";

			//---------------File Save To Directory--------------------//

			while((s = br.readLine())!=null)
			{
				//---------------File Save To Directory ShowAction By Didar--------------------//
				saveAttendanceFileToDirectory(path,attUloadDateTime,workdate,s,id,ip,deviceType,actionType);
				//---------------File Save To Directory ShowAction By Didar--------------------//
				
				st = new StringTokenizer(s,"#");
				String str[]={"", "", "", "", ""};
				int j = 0;
				while (st.hasMoreTokens())
				{
					str[j] = st.nextToken();
					j++;
				}
				strdate=str[2].replace("/", "-");

				if(strdate.equals(dworkdate) || strdate.equals(dworkdate2))
				{
					if(hmEmployeeName.get(str[1])!=null)
					{
		
						String proxId = str[1];
						
						if((hmDepartmentId.get(proxId).equals("DEPT10")|| hmSectionId.get(proxId).equals("SEC-5")))
						{
							lblProximityId.get(i).setValue(proxId);
							lblEmployeeId.get(i).setValue(hmEmployeeID.get(proxId));
							lblEmployeeCode.get(i).setValue(hmempID.get(proxId));
							lblEmployeeName.get(i).setValue(hmEmployeeName.get(proxId));
							lblDesignationID.get(i).setValue(hmDesignationID.get(proxId));
							lblDesignation.get(i).setValue(hmDesignation.get(proxId));
							lblDepartmentID.get(i).setValue(hmDepartmentId.get(proxId));
							lblDepartment.get(i).setValue(hmDepartment.get(proxId));
							lblSectionID.get(i).setValue(hmSectionId.get(proxId));
							lblSection.get(i).setValue(hmSection.get(proxId));

							@SuppressWarnings("deprecation")
							Date dt = new Date(str[2]);

							lblAttDate.get(i).setReadOnly(false);
							lblAttDate.get(i).setValue(dt);
							lblAttDate.get(i).setReadOnly(true);
							lblInOutTime.get(i).setValue(str[3]);

							if(i==lblProximityId.size()-1)
							{
								tableRowAdd(lblProximityId.size());
							}
							i++;
						}
						
						else
						{
							showNotification("Warning","Please Provide valid Data!!!",Notification.TYPE_WARNING_MESSAGE);
						}
						
					
					}
				}
			}
			if(i==0)
				showNotification("Warning","No Data Found!!!",Notification.TYPE_WARNING_MESSAGE);
			fr.close();
		}
		catch(java.io.FileNotFoundException n)
		{
			showNotification("Warning!","Please first upload a valid attendance file.",Notification.TYPE_WARNING_MESSAGE);
		}
		catch(Exception exp)
		{
			showNotification("Unable to upload",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
		finally{session.close();}
	}

	private void tableClear()
	{
		for(int i=0; i<lblEmployeeName.size(); i++)
		{
			lblEmployeeId.get(i).setValue("");
			lblEmployeeCode.get(i).setValue("");
			lblProximityId.get(i).setValue("");
			lblEmployeeName.get(i).setValue("");
			lblDesignationID.get(i).setValue("");
			lblDesignation.get(i).setValue("");
			lblDepartmentID.get(i).setValue("");
			lblDepartment.get(i).setValue("");
			lblSectionID.get(i).setValue("");
			lblSection.get(i).setValue("");

			lblAttDate.get(i).setReadOnly(false);
			lblAttDate.get(i).setValue(null);
			lblAttDate.get(i).setReadOnly(true);
			lblInOutTime.get(i).setValue("");
		}
	}

	private void saveBtnAction(ClickEvent e)
	{
		MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to save information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
		mb.show(new EventListener()
		{
			public void buttonClicked(ButtonType buttonType)
			{
				if(buttonType == ButtonType.YES)
				{
					insertData();
					componentIni(true);
					btnIni(true);
					saveDataToDirectory();
					txtClear();
				}
			}
		});
	}

	private void insertData()
	{
		Session session = SessionFactoryUtil.getInstance().openSession();
		Transaction tx = session.beginTransaction();
		try
		{
			String checkExists = "";
			String query = "";

			for(int i = 0; i < lblEmployeeId.size(); i++)
			{
				if(!lblEmployeeId.get(i).getValue().toString().isEmpty())
				{
					
					
					checkExists = "select * from tbEmployeeAttendanceSingleDevice "
							+ "where vEmployeeID = '"+lblEmployeeId.get(i).getValue()+"' "
									+ "and dAttTime = '"+dFormat.format(lblAttDate.get(i).getValue())+"'+' "+lblInOutTime.get(i).getValue()+"'";
					
					System.out.println("Sql :"+checkExists);
					List <?> lstCheck = session.createSQLQuery(checkExists).list();
					if(lstCheck.isEmpty())
					{
						query = "insert into tbEmployeeAttendanceSingleDevice(vEmployeeId,vEmployeeCode,vProximityID,vEmployeeName," +
								" vDesignationID,vDesignation,vDepartmentID,vDepartmentName,vSectionId,vSectionName,dAttDate,dAttTime,vUserId," +
								" vUserIP,dEntryTime) values ('"+lblEmployeeId.get(i).getValue()+"'," +
								" '"+lblEmployeeCode.get(i).getValue()+"'," +
								" '"+lblProximityId.get(i).getValue()+"'," +
								" '"+lblEmployeeName.get(i).getValue()+"'," +
								" '"+lblDesignationID.get(i).getValue()+"'," +
								" '"+lblDesignation.get(i).getValue()+"'," +
								" '"+lblDepartmentID.get(i).getValue()+"'," +
								" '"+lblDepartment.get(i).getValue()+"'," +
								" '"+lblSectionID.get(i).getValue()+"'," +
								" '"+lblSection.get(i).getValue()+"'," +
								" '"+dFormat.format(lblAttDate.get(i).getValue())+"'," +
								" '"+dFormat.format(lblAttDate.get(i).getValue())+"'+' "+lblInOutTime.get(i).getValue()+"'," +
								" '"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',GETDATE())";
						session.createSQLQuery(query).executeUpdate();
					}
				}
			}

			//----------Procedure Action----------//
			execProcedure(session);
			//----------Procedure Action----------//
			
			tx.commit();
			showNotification("All information save successfully.");
		}
		catch(Exception ex)
		{
			showNotification("Error", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		finally{session.close();}
	}

	private void execProcedure(Session session)
	{
		String query="exec prcDailyEmployeeAttendanceSingleMachine '"+dFormat.format(dWorkingDate.getValue())+"','%','%','%','"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"'";
		session.createSQLQuery(query).executeUpdate();
	}

	private void componentIni(boolean b) 
	{
		dWorkingDate.setEnabled(!b);
		btnShowFile.setEnabled(!b);
		fileAttUpload.setEnabled(!b);
		table.setEnabled(!b);
	}

	private void btnIni(boolean t)
	{
		button.btnNew.setEnabled(t);
		button.btnEdit.setEnabled(t);
		button.btnSave.setEnabled(!t);
		button.btnRefresh.setEnabled(!t);
		button.btnDelete.setEnabled(t);
		button.btnFind.setEnabled(t);
	}

	public void txtClear()
	{
		tableClear();
	}

	private AbsoluteLayout buildMainLayout() 
	{
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(true);
		mainLayout.setMargin(false);
		mainLayout.setWidth("1015px");
		mainLayout.setHeight("400px");

		lblDate = new Label("Date :");
		lblDate.setImmediate(false);
		lblDate.setWidth("-1px");
		lblDate.setHeight("-1px");
		mainLayout.addComponent(lblDate, "top:20.0px; left:40.0px;");

		dWorkingDate = new PopupDateField();
		dWorkingDate.setImmediate(true);
		dWorkingDate.setWidth("110px");
		dWorkingDate.setDateFormat("dd-MM-yyyy");
		dWorkingDate.setValue(new java.util.Date());
		dWorkingDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(dWorkingDate, "top:18.0px; left:100.0px;");

		fileAttUpload = new BtUpload("temp/attendanceFolder/c");
		fileAttUpload.setImmediate(true);
		mainLayout.addComponent(fileAttUpload, "top:54.0px; left:40.0px;");

		btnShowFile = new NativeButton("Show");
		btnShowFile.setIcon(new ThemeResource("../icons/find.png"));
		btnShowFile.setImmediate(true);
		btnShowFile.setWidth("75px");
		btnShowFile.setHeight("28px");
		mainLayout.addComponent(btnShowFile, "top:42.0px; right:30.0px;");

		table.setWidth("98%");
		table.setHeight("280px");
		table.setColumnCollapsingAllowed(true);

		table.addContainerProperty("SL", Label.class, new Label());
		table.setColumnWidth("SL", 25);

		table.addContainerProperty("Emp. Id", Label.class, new Label());
		table.setColumnWidth("Emp. Id", 80);

		table.addContainerProperty("Employee ID", Label.class, new Label());
		table.setColumnWidth("Employee ID", 80);

		table.addContainerProperty("Proximity ID", Label.class, new Label());
		table.setColumnWidth("Proximity ID", 80);

		table.addContainerProperty("Employee Name", Label.class, new Label());
		table.setColumnWidth("Employee Name",170);

		table.addContainerProperty("Designation ID", Label.class, new Label());
		table.setColumnWidth("Designation ID", 80);

		table.addContainerProperty("Designation", Label.class, new Label());
		table.setColumnWidth("Designation", 140);

		table.addContainerProperty("Department ID", Label.class, new Label());
		table.setColumnWidth("Department ID", 100);

		table.addContainerProperty("Department", Label.class, new Label());
		table.setColumnWidth("Department", 90);

		table.addContainerProperty("Section ID", Label.class, new Label());
		table.setColumnWidth("Section ID", 100);

		table.addContainerProperty("Section", Label.class, new Label());
		table.setColumnWidth("Section", 90);

		table.addContainerProperty("Att. Date", PopupDateField.class, new PopupDateField());
		table.setColumnWidth("Att. Date", 90);

		table.addContainerProperty("Time", Label.class, new Label());
		table.setColumnWidth("Time",70);

		table.setColumnAlignments(new String[] {Table.ALIGN_RIGHT, Table.ALIGN_LEFT, Table.ALIGN_LEFT,
				Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, 
				Table.ALIGN_LEFT, Table.ALIGN_LEFT,Table.ALIGN_LEFT, Table.ALIGN_CENTER, Table.ALIGN_CENTER});

		table.setColumnCollapsingAllowed(true);
		table.setColumnCollapsed("Emp. Id", true);
		table.setColumnCollapsed("Designation ID", true);
		table.setColumnCollapsed("Department ID", true);
		table.setColumnCollapsed("Section ID", true);

		mainLayout.addComponent(table,"top:80.0px; left:20.0px;");
		mainLayout.addComponent(button,"top:365.0px; left:350.0px");

		return mainLayout;
	}

	private void tableinitialise()
	{
		for(int i=0;i<10;i++)
		{
			tableRowAdd(i);
		}
	}

	private void tableRowAdd( final int ar)
	{
		lblsl.add(ar,new Label());
		lblsl.get(ar).setWidth("100%");
		lblsl.get(ar).setValue(ar+1);
		lblsl.get(ar).setImmediate(true);
		lblsl.get(ar).setHeight("16px");

		lblEmployeeId.add(ar, new Label());
		lblEmployeeId.get(ar).setImmediate(true);
		lblEmployeeId.get(ar).setWidth("100%");

		lblEmployeeCode.add(ar, new Label());
		lblEmployeeCode.get(ar).setImmediate(true);
		lblEmployeeCode.get(ar).setWidth("100%");

		lblProximityId.add(ar, new Label());
		lblProximityId.get(ar).setImmediate(true);
		lblProximityId.get(ar).setWidth("100%");

		lblEmployeeName.add(ar,new Label());
		lblEmployeeName.get(ar).setWidth("100%");
		lblEmployeeName.get(ar).setImmediate(true);

		lblDesignationID.add(ar, new Label());
		lblDesignationID.get(ar).setWidth("100%");
		lblDesignationID.get(ar).setImmediate(true);

		lblDesignation.add(ar, new Label());
		lblDesignation.get(ar).setWidth("100%");
		lblDesignation.get(ar).setImmediate(true);

		lblDepartmentID.add(ar, new Label());
		lblDepartmentID.get(ar).setWidth("100%");
		lblDepartmentID.get(ar).setImmediate(true);

		lblDepartment.add(ar, new Label());
		lblDepartment.get(ar).setWidth("100%");
		lblDepartment.get(ar).setImmediate(true);

		lblSectionID.add(ar, new Label());
		lblSectionID.get(ar).setWidth("100%");
		lblSectionID.get(ar).setImmediate(true);

		lblSection.add(ar, new Label());
		lblSection.get(ar).setWidth("100%");
		lblSection.get(ar).setImmediate(true);

		lblAttDate.add(ar, new PopupDateField());
		lblAttDate.get(ar).setWidth("100%");
		lblAttDate.get(ar).setDateFormat("dd-MM-yyyy");
		lblAttDate.get(ar).setResolution(PopupDateField.RESOLUTION_DAY);
		lblAttDate.get(ar).setReadOnly(true);
		lblAttDate.get(ar).setImmediate(true);

		lblInOutTime.add(ar, new Label());
		lblInOutTime.get(ar).setWidth("100%");
		lblInOutTime.get(ar).setImmediate(true);

		lblEmployeeId.add(ar, new Label());
		lblEmployeeId.get(ar).setImmediate(true);

		table.addItem(new Object[]{lblsl.get(ar),lblEmployeeId.get(ar),lblEmployeeCode.get(ar),lblProximityId.get(ar),
				lblEmployeeName.get(ar),lblDesignationID.get(ar),lblDesignation.get(ar),lblDepartmentID.get(ar),
				lblDepartment.get(ar),lblSectionID.get(ar),lblSection.get(ar),lblAttDate.get(ar),lblInOutTime.get(ar)},ar);
	}
}