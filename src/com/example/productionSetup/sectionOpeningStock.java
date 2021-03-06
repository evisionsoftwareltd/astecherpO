package com.example.productionSetup;
import com.common.share.AmountField;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.SessionBean;
import com.common.share.TextRead;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.common.share.MessageBox;
import com.common.share.SessionFactoryUtil;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.example.productionSetup.ProductionFindWindow;
//import com.ibm.icu.text.SimpleDateFormat;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class sectionOpeningStock extends Window 
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
	boolean isFind =false;

	private Label lblproductionType;
	private ComboBox cmbproductionType;

	private Label lblSection ;
	private ComboBox cmbsection;
	private Label lblproductname;
	private ComboBox cmbproductname;
	private Label lblrate;
	private TextField txtrate;

	private Label  lblunitname;
	private TextField txtunitName;

	private Label lblqty;
	private AmountField amtqty;

	private Label lblAmount;
	private TextRead txtAmount;

	private InlineDateField dOpeningYear;
	private Label lblOpeningYear;
	private java.text.SimpleDateFormat df= new java.text.SimpleDateFormat("yyyy-MM-dd");
	private DecimalFormat dformat=new DecimalFormat("#0.00");
	private java.text.SimpleDateFormat datef=new java.text.SimpleDateFormat("yyyy");

	private Label lblline;
	CommonButton button = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "", "","Exit");
	String Autoid="";


	private TextField txtstepid=new TextField();
	private TextField txttypeid=new TextField();
	private TextField txtrawid=new TextField();
	private TextField txtfinishid=new TextField();
	private TextField txtyear=new TextField();
	private TextField txtsectionId=new TextField();


	ArrayList<Component> allComp = new ArrayList<Component>();

	public sectionOpeningStock(SessionBean sessionBean) 
	{
		this.sessionBean=sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("SECTION OPENING:: " + sessionBean.getCompany());
		btnIni(true);
		componentIni(true);
		setEventAction();
		button.btnNew.focus();
		productiontypedataload() ;
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
	private void focusEnter()
	{
		allComp.add(cmbproductionType);
		allComp.add(cmbsection);
		allComp.add(cmbproductname);
		allComp.add(txtunitName);
		allComp.add(amtqty);
		allComp.add(txtrate);
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
				isFind=true;
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
		
		cmbproductionType.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbproductionType.getValue()!=null)
				{
				   productDataLoad();	
				}
				
			}
		});


		cmbproductname.addListener(new ValueChangeListener() 
		{

			public void valueChange(ValueChangeEvent event) 
			{

				if(cmbproductname.getValue()!=null)
				{
					productUnit();
				}

			}
		});
		txtrate.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				//calcAmount();
			}
		});
		amtqty.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				//calcAmount();
			}
		});

	}

	private void calcAmount() {
		double qty=Double.parseDouble(amtqty.getValue().toString().isEmpty()?"0":amtqty.getValue().toString());
		double rate=Double.parseDouble(txtrate.getValue().toString().isEmpty()?"0":txtrate.getValue().toString());
		double amount=qty*rate;
		txtAmount.setValue(dformat.format(amount));
	}
	private void findButtonEvent() 
	{
		Window win = new sectionOPeningFind(sessionBean, txttypeid, txtrawid,txtyear);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtyear.getValue().toString().length() > 0)
				{
					txtClear();
					String typeid=txttypeid.getValue().toString();
					String rawid=txtrawid.getValue().toString();
					String year=txtyear.getValue().toString();


					findInitialise(typeid,rawid,year);
				}
			}
		});

		this.getParent().addWindow(win);
	}


	private void findInitialise(String typeid,String rawid,String year  ) 
	{
		Transaction tx = null;
		try 
		{
			System.out.println("I am Ok");
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			String query= " select  distinct  openignYear, productionType,productid,unit,qty  from tbsectionOpenignStock "
					+" where productionType like '"+typeid+"' and productid like '"+rawid+"'  and YEAR(openignYear) like  '"+year+"'  ";

			System.out.print("desire sql is"+query);

			List led = session.createSQLQuery(query).list();

			if (led.iterator().hasNext()) 
			{
				Object[] element = (Object[]) led.iterator().next();
				dOpeningYear.setValue(element[0]);
				cmbproductionType.setValue(element[1].toString());
				cmbproductname.setValue(element[2].toString());
				txtunitName.setValue(element[3].toString());
				amtqty.setValue(dformat.format(element[4]));
				
			}
		}
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}


	private void InitialproducUnitLoad() 
	{
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List led=null;
			if(cmbproductionType.getItemCaption(cmbproductionType.getValue()).equals("Tube Production")&& cmbsection.getItemCaption(cmbsection.getValue()).equals("Printing") )
			{
				led = session.createSQLQuery("select vRawItemCode,vRawItemName,vUnitName from tbRawItemInfo where vRawItemCode like '"+cmbproductname.getValue()+"' ").list();	
			}




			if (led.iterator().hasNext()) 
			{
				Object[] element = (Object[]) led.iterator().next();
				txtunitName.setValue(element[2].toString());

			}
		}
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}




	private void productiontypedataload() 
	{
		
		
		cmbproductionType.addItem("Dry Offset Printing");
		cmbproductionType.addItem("Screen Printing");
		cmbproductionType.addItem("Heat Trasfer Label");
		cmbproductionType.addItem("Manual Printing");
		cmbproductionType.addItem("Labeling");
		cmbproductionType.addItem("Cap Folding");
		cmbproductionType.addItem("Stretch Blow Molding");
		
		/*cmbproductionType.removeAllItems();
		String sql="select  productTypeId,productTypeName from tbProductionType";
		Iterator<?> iter=dbService(sql);
		while(iter.hasNext())
		{
			Object[] element = (Object[]) iter.next();
			cmbproductionType.addItem(element[0]);
			cmbproductionType.setItemCaption(element[0],element[1].toString());
		}*/

	}

	private void sectionDataLoad() 
	{
		
		//cmbsection.removeAllItems();
		
		/*String sql="select vSectionId,SectionName  from tbSectionInfo order by AutoID";
		Iterator<?> iter=dbService(sql);
		while(iter.hasNext()){
			Object[] element = (Object[]) iter.next();
			cmbsection.addItem(element[0]);
			cmbsection.setItemCaption(element[0],element[1].toString());
		}*/
	}

	private void productDataLoad() 
	{
		
		cmbproductname.removeAllItems();
		
		String sql="select semiFgSubId,semiFgSubName from tbSemiFgSubInformation where productionStepId='"+cmbproductionType.getValue()+"' ";
		Iterator<?>iter=dbService(sql);
		while(iter.hasNext()){
			Object[] element = (Object[]) iter.next();
			cmbproductname.addItem(element[0]);
			cmbproductname.setItemCaption(element[0], element[1].toString());
		}
	}

	private void productUnit() 
	{
		
		
		String sql="select 0,unit from  ( select semiFgCode,semiFgName,unit from tbSemiFgInfo "
				   +"union "
				   +"select semiFgSubId,semiFgSubName,'' from tbSemiFgSubInformation "
				   +")  as temp where semiFgCode ='"+cmbproductname.getValue().toString()+"' order by semiFgName " ;

		//String sql="select 0, vUnitName  from tbRawItemInfo where vRawItemCode like '"+cmbproductname.getValue().toString()+"' ";
		Iterator<?> iter=dbService(sql);
		if(iter.hasNext()){
			Object[] element = (Object[]) iter.next();
			txtunitName.setValue(element[1]);
		}
	}



	private void refreshButtonEvent()
	{
		isUpdate=false;
		componentIni(true);
		btnIni(true);
		txtClear();
	}

	/*private void saveButtonEvent() 
	{
		if(!txtGroup.getValue().toString().isEmpty())
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
							//cmbValueAdd();
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
	}*/


	private void saveButtonEvent() 
	{
		if(cmbproductionType.getValue()!=null)
		{
			if(cmbproductname.getValue()!=null  )
				{

					if(!amtqty.getValue().toString().isEmpty() )
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
										componentIni(true);
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
										button.btnNew.focus();
									}
								}
							});		
						}	
					}

					else
					{
						this.getParent().showNotification("Warning :", "Please Provide Your Desire Qty ", Notification.TYPE_WARNING_MESSAGE);	
					}


				}

				else
				{
					this.getParent().showNotification("Warning :", "Please Select Finished Goods OR Raw Materials", Notification.TYPE_WARNING_MESSAGE);
				}

		}

		else{
			this.getParent().showNotification("Warning :", "Please Select Production Type", Notification.TYPE_WARNING_MESSAGE);

		}
	}




	private void insertData()
	{
		Transaction tx = null;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			System.out.println("Username : "+sessionBean.getUserName());
			System.out.println("User IP : "+sessionBean.getUserIp());

			String productyiontype= cmbproductionType.getValue().toString();
			//String section=cmbsection.getValue().toString();
			String productid=cmbproductname.getValue().toString();
			String FinishGoods="";
			String Unit=txtunitName.getValue().toString();
			if(!txtunitName.getValue().toString().isEmpty())
			{
				Unit=txtunitName.getValue().toString().trim();	
			}

			Double qty=0.00;
			Double rate=0.00;

			if(!amtqty.getValue().toString().isEmpty())
			{
				qty=Double.parseDouble(amtqty.getValue().toString()) ;
			}
			if(!txtrate.getValue().toString().isEmpty())
			{
				rate=Double.parseDouble(txtrate.getValue().toString()) ;
			}


			String insertQuery = "Insert Into tbsectionOpenignStock (openignYear,productionType,sectionId,productid,unit,qty,userIp,userId,entryTime,RunningFlag,rate,amount,productName) values " +
					" ( '"+ df.format(dOpeningYear.getValue())+"','"+productyiontype+"','', '"+productid+"','"+Unit+"','"+qty+"'   "
					+" ,'"+sessionBean.getUserIp()+"','"+sessionBean.getUserId()+"',getdate(),'1','"+rate+"','"+txtAmount.getValue()+"','"+cmbproductname.getItemCaption(cmbproductname.getValue().toString())+"' ) ";

			System.out.println("insertQuery : "+insertQuery);
			session.createSQLQuery(insertQuery).executeUpdate();
			tx.commit();

			this.getParent().showNotification("All Information Save successfully.");
			btnIni(true);
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error ",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}

	public void updateData() 
	{
		Transaction tx = null;
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			WebApplicationContext context = ((WebApplicationContext) getApplication().getContext());
			WebBrowser webBrowser = context.getBrowser();
			sessionBean.setUserIp(webBrowser.getAddress());    

			InetAddress inetAddress =InetAddress.getByName(webBrowser.getAddress().toString());//get the host Inet using ip
			System.out.println ("Host Name: "+ inetAddress.getHostName());//display the host

			String  updatesql=" update tbsectionOpenignStock  set qty='"+amtqty.getValue().toString()+"'  where   productid like '"+cmbproductname.getValue().toString()+"' ";


			session.createSQLQuery(updatesql).executeUpdate();
			System.out.println("updateQuery : "+updatesql);
			tx.commit();
			this.getParent().showNotification("Update successfully.");
		}
		catch(Exception exp)
		{
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}	
	}

	private void updateButtonEvent()
	{
		if(cmbproductionType.getValue()!=null)
		{
			isUpdate = true;
			btnIni(false);
			//componentIni(false);
			dOpeningYear.setEnabled(false);
			cmbproductionType.setEnabled(false);
			cmbsection.setEnabled(false);
			cmbproductname.setEnabled(false);
			txtunitName.setEnabled(true);
			amtqty.setEnabled(true);
			txtrate.setEnabled(true);

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

	}

	public void newBtnData(int flag)
	{
		if(txtGroup.getValue().toString().isEmpty() || flag==1)
		{
			Transaction tx = null;
			try
			{
				Session session = SessionFactoryUtil.getInstance().getCurrentSession();
				tx = session.beginTransaction();
				String sql = "select isnull(max(cast(SUBSTRING(productTypeId,4,LEN(productTypeId))as int)),0)+1 as cat from tbProductionType";
				Iterator iter = session.createSQLQuery(sql).list().iterator();
				int num = 0;
				if (iter.hasNext()) 
				{
					num = Integer.parseInt(iter.next().toString());
					groupRead.setValue("PT-"+num);
				}
			}
			catch(Exception ex)
			{
				this.getParent().showNotification("Error",ex+"",Notification.TYPE_ERROR_MESSAGE);
			}
		}
		else
		{
			
		}
	}

	public void txtClear()
	{
		cmbproductionType.setValue(null);
		cmbsection.setValue(null);
		cmbproductname.setValue(null);
		txtunitName.setValue("");
		amtqty.setValue("");
		txtrate.setValue("");
	}

	private void componentIni(boolean b) 
	{

		dOpeningYear.setEnabled(!b);
		cmbproductionType.setEnabled(!b);
		cmbsection.setEnabled(!b);
		cmbproductname.setEnabled(!b);
		txtunitName.setEnabled(!b);
		amtqty.setEnabled(!b);
		txtrate.setEnabled(!b);
		txtAmount.setEnabled(!b);

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

		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("560px");
		setHeight("336px");

		// lblOpeningYear
		lblOpeningYear = new Label();
		lblOpeningYear.setImmediate(false);
		lblOpeningYear.setWidth("-1px");
		lblOpeningYear.setHeight("-1px");
		lblOpeningYear.setValue("Opening Year :");
		mainLayout.addComponent(lblOpeningYear, "top:40.0px;left:70.0px;");

		// dOpeningYear
		dOpeningYear = new InlineDateField();
		dOpeningYear.setImmediate(true);
		dOpeningYear.setDateFormat("yyyy");
		dOpeningYear.setWidth("-1px");
		dOpeningYear.setHeight("-1px");
		dOpeningYear.setInvalidAllowed(false);
		dOpeningYear.setResolution(6);
		mainLayout.addComponent(dOpeningYear, "top:38.0px;left:175.0px;");


		lblproductionType = new Label("Production Type :");
		lblproductionType.setImmediate(false);
		lblproductionType.setWidth("-1px");
		lblproductionType.setHeight("-1px");
		mainLayout.addComponent(lblproductionType, "top:62.0px;left:70.0px;");

		cmbproductionType= new ComboBox();
		cmbproductionType.setImmediate(true);
		cmbproductionType.setWidth("280px");
		cmbproductionType.setNullSelectionAllowed(true);
		cmbproductionType.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbproductionType, "top:64.0px;left:175.0px;");

		lblSection = new Label("Section Name :");
		lblSection.setImmediate(false);
		lblSection.setWidth("-1px");
		lblSection.setHeight("-1px");
		//mainLayout.addComponent(lblSection, "top:88.0px;left:70.0px;");

		cmbsection= new ComboBox();
		cmbsection.setImmediate(true);
		cmbsection.setWidth("280px");
		cmbsection.setNullSelectionAllowed(true);
		cmbsection.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		cmbsection.setNewItemsAllowed(false);
		//mainLayout.addComponent(cmbsection, "top:90.0px;left:175.0px;");


		lblproductname = new Label("Product Name:");
		lblproductname.setImmediate(false);
		lblproductname.setWidth("-1px");
		lblproductname.setHeight("-1px");
		mainLayout.addComponent(lblproductname, "top:92.0px;left:70.0px;");

		cmbproductname= new ComboBox();
		cmbproductname.setImmediate(true);
		cmbproductname.setWidth("280px");
		cmbproductname.setNullSelectionAllowed(true);
		cmbproductname.setNewItemsAllowed(false);
		cmbproductname.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbproductname, "top:90.0px;left:175.0px;");


		lblunitname = new Label("Unit:");
		lblunitname.setImmediate(false);
		lblunitname.setWidth("-1px");
		lblunitname.setHeight("-1px");
		mainLayout.addComponent(lblunitname, "top:118.0px;left:70.0px;");

		txtunitName = new TextField();
		txtunitName.setImmediate(false);
		txtunitName.setWidth("140px");
		txtunitName.setHeight("24px");
		mainLayout.addComponent(txtunitName, "top:116.0px;left:177.0px;");


		lblqty = new Label("Qty:");
		lblqty.setImmediate(false);
		lblqty.setWidth("-1px");
		lblqty.setHeight("-1px");
		mainLayout.addComponent(lblqty, "top:144.0px;left:70.0px;");

		amtqty = new AmountField();
		amtqty.setImmediate(false);
		amtqty.setWidth("100px");
		amtqty.setHeight("24px");
		mainLayout.addComponent(amtqty, "top:142.0px;left:177.0px;");

		lblrate = new Label("Rate:");
		lblrate.setImmediate(false);
		lblrate.setWidth("-1px");
		lblrate.setHeight("-1px");
		//mainLayout.addComponent(lblrate, "top:192.0px;left:70.0px;");

		txtrate = new AmountField();
		txtrate.setImmediate(false);
		txtrate.setWidth("100px");
		txtrate.setHeight("24px");
		//mainLayout.addComponent(txtrate, "top:194.0px;left:177.0px;");

		lblAmount = new Label("Amount:");
		lblAmount.setImmediate(false);
		lblAmount.setWidth("-1px");
		lblAmount.setHeight("-1px");
		//mainLayout.addComponent(lblAmount, "top:218.0px;left:70.0px;");

		txtAmount = new TextRead(1);
		txtAmount.setImmediate(false);
		txtAmount.setWidth("100px");
		txtAmount.setHeight("24px");
		//mainLayout.addComponent(txtAmount, "top:220.0px;left:177.0px;");

		lblline= new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setContentMode(Label.CONTENT_XHTML);
		lblline.setValue("<b><font color='#e65100'>======================================================================================================================</font></b>");
		mainLayout.addComponent(lblline, "top:245.0px;left:0.0px;");


		mainLayout.addComponent(button, "top:263.0px;left:12.0px;");

		return mainLayout;
	}
}
