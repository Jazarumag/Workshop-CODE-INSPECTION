package espol.poo.modelo.juego;

import espol.poo.modelo.academico.Materia;
import java.io.*;
import java.util.*;

/**
 * Clase que representa una pregunta dentro del juego.
 * Contiene su texto, nivel, materia asociada, respuestas y posible comodín.
 * 
 * @author Omen
 */
public class Pregunta implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String PATH = "archivo\\preguntas.ser";

    private String texto;
    private int nivel;
    private Materia materia;
    private List<Respuesta> respuestas;
    private Comodin comodinUsado;

    // Lista de preguntas cargadas desde archivo
    protected static final List<Pregunta> preguntas = cargarPreguntas();

    /**
     * Constructor vacío requerido para serialización.
     */
    public Pregunta() {
    }

    /**
     * Constructor completo.
     * @param texto Texto de la pregunta
     * @param nivel Nivel de dificultad
     * @param materia Materia a la que pertenece
     * @param respuestas Lista de respuestas posibles
     */
    public Pregunta(String texto, int nivel, Materia materia, List<Respuesta> respuestas) {
        this.texto = texto;
        this.nivel = nivel;
        this.materia = materia;
        this.respuestas = respuestas;
    }

    /**
     * Constructor alternativo para comodín 50/50.
     * @param respuestas Lista de respuestas disponibles
     */
    public Pregunta(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    // --- Getters y Setters ---

    public String getTexto() {
        return texto;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    public Comodin getComodinUsado() {
        return comodinUsado;
    }

    public void setComodinUsado(Comodin comodinUsado) {
        this.comodinUsado = comodinUsado;
    }

    // --- Métodos funcionales ---

    /**
     * Obtiene las preguntas que pertenecen a una materia específica.
     * @param preguntas Lista general de preguntas
     * @param materia Materia de interés
     * @return Lista de preguntas que pertenecen a esa materia
     */
    public static List<Pregunta> getPreguntasMateria(List<Pregunta> preguntas, Materia materia) {
        List<Pregunta> preguntasMateria = new ArrayList<>();
        for (Pregunta p : preguntas) {
            if (p.getMateria().equals(materia)) {
                preguntasMateria.add(p);
            }
        }
        return preguntasMateria;
    }

    /**
     * Crea una copia superficial de una pregunta.
     * @param p Pregunta original
     * @return Nueva instancia con los mismos valores
     */
    public static Pregunta copy(Pregunta p) {
        List<Respuesta> respuestas = new ArrayList<>(p.getRespuestas());
        return new Pregunta(p.getTexto(), p.getNivel(), p.getMateria(), respuestas);
    }

    /**
     * Elimina un número de respuestas incorrectas (usado en comodines).
     * @param n Cantidad de respuestas a eliminar
     */
    public void removeRespuestasIncorrectas(int n) {
        Iterator<Respuesta> it = respuestas.iterator();
        int contador = 0;
        while (it.hasNext() && contador < n) {
            Respuesta r = it.next();
            if (r.getTipo().equals(TipoRespuesta.INCORRECTA)) {
                it.remove();
                contador++;
            }
        }
    }

    // --- Métodos de objeto ---

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pregunta other)) return false;
        return nivel == other.nivel &&
               Objects.equals(texto, other.texto) &&
               Objects.equals(materia, other.materia) &&
               Objects.equals(respuestas, other.respuestas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texto, nivel, materia, respuestas);
    }

    @Override
    public String toString() {
        List<String> literales = Arrays.asList("A", "B", "C", "D");
        StringBuilder sb = new StringBuilder();
        sb.append("Nivel: ").append(nivel)
          .append("\nPregunta:\n").append(texto)
          .append("\nOpciones de respuesta:\n");

        Collections.shuffle(respuestas);
        for (int i = 0; i < respuestas.size(); i++) {
            sb.append(literales.get(i)).append(". ").append(respuestas.get(i)).append("\n");
        }

        return sb.toString();
    }

    // --- Persistencia ---

    /**
     * Carga las preguntas desde el archivo serializado.
     * @return Lista de preguntas cargadas
     */
    public static List<Pregunta> cargarPreguntas() {
        List<Pregunta> preguntasCargadas = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(PATH))) {
            preguntasCargadas = (List<Pregunta>) in.readObject();
        } catch (EOFException e) {
            // Archivo vacío: no hacer nada
        } catch (Exception e) {
            e.printStackTrace();
        }
        return preguntasCargadas;
    }

    /**
     * Guarda una lista inicial de preguntas de ejemplo.
     */
    public static void subirArchivo() {
        List<Pregunta> lista = new ArrayList<>();

        lista.add(new Pregunta("¿Cuánto es 2 + 2?", 2, Materia.materias.get(0),
                Arrays.asList(new Respuesta("4", TipoRespuesta.CORRECTA),
                              new Respuesta("5", TipoRespuesta.INCORRECTA),
                              new Respuesta("0", TipoRespuesta.INCORRECTA),
                              new Respuesta("22", TipoRespuesta.INCORRECTA))));

        lista.add(new Pregunta("¿Qué es el polimorfismo en programación orientada a objetos?",
                1, Materia.materias.get(0),
                Arrays.asList(
                        new Respuesta("Encapsulación de datos y métodos en una entidad.", TipoRespuesta.INCORRECTA),
                        new Respuesta("Capacidad de objetos distintos de responder al mismo método de forma diferente.", TipoRespuesta.CORRECTA),
                        new Respuesta("Combinación de múltiples clases en una jerarquía.", TipoRespuesta.INCORRECTA),
                        new Respuesta("Capacidad de una clase para heredar de sí misma.", TipoRespuesta.INCORRECTA))));

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH))) {
            out.writeObject(lista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Agrega una nueva pregunta si no existe ya en la lista.
     * @param p Pregunta a agregar
     */
    public static void agregarPregunta(Pregunta p) {
        if (!preguntas.contains(p)) {
            preguntas.add(p);
        }
        guardarPreguntas();
    }

    /**
     * Elimina una pregunta existente.
     * @param p Pregunta a eliminar
     */
    public static void eliminarPregunta(Pregunta p) {
        preguntas.remove(p);
        guardarPreguntas();
    }

    /**
     * Guarda la lista actual de preguntas en el archivo.
     */
    private static void guardarPreguntas() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(PATH))) {
            out.writeObject(preguntas);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
