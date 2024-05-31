import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class BusquedaEstado {
    private Map<String, Pais> paisesMap;
    private Map<Integer, Estado> estadosMap;
    private Map<Integer, Municipio> municipiosMap;

    public BusquedaEstado(Map<String, Pais> paisesMap, Map<Integer, Estado> estadosMap, Map<Integer, Municipio> municipiosMap) {
        this.paisesMap = paisesMap;
        this.estadosMap = estadosMap;
        this.municipiosMap = municipiosMap;
    }

    public Estado buscarEstado(int idEstado) {
        long startTime = System.nanoTime();

        Estado estado = estadosMap.get(idEstado);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("TIEMPO DE BUSQUEDA: " + elapsedTime / 1000000.0 + " milisegundos");

        if (estado != null) {
            System.out.println("ESTADO REGISTRADO : " + estado.getNombre());
            Pais pais = null;
            for (Pais p : paisesMap.values()) {
                if (p.getId() == estado.getIdPais()) {
                    pais = p;
                    System.out.println("PAIS: " + pais.getNombre());
                    break;
                }
            }
            System.out.println("CIUDADES / MUNICIPIOS:");
            for (Municipio municipio : municipiosMap.values()) {
                if (municipio.getIdEstado() == estado.getId()) {
                    System.out.println(" - " + municipio.getNombre());
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿REPORTE EXCEL? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\REPORTE_ESTADO.xlsx";
                exportarResultadosBusquedaEstado(filePath, estado, pais, elapsedTime);
            }
        } else {
            System.out.println("ESTADO NO REGISTRADO.");
        }

        return estado;
    }

    public void exportarResultadosBusquedaEstado(String filePath, Estado estado, Pais pais, long elapsedTime) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BUSQUEDA");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("PAIS");
        headerRow.createCell(1).setCellValue("DEPARTAMENTO / ESTADO");
        headerRow.createCell(2).setCellValue("MUNICIPIO / CIUDAD");
        headerRow.createCell(3).setCellValue("TIEMPO BUSQUEDA (ms)");

        int rowNum = 1;
        boolean hasMunicipios = false;
        for (Map.Entry<Integer, Municipio> municipioEntry : municipiosMap.entrySet()) {
            Municipio municipio = municipioEntry.getValue();
            if (municipio.getIdEstado() == estado.getId()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pais.getNombre());
                row.createCell(1).setCellValue(estado.getNombre());
                row.createCell(2).setCellValue(municipio.getNombre());
                row.createCell(3).setCellValue(elapsedTime / 1000000.0);
                hasMunicipios = true;
            }
        }
        if (!hasMunicipios) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(pais.getNombre());
            row.createCell(1).setCellValue(estado.getNombre());
            row.createCell(2).setCellValue("N/A");
            row.createCell(3).setCellValue(elapsedTime / 1000000.0);
        }

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
