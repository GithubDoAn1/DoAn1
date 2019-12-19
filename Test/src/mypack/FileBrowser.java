package mypack;
import Connection.Connect;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.table.*;
import javax.swing.filechooser.FileSystemView;
import java.util.Date;
import java.util.List;
import java.io.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Permission;
//import static mypack.FileBrowser.per;
class FileBrowser extends javax.swing.JFrame 
{
    public static final String APP_TITLE = "FileBro";
    private Desktop desktop;
    private FileSystemView fileSystemView;
    private File currentFile;
    private JPanel gui;
    private JTree tree;
    private DefaultTreeModel treeModel;
    private JTable table;
    private JProgressBar progressBar;
    private FileTableModel fileTableModel;
    private ListSelectionListener listSelectionListener;
    private boolean cellSizesSet = false;
    private int rowIconPadding = 6;
    private JButton openFile;
    private JButton reload;
    private JLabel fileName;
    private JTextField path;
    private JLabel date;
    private JLabel size;
    private JCheckBox readable;
    private JCheckBox writable;
    private JCheckBox executable;
    private JRadioButton isOwn;
    JPanel flags;
    private File path1 = new File("D:/");
    public static Account Authorize = new Account();
    public static Account Authorized = new Account();
    public static int change = 0;
    public static void listf() throws SQLException 
    {
        File directory = new File("D:/");
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) 
            {      
                if (file.isDirectory()) Connect.WriteFolder(file.getPath(), file.getName());
                else if (file.isFile()) Connect.WriteFolder(file.getPath(), file.getName());
                checkNewDirectory(file);
            }
    }
    public static void checkNewDirectory(File file) 
    {
        boolean f = false;
        for (Permission p : Authorized.per) 
            if (p.path.equals(file.getPath())) f = true;
        if (!f)
        {
        	change ++;
            Permission p;
            p = new Permission();
            p.path = file.getPath();
            if (Authorize.userid != Authorized.userid && !Authorize.role.equals("root")) 
            {
                p.per = "1111";
                p.userid = Authorize.userid;
                Authorize.per.add(p);
                p.per = "0000";
                p.userid = Authorized.userid;
                Authorized.per.add(p);
            }
            else if (Authorize.userid == Authorized.userid && !Authorize.role.equals("root")) 
            {
                p.per = "1111";
                p.userid = Authorized.userid;
                Authorized.per.add(p);
            }
        }     
    }
    public void getACL () 
    {
        String pm = "";
        if (isOwn.isSelected()) pm += "1";
        else pm += "0";
        if (readable.isSelected()) pm += "1";
        else pm += "0";
        if (writable.isSelected()) pm += "1";
        else pm += "0";
        if (executable.isSelected()) pm += "1";
        else pm += "0";
        for (Permission p : Authorized.per)
            if (p.path.contains(currentFile.getPath())) p.per = pm;
    }
    public boolean checkRead()
    {
        String pm = "";
        for (Permission p : Authorize.per)
            if (p.path.equals(currentFile.getPath())) pm = p.per;
        return (Authorize.userid == 1) || !pm.equals("") && pm.split("")[1].equals("1");
    }
    public boolean canTick()
    {
        String pm = "";
        for (Permission p : Authorize.per)
            if (p.path.equals(currentFile.getPath())) pm = p.per;
        return (Authorize.userid == 1) || (!pm.equals("") && pm.split("")[0].equals("1"));
    }
    public boolean canOpen()
    {
        String pm = "";
        for (Permission p : Authorize.per)
            if (p.path.equals(currentFile.getPath())) pm = p.per;
        return (Authorize.userid == 1) || (!pm.equals("") && pm.split("")[2].equals("1"));
    }
    public Container getGui() throws SQLException 
    {
        if (gui == null) 
        {
            gui = new JPanel(new BorderLayout(3,3));
            gui.setBorder(new EmptyBorder(5,5,5,5));
            fileSystemView = FileSystemView.getFileSystemView();
            desktop = Desktop.getDesktop();
            JPanel detailView = new JPanel(new BorderLayout(3,3));
            table = new JTable();
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setAutoCreateRowSorter(true);
            table.setShowVerticalLines(false);
            listSelectionListener = (ListSelectionEvent lse) -> 
            {
                int row = table.getSelectionModel().getLeadSelectionIndex();
                setFileDetails( ((FileTableModel)table.getModel()).getFile(row));
            };
            table.getSelectionModel().addListSelectionListener(listSelectionListener);
            JScrollPane tableScroll = new JScrollPane(table);
            Dimension d = tableScroll.getPreferredSize();
            tableScroll.setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight()/2));
            detailView.add(tableScroll, BorderLayout.CENTER);
            DefaultMutableTreeNode root = new DefaultMutableTreeNode();
            treeModel = new DefaultTreeModel(root);
            TreeSelectionListener treeSelectionListener = (TreeSelectionEvent tse) -> 
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)tse.getPath().getLastPathComponent();
                setFileDetails((File)node.getUserObject());
                if (!checkRead()) 
                {
                    JOptionPane.showMessageDialog(null, "Cannot access!", "Unreadble Directory", 2);
                    openFile.setEnabled(false);
                }
                else {
                    showChildren(node);
                    openFile.setEnabled(true);
                }
            };
            File[] roots = fileSystemView.getFiles(path1, false);
            for (File fileSystemRoot : roots) 
            {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
                root.add(node);
                File[] files = fileSystemView.getFiles(fileSystemRoot, true);
                for (File file : files) 
                {
                    if (file.isDirectory()) 
                        node.add(new DefaultMutableTreeNode(file));
                }
            }
            tree = new JTree(treeModel);
            tree.setRootVisible(false);
            tree.addTreeSelectionListener(treeSelectionListener);
            tree.setCellRenderer(new FileTreeCellRenderer());
            tree.expandRow(0);
            JScrollPane treeScroll = new JScrollPane(tree);
            // as per trashgod tip
            tree.setVisibleRowCount(15);
            Dimension preferredSize = treeScroll.getPreferredSize();
            Dimension widePreferred = new Dimension(200,(int)preferredSize.getHeight());
            treeScroll.setPreferredSize( widePreferred );
            // details for a File
            JPanel fileMainDetails = new JPanel(new BorderLayout(4,2));
            fileMainDetails.setBorder(new EmptyBorder(0,6,0,6));
            JPanel fileDetailsLabels = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsLabels, BorderLayout.WEST);
            JPanel fileDetailsValues = new JPanel(new GridLayout(0,1,2,2));
            fileMainDetails.add(fileDetailsValues, BorderLayout.CENTER);
            fileDetailsLabels.add(new JLabel("File", JLabel.TRAILING));
            fileName = new JLabel();
            fileDetailsValues.add(fileName);
            fileDetailsLabels.add(new JLabel("Path/name", JLabel.TRAILING));
            path = new JTextField(5);
            path.setEditable(false);
            fileDetailsValues.add(path);
            fileDetailsLabels.add(new JLabel("Last Modified", JLabel.TRAILING));
            date = new JLabel();
            fileDetailsValues.add(date);
            fileDetailsLabels.add(new JLabel("File size", JLabel.TRAILING));
            size = new JLabel();
            fileDetailsValues.add(size);
            fileDetailsLabels.add(new JLabel("Type", JLabel.TRAILING));
            flags = new JPanel(new FlowLayout(FlowLayout.LEADING,4,0));
            isOwn = new JRadioButton("Owned:");
            flags.add(isOwn);
            isOwn.setEnabled(false);
            JToolBar toolBar = new JToolBar();
            toolBar.setFloatable(false);
            openFile = new JButton("Open");
            openFile.setMnemonic('o');
            openFile.addActionListener((ActionEvent ae) -> 
            {
                try 
                {
                    System.out.println("Open: " + currentFile);
                    desktop.open(currentFile);
                } catch(IOException t) {showThrowable(t);}
                gui.repaint();
            });
            reload = new JButton("Reload");
            reload.addActionListener((ActionEvent ae) -> 
            {
                try 
                {
                    getGui();
                } catch (SQLException ex) {Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);}
                gui.repaint();
            });
            toolBar.add(openFile);
            toolBar.add(reload);
            // Check the actions are supported on this platform!
            openFile.setEnabled(desktop.isSupported(Desktop.Action.OPEN));
            flags.add(new JLabel("::  Flags"));
            readable = new JCheckBox("Read  ");
            readable.setMnemonic('a');
            readable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) { 
                    getACL(); 
                    change ++;
                }
            });
            flags.add(readable);
            writable = new JCheckBox("Write  ");
            writable.setMnemonic('w');
            writable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) 
                { 
                    if(writable.isSelected() && !readable.isSelected()) readable.setSelected(writable.isSelected());
                    getACL();
                    change ++;
                }
            });
            flags.add(writable);
            executable = new JCheckBox("Execute");
            executable.setMnemonic('x');
            executable.addMouseListener(new MouseAdapter() 
            {
                @Override
                public void mouseClicked(MouseEvent e) { 
                    if(!writable.isSelected() && !readable.isSelected() && executable.isSelected()) 
                    {
                        readable.setSelected(executable.isSelected());
                        writable.setSelected(executable.isSelected());
                    }
                    getACL(); 
                    change ++;
                }
            });
            flags.add(executable);
            int count = fileDetailsLabels.getComponentCount();
            for (int ii=0; ii<count; ii++) 
                 fileDetailsLabels.getComponent(ii).setEnabled(false);
            count = flags.getComponentCount();
