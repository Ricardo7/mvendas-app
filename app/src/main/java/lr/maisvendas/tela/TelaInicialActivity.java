package lr.maisvendas.tela;

import android.os.Bundle;
import android.view.Menu;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Meta;
import lr.maisvendas.repositorio.sql.MetaDAO;

public class TelaInicialActivity extends BaseActivity {

    public static final String PARAM_USUARIO = "PARAM_USUARIO";
    private static final String TAG = "TelaInicialActivity";

    //private LineChart grafico;
    private BarChart grafico;

    float itensGrafico[] = {1.2f, 3.5f, 7.56f, 2.25f};

    String descricao[] = {"Item 1", "Item 2", "Item 3", "Item 4"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tela_inicial);
        setDrawerLayoutId(R.id.activity_tela_inicial_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Mais Vendas");

        //grafico = (LineChart) findViewById(R.id.activity_tela_inicial_linechart);
        grafico = (HorizontalBarChart) findViewById(R.id.activity_tela_inicial_horizontalbarchart);


        //PopulaDadosTeste populaDadosTeste = new PopulaDadosTeste();

        //List<Meta> metas = populaDadosTeste.populaMeta(this);
        List<Meta> metas = null;

        MetaDAO metaDAO = MetaDAO.getInstance(this);
        metas = metaDAO.buscaMetas();

        /*
        List<Entry> entradasGrafico = new ArrayList<>();

        for(int i = 0;i < itensGrafico.length; i++){
            entradasGrafico.add(new PieEntry(itensGrafico[i],descricao[i]));
        }

        LineDataSet dataSet = new LineDataSet(entradasGrafico,"Legenda Gráfico");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        LineData lineData = new LineData(dataSet);

        grafico.setData(lineData);

        grafico.invalidate();
        */
        /*
        List<Entry> sinEntries = new ArrayList<>(); // List to store data-points of sine curve
        List<Entry> cosEntries = new ArrayList<>(); // List to store data-points of cosine curve

        // Obtaining data points by using Math.sin and Math.cos functions
        Ferramentas ferramentas = new Ferramentas();
        float j=0;
        for (float i = 0; i <= 3000f; i += 500f) {
            j=i;
            if (i == 1500f){

                i = i - 800f;
                ferramentas.customLog(TAG,"Entrou: "+i);
            }
            sinEntries.add(new Entry(100,  150));
            cosEntries.add(new Entry(100, 150));
        }


        --
        for (Meta meta: metas) {
            ferramentas.customLog(TAG,meta.getRealizado().floatValue()+"");
            ferramentas.customLog(TAG,meta.getRealizado().floatValue()+"");

            sinEntries.add(new Entry(meta.getRealizado().floatValue(), meta.getRealizado().floatValue()));
            cosEntries.add(new Entry(meta.getRealizado().floatValue(), meta.getRealizado().floatValue()));

            ferramentas.customLog(TAG,"Fim");
        }
        --

        List<ILineDataSet> dataSets = new ArrayList<>(); // for adding multiple plots

        LineDataSet sinSet = new LineDataSet(sinEntries, "sin curve");
        LineDataSet cosSet = new LineDataSet(cosEntries, "cos curve");

        // Adding colors to different plots
        cosSet.setColor(Color.GREEN);
        cosSet.setCircleColor(Color.GREEN);
        sinSet.setColor(Color.BLUE);
        sinSet.setCircleColor(Color.BLUE);

        // Adding each plot data to a List
        dataSets.add(sinSet);
        dataSets.add(cosSet);

        // Setting datapoints and invalidating chart to update with data points
        grafico.setData(new LineData(dataSets));
        grafico.invalidate();
        */

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuSearch().setVisible(false);
        return true;
    }
}
