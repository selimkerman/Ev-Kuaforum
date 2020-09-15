package com.abaskan.evkuaforum.BarberAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.abaskan.evkuaforum.BarberActivity.BarberCommentListActivity;
import com.abaskan.evkuaforum.BarberActivity.BarberLoginActivity;
import com.abaskan.evkuaforum.BarberActivity.BarberPersonalInfoActivity;
import com.abaskan.evkuaforum.BarberActivity.BarberStoreInfoActivity;
import com.abaskan.evkuaforum.BarberActivity.BarberAppointmentHistoryActivity;
import com.abaskan.evkuaforum.BarberClass.BarberSettingsClass;
import com.abaskan.evkuaforum.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BarberSettingsAdapter extends RecyclerView.Adapter<BarberSettingsAdapter.BarberSettingsHolder>{
    private Context context;
    private List<BarberSettingsClass> barberSettingsClassList;
    FirebaseAuth firebaseAuth;

    public BarberSettingsAdapter(Context context, List<BarberSettingsClass> barberSettingsClassList) {
        this.context = context;
        this.barberSettingsClassList = barberSettingsClassList;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public BarberSettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_barber_settings,parent,false);
        return new BarberSettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberSettingsHolder holder, final int position) {
        BarberSettingsClass barberSettingsClass = barberSettingsClassList.get(position);
        holder.barberSettingsTextview.setText(barberSettingsClass.getSettingName());
        holder.barberSettingsImageview.setImageResource(context.getResources()
                .getIdentifier(barberSettingsClass.getSettingIconName(),"drawable",context.getPackageName()));

        holder.barberSettingsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        Intent toBarberInfo = new Intent(v.getContext(), BarberPersonalInfoActivity.class);
                        toBarberInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toBarberInfo);
                        break;

                    case 1:
                        Intent toStoreInfo = new Intent(v.getContext(), BarberStoreInfoActivity.class);
                        toStoreInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toStoreInfo);
                        break;

                    case 2:
                        Intent toAppHistory = new Intent(v.getContext(), BarberAppointmentHistoryActivity.class);
                        toAppHistory.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toAppHistory);
                        break;

                    case 3:
                        Intent toCommentInfo = new Intent(v.getContext(), BarberCommentListActivity.class);
                        toCommentInfo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toCommentInfo);
                        break;

                    case 5:
                        firebaseAuth.signOut();
                        Intent toBarberLogin = new Intent(v.getContext(), BarberLoginActivity.class);
                        toBarberLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(toBarberLogin);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return barberSettingsClassList.size();
    }

    public class BarberSettingsHolder extends RecyclerView.ViewHolder{
        public ImageView barberSettingsImageview;
        public TextView barberSettingsTextview;
        public CardView barberSettingsCardview;

        public BarberSettingsHolder(@NonNull View itemView) {
            super(itemView);
            barberSettingsImageview = itemView.findViewById(R.id.barberSettingsImageview);
            barberSettingsTextview = itemView.findViewById(R.id.barberSettingsTextview);
            barberSettingsCardview = itemView.findViewById(R.id.barberSettingsCardview);
        }
    }
}
