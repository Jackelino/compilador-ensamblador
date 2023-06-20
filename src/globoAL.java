import java.io.*;
import java.util.ArrayList;

public class globoAL {
    /**
     * Apuntador de avance
     */
    static int a_a = 0;
    /**
     * Apuntador de inicio
     */
    static int a_i = 0;
    /**
     * Tamaño de archivo
     */
    static int filesize = 0;
    /**
     * Bandera de fin de archivo
     */
    static boolean fin_archivo = false;
    /**
     * Linea del archivo leida
     */
    public static char[] linea = null;
    /**
     * Diagrama del analisis
     */
    static int diag = 0;
    /**
     * Estado del diagrama
     */
    static int ESTADO = 0;
    /**
     * Caracter leido de la linea
     */
    static int c;
    /**
     * Lexema de la expreción
     */
    static String lex = "";
    /**
     * Nombre del archivo para el Analisis
     */
    static String entrada = "";
    /**
     * Nombre del archivo para la salida del analisis.
     */
    static String salida = "";
    /**
     * Token encontrado de los estados.
     */
    static String miToken = "";
    /**
     * Número del renglón leído del archivo.
     */
    static int renglon = 1;

    /**
     * Arreglo de palabras reservadas
     */
    static ArrayList<String> palabraReservada;

    public static void main(String argumento[]) {
        entrada = argumento[0] + ".prg";
        salida = argumento[0] + ".sal";
        inicializaPalabrasReservadas();
        if (!xArchivo(entrada).exists()) {
            System.out.println("El archivo: " + entrada + " no existe.");
            System.exit(4);
        }
        do {
            ESTADO = 0;
            diag = 0;
            miToken = token();
            //if (miToken.equals("nosirve")) {
                creaArchivo(xArchivo(salida), miToken);
                creaArchivo(xArchivo(salida), lex);
                creaArchivo(xArchivo(salida), Integer.toString(renglon));
            //

            System.out.println("Este es el token hallado r(" + miToken + ")");
            System.out.println("Este es el lexema hallado {" + lex + "}");
            System.out.println("Renglon (" + renglon + ")");

            //pausa();
            a_i = a_a;
        } while (!fin_archivo);
        creaArchivo(xArchivo(salida), "eof");
        creaArchivo(xArchivo(salida), "eof");
        creaArchivo(xArchivo(salida), "666");
        System.out.println("Análisis lexicográfico exitoso.");
    }

    /**
     * Inicializa el arreglo de palabras reservadas
     */
    public static void inicializaPalabrasReservadas() {
        palabraReservada = new ArrayList<>();
        palabraReservada.add("datos");
        palabraReservada.add("fin_datos");
        palabraReservada.add("entero");
        palabraReservada.add("decimal");
        palabraReservada.add("cierto");
        palabraReservada.add("haz");
        palabraReservada.add("falso");
        palabraReservada.add("fin_cond");
        palabraReservada.add("mientras");
        palabraReservada.add("fin_mientras");
    }

    /**
     * Determina el diagrama para realizar el analizis
     *
     * @return int diagrama que utilizara para el analisis
     */
    public static int diagrama() {
        a_a = a_i;
        switch (diag) {
            case 0:
                diag = 3;
                break;
            case 3:
                diag = 7;
                break;
            case 7:
                diag = 12;
                break;
            case 12:
                diag = 15;
                break;
            case 15:
                diag = 19;
                break;
            case 19:
                diag = 30;
                break;
            case 30:
                diag = 47;
                break;
            case 47:
                error();
                break;
        }
        return (diag);
    }

