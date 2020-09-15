package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarberAddWorkingHours extends AppCompatActivity {
    Toolbar barberAddWorkingHoursToolbar;
    private List<String> selectedHours;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_add_working_hours);
        toolbar();
        selectedHours = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void toolbar() {
        barberAddWorkingHoursToolbar = findViewById(R.id.barberAddWorkingHoursToolbar);
        barberAddWorkingHoursToolbar.setTitle("Çalışma Saatlerini Düzenle");
        barberAddWorkingHoursToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        setSupportActionBar(barberAddWorkingHoursToolbar);
        barberAddWorkingHoursToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addHourButtonClick (View view){
        Toast.makeText(BarberAddWorkingHours.this, "Kaydedildi", Toast.LENGTH_SHORT).show();
    }



    public void addHourMondayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Pazartesi");
                hours.put("hours", selection);
                hours.put("sequence",1);


                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Monday").set(hours)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }







    public void addHourTuesdayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Salı");
                hours.put("hours", selection);
                hours.put("sequence",2);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Tuesday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }






    public void addHourWednesdayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Çarşamba");
                hours.put("hours", selection);
                hours.put("sequence",3);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Wednesday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }





    public void addHourThursdayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Perşembe");
                hours.put("hours", selection);
                hours.put("sequence",4);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Thursday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }






    public void addHourFridayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Cuma");
                hours.put("hours", selection);
                hours.put("sequence",5);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Friday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }






    public void addHourSaturdayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Cumartesi");
                hours.put("hours", selection);
                hours.put("sequence",6);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Saturday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }





    public void addHourSundayClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(BarberAddWorkingHours.this);
        builder.setTitle("Çalışma Saatlerini Seçiniz");
        selectedHours.clear();
        builder.setMultiChoiceItems(R.array.workingHours, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                String[] hours = getResources().getStringArray(R.array.workingHours);
                if (isChecked) {
                    selectedHours.add(hours[which]);
                }

                else selectedHours.remove(hours[which]);
            }
        });

        builder.setCancelable(false);

        builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selection = "";
                for (int i = 0; i< selectedHours.size();i++) {
                    if( i == selectedHours.size() -1){
                        selection= selection + selectedHours.get(i);
                    } else {
                        selection = selection + selectedHours.get(i) + " ";
                    }
                }

                Map<String, Object> hours = new HashMap<>();
                hours.put("day","Pazar");
                hours.put("hours", selection);
                hours.put("sequence",7);

                firebaseFirestore.collection("BarberFields").document(firebaseAuth.getUid())
                        .collection("WorkingHours").document("Sunday").set(hours);

                Toast.makeText(BarberAddWorkingHours.this, "Saatler seçildi : " + selection, Toast.LENGTH_LONG).show();
                selectedHours.clear();

            }
        });

        builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Temizle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedHours.clear();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
