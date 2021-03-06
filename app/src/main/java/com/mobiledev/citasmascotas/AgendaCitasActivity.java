package com.mobiledev.citasmascotas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AgendaCitasActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_detalle, tv_totalShow;
    boolean[] selected_detalle;
    Context context;
    EditText input_minimal,
            input_maximal;
    Button btn_minimal,
            btn_maximal,
            cari;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("es", "EC");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY", id);
    Date date_minimal;
    Date date_maximal;
    ProgressDialog progressDialog;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<Cita> list = new ArrayList<>();
    ArrayList<Integer> detalle_list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    String[] detalle_array = {"Vacunación", "Chequeo Interno", "Chequeo Externo", "Cuidado Personal"};
    Servicio[] servicios = {
            new Servicio("Vacunación", 5.0),
            new Servicio("Chequeo Interno", 7.0),
            new Servicio("Chequeo Externo", 6.0),
            new Servicio("Cuidado Personal", 10.0)
    };

    Button btnAgregar;
    Spinner spinner;
    DatabaseReference databaseReference;
    Cita cita;
    String tipo;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_citas);


        cari = findViewById(R.id.cari);
        context = this;
        input_minimal = findViewById(R.id.input_minimal);
        input_maximal = findViewById(R.id.input_maximal);
        btn_minimal = (Button) findViewById(R.id.btn_minimal);
        btn_maximal = findViewById(R.id.btn_maximal);

        tv_detalle = findViewById(R.id.tv_detalle);
        tv_totalShow = findViewById(R.id.tv_totalShow);
        btnAgregar = findViewById(R.id.btnAgregar);

        btnAgregar.setOnClickListener(this);

        selected_detalle = new boolean[detalle_array.length];

        spinner = findViewById(R.id.spinner);

        btn_minimal.setOnClickListener(this);
        btn_maximal.setOnClickListener(this);
        cari.setOnClickListener(this);
        tv_detalle.setOnClickListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tipo = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    private void showLisener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()) {
            Cita cita = item.getValue(Cita.class);
            list.add(cita);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
        progressDialog.dismiss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAgregar:
                add();
                break;
            case R.id.btn_minimal:
                showMinDate();
                break;
            case R.id.btn_maximal:
                showMaxDate();
                break;
            case R.id.cari:
                showCari();
                break;
            case R.id.tv_detalle:
                showDetalle();
                break;
        }
    }

    void showDetalle() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                AgendaCitasActivity.this
        );
        builder.setTitle("Seleccione Detalle");
        builder.setCancelable(false);
        builder.setMultiChoiceItems(detalle_array, selected_detalle, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked) {
                    detalle_list.add(which);
                    Collections.sort(detalle_list);
                } else {
                    try {

                        if (detalle_list.size() <= which) {
                            detalle_list.remove(which - 1);
                        } else {
                            detalle_list.remove(which);
                        }
                    } catch (Exception e) {
                        System.out.println("Este es el error" + e.toString());
                    }
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < detalle_list.size(); j++) {
                    stringBuilder.append(detalle_array[detalle_list.get(j)]);
                    if (j != detalle_list.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                tv_detalle.setText(stringBuilder.toString());
                calculateTotal();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int j = 0; j < selected_detalle.length; j++) {
                    selected_detalle[j] = false;
                    detalle_list.clear();
                    tv_detalle.setText("");
                }
                calculateTotal();
            }
        });
        builder.show();
    }

    void showMaxDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                input_maximal.setText(simpleDateFormat.format(calendar.getTime()));
                date_maximal = calendar.getTime();

                String input1 = input_maximal.getText().toString();
                String input2 = input_minimal.getText().toString();
                if (input1.isEmpty() && input2.isEmpty()) {
                    cari.setEnabled(false);
                } else {
                    cari.setEnabled(true);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void showMinDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                input_minimal.setText(simpleDateFormat.format(calendar.getTime()));
                date_minimal = calendar.getTime();

                String input1 = input_minimal.getText().toString();
                String input2 = input_maximal.getText().toString();
                if (input1.isEmpty() && input2.isEmpty()) {
                    cari.setEnabled(false);
                } else {
                    cari.setEnabled(true);
                }
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    void showCari() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Query query = database.child("citas").orderByChild("fecha").startAt(date_minimal.getTime()).endAt(date_maximal.getTime());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLisener(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void calculateTotal() {
        ArrayList<Servicio> serviciosSelect = getServicios();
        total = 0.0;
        for (Servicio s : serviciosSelect) {
            total += s.getPrecio();
        }
        tv_totalShow.setText("Total a pagar: $" + total);

    }

    ArrayList<Servicio> getServicios() {
        ArrayList<Servicio> servi = new ArrayList<>();
        for (int v : detalle_list) {
            servi.add(servicios[v]);
        }
        return servi;
    }

    void add() {
        System.out.println(total);
        System.out.println(tipo);
    }
}