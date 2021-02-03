package acc.appform.FinishedGoodsModule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.MessageBox;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.TextRead;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class SizeInfo extends Window 
{


	private AbsoluteLayout mainLayout;

	private Label lblSizeName;
	private TextField txtSizeName;

	private Label lblSizeId;
	private TextRead txtSizeId;
	

	private Label lblSizeDes;
	private TextArea txtSizeDes;
	
	private Label lblExistSizeName;
	private Label lblline;

	CommonButton cButton = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "","","Exit");
	String px="250px";
	
	boolean isUpdate=false;
	boolean isFind=false;
	
	String updateSID;
	private TextField txtSizezID = new TextField();

	SessionBean sessionBean;
	ArrayList<Component> allComp = new ArrayList<Component>();

	public SizeInfo(SessionBean sessionBean) 
	{
		this.sessionBean=sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("SIZE INFORMATION ::" +" "+sessionBean.getCompany() );

		txtInit(true);
		btnIni(true);
		btnAction();
		focusEnter();

		authenticationCheck();
		cButton.btnNew.focus();
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
		allComp.add(txtSizeName);
		allComp.add(txtSizeDes);
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
		cButton.btnNew.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				isFind=true;
				txtSizeName.focus();
				newButtonEvent();
			}
		});

		cButton.btnSave.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(!txtSizeName.getValue().toString().isEmpty()){
					if(sessionBean.isSubmitable()){
						saveBtnAction();
					}else{
						showNotification("Warning,","You have not Proper Authentication to Save.", Notification.TYPE_WARNING_MESSAGE);
					}
				}else{
					showNotification("Provide Design Name",Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});


		cButton.btnEdit.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				txtSizeName.focus();
				if(!txtSizeName.getValue().toString().isEmpty())
				{
					isFind=false;
					updateButtonEvent();
				}
				else
				{
					getParent().showNotification("Warning!,","There are nothing to update", Notification.TYPE_WARNING_MESSAGE);
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
				txtSizezID.setValue("");
				findButtonEvent();
				isFind=false;
			}
		});
		
		txtSizeName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(isFind)
				{
					if(!txtSizeName.getValue().toString().isEmpty())
					{
						if(duplicateName())
						{
							lblExistSizeName.setVisible(true);
							lblExistSizeName.setValue("<b><Font Color='#CD0606'>! Already Exist</Font></b>");
							txtSizeName.setValue("");
							txtSizeName.focus();
						}
						else
						{	
							lblExistSizeName.setVisible(false);
						}
					}
				}
			}
		});
	}

	
	private boolean duplicateName()
	{
		String ColorName="";
		
		if(!isUpdate){
			
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			String query = " select vSizeName from tbSizeInfo where vSizeName='"+txtSizeName.getValue().toString().trim()+"' ";
			Iterator iter = session.createSQLQuery(query).list().iterator();

			if (iter.hasNext()) 
			{
				return true;
			}
		}
		catch (Exception ex) 
		{
			System.out.print(ex);
		}

		}
		return false;
	}

	
	private void findButtonEvent() 
	{
		Window win = new SizeFindWindow(sessionBean, txtSizezID,"SizezId");
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtSizezID.getValue().toString().length() > 0)
				{
					txtClear();
					findInitialise(txtSizezID.getValue().toString());
					cButton.btnEdit.focus();
				}
			}
		});

		this.getParent().addWindow(win);
	}

	private void findInitialise(String SizeID) 
	{
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			updateSID  = SizeID;

			List led = session.createSQLQuery("Select vSizeId,vSizeName,vSizeDescription from tbSizeInfo Where vSizeId = '"+SizeID+"'").list();

			if (led.iterator().hasNext()) 
			{
				Object[] element = (Object[]) led.iterator().next();

				txtSizeId.setValue(element[0]);
				txtSizeName.setValue(element[1]);
				txtSizeDes.setValue(element[2]);
			}
		}
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}

	private void updateButtonEvent()
	{
		if(!txtSizeName.getValue().toString().isEmpty())
		{
			isUpdate = true;
			btnIni(false);
			txtInit(false);
		}
		else{
			this.getParent().showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
		}
	}

	private void saveBtnAction()
	{
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
						isUpdate = false;
						txtInit(true);
						btnIni(true);
						txtClear();
						cButton.btnNew.focus();
					}
				}
			});
		}
		else
		{
			MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to save all information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
			mb.show(new EventListener()
			{
				public void buttonClicked(ButtonType buttonType)
				{
					if(buttonType == ButtonType.YES)
					{
						insertData();
						isUpdate = false;		
						txtInit(true);
						btnIni(true);
						txtClear();
						cButton.btnNew.focus();
					}
				}
			});
		}

	}

	public void updateData() 
	{
		Transaction tx = null;
		try
		{			
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			session.createSQLQuery("Update tbSizeInfo set vSizeName= '"+txtSizeName.getValue().toString().trim().replaceAll("'", "''")+"', vSizeDescription= '"+txtSizeDes.getValue().toString().trim()+"', " +
					"UserId = '"+sessionBean.getUserId()+"',userIp = '"+sessionBean.getUserIp()+"', EntryTime = CURRENT_TIMESTAMP where iAutoId = '"+updateSID+"'").executeUpdate();

			System.out.println("User IP : "+sessionBean.getUserIp());

			tx.commit();
			this.getParent().showNotification("Desire information update successfully.");

			isUpdate=false;
		}
		catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}

	private void insertData()
	{
		Transaction tx = null;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			
			session.createSQLQuery("Insert Into tbSizeInfo(vSizeId,vSizeName,vSizeDescription,UserId,UserIp,EntryTime) values('"+autoIdGenerate()+"','"+txtSizeName.getValue().toString().trim().replaceAll("'", "''")+"','"+txtSizeDes.getValue().toString().trim()+"'," +
					"'"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',CURRENT_TIMESTAMP)").executeUpdate();
			tx.commit();
			this.getParent().showNotification("All information save successfully.");
			btnIni(true);
		}
		catch(Exception exp){
			this.getParent().showNotification("Errora",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
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
		txtSizeId.setValue(autoIdGenerate());
	}

	
	private void txtClear()
	{
		txtSizeName.setValue("");
		txtSizeId.setValue("");
		txtSizeDes.setValue("");
		lblExistSizeName.setValue("");
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
		txtSizeName.setEnabled(!t);
		txtSizeId.setEnabled(!t);
		txtSizeDes.setEnabled(!t);
	}
	
	private String autoIdGenerate(){
		String autoCode = "";
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			String query = "Select isnull(max(iAutoId),0)+1 from tbSizeInfo";

			Iterator iter = session.createSQLQuery(query).list().iterator();

			if (iter.hasNext()) 
			{
				autoCode = iter.next().toString();
			}
		} 
		catch (Exception ex) 
		{
			System.out.print(ex);
		}

		return autoCode;
	}


	@AutoGenerated
	private AbsoluteLayout buildMainLayout() 
	{
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("530px");
		mainLayout.setHeight("220px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("555px");
		setHeight("260px");

		// lblStoreLocation
		lblSizeId= new Label();
		lblSizeId.setImmediate(false);
		lblSizeId.setWidth("-1px");
		lblSizeId.setHeight("-1px");
		lblSizeId.setValue("Size ID :");
		mainLayout.addComponent(lblSizeId, "top:40.0px;left:100.0px;");

		// cmbSLocation
		txtSizeId = new TextRead();
		txtSizeId.setImmediate(false);
		txtSizeId.setWidth("150px");
		txtSizeId.setHeight("23px");
		mainLayout.addComponent(txtSizeId, "top:38.0px;left:250.0px;");


		// lblStoreLocation
		lblSizeName= new Label();
		lblSizeName.setImmediate(false);
		lblSizeName.setWidth("-1px");
		lblSizeName.setHeight("-1px");
		lblSizeName.setValue("Size :");
		mainLayout.addComponent(lblSizeName, "top:70.0px;left:100.0px;");

		// cmbSLocation
		txtSizeName = new TextField();
		txtSizeName.setImmediate(true);
		txtSizeName.setWidth("-1px");
		txtSizeName.setHeight("-1px");
		mainLayout.addComponent(txtSizeName, "top:68.0px;left:250.0px;");
		

		lblExistSizeName = new Label();
		lblExistSizeName.setWidth("-1px");
		lblExistSizeName.setHeight("-1px");
		lblExistSizeName.setImmediate(true);
		lblExistSizeName.setContentMode(Label.CONTENT_XHTML);
		lblExistSizeName.setVisible(false);
		lblExistSizeName.setValue("");
		mainLayout.addComponent(lblExistSizeName, " top:70.0px;left:450.0px;");
		
		// lblStoreLocation
		lblSizeDes= new Label();
		lblSizeDes.setImmediate(false);
		lblSizeDes.setWidth("-1px");
		lblSizeDes.setHeight("-1px");
		lblSizeDes.setValue("Size Description :");
		mainLayout.addComponent(lblSizeDes, "top:100.0px;left:100.0px;");

		// cmbSLocation
		txtSizeDes = new TextArea();
		txtSizeDes.setImmediate(true);
		txtSizeDes.setWidth("250px");
		txtSizeDes.setHeight("40px");
		mainLayout.addComponent(txtSizeDes, "top:98.0px;left:250.0px;");



		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1");
		lblline.setHeight("-1");
		lblline.setValue("________________________________________________________________________");
		mainLayout.addComponent(lblline,"top:140.0px; left:18.0px;");

		mainLayout.addComponent(cButton, "top:175.0px; left:20.0px ");		
		return mainLayout;
	}
}