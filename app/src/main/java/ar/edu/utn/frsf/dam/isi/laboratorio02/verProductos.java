package ar.edu.utn.frsf.dam.isi.laboratorio02;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ar.edu.utn.frsf.dam.isi.laboratorio02.dao.ProductoRepository;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Categoria;
import ar.edu.utn.frsf.dam.isi.laboratorio02.modelo.Producto;

public class verProductos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verproductos);

        final Spinner spinnerCategorias = findViewById(R.id.cmbProductosCategoria);
        final ListView listaProductos = findViewById(R.id.lstProductos);

        Button btnProdAddPedido = findViewById(R.id.btnProdAddPedido);
        final EditText edtProdCant = findViewById(R.id.edtProdCantidad);

        final ProductoRepository prodRep = new ProductoRepository();

        List<Categoria> listaCategorias=prodRep.getCategorias();

        ArrayAdapter<String> adaptadorCategorias = new ArrayAdapter<String>(verProductos.this,
                                    android.R.layout.simple_spinner_item, (ArrayList)listaCategorias);

        spinnerCategorias.setAdapter(adaptadorCategorias);
        spinnerCategorias.setSelection(0);

        List<Producto> productos=prodRep.buscarPorCategoria((Categoria)spinnerCategorias.getSelectedItem());

        final ArrayAdapter<String> adaptadorProductos = new ArrayAdapter<String>(verProductos.this,
                android.R.layout.simple_list_item_single_choice, (ArrayList)productos);

        listaProductos.setAdapter(adaptadorProductos);
        listaProductos.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        final Intent intentExtras = getIntent();

        if (intentExtras.getExtras().getInt("NUEVO_PEDIDO")==0) {
            btnProdAddPedido.setEnabled(false);
            edtProdCant.setEnabled(false);
        }
        else {
            btnProdAddPedido.setEnabled(true);
            edtProdCant.setEnabled(true);
        }

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView spinner, View view, int position, long id) {
                listaProductos.clearChoices();
                List<Producto> productos=prodRep.buscarPorCategoria((Categoria)spinner.getSelectedItem());
                adaptadorProductos.clear();
                for(Producto p: productos)
                    adaptadorProductos.add(p.toString());
                adaptadorProductos.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnProdAddPedido.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent salida = new Intent();
                salida.putExtra("cantidad", edtProdCant.getText());
                salida.putExtra("idProducto",listaProductos.getSelectedItemId());
                setResult(Activity.RESULT_OK, salida);
                finish();
            }

        });
    }
}
