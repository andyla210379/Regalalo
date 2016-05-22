package andyla.es.regalalo;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Creado por Andyla
 */
public class RegaloAdapter extends RecyclerView.Adapter<RegaloAdapter.RegaloViewHolder> {
    private List<Regalo> items;

    public static class RegaloViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG ="" ;
        // Campos respectivos de un item
        public ImageView imagen;
        public TextView nombre;
        public TextView visitas;

        public RegaloViewHolder(View v) {
            super(v);
            //
            // Enlace con el layout
           //v.setOnClickListener(this);
            //mTextView = (TextView) view
            imagen = (ImageView) v.findViewById(R.id.imagen);
            nombre = (TextView) v.findViewById(R.id.nombreRegalo);
            visitas = (TextView) v.findViewById(R.id.visitas);
        }
    }

    public RegaloAdapter(List<Regalo> items)
    {
        this.items = items;
    }

    public void changeItems(List<Regalo> itemsNuevos)
    {
        this.items = itemsNuevos;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RegaloViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.tarjeta_regalo, viewGroup, false);
        return new RegaloViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RegaloViewHolder viewHolder, int i) {
        viewHolder.imagen.setImageBitmap(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.visitas.setText(items.get(i).getDetalle());

    }
}
