/*
 * MediathekView
 * Copyright (C) 2008 W. Xaver
 * W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.gui.dialogEinstellungen;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jidesoft.utils.SystemInfo;
import mediathek.controller.filmeLaden.importieren.DatenFilmlistenServer;
import mediathek.daten.DDaten;
import mediathek.daten.Daten;
import mediathek.gui.PanelVorlage;
import mediathek.tool.Funktionen;
import mediathek.tool.Konstanten;
import mediathek.tool.ListenerMediathekView;
import mediathek.tool.Log;
import mediathek.tool.TModel;

public class PanelListeFilmlistenServer extends PanelVorlage {

    public PanelListeFilmlistenServer(DDaten d, Component parentComponent) {
        super(d, parentComponent);
        initComponents();
        init();
    }

    private void init() {
        tabelleLaden();
        jButtonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tabelleLaden();
            }
        });
        jButtonDateiAuswaehlen.addActionListener(new BeobPfad());
        jButtonLoeschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextFieldUrl.setText("");
            }
        });
        jTable1.getSelectionModel().addListSelectionListener(new BeobachterTableSelect());
        jTextFieldUrl.setText(Daten.system[Konstanten.SYSTEM_URL_FILMLISTEN_NR]);
        jTextFieldUrl.getDocument().addDocumentListener(new BeobDateiUrl());
        ListenerMediathekView.addListener(new ListenerMediathekView(ListenerMediathekView.EREIGNIS_LISTE_FILMLISTEN_SERVER, PanelListeFilmlistenServer.class.getSimpleName()) {
            @Override
            public void ping() {
                tabelleLaden();
            }
        });
    }

    private void tabelleLaden() {
        TModel model = new TModel(DDaten.filmeLaden.getListeFilmlistnServer().getTableObjectData(), DatenFilmlistenServer.FILM_LISTEN_SERVER_COLUMN_NAMES_ANZEIGE);
        jTable1.setModel(model);
        for (int i = 0; i < jTable1.getColumnCount(); ++i) {
            jTable1.getColumnModel().getColumn(i).setMinWidth(10);
            jTable1.getColumnModel().getColumn(i).setMaxWidth(3000);
            if (i == DatenFilmlistenServer.FILM_LISTEN_SERVER_NR_NR) {
                jTable1.getColumnModel().getColumn(i).setPreferredWidth(50);
            } else if (i == DatenFilmlistenServer.FILM_LISTEN_SERVER_URL_NR) {
                jTable1.getColumnModel().getColumn(i).setPreferredWidth(500);
            }
        }
    }

    private void table1Select() {
        int selectedTableRow = jTable1.getSelectedRow();
        if (selectedTableRow >= 0) {
            //daten.system[Konstanten.SYSTEM_IMPORT_ART_FILME_NR] = String.valueOf(Konstanten.UPDATE_FILME_AUS);
            jTextFieldUrl.setText(jTable1.getModel().getValueAt(
                    jTable1.convertRowIndexToModel(jTable1.getSelectedRow()), DatenFilmlistenServer.FILM_LISTEN_SERVER_URL_NR).toString());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.ButtonGroup buttonGroup1 = new javax.swing.ButtonGroup();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jTextFieldUrl = new javax.swing.JTextField();
        jButtonDateiAuswaehlen = new javax.swing.JButton();
        jButtonLoeschen = new javax.swing.JButton();
        jButtonUpdate = new javax.swing.JButton();

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("URL/Datei:");

        jButtonDateiAuswaehlen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mediathek/res/fileopen_16.png"))); // NOI18N

        jButtonLoeschen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mediathek/res/del_16.png"))); // NOI18N

        jButtonUpdate.setText("Update");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldUrl, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonDateiAuswaehlen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonLoeschen))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButtonUpdate)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonDateiAuswaehlen, jButtonLoeschen});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldUrl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButtonDateiAuswaehlen)
                    .addComponent(jButtonLoeschen))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonDateiAuswaehlen, jLabel1, jTextFieldUrl});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonDateiAuswaehlen;
    private javax.swing.JButton jButtonLoeschen;
    private javax.swing.JButton jButtonUpdate;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldUrl;
    // End of variables declaration//GEN-END:variables

    private class BeobachterTableSelect implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent event) {
            if (!event.getValueIsAdjusting()) {
                table1Select();
            }
        }
    }

    private class BeobPfad implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            //we can use native chooser on Mac...
            if (SystemInfo.isMacOSX()) {
                FileDialog chooser = new FileDialog(ddaten.mediathekGui, "Filmlisten-Liste verwenden");
                chooser.setMode(FileDialog.LOAD);
                chooser.setVisible(true);
                if (chooser.getFile() != null) {
                    try {
                        File destination = new File(chooser.getDirectory() + chooser.getFile());
                        jTextFieldUrl.setText(destination.getAbsolutePath());
                    } catch (Exception ex) {
                        Log.fehlerMeldung(794512036, Log.FEHLER_ART_PROG, "PanelListeFilmlistenServer.BeobPfad", ex);
                    }
                }
            } else {
                int returnVal;
                JFileChooser chooser = new JFileChooser();
                if (!jTextFieldUrl.getText().equals("")) {
                    chooser.setCurrentDirectory(new File(jTextFieldUrl.getText()));
                }
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setFileHidingEnabled(false);
                returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        jTextFieldUrl.setText(chooser.getSelectedFile().getAbsolutePath());
                    } catch (Exception ex) {
                        Log.fehlerMeldung(733025319, Log.FEHLER_ART_PROG, "PanelListeFilmlistenServer.BeobPfad", ex);
                    }
                }
            }
        }
    }

    private class BeobDateiUrl implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            tus();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            tus();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            tus();
        }

        private void tus() {
            Daten.system[Konstanten.SYSTEM_URL_FILMLISTEN_NR] = jTextFieldUrl.getText();
        }
    }
}
