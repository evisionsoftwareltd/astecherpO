package com.example.productionReport;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.xerces.impl.dtd.models.CMBinOp;
import org.hibernate.Session;
import org.hibernate.Transaction;


import com.common.share.ReportViewerNew;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;
public class RptRmConsumptionFGWiseRmWise extends Window{

	SessionBean sessionBean;
	private Label lblFrom=new Label("From : ");
	private ComboBox cmbType=new ComboBox();
	private Label lblTo=new Label("To : ");
	private ComboBox cmbStep=new ComboBox();
	
	CheckBox chkPrinting=new CheckBox("Printing");
	CheckBox chkTubing=new CheckBox("Tubing");
	CheckBox chkShouldering=new CheckBox("Shouldering");
	CheckBox chkSealing=new CheckBox("Sealing");

	private CheckBox chkAll=new CheckBox("All");
	private Label lblAll=new Label();

	int type=0;
	private CheckBox chkpdf=new CheckBox("PDF");
	private CheckBox chkother=new CheckBox("Others");
	private HorizontalLayout chklayout=new HorizontalLayout();

	private Label lblFDate;
	private Label lblToDate;
	private Label lblLine;
	private PopupDateField formDate=new PopupDateField();
	private PopupDateField toDate=new PopupDateField();
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private Label lblline;

	private SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
	private SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");


	private AbsoluteLayout mainLayout;

