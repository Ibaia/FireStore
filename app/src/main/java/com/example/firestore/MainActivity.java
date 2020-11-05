package com.example.firestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    //Declarar variables necesarias
    private static final String TAG = "aaa";
    private LinearLayout linearLayoutPrincipal;
    static FirebaseFirestore db;

    private final String StringOverlord = "Overlord";
    private final String StringTateNoiusa = "TateNoiusa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Localizas el linear layout en el que quieres añadir los datos
        linearLayoutPrincipal = findViewById(R.id.linearPrincipal);

        //Crear la conexion con la base de datos
        db = FirebaseFirestore.getInstance();

        //Conectas con la base de datos para poder acceder a los datos
        db.collection("Animus")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                //comprobar uno de los campos del documento
                                if (StringOverlord.equals(document.getString("nombre")) || StringTateNoiusa.equals((document.getString("nombre")))) {

                                    //Cargar datos de la base de datos de los animes seleccionados para rellenar el linearlayout
                                    String id = document.getId();
                                    String nombre = document.getString("nombre");
                                    String valoracion = document.getString("valoracion");

                                    //Pasando los datos necesarios llamas al metodo que crea linearLayout
                                    anadirAnimeUI(nombre, valoracion);
                                } else {
                                    //Pasandole la ID del documento llama a un metodo para borrar el document actual
                                    deleteAnime(document.getId());
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }

                        //Dentro del onComplet de la carga de datos para ejecutarlo despues de borrar lo que no quieras
                        insertAnime();
                    }
                });


    }

    private void deleteAnime(String id) {

        //Recive una ID que hace refencia al documento que quieres borrar y lo borrar de la BBDD

        db.collection("Animus").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }


    private void anadirAnimeUI(String nombre, String valoracion) {

        //Crear un linear layout cada vez que recive datos y la horientacion del mismo
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Crear textview para mostrarlos
        TextView tvNombre = new TextView(this);
        TextView tvValoracion = new TextView(this);

        //Cargar los datos en los textViews
        tvNombre.setText(nombre);
        tvValoracion.setText(valoracion);

        //Añades estilos al textview
        tvNombre.setPadding(50,0,50,15);
        tvValoracion.setPadding(50,0,50,15);

        //Añade los textView al linear layout
        linearLayout.addView(tvNombre);
        linearLayout.addView(tvValoracion);

        // Añades el linear layout que acabas de crear al layout principal
        linearLayoutPrincipal.addView(linearLayout);


    }

    private void insertAnime() {

        //     Insert

        // Creas el mapa que quieres insertar en el documento (Los campos)
        Map<String, Object> Animus = new HashMap<>();
        Animus.put("nombre", "conejoenfadado");
        Animus.put("valoracion", "8");

        // Add a new document with a generated ID
        db.collection("Animus")
                .add(Animus)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}



