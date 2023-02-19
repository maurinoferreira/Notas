package com.brasil.app.notas.modelo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.brasil.app.notas.R;
import com.brasil.app.notas.apresentacao.AdicionarNotas;
import com.brasil.app.notas.apresentacao.AlterarNotas;
import com.brasil.app.notas.apresentacao.MainActivity;
import com.brasil.app.notas.dal.NotaDAO;
import com.brasil.app.notas.dal.NotasDB;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.ViewHolder> {


    private List<Nota> notaList;
    private Context context;
    private Nota nota;

    private Activity activity;
    ArrayList<Nota> arrayList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titulo;
        private final TextView mensagem;
        private final TextView data;
        private final ImageView excluir;
        private final CardView cardItem;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.txt_titulo);
            mensagem = itemView.findViewById(R.id.txt_nota);
            data = itemView.findViewById(R.id.txt_data);
            excluir = itemView.findViewById(R.id.btn_excluir);
            cardItem = itemView.findViewById(R.id.layout_nota);
        }

        public CardView getCardItem() {
            return cardItem;
        }

        public TextView getTitulo() {
            return titulo;
        }

        public TextView getMensagem() {
            return mensagem;
        }

        public TextView getData() {
            return data;
        }

        public ImageView getExcluir() {
            return excluir;
        }

    }

    public NotasAdapter(Context context) {
        this.context = context;
    }

    public NotasAdapter(List<Nota> notaList, Context context, Activity activity) {
        this.notaList = notaList;
        this.context = context;
        this.activity = activity;
        this.arrayList.addAll(notaList);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);



        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        nota = notaList.get(position);

        holder.getTitulo().setText(nota.getTitulo());
        holder.getMensagem().setText(nota.getMensagem());
        holder.getData().setText(nota.getData());



        holder.getCardItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = holder.getAdapterPosition();
                nota = notaList.get(pos);

                Intent intent = new Intent(context, AlterarNotas.class);
                intent.putExtra("id", nota.getId().toString());
                intent.putExtra("titulo", nota.getTitulo());
                intent.putExtra("nota", nota.getMensagem());
                intent.putExtra("data", nota.getData());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                notifyDataSetChanged();

            }
        });

        holder.getExcluir().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Tem certeza que deseja excluir a nota ?")
                        .setTitle("Excluir Nota")
                        .setCancelable(false)
                        .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                int pos = holder.getAdapterPosition();
                                nota = notaList.get(pos);

                                notaList.remove(nota);

                                NotaDAO notaDAO = new NotaDAO(context);
                                notaDAO.deletarNota(nota);

                                if (notaDAO.getMensagem().equals(""))
                                    Toast.makeText(context, "Erro ao apagar a nota!!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(context, notaDAO.getMensagem(), Toast.LENGTH_SHORT).show();

                                if (notaList.size() == 0) {
                                    Intent intent = context.getPackageManager()
                                            .getLaunchIntentForPackage(context.getPackageName());

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }

                                notifyItemRemoved(position);

                            }
                        })
                        .setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert);
                AlertDialog alert = builder.create();
                alert.show();


            }
        });




    }

    @Override
    public int getItemCount() {
        return  notaList != null ? notaList.size() : 0;
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }



    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        notaList.clear();
        if (charText.length()==0){
            notaList.addAll(arrayList);
        }
        else {
            for (Nota model : arrayList){
                if (model.getTitulo().toLowerCase(Locale.getDefault())
                         .contains(charText) ||
                    model.getMensagem().toLowerCase(Locale.getDefault())
                            .contains(charText)) {

                    notaList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }


}
