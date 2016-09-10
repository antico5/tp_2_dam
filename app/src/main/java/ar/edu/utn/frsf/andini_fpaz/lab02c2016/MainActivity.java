package ar.edu.utn.frsf.andini_fpaz.lab02c2016;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    DecimalFormat f = new DecimalFormat("##.00");

    ElementoMenu[] listaBebidas;
    ElementoMenu[] listaPlatos;
    ElementoMenu[] listaPostre;
    ElementoMenu elementoActual;
    Vector<ElementoMenu> pedidoActual;

    ArrayList<ElementoMenu> listaElementos;
    ArrayAdapter<ElementoMenu> adaptador;

    ListView listaComidas;
    ToggleButton toggleDelivery;
    Spinner spinnerHorario;
    Switch switchNotificar;
    Button botonAgregar;
    Button botonConfirmar;
    Button botonReiniciar;
    RadioButton radioPlato;
    RadioButton radioBebida;
    RadioButton radioPostre;
    TextView textPedido;
    RadioGroup groupRadios;

    boolean confirmado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniciarListas();
        setearWidgets();
        bindearRadios();
        bindearAgregar();
        bindearReiniciar();
        reiniciar();

    }

    private void reiniciar(){
        elementoActual = null;
        confirmado = false;
        pedidoActual = new Vector<ElementoMenu>();
        actualizarDetallePedido();
    }

    private void actualizarDetallePedido(){
        String detalle = "";
        double total = 0;
        for(ElementoMenu elemento : pedidoActual){
            detalle += elemento.toString() + '\n';
            total += elemento.getPrecio();
        }
        detalle += "Total: $" + String.valueOf(total);
        textPedido.setText(detalle);
    }

    private void bindearRadios(){
        groupRadios.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton seleccionado = (RadioButton)group.findViewById(checkedId);
                if (!seleccionado.isChecked())
                    return;
                listaElementos.clear();
                switch (checkedId){
                    case R.id.plato:
                        listaElementos.addAll(Arrays.asList(listaPlatos));
                        break;
                    case R.id.bebida:
                        listaElementos.addAll(Arrays.asList(listaBebidas));
                        break;
                    case R.id.postre:
                        listaElementos.addAll(Arrays.asList(listaPostre));
                        break;
                }
                adaptador.notifyDataSetChanged();
            }
        });
    }

    private void bindearAgregar(){
        botonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmado){
                    Toast toast = Toast.makeText(getApplicationContext(), "El pedido ya ha sido confirmado.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else if(elementoActual == null){
                    Toast toast = Toast.makeText(getApplicationContext(), "Debe seleccionar algo del menu.", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    pedidoActual.add(elementoActual);
                    actualizarDetallePedido();
                }
            }
        });
    }

    private void bindearReiniciar(){
        botonReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reiniciar();
            }
        });
    }

    private void setearWidgets() {
        listaComidas = (ListView)findViewById(R.id.comidas);
        toggleDelivery = (ToggleButton)findViewById(R.id.delivery);
        spinnerHorario = (Spinner)findViewById(R.id.horario);
        switchNotificar = (Switch)findViewById(R.id.notificar);
        botonAgregar = (Button)findViewById(R.id.agregar);
        botonConfirmar = (Button)findViewById(R.id.confirmar);
        botonReiniciar = (Button)findViewById(R.id.reiniciar);
        radioPlato = (RadioButton)findViewById(R.id.plato);
        radioBebida = (RadioButton)findViewById(R.id.bebida);
        radioPostre = (RadioButton)findViewById(R.id.postre);
        textPedido = (TextView)findViewById(R.id.pedido);
        groupRadios = (RadioGroup)findViewById(R.id.group);

        textPedido.setMovementMethod(new ScrollingMovementMethod());

        listaElementos = new ArrayList<ElementoMenu>();
        adaptador = new ArrayAdapter<ElementoMenu>(this,R.layout.support_simple_spinner_dropdown_item,listaElementos);

        listaComidas.setAdapter(adaptador);

        listaComidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                elementoActual= (ElementoMenu)listaComidas.getItemAtPosition(position);
            }
        });
    }

    class ElementoMenu {
        private Integer id;
        private String nombre;
        private Double precio;

        public ElementoMenu() {
        }

        public ElementoMenu(Integer i, String n, Double p) {
            this.setId(i);
            this.setNombre(n);
            this.setPrecio(p);
        }

        public ElementoMenu(Integer i, String n) {
            this(i,n,0.0);
            Random r = new Random();
            this.precio= (r.nextInt(3)+1)*((r.nextDouble()*100));
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public Double getPrecio() {
            return precio;
        }

        public void setPrecio(Double precio) {
            this.precio = precio;
        }

        @Override
        public String toString() {
            return this.nombre+ "( "+f.format(this.precio)+")";
        }
    }

    private void iniciarListas(){
        // inicia lista de bebidas
        listaBebidas = new ElementoMenu[7];
        listaBebidas[0]=new ElementoMenu(1,"Coca");
        listaBebidas[1]=new ElementoMenu(4,"Jugo");
        listaBebidas[2]=new ElementoMenu(6,"Agua");
        listaBebidas[3]=new ElementoMenu(8,"Soda");
        listaBebidas[4]=new ElementoMenu(9,"Fernet");
        listaBebidas[5]=new ElementoMenu(10,"Vino");
        listaBebidas[6]=new ElementoMenu(11,"Cerveza");
        // inicia lista de platos
        listaPlatos= new ElementoMenu[14];
        listaPlatos[0]=new ElementoMenu(1,"Ravioles");
        listaPlatos[1]=new ElementoMenu(2,"Gnocchi");
        listaPlatos[2]=new ElementoMenu(3,"Tallarines");
        listaPlatos[3]=new ElementoMenu(4,"Lomo");
        listaPlatos[4]=new ElementoMenu(5,"Entrecot");
        listaPlatos[5]=new ElementoMenu(6,"Pollo");
        listaPlatos[6]=new ElementoMenu(7,"Pechuga");
        listaPlatos[7]=new ElementoMenu(8,"Pizza");
        listaPlatos[8]=new ElementoMenu(9,"Empanadas");
        listaPlatos[9]=new ElementoMenu(10,"Milanesas");
        listaPlatos[10]=new ElementoMenu(11,"Picada 1");
        listaPlatos[11]=new ElementoMenu(12,"Picada 2");
        listaPlatos[12]=new ElementoMenu(13,"Hamburguesa");
        listaPlatos[13]=new ElementoMenu(14,"Calamares");
        // inicia lista de postres
        listaPostre= new ElementoMenu[15];
        listaPostre[0]=new ElementoMenu(1,"Helado");
        listaPostre[1]=new ElementoMenu(2,"Ensalada de Frutas");
        listaPostre[2]=new ElementoMenu(3,"Macedonia");
        listaPostre[3]=new ElementoMenu(4,"Brownie");
        listaPostre[4]=new ElementoMenu(5,"Cheescake");
        listaPostre[5]=new ElementoMenu(6,"Tiramisu");
        listaPostre[6]=new ElementoMenu(7,"Mousse");
        listaPostre[7]=new ElementoMenu(8,"Fondue");
        listaPostre[8]=new ElementoMenu(9,"Profiterol");
        listaPostre[9]=new ElementoMenu(10,"Selva Negra");
        listaPostre[10]=new ElementoMenu(11,"Lemon Pie");
        listaPostre[11]=new ElementoMenu(12,"KitKat");
        listaPostre[12]=new ElementoMenu(13,"IceCreamSandwich");
        listaPostre[13]=new ElementoMenu(14,"Frozen Yougurth");
        listaPostre[14]=new ElementoMenu(15,"Queso y Batata");

    }

}