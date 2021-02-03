package com.example.rawMaterialReport;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.ReportViewer;
import com.common.share.ReportViewerNew;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class StockReport extends Window {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private CheckBox subCateCheckBox;
	@AutoGenerated
	private CheckBox CategoryCheckBox;
	@AutoGenerated
	private CheckBox sectionCheckBox;
	@AutoGenerated
	private ComboBox cmbSubCategory;
	@AutoGenerated
	private Label lblSubCategory;
	@AutoGenerated
	private ComboBox cmbCategory;
	@AutoGenerated
	private Label lblCategory;
	private Label lblAson;
	@AutoGenerated
	private ComboBox cmbDepartment;
	private Label lblcategorytype;
	private ComboBox cmbcategorytyoe;
	@AutoGenerated
	private Label lblDepartment;
	private Label lblline;
	private PopupDateField AsonDate;
	private CheckBox chkpdf,chkother;

	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	private SessionBean sessionBean;
	int type=0;
	public StockReport(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		//cmbDepartment.focus();
		this.setCaption("STOCK SUMMARY :: "+ sessionBean.getCompany());
		Component ob[]={cmbcategorytyoe,cmbCategory,cmbSubCategory,previewButton};
		new FocusMoveByEnter(this,ob);
		cmbDepartmentNameAdd();
		cmbcategorytyoe.focus();
		//addCategoryName();
		//addSubCategoryName();
		AllbtnAction();
	}
	
	private void cmbDepartmentNameAdd()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();

		String query = " select distinct 0, vCategoryType from tbRawItemCategory";
		System.out.println(query);

		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbcategorytyoe.addItem(element[1]);
			cmbcategorytyoe.setItemCaption(element[1], element[1].toString());
		}
	}
	
	public void addCategoryName()
	{
		cmbCategory.removeAllItems();
		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery(" select Group_Id,vCategoryName  from tbRawItemCategory where vCategoryType like '"+cmbcategorytyoe.getValue().toString()+"'").list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbCategory.addItem(element[0]);
				cmbCategory.setItemCaption(element[0], element[1].toString());
			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}

	}

	public void addSubCategoryName(){
		cmbSubCategory.removeAllItems();

		Transaction tx = null;
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			tx = session.beginTransaction();
			List list = session.createSQLQuery(" select SubGroup_Id,vSubCategoryName from tbRawItemSubCategory where Group_Id like '"+cmbCategory.getValue()+"'").list();

			for(Iterator iter=list.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbSubCategory.addItem(element[0]);
				cmbSubCategory.setItemCaption(element[0], element[1].toString());

			}
		}catch(Exception exp){
			this.getParent().showNotification("Error",exp+"",Notification.TYPE_ERROR_MESSAGE);
		}

	}

	private void AllbtnAction() {
		
		chkpdf.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkpdf.booleanValue())
				{

					chkother.setValue(false);
					type=1;

				}

				else
				{

					chkother.setValue(true);
					type=0;

				}


			}

		});

		chkother.addListener(new ValueChangeListener()
		{

			public void valueChange(ValueChangeEvent event)
			{

				if(chkother.booleanValue())
				{

					chkpdf.setValue(false);
					type=1;

				}

				else
				{

					chkpdf.setValue(true);
					type=0;

				}

			}
		});
		CategoryCheckBox.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) {
				boolean bv = CategoryCheckBox.booleanValue();
				if(bv==true){
					cmbCategory.setValue(null);
					cmbCategory.setEnabled(false);
				}
				else{
					cmbCategory.setEnabled(true);
				}
			}
		});

		subCateCheckBox.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) {
				boolean bv = subCateCheckBox.booleanValue();
				if(bv==true){
					cmbSubCategory.setValue(null);
					cmbSubCategory.setEnabled(false);
				}
				else{
					cmbSubCategory.setEnabled(true);
				}
			}
		});
		
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if((cmbcategorytyoe.getValue()!=null) && (cmbCategory.getValue()!=null || CategoryCheckBox.booleanValue()==true) && (cmbSubCategory.getValue()!=null  || subCateCheckBox.booleanValue()==true)){
					System.out.println("Preview...");
					reportView();
				}
				else{
					getParent().showNotification("All Fields are Mandetory", Notification.TYPE_WARNING_MESSAGE);
				}
			}
		});
		
		exitButton.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				close();
			}
		});
		
		
		
		cmbcategorytyoe.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbcategorytyoe.getValue()!=null)
				{
					addCategoryName();
				}
				
			}
		});
		
		
		cmbCategory.addListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				
				if(cmbCategory.getValue()!=null)
				{
					addSubCategoryName();
				}
			}
		});
		
	}
	
	private void reportView()
	{	
		String query=null;
		String sectionId = "";
		String CategoryId = "";
		String SubCategoryId = "";

		//sectionId = cmbDepartment.getValue().toString();

		if(CategoryCheckBox.booleanValue()==true){
			CategoryId = "%";
		}else{
			CategoryId = cmbCategory.getValue().toString();
		}

		if(subCateCheckBox.booleanValue()==true){
			SubCategoryId = "%";
		}else{
			SubCategoryId = cmbSubCategory.getValue().toString();
		}

		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("logo", sessionBean.getCompanyLogo());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("phone", sessionBean.getCompanyContact());
			//hm.put("phone", "Phone : "+sessionBean.getCompanyPhone()+" Fax : "+sessionBean.getCompanyFax()+" E-Mail : "+sessionBean.getCompanyEmail());
			//System.out.println(sessionBean.getCompanyPhone());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("asOndate", new SimpleDateFormat("dd-MM-yyyy").format(AsonDate.getValue()));
			System.out.println(new SimpleDateFormat("yyyy-dd-MM").format(AsonDate.getValue())+" 23:59:59");
			
			query= "select * from dbo.funcStockSummary('"+CategoryId+"','"+SubCategoryId+"','"+new SimpleDateFormat("yyyy-MM-dd").format(AsonDate.getValue())+" 23:59:59"+"','"+cmbcategorytyoe.getValue().toString()+"') order by itemtype,categoryName,subCategoryName ";
			System.out.println("query : "+query);
			hm.put("sql", query);

			Window win = new ReportViewerNew(hm,"report/raw/rptStokSummary.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",1);
			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);
		}
		catch(Exception exp){
			this.getParent().showNotification("Error",exp.toString(),Notification.TYPE_ERROR_MESSAGE);
			System.out.println(exp);
		}
	}

	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("460px");
		mainLayout.setHeight("240px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("480px");
		setHeight("280px");

		// lblDepartment
		lblcategorytype = new Label();
		lblcategorytype.setImmediate(false);
		lblcategorytype.setWidth("-1px");
		lblcategorytype.setHeight("-1px");
		lblcategorytype.setValue("Category Type:");
		mainLayout.addComponent(lblcategorytype, "top:40.0px;left:64.0px;");

		// cmbDepartment
		cmbcategorytyoe = new ComboBox();
		cmbcategorytyoe.setImmediate(false);
		cmbcategorytyoe.setWidth("260px");
		cmbcategorytyoe.setHeight("24px");
		cmbcategorytyoe.setNullSelectionAllowed(false);
		cmbcategorytyoe.setImmediate(true);
		cmbcategorytyoe.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbcategorytyoe, "top:38.0px;left:150.0px;");

		// lblCategory
		lblCategory = new Label();
		lblCategory.setImmediate(false);
		lblCategory.setWidth("100.0%");
		lblCategory.setHeight("-1px");
		lblCategory.setValue("Category:");
		mainLayout.addComponent(lblCategory,"top:66.0px;left:64.0px;");

		// cmbCategory
		cmbCategory = new ComboBox();
		cmbCategory.setImmediate(false);
		cmbCategory.setWidth("260px");
		cmbCategory.setHeight("-1px");
		cmbCategory.setNullSelectionAllowed(false);
		cmbCategory.setImmediate(true);
		cmbCategory.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbCategory, "top:64.0px;left:150.0px;");

		// lblSubCategory
		lblSubCategory = new Label();
		lblSubCategory.setImmediate(false);
		lblSubCategory.setWidth("-1px");
		lblSubCategory.setHeight("-1px");
		lblSubCategory.setValue("Sub Category :");
		mainLayout.addComponent(lblSubCategory, "top:92.0px;left:64.0px;");

		// cmbSubCategory
		cmbSubCategory = new ComboBox();
		cmbSubCategory.setImmediate(false);
		cmbSubCategory.setWidth("260px");
		cmbSubCategory.setHeight("-1px");
		cmbSubCategory.setNullSelectionAllowed(false);
		cmbSubCategory.setImmediate(true);
		cmbSubCategory.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		mainLayout.addComponent(cmbSubCategory, "top:90.0px;left:150.0px;");

		// CategoryCheckBox
		CategoryCheckBox = new CheckBox();
		CategoryCheckBox.setCaption("All");
		CategoryCheckBox.setImmediate(false);
		CategoryCheckBox.setWidth("-1px");
		CategoryCheckBox.setHeight("24px");
		CategoryCheckBox.setImmediate(true);
		mainLayout.addComponent(CategoryCheckBox, "top:66.0px;left:410.0px;");

		// subCateCheckBox
		subCateCheckBox = new CheckBox();
		subCateCheckBox.setCaption("All");
		subCateCheckBox.setImmediate(false);
		subCateCheckBox.setWidth("-1px");
		subCateCheckBox.setHeight("-1px");
		subCateCheckBox.setImmediate(true);
		mainLayout.addComponent(subCateCheckBox, "top:93.0px;left:410.0px;");

		lblAson = new Label();
		lblAson.setImmediate(false);
		lblAson.setWidth("-1px");
		lblAson.setHeight("-1px");
		lblAson.setValue("As on :");
		mainLayout.addComponent(lblAson, "top:118.0px;left:108.0px;");

		AsonDate= new PopupDateField();
		AsonDate.setWidth("110px");
		AsonDate.setDateFormat("dd-MM-yyyy");
		AsonDate.setValue(new java.util.Date());
		AsonDate.setResolution(PopupDateField.RESOLUTION_DAY);
		mainLayout.addComponent(AsonDate, "top:116.0px;left:150.0px;");
		
		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);
		mainLayout.addComponent(chkpdf, "top:145px; left:145px");

		chkother=new CheckBox("Other");
		chkother.setImmediate(true);
		mainLayout.addComponent(chkother, "top:145px; left:205px");

		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setValue("________________________________________________________________");
		mainLayout.addComponent(lblline, "top:185.0px;left:20.0px;");

		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));
		mainLayout.addComponent(previewButton,"top:205.opx; left:170.0px");

		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));
		mainLayout.addComponent(exitButton,"top:205.opx; left:250.0px");

		return mainLayout;
	}

}
