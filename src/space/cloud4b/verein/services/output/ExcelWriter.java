package space.cloud4b.verein.services.output;


import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import space.cloud4b.verein.model.verein.adressbuch.Mitglied;
import space.cloud4b.verein.services.DatabaseReader;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class ExcelWriter extends Application {


    /**
     * exportiert die aktuelle Mitgliederliste in ein Excel-File
     * @throws IOException
     */
    public static void exportMirgliederToExcel() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Window stage = new Stage();
        fileChooser.setTitle("Speichern unter..");
        fileChooser.setInitialFileName("Mitgliederliste_" + LocalDate.now() + ".pdf");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
        File file = fileChooser.showSaveDialog(stage);
        System.out.println(file.getName());

        String[] columns = {"Name", "Vorname", "Anrede", "Adresse", "Adresszusatz",
            "PLZ", "Ort", "E-Mail", "Telefon", "Geburtsdatum", "Kat I", "Kat II", "Eintrittsdatum", "Vorstandsmitglied"};
        List<Mitglied> mitgliedList = new ArrayList<>(DatabaseReader.getMitgliederAsArrayList());

        Workbook workbook = new XSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Mitglieder");
        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());


        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Cell Style for formatting Date
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        // Create Other rows and cells with employees data
        int rowNum = 1;
        for(Mitglied mitglied: mitgliedList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(mitglied.getNachName());
            row.createCell(1)
                    .setCellValue(mitglied.getVorname());
            row.createCell(2)
                    .setCellValue(mitglied.getAnredeElement().toString());
            row.createCell(3)
                    .setCellValue(mitglied.getAdresse());
            row.createCell(4)
                    .setCellValue(mitglied.getAdresszusatz());
            row.createCell(5)
                    .setCellValue(mitglied.getPlz());
            row.createCell(6)
                    .setCellValue(mitglied.getOrt());



            row.createCell(7)
                    .setCellValue(mitglied.getEmail());
            row.createCell(8)
                    .setCellValue(mitglied.getTelefon());

            Cell dateOfBirthCell = row.createCell(9);
            if(mitglied.getGeburtsdatum()!=null) {
                dateOfBirthCell.setCellValue(mitglied.getGeburtsdatum().toString());
            } else {
                dateOfBirthCell.setCellValue("na");
            }
            dateOfBirthCell.setCellStyle(dateCellStyle);




            row.createCell(10)
                    .setCellValue(mitglied.getKategorieIElement().toString());
            row.createCell(11)
                    .setCellValue(mitglied.getKategorieIIElement().toString());
            Cell dateOfEntry = row.createCell(12);
            dateOfEntry.setCellValue(mitglied.getEintrittsdatum().toString());
            dateOfEntry.setCellStyle(dateCellStyle);
            row.createCell(13)
                    .setCellValue(mitglied.getIstVorstandsmitglied().getValue().toString());
        }

        // Resize all columns to fit the content size
        for(int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream(file.getParentFile() + "/" + file.getName());
        workbook.write(fileOut);
        fileOut.close();
        workbook.setForceFormulaRecalculation(true);
        Desktop.getDesktop().open(file);
        // Closing the workbook
        //workbook.close();

    }





}

