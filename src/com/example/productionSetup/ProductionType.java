package com.example.productionSetup;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.SessionBean;
import com.common.share.TextRead;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.common.share.MessageBox;
import com.common.share.SessionFactoryUtil;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class ProductionType extends Window 
{
	SessionBean sessionBean;
	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private TextField txtGroup;
	@AutoGenerated
	private Label lblGroup;
	@AutoGenerated
	private TextRead groupRead;
	@AutoGenerated
	private Label lblGroupId;

	boolean isUpdate=false;

	private Label lblline;
	CommonButton button = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "", "","Exit");

	private TextField txtGroupID = new TextField();
	ArrayList<Component> allComp = new ArrayList<Component>();

	public ProductionType(SessionBean sessionBean) 
	{
		this.sessionBean=sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("PRODUCTION TYPE :: " + sessionBean.getCompany());
		btnIni(true);
		componentIni(true);
		setEventAction();
		button.btnNew.focus();
	}

	private void focusEnter()
	{
		allComp.add(txtGroup);

		allComp.add(button.btnNew);
		allComp.add(button.btnSave);
		allComp.add(button.btnEdit);
		allComp.add(button.btnRefresh);
		allComp.add(button.btnFind);
		allComp.add(button.btnExit);

		new FocusMoveByEnter(this,allComp);
	}

	public void setEventAction()
	{
		button.btnNew.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				focusEnter();
				newButtonEvent();
			}
		});

		button.btnEdit.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				if(sessionBean.isUpdateable()){
					updateButtonEvent();
					txtGroup.focus();
				}else{
					getParent().showNotification("You are not Permitted to Update",Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnSave.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				if(sessionBean.isSubmitable()){
					saveButtonEvent();
				}else{
					getParent().showNotification("You are not Permitted to Save",Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnFind.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				txtClear();
				findButtonEvent();
			}
		});

		button.btnRefresh.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
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

	private void findButtonEvent() 
	{
		Window win = new ProductionFindWindow(sessionBean, txtGroupID);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtGroupID.getValue().toString().length() > 0)
				{
					txtClear();
					findInitialise(txtGroupID.getValue().toString());
				}
			}
		});

		this.getParent().addWindow(win);
	}


	private void findInitialise(String txtGroupId) 
	{
		String sql="select productTypeId,ProductTypeName from tbProductionType  where productTypeId = '"+txtGroupId+"'";
		Iterator<?> iter=dbService(sql);
		if(iter.hasNext()){
			Object[] element = (Object[]) iter.next();

			groupRead.setValue(element[0]);
			txtGroup.setValue(element[1]);
		}
	}

	private void refreshButtonEvent()
	{
		isUpdate=false;
		componentIni(true);
		btnIni(true);
		txtClear();
	}

	private void saveButtonEvent() 
	{
		if(!txtGroup.getValue().toString().isEmpty())
		{
			if(isUpdate)
			{
				final MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to update product information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
				mb.show(new EventListener()
				{
					public void buttonClicked(ButtonType buttonType)
					{
						if(buttonType == ButtonType.YES)
						{
							mb.buttonLayout.getComponent(0).setEnabled(false);
							updateData();
							button.btnNew.focus();
							isUpdate=false;
							txtClear();
							btnIni(true);
						}
					}
				});		
			}
			else
			{
				final MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to Save information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
				mb.show(new EventListener()
				{
					public void buttonClicked(ButtonType buttonType)
					{
						if(buttonType == ButtonType.YES)
						{
							mb.buttonLayout.getComponent(0).setEnabled(false);
							insertData();
							btnIni(true);
							componentIni(true);
							txtClear();
							btnIni(true);
							button.btnNew.focus();
						}
					}
				});		
			}
		}

		else{
			this.getParent().showNotification("Warning :", "Please select Group Name", Notification.TYPE_WARNING_MESSAGE);
			txtGroup.focus();
		}
	}


	private void insertData()
	{
		Transaction tx=null;
		Session session=null;
		try{
			session=SessionFactoryUtil.getInstance().openSession();
			tx=session.beginTransaction();
			String insertQuery = "Insert Into tbProductionType (productTypeId,productTypeName,UserName,UserIP,EntryTime)" +
					" values ('"+groupRead.getValue()+"','"+txtGroup.getValue()+"','"+sessionBean.getUserName()+"'," +
					"'"+sessionBean.getUserIp()+"',CURRENT_TIMESTAMP)";
			session.createSQLQuery(insertQuery).executeUpdate();
		}
		catch(Exception exp){
			showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		finally{
			if(session!=null||tx!=null){
				tx.commit();
				session.close();
				this.getParent().showNotification("All Information Save successfully.");
				btnIni(true);
			}
		}
	}

	public void updateData() 
	{
		Transaction tx=null;
		Session session=null;
		try{
			session=SessionFactoryUtil.getInstance().openSession();
			tx=session.beginTransaction();
			WebApplicationContext context = ((WebApplicationContext) getApplication().getContext());
			WebBrowser webBrowser = context.getBrowser();
			sessionBean.setUserIp(webBrowser.getAddress());    

			InetAddress inetAddress =InetAddress.getByName(webBrowser.getAddress().toString());//get the host Inet using ip
			String updateQuery = "Update tbProductionType set ProductTypeName = '"+txtGroup.getValue().toString().trim()+"', UserName ='"+sessionBean.getUserName()+"', UserIP='"+inetAddress.getHostName()+"', EntryTime= CURRENT_TIMESTAMP where productTypeId = '"+groupRead.getValue()+"'";
			session.createSQLQuery(updateQuery).executeUpdate();
		}
		catch(Exception exp){
			showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		finally{
			if(session!=null||tx!=null){
				tx.commit();
				session.close();
				this.getParent().showNotification("All Information Save successfully.");
				btnIni(true);
			}
		}
	}

	private void updateButtonEvent()
	{
		if(!txtGroup.getValue().toString().isEmpty())
		{
			isUpdate = true;
			btnIni(false);
			componentIni(false);//Enable(true);
			groupRead.setEnabled(false);
		}
		else
		{
			this.getParent().showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
		}
	}

	private void newButtonEvent() 
	{
		componentIni(false);
		btnIni(false);
		txtClear();
		newBtnData(1);
		txtGroup.focus();
		groupRead.setEnabled(false);
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
			tx.rollback();
			showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
		}
		finally{
			if(tx!=null||session!=null){
				tx.commit();
				session.close();
			}
		}
		return null;
	}
	public void newBtnData(int flag)
	{
		if(txtGroup.getValue().toString().isEmpty() || flag==1)
		{
			String sql = "select isnull(max(cast(SUBSTRING(productTypeId,4,LEN(productTypeId))as int)),0)+1 as cat from tbProductionType";
			Iterator<?> iter=dbService(sql);
			int num = 0;
			if (iter.hasNext()) 
			{
				num = Integer.parseInt(iter.next().toString());
				groupRead.setValue("PT-"+num);
			}
		}
	}

	public void txtClear()
	{
		txtGroup.setValue("");
		groupRead.setValue("");
	}

	private void componentIni(boolean b) 
	{
		lblline.setEnabled(!b);
		groupRead.setEnabled(!b);
		txtGroup.setEnabled(!b);
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

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() 
	{
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);

		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("540px");
		setHeight("220px");

		// lblGroupId
		lblGroupId = new Label("Production Type ID :");
		lblGroupId.setImmediate(false);
		lblGroupId.setWidth("-1px");
		lblGroupId.setHeight("-1px");
		mainLayout.addComponent(lblGroupId, "top:40.0px;left:67.0px;");

		// categoryRead
		groupRead = new TextRead();
		groupRead.setImmediate(false);
		groupRead.setWidth("101px");
		groupRead.setHeight("24px");
		mainLayout.addComponent(groupRead, "top:34.0px;left:185.0px;");

		// lblGroup
		lblGroup = new Label("Production Type Name :");
		lblGroup.setImmediate(false);
		lblGroup.setWidth("-1px");
		lblGroup.setHeight("-1px");
		mainLayout.addComponent(lblGroup, "top:64.0px;left:45.0px;");

		// txtGroup
		txtGroup = new TextField();
		txtGroup.setImmediate(false);
		txtGroup.setWidth("280px");
		txtGroup.setHeight("-1px");
		txtGroup.setSecret(false);
		mainLayout.addComponent(txtGroup, "top:60.0px;left:185.0px;");

		lblline= new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setContentMode(Label.CONTENT_XHTML);
		lblline.setValue("<b><font color='#e65100'>======================================================================================================================</font></b>");
		mainLayout.addComponent(lblline, "top:105.0px;left:0.0px;");

		mainLayout.addComponent(button, "top:135.0px;left:12.0px;");

		return mainLayout;
	}
}
