package com.example.productionTransaction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.common.share.AmountField;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.MessageBox;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.TextRead;
import com.common.share.MessageBox.ButtonType;
import com.common.share.MessageBox.EventListener;
import com.vaadin.data.Property.*;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.Notification;

public class IssueToLabelingPrinting extends Window {

	private TextField txtItemID = new TextField();
	private AbsoluteLayout mainLayout;
	SessionBean sessionBean;
	CommonButton cButton = new CommonButton("New", "Save", "Edit", "", "Refresh", "Find", "", "","","Exit");
	boolean isUpdate=false,isFind=false;

	private Label lblIssueTo,lblBatchNo,lblReqNo,lblIssueDate,lblIssueNo,lblReqDate;
	private ComboBox cmbIssueTo,cmbBatchNo;
	private TextRead txtReqNo,txtIssueNo;
	private PopupDateField dIssueDate,dReqDate;

	Table table = new Table();
	ArrayList<Label>tbSl = new ArrayList<Label>();
	ArrayList<ComboBox>tbCmbItemName = new ArrayList<ComboBox>();
	
	
	ArrayList<Label>tbColor =  new ArrayList<Label>();
	ArrayList<Label>tbUnit =  new ArrayList<Label>();
	ArrayList<TextRead>tbStoreStock =  new ArrayList<TextRead>();
	ArrayList<TextRead>tbSectionStock =  new ArrayList<TextRead>();
	ArrayList<TextRead>tbReqQty =  new ArrayList<TextRead>();
	ArrayList<TextRead>tbIssuedQty =  new ArrayList<TextRead>();
	ArrayList<TextRead>tbRemainQty =  new ArrayList<TextRead>();
	ArrayList<AmountField>tbIssueQty = new ArrayList<AmountField>();
	ArrayList<TextField>tbRemarks=new ArrayList<TextField>();
	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df=new DecimalFormat("#0");
	public IssueToLabelingPrinting(SessionBean sessionBean,String caption,int a){
		this.sessionBean=sessionBean;
		this.setResizable(false);
		this.setCaption("ISSUE TO LABEL/PRINTING/CAP FOLDING/SHRINK/ASSEMBLE :: "+sessionBean.getCompany());
		buildMainLayout();
		setContent(mainLayout);
		setEventAction();
		componentIni(true);
		btnIni(true);
		focusEnter();
	}

