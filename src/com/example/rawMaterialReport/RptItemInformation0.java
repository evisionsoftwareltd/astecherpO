package com.example.rawMaterialReport;

import java.awt.Checkbox;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.ReportViewer;
import com.common.share.ReportViewerAuto;
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
import com.vaadin.ui.DateField;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class RptItemInformation0 extends Window {

	private AbsoluteLayout mainLayout;
	private ComboBox  cmbCategoryName;
	private Label lblCategoryName;
	private Label lblProductName;
	private ComboBox  cmbproductName;
	private Label lblsubcategoryName;
	private ComboBox cmbsubcategoryName;
	private CheckBox chkAllsubcategory;
	private ComboBox cmbsubsubCategory;
	private Label lblsubsubCategory;
	private Label lblline;
	private NativeButton previewButton = new NativeButton("Preview");
	private NativeButton exitButton = new NativeButton("Exit");
	SessionBean sessionBean;
	private CheckBox chkAllName;
	private CheckBox chkAllCategory;
	private CheckBox chkAllsubsubCategory;
	private Label lblCategoryType=new Label();
	private ComboBox cmbcategorytype=new ComboBox();
	private CheckBox chkallCategoryType=new CheckBox();
	private CheckBox chkpdf,chkother;
	boolean type=true;

	public  RptItemInformation0(SessionBean sessionBean,String str) {
		this.sessionBean = sessionBean;
		buildMainLayout();
		setContent(mainLayout);
		this.setResizable(false);
		this.setCaption("Item Information ::  "+ sessionBean.getCompany());
		cmbcategorytype.focus();
		Component ob[]={cmbcategorytype,cmbCategoryName,cmbsubcategoryName,cmbsubsubCategory,cmbproductName,previewButton};
		new FocusMoveByEnter(this, ob);
		categoryType();
		allButtonAction();

		cmbCategoryName.setEnabled(false);
		chkAllCategory.setEnabled(false);

		cmbsubcategoryName.setEnabled(false);
		chkAllsubcategory.setEnabled(false);

		cmbsubsubCategory.setEnabled(false);
		chkAllsubsubCategory.setEnabled(false);

		cmbproductName.setEnabled(false);
		chkAllName.setEnabled(false);

	}

	private AbsoluteLayout buildMainLayout() 
	{
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		//mainLayout.setWidth("520px");
		//mainLayout.setHeight("257px");
		mainLayout.setMargin(false);

		// top-level component properties
		setWidth("560px");
		setHeight("325px");

		lblCategoryType = new Label();
		lblCategoryType.setImmediate(false);
		lblCategoryType.setWidth("-1px");
		lblCategoryType.setHeight("-1px");
		lblCategoryType.setValue("Parent Type :");

		// cmbSection
		cmbcategorytype = new ComboBox();
		cmbcategorytype.setImmediate(true);
		cmbcategorytype.setWidth("260px");
		cmbcategorytype.setHeight("24px");
		cmbcategorytype.setNullSelectionAllowed(false);
		cmbcategorytype.setNewItemsAllowed(false);
		cmbcategorytype.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		// chkallCategoryType
		chkallCategoryType = new CheckBox("");
		chkallCategoryType.setCaption("All");
		chkallCategoryType.setWidth("-1px");
		chkallCategoryType.setHeight("24px");
		chkallCategoryType.setImmediate(true);
		chkallCategoryType.setVisible(false);		

		// lblSection
		lblCategoryName = new Label();
		lblCategoryName.setImmediate(false);
		lblCategoryName.setWidth("-1px");
		lblCategoryName.setHeight("-1px");
		lblCategoryName.setValue("Category  Name :");

		// cmbSection
		cmbCategoryName = new ComboBox();
		cmbCategoryName.setImmediate(true);
		cmbCategoryName.setWidth("260px");
		cmbCategoryName.setHeight("24px");
		cmbCategoryName.setNullSelectionAllowed(false);
		cmbCategoryName.setNewItemsAllowed(false);
		cmbCategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		// chkAllCategory
		chkAllCategory = new CheckBox("");
		chkAllCategory.setCaption("All");
		chkAllCategory.setWidth("-1px");
		chkAllCategory.setHeight("24px");
		chkAllCategory.setImmediate(true);

		// lblsubcategoryName
		lblsubcategoryName = new Label();
		lblsubcategoryName.setImmediate(false);
		lblsubcategoryName.setWidth("-1px");
		lblsubcategoryName.setHeight("-1px");
		lblsubcategoryName.setValue("Sub-Category Name :");

		// cmbsubcategoryName
		cmbsubcategoryName = new ComboBox();
		cmbsubcategoryName.setWidth("260px");
		cmbsubcategoryName.setHeight("24px");
		cmbsubcategoryName.setImmediate(true);
		cmbsubcategoryName.setNewItemsAllowed(false);
		cmbsubcategoryName.setNullSelectionAllowed(false);
		cmbsubcategoryName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		// chkAllsubcategory
		chkAllsubcategory=new CheckBox("");
		chkAllsubcategory.setCaption("All");
		chkAllsubcategory.setImmediate(true);
		chkAllsubcategory.setWidth("-1px");
		chkAllsubcategory.setHeight("24px");

		// lblsubsubCategory
		lblsubsubCategory = new Label();
		lblsubsubCategory.setImmediate(false);
		lblsubsubCategory.setWidth("-1px");
		lblsubsubCategory.setHeight("-1px");
		lblsubsubCategory.setValue("Sub Sub-Category Name :");

		// cmbsubsubCategory
		cmbsubsubCategory = new ComboBox();
		cmbsubsubCategory.setWidth("260px");
		cmbsubsubCategory.setHeight("24px");
		cmbsubsubCategory.setImmediate(true);
		cmbsubsubCategory.setNewItemsAllowed(false);
		cmbsubsubCategory.setNullSelectionAllowed(false);
		cmbsubsubCategory.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);

		// chkAllsubsubCategory
		chkAllsubsubCategory=new CheckBox("");
		chkAllsubsubCategory.setCaption("All");
		chkAllsubsubCategory.setImmediate(true);
		chkAllsubsubCategory.setWidth("-1px");
		chkAllsubsubCategory.setHeight("24px");

		// lblProductName
		lblProductName = new Label();
		lblProductName.setImmediate(false);
		lblProductName.setWidth("-1px");
		lblProductName.setHeight("-1px");
		lblProductName.setValue("Item Name :");

		// cmbproductName
		cmbproductName = new ComboBox();
		cmbproductName.setImmediate(true);
		cmbproductName.setWidth("260px");
		cmbproductName.setHeight("24px");
		cmbproductName.setNullSelectionAllowed(false);
		cmbproductName.setNewItemsAllowed(false);
		cmbproductName.setFilteringMode(ComboBox.FILTERINGMODE_CONTAINS);
		// chkAllName
		chkAllName = new CheckBox("");
		chkAllName.setCaption("All");
		chkAllName.setWidth("-1px");
		chkAllName.setHeight("24px");
		chkAllName.setImmediate(true);

		// chkpdf
		chkpdf=new CheckBox("PDF");
		chkpdf.setImmediate(true);
		chkpdf.setValue(true);

		// chkother
		chkother=new CheckBox("Other");
		chkother.setImmediate(true);

		// lblline
		lblline = new Label();
		lblline.setImmediate(false);
		lblline.setWidth("-1px");
		lblline.setHeight("-1px");
		lblline.setContentMode(Label.CONTENT_XHTML);
		lblline.setValue("<font color='#e65100'>======================================================================================================================</font>");

		// previewButton
		previewButton.setWidth("95px");
		previewButton.setHeight("28px");
		previewButton.setIcon(new ThemeResource("../icons/print.png"));

		// exitButton
		exitButton.setWidth("70px");
		exitButton.setHeight("28px");
		exitButton.setIcon(new ThemeResource("../icons/exit1.png"));

		mainLayout.addComponent(lblCategoryType, "top:40.0px;left:40.0px;");
		mainLayout.addComponent( cmbcategorytype, "top:38.0px;left:186.0px;");
		mainLayout.addComponent(chkallCategoryType, "top:40.0px;left:448.0px;");

		mainLayout.addComponent(lblCategoryName, "top:70.0px;left:40.0px;");
		mainLayout.addComponent( cmbCategoryName, "top:68px;left:186.0px;");	
		mainLayout.addComponent(chkAllCategory, "top:70.0px;left:448.0px;");

		mainLayout.addComponent(lblsubcategoryName, "top:100.0px;left:40.0px;");
		mainLayout.addComponent( cmbsubcategoryName, "top:98.0px;left:186.0px;");
		mainLayout.addComponent(chkAllsubcategory, "top:100.0px;left:448.0px;");

		mainLayout.addComponent(lblsubsubCategory, "top:130.0px;left:40.0px;");
		mainLayout.addComponent( cmbsubsubCategory, "top:128.0px;left:186.0px;");
		mainLayout.addComponent(chkAllsubsubCategory, "top:130.0px;left:448.0px;");

		mainLayout.addComponent(lblProductName, "top:160.0px;left:40.0px;");
		mainLayout.addComponent(cmbproductName, "top:158.0px;left:186.0px;");
		mainLayout.addComponent(chkAllName, "top:160.0px; left:448.0px;");

		mainLayout.addComponent(chkpdf, "top:190px; left:186.0px");
		mainLayout.addComponent(chkother, "top:190px; left:245px");
		mainLayout.addComponent(lblline, "top:210.0px;left:0.0px;");
		mainLayout.addComponent(previewButton,"top:240.opx; left:186.0px");
		mainLayout.addComponent(exitButton,"top:240.opx; left:280.0px");

		return mainLayout;
	}

	private void categoryData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String categorytype="";

		if(cmbcategorytype.getValue()!=null)
		{
			categorytype=cmbcategorytype.getValue().toString();
		}

		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";
		}

		String query = "  select  Group_Id ,vCategoryName  from tbRawItemCategory where vCategoryType  like '"+categorytype+"'  order by vCategoryName";

		System.out.println(query);

		cmbCategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbCategoryName.addItem(element[0]);
			cmbCategoryName.setItemCaption(element[0], element[1].toString());
		}
	}

	private void categoryType()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();

		String query = "  select  distinct 0,vCategoryType from tbRawItemCategory order by vCategoryType";

		System.out.println(query);

		cmbCategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();		
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbcategorytype.addItem(element[1]);
			cmbcategorytype.setItemCaption(element[1], element[1].toString());
		}
	}

	private void productData()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";

		if(chkAllCategory.booleanValue())
			strcategory="%";
		else
			strcategory=cmbCategoryName.getValue().toString();

		if(chkAllsubcategory.booleanValue())
			strsubcategory="%";
		else
			strsubcategory=cmbsubcategoryName.getValue().toString();

		String  query=  "select vRawItemCode, vRawItemName,vGroupId from tbRawItemInfo  where" +
				" vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"' order by  vRawItemName" ;


		System.out.println(query);

		List list = session.createSQLQuery(query).list();
		cmbproductName.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbproductName.addItem(element[0]);
			cmbproductName.setItemCaption(element[0], element[1].toString());
		}
	}


	private void subcategorydataload()
	{

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();

		String categoryType="";
		String Category="";

		if(chkallCategoryType.booleanValue())
		{
			categoryType="%";	
		}

		if(cmbcategorytype.getValue()!=null)
		{
			categoryType=cmbcategorytype.getValue().toString();
		}

		if(chkAllCategory.booleanValue())
		{
			Category="%";
		}
		if(cmbCategoryName.getValue()!=null)
		{
			Category=cmbCategoryName.getValue().toString();
		}


		String query = "select SubGroup_Id, vSubCategoryName from tbRawItemSubCategory where " +
				"Group_Id like '"+Category+"'and vCategoryType like '"+categoryType+"' order by vSubCategoryName";

		System.out.println(query);

		cmbsubcategoryName.removeAllItems();
		List list = session.createSQLQuery(query).list();
		if(!list.isEmpty())
		{
			for(Iterator iter = list.iterator(); iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbsubcategoryName.addItem(element[0]);
				cmbsubcategoryName.setItemCaption(element[0], element[1].toString());
			}
		}
	}

	private void productDataload()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";
		String strsubsubCategory="";
		String subsubcategory="";
		String categorytype="";
		if(chkAllCategory.booleanValue())
		{
			strcategory="%";
		}
		else
		{
			strcategory=cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue())
		{
			strsubcategory="%";
		}
		else
		{
			strsubcategory=cmbsubcategoryName.getValue().toString();
		}
		if(chkAllsubsubCategory.booleanValue())
		{
			subsubcategory="%";
		}
		else
		{
			subsubcategory=cmbsubsubCategory.getValue().toString();
		}

		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";
		}
		else
		{
			categorytype=cmbcategorytype.getValue().toString();	
		}

		String query= " select vRawItemCode,vRawItemName  from tbRawItemInfo a inner join tbRawItemCategory b on " +
				"a.vGroupId=b.Group_Id where vGroupId like '"+strcategory+"' and vSubGroupId like '"+strsubcategory+"' " +
						"and  vsubsubCategoryId like '"+subsubcategory+"'  and a.vCategoryType like '"+categorytype+"'  order by vRawItemName";


		System.out.println(query);

		List list = session.createSQLQuery(query).list();
		cmbproductName.removeAllItems();
		for(Iterator iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbproductName.addItem(element[0]);
			cmbproductName.setItemCaption(element[0], element[1].toString());
		}
	}

	private void SubsubCategoryDataAdd()
	{
		Session session = SessionFactoryUtil.getInstance().getCurrentSession();
		Transaction tx;
		tx = session.beginTransaction();
		String strcategory="", strsubcategory="";
		String categorytype="";

		if(chkAllCategory.booleanValue())
		{
			strcategory="%";
		}
		else
		{
			strcategory=cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue())
		{
			strsubcategory="%";
		}
		else
		{
			strsubcategory=cmbsubcategoryName.getValue().toString();
		}

		if(chkallCategoryType.booleanValue())
		{
			categorytype="%";	
		}
		else
		{
			categorytype=cmbcategorytype.getValue().toString();
		}

		String query= " select iSubCategoryid,vSubSubCategoryName from tbRawItemsubSubCategory " +
				" where groupid like '"+strcategory+"' and SubGroupid like '"+strsubcategory+"' and " +
						"vCategoryType like '"+categorytype+"'  order by vSubSubCategoryName";


		System.out.println(query);

		List<?> list = session.createSQLQuery(query).list();
		cmbsubsubCategory.removeAllItems();
		for(Iterator<?> iter = list.iterator(); iter.hasNext();)
		{
			Object[] element = (Object[]) iter.next();
			cmbsubsubCategory.addItem(element[0]);
			cmbsubsubCategory.setItemCaption(element[0], element[1].toString());
		}
	}

	private void allButtonAction()
	{
		chkpdf.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(chkpdf.booleanValue())
				{
					chkother.setValue(false);
					type=true;
				}

				else
				{
					chkother.setValue(true);
					type=false;
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
					type=false;

				}
				else
				{

					chkpdf.setValue(true);
					type=true;
				}
			}
		});

		cmbcategorytype.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbcategorytype.getValue()!=null)
				{

					//productDataload();
					cmbCategoryName.setEnabled(true);
					chkAllCategory.setEnabled(true);

					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					
					cmbsubcategoryName.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);
					
					cmbsubsubCategory.setValue(null);
					chkAllsubsubCategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					
					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					
					chkAllCategory.setValue(false);

					categoryData();
				}

				else
				{
					if(!chkallCategoryType.booleanValue())
					{
					cmbCategoryName.setEnabled(false);
					chkAllCategory.setEnabled(false);		
					
					cmbCategoryName.setValue(null);
					chkAllCategory.setValue(false);

					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);
					
					cmbsubcategoryName.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);
					
					cmbsubsubCategory.setValue(null);
					chkAllsubsubCategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					
					cmbproductName.setValue(null);
					chkAllName.setValue(false);
				}
				}
			}
		});
		
		chkallCategoryType.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) 
			{
				if(chkallCategoryType.booleanValue())
				{
					cmbcategorytype.setValue(null);
					cmbcategorytype.setEnabled(false);
					categoryData(); 
				}
			}
		});

		cmbCategoryName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(cmbCategoryName.getValue()!=null)
				{
					cmbsubcategoryName.setEnabled(true);
					chkAllsubcategory.setEnabled(true);

					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);

					cmbsubsubCategory.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);

					subcategorydataload();
				}
				else
				{
					cmbsubcategoryName.setEnabled(false);
					chkAllsubcategory.setEnabled(false);

					cmbsubsubCategory.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);

					cmbsubsubCategory.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
				}
			}
		});

		chkAllCategory.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(chkAllCategory.booleanValue()==true)
				{
						cmbCategoryName.setValue(null);
						cmbCategoryName.setEnabled(false); 
						
						cmbsubcategoryName.setEnabled(true);
						chkAllsubcategory.setEnabled(true);

						cmbsubsubCategory.setEnabled(false);
						chkAllsubsubCategory.setEnabled(false);

						cmbsubcategoryName.setValue(null);
						chkAllsubsubCategory.setValue(false);

						cmbproductName.setEnabled(false);
						chkAllName.setEnabled(false);

						cmbproductName.setValue(null);
						chkAllName.setValue(false);

						subcategorydataload(); 
					}
					else
					{
						cmbCategoryName.setEnabled(true);
						cmbsubcategoryName.setEnabled(false);
						chkAllsubcategory.setEnabled(false);

						cmbsubcategoryName.setValue(null);
						chkAllsubcategory.setValue(false);

						cmbsubsubCategory.setEnabled(false);
						chkAllsubsubCategory.setEnabled(false);

						cmbsubsubCategory.setValue(null);
						chkAllsubsubCategory.setValue(false);

						cmbproductName.setEnabled(false);
						chkAllName.setEnabled(false);

						cmbproductName.setValue(null);
						chkAllName.setValue(false);
						cmbsubcategoryName.removeAllItems();
						categoryData();
					}
			}
		});
		cmbsubcategoryName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
					if(cmbsubcategoryName.getValue()!=null)
					{
					cmbsubsubCategory.setEnabled(true);
					chkAllsubsubCategory.setEnabled(true);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);

					SubsubCategoryDataAdd();
				}
				else
				{
					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);

					cmbsubsubCategory.setValue(null);
					chkAllsubsubCategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					cmbsubsubCategory.removeAllItems();
					subcategorydataload();
				}
			}
		});
		
		chkAllsubcategory.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				
				if(chkAllsubcategory.booleanValue()==true)
				{
					cmbsubcategoryName.setValue(null);
					cmbsubcategoryName.setEnabled(false);

					cmbsubsubCategory.setEnabled(true);
					chkAllsubsubCategory.setEnabled(true);

					cmbsubsubCategory.setValue(null);
					chkAllsubsubCategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);

					SubsubCategoryDataAdd();
				}
				else
				{
					cmbsubcategoryName.setEnabled(true);

					cmbsubcategoryName.setValue(null);
					chkAllsubcategory.setValue(false);

					cmbsubsubCategory.setEnabled(false);
					chkAllsubsubCategory.setEnabled(false);

					cmbsubsubCategory.setValue(null);
					chkAllsubsubCategory.setValue(false);

					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);

					cmbproductName.setValue(null);
					chkAllName.setValue(false);
					cmbsubsubCategory.removeAllItems();
					subcategorydataload();
				}
			}
		});
		
		cmbsubsubCategory.addListener(new ValueChangeListener() {
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbsubsubCategory.getValue()!=null)
				{
					cmbproductName.setEnabled(true);
					chkAllName.setEnabled(true);
					productDataload(); 
				}
				else
				{
					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					cmbproductName.removeAllItems();
					SubsubCategoryDataAdd();
				}

			}
		});

		chkAllsubsubCategory.addListener(new ValueChangeListener() {

			public void valueChange(ValueChangeEvent event) 
			{
				if(chkAllsubsubCategory.booleanValue()==true)
				{
						cmbsubsubCategory.setEnabled(false);
						cmbsubsubCategory.setValue(null);

						cmbproductName.setEnabled(true);
						cmbproductName.setValue(null);

						chkAllName.setEnabled(true);
						chkAllName.setValue(false);
						if((cmbsubsubCategory.getValue()!=null || chkAllsubsubCategory.booleanValue()) && (cmbsubcategoryName.getValue()!=null || chkAllsubcategory.booleanValue()) && (cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()) && (cmbcategorytype.getValue()!=null))
						productDataload(); 
					}
				
				else
				{
					cmbsubsubCategory.setValue(null);
					cmbsubsubCategory.setEnabled(true);
					cmbproductName.setEnabled(false);
					chkAllName.setEnabled(false);
					cmbproductName.removeAllItems();
					if( (cmbsubcategoryName.getValue()!=null || chkAllsubcategory.booleanValue()) && (cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()) && (cmbcategorytype.getValue()!=null))
					SubsubCategoryDataAdd();
					
				}
			}
	});
		previewButton.addListener(new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) {
				if(cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()==true)
				{
					if(cmbproductName.getValue()!=null || chkAllName.booleanValue()==true)
					{
						reportShow();
					}

					else
					{
						showNotification("Select Product Name", Notification.TYPE_WARNING_MESSAGE);
					}

				}
				else
				{
					getParent().showNotification("Select Category Name", Notification.TYPE_WARNING_MESSAGE);
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
		chkAllName.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event) 
			{
				if(chkAllName.booleanValue()==true)
				{
					cmbproductName.setValue(null);
					cmbproductName.setEnabled(false);
				}
				else{
					cmbproductName.setEnabled(true);
					if((cmbsubsubCategory.getValue()!=null || chkAllsubsubCategory.booleanValue()) && (cmbsubcategoryName.getValue()!=null || chkAllsubcategory.booleanValue()) && (cmbCategoryName.getValue()!=null || chkAllCategory.booleanValue()) && (cmbcategorytype.getValue()!=null ))
					productDataload();
				}
			}
		});
	}
	private void reportShow()
	{
		System.out.println("into Reportshow");
		String query=null;
		String activeFlag = null;
		String categoryID="";
		String productID="";
		String subcategoryID="";
		String categirytype="";
		String subsubcategory="";


		if(chkAllCategory.booleanValue())
		{
			categoryID ="%"; 
		}
		else
		{
			categoryID = cmbCategoryName.getValue().toString();
		}

		if(chkAllsubcategory.booleanValue())
		{
			subcategoryID="%";
		}
		else
		{
			subcategoryID=cmbsubcategoryName.getValue().toString();
		}

		if(chkAllName.booleanValue())
		{
			productID ="%";
		}
		else
		{
			productID=cmbproductName.getValue().toString();
		}

		if(cmbcategorytype.getValue()!=null)
		{
			categirytype=cmbcategorytype.getValue().toString();	
		}

		if(chkallCategoryType.booleanValue())
		{
			categirytype="%";	
		}

		if(cmbsubsubCategory.getValue()!=null)
		{
			subsubcategory=cmbsubsubCategory.getValue().toString();
		}

		if(chkAllsubsubCategory.booleanValue())
		{
			subsubcategory="%";	
		}
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();

			HashMap hm = new HashMap();
			hm.put("parentType", cmbcategorytype.getValue().toString());
			hm.put("company", sessionBean.getCompany());
			hm.put("address", sessionBean.getCompanyAddress());
			hm.put("Phone", sessionBean.getCompanyContact());
			hm.put("username", sessionBean.getUserName()+"  "+sessionBean.getUserIp());
			hm.put("logo", sessionBean.getCompanyLogo());

			System.out.println("Logo Image: "+sessionBean.getCompanyLogo());
			query= " select * from tbRawItemInfo a inner join tbRawItemCategory b "
					+" on a.vGroupId=b.Group_Id "
					+"where a.vCategoryType like '"+categirytype+"' and a.vGroupId like '"+categoryID+"' and a.vSubGroupId like " +
					"'"+subcategoryID+"' and a.vsubsubCategoryId like '"+subsubcategory+"' and a.vRawItemCode like '"+productID+"'  " +
					"order by  vGroupName   ,vSubGroupName ,vsubsubCategoryName,vRawItemName";

			System.out.println(query);
			hm.put("sql", query);
			System.out.println("123");

			/*Window win = new ReportViewer(hm,"report/raw/rawProductInfoRpt.jasper",query);

			System.out.println("789");

			win.setCaption("Project Report");
			
			
			
			//this.getParent().getWindow().addWindow(win);*/
			
			//new ReportViewer(hm,"report/raw/rawProductInfoRpt.jasper",query);
			
			
			Window win = new ReportViewer(hm,"report/raw/rawProductInfoRpt.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",type);
			
			
			/*Window win=	 new ReportViewer(hm,"report/raw/rawProductInfoRpt.jasper",
					this.getWindow().getApplication().getContext().getBaseDirectory()+"".replace("\\","/")+"/VAADIN/rpttmp",
					this.getWindow().getApplication().getURL()+"VAADIN/rpttmp",false,
					this.getWindow().getApplication().getURL()+"VAADIN/applet",1);*/
			win.setCaption("Project Report");
			this.getParent().getWindow().addWindow(win);
			
			
		}
		catch(Exception exp){

			this.getParent().showNotification("Error "+exp,Notification.TYPE_ERROR_MESSAGE);

		}
	}
}
