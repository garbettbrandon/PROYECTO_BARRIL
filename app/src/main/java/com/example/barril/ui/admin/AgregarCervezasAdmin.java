package com.example.barril.ui.admin;

import static com.example.barril.ui.MainActivity.ProviderType.BASIC;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barril.R;
import com.example.barril.ui.MainActivity;
import com.example.barril.ui.auth.LogIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class AgregarCervezasAdmin extends AppCompatActivity {

    private static final String EXITO = "Llenando barril";
    private static final String MENSAJE_AUTORIZADO = "Otra gota más a nuestro barril";
    private static final String SEGUIR = "Seguir";
    private static final String ERROR = "Error";
    private static final String MENSAJE_NO_AUTORIZADO = "No se ha podido subir la cerveza";
    private static final String ACEPTAR = "Aceptar";
    private static final String COMPLETAR_CAMPOS = "Completar todos los camposy añade imágenes";

    EditText idMarcaAdmin, idModeloAdmin, idDescripcionAdmin, idCantidadAdmin, idPrecioAdmin, idGradosAdmin, idTipoAdmin, idValorColorAdmin;
    Button idAgregarLogo, idAgregarBotella, idBotonVer, idBotonVolverAdmin, idBotonAgregarAdmin,idRadar, idMaridaje;
    TextView idTituloCervezaAdmin, idDescripcionCervezaAdmin, idTipoCardAdmin, idTamanioCervezaAdmin, idPrecioCervezaAdmin, idPorcentajeCervezaAdmin;
    LinearLayout colorCabeceraAdmin;
    ImageView idVectorCervezaAdmin, idVectorLogoAdmin;
    String marca, modelo, descripcion, color, cantidad, precio, grados, tipo, urlLogo, urlBotella, urlMaridaje, urlRadar,urlMaridajeComprobado,urlRadarComprobado,urlBotellaComprobada,urlLogoComprobado;

    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String email;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_agregar_cervezas_admin);

        // Inicializar Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Inicializar variables
        idMarcaAdmin = findViewById(R.id.idMarcaAdmin);
        idModeloAdmin = findViewById(R.id.idModeloAdmin);
        idDescripcionAdmin = findViewById(R.id.idDescripcionAdmin);
        idCantidadAdmin = findViewById(R.id.idCantidadAdmin);
        idPrecioAdmin = findViewById(R.id.idPrecioAdmin);
        idGradosAdmin = findViewById(R.id.idGradosAdmin);
        idTipoAdmin = findViewById(R.id.idTipoAdmin);

        idAgregarLogo = findViewById(R.id.idAgregarLogo);
        idAgregarBotella = findViewById(R.id.idAgregarCerveza);
        idRadar = findViewById(R.id.idRadarImagen);
        idMaridaje = findViewById(R.id.idMaridajeImagen);
        idBotonVer = findViewById(R.id.idBotonVer);
        idBotonVolverAdmin = findViewById(R.id.idBotonVolverAdmin);
        idBotonAgregarAdmin = findViewById(R.id.idBotonAgregarAdmin);

        idValorColorAdmin = findViewById(R.id.idValorColorAdmin);
        idTituloCervezaAdmin = findViewById(R.id.idTituloCervezaAdmin);
        idDescripcionCervezaAdmin = findViewById(R.id.idDescripcionCervezaAdmin);
        idTipoCardAdmin = findViewById(R.id.idTipoCardAdmin);
        idTamanioCervezaAdmin = findViewById(R.id.idTamanioCervezaAdmin);
        idPrecioCervezaAdmin = findViewById(R.id.idPrecioCervezaAdmin);
        idPorcentajeCervezaAdmin = findViewById(R.id.idPorcentajeCervezaAdmin);

        idVectorCervezaAdmin = findViewById(R.id.idVectorCervezaAdmin);
        idVectorLogoAdmin = findViewById(R.id.idVectorLogoAdmin);

        colorCabeceraAdmin = findViewById(R.id.idColorCabeceraAdmin);


        // Evento de clic para ver la información ingresada
        idBotonVer.setOnClickListener(view -> {
            marca = idMarcaAdmin.getText().toString();
            modelo = idModeloAdmin.getText().toString();
            descripcion = idDescripcionAdmin.getText().toString();
            color = idValorColorAdmin.getText().toString();
            cantidad = idCantidadAdmin.getText().toString();
            precio = idPrecioAdmin.getText().toString();
            grados = idGradosAdmin.getText().toString();
            tipo = idTipoAdmin.getText().toString();

            idTituloCervezaAdmin.setText(marca);
            idDescripcionCervezaAdmin.setText(descripcion);
            idTamanioCervezaAdmin.setText(cantidad);
            idPrecioCervezaAdmin.setText(precio);
            idPorcentajeCervezaAdmin.setText(grados);
            idTipoCardAdmin.setText(tipo);

            colorCabeceraAdmin.setBackgroundColor(Color.parseColor(color));
        });

        // Evento de clic para agregar la cerveza
        idBotonAgregarAdmin.setOnClickListener(view -> {
            subirCervezaAFirestore();
        });

        // Evento de clic para volver
        idBotonVolverAdmin.setOnClickListener(view -> {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            email = user.getEmail();

            Intent i = new Intent(AgregarCervezasAdmin.this, MainActivity.class);
            i.putExtra("email", email);
            i.putExtra("provider", BASIC);
            startActivity(i);
            finish();
        });
        // Evento de clic para agregar una imagen de cerveza desde la galería
        idAgregarLogo.setOnClickListener(view -> {
            galleryLauncher.launch("image/*");
        });

        // Evento de clic para agregar una imagen de botella desde la galería
        idAgregarBotella.setOnClickListener(view -> {
            galleryLauncherBotellas.launch("image/*");
        });

        idRadar.setOnClickListener(view ->{
            galleryLauncherRadar.launch("image/*");
        });

        idMaridaje.setOnClickListener(view ->{
            galleryLauncherMaridaje.launch("image/*");
        });
    }


    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            subirImagenAFirebaseStorage(result, "logos");
            Picasso.get().load(result).into(idVectorLogoAdmin);
        }
    });

    private final ActivityResultLauncher<String> galleryLauncherBotellas = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            subirImagenAFirebaseStorage(result, "botellas");
            Picasso.get().load(result).into(idVectorCervezaAdmin);
        }
    });
    private final ActivityResultLauncher<String> galleryLauncherMaridaje = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            subirImagenAFirebaseStorage(result, "maridaje");
        }
    });
    private final ActivityResultLauncher<String> galleryLauncherRadar = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        if (result != null) {
            subirImagenAFirebaseStorage(result, "radar");
        }
    });

    private void subirImagenAFirebaseStorage(Uri imageUri, String carpeta) {
        // Crear una referencia al lugar donde se almacenará la imagen en Firebase Storage
        StorageReference imageRef = storageRef.child(carpeta + "/" + System.currentTimeMillis() + ".jpg");

        // Subir la imagen a Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Obtener la URL de descarga de la imagen
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Parsear la URL para obtener el nombre de la imagen
                        Uri parsedUri = Uri.parse(uri.toString());
                        String nombreImagen = parsedUri.getLastPathSegment();

                        // Realizar acciones adicionales según sea necesario
                        if (carpeta.equals("logos")) {
                            urlLogo = nombreImagen;
                        } else if (carpeta.equals("botellas")) {
                            urlBotella = nombreImagen;
                        }else if (carpeta.equals("maridaje")) {
                            urlMaridaje = nombreImagen;
                        }else if (carpeta.equals("radar")) {
                            urlRadar = nombreImagen;
                        }
                    });

                    // Hacer algo adicional después de una subida exitosa si es necesario
                })
                .addOnFailureListener(e -> {
                    // Manejar errores durante la subida de la imagen
                    showAlert();
                });
    }

    private void subirCervezaAFirestore() {
        // Obtener una instancia de Firestore
        db = FirebaseFirestore.getInstance();

        // Obtener los valores de los EditText y TextView
        marca = idMarcaAdmin.getText().toString();
        modelo = idModeloAdmin.getText().toString();
        descripcion = idDescripcionAdmin.getText().toString();
        color = idValorColorAdmin.getText().toString();
        cantidad = idCantidadAdmin.getText().toString();
        precio = idPrecioAdmin.getText().toString();
        grados = idGradosAdmin.getText().toString();
        tipo = idTipoAdmin.getText().toString();
        //aqui compruebo que se ha seleccionado una botella y un logo
        urlBotellaComprobada =urlBotella;
        urlLogoComprobado = urlLogo;
        urlMaridajeComprobado = urlMaridaje;
        urlRadarComprobado = urlRadar;

        // URL de la botella (pueden ser vacías por ahora)
        //String urlBotella = ""; // Puedes cambiar esto con la URL real de la botella

        // Verificar que no haya campos vacíos
        if (marca.isEmpty() || modelo.isEmpty() || descripcion.isEmpty() || color.isEmpty()
                || cantidad.isEmpty() || precio.isEmpty() || grados.isEmpty() || tipo.isEmpty()
                || urlBotellaComprobada==null || urlLogoComprobado==null || urlMaridajeComprobado==null || urlRadarComprobado==null) {
            showCompletar();
            return;
        }

        // Crear un mapa con los datos que deseas subir a Firestore
        Map<String, Object> cerveza = new HashMap<>();
        cerveza.put("marca", marca);
        cerveza.put("modelo", modelo);
        cerveza.put("descripcion", descripcion);
        cerveza.put("color", color);
        cerveza.put("cantidad", cantidad);
        cerveza.put("precio", precio);
        cerveza.put("grados", grados);
        cerveza.put("tipo", tipo);
        cerveza.put("urlBotella", urlBotellaComprobada);
        cerveza.put("urlLogo", urlLogoComprobado);
        cerveza.put("urlRadar", urlRadarComprobado);
        cerveza.put("urlMaridaje", urlMaridajeComprobado);

        // Subir el mapa a Firestore
        db.collection("cervezas").add(cerveza)
                .addOnSuccessListener(documentReference -> {
                    showExito();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> {
                    showAlert();
                });

    }

    private void showExito() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(EXITO);
        builder.setMessage(MENSAJE_AUTORIZADO);
        builder.setPositiveButton(SEGUIR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(MENSAJE_NO_AUTORIZADO);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showCompletar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(ERROR);
        builder.setMessage(COMPLETAR_CAMPOS);
        builder.setPositiveButton(ACEPTAR, null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void limpiarCampos() {
        idMarcaAdmin.setText("");
        idModeloAdmin.setText("");
        idDescripcionAdmin.setText("");
        idCantidadAdmin.setText("");
        idPrecioAdmin.setText("");
        idGradosAdmin.setText("");
        idTipoAdmin.setText("");
        idValorColorAdmin.setText("");
    }
}
