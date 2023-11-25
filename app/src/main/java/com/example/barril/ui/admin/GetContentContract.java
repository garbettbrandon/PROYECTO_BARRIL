/*package com.example.barril.ui.admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import androidx.activity.result.contract.ActivityResultContract;

public class GetContentContract extends ActivityResultContract<String, Uri> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        // Aquí creas y devuelves el intent necesario, por ejemplo, para seleccionar una imagen
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(input);
        return intent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        // Aquí analizas el resultado y devuelves el resultado deseado, por ejemplo, la URI de la imagen seleccionada
        if (intent == null || resultCode != Activity.RESULT_OK) {
            return null;
        }
        return intent.getData();
    }*/
