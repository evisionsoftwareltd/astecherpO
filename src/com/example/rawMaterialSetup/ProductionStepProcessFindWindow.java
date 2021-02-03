package com.example.rawMaterialSetup;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.common.share.AmountField;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.TextRead;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window.Notification;

public class ProductionStepProcessFindWindow extends Window
{
	private VerticalLayout mainLayout=new VerticalLayout();
	private FormLayout cmbLayout=new FormLayout();
	private HorizontalLayout btnLayout=new HorizontalLayout();
	private TextField txtReceiptSupplierId;
	private Table table=new Table();

	private String[] co=new String[]{"a","b"};
	public String receiptSubCateId = "";

	private ArrayList<Label> lbSubGroupID = new ArrayList<Label>();
	private ArrayList<Label> lbSubGroupName = new ArrayList<Label>();
	private ArrayList<Label> lbGroupName = new ArrayList<Label>();

	private SessionBean sessionBean;
	public ProductionStepProcessFindWindow(SessionBean sessionBean,TextField txtReceiptSupplierId)
	{
		this.txtReceiptSupplierId = txtReceiptSupplierId;
		this.sessionBean=sessionBean;
		this.setCaption("FIND PRODUCTION PROCESS STEP :: "+sessionBean.getCompany());
		this.center();
		this.setWidth("470px");
		this.setHeight("450");
		this.setCloseShortcut(KeyCode.ESCAPE);
		this.setModal(true);
		this.setResizable(false);
		this.setStyleName("cwindow");
		compInit();
		compAdd();
		tableInitialise();
		setEventAction();
		tableclear();
	    tableDataAdding();
	}

	public void tableInitialise()
	{
		for(int i=0;i<7;i++)
		{
			tableRowAdd(i);
		}
	}

	public void tableRowAdd(final int ar)
	{
		lbSubGroupID.add(ar, new Label(""));
		lbSubGroupID.get(ar).setWidth("100%");
		lbSubGroupID.get(ar).setImmediate(true);
		lbSubGroupID.get(ar).setHeight("23px");

		lbSubGroupName.add(ar, new Label(""));
		lbSubGroupName.get(ar).setWidth("100%");
		lbSubGroupName.get(ar).setImmediate(true);
		lbSubGroupName.get(ar).setHeight("23px");
		
		lbGroupName.add(ar, new Label(""));
		lbGroupName.get(ar).setWidth("100%");
		lbGroupName.get(ar).setImmediate(true);
		lbGroupName.get(ar).setHeight("23px");

		table.addItem(new Object[]{lbSubGroupID.get(ar),lbSubGroupName.get(ar),lbGroupName.get(ar)},ar);
	}

	public void setEventAction()
	{
		table.addListener(new ItemClickListener() 
		{
			public void itemClick(ItemClickEvent event) 
			{
				if(event.isDoubleClick())
				{
					receiptSubCateId = lbSubGroupID.get(Integer.valueOf(event.getItemId().toString())).getValue().toString();
					txtReceiptSupplierId.setValue(receiptSubCateId);
					windowClose();
				}
			}
		});
	}

	private void tableclear()
	{
		for(int i=0; i<lbSubGroupID.size(); i++)
		{
			lbSubGroupID.get(i).setValue("");
			lbSubGroupName.get(i).setValue("");
			lbGroupName.get(i).setValue("");
		}
	}

	private void tableDataAdding()
	{
		try{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			Transaction tx = session.beginTransaction();
			/*String query ="select iSubGroupId,vSubGroupName,b.vGroupName from tbSubGroupInfo a"
						+" inner join tbGroupInfo b on a.iGroupId=b.iGroupCode order by a.iGroupId";*/
			
			String query="select a.stepId,a.stepName,b.productTypeName from tbProductionstep a"
					+" inner join tbProductionType b on a.productionTypeId=b.productTypeId order by a.productionTypeId";
			System.out.println("Increment : "+query);
			List list = session.createSQLQuery(query).list();
			if(!list.isEmpty())
			{
				int i=0;
				for(Iterator iter = list.iterator(); iter.hasNext();)
				{						  
					Object[] element = (Object[]) iter.next();

					lbSubGroupID.get(i).setValue(element[0]);
					lbSubGroupName.get(i).setValue(element[1]);
					lbGroupName.get(i).setValue(element[2]);

					if((i)==lbSubGroupID.size()-1) {
						tableRowAdd(i+1);
					}
					i++;
				}
			}
			else {
				tableclear();
				this.getParent().showNotification("Data not Found !!", Notification.TYPE_WARNING_MESSAGE); 
			}
		}
		catch (Exception ex) {
			this.getParent().showNotification("Error", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
	}

	private void windowClose()
	{
		this.close();
	}

	private void compInit()
	{
		mainLayout.setSpacing(true);
		table.setSelectable(true);
		table.setWidth("100%");
		table.setHeight("350px");

		table.setImmediate(true);
		table.setColumnReorderingAllowed(true);
		table.setColumnCollapsingAllowed(true);		

		table.addContainerProperty("Step ID", Label.class, new Label());
		table.setColumnWidth("Step ID",60);

		table.addContainerProperty("Production Step Name", Label.class, new Label());
		table.setColumnWidth("Production Step Name",150);
		
		table.addContainerProperty("Production Type", Label.class, new Label());
		table.setColumnWidth("Production Type",150);
	}

	private void compAdd()
	{
		cmbLayout.setSpacing(true);
		mainLayout.addComponent(cmbLayout);
		mainLayout.addComponent(btnLayout);
		mainLayout.addComponent(table);
		addComponent(mainLayout);
	}
}