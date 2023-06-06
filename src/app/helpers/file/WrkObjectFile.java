package app.helpers.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class WrkObjectFile {

    /**
     * Cette fonction sérialise l'objet passé en paramètre dans un fichier.
     *
     * @param filepath le chemin complet du fichier à utiliser
     * @param obj l'objet a sérialiser
     *
     * @return l'objet a-t-il été correctement sérialisé ?
     */
    public static boolean serialiseObjet(String filepath, Object obj) {

        boolean bResult = false;

        if (obj != null) {    // S'il n'y a rien à faire on ne fait rien (pas même flinguer le fichier) !

            ObjectOutputStream out = null;

            try {
                out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filepath)));
                out.writeObject(obj);
                out.flush();
                out.close();
                out = null;
                bResult = true;     // Si on est ici c'est que vraiment tout s'est bien passé !
            } catch (IOException ex) {
                System.out.println("");
            } finally {
                if (out != null) {
                    try {
                        out.close();
                        out = null;
                    } catch (IOException ex) {
                    }
                }

                // Si l'écriture a échoué d'une façon où d'une autre, ne pas laisser un fichier incomplet
                if (!bResult) {
                    try {
                        new File(filepath).delete();
                    } catch (Exception e) {
                    }
                }
            }
        }

        return bResult;
    }

    /**
     * Cette fonction desérialise un objet précédemment sérialisé dans un
     * fichier.
     *
     * @param filepath le chemin complet du fichier à utiliser
     *
     * @return l'objet s'il a été correctement désérialisé, sinon null
     */
    public static Object deserialiseObjet(String filepath) {
        Object result = null;

        ObjectInputStream in = null;

        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filepath)));
            result = in.readObject();
            in.close();
            in = null;
        } catch (Exception ex) {
            result = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException ex) {
                }
            }
        }

        return result;
    }
}
