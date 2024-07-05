package com.tatanstudios.abbaappandroid.activity.pruebas;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


import com.tatanstudios.abbaappandroid.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class BibliaActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblia);


        listView = findViewById(R.id.listView);
        books = new ArrayList<>();

        obtenerLibros();
    }



    private void obtenerLibros() {
        try {
            // Obtener el nombre del archivo XML
            String archivoXML = "biblia.xml";

            // Crear un DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Crear un DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parsear el archivo XML
            Document document = builder.parse(getResources().getAssets().open(archivoXML));

            // Obtener la raíz del documento
            Element raiz = document.getDocumentElement();

            // Obtener la lista de elementos "libro"
            NodeList libros = raiz.getElementsByTagName("libro");

            // Recorrer la lista de libros
            for (int i = 0; i < libros.getLength(); i++) {
                // Obtener el elemento "libro" actual
                Element libro = (Element) libros.item(i);

                // Obtener el nombre del libro
                String nombreLibro = libro.getAttribute("nombre");

                // Imprimir el nombre del libro en el log
                Log.i("INFORMACION", nombreLibro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void parseXML(InputStream is) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("BN")) {
                    String bookName = parser.nextText();
                    books.add(bookName);
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}