	public RptRmConsumptionFGWiseRmWise(SessionBean sessionBean,String s){

		this.sessionBean=sessionBean;
		this.setCaption("FINISHED GOODS WITH CONSUMPTION RM WISE::"+sessionBean.getCompany());
		this.setResizable(false);
		buildMainLayout();
		productionTypeDataLoad();
		this.addComponent(mainLayout);
		setEventAction();
	}
	private void productionTypeDataLoad()
	{
		Transaction tx=null;
		try
		{
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();
			String sql="select productTypeId,productTypeName from tbProductionType";
			List list=session.createSQLQuery(sql).list();

			int i=0;
			for(Iterator iter=list.iterator();iter.hasNext();){
				Object element[]=(Object[]) iter.next();
				cmbType.addItem(element[0]);
				if(i==0){
					cmbType.setValue(element[0]);
					// productionStepDataLoad();
					i=1;
				}
				cmbType.setItemCaption(element[0], element[1].toString());
			}
		}
		catch(Exception exp)
		{
			this.getParent().showNotification(exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}

	private void setEventAction() {

		previewButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event)
			{
				/*if(cmbType.getValue()!=null)
				{*/
					/*if(cmbStep.getValue()!=null)
					{
						reportView(); 
					}
					else
					{
						showNotification("Please Select Issue TO",Notification.TYPE_WARNING_MESSAGE); 
					}*/
					reportView(); 
				/*}
				else
				{
					showNotification("Please Select Issue From",Notification.TYPE_WARNING_MESSAGE);
				}*/

			}
		});
		exitButton.addListener(new ClickListener() {

			public void buttonClick(ClickEvent event) {
				close();
			}
		});
		chkAll.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) {
				boolean bv = chkAll.booleanValue();
				System.out.println("ST : "+bv);
				if(bv==true){
					cmbType.setEnabled(false);
					cmbType.setValue(null);

				}
				else{
					cmbType.setEnabled(true);
					cmbType.focus();
				}
			}
		});

		chkpdf.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkpdf.booleanValue()==true)
					chkother.setValue(false);
				else
					chkother.setValue(true);

			}
		});

		chkother.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkother.booleanValue()==true)
					chkpdf.setValue(false);
				else
					chkpdf.setValue(true);

			}
		});

		

	}
	/*private void productionStepDataLoad() {
		
		Transaction tx=null;
		try
		{
			Session session=SessionFactoryUtil.getInstance().getCurrentSession();
			tx=session.beginTransaction();
			String sql="select StepId,StepName from tbProductionStep where productionTypeId like '"+cmbType.getValue()+"'";
			List list=session.createSQLQuery(sql).list();
			for(Iterator iter=list.iterator();iter.hasNext();){
				Object element[]=(Object[]) iter.next();
				cmbStep.addItem(element[0]);
				
				cmbStep.setItemCaption(element[0], element[1].toString());
			}
		}
		catch(Exception exp)
		{
			this.getParent().showNotification(exp+"",Notification.TYPE_ERROR_MESSAGE);
		}
	}*/
	

	private void reportView()
	{	
		String query=null;
		Transaction tx=null;

		if(chkpdf.booleanValue()==true)
			type=1;
		else
			type=0;

		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("Phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("fromdate",new SimpleDateFormat("dd-MM-yyyy").format(formDate.getValue()) );
			hm.put("todate", new SimpleDateFormat("dd-MM-yyyy").format(toDate.getValue()));
			hm.put("issueFrom",cmbType.getItemCaption(cmbType.getValue()) );
			//hm.put("issueTo",cmbType.getItemCaption(cmbStep.getValue()) );


			/*query= " select  a.IssueDate,a.IssueNo,a.ChallanNo, a.FinishedGood ,b.IssueQty,b.RMitemCode ,case when  Fromflag='1'  then c.vRawItemName  else d.vProductName  end as  productName,b.unit, e.vProductName as finishProductName     from tbTubeIssueInfo a "
					+" inner join "
					+" tbTubeIssueDetails b "
					+"on a.IssueNo=b.IssueNo  "
					+"left join "
					+"tbRawItemInfo c " 
					+"on c.vRawItemCode=b.RMitemCode "
					+"left join "
					+"tbFinishedProductInfo d "
					+"on d.vProductId =b.RMitemCode "
					+"left join "
					+"tbFinishedProductInfo e "
					+"on e.vProductId=a.FinishedGood "
					+"where CONVERT(Date,a.IssueDate,105) <='"+datef.format(formDate.getValue())+"'  and a.IssueFrom like '"+cmbType.getValue()+"' and a.IssueTo like '"+cmbStep.getValue()+"' ";*/
			/*query="select fgid,fgName,rawItemId,rawItemName,sum(finishedPcs)as finishedPcs,sum(cogpStdQty) "+
					" as cogpStdQty,sum(printingInk)as PrintingInk,sum(hdpe)as hdpe,sum(mb)as mb," +
					"(SUM(laminateWastage)+SUM(hdpeWastage)+SUM(mbWastage)+SUM(inkWastage)) as ttlWastage from funcCOGPstandard "+
					" ('"+new SimpleDateFormat("yyyy-MM-dd").format(formDate.getValue())+"','"+ new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"') group by fgid,fgName,rawItemId,rawItemName";*/

			/*query="select fgid,fgName,rawItemId,rawItemName,sum(finishedPcs)as finishedPcs,sum(cogpStdQty) "+
					"as cogpStdQty,SUM(labelStock)as labelStock,SUM(labelInk)as labelInk,SUM(lblStockWastage)as lblStockWastage, "+
					"SUM(lblinkWastage)as lblInkWastage,sum(printingInk)as PrintingInk,sum(hdpe)as hdpe,sum(mb)as mb, "+
					"(SUM(laminateWastage)+SUM(hdpeWastage)+SUM(mbWastage)+SUM(inkWastage)) as ttlWastage from funcCOGPstandard  "+
					"('"+new SimpleDateFormat("yyyy-MM-dd").format(formDate.getValue())+"','"+ new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"') group by fgid,fgName,rawItemId,rawItemName";
			
			String subQuery="select fgid,fgName,rawItemId,rawItemName,finishedPcs,sum(cogpStdQty)as LabelStock," +
					"sum(PrintingInk)as labelInk,sum(laminateWastage)as labelStockWastage,sum(inkWastage)as " +
					"labelInkWastage from funcCOGPLabel('"+new SimpleDateFormat("yyyy-MM-dd").format(formDate.getValue())+"'," +
					"'"+ new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"')group by fgid,fgName,rawItemId,rawItemName,finishedPcs";*/
			//query=" select *,(select vUnitName from tbFinishedProductInfo where vProductId like fgId) fgunit,(select vUnitName from tbRawItemInfo where vRawItemCode like rawId) rawunit from " +
					//"funcCOGPStandardAstech('"+new SimpleDateFormat("yyyy-MM-dd").format(formDate.getValue())+"','"+new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"') order by  categoryId,fgId ";

			query="select categoryName,fgName,unit,finishedPcs,rawid,rawName,stdQty,SUM(consumpedQty)consumpedQty,rate,sum(amount)amount from  "+
					" funcCOGPStandardAstech('"+new SimpleDateFormat("yyyy-MM-dd").format(formDate.getValue())+"','"+new SimpleDateFormat("yyyy-MM-dd").format(toDate.getValue())+"') " +
					"group by categoryName,fgName,unit,finishedPcs,rawid,rawName,stdQty,rate   "+
					" order by cast(subString(rawId,CHARINDEX('-',rawid)+1,len(rawid)-CHARINDEX('-',rawid))as int)";
			hm.put("sql", query);
			//hm.put("subSql", subQuery);

			List list=session.createSQLQuery(query).list();
			if(!list.isEmpty()){
				Window win = new ReportViewerNew(hm,"report/production/rptFgWithConsumptionRmWise.jasper",
						this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
						this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
						this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
				win.setCaption("Project Report");
				this.getParent().getWindow().addWindow(win);

			}
			else{
				this.getParent().showNotification("There are no Data!!",Notification.TYPE_WARNING_MESSAGE);
			}

		}
		catch(Exception exp){
			this.getParent().showNotification("Error",exp.toString(),Notification.TYPE_ERROR_MESSAGE);
			System.out.println(exp);
		}
	}
	private AbsoluteLayout buildMainLayout() {

		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("460px");
		mainLayout.setHeight("200px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("490px");
		setHeight("280px");

		lblFrom = new Label();
		lblFrom.setImmediate(false);
		lblFrom.setWidth("-1px");
		lblFrom.setHeight("-1px");
		lblFrom.setValue("Production Type :");
		mainLayout.addComponent(lblFrom, "top:16.0px;left:40.0px;");

		cmbType = new ComboBox();
		cmbType.setImmediate(true);
		cmbType.setWidth("200px");
		cmbType.setHeight("24px");
		cmbType.setNullSelectionAllowed(false);
		cmbType.setNewItemsAllowed(false);
		//cmbType.setEnabled(false);
		mainLayout.addComponent( cmbType, "top:15.0px;left:140.0px;");

		/*lblTo = new Label();
		lblTo.setImmediate(false);
		lblTo.setWidth("-1px");
		lblTo.setHeight("-1px");
		lblTo.setValue("Production Step :");
		mainLayout.addComponent(lblTo, "top:41.0px;left:40.0px;");*/

		/*HorizontalLayout hLayout=new HorizontalLayout();
		hLayout.setSpacing(true);
		hLayout.addComponent(chkPrinting);
		chkPrinting.setValue(true);
		chkPrinting.setImmediate(true);
		hLayout.addComponent(chkTubing);
		chkTubing.setValue(true);
		chkTubing.setImmediate(true);
		hLayout.addComponent(chkShouldering);
		chkShouldering.setValue(true);
		chkShouldering.setImmediate(true);
		hLayout.addComponent(chkSealing);
		chkSealing.setValue(true);
		chkSealing.setImmediate(true);
		
		mainLayout.addComponent( hLayout, "top:41.0px;left:140.0px;");*/


		lblFDate = new Label();
		lblFDate.setImmediate(false);
		lblFDate.setWidth("-1px");
		lblFDate.setHeight("-1px");
		lblFDate.setValue("From Date: ");
		mainLayout.addComponent(lblFDate, "top:41.0px;left:40.0px;");

		formDate.setImmediate(true);
		formDate.setResolution(PopupDateField.RESOLUTION_DAY);
		formDate.setValue(new java.util.Date());
		formDate.setDateFormat("dd-MM-yyyy");
		formDate.setWidth("107px");
		formDate.setHeight("-1px");
		formDate.setInvalidAllowed(false);
		mainLayout.addComponent( formDate, "top:41.0px;left:140.0px;");

		lblToDate = new Label();
		lblToDate.setImmediate(false);
		lblToDate.setWidth("-1px");
		lblToDate.setHeight("-1px");
		lblToDate.setValue("To Date: ");
		mainLayout.addComponent(lblToDate, "top:67.0px;left:40.0px;");

		toDate.setImmediate(true);
		toDate.setResolution(PopupDateField.RESOLUTION_DAY);
		toDate.setValue(new java.util.Date());
		toDate.setDateFormat("dd-MM-yyyy");
		toDate.setWidth("107px");
		toDate.setHeight("-1px");
		toDate.setInvalidAllowed(false);
		mainLayout.addComponent( toDate, "top:67.0px;left:140.0px;");

		chkpdf.setValue(true);
		chkpdf.setImmediate(true);
		chkother.setImmediate(true);
		chklayout.addComponent(chkpdf);
		chklayout.addComponent(chkother);
		mainLayout.addComponent(chklayout, "top:115.0px; left:140.0px");

		lblLine = new Label();
		lblLine.setImmediate(false);
		lblLine.setWidth("-1px");
		lblLine.setHeight("-1px");
		lblLine.setContentMode(Label.CONTENT_XHTML);
		lblLine.setValue("<b><font color='#e65100'>======================================================================================================================</font></b>");
		mainLayout.addComponent(lblLine, "top:145.0px;left:25.0px;");

		previewButton.setWidth("80px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:171.opx; left:135.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:171.opx; left:220.0px");

		chkAll.setImmediate(true);
		chkAll.setVisible(true);
		mainLayout.addComponent( chkAll, "top:15.0px;left:350.0px;");
		lblAll.setVisible(false);
		return mainLayout;


	}
}

