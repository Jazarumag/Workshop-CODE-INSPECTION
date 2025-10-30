package espol.poo.modelo.academico;

import java.io.*;
import java.util.*;

/**
 * Clase que representa una materia académica.
 * @author Omen
 */
public class Materia implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String PATH = "archivo\\materias.ser";

    private String codigo;
    private String nombre;
    private int cantNiveles;

    // Cambiado ArrayList -> List y protegido acceso directo
    protected static final List<Materia> materias = cargarMaterias();

    /**
     * Constructor de la clase Materia.
     * @param codigo Código de la materia
     * @param nombre Nombre de la materia
     * @param cantidad Número de niveles de la materia
     */
    public Materia(String codigo, String nombre, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantNiveles = cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String c) {
        this.codigo = c;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String n) {
        this.nombre = n;
    }

    public int getCantNiveles() {
        return cantNiveles;
    }

    public void setCantNiveles(int c) {
        this.cantNiveles = c;
    }

    @Override
    public String toString() {
        return String.valueOf(codigo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Materia other = (Materia) obj;
        return cantNiveles == other.cantNiveles
                && Objects.equals(codigo, other.codigo)
                && Objects.equals(nombre, other.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nombre, cantNiveles);
    }

    /**
     * Guarda una lista inicial de materias en el archivo.
     */
    public static void subirArchivo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH))) {
            List<Materia> lista = Arrays.asList(
                    new Materia("CCPG1052", "PROGRAMACIÓN ORIENTADA A OBJETOS", 3),
                    new Materia("CCPG1000", "ÁLGEBRA LINEAL", 2)
            );
            out.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga las materias desde el archivo.
     * @return lista de materias
     */
    public static List<Materia> cargarMaterias() {
        List<Materia> materiasCargadas = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH))) {
            materiasCargadas = (List<Materia>) in.readObject();
        } catch (EOFException e) {
            // Fin de archivo alcanzado: no se hace nada
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materiasCargadas;
    }

    /**
     * Agrega una nueva materia si no existe en la lista.
     * @param m materia a agregar
     */
    public static void agregarMateria(Materia m) {
        if (!materias.contains(m)) {
            materias.add(m);
        }
        guardarMaterias();
    }

    /**
     * Edita una materia existente.
     * @param mantigua materia a editar
     * @param nombre nuevo nombre
     * @param cantNiveles nueva cantidad de niveles
     */
    public static void editarMateria(Materia mantigua, String nombre, int cantNiveles) {
        if (materias.contains(mantigua)) {
            materias.remove(mantigua);
        }
        mantigua.setNombre(nombre);
        mantigua.setCantNiveles(cantNiveles);
        materias.add(mantigua);
        guardarMaterias();
    }

    /**
     * Elimina una materia del registro.
     * @param m materia a eliminar
     */
    public static void eliminarMateria(Materia m) {
        materias.remove(m);
        guardarMaterias();
    }

    /**
     * Guarda la lista actual de materias en el archivo.
     */
    private static void guardarMaterias() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH))) {
            out.writeObject(materias);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
