	package acc.appform.hrmModule;

	import java.util.ArrayList;
	import java.util.Iterator;
	import java.util.List;
	import org.hibernate.Session;
	import org.hibernate.Transaction;
	import com.common.share.*;
	import com.common.share.MessageBox.ButtonType;
	import com.common.share.MessageBox.EventListener;

	import java.text.SimpleDateFormat;

	import com.vaadin.data.Property.ValueChangeEvent;
	import com.vaadin.data.Property.ValueChangeListener;
	import com.vaadin.terminal.ThemeResource;
	import com.vaadin.ui.AbsoluteLayout;
	import com.vaadin.ui.Button;
	import com.vaadin.ui.CheckBox;
	import com.vaadin.ui.ComboBox;
	import com.vaadin.ui.Component;
	import com.vaadin.ui.Label;
	import com.vaadin.ui.NativeButton;
	import com.vaadin.ui.PopupDateField;
	import com.vaadin.ui.Table;
	import com.vaadin.ui.TextField;
	import com.vaadin.ui.Window;
	import com.vaadin.ui.Button.ClickEvent;

public class EditEmployeeAttendanceCHO extends Window {
	
		private AbsoluteLayout mainLayout;
		private Label lblDate = new Label("Date");
		private PopupDateField dDate = new PopupDateField();

		private Label lblDeptName = new Label("");
		private ComboBox cmbDeptName = new ComboBox();

		private Label lblSectionName = new Label("");
		private ComboBox cmbSectionName = new ComboBox();
		private CheckBox chkSectionAll;

		private Label lblCl= new Label("");

		private SessionBean sessionBean;

		String computerName = "";
		String userName = "";
		String year = "";
		String deptID = "";
		String strEmpDeptID ="";
		String strEmpID ="";
		String strDeptID ="";
		int ind=0;
		int j=0;
		String FingerID ="";
		boolean t;
		String Notify="";

		private boolean isFind= false;

		private SimpleDateFormat DBdateformat = new SimpleDateFormat("yyyy-MM-dd");
		private Table table = new Table();
		private ArrayList<Label> lbSl = new ArrayList<Label>();
		private ArrayList<Label> lblAutoEmpID = new ArrayList<Label>();
		private ArrayList<Label> lblempID = new ArrayList<Label>();
		private ArrayList<Label> lbProxID = new ArrayList<Label>();
		private ArrayList<Label> lbEmployeeName = new ArrayList<Label>();
		private ArrayList<Label> lblDesignationID = new ArrayList<Label>();
		private ArrayList<Label> lbDesignation = new ArrayList<Label>();
		private ArrayList<PopupDateField> lbAttendDate = new ArrayList<PopupDateField>();
		private ArrayList<TimeField> InTime1 = new ArrayList<TimeField>();
		private ArrayList<TimeField> InTime2 = new ArrayList<TimeField>();
		private ArrayList<TimeField> InTime3 = new ArrayList<TimeField>();
		private ArrayList<TimeField> OutTime1 = new ArrayList<TimeField>();
		private ArrayList<TimeField> OutTime2 = new ArrayList<TimeField>();
		private ArrayList<TimeField> OutTime3 = new ArrayList<TimeField>();
		private ArrayList<NativeButton> Close = new ArrayList<NativeButton>();
		private ArrayList<TextField> txtPermitBy = new ArrayList<TextField>();
		private ArrayList<TextField> txtReason = new ArrayList<TextField>();
		private ArrayList<Label> lbDeptId = new ArrayList<Label>();
		private ArrayList<Label> lbDeptName = new ArrayList<Label>();
		private ArrayList<Label> lbSecId = new ArrayList<Label>();
		private ArrayList<Label> lbSecName = new ArrayList<Label>();

		private CommonButton button = new CommonButton("New", "Save", "", "","Refresh","","","","","Exit");
		ArrayList<Component> allComp = new ArrayList<Component>();

		public EditEmployeeAttendanceCHO(SessionBean sessionBean)
		{
			this.sessionBean = sessionBean;
			this.setCaption("EDIT EMPLOYEE ATTENDANCE CHO:: "+sessionBean.getCompany());
			this.setResizable(false);
			buildMainLayout();
			setContent(mainLayout);
			tableInitialize();
			btnIni(true);
			componentIni(true);
			buttonAction();
			departMentdataAdd();
			focusEnter();
			authenticationCheck();
			button.btnNew.focus();
		}

		private void authenticationCheck()
		{
			if(!sessionBean.isSubmitable()){
				button.btnSave.setVisible(false);
			}

			if(!sessionBean.isUpdateable()){
				button.btnEdit.setVisible(false);
			}

			if(!sessionBean.isDeleteable()){
				button.btnDelete.setVisible(false);
			}
		}

		private void buttonAction()
		{

			button.btnNew.addListener(new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{
					isFind = false;
					newButtonEvent();
				}
			});

