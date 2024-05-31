import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class BusquedaPais {
    private Map<String, Pais> paisesMap;
    private Map<Integer, Estado> estadosMap;
    private Map<Integer, Municipio> municipiosMap;

    public BusquedaPais(Map<String, Pais> paisesMap, Map<Integer, Estado> estadosMap, Map<Integer, Municipio> municipiosMap) {
        this.paisesMap = paisesMap;
        this.estadosMap = estadosMap;
        this.municipiosMap = municipiosMap;
    }

    public Pais buscarPais(String isoCode) {
        long startTime = System.nanoTime();

        Pais pais = paisesMap.get(isoCode);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("TIEMPO: " + elapsedTime / 1000000.0 + " milisegundos");

        if (pais != null) {
            System.out.println("PAIS REGISTRADO: " + pais.getNombre());
            boolean hasStates = false;
            for (Estado estado : estadosMap.values()) {
                if (estado.getIdPais() == pais.getId()) {
                    if (!hasStates) {
                        System.out.println("Estados:");
                        hasStates = true;
                    }
                    System.out.println(" - " + estado.getNombre());
                    boolean hasMunicipios = false;
                    for (Municipio municipio : municipiosMap.values()) {
                        if (municipio.getIdEstado() == estado.getId()) {
                            if (!hasMunicipios) {
                                System.out.println("   Municipios:");
                                hasMunicipios = true;
                            }
                            System.out.println("     - " + municipio.getNombre());
                        }
                    }
                }
            }
            if (!hasStates) {
                System.out.println("SIN DATOS");
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿REPORTE DE EXCEL? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\REPORTE_PAIS.xlsx";
                exportarResultadosBusqueda(filePath, pais, elapsedTime);
            }

            return pais;
        } else {
            System.out.println("PAIS NO REGISTRADO");
            return null;
        }
    }

    public void exportarResultadosBusqueda(String filePath, Pais pais, long elapsedTime) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BUSQUEDA");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("PAIS");
        headerRow.createCell(1).setCellValue("ESTADO / DEPARTAMENTO");
        headerRow.createCell(2).setCellValue("CIUDAD / MUNICIPIO");
        headerRow.createCell(3).setCellValue("TIEMPO BUSQUEDA (ms)");

        int rowNum = 1;
        for (Map.Entry<Integer, Estado> estadoEntry : estadosMap.entrySet()) {
            Estado estado = estadoEntry.getValue();
            if (estado.getIdPais() == pais.getId()) {
                boolean hasMunicipios = false;
                for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) {
                    Municipio municipio = municipioEntry.getValue();
                    if (municipio.getIdEstado() == estado.getId()) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(pais.getNombre());
                        row.createCell(1).setCellValue(estado.getNombre());
                        row.createCell(2).setCellValue(municipio.getNombre());
                        hasMunicipios = true;
                    }
                }
                if (!hasMunicipios) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(pais.getNombre());
                    row.createCell(1).setCellValue(estado.getNombre());
                    row.createCell(2).setCellValue("N/A");
                }
                sheet.getRow(rowNum - 1).createCell(3).setCellValue(elapsedTime / 1000000.0);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
