package acc.appform.hrmModule;

import java.util.ArrayList;
import java.util.List;

import com.common.share.CommonButton;
import com.common.share.SessionBean;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.Window;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Iterator;

import com.common.share.FocusMoveByEnter;
import com.common.share.MessageBox;
import com.common.share.SessionFactoryUtil;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class SectionInformation extends Window 
{
	private AbsoluteLayout mainLayout;
	
	private Label lblAddress;
	private TextArea txtAddress;
	
	private Label lblSectionName;
	private TextField txtSectionName;
	
	private Label lblDepartmentName;
	private ComboBox cmbDepartmentName;
	
	private Label lblline;

	CommonButton cButton = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "","","Exit");
	String px="250px";
	boolean isUpdate=false;
	boolean isFind=false;
	String updateSID;
	private TextField txtSectionID = new TextField();
	private TextField txtDepartmentID = new TextField();

	SessionBean sessionBean;
	ArrayList<Component> allComp = new ArrayList<Component>();

	public SectionInformation(SessionBean sessionBean) 
	{
		this.sessionBean=sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("Section Information :: "+sessionBean.getCompany() );

		txtInit(true);
		btnIni(true);
		btnAction();
		focusEnter();
		authenticationCheck();
		cmbDepartmentNameAdd();
	}

	private void authenticationCheck()
	{
		if(!sessionBean.isSubmitable()){
			cButton.btnSave.setVisible(false);
		}
		if(!sessionBean.isUpdateable()){
			cButton.btnEdit.setVisible(false);
		}
		if(!sessionBean.isDeleteable()){
			cButton.btnDelete.setVisible(false);
		}
	}
	
	private void focusEnter()
	{
		allComp.add(cmbDepartmentName);
		allComp.add(txtSectionName);
		allComp.add(txtAddress);

		allComp.add(cButton.btnNew);
		allComp.add(cButton.btnEdit);
		allComp.add(cButton.btnSave);
		allComp.add(cButton.btnRefresh);
		allComp.add(cButton.btnDelete);
		allComp.add(cButton.btnFind);

		new FocusMoveByEnter(this,allComp);
	}

	public void btnAction()
	{
		txtSectionName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(!txtSectionName.getValue().toString().trim().isEmpty())
					sectionNameCheck();
			}
		});
		
		cButton.btnNew.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				cmbDepartmentName.focus();
				newButtonEvent();
				txtClear();
			}
		});

		cButton.btnSave.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(cmbDepartmentName.getValue()!=null)
				{
					if(!txtSectionName.getValue().toString().isEmpty())
					{
						if(sessionBean.isSubmitable())
						{
							saveButtonEvent();
						}
						else
						{
							showNotification("Warning","You have not Proper Authentication to Save!!!", Notification.TYPE_WARNING_MESSAGE);
						}
					}
					else
					{
						showNotification("Warning","Provide Section Name!!!",Notification.TYPE_WARNING_MESSAGE);
					}
				}
				else
				{
					showNotification("Warning","Please Select Department Name!!!", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});


		cButton.btnEdit.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(!txtSectionName.getValue().toString().isEmpty())
				{
					updateButtonEvent();
				}
				else
				{
					getParent().showNotification("Warning!","There are nothing to update", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		cButton.btnExit.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});

		cButton.btnRefresh.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				refreshButtonEvent();
				txtClear();
			}
		});

		cButton.btnFind.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				isFind=true;
				findButtonEvent();
			}
		});
	}
	
	private void cmbDepartmentNameAdd()
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		
		try
		{
			String query="select vDepartmentID,vDepartmentName from tbDepartmentInfo";
			List <?> lst=session.createSQLQuery(query).list();
			if(!lst.isEmpty())
			{
				for(Iterator <?> itr=lst.iterator();itr.hasNext();)
				{
					Object[] element=(Object[])itr.next();
					cmbDepartmentName.addItem(element[0]);
					cmbDepartmentName.setItemCaption(element[0], element[1].toString());
				}
			}
			else
			{
				showNotification("Warning", "No Data Found!!!", Notification.TYPE_WARNING_MESSAGE);
			}
		}
		catch (Exception exp)
		{
			showNotification("cmbDepartmentNameAdd", exp.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally
		{
			session.close();
		}
	}
	
	private void sectionNameCheck()
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		
		try
		{
			String query="select vSectionID,SectionName from tbSectionInfo where SectionName = " +
						 "'"+txtSectionName.getValue().toString().trim()+"' and vDepartmentID = " +
						 "'"+cmbDepartmentName.getValue()+"'";
			List <?> lst=session.createSQLQuery(query).list();
			if(!lst.isEmpty() && !isFind)
			{
				txtSectionName.setValue("");
				txtSectionName.focus();
				showNotification("Warning", "Section Name Already Found!!!", Notification.TYPE_WARNING_MESSAGE);
			}
		}
		catch (Exception exp)
		{
			showNotification("sectionNameCheck", exp.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally
		{
			session.close();
		}
	}
	
	private void findButtonEvent() 
	{
		Window win = new SectionFindWindow(sessionBean, txtDepartmentID, txtSectionID,"SectionId");
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtSectionID.getValue().toString().length() > 0 && txtDepartmentID.getValue().toString().length() > 0)
				{
					txtClear();
					findInitialise(txtDepartmentID.getValue().toString(),txtSectionID.getValue().toString());
				}
			}
		});

		this.getParent().addWindow(win);
	}

	private void findInitialise(String DepartmentID,String txtSectonId) 
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try 
		{
			updateSID  = txtSectonId;

			List <?> led = session.createSQLQuery("Select vDepartmentID,SectionName,Address from tbSectionInfo Where vDepartmentID = '"+DepartmentID+"' and vSectionId = '"+txtSectonId+"'").list();

			if (led.iterator().hasNext()) 
			{
				Object[] element = (Object[]) led.iterator().next();

				cmbDepartmentName.setValue(element[0]);
				txtSectionName.setValue(element[1]);
				txtAddress.setValue(element[2]);
			}
			isFind=false;
		}
		catch (Exception exp) 
		{
			showNotification("findInitialise", exp + "",Notification.TYPE_ERROR_MESSAGE);
		}
		finally
		{
			session.close();
		}
	}

	private void updateButtonEvent()
	{
		if(!txtSectionName.getValue().toString().isEmpty())
		{
			isUpdate = true;
			txtAddress.setEnabled(true);
			btnIni(false);
			txtInit(false);
		}
		else{
			showNotification("updateButtonEvent","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
		}
	}

	private void saveButtonEvent() {

		if(isUpdate)
		{
			MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to update information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
			mb.show(new EventListener()
			{
				public void buttonClicked(ButtonType buttonType)
				{
					if(buttonType == ButtonType.YES)
					{	
						updateData();
						btnIni(true);
						txtInit(true);
						txtClear();
					}
				}
			});
		}
		else{

			MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to save information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
			mb.show(new EventListener()
			{
				public void buttonClicked(ButtonType buttonType)
				{
					if(buttonType == ButtonType.YES)
					{

						insertData();
						btnIni(true);
						txtInit(true);
						txtClear();
					}
				}
			});
		}
	}

	public void updateData() 
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		Transaction tx=session.beginTransaction();
		try
		{			
			session.createSQLQuery("insert into tbUDSectionInfo (SectionName,Address,userName,userIp,entryDate,vSectionID,"
					+ "vDepartmentID,vDepartmentName,vUDFlag) select SectionName,Address,userName,userIp,entryDate,vSectionID,"
					+ "vDepartmentID,vDepartmentName,'OLD' from tbSectionInfo where vDepartmentID = '"+cmbDepartmentName.getValue()+"' "
					+ "and vSectionID = '"+txtSectionID.getValue().toString().trim()+"'").executeUpdate();
			
			session.createSQLQuery("Update tbSectionInfo set SectionName= '"+txtSectionName.getValue().toString().trim()+"', " +
					"Address='"+txtAddress.getValue().toString().trim()+"',userName = '"+sessionBean.getUserName()+"'," +
					"userIp = '"+sessionBean.getUserIp()+"', entryDate = CURRENT_TIMESTAMP where vDepartmentID = '"+cmbDepartmentName.getValue()+"' and " +
					"vSectionID = '"+updateSID+"'").executeUpdate();
			
			session.createSQLQuery("insert into tbUDSectionInfo (SectionName,Address,userName,userIp,entryDate,vSectionID,"
					+ "vDepartmentID,vDepartmentName,vUDFlag) select SectionName,Address,userName,userIp,entryDate,vSectionID,"
					+ "vDepartmentID,vDepartmentName,'UPDATE' from tbSectionInfo where vDepartmentID = '"+cmbDepartmentName.getValue()+"' "
					+ "and vSectionID = '"+txtSectionID.getValue().toString().trim()+"'").executeUpdate();
			
			tx.commit();
			showNotification("All Information Updated Successfully.");

			isUpdate=false;
		}
		catch(Exception exp){
			showNotification("updateData",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		finally
		{
			session.close();
		}
	}

	private void insertData()
	{
		sectionNameCheck();
		Session session=SessionFactoryUtil.getInstance().openSession();
		Transaction tx=session.beginTransaction();
		try
		{
			session.createSQLQuery("Insert Into tbSectionInfo(SectionName,Address,userName,userIp,entryDate," +
					"vSectionID,vDepartmentID,vDepartmentName) values('"+txtSectionName.getValue().toString().trim()+"'," +
					"'"+txtAddress.getValue().toString().trim()+"','"+sessionBean.getUserName()+"'," +
					"'"+sessionBean.getUserIp()+"',CURRENT_TIMESTAMP,'SEC-"+SectionIDCreate()+"'," +
					"'"+cmbDepartmentName.getValue()+"','"+cmbDepartmentName.getItemCaption(cmbDepartmentName.getValue())+"')").executeUpdate();
			tx.commit();
			showNotification("All Information Saved Successfully.");
			btnIni(true);
		}
		catch(Exception exp){
			showNotification("insertData",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		finally
		{
			session.close();
		}
	}

	private int SectionIDCreate()
	{
		Session session=SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		int iSectionID=0;
		try
		{
			iSectionID=Integer.parseInt(session.createSQLQuery("select ISNULL(Max(cast(substring(vSectionID,5,Len(vSectionID)) as int)),0)+1 " +
										"from tbSectionInfo").list().iterator().next().toString());
		}
		catch(Exception exp)
		{
			showNotification("Warning", exp.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally
		{
			session.close();
		}
		return iSectionID;
	}
	
	private void refreshButtonEvent() 
	{
		txtInit(true);
		btnIni(true);
	}

	private void newButtonEvent()
	{
		txtInit(false);
		btnIni(false);
	}

	private void txtClear()
	{
		cmbDepartmentName.setValue(null);
		txtSectionName.setValue("");
		txtAddress.setValue("");
	}

	private void btnIni(boolean t)
	{
		cButton.btnNew.setEnabled(t);
		cButton.btnEdit.setEnabled(t);
		cButton.btnSave.setEnabled(!t);
		cButton.btnRefresh.setEnabled(!t);
		cButton.btnDelete.setEnabled(t);
		cButton.btnFind.setEnabled(t);
	}

	public void txtInit(boolean t)
	{
		cmbDepartmentName.setEnabled(!t);
		txtSectionName.setEnabled(!t);
		txtAddress.setEnabled(!t);
	}

	private AbsoluteLayout buildMainLayout() 
	{
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("535px");
		mainLayout.setHeight("190px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("560px");
		setHeight("250px");

		lblDepartmentName= new Label();
		lblDepartmentName.setImmediate(false);
		lblDepartmentName.setWidth("-1px");
		lblDepartmentName.setHeight("-1px");
		lblDepartmentName.setValue("Department Name :");
		mainLayout.addComponent(lblDepartmentName, "top:27.0px;left:49.0px;");

		// cmbSLocation
		cmbDepartmentName = new ComboBox();
		cmbDepartmentName.setImmediate(true);
		cmbDepartmentName.setWidth("290px");
		cmbDepartmentName.setHeight("-1px");
		mainLayout.addComponent(cmbDepartmentName, "top:25.0px;left:160.0px;");
		
		// lblStoreLocation
		lblSectionName= new Label();
		lblSectionName.setImmediate(false);
		lblSectionName.setWidth("-1px");
		lblSectionName.setHeight("-1px");
		lblSectionName.setValue("Section Name :");
		mainLayout.addComponent(lblSectionName, "top:57.0px;left:49.0px;");

		// cmbSLocation
		txtSectionName = new TextField();
		txtSectionName.setImmediate(true);
		txtSectionName.setWidth("290px");
		txtSectionName.setHeight("-1px");
		mainLayout.addComponent(txtSectionName, "top:55.0px;left:160.0px;");
		
		// lblStoreLocationAddress
		lblAddress = new Label();
		lblAddress.setImmediate(false);
		lblAddress.setWidth("-1px");
		lblAddress.setHeight("-1px");
		lblAddress.setValue(" Address :");
		mainLayout.addComponent(lblAddress,"top:87.0px;left:49.0px;");

		// txtAddress
		txtAddress = new TextArea();
		txtAddress.setImmediate(false);
		txtAddress.setWidth("290px");
		txtAddress.setHeight("48px");
		mainLayout.addComponent(txtAddress, "top:85.0px;left:160.0px;");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1");
		lblline.setHeight("-1");
		lblline.setValue("________________________________________________________________________");
		mainLayout.addComponent(lblline,"top:125.0px; left:18.0px;");

		mainLayout.addComponent(cButton, "top:155.0px; left:20.0px ");		
		return mainLayout;
	}
}