//            for (int ii=1; ii<count; ii++) 
//            {
//                flags.getComponent(ii).setEnabled(canTick());
//            }
            fileDetailsValues.add(flags);
            JPanel fileView = new JPanel(new BorderLayout(3,3));
            fileView.add(toolBar,BorderLayout.NORTH);
            fileView.add(fileMainDetails,BorderLayout.CENTER);
            detailView.add(fileView, BorderLayout.SOUTH);
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScroll, detailView);
            gui.add(splitPane, BorderLayout.CENTER);
            JPanel simpleOutput = new JPanel(new BorderLayout(3,3));
            progressBar = new JProgressBar();
            simpleOutput.add(progressBar, BorderLayout.EAST);
            progressBar.setVisible(false);
            gui.add(simpleOutput, BorderLayout.SOUTH);
        }
        return gui;
    }

    public void showRootFile() 
    {
        tree.setSelectionInterval(0,0);
    }
    private void showThrowable(Throwable t) 
    {
        JOptionPane.showMessageDialog
        (
            gui,
            t.toString(),
            t.getMessage(),
            JOptionPane.ERROR_MESSAGE
        );
        gui.repaint();
    }
    private void setTableData(final File[] files) 
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() 
            {
                if (fileTableModel == null)
                {
                    fileTableModel = new FileTableModel();
                    table.setModel(fileTableModel);
                    fileTableModel.write = writable.isSelected();
                    fileTableModel.read = readable.isSelected();
                    fileTableModel.execute = executable.isSelected();
                    fileTableModel.userid = Authorized.userid;
                }
                table.getSelectionModel().removeListSelectionListener(listSelectionListener);
                fileTableModel.setFiles(files);
                for (File f : files) try
                {
                    if (f.isDirectory()) Connect.WriteFolder(f.getPath(), f.getName());
                    else if (f.isFile()) Connect.WriteFolder(f.getPath(), f.getName());
                    checkNewDirectory(f);
                } catch (SQLException ex) {Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);}
                table.getSelectionModel().addListSelectionListener(listSelectionListener);
                if (!cellSizesSet)
                {
                    Icon icon = fileSystemView.getSystemIcon(files[0]);
                    table.setRowHeight( icon.getIconHeight()+rowIconPadding );
                    setColumnWidth(0,-1);
                    setColumnWidth(3,60);
                    table.getColumnModel().getColumn(3).setMaxWidth(120);
                    setColumnWidth(4,-1);
                    setColumnWidth(5,-1);
                    setColumnWidth(6,-1);
                    setColumnWidth(7,-1);
                    setColumnWidth(8,-1);
                    setColumnWidth(9,-1);
                    cellSizesSet = true;
                }
            }
        });
    }
    private void setColumnWidth(int column, int width) {
        TableColumn tableColumn = table.getColumnModel().getColumn(column);
        if (width<0) 
        {
            JLabel label = new JLabel( (String)tableColumn.getHeaderValue() );
            Dimension preferred = label.getPreferredSize();
            width = (int)preferred.getWidth()+14;
        }
        tableColumn.setPreferredWidth(width);
        tableColumn.setMaxWidth(width);
        tableColumn.setMinWidth(width);
    }

    /** Add the files that are contained within the directory of this node.*/
    private void showChildren(final DefaultMutableTreeNode node) 
    {
        tree.setEnabled(false);
        progressBar.setVisible(true);
        progressBar.setIndeterminate(true);
        SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {
            @Override
            public Void doInBackground() throws SQLException {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) 
                {
                    File[] files = fileSystemView.getFiles(file, true); //!!
                        if (node.isLeaf()) 
                            for (File child : files) 
                                if (child.isDirectory()) publish(child);
                    setTableData(files);           
                }
                return null;
            }
            @Override
            protected void process(List<File> chunks) 
            {
                chunks.forEach((child) -> 
                {
                    node.add(new DefaultMutableTreeNode(child));
                });
            }
            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                tree.setEnabled(true);
            }
        };
        worker.execute();
    }

    /** Update the File details view with the details of this File. */
    private void setFileDetails(File file) 
    {
        String[] q = null;
        if (Authorized.userid == 1)
        {
            readable.setSelected(file.canRead());
            writable.setSelected(file.canWrite());
            executable.setSelected(file.canExecute());
            isOwn.setSelected(true);
        }
        else if (Authorized.userid != 1)
        {
            String pm = "";
            for (Permission p : Authorized.per)
                if (p.path.equals(file.getPath())) pm = p.per;
            if(!pm.equals("")) q = pm.split("");
            if (q != null && q[0].equals("1")) isOwn.setSelected(true);
            else isOwn.setSelected(false);
            if (q != null && q[1].equals("1")) readable.setSelected(file.canRead());
            else readable.setSelected(false);
            if (q != null && q[2].equals("1")) writable.setSelected(file.canWrite());
            else writable.setSelected(false);
            if (q != null && q[3].equals("1")) executable.setSelected(file.canExecute());
            else executable.setSelected(false);           
        }
        currentFile = file;
        for (int ii=1; ii < flags.getComponentCount(); ii++) 
            flags.getComponent(ii).setEnabled(canTick());
        openFile.setEnabled(canOpen());
        Icon icon = fileSystemView.getSystemIcon(file);
        fileName.setIcon(icon);
        fileName.setText(fileSystemView.getSystemDisplayName(file));
        path.setText(file.getPath());
        date.setText(new Date(file.lastModified()).toString());
        size.setText(file.length() + " bytes");
        JFrame f = (JFrame)gui.getTopLevelAncestor();
        if (f != null) f.setTitle(APP_TITLE + " :: " + fileSystemView.getSystemDisplayName(file) );
        gui.repaint();
    }
    
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> 
        {
            try
            {            
                listf();  
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException weTried) {} catch (SQLException ex) {Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);}
            JFrame f = new JFrame(APP_TITLE);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() 
            {
                @Override
                public void windowClosed(WindowEvent arg0)
                {
                    fMenu ac;
                    ac = new fMenu();
                    ac.a = Authorize;
                    ac.txtLoginUser.setText(Authorize.username);
                    ac.setVisible(true);
                    ac.setLocationRelativeTo(null);
                }      
                @Override
                public void windowClosing(WindowEvent arg0)
                {
                    if (change != 0)
                    {
                        int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Save your changes?","Warning",JOptionPane.YES_NO_OPTION);
                        if(dialogResult == JOptionPane.YES_OPTION)
                        {
                            Authorized.per.forEach((p) -> 
                            {
                                try
                                {
                                    Connect.InsertPermission(p.path, p.userid, p.per);
                                } catch (SQLException ex) {Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);}
                            });
                            Authorize.per.forEach((p) -> 
                            {
                                try
                                {
                                    Connect.InsertPermission(p.path, p.userid, p.per);
                                } catch (SQLException ex) {Logger.getLogger(FileBrowser.class.getName()).log(Level.SEVERE, null, ex);}
                            });
                        }
                    }     
                }         
            });
            f.setLocationRelativeTo(null);
            FileBrowser FileBrowser = new FileBrowser();
            try
            {
                f.setContentPane(FileBrowser.getGui());
            } catch (SQLException ex) {}
