import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class BusquedaMunicipio {
    private Map<String, Pais> paisesMap;
    private Map<Integer, Estado> estadosMap;
    private Map<Integer, Municipio> municipiosMap;

    public BusquedaMunicipio(Map<String, Pais> paisesMap, Map<Integer, Estado> estadosMap, Map<Integer, Municipio> municipiosMap) {
        this.paisesMap = paisesMap;
        this.estadosMap = estadosMap;
        this.municipiosMap = municipiosMap;
    }

    public Municipio buscarMunicipio(int idMunicipio) {
        long startTime = System.nanoTime();

        Municipio municipio = municipiosMap.get(idMunicipio);

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println("TIEMPO DE BUSQUEDA  " + elapsedTime / 1000000.0 + " milisegundos");

        if (municipio != null) {
            System.out.println("MUNICIPIO / CIUDAD ENCONTRADO : " + municipio.getNombre());
            Estado estado = null;
            for (Estado e : estadosMap.values()) {
                if (e.getId() == municipio.getIdEstado()) {
                    estado = e;
                    System.out.println("ESTADO: " + estado.getNombre());
                    break;
                }
            }
            Pais pais = null;
            if (estado != null) {
                for (Pais p : paisesMap.values()) {
                    if (p.getId() == estado.getIdPais()) {
                        pais = p;
                        System.out.println("PAIS: " + pais.getNombre());
                        break;
                    }
                }
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("¿REPORTE EXCEL? (S/N)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("S")) {
                String filePath = "C:\\Users\\eduar\\Desktop\\Prueba\\REPORTE_MUNICIPIOCIUDADxlsx";
                exportarResultadosBusquedaMunicipio(filePath, municipio, estado, pais, elapsedTime);
            }
        } else {
            System.out.println("MUNICIPIO CIUDAD NO REGISTRADO.");
        }

        return municipio;
    }

    public void exportarResultadosBusquedaMunicipio(String filePath, Municipio municipio, Estado estado, Pais pais, long elapsedTime) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BUSQUEDA");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("PAIS");
        headerRow.createCell(1).setCellValue("ESTADO / DEPARTAMENTO");
        headerRow.createCell(2).setCellValue("MUNICIPIO / CIUDAD");
        headerRow.createCell(3).setCellValue("TIEMPO DE BUSQUEDA (ms)");

        Row row = sheet.createRow(1);
        row.createCell(0).setCellValue(pais.getNombre());
        row.createCell(1).setCellValue(estado.getNombre());
        row.createCell(2).setCellValue(municipio.getNombre());
        row.createCell(3).setCellValue(elapsedTime / 1000000.0);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
