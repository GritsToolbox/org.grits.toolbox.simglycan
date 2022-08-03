package org.grits.toolbox.simglycan.handler;

import java.io.File;
import java.util.Random;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.core.datamodel.Entry;
import org.grits.toolbox.core.datamodel.property.ProjectProperty;
import org.grits.toolbox.core.datamodel.util.DataModelSearch;
import org.grits.toolbox.core.utilShare.processDialog.ProgressDialog;
import org.grits.toolbox.simglycan.dialog.ImportSimGlycanDialog;

import edu.uga.ccrc.simiantools.entry.ms.annotation.property.MSAnnotationProperty;
import edu.uga.ccrc.simiantools.entry.ms.property.MassSpecProperty;

public class SimGlycanImportHandler implements IHandler
{
    //log4J Logger
    private static final Logger logger = Logger.getLogger(SimGlycanImportHandler.class);

    @Override
    public void addHandlerListener(IHandlerListener handlerListener) 
    {}

    @Override
    public void dispose() 
    {}

    @Override
    public boolean isEnabled() 
    {
        return true;
    }

    @Override
    public boolean isHandled() 
    {
        return true;
    }

    @Override
    public void removeHandlerListener(IHandlerListener handlerListener)
    {}

    @Override
    public Object execute(ExecutionEvent a_event) throws ExecutionException 
    {
        Shell t_shell = HandlerUtil.getActiveShell(a_event);
        //convert it to TreeSelection object type
        TreeSelection t_selection = (TreeSelection)HandlerUtil.getActiveMenuSelection(a_event);
        if(t_selection == null)
        {
            this.createSimGlycanImportDialog(t_shell, null);
        }
        else
        {
            if (t_selection.size() == 1)
            {
                //get the selected node..
                Entry t_entry = (Entry)t_selection.getFirstElement();

                //find out which property
                if (t_entry.getProperty().getType().equals(MassSpecProperty.TYPE))
                {
                    //create a new dialog to create a new sample
                    this.createSimGlycanImportDialog(t_shell, t_entry);
                }
                else
                {
                    this.createSimGlycanImportDialog(t_shell, null);
                }
            }
            else
            {
                //create a new dialog 
                this.createSimGlycanImportDialog(t_shell, null);
            }
        }
        return null;
    }

    private void createSimGlycanImportDialog(Shell activeShell, Entry a_entry) 
    {
        ImportSimGlycanDialog t_dialog = new ImportSimGlycanDialog(PropertyHandler.getModalDialog(activeShell));
        t_dialog.setMsEntry(a_entry);
        if(t_dialog.open() == 0)
        {
            //activeShell is closed already. Thus create a new shell for errors
            Shell t_modalDialog = PropertyHandler.getModalDialog(new Shell());
            // TODO create the entry and add it to the parent to prevent dublicated names => needs to be cleaned up in case all fails
            Entry t_entry = new Entry();
            t_entry.setDisplayName(t_dialog.getName());
            MSAnnotationProperty t_property = new MSAnnotationProperty();
            t_property.setDescription(t_dialog.getDescription());
            t_entry.setProperty(t_property);
            PropertyHandler.getDataModel().addEntry(a_entry, t_entry, true);
            String t_projectFolder = this.getProjectFolder(a_entry);
            // need an ID for the Property
            Integer t_fileID = this.createUniqueAnnotationID(t_projectFolder,t_property);
            t_property.setAnnotationId(t_fileID);
            // start processing the File
            ProgressDialog t_dialogProgress = new ProgressDialog(t_modalDialog);
            SimGlycanProcess t_simglycan = new SimGlycanProcess();
            t_dialogProgress.setWorker(t_simglycan);
            if(t_dialogProgress.open() != SWT.OK)
            {
                this.cleanup(t_entry);
            }
        }
    }

    private String getProjectFolder(Entry a_entry)
    {
        //get the workspace location 
        String t_workspaceLocation = PropertyHandler.getVariable("workspace_location");
        //get the project name//at least one entry has to be there
        Entry t_projectEntry = DataModelSearch.findParentByType(a_entry, ProjectProperty.TYPE);
        return t_workspaceLocation + t_projectEntry.getDisplayName() + File.separator;
    }

    private void cleanup(Entry a_entry)
    {
        try
        {
            PropertyHandler.getDataModel().deleteEntry(a_entry, true);
        }
        catch(Exception t_excpetion)
        {
            SimGlycanImportHandler.logger.debug("Unable to clean up after failed SimGlycan import", t_excpetion);
        }
    }

    private Integer createUniqueAnnotationID(String a_projectFolder, MSAnnotationProperty t_propertyAnnotation)
    {
        // make sure the annotation folder exists
        File t_folder = new File(a_projectFolder + t_propertyAnnotation.getArchiveFolder());
        if ( !t_folder.exists() )
        {
            t_folder.mkdirs();
        }
        Integer t_proptertyID = SimGlycanImportHandler.getRandomId();
        File t_file = new File(a_projectFolder + t_propertyAnnotation.getArchiveFolder() + File.separator + t_proptertyID.toString() + t_propertyAnnotation.getArchiveExtension());
        while(t_file.exists())
        {
            t_proptertyID = SimGlycanImportHandler.getRandomId();
            t_file = new File(a_projectFolder + t_propertyAnnotation.getArchiveFolder() + File.separator + t_proptertyID.toString() + t_propertyAnnotation.getArchiveExtension());
        }
        return t_proptertyID;
    }
    
    public static Integer getRandomId() 
    {
        Random random = new Random();
        return random.nextInt(10000);
    }

}
