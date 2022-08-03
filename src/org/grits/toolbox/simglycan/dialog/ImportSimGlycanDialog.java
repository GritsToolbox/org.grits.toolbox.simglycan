package org.grits.toolbox.simglycan.dialog;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.core.datamodel.Entry;
import org.grits.toolbox.core.datamodel.dialog.ModalDialog;
import org.grits.toolbox.core.datamodel.dialog.ProjectExplorerDialog;
import org.grits.toolbox.core.utilShare.FileSelectionAdapter;
import org.grits.toolbox.core.utilShare.ListenerFactory;

import edu.uga.ccrc.simiantools.entry.ms.property.MassSpecProperty;

public class ImportSimGlycanDialog extends ModalDialog
{
    private Text m_nameText;
    private String m_name;
    private Label m_nameLabel;

    private Text m_msNameText;
    private Label m_msNameLabel;

    private Text m_fileNameText;
    private Label m_fileNameLabel;
    private String m_fileName;

    private Text m_descriptionText;
    private String m_description;
    private Label m_descriptionLabel;

    private Entry m_msEntry = null;

    protected Composite m_parent = null;

    public ImportSimGlycanDialog(Shell parentShell) 
    {
        super(parentShell);
    }

    @Override
    public void create()
    {
        super.create();
        this.setTitle("Create Glycan Annotation");
        this.setMessage("Import Glycan Annotation from SimGlycan");
    }