	private void focusEnter() {
		ArrayList<Component> focusComp = new ArrayList<Component>();

		focusComp.add(cmbIssueTo);
		focusComp.add(cmbBatchNo);

		for(int i = 0; i < tbSl.size(); i++)
		{
			focusComp.add(tbCmbItemName.get(i));
			focusComp.add(tbIssueQty.get(i));
			focusComp.add(tbRemarks.get(i));
		}

		focusComp.add(cButton.btnNew);
		focusComp.add(cButton.btnEdit);
		focusComp.add(cButton.btnSave);
		focusComp.add(cButton.btnRefresh);
		focusComp.add(cButton.btnDelete);
		focusComp.add(cButton.btnFind);

		new FocusMoveByEnter(this, focusComp);
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
	private Iterator dbService(String sql){
		Session session=null;
		Iterator iter=null;
		try{
			session=SessionFactoryUtil.getInstance().openSession();
			iter= session.createSQLQuery(sql).list().iterator();
		}
		catch(Exception exp){
			showNotification(exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
		finally{
			if(session!=null){
				session.close();
			}
		}
		return iter;
	}
	protected void refreshButtonEvent()
	{
		isFind=false;
		isUpdate=false;
		componentIni(true);
		btnIni(true);
		txtClear();
	}

	private void txtClear() {
		cmbIssueTo.setValue(null);
		cmbBatchNo.setValue(null);
		txtReqNo.setValue("");
		dIssueDate.setValue(new java.util.Date());
		dReqDate.setValue(new java.util.Date());
		txtIssueNo.setValue("");
		tableClear();
	}

	private void tableClear() {
		for(int a=0;a<tbSl.size();a++){
			//tbCmbItemName.get(a).removeAllItems();
			tbCmbItemName.get(a).setValue(null);
			tbUnit.get(a).setValue("");
			tbColor.get(a).setValue("");
			tbStoreStock.get(a).setValue("");
			tbSectionStock.get(a).setValue("");
			tbReqQty.get(a).setValue("");
			tbIssuedQty.get(a).setValue("");
			tbRemainQty.get(a).setValue("");
			tbIssueQty.get(a).setValue("");
			tbRemarks.get(a).setValue("");
		}
	}

	protected void newButtonEvent() 
	{
		componentIni(false);
		btnIni(false);
		txtClear();
		//txtFinishItemName.setValue("FI-"+selectFinishItemName());
		cmbIssueTo.focus();
	}
	private boolean doubleEntryCheck(int ar){
		for(int a=0;a<tbSl.size();a++){
			if(a!=ar){
				if(tbCmbItemName.get(a).getValue()!=null){
					if(tbCmbItemName.get(ar).getValue().toString().equalsIgnoreCase(tbCmbItemName.get(a).getValue().toString())){
						return false;
					}
				}
			}
		}
		return true;
	}
	private void componentIni(boolean b) {
		cmbIssueTo.setEnabled(!b);
		cmbBatchNo.setEnabled(!b);
		txtReqNo.setEnabled(!b);
		dIssueDate.setEnabled(!b);
		dReqDate.setEnabled(!b);
		txtIssueNo.setEnabled(!b);
		table.setEnabled(!b);
	}
	private void updateButtonEvent(){
		if(!txtIssueNo.getValue().toString().isEmpty())
		{
			btnIni(false);
			componentIni(false);//Enable(true);
		}
		else
		{
			this.getParent().showNotification("Update Failed","There are no data for update.",Notification.TYPE_WARNING_MESSAGE);
		}
	}
	private void setEventAction() {
		cButton.btnNew.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				isUpdate = false;
				isFind=false;
				newButtonEvent();
			}
		});

		cButton.btnEdit.addListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event)
			{
				if(sessionBean.isUpdateable()){
					isUpdate = true;
					updateButtonEvent();
				}
				else{
					getParent().showNotification("You are not Permitted to Edit",Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});
		cButton.btnExit.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				close();
			}
		});

		cButton.btnRefresh.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				refreshButtonEvent();
			}
		});
		cButton.btnSave.addListener(new Button.ClickListener()
		{
			public void buttonClick(ClickEvent event) 
			{
				if(checkValidation()){
					saveButtonEvent();
				}
			}
		});
		cButton.btnFind.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				isFind=true;
				isUpdate=true;
				findButtonEvent();
			}
		});
		cmbIssueTo.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				if(cmbIssueTo.getValue()!=null)
				{
					cmbBatchNoLoad();
				}
				else{
					cmbBatchNo.removeAllItems();
				}
			}
		});
		cmbBatchNo.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {
				if(cmbBatchNo.getValue()!=null){
					reqIssueNoSet();
					tableClear();
					tableCmbItemDataLoad();
				}
				else{
					tableClear();
					txtReqNo.setValue("");
					txtIssueNo.setValue("");
				}
			}
		});
		
		dIssueDate.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				tableClear();
				
			}
		});
		
	}
	public boolean deleteData(Session session, Transaction tx) 
	{
		try 
		{
			session.createSQLQuery("delete from tbIssueToLabelPrintingInfo where issueNo='"+txtIssueNo.getValue()+ "'").executeUpdate();
			///System.out.println("delete tbLabelProductionInfo where ProductionNo='"+txtProductionNo.getValue()+ "' ");

			session.createSQLQuery("delete from tbIssueToLabelPrintingDetails where issueNo='"+txtIssueNo.getValue()+ "'").executeUpdate();
			//System.out.println("delete tbLabelProductionDetails where ProductionNo='"+txtProductionNo.getValue()+"' ");
			//session.createSQLQuery(" delete from tbMouldFinishProduct where ProductionNo like '"+txtProductionNo.getValue().toString()+"' ").executeUpdate();
			if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble")){
				session.createSQLQuery("delete from tbIssueToAssembleInfo where issueNo='"+txtIssueNo.getValue()+ "'").executeUpdate();
				session.createSQLQuery("delete from tbIssueToAssembleDetails where issueNo='"+txtIssueNo.getValue()+ "'").executeUpdate();
			}
			return true;
		} 
		catch (Exception exp) 
		{
			this.getParent().showNotification("Error", exp + "",Notification.TYPE_ERROR_MESSAGE);
			return false;
		}
	}
	private void saveButtonEvent() 
	{
		if (isUpdate) 
		{
			final MessageBox mb = new MessageBox(getParent(), "Are you sure?",MessageBox.Icon.QUESTION, "Do you want to update ?",new MessageBox.ButtonConfig(MessageBox.ButtonType.YES,"Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
			mb.show(new EventListener() 
			{
				public void buttonClicked(ButtonType buttonType) 
				{
					if (buttonType == ButtonType.YES) 
					{
						mb.buttonLayout.getComponent(0).setEnabled(false);
						Transaction tx = null;
						Session session = SessionFactoryUtil.getInstance().getCurrentSession();

						tx = session.beginTransaction();

						if (deleteData(session, tx))
						{
							insertData();
						}
						else 
						{
							tx.rollback();
						}
						isUpdate=false;
						isFind=false;
						componentIni(true);
						btnIni(true);
						tableClear();
						txtClear();
					}
				}
			});
		} 
		else
		{
			final MessageBox mb = new MessageBox(getParent(), "Are you sure?",MessageBox.Icon.QUESTION, "Do you want to Save ?",new MessageBox.ButtonConfig(MessageBox.ButtonType.YES,"Yes"), new MessageBox.ButtonConfig(MessageBox.ButtonType.NO, "No"));
			mb.show(new EventListener() 
			{
				public void buttonClicked(ButtonType buttonType) 
				{
					if(buttonType == ButtonType.YES)
					{
						mb.buttonLayout.getComponent(0).setEnabled(false);
						insertData();
						componentIni(true);
						btnIni(true);
						tableClear();
						txtClear();							
					}
				}
			});
		}

	}
	private void insertData(){
		Transaction tx = null;

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		tx = session.beginTransaction();
		String issueNo="";
		String type="New";
		if(isUpdate){
			type="Update";
		}
		else{
			issueNoLoad();
		}
		try{
			String sqlInfo="insert into tbIssueToLabelPrintingInfo(issueTo,batchNo,reqNo,issueNo,reqDate,issueDate, "+
					" userIp,userName,entryTime,isAdjust)values('"+cmbIssueTo.getValue()+"','"+cmbBatchNo.getValue()+"'," +
					"'"+txtReqNo.getValue()+"','"+txtIssueNo.getValue()+"','"+dateFormat.format(dReqDate.getValue())+"'," +
					"'"+dateFormat.format(dReqDate.getValue())+"','"+sessionBean.getUserIp()+"'," +
					"'"+sessionBean.getUserName()+"',CURRENT_TIMESTAMP,0)";
			session.createSQLQuery(sqlInfo).executeUpdate();
			
			if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble"))
			{
				String sqlInfo1="insert into tbIssueToAssembleInfo(issueTo,batchNo,reqNo,issueNo,reqDate,issueDate, "+
						" userIp,userName,entryTime,isAdjust)values('"+cmbIssueTo.getValue()+"','"+cmbBatchNo.getValue()+"'," +
						"'"+txtReqNo.getValue()+"','"+txtIssueNo.getValue()+"','"+dateFormat.format(dReqDate.getValue())+"'," +
						"'"+dateFormat.format(dReqDate.getValue())+"','"+sessionBean.getUserIp()+"'," +
						"'"+sessionBean.getUserName()+"',CURRENT_TIMESTAMP,0)";
				session.createSQLQuery(sqlInfo1).executeUpdate();
			}

			for(int a=0;a<tbSl.size();a++){
				if(tbCmbItemName.get(0).getValue()!=null&&Double.parseDouble("0"+tbIssueQty.get(a).getValue().toString())>0){
					String sqlDetails="insert into tbIssueToLabelPrintingDetails(issueNo,productId,productName,unit,color,ReqQty, "+
							" issuedQty,remainQty,issueQty,remarks,isAdjust,batchNo)values('"+txtIssueNo.getValue()+"','"+tbCmbItemName.get(a).getValue()+"'," +
							"'"+tbCmbItemName.get(a).getItemCaption(tbCmbItemName.get(a).getValue())+"','"+tbUnit.get(a).getValue()+"'," +
							"'"+tbColor.get(a).getValue()+"','"+tbReqQty.get(a).getValue()+"','"+tbIssuedQty.get(a).getValue()+"'," +
							"'"+tbRemainQty.get(a).getValue()+"','"+tbIssueQty.get(a).getValue()+"','"+tbRemarks.get(a).getValue()+"',0,'"+cmbBatchNo.getValue()+"')";

					session.createSQLQuery(sqlDetails).executeUpdate();
					if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble")){
						String sqlDetails1="insert into tbIssueToAssembleDetails(issueNo,productId,productName,unit,color,ReqQty, "+
								" issuedQty,remainQty,issueQty,remarks,isAdjust,batchNo)values('"+txtIssueNo.getValue()+"','"+tbCmbItemName.get(a).getValue()+"'," +
								"'"+tbCmbItemName.get(a).getItemCaption(tbCmbItemName.get(a).getValue())+"','"+tbUnit.get(a).getValue()+"'," +
								"'"+tbColor.get(a).getValue()+"','"+tbReqQty.get(a).getValue()+"','"+tbIssuedQty.get(a).getValue()+"'," +
								"'"+tbRemainQty.get(a).getValue()+"','"+tbIssueQty.get(a).getValue()+"','"+tbRemarks.get(a).getValue()+"',0,'"+cmbBatchNo.getValue()+"')";

						session.createSQLQuery(sqlDetails1).executeUpdate();
					}
				}
			}

			String sqlInfoUd="insert into tbUdIssueToLabelPrintingInfo(issueTo,batchNo,reqNo,issueNo,reqDate,issueDate, "+
					" userIp,userName,entryTime,type)values('"+cmbIssueTo.getValue()+"','"+cmbBatchNo.getValue()+"'," +
					"'"+txtReqNo.getValue()+"','"+txtIssueNo.getValue()+"','"+dateFormat.format(dReqDate.getValue())+"'," +
					"'"+dateFormat.format(dReqDate.getValue())+"','"+sessionBean.getUserIp()+"','"+sessionBean.getUserName()+"',CURRENT_TIMESTAMP,'"+type+"')";
			session.createSQLQuery(sqlInfoUd).executeUpdate();
			
			if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble"))
			{
				String sqlInfoUd1="insert into tbUdIssueToAssembleInfo(issueTo,batchNo,reqNo,issueNo,reqDate,issueDate, "+
						" userIp,userName,entryTime,type)values('"+cmbIssueTo.getValue()+"','"+cmbBatchNo.getValue()+"'," +
						"'"+txtReqNo.getValue()+"','"+txtIssueNo.getValue()+"','"+dateFormat.format(dReqDate.getValue())+"'," +
						"'"+dateFormat.format(dReqDate.getValue())+"','"+sessionBean.getUserIp()+"','"+sessionBean.getUserName()+"',CURRENT_TIMESTAMP,'"+type+"')";
				session.createSQLQuery(sqlInfoUd1).executeUpdate();
			}

			for(int a=0;a<tbSl.size();a++){
				if(tbCmbItemName.get(0).getValue()!=null&&Double.parseDouble("0"+tbIssueQty.get(a).getValue().toString())>0){
					String sqlDetailsUd="insert into tbUdIssueToLabelPrintingDetails(issueNo,productId,productName,unit,color,ReqQty, "+
							" issuedQty,remainQty,issueQty,remarks,type)values('"+txtIssueNo.getValue()+"','"+tbCmbItemName.get(a).getValue()+"'," +
							"'"+tbCmbItemName.get(a).getItemCaption(tbCmbItemName.get(a).getValue())+"','"+tbUnit.get(a).getValue()+"'," +
							"'"+tbColor.get(a).getValue()+"','"+tbReqQty.get(a).getValue()+"','"+tbIssuedQty.get(a).getValue()+"'," +
							"'"+tbRemainQty.get(a).getValue()+"','"+tbIssueQty.get(a).getValue()+"','"+tbRemarks.get(a).getValue()+"','"+type+"')";

					session.createSQLQuery(sqlDetailsUd).executeUpdate();
					if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble"))
					{
						String sqlDetailsUd1="insert into tbUdIssueToAssembleDetails(issueNo,productId,productName,unit,color,ReqQty, "+
								" issuedQty,remainQty,issueQty,remarks,type)values('"+txtIssueNo.getValue()+"','"+tbCmbItemName.get(a).getValue()+"'," +
								"'"+tbCmbItemName.get(a).getItemCaption(tbCmbItemName.get(a).getValue())+"','"+tbUnit.get(a).getValue()+"'," +
								"'"+tbColor.get(a).getValue()+"','"+tbReqQty.get(a).getValue()+"','"+tbIssuedQty.get(a).getValue()+"'," +
								"'"+tbRemainQty.get(a).getValue()+"','"+tbIssueQty.get(a).getValue()+"','"+tbRemarks.get(a).getValue()+"','"+type+"')";

						session.createSQLQuery(sqlDetailsUd1).executeUpdate();
						
					}
				}
			}
			showNotification("All Information Save Successfully",Notification.TYPE_WARNING_MESSAGE);
			tx.commit();
		}
		catch(Exception exp){
			tx.rollback();
			showNotification(""+exp,Notification.TYPE_ERROR_MESSAGE);
		}
	}
	private boolean checkValidation(){
		if(cmbIssueTo.getValue()!=null){
			if(cmbBatchNo.getValue()!=null){
				if(tbCmbItemName.get(0).getValue()!=null&&
						Double.parseDouble("0"+tbIssueQty.get(0).getValue().toString())>0){
					return true;
				}
				else{
					showNotification("Please Provide All Data",Notification.TYPE_WARNING_MESSAGE);
				}
			}
			else{
				showNotification("Please Provide Batch No",Notification.TYPE_WARNING_MESSAGE);
			}
		}
		else{
			showNotification("Please Provide Issue To",Notification.TYPE_WARNING_MESSAGE);
		}

		return false;
	}
	private void tableCmbItemDataLoad() 
	{
		/*for(int a=0;a<tbSl.size();a++){
			tbCmbItemName.get(a).removeAllItems();
			Iterator iter=dbService("select  b.productId,b.productName "+
					" from tbRequisitionEntryLabelingPrintingInfo a "+
					" inner join tbRequisitionEntryLabelingPrintingDetails b "+ 
					" on a.batchNo=b.batchNo where a.batchNo='"+cmbBatchNo.getValue()+"'  order by b.productName");
			while(iter.hasNext()){
				Object element[]=(Object[])iter.next();
				tbCmbItemName.get(a).addItem(element[0]);
				tbCmbItemName.get(a).setItemCaption(element[0], element[1].toString());
			}
		}*/
		//tbCmbItemName.get(a).removeAllItems();
		
		
		Iterator iter=dbService("select  b.productId,b.productName "+
				" from tbRequisitionEntryLabelingPrintingInfo a "+
				" inner join tbRequisitionEntryLabelingPrintingDetails b "+ 
				" on a.batchNo=b.batchNo where a.batchNo='"+cmbBatchNo.getValue()+"' and b.productId not like '%RI%'  order by b.productName");
		int a=0;
		while(iter.hasNext())
		{
			Object element[]=(Object[])iter.next();
			tbCmbItemName.get(a).addItem(element[0]);
			tbCmbItemName.get(a).setItemCaption(element[0], element[1].toString());
			tbCmbItemName.get(a).setValue(element[0]);
			
			if(a==tbColor.size()-1)
			{
				tableRowAdd(a+1);
			}
			a++;
		}
		for(int x=0;x<a;x++)
		{
			tbCmbAction(x);
		}
		tbIssueQty.get(0).focus();
	}
	
	
	private void reqIssueNoSet() 
	{
		Iterator iter=dbService("select 0,ReqNo from tbRequisitionEntryLabelingPrintingInfo where ReqFrom like" +
				" '"+cmbIssueTo.getValue()+"' and batchNo='"+cmbBatchNo.getValue()+"'");
		if(iter.hasNext()){
			Object element[]=(Object[])iter.next();
			txtReqNo.setValue(element[1]);
		}

	}
	private void issueNoLoad(){
		String sql="select isnull(max(issueNo),0)+1 id from tbIssueToLabelPrintingInfo";
		String assembleissueNo="";
		if(cmbIssueTo.getValue().toString().equalsIgnoreCase("Assemble"))
		{
			sql="select [dbo].[getIssueNoForIssueToLabeling_Printing_AssembleNew](1)";	
			/*Iterator iter1=dbService(sql);
			if(iter1.hasNext())
			{
				assembleissueNo= iter1.next().toString();	
			}
			
			String innersql="";
			
			innersql="select 0,isnull(max(issueNo),0)+1 id from tbIssueToLabelPrintingInfo where issueNo='"+assembleissueNo+"' ";
			
			Iterator iter2=dbService(sql);
			if(iter2.hasNext())
			{
				sql="select 0,isnull(max(issueNo),0)+1 id from tbIssueToLabelPrintingInfo"	;
			}
			else
			{
				sql="select 0,isnull(max(cast(issueNo as bigint)),0)+1 id from tbIssueToAssembleInfo";	
			}*/
			
		}
		Iterator iter1=dbService(sql);
		if(iter1.hasNext()){
			//Object element1[]=(Object[])iter1.next();
			txtIssueNo.setValue(iter1.next());
		}
	}
	private void cmbBatchNoLoad() 
	{
		String sql="select 0,batchNo,CONVERT(varchar(10),ReqDate,103)ReqDate from tbRequisitionEntryLabelingPrintingInfo where ReqFrom like '"+cmbIssueTo.getValue()+"' and batchNo not in( "+
				" select batchNo from tbIssueToLabelPrintingInfo where issueTo like '"+cmbIssueTo.getValue()+"' "+
				" 	union "+
				" 	select batchNo from tbIssueToAssembleInfo where issueTo like 'Assemble' "+
				" )";
		if(isFind){
			sql="select 0,batchNo,CONVERT(varchar(10),ReqDate,103)ReqDate from tbRequisitionEntryLabelingPrintingInfo where ReqFrom like '"+cmbIssueTo.getValue()+"'";
		}
		cmbBatchNo.removeAllItems();
		Iterator iter=dbService(sql);
		while(iter.hasNext()){
			Object element[]=(Object[])iter.next();
			cmbBatchNo.addItem(element[1]);
			cmbBatchNo.setItemCaption(element[1], element[1]+" # "+element[2]);
		}

	}
	private void tableInitialize()
	{
		for (int a = 0; a<10; a++)
		{
			tableRowAdd(a);
		}
	}
	private void tbCmbAction(int ar){

		Iterator iter=dbService("select unit,color,storeStock,isnull(reqQty,0)reqQty,issuedQty," +
				"isnull(remainQty,0)remainQty,isnull((select semiFgSectionStock from " +
				"funcSemiFgPrintingLabelingStock('"+tbCmbItemName.get(ar).getValue()+"','"+dateFormat.format(dIssueDate.getValue())+"')),0)semiFgSectionStock" +
				" from funcIssueToLabelPrintingData" +
				"('"+cmbBatchNo.getValue()+"','"+tbCmbItemName.get(ar).getValue()+"'," +
				"'"+dateFormat.format(dIssueDate.getValue())+"')");
		while(iter.hasNext())
		{
			Object element[]=(Object[])iter.next();
			
			tbUnit.get(ar).setValue(element[0]);
			tbColor.get(ar).setValue(element[1]);
			tbStoreStock.get(ar).setValue(df.format(element[2]));
			tbReqQty.get(ar).setValue(df.format(element[3]));
			tbIssuedQty.get(ar).setValue(df.format(element[4]));
			tbRemainQty.get(ar).setValue(df.format(element[5]));
			tbSectionStock.get(ar).setValue(df.format(element[6]));	
		}
	}
	private void tableRowAdd(final int rq)
	{
		tbSl.add(rq, new Label());
		tbSl.get(rq).setValue(rq+1);
		tbSl.get(rq).setWidth("100%");
		tbSl.get(rq).setHeight("-1px");

		tbCmbItemName.add(rq, new ComboBox() );
		tbCmbItemName.get(rq).setImmediate(true);
		tbCmbItemName.get(rq).setNullSelectionAllowed(true);
		tbCmbItemName.get(rq).setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		tbCmbItemName.get(rq).setWidth("100%");
		tbCmbItemName.get(rq).setHeight("-1px");
		tbCmbItemName.get(rq).setEnabled(false);

		/*tbCmbItemName.get(rq).addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) {

				if(tbCmbItemName.get(rq).getValue()!=null){
					if(doubleEntryCheck(rq)){
						tbCmbAction(rq);
						tbIssueQty.get(rq).focus();
						if(rq==tbCmbItemName.get(rq).size()-1){
							tableRowAdd(rq+1);
						}
					}
					else{
						showNotification("Sorry!!","Double Entry",Notification.TYPE_WARNING_MESSAGE);
						tbCmbItemName.get(rq).setValue(null);
						tbCmbItemName.get(rq).focus();
					}
				}
				else{
					tbUnit.get(rq).setValue("");
					tbColor.get(rq).setValue("");
					tbStoreStock.get(rq).setValue("");
					tbReqQty.get(rq).setValue("");
					tbIssuedQty.get(rq).setValue("");
					tbRemainQty.get(rq).setValue("");
					tbIssueQty.get(rq).setValue("");
					tbRemarks.get(rq).setValue("");
				}
			}
		});*/

		tbColor.add(rq, new Label());
		tbColor.get(rq).setImmediate(true);
		tbColor.get(rq).setWidth("-1px");
		tbColor.get(rq).setHeight("-1px");

		tbUnit.add(rq, new Label());
		tbUnit.get(rq).setImmediate(true);
		tbUnit.get(rq).setWidth("-1px");
		tbUnit.get(rq).setHeight("-1px");

		tbStoreStock.add(rq, new TextRead(1));
		tbStoreStock.get(rq).setImmediate(true);
		tbStoreStock.get(rq).setWidth("100%");
		tbStoreStock.get(rq).setHeight("-1px");

		tbSectionStock.add(rq, new TextRead(1));
		tbSectionStock.get(rq).setImmediate(true);
		tbSectionStock.get(rq).setWidth("100%");
		tbSectionStock.get(rq).setHeight("-1px");

		tbReqQty.add(rq, new TextRead(1));
		tbReqQty.get(rq).setImmediate(true);
		tbReqQty.get(rq).setWidth("100%");
		tbReqQty.get(rq).setHeight("-1px");

		tbIssuedQty.add(rq, new TextRead(1));
		tbIssuedQty.get(rq).setImmediate(true);
		tbIssuedQty.get(rq).setWidth("100%");
		tbIssuedQty.get(rq).setHeight("-1px");

		tbRemainQty.add(rq, new TextRead(1));
		tbRemainQty.get(rq).setImmediate(true);
		tbRemainQty.get(rq).setWidth("100%");
		tbRemainQty.get(rq).setHeight("-1px");

		tbIssueQty.add(rq, new AmountField());
		tbIssueQty.get(rq).setImmediate(true);
		tbIssueQty.get(rq).setWidth("100%");
		tbIssueQty.get(rq).setHeight("-1px");

		tbIssueQty.get(rq).addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) 
			{

				/*if(!isUpdate&&!isFind){
					double remainQty=Double.parseDouble(tbRemainQty.get(rq).getValue().toString().isEmpty()?"0.0":tbRemainQty.get(rq).getValue().toString());
					double issueQty=Double.parseDouble(tbIssueQty.get(rq).getValue().toString().isEmpty()?"0.0":tbIssueQty.get(rq).getValue().toString());
					double stockQty=Double.parseDouble(tbStoreStock.get(rq).getValue().toString().isEmpty()?"0.0":tbStoreStock.get(rq).getValue().toString());

					if(stockQty>=issueQty){
						if(remainQty<issueQty){
							showNotification("Sorry!!","Issue Qty Exceed Remain Qty",Notification.TYPE_WARNING_MESSAGE);
							tbIssueQty.get(rq).setValue("");
							tbIssueQty.get(rq).focus();
						}
					}
					else{
						showNotification("Sorry!!","Issue Qty Exceed Stock Qty",Notification.TYPE_WARNING_MESSAGE);
						tbIssueQty.get(rq).setValue("");
						tbIssueQty.get(rq).focus();
					}
				}*/
				
				
					double remainQty=Double.parseDouble(tbRemainQty.get(rq).getValue().toString().isEmpty()?"0.0":tbRemainQty.get(rq).getValue().toString());
					double issueQty=Double.parseDouble(tbIssueQty.get(rq).getValue().toString().isEmpty()?"0.0":tbIssueQty.get(rq).getValue().toString());
					double stockQty=Double.parseDouble(tbStoreStock.get(rq).getValue().toString().isEmpty()?"0.0":tbStoreStock.get(rq).getValue().toString());

					if(stockQty>=issueQty){
						if(remainQty<issueQty){
							showNotification("Sorry!!","Issue Qty Exceed Remain Qty",Notification.TYPE_WARNING_MESSAGE);
							tbIssueQty.get(rq).setValue("");
							tbIssueQty.get(rq).focus();
						}
					}
					else{
						showNotification("Sorry!!","Issue Qty Exceed Stock Qty",Notification.TYPE_WARNING_MESSAGE);
						tbIssueQty.get(rq).setValue("");
						tbIssueQty.get(rq).focus();
					}
				
				
				
				
			}
		});

		tbRemarks.add(rq, new TextField());
		tbRemarks.get(rq).setImmediate(true);
		tbRemarks.get(rq).setWidth("100%");
		tbRemarks.get(rq).setHeight("-1px");

		table.addItem(new Object[]{tbSl.get(rq),tbCmbItemName.get(rq),tbUnit.get(rq),tbColor.get(rq),tbStoreStock.get(rq),
				tbSectionStock.get(rq),tbReqQty.get(rq),tbIssuedQty.get(rq),tbRemainQty.get(rq),tbIssueQty.get(rq),tbRemarks.get(rq)},rq);

	}
	private void findButtonEvent(){
		Window win = new IssueToLabelingPrintingFind(sessionBean, txtItemID);
		win.addListener(new Window.CloseListener() 
		{
			public void windowClose(CloseEvent e) 
			{
				if (txtItemID.getValue().toString().length() > 0)
				{
					txtClear();
					findInitialise(txtItemID.getValue().toString());
					//System.out.println("Issue No: "+txtItemID.getValue().toString());
				}
			}
		});

		this.getParent().addWindow(win);
	}
	private void findInitialise(String issueNo){
		String sql="select a.issueTo,a.batchNo,a.ReqNo,a.issueDate,a.reqDate,a.issueNo, "+
				" b.productId,b.ReqQty,b.IssuedQty,b.remainQty,b.issueQty,b.remarks "+
				" from tbIssueToLabelPrintingInfo a inner join tbIssueToLabelPrintingDetails b "+ 
				" on a.issueNo=b.issueNo where a.issueNo='"+issueNo+"'";
		System.out.println("find sql is:"+sql);
		
		Iterator iter=dbService("select a.issueTo,a.batchNo,a.ReqNo,a.issueDate,a.reqDate,a.issueNo, "+
				" b.productId,b.ReqQty,b.IssuedQty,b.remainQty,b.issueQty,b.remarks "+
				" from tbIssueToLabelPrintingInfo a inner join tbIssueToLabelPrintingDetails b "+ 
				" on a.issueNo=b.issueNo where a.issueNo='"+issueNo+"'");
		int a=0;
		while(iter.hasNext()){
			Object element[]=(Object[])iter.next();
			if(a==0){
				cmbIssueTo.setValue(element[0]);
				cmbBatchNo.setValue(element[1]);
				txtReqNo.setValue(element[2]);
				dIssueDate.setValue(element[3]);
				dReqDate.setValue(element[4]);
				txtIssueNo.setValue(element[5]);
			}
			
			tbCmbItemName.get(a).setValue(element[7]);
			tbReqQty.get(a).setValue(df.format(element[7]));
			tbIssuedQty.get(a).setValue(df.format(element[8]));
			tbRemainQty.get(a).setValue(df.format(element[9]));
			
			double storestock=Double.parseDouble(tbStoreStock.get(a).getValue().toString().isEmpty()?"0.00":tbStoreStock.get(a).getValue().toString())+Double.parseDouble(element[10].toString()); 
			tbStoreStock.get(a).setValue(df.format(storestock) );
			tbIssueQty.get(a).setValue(df.format(element[10]));
			tbRemarks.get(a).setValue(element[11]);
			a++;
			if(a==tbCmbItemName.size()-1){
				tableRowAdd(a+1);
			}
		}
	}
	private AbsoluteLayout buildMainLayout() {
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setMargin(false);

		setWidth("1165px");
		setHeight("520px");

		cmbIssueTo=new ComboBox();
		cmbIssueTo.setImmediate(true);
		cmbIssueTo.setWidth("200px");
		cmbIssueTo.setHeight("24px");
		cmbIssueTo.setNullSelectionAllowed(true);
		cmbIssueTo.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		cmbIssueTo.addItem("Assemble");
		cmbIssueTo.addItem("Dry Offset Printing");
		cmbIssueTo.addItem("Screen Printing");
		cmbIssueTo.addItem("Heat Trasfer Label");
		cmbIssueTo.addItem("Manual Printing");
		cmbIssueTo.addItem("Labeling");
		cmbIssueTo.addItem("Cap Folding");
		cmbIssueTo.addItem("Shrink");
		//cmbIssueTo.addItem("Stretch Blow Molding");

		mainLayout.addComponent(new Label("Issue To: "),"top:10.0px;left:10px;");
		mainLayout.addComponent(cmbIssueTo,"top:8.0px;left:100px;");

		cmbBatchNo=new ComboBox();
		cmbBatchNo.setImmediate(true);
		cmbBatchNo.setWidth("200px");
		cmbBatchNo.setHeight("24px");
		cmbBatchNo.setNullSelectionAllowed(true);
		cmbBatchNo.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(new Label("Batch No: "),"top:35.0px;left:10px;");
		mainLayout.addComponent(cmbBatchNo,"top:33.0px;left:100px;");

		txtReqNo=new TextRead(1);
		txtReqNo.setImmediate(true);
		txtReqNo.setWidth("130px");
		txtReqNo.setHeight("24px");
		mainLayout.addComponent(new Label("Req No: "),"top:60.0px;left:10px;");
		mainLayout.addComponent(txtReqNo,"top:58.0px;left:100px;");


		dIssueDate = new PopupDateField();
		dIssueDate.setImmediate(true);
		dIssueDate.setWidth("110px");
		dIssueDate.setDateFormat("dd-MM-yyyy");
		dIssueDate.setValue(new java.util.Date());
		dIssueDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(new Label("Issue Date: "),"top:10.0px;left:350px;");
		mainLayout.addComponent(dIssueDate,"top:8.0px;left:440px;");


		dReqDate = new PopupDateField();
		dReqDate.setImmediate(true);
		dReqDate.setWidth("110px");
		dReqDate.setDateFormat("dd-MM-yyyy");
		dReqDate.setValue(new java.util.Date());
		dReqDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(new Label("Req Date: "),"top:35.0px;left:350px;");
		mainLayout.addComponent(dReqDate,"top:33.0px;left:440px;");

		txtIssueNo=new TextRead(1);
		txtIssueNo.setImmediate(true);
		txtIssueNo.setWidth("100px");
		txtIssueNo.setHeight("24px");
		mainLayout.addComponent(new Label("Issue No: "),"top:60.0px;left:350px;");
		mainLayout.addComponent(txtIssueNo,"top:58.0px;left:440px;");


		table.setWidth("100%");
		table.setHeight("320px");
		table.setFooterVisible(true);
		table.setImmediate(true); 
		table.setColumnCollapsingAllowed(true);

		table.addContainerProperty("SL", Label.class, new Label());
		table.setColumnWidth("SL", 15);
		table.setColumnAlignment("SL", table.ALIGN_CENTER);

		table.addContainerProperty("ITEM Name", ComboBox.class , new ComboBox());
		table.setColumnWidth("ITEM Name",320);

		table.addContainerProperty("Unit", Label.class , new Label());
		table.setColumnWidth("Unit",40);

		table.addContainerProperty("Color", Label.class , new Label());
		table.setColumnWidth("Color",80);

		table.addContainerProperty("Store Stock", TextRead.class , new TextRead(1));
		table.setColumnWidth("Store Stock",65);

		table.addContainerProperty("Section Stock", TextRead.class , new TextRead(1));
		table.setColumnWidth("Section Stock",75);

		table.addContainerProperty("Req Qty", TextRead.class , new TextRead(1));
		table.setColumnWidth("Req Qty",60);

		table.addContainerProperty("Issued Qty", TextRead.class , new TextRead(1));
		table.setColumnWidth("Issued Qty",60);

		table.addContainerProperty("Remain Qty", TextRead.class , new TextRead(1));
		table.setColumnWidth("Remain Qty",60);

		table.addContainerProperty("Issue Qty", AmountField.class , new AmountField());
		table.setColumnWidth("Issue Qty",60);

		table.addContainerProperty("Remarks", TextField.class , new TextField());
		table.setColumnWidth("Remarks",150);

		mainLayout.addComponent(table,"top:90.0px;left:10px;");
		tableInitialize();

		Label lblLine = new Label("<b><font color='#e65100'>===================================================================================================================================================================================</font></b>", Label.CONTENT_XHTML);
		mainLayout.addComponent(lblLine, "top:410.0px;left:0.0px;");
		mainLayout.addComponent(cButton, "top:430.0px;left:260.0px;");
		return mainLayout;
	}

}