//                try 
//                {
//                    URL urlBig = FileBrowser.getClass().getResource("fb-icon-32x32.png");
//                    URL urlSmall = FileBrowser.getClass().getResource("fb-icon-16x16.png");
//                    ArrayList<Image> images = new ArrayList<>();
//                    images.add( ImageIO.read(urlBig) );
//                    images.add( ImageIO.read(urlSmall) );
//                    f.setIconImages(images);
//                } catch(IOException weTried) {}
            f.pack();
            f.setLocationByPlatform(true);
            f.setMinimumSize(f.getSize());
            f.setVisible(true);
            FileBrowser.showRootFile();
        });
    }
}

class FileTableModel extends AbstractTableModel 
{
    private static final long serialVersionUID = 1L;
    private File[] files;
    public int userid;
    public boolean read, write, execute;
    private FileSystemView fileSystemView = FileSystemView.getFileSystemView();
    private String[] columns = 
    {
        "Icon",
        "File",
        "Path/name",
        "Size",
        "Last Modified",
        "R",
        "W",
        "E",
        "D",
        "F",
    };
    FileTableModel() 
    {
        this(new File[0]);
    }
    FileTableModel(File[] files) 
    {
        this.files = files;
    }

    @Override
    public Object getValueAt(int row, int column) 
    {
        File file = files[row];
        switch (column) 
        {
            case 0:
                return fileSystemView.getSystemIcon(file);
            case 1:
                return fileSystemView.getSystemDisplayName(file);
            case 2:
                return file.getPath();
            case 3:
                return file.length();
            case 4:
                return file.lastModified();
            case 5:
                return read;
            case 6:
                return write;
            case 7:
                return execute;
            case 8:
                return file.isDirectory();
            case 9:
                return file.isFile();
            default:
                System.err.println("Logic Error");
        }
        return "";
    }

