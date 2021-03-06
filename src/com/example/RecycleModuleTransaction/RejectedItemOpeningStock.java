package com.example.RecycleModuleTransaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.AmountField;
import com.common.share.BtUpload;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.TextRead;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.text.DecimalFormat;

import com.common.share.*;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.terminal.gwt.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class RejectedItemOpeningStock extends Window 
{
	private CommonButtonNew button = new CommonButtonNew( "New",  "Save",  "Edit",  "",  "Refresh",  "Find", "", "Exit","","");
	private VerticalLayout btnLayout = new VerticalLayout();

	@AutoGenerated
	private AbsoluteLayout mainLayout;

	@AutoGenerated
	private InlineDateField dOpeningYear;
	@AutoGenerated
	private Label lblOpeningYear;

	@AutoGenerated
	private Label lblProductName;
	@AutoGenerated
	private ComboBox cmbProductName;


	private Label lblStore;
	private ComboBox cmbStore;

	@AutoGenerated
	private Label lblUnit;
	@AutoGenerated
	private TextRead trUnit;

	private Label lblcolor;
	@AutoGenerated
	private TextRead txtcolor;


	@AutoGenerated
	private Label lblQuantitykg;
	@AutoGenerated
	private AmountField amtQuantityKg;

	private Label lblRateKg;
	@AutoGenerated
	private AmountField amtRateKg;


	@AutoGenerated
	private Label lblQuantityPcs;
	@AutoGenerated
	private AmountField amtQuantityPcs;


	private Label lblRatePcs;
	@AutoGenerated
	private AmountField amtRatePcs;



	@AutoGenerated
	private Label lblline;

	boolean isUpdate=false;
	int index;



	private TextField txtProductID = new TextField();
	private TextField txtOpeningYear = new TextField();

	private Label lblTrnsactionDate;
	private PopupDateField dTransactionDate;

	private TextRead txtTransactionId;

	private TextField txtFindtransactionId=new TextField();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat dYFormat = new SimpleDateFormat("yyyy");
	ArrayList<Component> allComp = new ArrayList<Component>();

	private DecimalFormat df = new DecimalFormat("#0.00");

	private Formatter fmt = new Formatter();

	private SessionBean sessionBean;
	double previousDrAmount;


	public RejectedItemOpeningStock(SessionBean sessionBean)
	{
		buildMainLayout();
		this.sessionBean = sessionBean;
		this.setCaption("REJECTED ITEM OPENING STOCK INFORMATION :: "+sessionBean.getCompany());
		this.setResizable(false);
		this.setContent(mainLayout);
		buttonLayoutAdd();
		btnIni(true);
		componentIni(true);
		updateBtnFileldED(true);
		setEventAction();	
		cmbStoreload();
		focusEnter();
		productLoad();
	}

	private void focusEnter()
	{

		allComp.add(dOpeningYear);
		allComp.add(dTransactionDate);
		allComp.add(cmbProductName);
		allComp.add(amtQuantityKg);
		allComp.add(amtRateKg);
		allComp.add(amtQuantityPcs);
		allComp.add(amtRatePcs);
		allComp.add(cmbStore);



		allComp.add(button.btnNew);
		allComp.add(button.btnUpdate);
		allComp.add(button.btnSave);
		allComp.add(button.btnRefresh);
		allComp.add(button.btnDelete);
		allComp.add(button.btnFind);

		new FocusMoveByEnter(this,allComp);
	}


	private String selectAutoTransactionNo()
	{
		String transactionNo = "";
		Transaction tx = null;
		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			String query = "select ISNULL( MAX(iTransactionId),0)+1 from tbRejectedOpening";

			Iterator iter = session.createSQLQuery(query).list().iterator();

			if (iter.hasNext())
			{
				transactionNo = iter.next().toString();
			}
		} 
		catch (Exception ex) 
		{
			System.out.print(ex);
		}

		return transactionNo;
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

		button.btnUpdate.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				if(sessionBean.isUpdateable()){
					updateButtonEvent();
				}else{
					getParent().showNotification("You are not Permitted to Update", Notification.TYPE_WARNING_MESSAGE);	
				}
			}
		});

		button.btnDelete.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) 
			{

				txtClear();
			}
		});

		button.btnSave.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				if(sessionBean.isSubmitable()){
					saveButtonEvent();
				}
				else{
					getParent().showNotification("You are not Permitted to Save",Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});

		button.btnFind.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				findButtonEvent();
			}
		});

		button.btnExit.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				close();

			}
		});

		button.btnRefresh.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event){ 
				refreshButtonEvent();

			}
		});


		cmbProductName.addListener(new ValueChangeListener() {


			public void valueChange(ValueChangeEvent event)
			{
				if(cmbProductName.getValue()!=null)
				{
					prodataAction();	
				}
				

			}
		});

	}

	private void updateButtonEvent()
	{
		if(cmbProductName.getValue()!= null)
		{
			isUpdate = true;
			btnIni(false);
			updateinit(true);
		}
		else
			this.getParent().showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
	}

	private void findButtonEvent() 
	{
		Window win = new RejectedItemOpeningFind(sessionBean, txtFindtransactionId);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtFindtransactionId.getValue().toString().length() > 0)
				{
					txtClear();
					cmbProductName.removeAllItems();
					findInitialise(txtFindtransactionId.getValue().toString());
				}
			}
		});

		this.getParent().addWindow(win);
	}

	private void findInitialise(String transactionId) 
	{


		Transaction tx=null;
		try{
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();


			String sql=  
					"select iTransactionId,dTransactionDate,openingYear, "
							+"vProductId,vProductName,mQtykg,mQtyPcs,iStoreId,vUnitName,vColor,mRatekg,mRatepcs from tbRejectedOpening where iTransactionId='"+transactionId+"' ";


			System.out.println("query is"+sql);

			List list=session.createSQLQuery(sql).list();
			for(Iterator iter=list.iterator();iter.hasNext();){

				Object element[]=(Object[]) iter.next();

				txtTransactionId.setValue(element[0]);
				dTransactionDate.setValue(element[1]);
				dOpeningYear.setValue(element[2]);
				cmbProductName.addItem(element[3].toString());
				cmbProductName.setItemCaption(element[3].toString(), element[4].toString());
				cmbProductName.setValue(element[3]);
				fmt=new Formatter();
				amtQuantityKg.setValue(df.format(Double.parseDouble(element[5].toString())));
				amtRateKg.setValue(df.format(Double.parseDouble(element[10].toString())));
				amtQuantityPcs.setValue(df.format(Double.parseDouble(element[6].toString())));
				amtRatePcs.setValue(df.format(Double.parseDouble(element[11].toString())));
				trUnit.setValue(element[8]);
				txtcolor.setValue(element[9]);
				cmbStore.setValue(element[7].toString());
				
				double kgAmount, pcsAmount, drAmount = 0;
				kgAmount=0; pcsAmount=0;
				previousDrAmount=0;
				
				kgAmount=Double.parseDouble(amtRateKg.getValue().toString().isEmpty()?"0.00":amtRateKg.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityKg.getValue().toString().isEmpty()?"0.00":amtQuantityKg.getValue().toString().replaceAll(",", "")) ; 
				pcsAmount=Double.parseDouble(amtRatePcs.getValue().toString().isEmpty()?"0.00":amtRatePcs.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityPcs.getValue().toString().isEmpty()?"0.00":amtQuantityPcs.getValue().toString().replaceAll(",", "")) ; 
		         
				if(kgAmount+pcsAmount>0)
				{
					
				   if(kgAmount>0)
				   {
					 previousDrAmount=kgAmount;   
				   }
				   else if (pcsAmount>0)
				   {
					   previousDrAmount=pcsAmount;  
				   }
					
				}
					
			}
		}
		catch(Exception exp){
			this.getParent().showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
		}
	}


	private void saveButtonEvent()
	{
		System.out.print(cmbProductName.getValue());



		if(cmbProductName.getValue()!=null)
		{
			if(cmbStore.getValue()!=null)
			{
				
					if(!amtQuantityKg.getValue().toString().trim().isEmpty())
					{

						if(isUpdate)
						{
							final MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to update ?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
							mb.show(new EventListener()
							{
								public void buttonClicked(ButtonType buttonType)
								{
									if(buttonType == ButtonType.YES)
									{

										mb.buttonLayout.getComponent(0).setEnabled(false);
										updateData();
										isUpdate=false;
										componentIni(true);
										updateBtnFileldED(true);
										txtClear();
										btnIni(true);
										button.btnNew.focus();
										mb.close();


									}
								}
							});	
						}
						else
						{
							final MessageBox mb = new MessageBox(getParent(), "Are you sure?", MessageBox.Icon.QUESTION, "Do you want to Save ?", new MessageBox.ButtonConfig(MessageBox.ButtonType.YES, "Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
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
										mb.close();

									}
								}
							});	
						}
					}
					else{
						this.getParent().showNotification("Warning !","Enter Quantity.", Notification.TYPE_WARNING_MESSAGE);
						amtQuantityKg.focus();
					}
				
			}
			else{
				this.getParent().showNotification("Warning !","Select Store Name.", Notification.TYPE_WARNING_MESSAGE);
				cmbStore.focus();
			}  	
		}
		else{
			this.getParent().showNotification("Warning !","Select Product Name.", Notification.TYPE_WARNING_MESSAGE);
			cmbProductName.focus();
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

			String updateQOS = "update tbRejectedOpening set mQtykg='"+amtQuantityKg.getValue().toString()+"',mQtyPcs='"+amtQuantityPcs.getValue().toString()+"',iStoreId='"+cmbStore.getValue()+"', "
					+"vStoreName='"+cmbStore.getItemCaption(cmbStore.getValue())+"',vUserIp='"+sessionBean.getUserIp()+"', vUserId='"+sessionBean.getUserId()+"',dEntryTime=getdate(),"
					+ "  mRatekg='"+amtRateKg.getValue().toString().replaceAll(",", "")+"', mRatepcs='"+amtRatePcs.getValue().toString().replaceAll(",", "")+"'   where iTransactionId='"+txtTransactionId.getValue()+"' ";


			session.createSQLQuery(updateQOS).executeUpdate();
			
			
			double kgAmount, pcsAmount, drAmount = 0,diffAmount=0;
			kgAmount=0; pcsAmount=0;
			
			kgAmount=Double.parseDouble(amtRateKg.getValue().toString().isEmpty()?"0.00":amtRateKg.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityKg.getValue().toString().isEmpty()?"0.00":amtQuantityKg.getValue().toString().replaceAll(",", "")) ; 
			pcsAmount=Double.parseDouble(amtRatePcs.getValue().toString().isEmpty()?"0.00":amtRatePcs.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityPcs.getValue().toString().isEmpty()?"0.00":amtQuantityPcs.getValue().toString().replaceAll(",", "")) ; 
	         
			if(kgAmount+pcsAmount>0)
			{
				
			   if(kgAmount>0)
			   {
				 drAmount=kgAmount;   
			   }
			   else if (pcsAmount>0)
			   {
				   drAmount=pcsAmount;  
			   }
				
			}
			
			diffAmount=drAmount-previousDrAmount;
			
			String fsl = session.createSQLQuery("Select  [dbo].[VoucherSelect]('"+dateFormat.format(dTransactionDate.getValue())+"')").list().iterator().next().toString();
			String voucher =  "voucher"+fsl;
			
			
			//Fixed Ledger For Rejected 
			
			if (Integer.parseInt(fsl)==5)
			{
				String LedgerOpen=" update tbLedger_Op_Balance set  DrAmount=DrAmount+'"+diffAmount+"',CrAmount='0.00' ,userId='"+sessionBean.getUserId()+"' ,userIp='"+sessionBean.getUserIp()+"',entryTime=getdate() where Ledger_Id like '' ";
				System.out.println("LedgerOpen : "+LedgerOpen);
				session.createSQLQuery(LedgerOpen).executeUpdate();	
			}
			
			tx.commit();
			this.getParent().showNotification("All information update successfully.");

		}catch(Exception exp){
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

			WebApplicationContext context = ((WebApplicationContext) getApplication().getContext());
			WebBrowser webBrowser = context.getBrowser();
			sessionBean.setUserIp(webBrowser.getAddress());    

			InetAddress inetAddress =InetAddress.getByName(webBrowser.getAddress().toString());//get the host Inet using ip
			System.out.println ("Host Name: "+ inetAddress.getHostName());//display the host

			txtTransactionId.setValue(selectAutoTransactionNo());

			String insertProductOpening = "insert into tbRejectedOpening (iTransactionId,dTransactionDate,openingYear,vProductId, "
					+"vProductName,mQtykg,mQtyPcs,iStoreId,vStoreName,vUserIp,vUserId,dEntryTime,vUnitName,vColor,mRatekg,mRatepcs) values("
					+ "  '"+txtTransactionId.getValue()+"','"+dateFormat.format(dTransactionDate.getValue())+"',"
					+ "  '"+dateFormat.format(dOpeningYear.getValue())+"','"+cmbProductName.getValue()+"' ,"
					+ "  '"+cmbProductName.getItemCaption(cmbProductName.getValue())+"','"+amtQuantityKg.getValue().toString().replaceAll(",", "")+"',"
					+ "  '"+amtQuantityPcs.getValue().toString().replaceAll(",", "")+"','"+cmbStore.getValue()+"','"+cmbStore.getItemCaption(cmbStore.getValue())+"',"
					+ "  '"+sessionBean.getUserIp()+"','"+sessionBean.getUserId()+"',getdate(),'"+trUnit.getValue().toString()+"',"
					+ " '"+txtcolor.getValue().toString()+"','"+amtRateKg.getValue().toString().replaceAll(",", "")+"','"+amtRatePcs.getValue().toString()+"'  )";


			System.out.println("insertProductInfo : "+insertProductOpening);

			session.createSQLQuery(insertProductOpening).executeUpdate();

	
			
			double kgAmount, pcsAmount, drAmount = 0;
			kgAmount=0; pcsAmount=0;
			
			kgAmount=Double.parseDouble(amtRateKg.getValue().toString().isEmpty()?"0.00":amtRateKg.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityKg.getValue().toString().isEmpty()?"0.00":amtQuantityKg.getValue().toString().replaceAll(",", "")) ; 
			pcsAmount=Double.parseDouble(amtRatePcs.getValue().toString().isEmpty()?"0.00":amtRatePcs.getValue().toString().replaceAll(",", ""))* Double.parseDouble(amtQuantityPcs.getValue().toString().isEmpty()?"0.00":amtQuantityPcs.getValue().toString().replaceAll(",", "")) ; 
	         
			if(kgAmount+pcsAmount>0)
			{
				
			   if(kgAmount>0)
			   {
				 drAmount=kgAmount;   
			   }
			   else if (pcsAmount>0)
			   {
				   drAmount=pcsAmount;  
			   }
				
			}
			
			String fsl = session.createSQLQuery("Select  [dbo].[VoucherSelect]('"+dateFormat.format(dTransactionDate.getValue())+"')").list().iterator().next().toString();
			String voucher =  "voucher"+fsl;
			
			if(Integer.parseInt(fsl)==5)
			{
				String LedgerOpen=" update tbLedger_Op_Balance set  DrAmount=DrAmount+'"+drAmount+"',CrAmount='0.00' ,userId='"+sessionBean.getUserId()+"' ,userIp='"+sessionBean.getUserIp()+"',entryTime=getdate() where Ledger_Id like '' ";
				System.out.println("LedgerOpen : "+LedgerOpen);
				session.createSQLQuery(LedgerOpen).executeUpdate();	
			}
			

			tx.commit();
			this.getParent().showNotification("All information save successfully.");

		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
			tx.rollback();
		}
	}


	public String productlededger() 
	{
		String autoCode = "";

		Transaction tx = null;

		try 
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			String query="select vLedgerCode from tbRecycleItemInfo where vItemCode='"+cmbProductName.getValue()+"' ";
			System.out.println("ledgerpr"+query);
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


	private void refreshButtonEvent()
	{
		componentIni(true);
		updateBtnFileldED(true);
		btnIni(true);
		txtClear();
	}

	private void newButtonEvent() 
	{
		componentIni(false);
		updateBtnFileldED(false);
		btnIni(false);
		txtClear();
		isUpdate = false;
	}

	public void txtClear()
	{
		dOpeningYear.setValue(new java.util.Date());
		cmbProductName.setValue(null);
		trUnit.setValue("");
		txtcolor.setValue("");
		amtQuantityKg.setValue("");
		amtRateKg.setValue("");
		amtQuantityPcs.setValue("");
		amtRatePcs.setValue("");
		cmbStore.setValue(null);
		txtTransactionId.setValue("");
		previousDrAmount=0;

	}


	public void productLoad()
	{
		cmbProductName.removeAllItems();

		String groupId="";
		String subGroupId="";
		String partyId="";


		Transaction tx = null;
		try {
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();


			String query="select semiFgCode,semiFgName from tbSemiFgInfo "
					+"union "
					+"select semiFgSubId,semiFgSubName from tbSemiFgSubInformation  order by semiFgName ";


			List list = session.createSQLQuery(query).list();

			for (Iterator iter = list.iterator();iter.hasNext();) 
			{
				Object[] element = (Object[]) iter.next();
				cmbProductName.addItem(element[0].toString());
				cmbProductName.setItemCaption(element[0].toString(), element[1].toString());
			}
		}
		catch (Exception ex){
			this.getParent().showNotification("Error", ex.toString(),Notification.TYPE_ERROR_MESSAGE);
		}
	}



	public void prodataAction()
	{
		Transaction tx = null;
		try {
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();


			String sql= "select unit,color from tbSemiFgInfo   where semiFgCode='"+cmbProductName.getValue().toString()+"' "
					+"union "
					+"select '',color from tbSemiFgSubInformation   where semiFgSubId='"+cmbProductName.getValue().toString()+"' ";


			List list = session.createSQLQuery(sql).list();

			for (Iterator iter = list.iterator();iter.hasNext();) 
			{
				Object[] element = (Object[]) iter.next();
				trUnit.setValue(element[0].toString());
				txtcolor.setValue(element[1].toString());

			}
		}
		catch (Exception ex){
			this.getParent().showNotification("Error", ex.toString(),Notification.TYPE_ERROR_MESSAGE);
		}
	}




	public void cmbStoreload()
	{
		cmbStore.removeAllItems();

		Transaction tx = null;

		try 
		{
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();

			String squery= "select vDepoId, vDepoName from tbDepoInformation order by vDepoName";
			System.out.println("squery : "+squery);

			List list = session.createSQLQuery(squery).list();

			for (Iterator iter = list.iterator();iter.hasNext();) 
			{
				Object[] element = (Object[]) iter.next();
				cmbStore.addItem(element[0].toString());
				cmbStore.setItemCaption(element[0].toString(), element[1].toString());
			}
		}
		catch (Exception ex)
		{
			this.getParent().showNotification("Error", ex.toString(),Notification.TYPE_ERROR_MESSAGE);
		}
	}


	private void updateBtnFileldED(boolean b)
	{
		lblQuantitykg.setEnabled(!b);
		amtQuantityKg.setEnabled(!b);

		lblline.setEnabled(!b);
	}

	private void componentIni(boolean b) 
	{
		lblOpeningYear.setEnabled(!b);
		dOpeningYear.setEnabled(!b);
		
		lblTrnsactionDate.setEnabled(!b);
		dTransactionDate.setEnabled(!b);

		lblProductName.setEnabled(!b);
		cmbProductName.setEnabled(!b);


		lblUnit.setEnabled(!b);
		trUnit.setEnabled(!b);

		lblcolor.setEnabled(!b);
		txtcolor.setEnabled(!b);

		lblQuantitykg.setEnabled(!b);
		amtQuantityKg.setEnabled(!b);

		lblQuantityPcs.setEnabled(!b);
		amtQuantityPcs.setEnabled(!b);

		lblStore.setEnabled(!b);
		cmbStore.setEnabled(!b);
		
		lblRateKg.setEnabled(!b);
		amtRateKg.setEnabled(!b);
		
		lblRatePcs.setEnabled(!b);
		amtRatePcs.setEnabled(!b);
		
		
		lblline.setEnabled(!b);
	}

	private void updateinit(boolean b) 
	{
		lblOpeningYear.setEnabled(b);
		dOpeningYear.setEnabled(b);

		lblProductName.setEnabled(!b);
		cmbProductName.setEnabled(!b);

		lblUnit.setEnabled(!b);
		trUnit.setEnabled(!b);

		lblQuantitykg.setEnabled(b);
		amtQuantityKg.setEnabled(b);
		
		lblRateKg.setEnabled(b);
		amtRateKg.setEnabled(b);
		
		
		lblQuantityPcs.setEnabled(b);
		amtQuantityPcs.setEnabled(b);
		
		lblRatePcs.setEnabled(b);
		amtRatePcs.setEnabled(b);



		lblline.setEnabled(!b);
	}





	private void btnIni(boolean t)
	{
		button.btnNew.setEnabled(t);
		button.btnUpdate.setEnabled(t);
		button.btnSave.setEnabled(!t);
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
		mainLayout.setWidth("566px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("600px");
		setHeight("470px");

		// lblOpeningYear
		lblOpeningYear = new Label();
		lblOpeningYear.setImmediate(false);
		lblOpeningYear.setWidth("-1px");
		lblOpeningYear.setHeight("-1px");
		lblOpeningYear.setValue("Opening Year :");


		// dOpeningYear
		dOpeningYear = new InlineDateField();
		dOpeningYear.setImmediate(true);
		dOpeningYear.setDateFormat("yyyy");
		dOpeningYear.setWidth("-1px");
		dOpeningYear.setHeight("-1px");
		dOpeningYear.setInvalidAllowed(false);
		dOpeningYear.setResolution(6);

		lblTrnsactionDate = new Label("Date :");
		lblTrnsactionDate.setWidth("-1px");
		lblTrnsactionDate.setHeight("-1px");


		dTransactionDate = new PopupDateField();
		dTransactionDate.setWidth("110px");
		dTransactionDate.setDateFormat("dd-MM-yyyy");
		dTransactionDate.setValue(new java.util.Date());
		dTransactionDate.setResolution(PopupDateField.RESOLUTION_DAY);


		txtTransactionId = new TextRead();
		txtTransactionId.setImmediate(false);
		txtTransactionId.setWidth("100px");
		txtTransactionId.setHeight("22px");

		// lblProductList
		lblProductName = new Label();
		lblProductName.setImmediate(false);
		lblProductName.setWidth("-1px");
		lblProductName.setHeight("-1px");
		lblProductName.setValue("Product Name:");


		// comProductName
		cmbProductName = new ComboBox();
		cmbProductName.setImmediate(true);
		cmbProductName.setNullSelectionAllowed(false);
		cmbProductName.setNewItemsAllowed(false);
		cmbProductName.setWidth("300px");
		cmbProductName.setHeight("-1px");
		cmbProductName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		// lblUnit
		lblUnit = new Label();
		lblUnit.setImmediate(false);
		lblUnit.setWidth("-1px");
		lblUnit.setHeight("-1px");
		lblUnit.setValue("Unit :");


		// trUnit
		trUnit = new TextRead();
		trUnit.setImmediate(false);
		trUnit.setWidth("100px");
		trUnit.setHeight("22px");


		// lblUnit
		lblcolor = new Label();
		lblcolor.setImmediate(false);
		lblcolor.setWidth("-1px");
		lblcolor.setHeight("-1px");
		lblcolor.setValue("Color :");


		// trUnit
		txtcolor = new TextRead();
		txtcolor.setImmediate(false);
		txtcolor.setWidth("100px");
		txtcolor.setHeight("22px");

		// lblQuantity
		lblQuantitykg = new Label();
		lblQuantitykg.setImmediate(false);
		lblQuantitykg.setWidth("-1px");
		lblQuantitykg.setHeight("-1px");
		lblQuantitykg.setValue("Qty(kg) :");



		// amtQuantityKg
		amtQuantityKg = new AmountField();
		amtQuantityKg.setImmediate(true);
		amtQuantityKg.setWidth("102px");
		amtQuantityKg.setHeight("22px");


		// lblQuantity
		lblRateKg = new Label();
		lblRateKg.setImmediate(false);
		lblRateKg.setWidth("-1px");
		lblRateKg.setHeight("-1px");
		lblRateKg.setValue("Rate/Kg:");



		// amtQuantityKg
		amtRateKg = new AmountField();
		amtRateKg.setImmediate(true);
		amtRateKg.setWidth("102px");
		amtRateKg.setHeight("22px");



		// lblQuantity
		lblQuantityPcs = new Label();
		lblQuantityPcs.setImmediate(false);
		lblQuantityPcs.setWidth("-1px");
		lblQuantityPcs.setHeight("-1px");
		lblQuantityPcs.setValue("Qty(Pcs) :");


		// amtQuantityKg
		amtQuantityPcs = new AmountField();
		amtQuantityPcs.setImmediate(true);
		amtQuantityPcs.setWidth("102px");
		amtQuantityPcs.setHeight("22px");

		// lblQuantity
		lblRatePcs = new Label();
		lblRatePcs.setImmediate(false);
		lblRatePcs.setWidth("-1px");
		lblRatePcs.setHeight("-1px");
		lblRatePcs.setValue("Rate/Pcs:");



		// amtQuantityKg
		amtRatePcs = new AmountField();
		amtRatePcs.setImmediate(true);
		amtRatePcs.setWidth("102px");
		amtRatePcs.setHeight("22px");



		// lblStore
		lblStore = new Label();
		lblStore.setImmediate(false);
		lblStore.setWidth("-1px");
		lblStore.setHeight("-1px");
		lblStore.setValue("Store:");

		// cmbStore
		cmbStore = new ComboBox();
		cmbStore.setImmediate(true);
		cmbStore.setNullSelectionAllowed(false);
		cmbStore.setNewItemsAllowed(false);
		cmbStore.setWidth("280px");
		cmbStore.setHeight("-1px");
		cmbStore.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);



		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1");
		lblline.setHeight("-1");
		lblline.setValue("________________________________________________________________________");

		// adding components to mainLayout

		mainLayout.addComponent(lblOpeningYear, "top:30.0px;right:380.0px;");
		mainLayout.addComponent(dOpeningYear, "top:30.0px;left:194.0px;");
		
		mainLayout.addComponent(lblTrnsactionDate, "top:60.0px;right:380.0px;");
		mainLayout.addComponent(dTransactionDate, "top:60.0px;left:194.0px;");

		mainLayout.addComponent(lblProductName, "top:90.0px;right:380.0px;");
		mainLayout.addComponent(cmbProductName, "top:90.0px;left:194.0px;");

		mainLayout.addComponent(lblUnit, "top:120.0px;right:380.0px;");
		mainLayout.addComponent(trUnit, "top:120.0px;left:193.5px;");

		mainLayout.addComponent(lblcolor, "top:150.0px;right:380.0px;");
		mainLayout.addComponent(txtcolor, "top:150.0px;left:193.5px;");

		mainLayout.addComponent(lblQuantitykg,"top:180.0px;right:380.0px;");
		mainLayout.addComponent(amtQuantityKg,"top:180.0px;left:193.5px;");
		
		mainLayout.addComponent(lblRateKg,"top:210.0px;right:380.0px;");
		mainLayout.addComponent(amtRateKg,"top:210.0px;left:193.5px;");

		mainLayout.addComponent(lblQuantityPcs,"top:240.0px;right:380.0px;");
		mainLayout.addComponent(amtQuantityPcs,"top:240.0px;left:193.5px;");
		
		mainLayout.addComponent(lblRatePcs,"top:270.0px;right:380.0px;");
		mainLayout.addComponent(amtRatePcs,"top:270.0px;left:193.5px;");


		mainLayout.addComponent(lblStore, "top:300.0px;right:380.0px;");
		mainLayout.addComponent(cmbStore, "top:300.0px;left:195.0px;");


		mainLayout.addComponent(lblline,"top:330.0px; left:18.0px;");

		return mainLayout;
	}

	private void buttonLayoutAdd()
	{
		btnLayout.addComponent(button);
		mainLayout.addComponent(btnLayout,"top:370.0px;left:15px;");

	}

}
