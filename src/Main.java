import java.util.Scanner;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        sistema.cargarInformacionDesdeBD();
        mostrarMenuBusqueda(sistema);
    }

    private static void mostrarMenuBusqueda(Sistema sistema) {
        Scanner scanner = new Scanner(System.in);

        BusquedaPais busquedaPais = new BusquedaPais(sistema.getPaisesMap(), sistema.getEstadosMap(), sistema.getMunicipiosMap());
        BusquedaEstado busquedaEstado = new BusquedaEstado(sistema.getPaisesMap(), sistema.getEstadosMap(), sistema.getMunicipiosMap());
        BusquedaMunicipio busquedaMunicipio = new BusquedaMunicipio(sistema.getPaisesMap(), sistema.getEstadosMap(), sistema.getMunicipiosMap());

        String continuar;
        do {
            System.out.println("MENU DE BUSQUEDA");
            System.out.println("1. Búsqueda por país");
            System.out.println("2. Búsqueda por estado");
            System.out.println("3. Búsqueda por municipio");
            System.out.println("4. Salir");
            int opcion = scanner.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el ISO code del país:");
                    String isoCode = scanner.next();
                    Pais pais = busquedaPais.buscarPais(isoCode);
                    if (pais != null) {
                        System.out.println("País encontrado: " + pais.getNombre());
                    } else {
                        System.out.println("País no encontrado.");
                    }
                    break;
                case 2:
                    System.out.println("Ingrese el ID del estado:");
                    int idEstado = scanner.nextInt();
                    Estado estado = busquedaEstado.buscarEstado(idEstado);
                    if (estado != null) {
                        System.out.println("Estado encontrado: " + estado.getNombre());
                    } else {
                        System.out.println("Estado no encontrado.");
                    }
                    break;
                case 3:
                    System.out.println("Ingrese el ID del municipio:");
                    int idMunicipio = scanner.nextInt();
                    Municipio municipio = busquedaMunicipio.buscarMunicipio(idMunicipio);
                    if (municipio != null) {
                        System.out.println("Municipio encontrado: " + municipio.getNombre());
                    } else {
                        System.out.println("Municipio no encontrado.");
                    }
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }

            System.out.println("¿Desea volver al menú? (S/N)");
            continuar = scanner.next();
        } while (continuar.equalsIgnoreCase("S"));

        System.out.println("Saliendo del programa...");
    }
}