    @Override
    public int getColumnCount() 
    {
        return columns.length;
    }

    @Override
    public Class<?> getColumnClass(int column) 
    {
        switch (column) 
        {
            case 0:
                return ImageIcon.class;
            case 3:
                return Long.class;
            case 4:
                return Date.class;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                return Boolean.class;
        }
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return files.length;
    }

    public File getFile(int row) {
        return files[row];
    }

    public void setFiles(File[] files) {
        this.files = files;
        fireTableDataChanged();
    }
}

/** A TreeCellRenderer for a File. */
class FileTreeCellRenderer extends DefaultTreeCellRenderer {
    private static final long serialVersionUID = 4038801826310612582L;
    private final FileSystemView fileSystemView;
    private final JLabel label;
    FileTreeCellRenderer() 
    {
        label = new JLabel();
        label.setOpaque(true);
        fileSystemView = FileSystemView.getFileSystemView();
    }
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
        boolean hasFocus) 
    {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        File file = (File)node.getUserObject();
        label.setIcon(fileSystemView.getSystemIcon(file));
        label.setText(fileSystemView.getSystemDisplayName(file));
        label.setToolTipText(file.getPath());
        if (selected) 
        {
            label.setBackground(backgroundSelectionColor);
            label.setForeground(textSelectionColor);
        } else 
        {
            label.setBackground(backgroundNonSelectionColor);
            label.setForeground(textNonSelectionColor);
        }
        return label;
    }
}