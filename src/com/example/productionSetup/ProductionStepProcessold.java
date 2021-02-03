package com.example.productionSetup;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.SessionBean;
import com.common.share.TextRead;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.*;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;
import com.common.share.MessageBox;
import com.common.share.SessionFactoryUtil;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class ProductionStepProcessold extends Window 
{
	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private Label lblSubGRoupID;
	@AutoGenerated
	private TextRead trSubGroupId ;

	@AutoGenerated
	private Label lblSubGroupName;
	@AutoGenerated
	private TextField txtSubGroupName ;

	@AutoGenerated
	private Label lblGroupName;
	@AutoGenerated
	private ComboBox cmbGroupName ;

	@AutoGenerated
	private NativeButton nbGroup;

	@AutoGenerated
	private Label lblline;

	CommonButton button = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "", "","Exit");

	private TextField txtSubGroupID = new TextField();

	private SessionBean sessionBean;
	boolean isUpdate=false;
	ArrayList<Component> allComp = new ArrayList<Component>();
	public ProductionStepProcessold(SessionBean sessionBean) 
	{
		buildMainLayout();
		this.sessionBean=sessionBean;
		this.setCaption("PRODUCTION PROCESS STEP :: " + sessionBean.getCompany());
		setContent(mainLayout);
		this.setResizable(false);
		btnIni(true);
		componentIni(true);
		cmbCategoryValueAdd();
		setEventAction();
	}

	private void focusEnter()
	{
		allComp.add(txtSubGroupName);
		allComp.add(cmbGroupName);

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
				txtSubGroupName.focus();
				newButtonEvent();
			}
		});

		button.btnEdit.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(sessionBean.isUpdateable())
				{
					updateButtonEvent();
					focusEnter();
					txtSubGroupName.focus();
				}
				else
				{
					getParent().showNotification("Warning!","You have not Proper Authentication to Edit.", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnSave.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(sessionBean.isUpdateable())
				{
					if(!txtSubGroupName.getValue().toString().isEmpty())
					{
						saveButtonEvent();
					}
					else
					{
						getParent().showNotification("Warning!","Enter Sub Category Name",Notification.TYPE_WARNING_MESSAGE);
						txtSubGroupName.focus();
					}
				}
				else
				{
					getParent().showNotification("Warning!","You have not Proper Authentication to Save.", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnFind.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				// TODO Auto-generated method stub
				findButtonEvent();
			}
		});

		button.btnRefresh.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				refreshButtonEvent();
				cmbCategoryValueAdd();
			}
		});

		button.btnExit.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});

		nbGroup.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				System.out.println("Category Form");
				gradeFormLink();				
			}
		});
		cmbGroupName.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {

				if(cmbGroupName.getValue()!=null){
					String name=cmbGroupName.getItemCaption(cmbGroupName.getValue());
					StringTokenizer token=new StringTokenizer(name);
					
					trSubGroupId.setValue(token.nextToken()+"STP-"+subGroupIdWork());
					System.out.println("Mezbah :"+subGroupIdWork());
				}
			}
		});
	}
	private String subGroupIdWork(){
		
		String id=null;
		
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			String sql="select isnull(max(cast(SUBSTRING(stepid,CHARINDEX('-',stepId)+1,len(stepId)-CHARINDEX('-',stepId)) as int)),0)+1 as cat from tbProductionStep where productionTypeId like  '"+cmbGroupName.getValue()+"'";
			
			List led = session.createSQLQuery(sql).list();

			if (led.iterator().hasNext()) 
			{
				Object element=(Object) led.iterator().next();
				id=element.toString();
				System.out.println(id);
				return id;
			}
		}
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
		
		return "";
	}
	private void findButtonEvent() 
	{
		Window win = new ProductionStepProcessFindWindow(sessionBean, txtSubGroupID);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtSubGroupID.getValue().toString().length() > 0)
				{
					txtClear();
					findInitialise(txtSubGroupID.getValue().toString());
				}
			}
		});

		this.getParent().addWindow(win);
	}

	private void findInitialise(String txtSubGroupId) 
	{
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			List led = session.createSQLQuery("select stepId,stepName,ProductionTypeId from tbProductionStep Where stepId = '"+txtSubGroupId+"'").list();

			if (led.iterator().hasNext()) 
			{
				Object[] element = (Object[]) led.iterator().next();

				//trSubGroupId.setValue(element[0]);
				txtSubGroupName.setValue(element[1]);
				cmbGroupName.setValue(element[2].toString());
				trSubGroupId.setValue(element[0]);
				
			}
		}
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}

	public void gradeFormLink()
	{
		Window win = new ProductionType(sessionBean);

		win.setStyleName("cwindow");
		win.setModal(true);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				cmbCategoryData();
				System.out.println("Category Form");
			}
		});
		this.getParent().addWindow(win);
	}

	private void cmbCategoryData()
	{
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();
			String query = "select productTypeId,ProductTypeName from tbProductionType  ";
			System.out.println(query);

			List list = session.createSQLQuery(query).list();

			for(Iterator iter = list.iterator(); iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				if (element[0] != null) {
					cmbGroupName.addItem(element[0]);
					cmbGroupName.setItemCaption(element[0], (String) element[1]);			
				}
			}
		}
		catch (Exception ex) 
		{
			this.getParent().showNotification("Error",ex.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
	}

	private void updateButtonEvent()
	{
		if(!txtSubGroupName.getValue().toString().isEmpty())
		{
			isUpdate = true;
			btnIni(false);
			componentIni(false);
			trSubGroupId.setEnabled(false);
			cmbGroupName.setEnabled(false);
		}
		else
		{
			this.getParent().showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
		}
	}

	private void refreshButtonEvent() 
	{
		isUpdate=false;
		componentIni(true);
		btnIni(true);
		txtClear();
	}

	public void updateData() 
	{
		Transaction tx = null;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			//String updateQuery = "Update tbRawSubCategory set vSubCategoryName = 'cate', iCategoryID='12',vUserName ='nd', vUserIP='12326444', dtEntryTime= CURRENT_TIMESTAMP where iSubCategoryID = '1'";
			String updateQuery = "Update tbProductionStep set stepName = '"+txtSubGroupName.getValue().toString().trim()+"', ProductionTypeId='"+cmbGroupName.getValue()+"',UserName ='"+sessionBean.getUserName()+"', UserIP='"+sessionBean.getUserIp()+"', EntryTime= CURRENT_TIMESTAMP where stepId = '"+trSubGroupId.getValue().toString().trim()+"'";
			session.createSQLQuery(updateQuery).executeUpdate();
			System.out.println("updateQuery : "+updateQuery);
			tx.commit();
			this.getParent().showNotification("Update successfully.");
			cmbCategoryValueAdd();
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error",exp.toString(),Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}	
	}

	private void saveButtonEvent() 
	{
		if(cmbGroupName.getValue()!=null)
		{
			if(isUpdate)
			{
				MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to update product information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
				mb.show(new EventListener()
				{
					public void buttonClicked(ButtonType buttonType)
					{
						if(buttonType == ButtonType.YES)
						{
							updateData();
							button.btnNew.focus();
							isUpdate=false;
							componentIni(true);
							txtClear();
							btnIni(true);
						}
					}
				});		
			}
			else
			{
				MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to Save information?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
				mb.show(new EventListener()
				{
					public void buttonClicked(ButtonType buttonType)
					{
						if(buttonType == ButtonType.YES)
						{
							insertData();
							btnIni(true);
							cmbCategoryValueAdd();
							componentIni(true);
							txtClear();
							btnIni(true);
							button.btnNew.focus();
						}
					}
				});		
			}
		}

		else
		{
			this.getParent().showNotification("Warning :", "Please select Category Name", Notification.TYPE_WARNING_MESSAGE);
			cmbGroupName.focus();
		}
	}

	private void insertData()
	{
		Transaction tx = null;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			System.out.println("Mezbah: "+cmbGroupName.getValue());
			//String insertQuery = Insert Into tbRawSubCategory (iSubCategoryID,vSubCategoryName,iCategoryID,vUserName,vUserIP,dtEntryTime) values ('5','sub Category 1','22','esl12','10.201.0232.10',CURRENT_TIMESTAMP)
			String insertQuery = "Insert Into tbProductionStep (StepId,StepName,productionTypeId,UserName,UserIP,EntryTime) values ('"+trSubGroupId.getValue().toString().trim()+"','"+txtSubGroupName.getValue().toString().trim()+"','"+cmbGroupName.getValue()+"','"+sessionBean.getUserName()+"','"+sessionBean.getUserIp()+"',CURRENT_TIMESTAMP)";
			System.out.println("insertQuery : "+insertQuery);
			session.createSQLQuery(insertQuery).executeUpdate();
			tx.commit();

			this.getParent().showNotification("All information Save successfully.");
			btnIni(true);
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}

	private void newButtonEvent() 
	{
		componentIni(false);
		btnIni(false);
		txtClear();
		//newBtnData(1);
		cmbCategoryValueAdd();
		trSubGroupId.setEnabled(false);	
	}

	public void cmbCategoryValueAdd()
	{
		cmbGroupName.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery("select productTypeId,ProductTypeName from tbProductionType").list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbGroupName.addItem(element[0]);
				cmbGroupName.setItemCaption(element[0], (String) element[1]);
			}
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error here",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}

	/*public void newBtnData(int flag)
	{
		if(txtSubGroupName.getValue().toString().isEmpty() || flag==1)
		{
			Transaction tx = null;
			try{
				Session session = SessionFactoryUtil.getInstance().getCurrentSession();
				tx = session.beginTransaction();
				String sql = "Select isnull(max(iSubGroupID)+1,1) as subcat from tbSubGroupInfo";
				Iterator iter = session.createSQLQuery(sql).list().iterator();
				int num = 0;
				if (iter.hasNext()) 
				{
					num = Integer.parseInt(iter.next().toString());
					trSubGroupId.setValue(num);
				}
			}
			catch(Exception ex)
			{
				this.getParent().showNotification("Error",ex+"",Notification.TYPE_ERROR_MESSAGE);
			}
		}
		else{}
	}*/

	public void txtClear()
	{
		trSubGroupId.setValue("");
		txtSubGroupName.setValue("");
		cmbGroupName.setValue(null);
	}

	private void componentIni(boolean b) 
	{
		lblSubGRoupID.setEnabled(!b);
		trSubGroupId.setEnabled(!b);		

		lblSubGroupName.setEnabled(!b);
		txtSubGroupName.setEnabled(!b);

		lblGroupName.setEnabled(!b);
		cmbGroupName.setEnabled(!b);

		lblline.setEnabled(!b);

		nbGroup.setEnabled(!b);
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
//		mainLayout.setWidth("530px");
//		mainLayout.setHeight("210px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("540px");
		setHeight("270px");

		// lblSubGRoupID
		lblSubGRoupID = new Label("Production Step Id :");
		lblSubGRoupID.setImmediate(false);
		lblSubGRoupID.setWidth("-1px");
		lblSubGRoupID.setHeight("-1px");
		mainLayout.addComponent(lblSubGRoupID, "top:37.0px;left:20.0px;");

		// trSubGroupId
		trSubGroupId= new TextRead();
		trSubGroupId.setImmediate(true);
		trSubGroupId.setWidth("101px");
		trSubGroupId.setHeight("24px");
		mainLayout.addComponent(trSubGroupId, "top:34.0px;left:158.0px;");

		// lblSubGroupName
		lblSubGroupName = new Label("Production Step Name :");
		lblSubGroupName.setImmediate(false);
		lblSubGroupName.setWidth("-1px");
		lblSubGroupName.setHeight("-1px");
		mainLayout.addComponent(lblSubGroupName, "top:63.0px;left:20.0px;");

		// txtSubGroupName
		txtSubGroupName = new TextField();
		txtSubGroupName.setImmediate(false);
		txtSubGroupName.setWidth("280px");
		txtSubGroupName.setHeight("-1px");
		txtSubGroupName.setSecret(false);
		mainLayout.addComponent(txtSubGroupName, "top:63.0px;left:158.0px;");
		//lblGrouopName
		lblGroupName = new Label("Production Type Name :");
		lblGroupName.setImmediate(false);
		lblGroupName.setWidth("-1px");
		lblGroupName.setHeight("-1px");
		mainLayout.addComponent(lblGroupName, "top:92.0px;left:20.0px;");

		//cmbGroupName
		cmbGroupName= new ComboBox();
		cmbGroupName.setImmediate(true);
		cmbGroupName.setWidth("280px");
		cmbGroupName.setNullSelectionAllowed(true);
		cmbGroupName.setNewItemsAllowed(false);
		mainLayout.addComponent(cmbGroupName, "top:92.0px;left:158.0px;");

		//nbGroup
		nbGroup = new NativeButton();
		nbGroup.setIcon(new ThemeResource("../icons/add.png"));
		nbGroup.setImmediate(true);
		nbGroup.setWidth("28px");
		nbGroup.setHeight("24px");
		mainLayout.addComponent(nbGroup,"top:92.0px;left:443.0px;");

		//lblline
		lblline= new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setContentMode(Label.CONTENT_XHTML);
		lblline.setValue("<b><font color='#e65100'>======================================================================================================================</font></b>");
		mainLayout.addComponent(lblline, "top:145.0px;left:0.0px;");

		mainLayout.addComponent(button, "top:180.0px;left:12.0px;");

		return mainLayout;
	}
}