    @Override
    protected Control createDialogArea(final Composite a_parent) 
    {
        this.m_parent = a_parent;
        GridLayout t_gridLayout = new GridLayout();
        t_gridLayout.numColumns = 4;
        t_gridLayout.verticalSpacing = 10;
        a_parent.setLayout(t_gridLayout);

        /*
         * First row starts
         */
        // Label
        GridData t_gridData = new GridData();
        this.m_msNameLabel = new Label(a_parent, SWT.NONE);
        this.m_msNameLabel.setText("Mass spec");
        this.m_msNameLabel = this.setMandatoryLabel(this.m_msNameLabel);
        this.m_msNameLabel.setLayoutData(t_gridData);
        // text field
        t_gridData = new GridData();
        t_gridData.grabExcessHorizontalSpace = true;
        t_gridData.horizontalAlignment = GridData.FILL;
        t_gridData.horizontalSpan = 2;
        this.m_msNameText = new Text(a_parent, SWT.BORDER);
        this.m_msNameText.setLayoutData(t_gridData);
        //for the first time if an entry was chosen by a user
        if(this.m_msEntry != null)
        {
            this.m_msNameText.setText(this.m_msEntry.getDisplayName());
        }
        this.m_msNameText.setEditable(false);
        //browse button
        t_gridData = new GridData();
        Button t_button = new Button(a_parent, SWT.PUSH);
        t_button.setText("Browse");
        t_button.setLayoutData(t_gridData);
        t_button.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event) 
            {
                Shell newShell = new Shell(a_parent.getShell(),SWT.PRIMARY_MODAL | SWT.SHEET);
                ProjectExplorerDialog dlg = new ProjectExplorerDialog(newShell);
                // Set the parent as a filter
                dlg.addFilter(MassSpecProperty.TYPE);
                // Change the title bar text
                dlg.setTitle("Mass Spec Selection");
                // Customizable message displayed in the dialog
                dlg.setMessage("Choose a ms entry");
                // Calling open() will open and run the dialog.
                Entry selected = dlg.open();
                if (selected != null) {
                    m_msEntry = selected;
                    // Set the text box as the project text
                    m_msNameText.setText(m_msEntry.getDisplayName());
                }
            }
        });
        //then add separator
        this.createSeparator(4);

        /*
         * Second row
         */
        // Name field
        t_gridData = new GridData();
        this.m_nameLabel = new Label(a_parent, SWT.LEFT);
        this.m_nameLabel.setText("Name");
        this.m_nameLabel = this.setMandatoryLabel(this.m_nameLabel);
        this.m_nameLabel.setLayoutData(t_gridData);

        t_gridData = new GridData();
        t_gridData.grabExcessHorizontalSpace = true;
        t_gridData.horizontalAlignment = GridData.FILL;
        t_gridData.horizontalSpan = 3;
        this.m_nameText = new Text(a_parent, SWT.BORDER);
        this.m_nameText.setLayoutData(t_gridData);

        /*
         * Third row
         */
        // description
        t_gridData = new GridData();
        this.m_descriptionLabel = new Label(a_parent, SWT.LEFT);
        this.m_descriptionLabel.setText("Description");
        this.m_descriptionLabel.setLayoutData(t_gridData);

        t_gridData = new GridData();
        t_gridData.minimumHeight = 80;
        t_gridData.grabExcessHorizontalSpace = true;
        t_gridData.grabExcessVerticalSpace = true;
        t_gridData.horizontalAlignment = GridData.FILL;
        t_gridData.horizontalSpan = 3;
        this.m_descriptionText = new Text(a_parent, SWT.MULTI | SWT.V_SCROLL | SWT.BORDER);
        this.m_descriptionText.setLayoutData(t_gridData);
        this.m_descriptionText.addTraverseListener(ListenerFactory.getTabTraverseListener());
        this.m_descriptionText.addKeyListener(ListenerFactory.getCTRLAListener());
        
        /*
         * Forth row starts
         */
        // Label
        t_gridData = new GridData();
        this.m_fileNameLabel = new Label(a_parent, SWT.NONE);
        this.m_fileNameLabel.setText("CSV file");
        this.m_fileNameLabel = this.setMandatoryLabel(this.m_fileNameLabel);
        this.m_fileNameLabel.setLayoutData(t_gridData);
        // text field
        t_gridData = new GridData();
        t_gridData.grabExcessHorizontalSpace = true;
        t_gridData.horizontalAlignment = GridData.FILL;
        t_gridData.horizontalSpan = 2;
        this.m_fileNameText = new Text(a_parent, SWT.BORDER);
        this.m_fileNameText.setLayoutData(t_gridData);
        this.m_fileNameText.setEditable(false);
        //browse button
        t_gridData = new GridData();
        t_button = new Button(a_parent, SWT.PUSH);
        t_button.setText("Browse");
        t_button.setLayoutData(t_gridData);
        FileSelectionAdapter t_fileSelectAdapter = new FileSelectionAdapter();
        t_fileSelectAdapter.setShell(a_parent.getShell());
        t_fileSelectAdapter.setText(this.m_fileNameText);
        t_button.addSelectionListener(t_fileSelectAdapter);
        /*
         * Buttons area
         */
        this.createButtonCustomeOK(a_parent);
        this.createButtonCustomeCancel(a_parent);

        return a_parent;
    }

    protected Button createButtonCustomeCancel(final Composite a_parent) 
    {
        //create a grdiData for CANCEL button
        GridData t_gridData = new GridData();
        t_gridData.horizontalAlignment = GridData.BEGINNING;
        Button t_buttonCancel = new Button(a_parent, SWT.PUSH);
        t_buttonCancel.setText("Cancel");
        t_buttonCancel.addSelectionListener(new SelectionAdapter() 
        {
            public void widgetSelected(SelectionEvent event) 
            {
                setReturnCode(CANCEL);
                close();
            }
        });
        t_buttonCancel.setLayoutData(t_gridData);
        return t_buttonCancel;
    }

    protected Button createButtonCustomeOK(final Composite a_parent) 
    {
        //create a grdiData for OKButton
        GridData t_gridData = new GridData();
        t_gridData.grabExcessHorizontalSpace = true;
        t_gridData.horizontalAlignment = GridData.END;
        t_gridData.horizontalSpan = 3;
        Button t_buttonOK = new Button(a_parent, SWT.PUSH);
        t_buttonOK.setText("   OK   ");
        t_buttonOK.addSelectionListener(new SelectionAdapter()
        {
            public void widgetSelected(SelectionEvent event)
            {
                if (isValidInput()) 
                {
                    okPressed();
                    close();
                }
            }
        });
        t_buttonOK.setLayoutData(t_gridData);
        return t_buttonOK;
    }

    @Override
    public Entry createEntry() 
    {
        return null;
    }

    @Override
    protected boolean isValidInput() 
    {
        // check if the parent entry is there
        if(!this.checkBasicLengthCheck(this.m_msNameLabel, this.m_msNameText, 0, 32))
        {
            return false;
        }

        // check the name
        if(!this.checkBasicLengthCheck(this.m_nameLabel, this.m_nameText, 0, 32))
        {
            return false;
        }

        // need to check if as sibling has the same name 
        if(this.findSameNameEntry(this.m_nameText.getText(),this.m_msEntry,MassSpecProperty.TYPE))
        {
            this.setError(m_nameLabel, "Same name exists in other sample. Please use a different name.");
            return false;
        }
        else
        {
            // if OK then remove error
            this.removeError(m_nameLabel);
        }

        if( !this.checkBasicLengthCheck(this.m_descriptionLabel, 
                this.m_descriptionText, 0, 
                Integer.parseInt(PropertyHandler.getVariable("descriptionLength"))))
        {
            this.setError(this.m_descriptionLabel, "Description text is too long.");
            return false;
        }
        else
        {
            this.removeError(m_nameLabel);
        }
        if ( !this.m_fileNameText.getText().isEmpty() )
        {
            if ( ! fileExists(this.m_fileNameText.getText()) )
            {
                this.setError(this.m_fileNameLabel,"Specified file does not exist.");
                return false;
            }
            else
            {
                this.removeError(m_nameLabel);
            }
        }
        else
        {
            this.setError(this.m_fileNameLabel,"Provide a SimGlycan CSV file.");
            return false;
        }
        return true;
    }

    private boolean fileExists(String _sFileName) 
    {
        File file = new File(_sFileName);
        return file.exists();
    }

    @Override
    protected void okPressed() {
        this.setName(this.m_nameText.getText());
        this.setDescription(this.m_descriptionText.getText());
        this.setFileName(this.m_fileNameText.getText());
    }

    public Entry getMsEntry()
    {
        return m_msEntry;
    }

    public void setMsEntry(Entry a_msEntry)
    {
        m_msEntry = a_msEntry;
    }

    public String getName()
    {
        return m_name;
    }

    public void setName(String a_name)
    {
        m_name = a_name;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String a_description)
    {
        m_description = a_description;
    }

    public String getFileName()
    {
        return m_fileName;
    }

    public void setFileName(String a_fileName)
    {
        m_fileName = a_fileName;
    }
} 