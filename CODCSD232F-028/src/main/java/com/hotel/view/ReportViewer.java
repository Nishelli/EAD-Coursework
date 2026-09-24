package com.hotel.view;

import com.hotel.dao.DatabaseConnection;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.io.InputStream;
import java.sql.Connection;

public class ReportViewer {

    public static void showReport() {
        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            
            // Load the Jasper report from resources
            InputStream reportStream = ReportViewer.class.getResourceAsStream("/reports/booking_report.jrxml");
            if (reportStream == null) {
                JOptionPane.showMessageDialog(null, "Report file not found!");
                return;
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);
            
            // View report
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error generating report: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
