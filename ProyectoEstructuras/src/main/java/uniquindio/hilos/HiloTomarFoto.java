package uniquindio.hilos;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class HiloTomarFoto extends Thread{
    String nombreUsuario;
    String rutaCarpeta = "C://td";

    public String getRutaCarpeta() {
        return rutaCarpeta;
    }

    public HiloTomarFoto(String nombreUsuario) {
        this.nombreUsuario = "imagen_"+nombreUsuario+".jpg";
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public String obtenerUrl(){
        return rutaCarpeta+"/"+nombreUsuario;
    }

    public void run(){
        tomarFoto();

    }
    private void tomarFoto(){
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize()); // Define la resolución deseada

        if (webcam == null) {
            System.out.println("No se encontró una cámara.");
            return;
        }

        // Abre la cámara
        webcam.open();

        // Captura una imagen
        try {
            // Captura la imagen en formato BufferedImage
            BufferedImage image = webcam.getImage();

            // Define la ruta de la carpeta y el nombre del archivo

            String nombreArchivo = nombreUsuario;

            // Guarda la imagen en un archivo
            File archivo = new File(rutaCarpeta, nombreArchivo);
            ImageIO.write(image, "JPG", archivo);

            System.out.println("Imagen guardada en: " + archivo.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cierra la cámara
        webcam.close();
    }
}