			button.btnFind.addListener( new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{
					isFind = true;
				}
			});

			button.btnEdit.addListener( new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{
					updateAction();
				}
			});

			cmbDeptName.addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					cmbSectionName.removeAllItems();
					cmbSectionName.setEnabled(true);
					chkSectionAll.setValue(false);
					if(cmbDeptName.getValue()!=null)
					{
						sectiondataAdd();
					}
				}
			});

			cmbSectionName.addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					tableclear();
					if(cmbSectionName.getValue()!=null)
					{
						tableDataAdd(cmbSectionName.getValue().toString());
					}
				}
			});

			chkSectionAll.addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					tableclear();
					if(chkSectionAll.booleanValue())
					{
						cmbSectionName.setValue(null);
						cmbSectionName.setEnabled(false);
						tableDataAdd("%");
					}
					else
					{
						cmbSectionName.setEnabled(true);
					}
				}
			});

			dDate.addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					cmbDeptName.setValue(null);
					tableclear();
				}
			});

			button.btnSave.addListener( new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{
					formValidation();
				}
			});

			button.btnRefresh.addListener(new Button.ClickListener()
			{
				public void buttonClick(ClickEvent event) 
				{
					isFind=false;
					refreshButtonEvent();
				}
			});

			button.btnExit.addListener( new Button.ClickListener() 
			{
				public void buttonClick(ClickEvent event) 
				{
					close();
				}
			});
		}

		private void tableDataAdd(String SectionID)
		{
			Session session=SessionFactoryUtil.getInstance().openSession();
			session.beginTransaction();
			try
			{
				String Query="select vEmployeeID,vEmployeeCode,vProximityID,vEmployeeName,vDesignationID,vDesignationName,"+
						"dAttendanceDate,txtDay,vDepartmentID,vDepartmentName,vSectionID,vSectionName,inHour,"+
						"inMinute,inSecond,outHour,outMinute,outSecond from funEditDailyEmployeeAttendance" +
						"('"+DBdateformat.format(dDate.getValue())+"','"+cmbDeptName.getValue()+"','"+SectionID+"') where " +
						"dAttendanceDate<convert(date,getdate()) order by vEmployeeCode";
				List <?> lst=session.createSQLQuery(Query).list();
				if(!lst.isEmpty())
				{
					int i=0;
					for(Iterator <?> itr=lst.iterator(); itr.hasNext();)
					{
						Object [] element=(Object[]) itr.next();
						lblAutoEmpID.get(i).setValue(element[0]);
						lblempID.get(i).setValue(element[1]);
						lbProxID.get(i).setValue(element[2]);
						lbEmployeeName.get(i).setValue(element[3]);
						lblDesignationID.get(i).setValue(element[4]);
						lbDesignation.get(i).setValue(element[5]);
						lbAttendDate.get(i).setReadOnly(false);
						lbAttendDate.get(i).setValue(element[6]);
						lbAttendDate.get(i).setReadOnly(true);

						lbDeptId.get(i).setValue(element[8]);
						lbDeptName.get(i).setValue(element[9]);
						lbSecId.get(i).setValue(element[10]);
						lbSecName.get(i).setValue(element[11]);

						if(!element[12].toString().equals("0"))
						{
							InTime1.get(i).setValue(element[12]);
							InTime2.get(i).setValue(element[13]);
							InTime3.get(i).setValue(element[14]);
						}
						if(!element[15].toString().equals("0"))
						{
							OutTime1.get(i).setValue(element[15]);
							OutTime2.get(i).setValue(element[16]);
							OutTime3.get(i).setValue(element[17]);
						}

						if(i==lblAutoEmpID.size()-1)
							tableRowAdd(i+1);

						i++;
					}
				}
				else
				{
					showNotification("Warning", "No Data Found!!!", Notification.TYPE_WARNING_MESSAGE);
				}
			}
			catch(Exception exp)
			{
				showNotification("tableDataAdd", exp.toString(), Notification.TYPE_ERROR_MESSAGE);
			}
			finally
			{
				session.close();
			}
		}

		private void tableInitialize()
		{
			table.setColumnCollapsingAllowed(true);
			table.setWidth("99%");
			table.setHeight("330px");
			table.setPageLength(0);

			table.addContainerProperty("Del", NativeButton.class , new NativeButton());
			table.setColumnWidth("Del",30);

			table.addContainerProperty("SL #", Label.class , new Label());
			table.setColumnWidth("SL #",20);

			table.addContainerProperty("EMP ID", Label.class, new Label());
			table.setColumnWidth("EMP ID", 100);

			table.addContainerProperty("Employee ID", Label.class, new Label());
			table.setColumnWidth("Employee ID", 100);

			table.addContainerProperty("Proximity ID", Label.class , new Label());
			table.setColumnWidth("Proximity ID",80);

			table.addContainerProperty("Employee Name", Label.class , new Label());
			table.setColumnWidth("Employee Name",170);

			table.addContainerProperty("Designation ID", Label.class , new Label());
			table.setColumnWidth("Designation ID",110);

			table.addContainerProperty("Designation", Label.class , new Label());
			table.setColumnWidth("Designation",110);	

			table.addContainerProperty("Attend date", PopupDateField.class , new PopupDateField());
			table.setColumnWidth("Attend date",100);

			table.addContainerProperty("HH(IN)", TimeField.class , new TimeField());
			table.setColumnWidth("HH(IN)",28);

			table.addContainerProperty("Min(IN)", TimeField.class , new TimeField());
			table.setColumnWidth("Min(IN)",28);

			table.addContainerProperty("Sec(IN)", TimeField.class , new TimeField());
			table.setColumnWidth("Sec(IN)",28);

			table.addContainerProperty("HH(OUT)", TimeField.class , new TimeField());
			table.setColumnWidth("HH(OUT)",28);

			table.addContainerProperty("Min(OUT)", TimeField.class , new TimeField());
			table.setColumnWidth("Min(OUT)",28);

			table.addContainerProperty("Sec(OUT)", TimeField.class , new TimeField());
			table.setColumnWidth("Sec(OUT)",28);

			table.addContainerProperty("Permitted By", TextField.class , new TextField());
			table.setColumnWidth("Permitted By",120);

			table.addContainerProperty("Reason", TextField.class , new TextField());
			table.setColumnWidth("Reason",105);	

			table.addContainerProperty("DId", Label.class , new Label());
			table.setColumnWidth("DId",50);

			table.addContainerProperty("DName", Label.class , new Label());
			table.setColumnWidth("DName",120);

			table.addContainerProperty("sId", Label.class , new Label());
			table.setColumnWidth("sId",50);

			table.addContainerProperty("sName", Label.class , new Label());
			table.setColumnWidth("sName",120);

			table.setColumnCollapsed("EMP ID", true);
			table.setColumnCollapsed("Designation ID", true);
			table.setColumnCollapsed("Sec(IN)", true);
			table.setColumnCollapsed("Sec(OUT)", true);
			table.setColumnCollapsed("p", true);
			table.setColumnCollapsed("DId", true);
			table.setColumnCollapsed("DName", true);
			table.setColumnCollapsed("sId", true);
			table.setColumnCollapsed("sName", true);

			table.setColumnAlignments(new String[] {Table.ALIGN_CENTER,Table.ALIGN_CENTER,Table.ALIGN_LEFT,Table.ALIGN_CENTER,
					Table.ALIGN_CENTER,Table.ALIGN_LEFT,Table.ALIGN_LEFT,Table.ALIGN_LEFT,Table.ALIGN_LEFT,Table.ALIGN_RIGHT,
					Table.ALIGN_CENTER,Table.ALIGN_RIGHT,Table.ALIGN_CENTER,Table.ALIGN_CENTER,Table.ALIGN_CENTER,
					Table.ALIGN_CENTER,Table.ALIGN_CENTER ,Table.ALIGN_CENTER,Table.ALIGN_CENTER,Table.ALIGN_CENTER,
					Table.ALIGN_CENTER});

			rowAddinTable();
		}

		public void rowAddinTable()
		{
			for(int i=0;i<10;i++)
			{
				tableRowAdd(i);
			}
		}

		public void tableRowAdd(final int ar)
		{
			Close.add(ar, new NativeButton(""));
			Close.get(ar).setWidth("100%");
			Close.get(ar).setImmediate(true);
			Close.get(ar).setIcon(new ThemeResource("../icons/cancel.png"));
			Close.get(ar).addListener(new Button.ClickListener()
			{
				public void buttonClick(ClickEvent event) 
				{
					lblempID.get(ar).setValue("");
					lblAutoEmpID.get(ar).setValue("");
					lbProxID.get(ar).setValue("");
					lbEmployeeName.get(ar).setValue("");
					lblDesignationID.get(ar).setValue("");
					lbDesignation.get(ar).setValue("");
					lbAttendDate.get(ar).setReadOnly(false);
					lbAttendDate.get(ar).setValue(null);
					lbAttendDate.get(ar).setReadOnly(true);
					txtPermitBy.get(ar).setValue("");
					txtReason.get(ar).setValue("");
					txtReason.get(ar).setValue("");
					InTime1.get(ar).setValue("");
					InTime2.get(ar).setValue("");
					InTime3.get(ar).setValue("");
					OutTime1.get(ar).setValue("");
					OutTime2.get(ar).setValue("");
					OutTime3.get(ar).setValue("");
					lbDeptId.get(ar).setValue("");
					lbDeptName.get(ar).setValue("");
					lbSecId.get(ar).setValue("");
					lbSecName.get(ar).setValue("");

					for(int rowcount=ar;rowcount<=lbProxID.size()-1;rowcount++)
					{
						if(rowcount+1<=lbProxID.size()-1)
						{
							if(!lbProxID.get(rowcount+1).getValue().toString().equals(""))
							{
								lblAutoEmpID.get(rowcount).setValue(lblAutoEmpID.get(rowcount+1).getValue().toString());
								lblempID.get(rowcount).setValue(lblempID.get(rowcount+1).getValue().toString());
								lbProxID.get(rowcount).setValue(lbProxID.get(rowcount+1).getValue().toString());
								lbEmployeeName.get(rowcount).setValue(lbEmployeeName.get(rowcount+1).getValue().toString());
								lblDesignationID.get(rowcount).setValue(lblDesignationID.get(rowcount+1).getValue().toString());
								lbDesignation.get(rowcount).setValue(lbDesignation.get(rowcount+1).getValue().toString());
								lbAttendDate.get(rowcount).setReadOnly(false);
								lbAttendDate.get(rowcount).setValue(lbAttendDate.get(rowcount+1).getValue());
								lbAttendDate.get(rowcount).setReadOnly(false);
								txtPermitBy.get(rowcount).setValue(txtPermitBy.get(rowcount+1).getValue().toString());
								txtReason.get(rowcount).setValue(txtReason.get(rowcount+1).getValue().toString());
								InTime1.get(rowcount).setValue(InTime1.get(rowcount+1).getValue().toString());
								InTime2.get(rowcount).setValue(InTime2.get(rowcount+1).getValue().toString());
								InTime3.get(rowcount).setValue(InTime3.get(rowcount+1).getValue().toString());
								OutTime1.get(rowcount).setValue(OutTime1.get(rowcount+1).getValue().toString());
								OutTime2.get(rowcount).setValue(OutTime2.get(rowcount+1).getValue().toString());
								OutTime3.get(rowcount).setValue(OutTime3.get(rowcount+1).getValue().toString());
								lbDeptId.get(rowcount).setValue(lbSecId.get(rowcount+1).getValue().toString());
								lbDeptName.get(rowcount).setValue(lbSecName.get(rowcount+1).getValue().toString());
								lbSecId.get(rowcount).setValue(lbSecId.get(rowcount+1).getValue().toString());
								lbSecName.get(rowcount).setValue(lbSecName.get(rowcount+1).getValue().toString());

								lblAutoEmpID.get(rowcount+1).setValue("");
								lblempID.get(rowcount+1).setValue("");
								lbProxID.get(rowcount+1).setValue("");
								lbEmployeeName.get(rowcount+1).setValue("");
								lblDesignationID.get(rowcount+1).setValue("");
								lbDesignation.get(rowcount+1).setValue("");
								lbAttendDate.get(rowcount+1).setReadOnly(false);
								lbAttendDate.get(rowcount+1).setValue(null);
								lbAttendDate.get(rowcount+1).setReadOnly(true);
								txtPermitBy.get(rowcount+1).setValue("");
								txtReason.get(rowcount+1).setValue("");
								InTime1.get(rowcount+1).setValue("");
								InTime2.get(rowcount+1).setValue("");
								InTime3.get(rowcount+1).setValue("");
								OutTime1.get(rowcount+1).setValue("");
								OutTime2.get(rowcount+1).setValue("");
								OutTime3.get(rowcount+1).setValue("");
								lbDeptId.get(rowcount+1).setValue("");
								lbDeptName.get(rowcount+1).setValue("");
								lbSecId.get(rowcount+1).setValue("");
								lbSecName.get(rowcount+1).setValue("");
							}
						}
					}
				}
			});

			lbSl.add(ar, new Label(""));
			lbSl.get(ar).setWidth("100%");
			lbSl.get(ar).setHeight("20px");
			lbSl.get(ar).setValue(ar+1);

			lblAutoEmpID.add(ar, new Label());
			lblAutoEmpID.get(ar).setImmediate(true);
			lblAutoEmpID.get(ar).setWidth("100%");
			lblAutoEmpID.get(ar).setHeight("20px");

			lblempID.add(ar,new Label(""));
			lblempID.get(ar).setImmediate(true);
			lblempID.get(ar).setWidth("100%");
			lblempID.get(ar).setHeight("20px");

			lbProxID.add(ar, new Label(""));
			lbProxID.get(ar).setWidth("100%");
			lbProxID.get(ar).setImmediate(true);
			lbProxID.get(ar).setHeight("-1px");

			lbEmployeeName.add(ar, new Label(""));
			lbEmployeeName.get(ar).setWidth("100%");
			lbEmployeeName.get(ar).setImmediate(true);

			lblDesignationID.add(ar, new Label(""));
			lblDesignationID.get(ar).setWidth("100%");
			lblDesignationID.get(ar).setImmediate(true);

			lbDesignation.add(ar, new Label(""));
			lbDesignation.get(ar).setWidth("100%");
			lbDesignation.get(ar).setImmediate(true);

			lbAttendDate.add(ar, new PopupDateField(""));
			lbAttendDate.get(ar).setImmediate(true);
			lbAttendDate.get(ar).setDateFormat("dd-MM-yyy");
			lbAttendDate.get(ar).setResolution(PopupDateField.RESOLUTION_DAY);
			lbAttendDate.get(ar).setWidth("100%");
			lbAttendDate.get(ar).setReadOnly(true);

			InTime1.add(ar, new TimeField());
			InTime1.get(ar).setWidth("28px");
			InTime1.get(ar).setInputPrompt("hh");
			InTime1.get(ar).setImmediate(true);
			InTime1.get(ar).setStyleName("Intime");
			InTime1.get(ar).setMaxLength(2);
			InTime1.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!InTime1.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(InTime1.get(ar).getValue().toString())>23)
						{
							InTime1.get(ar).setValue("");
						}
						else
						{
							InTime1.get(ar).setValue(InTime1.get(ar).getValue().toString());
						}
					}
				}
			});

			InTime2.add(ar, new TimeField());
			InTime2.get(ar).setWidth("28px");
			InTime2.get(ar).setInputPrompt("mm");
			InTime2.get(ar).setImmediate(true);
			InTime2.get(ar).setStyleName("Intime");
			InTime2.get(ar).setMaxLength(2);
			InTime2.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!InTime2.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(InTime2.get(ar).getValue().toString())>59)
						{
							InTime2.get(ar).setValue("");
						}
						else{
							InTime2.get(ar).setValue(InTime2.get(ar).getValue().toString());
						}
					}
				}
			});

			InTime3.add(ar, new TimeField());
			InTime3.get(ar).setWidth("28px");
			InTime3.get(ar).setInputPrompt("ss");
			InTime3.get(ar).setImmediate(true);
			InTime3.get(ar).setStyleName("Intime");
			InTime3.get(ar).setMaxLength(2);
			InTime3.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!InTime3.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(InTime3.get(ar).getValue().toString())>59)
						{
							InTime3.get(ar).setValue("");
						}
						else{
							InTime3.get(ar).setValue(InTime3.get(ar).getValue().toString());
						}
					}
				}
			});
			InTime3.get(ar).setEnabled(false);

			OutTime1.add(ar, new TimeField());
			OutTime1.get(ar).setWidth("28px");
			OutTime1.get(ar).setInputPrompt("hh");
			OutTime1.get(ar).setImmediate(true);
			OutTime1.get(ar).addStyleName("Outtime");
			OutTime1.get(ar).setMaxLength(2);
			OutTime1.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!OutTime1.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(OutTime1.get(ar).getValue().toString())>23)
						{
							OutTime1.get(ar).setValue("");
						}
						else{
							OutTime1.get(ar).setValue(OutTime1.get(ar).getValue().toString());
						}
					}
				}
			});

			OutTime2.add(ar, new TimeField());
			OutTime2.get(ar).setWidth("28px");
			OutTime2.get(ar).setInputPrompt("mm");
			OutTime2.get(ar).setImmediate(true);
			OutTime2.get(ar).setStyleName("Outtime");
			OutTime2.get(ar).setMaxLength(2);
			OutTime2.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!OutTime2.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(OutTime2.get(ar).getValue().toString())>59)
						{
							OutTime2.get(ar).setValue("");
						}
						else{
							OutTime2.get(ar).setValue(OutTime2.get(ar).getValue().toString());
						}
					}
				}
			});

			OutTime3.add(ar, new TimeField());
			OutTime3.get(ar).setWidth("28px");
			OutTime3.get(ar).setInputPrompt("ss");
			OutTime3.get(ar).setImmediate(true);
			OutTime3.get(ar).setStyleName("Outtime");
			OutTime3.get(ar).setMaxLength(2);
			OutTime3.get(ar).addListener(new ValueChangeListener()
			{
				public void valueChange(ValueChangeEvent event)
				{
					if(!OutTime3.get(ar).getValue().toString().isEmpty())
					{			
						if(Double.parseDouble(OutTime3.get(ar).getValue().toString())>59)
						{
							OutTime3.get(ar).setValue("");
						}
						else{
							OutTime3.get(ar).setValue(OutTime3.get(ar).getValue().toString());
						}
					}
				}
			});
			OutTime3.get(ar).setEnabled(false);

			txtPermitBy.add(ar, new TextField(""));
			txtPermitBy.get(ar).setWidth("100%");
			txtPermitBy.get(ar).setImmediate(true);
			txtPermitBy.get(ar).setHeight("-1px");

			txtReason.add(ar, new TextField(""));
			txtReason.get(ar).setWidth("100%");
			txtReason.get(ar).setImmediate(true);

			lbDeptId.add(ar, new Label(""));
			lbDeptId.get(ar).setWidth("100%");
			lbDeptId.get(ar).setImmediate(true);
			lbDeptId.get(ar).setHeight("-1px");

			lbDeptName.add(ar, new Label(""));
			lbDeptName.get(ar).setWidth("100%");
			lbDeptName.get(ar).setImmediate(true);
			lbDeptName.get(ar).setHeight("-1px");

			lbSecId.add(ar, new Label(""));
			lbSecId.get(ar).setWidth("100%");
			lbSecId.get(ar).setImmediate(true);
			lbSecId.get(ar).setHeight("-1px");

			lbSecName.add(ar, new Label(""));
			lbSecName.get(ar).setWidth("100%");
			lbSecName.get(ar).setImmediate(true);
			lbSecName.get(ar).setHeight("-1px");

			table.addItem(new Object[]{Close.get(ar),lbSl.get(ar),lblAutoEmpID.get(ar),lblempID.get(ar),lbProxID.get(ar),
					lbEmployeeName.get(ar),lblDesignationID.get(ar),lbDesignation.get(ar),lbAttendDate.get(ar),InTime1.get(ar),
					InTime2.get(ar),InTime3.get(ar),OutTime1.get(ar),OutTime2.get(ar),OutTime3.get(ar),txtPermitBy.get(ar),
					txtReason.get(ar),lbDeptId.get(ar),lbDeptName.get(ar),lbSecId.get(ar),lbSecName.get(ar)},ar);
		}

		private void btnIni(boolean t)
		{
			button.btnNew.setEnabled(t);
			button.btnEdit.setEnabled(t);
			button.btnSave.setEnabled(!t);
			button.btnRefresh.setEnabled(!t);
			button.btnDelete.setEnabled(t);
			button.btnFind.setEnabled(t);;
		}

		private void componentIni(boolean b) 
		{
			dDate.setEnabled(!b);

			if(isFind==false)
			{
				cmbDeptName.setEnabled(!b);
				cmbSectionName.setEnabled(!b);
				chkSectionAll.setEnabled(!b);
			}
			else
			{
				cmbDeptName.setEnabled(b);
				cmbSectionName.setEnabled(b);
				chkSectionAll.setEnabled(b);
			}
			table.setEnabled(!b);
		}

		private void txtClear()
		{
			cmbDeptName.setValue(null);
			cmbSectionName.setValue(null);
			chkSectionAll.setValue(false);
			tableclear();
		}

		private void tableclear()
		{
			for(int i =0; i<lbProxID.size(); i++)
			{
				lblAutoEmpID.get(i).setValue("");
				lblempID.get(i).setValue("");
				lbProxID.get(i).setValue("");
				lbEmployeeName.get(i).setValue("");
				lbDesignation.get(i).setValue("");
				txtPermitBy.get(i).setValue("");
				txtReason.get(i).setValue("");
				txtPermitBy.get(i).setValue("");
				InTime1.get(i).setValue("");
				InTime2.get(i).setValue("");
				InTime3.get(i).setValue("");
				OutTime1.get(i).setValue("");
				OutTime2.get(i).setValue("");
				OutTime3.get(i).setValue("");
				lbDeptId.get(i).setValue("");
				lbDeptName.get(i).setValue("");
				lbSecId.get(i).setValue("");
				lbSecName.get(i).setValue("");
				lbAttendDate.get(i).setReadOnly(false);
				lbAttendDate.get(i).setValue(null);
				lbAttendDate.get(i).setReadOnly(true);
			}
		}

		private void focusEnter()
		{
			allComp.add(dDate);
			allComp.add(cmbDeptName);
			allComp.add(cmbSectionName);

			for(int i=0; i<lbEmployeeName.size();i++)
			{
				allComp.add(InTime1.get(i));
				allComp.add(InTime2.get(i));
				allComp.add(OutTime1.get(i));
				allComp.add(OutTime2.get(i));
				allComp.add(txtPermitBy.get(i));
				allComp.add(txtReason.get(i));
			}

			allComp.add(button.btnSave);

			new FocusMoveByEnter(this,allComp);
		}

		public boolean validTableSelect()
		{
			boolean ret=false;
			for(int i=0; i<lbProxID.size(); i++)
			{
				if(!lblAutoEmpID.get(i).getValue().toString().isEmpty() &&
						!lbProxID.get(i).getValue().toString().isEmpty() && 
						!lbDesignation.get(i).getValue().toString().isEmpty()&& 
						!InTime1.get(i).getValue().toString().isEmpty() && 
						!InTime2.get(i).getValue().toString().isEmpty() && 
						!OutTime1.get(i).getValue().toString().isEmpty() &&
						!OutTime2.get(i).getValue().toString().isEmpty() && 
						!txtPermitBy.get(i).getValue().toString().isEmpty() &&
						!txtReason.get(i).getValue().toString().isEmpty())
				{
					ret=true;
					break;
				}

			}
			return ret;
		}

		private void departMentdataAdd()
		{
			Session session=SessionFactoryUtil.getInstance().openSession();
			session.beginTransaction();
			try
			{
				String query = "select distinct ein.vDepartmentId,dept.vDepartmentName from tbDepartmentInfo dept inner join " +
						"tbEmployeeInfo ein on dept.vDepartmentId=ein.vDepartmentId where dept.vDepartmentName='CHO' order by dept.vDepartmentName";
				List <?> list = session.createSQLQuery(query).list();

				for (Iterator <?> iter = list.iterator(); iter.hasNext();)
				{
					Object[] element =  (Object[]) iter.next();	
					cmbDeptName.addItem(element[0]);
					cmbDeptName.setItemCaption(element[0], element[1].toString());	
				}
			}
			catch(Exception ex)
			{
				showNotification("departMentdataAdd", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
			}
			finally
			{
				session.close();
			}
		}

		private void sectiondataAdd()
		{
			Session session=SessionFactoryUtil.getInstance().openSession();
			session.beginTransaction();
			try
			{
				String query = "select distinct ein.vSectionId,sein.SectionName from tbSectionInfo sein inner join " +
						"tbEmployeeInfo ein on sein.vSectionId=ein.vSectionId where ein.vDepartmentID='"+cmbDeptName.getValue()+"' " +
						"order by sein.SectionName";
				List <?> list = session.createSQLQuery(query).list();

				for (Iterator <?> iter = list.iterator(); iter.hasNext();)
				{
					Object[] element =  (Object[]) iter.next();	
					cmbSectionName.addItem(element[0]);
					cmbSectionName.setItemCaption(element[0], element[1].toString());	
				}
			}
			catch(Exception ex)
			{
				showNotification("sectiondataAdd", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
			}
			finally
			{
				session.close();
			}
		}

		private void updateAction() 
		{
			if (!lbProxID.get(0).getValue().toString().isEmpty()) 
			{
				btnIni(false);
				componentIni(false);
			} 
			else
			{
				showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
			}
		}

		private void refreshButtonEvent() 
		{
			componentIni(true);
			btnIni(true);
			txtClear();	
		}

		private void newButtonEvent() 
		{
			cmbDeptName.focus();
			componentIni(false);
			btnIni(false);
			txtClear();
		}

		private void saveButtonAction()
		{
			try
			{
				if (sessionBean.isUpdateable())
				{	
					MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to save information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
					mb.show(new EventListener()
					{
						public void buttonClicked(ButtonType buttonType)
						{
							if(buttonType == ButtonType.YES)
							{
								insertData();
							}
						}
					});
				}
			}
			catch(Exception ex)
			{
				showNotification("saveButtonAction.", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
			}
		}

		private void insertData ()
		{
			Session session=SessionFactoryUtil.getInstance().openSession();
			Transaction tx=session.beginTransaction();
			try
			{
				String query = "";
				String query1 = "";
				String query2="";
				String query3="";
				for(int i=0; i<lbProxID.size(); i++)
				{
					if(!txtPermitBy.get(i).getValue().toString().trim().isEmpty() && !txtReason.get(i).getValue().toString().trim().isEmpty())
					{
						if(!InTime1.get(i).getValue().toString().isEmpty() && !InTime2.get(i).getValue().toString().isEmpty() &&
								!OutTime1.get(i).getValue().toString().isEmpty() && !OutTime2.get(i).getValue().toString().isEmpty())
						{

							int in_time=Integer.parseInt(InTime1.get(i).getValue().toString().trim());

							int out_time=Integer.parseInt(OutTime1.get(i).getValue().toString().trim());

							String intime=InTime1.get(i).getValue().toString()+":"+InTime2.get(i).getValue().toString()+":00";

							String outtime=OutTime1.get(i).getValue().toString()+":"+OutTime2.get(i).getValue().toString()+":00";

							List <?> lst=session.createSQLQuery("select * from tbEmployeeMainAttendance where dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"' " +
									"and vEmployeeId='"+lblAutoEmpID.get(i).getValue().toString()+"'").list();
							session.clear();

							String AttendanceFlag="SA";

							if(!lst.isEmpty())
							{
								query = "insert into tbUDEmployeeAttendance select dDate,vEmployeeID,employeeCode,vProximityID,vEmployeeName," +
										"vDesignationID,vSectionId,vSectionName,dDate,dInTimeOne,dOutTimeOne,'','',0,'New','Present'," +
										"'"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',GETDATE()," +
										"vDesignationID,vDepartmentID,vDepartmentName from tbEmployeeMainAttendance where " +
										"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' and " +
										"dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
								System.out.println("query"+query);
								session.createSQLQuery(query).executeUpdate();
								session.clear();

								query1="update tbEmployeeMainAttendance set dInTimeOne='"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+intime+"'," +
										"dOutTimeOne='"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+outtime+"',vEditFlag='Edited'," +
										"vAttendanceFlag='"+AttendanceFlag+"' where vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' and " +
										"dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
								if(in_time>out_time)
								{
									query1="update tbEmployeeMainAttendance set dInTimeOne='"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+intime+"'," +
											"dOutTimeOne=DateAdd(dd,1,'"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+outtime+"')," +
											"vEditFlag='Edited',vAttendanceFlag='"+AttendanceFlag+"' where vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' " +
											"and dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
								}

								query2 = "insert into tbUDEmployeeAttendance select dDate,vEmployeeID,employeeCode,vProximityID,vEmployeeName," +
										"vDesignationID,vSectionId,vSectionName,dDate,dInTimeOne,dOutTimeOne,'"+txtPermitBy.get(i).getValue()+"'," +
										"'"+txtReason.get(i).getValue()+"',0,'Update','Present'," +
										"'"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',GETDATE()," +
										"vDesignationID,vDepartmentID,vDepartmentName from tbEmployeeMainAttendance where " +
										"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' and " +
										"dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";

							}
							else
							{
								query1="insert into tbEmployeeMainAttendance (dDate,vEmployeeID,employeeCode,vProximityID,vEmployeeName,vDepartmentID," +
										"vDepartmentName,vSectionId,vSectionName,vDesignationID,vDesignationName,iDesignationSerial,vShiftID,vShiftName," +
										"dInTimeOne,dOutTimeOne,vEditFlag,vAttendanceFlag,bOtStatus,ishiftStatus) values " +
										"('"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'," +
										"'"+lblAutoEmpID.get(i).getValue().toString()+"'," +
										"'"+lblempID.get(i).getValue().toString()+"'," +
										"'"+lbProxID.get(i).getValue().toString()+"'," +
										"'"+lbEmployeeName.get(i).getValue().toString()+"'," +
										"'"+lbDeptId.get(i).getValue().toString()+"'," +
										"'"+lbDeptName.get(i).getValue().toString()+"'," +
										"'"+lbSecId.get(i).getValue().toString()+"'," +
										"'"+lbSecName.get(i).getValue().toString()+"'," +
										"'"+lblDesignationID.get(i).getValue().toString()+"'," +
										"'"+lbDesignation.get(i).getValue().toString()+"'," +
										"(select designationSerial from tbDesignationInfo where designationID='"+lblDesignationID.get(i).getValue().toString()+"')," +
										"'0','General','"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+intime+"'," +
										"'"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+outtime+"'," +
										"'Edited','"+AttendanceFlag+"',(select OtStatus from tbEmployeeInfo where " +
										"vEmployeeId='"+lblAutoEmpID.get(i).getValue().toString()+"'),'')";
								if(in_time>out_time)
								{
									query1="insert into tbEmployeeMainAttendance (dDate,vEmployeeID,employeeCode,vProximityID,vEmployeeName,vDepartmentID," +
											"vDepartmentName,vSectionId,vSectionName,vDesignationID,vDesignationName,iDesignationSerial,vShiftID,vShiftName," +
											"dInTimeOne,dOutTimeOne,vEditFlag,vAttendanceFlag,bOtStatus,ishiftStatus) values " +
											"('"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'," +
											"'"+lblAutoEmpID.get(i).getValue().toString()+"'," +
											"'"+lblempID.get(i).getValue().toString()+"'," +
											"'"+lbProxID.get(i).getValue().toString()+"'," +
											"'"+lbEmployeeName.get(i).getValue().toString()+"'," +
											"'"+lbDeptId.get(i).getValue().toString()+"'," +
											"'"+lbDeptName.get(i).getValue().toString()+"'," +
											"'"+lbSecId.get(i).getValue().toString()+"'," +
											"'"+lbSecName.get(i).getValue().toString()+"'," +
											"'"+lblDesignationID.get(i).getValue().toString()+"'," +
											"'"+lbDesignation.get(i).getValue().toString()+"'," +
											"(select designationSerial from tbDesignationInfo where designationID='"+lblDesignationID.get(i).getValue().toString()+"')," +
											"'0','General','"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+intime+"'," +
											"DateAdd(dd,1,'"+DBdateformat.format(lbAttendDate.get(i).getValue())+" "+outtime+"')," +
											"'Edited','"+AttendanceFlag+"',(select OtStatus from tbEmployeeInfo where " +
											"vEmployeeId='"+lblAutoEmpID.get(i).getValue().toString()+"'),'')";
								}
								query2 = "insert into tbUDEmployeeAttendance select dDate,vEmployeeID,employeeCode,vProximityID,vEmployeeName," +
										"vDesignationID,vSectionId,vSectionName,dDate,dInTimeOne,dOutTimeOne,'"+txtPermitBy.get(i).getValue()+"'," +
										"'"+txtReason.get(i).getValue()+"',0,'Update','Absent'," +
										"'"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',GETDATE()," +
										"vDesignationID,vDepartmentID,vDepartmentName from tbEmployeeMainAttendance where " +
										"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' and " +
										"dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
							}
							session.createSQLQuery(query1).executeUpdate();
							session.createSQLQuery(query2).executeUpdate();

							List <?> chkLst=session.createSQLQuery("select dDate from tbHoliday where " +
									"dDate = '"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'").list();
							if(!chkLst.isEmpty())
							{
								int chkHoliHour=Integer.parseInt(session.createSQLQuery("select convert(varchar,DATEDIFF(HH,dInTimeOne,dOutTimeOne)) as hourdiff " +
										"from tbEmployeeMainAttendance where " +
										"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' " +
										"and dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'").list().iterator().next().toString());
								if(chkHoliHour>=5)
								{
									query3="update tbEmployeeMainAttendance set vAttendanceFlag='HP' where " +
											"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' " +
											"and dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
									System.out.println("query3 = "+query3);
									session.createSQLQuery(query3).executeUpdate();
								}
							}
							else
							{
								int chkHour=Integer.parseInt(session.createSQLQuery("select convert(varchar,DATEDIFF(HH,dInTimeOne,dOutTimeOne)) as hourdiff " +
										"from tbEmployeeMainAttendance where " +
										"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' " +
										"and dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'").list().iterator().next().toString());
								if(chkHour>=7)
								{
									query3="update tbEmployeeMainAttendance set vAttendanceFlag='PR' where " +
											"vEmployeeID='"+lblAutoEmpID.get(i).getValue().toString()+"' " +
											"and dDate='"+DBdateformat.format(lbAttendDate.get(i).getValue())+"'";
									System.out.println("query3 = "+query3);
									session.createSQLQuery(query3).executeUpdate();
								}
							}
						}
					}
				}

				tx.commit();
				showNotification("All Information Save Successfully");
				isFind = false;
				txtClear();
				componentIni(true);
				btnIni(true);
			}
			catch(Exception ex)
			{
				showNotification("insertData", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
				tx.rollback();
			}
			finally
			{
				session.close();
			}
		}

		private void formValidation()
		{
			if(cmbDeptName.getValue()!=null)
			{
				if(validTableSelect())
				{
					saveButtonAction();
				}
				else
				{
					showNotification("Warning!","Provide All Data To Table ",Notification.TYPE_WARNING_MESSAGE);
				}
			}
			else
			{
				showNotification("Warning,","Select Section Name.", Notification.TYPE_WARNING_MESSAGE);
				cmbDeptName.focus();
			}
		}

		private AbsoluteLayout buildMainLayout()
		{
			mainLayout = new AbsoluteLayout();
			mainLayout.setImmediate(true);
			mainLayout.setMargin(false);

			setWidth("1180px");
			setHeight("500px");

			lblDate = new Label();
			lblDate.setImmediate(true);
			lblDate.setWidth("-1px");
			lblDate.setHeight("-1px");
			lblDate.setValue("Date :");
			mainLayout.addComponent(lblDate, "top:20.0px;left:20.0px;");

			dDate = new PopupDateField();
			dDate.setValue(new java.util.Date());
			dDate.setWidth("110px");
			dDate.setHeight("24px");
			dDate.setResolution(PopupDateField.RESOLUTION_DAY);
			dDate.setDateFormat("dd-MM-yyyy");
			dDate.setInvalidAllowed(false);
			dDate.setImmediate(true);
			mainLayout.addComponent(dDate, "top:18.0px;left:130.0px;");

			lblDeptName = new Label();
			lblDeptName.setImmediate(true);
			lblDeptName.setWidth("-1px");
			lblDeptName.setHeight("-1px");
			lblDeptName.setValue("Department Name :");
			mainLayout.addComponent(lblDeptName, "top:45.0px;left:20.0px;");

			cmbDeptName = new ComboBox();
			cmbDeptName.setImmediate(true);
			cmbDeptName.setWidth("300px");
			cmbDeptName.setHeight("24px");
			cmbDeptName.setImmediate(true);
			cmbDeptName.setNullSelectionAllowed(false);
			mainLayout.addComponent(cmbDeptName, "top:43.0px;left:130.0px;");

			lblSectionName = new Label();
			lblSectionName.setImmediate(true);
			lblSectionName.setWidth("-1px");
			lblSectionName.setHeight("-1px");
			lblSectionName.setValue("Section Name :");
			mainLayout.addComponent(lblSectionName, "top:45.0px;left:470.0px;");

			cmbSectionName = new ComboBox();
			cmbSectionName.setImmediate(true);
			cmbSectionName.setWidth("300px");
			cmbSectionName.setHeight("24px");
			cmbSectionName.setImmediate(true);
			cmbSectionName.setNullSelectionAllowed(false);
			mainLayout.addComponent(cmbSectionName, "top:43.0px;left:590.0px;");

			chkSectionAll = new CheckBox("All");
			chkSectionAll.setImmediate(true);
			mainLayout.addComponent(chkSectionAll, "top:45.0px; left:895.0px;");
			mainLayout.addComponent(table, "top:75.0px;left:20.0px;");

			lblCl = new Label("<font color='#A00324'><b><Strong>Use 24-Hour Format<Strong></b></font>");
			lblCl.setImmediate(true);
			lblCl.setWidth("-1px");
			lblCl.setHeight("-1px");
			lblCl.setContentMode(Label.CONTENT_XHTML);
			mainLayout.addComponent(lblCl, "top:45px;left:1000px;");

			mainLayout.addComponent(button, "top:420.0px;left:420.0px;");
			return mainLayout;
		}
	}
