package acc.appform.hrmModule;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import com.common.share.SessionBean;
import com.common.share.SessionFactoryUtil;
import com.common.share.TextRead;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class LoanRecoveryFind extends Window 
{
	@SuppressWarnings("unused")
	private SessionBean sessionBean;	
	private AbsoluteLayout mainLayout;

	private Label lblEmployeeName;
	private ComboBox cmbEmployeeName;
	
	private Label lblLoanNo;
	private ComboBox cmbLoanNo ;

	private Table table = new Table();
	private ArrayList<Label> lbSL = new ArrayList<Label>();
	private ArrayList<Label> lbTranId = new ArrayList<Label>();
	private ArrayList<Label> lbRecoveryDate = new ArrayList<Label>();
	private ArrayList<Label> lbRecoveryAmount = new ArrayList<Label>();
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	String autoId = "";
	String empId = "";
	String findDate = "";

	private TextRead txtTranId = new TextRead();
	private TextRead txtFindDate = new TextRead();

	public LoanRecoveryFind(SessionBean sessionBean, TextRead txtTranId, TextRead txtFindDate)
	{		
		this.txtTranId = txtTranId;
		this.txtFindDate = txtFindDate;
		this.sessionBean = sessionBean;
		this.setCaption("FIND LOAN RECOVERY :: "+sessionBean.getCompany());
		this.center();
		this.setWidth("570px");
		this.setCloseShortcut(KeyCode.ESCAPE);
		this.setModal(true);
		this.setResizable(false);
		this.setStyleName("cwindow");

		buildMainLayout();
		setContent(mainLayout);
		tableinitialization();
		cmbAddEmployeeData();
		setEventActions();
		cmbLoanNo.focus();
	}

	private void tableinitialization()
	{
		table.setColumnCollapsingAllowed(true);
		table.setWidth("98%");
		table.setHeight("200px");
		table.setPageLength(0);

		table.addContainerProperty("SL #", Label.class , new Label());
		table.setColumnWidth("SL #",40);
		
		table.addContainerProperty("Recovery Date", Label.class , new Label());
		table.setColumnWidth("Recovery Date",110);

		table.addContainerProperty("Recovery Amount", Label.class , new Label());
		table.setColumnWidth("Recovery Amount",100);
		
		table.setColumnAlignments(new String[] {Table.ALIGN_CENTER, Table.ALIGN_CENTER, Table.ALIGN_RIGHT});
		rowAddinTable();	
	}

	public void rowAddinTable()
	{
		for(int i=0; i<10; i++)
		{
			tableRowAdd(i);
		}
	}

	public void tableRowAdd(final int ar)
	{
		table.setSelectable(true);
		table.setImmediate(true);
		table.setColumnReorderingAllowed(true);
		table.setColumnCollapsingAllowed(true);
		
		lbSL.add(ar, new Label(""));
		lbSL.get(ar).setWidth("100%");
		lbSL.get(ar).setHeight("20px");
		lbSL.get(ar).setValue(ar+1);
		
		lbTranId.add(ar, new Label());

		lbRecoveryDate.add(ar,new Label());
		lbRecoveryDate.get(ar).setWidth("100%");
		lbRecoveryDate.get(ar).setHeight("14px");

		lbRecoveryAmount.add(ar, new Label(""));
		lbRecoveryAmount.get(ar).setWidth("100%");
		lbRecoveryAmount.get(ar).setHeight("14px");

		table.addItem(new Object[]{lbSL.get(ar),lbRecoveryDate.get(ar),lbRecoveryAmount.get(ar)},ar);
	}

	private void cmbAddEmployeeData()
	{
		Session session = SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			String query = "Select distinct vAutoEmployeeId,vEmployeeId FROM tbLoanRecoveryInfo";
			List <?> list = session.createSQLQuery(query).list();

			for (Iterator <?> iter = list.iterator(); iter.hasNext();)
			{
				Object[] element =  (Object[]) iter.next();	
				cmbEmployeeName.addItem(element[0]);
				cmbEmployeeName.setItemCaption(element[0], element[1].toString());	
			}
		}
		catch(Exception ex)
		{
			showNotification("cmbAddEmployeeData", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally{session.close();}
	}

	private void setEventActions()
	{
		cmbEmployeeName.addListener(new ValueChangeListener()
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbEmployeeName.getValue()!=null)
				{
					employeeLoanData(cmbEmployeeName.getValue().toString());
				}
			}
		});
		
		cmbLoanNo.addListener(new ValueChangeListener() 
		{
			public void valueChange(ValueChangeEvent event)
			{
				if(cmbLoanNo.getValue()!=null)
				{
					findButtonEvent();
				}
			}
		});

		table.addListener(new ItemClickListener() 
		{
			public void itemClick(ItemClickEvent event) 
			{
				if(event.isDoubleClick())
				{
					autoId = lbTranId.get(Integer.valueOf(event.getItemId().toString())).getValue().toString();
					txtTranId.setValue(autoId);
					
					txtFindDate.setValue(lbRecoveryDate.get(Integer.valueOf(event.getItemId().toString())).getValue());
					windowClose();
				}
			}
		});
	}

	private void findButtonEvent()
	{
		Session session = SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			String Findquery = " select vTransactionId,dRecoveryDate,mRecoveryAmount from tbLoanRecoveryInfo" +
							" where vLoanNo='"+cmbLoanNo.getValue().toString()+"' and vAutoEmployeeId='"+cmbEmployeeName.getValue().toString()+"' order by dRecoveryDate ";
			List <?> list = session.createSQLQuery(Findquery).list();

			if(!list.isEmpty())
			{
				tableclear();
				int i=0;
				for(Iterator <?> iter = list.iterator(); iter.hasNext();)
				{
					Object[] element = (Object[]) iter.next();
					lbTranId.get(i).setValue(element[0].toString());
					lbRecoveryDate.get(i).setValue(dateFormat.format(element[1]));//dFormFormat.format
					lbRecoveryAmount.get(i).setValue(df.format(Double.parseDouble((element[2].toString()))));
					
					if((i)==lbRecoveryDate.size()-1)
					{
						tableRowAdd(i+1);
					}
					i++;
				}
			}
			else
			{
				tableclear();
				showNotification("No data found!", Notification.TYPE_WARNING_MESSAGE); 
			}
		}
		catch (Exception ex)
		{
			showNotification("findButtonEvent", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally{session.close();}
	}
	
	private void employeeLoanData(String empId)
	{
		cmbLoanNo.removeAllItems();
		Session session = SessionFactoryUtil.getInstance().openSession();
		session.beginTransaction();
		try
		{
			String query = " select dRecoveryDate,vLoanNo from tbLoanRecoveryInfo where " +
					"vAutoEmployeeId=(select vEmployeeId from tbEmployeeInfo where vEmployeeId='"+empId+"') --and iLoanStatus='1' ";
			List <?> list = session.createSQLQuery(query).list();

			for (Iterator <?> iter = list.iterator(); iter.hasNext();)
			{
				Object[] element =  (Object[]) iter.next();	
				cmbLoanNo.addItem(element[1]);
				cmbLoanNo.setItemCaption(element[1], element[1].toString());	
			}
		}
		catch(Exception ex)
		{
			showNotification("employeeLoanData", ex.toString(), Notification.TYPE_ERROR_MESSAGE);
		}
		finally{session.close();}
	}

	private void tableclear()
	{
		for(int i=0; i<lbSL.size(); i++)
		{
			lbRecoveryDate.get(i).setValue("");
			lbRecoveryAmount.get(i).setValue("");
		}
	}

	private void windowClose()
	{
		this.close();
	}

	private AbsoluteLayout buildMainLayout()
	{
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(true);
		mainLayout.setMargin(false);

		setWidth("330px");
		setHeight("330px");

		lblEmployeeName = new Label();
		lblEmployeeName.setImmediate(true);
		lblEmployeeName.setWidth("-1px");
		lblEmployeeName.setHeight("-1px");
		lblEmployeeName.setValue("Employee ID :");
		mainLayout.addComponent(lblEmployeeName, "top:20.0px;left:15.0px;");

		cmbEmployeeName = new ComboBox();
		cmbEmployeeName.setImmediate(true);
		cmbEmployeeName.setWidth("190px");
		cmbEmployeeName.setHeight("-1px");
		cmbEmployeeName.setImmediate(true);
		cmbEmployeeName.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbEmployeeName, "top:18.0px;left:120.0px;");

		lblLoanNo = new Label();
		lblLoanNo.setImmediate(true);
		lblLoanNo.setWidth("-1px");
		lblLoanNo.setHeight("-1px");
		lblLoanNo.setValue("Loan No :");
		mainLayout.addComponent(lblLoanNo, "top:45.0px;left:15.0px;");

		cmbLoanNo = new ComboBox();
		cmbLoanNo.setImmediate(true);
		cmbLoanNo.setWidth("100px");
		cmbLoanNo.setHeight("-1px");
		cmbLoanNo.setImmediate(true);
		cmbLoanNo.setNullSelectionAllowed(false);
		mainLayout.addComponent(cmbLoanNo, "top:43.0px;left:120.0px;");

		mainLayout.addComponent(table, "top:75.0px;left:10.0px;");
		return mainLayout;
	}
}
