package sales.repricing;

import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.GroupLayout;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.JTextComponent;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;
import org.jdesktop.application.Action;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import sales.auxiliarly.MyCellEditor;
import sales.SalesApp;
import sales.SalesView;
import sales.catalogs.NomenclatureView;
import sales.entity.Nomenclature;
import sales.entity.Repricing;
import sales.entity.Repricingtable;
import sales.interfaces.IClose;
import sales.interfaces.ICommit;
import sales.interfaces.IContractor;
import sales.interfaces.IFocus;
import sales.interfaces.IHashAndSave;
import sales.interfaces.IProductItem;
import sales.util.HUtil;
import sales.util.Util;
import sales.util.fwMoney;

public class RepricingDocView extends JInternalFrame implements IProductItem, IContractor, IHashAndSave, IFocus, IClose, ICommit {

    static Logger logger = Logger.getLogger(SalesApp.class.getName());

    public RepricingDocView(SalesView salesView, RepricingView repricingView, Integer documentCode, Integer productCode) {
        initComponents();

        Util.initJIF(this, "Акт переоценки", repricingView, salesView);
        Util.initJTable(jtRepricingDoc);

        this.salesView = salesView;
        this.repricingView = repricingView;
        this.documentCode = documentCode;

        jsTime.setModel(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(jsTime, "HH:mm");
        jsTime.setEditor(timeEditor);

        Session session = HUtil.getSession();
        if (documentCode != null) {
            Repricing r = (Repricing) HUtil.getSession().load(Repricing.class, documentCode);
            jtfNumber.setText(r.getNumber());
            date = new JDateChooser(r.getDatetime());
            jsTime.setValue(r.getDatetime());
            active = r.getActive();
            if (active == 1) {
                jcbActive.setSelected(true);
            } else if (active == 2) {
                jcbActive.setEnabled(false);
            }
            lock = r.getLocked();
            if (lock == 1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Элемент редактируется другим пользователем и будет открыт только для просмотра!",
                        "Блокировка", JOptionPane.PLAIN_MESSAGE);
                jtfNumber.setEditable(false);
                date.setEnabled(false);
                jtfCoefficient.setEditable(false);
                jcbActive.setEnabled(false);
                jbSave.setEnabled(false);
            } else {
                session.beginTransaction();
                r.setLocked(1);
                session.merge(r);
                session.getTransaction().commit();
            }
            table = HUtil.executeHql("from Repricingtable rt where rt.documentCode = " + documentCode + " order by rt.line");

        } else {
            jtfNumber.setText(HUtil.getNextDocNumber("Repricing", "", session));
            Calendar cl = Calendar.getInstance();
            date = new JDateChooser(cl.getTime());
            active = 0;
            table = new ArrayList();
            lock = 0;
        }
        session.close();

        GroupLayout gl = (GroupLayout) jpDate.getLayout();
        gl.setHorizontalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));
        gl.setVerticalGroup(gl.createParallelGroup().addGroup(gl.createSequentialGroup().addComponent(date)));

        jTextBox = new JTextField();
        jTextBox.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jTextBox.setEditable(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });

        jtRepricingDoc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                int r = jtRepricingDoc.getSelectedRow();
                int c = jtRepricingDoc.getSelectedColumn();
                if (r != -1 && c != -1) {
                    jTextBox.setEditable(Character.isDigit(e.getKeyChar())
                            || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
                }
            }
        });

        updateTable = true;
        showTable();

        if (productCode != null && table.size() > 0) {
            for (int i = 0; i < table.size(); i++) {
                if (((Repricingtable) table.get(i)).getProductCode().equals(productCode)) {
                    Rectangle rect = jtRepricingDoc.getCellRect(i, 1, true);
                    jtRepricingDoc.scrollRectToVisible(rect);
                    jtRepricingDoc.getSelectionModel().addSelectionInterval(i, i);
                    jtRepricingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
                    break;
                }
            }
        } else {
            Util.selectFirstRow(jtRepricingDoc);
        }

        //set move to the right cell on enter
        Util.setMoveRight(jtRepricingDoc);

        jtfCoefficient.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                jtfCoefficient.setEditable(Character.isDigit(e.getKeyChar()) || e.getKeyChar() == KeyEvent.VK_BACK_SPACE);
            }
        });

        if (documentCode != null) {
            hash = Util.hash(this);
        } else {
            hash = null;
        }
    }

    private class MyTableModel extends DefaultTableModel {

        public MyTableModel(Vector table, Vector header) {
            super(table, header);
        }

        @Override
        public boolean isCellEditable(int r, int c) {
            return c == 5;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jtfNumber = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtRepricingDoc = new javax.swing.JTable() {

            public Component prepareEditor(TableCellEditor editor,int row,int col)
            {
                Component c = super.prepareEditor(editor, row, col);
                if (c instanceof JTextComponent) {
                    ((JTextComponent) c).selectAll();
                    SwingUtilities.invokeLater(new Util.SelectLater((JTextComponent) c));
                }
                return c;
            }
        };
        jbSave = new javax.swing.JButton();
        jbAddProduct = new javax.swing.JButton();
        jbDeleteItem = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();
        jcbActive = new javax.swing.JCheckBox();
        jbPrintRepricing = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jtfCoefficient = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jbPriceList = new javax.swing.JButton();
        jbFill = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jpDate = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jsTime = new javax.swing.JSpinner();
        jbUp = new javax.swing.JButton();
        jbDown = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jtfFilter = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance().getContext().getResourceMap(RepricingDocView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jtfNumber.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jtfNumber.setText(resourceMap.getString("jtfNumber.text")); // NOI18N
        jtfNumber.setName("jtfNumber"); // NOI18N

        jLabel2.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtRepricingDoc.setFont(resourceMap.getFont("jtRepricingDoc.font")); // NOI18N
        jtRepricingDoc.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jtRepricingDoc.setCellSelectionEnabled(true);
        jtRepricingDoc.setFillsViewportHeight(true);
        jtRepricingDoc.setName("jtRepricingDoc"); // NOI18N
        jtRepricingDoc.setSurrendersFocusOnKeystroke(true);
        jtRepricingDoc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtRepricingDocMouseClicked(evt);
            }
        });
        jtRepricingDoc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jtRepricingDocKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jtRepricingDoc);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance().getContext().getActionMap(RepricingDocView.class, this);
        jbSave.setAction(actionMap.get("saveDoc")); // NOI18N
        jbSave.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbSave.setText(resourceMap.getString("jbSave.text")); // NOI18N
        jbSave.setName("jbSave"); // NOI18N
        jbSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSaveActionPerformed(evt);
            }
        });

        jbAddProduct.setAction(actionMap.get("addProduct")); // NOI18N
        jbAddProduct.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbAddProduct.setText(resourceMap.getString("jbAddProduct.text")); // NOI18N
        jbAddProduct.setName("jbAddProduct"); // NOI18N

        jbDeleteItem.setAction(actionMap.get("deleteProduct")); // NOI18N
        jbDeleteItem.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbDeleteItem.setText(resourceMap.getString("jbDeleteItem.text")); // NOI18N
        jbDeleteItem.setName("jbDeleteItem"); // NOI18N
        jbDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbDeleteItemActionPerformed(evt);
            }
        });

        jbCancel.setAction(actionMap.get("closeDoc")); // NOI18N
        jbCancel.setFont(resourceMap.getFont("jbAddProduct.font")); // NOI18N
        jbCancel.setText(resourceMap.getString("jbCancel.text")); // NOI18N
        jbCancel.setName("jbCancel"); // NOI18N

        jcbActive.setAction(actionMap.get("setjcbActive")); // NOI18N
        jcbActive.setBackground(resourceMap.getColor("jcbActive.background")); // NOI18N
        jcbActive.setText(resourceMap.getString("jcbActive.text")); // NOI18N
        jcbActive.setName("jcbActive"); // NOI18N

        jbPrintRepricing.setAction(actionMap.get("showRepricing")); // NOI18N
        jbPrintRepricing.setText(resourceMap.getString("jbPrintRepricing.text")); // NOI18N
        jbPrintRepricing.setName("jbPrintRepricing"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jtfCoefficient.setText(resourceMap.getString("jtfCoefficient.text")); // NOI18N
        jtfCoefficient.setName("jtfCoefficient"); // NOI18N

        jButton1.setAction(actionMap.get("compute")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jbPriceList.setAction(actionMap.get("showPriceList")); // NOI18N
        jbPriceList.setText(resourceMap.getString("jbPriceList.text")); // NOI18N
        jbPriceList.setName("jbPriceList"); // NOI18N

        jbFill.setAction(actionMap.get("fillNomenclature")); // NOI18N
        jbFill.setText(resourceMap.getString("jbFill.text")); // NOI18N
        jbFill.setName("jbFill"); // NOI18N

        jButton2.setAction(actionMap.get("fillNonzero")); // NOI18N
        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        jpDate.setBackground(resourceMap.getColor("jpDate.background")); // NOI18N
        jpDate.setName("jpDate"); // NOI18N

        javax.swing.GroupLayout jpDateLayout = new javax.swing.GroupLayout(jpDate);
        jpDate.setLayout(jpDateLayout);
        jpDateLayout.setHorizontalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
        jpDateLayout.setVerticalGroup(
            jpDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
        );

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jsTime.setName("jsTime"); // NOI18N

        jbUp.setAction(actionMap.get("up")); // NOI18N
        jbUp.setText(resourceMap.getString("jbUp.text")); // NOI18N
        jbUp.setName("jbUp"); // NOI18N

        jbDown.setAction(actionMap.get("down")); // NOI18N
        jbDown.setText(resourceMap.getString("jbDown.text")); // NOI18N
        jbDown.setName("jbDown"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jtfFilter.setText(resourceMap.getString("jtfFilter.text")); // NOI18N
        jtfFilter.setName("jtfFilter"); // NOI18N
        jtfFilter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfFilterKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbAddProduct)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDeleteItem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbFill)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jcbActive)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)
                        .addComponent(jbSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jsTime, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPrintRepricing)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPriceList)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfCoefficient, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jbUp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbDown)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jtfFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jtfNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jbPrintRepricing, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbPriceList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addComponent(jtfCoefficient, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jsTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbUp)
                    .addComponent(jbDown)
                    .addComponent(jLabel5)
                    .addComponent(jtfFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAddProduct)
                    .addComponent(jbDeleteItem)
                    .addComponent(jbFill)
                    .addComponent(jButton2)
                    .addComponent(jcbActive)
                    .addComponent(jbCancel)
                    .addComponent(jbSave, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSaveActionPerformed
    }//GEN-LAST:event_jbSaveActionPerformed

    private void jtRepricingDocKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtRepricingDocKeyPressed

        if (evt.getKeyCode() == 155) {
            addProduct();
        }
        if (evt.getKeyCode() == 127) {
            deleteProduct();
        }
    }//GEN-LAST:event_jtRepricingDocKeyPressed

    private void jbDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbDeleteItemActionPerformed
        deleteProduct();
    }//GEN-LAST:event_jbDeleteItemActionPerformed

    private void jtRepricingDocMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtRepricingDocMouseClicked

        if (evt.getClickCount() > 1) {
            if (jtRepricingDoc.columnAtPoint(evt.getPoint()) < 2) {
                JInternalFrame nv =
                        new NomenclatureView(
                        salesView, this,
                        ((Repricingtable) table.get(jtRepricingDoc.getSelectedRow())).getProductCode(),
                        jtRepricingDoc.getSelectedRow(), "", "", null, false);
                salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
                nv.setVisible(true);
                try {
                    nv.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
            }
        }
    }//GEN-LAST:event_jtRepricingDocMouseClicked

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        jtRepricingDocKeyPressed(evt);
    }//GEN-LAST:event_formKeyPressed