    /**
     * Determina el token del analisis de los estados en los diagramas
     *
     * @return String - token de la expreción identificado.
     */
    public static String token() {
        do {
            switch (ESTADO) {
                case 0:
                    c = lee_car();
                    if (esDelimitador(c)) {
                        ESTADO = 1;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 1:
                    c = lee_car();
                    if (esDelimitador(c)) {
                        ESTADO = 1;
                    } else {
                        ESTADO = 2;
                    }
                    break;
                case 2:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("nosirve");
                case 3:
                    c = lee_car();
                    if (esLetra(c)) {
                        ESTADO = 4;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 4:
                    c = lee_car();
                    if (esLetra(c)) {
                        ESTADO = 4;
                    } else if (esDigito(c)) {
                        ESTADO = 4;
                    } else if (c == '_') {
                        ESTADO = 5;
                    } else {
                        ESTADO = 6;
                    }
                    break;
                case 5:
                    c = lee_car();
                    if (esLetra(c)) {
                        ESTADO = 4;
                    } else if (esDigito(c)) {
                        ESTADO = 4;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 6:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    if (esReservada(lex)) {
                        return (lex);
                    }
                    return ("id");
                case 7:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 8;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 8:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 8;
                    } else if (c == '.') {
                        ESTADO = 9;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 9:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 10;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 10:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 10;
                    } else {
                        ESTADO = 11;
                    }
                    break;
                case 11:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("dec");
                case 12:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 13;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 13:
                    c = lee_car();
                    if (esDigito(c)) {
                        ESTADO = 13;
                    } else {
                        ESTADO = 14;
                    }
                    break;
                case 14:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("ent");
                case 15:
                    c = lee_car();
                    if (c == '/') {
                        ESTADO = 16;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 16:
                    c = lee_car();
                    if (c == '/') {
                        ESTADO = 17;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 17:
                    c = lee_car();
                    if (!esDelimitadorEspacio(c)) {
                        ESTADO = 17;
                    } else {
                        ESTADO = 18;
                    }
                    break;
                case 18:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("nosirve");
                case 19:
                    c = lee_car();
                    switch (c) {
                        case '+':
                            ESTADO = 20;
                            break;
                        case '-':
                            ESTADO = 21;
                            break;
                        case '*':
                            ESTADO = 22;
                            break;
                        case '/':
                            ESTADO = 23;
                            break;
                        case '(':
                            ESTADO = 24;
                            break;
                        case ')':
                            ESTADO = 25;
                            break;
                        case '{':
                            ESTADO = 26;
                            break;
                        case '}':
                            ESTADO = 27;
                            break;
                        case ':':
                            ESTADO = 28;
                            break;
                        case ',':
                            ESTADO = 29;
                            break;
                        default:
                            ESTADO = diagrama();
                    }
                    break;
                case 20:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("+");
                case 21:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("-");
                case 22:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("*");
                case 23:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("/");
                case 24:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("(");
                case 25:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return (")");
                case 26:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("{");
                case 27:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("}");
                case 28:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return (":");
                case 29:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return (",");
                case 30:
                    c = lee_car();
                    switch (c) {
                        case '=':
                            ESTADO = 31;
                            break;
                        case '<':
                            ESTADO = 37;
                            break;
                        case '>':
                            ESTADO = 42;
                            break;
                        case '!':
                            ESTADO = 35;
                            break;
                        default:
                            ESTADO = diagrama();
                    }
                    break;
                case 31:
                    c = lee_car();
                    switch (c) {
                        case '/':
                            ESTADO = 32;
                            break;
                        case '>':
                            ESTADO = 34;
                            break;
                        case '<':
                            ESTADO = 35;
                            break;
                        default:
                            ESTADO = diagrama();
                    }
                    break;
                case 32:
                    c = lee_car();
                    if (c == '=') {
                        ESTADO = 33;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 33:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("dif");
                case 34:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("mayi");
                case 35:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("meni");
                case 36:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("igual");
                case 37:
                    c = lee_car();
                    switch (c) {
                        case '>':
                            ESTADO = 38;
                            break;
                        case '=':
                            ESTADO = 39;
                            break;
                        case '-':
                            ESTADO = 40;
                            break;
                        default:
                            ESTADO = diagrama();
                    }
                    break;
                case 38:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("dif");
                case 39:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("meni");
                case 40:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("asig");
                case 41:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("men");
                case 42:
                    c = lee_car();
                    if (c == '=') {
                        ESTADO = 43;
                    } else {
                        ESTADO = 44;
                    }
                    break;
                case 43:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("mayi");
                case 44:
                    a_a--;
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("may");
                case 45:
                    c = lee_car();
                    if (c == '=') {
                        ESTADO = 46;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 46:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("dif");
                case 47:
                    c = lee_car();
                    if (c == 255) {
                        ESTADO = 48;
                    } else {
                        ESTADO = diagrama();
                    }
                    break;
                case 48:
                    lex = obtenerLexema();
                    a_i = a_a;
                    return ("nosirve");
            }
        } while (true);
    }

    /**
     * Verifica si el carácter es una letra en el codigo ASCII
     *
     * @param caracter : carater leido
     * @return
     */
    public static boolean esLetra(int caracter) {
        return (caracter >= 65 && caracter <= 90) || (caracter >= 97 && caracter <= 122);
    }

    /**
     * Verifica si el carácter es un dígito en el código ASCII
     *
     * @param caracter
     * @return
     */
    public static boolean esDigito(int caracter) {
        return caracter >= 48 && caracter <= 57;
    }

    /**
     * Verifica si es una palabra reservada
     *
     * @param x : palabra a verificar
     * @return
     */
    public static boolean esReservada(String x) {
        for (String palabra : palabraReservada) {
            if (palabra.equals(x)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determina si es un espacio, enter o tabulador
     *
     * @param x
     * @return
     */
    public static Boolean esDelimitador(int x) {
        return x == 9 || x == 10 || x == 13 || x == 32;
    }

    /**
     * @param x
     * @return
     */
    public static Boolean esDelimitadorEspacio(int x) {
        return x == 9 || x == 10 || x == 13;
    }

    /**
     * Crea un archivo
     *
     * @param archivo nombre del archivo
     * @return
     */
    public static File xArchivo(String archivo) {
        return (new File(archivo));
    }

    /**
     * Escribe en el archivo
     *
     * @param xFile
     * @param mensaje
     * @return
     */
    public static Boolean creaArchivo(File xFile, String mensaje) {
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter(xFile, true));
            fileOut.println(mensaje);
            fileOut.close();
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    /**
     * Obtiene el lexema que encuentra.
     *
     * @return String lexema encontrado.
     */
    public static String obtenerLexema() {
        String lexema = "";
        for (int i = a_i; i < a_a; i++) {
            lexema += linea[i];
        }
        return (lexema);
    }

    /**
     * Determina si no existe el fin del archivo para seguir
     * con la lectura del archivo.
     *
     * @return int
     */
    public static int lee_car() {
        if (a_a <= filesize - 1) {
            if (c == 10) {
                renglon++;
            }
            return linea[a_a++];
        } else {
            fin_archivo = true;
            return 255;
        }
    }

    /**
     * Lee el archivo linea a linea
     *
     * @param nombreArchivo : nombre del archivo a leer.
     * @return
     */
    public static char[] leeArchivo(String nombreArchivo) {
        File xFile = new File(nombreArchivo);
        char[] data;
        try {
            FileReader fin = new FileReader(xFile);
            filesize = (int) xFile.length();
            data = new char[filesize + 1];
            fin.read(data, 0, filesize);
            data[filesize] = ' ';
            filesize++;
            return (data);
        } catch (FileNotFoundException exc) {
            System.out.println(exc);
        } catch (IOException exc) {
            System.out.println(exc);

        }
        return null;
    }

    /**
     * Muestra el error encontrado y termina la ejecución.
     */
    public static void error() {
        System.out.println("ERROR: en el carácter [" + (char) c + "] cerca del renglón: " + renglon);
        System.exit(4);
    }
}
