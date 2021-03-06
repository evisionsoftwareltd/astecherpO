package acc.appform.transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.common.share.CommaSeparator;
import com.common.share.CommonButton;
import com.common.share.FocusMoveByEnter;
import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class DebitNoteFindWindow extends Window
{
	private SessionBean sessionBean;
	private VerticalLayout mainLayout = new VerticalLayout();
	private HorizontalLayout hLayout = new HorizontalLayout();
	private PopupDateField fromDate = new PopupDateField();
	private PopupDateField toDate = new PopupDateField();
	private Label lblFrom = new Label("Form:");
	private Label lblTo = new Label("To:");
	private TextField txtReceiptId;

	CommonButton cButton = new CommonButton("", "", "", "","","Find","","","","");

	private Table table = new Table();

	private ComboBox cmbSupplierName;

	private ArrayList<Label>lblIdAndDate = new ArrayList<Label>();	
	private ArrayList<Component> allComp = new ArrayList<Component>();

	public DebitNoteFindWindow(SessionBean sessionBean,TextField txtReceiptId,String frmName)
	{
		this.sessionBean = sessionBean;
		this.txtReceiptId = txtReceiptId;
		this.setCaption("DEBIT NOTE FIND WINDOW :: "+sessionBean.getCompany());
		this.center();
		this.setWidth("700px");
		this.setCloseShortcut(KeyCode.ESCAPE);
		this.setModal(true);
		this.setResizable(false);
		this.setStyleName("cwindow");

		compInit();
		compAdd();
		tableInitialise();
		partyNameData();

		setEventAction();
		focusEnter();
	}

	public void partyNameData()
	{
		cmbSupplierName.removeAllItems();
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			session.beginTransaction();
			List<?> lst = session.createSQLQuery("select distinct vSupplierId,vSupplierName"
					+ " from tbDebitNote order by vSupplierName").list();
			for(Iterator<?> iter = lst.iterator();iter.hasNext();)
			{
				Object[] element = (Object[]) iter.next();
				cmbSupplierName.addItem(element[0].toString());
				cmbSupplierName.setItemCaption(element[0].toString(), element[1].toString());
			}
		}
		catch (Exception ex) 
		{
			System.out.print(ex);
		} 
	}

	public void tableInitialise()
	{
		for(int i=0;i<10;i++)
		{
			tableRowAdd(i);
		}
	}

	private void focusEnter()
	{
		allComp.add(cmbSupplierName);
		allComp.add(fromDate);
		allComp.add(toDate);
		allComp.add(cButton.btnFind);

		new FocusMoveByEnter(this,allComp);
	}

	public void tableRowAdd(final int ar)
	{
		lblIdAndDate.add(ar,new Label());
		lblIdAndDate.get(ar).setImmediate(true);

		table.addItem(new Object[]{lblIdAndDate.get(ar)},ar);
	}

	public void setEventAction()
	{
		cButton.btnFind.addListener( new Button.ClickListener() 
		{
			public void buttonClick(ClickEvent event) 
			{
				if(cmbSupplierName.getValue()!=null)
				{
					tableClear();
					findButtonEvent();
				}
				else
				{
					showNotification("Warning!","Select supplier name.",Notification.TYPE_WARNING_MESSAGE);
					cmbSupplierName.focus();
				}
			}
		});

		table.addListener(new ItemClickListener() 
		{
			public void itemClick(ItemClickEvent event) 
			{
				if(event.isDoubleClick())
				{
					txtReceiptId.setValue(lblIdAndDate.get(Integer.valueOf(event.getItemId().toString())).getValue().toString());
					close();
				}
			}
		});
	}

	private void findButtonEvent()
	{
		String auditApp = "Not Audit";
		try
		{
			Session session = SessionFactoryUtil.getInstance().getCurrentSession();
			session.beginTransaction();
			String sql = " select vReferenceNo,dDate,mAmount,vModeOfPayment,vUserName,iApproveFlag from tbDebitNote where"
					+ " vSupplierId = '"+cmbSupplierName.getValue().toString()+"' and"
					+ " dDate between '"+sessionBean.dfDb.format(fromDate.getValue())+"' and"
					+ " '"+sessionBean.dfDb.format(toDate.getValue())+"' order by dDate ";
			List<?> lst = session.createSQLQuery(sql).list();
			int i=1;
			if(!lst.isEmpty())
			{
				for(Iterator<?> iter = lst.iterator(); iter.hasNext();)
				{
					Object[] element = (Object[]) iter.next();

					if(i==lblIdAndDate.size())
					{
						tableRowAdd(i);
					}
					if(element[5].toString().equals("1"))
					{
						auditApp = "Not Approve";
					}
					else if(element[5].toString().equals("2"))
					{
						auditApp = "Approved";
					}
					table.addItem(new Object[] {i,element[0].toString(), sessionBean.dfBd.format(element[1]).toString(),
							new CommaSeparator().setComma(Double.parseDouble(element[2].toString())), element[3].toString(),
							element[4].toString(),(auditApp)}, new Integer(i));
					lblIdAndDate.get(i).setValue(element[0].toString());
					i++;
				}
			}
			else
			{
				getParent().showNotification("Warning!","There are no data.",Notification.TYPE_WARNING_MESSAGE);					
			}
		}
		catch(Exception exp)
		{
			System.out.println(exp);
		}
	}

	private void compInit()
	{
		cmbSupplierName = new ComboBox();
		cmbSupplierName.setImmediate(true);
		cmbSupplierName.setWidth("270px");
		cmbSupplierName.setHeight("-1px");

		fromDate.setValue(new java.util.Date());
		fromDate.setResolution(PopupDateField.RESOLUTION_DAY);
		fromDate.setDateFormat("dd-MM-yyyy");
		fromDate.setWidth("110px");
		fromDate.setInvalidAllowed(false);
		fromDate.setImmediate(true);

		toDate.setValue(new java.util.Date());
		toDate.setResolution(PopupDateField.RESOLUTION_DAY);
		toDate.setWidth("110px");
		toDate.setDateFormat("dd-MM-yyyy");
		toDate.setInvalidAllowed(false);
		toDate.setImmediate(true);

		mainLayout.setSpacing(true);
		table.setSelectable(true);
		table.setWidth("100%");
		table.setHeight("250px");

		table.setImmediate(true); // react at once when something is selected
		table.setColumnReorderingAllowed(true);
		table.setColumnCollapsingAllowed(true);

		table.addContainerProperty("SL", Label.class, new Label());
		table.setColumnWidth("SL",20);

		table.addContainerProperty("Reference No", Label.class, new Label());
		table.setColumnWidth("Reference No",100);

		table.addContainerProperty("Date", Label.class, new Label());
		table.setColumnWidth("Date",65);

		table.addContainerProperty("Amount", Label.class, new Label());
		table.setColumnWidth("Amount",100);

		table.addContainerProperty("Pay Type", Label.class, new Label());
		table.setColumnWidth("Pay Type",60);

		table.addContainerProperty("Prepared By", Label.class, new Label());
		table.setColumnWidth("Prepared By",80);

		table.addContainerProperty("Approve", Label.class, new Label());
		table.setColumnWidth("Approve",80);

		table.setColumnAlignments(new String[] { Table.ALIGN_CENTER, Table.ALIGN_LEFT,
				Table.ALIGN_CENTER, Table.ALIGN_RIGHT, Table.ALIGN_LEFT, Table.ALIGN_LEFT, Table.ALIGN_LEFT});	
	}

	private void compAdd()
	{
		hLayout.setSpacing(true);
		hLayout.addComponent(cmbSupplierName);
		hLayout.addComponent(lblFrom);
		hLayout.addComponent(fromDate);
		hLayout.addComponent(lblTo);
		hLayout.addComponent(toDate);
		hLayout.addComponent(cButton.btnFind);
		mainLayout.addComponent(hLayout);
		mainLayout.addComponent(table);
		addComponent(mainLayout);
	}

	private void tableClear()
	{
		table.removeAllItems();
	}
}