private void jtfFilterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfFilterKeyReleased
    processFilter();
}//GEN-LAST:event_jtfFilterKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jbAddProduct;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbDeleteItem;
    private javax.swing.JButton jbDown;
    private javax.swing.JButton jbFill;
    private javax.swing.JButton jbPriceList;
    private javax.swing.JButton jbPrintRepricing;
    private javax.swing.JButton jbSave;
    private javax.swing.JButton jbUp;
    private javax.swing.JCheckBox jcbActive;
    private javax.swing.JPanel jpDate;
    private javax.swing.JSpinner jsTime;
    private javax.swing.JTable jtRepricingDoc;
    private javax.swing.JTextField jtfCoefficient;
    private javax.swing.JTextField jtfFilter;
    private javax.swing.JTextField jtfNumber;
    // End of variables declaration//GEN-END:variables
    private SalesView salesView;
    private RepricingView repricingView;
    private Integer documentCode;
    private JDateChooser date;
    private List table;
    private JTextField jTextBox;
    private Integer active;
    private Integer lock;
    private Integer hash;
    private boolean updateTable;

    public void showTable() {

        Vector<String> tableHeaders = new Vector<String>();
        Vector tableData = new Vector();
        tableHeaders.add("№");
        tableHeaders.add("Наименование");
        tableHeaders.add("Пред.цена");
        tableHeaders.add("Кол-во");
        tableHeaders.add("Сумма переоценки");
        tableHeaders.add("Цена после переоценки");

        if (table != null) {
            Session session = HUtil.getSession();
            for (int i = 0; i < table.size(); i++) {
                Repricingtable rt = (Repricingtable) table.get(i);
                Vector<Object> oneRow = new Vector<Object>();
                oneRow.add(i + 1);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", rt.getProductCode(), session);
                if (n != null) {
                    oneRow.add(n.getName());
                } else {
                    oneRow.add("");
                }
                oneRow.add(rt.getStartPrice());
                oneRow.add(rt.getQuantity());
                oneRow.add(rt.getChangePrice());
                oneRow.add(rt.getNewPrice());
                tableData.add(oneRow);
            }
            session.close();
        }

        jtRepricingDoc.setModel(new MyTableModel(tableData, tableHeaders));
        tuneTable();
    }

    private void tuneTable() {

        Util.autoResizeColWidth(jtRepricingDoc);

        jtRepricingDoc.getColumnModel().getColumn(5).setCellEditor(new MyCellEditor(jTextBox));

        jtRepricingDoc.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                int r = e.getFirstRow();
                int c = e.getColumn();
                updateTable(r, c);
                showTable();
                Util.moveCell(r, c, jtRepricingDoc, true);
            }
        });
    }

    @Action
    public void addProduct() {
        JInternalFrame nv = new NomenclatureView(salesView, this, -1, -1, "", "", null, false);
        salesView.getJDesktopPane().add(nv, javax.swing.JLayeredPane.DEFAULT_LAYER);
        nv.setVisible(true);
        try {
            nv.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    @Override
    public void setProductItem(Integer productCode, int r) {
        try {
            Session session = HUtil.getSession();
            Repricingtable rt = (Repricingtable) table.get(r);
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            rt.setProductCode(productCode);

            if (n != null) {
                rt.setStartPrice(n.getInPrice());
                rt.setQuantity(HUtil.getBalance(productCode));
                if (!jtfCoefficient.getText().equals("")) {
                    rt.setNewPrice(rt.getStartPrice() * new Integer(jtfCoefficient.getText()) / 100);
                } else {
                    rt.setNewPrice(rt.getStartPrice());
                }
                rt.setChangePrice(rt.getNewPrice() - rt.getStartPrice());
            }
            table.set(r, rt);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }
        showTable();
    }

    @Override
    public void setProductItemPart(Integer productCode, int row) {
        setProductItem(productCode, row);
    }

    public void updateTable(Integer r, Integer c) {
        try {
            Repricingtable rt = (Repricingtable) table.get(r);
            if (c == 2) {
                rt.setStartPrice(new Integer(jtRepricingDoc.getValueAt(r, 2).toString()));
                if (rt.getNewPrice() != null && rt.getStartPrice() != null) {
                    rt.setChangePrice(rt.getNewPrice() - rt.getStartPrice());
                }
            }
            if (c == 5) {
                rt.setNewPrice(new Integer(jtRepricingDoc.getValueAt(r, 5).toString()));
                rt.setChangePrice(rt.getQuantity() * (rt.getNewPrice() - rt.getStartPrice()));
            }
            table.set(r, rt);

        } catch (Exception e) {
            logger.error(e);
        }

    }

    @Action
    public void showRepricing() {

        Util.checkDocSaved(this);

        try {
            File file = new File(Util.getAppPath() + "\\templates\\Repricing.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            sheet.getCellAt("A1").setValue("Акт переоценки № " + jtfNumber.getText() + " от " + sdf.format(date.getDate()));
            sheet.getCellAt("A3").setValue(
                    "Мы, комиссия в составе: директор " + HUtil.getShortNameByCode(HUtil.getIntConstant("director"))
                    + ", гл. бух. " + HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant"))
                    + " произвели переоценку товара:");
            int ts = table.size();
            if (ts > 0) {
                sheet.duplicateRows(7, 1, ts - 1);
            }
            int amount = 0;
            int i;
            for (i = 0; i < ts; i++) {
                sheet.getCellAt("A" + (8 + i)).setValue(i + 1);
                sheet.getCellAt("B" + (8 + i)).setValue(jtRepricingDoc.getValueAt(i, 1));
                sheet.getCellAt("C" + (8 + i)).setValue(jtRepricingDoc.getValueAt(i, 2));
                sheet.getCellAt("D" + (8 + i)).setValue(jtRepricingDoc.getValueAt(i, 3));
                sheet.getCellAt("E" + (8 + i)).setValue(jtRepricingDoc.getValueAt(i, 4));
                sheet.getCellAt("F" + (8 + i)).setValue(jtRepricingDoc.getValueAt(i, 5));
                amount += (Integer) jtRepricingDoc.getValueAt(i, 4);
            }
            sheet.getCellAt("E" + (8 + i)).setValue(amount);

            if (amount >= 0) {
                fwMoney mo = new fwMoney(amount);
                sheet.getCellAt("B" + (10 + i)).setValue("Сумма переоценки составила: " + mo.num2str(true));
            } else {
                fwMoney mo = new fwMoney(-amount);
                sheet.getCellAt("B" + (10 + i)).setValue("Сумма переоценки составила: минус " + mo.num2str(true));
            }

            sheet.getCellAt("C" + (12 + i)).setValue("_____________________ " + HUtil.getShortNameByCode(HUtil.getIntConstant("director")));
            sheet.getCellAt("C" + (13 + i)).setValue("_____________________ " + HUtil.getShortNameByCode(HUtil.getIntConstant("chiefAccountant")));

            String r = " " + Math.round(Math.random() * 100000);
            String doc = "temp\\Акт переоценки №" + jtfNumber.getText() + r + ".ods";
            File outputFile = new File(doc);
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc(doc);

        } catch (Exception e) {
            logger.error(e);
        }
    }

    @Override
    public void setContractor(Integer contractorCode) {
    }

    @Action
    public void compute() {
        if (!jtfCoefficient.getText().equals("")) {
            Double coefficient = new Double(jtfCoefficient.getText());
            for (int i = 0; i < table.size(); i++) {
                Repricingtable rt = (Repricingtable) table.get(i);
                rt.setNewPrice(Util.roundInt((int) Math.round(((double) rt.getStartPrice()) * (100 + coefficient) / 100)));
                rt.setChangePrice((rt.getNewPrice() - rt.getStartPrice()) * rt.getQuantity());
                table.set(i, rt);
            }
            showTable();
        }
    }

    @Action
    public void showPriceList() {

        Util.checkDocSaved(this);

        try {

            int NUM_PRICELIST = Util.getFinalPriceList();

            File file = new File(Util.getAppPath() + "\\templates\\PriceListStandard.ods");
            final Sheet sheet = SpreadSheet.createFromFile(file).getSheet(0);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Session session = HUtil.getSession();
            int ts = table.size();
            sheet.duplicateRows(0, 5, (int) Math.ceil((ts - 1) / NUM_PRICELIST));
            String companyName = HUtil.getConstant("name");
            int j = 0;
            for (int i = 0; i < ts; i++) {
                Repricingtable rt = (Repricingtable) table.get(i);
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", rt.getProductCode(), session);
                String ean = "";
                if (n != null) {
                    String sc = n.getScanCode();
                    if (sc != null && sc.length() > 0) {
                        ean = Util.ean13(sc.substring(0, sc.length() - 1));
                    }
                }
                int c = i % NUM_PRICELIST * 4;
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 1)).setValue(companyName);
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 2)).setValue(ean);
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 3)).setValue(n.getName());
                sheet.getCellAt("" + (char) (67 + c) + (j * 5 + 4)).setValue(n.getPrice());
                sheet.getCellAt("" + (char) (65 + c) + (j * 5 + 5)).setValue(
                        "Акт переоценки №" + jtfNumber.getText() + " от " + sdf.format(date.getDate()));
                if (i % NUM_PRICELIST == NUM_PRICELIST - 1) {
                    j++;
                }
            }

            session.close();

            String r = " " + Math.round(Math.random() * 100000);
            File outputFile = new File("temp\\Прайс-лист по акту переоценки № " + jtfNumber.getText() + r + ".ods");
            sheet.getSpreadSheet().saveAs(outputFile);
            Util.openDoc("temp\\Прайс-лист по акту переоценки № " + jtfNumber.getText() + r + ".ods");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void close() {

        if (lock == 0) {

            save();

            Session session = HUtil.getSession();
            Repricing x = (Repricing) HUtil.getElement("Repricing", documentCode, session);
            if (x != null) {
                session.beginTransaction();
                x.setLocked(0);
                session.merge(x);
                session.getTransaction().commit();
            }
            session.close();
        }
    }

    @Action
    public void closeDoc() {
        close();
        Util.closeJIF(this, repricingView, salesView);
        Util.closeJIFTab(this, salesView);
    }

    @Override
    public void setFocus() {
        jtRepricingDoc.setRequestFocusEnabled(true);
        jtRepricingDoc.requestFocusInWindow();
    }

    @Override
    public void addProductItem(Integer productCode) {
        try {
            Session session = HUtil.getSession();
            Repricingtable rt = new Repricingtable();
            Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", productCode, session);
            rt.setProductCode(productCode);

            if (documentCode != null) {
                rt.setDocumentCode(documentCode);
            }

            if (n != null) {
                rt.setStartPrice(n.getPrice());
                rt.setNewPrice(n.getPrice());
                rt.setQuantity(HUtil.getBalance(productCode));
            }

            table.add(rt);

            session.close();
        } catch (Exception e) {
            logger.error(e);
        }

        showTable();

        Rectangle rect = jtRepricingDoc.getCellRect(jtRepricingDoc.getRowCount() - 1, 1, true);
        jtRepricingDoc.scrollRectToVisible(rect);
        jtRepricingDoc.getSelectionModel().addSelectionInterval(jtRepricingDoc.getRowCount() - 1, jtRepricingDoc.getRowCount() - 1);
        jtRepricingDoc.getColumnModel().getSelectionModel().setSelectionInterval(1, 1);
    }

    @Action
    public void deleteProduct() {
        if (jtRepricingDoc.getSelectedRow() >= 0) {
            table.remove(jtRepricingDoc.getSelectedRow());
            showTable();
        }
    }

    public void fillTable(boolean nonzero) {
        Session session = HUtil.getSession();
        List res = HUtil.getBalanceNow();
        for (int i = 0; i < res.size(); i++) {
            Object[] row = (Object[]) res.get(i);
            Integer iv = 0;
            if (row[2] != null) {
                iv = new Integer(row[2].toString());
            }
            Integer ov = 0;
            if (row[3] != null) {
                ov = new Integer(row[3].toString());
            }
            if (!nonzero || iv > ov) {
                Repricingtable rt = new Repricingtable();
                rt.setProductCode(new Integer(row[0].toString()));
                rt.setStartPrice(new Integer(row[1].toString()));
                if (!jtfCoefficient.getText().equals("")) {
                    rt.setNewPrice(rt.getStartPrice() * (new Integer(jtfCoefficient.getText()) + 100) / 100);
                    rt.setChangePrice(rt.getNewPrice() - rt.getStartPrice());
                }
                rt.setQuantity(iv - ov);
                table.add(rt);
            }
        }

        session.close();

        showTable();
    }

    @Action
    public void fillNomenclature() {
        if (table.size() > 0) {
            int a = JOptionPane.showOptionDialog(
                    this, "Очистить таблицу?", "Очистка таблицы", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
            if (a == 0) {
                table.clear();
            }
        }
        fillTable(false);
    }

    @Action
    public void fillNonzero() {
        if (table.size() > 0) {
            int a = JOptionPane.showOptionDialog(
                    this, "Очистить таблицу?", "Очистка таблицы", JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Нет");
            if (a == 0) {
                table.clear();
            }
        }
        fillTable(true);
    }

    public void commit() {
        try {

            if (jtRepricingDoc.getSelectedRow() >= 0) {
                updateTable(jtRepricingDoc.getSelectedRow(), jtRepricingDoc.getSelectedColumn());
            }

            Session session = HUtil.getSession();
            session.beginTransaction();

            Repricing r = null;
            if (documentCode != null) {
                r = (Repricing) HUtil.getElement("Repricing", documentCode, session);
            }
            if (r == null) {
                r = new Repricing();
            }
            r.setDatetime(Util.composeDate(date.getDate(), (Date) jsTime.getValue()));
            r.setNumber(jtfNumber.getText());
            if (jcbActive.isSelected()) {
                r.setActive(1);
            } else {
                r.setActive(0);
            }

            if (documentCode != null) {
                session.update(r);
            } else {
                session.save(r);
            }

            session.getTransaction().commit();

            documentCode = r.getCode();

            session.beginTransaction();

            List objs = session.createQuery("from Repricingtable rt where rt.documentCode = " + documentCode).list();

            for (int j = 0; j < table.size(); j++) {
                Repricingtable rt = (Repricingtable) table.get(j);
                rt.setDocumentCode(documentCode);
                rt.setLine(j + 1);
                boolean newRt = true;
                for (int k = 0; k < objs.size(); k++) {
                    if (((Repricingtable) objs.get(k)).getCode() == rt.getCode()) {
                        newRt = false;
                        objs.remove(k);
                        break;
                    }
                }
                if (newRt) {
                    session.save(rt);
                } else {
                    session.merge(rt);
                }
            }

            for (int j = 0; j < objs.size(); j++) {
                session.delete((Repricingtable) objs.get(j));
            }

            session.getTransaction().commit();

            if (r.getActive() == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Repricingtable rt = (Repricingtable) table.get(j);
                    HUtil.updatePrice(rt.getProductCode(), rt.getNewPrice(), date.getDate(), session);
                }
                session.getTransaction().commit();

            } else if (r.getActive() == 0 && active == 1) {

                session.beginTransaction();
                for (int j = 0; j < table.size(); j++) {
                    Repricingtable rt = (Repricingtable) table.get(j);
                    Integer[] prices = HUtil.getPriceBeforeDate(rt.getProductCode(), date.getDate());
                    if (prices[0] > 0) {
                        HUtil.updatePrice(rt.getProductCode(), prices[1], prices[0], date.getDate(), session);
                    }
                }
                session.getTransaction().commit();
            }

            session.close();

        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void save() {

        if (lock == 0) {

            TableCellEditor tce = jtRepricingDoc.getCellEditor();
            if (tce != null) {
                tce.stopCellEditing();
            }

            if (hash == null || !hash.equals(Util.hash(this))) {
                int a = JOptionPane.showOptionDialog(
                        this, "Сохранить документ?", "Сохранение", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Да", "Нет"}, "Да");
                if (a == 0) {

                    commit();

                    Util.updateJournals(salesView, RepricingView.class);
                    Util.updateJournals(salesView, NomenclatureView.class);

                    hash = Util.hash(this);

                }
            }
        }

    }

    public Integer getHash() {
        return hash;
    }

    public Integer getHashCode() {
        return Util.hash(this);
    }

    @Action
    public void saveDoc() {
        save();
    }

    @Action
    public void up() {
        if (lock == 0) {
            int r = jtRepricingDoc.getSelectedRow();
            if (r != -1 && r > 0) {
                Collections.swap(table, r, r - 1);
                updateTable = false;
                Util.swapRows(jtRepricingDoc, r, r - 1);
                tuneTable();
                Util.move2row(jtRepricingDoc, r - 1);
                updateTable = true;
            }
        }
    }

    @Action
    public void down() {
        if (lock == 0) {
            int r = jtRepricingDoc.getSelectedRow();
            if (r != -1 && r < table.size() - 1) {
                Collections.swap(table, r, r + 1);
                updateTable = false;
                Util.swapRows(jtRepricingDoc, r, r + 1);
                tuneTable();
                Util.move2row(jtRepricingDoc, r + 1);
                updateTable = true;
            }
        }
    }

    public void processFilter() {
        jtRepricingDoc.getSelectionModel().clearSelection();
        jtRepricingDoc.getColumnModel().getSelectionModel().clearSelection();

        if (!jtfFilter.getText().trim().isEmpty()) {

            StringTokenizer st = new StringTokenizer(jtfFilter.getText().trim());
            ArrayList ts = new ArrayList();
            while (st.hasMoreTokens()) {
                ts.add(st.nextElement().toString().toLowerCase());
            }

            boolean first = true;
            for (int i = 0; i < table.size(); i++) {
                boolean mark = true;
                Nomenclature n = (Nomenclature) HUtil.getElement("Nomenclature", ((Repricingtable) table.get(i)).getProductCode());
                String productName = n.getName().toLowerCase();
                String price = "" + ((Repricingtable) table.get(i)).getNewPrice();
                for (int j = 0; j < ts.size(); j++) {
                    if (!productName.contains((String) ts.get(j)) && !price.contains((String) ts.get(j))) {
                        mark = false;
                        break;
                    }
                }
                if (mark) {
                    jtRepricingDoc.getSelectionModel().addSelectionInterval(i, i);
                    jtRepricingDoc.getColumnModel().getSelectionModel().setSelectionInterval(0, 12);
                    if (first) {
                        Rectangle rect = jtRepricingDoc.getCellRect(i, 1, true);
                        jtRepricingDoc.scrollRectToVisible(rect);
                        first = false;
                    }
                }
            }
        }
    }
}
