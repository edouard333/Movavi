package com.phenix.movavi.ui;

import com.phenix.movavi.bl.MovaviBL;
import com.phenix.movavi.exception.MovaviException;
import com.phenix.tools.swing.FileDrop;

/**
 * Fenêtre principale.
 *
 * @author <a href="mailto:edouard128@hotmail.com">Edouard Jeanjean</a>
 */
public class Fenetre extends javax.swing.JFrame {

    /**
     * Crée la fenêtre.
     */
    public Fenetre() {
        initComponents();

        // On centre la fenêtre.
        super.setLocationRelativeTo(null);

        // <editor-fold defaultstate="collapsed" desc="Evenement détectant quand on dépose un fichier dans la fenêtre.">
        new FileDrop(this,
                files -> {
                    try {
                        for (int i = 0; i < files.length; i++) {
                            MovaviBL.convert(files[i]);
                        }
                    } catch (MovaviException ex) {
                        ex.printStackTrace();
                    }
                }
        );
        // </editor-fold>
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Convert Movavi to Resolve");

        jLabel1.setText("Déposé un projet Movavi");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(139, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(127, 127, 127))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(jLabel1)
                .addContainerGap(153, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
